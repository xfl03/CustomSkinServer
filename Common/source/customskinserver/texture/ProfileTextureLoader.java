package customskinserver.texture;

import org.apache.commons.lang3.StringUtils;

import customskinserver.CustomSkinServer;
import customskinserver.CustomSkinServer.BasicPlayer;
import customskinserver.handler.TextureHandler.TextureLoadedCallback;
import customskinserver.profile.Profile;

public class ProfileTextureLoader implements TextureLoadedCallback {
	public static void update(BasicPlayer player,Profile profile){
		String username=player.getUsername();
		if(profile==null){
			profile=Profile.createEmptyProfile(username);
			CustomSkinServer.profileManager.putProfileAndBroadcast(username, profile);
			return;
		}
		profile.username=username;
		
		ProfileTextureLoader callback=new ProfileTextureLoader(profile);
		if(StringUtils.isEmpty(profile.skin)||CustomSkinServer.textureManager.textureExists(profile.skin))
			callback.skinLoaded=true;
		else
			CustomSkinServer.textureManager.loadTextureAsync(player, profile.skin, callback);
		
		if(StringUtils.isEmpty(profile.cape)||CustomSkinServer.textureManager.textureExists(profile.cape))
			callback.capeLoaded=true;
		else
			CustomSkinServer.textureManager.loadTextureAsync(player, profile.cape, callback);
			
	}
	
	public Profile profile;
	public boolean skinLoaded;
	public boolean capeLoaded;
	public ProfileTextureLoader(Profile profile){
		this.profile=profile;
	}

	@Override
	public void onTextureLoaded(String hash) {
		if(hash.equals(profile.skin))
			skinLoaded=true;
		else
			capeLoaded=true;
		if(skinLoaded && capeLoaded)
			CustomSkinServer.profileManager.putProfileAndBroadcast(profile.username, profile);
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
		if(skinLoaded && capeLoaded)
			CustomSkinServer.profileManager.putProfileAndBroadcast(profile.username, profile);
	}

}
