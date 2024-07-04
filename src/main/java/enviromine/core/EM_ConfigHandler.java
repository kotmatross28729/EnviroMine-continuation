package enviromine.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.registry.EntityRegistry;
import enviromine.client.gui.hud.HUDRegistry;
import enviromine.handlers.ObjectHandler;
import enviromine.handlers.Legacy.LegacyHandler;
import enviromine.trackers.properties.ArmorProperties;
import enviromine.trackers.properties.BiomeProperties;
import enviromine.trackers.properties.BlockProperties;
import enviromine.trackers.properties.CaveBaseProperties;
import enviromine.trackers.properties.CaveGenProperties;
import enviromine.trackers.properties.CaveSpawnProperties;
import enviromine.trackers.properties.DimensionProperties;
import enviromine.trackers.properties.EntityProperties;
import enviromine.trackers.properties.ItemProperties;
import enviromine.trackers.properties.RotProperties;
import enviromine.trackers.properties.StabilityType;
import enviromine.trackers.properties.helpers.PropertyBase;
import enviromine.utils.ModIdentification;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class EM_ConfigHandler
{
	// Dirs for Custom Files
	public static String configPath = "config/enviromine/";
	public static String customPath = "CustomProperties/";

	public static String profilePath = configPath + "profiles/";
	public static String defaultProfile = profilePath +"default/";

	// Category names
	private static final String CATEGORY_CONFIG = "Config";
	private static final String CATEGORY_PHYSICS = "Physics";
	private static final String CATEGORY_POTIONS = "Potions";
	private static final String CATEGORY_GASES = "Gases";
	private static final String CATEGORY_SPEED_MULTIPLIERS = "Speed Multipliers";
	private static final String CATEGORY_EARTHQUAKES = "Earthquakes";
	private static final String CATEGORY_EASTER_EGGS = "Easter Eggs";
	private static final String CATEGORY_WORLD_GENERATION = "World Generation";
	private static final String CATEGORY_MOD_INTEGRATION = "Mod Integration";

	// Values
	public static final int HUD_ID_AIR_QUALITY = 3;
	public static final int HUD_ID_BODY_TEMPERATURE = 0;
	public static final int HUD_ID_HYDRATION = 1;
	public static final int HUD_ID_SANITY = 2;

    public static final int HUD_ID_BLOOD = 4;

	/**
	 * Configuration version number. If changed the version file will be reset to defaults to prevent glitches
	 */
	public static final String CONFIG_VERSION = "1.0.0";

	/**
	 * The version of the configs last loaded from file. This will be compared to the version number above when determining whether a reset is necessary
	 */


	public static String loadedProfile = defaultProfile;

	static HashMap<String, PropertyBase> propTypes;
	public static HashMap<String, PropertyBase> globalTypes;

	public static List loadedConfigs = new ArrayList();



	/**
	 * Register all property types and their category names here. The rest is handled automatically.
	 */
	static
	{
		propTypes = new HashMap<String, PropertyBase>();

		propTypes.put(BiomeProperties.base.categoryName(), BiomeProperties.base);
		propTypes.put(ArmorProperties.base.categoryName(), ArmorProperties.base);
		propTypes.put(BlockProperties.base.categoryName(), BlockProperties.base);
		propTypes.put(DimensionProperties.base.categoryName(), DimensionProperties.base);
		propTypes.put(EntityProperties.base.categoryName(), EntityProperties.base);
		propTypes.put(ItemProperties.base.categoryName(), ItemProperties.base);
		propTypes.put(RotProperties.base.categoryName(), RotProperties.base);

		globalTypes = new HashMap<String, PropertyBase>();
		globalTypes.put(CaveGenProperties.base.categoryName(), CaveGenProperties.base);
		globalTypes.put(CaveSpawnProperties.base.categoryName(), CaveSpawnProperties.base);
		globalTypes.put(CaveBaseProperties.base.categoryName(), CaveBaseProperties.base);

	}


	public static void initProfile()
	{
		/*EM_WorldData theWorldEM = EnviroMine.theWorldEM;

		String profile = theWorldEM.getProfile();

		// if profile is overriden than switch profiles
		if(EM_Settings.profileOverride)
		{
			profile = EM_Settings.profileSelected;
			theWorldEM.setProfile(profile);
		}*/

		String profile = EM_Settings.profileSelected;

		if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.ALL.getLevel()) EnviroMine.logger.log(Level.INFO, "LOADING PROFILE: " + profile);

		File profileDir = new File(profilePath + profile +"/"+ customPath);

		//CheckDir(profileDir);

		if(!profileDir.exists())
		{
			try
			{
				profileDir.mkdirs();
			}
			catch(Exception e)
			{
				if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.ERROR, "Unable to create directories for profile", e);
			}
		}

		if(!profileDir.exists())
		{
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.ERROR, "Failed to load Profile:"+ profile +". Loading Default");
			profileDir = new File(defaultProfile + customPath);
			loadedProfile = defaultProfile;
		}
		else
		{
			loadedProfile = profilePath + profile +"/";
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.ALL.getLevel()) EnviroMine.logger.log(Level.INFO, "Loading Profile: "+ profile);
		}

		File ProfileSettings = new File(loadedProfile + profile +"_Settings.cfg");
		loadProfileConfig(ProfileSettings);
		// load defaults

		loadHudItems();

		//These must be run before the block configs generate/load
		StabilityType.base.GenDefaults();
		StabilityType.base.customLoad();

		if(EM_Settings.genDefaults)
		{
			loadDefaultProperties();
		}


		// Now load Files from "Custom Objects"
		File[] customFiles = GetFileList(loadedProfile + customPath);
		for(int i = 0; i < customFiles.length; i++)
		{
			LoadCustomObjects(customFiles[i]);
		}




		Iterator<PropertyBase> iterator = propTypes.values().iterator();

		// Load non standard property files
		while(iterator.hasNext())
		{
			PropertyBase props = iterator.next();

			if(!props.useCustomConfigs())
			{
				props.customLoad();
			}
		}

		if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.ALL.getLevel())
		{
			EnviroMine.logger.log(Level.INFO, "Loaded " + EM_Settings.stabilityTypes.size() + " stability types");
			EnviroMine.logger.log(Level.INFO, "Loaded " + EM_Settings.armorProperties.size() + " armor properties");
			EnviroMine.logger.log(Level.INFO, "Loaded " + EM_Settings.blockProperties.size() + " block properties");
			EnviroMine.logger.log(Level.INFO, "Loaded " + EM_Settings.livingProperties.size() + " entity properties");
			EnviroMine.logger.log(Level.INFO, "Loaded " + EM_Settings.itemProperties.size() + " item properties");
			EnviroMine.logger.log(Level.INFO, "Loaded " + EM_Settings.rotProperties.size() + " rot properties");
			EnviroMine.logger.log(Level.INFO, "Loaded " + EM_Settings.biomeProperties.size() + " biome properties");
			EnviroMine.logger.log(Level.INFO, "Loaded " + EM_Settings.dimensionProperties.size() + " dimension properties");
			EnviroMine.logger.log(Level.INFO, "Loaded " + EM_Settings.caveGenProperties.size() + " cave ore properties");
			EnviroMine.logger.log(Level.INFO, "Loaded " + EM_Settings.caveSpawnProperties.size() + " cave entity properties");
		}

	}

	// HardCoded for now
	public static void loadHudItems()
	{
		if(!EM_Settings.enableAirQ
				&& HUDRegistry.isActiveHudItem(HUDRegistry.getHudItemByID(HUD_ID_AIR_QUALITY)))
		{
			HUDRegistry.disableHudItem(HUDRegistry.getHudItemByID(HUD_ID_AIR_QUALITY));
		}
		else if (!HUDRegistry.isActiveHudItem(HUDRegistry.getHudItemByID(HUD_ID_AIR_QUALITY)))
		{
			HUDRegistry.enableHudItem(HUDRegistry.getHudItemByID(HUD_ID_AIR_QUALITY));
		}

		if(!EM_Settings.enableBodyTemp
				&& HUDRegistry.isActiveHudItem(HUDRegistry.getHudItemByID(HUD_ID_BODY_TEMPERATURE)))
		{
			HUDRegistry.disableHudItem(HUDRegistry.getHudItemByID(HUD_ID_BODY_TEMPERATURE));
		}
		else if (!HUDRegistry.isActiveHudItem(HUDRegistry.getHudItemByID(HUD_ID_BODY_TEMPERATURE)))
		{
			HUDRegistry.enableHudItem(HUDRegistry.getHudItemByID(HUD_ID_BODY_TEMPERATURE));
		}

		if(!EM_Settings.enableHydrate
				&& HUDRegistry.isActiveHudItem(HUDRegistry.getHudItemByID(HUD_ID_HYDRATION)))
		{
			HUDRegistry.disableHudItem(HUDRegistry.getHudItemByID(HUD_ID_HYDRATION));
		}
		else if (!HUDRegistry.isActiveHudItem(HUDRegistry.getHudItemByID(HUD_ID_HYDRATION)))
		{
			HUDRegistry.enableHudItem(HUDRegistry.getHudItemByID(HUD_ID_HYDRATION));
		}

		if(!EM_Settings.enableSanity
				&& HUDRegistry.isActiveHudItem(HUDRegistry.getHudItemByID(HUD_ID_SANITY)))
		{
			HUDRegistry.disableHudItem(HUDRegistry.getHudItemByID(HUD_ID_SANITY));
		}
		else if (!HUDRegistry.isActiveHudItem(HUDRegistry.getHudItemByID(HUD_ID_SANITY)))
		{
			HUDRegistry.enableHudItem(HUDRegistry.getHudItemByID(HUD_ID_SANITY));
		}
        else if (!HUDRegistry.isActiveHudItem(HUDRegistry.getHudItemByID(HUD_ID_BLOOD)))
        {
            HUDRegistry.enableHudItem(HUDRegistry.getHudItemByID(HUD_ID_BLOOD));
        }
	}

	public static int initConfig()
	{
		// Check for Data Directory
		//CheckDir(new File(customPath));

		if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.ALL.getLevel()) EnviroMine.logger.log(Level.INFO, "Loading configs...");

		// Load Global Configs
		File configFile = new File(configPath + "Global_Settings.cfg");
		loadGlobalConfig(configFile);

		Iterator<PropertyBase> iterator = globalTypes.values().iterator();

		// Load non standard property files
		while(iterator.hasNext())
		{
			PropertyBase props = iterator.next();

			if(!props.useCustomConfigs())
			{
				props.customLoad();
			}
		}

		int Total = EM_Settings.armorProperties.size() + EM_Settings.blockProperties.size() + EM_Settings.livingProperties.size() + EM_Settings.itemProperties.size() + EM_Settings.biomeProperties.size() + EM_Settings.dimensionProperties.size() + EM_Settings.caveGenProperties.size() + EM_Settings.caveSpawnProperties.size();

		return Total;
	}

	public enum EnumLogVerbosity
	{
		NONE(0),
		LOW(1),
		NORMAL(2),
		ALL(3);

		private final int level;

		private EnumLogVerbosity(int level)
		{
			this.level = level;
		}

		public int getLevel()
		{
			return this.level;
		}
	}

	private static void loadGlobalConfig(File file)
	{
		Configuration config;
		try
		{
			config = new Configuration(file, true);
		} catch(Exception e)
		{
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.WARN, "Failed to load main configuration file!", e);
			return;
		}

		config.load();


		// ------------------------ //
		// --- World Generation --- //
		// ------------------------ //

		EM_Settings.shaftGen = config.get(CATEGORY_WORLD_GENERATION, "Enable Village MineShafts", EM_Settings.shaftGen, "Generates mineshafts in villages").getBoolean(EM_Settings.shaftGen);
		EM_Settings.oldMineGen = config.get(CATEGORY_WORLD_GENERATION, "Enable New Abandoned Mineshafts", EM_Settings.oldMineGen, "Generates massive abandoned mineshafts (size doesn't cause lag) (This Overrides all Dimensions. Check Custom Dimension properties if you want to set it only for certain Dimensions.)").getBoolean(EM_Settings.oldMineGen);
		EM_Settings.gasGen = config.get(CATEGORY_WORLD_GENERATION, "Generate Gases", EM_Settings.gasGen).getBoolean(EM_Settings.gasGen);
		//EM_Settings.disableCaves = config.get(catWorldGen, "Disable Cave Dimension", false).getBoolean(false); // Moved to CaveBaseProperties
		//EM_Settings.limitElevatorY = config.get(catWorldGen, "Limit Elevator Height", true).getBoolean(true); // Moved to CaveBaseProperties

		config.get("Do not Edit", "Current Config Version", CONFIG_VERSION).getString();


		// --------------- //
		// --- General --- //
		// --------------- //

		//EM_Settings.updateCheck = config.get(Configuration.CATEGORY_GENERAL, "Check For Updates", EM_Settings.updateCheck).getBoolean(EM_Settings.updateCheck);
        EM_Settings.HbmGasMaskBreakMultiplier = config.getInt("Hbm Gas Mask filter break multiplier",Configuration.CATEGORY_GENERAL, 10, 0, 65536,  "Multiplier for breaking a hbm gas mask filter");
        EM_Settings.HbmGasMaskBreakChanceNumber = config.getInt("Hbm Gas Mask filter break chance number",Configuration.CATEGORY_GENERAL, 15, 0, 100,  "The number on which the chance of reducing the durability of the hbm filter depends. The lower the number, the greater the chance that the filter durability will decrease");
        EM_Settings.EnviromineGasMaskBreakMultiplier = config.getInt("Gas Masks break multiplier",Configuration.CATEGORY_GENERAL, 10, 0, 65536,  "A number that is subtracted from the current enviromine mask filter durability if the player is in a gas block, that is suffocating");

        EM_Settings.SulfurDioxideGasDebugLogger = config.getBoolean("SulfurDioxideGasDebugLogger", Configuration.CATEGORY_GENERAL, false, "Don't use unless you are a developer");
        EM_Settings.CarbonMonoxideGasDebugLogger = config.getBoolean("CarbonMonoxideGasDebugLogger", Configuration.CATEGORY_GENERAL, false, "Don't use unless you are a developer");
        EM_Settings.HydrogenSulfideGasDebugLogger = config.getBoolean("HydrogenSulfideGasDebugLogger", Configuration.CATEGORY_GENERAL, false, "Don't use unless you are a developer");

        EM_Settings.SulfurDioxidePoisoningAmplifier = config.getInt("Sulfur Dioxide Poisoning Amplifier",Configuration.CATEGORY_GENERAL, 5, 1, 65536,  "How dense the Sulfur Dioxide gas should  be, in order for it to have the opportunity to poison you?");
        EM_Settings.SulfurDioxideSeverePoisoningAmplifier = config.getInt("Sulfur Dioxide Severe Poisoning Amplifier",Configuration.CATEGORY_GENERAL, 10, 1, 65536,  "How dense the Sulfur Dioxide gas should  be, in order for it to have the opportunity to severe poison you?");
        EM_Settings.SulfurDioxidePoisoningTime = config.getInt("Sulfur Dioxide Poisoning Time",Configuration.CATEGORY_GENERAL, 200, 1, 65536,  "How long does Sulfur Dioxide poisoning last?");
        EM_Settings.SulfurDioxideSeverePoisoningTime = config.getInt("Sulfur Dioxide Severe Poisoning Time",Configuration.CATEGORY_GENERAL, 600, 1, 65536,  "How long does severe Sulfur Dioxide poisoning last?");
        EM_Settings.SulfurDioxidePoisoningLevel = config.getInt("Sulfur Dioxide Poisoning Level",Configuration.CATEGORY_GENERAL, 0, 0, 65536,  "What level of poisoning applies when player is Sulfur Dioxide poisoned?");
        EM_Settings.SulfurDioxideSeverePoisoningLevel = config.getInt("Sulfur Dioxide Severe Poisoning Level",Configuration.CATEGORY_GENERAL, 1, 0, 65536,  "What level of poisoning applies when player is severe Sulfur Dioxide poisoned?");
        EM_Settings.SulfurDioxidePoisoningChance = config.getInt("Chance of Sulfur Dioxide Poisoning",Configuration.CATEGORY_GENERAL, 5, 1, 65536,  "What is the chance of Sulfur Dioxide poisoning if the player has no protection?");

        EM_Settings.CarbonMonoxidePoisoningAmplifier = config.getInt("Carbon Monoxide Poisoning Amplifier",Configuration.CATEGORY_GENERAL, 5, 1, 65536,  "How dense the Carbon Monoxide gas should  be, in order for it to have the opportunity to poison you?");
        EM_Settings.CarbonMonoxidePoisoningTime = config.getInt("Carbon Monoxide Poisoning Time",Configuration.CATEGORY_GENERAL, 200, 1, 65536,  "How long does Carbon Monoxide poisoning last?");
        EM_Settings.CarbonMonoxidePoisoningLevel = config.getInt("Carbon Monoxide Poisoning Level",Configuration.CATEGORY_GENERAL, 0, 0, 65536,  "What level of poisoning applies when player is Carbon Monoxide poisoned?");
        EM_Settings.CarbonMonoxidePoisoningChance = config.getInt("Chance of Carbon Monoxide Poisoning",Configuration.CATEGORY_GENERAL, 5, 1, 65536,  "What is the chance of Carbon Monoxide poisoning if the player has no protection?");

        EM_Settings.HydrogenSulfidePoisoningAmplifier = config.getInt("Hydrogen Sulfide Poisoning Amplifier",Configuration.CATEGORY_GENERAL, 5, 1, 65536,  "How dense the Hydrogen Sulfide gas should  be, in order for it to have the opportunity to poison you?");
        EM_Settings.HydrogenSulfideSeverePoisoningAmplifier = config.getInt("Hydrogen Sulfide Severe Poisoning Amplifier",Configuration.CATEGORY_GENERAL, 10, 1, 65536,  "How dense the Hydrogen Sulfide gas should  be, in order for it to have the opportunity to severe poison you?");
        EM_Settings.HydrogenSulfidePoisoningTime = config.getInt("Hydrogen Sulfide Poisoning Time",Configuration.CATEGORY_GENERAL, 200, 1, 65536,  "How long does Hydrogen Sulfide poisoning last?");
        EM_Settings.HydrogenSulfideSeverePoisoningTime = config.getInt("Hydrogen Sulfide Severe Poisoning Time",Configuration.CATEGORY_GENERAL, 600, 1, 65536,  "How long does severe Hydrogen Sulfide poisoning last?");
        EM_Settings.HydrogenSulfidePoisoningLevel = config.getInt("Hydrogen Sulfide Poisoning Level",Configuration.CATEGORY_GENERAL, 0, 0, 65536,  "What level of poisoning applies when player is Hydrogen Sulfide poisoned?");
        EM_Settings.HydrogenSulfideSeverePoisoningLevel = config.getInt("Hydrogen Sulfide Severe Poisoning Level",Configuration.CATEGORY_GENERAL, 1, 0, 65536,  "What level of poisoning applies when player is severe Hydrogen Sulfide poisoned?");
        EM_Settings.HydrogenSulfidePoisoningChance = config.getInt("Chance of Hydrogen Sulfide Poisoning",Configuration.CATEGORY_GENERAL, 5, 1, 65536,  "What is the chance of Hydrogen Sulfide poisoning if the player has no protection?");

        EM_Settings.versionChecker = config.getBoolean("Version Checker", Configuration.CATEGORY_GENERAL, false, "Displays a client-side chat message on login if there's an update available.");
		EM_Settings.loggerVerbosity = config.getInt("Logger Verbosity", Configuration.CATEGORY_GENERAL, 2, 0, 3, "Amount of messaging to dump to the console."
				+ "\n0: No log messages are printed whatsoever"
				+ "\n1: Minimal log messages are printed"
				+ "\n2: All but the lowest-importance messages are printed"
				+ "\n3: Every classic EnviroMine message is printed"
				+ "\n"
				);
		EM_Settings.noNausea = config.get(Configuration.CATEGORY_GENERAL, "Blindness instead of Nausea", EM_Settings.noNausea).getBoolean(EM_Settings.noNausea);
		EM_Settings.keepStatus = config.get(Configuration.CATEGORY_GENERAL, "Keep statuses on death", EM_Settings.keepStatus).getBoolean(EM_Settings.keepStatus);
		EM_Settings.renderGear = config.get(Configuration.CATEGORY_GENERAL, "Render Gear", EM_Settings.renderGear ,"Render 3d gear worn on player. Must reload game to take effect").getBoolean(EM_Settings.renderGear);
		EM_Settings.finiteWater = config.get(Configuration.CATEGORY_GENERAL, "Finite Water", EM_Settings.finiteWater).getBoolean(EM_Settings.finiteWater);
        EM_Settings.DeathFromHeartAttack = config.getBoolean("Death From Heart Attack", Configuration.CATEGORY_GENERAL, true, "Should a player die from a heart attack if sanity < 5?");
        EM_Settings.hardcoregases = config.getBoolean("Hardcore gases", Configuration.CATEGORY_GENERAL, false, "If true, then all gases will be invisible");
        EM_Settings.HeartAttackTimeToDie = config.getInt("Heart Attack Time To Die",Configuration.CATEGORY_GENERAL, 20, 1, 65536,  "Time after which the player dies from a heart attack (for sanity > 0 but <5, it will be the same number. For sanity = 0, it will be this number / 2). To calculate the time, multiply this number by 3, this will be the time in seconds");
		EM_Settings.cauldronHeatingBlocks = config.get(Configuration.CATEGORY_GENERAL, "Cauldron Heating Blocks", EM_Settings.cauldronHeatingBlocks,
				"List of blocks that will purify a cauldron's water when placed underneath the cauldron. Of the form mod:block:meta. Append no meta value in order to use any meta."
				).getStringList();
		EM_Settings.blockTempDropoffPower = config.getFloat("Block Temperature Dropoff Power", Configuration.CATEGORY_GENERAL, 0.75F, 0.25F, 2F, "How rapidly the temperature influence of blocks falls off with your distance from them. 2 is realistic for open areas; 1 for large enclosed areas, and lower for more claustrophobic areas. Classic EnviroMine used 0.5.");
//		EM_Settings.scanRadius = config.getInt("Environment scan radius", Configuration.CATEGORY_GENERAL, 6, 1, 10, "Max distance blocks with temperature effects can affect you. Higher radius is more accurate, but more computationally expensive.");
//		EM_Settings.auraRadius = config.getFloat("Emission Aura Radius", Configuration.CATEGORY_GENERAL, 0.5F, 0F, 5F, "A block with temperature effects maxes out its effects when you're this far from the block's center. ");


		// --------------- //
		// --- Physics --- //
		// --------------- //
		int minPhysInterval = 6;
		EM_Settings.spreadIce = config.get(CATEGORY_PHYSICS, "Large Ice Cracking", EM_Settings.spreadIce , "Setting Large Ice Cracking to true can cause Massive Lag").getBoolean(EM_Settings.spreadIce );
		EM_Settings.updateCap = config.get(CATEGORY_PHYSICS, "Consecutive Physics Update Cap", EM_Settings.updateCap, "This will change maximum number of blocks that can be updated with physics at a time. - 1 = Unlimited").getInt(EM_Settings.updateCap);
		EM_Settings.physInterval = getConfigIntWithMinInt(config.get(CATEGORY_PHYSICS, "Physics Interval", minPhysInterval, "The number of ticks between physics update passes (must be " + minPhysInterval + " or more)"), minPhysInterval);
		EM_Settings.worldDelay = config.get(CATEGORY_PHYSICS, "World Start Delay", EM_Settings.worldDelay, "How long after world start until the physics system kicks in (DO NOT SET TOO LOW)").getInt(EM_Settings.worldDelay);
		EM_Settings.chunkDelay = config.get(CATEGORY_PHYSICS, "Chunk Physics Delay", EM_Settings.chunkDelay, "How long until individual chunk's physics starts after loading (DO NOT SET TOO LOW)").getInt(EM_Settings.chunkDelay);
		EM_Settings.physInterval = EM_Settings.physInterval >= 2 ? EM_Settings.physInterval : 2;
		EM_Settings.entityFailsafe = config.get(CATEGORY_PHYSICS, "Physics entity fail safe level", EM_Settings.entityFailsafe, "0 = No action, 1 = Limit to < 100 per 8x8 block area, 2 = Delete excessive entities & Dump physics (EMERGENCY ONLY)").getInt(EM_Settings.entityFailsafe);


		// --------------- //
		// --- Potions --- //
		// --------------- //
		if(!LegacyHandler.getByKey("ConfigHandlerLegacy").didRun())
		{
			// Potion IDs
			EM_Settings.hypothermiaPotionID = nextAvailPotion(EM_Settings.hypothermiaPotionID);
			EM_Settings.heatstrokePotionID = nextAvailPotion(EM_Settings.heatstrokePotionID);
			EM_Settings.frostBitePotionID = nextAvailPotion(EM_Settings.frostBitePotionID);
			EM_Settings.dehydratePotionID = nextAvailPotion(EM_Settings.dehydratePotionID);
			EM_Settings.insanityPotionID = nextAvailPotion(EM_Settings.insanityPotionID);
		}
		EM_Settings.hypothermiaPotionID = config.get(CATEGORY_POTIONS, "Hypothermia", EM_Settings.hypothermiaPotionID).getInt(EM_Settings.hypothermiaPotionID);
		EM_Settings.heatstrokePotionID = config.get(CATEGORY_POTIONS, "Heat Stroke", EM_Settings.heatstrokePotionID).getInt(EM_Settings.heatstrokePotionID);
		EM_Settings.frostBitePotionID = config.get(CATEGORY_POTIONS, "Frostbite", EM_Settings.frostBitePotionID).getInt(EM_Settings.frostBitePotionID);
		EM_Settings.dehydratePotionID = config.get(CATEGORY_POTIONS, "Dehydration", EM_Settings.dehydratePotionID).getInt(EM_Settings.dehydratePotionID);
		EM_Settings.insanityPotionID = config.get(CATEGORY_POTIONS, "Insanity", EM_Settings.insanityPotionID).getInt(EM_Settings.insanityPotionID);

		EM_Settings.enableFrostbiteGlobal = config.get(CATEGORY_POTIONS, "Enable Frostbite", EM_Settings.enableFrostbiteGlobal).getBoolean();
		EM_Settings.enableHeatstrokeGlobal = config.get(CATEGORY_POTIONS, "Enable Heat Stroke", EM_Settings.enableHeatstrokeGlobal).getBoolean();
		EM_Settings.enableHypothermiaGlobal = config.get(CATEGORY_POTIONS, "Enable Hypothermia", EM_Settings.enableHypothermiaGlobal).getBoolean();
		EM_Settings.frostbitePermanent = config.get(CATEGORY_POTIONS, "Frostbite is permanent after enough time passes", EM_Settings.enableFrostbiteGlobal).getBoolean(); // Convenience - AstroTibs

		// Config Options

		//Default Profile Override
		EM_Settings.profileSelected = config.get(CATEGORY_CONFIG, "Profile", EM_Settings.profileSelected).getString();
		EM_Settings.profileOverride = config.get(CATEGORY_CONFIG, "Override Profile", EM_Settings.profileOverride,  "Override Profile. It Can be used for servers to force profiles on servers or modpack. This Overrides any world loaded up. Name is Case Sensitive!").getBoolean(false);
		EM_Settings.enableConfigOverride = config.get(CATEGORY_CONFIG, "Client Config Override (SMP)", EM_Settings.enableConfigOverride, "[DISABLED][WIP] Temporarily overrides client configurations with the server's (NETWORK INTESIVE!)").getBoolean(EM_Settings.enableConfigOverride);


		// ------------- //
		// --- Gases --- //
		// ------------- //

		EM_Settings.noGases = config.get(CATEGORY_GASES, "Disable Gases", EM_Settings.noGases, "Disables all gases and slowly deletes existing pockets").getBoolean(EM_Settings.noGases);
		EM_Settings.slowGases = config.get(CATEGORY_GASES, "Slow Gases", EM_Settings.slowGases, "Normal gases will move extremely slowly and reduce TPS lag").getBoolean(EM_Settings.slowGases);
		EM_Settings.renderGases = config.get(CATEGORY_GASES, "Render normal gas", EM_Settings.renderGases, "Whether to render gases not normally visible").getBoolean(EM_Settings.renderGases);
		EM_Settings.gasTickRate = config.get(CATEGORY_GASES, "Gas Tick Rate", EM_Settings.gasTickRate, "How many ticks between gas updates. Gas fires are 1/4 of this.").getInt(EM_Settings.gasTickRate);
		EM_Settings.gasPassLimit = config.get(CATEGORY_GASES, "Gas Pass Limit", EM_Settings.gasPassLimit, "How many gases can be processed in a single pass per chunk (-1 = infinite)").getInt(EM_Settings.gasPassLimit);
		EM_Settings.gasWaterLike = config.get(CATEGORY_GASES, "Water like spreading", EM_Settings.gasWaterLike, "Whether gases should spread like water (faster) or even out as much as possible (realistic)").getBoolean(EM_Settings.gasWaterLike);
		// Custom ignite list
		String[] igniteList = config.getStringList("Ignite List", CATEGORY_GASES, ObjectHandler.DefaultIgnitionSources(), "List of Blocks that will ignite flamable gasses.");

		ObjectHandler.LoadIgnitionSources(igniteList);
		config.save();


	}

	private static void loadProfileConfig(File file)
	{
		Configuration config;
		try
		{
			config = new Configuration(file, true);
		} catch(Exception e)
		{
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.WARN, "Failed to load main configuration file!", e);
			return;
		}

		config.load();

		//General Settings
		EM_Settings.enablePhysics = config.get(Configuration.CATEGORY_GENERAL, "Enable Physics", EM_Settings.enablePhysics, "Turn physics On/Off").getBoolean(EM_Settings.enablePhysics);
		EM_Settings.enableLandslide = config.get(Configuration.CATEGORY_GENERAL, "Enable Physics Landslide", EM_Settings.enableLandslide).getBoolean(EM_Settings.enableLandslide);
		EM_Settings.enableSanity = config.get(Configuration.CATEGORY_GENERAL, "Allow Sanity", EM_Settings.enableSanity).getBoolean(EM_Settings.enableSanity);
		EM_Settings.enableHydrate = config.get(Configuration.CATEGORY_GENERAL, "Allow Hydration", EM_Settings.enableHydrate).getBoolean(EM_Settings.enableHydrate);
		EM_Settings.enableBodyTemp = config.get(Configuration.CATEGORY_GENERAL, "Allow Body Temperature", EM_Settings.enableBodyTemp).getBoolean(EM_Settings.enableBodyTemp);
		EM_Settings.enableAirQ = config.get(Configuration.CATEGORY_GENERAL, "Allow Air Quality", EM_Settings.enableAirQ, "True/False to turn Enviromine Trackers for Sanity, Air Quality, Hydration, and Body Temperature.").getBoolean(EM_Settings.enableAirQ);
		EM_Settings.trackNonPlayer = config.get(Configuration.CATEGORY_GENERAL, "Track NonPlayer entities", EM_Settings.trackNonPlayer, "Track enviromine properties on Non-player entities(mobs & animals)").getBoolean(EM_Settings.trackNonPlayer);
		EM_Settings.villageAssist = config.get(Configuration.CATEGORY_GENERAL, "Enable villager assistance", EM_Settings.villageAssist).getBoolean(EM_Settings.villageAssist);
		EM_Settings.foodSpoiling = config.get(Configuration.CATEGORY_GENERAL, "Enable food spoiling", EM_Settings.foodSpoiling).getBoolean(EM_Settings.foodSpoiling);
		EM_Settings.foodRotTime = config.get(Configuration.CATEGORY_GENERAL, "Default spoil time (days)", EM_Settings.foodRotTime).getInt(EM_Settings.foodRotTime);
		EM_Settings.torchesBurn = config.get(Configuration.CATEGORY_GENERAL, "Torches burn", EM_Settings.torchesBurn).getBoolean(EM_Settings.torchesBurn);
		EM_Settings.torchesGoOut = config.get(Configuration.CATEGORY_GENERAL, "Torches go out", EM_Settings.torchesGoOut).getBoolean(EM_Settings.torchesGoOut);
		EM_Settings.genFlammableCoal = config.get(Configuration.CATEGORY_GENERAL, "Generate Combustable Coal Ore", EM_Settings.genFlammableCoal, "On worldgen, replace coal ore blocks with the flammable EM variant. Turning this off does not revert previous generation.").getBoolean(EM_Settings.genFlammableCoal); // Added from request -- AstroTibs
		EM_Settings.randomizeInsanityPitch = config.get(Configuration.CATEGORY_GENERAL, "Randomize Insanity Pitch", EM_Settings.randomizeInsanityPitch, "Sounds you hear when under the influence of insanity have a randomized pitch").getBoolean(EM_Settings.randomizeInsanityPitch);
		EM_Settings.catchFireAtHighTemps = config.get(Configuration.CATEGORY_GENERAL, "Catch fire at high temperatures", EM_Settings.catchFireAtHighTemps, "Spontaneously combust when your body temperature gets high enough").getBoolean(EM_Settings.catchFireAtHighTemps);

		// Item capacities
		EM_Settings.gasMaskMax = config.get(Configuration.CATEGORY_GENERAL, "Capacity: Gas Mask", EM_Settings.gasMaskMax).getInt(EM_Settings.gasMaskMax);
		EM_Settings.filterRestore = config.get(Configuration.CATEGORY_GENERAL, "Capacity: Gas Mask Filter Restore", EM_Settings.filterRestore).getInt(EM_Settings.filterRestore);
		EM_Settings.camelPackMax = config.get(Configuration.CATEGORY_GENERAL, "Capacity: Camel Pack", EM_Settings.camelPackMax).getInt(EM_Settings.camelPackMax);
		EM_Settings.gasMaskUpdateRestoreFraction = config.getFloat("Restore Fraction: Gas Mask", Configuration.CATEGORY_GENERAL, 1F, 0F, 1F, "Fraction of your air quality deficiency restored on an update check while wearing the gas mask. 1 is \"full.\" Set to less than 1 for a more gradual restoration and so players don't simply pop the mask on intermittently to get back full air quality.");
		if (EM_Settings.gasMaskMax < 1) {EM_Settings.gasMaskMax = 1;}
		if (EM_Settings.filterRestore < 1) {EM_Settings.filterRestore = 1;}
		if (EM_Settings.camelPackMax < 1) {EM_Settings.camelPackMax = 1;}


		// ----------------------- //
		// --- Mod Integration --- //
		// ----------------------- //

		EM_Settings.streamsDrink = config.get(CATEGORY_MOD_INTEGRATION, "Streams: River Drinking", EM_Settings.streamsDrink, "Allows players to drink from river blocks from the Streams mod").getBoolean(EM_Settings.streamsDrink);
		EM_Settings.witcheryVampireImmunities = config.get(CATEGORY_MOD_INTEGRATION, "Witchery: Vampire Immunities", EM_Settings.witcheryVampireImmunities, "Vampire players gain resistances and immunities to various effects inflicted by enviromine status bars").getBoolean(EM_Settings.witcheryVampireImmunities);
		EM_Settings.witcheryWerewolfImmunities = config.get(CATEGORY_MOD_INTEGRATION, "Witchery: Werewolf Immunities", EM_Settings.witcheryWerewolfImmunities, "Players gain resistances and immunities to various effects inflicted by enviromine status bars when in wolf or werewolf form").getBoolean(EM_Settings.witcheryWerewolfImmunities);
		EM_Settings.matterOverdriveAndroidImmunities = config.get(CATEGORY_MOD_INTEGRATION, "Matter Overdrive: Android Immunities", EM_Settings.matterOverdriveAndroidImmunities, "Players who are in android form gain immunities to most EnviroMine effects").getBoolean(EM_Settings.matterOverdriveAndroidImmunities);


		// Physics Settings
		EM_Settings.stoneCracks = config.get(CATEGORY_PHYSICS, "Stone Cracks Before Falling", EM_Settings.stoneCracks).getBoolean(EM_Settings.stoneCracks);
		EM_Settings.defaultStability = config.get(CATEGORY_PHYSICS, "Default Stability Type (BlockIDs > 175)", EM_Settings.defaultStability).getString();
		EM_Settings.waterCollapse = config.get(CATEGORY_PHYSICS, "Water Causes Collapse", EM_Settings.waterCollapse).getBoolean(EM_Settings.waterCollapse); // Added out of necessity/annoyance -- AstroTibs


		// Multipliers IDs
		EM_Settings.tempMult = config.get(CATEGORY_SPEED_MULTIPLIERS, "BodyTemp", EM_Settings.tempMult).getDouble(EM_Settings.tempMult);
		EM_Settings.hydrationMult = config.get(CATEGORY_SPEED_MULTIPLIERS, "Hydration", EM_Settings.hydrationMult).getDouble(EM_Settings.hydrationMult);
		EM_Settings.airMult = config.get(CATEGORY_SPEED_MULTIPLIERS, "AirQuality", EM_Settings.airMult).getDouble(EM_Settings.airMult);
		EM_Settings.sanityMult = config.get(CATEGORY_SPEED_MULTIPLIERS, "Sanity", EM_Settings.sanityMult).getDouble(EM_Settings.sanityMult);

		EM_Settings.tempMult = EM_Settings.tempMult < 0 ? 0F : EM_Settings.tempMult;
		EM_Settings.hydrationMult = EM_Settings.hydrationMult < 0 ? 0F : EM_Settings.hydrationMult;
		EM_Settings.airMult = EM_Settings.airMult < 0 ? 0F : EM_Settings.airMult;
		EM_Settings.sanityMult = EM_Settings.sanityMult < 0 ? 0F : EM_Settings.sanityMult;

		// Config Options
		Property genConfig = config.get(CATEGORY_CONFIG, "Generate Blank Configs", false, "Will attempt to find and generate blank configs for any custom items/blocks/etc loaded before "+EM_Settings.MOD_NAME+". Pack developers are highly encouraged to enable this! (Resets back to false after use)");
		if(!EM_Settings.genConfigs)
		{
			EM_Settings.genConfigs = genConfig.getBoolean(false);
		}
		genConfig.set(false);

		Property genDefault = config.get(CATEGORY_CONFIG, "Generate Defaults", true, "Generates "+EM_Settings.MOD_NAME+"'s initial default files");
		if(!EM_Settings.genDefaults)
		{
			EM_Settings.genDefaults = genDefault.getBoolean(true);
		}
		genDefault.set(false);

		EM_Settings.enableConfigOverride = config.get(CATEGORY_CONFIG, "Client Config Override (SMP)", EM_Settings.enableConfigOverride, "[DISABLED][WIP] Temporarily overrides client configurations with the server's (NETWORK INTESIVE!)").getBoolean(EM_Settings.enableConfigOverride);

		// Earthquake
		EM_Settings.enableQuakes = config.get(CATEGORY_EARTHQUAKES, "Enable Earthquakes", EM_Settings.enableQuakes).getBoolean(EM_Settings.enableQuakes);
		EM_Settings.quakePhysics = config.get(CATEGORY_EARTHQUAKES, "Triggers Physics", EM_Settings.quakePhysics, "Can cause major lag at times (Requires main physics to be enabled)").getBoolean(EM_Settings.quakePhysics);
		EM_Settings.quakeRarity = config.get(CATEGORY_EARTHQUAKES, "Rarity", EM_Settings.quakeRarity).getInt(EM_Settings.quakeRarity);
		if(EM_Settings.quakeRarity < 0)
		{
			EM_Settings.quakeRarity = 0;
		}

		// Easter Eggs!
		EM_Settings.thingChance = config.getFloat("Cave Dimension Grue", CATEGORY_EASTER_EGGS, 0.000001F, 0F, 1F, "Chance the (extremely rare) grue in the cave dimension will attack in the dark (ignored on Halloween or Friday 13th)");

		config.save();
	}

	/**
	 * @deprecated Use config.getInt(...) instead as it provides min & max value caps
	 */
	@Deprecated
	private static int getConfigIntWithMinInt(Property prop, int min)
	{
		if (prop.getInt(min) >= min) {
			return prop.getInt(min);
		} else {
			prop.set(min);
			return min;
		}
	}

	static int nextAvailPotion(int startID)
	{
		for(int i = startID; i > 0; i++)
		{
			if(i == EM_Settings.hypothermiaPotionID || i == EM_Settings.heatstrokePotionID || i == EM_Settings.frostBitePotionID || i == EM_Settings.dehydratePotionID || i == EM_Settings.insanityPotionID)
			{
				continue;
			} else if(i >= Potion.potionTypes.length)
			{
				return i;
			} else if(Potion.potionTypes[i] == null)
			{
				return i;
			}
		}

		return startID;
	}

	//#######################################
	//#          Get File List              #
	//#This Grabs Directory List for Custom #
	//#######################################
	private static File[] GetFileList(String path)
	{

		// Will be used Auto Load Custom Objects from ??? Dir
		File f = new File(path);
		File[] list = f.listFiles();
		list = list != null? list : new File[0];

		return list;
	}

	private static boolean isCFGFile(File file)
	{
		String fileName = file.getName();

		if(file.isHidden()) return false;

		//Matcher
		String patternString = "(.*\\.cfg$)";

		Pattern pattern;
		Matcher matcher;
		// Make Sure its a .cfg File
		pattern = Pattern.compile(patternString);
		matcher = pattern.matcher(fileName);

		String MacCheck = ".DS_Store.cfg";

		if (matcher.matches() && matcher.group(0).toString().toLowerCase() == MacCheck.toLowerCase()) { return false;}

		return matcher.matches();
	}

	//###################################
	//#           Check Dir             #
	//#  Checks for, or makes Directory #
	//###################################
	public static void CheckDir(File Dir)
	{
		boolean dirFlag = false;

		// create File object

		if(Dir.exists())
		{
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.ALL.getLevel()) EnviroMine.logger.log(Level.INFO, "Dir already exists:"+ Dir.getName());
			return;
		}

		try
		{
			Dir.setWritable(true);
			dirFlag = Dir.mkdirs();
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.ALL.getLevel()) EnviroMine.logger.log(Level.INFO, "Created new Folder "+ Dir.getName());
		} catch(Exception e)
		{
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.ERROR, "Error occured while creating config directory: " + Dir.getAbsolutePath(), e);
		}

		if(!dirFlag)
		{
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.ERROR, "Failed to create config directory: " + Dir.getAbsolutePath());
		}
	}

	/**
	 * Load Custom Objects
	 * Used to Load Custom Blocks,Armor
	 * Entitys, & Items from Custom Config Dir
	 */
	private static void LoadCustomObjects(File customFiles)
	{
		boolean datFile = isCFGFile(customFiles);

		// Check to make sure this is a Data File Before Editing
		if(datFile == true)
		{
			Configuration config;
			try
			{
				config = new Configuration(customFiles, true);

				//EnviroMine.logger.log(Level.INFO, "Loading Config File: " + customFiles.getAbsolutePath());

				config.load();


			// 	Grab all Categories in File
			List<String> catagory = new ArrayList<String>();
			Set<String> nameList = config.getCategoryNames();
			Iterator<String> nameListData = nameList.iterator();

			// add Categories to a List
			while(nameListData.hasNext())
			{
				catagory.add(nameListData.next());
			}

			for(int x = 0; x < catagory.size(); x++)
			{
				String CurCat = catagory.get(x);

				if(!CurCat.isEmpty() && CurCat.contains(Configuration.CATEGORY_SPLITTER))
				{
					String parent = CurCat.split("\\" + Configuration.CATEGORY_SPLITTER)[0];

					if(propTypes.containsKey(parent) && propTypes.get(parent).useCustomConfigs())
					{
						PropertyBase property = propTypes.get(parent);
						property.LoadProperty(config, catagory.get(x));
					} else
					{
						if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.NORMAL.getLevel()) EnviroMine.logger.log(Level.WARN, "Failed to load object " + CurCat);
					}

				}
			}

			config.save();

			// Add to list of loaded Config files
			loadedConfigs.add(config.getConfigFile().getName());

			} catch(Exception e)
			{
				e.printStackTrace();
				if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.ERROR, "FAILED TO LOAD CUSTOM CONFIG: " + customFiles.getName() + "\nNEW SETTINGS WILL BE IGNORED!", e);
				return;
			}
		}


	}

	public static ArrayList<String> getSubCategories(Configuration config, String mainCat)
	{
		ArrayList<String> category = new ArrayList<String>();
		Set<String> nameList = config.getCategoryNames();
		Iterator<String> nameListData = nameList.iterator();

		// add Categories to a List
		while(nameListData.hasNext())
		{
			String catName = nameListData.next();

			if(catName.startsWith(mainCat + "."))
			{
				category.add(catName);
			}
		}

		return category;
	}

	public static String getProfileName()
	{
		return getProfileName(loadedProfile);
	}

	public static String getProfileName(String profile)
	{
		return profile.substring(profilePath.length(),profile.length()-1).toUpperCase();
	}

	public static boolean ReloadConfig()
	{
				 	try
				 	{

				 		EM_Settings.armorProperties.clear();
				 		EM_Settings.blockProperties.clear();
				 		EM_Settings.itemProperties.clear();
				 		EM_Settings.livingProperties.clear();
				 		EM_Settings.stabilityTypes.clear();
				 		EM_Settings.biomeProperties.clear();
				 		EM_Settings.dimensionProperties.clear();
				 		EM_Settings.rotProperties.clear();
				 		EM_Settings.caveGenProperties.clear();
				 		EM_Settings.caveSpawnProperties.clear();

				 		int Total = initConfig();

				 		initProfile();

				 		EnviroMine.caves.RefreshSpawnList();
				 		return true;

				 	} //try
					catch(NullPointerException e)
					{
						return false;
					}


	}

	public static void loadDefaultProperties()
	{
		Iterator<PropertyBase> iterator = propTypes.values().iterator();

		while(iterator.hasNext())
		{
			iterator.next().GenDefaults();
		}
	}

	public static Configuration getConfigFromObj(Object obj)
	{
		String ModID = ModIdentification.idFromObject(obj);

		File configFile = new File(loadedProfile+ customPath + ModID +".cfg");

		Configuration config;
		try
		{
			config = new Configuration(configFile, true);
		} catch(NullPointerException e)
		{
			e.printStackTrace();
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.WARN, "FAILED TO LOAD Config from OBJECT TO "+ModID+".cfg");
			return null;
		} catch(StringIndexOutOfBoundsException e)
		{
			e.printStackTrace();
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.WARN, "FAILED TO LOAD Config from OBJECT TO "+ModID+".cfg");
			return null;
		}


		return config;
	}

	public static String SaveMyCustom(Object obj)
	{
		return SaveMyCustom(obj, null);
	}

	public static String SaveMyCustom(Object obj, Object type)
	{

		String ModID = ModIdentification.idFromObject(obj);


		// Check to make sure this is a Data File Before Editing
		File configFile = new File(loadedProfile+ customPath + ModID +".cfg");

		Configuration config;
		try
		{
			config = new Configuration(configFile, true);
		} catch(NullPointerException e)
		{
			e.printStackTrace();
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.WARN, "FAILED TO SAVE NEW OBJECT TO "+ModID+".cfg");
			return "Failed to Open "+ModID+".cfg";
		} catch(StringIndexOutOfBoundsException e)
		{
			e.printStackTrace();
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.WARN, "FAILED TO SAVE NEW OBJECT TO "+ModID+".cfg");
			return "Failed to Open "+ModID+".cfg";
		}

		config.load();

		String returnValue = "";

		if(obj instanceof Block)
		{

			BlockProperties.base.generateEmpty(config, obj);
			returnValue = "(Block) Saved to "+ ModID + ".cfg on Profile "+ getProfileName();

		} else if(obj instanceof Entity)
		{

			Entity en = (Entity) obj;
			int id = 0;
			if(EntityList.getEntityID(en) > 0)
			{
				id = EntityList.getEntityID(en);
			} else if(EntityRegistry.instance().lookupModSpawn(en.getClass(), false) != null)
			{
				id = EntityRegistry.instance().lookupModSpawn(en.getClass(), false).getModEntityId() + 128;
			} else
			{
				returnValue = "Failed to add config entry. " + en.getCommandSenderName() + " has no ID!";
				if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.NORMAL.getLevel()) EnviroMine.logger.log(Level.WARN, "Failed to add config entry. " + en.getCommandSenderName() + " has no ID!");
			}
			EntityProperties.base.generateEmpty(config, id);
			returnValue = "(Entity) Saved to "+ ModID + ".cfg on Profile "+ getProfileName();
		} else if(obj instanceof Item && type == null )
		{
				ItemProperties.base.generateEmpty(config, obj);
				returnValue = "(Item) Saved to "+ ModID + ".cfg on Profile "+ getProfileName();
		} else if(obj instanceof ItemArmor && type instanceof ArmorProperties)
		{
				ArmorProperties.base.generateEmpty(config, obj);
				returnValue = "(ItemArmor) Saved to "+ ModID + ".cfg on Profile "+ getProfileName();
		}

		config.save();


		return returnValue;

		//return null;
	}

	private void removeProperty(Configuration config, String oldCat, String propName)
	{
		String remove = "Remove";
		config.moveProperty(oldCat, propName, remove);
		config.removeCategory(config.getCategory(remove));
	}

} // End of Page

