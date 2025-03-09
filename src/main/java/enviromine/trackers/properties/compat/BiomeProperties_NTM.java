package enviromine.trackers.properties.compat;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;

import com.hbm.world.biome.BiomeGenCraterBase;

public class BiomeProperties_NTM {

    public static void registerNTMBiomes(Configuration config, BiomeGenBase biome, String[] BOName, String biomeWater,
        double biomeTemp, double temp, double sanity, double water, double air, double TemperatureMultiplier,
        double DAWN_TEMPERATURE, double DAY_TEMPERATURE, double DUSK_TEMPERATURE, double NIGHT_TEMPERATURE,
        double TemperatureRainDecrease, double TemperatureThunderDecrease, boolean TemperatureRainBool,
        boolean TemperatureThunderBool, double TemperatureShadeDecrease, double ambientTemp_TERRAFORMED,
        double DAWN_TEMPERATURE_TERRAFORMED, double DAY_TEMPERATURE_TERRAFORMED, double DUSK_TEMPERATURE_TERRAFORMED,
        double NIGHT_TEMPERATURE_TERRAFORMED, double EARLY_SPRING_TEMPERATURE_DECREASE,
        double EARLY_SUMMER_TEMPERATURE_DECREASE, double EARLY_WINTER_TEMPERATURE_DECREASE,
        double EARLY_AUTUMN_TEMPERATURE_DECREASE, double MID_SPRING_TEMPERATURE_DECREASE,
        double MID_SUMMER_TEMPERATURE_DECREASE, double MID_WINTER_TEMPERATURE_DECREASE,
        double MID_AUTUMN_TEMPERATURE_DECREASE, double LATE_SPRING_TEMPERATURE_DECREASE,
        double LATE_SUMMER_TEMPERATURE_DECREASE, double LATE_WINTER_TEMPERATURE_DECREASE,
        double LATE_AUTUMN_TEMPERATURE_DECREASE, double tempRate_DAWN, double tempRate_DAY, double tempRate_DUSK,
        double tempRate_NIGHT, boolean tempRate_HARD, double TemperatureWaterDecrease, double dropSpeedWater,
        double dropSpeedRain, double dropSpeedThunder) {
        if (biome instanceof BiomeGenCraterBase.BiomeGenCraterInner) {
            DAWN_TEMPERATURE = 3D;
            DAY_TEMPERATURE = 0D;
            DUSK_TEMPERATURE = 3D;
            NIGHT_TEMPERATURE = 6D;
            biomeTemp = 80D;
            ambientTemp_TERRAFORMED = 80D;

            biomeWater = "RADIOACTIVE_HOT";
        } else if (biome instanceof BiomeGenCraterBase.BiomeGenCrater) {
            biomeTemp = 50D;
            ambientTemp_TERRAFORMED = 50D;

            biomeWater = "RADIOACTIVE_WARM";
        } else if (biome instanceof BiomeGenCraterBase.BiomeGenCraterOuter) {
            biomeTemp = 35D;
            ambientTemp_TERRAFORMED = 35D;

            biomeWater = "RADIOACTIVE";
        } else {
            return;
        }

        String catName = "biomes" + "." + biome.biomeName;

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
            .getDouble(biomeTemp);
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

        config.get(catName, BOName[9], DAWN_TEMPERATURE)
            .getDouble(DAWN_TEMPERATURE);
        config.get(catName, BOName[10], DAY_TEMPERATURE)
            .getDouble(DAY_TEMPERATURE);
        config.get(catName, BOName[11], DUSK_TEMPERATURE)
            .getDouble(DUSK_TEMPERATURE);
        config.get(catName, BOName[12], NIGHT_TEMPERATURE)
            .getDouble(NIGHT_TEMPERATURE);

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
        config.get(catName, BOName[19], DAWN_TEMPERATURE_TERRAFORMED)
            .getDouble(DAWN_TEMPERATURE_TERRAFORMED);
        config.get(catName, BOName[20], DAY_TEMPERATURE_TERRAFORMED)
            .getDouble(DAY_TEMPERATURE_TERRAFORMED);
        config.get(catName, BOName[21], DUSK_TEMPERATURE_TERRAFORMED)
            .getDouble(DUSK_TEMPERATURE_TERRAFORMED);
        config.get(catName, BOName[22], NIGHT_TEMPERATURE_TERRAFORMED)
            .getDouble(NIGHT_TEMPERATURE_TERRAFORMED);

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
    }
}
