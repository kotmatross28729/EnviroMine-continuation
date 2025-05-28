package enviromine.mixins.late.hbm.waterCompat;

import org.spongepowered.asm.mixin.Mixin;

import com.hbm.tileentity.machine.TileEntityReactorResearch;

@Mixin(value = TileEntityReactorResearch.class, priority = 999)
public class MixinTileEntityReactorResearch {
}
