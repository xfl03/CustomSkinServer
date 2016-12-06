package customskinserver.handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import customskinserver.CustomSkinServer;
import customskinserver.CustomSkinServer.BasicPlayer;
import customskinserver.handler.Handler.BasicHandler;

public class TextureHandler implements BasicHandler {
	public static final int TRUNK_LENGTH=30*1024;//30KB

	@Override
	public void handleRequest(BasicPlayer player, String message) {
		TextureRequest request=CustomSkinServer.GSON.fromJson(message, TextureRequest.class);
		if(!CustomSkinServer.textureExists(request.hash)){
			player.sendPluginMessage(CustomSkinServer.GSON.toJson(new TextureResponce(request.hash)));
			return;
		}
		File textureFile=CustomSkinServer.getTextureFile(request.hash);
		try {
			byte[] bytes=FileUtils.readFileToByteArray(textureFile);
			if(bytes.length<=TRUNK_LENGTH){
				player.sendPluginMessage(CustomSkinServer.GSON.toJson(new TextureResponce(request.hash,1,1,Base64.encodeBase64String(bytes))));
				return;
			}
			int total=(int) Math.ceil(bytes.length/TRUNK_LENGTH);
			for(int i=1;i<=total;i++){
				byte[] splitBytes=ArrayUtils.subarray(bytes, (i-1)*TRUNK_LENGTH, Math.min(TRUNK_LENGTH*i, bytes.length));
				player.sendPluginMessage(CustomSkinServer.GSON.toJson(new TextureResponce(request.hash,i,total,Base64.encodeBase64String(splitBytes))));
			}
		} catch (IOException e) {
			e.printStackTrace();
			player.sendPluginMessage(CustomSkinServer.GSON.toJson(new TextureResponce(request.hash)));
		}
	}
	public HashMap<String,ArrayList<String>> trunks=new HashMap<String, ArrayList<String>>();
	@Override
	public void handleResponce(BasicPlayer player, String message){
		TextureResponce responce=CustomSkinServer.GSON.fromJson(message, TextureResponce.class);
		if(responce.trunkTot==0){
			callFailed(responce.hash);
			return;
		}
		if(CustomSkinServer.textureExists(responce.hash)){
			callSuccess(responce.hash);
			return;
		}
		File textureFile=CustomSkinServer.getTextureFile(responce.hash);
		try{
			if(responce.trunkTot==1){
				FileUtils.writeByteArrayToFile(textureFile, Base64.decodeBase64(responce.content));
				callSuccess(responce.hash);
				return;
			}
		}catch(Exception e){
			e.printStackTrace();
			callFailed(responce.hash);
			return;
		}
		if(TRUNK_LENGTH*(responce.trunkTot-1)>=CustomSkinServer.config.textureMaxSize*1024){
			callFailed(responce.hash);
			return;
		}
		if(!trunks.containsKey(responce.hash))
			trunks.put(responce.hash, new ArrayList<String>());
		ArrayList<String> currentTrunks=trunks.get(responce.hash);
		currentTrunks.set(responce.trunkNum, responce.content);
		if(responce.trunkNum!=responce.trunkTot)
			return;
		byte[] bytes=new byte[0];
		for(int i=1;i<=responce.trunkTot;i++){
			ArrayUtils.addAll(bytes, Base64.decodeBase64(currentTrunks.get(i)));
		}
		if(bytes.length>TRUNK_LENGTH){
			callFailed(responce.hash);
			return;
		}
		try{
			FileUtils.writeByteArrayToFile(textureFile,bytes);
			callSuccess(responce.hash);
		}catch(Exception e){
			e.printStackTrace();
			callFailed(responce.hash);
		}
	}
	private static void callSuccess(String hash){
		if(callbacks.containsKey(hash))
			callbacks.get(hash).onTextureLoaded(hash);
	}
	private static void callFailed(String hash){
		if(callbacks.containsKey(hash))
			callbacks.get(hash).onTextureUnloaded(hash);
	}
	
	public interface TextureLoadedCallback{
		public void onTextureLoaded(String hash);
		public void onTextureUnloaded(String hash);
	}
	public static HashMap<String,TextureLoadedCallback> callbacks=new HashMap<String, TextureLoadedCallback>();
	
	public static class TextureRequest{
		public String action="TEXTURE";
		public String type="REQUEST";
		public String hash;
		
		public TextureRequest(String hash){
			this.hash=hash;
		}
	}
	public static class TextureResponce{
		public String action="TEXTURE";
		public String type="RESPONCE";
		public String hash;
		public int trunkNum;
		public int trunkTot=0;//0-Failed
		public String content;//Base64
		
		public TextureResponce(String hash){//For failed
			this.hash=hash;
		}
		public TextureResponce(String hash,int num,int tot,String content){
			this.hash=hash;
			this.trunkNum=num;
			this.trunkTot=num;
			this.content=content;
		}
	}
}
