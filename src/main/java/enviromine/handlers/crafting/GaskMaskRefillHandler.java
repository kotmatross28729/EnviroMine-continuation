package enviromine.handlers.crafting;

import java.util.ArrayList;

import enviromine.core.EM_Settings;
import enviromine.handlers.ObjectHandler;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class GaskMaskRefillHandler implements IRecipe
{
	public boolean isArmor;
	public int maskFill;
	public int maskMax;
	public ArrayList<ItemStack> filters = new ArrayList<ItemStack>();
	public ItemStack mask;
	public int filterFill = EM_Settings.filterRestore;
	public ItemStack output;
	
	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		if (!inv.getInventoryName().equals("container.crafting"))
		{
			return false;
		}
		
		this.output = null;
		this.isArmor = false;
		this.maskFill = 0;
		this.maskMax = EM_Settings.gasMaskMax;
		this.mask = null;
		this.filters.clear();
		
		for (int i = inv.getSizeInventory() - 1; i >= 0; i--)
		{
			ItemStack item = inv.getStackInSlot(i);
			
			if (item == null)
			{
				continue;
			} else if (item.hasTagCompound() && item.getTagCompound().hasKey(EM_Settings.GAS_MASK_FILL_TAG_KEY))
			{
				if (mask != null)
				{
					return false;
				} else
				{
					mask = item.copy();
					maskFill = item.getTagCompound().getInteger(EM_Settings.GAS_MASK_FILL_TAG_KEY);
					maskMax = (mask.getTagCompound().hasKey(EM_Settings.GAS_MASK_MAX_TAG_KEY)? mask.getTagCompound().getInteger(EM_Settings.GAS_MASK_MAX_TAG_KEY) : EM_Settings.gasMaskMax);
				}
			} else if (item.getItem() == ObjectHandler.airFilter)
			{
					filters.add(item);
			}else if (item != null)
			{
				return false;
			}
		}
		
		if (mask == null || maskFill >= maskMax)
		{
			return false;
		} else if (maskFill + (filters.size() * filterFill) >= maskMax + filterFill)
		{
			return false;
		}
	    else if(mask != null && filters.size() >= 1)
		{
			output = mask.copy();
			if ((maskFill +(filters.size() * filterFill)) <= maskMax)
			{
					output.getTagCompound().setInteger(EM_Settings.GAS_MASK_FILL_TAG_KEY, (maskFill + (filters.size() * filterFill)));
			} else
			{
					output.getTagCompound().setInteger(EM_Settings.GAS_MASK_FILL_TAG_KEY, maskMax);
			}
			
			return true;
		} else
		{
			return false;
		}
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		return output;
	}
	
	
	@Override
	public int getRecipeSize()
	{
		return 4;
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return output;
	}
	
	// This is completely redundant...
/*	@SubscribeEvent
	public void onCrafting(PlayerEvent.ItemCraftedEvent event)
	{
		
		IInventory craftMatrix = event.craftMatrix;
		if (!(craftMatrix instanceof InventoryCrafting))
		{
			return;
		}
		
		if (this.matches((InventoryCrafting)craftMatrix, event.player.worldObj)) {
			if (!craftMatrix.getInventoryName().equals("container.crafting"))
			{
				return;
			} else
			{
				for (int i = craftMatrix.getSizeInventory() - 1; i >= 0; i--)
				{
					ItemStack slot = craftMatrix.getStackInSlot(i);
					
					if (slot == null)
					{
						continue;
					}else if (slot.hasTagCompound() && slot.getTagCompound().hasKey(EM_Settings.GAS_MASK_FILL_TAG_KEY))
					{
						slot.stackSize -= 1;
					}
				}
			}
		}
	}*/
}