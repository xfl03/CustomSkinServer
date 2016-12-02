package customskinserver.handler;

import customskinserver.CustomSkinServer;
import customskinserver.CustomSkinServer.BasicPlayer;
import customskinserver.handler.Handler.BasicHandler;
import customskinserver.profile.Profile;
import customskinserver.profile.ProfileTextureLoader;

public class LoginHandler implements BasicHandler {

	@Override
	public void handleRequest(BasicPlayer player, String message) {
		LoginRequest request=CustomSkinServer.GSON.fromJson(message, LoginRequest.class);
		ProfileTextureLoader.load(player, request.profile);
		LoginResponce responce=new LoginResponce();
		player.sendPluginMessage(CustomSkinServer.GSON.toJson(responce));
	}

	@Override
	public void handleResponce(BasicPlayer player, String message) {
		//No responce
	}

	public static class LoginRequest{
		public Profile profile;
		public String version;//CustomSkinLoader Version
	}
	public static class LoginResponce{
		public String action="LOGIN";
		public String type="RESPONCE";
		public String version=CustomSkinServer.VERSION;
		public int maxSize=CustomSkinServer.config.maxSize;
	}
}
