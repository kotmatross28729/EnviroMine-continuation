package enviromine.mixins.late.hbm;

import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.LiquefactionRecipes;
import enviromine.handlers.ObjectHandler;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LiquefactionRecipes.class, priority = 999)
public class MixinLiquefactionRecipes {

	@Inject(method = "getOutput", at = @At(value = "HEAD"), cancellable = true, remap = false)
	private static void getOutput(ItemStack stack, CallbackInfoReturnable<FluidStack> cir) {
		if(stack != null && stack.getItem() != null && stack.getItem() == ObjectHandler.rottenFood) {
			//TODO config?
			cir.setReturnValue(new FluidStack(50, Fluids.SALIENT));
		}
	}
	
}
