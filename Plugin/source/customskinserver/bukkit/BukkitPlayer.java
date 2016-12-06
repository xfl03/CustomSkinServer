package customskinserver.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import customskinserver.CustomSkinServer;
import customskinserver.CustomSkinServer.BasicPlayer;
import customskinserver.util.BytesUtil;

public class BukkitPlayer implements BasicPlayer {
	public Plugin plugin;
	public Player player; 
	public BukkitPlayer(Plugin plugin,Player player){
		this.plugin=plugin;
		this.player=player;
	}
	@Override
	public String getUsername() {
		return player.getName();
	}

	@Override
	public void sendMessage(String message) {
		player.sendMessage(message);
	}

	@Override
	public void sendPluginMessage(String message) {
		CustomSkinServer.logger.debug("[CustomSkinServer] Message Send To "+player.getName()+" : "+message);
		player.sendPluginMessage(plugin, CustomSkinServer.PLUGIN_CHANNEL_NAME, BytesUtil.toBytes(message));
	}

}
