package customskinserver.handler;

import java.util.ArrayList;
import java.util.Map;

import customskinserver.CustomSkinServer;
import customskinserver.CustomSkinServer.BasicPlayer;
import customskinserver.handler.Handler.BasicHandler;
import customskinserver.profile.Profile;
import customskinserver.profile.ProfileTextureLoader;

public class LoginHandler implements BasicHandler {

	@Override
	public void handleRequest(BasicPlayer player, String message) {
		LoginRequest request=CustomSkinServer.GSON.fromJson(message, LoginRequest.class);
		ProfileTextureLoader.update(player, request.profile);
		LoginResponce responce=new LoginResponce();
		player.sendPluginMessage(CustomSkinServer.GSON.toJson(responce));
	}

	@Override
	public void handleResponce(BasicPlayer player, String message) {
		//No responce
	}

	public static class LoginRequest{
		public Profile profile;
		public ClientInfo clientInfo;
	}
	public static class ClientInfo{
		public String version;//Client Mod Version
	}
	public static class LoginResponce{
		public String action="LOGIN";
		public String type="RESPONCE";
		public ArrayList<Profile> profiles=new ArrayList<Profile>();
		public ServerInfo serverInfo=new ServerInfo();
		public LoginResponce(){
			for(Map.Entry<String,Profile> entry:CustomSkinServer.profiles.entrySet()){
				profiles.add(entry.getValue());
			}
		}
	}
	public static class ServerInfo{
		public String version="CustomSkinServer "+CustomSkinServer.VERSION;
		public int textureMaxSize=CustomSkinServer.config.textureMaxSize;
		public int updateTimeLimit=CustomSkinServer.config.updateTimeLimit;
	}
}
