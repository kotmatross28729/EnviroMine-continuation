package enviromine.mixins.late.hbm.waterCompat;

import org.spongepowered.asm.mixin.Mixin;

import com.hbm.blocks.bomb.Landmine;

@Mixin(value = Landmine.class, priority = 999)
public class MixinLandmine {
}
