package enviromine.mixins.late.hbm.waterCompat;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.hbm.tileentity.machine.TileEntityWasteDrum;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

import enviromine.blocks.water.BlockEnviroMineWater;

@Mixin(value = TileEntityWasteDrum.class, priority = 999)
public class MixinTileEntityWasteDrum extends TileEntity {

    @Inject(
        method = "func_145845_h",
        at = @At(
            value = "FIELD",
            target = "net/minecraftforge/common/util/ForgeDirection.VALID_DIRECTIONS : [Lnet/minecraftforge/common/util/ForgeDirection;"),
        remap = false)
    public void updateEntity(CallbackInfo ci, @Local LocalIntRef water) {
        if (!worldObj.isRemote) {
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                if (worldObj.getBlock(
                    xCoord + dir.offsetX,
                    yCoord + dir.offsetY,
                    zCoord + dir.offsetZ) instanceof BlockEnviroMineWater) {
                    water.set(water.get() + 1);
                }
            }
        }
    }

}
