package enviromine.mixins.late.hbm.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.render.tileentity.RenderFluidTank;

import enviromine.utils.misc.WaterUtilsCompat;

@Mixin(value = RenderFluidTank.class, priority = 999)
public class MixinRenderFluidTank {

    @Inject(method = "getTextureFromType", at = @At(value = "HEAD"), remap = false, cancellable = true)
    public void getTextureFromType(FluidType type, CallbackInfoReturnable<String> cir) {
        if (WaterUtilsCompat.isEnviromineType(type.getName())) { // Bypass customFluid tank_NONE
            cir.setReturnValue("textures/models/tank/tank_" + type.getName() + ".png");
        }
    }
}
