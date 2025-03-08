package enviromine.items;

import enviromine.blocks.water.BlockEnviroMineWater;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.world.World;

public class ItemModBucket extends ItemBucket {
	protected int overrideFluidMeta = 0;
	public Block containedFluid;
	
	public ItemModBucket(Block fluid) {
		super(fluid);
		this.containedFluid = fluid;
		this.maxStackSize = 1;
	}
	
	public ItemModBucket(Block fluid, int meta) {
		this(fluid);
		this.overrideFluidMeta = meta;
		this.maxStackSize = 1;
	}
	
	@Override
	public boolean tryPlaceContainedLiquid(World world, int x, int y, int z) {
		
		if(this.containedFluid == Blocks.air) {
			return false;
		} else {
			Material material = world.getBlock(x, y, z).getMaterial();
			boolean flag = !material.isSolid();
			
			if(!world.isAirBlock(x, y, z) && !flag) {
				return false;
			} else {
				if(world.provider.isHellWorld && (this.containedFluid == Blocks.flowing_water || this.containedFluid instanceof BlockEnviroMineWater)) {
					world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), "random.fizz", 0.5F,
							2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
					
					for(int l = 0; l < 8; ++l) {
						world.spawnParticle("largesmoke", (double) x + Math.random(), (double) y + Math.random(), (double) z + Math.random(), 0.0D, 0.0D, 0.0D);
					}
				} else {
					if(!world.isRemote && flag && !material.isLiquid()) {
						world.func_147480_a(x, y, z, true);
					}
					
					world.setBlock(x, y, z, this.containedFluid, overrideFluidMeta, 3);
				}
				
				return true;
			}
		}
	}
}
