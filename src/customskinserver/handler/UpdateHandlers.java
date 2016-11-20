package customskinserver.handler;

import customskinserver.CustomSkinServer.BasicPlayer;
import customskinserver.CustomSkinServer;
import customskinserver.handler.Handler.BasicHandler;
import customskinserver.profile.Profile;
import customskinserver.profile.ProfileTextureLoader;

public class UpdateHandlers implements BasicHandler {

	@Override
	public void handleRequest(BasicPlayer player, String message) {
		UpdateRequest request=CustomSkinServer.GSON.fromJson(message, UpdateRequest.class);
		ProfileTextureLoader.update(player, request.profile);
	}

	@Override
	public void handleResponce(BasicPlayer player, String message) {
		//No responce
	}
	public static class UpdateRequest{
		public Profile profile;
		public UpdateRequest(Profile profile){
			this.profile=profile;
		}
	}
}
