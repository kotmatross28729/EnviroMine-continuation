package enviromine.mixins.early;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import enviromine.core.EM_Settings;
import enviromine.handlers.ObjectHandler;
import enviromine.handlers.compat.ObjectHandler_Netherlicious;
import enviromine.trackers.properties.BlockProperties;

@Mixin(value = BlockTorch.class, priority = 1003)
public abstract class MixinBlockTorch extends Block {

    // TODO: REWORK

    protected MixinBlockTorch(Material materialIn) {
        super(materialIn);
    }

    @Inject(method = "onBlockAdded", at = @At(value = "HEAD"))
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_, CallbackInfo ci) {
        p_149726_1_.scheduleBlockUpdate(
            p_149726_2_,
            p_149726_3_,
            p_149726_4_,
            this,
            this.tickRate(p_149726_1_) + p_149726_1_.rand.nextInt(10));
    }

    @Inject(method = "updateTick", at = @At(value = "HEAD"))
    protected void updateTick(World world, int x, int y, int z, Random rand, CallbackInfo ci) {
        if ((!EM_Settings.oldTorchLogic) && EM_Settings.torchesGoOut) {
            Block block = world.getBlock(x, y, z);
            int meta = world.getBlockMetadata(x, y, z);
            if (BlockProperties.base.hasProperty(block, meta)) {
                BlockProperties blockProps = BlockProperties.base.getProperty(block, meta);
                if (blockProps != null) {
                    if (Block.getBlockFromName(blockProps.goOutName) != null) {
                        Block blockOut = Block.getBlockFromName(blockProps.goOutName);
                        if (Block.blockRegistry.getNameForObject(blockOut)
                            .equals(blockProps.goOutName)) {
                            if (blockProps.goOut) {
                                if (world.rand.nextInt(blockProps.goOutChance) == 0
                                    || (blockProps.goOutRain && world.isRaining()
                                        && world.canBlockSeeTheSky(x, y, z))) {
                                    world.playSoundEffect(
                                        (double) x + 0.5D,
                                        (double) y + 0.5D,
                                        (double) z + 0.5D,
                                        "random.fizz",
                                        0.5F,
                                        2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                                    world.setBlock(x, y, z, blockOut, meta, 3);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Inject(
        method = "updateTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/Block;updateTick(Lnet/minecraft/world/World;IIILjava/util/Random;)V",
            shift = At.Shift.AFTER))
    protected void updateTick2(World world, int x, int y, int z, Random rand, CallbackInfo ci) {
        // Don't go any further unless this torch is allowed to burn stuff
        if ((!EM_Settings.oldTorchLogic) && EM_Settings.torchesBurn
            && (this != ObjectHandler.offTorch && this != ObjectHandler_Netherlicious.offTorchBone)) {
            world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world) + rand.nextInt(10));

            int l = world.getBlockMetadata(x, y, z);
            boolean flag1 = world.isBlockHighHumidity(x, y, z);
            byte b0 = 0;

            if (flag1) {
                b0 = -50;
            }

            this.enviroMine$tryCatchFire(world, x, y + 1, z, 250 + b0, rand, l, DOWN);

            // for (int i1 = x - 1; i1 <= x + 1; ++i1) {
            // for (int j1 = z - 1; j1 <= z + 1; ++j1) {
            // for (int k1 = y - 1; k1 <= y + 4; ++k1) {
            // if (i1 != x || k1 != y || j1 != z) {
            // int l1 = 100;
            //
            // if (k1 > y + 1) {
            // l1 += (k1 - (y + 1)) * 100;
            // }
            //
            // int i2 = this.enviroMine$getChanceOfNeighborsEncouragingFire(world, i1, k1, j1);
            //
            // if (i2 > 0) {
            // int j2 = (i2 + 40 + world.difficultySetting.getDifficultyId() * 7) / (l + 30);
            //
            // if (flag1) {
            // j2 /= 2;
            // }
            //
            // if (j2 > 0 && rand.nextInt(l1) <= j2
            // && (!world.isRaining() || !world.canLightningStrikeAt(i1, k1, j1))
            // && !world.canLightningStrikeAt(i1 - 1, k1, z)
            // && !world.canLightningStrikeAt(i1 + 1, k1, j1)
            // && !world.canLightningStrikeAt(i1, k1, j1 - 1)
            // && !world.canLightningStrikeAt(i1, k1, j1 + 1)) {
            // int k2 = l + rand.nextInt(5) / 4;
            //
            // if (k2 > 15) {
            // k2 = 15;
            // }
            //
            // world.setBlock(i1, k1, j1, Blocks.fire, k2, 3);
            // }
            // }
            // }
            // }
            // }
            // }

        }
    }

    @Unique
    private void enviroMine$tryCatchFire(World p_149841_1_, int p_149841_2_, int p_149841_3_, int p_149841_4_,
        int p_149841_5_, Random p_149841_6_, int p_149841_7_, ForgeDirection face) {
        int j1 = p_149841_1_.getBlock(p_149841_2_, p_149841_3_, p_149841_4_)
            .getFlammability(p_149841_1_, p_149841_2_, p_149841_3_, p_149841_4_, face);

        if (p_149841_6_.nextInt(p_149841_5_) < j1) {
            boolean flag = p_149841_1_.getBlock(p_149841_2_, p_149841_3_, p_149841_4_) == Blocks.tnt;

            if (p_149841_6_.nextInt(p_149841_7_ + 10) < 5
                && !p_149841_1_.canLightningStrikeAt(p_149841_2_, p_149841_3_, p_149841_4_)) {
                int k1 = p_149841_7_ + p_149841_6_.nextInt(5) / 4;

                if (k1 > 15) {
                    k1 = 15;
                }

                p_149841_1_.setBlock(p_149841_2_, p_149841_3_, p_149841_4_, Blocks.fire, k1, 3);
            } else {
                p_149841_1_.setBlockToAir(p_149841_2_, p_149841_3_, p_149841_4_);
            }

            if (flag) {
                Blocks.tnt.onBlockDestroyedByPlayer(p_149841_1_, p_149841_2_, p_149841_3_, p_149841_4_, 1);
            }
        }
    }

    /**
     * Gets the highest chance of a neighbor block encouraging this block to catch fire
     */
    @Unique
    private int enviroMine$getChanceOfNeighborsEncouragingFire(World p_149845_1_, int p_149845_2_, int p_149845_3_,
        int p_149845_4_) {
        byte b0 = 0;

        if (!p_149845_1_.isAirBlock(p_149845_2_, p_149845_3_, p_149845_4_)) {
            return 0;
        } else {
            int l = b0;
            l = this
                .enviroMine$getChanceToEncourageFire(p_149845_1_, p_149845_2_ + 1, p_149845_3_, p_149845_4_, l, WEST);
            l = this
                .enviroMine$getChanceToEncourageFire(p_149845_1_, p_149845_2_ - 1, p_149845_3_, p_149845_4_, l, EAST);
            l = this.enviroMine$getChanceToEncourageFire(p_149845_1_, p_149845_2_, p_149845_3_ - 1, p_149845_4_, l, UP);
            l = this
                .enviroMine$getChanceToEncourageFire(p_149845_1_, p_149845_2_, p_149845_3_ + 1, p_149845_4_, l, DOWN);
            l = this
                .enviroMine$getChanceToEncourageFire(p_149845_1_, p_149845_2_, p_149845_3_, p_149845_4_ - 1, l, SOUTH);
            l = this
                .enviroMine$getChanceToEncourageFire(p_149845_1_, p_149845_2_, p_149845_3_, p_149845_4_ + 1, l, NORTH);
            return l;
        }
    }

    /**
     * Side sensitive version that calls the block function.
     *
     * @param world     The current world
     * @param x         X Position
     * @param y         Y Position
     * @param z         Z Position
     * @param oldChance The previous maximum chance.
     * @param face      The side the fire is coming from
     * @return The chance of the block catching fire, or oldChance if it is higher
     */
    @Unique
    public int enviroMine$getChanceToEncourageFire(IBlockAccess world, int x, int y, int z, int oldChance,
        ForgeDirection face) {
        int newChance = world.getBlock(x, y, z)
            .getFireSpreadSpeed(world, x, y, z, face);
        return (Math.max(newChance, oldChance));
    }
}
