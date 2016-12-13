package customskinserver.handler;

import customskinserver.CustomSkinServer.BasicPlayer;

import java.util.HashMap;

import customskinserver.CustomSkinServer;
import customskinserver.handler.Handler.BasicHandler;
import customskinserver.handler.Handler.RequestHandler;
import customskinserver.profile.Profile;
import customskinserver.texture.ProfileTextureLoader;

public class UpdateHandler implements BasicHandler,RequestHandler {
	public HashMap<String,Long> lastUpdate=new HashMap<String,Long>();

	@Override
	public void handleRequest(BasicPlayer player, String message) {
		Long lastUpdate=this.lastUpdate.get(player.getUsername().toLowerCase());
		if(lastUpdate!=null&&(lastUpdate+CustomSkinServer.config.updateTimeLimit)>(System.currentTimeMillis()/1000))
			return;
		this.lastUpdate.put(player.getUsername().toLowerCase(),System.currentTimeMillis()/1000);
		UpdateRequest request=CustomSkinServer.GSON.fromJson(message, UpdateRequest.class);
		ProfileTextureLoader.update(player, request.profile);
	}
	
	public static class UpdateRequest{
		public String action="UPDATE";
		public String type="REQUEST";
		public Profile profile;
		public UpdateRequest(Profile profile){
			this.profile=profile;
		}
	}
}
