package enviromine.blocks.water;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockEnviroMineWater extends BlockFluidClassic {
//	private IIcon stillWater;
//	private IIcon flowingWater;
	
	public BlockEnviroMineWater(Fluid fluid, Material material) {
		super(fluid, material);
	}
	
//	@SideOnly(Side.CLIENT)
//	public IIcon getIcon(final int side, final int meta) {
//		return (side == 0 || side == 1) ? this.stillWater : this.flowingWater;
//	}
//	
//	@SideOnly(Side.CLIENT)
//	public void registerBlockIcons(IIconRegister register) {
//		this.stillWater = register.registerIcon("WaterStill");
//		this.flowingWater = register.registerIcon("WaterFlow");
//	}
	
}
