package customskinserver.profile;

import org.apache.commons.lang3.StringUtils;

public class Profile {
	public String username;
	public String skin;
	public String model;
	public String cape;
	public String elytra;
	
	public Profile(String username){
		this.username=username;
	}
	
	public static Profile createEmptyProfile(String username){
		return new Profile(username);
	}
	public static Profile createDefaultProfile(String username,String skin,String cape){
		return new Profile(username).setSkin(skin).setCape(cape);
	}
	public static Profile createStardardProfile(String username,String skin,String model,String cape){
		return new Profile(username).setSkin(skin).setModel(model).setCape(cape);
	}
	public static Profile createFullProfile(String username,String skin,String model,String cape,String elytra){
		return new Profile(username).setSkin(skin).setModel(model).setCape(cape).setElytra(elytra);
	}
	
	public Profile setUsername(String username){
		this.username=username;
		return this;
	}
	public Profile setSkin(String skin){
		this.skin=skin;
		return this;
	}
	public Profile setModel(String model){
		this.model=StringUtils.isNotEmpty(model)?model:"default";
		return this;
	}
	public Profile setCape(String cape){
		this.cape=cape;
		return this;
	}
	public Profile setElytra(String elytra){
		this.elytra=elytra;
		return this;
	}
}
