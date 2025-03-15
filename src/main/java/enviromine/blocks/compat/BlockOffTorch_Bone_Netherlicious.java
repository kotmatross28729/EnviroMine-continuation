package enviromine.blocks.compat;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import DelirusCrux.Netherlicious.Common.BlockItemUtility.ModBlocks;
import DelirusCrux.Netherlicious.Common.BlockItemUtility.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.core.EM_Settings;
import enviromine.handlers.ObjectHandler;

public class BlockOffTorch_Bone_Netherlicious extends BlockTorch {

    public BlockOffTorch_Bone_Netherlicious() {
        super();
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Items.bone;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
        p_149726_1_.scheduleBlockUpdate(
            p_149726_2_,
            p_149726_3_,
            p_149726_4_,
            this,
            this.tickRate(p_149726_1_) + p_149726_1_.rand.nextInt(10));
        super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7,
        float par8, float par9) {
        ItemStack stack = player.getEquipmentInSlot(0);

        if (stack != null && stack.getItem() != null) {
            Block block = BlockOffTorch_Bone_Netherlicious.getBoneTorchNetherlicious(stack, player);
            if (block != null) {
                world.setBlock(i, j, k, block, world.getBlockMetadata(i, j, k), 3);
            }
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        // No particles
    }

    public static Block getBoneTorchNetherlicious(ItemStack lighter, EntityPlayer player) {
        if (lighter.getItem() == ModItems.FlintAndSteelSoul) {
            if (!player.capabilities.isCreativeMode) lighter.damageItem(1, player);
            return ModBlocks.SoulTorchBone;
        } else if (lighter.getItem() == ModItems.FlintAndSteelFoxfire) {
            if (!player.capabilities.isCreativeMode) lighter.damageItem(1, player);
            return ModBlocks.FoxfireTorchBone;
        } else if (lighter.getItem() == ModItems.FlintAndSteelShadow) {
            if (!player.capabilities.isCreativeMode) lighter.damageItem(1, player);
            return ModBlocks.ShadowTorchBone;
        } else if (lighter.getItem() == Items.flint_and_steel) {
            if (!player.capabilities.isCreativeMode) lighter.damageItem(1, player);
            return ModBlocks.TorchBone;
        }
        return null;
    }

    public static Block getTorchNetherlicious(ItemStack lighter, EntityPlayer player) {
        if (lighter.getItem() == ModItems.FlintAndSteelSoul) {
            if (!player.capabilities.isCreativeMode) lighter.damageItem(1, player);
            return ModBlocks.SoulTorch;
        } else if (lighter.getItem() == ModItems.FlintAndSteelFoxfire) {
            if (!player.capabilities.isCreativeMode) lighter.damageItem(1, player);
            return ModBlocks.FoxfireTorch;
        } else if (lighter.getItem() == ModItems.FlintAndSteelShadow) {
            if (!player.capabilities.isCreativeMode) lighter.damageItem(1, player);
            return ModBlocks.ShadowTorch;
        } else if (lighter.getItem() == Items.flint_and_steel) {
            if (!player.capabilities.isCreativeMode) lighter.damageItem(1, player);

            if (EM_Settings.oldTorchLogic) {
                return ObjectHandler.fireTorch;
            } else {
                return Blocks.torch;
            }

        }

        return null;
    }

}
