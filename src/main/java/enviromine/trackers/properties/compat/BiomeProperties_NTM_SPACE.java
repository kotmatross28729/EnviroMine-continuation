package enviromine.trackers.properties.compat;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;

import com.hbm.dim.Ike.BiomeGenIke;
import com.hbm.dim.dres.biome.BiomeGenBaseDres;
import com.hbm.dim.duna.biome.BiomeGenBaseDuna;
import com.hbm.dim.duna.biome.BiomeGenDunaPolar;
import com.hbm.dim.duna.biome.BiomeGenDunaPolarHills;
import com.hbm.dim.eve.biome.BiomeGenBaseEve;
import com.hbm.dim.laythe.biome.BiomeGenBaseLaythe;
import com.hbm.dim.laythe.biome.BiomeGenLaythePolar;
import com.hbm.dim.minmus.biome.BiomeGenBaseMinmus;
import com.hbm.dim.moho.biome.BiomeGenBaseMoho;
import com.hbm.dim.moon.BiomeGenMoon;
import com.hbm.dim.orbit.BiomeGenOrbit;

public class BiomeProperties_NTM_SPACE {

    public static void registerNTMSpaceBiomes(Configuration config, BiomeGenBase biome, String[] BOName,
        String biomeWater, double biomeTemp, double DAWN_TEMPERATURE, double DAY_TEMPERATURE, double DUSK_TEMPERATURE,
        double NIGHT_TEMPERATURE, double ambientTemp_TERRAFORMED, double DAWN_TEMPERATURE_TERRAFORMED,
        double DAY_TEMPERATURE_TERRAFORMED, double DUSK_TEMPERATURE_TERRAFORMED, double NIGHT_TEMPERATURE_TERRAFORMED,
        double tempRate_DAWN, double tempRate_DAY, double tempRate_DUSK, double tempRate_NIGHT, boolean tempRate_HARD) {
        if (biome instanceof BiomeGenBaseMoho) { // Mercury ✅
            DAWN_TEMPERATURE = 610D; // -180℃
            DAY_TEMPERATURE = 0D; // 430℃
            DUSK_TEMPERATURE = 10D; // 420℃
            NIGHT_TEMPERATURE = 620D; // -190℃
            biomeTemp = 430D;

            DAWN_TEMPERATURE_TERRAFORMED = -4D; // 24℃
            DAY_TEMPERATURE_TERRAFORMED = -15D; // 35℃
            DUSK_TEMPERATURE_TERRAFORMED = -5D; // 25℃
            NIGHT_TEMPERATURE_TERRAFORMED = 5D; // 15℃
            ambientTemp_TERRAFORMED = 20D;

            tempRate_DAWN = -0.07783D; // -4.67℃/m
            tempRate_DAY = 0.2126D; // 12.76℃/m
            tempRate_DUSK = 0.21116D; // 12.67℃/m
            tempRate_NIGHT = -0.0793D; // -4.76℃/m
            tempRate_HARD = true;

            biomeWater = "DIRTY_WARM";
        } else if (biome instanceof BiomeGenBaseEve) { // Venus ✅
            DAWN_TEMPERATURE = 1D; // 466℃
            DAY_TEMPERATURE = 0D; // 467℃
            DUSK_TEMPERATURE = 2D; // 465℃
            NIGHT_TEMPERATURE = 3D; // 464℃
            biomeTemp = 467;

            DAWN_TEMPERATURE_TERRAFORMED = -3D; // 23℃
            DAY_TEMPERATURE_TERRAFORMED = -12D; // 32℃
            DUSK_TEMPERATURE_TERRAFORMED = -2D; // 22℃
            NIGHT_TEMPERATURE_TERRAFORMED = 8D; // 12℃
            ambientTemp_TERRAFORMED = 20D;

            tempRate_DAWN = 0.262D; // 15.72℃/m
            tempRate_DAY = 0.2626D; // 15.76℃/m
            tempRate_DUSK = 0.2616D; // 15.7℃/m
            tempRate_NIGHT = 0.26083D; // 15.65℃/m
            tempRate_HARD = true;

            biomeWater = "HOT";
        } else if (biome instanceof BiomeGenMoon) { // Moon ✅
            DAWN_TEMPERATURE = 177D; // -50℃
            DAY_TEMPERATURE = 0D; // 127℃
            DUSK_TEMPERATURE = 177D; // -50℃
            NIGHT_TEMPERATURE = 300D; // -173℃
            biomeTemp = 127D;

            DAWN_TEMPERATURE_TERRAFORMED = 3D; // 17℃
            DAY_TEMPERATURE_TERRAFORMED = -3D; // 23℃
            DUSK_TEMPERATURE_TERRAFORMED = 2D; // 18℃
            NIGHT_TEMPERATURE_TERRAFORMED = 6D; // 14℃
            ambientTemp_TERRAFORMED = 20D;

            biomeWater = "CLEAN_COLD";
        } else if (biome instanceof BiomeGenBaseMinmus) { // ? ✅
            DAWN_TEMPERATURE = 64D; // -50℃
            DAY_TEMPERATURE = 0D; // 14℃
            DUSK_TEMPERATURE = 64D; // -50℃
            NIGHT_TEMPERATURE = 121D; // -107℃
            biomeTemp = 14D;

            DAWN_TEMPERATURE_TERRAFORMED = 6D; // 14℃
            DAY_TEMPERATURE_TERRAFORMED = 0D; // 20℃
            DUSK_TEMPERATURE_TERRAFORMED = 5D; // 15℃
            NIGHT_TEMPERATURE_TERRAFORMED = 14D; // 6℃
            ambientTemp_TERRAFORMED = 20D;

            biomeWater = "FROSTY";
        } else if (biome instanceof BiomeGenBaseDuna) { // Mars ✅
            if (biome instanceof BiomeGenDunaPolar || biome instanceof BiomeGenDunaPolarHills) {
                DAWN_TEMPERATURE = 43D; // -93℃
                DAY_TEMPERATURE = 0D; // -50℃
                DUSK_TEMPERATURE = 43D; // -93℃
                NIGHT_TEMPERATURE = 100D; // -150℃
                biomeTemp = -50D;

                DAWN_TEMPERATURE_TERRAFORMED = 0D; // -15℃
                DAY_TEMPERATURE_TERRAFORMED = -7D; // -8℃
                DUSK_TEMPERATURE_TERRAFORMED = 0D; // -15℃
                NIGHT_TEMPERATURE_TERRAFORMED = 8D; // -23℃
                ambientTemp_TERRAFORMED = -15D;

                tempRate_DAWN = -0.043D; // -2.6℃/m
                tempRate_DAY = -0.02D; // -1.2℃/m
                tempRate_DUSK = -0.043D; // -2.6℃/m
                tempRate_NIGHT = -0.0616D; // -3.7℃/m

                biomeWater = "FROSTY";

                tempRate_HARD = true;
            } else {
                DAWN_TEMPERATURE = 69D; // -49℃
                DAY_TEMPERATURE = 0D; // 20℃
                DUSK_TEMPERATURE = 69D; // -49℃
                NIGHT_TEMPERATURE = 160D; // -140℃
                biomeTemp = 20D;

                DAWN_TEMPERATURE_TERRAFORMED = 0D; // 20℃
                DAY_TEMPERATURE_TERRAFORMED = -7D; // 27℃
                DUSK_TEMPERATURE_TERRAFORMED = 0D; // 20℃
                NIGHT_TEMPERATURE_TERRAFORMED = 13D; // 7℃
                ambientTemp_TERRAFORMED = 20D;

                biomeWater = "CLEAN_COLD";
            }
        } else if (biome instanceof BiomeGenIke) { // Phobos? (Pluto-Charon) ✅
            DAWN_TEMPERATURE = 54D; // -58℃
            DAY_TEMPERATURE = 0D; // -4℃
            DUSK_TEMPERATURE = 54D; // -58℃
            NIGHT_TEMPERATURE = 108D; // -112℃
            biomeTemp = -4D;

            DAWN_TEMPERATURE_TERRAFORMED = 6D; // -6℃
            DAY_TEMPERATURE_TERRAFORMED = -13D; // 13℃
            DUSK_TEMPERATURE_TERRAFORMED = 7D; // -7℃
            NIGHT_TEMPERATURE_TERRAFORMED = 14D; // -14℃
            ambientTemp_TERRAFORMED = 0D;
        } else if (biome instanceof BiomeGenBaseDres) { // Ceres ✅
            DAWN_TEMPERATURE = 106D; // -106℃
            DAY_TEMPERATURE = 38D; // -38℃
            DUSK_TEMPERATURE = 106D; // -106℃
            NIGHT_TEMPERATURE = 163D; // -163℃
            biomeTemp = 0D;

            DAWN_TEMPERATURE_TERRAFORMED = 31D; // -31℃
            DAY_TEMPERATURE_TERRAFORMED = 17D; // -17℃
            DUSK_TEMPERATURE_TERRAFORMED = 31D; // -31℃
            NIGHT_TEMPERATURE_TERRAFORMED = 52D; // -52℃
            ambientTemp_TERRAFORMED = 0D;

            tempRate_DAWN = -0.046D; // -2.8℃/m
            tempRate_DAY = -0.016D; // -1.0℃/m
            tempRate_DUSK = -0.046D; // -2.8℃/m
            tempRate_NIGHT = -0.066D; // -4.0℃/m
            tempRate_HARD = true;
        } else if (biome instanceof BiomeGenBaseLaythe) { // ?, values from KSP wiki ✅
            if (biome instanceof BiomeGenLaythePolar) {
                DAWN_TEMPERATURE = 25D; // -25℃
                DAY_TEMPERATURE = 24D; // -24℃
                DUSK_TEMPERATURE = 25D; // -25℃
                NIGHT_TEMPERATURE = 26D; // -26℃
                biomeTemp = 0D;

                DAWN_TEMPERATURE_TERRAFORMED = 25D; // -25℃
                DAY_TEMPERATURE_TERRAFORMED = 24D; // -24℃
                DUSK_TEMPERATURE_TERRAFORMED = 25D; // -25℃
                NIGHT_TEMPERATURE_TERRAFORMED = 26D; // -26℃
                ambientTemp_TERRAFORMED = 0D;

                biomeWater = "FROSTY";
            } else {
                DAWN_TEMPERATURE = 8D; // 12℃
                DAY_TEMPERATURE = 5D; // 15℃
                DUSK_TEMPERATURE = 8D; // 12℃
                NIGHT_TEMPERATURE = 11D; // 9℃
                biomeTemp = 20D;

                DAWN_TEMPERATURE_TERRAFORMED = 8D; // 12℃
                DAY_TEMPERATURE_TERRAFORMED = 5D; // 15℃
                DUSK_TEMPERATURE_TERRAFORMED = 8D; // 12℃
                NIGHT_TEMPERATURE_TERRAFORMED = 11D; // 9℃
                ambientTemp_TERRAFORMED = 28D;

                biomeWater = "CLEAN";
            }
        } else if (biome instanceof BiomeGenOrbit) { // Space ✅
            DAWN_TEMPERATURE = 50D; // -150℃
            DAY_TEMPERATURE = 0D; // -100℃
            DUSK_TEMPERATURE = 50D; // -150℃
            NIGHT_TEMPERATURE = 100D; // -200℃
            biomeTemp = -100D;

            // Literally impossible
            DAWN_TEMPERATURE_TERRAFORMED = 50D; // -150℃
            DAY_TEMPERATURE_TERRAFORMED = 0D; // -100℃
            DUSK_TEMPERATURE_TERRAFORMED = 50D; // -150℃
            NIGHT_TEMPERATURE_TERRAFORMED = 100D; // -200℃
            ambientTemp_TERRAFORMED = -100D;

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

        config.get(catName, BOName[9], DAWN_TEMPERATURE)
            .getDouble(DAWN_TEMPERATURE);
        config.get(catName, BOName[10], DAY_TEMPERATURE)
            .getDouble(DAY_TEMPERATURE);
        config.get(catName, BOName[11], DUSK_TEMPERATURE)
            .getDouble(DUSK_TEMPERATURE);
        config.get(catName, BOName[12], NIGHT_TEMPERATURE)
            .getDouble(NIGHT_TEMPERATURE);

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

    }
}
