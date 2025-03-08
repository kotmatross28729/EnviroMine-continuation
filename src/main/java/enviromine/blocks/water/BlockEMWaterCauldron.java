package enviromine.blocks.water;

import com.hbm.util.ContaminationUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.blocks.tiles.TileEntityWaterCauldron;
import enviromine.items.ItemModBucket;
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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BlockEMWaterCauldron extends BlockContainer {
	
	//This is the most confusing code I have ever written.
	//I already hate what I'm doing, I just want to tear it all down and start over
	
	public BlockEMWaterCauldron() {
		super(Material.iron);
		this.setStepSound(Blocks.cauldron.stepSound);
		this.setHardness(2);
		this.setResistance(2);
		this.setBlockName("EM_water_cauldron");
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
		if (worldIn.isRemote) {
			return true;
		}
		else {
			ItemStack stack = player.getHeldItem();
			if (stack != null) {
				Item item = stack.getItem();
				if (item instanceof ItemModBucket bucket && bucket.containedFluid instanceof BlockEnviroMineWater water) {
					TileEntity te = worldIn.getTileEntity(x, y, z);
					if (te instanceof TileEntityWaterCauldron cauldron) {
						cauldron.fill(null, new FluidStack(water.getFluid(), 1000), true);
						if (!player.capabilities.isCreativeMode) {
							ItemStack newStack = new ItemStack(Items.bucket);
							newStack.stackSize = 1;
							newStack.setItemDamage(0);
							player.setCurrentItemOrArmor(0, newStack);
						}
					}
				}
			}
			
			return super.onBlockActivated(worldIn, x, y, z, player, side, subX, subY, subZ);
		}
	}
	
	public void func_150024_a(World worldIn, int x, int y, int z, int level) {
		worldIn.setBlockMetadataWithNotify(x, y, z, MathHelper.clamp_int(level, 0, 3), 2);
		worldIn.func_147453_f(x, y, z, this);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileEntityWaterCauldron cauldron) {
			FluidStack fluid = cauldron.getTank().getFluid();
			float fluidHeight = (6.0F + (float)(3 * getFluidLevel(cauldron))) / 16.0F;
			
			if(!world.isRemote && fluid != null && entity.boundingBox.minY <= (y + fluidHeight)) {
				if(WaterUtils.getTypeFromFluid(fluid.getFluid()).isRadioactive) {
					if(entity instanceof EntityLivingBase entityLivingBase) {
						ContaminationUtil.contaminate(entityLivingBase, ContaminationUtil.HazardType.RADIATION, ContaminationUtil.ContaminationType.CREATIVE, 5F);
					}
				}
			}
		}
	}
	private int getFluidLevel(TileEntityWaterCauldron cauldron) {
		return (int) Math.ceil((cauldron.getTank().getFluidAmount() / 1000.0F) * 3.0F);
	}
	
	public FluidStack getContainedFluid(IBlockAccess world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		return te instanceof TileEntityWaterCauldron ?
				((TileEntityWaterCauldron)te).getTank().getFluid() : null;
	}
	
	public static int func_150027_b(int meta) {
		return meta;
	}
	
	@Override
	public int tickRate(World world) {
		return 0;
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
