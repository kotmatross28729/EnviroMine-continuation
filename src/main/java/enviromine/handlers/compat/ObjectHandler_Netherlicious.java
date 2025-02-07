package enviromine.handlers.compat;

import cpw.mods.fml.common.registry.GameRegistry;
import enviromine.blocks.compat.BlockOffTorch_Bone_Netherlicious;
import enviromine.core.EnviroMine;
import net.minecraft.block.Block;

public class ObjectHandler_Netherlicious {
	public static Block offTorchBone;
	public static void initBlocks() {
		offTorchBone = new BlockOffTorch_Bone_Netherlicious().setTickRandomly(false).setBlockName("torch").setBlockTextureName("enviromine:torch_off_bone").setLightLevel(0F).setCreativeTab(EnviroMine.enviroTab);
	}
	public static void registerBlocks() {
		GameRegistry.registerBlock(offTorchBone, "offTorchBone");
	}
}
