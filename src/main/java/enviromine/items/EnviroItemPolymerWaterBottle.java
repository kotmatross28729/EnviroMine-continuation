package enviromine.items;

import com.hbm.extprop.HbmLivingProps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.EnviroPotion;
import enviromine.core.EM_Settings;
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

        //TODO: textures
        switch (this.waterType) {
            case RADIOACTIVE_FROSTY :
            case FROSTY             : setTextureName("enviromine:bottle_frosty");

            case RADIOACTIVE_COLD :
            case DIRTY_COLD       : setTextureName("enviromine:bottle_dirty_cold");
            case SALTY_COLD       :
            case CLEAN_COLD       : setTextureName("enviromine:bottle_cold");

            case RADIOACTIVE :
            case DIRTY       : setTextureName("enviromine:bottle_dirty");
            case SALTY       : setTextureName("enviromine:bottle_salt");
            case CLEAN       : setTextureName("enviromine:bottle_clean");

            case RADIOACTIVE_WARM :
            case DIRTY_WARM       :  setTextureName("enviromine:bottle_dirty_warm");
            case SALTY_WARM       :
            case CLEAN_WARM       :  setTextureName("enviromine:bottle_warm");

            case RADIOACTIVE_HOT :
            case HOT             :  setTextureName("enviromine:bottle_hot");
        }
    }

    public WaterUtils.WATER_TYPES getWaterType() {
        return waterType;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if(!par3EntityPlayer.capabilities.isCreativeMode) {
            --par1ItemStack.stackSize;
        }
    
        if (!par2World.isRemote) {
            if(waterType.isRadioactive) { //TODO config
                HbmLivingProps.incrementRadiation(par3EntityPlayer, 5.0F); 
            }
            
            if(waterType.isDirty) { //TODO config
                if(par3EntityPlayer.getRNG().nextInt(4) == 0) {
                    par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.hunger.id, 600));
                }
                if(par3EntityPlayer.getRNG().nextInt(4) == 0) {
                    par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.poison.id, 200));
                }
            }
            
            if(waterType.isSalty) { //TODO config
                if(par3EntityPlayer.getActivePotionEffect(EnviroPotion.dehydration) != null && par3EntityPlayer.getRNG().nextInt(5) == 0) {
                    int amp = par3EntityPlayer.getActivePotionEffect(EnviroPotion.dehydration).getAmplifier();
                    par3EntityPlayer.addPotionEffect(new PotionEffect(EnviroPotion.dehydration.id, 600, amp + 1));
                } else {
                    par3EntityPlayer.addPotionEffect(new PotionEffect(EnviroPotion.dehydration.id, 600));
                }
            }
        }
        
        if(!par3EntityPlayer.capabilities.isCreativeMode) {
            if(par1ItemStack.stackSize <= 0) {
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
