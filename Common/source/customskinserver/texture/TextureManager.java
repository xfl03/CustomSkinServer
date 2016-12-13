package customskinserver.texture;

import java.io.File;

import customskinserver.CustomSkinServer;
import customskinserver.CustomSkinServer.BasicPlayer;
import customskinserver.handler.TextureHandler;
import customskinserver.handler.TextureHandler.TextureLoadedCallback;
import customskinserver.handler.TextureHandler.TextureRequest;

public class TextureManager {
	public static final File TEXTURE_DIR=new File(CustomSkinServer.DATA_DIR,"textures");
	
	public TextureManager(){
		if(!TEXTURE_DIR.isDirectory())
			TEXTURE_DIR.mkdirs();
	}
	
	public File getTextureFile(String hash){
		return new File(TEXTURE_DIR,hash);
	}
	public boolean textureExists(String hash){
		return getTextureFile(hash).isFile();
	}
	public void loadTextureAsync(BasicPlayer player,String hash,TextureLoadedCallback callback){
		if(callback!=null)
			TextureHandler.callbacks.put(hash, callback);
		player.sendPluginMessage(CustomSkinServer.GSON.toJson(new TextureRequest(hash)));
	}
}
