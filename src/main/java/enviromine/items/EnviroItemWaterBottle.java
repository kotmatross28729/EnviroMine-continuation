package enviromine.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.EnviroPotion;
import enviromine.core.EM_Settings;
import enviromine.handlers.EM_StatusManager;
import enviromine.trackers.EnviroDataTracker;
import enviromine.utils.WaterUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class EnviroItemWaterBottle extends Item {
    WaterUtils.WATER_TYPES waterType;
    @SideOnly(Side.CLIENT)
    private IIcon field_94591_c;
    @SideOnly(Side.CLIENT)
    private IIcon field_94590_d;
    @SideOnly(Side.CLIENT)
    private IIcon field_94592_ct;

    public EnviroItemWaterBottle(WaterUtils.WATER_TYPES waterType)
    {
        super();
        setTextureName("potion");
        this.waterType = waterType;
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
                        if (tracker.bodyTemp >= EM_Settings.ColdFrostyWaterReducesTemperatureStartingValue) {
                            tracker.bodyTemp += EM_Settings.FrostyWaterTemperatureInfluence;
                        }
                        if(EM_Settings.FrostyWaterHydrate > 0F)
                        {
                            tracker.hydrate(EM_Settings.FrostyWaterHydrate);
                        } else if(EM_Settings.FrostyWaterHydrate < 0F)
                        {
                            tracker.dehydrate(Math.abs(EM_Settings.FrostyWaterHydrate));
                        }
                    } else if (waterType == WaterUtils.WATER_TYPES.DIRTY_COLD) {
                        if (tracker.bodyTemp >= EM_Settings.ColdFrostyWaterReducesTemperatureStartingValue) {
                            tracker.bodyTemp += EM_Settings.DirtyColdWaterTemperatureInfluence;
                        }
                        if(EM_Settings.DirtyColdWaterHydrate > 0F)
                        {
                            tracker.hydrate(EM_Settings.DirtyColdWaterHydrate);
                        } else if(EM_Settings.DirtyColdWaterHydrate < 0F)
                        {
                            tracker.dehydrate(Math.abs(EM_Settings.DirtyColdWaterHydrate));
                        }
                    } else if (waterType == WaterUtils.WATER_TYPES.CLEAN_COLD) {
                        if(tracker.bodyTemp >= EM_Settings.ColdFrostyWaterReducesTemperatureStartingValue)
                        {
                            tracker.bodyTemp += EM_Settings.CleanColdWaterTemperatureInfluence;
                        }
                        if(EM_Settings.CleanColdWaterHydrate > 0F)
                        {
                            tracker.hydrate(EM_Settings.CleanColdWaterHydrate);
                        } else if(EM_Settings.CleanColdWaterHydrate < 0F)
                        {
                            tracker.dehydrate(Math.abs(EM_Settings.CleanColdWaterHydrate));
                        }
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
                            if(tracker.bodyTemp > EM_Settings.WaterReducesTemperatureStartingValue)
                            {
                                tracker.bodyTemp += EM_Settings.SaltyWaterTemperatureInfluence;
                            }
                        if(EM_Settings.SaltyWaterHydrate > 0F)
                        {
                            tracker.hydrate(EM_Settings.SaltyWaterHydrate);
                        } else if(EM_Settings.SaltyWaterHydrate < 0F)
                        {
                            tracker.dehydrate(Math.abs(EM_Settings.SaltyWaterHydrate));
                        }
                    } else if (waterType == WaterUtils.WATER_TYPES.DIRTY) {
                        if(par3EntityPlayer.getRNG().nextInt(4) == 0)
                        {
                            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.hunger.id, 600));
                        }
                        if(par3EntityPlayer.getRNG().nextInt(4) == 0)
                        {
                            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.poison.id, 200));
                        }
                        if(tracker.bodyTemp > EM_Settings.WaterReducesTemperatureStartingValue)
                        {
                            tracker.bodyTemp += EM_Settings.DirtyWaterTemperatureInfluence;
                        }
                        if(EM_Settings.DirtyWaterHydrate > 0F)
                        {
                            tracker.hydrate(EM_Settings.DirtyWaterHydrate);
                        } else if(EM_Settings.DirtyWaterHydrate < 0F)
                        {
                            tracker.dehydrate(Math.abs(EM_Settings.DirtyWaterHydrate));
                        }
                    } else if (waterType == WaterUtils.WATER_TYPES.CLEAN_WARM) {
                        if(tracker.bodyTemp >= EM_Settings.WarmHotWaterReducesTemperatureStartingValue) {
                            tracker.bodyTemp += EM_Settings.CleanWarmWaterTemperatureInfluence;
                        }
                        if(EM_Settings.CleanWarmWaterHydrate > 0F)
                        {
                            tracker.hydrate(EM_Settings.CleanWarmWaterHydrate);
                        } else if(EM_Settings.CleanWarmWaterHydrate < 0F)
                        {
                            tracker.dehydrate(Math.abs(EM_Settings.CleanWarmWaterHydrate));
                        }
                    } else if (waterType == WaterUtils.WATER_TYPES.DIRTY_WARM) {
                        if(par3EntityPlayer.getRNG().nextInt(4) == 0)
                        {
                            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.hunger.id, 600));
                        }
                        if(par3EntityPlayer.getRNG().nextInt(4) == 0)
                        {
                            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.poison.id, 200));
                        }
                            if(tracker.bodyTemp >= EM_Settings.WarmHotWaterReducesTemperatureStartingValue) {
                                tracker.bodyTemp += EM_Settings.DirtyWarmWaterTemperatureInfluence;
                            }
                        if(EM_Settings.DirtyWarmWaterHydrate > 0F)
                        {
                            tracker.hydrate(EM_Settings.DirtyWarmWaterHydrate);
                        } else if(EM_Settings.DirtyWarmWaterHydrate < 0F)
                        {
                            tracker.dehydrate(Math.abs(EM_Settings.DirtyWarmWaterHydrate));
                        }

                    } else if (waterType == WaterUtils.WATER_TYPES.HOT) {
                        if(tracker.bodyTemp >= EM_Settings.WarmHotWaterReducesTemperatureStartingValue) {
                            tracker.bodyTemp += EM_Settings.HotWarmWaterTemperatureInfluence;
                        }
                        if(EM_Settings.HotWaterHydrate > 0F)
                        {
                            tracker.hydrate(EM_Settings.HotWaterHydrate);
                        } else if(EM_Settings.HotWaterHydrate < 0F)
                        {
                            tracker.dehydrate(Math.abs(EM_Settings.HotWaterHydrate));
                        }
                    }
                }
            }
        }

        if(!par3EntityPlayer.capabilities.isCreativeMode)
        {
            if(par1ItemStack.stackSize <= 0)
            {
                return new ItemStack(Items.glass_bottle);
            }

            par3EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
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
    /**
     * Gets an icon index based on an item's damage value
     */
    @Override
    public IIcon getIconFromDamage(int par1)
    {
        return this.field_94590_d;
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

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.field_94590_d = par1IconRegister.registerIcon(this.getIconString() + "_" + "bottle_drinkable");
        this.field_94591_c = par1IconRegister.registerIcon(this.getIconString() + "_" + "bottle_splash");
        this.field_94592_ct = par1IconRegister.registerIcon(this.getIconString() + "_" + "overlay");
    }

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int par2)
    {
        return par2 == 0 ? this.field_94592_ct : super.getIconFromDamageForRenderPass(par1, par2);
    }

    @SideOnly(Side.CLIENT)
    public static IIcon func_94589_d(String par0Str)
    {
        return ItemPotion.func_94589_d(par0Str);
    }

}
