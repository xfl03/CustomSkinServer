package customskinserver.profile;

public class Profile {
	public String username;
	public String skin;
	public String cape;
	
	public Profile(String username){
		this.username=username;
	}
	public Profile(String username,String skin,String cape){
		this.username=username;
		this.skin=skin;
		this.cape=cape;
	}
}
