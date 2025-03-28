package enviromine.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.core.EM_Settings;
import enviromine.handlers.EnviroAchievements;
import enviromine.handlers.ObjectHandler;

public class EnviroArmor extends ItemArmor {

    public IIcon gmIcon;
    public IIcon hhIcon;

    public EnviroArmor(ArmorMaterial par2EnumArmorMaterial, int par3, int par4) {
        super(par2EnumArmorMaterial, par3, par4);
        this.setMaxDamage(100);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        if (stack.getItem() == ObjectHandler.gasMask) {
            return "enviroMine:textures/models/armor/gasmask_layer_1.png";
        } else if (stack.getItem() == ObjectHandler.hardHat) {
            return "enviroMine:textures/models/armor/hardhat_layer_1.png";
        } else {
            return null;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.gmIcon = par1IconRegister.registerIcon("enviromine:gas_mask");
        this.hhIcon = par1IconRegister.registerIcon("enviromine:hard_hat");
    }

    @SideOnly(Side.CLIENT)
    /**
     * Gets an icon index based on an item's damage value
     */
    @Override
    public IIcon getIconFromDamage(int par1) {

        if (this == ObjectHandler.gasMask && gmIcon != null) {
            return this.gmIcon;
        } else if (this == ObjectHandler.hardHat && hhIcon != null) {
            return this.hhIcon;
        }
        {
            return super.getIconFromDamage(par1);
        }
    }

    @Override
    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        if (par1ItemStack.getItem() == ObjectHandler.hardHat
            && (par2ItemStack.getItem() == ObjectHandler.hardHat || par2ItemStack.getItem() == Items.iron_ingot)) {
            return true;
        } else return par1ItemStack.getItem() == ObjectHandler.gasMask
            && (par2ItemStack.getItem() == ObjectHandler.gasMask || par2ItemStack.getItem() == Items.iron_ingot);
    }

    // Creates a tag if item was grabbed from creative menu
    @Override
    public void onUpdate(ItemStack armor, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (armor.getItem() == ObjectHandler.gasMask) {
            if (!armor.hasTagCompound()) {
                armor.setTagCompound(new NBTTagCompound());
            }
            if (!armor.getTagCompound()
                .hasKey(EM_Settings.GAS_MASK_FILL_TAG_KEY)) {
                armor.getTagCompound()
                    .setInteger(EM_Settings.GAS_MASK_FILL_TAG_KEY, EM_Settings.gasMaskMax);
            }
            if (!armor.getTagCompound()
                .hasKey(EM_Settings.GAS_MASK_MAX_TAG_KEY)) {
                armor.getTagCompound()
                    .setInteger(EM_Settings.GAS_MASK_MAX_TAG_KEY, EM_Settings.gasMaskMax);
            }
        } else if (armor.getItem() == ObjectHandler.hardHat) {} else {}
    }

    @Override
    public void onCreated(ItemStack armor, World world, EntityPlayer player) {
        if (armor.getItem() == ObjectHandler.gasMask) {
            if (!armor.hasTagCompound()) {
                armor.setTagCompound(new NBTTagCompound());
            }
            armor.getTagCompound()
                .setInteger(EM_Settings.GAS_MASK_FILL_TAG_KEY, EM_Settings.gasMaskMax);
            armor.getTagCompound()
                .setInteger(EM_Settings.GAS_MASK_MAX_TAG_KEY, EM_Settings.gasMaskMax);
            player.addStat(EnviroAchievements.breatheEasy, 1);
        } else if (armor.getItem() == ObjectHandler.hardHat) {
            player.addStat(EnviroAchievements.safetyFirst, 1);
        }
    }
}
