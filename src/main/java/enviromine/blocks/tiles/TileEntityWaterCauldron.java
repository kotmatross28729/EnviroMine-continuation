package enviromine.blocks.tiles;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityWaterCauldron extends TileEntity {
	
	public Fluid water;
	public int ammount;
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		this.water = FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("water")).getFluid();
		this.ammount = FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("water")).amount;
		
		if (water == null) {
			System.err.println("Cauldron @ " + xCoord + " " + yCoord + " " + zCoord + " had an invalid FluidStack. Resetting to a normal cauldron.");
			resetCauldron();
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (water != null) {
			FluidStack stack = new FluidStack(water, ammount);
			
			compound.setTag("water", stack.writeToNBT(new NBTTagCompound()));
		}
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g()); // getNbtCompound
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		
		this.writeToNBT(nbt);
		
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbt);
	}
	
	@Override
	public boolean canUpdate() {
		return false;
	}
	
	protected void resetCauldron() {
		worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.cauldron, 0, 3);
	}
	
}
