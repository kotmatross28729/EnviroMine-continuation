package enviromine.mixins.early.waterCompat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLilyPad;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import enviromine.utils.WaterUtils;

@Mixin(value = BlockLilyPad.class, priority = 1003)
public class MixinBlockLilyPad {

    @Inject(method = "canPlaceBlockOn", at = @At(value = "HEAD"), cancellable = true)
    public void canPlaceBlockOn(Block ground, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(WaterUtils.isWater(ground, false));
    }

}
