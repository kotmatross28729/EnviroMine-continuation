package enviromine.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.world.biome.BiomeGenBase;
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

    private static final String CATEGORY_KOTMATROSS_FORK_CHANGES = "Kotmatross Fork Changes";

	// Values
	public static final int HUD_ID_AIR_QUALITY = 3;
	public static final int HUD_ID_BODY_TEMPERATURE = 0;
	public static final int HUD_ID_HYDRATION = 1;
	public static final int HUD_ID_SANITY = 2;

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


        // -------------------- //
        // --- Fork Changes --- //
        // -------------------- //

        EM_Settings.BodyTempBest = config.getFloat("1-1_HBMBodyTempBest", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTempBest , -65536F, 65536F, "Hbm, subtracted if: armor completely sealed and hbm temperature influence < -700 & > -1000, or HEV/Environment Suit and influence < -500 & > -700. OR added if: no fsb armor, no fireResistance, and hbm.isBurning");
        EM_Settings.BodyTempVeryGood = config.getFloat("1-2_HBMBodyTempVeryGood", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTempVeryGood , -65536F, 65536F, "Hbm, subtracted if: armor completely sealed and hbm.isFrozen");
        EM_Settings.BodyTempGood = config.getFloat("1-3_HBMBodyTempGood", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTempGood , -65536F, 65536F, "Hbm, subtracted if: HEV/Environment Suit suit and hbm temperature influence < -700 & > -1000, or no fsb armor and influence < -500 & > -700");
        EM_Settings.BodyTempBad = config.getFloat("1-4_HBMBodyTempBad", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTempBad , -65536F, 65536F, "Hbm, subtracted if: HEV/Environment Suit and player is frozen, or no fsb armor and influence < -700 & > -1000");
        EM_Settings.BodyTempWorst = config.getFloat("1-5_HBMBodyTempWorst", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTempWorst , -65536F, 65536F, "Hbm, subtracted if: no fsb armor and hbm.isFrozen");
        EM_Settings.BodyTempSleep = config.getFloat("1-5-2_BodyTempSleep", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTempSleep , -65536F, 65536F, "Maybe add");

        EM_Settings.StrongArmorMaxTemp = config.getFloat("1-6_StrongArmorMaxTemp", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.StrongArmorMaxTemp, -65536F, 65536F, "If the armor has 12.Is Temperature Sealed = true, or the armor is ArmorFSB from hbm's ntm, which has the \"Fireproof\" characteristic, then the body temperature will be maintained at 36.6, if at the moment the player's body temperature does not exceed n");
        EM_Settings.StrongArmorMinTemp = config.getFloat("1-6-2_StrongArmorMinTemp", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.StrongArmorMinTemp, -65536F, 65536F, "If the armor has 12.Is Temperature Sealed = true, or the armor is ArmorFSB from hbm's ntm, which has the \"Fireproof\" characteristic, then the body temperature will be maintained at 36.6, if at the moment the player's body temperature is not less than n");
        EM_Settings.LightArmorMaxTemp = config.getFloat("1-7_LightArmorMaxTemp", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.LightArmorMaxTemp, -65536F, 65536F, "If the armor has 11.Is Temperature Resistance = true, or the armor is HEV/Environment Suit from hbm's ntm, then the body temperature will be maintained at 36.6, if at the moment the player's body temperature does not exceed n");
        EM_Settings.LightArmorMinTemp = config.getFloat("1-7-2_LightArmorMinTemp", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.LightArmorMinTemp, -65536F, 65536F, "If the armor has 11.Is Temperature Resistance = true, or the armor is HEV/Environment Suit from hbm's ntm, then the body temperature will be maintained at 36.6, if at the moment the player's body temperature is not less than n");
        EM_Settings.LavaBlockAmbientTemperature = config.getFloat("1-8_LavaBlockAmbientTemperature", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.LavaBlockAmbientTemperature, -65536F, 65536F, "Ambient Temperature will be equal to this number if the player is in lava (if the armor has 12.Is Temperature Sealed = true, then no)");
        EM_Settings.BurningambientTemperature = config.getFloat("1-9_BurningambientTemperature", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BurningambientTemperature, -65536F, 65536F, "Ambient Temperature will be equal to this number if the player is on fire (or in lava if11.Is Temperature Resistance = true, if the armor has 11.Is Temperature Resistance = true, then no)");
        EM_Settings.RiseSpeedMin = config.getFloat("10_RiseSpeedMin", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.RiseSpeedMin, -65536F, 65536F, "Minimum RiseSpeed if player is on fire");
        EM_Settings.RiseSpeedLava = config.getFloat("11_RiseSpeedLava", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.RiseSpeedLava, -65536F, 65536F, "RiseSpeed if player in lava");
        EM_Settings.RiseSpeedLavaDecr = config.getFloat("12_RiseSpeedLavaDecr", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.RiseSpeedLavaDecr, -65536F, 65536F, "RiseSpeed if player in lava and wearing hev/env suit or armor has 11.Is Temperature Resistance = true");
        EM_Settings.SprintambientTemperature = config.getFloat("13_SprintAmbientTemperature", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SprintambientTemperature, -65536F, 65536F, "The player's temperature will increase by this number when he runs");
        EM_Settings.SweatTemperature = config.getFloat("13-2_SweatTemperature", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SweatTemperature, -65536F, 65536F, "The player will begin to sweat starting at this temperature");

        EM_Settings.SweatDehydrate = config.getFloat("13-3_SweatDehydrate", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SweatDehydrate, -65536F, 65536F, "When a player sweats, he will lose n% of water per second");
        EM_Settings.SweatHydration = config.getFloat("13-4_SweatHydration", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SweatHydration, -65536F, 65536F, "The player needs n (n or more)% of water to decrease temperature (when sweats)");
        EM_Settings.SweatBodyTemp = config.getFloat("13-5_SweatBodyTemp", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SweatBodyTemp, -65536F, 65536F, "If the player has enough %water (see SweatHydration), then the body temperature will decrease by n every second (when sweats)");

        EM_Settings.DeathFromHeartAttack = config.getBoolean("14_Death From Heart Attack", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DeathFromHeartAttack, "Should a player die from a heart attack if sanity < 5?");
        EM_Settings.HeartAttackTimeToDie = config.getInt("15_Heart Attack Time To Die",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HeartAttackTimeToDie, 1, 65536,  "Time after which the player dies from a heart attack (for sanity > 0 but <5, it will be the same number. For sanity = 0, it will be this number / 2). To calculate the time, multiply this number by 3, this will be the time in seconds");
        EM_Settings.hardcoregases = config.getBoolean("16_Hardcore gases", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.hardcoregases, "If true, then all gases will be invisible");

        EM_Settings.HbmGasMaskBreakMultiplier = config.getInt("17_Hbm Gas Mask filter break multiplier", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HbmGasMaskBreakMultiplier, 0, 65536,  "Multiplier for breaking a hbm gas mask filter");
        EM_Settings.HbmGasMaskBreakChanceNumber = config.getInt("18_Hbm Gas Mask filter break chance number", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HbmGasMaskBreakChanceNumber, 0, 100,  "The number on which the chance of reducing the durability of the hbm filter depends. The lower the number, the greater the chance that the filter durability will decrease");
        EM_Settings.EnviromineGasMaskBreakMultiplier = config.getInt("19_Gas Masks break multiplier", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.EnviromineGasMaskBreakMultiplier, 0, 65536,  "A number that is subtracted from the current enviromine mask filter durability if the player is in a gas block, that is suffocating");

        //GAS
        EM_Settings.SulfurDioxideGasDebugLogger = config.getBoolean("20_SulfurDioxideGasDebugLogger", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SulfurDioxideGasDebugLogger, "Don't use unless you are a developer");
        EM_Settings.CarbonMonoxideGasDebugLogger = config.getBoolean("21_CarbonMonoxideGasDebugLogger", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CarbonMonoxideGasDebugLogger, "Don't use unless you are a developer");
        EM_Settings.HydrogenSulfideGasDebugLogger = config.getBoolean("22_HydrogenSulfideGasDebugLogger", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HydrogenSulfideGasDebugLogger, "Don't use unless you are a developer");

        EM_Settings.SulfurDioxidePoisoningAmplifier = config.getInt("23_Sulfur Dioxide Poisoning Amplifier", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SulfurDioxidePoisoningAmplifier, 1, 65536,  "How dense the Sulfur Dioxide gas should  be, in order for it to have the opportunity to poison you?");
        EM_Settings.SulfurDioxideSeverePoisoningAmplifier = config.getInt("24_Sulfur Dioxide Severe Poisoning Amplifier", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SulfurDioxideSeverePoisoningAmplifier, 1, 65536,  "How dense the Sulfur Dioxide gas should  be, in order for it to have the opportunity to severe poison you?");
        EM_Settings.SulfurDioxidePoisoningTime = config.getInt("25_Sulfur Dioxide Poisoning Time", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SulfurDioxidePoisoningTime, 1, 65536,  "How long does Sulfur Dioxide poisoning last?");
        EM_Settings.SulfurDioxideSeverePoisoningTime = config.getInt("26_Sulfur Dioxide Severe Poisoning Time", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SulfurDioxideSeverePoisoningTime, 1, 65536,  "How long does severe Sulfur Dioxide poisoning last?");
        EM_Settings.SulfurDioxidePoisoningLevel = config.getInt("27_Sulfur Dioxide Poisoning Level", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SulfurDioxidePoisoningLevel, 0, 65536,  "What level of poisoning applies when player is Sulfur Dioxide poisoned?");
        EM_Settings.SulfurDioxideSeverePoisoningLevel = config.getInt("28_Sulfur Dioxide Severe Poisoning Level", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SulfurDioxideSeverePoisoningLevel, 0, 65536,  "What level of poisoning applies when player is severe Sulfur Dioxide poisoned?");
        EM_Settings.SulfurDioxidePoisoningChance = config.getInt("29_Chance of Sulfur Dioxide Poisoning", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SulfurDioxidePoisoningChance, 1, 65536,  "What is the chance of Sulfur Dioxide poisoning if the player has no protection?");

        EM_Settings.CarbonMonoxidePoisoningAmplifier = config.getInt("30_Carbon Monoxide Poisoning Amplifier", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CarbonMonoxidePoisoningAmplifier, 1, 65536,  "How dense the Carbon Monoxide gas should  be, in order for it to have the opportunity to poison you?");
        EM_Settings.CarbonMonoxidePoisoningTime = config.getInt("31_Carbon Monoxide Poisoning Time", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CarbonMonoxidePoisoningTime, 1, 65536,  "How long does Carbon Monoxide poisoning last?");
        EM_Settings.CarbonMonoxidePoisoningLevel = config.getInt("32_Carbon Monoxide Poisoning Level", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CarbonMonoxidePoisoningLevel, 0, 65536,  "What level of poisoning applies when player is Carbon Monoxide poisoned?");
        EM_Settings.CarbonMonoxidePoisoningChance = config.getInt("33_Chance of Carbon Monoxide Poisoning", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CarbonMonoxidePoisoningChance, 1, 65536,  "What is the chance of Carbon Monoxide poisoning if the player has no protection?");

        EM_Settings.HydrogenSulfidePoisoningAmplifier = config.getInt("34_Hydrogen Sulfide Poisoning Amplifier", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HydrogenSulfidePoisoningAmplifier, 1, 65536,  "How dense the Hydrogen Sulfide gas should  be, in order for it to have the opportunity to poison you?");
        EM_Settings.HydrogenSulfideSeverePoisoningAmplifier = config.getInt("35_Hydrogen Sulfide Severe Poisoning Amplifier", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HydrogenSulfideSeverePoisoningAmplifier, 1, 65536,  "How dense the Hydrogen Sulfide gas should  be, in order for it to have the opportunity to severe poison you?");
        EM_Settings.HydrogenSulfidePoisoningTime = config.getInt("36_Hydrogen Sulfide Poisoning Time", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HydrogenSulfidePoisoningTime, 1, 65536,  "How long does Hydrogen Sulfide poisoning last?");
        EM_Settings.HydrogenSulfideSeverePoisoningTime = config.getInt("37_Hydrogen Sulfide Severe Poisoning Time", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HydrogenSulfideSeverePoisoningTime, 1, 65536,  "How long does severe Hydrogen Sulfide poisoning last?");
        EM_Settings.HydrogenSulfidePoisoningLevel = config.getInt("38_Hydrogen Sulfide Poisoning Level", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HydrogenSulfidePoisoningLevel, 0, 65536,  "What level of poisoning applies when player is Hydrogen Sulfide poisoned?");
        EM_Settings.HydrogenSulfideSeverePoisoningLevel = config.getInt("39_Hydrogen Sulfide Severe Poisoning Level", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HydrogenSulfideSeverePoisoningLevel, 0, 65536,  "What level of poisoning applies when player is severe Hydrogen Sulfide poisoned?");
        EM_Settings.HydrogenSulfidePoisoningChance = config.getInt("40_Chance of Hydrogen Sulfide Poisoning", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HydrogenSulfidePoisoningChance, 1, 65536,  "What is the chance of Hydrogen Sulfide poisoning if the player has no protection?");

        EM_Settings.enablePlayerRandomMobRender = config.getBoolean("41_Player Insanity Random Mob Render", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.enablePlayerRandomMobRender, "If true, then if a player has the insanity effect, his model is replaced with a random mob. Causes bugs with the model");

        //MACHINES

        EM_Settings.EnableHBMMachinesHeat = config.getBoolean("42-0X_Enable HBM Machines Heat", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.EnableHBMMachinesHeat, "Enable ambient temperature heating mechanics for hbm's ntm machines");

        EM_Settings.BurnerPressHeatDivisor = config.getFloat("42-1_BurnerPressHeatDivisor", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BurnerPressHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 1600 temperature");
        EM_Settings.BurnerPressHeatHardCap = config.getFloat("42-2_BurnerPressHeatHardCap", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BurnerPressHeatHardCap, 1, 65536,  "Hard temperature limit (cannot exceed this value)");
        EM_Settings.FireboxHeatDivisor = config.getFloat("43_Firebox Heat Divisor", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.FireboxHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 200 temperature");
        EM_Settings.HeaterOvenHeatDivisor = config.getFloat("44_Heater Oven Heat Divisor", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HeaterOvenHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 1000 temperature");
        EM_Settings.FluidBurnerHeatDivisor = config.getFloat("45_FluidBurnerHeatDivisor", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.FluidBurnerHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 100_000 temperature");

        EM_Settings.HeaterElectricHeatDivisor = config.getFloat("46-1_HeaterElectricHeatDivisor", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HeaterElectricHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max (average) - 10_000 temperature");
        EM_Settings.HeaterElectricHeatHardCap = config.getFloat("46-2_HeaterElectricHeatDivisor", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HeaterElectricHeatHardCap, 1, 65536,  "Hard temperature limit (cannot exceed this value)");

        EM_Settings.IronFurnaceHeatDivisor = config.getFloat("47-1_IronFurnaceHeatDivisor", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.IronFurnaceHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 2000 temperature");
        EM_Settings.IronFurnaceHeatHardCap = config.getFloat("47-2_IronFurnaceHeatHardCap", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.IronFurnaceHeatHardCap, 1, 65536,  "Hard temperature limit (cannot exceed this value)");

        EM_Settings.SteelFurnaceHeatDivisor = config.getFloat("48_SteelFurnaceHeatDivisor", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SteelFurnaceHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 100_000 temperature");
        EM_Settings.CombinationOvenHeatDivisor = config.getFloat("49_CombinationOvenHeatDivisor", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CombinationOvenHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 100_000 temperature");

        EM_Settings.CrucibleHeatDivisor = config.getFloat("50_CrucibleHeatDivisor", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CrucibleHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 100_000 temperature");

        EM_Settings.BoilerHeatDivisor = config.getFloat("51-1_BoilerHeatDivisor", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BoilerHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Boiler specifics - valid up to 100_000 TU");
        EM_Settings.BoilerHeaterOvenDivisorConstant = config.getFloat("51-2_BoilerHeaterOvenDivisorConstant", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BoilerHeaterOvenDivisorConstant, 1, 65536,  "Divider value for temperature. Boiler specifics - valid up to 500_000 TU");
        EM_Settings.BoilerMAXDivisorConstant = config.getFloat("51-3_BoilerMAXDivisorConstant", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BoilerMAXDivisorConstant, 1, 65536,  "Divider value for temperature. Boiler specifics - valid up to 3_200_000 TU (in fact up to 999_001 TU)");

        EM_Settings.BoilerIndustrialHeatDivisor = config.getFloat("52-1_BoilerIndustrialHeatDivisor", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BoilerIndustrialHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Boiler specifics - valid up to 100_000 TU");
        EM_Settings.BoilerIndustrialHeaterOvenDivisorConstant = config.getFloat("52-2_BoilerIndustrialHeaterOvenDivisorConstant", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BoilerIndustrialHeaterOvenDivisorConstant, 1, 65536,  "Divider value for temperature. Boiler specifics - valid up to 500_000 TU");
        EM_Settings.BoilerIndustrialMAXDivisorConstant = config.getFloat("52-3_BoilerIndustrialMAXDivisorConstant", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BoilerIndustrialMAXDivisorConstant, 1, 65536,  "Divider value for temperature. Boiler specifics - valid up to 12_800_000 TU (in fact up to 999_001 TU)");

        EM_Settings.FurnaceBrickHeatDivisor = config.getFloat("53-1_FurnaceBrickHeatDivisor",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.FurnaceBrickHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 1600 temperature");
        EM_Settings.FurnaceBrickHeatHardCap = config.getFloat("53-2_FurnaceBrickHeatHardCap",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.FurnaceBrickHeatHardCap, 1, 65536,  "Hard temperature limit (cannot exceed this value)");

        EM_Settings.DiFurnaceHeatDivisor = config.getFloat("54_DiFurnaceHeatDivisor",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DiFurnaceHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 12_800 temperature");
        EM_Settings.DiFurnaceRTGHeatDivisor = config.getFloat("55_DiFurnaceRTGHeatDivisor",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DiFurnaceRTGHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Power level (max) = 600 X6 = 3600 (temperature)");
        EM_Settings.NukeFurnaceHeatDivisor = config.getFloat("56_NukeFurnaceHeatDivisor",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.NukeFurnaceHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Operations (max) = 200");

        EM_Settings.RTGFurnaceHeatConstant = config.getFloat("57_RTGFurnaceHeatConstant",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.RTGFurnaceHeatConstant, 1, 65536,  "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.WoodBurningGenHeatDivisor = config.getFloat("58_WoodBurningGenHeatDivisor",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.WoodBurningGenHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 1600 temperature");
        EM_Settings.DieselGenHeatConstant = config.getFloat("59_DieselGenHeatConstant",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DieselGenHeatConstant, 1, 65536,  "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.ICEHeatConstant = config.getFloat("60_ICEHeatConstant",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.ICEHeatConstant, 1, 65536,  "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.CyclotronHeatConstant = config.getFloat("61_CyclotronHeatConstant",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CyclotronHeatConstant, 1, 65536,  "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.GeothermalGenHeatDivisor = config.getFloat("62_GeothermalGenHeatDivisor",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.GeothermalGenHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine");

        EM_Settings.RBMKRodHeatDivisor = config.getFloat("63-1_RBMKRodHeatDivisor",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.RBMKRodHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine");
        EM_Settings.RBMKRodHeatHardCap = config.getFloat("63-2_RBMKRodHeatHardCap",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.RBMKRodHeatHardCap, 1, 65536,  "Hard temperature limit (cannot exceed this value)");

        EM_Settings.ArcFurnaceHeatConstant = config.getFloat("64_ArcFurnaceHeatConstant",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.ArcFurnaceHeatConstant, 1, 65536,  "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.FlareStackHeatConstant = config.getFloat("65_FlareStackHeatConstant",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.FlareStackHeatConstant, 1, 65536,  "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.CokerHeatDivisor = config.getFloat("66_CokerHeatDivisor",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CokerHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine");

        EM_Settings.TurbofanHeatConstant = config.getFloat("67-1_TurbofanHeatConstant",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.TurbofanHeatConstant, 1, 65536,  "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.TurbofanAfterburnerHeatConstant = config.getFloat("67-2_TurbofanAfterburnerHeatConstant",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.TurbofanAfterburnerHeatConstant, 1, 65536,  "The ambient temperature from this machine is a constant you can specify (in Celsius degrees). Turbofan specifics - temperature when using the \"Afterburner\" upgrade");

        EM_Settings.CCGasTurbineHeatDivisor = config.getFloat("68_CCGasTurbineHeatDivisor",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CCGasTurbineHeatDivisor, 1, 65536,  "Divider value for temperature. The temperature itself depends on the specifics of the machine");

        //MACHINES END


        EM_Settings.enableItemPropsDivideByTwo = config.getBoolean("69_enableItemPropsDivideByTwo", CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.EnableHBMMachinesHeat, "If true, then divides ItemProperties \"Effect n\" parameters by 2, since for some reason they are applied 2 times. If your values specified in \"Effect n\" are not applied as they are, but are 2 times smaller, then disable this option (you may have some kind of bugfix mod installed)");


        EM_Settings.FrostyWaterTemperatureInfluence = config.getFloat("70_FrostyWaterTemperatureInfluence",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.FrostyWaterTemperatureInfluence, 1, 65536,  "When the player drinks this type of water, their temperature changes by this value");
        EM_Settings.DirtyColdWaterTemperatureInfluence = config.getFloat("71_DirtyColdWaterTemperatureInfluence",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DirtyColdWaterTemperatureInfluence, 1, 65536,  "When the player drinks this type of water, their temperature changes by this value");
        EM_Settings.CleanColdWaterTemperatureInfluence = config.getFloat("72_CleanColdWaterTemperatureInfluence",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CleanColdWaterTemperatureInfluence, 1, 65536,  "When the player drinks this type of water, their temperature changes by this value");
        EM_Settings.SaltyWaterTemperatureInfluence = config.getFloat("73_SaltyWaterTemperatureInfluence",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SaltyWaterTemperatureInfluence, 1, 65536,  "When the player drinks this type of water, their temperature changes by this value");
        EM_Settings.DirtyWaterTemperatureInfluence = config.getFloat("74_DirtyWaterTemperatureInfluence",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DirtyWaterTemperatureInfluence, 1, 65536,  "When the player drinks this type of water, their temperature changes by this value");
        EM_Settings.CleanWaterTemperatureInfluence = config.getFloat("75_CleanWaterTemperatureInfluence",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CleanWaterTemperatureInfluence, 1, 65536,  "When the player drinks this type of water, their temperature changes by this value");
        EM_Settings.CleanWarmWaterTemperatureInfluence = config.getFloat("76_CleanWarmWaterTemperatureInfluence",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CleanWarmWaterTemperatureInfluence, 1, 65536,  "When the player drinks this type of water, their temperature changes by this value");
        EM_Settings.DirtyWarmWaterTemperatureInfluence = config.getFloat("77_DirtyWarmWaterTemperatureInfluence",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DirtyWarmWaterTemperatureInfluence, 1, 65536,  "When the player drinks this type of water, their temperature changes by this value");
        EM_Settings.HotWarmWaterTemperatureInfluence = config.getFloat("78_HotWarmWaterTemperatureInfluence",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HotWarmWaterTemperatureInfluence, 1, 65536,  "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.WaterReducesTemperatureStartingValue = config.getFloat("79_WaterReducesTemperatureStartingValue",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.WaterReducesTemperatureStartingValue, 1, 65536,  "The temperature will change when the player drinks salt/clean/dirty water and his body temperature is >= this value");
        EM_Settings.ColdFrostyWaterReducesTemperatureStartingValue = config.getFloat("80_ColdFrostyWaterReducesTemperatureStartingValue",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.ColdFrostyWaterReducesTemperatureStartingValue, 1, 65536,  "The temperature will change when the player drinks cold/frosty water and his body temperature is >= this value");
        EM_Settings.WarmHotWaterReducesTemperatureStartingValue = config.getFloat("81_WarmHotWaterReducesTemperatureStartingValue",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.WarmHotWaterReducesTemperatureStartingValue, 1, 65536,  "The temperature will change when the player drinks warm/hot water and his body temperature is >= this value");

        EM_Settings.FrostyWaterHydrate          = config.getFloat("82_FrostyWaterHydrate",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.FrostyWaterHydrate, 1, 65536,  "When a player drinks this type of water, their hydration will increase by that amount");
        EM_Settings.DirtyColdWaterHydrate       = config.getFloat("83_DirtyColdWaterHydrate",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DirtyColdWaterHydrate, 1, 65536,  "When a player drinks this type of water, their hydration will increase by that amount");
        EM_Settings.CleanColdWaterHydrate       = config.getFloat("84_CleanColdWaterHydrate",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CleanColdWaterHydrate, 1, 65536,  "When a player drinks this type of water, their hydration will increase by that amount");
        EM_Settings.SaltyWaterHydrate           = config.getFloat("85_SaltyWaterHydrate",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SaltyWaterHydrate, 1, 65536,  "When a player drinks this type of water, their hydration will increase by that amount");
        EM_Settings.DirtyWaterHydrate           = config.getFloat("86_DirtyWaterHydrate",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DirtyWaterHydrate, 1, 65536,  "When a player drinks this type of water, their hydration will increase by that amount");
        //in item props
        EM_Settings.CleanWaterHydrate           = config.getFloat("87_CleanWaterHydrate",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CleanWaterHydrate, 1, 65536,  "When a player drinks this type of water, their hydration will increase by that amount");
        EM_Settings.CleanWarmWaterHydrate       = config.getFloat("88_CleanWarmWaterHydrate",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CleanWarmWaterHydrate, 1, 65536,  "When a player drinks this type of water, their hydration will increase by that amount");
        EM_Settings.DirtyWarmWaterHydrate       = config.getFloat("89_DirtyWarmWaterHydrate",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DirtyWarmWaterHydrate, 1, 65536,  "When a player drinks this type of water, their hydration will increase by that amount");
        EM_Settings.HotWaterHydrate             = config.getFloat("90_HotWaterHydrate",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HotWaterHydrate, 1, 65536,  "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.FrostyWaterHydratePlastic         = config.getFloat("91_FrostyWaterHydratePlastic",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.FrostyWaterHydratePlastic, 1, 65536,  "When a player drinks this type of water from a plastic bottle, their hydration will increase by that amount");
        EM_Settings.DirtyColdWaterHydratePlastic      = config.getFloat("92_DirtyColdWaterHydratePlastic",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DirtyColdWaterHydratePlastic, 1, 65536,  "When a player drinks this type of water from a plastic bottle, their hydration will increase by that amount");
        EM_Settings.CleanColdWaterHydratePlastic      = config.getFloat("93_CleanColdWaterHydratePlastic",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CleanColdWaterHydratePlastic, 1, 65536,  "When a player drinks this type of water from a plastic bottle, their hydration will increase by that amount");
        EM_Settings.SaltyWaterHydratePlastic          = config.getFloat("94_SaltyWaterHydratePlastic",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SaltyWaterHydratePlastic, 1, 65536,  "When a player drinks this type of water from a plastic bottle, their hydration will increase by that amount");
        EM_Settings.DirtyWaterHydratePlastic          = config.getFloat("95_DirtyWaterHydratePlastic",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DirtyWaterHydratePlastic, 1, 65536,  "When a player drinks this type of water from a plastic bottle, their hydration will increase by that amount");
        EM_Settings.CleanWaterHydratePlastic          = config.getFloat("96_CleanWaterHydratePlastic",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CleanWaterHydratePlastic, 1, 65536,  "When a player drinks this type of water from a plastic bottle, their hydration will increase by that amount");
        EM_Settings.CleanWarmWaterHydratePlastic      = config.getFloat("97_CleanWarmWaterHydratePlastic",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CleanWarmWaterHydratePlastic, 1, 65536,  "When a player drinks this type of water from a plastic bottle, their hydration will increase by that amount");
        EM_Settings.DirtyWarmWaterHydratePlastic      = config.getFloat("98_DirtyWarmWaterHydratePlastic",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DirtyWarmWaterHydratePlastic, 1, 65536,  "When a player drinks this type of water from a plastic bottle, their hydration will increase by that amount");
        EM_Settings.HotWaterHydratePlastic        = config.getFloat("99_HotWaterHydratePlastic",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HotWaterHydratePlastic, 1, 65536,  "When a player drinks this type of water from a plastic bottle, their hydration will increase by that amount");

        EM_Settings.FrostyWaterHydrateWorld         = config.getFloat("a-100_FrostyWaterHydrateWorld",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.FrostyWaterHydrateWorld, 1, 65536,  "When a player drinks this type of water from the world (water block), their hydration will increase by that amount");
        EM_Settings.DirtyColdWaterHydrateWorld      = config.getFloat("a-101_DirtyColdWaterHydrateWorld",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DirtyColdWaterHydrateWorld, 1, 65536,  "When a player drinks this type of water from the world (water block), their hydration will increase by that amount");
        EM_Settings.CleanColdWaterHydrateWorld      = config.getFloat("a-102_CleanColdWaterHydrateWorld",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CleanColdWaterHydrateWorld, 1, 65536,  "When a player drinks this type of water from the world (water block), their hydration will increase by that amount");
        EM_Settings.SaltyWaterHydrateWorld          = config.getFloat("a-103_SaltyWaterHydrateWorld",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SaltyWaterHydrateWorld, 1, 65536,  "When a player drinks this type of water from the world (water block), their hydration will increase by that amount");
        EM_Settings.DirtyWaterHydrateWorld          = config.getFloat("a-104_DirtyWaterHydrateWorld",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DirtyWaterHydrateWorld, 1, 65536,  "When a player drinks this type of water from the world (water block), their hydration will increase by that amount");
        EM_Settings.CleanWaterHydrateWorld          = config.getFloat("a-105_CleanWaterHydrateWorld",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CleanWaterHydrateWorld, 1, 65536,  "When a player drinks this type of water from the world (water block), their hydration will increase by that amount");
        EM_Settings.CleanWarmWaterHydrateWorld      = config.getFloat("a-106_CleanWarmWaterHydrateWorld",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.CleanWarmWaterHydrateWorld, 1, 65536,  "When a player drinks this type of water from the world (water block), their hydration will increase by that amount");
        EM_Settings.DirtyWarmWaterHydrateWorld      = config.getFloat("a-107_DirtyWarmWaterHydrateWorld",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.DirtyWarmWaterHydrateWorld, 1, 65536,  "When a player drinks this type of water from the world (water block), their hydration will increase by that amount");
        EM_Settings.HotWaterHydrateWorld        = config.getFloat("a-108_HotWaterHydrateWorld",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HotWaterHydrateWorld, 1, 65536,  "When a player drinks this type of water from the world (water block), their hydration will increase by that amount");

        EM_Settings.SanityDropHealth                   = config.getFloat("a-109_SanityDropHealth",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityDropHealth, 1, 65536,  "When the player's health <= this value, sanity will begin to decrease");
        EM_Settings.SanityDropTemperatureHigh          = config.getFloat("a-110_SanityDropTemperatureHigh",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityDropTemperatureHigh, 1, 65536,  "When player's body temperature >= this value, sanity will begin to decrease");
        EM_Settings.SanityDropTemperatureLow           = config.getFloat("a-111_SanityDropTemperatureLow",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityDropTemperatureLow, 1, 65536,  "When player's body temperature <= this value, sanity will begin to decrease");

        EM_Settings.NTMSpaceAirQualityDecrease                   = config.getFloat("a-112_NTMSpaceAirQualityDecrease",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.NTMSpaceAirQualityDecrease, 1, 65536,  "If the player cannot breathe (not a suitable atmosphere) the air quality will begin to decrease by this value");
        EM_Settings.NTMSpaceAirVentAirQualityIncrease            = config.getFloat("a-113_NTMSpaceAirVentAirQualityIncrease",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.NTMSpaceAirVentAirQualityIncrease, 1, 65536,  "Air vent will replenish this amount of air quality when running");

        EM_Settings.RealTemperatureConstant                     = config.getFloat("a-114_RealTemperatureConstant",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.RealTemperatureConstant, 1, 65536,  "A constant used to calculate the player's body temperature. Ambient temperature + this constant = player's expected body temperature");

        EM_Settings.TimeBelow10AirAndTemperatureConstantAir                      = config.getFloat("a-115_TimeBelow10AirAndTemperatureConstantAir",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.TimeBelow10AirAndTemperatureConstantAir, 1, 65536,  "When the air temperature <= this value and the player's body temperature <= TimeBelow10AirAndTemperatureConstantBodyTemperature, a countdown will begin, at the end of which the player will be permanently frostbite");
        EM_Settings.TimeBelow10AirAndTemperatureConstantBodyTemperature          = config.getFloat("a-116_TimeBelow10AirAndTemperatureConstantBodyTemperature",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.TimeBelow10AirAndTemperatureConstantBodyTemperature, 1, 65536,  "When the player's body temperature <= this value and the air temperature <= TimeBelow10AirAndTemperatureConstantAir, a countdown will begin, at the end of which the player will be permanently frostbite");
        EM_Settings.TimeBelow10BodyTemperatureConstant                           = config.getFloat("a-117_TimeBelow10BodyTemperatureConstant",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.TimeBelow10BodyTemperatureConstant, 1, 65536,  "When the player's body temperature <= this value, a countdown will begin, at the end of which the player will be permanently frostbite");

        EM_Settings.BodyTemperatureHeatStartValue            = config.getFloat("a-118_BodyTemperatureHeatStartValue",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureHeatStartValue, 1, 65536,  "From this temperature value, the player will receive the effect of heatstroke (1 lvl)");
        EM_Settings.BodyTemperatureHeatInstantDeath            = config.getFloat("a-119_BodyTemperatureHeatInstantDeath",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureHeatInstantDeath, 1, 65536,  "If the player's body temperature is >= this value, they will die instantly");

        EM_Settings.BodyTemperatureHeatstroke6            = config.getFloat("a-120_BodyTemperatureHeatstroke6",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureHeatstroke6, 1, 65536,  "From this temperature value, the player will receive the effect of heatstroke (6 lvl)");
        EM_Settings.BodyTemperatureHeatstroke5            = config.getFloat("a-121_BodyTemperatureHeatstroke5",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureHeatstroke5, 1, 65536,  "From this temperature value, the player will receive the effect of heatstroke (5 lvl)");
        EM_Settings.BodyTemperatureHeatstroke4            = config.getFloat("a-122_BodyTemperatureHeatstroke4",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureHeatstroke4, 1, 65536,  "From this temperature value, the player will receive the effect of heatstroke (4 lvl)");
        EM_Settings.BodyTemperatureHeatstroke3            = config.getFloat("a-123_BodyTemperatureHeatstroke3",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureHeatstroke3, 1, 65536,  "From this temperature value, the player will receive the effect of heatstroke (3 lvl)");
        EM_Settings.BodyTemperatureHeatstroke2            = config.getFloat("a-124_BodyTemperatureHeatstroke2",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureHeatstroke2, 1, 65536,  "From this temperature value, the player will receive the effect of heatstroke (2 lvl)");

        EM_Settings.BodyTemperatureCatchFireMin                 = config.getFloat("a-125_BodyTemperatureCatchFireMin",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureCatchFireMin, 1, 65536,  "If the player's body temperature is >= this value, and there is lava nearby, the player will catch fire");
        EM_Settings.BodyTemperatureCatchFireMax                 = config.getFloat("a-126_BodyTemperatureCatchFireMax",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureCatchFireMax, 1, 65536,  "If the player's body temperature is >= this value, the player will catch fire");
        EM_Settings.BodyTemperatureCatchFireDuration            = config.getFloat("a-127_BodyTemperatureCatchFireDuration",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureCatchFireDuration, 1, 65536,  "When the player is on fire (body temperature), the duration of the effect will be this value in seconds");


        EM_Settings.BodyTemperatureColdStartValue                   = config.getFloat("a-128_BodyTemperatureColdStartValue",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureColdStartValue, 1, 65536,  "From this temperature value, the player will receive the effect of hypothermia (1 lvl)");
        EM_Settings.BodyTemperatureColdStartValueVampire            = config.getFloat("a-129_BodyTemperatureColdStartValueVampire",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureColdStartValueVampire, 1, 65536,  "From this temperature value, (if the player is a vampire) the player will receive the effect of hypothermia (1 lvl)");
        EM_Settings.BodyTemperatureHypothermia3                     = config.getFloat("a-130_BodyTemperatureHypothermia3",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureHypothermia3, 1, 65536,  "From this temperature value, the player will receive the effect of hypothermia (3 lvl)");
        EM_Settings.BodyTemperatureHypothermia2                     = config.getFloat("a-131_BodyTemperatureHypothermia2",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureHypothermia2, 1, 65536,  "From this temperature value, the player will receive the effect of hypothermia (2 lvl)");
        EM_Settings.BodyTemperatureHypothermia2Vampire              = config.getFloat("a-132_BodyTemperatureHypothermia2Vampire",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.BodyTemperatureHypothermia2Vampire, 1, 65536,  "From this temperature value, (if the player is a vampire) the player will receive the effect of hypothermia (2 lvl)");

        EM_Settings.TimeBelow10StartValue                      = config.getFloat("a-133_TimeBelow10StartValue",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.TimeBelow10StartValue, 1, 65536,  "From this counter value, the player will receive the effect of frostbite (1 lvl), in seconds");
        EM_Settings.TimeBelow10Frostbite2                      = config.getFloat("a-134_TimeBelow10Frostbite2",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.TimeBelow10Frostbite2, 1, 65536,  "From this counter value, the player will receive the effect of frostbite (2 lvl), in seconds");
        EM_Settings.TimeBelow10FrostbitePermanent              = config.getFloat("a-135_TimeBelow10FrostbitePermanent",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.TimeBelow10FrostbitePermanent, 1, 65536,  "From this counter value, the player will receive the effect of permanent frostbite, in seconds");

        EM_Settings.SanityStage0UpperBound              = config.getFloat("a-136_SanityStage0UpperBound",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityStage0UpperBound, 1, 65536,  "If current sanity <= this value, and > SanityStage0LowerBound, the player will receive the effect of insanity (1 lvl)");
        EM_Settings.SanityStage0LowerBound              = config.getFloat("a-137_SanityStage0LowerBound",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityStage0LowerBound, 1, 65536,  "See SanityStage0UpperBound");
        EM_Settings.SanityStage1UpperBound              = config.getFloat("a-138_SanityStage1UpperBound",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityStage1UpperBound, 1, 65536,  "If current sanity <= this value, and > SanityStage1LowerBound, the player will receive the effect of insanity (2 lvl)");
        EM_Settings.SanityStage1LowerBound              = config.getFloat("a-139_SanityStage1LowerBound",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityStage1LowerBound, 1, 65536,  "See SanityStage1UpperBound");
        EM_Settings.SanityStage2UpperBound              = config.getFloat("a-140_SanityStage2UpperBound",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityStage2UpperBound, 1, 65536,  "If current sanity <= this value, and > SanityStage2LowerBound, the player will receive the effect of insanity (3 lvl)");
        EM_Settings.SanityStage2LowerBound              = config.getFloat("a-141_SanityStage2LowerBound",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityStage2LowerBound, 1, 65536,  "See SanityStage2UpperBound");
        EM_Settings.SanityStage3UpperBound              = config.getFloat("a-142_SanityStage3UpperBound",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityStage3UpperBound, 1, 65536,  "If current sanity <= this value, and > SanityStage3LowerBound, the player will receive the effect of insanity (4 lvl), and the countdown to a heart attack will begin");
        EM_Settings.SanityStage3LowerBound              = config.getFloat("a-143_SanityStage3LowerBound",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityStage3LowerBound, 1, 65536,  "See SanityStage2UpperBound");

        //EM_StatusManager

        EM_Settings.TrackerUpdateTimer              = config.getFloat("a-144_TrackerUpdateTimer",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.TrackerUpdateTimer, 1, 65536,  "How often should the tracker be updated (in ticks)");
        EM_Settings.SanityRateDecreaseDark          = config.getFloat("a-145_SanityRateDecreaseDark",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityRateDecreaseDark, 1, 65536,  "Sanity Rate will decrease by this number if the player is in the dark");
        EM_Settings.SanityBoostFlowers              = config.getFloat("a-146_SanityBoostFlowers",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityBoostFlowers, 1, 65536,  "Sanity Boost, which is given by flowers in the inventory");

        EM_Settings.AirQualityIncreaseLight                 = config.getFloat("a-147_AirQualityIncreaseLight",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.AirQualityIncreaseLight, 1, 65536,  "Air quality will increase by this value if the player is in a lit area");
        EM_Settings.SanityRateIncreaseLight                 = config.getFloat("a-148_SanityRateIncreaseLight",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityRateIncreaseLight, 1, 65536,  "Sanity Rate will increase by this value if the player is in a lit area");
        EM_Settings.SanityRateDecreaseLight                 = config.getFloat("a-149_SanityRateDecreaseLight",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SanityRateDecreaseLight, 1, 65536,  "Sanity Rate will decrease by this value if the player is NOT in a lit area AND current Sanity Rate < 0");
        EM_Settings.SurfaceYPositionMultiplier              = config.getFloat("a-150_SurfaceYPositionMultiplier",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SurfaceYPositionMultiplier, 1, 65536,  "The surface is defined as: sea level in dimension * this number = Y surface. For overworld it is 64*0.75=48");
        EM_Settings.AirQualityIncreaseSurface               = config.getFloat("a-151_AirQualityIncreaseSurface",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.AirQualityIncreaseSurface, 1, 65536,  "Air quality will increase by this value if the player is on the surface");

        EM_Settings.MaxHighAltitudeTemp                  = config.getFloat("a-152_MaxHighAltitudeTemp",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.MaxHighAltitudeTemp, 1, 65536,  "Temperature that will be at maximum altitude");
        EM_Settings.MinLowAltitudeTemp                   = config.getFloat("a-153_MinLowAltitudeTemp",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.MinLowAltitudeTemp, 1, 65536,  "Temperature that will be at minimum altitude");
        EM_Settings.SurfaceYPosition                     = config.getFloat("a-154_SurfaceYPosition",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SurfaceYPosition, 1, 65536,  "Y position, which will be considered as a surface, for calculating temperature from height");
        EM_Settings.SkyYPositionLowerBound               = config.getFloat("a-155_SkyYPositionLowerBound",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SkyYPositionLowerBound, 1, 65536,  "Y position from which temperature reduction begins");
        EM_Settings.SkyYPositionLowerBoundDivider        = config.getFloat("a-156_SkyYPositionLowerBoundDivider",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SkyYPositionLowerBoundDivider, 1, 65536,  "Divider for temperature calculations ( > SkyYPositionLowerBound, < SkyYPositionUpperBound) ");
        EM_Settings.SkyYPositionUpperBound               = config.getFloat("a-157_SkyYPositionUpperBound",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SkyYPositionUpperBound, 1, 65536,  "Maximum altitude, at which the temperature will be equal to MaxHighAltitudeTemp (if the biome temperature is not lower)");
        EM_Settings.NTMSpaceAirVentTemperatureConstant               = config.getFloat("a-158_NTMSpaceAirVentTemperatureConstant",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.NTMSpaceAirVentTemperatureConstant, 1, 65536,  "Temperature created in the air vent's air pocket ");
        EM_Settings.AvgEntityTempDivider               = config.getFloat("a-159_AvgEntityTempDivider",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.AvgEntityTempDivider, 1, 65536,  "Divider for the ambient temperature of nearby mobs");

        EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider = config.getFloat("a-160_AmbientTempblockItemTempInfDivider",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider, 1, 65536,  "The influence of blocks/items from the world/inventory will be divided by this number");
        EM_Settings.AmbientTemperatureblockAndItemTempInfluencebiomeTemperatureForRiseSpeedConstant = config.getFloat("a-161_AmbientTempblockItemTempInfbiomeTemperatureForRiseSpeedConstant",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.AmbientTemperatureblockAndItemTempInfluencebiomeTemperatureForRiseSpeedConstant, 1, 65536,  "If influence of blocks/items > biomeTemperature + this value, then riseSpeed = AmbientTemperatureblockAndItemTempInfluenceRiseSpeedConstant");
        EM_Settings.AmbientTemperatureblockAndItemTempInfluenceRiseSpeedConstant = config.getFloat("a-162_AmbientTempblockItemTempInfRiseSpeedConstant",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.AmbientTemperatureblockAndItemTempInfluenceRiseSpeedConstant, 1, 65536,  "See AmbientTemperatureblockAndItemTempInfluencebiomeTemperatureForRiseSpeedConstant");

        EM_Settings.HungerEffectDehydrateBonus               = config.getFloat("a-163_HungerEffectDehydrateBonus",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.HungerEffectDehydrateBonus, 1, 65536,  "Bonus to dehydration if the player is under hunger effect");
        EM_Settings.NearLavaMinRiseSpeed                 = config.getFloat("a-164_NearLavaMinRiseSpeed",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.NearLavaMinRiseSpeed, 1, 65536,  "Minimum riseSpeed if the player is near to lava");
        EM_Settings.NearLavaDehydrateBonus               = config.getFloat("a-165_NearLavaDehydrateBonus",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.NearLavaDehydrateBonus, 1, 65536,  "Bonus to dehydration if the player is near to lava");
        EM_Settings.NoBiomeRainfallDayDehydrateBonus               = config.getFloat("a-166_NoBiomeRainfallDayDehydrateBonus",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.NoBiomeRainfallDayDehydrateBonus, 1, 65536,  "Bonus to dehydration if BiomeRainfall = 0 and it is daytime");

        EM_Settings.SprintDehydrateBonus               = config.getFloat("a-167_SprintDehydrateBonus",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SprintDehydrateBonus, 1, 65536,  "Bonus to dehydration when the player is running");
        EM_Settings.SprintMinRiseSpeed                 = config.getFloat("a-168_SprintMinRiseSpeed",CATEGORY_KOTMATROSS_FORK_CHANGES, EM_Settings.SprintMinRiseSpeed, 1, 65536,  "Minimum riseSpeed when the player is running");


        // --------------- //
        // --- General --- //
        // --------------- //

        //EM_Settings.versionChecker = config.getBoolean("Version Checker", Configuration.CATEGORY_GENERAL, false, "Displays a client-side chat message on login if there's an update available.");
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
        EM_Settings.cauldronHeatingBlocks = config.get(Configuration.CATEGORY_GENERAL, "Cauldron Heating Blocks", EM_Settings.cauldronHeatingBlocks,
				"List of blocks that will purify a cauldron's water when placed underneath the cauldron. Of the form mod:block:meta. Append no meta value in order to use any meta."
				).getStringList();
		EM_Settings.blockTempDropoffPower = config.getFloat("Block Temperature Dropoff Power", Configuration.CATEGORY_GENERAL, 0.75F, 0.25F, 2F, "How rapidly the temperature influence of blocks falls off with your distance from them. 2 is realistic for open areas; 1 for large enclosed areas, and lower for more claustrophobic areas. Classic EnviroMine used 0.5.");
		EM_Settings.scanRadius = config.getInt("Environment scan radius", Configuration.CATEGORY_GENERAL, 6, 1, 10, "Max distance blocks with temperature effects can affect you. Higher radius is more accurate, but more computationally expensive.");
		EM_Settings.auraRadius = config.getFloat("Emission Aura Radius", Configuration.CATEGORY_GENERAL, 0.5F, 0F, 5F, "A block with temperature effects maxes out its effects when you're this far from the block's center. ");


		// --------------- //
		// --- Physics --- //
		// --------------- //
		int minPhysInterval = 6;
		EM_Settings.spreadIce = config.get(CATEGORY_PHYSICS, "Large Ice Cracking", EM_Settings.spreadIce , "Setting Large Ice Cracking to true can cause Massive Lag").getBoolean(EM_Settings.spreadIce );
		EM_Settings.updateCap = config.get(CATEGORY_PHYSICS, "Consecutive Physics Update Cap", EM_Settings.updateCap, "This will change maximum number of blocks that can be updated with physics at a time. - 1 = Unlimited").getInt(EM_Settings.updateCap);
		EM_Settings.physInterval = getConfigIntWithMinInt(config.get(CATEGORY_PHYSICS, "Physics Interval", minPhysInterval, "The number of ticks between physics update passes (must be " + minPhysInterval + " or more)"), minPhysInterval);
		EM_Settings.worldDelay = config.get(CATEGORY_PHYSICS, "World Start Delay", EM_Settings.worldDelay, "How long after world start until the physics system kicks in (DO NOT SET TOO LOW)").getInt(EM_Settings.worldDelay);
		EM_Settings.chunkDelay = config.get(CATEGORY_PHYSICS, "Chunk Physics Delay", EM_Settings.chunkDelay, "How long until individual chunk's physics starts after loading (DO NOT SET TOO LOW)").getInt(EM_Settings.chunkDelay);
		EM_Settings.physInterval = Math.max(EM_Settings.physInterval, 2);
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
		} else if(obj instanceof BiomeGenBase && type instanceof BiomeProperties)
        {
            BiomeProperties.base.generateEmpty(config, obj);
            returnValue = "(BiomeGenBase) Saved to "+ ModID + ".cfg on Profile "+ getProfileName();
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

