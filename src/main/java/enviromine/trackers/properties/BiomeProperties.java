package enviromine.trackers.properties;

import enviromine.core.EM_ConfigHandler;
import enviromine.core.EM_ConfigHandler.EnumLogVerbosity;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.trackers.properties.compat.BiomeProperties_LOTR;
import enviromine.trackers.properties.compat.BiomeProperties_NTM_SPACE;
import enviromine.trackers.properties.helpers.PropertyBase;
import enviromine.trackers.properties.helpers.SerialisableProperty;
import enviromine.utils.EnviroUtils;
import enviromine.utils.ModIdentification;
import enviromine.utils.WaterUtils;
import enviromine.utils.misc.CompatSafe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

@CompatSafe
public class BiomeProperties implements SerialisableProperty, PropertyBase
{
	public static final BiomeProperties base = new BiomeProperties();
	static String[] BOName;

	public int id;
	public boolean biomeOveride;
	
	//TODO: serene seasons compat, water quality changes with season
	public String waterQuality;
	public float ambientTemp;
	public float tempRate;
	public float sanityRate;
	public float dehydrateRate;
	public float airRate;
	public String loadedFrom;
    public boolean isDesertBiome;
    public float DesertBiomeTemperatureMultiplier;
    public float DAWN_TEMPERATURE;
    public float DAY_TEMPERATURE;
    public float DUSK_TEMPERATURE;
    public float NIGHT_TEMPERATURE;

    public float TemperatureRainDecrease;
    public float TemperatureThunderDecrease;
    public boolean TemperatureRainBool;
    public boolean TemperatureThunderBool;
    public float TemperatureShadeDecrease;

    public float ambientTemp_TERRAFORMED;
    public float DAWN_TEMPERATURE_TERRAFORMED;
    public float DAY_TEMPERATURE_TERRAFORMED;
    public float DUSK_TEMPERATURE_TERRAFORMED;
    public float NIGHT_TEMPERATURE_TERRAFORMED;

    public float EARLY_SPRING_TEMPERATURE_DECREASE;
    public float EARLY_SUMMER_TEMPERATURE_DECREASE;
    public float EARLY_WINTER_TEMPERATURE_DECREASE;
    public float EARLY_AUTUMN_TEMPERATURE_DECREASE;
    public float MID_SPRING_TEMPERATURE_DECREASE;
    public float MID_SUMMER_TEMPERATURE_DECREASE;
    public float MID_WINTER_TEMPERATURE_DECREASE;
    public float MID_AUTUMN_TEMPERATURE_DECREASE;
    public float LATE_SPRING_TEMPERATURE_DECREASE;
    public float LATE_SUMMER_TEMPERATURE_DECREASE;
    public float LATE_WINTER_TEMPERATURE_DECREASE;
    public float LATE_AUTUMN_TEMPERATURE_DECREASE;

    public float tempRate_DAWN;
    public float tempRate_DAY;
    public float tempRate_DUSK;
    public float tempRate_NIGHT;
    public boolean tempRate_HARD;

    public float TemperatureWaterDecrease;
    public float dropSpeedWater;

    public float dropSpeedRain;
    public float dropSpeedThunder;

	public BiomeProperties(NBTTagCompound tags)
	{
		this.ReadFromNBT(tags);
	}

	public BiomeProperties()
	{
		// THIS CONSTRUCTOR IS FOR STATIC PURPOSES ONLY!
		if(base != null && base != this) //DONT TOUCH
		{
			throw new IllegalStateException();
		}
	}

	public BiomeProperties (
        int id,
        boolean biomeOveride,
        String waterQuality,
        float ambientTemp,
        float tempRate,
        float sanityRate,
        float dehydrateRate,
        float airRate,
        String filename,
        boolean isDesertBiome,
        float DesertBiomeTemperatureMultiplier,

        float DAWN_TEMPERATURE,
        float DAY_TEMPERATURE,
        float DUSK_TEMPERATURE,
        float NIGHT_TEMPERATURE,

        float TemperatureRainDecrease,
        float TemperatureThunderDecrease,
        boolean TemperatureRainBool,
        boolean TemperatureThunderBool,
        float TemperatureShadeDecrease,

        float ambientTemp_TERRAFORMED,
        float DAWN_TEMPERATURE_TERRAFORMED,
        float DAY_TEMPERATURE_TERRAFORMED,
        float DUSK_TEMPERATURE_TERRAFORMED,
        float NIGHT_TEMPERATURE_TERRAFORMED,

        float EARLY_SPRING_TEMPERATURE_DECREASE,
        float EARLY_SUMMER_TEMPERATURE_DECREASE,
        float EARLY_WINTER_TEMPERATURE_DECREASE,
        float EARLY_AUTUMN_TEMPERATURE_DECREASE,
        float MID_SPRING_TEMPERATURE_DECREASE,
        float MID_SUMMER_TEMPERATURE_DECREASE,
        float MID_WINTER_TEMPERATURE_DECREASE,
        float MID_AUTUMN_TEMPERATURE_DECREASE,
        float LATE_SPRING_TEMPERATURE_DECREASE,
        float LATE_SUMMER_TEMPERATURE_DECREASE,
        float LATE_WINTER_TEMPERATURE_DECREASE,
        float LATE_AUTUMN_TEMPERATURE_DECREASE,
        float tempRate_DAWN,
        float tempRate_DAY,
        float tempRate_DUSK,
        float tempRate_NIGHT,
        boolean tempRate_HARD,
        float TemperatureWaterDecrease,
        float dropSpeedWater,
        float dropSpeedRain,
        float dropSpeedThunder
    ) {
		this.id = id;
		this.biomeOveride = biomeOveride;
		this.waterQuality = waterQuality;
		this.ambientTemp = ambientTemp;
		this.tempRate = tempRate;
		this.sanityRate = sanityRate;
		this.dehydrateRate = dehydrateRate;
		this.airRate = airRate;
		this.loadedFrom = filename;
        this.isDesertBiome = isDesertBiome;
        this.DesertBiomeTemperatureMultiplier = DesertBiomeTemperatureMultiplier;

        this.DAWN_TEMPERATURE  = DAWN_TEMPERATURE;
        this.DAY_TEMPERATURE   = DAY_TEMPERATURE;
        this.DUSK_TEMPERATURE  = DUSK_TEMPERATURE;
        this.NIGHT_TEMPERATURE = NIGHT_TEMPERATURE;

        this.TemperatureRainDecrease    = TemperatureRainDecrease;
        this.TemperatureThunderDecrease = TemperatureThunderDecrease;
        this.TemperatureRainBool        = TemperatureRainBool;
        this.TemperatureThunderBool     = TemperatureThunderBool;
        this.TemperatureShadeDecrease   = TemperatureShadeDecrease;

        this.ambientTemp_TERRAFORMED        = ambientTemp_TERRAFORMED;
        this.DAWN_TEMPERATURE_TERRAFORMED   = DAWN_TEMPERATURE_TERRAFORMED;
        this.DAY_TEMPERATURE_TERRAFORMED    = DAY_TEMPERATURE_TERRAFORMED;
        this.DUSK_TEMPERATURE_TERRAFORMED   = DUSK_TEMPERATURE_TERRAFORMED;
        this.NIGHT_TEMPERATURE_TERRAFORMED  = NIGHT_TEMPERATURE_TERRAFORMED;

        this.EARLY_SPRING_TEMPERATURE_DECREASE = EARLY_SPRING_TEMPERATURE_DECREASE;
        this.EARLY_SUMMER_TEMPERATURE_DECREASE = EARLY_SUMMER_TEMPERATURE_DECREASE;
        this.EARLY_WINTER_TEMPERATURE_DECREASE = EARLY_WINTER_TEMPERATURE_DECREASE;
        this.EARLY_AUTUMN_TEMPERATURE_DECREASE = EARLY_AUTUMN_TEMPERATURE_DECREASE;
        this.MID_SPRING_TEMPERATURE_DECREASE   =   MID_SPRING_TEMPERATURE_DECREASE;
        this.MID_SUMMER_TEMPERATURE_DECREASE   =   MID_SUMMER_TEMPERATURE_DECREASE;
        this.MID_WINTER_TEMPERATURE_DECREASE   =   MID_WINTER_TEMPERATURE_DECREASE;
        this.MID_AUTUMN_TEMPERATURE_DECREASE   =   MID_AUTUMN_TEMPERATURE_DECREASE;
        this.LATE_SPRING_TEMPERATURE_DECREASE  =  LATE_SPRING_TEMPERATURE_DECREASE;
        this.LATE_SUMMER_TEMPERATURE_DECREASE  =  LATE_SUMMER_TEMPERATURE_DECREASE;
        this.LATE_WINTER_TEMPERATURE_DECREASE  =  LATE_WINTER_TEMPERATURE_DECREASE;
        this.LATE_AUTUMN_TEMPERATURE_DECREASE  =  LATE_AUTUMN_TEMPERATURE_DECREASE;

        this.tempRate_DAWN   = tempRate_DAWN;
        this.tempRate_DAY    = tempRate_DAY;
        this.tempRate_DUSK   = tempRate_DUSK;
        this.tempRate_NIGHT  = tempRate_NIGHT;

        this.tempRate_HARD = tempRate_HARD;

        this.TemperatureWaterDecrease = TemperatureWaterDecrease;
        this.dropSpeedWater = dropSpeedWater;

        this.dropSpeedRain = dropSpeedRain;
        this.dropSpeedThunder = dropSpeedThunder;
	}
	/**
	 * <b>hasProperty(BiomeGenBase biome)</b><bR><br>
	 * Checks if Property contains custom properties.
	 * @param biome
	 * @return true if has custom properties
	 */
	public boolean hasProperty(BiomeGenBase biome)
	{
		return EM_Settings.biomeProperties.containsKey(biome.biomeID);
	}
	/**
	 * 	<b>getProperty(BiomeGenBase biome)</b><bR><br>
	 * Gets Property.
	 * @param biome
	 * @return BiomeProperties
	 */
	public BiomeProperties getProperty(BiomeGenBase biome)
	{
		return EM_Settings.biomeProperties.get(biome.biomeID);
	}

	public WaterUtils.WATER_TYPES getWaterQuality() {
		return switch (this.waterQuality.trim()) {
			case "RADIOACTIVE_FROSTY" -> WaterUtils.WATER_TYPES.RADIOACTIVE_FROSTY;
			case "FROSTY" -> WaterUtils.WATER_TYPES.FROSTY;
			case "RADIOACTIVE_COLD" -> WaterUtils.WATER_TYPES.RADIOACTIVE_COLD;
			case "DIRTY_COLD" -> WaterUtils.WATER_TYPES.DIRTY_COLD;
			case "SALTY_COLD" -> WaterUtils.WATER_TYPES.SALTY_COLD;
			case "CLEAN_COLD" -> WaterUtils.WATER_TYPES.CLEAN_COLD;
			case "RADIOACTIVE" -> WaterUtils.WATER_TYPES.RADIOACTIVE;
			case "DIRTY" -> WaterUtils.WATER_TYPES.DIRTY;
			case "SALTY" -> WaterUtils.WATER_TYPES.SALTY;
			case "RADIOACTIVE_WARM" -> WaterUtils.WATER_TYPES.RADIOACTIVE_WARM;
			case "DIRTY_WARM" -> WaterUtils.WATER_TYPES.DIRTY_WARM;
			case "SALTY_WARM" -> WaterUtils.WATER_TYPES.SALTY_WARM;
			case "CLEAN_WARM" -> WaterUtils.WATER_TYPES.CLEAN_WARM;
			case "RADIOACTIVE_HOT" -> WaterUtils.WATER_TYPES.RADIOACTIVE_HOT;
			case "HOT" -> WaterUtils.WATER_TYPES.HOT;
			default -> WaterUtils.WATER_TYPES.CLEAN;
		};
	}

	@Override
	public NBTTagCompound WriteToNBT()
	{
		NBTTagCompound tags = new NBTTagCompound();
		tags.setInteger("id", this.id);
		tags.setBoolean("biomeOveride", this.biomeOveride);
		tags.setString("waterQuality", this.waterQuality);
		tags.setFloat("ambientTemp", this.ambientTemp);
		tags.setFloat("tempRate", this.tempRate);
		tags.setFloat("sanityRate", this.sanityRate);
		tags.setFloat("dehydrateRate", this.dehydrateRate);
        tags.setBoolean("isDesertBiome", this.isDesertBiome);
        tags.setFloat("DesertBiomeTemperatureMultiplier", this.DesertBiomeTemperatureMultiplier);

        tags.setFloat("DAWN_TEMPERATURE", this.DAWN_TEMPERATURE);
        tags.setFloat("DAY_TEMPERATURE", this.DAY_TEMPERATURE);
        tags.setFloat("DUSK_TEMPERATURE", this.DUSK_TEMPERATURE);
        tags.setFloat("NIGHT_TEMPERATURE", this.NIGHT_TEMPERATURE);

        tags.setFloat("TemperatureRainDecrease", this.TemperatureRainDecrease);
        tags.setFloat("TemperatureThunderDecrease", this.TemperatureThunderDecrease);
        tags.setBoolean("TemperatureRainBool", this.TemperatureRainBool);
        tags.setBoolean("TemperatureThunderBool", this.TemperatureThunderBool);
        tags.setFloat("TemperatureShadeDecrease", this.TemperatureShadeDecrease);

        tags.setFloat("ambientTemp_TERRAFORMED", this.ambientTemp_TERRAFORMED);
        tags.setFloat("DAWN_TEMPERATURE_TERRAFORMED", this.DAWN_TEMPERATURE_TERRAFORMED);
        tags.setFloat("DAY_TEMPERATURE_TERRAFORMED", this.DAY_TEMPERATURE_TERRAFORMED);
        tags.setFloat("DUSK_TEMPERATURE_TERRAFORMED", this.DUSK_TEMPERATURE_TERRAFORMED);
        tags.setFloat("NIGHT_TEMPERATURE_TERRAFORMED", this.NIGHT_TEMPERATURE_TERRAFORMED);


        tags.setFloat("EARLY_SPRING_TEMPERATURE_DECREASE", this.EARLY_SPRING_TEMPERATURE_DECREASE);
        tags.setFloat("EARLY_SUMMER_TEMPERATURE_DECREASE", this.EARLY_SUMMER_TEMPERATURE_DECREASE);
        tags.setFloat("EARLY_WINTER_TEMPERATURE_DECREASE", this.EARLY_WINTER_TEMPERATURE_DECREASE);
        tags.setFloat("EARLY_AUTUMN_TEMPERATURE_DECREASE", this.EARLY_AUTUMN_TEMPERATURE_DECREASE);
        tags.setFloat("MID_SPRING_TEMPERATURE_DECREASE", this.MID_SPRING_TEMPERATURE_DECREASE);
        tags.setFloat("MID_SUMMER_TEMPERATURE_DECREASE", this.MID_SUMMER_TEMPERATURE_DECREASE);
        tags.setFloat("MID_WINTER_TEMPERATURE_DECREASE", this.MID_WINTER_TEMPERATURE_DECREASE);
        tags.setFloat("MID_AUTUMN_TEMPERATURE_DECREASE", this.MID_AUTUMN_TEMPERATURE_DECREASE);
        tags.setFloat("LATE_SPRING_TEMPERATURE_DECREASE", this.LATE_SPRING_TEMPERATURE_DECREASE);
        tags.setFloat("LATE_SUMMER_TEMPERATURE_DECREASE", this.LATE_SUMMER_TEMPERATURE_DECREASE);
        tags.setFloat("LATE_WINTER_TEMPERATURE_DECREASE", this.LATE_WINTER_TEMPERATURE_DECREASE);
        tags.setFloat("LATE_AUTUMN_TEMPERATURE_DECREASE", this.LATE_AUTUMN_TEMPERATURE_DECREASE);

        tags.setFloat("tempRate_DAWN", this.tempRate_DAWN);
        tags.setFloat("tempRate_DAY", this.tempRate_DAY);
        tags.setFloat("tempRate_DUSK", this.tempRate_DUSK);
        tags.setFloat("tempRate_NIGHT", this.tempRate_NIGHT);

        tags.setBoolean("tempRate_HARD", this.tempRate_HARD);

        tags.setFloat("TemperatureWaterDecrease", this.TemperatureWaterDecrease);
        tags.setFloat("dropSpeedWater", this.dropSpeedWater);

        tags.setFloat("dropSpeedRain", this.dropSpeedRain);
        tags.setFloat("dropSpeedThunder", this.dropSpeedThunder);

		return tags;
	}

	@Override
	public void ReadFromNBT(NBTTagCompound tags)
	{
		this.id = tags.getInteger("id");
		this.biomeOveride = tags.getBoolean("biomeOveride");
		this.waterQuality = tags.getString("waterQuality");
		this.ambientTemp = tags.getFloat("ambientTemp");
		this.tempRate = tags.getFloat("tempRate");
		this.sanityRate = tags.getFloat("sanityRate");
		this.dehydrateRate = tags.getFloat("dehydrateRate");
        this.isDesertBiome = tags.getBoolean("isDesertBiome");
        this.DesertBiomeTemperatureMultiplier = tags.getFloat("DesertBiomeTemperatureMultiplier");

        this.DAWN_TEMPERATURE = tags.getFloat("DAWN_TEMPERATURE");
        this.DAY_TEMPERATURE = tags.getFloat("DAY_TEMPERATURE");
        this.DUSK_TEMPERATURE = tags.getFloat("DUSK_TEMPERATURE");
        this.NIGHT_TEMPERATURE = tags.getFloat("NIGHT_TEMPERATURE");

        this.TemperatureRainDecrease = tags.getFloat("TemperatureRainDecrease");
        this.TemperatureThunderDecrease = tags.getFloat("TemperatureThunderDecrease");
        this.TemperatureRainBool = tags.getBoolean("TemperatureRainBool");
        this.TemperatureThunderBool = tags.getBoolean("TemperatureThunderBool");
        this.TemperatureShadeDecrease = tags.getFloat("TemperatureShadeDecrease");

        this.ambientTemp_TERRAFORMED = tags.getFloat("ambientTemp_TERRAFORMED");
        this.DAWN_TEMPERATURE_TERRAFORMED = tags.getFloat("DAWN_TEMPERATURE_TERRAFORMED");
        this.DAY_TEMPERATURE_TERRAFORMED = tags.getFloat("DAY_TEMPERATURE_TERRAFORMED");
        this.DUSK_TEMPERATURE_TERRAFORMED = tags.getFloat("DUSK_TEMPERATURE_TERRAFORMED");
        this.NIGHT_TEMPERATURE_TERRAFORMED = tags.getFloat("NIGHT_TEMPERATURE_TERRAFORMED");

        this.EARLY_SPRING_TEMPERATURE_DECREASE = tags.getFloat("EARLY_SPRING_TEMPERATURE_DECREASE");
        this.EARLY_SUMMER_TEMPERATURE_DECREASE = tags.getFloat("EARLY_SUMMER_TEMPERATURE_DECREASE");
        this.EARLY_WINTER_TEMPERATURE_DECREASE = tags.getFloat("EARLY_WINTER_TEMPERATURE_DECREASE");
        this.EARLY_AUTUMN_TEMPERATURE_DECREASE = tags.getFloat("EARLY_AUTUMN_TEMPERATURE_DECREASE");

        this.MID_SPRING_TEMPERATURE_DECREASE = tags.getFloat("MID_SPRING_TEMPERATURE_DECREASE");
        this.MID_SUMMER_TEMPERATURE_DECREASE = tags.getFloat("MID_SUMMER_TEMPERATURE_DECREASE");
        this.MID_WINTER_TEMPERATURE_DECREASE = tags.getFloat("MID_WINTER_TEMPERATURE_DECREASE");
        this.MID_AUTUMN_TEMPERATURE_DECREASE = tags.getFloat("MID_AUTUMN_TEMPERATURE_DECREASE");
        this.LATE_SPRING_TEMPERATURE_DECREASE = tags.getFloat("LATE_SPRING_TEMPERATURE_DECREASE");
        this.LATE_SUMMER_TEMPERATURE_DECREASE = tags.getFloat("LATE_SUMMER_TEMPERATURE_DECREASE");
        this.LATE_WINTER_TEMPERATURE_DECREASE = tags.getFloat("LATE_WINTER_TEMPERATURE_DECREASE");
        this.LATE_AUTUMN_TEMPERATURE_DECREASE = tags.getFloat("LATE_AUTUMN_TEMPERATURE_DECREASE");

        this.tempRate_DAWN = tags.getFloat("tempRate_DAWN");
        this.tempRate_DAY = tags.getFloat("tempRate_DAY");
        this.tempRate_DUSK = tags.getFloat("tempRate_DUSK");
        this.tempRate_NIGHT = tags.getFloat("tempRate_NIGHT");

        this.tempRate_HARD = tags.getBoolean("tempRate_HARD");

        this.TemperatureWaterDecrease = tags.getFloat("TemperatureWaterDecrease");
        this.dropSpeedWater = tags.getFloat("dropSpeedWater");

        this.dropSpeedRain = tags.getFloat("dropSpeedRain");
        this.dropSpeedThunder = tags.getFloat("dropSpeedThunder");
	}

	@Override
	public String categoryName()
	{
		return "biomes";
	}

	@Override
	public String categoryDescription()
	{
		return "Manually change the environmental properties of each biome";
	}

	@Override
	public void LoadProperty(Configuration config, String category)
	{
		config.setCategoryComment(this.categoryName(), this.categoryDescription());
		int id = config.get(category, BOName[0], 0).getInt(0);
		boolean biomeOveride = config.get(category, BOName[1], false).getBoolean(false);
		String waterQ = config.get(category, BOName[2], "CLEAN", 
				"Water Quality: " +
						"RADIOACTIVE_FROSTY, " +
						"FROSTY, " +
						"RADIOACTIVE_COLD, " +
						"DIRTY_COLD, " +
						"SALTY_COLD, " +
						"CLEAN_COLD, " +
						"RADIOACTIVE, " +
						"DIRTY, " +
						"SALTY, " +
						"CLEAN, " +
						"RADIOACTIVE_WARM, " +
						"DIRTY_WARM, " +
						"SALTY_WARM, " +
						"CLEAN_WARM, " +
						"RADIOACTIVE_HOT, " +
						"HOT "
				).getString();
		float ambTemp = (float)config.get(category, BOName[3], 25.00, "Biome temperature in celsius (Player body temp is offset by + 12C)").getDouble(25.00);
		float tempRate = (float)config.get(category, BOName[4], 0.0).getDouble(0.0);
		float sanRate = (float)config.get(category, BOName[5], 0.0).getDouble(0.0);
		float dehyRate = (float)config.get(category, BOName[6], 0.0).getDouble(0.0);
		float airRate = (float)config.get(category, BOName[7], 0.0).getDouble(0.0);
		String filename = config.getConfigFile().getName();
        boolean isDesertBiome = config.get(category, BOName[8], false).getBoolean(false);
        float DesertBiomeTemperatureMultiplier = (float)config.get(category, BOName[9], 1.0).getDouble(1.0);

        float DAWN_TEMPERATURE  = (float)config.get(category, BOName[10], 4.0).getDouble(4.0);
        float DAY_TEMPERATURE   = (float)config.get(category, BOName[11], 0.0).getDouble(0.0);
        float DUSK_TEMPERATURE  = (float)config.get(category, BOName[12], 4.0).getDouble(4.0);
        float NIGHT_TEMPERATURE = (float)config.get(category, BOName[13], 8.0).getDouble(8.0);

        float TemperatureRainDecrease = (float)config.get(category, BOName[14], 6.0).getDouble(6.0);
        float TemperatureThunderDecrease = (float)config.get(category, BOName[15], 8.0).getDouble(8.0);

        boolean TemperatureRainBool = config.get(category, BOName[16], true).getBoolean(true);
        boolean TemperatureThunderBool = config.get(category, BOName[17], true).getBoolean(true);
        float TemperatureShadeDecrease = (float)config.get(category, BOName[18], 2.5).getDouble(2.5);

        float ambientTemp_TERRAFORMED       =   (float)config.get(category, BOName[19], 25.0).getDouble(25.0);
        float DAWN_TEMPERATURE_TERRAFORMED  =   (float)config.get(category, BOName[20], 4.0).getDouble(4.0);
        float DAY_TEMPERATURE_TERRAFORMED   =   (float)config.get(category, BOName[21], 0.0).getDouble(0.0);
        float DUSK_TEMPERATURE_TERRAFORMED  =   (float)config.get(category, BOName[22], 4.0).getDouble(4.0);
        float NIGHT_TEMPERATURE_TERRAFORMED =   (float)config.get(category, BOName[23], 8.0).getDouble(8.0);

        float EARLY_SPRING_TEMPERATURE_DECREASE = (float)config.get(category, BOName[24], 5.0).getDouble(5.0);
        float EARLY_SUMMER_TEMPERATURE_DECREASE = (float)config.get(category, BOName[25], -1.0).getDouble(-1.0);
        float EARLY_WINTER_TEMPERATURE_DECREASE = (float)config.get(category, BOName[26], 12.0).getDouble(12.0);
        float EARLY_AUTUMN_TEMPERATURE_DECREASE = (float)config.get(category, BOName[27], 6.0).getDouble(6.0);
        float MID_SPRING_TEMPERATURE_DECREASE   = (float)config.get(category, BOName[28], -2.0).getDouble(-2.0);
        float MID_SUMMER_TEMPERATURE_DECREASE   = (float)config.get(category, BOName[29], -3.0).getDouble(-3.0);
        float MID_WINTER_TEMPERATURE_DECREASE   = (float)config.get(category, BOName[30], 15.0).getDouble(15.0);
        float MID_AUTUMN_TEMPERATURE_DECREASE   = (float)config.get(category, BOName[31], 8.0).getDouble(8.0);
        float LATE_SPRING_TEMPERATURE_DECREASE  = (float)config.get(category, BOName[32], -1.0).getDouble(-1.0);
        float LATE_SUMMER_TEMPERATURE_DECREASE  = (float)config.get(category, BOName[33], -1.0).getDouble(-1.0);
        float LATE_WINTER_TEMPERATURE_DECREASE  = (float)config.get(category, BOName[34], 10.0).getDouble(10.0);
        float LATE_AUTUMN_TEMPERATURE_DECREASE  = (float)config.get(category, BOName[35], 10.0).getDouble(10.0);

        float tempRate_DAWN = (float)config.get(category, BOName[36], 0.0).getDouble(0.0);
        float tempRate_DAY = (float)config.get(category, BOName[37], 0.0).getDouble(0.0);
        float tempRate_DUSK = (float)config.get(category, BOName[38], 0.0).getDouble(0.0);
        float tempRate_NIGHT = (float)config.get(category, BOName[39], 0.0).getDouble(0.0);

        boolean tempRate_HARD = config.get(category, BOName[40], false).getBoolean(false);

        float TemperatureWaterDecrease = (float)config.get(category, BOName[41], 10.0).getDouble(10.0);
        float dropSpeedWater = (float)config.get(category, BOName[42], 0.01).getDouble(0.01);

        float dropSpeedRain = (float)config.get(category, BOName[43], 0.01).getDouble(0.01);
        float dropSpeedThunder = (float)config.get(category, BOName[44], 0.01).getDouble(0.01);

		BiomeProperties entry = new BiomeProperties (
            id,
            biomeOveride,
            waterQ,
            ambTemp,
            tempRate,
            sanRate,
            dehyRate,
            airRate,
            filename,
            isDesertBiome,
            DesertBiomeTemperatureMultiplier,
            DAWN_TEMPERATURE,
            DAY_TEMPERATURE,
            DUSK_TEMPERATURE,
            NIGHT_TEMPERATURE,
            TemperatureRainDecrease,
            TemperatureThunderDecrease,
            TemperatureRainBool,
            TemperatureThunderBool,
            TemperatureShadeDecrease,
            ambientTemp_TERRAFORMED,
            DAWN_TEMPERATURE_TERRAFORMED,
            DAY_TEMPERATURE_TERRAFORMED,
            DUSK_TEMPERATURE_TERRAFORMED,
            NIGHT_TEMPERATURE_TERRAFORMED,
            EARLY_SPRING_TEMPERATURE_DECREASE,
            EARLY_SUMMER_TEMPERATURE_DECREASE,
            EARLY_WINTER_TEMPERATURE_DECREASE,
            EARLY_AUTUMN_TEMPERATURE_DECREASE,
            MID_SPRING_TEMPERATURE_DECREASE,
            MID_SUMMER_TEMPERATURE_DECREASE,
            MID_WINTER_TEMPERATURE_DECREASE,
            MID_AUTUMN_TEMPERATURE_DECREASE,
            LATE_SPRING_TEMPERATURE_DECREASE,
            LATE_SUMMER_TEMPERATURE_DECREASE,
            LATE_WINTER_TEMPERATURE_DECREASE,
            LATE_AUTUMN_TEMPERATURE_DECREASE,
            tempRate_DAWN,
            tempRate_DAY,
            tempRate_DUSK,
            tempRate_NIGHT,
            tempRate_HARD,
            TemperatureWaterDecrease,
            dropSpeedWater,
            dropSpeedRain,
            dropSpeedThunder
        );

		if(EM_Settings.biomeProperties.containsKey(id) && !EM_ConfigHandler.loadedConfigs.contains(filename))
		{
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.NORMAL.getLevel()) EnviroMine.logger.log(Level.ERROR, "CONFIG DUPLICATE: Biome ID "+id +" was already added from "+ EM_Settings.biomeProperties.get(id).loadedFrom.toUpperCase() +" and will be overriden by "+ filename.toUpperCase());
		}
		EM_Settings.biomeProperties.put(id, entry);
	}

	@Override
	public void SaveProperty(Configuration config, String category)
	{
		config.get(category, BOName[0], this.id).getInt(0);
		config.get(category, BOName[1], this.biomeOveride).getBoolean(this.biomeOveride);
		config.get(category, BOName[2], this.waterQuality,
				"Water Quality: " +
						"RADIOACTIVE_FROSTY, " +
						"FROSTY, " +
						"RADIOACTIVE_COLD, " +
						"DIRTY_COLD, " +
						"SALTY_COLD, " +
						"CLEAN_COLD, " +
						"RADIOACTIVE, " +
						"DIRTY, " +
						"SALTY, " +
						"CLEAN, " +
						"RADIOACTIVE_WARM, " +
						"DIRTY_WARM, " +
						"SALTY_WARM, " +
						"CLEAN_WARM, " +
						"RADIOACTIVE_HOT, " +
						"HOT "
						).getString();
		config.get(category, BOName[3], this.ambientTemp, "Biome temperature in celsius (Player body temp is offset by + 12C)").getDouble(this.ambientTemp);
		config.get(category, BOName[4], this.tempRate).getDouble(this.tempRate);
		config.get(category, BOName[5], this.sanityRate).getDouble(this.sanityRate);
		config.get(category, BOName[6], this.dehydrateRate).getDouble(this.dehydrateRate);
		config.get(category, BOName[7], this.airRate).getDouble(this.airRate);
        config.get(category, BOName[8], this.isDesertBiome, "Affects the temperature difference at night / day (useful for deserts)").getBoolean(this.isDesertBiome);
        config.get(category, BOName[9], this.DesertBiomeTemperatureMultiplier, "The temperatureChange will be multiplied by this number if isDesertBiome=true").getDouble(this.DesertBiomeTemperatureMultiplier);

        config.get(category, BOName[10], this.DAWN_TEMPERATURE, "Biome temperature will decrease by this amount at dawn").getDouble(this.DAWN_TEMPERATURE);
        config.get(category, BOName[11], this.DAY_TEMPERATURE, "Biome temperature will decrease by this amount at day").getDouble(this.DAY_TEMPERATURE);
        config.get(category, BOName[12], this.DUSK_TEMPERATURE, "Biome temperature will decrease by this amount at dusk").getDouble(this.DUSK_TEMPERATURE);
        config.get(category, BOName[13], this.NIGHT_TEMPERATURE, "Biome temperature will decrease by this amount at midnight").getDouble(this.NIGHT_TEMPERATURE);

        config.get(category, BOName[14], this.TemperatureRainDecrease, "Biome temperature decreases by n degrees if it rains").getDouble(this.TemperatureRainDecrease);
        config.get(category, BOName[15], this.TemperatureThunderDecrease, "Biome temperature decreases by n degrees if there is a thunderstorm").getDouble(this.TemperatureThunderDecrease);
        config.get(category, BOName[16], this.TemperatureRainBool, "Should the biome temperature decreases if it rains?").getBoolean(this.TemperatureRainBool);
        config.get(category, BOName[17], this.TemperatureThunderBool, "Should the biome temperature decreases if there is a thunderstorm?").getBoolean(this.TemperatureThunderBool);
        config.get(category, BOName[18], this.TemperatureShadeDecrease, "Biome temperature decreases by n degrees if player in the shadow").getDouble(this.TemperatureShadeDecrease);

        config.get(category, BOName[19], this.ambientTemp_TERRAFORMED, "Air temperature will be equal to this number if the planet is terraformed").getDouble(this.ambientTemp_TERRAFORMED);
        config.get(category, BOName[20], this.DAWN_TEMPERATURE_TERRAFORMED, "Biome temperature will decrease by this amount at dawn if the planet is terraformed").getDouble(this.DAWN_TEMPERATURE_TERRAFORMED);
        config.get(category, BOName[21], this.DAY_TEMPERATURE_TERRAFORMED, "Biome temperature will decrease by this amount at day if the planet is terraformed").getDouble(this.DAY_TEMPERATURE_TERRAFORMED);
        config.get(category, BOName[22], this.DUSK_TEMPERATURE_TERRAFORMED, "Biome temperature will decrease by this amount at dusk if the planet is terraformed").getDouble(this.DUSK_TEMPERATURE_TERRAFORMED);
        config.get(category, BOName[23], this.NIGHT_TEMPERATURE_TERRAFORMED, "Biome temperature will decrease by this amount at midnight if the planet is terraformed").getDouble(this.NIGHT_TEMPERATURE_TERRAFORMED);

        config.get(category, BOName[24], this.EARLY_SPRING_TEMPERATURE_DECREASE, "Biome temperature will decrease by this amount at early spring").getDouble(this.EARLY_SPRING_TEMPERATURE_DECREASE);
        config.get(category, BOName[25], this.EARLY_SUMMER_TEMPERATURE_DECREASE, "Biome temperature will decrease by this amount at early summer").getDouble(this.EARLY_SUMMER_TEMPERATURE_DECREASE);
        config.get(category, BOName[26], this.EARLY_WINTER_TEMPERATURE_DECREASE, "Biome temperature will decrease by this amount at early winter").getDouble(this.EARLY_WINTER_TEMPERATURE_DECREASE);
        config.get(category, BOName[27], this.EARLY_AUTUMN_TEMPERATURE_DECREASE, "Biome temperature will decrease by this amount at early autumn").getDouble(this.EARLY_AUTUMN_TEMPERATURE_DECREASE);
        config.get(category, BOName[28], this.MID_SPRING_TEMPERATURE_DECREASE, "Biome temperature will decrease by this amount at mid spring").getDouble(this.MID_SPRING_TEMPERATURE_DECREASE);
        config.get(category, BOName[29], this.MID_SUMMER_TEMPERATURE_DECREASE, "Biome temperature will decrease by this amount at mid summer").getDouble(this.MID_SUMMER_TEMPERATURE_DECREASE);
        config.get(category, BOName[30], this.MID_WINTER_TEMPERATURE_DECREASE, "Biome temperature will decrease by this amount at mid winter").getDouble(this.MID_WINTER_TEMPERATURE_DECREASE);
        config.get(category, BOName[31], this.MID_AUTUMN_TEMPERATURE_DECREASE, "Biome temperature will decrease by this amount at mid autumn").getDouble(this.MID_AUTUMN_TEMPERATURE_DECREASE);
        config.get(category, BOName[32], this.LATE_SPRING_TEMPERATURE_DECREASE, "Biome temperature will decrease by this amount at late spring").getDouble(this.LATE_SPRING_TEMPERATURE_DECREASE);
        config.get(category, BOName[33], this.LATE_SUMMER_TEMPERATURE_DECREASE, "Biome temperature will decrease by this amount at late summer").getDouble(this.LATE_SUMMER_TEMPERATURE_DECREASE);
        config.get(category, BOName[34], this.LATE_WINTER_TEMPERATURE_DECREASE, "Biome temperature will decrease by this amount at late winter").getDouble(this.LATE_WINTER_TEMPERATURE_DECREASE);
        config.get(category, BOName[35], this.LATE_AUTUMN_TEMPERATURE_DECREASE, "Biome temperature will decrease by this amount at late autumn").getDouble(this.LATE_AUTUMN_TEMPERATURE_DECREASE);

        config.get(category, BOName[36], this.tempRate_DAWN, "Biome temperature rate will be this number at dawn").getDouble(this.tempRate_DAWN);
        config.get(category, BOName[37], this.tempRate_DAY, "Biome temperature rate will be this number at day").getDouble(this.tempRate_DAY);
        config.get(category, BOName[38], this.tempRate_DUSK, "Biome temperature rate will be this number at dusk").getDouble(this.tempRate_DUSK);
        config.get(category, BOName[39], this.tempRate_NIGHT, "Biome temperature rate will be this number at midnight").getDouble(this.tempRate_NIGHT);

        config.get(category, BOName[40], this.tempRate_HARD, "The temperature of the biome is so strong that weak suits like MITTY/HEV cannot protect against it").getBoolean(this.tempRate_HARD);

        config.get(category, BOName[41], this.TemperatureWaterDecrease,"Biome temperature decreases by this amount if the player is in water").getDouble(this.TemperatureWaterDecrease);
        config.get(category, BOName[42], this.dropSpeedWater,"Biome drop speed will be this number if the player is in water").getDouble(this.dropSpeedWater);

        config.get(category, BOName[43], this.dropSpeedRain,"Biome drop speed will be this number if it rains").getDouble(this.dropSpeedRain);
        config.get(category, BOName[44], this.dropSpeedThunder,"Biome drop speed will be this number if there is a thunderstorm").getDouble(this.dropSpeedThunder);
    }

	public void GenDefaultsProperty(BiomeGenBase[] biomeArray) {
		for (BiomeGenBase biome : biomeArray) {
			if (biome == null) {
				continue;
			}
			
			String modID = ModIdentification.idFromObject(biome);
			
			File file = new File(EM_ConfigHandler.loadedProfile + EM_ConfigHandler.customPath + EnviroUtils.SafeFilename(modID) + ".cfg");
			
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (Exception e) {
					if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel())
						EnviroMine.logger.log(Level.ERROR, "Failed to create file for biome '" + biome.biomeName + "'", e);
					continue;
				}
			}
			
			Configuration config = new Configuration(file, true);
			
			config.load();
			
			generateEmpty(config, biome);
			
			config.save();
		}
	}
	
	@Override
	public void GenDefaults() {
		BiomeGenBase[] biomeArray = BiomeGenBase.getBiomeGenArray();
		GenDefaultsProperty(biomeArray);
		if(EnviroMine.isLOTRLoaded) {
			BiomeProperties_LOTR.handleLOTRStuff(BOName);
		}
	}
	
	@Override
	public File GetDefaultFile()
	{
		return new File(EM_ConfigHandler.loadedProfile + EM_ConfigHandler.customPath + "Biomes.cfg");
	}

	@Override
	public void generateEmpty(Configuration config, Object obj)
	{
        if(obj == null || !(obj instanceof BiomeGenBase biome)) //DONT TOUCH
		{
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.NORMAL.getLevel()) EnviroMine.logger.log(Level.ERROR, "Tried to register config with non biome object!", new Exception());
			return;
		}

        ArrayList<Type> typeList = new ArrayList<Type>();
		Type[] typeArray = BiomeDictionary.getTypesForBiome(biome);
        Collections.addAll(typeList, typeArray);
		
		double air = typeList.contains(Type.NETHER)? -0.1D : 0D;
		double sanity = typeList.contains(Type.NETHER)? -0.1D : 0D;
		double water = typeList.contains(Type.NETHER) || typeList.contains(Type.DRY)? 0.05D : 0D;
		double temp = typeList.contains(Type.NETHER) || typeList.contains(Type.DRY)? 0.005D : 0D;
        boolean isDesertBiome = typeList.contains(Type.DESERT) || typeList.contains(Type.DRY);
        double DesertBiomeTemperatureMultiplier = typeList.contains(Type.DESERT) || typeList.contains(Type.DRY)? 3D : 1D;

        double TemperatureRainDecrease = typeList.contains(Type.WATER) ? 8D : typeList.contains(Type.JUNGLE)? 2D : 6D;
        double TemperatureThunderDecrease = typeList.contains(Type.WATER) ? 10D : typeList.contains(Type.JUNGLE)? 4D : 8D;
        //boolean TemperatureRainBool = typeList.contains(Type.WATER) || typeList.contains(Type.WET) || typeList.contains(Type.JUNGLE) || (typeList.contains(Type.FOREST) && typeList.contains(Type.WET)) || (typeList.contains(Type.FOREST) && !typeList.contains(Type.COLD)) || (typeList.contains(Type.PLAINS) && !typeList.contains(Type.HOT));
        //boolean TemperatureThunderBool = typeList.contains(Type.WATER) || typeList.contains(Type.WET) || typeList.contains(Type.JUNGLE) || (typeList.contains(Type.FOREST) && typeList.contains(Type.WET)) || (typeList.contains(Type.FOREST) && !typeList.contains(Type.COLD));

        boolean TemperatureRainBool = true;
        boolean TemperatureThunderBool = true;

        double TemperatureShadeDecrease = /*typeList.contains(Type.WATER) ? 12D : typeList.contains(Type.JUNGLE)? 10D :*/ 2.5D;

        String biomeWater = EnviroUtils.getBiomeWater(biome);

        double biomeTemp         = EnviroUtils.getBiomeTemp(biome);
        double DAWN_TEMPERATURE  = 4D;
        double DAY_TEMPERATURE   = 0D;
        double DUSK_TEMPERATURE  = 4D;
        double NIGHT_TEMPERATURE = 8D;

        double ambientTemp_TERRAFORMED 			= EnviroUtils.getBiomeTemp(biome);
        double DAWN_TEMPERATURE_TERRAFORMED		= 4F;
        double DAY_TEMPERATURE_TERRAFORMED		= 0F;
        double DUSK_TEMPERATURE_TERRAFORMED		= 4F;
        double NIGHT_TEMPERATURE_TERRAFORMED	= 8F;

        double tempRate_DAWN  = temp;
        double tempRate_DAY   = temp;
        double tempRate_DUSK  = temp;
        double tempRate_NIGHT = temp;

        boolean tempRate_HARD = false;

        float TemperatureWaterDecrease = 10.0F;
        float dropSpeedWater = 0.01F;

        float dropSpeedRain = 0.01F;
        float dropSpeedThunder = 0.01F;

        double EARLY_SPRING_TEMPERATURE_DECREASE =
            (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -3.0 : //DESERT              (-8 (DEFAULT+8))
            typeList.contains(Type.JUNGLE) ? 0.0 :                                  //JUNGLE              (-5 (DEFAULT+5))
            typeList.contains(Type.HOT) ? 3.0 :                                     //ELSE HOT (SAVANNA)  (-2 (DEFAULT+2C))
            typeList.contains(Type.CONIFEROUS) ? 7.0 :                              //TAIGA               (+2 (DEFAULT-2C))
            5.0;                                                                    //DEFAULT             (0)

        double MID_SPRING_TEMPERATURE_DECREASE =
            (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -10.0 : // DESERT              (-8 (DEFAULT+8))
                typeList.contains(Type.JUNGLE) ? -7.0 :                              // JUNGLE              (-5 (DEFAULT+5))
                        typeList.contains(Type.HOT) ? -4.0 :                         // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
                            typeList.contains(Type.CONIFEROUS) ? 0.0 :               // TAIGA               (+2 (DEFAULT-2))
                            -2.0;                                                    // DEFAULT             (0)

        double LATE_SPRING_TEMPERATURE_DECREASE =
            (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -9.0 : // DESERT              (-8 (DEFAULT+8))
                typeList.contains(Type.JUNGLE) ? -6.0 :                             // JUNGLE              (-5 (DEFAULT+5))
                        typeList.contains(Type.HOT) ? -3.0 :                        // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
                            typeList.contains(Type.CONIFEROUS) ? 1.0 :              // TAIGA               (+2 (DEFAULT-2))
                            -1.0;                                                   // DEFAULT             (0)

        double EARLY_SUMMER_TEMPERATURE_DECREASE =
            (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -9.0 : // DESERT              (-8 (DEFAULT+8))
                typeList.contains(Type.JUNGLE) ? -6.0 :                             // JUNGLE              (-5 (DEFAULT+5))
                        typeList.contains(Type.HOT) ? -3.0 :                        // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
                            typeList.contains(Type.CONIFEROUS) ? 1.0 :              // TAIGA               (+2 (DEFAULT-2))
                            -1.0;                                                   // DEFAULT             (0)

        double MID_SUMMER_TEMPERATURE_DECREASE =
            (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -11.0 : // DESERT              (-8 (DEFAULT+8))
                typeList.contains(Type.JUNGLE) ? -8.0 :                             // JUNGLE              (-5 (DEFAULT+5))
                        typeList.contains(Type.HOT) ? -5.0 :                        // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
                            typeList.contains(Type.CONIFEROUS) ? -1.0 :             // TAIGA               (+2 (DEFAULT-2))
                            -3.0;                                                   // DEFAULT             (0)

        double LATE_SUMMER_TEMPERATURE_DECREASE =
            (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -9.0 : // DESERT              (-8 (DEFAULT+8))
                typeList.contains(Type.JUNGLE) ? -6.0 :                             // JUNGLE              (-5 (DEFAULT+5))
                        typeList.contains(Type.HOT) ? -3.0 :                        // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
                            typeList.contains(Type.CONIFEROUS) ? 1.0 :              // TAIGA               (+2 (DEFAULT-2))
                            -1.0;                                                   // DEFAULT             (0)

        double EARLY_AUTUMN_TEMPERATURE_DECREASE =
            (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -2.0 : // DESERT              (-8 (DEFAULT+8))
                typeList.contains(Type.JUNGLE) ? 1.0 :                              // JUNGLE              (-5 (DEFAULT+5))
                        typeList.contains(Type.HOT) ? 4.0 :                         // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
                            typeList.contains(Type.CONIFEROUS) ? 8.0 :              // TAIGA               (+2 (DEFAULT-2))
                            6.0;                                                    // DEFAULT             (0)

        double MID_AUTUMN_TEMPERATURE_DECREASE =
            (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? 0.0 :  // DESERT              (-8 (DEFAULT+8))
                typeList.contains(Type.JUNGLE) ? 3.0 :                              // JUNGLE              (-5 (DEFAULT+5))
                        typeList.contains(Type.HOT) ? 6.0 :                         // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
                            typeList.contains(Type.CONIFEROUS) ? 10.0 :             // TAIGA               (+2 (DEFAULT-2))
                            8.0;                                                    // DEFAULT             (0)

        double LATE_AUTUMN_TEMPERATURE_DECREASE =
            (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -2.0 : // DESERT              (-8 (DEFAULT+8))
                typeList.contains(Type.JUNGLE) ? 5.0 :                             // JUNGLE              (-5 (DEFAULT+5))
                        typeList.contains(Type.HOT) ? 8.0 :                         // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
                            typeList.contains(Type.CONIFEROUS) ? 12.0 :             // TAIGA               (+2 (DEFAULT-2))
                            10.0;                                                   // DEFAULT             (0)

        double EARLY_WINTER_TEMPERATURE_DECREASE =
            (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? 4.0 :   // DESERT              (-8 (DEFAULT+8))
                typeList.contains(Type.JUNGLE) ? 7.0 :                               // JUNGLE              (-5 (DEFAULT+5))
                        typeList.contains(Type.HOT) ? 10.0 :                         // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
                            typeList.contains(Type.CONIFEROUS) ? 14.0 :              // TAIGA               (+2 (DEFAULT-2))
                            12.0;                                                    // DEFAULT             (0)

        double MID_WINTER_TEMPERATURE_DECREASE =
            (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? 8.0 :  // DESERT              (-8 (DEFAULT+8))
                typeList.contains(Type.JUNGLE) ? 11.0 :                             // JUNGLE              (-5 (DEFAULT+5))
                        typeList.contains(Type.HOT) ? 14.0 :                        // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2)))
                            typeList.contains(Type.CONIFEROUS) ? 18.0 :             // TAIGA               (+2 (DEFAULT-2))
                            16.0;                                                   // DEFAULT             (0)

        double LATE_WINTER_TEMPERATURE_DECREASE =
            (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? 2.0 :  // DESERT              (-8 (DEFAULT+8))
                typeList.contains(Type.JUNGLE) ? 5.0 :                              // JUNGLE              (-5 (DEFAULT+5))
                        typeList.contains(Type.HOT) ? 8.0 :                         // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
                            typeList.contains(Type.CONIFEROUS) ? 12.0 :             // TAIGA               (+2 (DEFAULT-2))
                            10.0;                                                   // DEFAULT             (0)


		//This is just fucked up,
		// I created a huge Excel spreadsheet with a ton of values to calculate all this crap.
		// I already have a headache because of the fucking game about fucking cubes
		
		//^^^ Look what 1 hour of "La Realismé" did to bro
		
		//If anyone is interested in which version is faster: external or integrated, then here it is:
		//integrated : 21:04:27 - 21:05:33 = 1 minute, 6 seconds
		//external   : 21:08:41 - 21:09:42 = 1 minute, 1 second
		
		//So it's NORMAL ↓↓↓
        if(EnviroMine.isHbmSpaceLoaded) {
			BiomeProperties_NTM_SPACE.registerNTMSpaceBiomes(
					config,
					biome,
					BOName,
					biomeWater,
					biomeTemp,
					temp,
					sanity,
					water,
					air,
			 		isDesertBiome,
			 		DesertBiomeTemperatureMultiplier,
					DAWN_TEMPERATURE,
					DAY_TEMPERATURE,
					DUSK_TEMPERATURE,
					NIGHT_TEMPERATURE,
					TemperatureRainDecrease,
					TemperatureThunderDecrease,
					TemperatureRainBool,
					TemperatureThunderBool,
					TemperatureShadeDecrease,
					ambientTemp_TERRAFORMED,
					DAWN_TEMPERATURE_TERRAFORMED,
					DAY_TEMPERATURE_TERRAFORMED,
					DUSK_TEMPERATURE_TERRAFORMED,
					NIGHT_TEMPERATURE_TERRAFORMED,
					EARLY_SPRING_TEMPERATURE_DECREASE,
					EARLY_SUMMER_TEMPERATURE_DECREASE,
					EARLY_WINTER_TEMPERATURE_DECREASE,
					EARLY_AUTUMN_TEMPERATURE_DECREASE,
					MID_SPRING_TEMPERATURE_DECREASE,
					MID_SUMMER_TEMPERATURE_DECREASE,
					MID_WINTER_TEMPERATURE_DECREASE,
					MID_AUTUMN_TEMPERATURE_DECREASE,
					LATE_SPRING_TEMPERATURE_DECREASE,
					LATE_SUMMER_TEMPERATURE_DECREASE,
					LATE_WINTER_TEMPERATURE_DECREASE,
					LATE_AUTUMN_TEMPERATURE_DECREASE,
					tempRate_DAWN,
					tempRate_DAY,
					tempRate_DUSK,
					tempRate_NIGHT,
					tempRate_HARD,
					TemperatureWaterDecrease,
					dropSpeedWater,
					dropSpeedRain,
					dropSpeedThunder
			);
        }
		
		String catName = this.categoryName() + "." + biome.biomeName;

		config.get(catName, BOName[0], biome.biomeID).getInt(biome.biomeID);
		config.get(catName, BOName[1], true).getBoolean(true);
		config.get(catName, BOName[2], biomeWater,
				"Water Quality: " +
						"RADIOACTIVE_FROSTY, " +
						"FROSTY, " +
						"RADIOACTIVE_COLD, " +
						"DIRTY_COLD, " +
						"SALTY_COLD, " +
						"CLEAN_COLD, " +
						"RADIOACTIVE, " +
						"DIRTY, " +
						"SALTY, " +
						"CLEAN, " +
						"RADIOACTIVE_WARM, " +
						"DIRTY_WARM, " +
						"SALTY_WARM, " +
						"CLEAN_WARM, " +
						"RADIOACTIVE_HOT, " +
						"HOT "
		).getString();
		config.get(catName, BOName[3], biomeTemp, "Biome temperature in celsius (Player body temp is offset by + 12C)").getDouble(25.00);
		config.get(catName, BOName[4], temp).getDouble(temp);
		config.get(catName, BOName[5], sanity).getDouble(sanity);
		config.get(catName, BOName[6], water).getDouble(water);
		config.get(catName, BOName[7], air).getDouble(air);
        config.get(catName, BOName[8], isDesertBiome).getBoolean(isDesertBiome);
        config.get(catName, BOName[9], DesertBiomeTemperatureMultiplier).getDouble(DesertBiomeTemperatureMultiplier);

        config.get(catName, BOName[10], DAWN_TEMPERATURE).getDouble(DAWN_TEMPERATURE);
        config.get(catName, BOName[11], DAY_TEMPERATURE).getDouble(DAY_TEMPERATURE);
        config.get(catName, BOName[12], DUSK_TEMPERATURE).getDouble(DUSK_TEMPERATURE);
        config.get(catName, BOName[13], NIGHT_TEMPERATURE).getDouble(NIGHT_TEMPERATURE);

        config.get(catName, BOName[14], TemperatureRainDecrease).getDouble(TemperatureRainDecrease);
        config.get(catName, BOName[15], TemperatureThunderDecrease).getDouble(TemperatureThunderDecrease);
        config.get(catName, BOName[16], TemperatureRainBool).getBoolean(TemperatureRainBool);
        config.get(catName, BOName[17], TemperatureThunderBool).getBoolean(TemperatureThunderBool);
        config.get(catName, BOName[18], TemperatureShadeDecrease).getDouble(TemperatureShadeDecrease);

        config.get(catName, BOName[19], ambientTemp_TERRAFORMED).getDouble(ambientTemp_TERRAFORMED);
        config.get(catName, BOName[20], DAWN_TEMPERATURE_TERRAFORMED).getDouble(DAWN_TEMPERATURE_TERRAFORMED);
        config.get(catName, BOName[21], DAY_TEMPERATURE_TERRAFORMED).getDouble(DAY_TEMPERATURE_TERRAFORMED);
        config.get(catName, BOName[22], DUSK_TEMPERATURE_TERRAFORMED).getDouble(DUSK_TEMPERATURE_TERRAFORMED);
        config.get(catName, BOName[23], NIGHT_TEMPERATURE_TERRAFORMED).getDouble(NIGHT_TEMPERATURE_TERRAFORMED);

        config.get(catName, BOName[24], EARLY_SPRING_TEMPERATURE_DECREASE).getDouble(EARLY_SPRING_TEMPERATURE_DECREASE);
        config.get(catName, BOName[25], EARLY_SUMMER_TEMPERATURE_DECREASE).getDouble(EARLY_SUMMER_TEMPERATURE_DECREASE);
        config.get(catName, BOName[26], EARLY_WINTER_TEMPERATURE_DECREASE).getDouble(EARLY_WINTER_TEMPERATURE_DECREASE);
        config.get(catName, BOName[27], EARLY_AUTUMN_TEMPERATURE_DECREASE).getDouble(EARLY_AUTUMN_TEMPERATURE_DECREASE);
        config.get(catName, BOName[28], MID_SPRING_TEMPERATURE_DECREASE).getDouble(MID_SPRING_TEMPERATURE_DECREASE);
        config.get(catName, BOName[29], MID_SUMMER_TEMPERATURE_DECREASE).getDouble(MID_SUMMER_TEMPERATURE_DECREASE);
        config.get(catName, BOName[30], MID_WINTER_TEMPERATURE_DECREASE).getDouble(MID_WINTER_TEMPERATURE_DECREASE);
        config.get(catName, BOName[31], MID_AUTUMN_TEMPERATURE_DECREASE).getDouble(MID_AUTUMN_TEMPERATURE_DECREASE);
        config.get(catName, BOName[32], LATE_SPRING_TEMPERATURE_DECREASE).getDouble(LATE_SPRING_TEMPERATURE_DECREASE);
        config.get(catName, BOName[33], LATE_SUMMER_TEMPERATURE_DECREASE).getDouble(LATE_SUMMER_TEMPERATURE_DECREASE);
        config.get(catName, BOName[34], LATE_WINTER_TEMPERATURE_DECREASE).getDouble(LATE_WINTER_TEMPERATURE_DECREASE);
        config.get(catName, BOName[35], LATE_AUTUMN_TEMPERATURE_DECREASE).getDouble(LATE_AUTUMN_TEMPERATURE_DECREASE);

        config.get(catName, BOName[36], tempRate_DAWN).getDouble(tempRate_DAWN);
        config.get(catName, BOName[37], tempRate_DAY).getDouble(tempRate_DAY);
        config.get(catName, BOName[38], tempRate_DUSK).getDouble(tempRate_DUSK);
        config.get(catName, BOName[39], tempRate_NIGHT).getDouble(tempRate_NIGHT);

        config.get(catName, BOName[40], tempRate_HARD).getBoolean(tempRate_HARD);

        config.get(catName, BOName[41], TemperatureWaterDecrease).getDouble(TemperatureWaterDecrease);
        config.get(catName, BOName[42], dropSpeedWater).getDouble(dropSpeedWater);

        config.get(catName, BOName[43], dropSpeedRain).getDouble(dropSpeedRain);
        config.get(catName, BOName[44], dropSpeedThunder).getDouble(dropSpeedThunder);
    }

	@Override
	public boolean useCustomConfigs()
	{
		return true;
	}

	@Override
	public void customLoad()
	{
	}

	static
	{
		BOName = new String[45];
		BOName[0] = "01.Biome ID";
		BOName[1] = "02.Allow Config Override";
		BOName[2] = "03.Water Quality";
		BOName[3] = "04.Ambient Temperature";
		BOName[4] = "05.Temperature Rate";
		BOName[5] = "06.Sanity Rate";
		BOName[6] = "07.Dehydrate Rate";
		BOName[7] = "08.Air Quality Rate";
        BOName[8] = "09.Is desert biome?";
        BOName[9] = "10.Desert Biome Temperature Multiplier";
        BOName[10] = "11.Dawn Biome Temperature Decrease";
        BOName[11] = "12.Day Biome Temperature Decrease";
        BOName[12] = "13.Dusk Biome Temperature Decrease";
        BOName[13] = "14.Night Biome Temperature Decrease";
        BOName[14] = "15.Biome Temperature Rain Decrease";
        BOName[15] = "16.Biome Temperature Thunder Decrease";
        BOName[16] = "17.Should Biome Temperature Decrease When Rain?";
        BOName[17] = "18.Should Biome Temperature Decrease When Thunder?";
        BOName[18] = "19.Biome Temperature Shadow Decrease";
        BOName[19] = "20.[HBM] Ambient Temperature Terraformed";
        BOName[20] = "21.[HBM] Dawn Biome Temperature Decrease Terraformed";
        BOName[21] = "22.[HBM] Day Biome Temperature Decrease Terraformed";
        BOName[22] = "23.[HBM] Dusk Biome Temperature Decrease Terraformed";
        BOName[23] = "24.[HBM] Night Biome Temperature Decrease Terraformed";
        BOName[24] = "25.[Serene Seasons] Early Spring Biome Temperature Decrease";
        BOName[25] = "26.[Serene Seasons] Early Summer Biome Temperature Decrease";
        BOName[26] = "27.[Serene Seasons] Early Winter Biome Temperature Decrease";
        BOName[27] = "28.[Serene Seasons] Early Autumn Biome Temperature Decrease";
        BOName[28] = "29.[Serene Seasons] Mid Spring Biome Temperature Decrease";
        BOName[29] = "30.[Serene Seasons] Mid Summer Biome Temperature Decrease";
        BOName[30] = "31.[Serene Seasons] Mid Winter Biome Temperature Decrease";
        BOName[31] = "32.[Serene Seasons] Mid Autumn Biome Temperature Decrease";
        BOName[32] = "33.[Serene Seasons] Late Spring Biome Temperature Decrease";
        BOName[33] = "34.[Serene Seasons] Late Summer Biome Temperature Decrease";
        BOName[34] = "35.[Serene Seasons] Late Winter Biome Temperature Decrease";
        BOName[35] = "36.[Serene Seasons] Late Autumn Biome Temperature Decrease";
        BOName[36] = "37.Dawn Biome Temperature Rate";
        BOName[37] = "38.Day Biome Temperature Rate";
        BOName[38] = "39.Dusk Biome Temperature Rate";
        BOName[39] = "40.Night Biome Temperature Rate";
        BOName[40] = "41.[HBM] Hard Biome Temperature Rate";
        BOName[41] = "42.Ambient Temperature Decrease Water";
        BOName[42] = "43.Drop Speed Water";
        BOName[43] = "44.Drop Speed Rain";
        BOName[44] = "45.Drop Speed Thunder";
	}
}
