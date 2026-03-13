package enviromine.trackers.properties.compat;

import static enviromine.utils.WaterUtils.coolDown;
import static enviromine.utils.WaterUtils.forcePollute;
import static enviromine.utils.WaterUtils.forceSaltDown;
import static enviromine.utils.WaterUtils.getStringFromType;
import static enviromine.utils.WaterUtils.getTypeFromString;
import static enviromine.utils.WaterUtils.heatUp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import enviromine.core.EM_ConfigHandler;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.utils.EnviroUtils;
import enviromine.utils.ModIdentification;
import lotr.common.LOTRDimension;
import lotr.common.world.biome.LOTRBiome;

public class BiomeProperties_LOTR {

    public static void handleLOTRStuff(String[] BOName) {
        LOTRBiome[] middleEarthBL = LOTRDimension.MIDDLE_EARTH.biomeList;
        LOTRBiome[] utumnoBL = LOTRDimension.UTUMNO.biomeList;
        GenDefaultsPropertyLOTR(middleEarthBL, BOName);
        GenDefaultsPropertyLOTR(utumnoBL, BOName);
    }

    public static void GenDefaultsPropertyLOTR(LOTRBiome[] LOTRBiomeArray, String[] BOName) {
        for (LOTRBiome LOTRBiome : LOTRBiomeArray) {
            if (LOTRBiome == null) {
                continue;
            }
            String modID = ModIdentification.idFromObject(LOTRBiome);
            File file = new File(
                EM_ConfigHandler.loadedProfile + EM_ConfigHandler.customPath
                    + EnviroUtils.SafeFilename(modID)
                    + ".cfg");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    if (EM_Settings.loggerVerbosity >= EM_ConfigHandler.EnumLogVerbosity.LOW.getLevel())
                        EnviroMine.logger
                            .log(Level.ERROR, "Failed to create file for biome '" + LOTRBiome.biomeName + "'", e);
                    continue;
                }
            }
            Configuration config = new Configuration(file, true);
            config.load();
            generateEmptyLOTR(config, LOTRBiome, BOName);
            config.save();
        }
    }

    public static void generateEmptyLOTR(Configuration config, Object obj, String[] BOName) {
        if (obj == null || !(obj instanceof BiomeGenBase biome)) // DONT TOUCH
        {
            if (EM_Settings.loggerVerbosity >= EM_ConfigHandler.EnumLogVerbosity.NORMAL.getLevel())
                EnviroMine.logger.log(Level.ERROR, "Tried to register config with non biome object!", new Exception());
            return;
        }

        ArrayList<BiomeDictionary.Type> typeList = new ArrayList<BiomeDictionary.Type>();
        BiomeDictionary.Type[] typeArray = BiomeDictionary.getTypesForBiome(biome);
        Collections.addAll(typeList, typeArray);

        double air = typeList.contains(BiomeDictionary.Type.NETHER) ? -0.1D : 0D;
        double sanity = typeList.contains(BiomeDictionary.Type.NETHER) ? -0.1D : 0D;
        double water = typeList.contains(BiomeDictionary.Type.NETHER) || typeList.contains(BiomeDictionary.Type.DRY)
            ? 0.05D
            : 0D;
        double temp = typeList.contains(BiomeDictionary.Type.NETHER) || typeList.contains(BiomeDictionary.Type.DRY)
            ? 0.005D
            : 0D;
        double TemperatureMultiplier = 1D;

        double TemperatureRainDecrease = typeList.contains(BiomeDictionary.Type.WATER) ? 8D
            : typeList.contains(BiomeDictionary.Type.JUNGLE) ? 2D : 6D;
        double TemperatureThunderDecrease = typeList.contains(BiomeDictionary.Type.WATER) ? 10D
            : typeList.contains(BiomeDictionary.Type.JUNGLE) ? 4D : 8D;

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

        double EARLY_SPRING_TEMPERATURE_DECREASE = (typeList.contains(BiomeDictionary.Type.HOT)
            && typeList.contains(BiomeDictionary.Type.SANDY)) ? -3.0 : // DESERT (-8 (DEFAULT+8))
                typeList.contains(BiomeDictionary.Type.JUNGLE) ? 0.0 : // JUNGLE (-5 (DEFAULT+5))
                    typeList.contains(BiomeDictionary.Type.HOT) ? 3.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2C))
                        typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 7.0 : // TAIGA (+2 (DEFAULT-2C))
                            5.0; // DEFAULT (0)

        double MID_SPRING_TEMPERATURE_DECREASE = (typeList.contains(BiomeDictionary.Type.HOT)
            && typeList.contains(BiomeDictionary.Type.SANDY)) ? -10.0 : // DESERT (-8 (DEFAULT+8))
                typeList.contains(BiomeDictionary.Type.JUNGLE) ? -7.0 : // JUNGLE (-5 (DEFAULT+5))
                    typeList.contains(BiomeDictionary.Type.HOT) ? -4.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                        typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 0.0 : // TAIGA (+2 (DEFAULT-2))
                            -2.0; // DEFAULT (0)

        double LATE_SPRING_TEMPERATURE_DECREASE = (typeList.contains(BiomeDictionary.Type.HOT)
            && typeList.contains(BiomeDictionary.Type.SANDY)) ? -9.0 : // DESERT (-8 (DEFAULT+8))
                typeList.contains(BiomeDictionary.Type.JUNGLE) ? -6.0 : // JUNGLE (-5 (DEFAULT+5))
                    typeList.contains(BiomeDictionary.Type.HOT) ? -3.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                        typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 1.0 : // TAIGA (+2 (DEFAULT-2))
                            -1.0; // DEFAULT (0)

        double EARLY_SUMMER_TEMPERATURE_DECREASE = (typeList.contains(BiomeDictionary.Type.HOT)
            && typeList.contains(BiomeDictionary.Type.SANDY)) ? -9.0 : // DESERT (-8 (DEFAULT+8))
                typeList.contains(BiomeDictionary.Type.JUNGLE) ? -6.0 : // JUNGLE (-5 (DEFAULT+5))
                    typeList.contains(BiomeDictionary.Type.HOT) ? -3.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                        typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 1.0 : // TAIGA (+2 (DEFAULT-2))
                            -1.0; // DEFAULT (0)

        double MID_SUMMER_TEMPERATURE_DECREASE = (typeList.contains(BiomeDictionary.Type.HOT)
            && typeList.contains(BiomeDictionary.Type.SANDY)) ? -11.0 : // DESERT (-8 (DEFAULT+8))
                typeList.contains(BiomeDictionary.Type.JUNGLE) ? -8.0 : // JUNGLE (-5 (DEFAULT+5))
                    typeList.contains(BiomeDictionary.Type.HOT) ? -5.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                        typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? -1.0 : // TAIGA (+2 (DEFAULT-2))
                            -3.0; // DEFAULT (0)

        double LATE_SUMMER_TEMPERATURE_DECREASE = (typeList.contains(BiomeDictionary.Type.HOT)
            && typeList.contains(BiomeDictionary.Type.SANDY)) ? -9.0 : // DESERT (-8 (DEFAULT+8))
                typeList.contains(BiomeDictionary.Type.JUNGLE) ? -6.0 : // JUNGLE (-5 (DEFAULT+5))
                    typeList.contains(BiomeDictionary.Type.HOT) ? -3.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                        typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 1.0 : // TAIGA (+2 (DEFAULT-2))
                            -1.0; // DEFAULT (0)

        double EARLY_AUTUMN_TEMPERATURE_DECREASE = (typeList.contains(BiomeDictionary.Type.HOT)
            && typeList.contains(BiomeDictionary.Type.SANDY)) ? -2.0 : // DESERT (-8 (DEFAULT+8))
                typeList.contains(BiomeDictionary.Type.JUNGLE) ? 1.0 : // JUNGLE (-5 (DEFAULT+5))
                    typeList.contains(BiomeDictionary.Type.HOT) ? 4.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                        typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 8.0 : // TAIGA (+2 (DEFAULT-2))
                            6.0; // DEFAULT (0)

        double MID_AUTUMN_TEMPERATURE_DECREASE = (typeList.contains(BiomeDictionary.Type.HOT)
            && typeList.contains(BiomeDictionary.Type.SANDY)) ? 0.0 : // DESERT
        // (-8
        // (DEFAULT+8))
                typeList.contains(BiomeDictionary.Type.JUNGLE) ? 3.0 : // JUNGLE (-5 (DEFAULT+5))
                    typeList.contains(BiomeDictionary.Type.HOT) ? 6.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                        typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 10.0 : // TAIGA (+2 (DEFAULT-2))
                            8.0; // DEFAULT (0)

        double LATE_AUTUMN_TEMPERATURE_DECREASE = (typeList.contains(BiomeDictionary.Type.HOT)
            && typeList.contains(BiomeDictionary.Type.SANDY)) ? -2.0 : // DESERT (-8 (DEFAULT+8))
                typeList.contains(BiomeDictionary.Type.JUNGLE) ? 5.0 : // JUNGLE (-5 (DEFAULT+5))
                    typeList.contains(BiomeDictionary.Type.HOT) ? 8.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                        typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 12.0 : // TAIGA (+2 (DEFAULT-2))
                            10.0; // DEFAULT (0)

        double EARLY_WINTER_TEMPERATURE_DECREASE = (typeList.contains(BiomeDictionary.Type.HOT)
            && typeList.contains(BiomeDictionary.Type.SANDY)) ? 4.0 : // DESERT (-8 (DEFAULT+8))
                typeList.contains(BiomeDictionary.Type.JUNGLE) ? 7.0 : // JUNGLE (-5 (DEFAULT+5))
                    typeList.contains(BiomeDictionary.Type.HOT) ? 10.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                        typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 14.0 : // TAIGA (+2 (DEFAULT-2))
                            12.0; // DEFAULT (0)

        double MID_WINTER_TEMPERATURE_DECREASE = (typeList.contains(BiomeDictionary.Type.HOT)
            && typeList.contains(BiomeDictionary.Type.SANDY)) ? 8.0 : // DESERT
        // (-8
        // (DEFAULT+8))
                typeList.contains(BiomeDictionary.Type.JUNGLE) ? 11.0 : // JUNGLE (-5 (DEFAULT+5))
                    typeList.contains(BiomeDictionary.Type.HOT) ? 14.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2)))
                        typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 18.0 : // TAIGA (+2 (DEFAULT-2))
                            16.0; // DEFAULT (0)

        double LATE_WINTER_TEMPERATURE_DECREASE = (typeList.contains(BiomeDictionary.Type.HOT)
            && typeList.contains(BiomeDictionary.Type.SANDY)) ? 2.0 : // DESERT
        // (-8
        // (DEFAULT+8))
                typeList.contains(BiomeDictionary.Type.JUNGLE) ? 5.0 : // JUNGLE (-5 (DEFAULT+5))
                    typeList.contains(BiomeDictionary.Type.HOT) ? 8.0 : // ELSE HOT (SAVANNA) (-2 (DEFAULT+2))
                        typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 12.0 : // TAIGA (+2 (DEFAULT-2))
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

}
