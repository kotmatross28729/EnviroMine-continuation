package enviromine.blocks.tiles;

import enviromine.utils.WaterUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCauldron extends TileEntity {
	private WaterUtils.WATER_TYPES waterType = WaterUtils.WATER_TYPES.CLEAN;
	
	public WaterUtils.WATER_TYPES getWaterType() {
		return waterType;
	}
	
	public void setWaterType(WaterUtils.WATER_TYPES type) {
		this.waterType = type;
		this.markDirty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.waterType = WaterUtils.WATER_TYPES.values()[nbt.getInteger("WaterType")];
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("WaterType", this.waterType.ordinal());
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
}
