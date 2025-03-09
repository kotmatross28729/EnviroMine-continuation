package enviromine.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.blocks.compat.BlockOffTorch_Bone_Netherlicious;
import enviromine.core.EnviroMine;
import enviromine.utils.misc.CompatSafe;

@CompatSafe
public class BlockOffTorch extends BlockTorch {

    public BlockOffTorch() {
        super();
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Items.stick;
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
            if (EnviroMine.isNetherliciousLoaded) {
                Block block = BlockOffTorch_Bone_Netherlicious.getTorchNetherlicious(stack, player);
                if (block != null) {
                    world.setBlock(i, j, k, block, world.getBlockMetadata(i, j, k), 3);
                }
            } else if (stack.getItem() == Items.flint_and_steel) {
                if (!player.capabilities.isCreativeMode) stack.damageItem(1, player);
                world.setBlock(i, j, k, Blocks.torch, world.getBlockMetadata(i, j, k), 3);
            }
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        // No particles
    }
}
