package customskinserver.handler;

import customskinserver.handler.Handler.BasicHandler;

public class RemoveHandler implements BasicHandler {
	public static class RemoveRequest{
		public String action="REMOVE";
		public String type="REQUEST";
		public String username;
		public RemoveRequest(String username){
			this.username=username;
		}
	}
}
