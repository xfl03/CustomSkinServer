package customskinserver.forge;

import customskinserver.CustomSkinServer.BasicPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

public class ForgePlayer implements BasicPlayer {
	private EntityPlayerMP player;
	public ForgePlayer(EntityPlayerMP player){
		this.player=player;
	}

	@Override
	public String getUsername() {
		return player.getName();
	}

	@Override
	public void sendMessage(String message) {
		player.sendMessage(new TextComponentString(message));
	}

	@Override
	public void sendPluginMessage(String message) {
		ForgeMod.network.sendTo(new ForgeMod.Message(message), player);
	}

}
