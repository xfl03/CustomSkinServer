package customskinserver.profile;

import java.util.HashMap;

import customskinserver.CustomSkinServer;
import customskinserver.handler.RemoveHandler.RemoveRequest;
import customskinserver.handler.UpdateHandler.UpdateRequest;

public class ProfileManager {
	public HashMap<String,Profile> profiles=new HashMap<String, Profile>();
	public void putProfile(String username,Profile profile){
		profiles.put(username.toLowerCase(), profile);
	}
	public void putProfileAndBroadcast(String username,Profile profile){
		putProfile(username,profile);
		CustomSkinServer.sendToAll(CustomSkinServer.GSON.toJson(new UpdateRequest(profile)));
	}
	
	public void removeProfile(String username){
		profiles.remove(username.toLowerCase());
	}
	public void removeProfileAndBroadcast(String username){
		removeProfile(username);
		CustomSkinServer.sendToAll(CustomSkinServer.GSON.toJson(new RemoveRequest(username)));
	}
	
	public Profile getProfile(String username){
		return profiles.get(username.toLowerCase());
	}
}
