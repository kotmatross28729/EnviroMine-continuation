package enviromine.handlers.compat;

import net.minecraft.block.Block;

import thaumcraft.common.blocks.BlockMagicalLeaves;

public class EM_PhysManager_TC {

    public static boolean checkLeaves(Block block) {
        return block instanceof BlockMagicalLeaves;
    }
}
