package enviromine.mixins.late.MrCrayfishFurnitureMod;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mrcrayfish.furniture.MrCrayfishFurnitureMod;
import com.mrcrayfish.furniture.tileentity.TileEntityFreezer;

import enviromine.core.EM_Settings;
import enviromine.trackers.properties.RotProperties;

@Mixin(value = TileEntityFreezer.class, priority = 1003)
public abstract class MixinTileEntityFreezer extends TileEntity implements ISidedInventory {

    @Unique
    int enviroMine$tick = 0;
    @Unique
    int enviroMine$interval = 30;
    @Unique
    long enviroMine$lastCheck = -1;

    @Shadow
    public int freezerFreezeTime = 0; // -- on update
    @Shadow
    public int currentItemCoolTime = 0; // 2500
    @Shadow
    public int freezerCoolTime = 0; // progress
    @Shadow
    private ItemStack[] freezerItemStacks = new ItemStack[3];

    @Shadow
    private static int getItemFreezeTime(ItemStack itemstack) {
        if (itemstack == null) {
            return 0;
        } else {
            Item i = itemstack.getItem();
            if (i == MrCrayfishFurnitureMod.itemCoolPack) {
                return 2500;
            } else {
                return i == (new ItemStack(Blocks.ice)).getItem() ? 5000 : 0;
            }
        }
    }

    @Inject(method = "func_145845_h", at = @At(value = "HEAD"), remap = false)
    public void updateEntity(CallbackInfo ci) {
        super.updateEntity();

        // Freezer Code
        if (this.getWorldObj() == null) {
            return;
        }

        if (enviroMine$lastCheck <= -1) {
            enviroMine$lastCheck = worldObj.getTotalWorldTime();
        }

        if (enviroMine$tick >= enviroMine$interval && !this.worldObj.isRemote) {
            enviroMine$tick = 0;

            long time = worldObj.getTotalWorldTime() - enviroMine$lastCheck;
            enviroMine$lastCheck = worldObj.getTotalWorldTime();

            for (int i = 0; i < this.getSizeInventory(); i++) {
                ItemStack stack = this.getStackInSlot(i);

                if (stack == null) {
                    continue;
                }

                RotProperties rotProps = null;
                long rotTime = (long) (EM_Settings.foodRotTime * 24000L);

                if (EM_Settings.rotProperties.containsKey("" + Item.itemRegistry.getNameForObject(stack.getItem()))) {
                    rotProps = EM_Settings.rotProperties.get("" + Item.itemRegistry.getNameForObject(stack.getItem()));
                    rotTime = (long) (rotProps.days * 24000L);
                } else if (EM_Settings.rotProperties.containsKey(
                    "" + Item.itemRegistry.getNameForObject(stack.getItem()) + "," + stack.getItemDamage())) {
                        rotProps = EM_Settings.rotProperties.get(
                            "" + Item.itemRegistry.getNameForObject(stack.getItem()) + "," + stack.getItemDamage());
                        rotTime = (long) (rotProps.days * 24000L);
                    }

                if (!EM_Settings.foodSpoiling || (rotProps == null && !(stack.getItem() instanceof ItemFood))
                    || rotTime < 0) {
                    if (stack.getTagCompound() != null) {
                        if (stack.getTagCompound()
                            .hasKey("EM_ROT_DATE")) {
                            stack.getTagCompound()
                                .removeTag("EM_ROT_DATE");
                        }
                        if (stack.getTagCompound()
                            .hasKey("EM_ROT_TIME")) {
                            stack.getTagCompound()
                                .removeTag("EM_ROT_TIME");
                        }
                    }
                } else {
                    if (this.freezerFreezeTime == 0) {
                        this.currentItemCoolTime = this.freezerFreezeTime = getItemFreezeTime(
                            this.freezerItemStacks[1]);
                        if (this.freezerFreezeTime > 0) {
                            if (this.freezerItemStacks[1] != null) {
                                --this.freezerItemStacks[1].stackSize;
                                if (this.freezerItemStacks[1].stackSize == 0) {
                                    this.freezerItemStacks[1] = null;
                                }
                            }
                        }
                    } else {
                        if (stack.getTagCompound() == null) {
                            stack.setTagCompound(new NBTTagCompound());
                        }
                        NBTTagCompound tags = stack.getTagCompound();

                        if (tags.hasKey("EM_ROT_DATE")) {
                            tags.setLong("EM_ROT_DATE", tags.getLong("EM_ROT_DATE") + time);
                            tags.setLong("EM_ROT_TIME", tags.getLong("EM_ROT_TIME") + time);
                        }
                    }
                }
            }
            this.markDirty();
        } else {
            enviroMine$tick++;
        }
    }

    @Inject(
        method = "func_145839_a",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/tileentity/TileEntity;readFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V",
            shift = At.Shift.AFTER),
        remap = false)
    public void readFromNBT(NBTTagCompound tags, CallbackInfo ci) {
        if (tags.hasKey("RotCheck")) {
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
