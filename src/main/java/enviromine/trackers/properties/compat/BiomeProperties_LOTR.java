package enviromine.trackers.properties.compat;

import enviromine.core.EM_ConfigHandler;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.utils.EnviroUtils;
import enviromine.utils.ModIdentification;
import lotr.common.LOTRDimension;
import lotr.common.world.biome.LOTRBiome;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class BiomeProperties_LOTR {
	public static void handleLOTRStuff(String[] BOName){
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
			File file = new File(EM_ConfigHandler.loadedProfile + EM_ConfigHandler.customPath + EnviroUtils.SafeFilename(modID) + ".cfg");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (Exception e) {
					if (EM_Settings.loggerVerbosity >= EM_ConfigHandler.EnumLogVerbosity.LOW.getLevel())
						EnviroMine.logger.log(Level.ERROR, "Failed to create file for biome '" + LOTRBiome.biomeName + "'", e);
					continue;
				}
			}
			Configuration config = new Configuration(file, true);
			config.load();
			generateEmptyLOTR(config, LOTRBiome, BOName);
			config.save();
		}
	}
	
	
	public static void generateEmptyLOTR(Configuration config, Object obj, String[] BOName)
	{
		if(obj == null || !(obj instanceof BiomeGenBase biome)) //DONT TOUCH
		{
			if (EM_Settings.loggerVerbosity >= EM_ConfigHandler.EnumLogVerbosity.NORMAL.getLevel()) EnviroMine.logger.log(Level.ERROR, "Tried to register config with non biome object!", new Exception());
			return;
		}
		
		ArrayList<BiomeDictionary.Type> typeList = new ArrayList<BiomeDictionary.Type>();
		BiomeDictionary.Type[] typeArray = BiomeDictionary.getTypesForBiome(biome);
		Collections.addAll(typeList, typeArray);
		
		double air = typeList.contains(BiomeDictionary.Type.NETHER)? -0.1D : 0D;
		double sanity = typeList.contains(BiomeDictionary.Type.NETHER)? -0.1D : 0D;
		double water = typeList.contains(BiomeDictionary.Type.NETHER) || typeList.contains(BiomeDictionary.Type.DRY)? 0.05D : 0D;
		double temp = typeList.contains(BiomeDictionary.Type.NETHER) || typeList.contains(BiomeDictionary.Type.DRY)? 0.005D : 0D;
		boolean isDesertBiome = typeList.contains(BiomeDictionary.Type.DESERT) || typeList.contains(BiomeDictionary.Type.DRY);
		double DesertBiomeTemperatureMultiplier = typeList.contains(BiomeDictionary.Type.DESERT) || typeList.contains(BiomeDictionary.Type.DRY)? 3D : 1D;
		
		double TemperatureRainDecrease = typeList.contains(BiomeDictionary.Type.WATER) ? 8D : typeList.contains(BiomeDictionary.Type.JUNGLE)? 2D : 6D;
		double TemperatureThunderDecrease = typeList.contains(BiomeDictionary.Type.WATER) ? 10D : typeList.contains(BiomeDictionary.Type.JUNGLE)? 4D : 8D;
		boolean TemperatureRainBool = true;
		boolean TemperatureThunderBool = true;
		
		double TemperatureShadeDecrease = /*typeList.contains(Type.WATER) ? 12D : typeList.contains(Type.JUNGLE)? 10D :*/ 2.5D;
		
		String biomeWater = EnviroUtils.getBiomeWater(biome);
		
		double biomeTemp;
		double DAWN_TEMPERATURE;
		double DAY_TEMPERATURE;
		double DUSK_TEMPERATURE;
		double NIGHT_TEMPERATURE;
		
		double ambientTemp_TERRAFORMED;
		double DAWN_TEMPERATURE_TERRAFORMED;
		double DAY_TEMPERATURE_TERRAFORMED;
		double DUSK_TEMPERATURE_TERRAFORMED;
		double NIGHT_TEMPERATURE_TERRAFORMED;
		
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
				(typeList.contains(BiomeDictionary.Type.HOT) && typeList.contains(BiomeDictionary.Type.SANDY)) ? -3.0 : //DESERT              (-8 (DEFAULT+8))
						typeList.contains(BiomeDictionary.Type.JUNGLE) ? 0.0 :                                  //JUNGLE              (-5 (DEFAULT+5))
								typeList.contains(BiomeDictionary.Type.HOT) ? 3.0 :                                     //ELSE HOT (SAVANNA)  (-2 (DEFAULT+2C))
										typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 7.0 :                              //TAIGA               (+2 (DEFAULT-2C))
												5.0;                                                                    //DEFAULT             (0)
		
		double MID_SPRING_TEMPERATURE_DECREASE =
				(typeList.contains(BiomeDictionary.Type.HOT) && typeList.contains(BiomeDictionary.Type.SANDY)) ? -10.0 : // DESERT              (-8 (DEFAULT+8))
						typeList.contains(BiomeDictionary.Type.JUNGLE) ? -7.0 :                              // JUNGLE              (-5 (DEFAULT+5))
								typeList.contains(BiomeDictionary.Type.HOT) ? -4.0 :                         // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
										typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 0.0 :               // TAIGA               (+2 (DEFAULT-2))
												-2.0;                                                    // DEFAULT             (0)
		
		double LATE_SPRING_TEMPERATURE_DECREASE =
				(typeList.contains(BiomeDictionary.Type.HOT) && typeList.contains(BiomeDictionary.Type.SANDY)) ? -9.0 : // DESERT              (-8 (DEFAULT+8))
						typeList.contains(BiomeDictionary.Type.JUNGLE) ? -6.0 :                             // JUNGLE              (-5 (DEFAULT+5))
								typeList.contains(BiomeDictionary.Type.HOT) ? -3.0 :                        // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
										typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 1.0 :              // TAIGA               (+2 (DEFAULT-2))
												-1.0;                                                   // DEFAULT             (0)
		
		double EARLY_SUMMER_TEMPERATURE_DECREASE =
				(typeList.contains(BiomeDictionary.Type.HOT) && typeList.contains(BiomeDictionary.Type.SANDY)) ? -9.0 : // DESERT              (-8 (DEFAULT+8))
						typeList.contains(BiomeDictionary.Type.JUNGLE) ? -6.0 :                             // JUNGLE              (-5 (DEFAULT+5))
								typeList.contains(BiomeDictionary.Type.HOT) ? -3.0 :                        // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
										typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 1.0 :              // TAIGA               (+2 (DEFAULT-2))
												-1.0;                                                   // DEFAULT             (0)
		
		double MID_SUMMER_TEMPERATURE_DECREASE =
				(typeList.contains(BiomeDictionary.Type.HOT) && typeList.contains(BiomeDictionary.Type.SANDY)) ? -11.0 : // DESERT              (-8 (DEFAULT+8))
						typeList.contains(BiomeDictionary.Type.JUNGLE) ? -8.0 :                             // JUNGLE              (-5 (DEFAULT+5))
								typeList.contains(BiomeDictionary.Type.HOT) ? -5.0 :                        // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
										typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? -1.0 :             // TAIGA               (+2 (DEFAULT-2))
												-3.0;                                                   // DEFAULT             (0)
		
		double LATE_SUMMER_TEMPERATURE_DECREASE =
				(typeList.contains(BiomeDictionary.Type.HOT) && typeList.contains(BiomeDictionary.Type.SANDY)) ? -9.0 : // DESERT              (-8 (DEFAULT+8))
						typeList.contains(BiomeDictionary.Type.JUNGLE) ? -6.0 :                             // JUNGLE              (-5 (DEFAULT+5))
								typeList.contains(BiomeDictionary.Type.HOT) ? -3.0 :                        // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
										typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 1.0 :              // TAIGA               (+2 (DEFAULT-2))
												-1.0;                                                   // DEFAULT             (0)
		
		double EARLY_AUTUMN_TEMPERATURE_DECREASE =
				(typeList.contains(BiomeDictionary.Type.HOT) && typeList.contains(BiomeDictionary.Type.SANDY)) ? -2.0 : // DESERT              (-8 (DEFAULT+8))
						typeList.contains(BiomeDictionary.Type.JUNGLE) ? 1.0 :                              // JUNGLE              (-5 (DEFAULT+5))
								typeList.contains(BiomeDictionary.Type.HOT) ? 4.0 :                         // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
										typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 8.0 :              // TAIGA               (+2 (DEFAULT-2))
												6.0;                                                    // DEFAULT             (0)
		
		double MID_AUTUMN_TEMPERATURE_DECREASE =
				(typeList.contains(BiomeDictionary.Type.HOT) && typeList.contains(BiomeDictionary.Type.SANDY)) ? 0.0 :  // DESERT              (-8 (DEFAULT+8))
						typeList.contains(BiomeDictionary.Type.JUNGLE) ? 3.0 :                              // JUNGLE              (-5 (DEFAULT+5))
								typeList.contains(BiomeDictionary.Type.HOT) ? 6.0 :                         // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
										typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 10.0 :             // TAIGA               (+2 (DEFAULT-2))
												8.0;                                                    // DEFAULT             (0)
		
		double LATE_AUTUMN_TEMPERATURE_DECREASE =
				(typeList.contains(BiomeDictionary.Type.HOT) && typeList.contains(BiomeDictionary.Type.SANDY)) ? -2.0 : // DESERT              (-8 (DEFAULT+8))
						typeList.contains(BiomeDictionary.Type.JUNGLE) ? 5.0 :                             // JUNGLE              (-5 (DEFAULT+5))
								typeList.contains(BiomeDictionary.Type.HOT) ? 8.0 :                         // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
										typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 12.0 :             // TAIGA               (+2 (DEFAULT-2))
												10.0;                                                   // DEFAULT             (0)
		
		double EARLY_WINTER_TEMPERATURE_DECREASE =
				(typeList.contains(BiomeDictionary.Type.HOT) && typeList.contains(BiomeDictionary.Type.SANDY)) ? 4.0 :   // DESERT              (-8 (DEFAULT+8))
						typeList.contains(BiomeDictionary.Type.JUNGLE) ? 7.0 :                               // JUNGLE              (-5 (DEFAULT+5))
								typeList.contains(BiomeDictionary.Type.HOT) ? 10.0 :                         // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
										typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 14.0 :              // TAIGA               (+2 (DEFAULT-2))
												12.0;                                                    // DEFAULT             (0)
		
		double MID_WINTER_TEMPERATURE_DECREASE =
				(typeList.contains(BiomeDictionary.Type.HOT) && typeList.contains(BiomeDictionary.Type.SANDY)) ? 8.0 :  // DESERT              (-8 (DEFAULT+8))
						typeList.contains(BiomeDictionary.Type.JUNGLE) ? 11.0 :                             // JUNGLE              (-5 (DEFAULT+5))
								typeList.contains(BiomeDictionary.Type.HOT) ? 14.0 :                        // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2)))
										typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 18.0 :             // TAIGA               (+2 (DEFAULT-2))
												16.0;                                                   // DEFAULT             (0)
		
		double LATE_WINTER_TEMPERATURE_DECREASE =
				(typeList.contains(BiomeDictionary.Type.HOT) && typeList.contains(BiomeDictionary.Type.SANDY)) ? 2.0 :  // DESERT              (-8 (DEFAULT+8))
						typeList.contains(BiomeDictionary.Type.JUNGLE) ? 5.0 :                              // JUNGLE              (-5 (DEFAULT+5))
								typeList.contains(BiomeDictionary.Type.HOT) ? 8.0 :                         // ELSE HOT (SAVANNA)  (-2 (DEFAULT+2))
										typeList.contains(BiomeDictionary.Type.CONIFEROUS) ? 12.0 :             // TAIGA               (+2 (DEFAULT-2))
												10.0;                                                   // DEFAULT             (0)
		
		DAWN_TEMPERATURE = 4D;
		DAY_TEMPERATURE = 0D;
		DUSK_TEMPERATURE = 4D;
		NIGHT_TEMPERATURE = 8D;
		
		biomeTemp = EnviroUtils.getBiomeTemp(biome);
		
		DAWN_TEMPERATURE_TERRAFORMED =  4D;
		DAY_TEMPERATURE_TERRAFORMED =  0D;
		DUSK_TEMPERATURE_TERRAFORMED = 4D;
		NIGHT_TEMPERATURE_TERRAFORMED = 8D;
		ambientTemp_TERRAFORMED = EnviroUtils.getBiomeTemp(biome);

		
		String catName = "biomes" + "." + biome.biomeName;
		
		config.get(catName, BOName[0], biome.biomeID).getInt(biome.biomeID);
		config.get(catName, BOName[1], true).getBoolean(true);
		config.get(catName, BOName[2], biomeWater, "Water Quality: dirty, salty, cold, dirty cold, frosty, warm, dirty warm, hot, clean").getString();
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
	
	
}
