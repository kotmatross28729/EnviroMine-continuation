package enviromine.mixins.late.hbm;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.RadiolysisRecipes;
import com.hbm.util.Tuple;
import com.hbm.util.Tuple.Pair;

@Mixin(value = RadiolysisRecipes.class, priority = 999)
public class MixinRadiolysisRecipes {

    @Shadow
    private static Map<FluidType, Tuple.Pair<FluidStack, FluidStack>> radiolysis = new HashMap();

    @Inject(method = "registerRadiolysis", at = @At(value = "HEAD"), remap = false)
    private static void registerRadiolysis(CallbackInfo ci) {
        FluidType RADIOACTIVE_FROSTY_WATER = Fluids.fromName("RADIOACTIVE_FROSTY_WATER");
        FluidType RADIOACTIVE_COLD_WATER = Fluids.fromName("RADIOACTIVE_COLD_WATER");
        FluidType RADIOACTIVE_WATER = Fluids.fromName("RADIOACTIVE_WATER");
        FluidType RADIOACTIVE_WARM_WATER = Fluids.fromName("RADIOACTIVE_WARM_WATER");
        FluidType RADIOACTIVE_HOT_WATER = Fluids.fromName("RADIOACTIVE_HOT_WATER");

        radiolysis
            .put(RADIOACTIVE_FROSTY_WATER, new Pair(new FluidStack(100, Fluids.WATER), new FluidStack(0, Fluids.NONE)));
        radiolysis
            .put(RADIOACTIVE_COLD_WATER, new Pair(new FluidStack(100, Fluids.WATER), new FluidStack(0, Fluids.NONE)));
        radiolysis.put(RADIOACTIVE_WATER, new Pair(new FluidStack(100, Fluids.WATER), new FluidStack(0, Fluids.NONE)));
        radiolysis
            .put(RADIOACTIVE_WARM_WATER, new Pair(new FluidStack(100, Fluids.WATER), new FluidStack(0, Fluids.NONE)));
        radiolysis
            .put(RADIOACTIVE_HOT_WATER, new Pair(new FluidStack(100, Fluids.WATER), new FluidStack(0, Fluids.NONE)));
    }
}
