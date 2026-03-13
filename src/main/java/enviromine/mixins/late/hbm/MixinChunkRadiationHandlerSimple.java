package enviromine.mixins.late.hbm;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.hbm.blocks.ModBlocks;
import com.hbm.handler.radiation.ChunkRadiationHandlerSimple;

import enviromine.blocks.water.BlockEnviroMineWater;
import enviromine.handlers.ObjectHandler;
import enviromine.utils.WaterUtils;

// @AbsoluteFuckingBullshit™
@Mixin(value = ChunkRadiationHandlerSimple.class, priority = 999)
public class MixinChunkRadiationHandlerSimple {

    @Shadow
    private HashMap<World, ChunkRadiationHandlerSimple.SimpleRadiationPerWorld> perWorld = new HashMap();

    // Please don't fucking use this.
    /**
     * @author kotmatross
     * @reason (my) skill issue
     */
    @Overwrite(remap = false) // FUCKING SHIT
    public void handleWorldDestruction() {

        int count = 10;
        int threshold = 10;
        int chunks = 5;

        // for all worlds
        for (Map.Entry<World, ChunkRadiationHandlerSimple.SimpleRadiationPerWorld> per : perWorld.entrySet()) {

            World world = per.getKey();
            ChunkRadiationHandlerSimple.SimpleRadiationPerWorld list = per.getValue();

            Object[] entries = list.radiation.entrySet()
                .toArray();

            if (entries.length == 0) continue;

            // chose this many random chunks
            for (int c = 0; c < chunks; c++) {

                Map.Entry<ChunkCoordIntPair, Float> randEnt = (Map.Entry<ChunkCoordIntPair, Float>) entries[world.rand
                    .nextInt(entries.length)];

                ChunkCoordIntPair coords = randEnt.getKey();
                WorldServer serv = (WorldServer) world;
                ChunkProviderServer provider = (ChunkProviderServer) serv.getChunkProvider();

                // choose this many random locations within the chunk
                for (int i = 0; i < count; i++) {

                    if (randEnt == null || randEnt.getValue() < threshold) continue;

                    if (provider.chunkExists(coords.chunkXPos, coords.chunkZPos)) {

                        for (int a = 0; a < 16; a++) {
                            for (int b = 0; b < 16; b++) {

                                if (world.rand.nextInt(3) != 0) continue;

                                int x = coords.getCenterXPos() - 8 + a;
                                int z = coords.getCenterZPosition() - 8 + b;
                                int y = world.getHeightValue(x, z) - world.rand.nextInt(2);

                                // FUCKING TPS DESTROYER
                                if (world.getBlock(x, y, z) == Blocks.water
                                    || world.getBlock(x, y, z) == Blocks.flowing_water) {
                                    world.setBlock(
                                        x,
                                        y,
                                        z,
                                        ObjectHandler.block_radioactive_Water,
                                        world.getBlockMetadata(x, y, z),
                                        3);
                                } else if (world.getBlock(x, y, z) instanceof BlockEnviroMineWater EMWater) {
                                    world.setBlock(
                                        x,
                                        y,
                                        z,
                                        WaterUtils.getBlockFromType(
                                            WaterUtils.radiate(WaterUtils.getTypeFromFluid(EMWater.getFluid()))),
                                        world.getBlockMetadata(x, y, z),
                                        3);
                                }

                                if (world.getBlock(x, y, z) == Blocks.grass) {
                                    world.setBlock(x, y, z, ModBlocks.waste_earth);

                                } else if (world.getBlock(x, y, z) == Blocks.tallgrass) {
                                    world.setBlock(x, y, z, Blocks.air);

                                } else if (world.getBlock(x, y, z)
                                    .getMaterial() == Material.leaves
                                    && !(world.getBlock(x, y, z) == ModBlocks.waste_leaves)) {

                                        if (world.rand.nextInt(7) <= 5) {
                                            world.setBlock(x, y, z, ModBlocks.waste_leaves);
                                        } else {
                                            world.setBlock(x, y, z, Blocks.air);
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * @Inject(
     * method = "handleWorldDestruction",
     * at = @At(
     * value = "FIELD",
     * target = "net/minecraft/init/Blocks.field_150349_c : Lnet/minecraft/block/BlockGrass;",
     * ordinal = 0,
     * shift = At.Shift.BEFORE),
     * remap = false)
     * public void handleWorldDestruction(CallbackInfo ci, @Local World world, @Local(ordinal = 7) int x,
     * @Local(ordinal = 9) int y, @Local(ordinal = 8) int z) {
     * if (world.getBlockMetadata(x, y, z) == 0) {
     * if (world.getBlock(x, y, z) == Blocks.water) {
     * world.setBlock(x, y, z, ObjectHandler.block_radioactive_Water);
     * } else if (world.getBlock(x, y, z) instanceof BlockEnviroMineWater EMWater) {
     * world.setBlock(
     * x,
     * y,
     * z,
     * WaterUtils.getBlockFromType(WaterUtils.radiate(WaterUtils.getTypeFromFluid(EMWater.getFluid()))));
     * }
     * }
     * }
     */

}
