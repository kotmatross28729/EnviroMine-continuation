package enviromine.blocks;

import net.minecraft.block.BlockTorch;

/**
 * A fake torch block that mimics the behavior of the vanilla torch
 * (extinguishing, fire spread) as configured in EM_Settings.
 * This block uses the same texture and name as the vanilla torch,
 * but has a different registry name.
 */
public class BlockFakeTorch extends BlockTorch {
    public BlockFakeTorch() {
        super(); // Access the protected constructor of BlockTorch
    }
}
