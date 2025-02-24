package enviromine.blocks.water;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.utils.WaterUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockEnviroMineWater extends BlockFluidClassic {
	private IIcon stillWater;
	private IIcon flowingWater;
	public BlockEnviroMineWater(Fluid fluid, Material material) {
		super(fluid, material);
	}
	
	//No, I DON'T want blue water everywhere, thanks
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z) {
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		
		for (int k1 = -1; k1 <= 1; ++k1) {
			for (int l1 = -1; l1 <= 1; ++l1) {
				int i2 = worldIn.getBiomeGenForCoords(x + l1, z + k1).getWaterColorMultiplier();
				l += (i2 & 16711680) >> 16;
				i1 += (i2 & 65280) >> 8;
				j1 += i2 & 255;
			}
		}
		
		return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int side, final int meta) {
		return (side == 0 || side == 1) ? this.stillWater : this.flowingWater;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.stillWater = register.registerIcon("water_still");
		this.flowingWater = register.registerIcon("water_flow");
	}
	
	public WaterUtils.WATER_TYPES getType(Fluid fluid) {
		
		switch (fluid.getName()) {
			case "radioactive_frosty_water" -> {return WaterUtils.WATER_TYPES.RADIOACTIVE_FROSTY;}
			case "frosty_water" -> {return WaterUtils.WATER_TYPES.FROSTY;}
					
			case "radioactive_cold_water" -> {return WaterUtils.WATER_TYPES.RADIOACTIVE_COLD;}
			case "dirty_cold_water" -> {return WaterUtils.WATER_TYPES.DIRTY_COLD;}
			case "salty_cold_water" -> {return WaterUtils.WATER_TYPES.SALTY_COLD;}
			case "clean_cold_water" -> {return WaterUtils.WATER_TYPES.CLEAN_COLD;}
					
			case "radioactive_water" -> {return WaterUtils.WATER_TYPES.RADIOACTIVE;}
			case "dirty_water" -> {return WaterUtils.WATER_TYPES.DIRTY;}
			case "salty_water" -> {return WaterUtils.WATER_TYPES.SALTY;}
					
			case "radioactive_warm_water" -> {return WaterUtils.WATER_TYPES.RADIOACTIVE_WARM;}
			case "dirty_warm_water" -> {return WaterUtils.WATER_TYPES.DIRTY_WARM;}
			case "salty_warm_water" -> {return WaterUtils.WATER_TYPES.SALTY_WARM;}
			case "clean_warm_water" -> {return WaterUtils.WATER_TYPES.CLEAN_WARM;}
					
			case "radioactive_hot_water" -> {return WaterUtils.WATER_TYPES.RADIOACTIVE_HOT;}
			case "hot_water" -> {return WaterUtils.WATER_TYPES.HOT;}
			
			default -> {return WaterUtils.WATER_TYPES.CLEAN;}
		}
	}
	
	
}
