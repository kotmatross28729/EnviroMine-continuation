package enviromine.mixins.late.hbm.waterCompat;

import org.spongepowered.asm.mixin.Mixin;

import com.hbm.explosion.ExplosionThermo;

@Mixin(value = ExplosionThermo.class, priority = 999)
public class MixinExplosionThermo {
}
