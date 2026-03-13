package enviromine.mixins.late.hbm.waterCompat;

import net.minecraft.block.Block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.tileentity.machine.TileEntityReactorResearch;

import enviromine.blocks.water.BlockEnviroMineWater;

@Mixin(value = TileEntityReactorResearch.class, priority = 999)
public abstract class MixinTileEntityReactorResearch extends TileEntityMachineBase {

    public MixinTileEntityReactorResearch(int slotCount) {
        super(slotCount);
    }

    @Inject(method = "blocksRad", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void blocksRad(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        Block b = worldObj.getBlock(x, y, z);
        if (b instanceof BlockEnviroMineWater && worldObj.getBlockMetadata(x, y, z) == 0) cir.setReturnValue(true);
    }

}
