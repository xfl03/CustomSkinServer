package customskinserver.handler;

import customskinserver.CustomSkinServer.BasicPlayer;
import customskinserver.handler.Handler.BasicHandler;

public class RemoveHandler implements BasicHandler {

	@Override
	public void handleRequest(BasicPlayer player, String message) throws Exception {
		//No Request
	}

	@Override
	public void handleResponce(BasicPlayer player, String message) throws Exception {
		//No Responce
	}
	
	public static class RemoveRequest{
		public String action="REMOVE";
		public String type="REQUEST";
		public String username;
		public RemoveRequest(String username){
			this.username=username;
		}
	}
}
