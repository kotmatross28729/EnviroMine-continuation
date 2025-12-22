package enviromine.blocks.water;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import com.hbm.util.ContaminationUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.blocks.water.compat.BlockEnviroMineWater_NTM_SPACE;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.utils.WaterUtils;

public class BlockEnviroMineWater extends BlockFluidClassic {

    public static IIcon stillWater;
    public static IIcon flowingWater;

    public BlockEnviroMineWater(Fluid fluid, Material material) {
        super(fluid, material);
    }

    // Adjust water color based on surrounding biomes to avoid uniform blue water
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z) {
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        for (int k1 = -1; k1 <= 1; ++k1) {
            for (int l1 = -1; l1 <= 1; ++l1) {
                int i2 = worldIn.getBiomeGenForCoords(x + l1, z + k1)
                    .getWaterColorMultiplier();
                l += (i2 & 16711680) >> 16;
                i1 += (i2 & 65280) >> 8;
                j1 += i2 & 255;
            }
        }

        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (!world.isRemote) {
            Block block = world.getBlock(x, y, z);
            if (block instanceof BlockEnviroMineWater water) {
                if (WaterUtils.getTypeFromFluid(water.getFluid()).isRadioactive) {
                    if (entity instanceof EntityLivingBase entityLivingBase) ContaminationUtil.contaminate(
                        entityLivingBase,
                        ContaminationUtil.HazardType.RADIATION,
                        ContaminationUtil.ContaminationType.CREATIVE,
                        0.125F);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int meta) {
        return (side == 0 || side == 1) ? stillWater : flowingWater;
    }

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
        return super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
        return super.displaceIfPossible(world, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        stillWater = register.registerIcon("water_still");
        flowingWater = register.registerIcon("water_flow");
    }

    public void updateTick(World world, int x, int y, int z, Random rand) {
        // Implementation of probabilistic check for vanilla water contact (Strategy 2)
        if (EM_Settings.convertToVanilla && rand.nextInt(EM_Settings.conversionChance) == 0) {
            boolean hasVanillaWater = checkForVanillaWater(world, x, y, z);

            if (hasVanillaWater) {
                // Convert current block to vanilla water
                int currentMeta = world.getBlockMetadata(x, y, z);
                world.setBlock(x, y, z, Blocks.flowing_water, currentMeta, 3);

                // Chain reaction conversion (Strategy 4)
                if (EM_Settings.chainReaction) {
                    convertAdjacentCustomWater(world, x, y, z, rand);
                }

                // After conversion, this block is no longer custom water, so skip further processing
                // However, vanilla water needs to continue updating, so call its updateTick
                Blocks.flowing_water.updateTick(world, x, y, z, rand);
                return;
            }
        }

        // Original HBM space evaporation check
        if (EnviroMine.isHbmSpaceLoaded) {
            if (BlockEnviroMineWater_NTM_SPACE.checkEvaporation(world, x, y, z, tickRate)) {
                return; // Will not try to spill further if removed
            }
        }

        // Original infinite water mechanism
        if (!this.isSourceBlock(world, x, y, z)) {
            int adjacentSourceBlocks = (this.isSourceBlock(world, x - 1, y, z) ? 1 : 0)
                + (this.isSourceBlock(world, x + 1, y, z) ? 1 : 0)
                + (this.isSourceBlock(world, x, y, z - 1) ? 1 : 0)
                + (this.isSourceBlock(world, x, y, z + 1) ? 1 : 0);
            int densityDir = getDensity(world, x, y, z) > 0 ? -1 : 1;
            if (adjacentSourceBlocks >= 2 && (world.getBlock(x, y + densityDir, z)
                .getMaterial()
                .isSolid() || this.isSourceBlock(world, x, y + densityDir, z))) {
                world.setBlockMetadataWithNotify(x, y, z, 0, 3);
            }
        }

        super.updateTick(world, x, y, z, rand);
    }

    // Helper method: Check surrounding blocks for vanilla water
    private boolean checkForVanillaWater(World world, int x, int y, int z) {
        // Check all six adjacent directions
        int[][] directions = { { 1, 0, 0 }, { -1, 0, 0 }, { 0, 0, 1 }, { 0, 0, -1 }, { 0, 1, 0 }, { 0, -1, 0 } };

        for (int[] dir : directions) {
            int checkX = x + dir[0];
            int checkY = y + dir[1];
            int checkZ = z + dir[2];

            Block neighborBlock = world.getBlock(checkX, checkY, checkZ);
            if (neighborBlock == Blocks.flowing_water || neighborBlock == Blocks.water) {
                return true;
            }
        }

        return false;
    }

    // Helper method: Convert adjacent custom water blocks
    private void convertAdjacentCustomWater(World world, int x, int y, int z, Random rand) {
        int[][] directions = { { 1, 0, 0 }, { -1, 0, 0 }, { 0, 0, 1 }, { 0, 0, -1 }, { 0, 1, 0 }, { 0, -1, 0 } };

        int conversions = 0;

        for (int[] dir : directions) {

            if (conversions >= EM_Settings.maxConversionsPerTick) {
                break; // Reached maximum conversions per tick
            }

            int checkX = x + dir[0];
            int checkY = y + dir[1];
            int checkZ = z + dir[2];

            Block neighborBlock = world.getBlock(checkX, checkY, checkZ);

            // If neighbor is custom water, convert it with a probability
            if (neighborBlock instanceof BlockEnviroMineWater) {
                // Convert with probability to avoid simultaneous conversion of all water
                if (rand.nextInt(3) == 0) { // 1 in 3 chance to convert adjacent block
                    int neighborMeta = world.getBlockMetadata(checkX, checkY, checkZ);
                    world.setBlock(checkX, checkY, checkZ, Blocks.flowing_water, neighborMeta, 3);
                    conversions++;

                    // Optional: Schedule update for newly converted block to continue chain reaction
                    // Note: This may cause performance issues, use with caution
                    // world.scheduleBlockUpdate(checkX, checkY, checkZ, Blocks.flowing_water, 1);
                }
            }
        }
    }

    // Optional: Override onNeighborBlockChange for faster response to neighbor changes
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
        super.onNeighborBlockChange(world, x, y, z, neighborBlock);

        // If neighbor is vanilla water, immediately check for conversion (without waiting for updateTick)
        if (EM_Settings.convertToVanilla && (neighborBlock == Blocks.flowing_water || neighborBlock == Blocks.water)) {
            // Check if current block is still custom water
            if (world.getBlock(x, y, z) instanceof BlockEnviroMineWater) {
                int currentMeta = world.getBlockMetadata(x, y, z);
                world.setBlock(x, y, z, Blocks.flowing_water, currentMeta, 3);

                // Trigger chain reaction
                if (EM_Settings.chainReaction) {
                    // Use world's random instance
                    Random rand = world.rand;
                    convertAdjacentCustomWater(world, x, y, z, rand);
                }
            }
        }
    }
}
