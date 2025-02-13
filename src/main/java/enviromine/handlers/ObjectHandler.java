package enviromine.handlers;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import enviromine.EntityPhysicsBlock;
import enviromine.blocks.BlockBurningCoal;
import enviromine.blocks.BlockDavyLamp;
import enviromine.blocks.BlockElevator;
import enviromine.blocks.BlockEsky;
import enviromine.blocks.BlockFreezer;
import enviromine.blocks.BlockGas;
import enviromine.blocks.BlockNoPhysics;
import enviromine.blocks.BlockOffTorch;
import enviromine.blocks.materials.MaterialElevator;
import enviromine.blocks.materials.MaterialGas;
import enviromine.blocks.tiles.TileEntityBurningCoal;
import enviromine.blocks.tiles.TileEntityDavyLamp;
import enviromine.blocks.tiles.TileEntityElevator;
import enviromine.blocks.tiles.TileEntityEsky;
import enviromine.blocks.tiles.TileEntityFreezer;
import enviromine.blocks.tiles.TileEntityGas;
import enviromine.blocks.water.BlockEnviroMineWater;
import enviromine.core.EM_ConfigHandler.EnumLogVerbosity;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.items.EnviroArmor;
import enviromine.items.EnviroItemWaterBottle;
import enviromine.items.ItemDavyLamp;
import enviromine.items.ItemElevator;
import enviromine.items.ItemSpoiledMilk;
import enviromine.items.RottenFood;
import enviromine.utils.WaterUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;

public class ObjectHandler {
	public static HashMap<Block, ArrayList<Integer>> igniteList = new HashMap<Block, ArrayList<Integer>>();
	public static ArmorMaterial camelPackMaterial;
	
	public static Item radioactive_frosty_WaterBottle;
	public static Item frosty_WaterBottle;
	
	public static Item radioactive_cold_WaterBottle;
	public static Item dirty_cold_WaterBottle;
	public static Item salty_cold_WaterBottle;
	public static Item clean_cold_WaterBottle;
	
	public static Item radioactive_WaterBottle;
	public static Item dirty_WaterBottle;
	public static Item salty_WaterBottle;
	
	public static Item radioactive_warm_WaterBottle;
	public static Item dirty_warm_WaterBottle;
	public static Item salty_warm_WaterBottle;
	public static Item clean_warm_WaterBottle;
	
	public static Item radioactive_hot_WaterBottle;
	public static Item hot_WaterBottle;
	
	public static Item airFilter;
	public static Item davyLamp;
	public static Item gasMeter;
	public static Item rottenFood;
	public static Item spoiledMilk;

	public static ItemArmor camelPack;
	public static ItemArmor gasMask;
	public static ItemArmor hardHat;

	public static Block davyLampBlock;
	public static Block elevator;
	public static Block gasBlock;
	public static Block fireGasBlock;
	public static Block burningCoal;
	public static Block offTorch;

	public static Block esky;
	public static Block freezer;

	public static Block noPhysBlock;

	public static int renderGasID;
	public static int renderSpecialID;

	public static Material gasMat;
	public static Material elevatorMat;
	
	///-------
	
	public static Fluid radioactive_frosty_Water;
	public static Fluid frosty_Water;
	
	public static Fluid radioactive_cold_Water;
	public static Fluid dirty_cold_Water;
	public static Fluid salty_cold_Water;
	public static Fluid clean_cold_Water;
	
	public static Fluid radioactive_Water;
	public static Fluid dirty_Water;
	public static Fluid salty_Water;
	
	public static Fluid radioactive_warm_Water;
	public static Fluid dirty_warm_Water;
	public static Fluid salty_warm_Water;
	public static Fluid clean_warm_Water;
	
	public static Fluid radioactive_hot_Water;
	public static Fluid hot_Water;
	
	///-------
	
	public static Block block_radioactive_frosty_Water;
	public static Block block_frosty_Water;
	
	public static Block block_radioactive_cold_Water;
	public static Block block_dirty_cold_Water;
	public static Block block_salty_cold_Water;
	public static Block block_clean_cold_Water;
	
	public static Block block_radioactive_Water;
	public static Block block_dirty_Water;
	public static Block block_salty_Water;
	
	public static Block block_radioactive_warm_Water;
	public static Block block_dirty_warm_Water;
	public static Block block_salty_warm_Water;
	public static Block block_clean_warm_Water;
	
	public static Block block_radioactive_hot_Water;
	public static Block block_hot_Water;
	
	///-------

	public static void initItems()
	{
		//NAMES
		//[trait]_[temp]_WaterBottle
		
		radioactive_frosty_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.RADIOACTIVE_FROSTY).setMaxStackSize(1).setUnlocalizedName("enviromine.radioactive_frosty_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		frosty_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.FROSTY).setMaxStackSize(1).setUnlocalizedName("enviromine.frosty_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		
		radioactive_cold_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.RADIOACTIVE_COLD).setMaxStackSize(1).setUnlocalizedName("enviromine.radioactive_cold_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		dirty_cold_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.DIRTY_COLD).setMaxStackSize(1).setUnlocalizedName("enviromine.dirty_cold_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		salty_cold_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.SALTY_COLD).setMaxStackSize(1).setUnlocalizedName("enviromine.salty_cold_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		clean_cold_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.CLEAN_COLD).setMaxStackSize(1).setUnlocalizedName("enviromine.clean_cold_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		
		radioactive_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.RADIOACTIVE).setMaxStackSize(1).setUnlocalizedName("enviromine.radioactive_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		dirty_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.DIRTY).setMaxStackSize(1).setUnlocalizedName("enviromine.dirty_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		salty_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.SALTY).setMaxStackSize(1).setUnlocalizedName("enviromine.salty_WaterBottle").setCreativeTab(EnviroMine.enviroTab);

		radioactive_warm_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.RADIOACTIVE_WARM).setMaxStackSize(1).setUnlocalizedName("enviromine.radioactive_warm_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		dirty_warm_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.DIRTY_WARM).setMaxStackSize(1).setUnlocalizedName("enviromine.dirty_warm_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		salty_warm_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.SALTY_WARM).setMaxStackSize(1).setUnlocalizedName("enviromine.salty_warm_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		clean_warm_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.CLEAN_WARM).setMaxStackSize(1).setUnlocalizedName("enviromine.clean_warm_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		
		radioactive_hot_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.RADIOACTIVE_HOT).setMaxStackSize(1).setUnlocalizedName("enviromine.radioactive_hot_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		hot_WaterBottle = new EnviroItemWaterBottle(WaterUtils.WATER_TYPES.HOT).setMaxStackSize(1).setUnlocalizedName("enviromine.hot_WaterBottle").setCreativeTab(EnviroMine.enviroTab);
		
        airFilter = new Item().setMaxStackSize(16).setUnlocalizedName("enviromine.airfilter").setCreativeTab(EnviroMine.enviroTab).setTextureName("enviromine:air_filter");
		rottenFood = new RottenFood(1).setMaxStackSize(64).setUnlocalizedName("enviromine.rottenfood").setCreativeTab(EnviroMine.enviroTab).setTextureName("enviromine:rot");
		spoiledMilk = new ItemSpoiledMilk().setUnlocalizedName("enviromine.spoiledmilk").setCreativeTab(EnviroMine.enviroTab).setTextureName("bucket_milk");

		camelPackMaterial = EnumHelper.addArmorMaterial("camelPack", EM_Settings.camelPackMax, new int[]{2, 2, 0, 0}, 0);

		camelPack = (ItemArmor)new EnviroArmor(camelPackMaterial, 4, 1).setTextureName("camel_pack").setUnlocalizedName("enviromine.camelpack").setCreativeTab(null);

		gasMask = (ItemArmor)new EnviroArmor(camelPackMaterial, 4, 0).setTextureName("gas_mask").setUnlocalizedName("enviromine.gasmask").setCreativeTab(null);
		hardHat = (ItemArmor)new EnviroArmor(camelPackMaterial, 4, 0).setTextureName("hard_hat").setUnlocalizedName("enviromine.hardhat").setCreativeTab(EnviroMine.enviroTab);
	}

	public static void registerItems() 
	{
		GameRegistry.registerItem(radioactive_frosty_WaterBottle, "radioactive_frosty_WaterBottle");
		GameRegistry.registerItem(frosty_WaterBottle, "frosty_WaterBottle");
		
		GameRegistry.registerItem(radioactive_cold_WaterBottle, "radioactive_cold_WaterBottle");
		GameRegistry.registerItem(dirty_cold_WaterBottle, "dirty_cold_WaterBottle");
		GameRegistry.registerItem(salty_cold_WaterBottle, "salty_cold_WaterBottle");
		GameRegistry.registerItem(clean_cold_WaterBottle, "clean_cold_WaterBottle");
		
		GameRegistry.registerItem(radioactive_WaterBottle, "radioactive_WaterBottle");
		GameRegistry.registerItem(dirty_WaterBottle, "dirty_WaterBottle");
		GameRegistry.registerItem(salty_WaterBottle, "salty_WaterBottle");
		
		GameRegistry.registerItem(radioactive_warm_WaterBottle, "radioactive_warm_WaterBottle");
		GameRegistry.registerItem(dirty_warm_WaterBottle, "dirty_warm_WaterBottle");
		GameRegistry.registerItem(salty_warm_WaterBottle, "salty_warm_WaterBottle");
		GameRegistry.registerItem(clean_warm_WaterBottle, "clean_warm_WaterBottle");
		
		GameRegistry.registerItem(radioactive_hot_WaterBottle, "radioactive_hot_WaterBottle");
		GameRegistry.registerItem(hot_WaterBottle, "hot_WaterBottle");

        GameRegistry.registerItem(airFilter, "airFilter");
		GameRegistry.registerItem(rottenFood, "rottenFood");
		GameRegistry.registerItem(spoiledMilk, "spoiledMilk");
		GameRegistry.registerItem(camelPack, "camelPack");
		GameRegistry.registerItem(gasMask, "gasMask");
		GameRegistry.registerItem(hardHat, "hardHat");

		// Empty Pack
		ItemStack camelStack1 = new ItemStack(camelPack);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(EM_Settings.CAMEL_PACK_FILL_TAG_KEY, 0);
		tag.setInteger(EM_Settings.CAMEL_PACK_MAX_TAG_KEY, EM_Settings.camelPackMax);
		tag.setBoolean(EM_Settings.IS_CAMEL_PACK_TAG_KEY, true);
		tag.setString("camelPath", Item.itemRegistry.getNameForObject(camelPack));
		camelStack1.setTagCompound(tag);
		EnviroMine.enviroTab.addRawStack(camelStack1);

		// Full Pack
		ItemStack camelStack2 = new ItemStack(camelPack);
		tag = new NBTTagCompound();
		tag.setInteger(EM_Settings.CAMEL_PACK_FILL_TAG_KEY, EM_Settings.camelPackMax);
		tag.setInteger(EM_Settings.CAMEL_PACK_MAX_TAG_KEY, EM_Settings.camelPackMax);
		tag.setBoolean(EM_Settings.IS_CAMEL_PACK_TAG_KEY, true);
		tag.setString("camelPath", Item.itemRegistry.getNameForObject(camelPack));
		camelStack2.setTagCompound(tag);
		EnviroMine.enviroTab.addRawStack(camelStack2);

		// Empty Mask
		ItemStack mask = new ItemStack(gasMask);
		tag = new NBTTagCompound();
		tag.setInteger(EM_Settings.GAS_MASK_FILL_TAG_KEY, 0);
		tag.setInteger(EM_Settings.GAS_MASK_MAX_TAG_KEY, EM_Settings.gasMaskMax);
		mask.setTagCompound(tag);
		EnviroMine.enviroTab.addRawStack(mask);

		// Full Mask
		mask = new ItemStack(gasMask);
		tag = new NBTTagCompound();
		tag.setInteger(EM_Settings.GAS_MASK_FILL_TAG_KEY, EM_Settings.gasMaskMax);
		tag.setInteger(EM_Settings.GAS_MASK_MAX_TAG_KEY, EM_Settings.gasMaskMax);
		mask.setTagCompound(tag);
		EnviroMine.enviroTab.addRawStack(mask);
	}

	public static void initBlocks() {
		radioactive_frosty_Water 	= new Fluid("radioactive_frosty_Water");
		frosty_Water				= new Fluid("frosty_Water");
		radioactive_cold_Water		= new Fluid("radioactive_cold_Water");
		dirty_cold_Water			= new Fluid("dirty_cold_Water");
		salty_cold_Water			= new Fluid("salty_cold_Water");
		clean_cold_Water			= new Fluid("clean_cold_Water");
		radioactive_Water			= new Fluid("radioactive_Wate");
		dirty_Water					= new Fluid("dirty_Water");
		salty_Water					= new Fluid("salty_Water");
		radioactive_warm_Water		= new Fluid("radioactive_warm_Water");
		dirty_warm_Water			= new Fluid("dirty_warm_Water");
		salty_warm_Water			= new Fluid("salty_warm_Water");
		clean_warm_Water			= new Fluid("clean_warm_Water");
		radioactive_hot_Water		= new Fluid("radioactive_hot_Water");
		hot_Water					= new Fluid("hot_Water");
		
		FluidRegistry.registerFluid(radioactive_frosty_Water);
		FluidRegistry.registerFluid(frosty_Water);
		FluidRegistry.registerFluid(radioactive_cold_Water);
		FluidRegistry.registerFluid(dirty_cold_Water);
		FluidRegistry.registerFluid(salty_cold_Water);
		FluidRegistry.registerFluid(clean_cold_Water);
		FluidRegistry.registerFluid(radioactive_Water);
		FluidRegistry.registerFluid(dirty_Water);
		FluidRegistry.registerFluid(salty_Water);
		FluidRegistry.registerFluid(radioactive_warm_Water);
		FluidRegistry.registerFluid(dirty_warm_Water);
		FluidRegistry.registerFluid(salty_warm_Water);
		FluidRegistry.registerFluid(clean_warm_Water);
		FluidRegistry.registerFluid(radioactive_hot_Water);
		FluidRegistry.registerFluid(hot_Water);
		
		block_radioactive_frosty_Water	= new BlockEnviroMineWater(radioactive_frosty_Water, Material.water).setBlockName("block_radioactive_frosty_Water").setHardness(100.0f);
		block_frosty_Water				= new BlockEnviroMineWater(frosty_Water, Material.water).setBlockName("block_frosty_Water").setHardness(100.0f);
		block_radioactive_cold_Water	= new BlockEnviroMineWater(radioactive_cold_Water, Material.water).setBlockName("block_radioactive_cold_Water").setHardness(100.0f);
		block_dirty_cold_Water			= new BlockEnviroMineWater(dirty_cold_Water, Material.water).setBlockName("block_dirty_cold_Water").setHardness(100.0f);
		block_salty_cold_Water			= new BlockEnviroMineWater(salty_cold_Water, Material.water).setBlockName("block_salty_cold_Water").setHardness(100.0f);
		block_clean_cold_Water			= new BlockEnviroMineWater(clean_cold_Water, Material.water).setBlockName("block_clean_cold_Water").setHardness(100.0f);
		block_radioactive_Water			= new BlockEnviroMineWater(radioactive_Water, Material.water).setBlockName("block_radioactive_Water").setHardness(100.0f);
		block_dirty_Water				= new BlockEnviroMineWater(dirty_Water, Material.water).setBlockName("block_dirty_Water").setHardness(100.0f);
		block_salty_Water				= new BlockEnviroMineWater(salty_Water, Material.water).setBlockName("block_salty_Water").setHardness(100.0f);
		block_radioactive_warm_Water	= new BlockEnviroMineWater(radioactive_warm_Water, Material.water).setBlockName("block_radioactive_warm_Water").setHardness(100.0f);
		block_dirty_warm_Water			= new BlockEnviroMineWater(dirty_warm_Water, Material.water).setBlockName("block_dirty_warm_Water").setHardness(100.0f);
		block_salty_warm_Water			= new BlockEnviroMineWater(salty_warm_Water, Material.water).setBlockName("block_salty_warm_Water").setHardness(100.0f);
		block_clean_warm_Water			= new BlockEnviroMineWater(clean_warm_Water, Material.water).setBlockName("block_clean_warm_Water").setHardness(100.0f);
		block_radioactive_hot_Water		= new BlockEnviroMineWater(radioactive_hot_Water, Material.water).setBlockName("block_radioactive_hot_Water").setHardness(100.0f);
		block_hot_Water					= new BlockEnviroMineWater(hot_Water, Material.water).setBlockName("block_hot_Water").setHardness(100.0f);
		
		gasMat = new MaterialGas(MapColor.airColor);
		gasBlock = new BlockGas(gasMat).setBlockName("enviromine.gas").setCreativeTab(EnviroMine.enviroTab).setBlockTextureName("enviromine:gas_block");
		fireGasBlock = new BlockGas(gasMat).setBlockName("enviromine.firegas").setCreativeTab(EnviroMine.enviroTab).setBlockTextureName("enviromine:gas_block").setLightLevel(1.0F);

		elevatorMat = new MaterialElevator(MapColor.ironColor);
		elevator = new BlockElevator(elevatorMat).setBlockName("enviromine.elevator").setCreativeTab(EnviroMine.enviroTab).setBlockTextureName("iron_block");

		davyLampBlock = new BlockDavyLamp(Material.redstoneLight).setLightLevel(1.0F).setBlockName("enviromine.davy_lamp").setCreativeTab(EnviroMine.enviroTab);
		davyLamp = new ItemDavyLamp(davyLampBlock).setUnlocalizedName("enviromine.davylamp").setCreativeTab(EnviroMine.enviroTab);
		
		burningCoal = new BlockBurningCoal(Material.rock).setBlockName("enviromine.burningcoal").setCreativeTab(EnviroMine.enviroTab);
		offTorch = new BlockOffTorch().setTickRandomly(false).setBlockName("torch").setBlockTextureName("enviromine:torch_off").setLightLevel(0F).setCreativeTab(EnviroMine.enviroTab);
		esky = new BlockEsky(Material.iron).setBlockName("enviromine.esky").setCreativeTab(EnviroMine.enviroTab);
		freezer = new BlockFreezer(Material.iron).setBlockName("enviromine.freezer").setCreativeTab(EnviroMine.enviroTab);

		noPhysBlock = new BlockNoPhysics();

		Blocks.redstone_torch.setLightLevel(0.9375F);
	}

	public static void registerBlocks() {
		
		GameRegistry.registerBlock(block_radioactive_frosty_Water, "block_radioactive_frosty_Water");
		GameRegistry.registerBlock(block_frosty_Water, "block_frosty_Water");
		GameRegistry.registerBlock(block_radioactive_cold_Water, "block_radioactive_cold_Water");
		GameRegistry.registerBlock(block_dirty_cold_Water, "block_dirty_cold_Water");
		GameRegistry.registerBlock(block_salty_cold_Water, "block_salty_cold_Water");
		GameRegistry.registerBlock(block_clean_cold_Water, "block_clean_cold_Water");
		GameRegistry.registerBlock(block_radioactive_Water, "block_radioactive_Water");
		GameRegistry.registerBlock(block_dirty_Water, "block_dirty_Water");
		GameRegistry.registerBlock(block_salty_Water, "block_salty_Water");
		GameRegistry.registerBlock(block_radioactive_warm_Water, "block_radioactive_warm_Water");
		GameRegistry.registerBlock(block_dirty_warm_Water, "block_dirty_warm_Water");
		GameRegistry.registerBlock(block_salty_warm_Water, "block_salty_warm_Water");
		GameRegistry.registerBlock(block_clean_warm_Water, "block_clean_warm_Water");
		GameRegistry.registerBlock(block_radioactive_hot_Water, "block_radioactive_hot_Water");
		GameRegistry.registerBlock(block_hot_Water, "block_hot_Water");
		
		GameRegistry.registerBlock(gasBlock, "gas");
		GameRegistry.registerBlock(fireGasBlock, "firegas");
		GameRegistry.registerBlock(elevator, ItemElevator.class, "elevator");
		GameRegistry.registerBlock(davyLampBlock, ItemDavyLamp.class, "davy_lamp");
		GameRegistry.registerBlock(offTorch, "offtorch");
		GameRegistry.registerBlock(burningCoal, "burningcoal");
		GameRegistry.registerBlock(esky, "esky");
		GameRegistry.registerBlock(freezer, "freezer");
		GameRegistry.registerBlock(noPhysBlock, "no_phys_block");
	}

	public static void registerGases()
	{
	}

	public static void registerEntities()
	{
		int physID = EntityRegistry.findGlobalUniqueEntityId();
		EntityRegistry.registerGlobalEntityID(EntityPhysicsBlock.class, "EnviroPhysicsBlock", physID);
		EntityRegistry.registerModEntity(EntityPhysicsBlock.class, "EnviroPhysicsEntity", physID, EnviroMine.instance, 64, 1, true);
		GameRegistry.registerTileEntity(TileEntityGas.class, "enviromine.tile.gas");
		GameRegistry.registerTileEntity(TileEntityBurningCoal.class, "enviromine.tile.burningcoal");
		GameRegistry.registerTileEntity(TileEntityEsky.class, "enviromine.tile.esky");
		GameRegistry.registerTileEntity(TileEntityFreezer.class, "enviromine.tile.freezer");

		GameRegistry.registerTileEntity(TileEntityElevator.class, "enviromine.tile.elevator");


		GameRegistry.registerTileEntity(TileEntityDavyLamp.class, "enviromine.tile.davy_lamp");
	}
	
	public static ItemStack getItemStackFromWaterType(WaterUtils.WATER_TYPES type) {
		switch (type) {
			
			case RADIOACTIVE_FROSTY -> {return new ItemStack(radioactive_frosty_WaterBottle);}
			case FROSTY -> {return new ItemStack(frosty_WaterBottle);}
			
			case RADIOACTIVE_COLD -> {return new ItemStack(radioactive_cold_WaterBottle);}
			case DIRTY_COLD -> {return new ItemStack(dirty_cold_WaterBottle);}
			case SALTY_COLD -> {return new ItemStack(salty_cold_WaterBottle);}
			case CLEAN_COLD -> {return new ItemStack(clean_cold_WaterBottle);}
			
			case RADIOACTIVE -> {return new ItemStack(radioactive_WaterBottle);}
			case DIRTY -> {return new ItemStack(dirty_WaterBottle);}
			case SALTY -> {return new ItemStack(salty_WaterBottle);}
			
			case RADIOACTIVE_WARM -> {return new ItemStack(radioactive_warm_WaterBottle);}
			case DIRTY_WARM -> {return new ItemStack(dirty_warm_WaterBottle);}
			case SALTY_WARM -> {return new ItemStack(salty_warm_WaterBottle);}
			case CLEAN_WARM -> {return new ItemStack(clean_warm_WaterBottle);}
			
			case RADIOACTIVE_HOT -> {return new ItemStack(radioactive_hot_WaterBottle);}
			case HOT -> {return new ItemStack(hot_WaterBottle);}
			
			default -> {return new ItemStack(Items.potionitem, 1, 0);}
		}
	}


	public static void registerRecipes()
	{
		String oreKeyAuBrass = "ingotGoldBrass";

		for (String oreDictName : new String[]{"ingotGold", "ingotBrass", "ingotAluminumBrass", "ingotAluminiumBrass"})
		{
            for (ItemStack oreItemStack : OreDictionary.getOres(oreDictName)) {
                OreDictionary.registerOre(oreKeyAuBrass, oreItemStack);
            }
		}

        ItemStack[] bottles = {
			new ItemStack(radioactive_frosty_WaterBottle),
			new ItemStack(frosty_WaterBottle),
					
			new ItemStack(radioactive_cold_WaterBottle),
			new ItemStack(dirty_cold_WaterBottle),
			new ItemStack(salty_cold_WaterBottle),
			new ItemStack(clean_cold_WaterBottle),
					
			new ItemStack(radioactive_WaterBottle),
			new ItemStack(dirty_WaterBottle),
			new ItemStack(salty_WaterBottle),
			new ItemStack(Items.potionitem, 1, 0),
					
			new ItemStack(radioactive_warm_WaterBottle),
			new ItemStack(dirty_warm_WaterBottle),
			new ItemStack(salty_warm_WaterBottle),
			new ItemStack(clean_warm_WaterBottle),
					
			new ItemStack(radioactive_hot_WaterBottle),
			new ItemStack(hot_WaterBottle),
        };
		
		//HEATING
		for(ItemStack bottle : bottles) {
			WaterUtils.WATER_TYPES localType = WaterUtils.WATER_TYPES.CLEAN;
			if (bottle.equals(new ItemStack(Items.potionitem, 1, 0))) {
				localType = WaterUtils.WATER_TYPES.CLEAN;
			} else if (bottle.getItem() instanceof EnviroItemWaterBottle enviroItemWaterBottle) {
				localType = enviroItemWaterBottle.getWaterType();
			}
			if(WaterUtils.heatUp(localType) != localType) {
				GameRegistry.addSmelting(bottle, getItemStackFromWaterType(WaterUtils.heatUp(localType)), 0.0F);
			}
		}
		
		//COOLING
		for(ItemStack bottle : bottles) {
			WaterUtils.WATER_TYPES localType = WaterUtils.WATER_TYPES.CLEAN;
			if (bottle.equals(new ItemStack(Items.potionitem, 1, 0))) {
				localType = WaterUtils.WATER_TYPES.CLEAN;
			} else if (bottle.getItem() instanceof EnviroItemWaterBottle enviroItemWaterBottle) {
				localType = enviroItemWaterBottle.getWaterType();
			}
			if(WaterUtils.coolDown(localType) != localType) {
				GameRegistry.addShapelessRecipe(getItemStackFromWaterType(WaterUtils.coolDown(localType)), bottle, new ItemStack(Items.snowball, 1));
			}
		}
		
		//SALTING
		for(ItemStack bottle : bottles) {
			WaterUtils.WATER_TYPES localType = WaterUtils.WATER_TYPES.CLEAN;
			if (bottle.equals(new ItemStack(Items.potionitem, 1, 0))) {
				localType = WaterUtils.WATER_TYPES.CLEAN;
			} else if (bottle.getItem() instanceof EnviroItemWaterBottle enviroItemWaterBottle) {
				localType = enviroItemWaterBottle.getWaterType();
			}
			if(WaterUtils.saltDown(localType) != localType) {
				GameRegistry.addShapelessRecipe(getItemStackFromWaterType(WaterUtils.saltDown(localType)), bottle, new ItemStack(Blocks.sand, 1));
			}
		}
		//POLLUTING or whatever is this
		for(ItemStack bottle : bottles) {
			WaterUtils.WATER_TYPES localType = WaterUtils.WATER_TYPES.CLEAN;
			if (bottle.equals(new ItemStack(Items.potionitem, 1, 0))) {
				localType = WaterUtils.WATER_TYPES.CLEAN;
			} else if (bottle.getItem() instanceof EnviroItemWaterBottle enviroItemWaterBottle) {
				localType = enviroItemWaterBottle.getWaterType();
			}
			if(WaterUtils.pollute(localType) != localType) {
				GameRegistry.addShapelessRecipe(getItemStackFromWaterType(WaterUtils.pollute(localType)), bottle, new ItemStack(Blocks.dirt, 1));
			}
		}
		
		GameRegistry.addRecipe(new ItemStack(Items.slime_ball, 4, 0), " r ", "rwr", " r ", 'w', new ItemStack(spoiledMilk, 1, 0), 'r', new ItemStack(rottenFood, 1));
		GameRegistry.addRecipe(new ItemStack(Blocks.mycelium), "xyx", "yzy", "xyx", 'z', new ItemStack(Blocks.grass), 'x', new ItemStack(Blocks.brown_mushroom), 'y', new ItemStack(rottenFood, 1));
		GameRegistry.addRecipe(new ItemStack(Blocks.mycelium), "xyx", "yzy", "xyx", 'z', new ItemStack(Blocks.grass), 'y', new ItemStack(Blocks.brown_mushroom), 'x', new ItemStack(rottenFood, 1));
		GameRegistry.addRecipe(new ItemStack(Blocks.mycelium), "xyx", "yzy", "xyx", 'z', new ItemStack(Blocks.grass), 'x', new ItemStack(Blocks.red_mushroom), 'y', new ItemStack(rottenFood, 1));
		GameRegistry.addRecipe(new ItemStack(Blocks.mycelium), "xyx", "yzy", "xyx", 'z', new ItemStack(Blocks.grass), 'y', new ItemStack(Blocks.red_mushroom), 'x', new ItemStack(rottenFood, 1));
		GameRegistry.addRecipe(new ItemStack(Blocks.dirt, 1), "xxx", "xxx", "xxx", 'x', new ItemStack(rottenFood));


		GameRegistry.addRecipe(new ItemStack(gasMask, 1), "xxx", "xzx", "yxy", 'x', new ItemStack(Items.iron_ingot), 'y', new ItemStack(airFilter), 'z', new ItemStack(Blocks.glass_pane));
		GameRegistry.addRecipe(new ItemStack(hardHat, 1), "xyx", "xzx", 'x', new ItemStack(Items.dye, 1, 11), 'y', new ItemStack(Blocks.redstone_lamp), 'z', new ItemStack(Items.iron_helmet, 1, 0));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(hardHat, 1), " y ", "xxx", "x x", 'x', oreKeyAuBrass, 'y', new ItemStack(Blocks.redstone_lamp)));

		GameRegistry.addRecipe(new ItemStack(airFilter, 4), "xyx", "xzx", "xyx", 'x', new ItemStack(Items.iron_ingot), 'y', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE), 'z', new ItemStack(Items.coal, 1, 1));
		GameRegistry.addRecipe(new ItemStack(airFilter, 4), "xyx", "xzx", "xyx", 'x', new ItemStack(Items.iron_ingot), 'y', new ItemStack(Items.paper), 'z', new ItemStack(Items.coal, 1, 1));
		GameRegistry.addRecipe(new ItemStack(airFilter, 4), "xyx", "xzx", "xpx", 'x', new ItemStack(Items.iron_ingot), 'y', new ItemStack(Items.paper), 'p', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),'z', new ItemStack(Items.coal, 1, 1));
		GameRegistry.addRecipe(new ItemStack(airFilter, 4), "xpx", "xzx", "xyx", 'x', new ItemStack(Items.iron_ingot), 'y', new ItemStack(Items.paper), 'p', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),'z', new ItemStack(Items.coal, 1, 1));

		GameRegistry.addRecipe(new ItemStack(elevator, 1, 0), "xyx", "z z", "z z", 'x', new ItemStack(Blocks.iron_block), 'y', new ItemStack(Blocks.redstone_lamp), 'z', new ItemStack(Blocks.iron_bars));
		GameRegistry.addRecipe(new ItemStack(elevator, 1, 1), "z z", "xyx", "www", 'x', new ItemStack(Blocks.iron_block), 'y', new ItemStack(Blocks.furnace), 'z', new ItemStack(Blocks.iron_bars), 'w', new ItemStack(Items.diamond_pickaxe));

		//GameRegistry.addRecipe(new ItemStack(davyLampBlock), " x ", "zyz", "xxx", 'x', new ItemStack(Items.gold_ingot), 'y', new ItemStack(Blocks.torch), 'z', new ItemStack(Blocks.glass_pane));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(davyLampBlock, 1), " x ", "zyz", "xxx", 'x', oreKeyAuBrass, 'y', new ItemStack(Blocks.torch), 'z', new ItemStack(Blocks.glass_pane)));

		GameRegistry.addShapelessRecipe(new ItemStack(davyLampBlock, 1, 1), new ItemStack(davyLampBlock, 1, 0), new ItemStack(Items.flint_and_steel, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(davyLampBlock, 1, 1), new ItemStack(davyLampBlock, 1, 0), new ItemStack(Blocks.torch));
		
		GameRegistry.addRecipe(new ItemStack(esky), "xxx", "yzy", "yyy", 'x', new ItemStack(Blocks.snow), 'y', new ItemStack(Items.dye, 1, 4), 'z', new ItemStack(Blocks.chest));
		GameRegistry.addRecipe(new ItemStack(freezer), "xyx", "yzy", "xyx", 'x', new ItemStack(Blocks.iron_block), 'y', new ItemStack(Blocks.ice), 'z', new ItemStack(esky));
		GameRegistry.addRecipe(new ItemStack(freezer), "xyx", "yzy", "xyx", 'x', new ItemStack(Blocks.iron_block), 'y', new ItemStack(Blocks.packed_ice), 'z', new ItemStack(esky));

		ItemStack camelStack = new ItemStack(camelPack);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(EM_Settings.CAMEL_PACK_FILL_TAG_KEY, 0);
		tag.setInteger(EM_Settings.CAMEL_PACK_MAX_TAG_KEY, EM_Settings.camelPackMax);
		tag.setBoolean(EM_Settings.IS_CAMEL_PACK_TAG_KEY, true);
		tag.setString("camelPath", Item.itemRegistry.getNameForObject(camelPack));
		camelStack.setTagCompound(tag);
		GameRegistry.addRecipe(camelStack, "xxx", "xyx", "xxx", 'x', new ItemStack(Items.leather), 'y', new ItemStack(Items.glass_bottle));

		ItemStack camelStack2 = camelStack.copy();
		camelStack2.getTagCompound().setInteger(EM_Settings.CAMEL_PACK_FILL_TAG_KEY, 25);
		GameRegistry.addRecipe(camelStack2, "xxx", "xyx", "xxx", 'x', new ItemStack(Items.leather), 'y', new ItemStack(Items.potionitem, 1, 0));
	}

	public static String[] DefaultIgnitionSources()
	{
		String[] list = new String[]{
				"minecraft:flowing_lava",
				"minecraft:lava",
				"minecraft:torch",
				"minecraft:lit_furnace",
				"minecraft:fire",
				"minecraft:lit_pumpkin",
				"minecraft:lava_bucket",
				"enviromine:firegas",
				"enviromine:burningcoal",
				"campfirebackport:campfire",
				"campfirebackport:soul_campfire",
				"CarpentersBlocks:blockCarpentersTorch",
				"CaveBiomes:stone_lavacrust",
				"chisel:jackolantern1",
				"chisel:jackolantern2",
				"chisel:jackolantern3",
				"chisel:jackolantern4",
				"chisel:jackolantern5",
				"chisel:jackolantern6",
				"chisel:jackolantern7",
				"chisel:jackolantern8",
				"chisel:jackolantern9",
				"chisel:jackolantern10",
				"chisel:jackolantern11",
				"chisel:jackolantern12",
				"chisel:jackolantern13",
				"chisel:jackolantern14",
				"chisel:jackolantern15",
				"chisel:jackolantern16",
				"chisel:torch1",
				"chisel:torch2",
				"chisel:torch3",
				"chisel:torch4",
				"chisel:torch5",
				"chisel:torch6",
				"chisel:torch7",
				"chisel:torch8",
				"demonmobs:hellfire",
				"easycoloredlights:easycoloredlightsCLStone=-1",
				"etfuturum:magma",
				"fossil:skullLantern",
				"GardenCore:small_fire",
				"harvestcraft:pamcandleDeco1",
				"harvestcraft:pamcandleDeco2",
				"harvestcraft:pamcandleDeco3",
				"harvestcraft:pamcandleDeco4",
				"harvestcraft:pamcandleDeco5",
				"harvestcraft:pamcandleDeco6",
				"harvestcraft:pamcandleDeco7",
				"harvestcraft:pamcandleDeco8",
				"harvestcraft:pamcandleDeco9",
				"harvestcraft:pamcandleDeco10",
				"harvestcraft:pamcandleDeco11",
				"harvestcraft:pamcandleDeco12",
				"harvestcraft:pamcandleDeco13",
				"harvestcraft:pamcandleDeco14",
				"harvestcraft:pamcandleDeco15",
				"harvestcraft:pamcandleDeco16",
				"IguanaTweaksTConstruct:clayBucketLava",
				"IguanaTweaksTConstruct:clayBucketsTinkers:*",
				"infernomobs:bucketpurelava",
				"infernomobs:cinderfallsword",
				"infernomobs:cinderfallswordverdant",
				"infernomobs:cinderfallswordazure",
				"infernomobs:emberscepter",
				"infernomobs:magmacharge",
				"infernomobs:magmascepter",
				"infernomobs:purelava",
				"infernomobs:scorchfire",
				"infernomobs:scorchfirescepter",
				"infernomobs:scorchfirecharge",
				"infernomobs:soulstoneinferno",
				"netherlicious:FoxFire",
				"netherlicious:MagmaBlock",
				"netherlicious:SoulFire",
				"Railcraft:lantern.stone",
				"Railcraft:lantern.metal",
				"Railcraft:lantern.stone",
				"TConstruct:decoration.stonetorch",
				"ThermalFoundation:bucket:*",
				"ThermalFoundation:FluidGlowstone",
				"ThermalFoundation:FluidEnder",
				"ThermalFoundation:FluidPyrotheum",
				"ThermalFoundation:FluidMana",
				"torchLevers:torchLever",
				"torchLevers:torchButton",
				"uptodate:magma_block",
			};

		return list;
	}

	public static void LoadIgnitionSources(String[] listIn)
	{
		for(String source : listIn)
		{
			try
			{
				Block sBlock = Block.getBlockFromName(source);
				igniteList.put(sBlock, new ArrayList<Integer>());
				if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.ALL.getLevel()) EnviroMine.logger.log(Level.INFO, "Registered "+ sBlock.getLocalizedName() +"("+source+") as an ignition source.");
			}catch(NullPointerException e)
			{
				if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.NORMAL.getLevel()) EnviroMine.logger.log(Level.ERROR, "Could not find "+ source);
			}

		}
	}
}
