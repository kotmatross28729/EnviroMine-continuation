package enviromine.trackers.properties.compat;

import DelirusCrux.Netherlicious.Common.BlockItemUtility.ModBlocks;
import enviromine.handlers.ObjectHandler;
import enviromine.handlers.compat.ObjectHandler_Netherlicious;
import enviromine.trackers.properties.StabilityType;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;

public class BlockProperties_Netherlicious {
	
	
	public static void registerNetherliciousTorches(Configuration config, String category, String[] BPName, Block block, StabilityType defStability) {
		if(block == ModBlocks.SoulTorch || block == ModBlocks.FoxfireTorch || block == ModBlocks.ShadowTorch) {
			config.get(category, BPName[0], Block.blockRegistry.getNameForObject(block)).getString();
			config.get(category, BPName[1], -1).getInt(-1);
			config.get(category, BPName[2], "").getString();
			config.get(category, BPName[3], -1).getInt(-1);
			config.get(category, BPName[4], -1).getInt(-1);
			config.get(category, BPName[5], true).getBoolean(true);
			config.get(category, BPName[6], 75.0D).getDouble(75.0D);
			config.get(category, BPName[7], -0.25D).getDouble(-0.25D);
			config.get(category, BPName[8], 0.0D).getDouble(0.0D);
			config.get(category, BPName[9], defStability.name).getString();
			config.get(category, BPName[10], false).getBoolean(false);
			config.get(category, BPName[11], false).getBoolean(false);
			
			config.get(category, BPName[12], true).getBoolean(true);
			config.get(category, BPName[13], true).getBoolean(true);
			config.get(category, BPName[14], 10000).getInt(10000);
			config.get(category, BPName[15], Block.blockRegistry.getNameForObject(ObjectHandler.offTorch)).getString();
			config.get(category, BPName[16], -1).getInt(-1);
		} else if(block == ModBlocks.TorchBone || block == ModBlocks.SoulTorchBone || block == ModBlocks.FoxfireTorchBone || block == ModBlocks.ShadowTorchBone) {
			config.get(category, BPName[0], Block.blockRegistry.getNameForObject(block)).getString();
			config.get(category, BPName[1], -1).getInt(-1);
			config.get(category, BPName[2], "").getString();
			config.get(category, BPName[3], -1).getInt(-1);
			config.get(category, BPName[4], -1).getInt(-1);
			config.get(category, BPName[5], true).getBoolean(true);
			config.get(category, BPName[6], 75.0D).getDouble(75.0D);
			config.get(category, BPName[7], -0.25D).getDouble(-0.25D);
			config.get(category, BPName[8], 0.0D).getDouble(0.0D);
			config.get(category, BPName[9], defStability.name).getString();
			config.get(category, BPName[10], false).getBoolean(false);
			config.get(category, BPName[11], false).getBoolean(false);
			
			config.get(category, BPName[12], true).getBoolean(true);
			config.get(category, BPName[13], true).getBoolean(true);
			config.get(category, BPName[14], 10000).getInt(10000);
			config.get(category, BPName[15], Block.blockRegistry.getNameForObject(ObjectHandler_Netherlicious.offTorchBone)).getString();
			config.get(category, BPName[16], -1).getInt(-1);
		}
	}
	
}
