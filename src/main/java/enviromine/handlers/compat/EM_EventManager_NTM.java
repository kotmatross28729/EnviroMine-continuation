package enviromine.handlers.compat;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.gas.BlockGasFlammable;
import com.hbm.blocks.gas.BlockGasMonoxide;
import com.hbm.blocks.gas.BlockGasRadonDense;
import com.hbm.blocks.gas.BlockGasRadonTomb;
import com.hbm.blocks.gas.BlockVacuum;
import com.hbm.main.MainRegistry;
import com.hbm.sound.AudioWrapper;
import enviromine.core.EM_Settings;
import enviromine.handlers.ObjectHandler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent;

import static enviromine.handlers.EM_EventManager.ReplaceInvoItems;
import static enviromine.handlers.EM_EventManager.getBlockWithinAABB;

public class EM_EventManager_NTM {
	public static void handleOnBlockBreakCoal(BlockEvent.BreakEvent event) {
		if (event.block == ObjectHandler.flammableCoal || event.block == ObjectHandler.burningCoal) {
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				
				int x = event.x + dir.offsetX;
				int y = event.y + dir.offsetY;
				int z = event.z + dir.offsetZ;
				
				if (event.world.rand.nextInt(2) == 0 && event.world.getBlock(x, y, z) == Blocks.air)
					event.world.setBlock(x, y, z, ModBlocks.gas_coal);
			}
		}
	}
	
	private static AudioWrapper audioBreathing;
	public static void handleGasMaskSound(LivingEvent.LivingUpdateEvent event, InventoryPlayer invo) {
		ItemStack mask = invo.armorItemInSlot(3);
		if (mask != null && mask.getItem() != null && mask.getItem() == ObjectHandler.gasMask) {
			if(mask.hasTagCompound() && mask.getTagCompound().hasKey(EM_Settings.GAS_MASK_FILL_TAG_KEY)) {
				if ((audioBreathing == null || !audioBreathing.isPlaying())) {
					audioBreathing = MainRegistry.proxy.getLoopedSound("enviromine:breathing", (float) event.entityLiving.posX, (float) event.entityLiving.posY, (float) event.entityLiving.posZ, 0.1F, 5.0F, 1.0F, 5);
					audioBreathing.startSound();
				}
				audioBreathing.updatePosition((float) event.entityLiving.posX, (float) event.entityLiving.posY, (float) event.entityLiving.posZ);
				audioBreathing.keepAlive();
			} else {
				if(audioBreathing != null) {
					audioBreathing.stopSound();
					audioBreathing = null;
				}
			}
		} else if(audioBreathing != null) {
			audioBreathing.stopSound();
			audioBreathing = null;
		}
	}
	
	public static void handleNTMGas(LivingEvent.LivingUpdateEvent event, AxisAlignedBB boundingBox, InventoryPlayer invo) {
		if(getBlockWithinAABB(boundingBox, event.entityLiving.worldObj, BlockGasFlammable.class)){
			//Fire -> Blue fire
			ReplaceInvoItems(invo, Item.getItemFromBlock(ObjectHandler.davyLampBlock), 1, Item.getItemFromBlock(ObjectHandler.davyLampBlock), 2);
		} else if (
				getBlockWithinAABB(boundingBox, event.entityLiving.worldObj, BlockGasMonoxide.class) ||
						getBlockWithinAABB(boundingBox, event.entityLiving.worldObj, BlockGasRadonDense.class) ||
						getBlockWithinAABB(boundingBox, event.entityLiving.worldObj, BlockGasRadonTomb.class) ||
						getBlockWithinAABB(boundingBox, event.entityLiving.worldObj, BlockVacuum.class))
		{
			//Fire -> No fire
			ReplaceInvoItems(invo, Item.getItemFromBlock(ObjectHandler.davyLampBlock), 1, Item.getItemFromBlock(ObjectHandler.davyLampBlock), 0);
		}
	}
	
}
