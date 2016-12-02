package customskinserver.handler;

import customskinserver.CustomSkinServer.BasicPlayer;
import customskinserver.CustomSkinServer;
import customskinserver.handler.Handler.BasicHandler;
import customskinserver.profile.Profile;

public class ProfileHandler implements BasicHandler {

	@Override
	public void handleRequest(BasicPlayer player, String message) {
		ProfileRequest request=CustomSkinServer.GSON.fromJson(message, ProfileRequest.class);
		ProfileResponce responce=new ProfileResponce(CustomSkinServer.getProfile(request.username));
		player.sendPluginMessage(CustomSkinServer.GSON.toJson(responce));
	}

	@Override
	public void handleResponce(BasicPlayer player, String message) {
		//No responce
	}
	public static class ProfileRequest{
		String username;
	}
	public static class ProfileResponce{
		public String action="PROFILE";
		public String type="RESPONCE";
		public Profile profile=null;
		public ProfileResponce(Profile profile){
			this.profile=profile;
		}
	}
}
