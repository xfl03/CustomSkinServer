package customskinserver.profile;

import org.apache.commons.lang3.StringUtils;

import customskinserver.CustomSkinServer;
import customskinserver.CustomSkinServer.BasicPlayer;
import customskinserver.handler.TextureHandler.TextureLoadedCallback;
import customskinserver.handler.UpdateHandlers.UpdateRequest;

public class ProfileTextureLoader implements TextureLoadedCallback {
	public static void load(BasicPlayer player,Profile profile){
		load(player,profile,false);
	}
	public static void update(BasicPlayer player,Profile profile){
		load(player,profile,true);
	}
	private static void load(BasicPlayer player,Profile profile,boolean isUpdate){
		String username=player.getUsername();
		if(profile==null){
			profile=new Profile(username);
			CustomSkinServer.putProfile(username, profile);
			if(isUpdate)
				CustomSkinServer.sendToAll(CustomSkinServer.GSON.toJson(new UpdateRequest(profile)));
			return;
		}
		profile.username=username;
		ProfileTextureLoader callback=new ProfileTextureLoader(profile,isUpdate);
		if(StringUtils.isEmpty(profile.skin)||CustomSkinServer.textureExists(profile.skin))
			callback.skinLoaded=true;
		else
			CustomSkinServer.loadTextureAsync(player, profile.skin, callback);
		if(StringUtils.isEmpty(profile.cape)||CustomSkinServer.textureExists(profile.cape))
			callback.capeLoaded=true;
		else
			CustomSkinServer.loadTextureAsync(player, profile.cape, callback);
			
	}
	
	public Profile profile;
	public boolean isUpdate;
	public boolean skinLoaded;
	public boolean capeLoaded;
	public ProfileTextureLoader(Profile profile,boolean isUpdate){
		this.profile=profile;
		this.isUpdate=isUpdate;
	}

	@Override
	public void onTextureLoaded(String hash) {
		if(hash.equals(profile.skin))
			skinLoaded=true;
		else
			capeLoaded=true;
		if(skinLoaded && capeLoaded){
			CustomSkinServer.putProfile(profile.username, profile);
			if(isUpdate)
				CustomSkinServer.sendToAll(CustomSkinServer.GSON.toJson(new UpdateRequest(profile)));
		}
	}

	@Override
	public void onTextureUnloaded(String hash) {
		if(hash.equals(profile.skin)){
			profile.skin=null;
			skinLoaded=true;
		}
		else{
			profile.cape=null;
			capeLoaded=true;
		}
		if(skinLoaded && capeLoaded){
			CustomSkinServer.putProfile(profile.username, profile);
			if(isUpdate)
				CustomSkinServer.sendToAll(CustomSkinServer.GSON.toJson(new UpdateRequest(profile)));
		}
	}

}
