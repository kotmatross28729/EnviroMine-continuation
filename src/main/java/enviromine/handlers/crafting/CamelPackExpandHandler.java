package enviromine.handlers.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import enviromine.core.EM_Settings;
import enviromine.handlers.ObjectHandler;

public class CamelPackExpandHandler implements IRecipe {

    public ItemStack pack1;
    public ItemStack pack2;

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        if (!inv.getInventoryName()
            .equals("container.crafting")) {
            return false;
        }

        this.pack1 = null;
        this.pack2 = null;

        for (int i = inv.getSizeInventory() - 1; i >= 0; i--) {
            ItemStack item = inv.getStackInSlot(i);
            if (item == null) {
                continue;
            } else if (item.hasTagCompound() && item.stackTagCompound.hasKey(EM_Settings.IS_CAMEL_PACK_TAG_KEY)) {
                if (item.getTagCompound()
                    .getInteger(EM_Settings.CAMEL_PACK_MAX_TAG_KEY) > EM_Settings.camelPackMax) {
                    return false; // Temp thing to disable more than double sized packs
                }

                if (pack1 != null) {
                    if (pack2 != null) {
                        return false;
                    } else {
                        pack2 = item.copy();
                    }
                } else {
                    pack1 = item.copy();
                }
            } else {
                return false;
            }
        }

        return (pack1 != null && pack2 != null);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        this.matches(inv, null);

        ItemStack pack = new ItemStack(ObjectHandler.camelPack);

        pack.setTagCompound(new NBTTagCompound());
        pack.getTagCompound()
            .setInteger(
                EM_Settings.CAMEL_PACK_FILL_TAG_KEY,
                pack1.getTagCompound()
                    .getInteger(EM_Settings.CAMEL_PACK_FILL_TAG_KEY)
                    + pack2.getTagCompound()
                        .getInteger(EM_Settings.CAMEL_PACK_FILL_TAG_KEY));
        pack.getTagCompound()
            .setInteger(
                EM_Settings.CAMEL_PACK_MAX_TAG_KEY,
                pack1.getTagCompound()
                    .getInteger(EM_Settings.CAMEL_PACK_MAX_TAG_KEY)
                    + pack2.getTagCompound()
                        .getInteger(EM_Settings.CAMEL_PACK_MAX_TAG_KEY));
        pack.getTagCompound()
            .setBoolean(EM_Settings.IS_CAMEL_PACK_TAG_KEY, true);

        return pack;
    }

    @Override
    public int getRecipeSize() {
        return 4;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    /*
     * @SubscribeEvent
     * public void onCrafting(PlayerEvent.ItemCraftedEvent event)
     * {
     * IInventory craftMatrix = event.craftMatrix;
     * if (!(craftMatrix instanceof InventoryCrafting) || !craftMatrix.getInventoryName().equals("container.crafting"))
     * {
     * return;
     * }
     * this.matches((InventoryCrafting)craftMatrix, event.player.worldObj);
     * }
     */

}
