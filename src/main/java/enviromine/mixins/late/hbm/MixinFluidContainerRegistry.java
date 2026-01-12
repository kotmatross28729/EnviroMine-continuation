package enviromine.mixins.late.hbm;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.hbm.inventory.FluidContainerRegistry;

import enviromine.handlers.ObjectHandlerCompat;

@Mixin(value = FluidContainerRegistry.class, priority = 999)
public class MixinFluidContainerRegistry {

    @Inject(method = "register", at = @At(value = "TAIL"), remap = false)
    private static void register(CallbackInfo ci) {
        ObjectHandlerCompat.registerNTMFluidContainers(); /// Soz Bob
    }

}
