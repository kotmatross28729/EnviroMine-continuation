package enviromine.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.EnviroPotion;
import enviromine.handlers.EM_StatusManager;
import enviromine.handlers.ObjectHandlerCompat;
import enviromine.trackers.EnviroDataTracker;
import enviromine.utils.WaterUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.world.World;

public class EnviroItemPolymerWaterBottle extends Item {
    WaterUtils.WATER_TYPES waterType;

    public EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES waterType)
    {
        super();
        this.waterType = waterType;

        if(this.waterType == WaterUtils.WATER_TYPES.FROSTY) {
            setTextureName("enviromine:bottle_frosty");
        } else if (this.waterType == WaterUtils.WATER_TYPES.DIRTY_COLD) {
            setTextureName("enviromine:bottle_dirty_cold");
        } else if (this.waterType == WaterUtils.WATER_TYPES.CLEAN_COLD) {
            setTextureName("enviromine:bottle_cold");
        } else if (this.waterType == WaterUtils.WATER_TYPES.SALTY) {
            setTextureName("enviromine:bottle_salt");
        } else if (this.waterType == WaterUtils.WATER_TYPES.DIRTY) {
            setTextureName("enviromine:bottle_dirty");
        } else if (this.waterType == WaterUtils.WATER_TYPES.CLEAN) {
            setTextureName("enviromine:bottle_clean");
        } else if (this.waterType == WaterUtils.WATER_TYPES.CLEAN_WARM) {
            setTextureName("enviromine:bottle_warm");
        } else if (this.waterType == WaterUtils.WATER_TYPES.DIRTY_WARM) {
            setTextureName("enviromine:bottle_dirty_warm");
        } else if (this.waterType == WaterUtils.WATER_TYPES.HOT) {
            setTextureName("enviromine:bottle_hot");
        }
    }

    public WaterUtils.WATER_TYPES getWaterType() {
        return waterType;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if(!par3EntityPlayer.capabilities.isCreativeMode)
        {
            --par1ItemStack.stackSize;
        }

        if(!par2World.isRemote)
        {
            EnviroDataTracker tracker = EM_StatusManager.lookupTracker(par3EntityPlayer);

            if(tracker != null)
            {
                if(tracker.bodyTemp >= 0)
                {
                    if(waterType == WaterUtils.WATER_TYPES.FROSTY) {
                        if (tracker.bodyTemp >= 30.1) {
                            tracker.bodyTemp -= 0.5F;
                        }
                        tracker.hydrate(50F);
                    } else if (waterType == WaterUtils.WATER_TYPES.DIRTY_COLD) {
                        if (tracker.bodyTemp >= 30.1) {
                            tracker.bodyTemp -= 0.1F;
                        }
                        tracker.hydrate(50F);
                    } else if (waterType == WaterUtils.WATER_TYPES.CLEAN_COLD) {
                        if(tracker.bodyTemp >= 30.1)
                        {
                            tracker.bodyTemp -= 0.1F;
                        }
                        tracker.hydrate(50F);
                    } else if (waterType == WaterUtils.WATER_TYPES.SALTY) {
                        if(par3EntityPlayer.getRNG().nextInt(1) == 0)
                        {
                            if(par3EntityPlayer.getActivePotionEffect(EnviroPotion.dehydration) != null && par3EntityPlayer.getRNG().nextInt(5) == 0)
                            {
                                int amp = par3EntityPlayer.getActivePotionEffect(EnviroPotion.dehydration).getAmplifier();
                                par3EntityPlayer.addPotionEffect(new PotionEffect(EnviroPotion.dehydration.id, 600, amp + 1));
                            } else
                            {
                                par3EntityPlayer.addPotionEffect(new PotionEffect(EnviroPotion.dehydration.id, 600));
                            }
                        }
                        if(tracker.bodyTemp > 37.05F)
                        {
                            tracker.bodyTemp -= 0.05F;
                        }
                        tracker.hydrate(30F);
                    } else if (waterType == WaterUtils.WATER_TYPES.DIRTY) {
                        if(par3EntityPlayer.getRNG().nextInt(4) == 0)
                        {
                            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.hunger.id, 600));
                        }
                        if(par3EntityPlayer.getRNG().nextInt(4) == 0)
                        {
                            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.poison.id, 200));
                        }
                        if(tracker.bodyTemp > 37.05F)
                        {
                            tracker.bodyTemp -= 0.05F;
                        }
                        tracker.hydrate(50F);
                    } else if (waterType == WaterUtils.WATER_TYPES.CLEAN) {
                        if(tracker.bodyTemp > 37.05F)
                        {
                            tracker.bodyTemp -= 0.05F;
                        }
                        tracker.hydrate(50F);
                    } else if (waterType == WaterUtils.WATER_TYPES.CLEAN_WARM) {
                        tracker.bodyTemp += 0.1F;
                        tracker.hydrate(50F);
                    } else if (waterType == WaterUtils.WATER_TYPES.DIRTY_WARM) {
                        if(par3EntityPlayer.getRNG().nextInt(4) == 0)
                        {
                            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.hunger.id, 600));
                        }
                        if(par3EntityPlayer.getRNG().nextInt(4) == 0)
                        {
                            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.poison.id, 200));
                        }
                        if(tracker.bodyTemp >= 0)
                        {
                            tracker.bodyTemp += 0.1F;
                            tracker.hydrate(50F);
                        }
                    } else if (waterType == WaterUtils.WATER_TYPES.HOT) {
                        tracker.bodyTemp += 0.5F;
                        tracker.hydrate(50F);
                    }
                }
            }
        }

        if(!par3EntityPlayer.capabilities.isCreativeMode)
        {
            if(par1ItemStack.stackSize <= 0)
            {
                return new ItemStack(ObjectHandlerCompat.waterBottle_polymer);
            }

            par3EntityPlayer.inventory.addItemStackToInventory(new ItemStack(ObjectHandlerCompat.waterBottle_polymer));
        }

        return par1ItemStack;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        return false;
    }
    @SideOnly(Side.CLIENT)
    public int getColorFromDamage(int par1)
    {
        return PotionHelper.func_77915_a(par1, false);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return par2 > 0 ? 16777215 : this.getColorFromDamage(par1ItemStack.getItemDamage());
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        return par1ItemStack;
    }
}
