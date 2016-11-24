package customskinserver;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

public class Config {
	public String version=CustomSkinServer.CustomSkinServerVersion;
	public boolean enable=true;
	public int maxSize=30;//KB
	
	public static Config loadConfig0() {
		Config config=loadConfig();
		return config;
	}

	private static Config loadConfig() {
		if(!CustomSkinServer.CONFIG_FILE.exists()){
			return initConfig();
		}
		try {
			String json=FileUtils.readFileToString(CustomSkinServer.CONFIG_FILE,Charsets.UTF_8);
			Config config=CustomSkinServer.GSON.fromJson(json, Config.class);
			return config;
		}catch (Exception e) {
			return initConfig();
		}
	}
	private static Config initConfig() {
		Config config=new Config();
		writeConfig(config,false);
		return config;
	}
	private static void writeConfig(Config config,boolean update){
		String json=CustomSkinServer.GSON.toJson(config);
		if(CustomSkinServer.CONFIG_FILE.exists())
			CustomSkinServer.CONFIG_FILE.delete();
		try {
			CustomSkinServer.CONFIG_FILE.createNewFile();
			FileUtils.write(CustomSkinServer.CONFIG_FILE, json, Charsets.UTF_8);
		} catch (Exception e) {
			
		}
	}
}
