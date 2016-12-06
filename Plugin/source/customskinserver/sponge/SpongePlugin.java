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
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.network.RemoteConnection;
import org.spongepowered.api.plugin.Plugin;

import customskinserver.CustomSkinServer;
import customskinserver.CustomSkinServer.BasicPlugin;
import customskinserver.handler.Handler;

@Plugin(id = "customskinserver", name = "CustomSkinServer", version = CustomSkinServer.VERSION,
    description = "Server plugin for minecraft to transport skins.",
    url = "https://github.com/xfl03/CustomSkinServer",
    authors = {"xfl03"})
public class SpongePlugin implements BasicPlugin {
	public static IndexedMessageChannel channel;
	
	@Listener
    public void onServerStart(GameStartedServerEvent event) {
		CustomSkinServer.logger.info("CustomSkinServer "+CustomSkinServer.VERSION+" Runs On SpongeAPI.");
		CustomSkinServer.onLoad(this);
		channel=Sponge.getChannelRegistrar().createChannel(this, CustomSkinServer.PLUGIN_CHANNEL_NAME);
		channel.registerMessage(Message.class, 0, new MessageHandler());
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
		CustomSkinServer.removeProfileAndBroadcast(event.getTargetEntity().getName());
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
	
	public static class MessageHandler implements org.spongepowered.api.network.MessageHandler<Message> {
		@Override
		public void handleMessage(Message message, RemoteConnection connection, Type side) {
			if(!(connection instanceof PlayerConnection)){
				CustomSkinServer.logger.warning("[CustomSkinServer] RemoteConnection not instanceof PlayerConnection.");
				return;
			}
			PlayerConnection pc=(PlayerConnection) connection;
			Player p=pc.getPlayer();
			CustomSkinServer.logger.debug("[CustomSkinServer] Message Received From "+p.getName()+" : "+message.text);
			Handler.handle(new SpongePlayer(p),message.text);
		}
		
    }
	
}
