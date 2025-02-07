package enviromine.trackers.properties.compat;

import enviromine.handlers.ObjectHandler;
import enviromine.trackers.properties.StabilityType;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import thaumcraft.common.blocks.BlockMagicalLeaves;

public class BlockProperties_TC {
	public static void registerTCLeaves(Configuration config, String category, String[] BPName, Block block, StabilityType defStability) {
		if(block instanceof BlockMagicalLeaves) {
			config.get(category, BPName[0], Block.blockRegistry.getNameForObject(block)).getString();
			config.get(category, BPName[1], -1).getInt(-1);
			config.get(category, BPName[2], block == Blocks.grass ? Block.blockRegistry.getNameForObject(Blocks.dirt) : "").getString();
			config.get(category, BPName[3], -1).getInt(-1);
			config.get(category, BPName[4], -1).getInt(-1);
			config.get(category, BPName[5], false).getBoolean(false);
			config.get(category, BPName[6], 0.0D).getDouble(0.0D);
			config.get(category, BPName[7], 1.0D).getDouble(1.0D);
			config.get(category, BPName[8], 0.1D).getDouble(0.1D);
			config.get(category, BPName[9], defStability.name).getString();
			config.get(category, BPName[10], false).getBoolean(false);
			config.get(category, BPName[11], false).getBoolean(false);
			config.get(category, BPName[12], false).getBoolean(false);
			config.get(category, BPName[13], false).getBoolean(false);
			config.get(category, BPName[14], 10000).getInt(10000);
			config.get(category, BPName[15], Block.blockRegistry.getNameForObject(ObjectHandler.offTorch)).getString();
		}
	}
}
