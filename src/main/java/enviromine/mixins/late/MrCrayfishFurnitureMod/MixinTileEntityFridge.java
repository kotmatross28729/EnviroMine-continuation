package enviromine.mixins.late.MrCrayfishFurnitureMod;

import com.mrcrayfish.furniture.tileentity.TileEntityFridge;
import enviromine.core.EM_Settings;
import enviromine.trackers.properties.RotProperties;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityFridge.class, priority = 1003)
public abstract class MixinTileEntityFridge extends TileEntity implements ISidedInventory {
	@Unique
	int enviroMine$tick = 0;
	@Unique
	int enviroMine$interval = 30;
	@Unique
	long enviroMine$lastCheck = -1;

	@Inject(method = "func_145845_h", at = @At(value = "HEAD"), remap = false)
	public void updateEntity(CallbackInfo ci) {
		// Esky Code

		if(this.getWorldObj() == null)
		{
			return;
		}

		if(enviroMine$lastCheck <= -1)
		{
			enviroMine$lastCheck = worldObj.getTotalWorldTime();
		}

		if(enviroMine$tick >=  enviroMine$interval && !this.worldObj.isRemote) {
			enviroMine$tick = 0;

			long time = worldObj.getTotalWorldTime() -  enviroMine$lastCheck;
			enviroMine$lastCheck = worldObj.getTotalWorldTime();

			for(int i = 0; i < this.getSizeInventory(); i++) {
				ItemStack stack = this.getStackInSlot(i);

				if(stack == null) {
					continue;
				}

				RotProperties rotProps = null;
				long rotTime = (long)(EM_Settings.foodRotTime * 24000L);

				if(EM_Settings.rotProperties.containsKey("" + Item.itemRegistry.getNameForObject(stack.getItem())))
				{
					rotProps = EM_Settings.rotProperties.get("" + Item.itemRegistry.getNameForObject(stack.getItem()));
					rotTime = (long)(rotProps.days * 24000L);
				} else if(EM_Settings.rotProperties.containsKey("" + Item.itemRegistry.getNameForObject(stack.getItem()) + "," + stack.getItemDamage()))
				{
					rotProps = EM_Settings.rotProperties.get("" + Item.itemRegistry.getNameForObject(stack.getItem()) + "," + stack.getItemDamage());
					rotTime = (long)(rotProps.days * 24000L);
				}

				if(!EM_Settings.foodSpoiling || (rotProps == null && !(stack.getItem() instanceof ItemFood)) || rotTime < 0)
				{
					if(stack.getTagCompound() != null)
					{
						if(stack.getTagCompound().hasKey("EM_ROT_DATE"))
						{
							stack.getTagCompound().removeTag("EM_ROT_DATE");
						}
						if(stack.getTagCompound().hasKey("EM_ROT_TIME"))
						{
							stack.getTagCompound().removeTag("EM_ROT_TIME");
						}
					}
				} else {
					if(stack.getTagCompound() == null)
					{
						stack.setTagCompound(new NBTTagCompound());
					}
					NBTTagCompound tags = stack.getTagCompound();

					if(tags.hasKey("EM_ROT_DATE"))
					{
						tags.setLong("EM_ROT_DATE", tags.getLong("EM_ROT_DATE") + time/2);
						tags.setLong("EM_ROT_TIME", tags.getLong("EM_ROT_TIME") + time/2);
					}
				}
			}

			this.markDirty();
		} else {
			enviroMine$tick++;
		}
	}
	
	@Inject(method = "func_145839_a",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/tileentity/TileEntity;readFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V",
					shift = At.Shift.AFTER), remap = false)
	public void readFromNBT(NBTTagCompound tags, CallbackInfo ci) {
		if(tags.hasKey("RotCheck")) {
			this.enviroMine$lastCheck = tags.getLong("RotCheck");
		} else {
			this.enviroMine$lastCheck = -1;
		}
	}

	@Inject(method = "func_145841_b", at = @At(value = "TAIL"), remap = false)
	public void writeToNBT(NBTTagCompound tags, CallbackInfo ci) {
		tags.setLong("RotCheck", this.enviroMine$lastCheck);
	}
}
