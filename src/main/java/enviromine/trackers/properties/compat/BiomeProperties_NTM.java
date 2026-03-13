package enviromine.trackers.properties.compat;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;

import com.hbm.world.biome.BiomeGenCraterBase;

public class BiomeProperties_NTM {

    public static void registerNTMBiomes(Configuration config, BiomeGenBase biome, String[] BOName, String biomeWater,
        double biomeTemp, double DAWN_TEMPERATURE_DECREASE, double DAY_TEMPERATURE_DECREASE,
        double DUSK_TEMPERATURE_DECREASE, double NIGHT_TEMPERATURE_DECREASE, double ambientTemp_TERRAFORMED,
        String SPRING_waterQuality, String SUMMER_waterQuality, String AUTUMN_waterQuality,
        String WINTER_waterQuality) {
        if (biome instanceof BiomeGenCraterBase.BiomeGenCraterInner) {
            DAWN_TEMPERATURE_DECREASE = 3D;
            DAY_TEMPERATURE_DECREASE = 0D;
            DUSK_TEMPERATURE_DECREASE = 3D;
            NIGHT_TEMPERATURE_DECREASE = 6D;
            biomeTemp = 80D;
            ambientTemp_TERRAFORMED = 80D;

            biomeWater = "RADIOACTIVE_HOT";
            SPRING_waterQuality = "RADIOACTIVE_HOT";
            SUMMER_waterQuality = "RADIOACTIVE_HOT";
            AUTUMN_waterQuality = "RADIOACTIVE_HOT";
            WINTER_waterQuality = "RADIOACTIVE_WARM";
        } else if (biome instanceof BiomeGenCraterBase.BiomeGenCrater) {
            biomeTemp = 50D;
            ambientTemp_TERRAFORMED = 50D;

            biomeWater = "RADIOACTIVE_WARM";
            SPRING_waterQuality = "RADIOACTIVE_WARM";
            SUMMER_waterQuality = "RADIOACTIVE_HOT";
            AUTUMN_waterQuality = "RADIOACTIVE_WARM";
            WINTER_waterQuality = "RADIOACTIVE_COLD";
        } else if (biome instanceof BiomeGenCraterBase.BiomeGenCraterOuter) {
            biomeTemp = 35D;
            ambientTemp_TERRAFORMED = 35D;

            biomeWater = "RADIOACTIVE";
            SPRING_waterQuality = "RADIOACTIVE";
            SUMMER_waterQuality = "RADIOACTIVE_WARM";
            AUTUMN_waterQuality = "RADIOACTIVE";
            WINTER_waterQuality = "RADIOACTIVE_FROSTY";
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

        config.get(catName, BOName[9], DAWN_TEMPERATURE_DECREASE)
            .getDouble(DAWN_TEMPERATURE_DECREASE);
        config.get(catName, BOName[10], DAY_TEMPERATURE_DECREASE)
            .getDouble(DAY_TEMPERATURE_DECREASE);
        config.get(catName, BOName[11], DUSK_TEMPERATURE_DECREASE)
            .getDouble(DUSK_TEMPERATURE_DECREASE);
        config.get(catName, BOName[12], NIGHT_TEMPERATURE_DECREASE)
            .getDouble(NIGHT_TEMPERATURE_DECREASE);

        config.get(catName, BOName[18], ambientTemp_TERRAFORMED)
            .getDouble(ambientTemp_TERRAFORMED);

        config.get(catName, BOName[44], SPRING_waterQuality, "Water Quality at spring")
            .getString();
        config.get(catName, BOName[45], SUMMER_waterQuality, "Water Quality at summer")
            .getString();
        config.get(catName, BOName[46], AUTUMN_waterQuality, "Water Quality at autumn")
            .getString();
        config.get(catName, BOName[47], WINTER_waterQuality, "Water Quality at winter")
            .getString();
    }
}
