package enviromine.mixins.late.hbm.waterCompat;

import org.spongepowered.asm.mixin.Mixin;

import com.hbm.tileentity.machine.TileEntityWasteDrum;

@Mixin(value = TileEntityWasteDrum.class, priority = 999)
public class MixinTileEntityWasteDrum {

}
