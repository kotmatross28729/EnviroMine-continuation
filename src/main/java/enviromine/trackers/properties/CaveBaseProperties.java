package enviromine.trackers.properties;

import java.io.File;

import org.apache.logging.log4j.Level;

import enviromine.core.EM_ConfigHandler;
import enviromine.core.EM_ConfigHandler.EnumLogVerbosity;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.trackers.properties.helpers.PropertyBase;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

public class CaveBaseProperties implements PropertyBase
{
	public static final CaveBaseProperties base = new CaveBaseProperties();
	static String[] CBName;
	
	// Blocks that construct cave dimension
	public static Block caveStone_block = Blocks.stone;
	public static byte caveStone_meta = 0;
	public static Block caveGravel_block = Blocks.gravel;
	public static byte caveGravel_meta = 0;
	public static Block caveDirt_block = Blocks.dirt;
	public static byte caveDirt_meta = 0;
	
	public CaveBaseProperties()
	{
		// THIS CONSTRUCTOR IS FOR STATIC PURPOSES ONLY!
		
		if(base != null && base != this)
		{
			throw new IllegalStateException();
		}
	}

	@Override
	public String categoryName()
	{
		return "Main";
	}

	@Override
	public String categoryDescription()
	{
		return "The main options for the cave dimension";
	}

	@Override
	public void LoadProperty(Configuration config, String category)
	{
	}

	@Override
	public void SaveProperty(Configuration config, String category)
	{
	}

	@Override
	public void GenDefaults()
	{
		File file = this.GetDefaultFile();

		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch(Exception e)
			{
				if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.ERROR, "Failed to create file for StabilityTypes", e);
				return;
			}
		}
		
		Configuration config = new Configuration(file, true);
		String catName = this.categoryName();
		
		config.load();
		
		config.get(catName, CBName[0], -2).getInt(-2);
		config.get(catName, CBName[1], true).getBoolean(true);
		config.get(catName, CBName[2], false, "Makes the dimension more volcanic").getBoolean(false);
		config.get(catName, CBName[3], 30).getInt(30);
		config.get(catName, CBName[4], 7).getInt(7);
		config.get(catName, CBName[5], 8).getInt(8);
		config.get(catName, CBName[6], 23).getInt(23);
		config.get(catName, CBName[7], false).getBoolean(false);
		config.get(catName, CBName[8], 10).getInt(10);
		config.getInt(CBName[9], catName, 32, 0, 255, "Height at which water/lava generates");
		config.get(catName, CBName[10], true).getBoolean(true);
		config.get(catName, CBName[11], false).getBoolean(false);
		config.get(catName, CBName[12], false).getBoolean(false);
		config.get(catName, CBName[13], "minecraft:stone", "The main block used to populate the Cave dimension. Use the form mod:blockname:meta or mod:blockname").getString().trim();
		config.get(catName, CBName[14], "minecraft:gravel", "A block used to populate the Cave dimension. Use the form mod:blockname:meta or mod:blockname").getString().trim();
		config.get(catName, CBName[15], "minecraft:dirt", "A block used to populate the Cave dimension. Use the form mod:blockname:meta or mod:blockname").getString().trim();
		
		config.save();
	}

	@Override
	public File GetDefaultFile()
	{
		return new File(EM_ConfigHandler.configPath + "CaveDimension.cfg");
	}

	@Override
	public void generateEmpty(Configuration config, Object obj)
	{
	}

	@Override
	public boolean useCustomConfigs()
	{
		return false;
	}

	@Override
	public void customLoad()
	{
		File file = this.GetDefaultFile();

		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch(Exception e)
			{
				if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.ERROR, "Failed to create file for StabilityTypes", e);
				return;
			}
		}
		
		Configuration config = new Configuration(file, true);
		String catName = this.categoryName();
		
		config.load();
		
		EM_Settings.caveDimID = config.get(catName, CBName[0], -2).getInt(-2);
		EM_Settings.caveOreEvent = config.get(catName, CBName[1], true).getBoolean(true);
		EM_Settings.caveLava = config.get(catName, CBName[2], false, "Makes the dimension more volcanic").getBoolean(false);
		EM_Settings.caveRavineRarity = config.get(catName, CBName[3], 30).getInt(30);
		EM_Settings.caveTunnelRarity = config.get(catName, CBName[4], 7).getInt(7);
		EM_Settings.caveDungeons = config.get(catName, CBName[5], 8).getInt(8);
		EM_Settings.caveBiomeID = config.get(catName, CBName[6], 23).getInt(23);
		EM_Settings.disableCaves = config.get(catName, CBName[7], false).getBoolean(false);
		EM_Settings.limitElevatorY = config.get(catName, CBName[8], 10).getInt(10);
		EM_Settings.caveLiquidY = config.getInt(CBName[9], catName, 32, 0, 255, "Height at which water/lava generates");
		EM_Settings.caveFlood = config.get(catName, CBName[10], true).getBoolean(true);
		EM_Settings.caveRespawn = config.get(catName, CBName[11], false).getBoolean(false);
		EM_Settings.enforceWeights = config.get(catName, CBName[12], false, "If set to true EnviroMine will try to strictly enforce the configured spawn weights regardless of spawn method. Has some side effects!").getBoolean(false);
		
		// Load and assign Cave dimension blocks
		String caveStoneRaw = config.get(catName, CBName[13], "minecraft:stone", "The main block used to populate the Cave dimension. Use the form mod:blockname:meta or mod:blockname").getString().trim();
		String caveGravelRaw = config.get(catName, CBName[14], "minecraft:gravel", "A block used to populate the Cave dimension. Use the form mod:blockname:meta or mod:blockname").getString().trim();
		String caveDirtRaw = config.get(catName, CBName[15], "minecraft:dirt", "A block used to populate the Cave dimension. Use the form mod:blockname:meta or mod:blockname").getString().trim();
		
		String[] caveBlockSplit, caveBlockSplitTemp;
		Block tempBlock;
		
		
		// --- Stone replacement --- //
		
		caveBlockSplit = new String[]{"minecraft","stone","0"};
		caveBlockSplitTemp = caveStoneRaw.split(":");
		for (int i=0; i<caveBlockSplitTemp.length; i++) {caveBlockSplit[i] = caveBlockSplitTemp[i];}
		tempBlock = Block.getBlockFromName(caveBlockSplit[0]+":"+caveBlockSplit[1]);
		// Try re-assigning block
		if (tempBlock != null)
		{
			caveStone_block = tempBlock;
			// Try re-assigning meta
			if (caveBlockSplitTemp.length>2)
			{
				try {caveStone_meta = Byte.parseByte(caveBlockSplitTemp[2]);}
				catch (NumberFormatException e) {caveStone_meta = 0;}
			}
		}
		else {caveStone_block = Blocks.stone; caveStone_meta = 0;} // Revert to default
		
		
		// --- Gravel replacement --- //
		
		caveBlockSplit = new String[]{"minecraft","gravel","0"};
		caveBlockSplitTemp = caveGravelRaw.split(":");
		for (int i=0; i<caveBlockSplitTemp.length; i++) {caveBlockSplit[i] = caveBlockSplitTemp[i];}
		tempBlock = Block.getBlockFromName(caveBlockSplit[0]+":"+caveBlockSplit[1]);
		// Try re-assigning block
		if (tempBlock != null)
		{
			caveGravel_block = tempBlock;
			// Try re-assigning meta
			if (caveBlockSplitTemp.length>2)
			{
				try {caveGravel_meta = Byte.parseByte(caveBlockSplitTemp[2]);}
				catch (NumberFormatException e) {caveGravel_meta = 0;}
			}
		}
		else {caveGravel_block = Blocks.gravel; caveGravel_meta = 0;} // Revert to default
		
		
		// --- Dirt replacement --- //
		
		caveBlockSplit = new String[]{"minecraft","dirt","0"};
		caveBlockSplitTemp = caveDirtRaw.split(":");
		for (int i=0; i<caveBlockSplitTemp.length; i++) {caveBlockSplit[i] = caveBlockSplitTemp[i];}
		tempBlock = Block.getBlockFromName(caveBlockSplit[0]+":"+caveBlockSplit[1]);
		// Try re-assigning block
		if (tempBlock != null)
		{
			caveDirt_block = tempBlock;
			// Try re-assigning meta
			if (caveBlockSplitTemp.length>2)
			{
				try {caveDirt_meta = Byte.parseByte(caveBlockSplitTemp[2]);}
				catch (NumberFormatException e) {caveDirt_meta = 0;}
			}
		}
		else {caveDirt_block = Blocks.dirt; caveDirt_meta = 0;} // Revert to default
		
		config.save();
	}
	
	static
	{
		CBName = new String[16];
		
		CBName[0] = "Dimension ID";
		CBName[1] = "Fire OreGen event";
		CBName[2] = "Lava instead of Water";
		CBName[3] = "Ravine Rarity";
		CBName[4] = "Small Cave Rarity";
		CBName[5] = "Dungeons";
		CBName[6] = "Cave Biome ID";
		CBName[7] = "Disable Elevator Access";
		CBName[8] = "Elevator Height Limit";
		CBName[9] = "Water/Lava Height";
		CBName[10] = "Flood Side Caves";
		CBName[11] = "Can Respawn in Caves";
		CBName[12] = "Enforce Spawn Weights";
		CBName[13] = "Cave Dimension Block: Main";
		CBName[14] = "Cave Dimension Block: Scattered 1";
		CBName[15] = "Cave Dimension Block: Scattered 2";
	}
}
