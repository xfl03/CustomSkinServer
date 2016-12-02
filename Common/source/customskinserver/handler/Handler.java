package customskinserver.handler;

import java.util.HashMap;

import customskinserver.CustomSkinServer;
import customskinserver.CustomSkinServer.BasicPlayer;

public class Handler {
	public interface BasicHandler{
		public void handleRequest(BasicPlayer player,String message) throws Exception;
		public void handleResponce(BasicPlayer player,String message) throws Exception;
	}
	public static HashMap<String,BasicHandler> handlers;
	static{
		handlers=new HashMap<String,BasicHandler>();
		//TODO add handlers here
	}
	public static class BasicMessage{
		public String action;
		public String type;
	}
	
	public static void handle(BasicPlayer player,String message){
		if(message==null||message.equals(""))
			return;
		if(!CustomSkinServer.config.enable)
			return;
		BasicMessage parsedMessage=CustomSkinServer.GSON.fromJson(message, BasicMessage.class);
		BasicHandler handler=handlers.get(parsedMessage.action.toLowerCase());
		if(handler==null)
			return;
		try{
			if("responce".equalsIgnoreCase(parsedMessage.type)){
				handler.handleResponce(player, message);
			}else{
				handler.handleRequest(player, message);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
