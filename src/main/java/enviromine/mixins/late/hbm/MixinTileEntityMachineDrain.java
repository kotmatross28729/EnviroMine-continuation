package enviromine.mixins.late.hbm;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.hbm.handler.radiation.ChunkRadiationManager;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_VentRadiation;
import com.hbm.tileentity.IBufPacketReceiver;
import com.hbm.tileentity.IFluidCopiable;
import com.hbm.tileentity.TileEntityLoadedBase;
import com.hbm.tileentity.machine.TileEntityMachineDrain;
import com.llamalad7.mixinextras.sugar.Local;

import api.hbm.fluid.IFluidStandardReceiver;
import enviromine.core.EM_Settings;

@Mixin(value = TileEntityMachineDrain.class, priority = 999)
public abstract class MixinTileEntityMachineDrain extends TileEntityLoadedBase
    implements IFluidStandardReceiver, IBufPacketReceiver, IFluidCopiable {

    // Because someone forgot that this is how it actually works.

    @Shadow
    public FluidTank tank;

    @Inject(
        method = "func_145845_h",
        at = @At(
            value = "INVOKE",
            target = "Lcom/hbm/inventory/fluid/trait/FT_Polluting;pollute(Lnet/minecraft/world/World;IIILcom/hbm/inventory/fluid/FluidType;Lcom/hbm/inventory/fluid/trait/FluidTrait$FluidReleaseType;F)V",
            shift = At.Shift.BEFORE),
        remap = false)

    public void func_145845_h(CallbackInfo ci, @Local int toSpill) {
        FT_VentRadiation trait = this.tank.getTankType()
            .getTrait(FT_VentRadiation.class);
        if (trait == null) return;

        ChunkRadiationManager.proxy.incrementRad(
            worldObj,
            xCoord,
            yCoord,
            zCoord,
            ((float) (toSpill * trait.getRadPerMB() / EM_Settings.drainagePipeRadiationDivisor)));
    }

}
