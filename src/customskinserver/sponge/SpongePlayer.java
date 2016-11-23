package customskinserver.sponge;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import customskinserver.CustomSkinServer.BasicPlayer;
import customskinserver.sponge.SpongePlugin.Message;

public class SpongePlayer implements BasicPlayer {
	private Player player;
	public SpongePlayer(Player player){
		this.player=player;
	}

	@Override
	public String getUsername() {
		return player.getName();
	}

	@Override
	public void sendMessage(String message) {
		player.sendMessage(Text.of(message));
	}

	@Override
	public void sendPluginMessage(String message) {
		SpongePlugin.channel.sendTo(player, new Message(message));
	}

}
