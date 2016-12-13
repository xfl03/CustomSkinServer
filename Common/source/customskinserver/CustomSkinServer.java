package customskinserver;

import java.io.File;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import customskinserver.handler.Handler;
import customskinserver.profile.ProfileManager;
import customskinserver.texture.TextureManager;

public class CustomSkinServer {
	public static final String VERSION="${full_version}";
	public static final String PLUGIN_CHANNEL_NAME="ServerProfile";
	
	public static final File DATA_DIR=new File("CustomSkinServer");
	
	public static final File LOG_FILE=new File(DATA_DIR,"CustomSkinServer.log");
	public static final File CONFIG_FILE=new File(DATA_DIR,"CustomSkinServer.json");
	
	public static final Gson GSON=new Gson();
	public static final Gson GSON_PRETTY=new GsonBuilder().setPrettyPrinting().create();
	
	public static final Logger logger=new Logger(LOG_FILE);
	public static final Config config=Config.loadConfig();
	
	public static final ProfileManager profileManager=new ProfileManager();
	public static final TextureManager textureManager=new TextureManager();
	
	public interface BasicPlayer{
		public String getUsername();
		public void sendMessage(String message);
		public void sendPluginMessage(String message);
	}
	public interface BasicPlugin{
		public void sendToAll(Set<String> players,String message);
	}
	public static BasicPlugin plugin=null;
	
	public static void onLoad(BasicPlugin plugin){
		CustomSkinServer.plugin=plugin;
	}
	public static void onPluginMessage(BasicPlayer player,String message){
		logger.debug("[CustomSkinServer] Message Received From "+player.getUsername()+" : "+message);
		Handler.handle(player,message);
	}
	public static void onPlayerQuit(String username){
		profileManager.removeProfileAndBroadcast(username);
	}
	public static void sendToAll(String message){
		plugin.sendToAll(profileManager.profiles.keySet(), message);
	}
}
