package enviromine.mixins.late.hbm.waterCompat;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.hbm.explosion.ExplosionThermo;

import enviromine.blocks.water.BlockEnviroMineWater;

@Mixin(value = ExplosionThermo.class, priority = 999)
public class MixinExplosionThermo {

    @Inject(method = "freezeDest", at = @At(value = "HEAD"), remap = false)
    private static void freezeDest(World world, int x, int y, int z, CallbackInfo ci) {
        // TODO: rad ice
        Block block = world.getBlock(x, y, z);
        if (block instanceof BlockEnviroMineWater) {
            world.setBlock(x, y, z, Blocks.ice);
        }
    }

}
