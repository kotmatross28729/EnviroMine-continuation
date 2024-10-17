package enviromine.core;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import enviromine.trackers.properties.ArmorProperties;
import enviromine.trackers.properties.BiomeProperties;
import enviromine.trackers.properties.BlockProperties;
import enviromine.trackers.properties.CaveGenProperties;
import enviromine.trackers.properties.CaveSpawnProperties;
import enviromine.trackers.properties.DimensionProperties;
import enviromine.trackers.properties.EntityProperties;
import enviromine.trackers.properties.ItemProperties;
import enviromine.trackers.properties.RotProperties;
import enviromine.trackers.properties.StabilityType;
import net.minecraft.util.EnumChatFormatting;

public class EM_Settings
{
	public static final UUID FROST1_UUID = UUID.fromString("B0C5F86A-78F3-417C-8B5A-527B90A1E919");
	public static final UUID FROST2_UUID = UUID.fromString("5C4111A7-A66C-40FB-9FAD-1C6ADAEE7E27");
	public static final UUID FROST3_UUID = UUID.fromString("721E793E-2203-4F6F-883F-6F44D7DDCCE1");
	public static final UUID HEAT1_UUID = UUID.fromString("CA6E2CFA-4C53-4CD2-AAD3-3D6177A4F126");
	public static final UUID DEHY1_UUID = UUID.fromString("38909A39-E1A1-4E93-9016-B2CCBE83D13D");

	public static File worldDir = null;

	//Mod Data
	public static final String VERSION = "1.3.143" + " kotmatross edition";
	public static final String MOD_ID = "enviromine";
	public static final String MOD_NAME = "EnviroMine";
	public static final String MOD_NAME_COLORIZED = EnumChatFormatting.AQUA + MOD_NAME + EnumChatFormatting.RESET;
	public static final String Channel = "EM_CH";
	public static final String Proxy = "enviromine.core.proxies";
	public static final String URL = "https://modrinth.com/mod/enviromine-continuation";
	public static final String VERSION_CHECKER_URL = "https://github.com/kotmatross28729/EnviroMine-continuation/blob/main/CURRENT_VERSION";
	public static final String GUI_FACTORY = MOD_ID+".client.gui.menu.config.EnviroMineGuiFactory";

	public static boolean versionChecker;

	public static int loggerVerbosity;


    //Fork changes

    //EM_StatusManager
    public static float LavaBlockAmbientTemperature = 200F;
    public static float BurningambientTemperature = 75F;
    public static float RiseSpeedMin = 0.1F;
    public static float RiseSpeedLava = 1F;

    public static float RiseSpeedLavaDecr = 0.3F;

    public static float SprintambientTemperature = 2F;

    public static float SweatTemperature = 38F;
    public static float SweatDehydrate = 0.1F;
    public static float SweatHydration = 75F;
    public static float SweatBodyTemp = 0.01F;


    //EnviroDataTracker
    public static float StrongArmorMaxTemp = 100.0F;
    public static float LightArmorMaxTemp = 37.7F;

    public static float StrongArmorMinTemp = 30.0F;
    public static float LightArmorMinTemp = 35.5F;

    public static float BodyTempBest = 0.1F;
    public static float BodyTempVeryGood = 0.2F;
    public static float BodyTempGood = 0.3F;
    public static float BodyTempBad = 0.5F;
    public static float BodyTempWorst = 0.8F;

    public static float BodyTempSleep = 10F;

    public static boolean DeathFromHeartAttack;
    public static int HeartAttackTimeToDie;
    public static int HbmGasMaskBreakMultiplier;
    public static int EnviromineGasMaskBreakMultiplier;
    public static int HbmGasMaskBreakChanceNumber;
    public static boolean SulfurDioxideGasDebugLogger;
    public static boolean CarbonMonoxideGasDebugLogger;
    public static boolean HydrogenSulfideGasDebugLogger;
//SulfurDioxide
    public static int SulfurDioxidePoisoningAmplifier;
    public static int SulfurDioxideSeverePoisoningAmplifier;
    public static int SulfurDioxidePoisoningTime;
    public static int SulfurDioxideSeverePoisoningTime;
    public static int SulfurDioxidePoisoningLevel;
    public static int SulfurDioxideSeverePoisoningLevel;
    public static int SulfurDioxidePoisoningChance;
//CarbonMonoxide
    public static int CarbonMonoxidePoisoningAmplifier;
    public static int CarbonMonoxidePoisoningTime;
    public static int CarbonMonoxidePoisoningLevel;
    public static int CarbonMonoxidePoisoningChance;
//HydrogenSulfide
    public static int HydrogenSulfidePoisoningAmplifier;
    public static int HydrogenSulfideSeverePoisoningAmplifier;
    public static int HydrogenSulfidePoisoningTime;
    public static int HydrogenSulfideSeverePoisoningTime;
    public static int HydrogenSulfidePoisoningLevel;
    public static int HydrogenSulfideSeverePoisoningLevel;
    public static int HydrogenSulfidePoisoningChance;
    public static boolean hardcoregases = false;


	public static boolean enablePhysics = false;
	public static boolean enableLandslide = true;
	public static boolean waterCollapse = true; // Added out of necessity/annoyance -- AstroTibs
	public static float blockTempDropoffPower = 0.75F;
	public static int scanRadius = 6;
	public static float auraRadius = 0.5F;

	@ShouldOverride
	public static boolean enableAirQ = true;
	@ShouldOverride
	public static boolean enableHydrate = true;
	@ShouldOverride
	public static boolean enableSanity = true;
	@ShouldOverride
	public static boolean enableBodyTemp = true;

	public static boolean trackNonPlayer = false;

	public static boolean spreadIce = false;

	public static boolean useFarenheit = false;
	public static String heatBarPos;
	public static String waterBarPos;
	public static String sanityBarPos;
	public static String oxygenBarPos;

	public static int dirtBottleID = 5001;
	public static int saltBottleID = 5002;
	public static int coldBottleID = 5003;
	public static int camelPackID = 5004;

	public static final String GAS_MASK_FILL_TAG_KEY = "gasMaskFill";
	public static final String GAS_MASK_MAX_TAG_KEY = "gasMaskMax";
	public static final String CAMEL_PACK_FILL_TAG_KEY = "camelPackFill";
	public static final String CAMEL_PACK_MAX_TAG_KEY = "camelPackMax";
	public static final String IS_CAMEL_PACK_TAG_KEY = "isCamelPack";
	public static int gasMaskMax = 1000;
	public static int filterRestore = 500;
	public static int camelPackMax = 100;
	public static float gasMaskUpdateRestoreFraction = 1F;

	/*
	public static int gasMaskID = 5005;
	public static int airFilterID = 5006;
	public static int hardHatID = 5007;
	public static int rottenFoodID = 5008;

	public static int blockElevatorTopID = 501;
	public static int blockElevatorBottomID = 502;
	public static int gasBlockID = 503;
	public static int fireGasBlockID = 504;
	*/

	public static int hypothermiaPotionID = 27;
	public static int heatstrokePotionID = 28;
	public static int frostBitePotionID = 29;
	public static int dehydratePotionID = 30;
	public static int insanityPotionID = 31;

	public static boolean enableHypothermiaGlobal = true;
	public static boolean enableHeatstrokeGlobal = true;
	public static boolean enableFrostbiteGlobal = true;
	public static boolean frostbitePermanent = true;

	//Gases
	public static boolean renderGases = false;
	public static int gasTickRate = 32; //GasFires are 4x faster than this
	public static int gasPassLimit = -1;
	public static boolean gasWaterLike = true;
	public static boolean slowGases = true; // Normal gases use random ticks to move
	public static boolean noGases = false;

	//World Gen
	public static boolean shaftGen = false;
	public static boolean gasGen = true;
	public static boolean oldMineGen = false;

	//Properties
	//@ShouldOverride("enviromine.network.packet.encoders.ArmorPropsEncoder")
	@ShouldOverride({String.class, ArmorProperties.class})
	public static HashMap<String,ArmorProperties> armorProperties = new HashMap<String,ArmorProperties>();
	//@ShouldOverride("enviromine.network.packet.encoders.BlocksPropsEncoder")
	@ShouldOverride({String.class, BlockProperties.class})
	public static HashMap<String,BlockProperties> blockProperties = new HashMap<String,BlockProperties>();
	@ShouldOverride({Integer.class, EntityProperties.class})
	public static HashMap<Integer,EntityProperties> livingProperties = new HashMap<Integer,EntityProperties>();
	@ShouldOverride({String.class, ItemProperties.class})
	public static HashMap<String,ItemProperties> itemProperties = new HashMap<String,ItemProperties>();
	@ShouldOverride({Integer.class, BiomeProperties.class})
	public static HashMap<Integer,BiomeProperties> biomeProperties = new HashMap<Integer,BiomeProperties>();
	@ShouldOverride({Integer.class, DimensionProperties.class})
	public static HashMap<Integer,DimensionProperties> dimensionProperties = new HashMap<Integer,DimensionProperties>();
	public static HashMap<String,StabilityType> stabilityTypes = new HashMap<String,StabilityType>();

	@ShouldOverride({String.class, RotProperties.class})
	public static HashMap<String,RotProperties> rotProperties = new HashMap<String,RotProperties>();

	public static boolean streamsDrink = true;
	public static boolean witcheryVampireImmunities = true;
	public static boolean witcheryWerewolfImmunities = true;
	public static boolean matterOverdriveAndroidImmunities = true;

	public static int updateCap = 128;
	public static boolean stoneCracks = true;
	public static String defaultStability = "loose";

	public static double sanityMult = 1.0D;
	public static double hydrationMult = 1.0D;
	public static double tempMult = 1.0D;
	public static double airMult = 1.0D;

	//public static boolean updateCheck = true;
	//public static boolean useDefaultConfig = true;
	public static boolean genConfigs = false;
	public static boolean genDefaults = false;

	public static int physInterval = 6;
	public static int worldDelay = 1000;
	public static int chunkDelay = 1000;
	public static int entityFailsafe = 1;
	public static boolean villageAssist = true;

	public static boolean minimalHud;
	public static boolean limitCauldron;
	public static boolean allowTinting = true;
	public static boolean torchesBurn = true;
	public static boolean torchesGoOut = true;
	public static boolean genFlammableCoal = true; // In case you don't want burny-coal
	public static boolean randomizeInsanityPitch = true;
	public static boolean catchFireAtHighTemps = true;

	public static int caveDimID = -3;
	public static int caveBiomeID = 23;
	public static boolean disableCaves = false;
	public static int limitElevatorY = 10;
	public static boolean caveOreEvent = true;
	public static boolean caveLava = false;
	public static int caveRavineRarity = 30;
	public static int caveTunnelRarity = 7;
	public static int caveDungeons = 8;
	public static int caveLiquidY = 32;
	public static boolean caveFlood = true;
	public static boolean caveRespawn = false;
	public static boolean enforceWeights = false;
	public static ArrayList<CaveGenProperties> caveGenProperties = new ArrayList<CaveGenProperties>();
	public static HashMap<Integer, CaveSpawnProperties> caveSpawnProperties = new HashMap<Integer, CaveSpawnProperties>();

	public static boolean foodSpoiling = true;
	public static int foodRotTime = 7;

	/** Whether or not this overridden with server settings */
	public static boolean isOverridden = false;
	public static boolean enableConfigOverride = false;
	public static boolean profileOverride = false;
	public static String profileSelected = "default";

	public static boolean enableQuakes = true;
	public static boolean quakePhysics = true;
	public static int quakeRarity = 100;

	public static boolean finiteWater = false;
	public static float thingChance = 0.000001F;
	public static boolean noNausea = false;
	public static boolean keepStatus = false;
	public static boolean renderGear = true;
	public static String[] cauldronHeatingBlocks = new String[]{ // Added on request - AstroTibs
			"minecraft:fire",
			"minecraft:lava",
			"minecraft:flowing_lava",
			"campfirebackport:campfire",
			"campfirebackport:soul_campfire",
			"CaveBiomes:stone_lavacrust",
			"demonmobs:hellfire",
			"etfuturum:magma",
			"infernomobs:purelava",
			"infernomobs:scorchfire",
			"netherlicious:FoxFire",
			"netherlicious:MagmaBlock",
			"netherlicious:SoulFire",
			"uptodate:magma_block",
	};
	public static String[] notWaterBlocks = new String[]{ // Added on request - AstroTibs
			"minecraft:fire",
			"minecraft:lava",
			"minecraft:flowing_lava",
			"campfirebackport:campfire",
			"campfirebackport:soul_campfire",
			"CaveBiomes:stone_lavacrust",
			"demonmobs:hellfire",
			"etfuturum:magma",
			"infernomobs:purelava",
			"infernomobs:scorchfire",
			"netherlicious:FoxFire",
			"netherlicious:MagmaBlock",
			"netherlicious:SoulFire",
			"uptodate:magma_block",
	};

	public static boolean voxelMenuExists = false;

	/**
	 * Tells the server that this field should be sent to the client to overwrite<br>
	 * Usage:<br>
	 * <tt>@ShouldOverride</tt> - for ints/booleans/floats/Strings<br>
	 * <tt>@ShouldOverride(Class[] value)</tt> - for ArrayList or HashMap types
	 * */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ShouldOverride
	{
		Class<?>[] value() default {};
	}
}
