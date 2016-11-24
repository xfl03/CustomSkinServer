package customskinserver;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import com.google.gson.Gson;

import customskinserver.handler.TextureHandler;
import customskinserver.handler.TextureHandler.TextureLoadedCallback;
import customskinserver.handler.TextureHandler.TextureRequest;
import customskinserver.profile.Profile;

public class CustomSkinServer {
	public static final String VERSION="${full_version}";
	public static final String PLUGIN_CHANNEL_NAME="CustomSkinServer";
	
	public static final File DATA_DIR=new File("CustomSkinServer");
	public static final File TEXTURE_DIR=new File(DATA_DIR,"textures");
	public static final File LOG_FILE=new File(DATA_DIR,"CustomSkinServer.log");
	public static final File CONFIG_FILE=new File(DATA_DIR,"CustomSkinServer.json");
	
	public static final Logger logger=new Logger(LOG_FILE);
	public static final Gson GSON=new Gson();
	public static final Config config=Config.loadConfig0();
	public static final HashMap<String,Profile> profiles=new HashMap<String, Profile>();
	
	
	public interface BasicPlayer{
		public String getUsername();
		public void sendMessage(String message);
		public void sendPluginMessage(String message);
	}
	public interface BasicPlugin{
		public void sendToAll(Set<String> players,String message);
	}
	public static BasicPlugin plugin=null;
	
	static{
		if(!TEXTURE_DIR.isDirectory())
			TEXTURE_DIR.mkdirs();
	}
	
	public static void onLoad(BasicPlugin plugin){
		CustomSkinServer.plugin=plugin;
	}
	public static void sendToAll(String message){
		plugin.sendToAll(profiles.keySet(), message);
	}
	
	public static File getTextureFile(String hash){
		return new File(TEXTURE_DIR,hash);
	}
	public static boolean textureExists(String hash){
		return getTextureFile(hash).isFile();
	}
	public static void loadTextureAsync(BasicPlayer player,String hash,TextureLoadedCallback callback){
		if(callback!=null)
			TextureHandler.callbacks.put(hash, callback);
		player.sendPluginMessage(CustomSkinServer.GSON.toJson(new TextureRequest(hash)));
	}
	
	public static void putProfile(String username,Profile profile){
		profiles.put(username.toLowerCase(), profile);
	}
	public static Profile getProfile(String username){
		return profiles.get(username.toLowerCase());
	}
	public static void removeProfile(String username){
		profiles.remove(username.toLowerCase());
	}
}
