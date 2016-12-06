package customskinserver.forge;

import java.util.Set;

import customskinserver.CustomSkinServer;
import customskinserver.CustomSkinServer.BasicPlugin;
import customskinserver.handler.Handler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "customskinserver", name = "CustomSkinServer", version = CustomSkinServer.VERSION, serverSideOnly=true)
public class ForgeMod implements BasicPlugin {

	public static SimpleNetworkWrapper network;
	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
		CustomSkinServer.logger.info("CustomSkinServer "+CustomSkinServer.VERSION+" Runs On ForgeAPI.");
		CustomSkinServer.onLoad(this);
		network = NetworkRegistry.INSTANCE.newSimpleChannel(CustomSkinServer.PLUGIN_CHANNEL_NAME);
        network.registerMessage(MessageHandler.class, Message.class, 0, Side.SERVER);
        MinecraftForge.EVENT_BUS.register(this);
    }
	@Override
	public void sendToAll(Set<String> players, String message) {
		
	}
	@SubscribeEvent
	public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event){
		CustomSkinServer.removeProfileAndBroadcast(event.player.getName());
	}
	
	public static class Message implements IMessage{

		public Message(String text){
			this.text=text;
		}
		public String text;
		@Override
	    public void fromBytes(ByteBuf buf) {
	        text = ByteBufUtils.readUTF8String(buf);
	    }

	    @Override
	    public void toBytes(ByteBuf buf) {
	        ByteBufUtils.writeUTF8String(buf, text);
	    }
		
	}
	
	public static class MessageHandler implements IMessageHandler<Message,Message> {

		@Override
		public Message onMessage(Message message, MessageContext ctx) {
			EntityPlayerMP p=ctx.getServerHandler().playerEntity;
			CustomSkinServer.logger.debug("[CustomSkinServer] Message Received From "+p.getName()+" : "+message.text);
			Handler.handle(new ForgePlayer(p),message.text);
			return null;
		}
		
    }

}
