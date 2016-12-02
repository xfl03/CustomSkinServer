package customskinserver.bukkit;

import java.util.Collection;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import customskinserver.CustomSkinServer;
import customskinserver.CustomSkinServer.BasicPlugin;
import customskinserver.handler.Handler;
import customskinserver.util.BytesUtil;

public class BukkitPlugin extends JavaPlugin implements BasicPlugin,Listener,PluginMessageListener {
	@Override
	public void onEnable(){
		CustomSkinServer.logger.info("CustomSkinServer "+CustomSkinServer.VERSION+" Runs On BukkitAPI.");
		CustomSkinServer.onLoad(this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, CustomSkinServer.PLUGIN_CHANNEL_NAME);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, CustomSkinServer.PLUGIN_CHANNEL_NAME,this);
        this.getServer().getPluginManager().registerEvents(this, this);
	}
	@Override
	public void sendToAll(Set<String> players, String message) {
		CustomSkinServer.logger.debug("[CustomSkinServer] Send to All: "+message);
		Collection<? extends Player> onlinePlayers=this.getServer().getOnlinePlayers();
		for(Player player:onlinePlayers){
			if(!players.contains(player.getName().toLowerCase()))
				continue;
			player.sendPluginMessage(this, CustomSkinServer.PLUGIN_CHANNEL_NAME, BytesUtil.toBytes(message));
		}
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if(!channel.equalsIgnoreCase(CustomSkinServer.PLUGIN_CHANNEL_NAME))
			return;
		String m=BytesUtil.fromBytes(message);
		CustomSkinServer.logger.debug("[CustomSkinServer] Message Received From "+player.getName()+" : "+m);
		Handler.handle(new BukkitPlayer(this,player), m);
	}
	
	public void onPlayerQuit(PlayerQuitEvent event){
		CustomSkinServer.removeProfile(event.getPlayer().getName());
	}
}
