package customskinserver.handler;

import java.util.HashMap;

import customskinserver.CustomSkinServer;
import customskinserver.CustomSkinServer.BasicPlayer;

public class Handler {
	public interface BasicHandler{}
	public interface RequestHandler{
		public void handleRequest(BasicPlayer player,String message) throws Exception;
	}
	public interface ResponceHandler{
		public void handleResponce(BasicPlayer player,String message) throws Exception;
	}
	public static HashMap<String,BasicHandler> handlers;
	static{
		handlers=new HashMap<String,BasicHandler>();
		
		handlers.put("login", new LoginHandler());
		handlers.put("texture", new TextureHandler());
		handlers.put("update", new UpdateHandler());
	}
	public static class BasicMessage{
		public String action;
		public String type;
	}
	
	public static void handle(BasicPlayer player,String message){
		if(message==null||message.equals(""))
			return;
		BasicMessage parsedMessage=CustomSkinServer.GSON.fromJson(message, BasicMessage.class);
		BasicHandler handler=handlers.get(parsedMessage.action.toLowerCase());
		if(handler==null)
			return;
		try{
			if("responce".equalsIgnoreCase(parsedMessage.type)){
				if(handler instanceof ResponceHandler)
					((ResponceHandler)handler).handleResponce(player, message);
			}else{
				if(handler instanceof RequestHandler)
					((RequestHandler)handler).handleRequest(player, message);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
