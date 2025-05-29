package enviromine.mixins.late.hbm.waterCompat;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.hbm.blocks.bomb.Landmine;
import com.llamalad7.mixinextras.sugar.Local;

import enviromine.blocks.water.BlockEnviroMineWater;

@Mixin(value = Landmine.class, priority = 999)
public class MixinLandmine {

    @Inject(
        method = "isWaterAbove",
        at = @At(
            value = "FIELD",
            target = "net/minecraft/init/Blocks.field_150355_j : Lnet/minecraft/block/Block;",
            shift = At.Shift.AFTER),
        cancellable = true,
        remap = false)
    public void isWaterAbove(World world, int x, int y, int z, CallbackInfoReturnable<Boolean> cir,
        @Local Block blockAbove) {
        if (blockAbove instanceof BlockEnviroMineWater) {
            cir.setReturnValue(true);
        }
    }

}
