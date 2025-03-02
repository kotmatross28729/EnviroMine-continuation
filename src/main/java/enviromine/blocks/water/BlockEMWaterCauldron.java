package enviromine.blocks.water;

import com.hbm.util.ContaminationUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.blocks.tiles.TileEntityWaterCauldron;
import enviromine.utils.WaterUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockEMWaterCauldron extends BlockContainer {
	
	public BlockEMWaterCauldron() {
		super(Material.iron);
		this.setStepSound(Blocks.cauldron.stepSound);
		this.setHardness(2);
		this.setResistance(2);
		this.setBlockName("EM_water_cauldron");
	}
	public Fluid containedFluid;
	
	public void setContainedFluid(Fluid fluid) {
		this.containedFluid = fluid;
	}
	
	public Fluid getContainedFluid() {
		return this.containedFluid;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
		return super.onBlockActivated(worldIn, x, y, z, player, side, subX, subY, subZ);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		int l = func_150027_b(world.getBlockMetadata(x, y, z));
		float f = (float)y + (6.0F + (float)(3 * l)) / 16.0F;
		
		if (!world.isRemote && l > 0 && entity.boundingBox.minY <= (double)f) {
			Block block = world.getBlock(x, y, z);
			if (block instanceof BlockEMWaterCauldron cauldron) {
				if (WaterUtils.getTypeFromFluid(cauldron.getContainedFluid()).isRadioactive) {
					if (entity instanceof EntityLivingBase entityLivingBase) {
						ContaminationUtil.contaminate(entityLivingBase, ContaminationUtil.HazardType.RADIATION, ContaminationUtil.ContaminationType.CREATIVE, 5F);
					}
				}
			}
		}
	}
	
	public static int func_150027_b(int meta) {
		return meta;
	}
	
	@Override
	public int tickRate(World world) {
		return 0;
	}
	
	public IIcon grayscaleWaterIcon() {
		return blockIcon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon("water_still");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityWaterCauldron();
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return Blocks.cauldron.getIcon(side, meta);
	}
}
