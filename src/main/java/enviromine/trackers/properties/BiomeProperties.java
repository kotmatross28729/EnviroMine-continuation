package enviromine.trackers.properties;

import static enviromine.utils.WaterUtils.coolDown;
import static enviromine.utils.WaterUtils.forcePollute;
import static enviromine.utils.WaterUtils.forceSaltDown;
import static enviromine.utils.WaterUtils.getStringFromType;
import static enviromine.utils.WaterUtils.getTypeFromString;
import static enviromine.utils.WaterUtils.heatUp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import enviromine.core.EM_ConfigHandler;
import enviromine.core.EM_ConfigHandler.EnumLogVerbosity;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.trackers.properties.compat.BiomeProperties_LOTR;
import enviromine.trackers.properties.compat.BiomeProperties_NTM;
import enviromine.trackers.properties.compat.BiomeProperties_NTM_SPACE;
import enviromine.trackers.properties.compat.BiomeProperties_SS;
import enviromine.trackers.properties.helpers.PropertyBase;
import enviromine.trackers.properties.helpers.SerialisableProperty;
import enviromine.utils.EnviroUtils;
import enviromine.utils.ModIdentification;
import enviromine.utils.WaterUtils;
import enviromine.utils.misc.CompatSafe;

@CompatSafe
public class BiomeProperties implements SerialisableProperty, PropertyBase {

    public static final BiomeProperties base = new BiomeProperties();
    static String[] BOName;

    public int id;
    public boolean biomeOveride;
    public String waterQuality;
    public float ambientTemp;
    public float tempRate;
    public float sanityRate;
    public float dehydrateRate;
    public float airRate;
    public String loadedFrom;
    public float TemperatureMultiplier;
    public float DAWN_TEMPERATURE_DECREASE;
    public float DAY_TEMPERATURE_DECREASE;
    public float DUSK_TEMPERATURE_DECREASE;
    public float NIGHT_TEMPERATURE_DECREASE;

    public float TemperatureRainDecrease;
    public float TemperatureThunderDecrease;
    public boolean TemperatureRainBool;
    public boolean TemperatureThunderBool;
    public float TemperatureShadeDecrease;

    public float ambientTemp_TERRAFORMED;
    public float DAWN_TEMPERATURE_DECREASE_TERRAFORMED;
    public float DAY_TEMPERATURE_DECREASE_TERRAFORMED;
    public float DUSK_TEMPERATURE_DECREASE_TERRAFORMED;
    public float NIGHT_TEMPERATURE_DECREASE_TERRAFORMED;

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

    public String SPRING_waterQuality;
    public String SUMMER_waterQuality;
    public String AUTUMN_waterQuality;
    public String WINTER_waterQuality;

    public BiomeProperties(NBTTagCompound tags) {
        this.ReadFromNBT(tags);
    }

    public BiomeProperties() {
        // THIS CONSTRUCTOR IS FOR STATIC PURPOSES ONLY!
        if (base != null && base != this) // DONT TOUCH
        {
            throw new IllegalStateException();
        }
    }

    public BiomeProperties(int id, boolean biomeOveride, String waterQuality, float ambientTemp, float tempRate,
        float sanityRate, float dehydrateRate, float airRate, String filename, float TemperatureMultiplier,

        float DAWN_TEMPERATURE_DECREASE, float DAY_TEMPERATURE_DECREASE, float DUSK_TEMPERATURE_DECREASE,
        float NIGHT_TEMPERATURE_DECREASE,

        float TemperatureRainDecrease, float TemperatureThunderDecrease, boolean TemperatureRainBool,
        boolean TemperatureThunderBool, float TemperatureShadeDecrease,

        float ambientTemp_TERRAFORMED, float DAWN_TEMPERATURE_DECREASE_TERRAFORMED,
        float DAY_TEMPERATURE_DECREASE_TERRAFORMED, float DUSK_TEMPERATURE_DECREASE_TERRAFORMED,
        float NIGHT_TEMPERATURE_DECREASE_TERRAFORMED,

        float EARLY_SPRING_TEMPERATURE_DECREASE, float EARLY_SUMMER_TEMPERATURE_DECREASE,
        float EARLY_WINTER_TEMPERATURE_DECREASE, float EARLY_AUTUMN_TEMPERATURE_DECREASE,
        float MID_SPRING_TEMPERATURE_DECREASE, float MID_SUMMER_TEMPERATURE_DECREASE,
        float MID_WINTER_TEMPERATURE_DECREASE, float MID_AUTUMN_TEMPERATURE_DECREASE,
        float LATE_SPRING_TEMPERATURE_DECREASE, float LATE_SUMMER_TEMPERATURE_DECREASE,
        float LATE_WINTER_TEMPERATURE_DECREASE, float LATE_AUTUMN_TEMPERATURE_DECREASE, float tempRate_DAWN,
        float tempRate_DAY, float tempRate_DUSK, float tempRate_NIGHT, boolean tempRate_HARD,
        float TemperatureWaterDecrease, float dropSpeedWater, float dropSpeedRain, float dropSpeedThunder,
        String SPRING_waterQuality, String SUMMER_waterQuality, String AUTUMN_waterQuality,
        String WINTER_waterQuality) {
        this.id = id;
        this.biomeOveride = biomeOveride;
        this.waterQuality = waterQuality;
        this.ambientTemp = ambientTemp;
        this.tempRate = tempRate;
        this.sanityRate = sanityRate;
        this.dehydrateRate = dehydrateRate;
        this.airRate = airRate;
        this.loadedFrom = filename;
        this.TemperatureMultiplier = TemperatureMultiplier;

        this.DAWN_TEMPERATURE_DECREASE = DAWN_TEMPERATURE_DECREASE;
        this.DAY_TEMPERATURE_DECREASE = DAY_TEMPERATURE_DECREASE;
        this.DUSK_TEMPERATURE_DECREASE = DUSK_TEMPERATURE_DECREASE;
        this.NIGHT_TEMPERATURE_DECREASE = NIGHT_TEMPERATURE_DECREASE;

        this.TemperatureRainDecrease = TemperatureRainDecrease;
        this.TemperatureThunderDecrease = TemperatureThunderDecrease;
        this.TemperatureRainBool = TemperatureRainBool;
        this.TemperatureThunderBool = TemperatureThunderBool;
        this.TemperatureShadeDecrease = TemperatureShadeDecrease;

        this.ambientTemp_TERRAFORMED = ambientTemp_TERRAFORMED;
        this.DAWN_TEMPERATURE_DECREASE_TERRAFORMED = DAWN_TEMPERATURE_DECREASE_TERRAFORMED;
        this.DAY_TEMPERATURE_DECREASE_TERRAFORMED = DAY_TEMPERATURE_DECREASE_TERRAFORMED;
        this.DUSK_TEMPERATURE_DECREASE_TERRAFORMED = DUSK_TEMPERATURE_DECREASE_TERRAFORMED;
        this.NIGHT_TEMPERATURE_DECREASE_TERRAFORMED = NIGHT_TEMPERATURE_DECREASE_TERRAFORMED;

        this.EARLY_SPRING_TEMPERATURE_DECREASE = EARLY_SPRING_TEMPERATURE_DECREASE;
        this.EARLY_SUMMER_TEMPERATURE_DECREASE = EARLY_SUMMER_TEMPERATURE_DECREASE;
        this.EARLY_WINTER_TEMPERATURE_DECREASE = EARLY_WINTER_TEMPERATURE_DECREASE;
        this.EARLY_AUTUMN_TEMPERATURE_DECREASE = EARLY_AUTUMN_TEMPERATURE_DECREASE;
        this.MID_SPRING_TEMPERATURE_DECREASE = MID_SPRING_TEMPERATURE_DECREASE;
        this.MID_SUMMER_TEMPERATURE_DECREASE = MID_SUMMER_TEMPERATURE_DECREASE;
        this.MID_WINTER_TEMPERATURE_DECREASE = MID_WINTER_TEMPERATURE_DECREASE;
        this.MID_AUTUMN_TEMPERATURE_DECREASE = MID_AUTUMN_TEMPERATURE_DECREASE;
        this.LATE_SPRING_TEMPERATURE_DECREASE = LATE_SPRING_TEMPERATURE_DECREASE;
        this.LATE_SUMMER_TEMPERATURE_DECREASE = LATE_SUMMER_TEMPERATURE_DECREASE;
        this.LATE_WINTER_TEMPERATURE_DECREASE = LATE_WINTER_TEMPERATURE_DECREASE;
        this.LATE_AUTUMN_TEMPERATURE_DECREASE = LATE_AUTUMN_TEMPERATURE_DECREASE;

        this.tempRate_DAWN = tempRate_DAWN;
        this.tempRate_DAY = tempRate_DAY;
        this.tempRate_DUSK = tempRate_DUSK;
        this.tempRate_NIGHT = tempRate_NIGHT;

        this.tempRate_HARD = tempRate_HARD;

        this.TemperatureWaterDecrease = TemperatureWaterDecrease;
        this.dropSpeedWater = dropSpeedWater;

        this.dropSpeedRain = dropSpeedRain;
        this.dropSpeedThunder = dropSpeedThunder;

        this.SPRING_waterQuality = SPRING_waterQuality;
        this.SUMMER_waterQuality = SUMMER_waterQuality;
        this.AUTUMN_waterQuality = AUTUMN_waterQuality;
        this.WINTER_waterQuality = WINTER_waterQuality;
    }

    /**
     * <b>hasProperty(BiomeGenBase biome)</b><bR>
     * <br>
     * Checks if Property contains custom properties.
     * 
     * @param biome
     * @return true if has custom properties
     */
    public boolean hasProperty(BiomeGenBase biome) {
        return EM_Settings.biomeProperties.containsKey(biome.biomeID);
    }

    /**
     * <b>getProperty(BiomeGenBase biome)</b><bR>
     * <br>
     * Gets Property.
     * 
     * @param biome
     * @return BiomeProperties
     */
    public BiomeProperties getProperty(BiomeGenBase biome) {
        return EM_Settings.biomeProperties.get(biome.biomeID);
    }

    public WaterUtils.WATER_TYPES getWaterQuality(World world) {

        if (EnviroMine.isSereneSeasonsLoaded) {
            try {
                String seasonWater = BiomeProperties_SS.checkSeasonWater(
                    world,
                    this.SPRING_waterQuality,
                    this.SUMMER_waterQuality,
                    this.AUTUMN_waterQuality,
                    this.WINTER_waterQuality);
                if (seasonWater != null) {
                    return WaterUtils.getTypeFromString(seasonWater);
                }
            } catch (NoSuchFieldError fuckoff) {}
        }

        return WaterUtils.getTypeFromString(this.waterQuality);
    }

    @Override
    public NBTTagCompound WriteToNBT() {
        NBTTagCompound tags = new NBTTagCompound();
        tags.setInteger("id", this.id);
        tags.setBoolean("biomeOveride", this.biomeOveride);
        tags.setString("waterQuality", this.waterQuality);
        tags.setFloat("ambientTemp", this.ambientTemp);
        tags.setFloat("tempRate", this.tempRate);
        tags.setFloat("sanityRate", this.sanityRate);
        tags.setFloat("dehydrateRate", this.dehydrateRate);
        tags.setFloat("TemperatureMultiplier", this.TemperatureMultiplier);

        tags.setFloat("DAWN_TEMPERATURE_DECREASE", this.DAWN_TEMPERATURE_DECREASE);
        tags.setFloat("DAY_TEMPERATURE_DECREASE", this.DAY_TEMPERATURE_DECREASE);
        tags.setFloat("DUSK_TEMPERATURE_DECREASE", this.DUSK_TEMPERATURE_DECREASE);
        tags.setFloat("NIGHT_TEMPERATURE_DECREASE", this.NIGHT_TEMPERATURE_DECREASE);

        tags.setFloat("TemperatureRainDecrease", this.TemperatureRainDecrease);
        tags.setFloat("TemperatureThunderDecrease", this.TemperatureThunderDecrease);
        tags.setBoolean("TemperatureRainBool", this.TemperatureRainBool);
        tags.setBoolean("TemperatureThunderBool", this.TemperatureThunderBool);
        tags.setFloat("TemperatureShadeDecrease", this.TemperatureShadeDecrease);

        tags.setFloat("ambientTemp_TERRAFORMED", this.ambientTemp_TERRAFORMED);
        tags.setFloat("DAWN_TEMPERATURE_DECREASE_TERRAFORMED", this.DAWN_TEMPERATURE_DECREASE_TERRAFORMED);
        tags.setFloat("DAY_TEMPERATURE_DECREASE_TERRAFORMED", this.DAY_TEMPERATURE_DECREASE_TERRAFORMED);
        tags.setFloat("DUSK_TEMPERATURE_DECREASE_TERRAFORMED", this.DUSK_TEMPERATURE_DECREASE_TERRAFORMED);
        tags.setFloat("NIGHT_TEMPERATURE_DECREASE_TERRAFORMED", this.NIGHT_TEMPERATURE_DECREASE_TERRAFORMED);

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

        tags.setString("SPRING_waterQuality", this.SPRING_waterQuality);
        tags.setString("SUMMER_waterQuality", this.SUMMER_waterQuality);
        tags.setString("AUTUMN_waterQuality", this.AUTUMN_waterQuality);
        tags.setString("WINTER_waterQuality", this.WINTER_waterQuality);

        return tags;
    }

    @Override
    public void ReadFromNBT(NBTTagCompound tags) {
        this.id = tags.getInteger("id");
        this.biomeOveride = tags.getBoolean("biomeOveride");
        this.waterQuality = tags.getString("waterQuality");
        this.ambientTemp = tags.getFloat("ambientTemp");
        this.tempRate = tags.getFloat("tempRate");
        this.sanityRate = tags.getFloat("sanityRate");
        this.dehydrateRate = tags.getFloat("dehydrateRate");
        this.TemperatureMultiplier = tags.getFloat("TemperatureMultiplier");

        this.DAWN_TEMPERATURE_DECREASE = tags.getFloat("DAWN_TEMPERATURE_DECREASE");
        this.DAY_TEMPERATURE_DECREASE = tags.getFloat("DAY_TEMPERATURE_DECREASE");
        this.DUSK_TEMPERATURE_DECREASE = tags.getFloat("DUSK_TEMPERATURE_DECREASE");
        this.NIGHT_TEMPERATURE_DECREASE = tags.getFloat("NIGHT_TEMPERATURE_DECREASE");

        this.TemperatureRainDecrease = tags.getFloat("TemperatureRainDecrease");
        this.TemperatureThunderDecrease = tags.getFloat("TemperatureThunderDecrease");
        this.TemperatureRainBool = tags.getBoolean("TemperatureRainBool");
        this.TemperatureThunderBool = tags.getBoolean("TemperatureThunderBool");
        this.TemperatureShadeDecrease = tags.getFloat("TemperatureShadeDecrease");

        this.ambientTemp_TERRAFORMED = tags.getFloat("ambientTemp_TERRAFORMED");
        this.DAWN_TEMPERATURE_DECREASE_TERRAFORMED = tags.getFloat("DAWN_TEMPERATURE_DECREASE_TERRAFORMED");
        this.DAY_TEMPERATURE_DECREASE_TERRAFORMED = tags.getFloat("DAY_TEMPERATURE_DECREASE_TERRAFORMED");
        this.DUSK_TEMPERATURE_DECREASE_TERRAFORMED = tags.getFloat("DUSK_TEMPERATURE_DECREASE_TERRAFORMED");
        this.NIGHT_TEMPERATURE_DECREASE_TERRAFORMED = tags.getFloat("NIGHT_TEMPERATURE_DECREASE_TERRAFORMED");

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

        this.SPRING_waterQuality = tags.getString("SPRING_waterQuality");
        this.SUMMER_waterQuality = tags.getString("SUMMER_waterQuality");
        this.AUTUMN_waterQuality = tags.getString("AUTUMN_waterQuality");
        this.WINTER_waterQuality = tags.getString("WINTER_waterQuality");
    }

    @Override
    public String categoryName() {
        return "biomes";
    }

    @Override
    public String categoryDescription() {
        return "Manually change the environmental properties of each biome";
    }

    @Override
    public void LoadProperty(Configuration config, String category) {
        config.setCategoryComment(this.categoryName(), this.categoryDescription());
        int id = config.get(category, BOName[0], 0)
            .getInt(0);
        boolean biomeOveride = config.get(category, BOName[1], false)
            .getBoolean(false);
        String waterQ = config
            .get(
                category,
                BOName[2],
                "CLEAN",
                "Water Quality: " + "RADIOACTIVE_FROSTY, "
                    + "FROSTY, "
                    + "RADIOACTIVE_COLD, "
                    + "DIRTY_COLD, "
                    + "SALTY_COLD, "
                    + "CLEAN_COLD, "
                    + "RADIOACTIVE, "
                    + "DIRTY, "
                    + "SALTY, "
                    + "CLEAN, "
                    + "RADIOACTIVE_WARM, "
                    + "DIRTY_WARM, "
                    + "SALTY_WARM, "
                    + "CLEAN_WARM, "
                    + "RADIOACTIVE_HOT, "
                    + "HOT ")
            .getString();
        float ambTemp = (float) config
            .get(category, BOName[3], 25.00, "Biome temperature in celsius (Player body temp is offset by + 12C)")
            .getDouble(25.00);
        float tempRate = (float) config.get(category, BOName[4], 0.0)
            .getDouble(0.0);
        float sanRate = (float) config.get(category, BOName[5], 0.0)
            .getDouble(0.0);
        float dehyRate = (float) config.get(category, BOName[6], 0.0)
            .getDouble(0.0);
        float airRate = (float) config.get(category, BOName[7], 0.0)
            .getDouble(0.0);
        String filename = config.getConfigFile()
            .getName();
        float TemperatureMultiplier = (float) config.get(category, BOName[8], 1.0)
            .getDouble(1.0);

        float DAWN_TEMPERATURE_DECREASE = (float) config.get(category, BOName[9], 4.0)
            .getDouble(4.0);
        float DAY_TEMPERATURE_DECREASE = (float) config.get(category, BOName[10], 0.0)
            .getDouble(0.0);
        float DUSK_TEMPERATURE_DECREASE = (float) config.get(category, BOName[11], 4.0)
            .getDouble(4.0);
        float NIGHT_TEMPERATURE_DECREASE = (float) config.get(category, BOName[12], 8.0)
            .getDouble(8.0);

        float TemperatureRainDecrease = (float) config.get(category, BOName[13], 6.0)
            .getDouble(6.0);
        float TemperatureThunderDecrease = (float) config.get(category, BOName[14], 8.0)
            .getDouble(8.0);

        boolean TemperatureRainBool = config.get(category, BOName[15], true)
            .getBoolean(true);
        boolean TemperatureThunderBool = config.get(category, BOName[16], true)
            .getBoolean(true);
        float TemperatureShadeDecrease = (float) config.get(category, BOName[17], 2.5)
            .getDouble(2.5);

        float ambientTemp_TERRAFORMED = (float) config.get(category, BOName[18], 25.0)
            .getDouble(25.0);
        float DAWN_TEMPERATURE_DECREASE_TERRAFORMED = (float) config.get(category, BOName[19], 4.0)
            .getDouble(4.0);
        float DAY_TEMPERATURE_DECREASE_TERRAFORMED = (float) config.get(category, BOName[20], 0.0)
            .getDouble(0.0);
        float DUSK_TEMPERATURE_DECREASE_TERRAFORMED = (float) config.get(category, BOName[21], 4.0)
            .getDouble(4.0);
        float NIGHT_TEMPERATURE_DECREASE_TERRAFORMED = (float) config.get(category, BOName[22], 8.0)
            .getDouble(8.0);

        float EARLY_SPRING_TEMPERATURE_DECREASE = (float) config.get(category, BOName[23], 5.0)
            .getDouble(5.0);
        float EARLY_SUMMER_TEMPERATURE_DECREASE = (float) config.get(category, BOName[24], -1.0)
            .getDouble(-1.0);
        float EARLY_WINTER_TEMPERATURE_DECREASE = (float) config.get(category, BOName[25], 12.0)
            .getDouble(12.0);
        float EARLY_AUTUMN_TEMPERATURE_DECREASE = (float) config.get(category, BOName[26], 6.0)
            .getDouble(6.0);
        float MID_SPRING_TEMPERATURE_DECREASE = (float) config.get(category, BOName[27], -2.0)
            .getDouble(-2.0);
        float MID_SUMMER_TEMPERATURE_DECREASE = (float) config.get(category, BOName[28], -3.0)
            .getDouble(-3.0);
        float MID_WINTER_TEMPERATURE_DECREASE = (float) config.get(category, BOName[29], 15.0)
            .getDouble(15.0);
        float MID_AUTUMN_TEMPERATURE_DECREASE = (float) config.get(category, BOName[30], 8.0)
            .getDouble(8.0);
        float LATE_SPRING_TEMPERATURE_DECREASE = (float) config.get(category, BOName[31], -1.0)
            .getDouble(-1.0);
        float LATE_SUMMER_TEMPERATURE_DECREASE = (float) config.get(category, BOName[32], -1.0)
            .getDouble(-1.0);
        float LATE_WINTER_TEMPERATURE_DECREASE = (float) config.get(category, BOName[33], 10.0)
            .getDouble(10.0);
        float LATE_AUTUMN_TEMPERATURE_DECREASE = (float) config.get(category, BOName[34], 10.0)
            .getDouble(10.0);

        float tempRate_DAWN = (float) config.get(category, BOName[35], 0.0)
            .getDouble(0.0);
        float tempRate_DAY = (float) config.get(category, BOName[36], 0.0)
            .getDouble(0.0);
        float tempRate_DUSK = (float) config.get(category, BOName[37], 0.0)
            .getDouble(0.0);
        float tempRate_NIGHT = (float) config.get(category, BOName[38], 0.0)
            .getDouble(0.0);

        boolean tempRate_HARD = config.get(category, BOName[39], false)
            .getBoolean(false);

        float TemperatureWaterDecrease = (float) config.get(category, BOName[40], 10.0)
            .getDouble(10.0);
        float dropSpeedWater = (float) config.get(category, BOName[41], 0.01)
            .getDouble(0.01);

        float dropSpeedRain = (float) config.get(category, BOName[42], 0.01)
            .getDouble(0.01);
        float dropSpeedThunder = (float) config.get(category, BOName[43], 0.01)
            .getDouble(0.01);

        String SPRING_waterQuality = config.get(category, BOName[44], "CLEAN", "Water Quality at spring")
            .getString();
        String SUMMER_waterQuality = config.get(category, BOName[45], "CLEAN", "Water Quality at summer")
            .getString();
        String AUTUMN_waterQuality = config.get(category, BOName[46], "CLEAN", "Water Quality at autumn")
            .getString();
        String WINTER_waterQuality = config.get(category, BOName[47], "CLEAN", "Water Quality at winter")
            .getString();

        BiomeProperties entry = new BiomeProperties(
            id,
            biomeOveride,
            waterQ,
            ambTemp,
            tempRate,
            sanRate,
            dehyRate,
            airRate,
            filename,
            TemperatureMultiplier,
            DAWN_TEMPERATURE_DECREASE,
            DAY_TEMPERATURE_DECREASE,
            DUSK_TEMPERATURE_DECREASE,
            NIGHT_TEMPERATURE_DECREASE,
            TemperatureRainDecrease,
            TemperatureThunderDecrease,
            TemperatureRainBool,
            TemperatureThunderBool,
            TemperatureShadeDecrease,
            ambientTemp_TERRAFORMED,
            DAWN_TEMPERATURE_DECREASE_TERRAFORMED,
            DAY_TEMPERATURE_DECREASE_TERRAFORMED,
            DUSK_TEMPERATURE_DECREASE_TERRAFORMED,
            NIGHT_TEMPERATURE_DECREASE_TERRAFORMED,
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
            dropSpeedThunder,
            SPRING_waterQuality,
            SUMMER_waterQuality,
            AUTUMN_waterQuality,
            WINTER_waterQuality);

        if (EM_Settings.biomeProperties.containsKey(id) && !EM_ConfigHandler.loadedConfigs.contains(filename)) {
            if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.NORMAL.getLevel()) EnviroMine.logger.log(
                Level.ERROR,
                "CONFIG DUPLICATE: Biome ID " + id
                    + " was already added from "
                    + EM_Settings.biomeProperties.get(id).loadedFrom.toUpperCase()
                    + " and will be overriden by "
                    + filename.toUpperCase());
        }
        EM_Settings.biomeProperties.put(id, entry);
    }

    @Override
    public void SaveProperty(Configuration config, String category) {
        config.get(category, BOName[0], this.id)
            .getInt(0);
        config.get(category, BOName[1], this.biomeOveride)
            .getBoolean(this.biomeOveride);
        config
            .get(
                category,
                BOName[2],
                this.waterQuality,
                "Water Quality: " + "RADIOACTIVE_FROSTY, "
                    + "FROSTY, "
                    + "RADIOACTIVE_COLD, "
                    + "DIRTY_COLD, "
                    + "SALTY_COLD, "
                    + "CLEAN_COLD, "
                    + "RADIOACTIVE, "
                    + "DIRTY, "
                    + "SALTY, "
                    + "CLEAN, "
                    + "RADIOACTIVE_WARM, "
                    + "DIRTY_WARM, "
                    + "SALTY_WARM, "
                    + "CLEAN_WARM, "
                    + "RADIOACTIVE_HOT, "
                    + "HOT ")
            .getString();
        config
            .get(
                category,
                BOName[3],
                this.ambientTemp,
                "Biome temperature in celsius (Player body temp is offset by + 12C)")
            .getDouble(this.ambientTemp);
        config.get(category, BOName[4], this.tempRate)
            .getDouble(this.tempRate);
        config.get(category, BOName[5], this.sanityRate)
            .getDouble(this.sanityRate);
        config.get(category, BOName[6], this.dehydrateRate)
            .getDouble(this.dehydrateRate);
        config.get(category, BOName[7], this.airRate)
            .getDouble(this.airRate);
        config
            .get(
                category,
                BOName[8],
                this.TemperatureMultiplier,
                "The temperatureChange will be multiplied by this number")
            .getDouble(this.TemperatureMultiplier);

        config
            .get(
                category,
                BOName[9],
                this.DAWN_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at dawn")
            .getDouble(this.DAWN_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[10],
                this.DAY_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at day")
            .getDouble(this.DAY_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[11],
                this.DUSK_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at dusk")
            .getDouble(this.DUSK_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[12],
                this.NIGHT_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at midnight")
            .getDouble(this.NIGHT_TEMPERATURE_DECREASE);

        config
            .get(
                category,
                BOName[13],
                this.TemperatureRainDecrease,
                "Biome temperature decreases by n degrees if it rains")
            .getDouble(this.TemperatureRainDecrease);
        config
            .get(
                category,
                BOName[14],
                this.TemperatureThunderDecrease,
                "Biome temperature decreases by n degrees if there is a thunderstorm")
            .getDouble(this.TemperatureThunderDecrease);
        config
            .get(category, BOName[15], this.TemperatureRainBool, "Should the biome temperature decreases if it rains?")
            .getBoolean(this.TemperatureRainBool);
        config
            .get(
                category,
                BOName[16],
                this.TemperatureThunderBool,
                "Should the biome temperature decreases if there is a thunderstorm?")
            .getBoolean(this.TemperatureThunderBool);
        config
            .get(
                category,
                BOName[17],
                this.TemperatureShadeDecrease,
                "Biome temperature decreases by n degrees if player in the shadow")
            .getDouble(this.TemperatureShadeDecrease);

        config
            .get(
                category,
                BOName[18],
                this.ambientTemp_TERRAFORMED,
                "Air temperature will be equal to this number if the planet is terraformed")
            .getDouble(this.ambientTemp_TERRAFORMED);
        config
            .get(
                category,
                BOName[19],
                this.DAWN_TEMPERATURE_DECREASE_TERRAFORMED,
                "Biome temperature will decrease by this amount at dawn if the planet is terraformed")
            .getDouble(this.DAWN_TEMPERATURE_DECREASE_TERRAFORMED);
        config
            .get(
                category,
                BOName[20],
                this.DAY_TEMPERATURE_DECREASE_TERRAFORMED,
                "Biome temperature will decrease by this amount at day if the planet is terraformed")
            .getDouble(this.DAY_TEMPERATURE_DECREASE_TERRAFORMED);
        config
            .get(
                category,
                BOName[21],
                this.DUSK_TEMPERATURE_DECREASE_TERRAFORMED,
                "Biome temperature will decrease by this amount at dusk if the planet is terraformed")
            .getDouble(this.DUSK_TEMPERATURE_DECREASE_TERRAFORMED);
        config
            .get(
                category,
                BOName[22],
                this.NIGHT_TEMPERATURE_DECREASE_TERRAFORMED,
                "Biome temperature will decrease by this amount at midnight if the planet is terraformed")
            .getDouble(this.NIGHT_TEMPERATURE_DECREASE_TERRAFORMED);

        config
            .get(
                category,
                BOName[23],
                this.EARLY_SPRING_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at early spring")
            .getDouble(this.EARLY_SPRING_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[24],
                this.EARLY_SUMMER_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at early summer")
            .getDouble(this.EARLY_SUMMER_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[25],
                this.EARLY_WINTER_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at early winter")
            .getDouble(this.EARLY_WINTER_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[26],
                this.EARLY_AUTUMN_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at early autumn")
            .getDouble(this.EARLY_AUTUMN_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[27],
                this.MID_SPRING_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at mid spring")
            .getDouble(this.MID_SPRING_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[28],
                this.MID_SUMMER_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at mid summer")
            .getDouble(this.MID_SUMMER_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[29],
                this.MID_WINTER_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at mid winter")
            .getDouble(this.MID_WINTER_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[30],
                this.MID_AUTUMN_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at mid autumn")
            .getDouble(this.MID_AUTUMN_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[31],
                this.LATE_SPRING_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at late spring")
            .getDouble(this.LATE_SPRING_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[32],
                this.LATE_SUMMER_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at late summer")
            .getDouble(this.LATE_SUMMER_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[33],
                this.LATE_WINTER_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at late winter")
            .getDouble(this.LATE_WINTER_TEMPERATURE_DECREASE);
        config
            .get(
                category,
                BOName[34],
                this.LATE_AUTUMN_TEMPERATURE_DECREASE,
                "Biome temperature will decrease by this amount at late autumn")
            .getDouble(this.LATE_AUTUMN_TEMPERATURE_DECREASE);

        config.get(category, BOName[35], this.tempRate_DAWN, "Biome temperature rate will be this number at dawn")
            .getDouble(this.tempRate_DAWN);
        config.get(category, BOName[36], this.tempRate_DAY, "Biome temperature rate will be this number at day")
            .getDouble(this.tempRate_DAY);
        config.get(category, BOName[37], this.tempRate_DUSK, "Biome temperature rate will be this number at dusk")
            .getDouble(this.tempRate_DUSK);
        config.get(category, BOName[38], this.tempRate_NIGHT, "Biome temperature rate will be this number at midnight")
            .getDouble(this.tempRate_NIGHT);

        config
            .get(
                category,
                BOName[39],
                this.tempRate_HARD,
                "The temperature of the biome is so strong that weak suits like MITTY/HEV cannot protect against it")
            .getBoolean(this.tempRate_HARD);

        config
            .get(
                category,
                BOName[40],
                this.TemperatureWaterDecrease,
                "Biome temperature decreases by this amount if the player is in water")
            .getDouble(this.TemperatureWaterDecrease);
        config
            .get(
                category,
                BOName[41],
                this.dropSpeedWater,
                "Biome drop speed will be this number if the player is in water")
            .getDouble(this.dropSpeedWater);

        config.get(category, BOName[42], this.dropSpeedRain, "Biome drop speed will be this number if it rains")
            .getDouble(this.dropSpeedRain);
        config
            .get(
                category,
                BOName[43],
                this.dropSpeedThunder,
                "Biome drop speed will be this number if there is a thunderstorm")
            .getDouble(this.dropSpeedThunder);

        config.get(category, BOName[44], this.SPRING_waterQuality, "Water Quality at spring")
            .getString();
        config.get(category, BOName[45], this.SUMMER_waterQuality, "Water Quality at summer")
            .getString();
        config.get(category, BOName[46], this.AUTUMN_waterQuality, "Water Quality at autumn")
            .getString();
        config.get(category, BOName[47], this.WINTER_waterQuality, "Water Quality at winter")
            .getString();
    }

    public void GenDefaultsProperty(BiomeGenBase[] biomeArray) {
        for (BiomeGenBase biome : biomeArray) {
            if (biome == null) {
                continue;
            }

            String modID = ModIdentification.idFromObject(biome);

            File file = new File(
                EM_ConfigHandler.loadedProfile + EM_ConfigHandler.customPath
                    + EnviroUtils.SafeFilename(modID)
                    + ".cfg");

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger
                        .log(Level.ERROR, "Failed to create file for biome '" + biome.biomeName + "'", e);
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
        if (EnviroMine.isLOTRLoaded) {
            try {
                BiomeProperties_LOTR.handleLOTRStuff(BOName);
            } catch (NoSuchFieldError fuckoff) {}
        }
    }

    @Override
    public File GetDefaultFile() {
        return new File(EM_ConfigHandler.loadedProfile + EM_ConfigHandler.customPath + "Biomes.cfg");
    }

    @Override
    public void generateEmpty(Configuration config, Object obj) {
        if (obj == null || !(obj instanceof BiomeGenBase biome)) // DONT TOUCH
        {
            if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.NORMAL.getLevel())
                EnviroMine.logger.log(Level.ERROR, "Tried to register config with non biome object!", new Exception());
            return;
        }

        ArrayList<Type> typeList = new ArrayList<Type>();
        Type[] typeArray = BiomeDictionary.getTypesForBiome(biome);
        Collections.addAll(typeList, typeArray);

        double air = typeList.contains(Type.NETHER) ? -0.1D : 0D;
        double sanity = typeList.contains(Type.NETHER) ? -0.1D : 0D;
        double water = typeList.contains(Type.NETHER) || typeList.contains(Type.DRY) ? 0.05D : 0D;
        double temp = typeList.contains(Type.NETHER) || typeList.contains(Type.DRY) ? 0.005D : 0D;
        double TemperatureMultiplier = 1D;

        double TemperatureRainDecrease = typeList.contains(Type.WATER) ? 8D : typeList.contains(Type.JUNGLE) ? 2D : 6D;
        double TemperatureThunderDecrease = typeList.contains(Type.WATER) ? 10D
            : typeList.contains(Type.JUNGLE) ? 4D : 8D;

        boolean TemperatureRainBool = true;
        boolean TemperatureThunderBool = true;

        double TemperatureShadeDecrease = 2.5D;

        String biomeWater = EnviroUtils.getBiomeWater(biome);

        double biomeTemp = EnviroUtils.getBiomeTemp(biome);
        double DAWN_TEMPERATURE_DECREASE = 4D;
        double DAY_TEMPERATURE_DECREASE = 0D;
        double DUSK_TEMPERATURE_DECREASE = 4D;
        double NIGHT_TEMPERATURE_DECREASE = 8D;

        double ambientTemp_TERRAFORMED = EnviroUtils.getBiomeTemp(biome);
        double DAWN_TEMPERATURE_DECREASE_TERRAFORMED = 4F;
        double DAY_TEMPERATURE_DECREASE_TERRAFORMED = 0F;
        double DUSK_TEMPERATURE_DECREASE_TERRAFORMED = 4F;
        double NIGHT_TEMPERATURE_DECREASE_TERRAFORMED = 8F;

        double tempRate_DAWN = temp;
        double tempRate_DAY = temp;
        double tempRate_DUSK = temp;
        double tempRate_NIGHT = temp;

        boolean tempRate_HARD = false;

        float TemperatureWaterDecrease = 10.0F;
        float dropSpeedWater = 0.01F;

        float dropSpeedRain = 0.01F;
        float dropSpeedThunder = 0.01F;

        String SPRING_waterQuality;
        String SUMMER_waterQuality;
        String AUTUMN_waterQuality;
        String WINTER_waterQuality;

        if (getTypeFromString(EnviroUtils.getBiomeWater(biome)).isSalty) { // Because oceans don't become fresh in
                                                                           // summer/winter
            SPRING_waterQuality = EnviroUtils.getBiomeWater(biome);
            SUMMER_waterQuality = getStringFromType(
                forceSaltDown(heatUp(getTypeFromString(EnviroUtils.getBiomeWater(biome)))));
            AUTUMN_waterQuality = EnviroUtils.getBiomeWater(biome);
            WINTER_waterQuality = getStringFromType(
                forceSaltDown(coolDown(getTypeFromString(EnviroUtils.getBiomeWater(biome)), 2)));
        } else if (getTypeFromString(EnviroUtils.getBiomeWater(biome)).isDirty) { // Same with swamps
            SPRING_waterQuality = EnviroUtils.getBiomeWater(biome);
            SUMMER_waterQuality = getStringFromType(
                forcePollute(heatUp(getTypeFromString(EnviroUtils.getBiomeWater(biome)))));
            AUTUMN_waterQuality = EnviroUtils.getBiomeWater(biome);
            WINTER_waterQuality = getStringFromType(
                forcePollute(coolDown(getTypeFromString(EnviroUtils.getBiomeWater(biome)), 2)));
        } else {
            SPRING_waterQuality = EnviroUtils.getBiomeWater(biome);
            SUMMER_waterQuality = getStringFromType(heatUp(getTypeFromString(EnviroUtils.getBiomeWater(biome))));
            AUTUMN_waterQuality = EnviroUtils.getBiomeWater(biome);
            WINTER_waterQuality = getStringFromType(coolDown(getTypeFromString(EnviroUtils.getBiomeWater(biome)), 2));
        }

        double EARLY_SPRING_TEMPERATURE_DECREASE = (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -3.0
            : // DESERT (-8 (DEFAULT+8))
            typeList.contains(Type.JUNGLE) ? 0.0 : // JUNGLE (-5 (DEFAULT+5))
                typeList.contains(Type.HOT) ? 3.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2C))
                    typeList.contains(Type.CONIFEROUS) ? 7.0 : // TAIGA (+2 (DEFAULT-2C))
                        5.0; // DEFAULT (0)

        double MID_SPRING_TEMPERATURE_DECREASE = (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -10.0
            : // DESERT (-8 (DEFAULT+8))
            typeList.contains(Type.JUNGLE) ? -7.0 : // JUNGLE (-5 (DEFAULT+5))
                typeList.contains(Type.HOT) ? -4.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                    typeList.contains(Type.CONIFEROUS) ? 0.0 : // TAIGA (+2 (DEFAULT-2))
                        -2.0; // DEFAULT (0)

        double LATE_SPRING_TEMPERATURE_DECREASE = (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -9.0
            : // DESERT (-8 (DEFAULT+8))
            typeList.contains(Type.JUNGLE) ? -6.0 : // JUNGLE (-5 (DEFAULT+5))
                typeList.contains(Type.HOT) ? -3.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                    typeList.contains(Type.CONIFEROUS) ? 1.0 : // TAIGA (+2 (DEFAULT-2))
                        -1.0; // DEFAULT (0)

        double EARLY_SUMMER_TEMPERATURE_DECREASE = (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -9.0
            : // DESERT (-8 (DEFAULT+8))
            typeList.contains(Type.JUNGLE) ? -6.0 : // JUNGLE (-5 (DEFAULT+5))
                typeList.contains(Type.HOT) ? -3.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                    typeList.contains(Type.CONIFEROUS) ? 1.0 : // TAIGA (+2 (DEFAULT-2))
                        -1.0; // DEFAULT (0)

        double MID_SUMMER_TEMPERATURE_DECREASE = (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -11.0
            : // DESERT (-8 (DEFAULT+8))
            typeList.contains(Type.JUNGLE) ? -8.0 : // JUNGLE (-5 (DEFAULT+5))
                typeList.contains(Type.HOT) ? -5.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                    typeList.contains(Type.CONIFEROUS) ? -1.0 : // TAIGA (+2 (DEFAULT-2))
                        -3.0; // DEFAULT (0)

        double LATE_SUMMER_TEMPERATURE_DECREASE = (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -9.0
            : // DESERT (-8 (DEFAULT+8))
            typeList.contains(Type.JUNGLE) ? -6.0 : // JUNGLE (-5 (DEFAULT+5))
                typeList.contains(Type.HOT) ? -3.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                    typeList.contains(Type.CONIFEROUS) ? 1.0 : // TAIGA (+2 (DEFAULT-2))
                        -1.0; // DEFAULT (0)

        double EARLY_AUTUMN_TEMPERATURE_DECREASE = (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -2.0
            : // DESERT (-8 (DEFAULT+8))
            typeList.contains(Type.JUNGLE) ? 1.0 : // JUNGLE (-5 (DEFAULT+5))
                typeList.contains(Type.HOT) ? 4.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                    typeList.contains(Type.CONIFEROUS) ? 8.0 : // TAIGA (+2 (DEFAULT-2))
                        6.0; // DEFAULT (0)

        double MID_AUTUMN_TEMPERATURE_DECREASE = (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? 0.0 : // DESERT
                                                                                                                        // (-8
                                                                                                                        // (DEFAULT+8))
            typeList.contains(Type.JUNGLE) ? 3.0 : // JUNGLE (-5 (DEFAULT+5))
                typeList.contains(Type.HOT) ? 6.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                    typeList.contains(Type.CONIFEROUS) ? 10.0 : // TAIGA (+2 (DEFAULT-2))
                        8.0; // DEFAULT (0)

        double LATE_AUTUMN_TEMPERATURE_DECREASE = (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? -2.0
            : // DESERT (-8 (DEFAULT+8))
            typeList.contains(Type.JUNGLE) ? 5.0 : // JUNGLE (-5 (DEFAULT+5))
                typeList.contains(Type.HOT) ? 8.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                    typeList.contains(Type.CONIFEROUS) ? 12.0 : // TAIGA (+2 (DEFAULT-2))
                        10.0; // DEFAULT (0)

        double EARLY_WINTER_TEMPERATURE_DECREASE = (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? 4.0
            : // DESERT (-8 (DEFAULT+8))
            typeList.contains(Type.JUNGLE) ? 7.0 : // JUNGLE (-5 (DEFAULT+5))
                typeList.contains(Type.HOT) ? 10.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                    typeList.contains(Type.CONIFEROUS) ? 14.0 : // TAIGA (+2 (DEFAULT-2))
                        12.0; // DEFAULT (0)

        double MID_WINTER_TEMPERATURE_DECREASE = (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? 8.0 : // DESERT
                                                                                                                        // (-8
                                                                                                                        // (DEFAULT+8))
            typeList.contains(Type.JUNGLE) ? 11.0 : // JUNGLE (-5 (DEFAULT+5))
                typeList.contains(Type.HOT) ? 14.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2)))
                    typeList.contains(Type.CONIFEROUS) ? 18.0 : // TAIGA (+2 (DEFAULT-2))
                        16.0; // DEFAULT (0)

        double LATE_WINTER_TEMPERATURE_DECREASE = (typeList.contains(Type.HOT) && typeList.contains(Type.SANDY)) ? 2.0 : // DESERT
                                                                                                                         // (-8
                                                                                                                         // (DEFAULT+8))
            typeList.contains(Type.JUNGLE) ? 5.0 : // JUNGLE (-5 (DEFAULT+5))
                typeList.contains(Type.HOT) ? 8.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                    typeList.contains(Type.CONIFEROUS) ? 12.0 : // TAIGA (+2 (DEFAULT-2))
                        10.0; // DEFAULT (0)

        // COMPAT
        if (EnviroMine.isHbmLoaded) {
            try {
                BiomeProperties_NTM.registerNTMBiomes(
                    config,
                    biome,
                    BOName,
                    biomeWater,
                    biomeTemp,
                    DAWN_TEMPERATURE_DECREASE,
                    DAY_TEMPERATURE_DECREASE,
                    DUSK_TEMPERATURE_DECREASE,
                    NIGHT_TEMPERATURE_DECREASE,
                    ambientTemp_TERRAFORMED,
                    SPRING_waterQuality,
                    SUMMER_waterQuality,
                    AUTUMN_waterQuality,
                    WINTER_waterQuality);
            } catch (NoSuchFieldError fuckoff) {}
        }
        if (EnviroMine.isHbmSpaceLoaded) {
            try {
                BiomeProperties_NTM_SPACE.registerNTMSpaceBiomes(
                    config,
                    biome,
                    BOName,
                    biomeWater,
                    biomeTemp,
                    DAWN_TEMPERATURE_DECREASE,
                    DAY_TEMPERATURE_DECREASE,
                    DUSK_TEMPERATURE_DECREASE,
                    NIGHT_TEMPERATURE_DECREASE,
                    ambientTemp_TERRAFORMED,
                    DAWN_TEMPERATURE_DECREASE_TERRAFORMED,
                    DAY_TEMPERATURE_DECREASE_TERRAFORMED,
                    DUSK_TEMPERATURE_DECREASE_TERRAFORMED,
                    NIGHT_TEMPERATURE_DECREASE_TERRAFORMED,
                    tempRate_DAWN,
                    tempRate_DAY,
                    tempRate_DUSK,
                    tempRate_NIGHT,
                    tempRate_HARD);
            } catch (NoSuchFieldError fuckoff) {}
        }

        String catName = this.categoryName() + "." + biome.biomeName;

        config.get(catName, BOName[0], biome.biomeID)
            .getInt(biome.biomeID);
        config.get(catName, BOName[1], true)
            .getBoolean(true);
        config
            .get(
                catName,
                BOName[2],
                biomeWater,
                "Water Quality: " + "RADIOACTIVE_FROSTY, "
                    + "FROSTY, "
                    + "RADIOACTIVE_COLD, "
                    + "DIRTY_COLD, "
                    + "SALTY_COLD, "
                    + "CLEAN_COLD, "
                    + "RADIOACTIVE, "
                    + "DIRTY, "
                    + "SALTY, "
                    + "CLEAN, "
                    + "RADIOACTIVE_WARM, "
                    + "DIRTY_WARM, "
                    + "SALTY_WARM, "
                    + "CLEAN_WARM, "
                    + "RADIOACTIVE_HOT, "
                    + "HOT ")
            .getString();
        config.get(catName, BOName[3], biomeTemp, "Biome temperature in celsius (Player body temp is offset by + 12C)")
            .getDouble(25.00);
        config.get(catName, BOName[4], temp)
            .getDouble(temp);
        config.get(catName, BOName[5], sanity)
            .getDouble(sanity);
        config.get(catName, BOName[6], water)
            .getDouble(water);
        config.get(catName, BOName[7], air)
            .getDouble(air);
        config.get(catName, BOName[8], TemperatureMultiplier)
            .getDouble(TemperatureMultiplier);

        config.get(catName, BOName[9], DAWN_TEMPERATURE_DECREASE)
            .getDouble(DAWN_TEMPERATURE_DECREASE);
        config.get(catName, BOName[10], DAY_TEMPERATURE_DECREASE)
            .getDouble(DAY_TEMPERATURE_DECREASE);
        config.get(catName, BOName[11], DUSK_TEMPERATURE_DECREASE)
            .getDouble(DUSK_TEMPERATURE_DECREASE);
        config.get(catName, BOName[12], NIGHT_TEMPERATURE_DECREASE)
            .getDouble(NIGHT_TEMPERATURE_DECREASE);

        config.get(catName, BOName[13], TemperatureRainDecrease)
            .getDouble(TemperatureRainDecrease);
        config.get(catName, BOName[14], TemperatureThunderDecrease)
            .getDouble(TemperatureThunderDecrease);
        config.get(catName, BOName[15], TemperatureRainBool)
            .getBoolean(TemperatureRainBool);
        config.get(catName, BOName[16], TemperatureThunderBool)
            .getBoolean(TemperatureThunderBool);
        config.get(catName, BOName[17], TemperatureShadeDecrease)
            .getDouble(TemperatureShadeDecrease);

        config.get(catName, BOName[18], ambientTemp_TERRAFORMED)
            .getDouble(ambientTemp_TERRAFORMED);
        config.get(catName, BOName[19], DAWN_TEMPERATURE_DECREASE_TERRAFORMED)
            .getDouble(DAWN_TEMPERATURE_DECREASE_TERRAFORMED);
        config.get(catName, BOName[20], DAY_TEMPERATURE_DECREASE_TERRAFORMED)
            .getDouble(DAY_TEMPERATURE_DECREASE_TERRAFORMED);
        config.get(catName, BOName[21], DUSK_TEMPERATURE_DECREASE_TERRAFORMED)
            .getDouble(DUSK_TEMPERATURE_DECREASE_TERRAFORMED);
        config.get(catName, BOName[22], NIGHT_TEMPERATURE_DECREASE_TERRAFORMED)
            .getDouble(NIGHT_TEMPERATURE_DECREASE_TERRAFORMED);

        config.get(catName, BOName[23], EARLY_SPRING_TEMPERATURE_DECREASE)
            .getDouble(EARLY_SPRING_TEMPERATURE_DECREASE);
        config.get(catName, BOName[24], EARLY_SUMMER_TEMPERATURE_DECREASE)
            .getDouble(EARLY_SUMMER_TEMPERATURE_DECREASE);
        config.get(catName, BOName[25], EARLY_WINTER_TEMPERATURE_DECREASE)
            .getDouble(EARLY_WINTER_TEMPERATURE_DECREASE);
        config.get(catName, BOName[26], EARLY_AUTUMN_TEMPERATURE_DECREASE)
            .getDouble(EARLY_AUTUMN_TEMPERATURE_DECREASE);
        config.get(catName, BOName[27], MID_SPRING_TEMPERATURE_DECREASE)
            .getDouble(MID_SPRING_TEMPERATURE_DECREASE);
        config.get(catName, BOName[28], MID_SUMMER_TEMPERATURE_DECREASE)
            .getDouble(MID_SUMMER_TEMPERATURE_DECREASE);
        config.get(catName, BOName[29], MID_WINTER_TEMPERATURE_DECREASE)
            .getDouble(MID_WINTER_TEMPERATURE_DECREASE);
        config.get(catName, BOName[30], MID_AUTUMN_TEMPERATURE_DECREASE)
            .getDouble(MID_AUTUMN_TEMPERATURE_DECREASE);
        config.get(catName, BOName[31], LATE_SPRING_TEMPERATURE_DECREASE)
            .getDouble(LATE_SPRING_TEMPERATURE_DECREASE);
        config.get(catName, BOName[32], LATE_SUMMER_TEMPERATURE_DECREASE)
            .getDouble(LATE_SUMMER_TEMPERATURE_DECREASE);
        config.get(catName, BOName[33], LATE_WINTER_TEMPERATURE_DECREASE)
            .getDouble(LATE_WINTER_TEMPERATURE_DECREASE);
        config.get(catName, BOName[34], LATE_AUTUMN_TEMPERATURE_DECREASE)
            .getDouble(LATE_AUTUMN_TEMPERATURE_DECREASE);

        config.get(catName, BOName[35], tempRate_DAWN)
            .getDouble(tempRate_DAWN);
        config.get(catName, BOName[36], tempRate_DAY)
            .getDouble(tempRate_DAY);
        config.get(catName, BOName[37], tempRate_DUSK)
            .getDouble(tempRate_DUSK);
        config.get(catName, BOName[38], tempRate_NIGHT)
            .getDouble(tempRate_NIGHT);

        config.get(catName, BOName[39], tempRate_HARD)
            .getBoolean(tempRate_HARD);

        config.get(catName, BOName[40], TemperatureWaterDecrease)
            .getDouble(TemperatureWaterDecrease);
        config.get(catName, BOName[41], dropSpeedWater)
            .getDouble(dropSpeedWater);

        config.get(catName, BOName[42], dropSpeedRain)
            .getDouble(dropSpeedRain);
        config.get(catName, BOName[43], dropSpeedThunder)
            .getDouble(dropSpeedThunder);

        config.get(catName, BOName[44], SPRING_waterQuality, "Water Quality at spring")
            .getString();
        config.get(catName, BOName[45], SUMMER_waterQuality, "Water Quality at summer")
            .getString();
        config.get(catName, BOName[46], AUTUMN_waterQuality, "Water Quality at autumn")
            .getString();
        config.get(catName, BOName[47], WINTER_waterQuality, "Water Quality at winter")
            .getString();
    }

    @Override
    public boolean useCustomConfigs() {
        return true;
    }

    @Override
    public void customLoad() {}

    static {
        BOName = new String[48];
        BOName[0] = "01.Biome ID";
        BOName[1] = "02.Allow Config Override";
        BOName[2] = "03.Water Quality";
        BOName[3] = "04.Ambient Temperature";
        BOName[4] = "05.Temperature Rate";
        BOName[5] = "06.Sanity Rate";
        BOName[6] = "07.Dehydrate Rate";
        BOName[7] = "08.Air Quality Rate";
        BOName[8] = "9.Temperature Multiplier";
        BOName[9] = "10.Dawn Biome Temperature Decrease";
        BOName[10] = "11.Day Biome Temperature Decrease";
        BOName[11] = "12.Dusk Biome Temperature Decrease";
        BOName[12] = "13.Night Biome Temperature Decrease";
        BOName[13] = "14.Biome Temperature Rain Decrease";
        BOName[14] = "15.Biome Temperature Thunder Decrease";
        BOName[15] = "16.Should Biome Temperature Decrease When Rain?";
        BOName[16] = "17.Should Biome Temperature Decrease When Thunder?";
        BOName[17] = "18.Biome Temperature Shadow Decrease";
        BOName[18] = "19.[HBM] Ambient Temperature Terraformed";
        BOName[19] = "20.[HBM] Dawn Biome Temperature Decrease Terraformed";
        BOName[20] = "21.[HBM] Day Biome Temperature Decrease Terraformed";
        BOName[21] = "22.[HBM] Dusk Biome Temperature Decrease Terraformed";
        BOName[22] = "23.[HBM] Night Biome Temperature Decrease Terraformed";
        BOName[23] = "24.[Serene Seasons] Early Spring Biome Temperature Decrease";
        BOName[24] = "25.[Serene Seasons] Early Summer Biome Temperature Decrease";
        BOName[25] = "26.[Serene Seasons] Early Winter Biome Temperature Decrease";
        BOName[26] = "27.[Serene Seasons] Early Autumn Biome Temperature Decrease";
        BOName[27] = "28.[Serene Seasons] Mid Spring Biome Temperature Decrease";
        BOName[28] = "29.[Serene Seasons] Mid Summer Biome Temperature Decrease";
        BOName[29] = "30.[Serene Seasons] Mid Winter Biome Temperature Decrease";
        BOName[30] = "31.[Serene Seasons] Mid Autumn Biome Temperature Decrease";
        BOName[31] = "32.[Serene Seasons] Late Spring Biome Temperature Decrease";
        BOName[32] = "33.[Serene Seasons] Late Summer Biome Temperature Decrease";
        BOName[33] = "34.[Serene Seasons] Late Winter Biome Temperature Decrease";
        BOName[34] = "35.[Serene Seasons] Late Autumn Biome Temperature Decrease";
        BOName[35] = "36.Dawn Biome Temperature Rate";
        BOName[36] = "37.Day Biome Temperature Rate";
        BOName[37] = "38.Dusk Biome Temperature Rate";
        BOName[38] = "39.Night Biome Temperature Rate";
        BOName[39] = "40.[HBM] Hard Biome Temperature Rate";
        BOName[40] = "41.Ambient Temperature Decrease Water";
        BOName[41] = "42.Drop Speed Water";
        BOName[42] = "43.Drop Speed Rain";
        BOName[43] = "44.Drop Speed Thunder";
        BOName[44] = "45.[Serene Seasons] Water Quality Spring";
        BOName[45] = "46.[Serene Seasons] Water Quality Summer";
        BOName[46] = "47.[Serene Seasons] Water Quality Autumn";
        BOName[47] = "48.[Serene Seasons] Water Quality Winter";
    }
}
