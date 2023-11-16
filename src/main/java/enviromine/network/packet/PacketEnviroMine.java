/**
 * @author thislooksfun
 */
package enviromine.network.packet;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import enviromine.client.gui.hud.HUDRegistry;
import enviromine.core.EM_ConfigHandler;
import enviromine.core.EM_ConfigHandler.EnumLogVerbosity;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.handlers.EM_EventManager;
import enviromine.handlers.EM_StatusManager;
import enviromine.trackers.EnviroDataTracker;
import enviromine.world.ClientQuake;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketEnviroMine implements IMessage
{
	private NBTTagCompound tags;
	
	public PacketEnviroMine() {}
	public PacketEnviroMine(NBTTagCompound _tags) {
		this.tags = _tags;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.tags = ByteBufUtils.readTag(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, this.tags);
	}
	
	public static class HandlerServer implements IMessageHandler<PacketEnviroMine,IMessage>
	{
		@Override
		public IMessage onMessage(PacketEnviroMine packet, MessageContext ctx)
		{
			int id = packet.tags.hasKey("id")? packet.tags.getInteger("id") : -1;
			
			if(id == 1)
			{
				this.emptyRightClick(packet.tags);
			} else
			{
				if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.NORMAL.getLevel()) EnviroMine.logger.log(Level.ERROR, "Received invalid packet on serverside!");
			}
			return null; //Reply
		}
		
		private void emptyRightClick(NBTTagCompound tags)
		{
			EntityPlayer player = EM_StatusManager.findPlayer(tags.getString("player"));
			
			if(player != null)
			{
				EM_EventManager.drinkWater(player, null);
			}
		}
	}
	
	public static class HandlerClient implements IMessageHandler<PacketEnviroMine,IMessage>
	{
		@Override
		public IMessage onMessage(PacketEnviroMine packet, MessageContext ctx)
		{
			int id = packet.tags.hasKey("id")? packet.tags.getInteger("id") : -1;
			
			if(id == 0)
			{
				this.trackerSync(packet.tags);
			}
			else if(id == 3)
			{
				this.registerQuake(packet.tags);
			}
			else if(id == 4)
			{
				//TODO this will need to be updated on the Teardown
				if(!packet.tags.getBoolean("enableAirQ"))
				{
					HUDRegistry.disableHudItem(HUDRegistry.getHudItemByID(EM_ConfigHandler.HUD_ID_AIR_QUALITY));
				}
				else
				{
					HUDRegistry.enableHudItem(HUDRegistry.getHudItemByID(EM_ConfigHandler.HUD_ID_AIR_QUALITY));
				}
				
				if(!packet.tags.getBoolean("enableBodyTemp"))
				{
					HUDRegistry.disableHudItem(HUDRegistry.getHudItemByID(EM_ConfigHandler.HUD_ID_BODY_TEMPERATURE));
				}
				else
				{
					HUDRegistry.enableHudItem(HUDRegistry.getHudItemByID(EM_ConfigHandler.HUD_ID_BODY_TEMPERATURE));
				}

				if(!packet.tags.getBoolean("enableHydrate"))
				{
					HUDRegistry.disableHudItem(HUDRegistry.getHudItemByID(EM_ConfigHandler.HUD_ID_HYDRATION));
				}
				else
				{
					HUDRegistry.enableHudItem(HUDRegistry.getHudItemByID(EM_ConfigHandler.HUD_ID_HYDRATION));
				}

				if(!packet.tags.getBoolean("enableSanity"))
				{
					HUDRegistry.disableHudItem(HUDRegistry.getHudItemByID(EM_ConfigHandler.HUD_ID_SANITY));
				}
				else
				{
					HUDRegistry.enableHudItem(HUDRegistry.getHudItemByID(EM_ConfigHandler.HUD_ID_SANITY));
				}
				
			}
			else
			{
				if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.NORMAL.getLevel()) EnviroMine.logger.log(Level.ERROR, "Received invalid packet on clientside!");
			}
			return null; //Reply
		}
		
		private void trackerSync(NBTTagCompound tags)
		{
			
			EnviroDataTracker tracker = EM_StatusManager.lookupTrackerFromUsername(tags.getString("player"));
			
			if(tracker != null)
			{
				tracker.prevAirQuality = tracker.airQuality;
				tracker.prevBodyTemp = tracker.bodyTemp;
				tracker.prevHydration = tracker.hydration;
				tracker.prevSanity = tracker.sanity;
				tracker.airQuality = Float.valueOf(tags.getFloat("airQuality"));
				tracker.bodyTemp = Float.valueOf(tags.getFloat("bodyTemp"));
				tracker.hydration = Float.valueOf(tags.getFloat("hydration"));
				tracker.sanity = Float.valueOf(tags.getFloat("sanity"));
				tracker.airTemp = Float.valueOf(tags.getFloat("airTemp"));
			}
		}
		
		private void registerQuake(NBTTagCompound tags)
		{
			int b = tags.getInteger("action");
			int d = tags.getInteger("dimension");
			int x = tags.getInteger("posX");
			int z = tags.getInteger("posZ");
			int l = tags.getInteger("length");
			int w = tags.getInteger("width");
			float a = tags.getFloat("angle");
			int h = tags.getInteger("height");
			
			if(b == 0)
			{
				new ClientQuake(d, x, z, l, w, a);
			} else if(b == 1)
			{
				ClientQuake.UpdateQuakeHeight(d, x, z, l, w, a, h);
			} else if(b == 2)
			{
				ClientQuake.RemoveQuake(x, z);
			}
		}
	}
}