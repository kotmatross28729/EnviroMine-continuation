package enviromine.blocks.water.compat;

import com.hbm.dim.trait.CBT_Atmosphere;
import com.hbm.handler.atmosphere.ChunkAtmosphereManager;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class BlockEnviroMineWater_NTM_SPACE {
	public static boolean TEST(World world, int x, int y, int z, int tickRate) {
		if (!world.isRemote) {
			Block block = world.getBlock(x, y, z);
			CBT_Atmosphere atmosphere = ChunkAtmosphereManager.proxy.getAtmosphere(world, x, y, z);
			if (block != null) {
				world.scheduleBlockUpdate(x, y, z, block, tickRate);
				
				if (!ChunkAtmosphereManager.proxy.hasLiquidPressure(atmosphere)) {
					world.setBlockToAir(x, y, z);
					return true;
				}
			}
		}
		return false;
	}

}
