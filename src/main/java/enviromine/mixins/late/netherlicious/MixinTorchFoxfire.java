package enviromine.mixins.late.netherlicious;

import DelirusCrux.Netherlicious.Common.Blocks.TorchFoxfire;
import net.minecraft.block.BlockTorch;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(value = TorchFoxfire.class, priority = 1003)
public class MixinTorchFoxfire extends BlockTorch {
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
