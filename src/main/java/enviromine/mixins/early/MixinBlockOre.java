package enviromine.mixins.early;

import enviromine.blocks.tiles.TileEntityGas;
import enviromine.core.EM_Settings;
import enviromine.gases.EnviroGasDictionary;
import enviromine.handlers.EM_PhysManager;
import enviromine.handlers.ObjectHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockOre.class, priority = 1003)
public class MixinBlockOre extends Block {
	
	protected MixinBlockOre(Material materialIn) {
		super(materialIn);
	}
	
	public void harvestBlock(World world, EntityPlayer player, int i, int j, int k, int meta) {
		super.harvestBlock(world, player, i, j, k, meta);
		if(EM_Settings.flammableCoal && this == Blocks.coal_ore) {
			if (world.rand.nextBoolean()) {
				world.setBlock(i, j, k, ObjectHandler.gasBlock);
				TileEntity tile = world.getTileEntity(i, j, k);
				
				if (tile != null && tile instanceof TileEntityGas gasTile) {
					gasTile.addGas(EnviroGasDictionary.carbonDioxide.gasID, 1 + world.rand.nextInt(5));
					gasTile.addGas(EnviroGasDictionary.methane.gasID, 1 + world.rand.nextInt(5));
				}
			}
		}
	}
	
	public void onNeighborBlockChange(World world, int x, int y, int z, Block nBlock) {
		if(EM_Settings.flammableCoal && this == Blocks.coal_ore) {
			if (world.scheduledUpdatesAreImmediate) {
				return;
			} else {
				if (world.getTotalWorldTime() < EM_PhysManager.worldStartTime + EM_Settings.worldDelay) {
					return;
				} else if (EM_PhysManager.chunkDelay.containsKey(world.provider.dimensionId + "" + (x >> 4) + "," + (z >> 4))) {
					if (EM_PhysManager.chunkDelay.get(world.provider.dimensionId + "" + (x >> 4) + "," + (z >> 4)) > world.getTotalWorldTime()) {
						return;
					}
				}
			}
			
			for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
				int xOff = ForgeDirection.VALID_DIRECTIONS[i].offsetX + x;
				int yOff = ForgeDirection.VALID_DIRECTIONS[i].offsetY + y;
				int zOff = ForgeDirection.VALID_DIRECTIONS[i].offsetZ + z;
				Block block = world.getBlock(xOff, yOff, zOff);
				int meta = world.getBlockMetadata(xOff, yOff, zOff);
				
				if (ObjectHandler.igniteList.containsKey(block) && (ObjectHandler.igniteList.get(block).isEmpty() || ObjectHandler.igniteList.get(block).contains(meta))) {
					world.setBlock(x, y, z, ObjectHandler.burningCoal, 0, 3);
					return;
				}
			}
		}
	}
}
