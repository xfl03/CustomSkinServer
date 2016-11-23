package customskinserver.sponge;

import java.util.Collection;
import java.util.Set;

import org.spongepowered.api.Platform.Type;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.network.ChannelBinding.IndexedMessageChannel;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.network.MessageHandler;
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.network.RemoteConnection;
import org.spongepowered.api.plugin.Plugin;

import customskinserver.CustomSkinServer;
import customskinserver.CustomSkinServer.BasicPlugin;

@Plugin(id = "customskinserver", name = "CustomSkinServer", version = "0.1")
public class SpongePlugin implements BasicPlugin {
	public static IndexedMessageChannel channel;
	
	@Listener
    public void onServerStart(GameStartedServerEvent event) {
		CustomSkinServer.onLoad(this);
		//TODO
		channel=Sponge.getChannelRegistrar().createChannel(this, CustomSkinServer.PLUGIN_CHANNEL_NAME);
		channel.registerMessage(Message.class, 0, new Handler());
    }

	@Override
	public void sendToAll(Set<String> players, String message) {
		Collection<Player> onlinePlayers=Sponge.getServer().getOnlinePlayers();
		for(Player player:onlinePlayers){
			if(!players.contains(player.getName().toLowerCase()))
				continue;
			channel.sendTo(player, new Message(message));
		}
	}
	
	@Listener
	public void onPlayerQuit(ClientConnectionEvent.Disconnect event){
		CustomSkinServer.removeProfile(event.getTargetEntity().getName());
	}
	
	public static class Message implements org.spongepowered.api.network.Message{

		public Message(String text){
			this.text=text;
		}
		public String text;
		@Override
		public void readFrom(ChannelBuf buf) {
			buf.readUTF();
		}

		@Override
		public void writeTo(ChannelBuf buf) {
			buf.writeUTF(text);
		}
		
	}
	
	public static class Handler implements MessageHandler<Message> {
		@Override
		public void handleMessage(Message message, RemoteConnection connection, Type side) {
			if(!(connection instanceof PlayerConnection)){
				CustomSkinServer.logger.warning("[CustomSkinServer] RemoteConnection not instanceof PlayerConnection.");
				return;
			}
			PlayerConnection pc=(PlayerConnection) connection;
			Player p=pc.getPlayer();
			CustomSkinServer.logger.debug("[CustomSkinServer] Message Received From "+p.getName()+" : "+message.text);
		}
		
    }
	
}
