package enviromine.mixins.late.netherlicious;

import java.util.Random;

import net.minecraft.block.BlockTorch;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import DelirusCrux.Netherlicious.Common.Blocks.TorchSoul;

@Mixin(value = TorchSoul.class, priority = 1003)
public class MixinTorchSoul extends BlockTorch {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void updateTick(World world, int x, int y, int z, Random rand) {
        super.updateTick(world, x, y, z, rand);
    }
}
