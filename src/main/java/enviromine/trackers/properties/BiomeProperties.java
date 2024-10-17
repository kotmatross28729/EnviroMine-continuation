package enviromine.trackers.properties;

import java.io.File;
import java.util.ArrayList;

import com.hbm.dim.Ike.BiomeGenIke;
import com.hbm.dim.duna.biome.BiomeGenBaseDuna;
import com.hbm.dim.duna.biome.BiomeGenDunaPolar;
import com.hbm.dim.duna.biome.BiomeGenDunaPolarHills;
import com.hbm.dim.eve.biome.BiomeGenBaseEve;
import com.hbm.dim.minmus.biome.BiomeGenBaseMinmus;
import com.hbm.dim.moho.biome.BiomeGenBaseMoho;
import com.hbm.dim.moon.BiomeGenMoon;
import org.apache.logging.log4j.Level;

import enviromine.core.EM_ConfigHandler;
import enviromine.core.EM_ConfigHandler.EnumLogVerbosity;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.trackers.properties.helpers.PropertyBase;
import enviromine.trackers.properties.helpers.SerialisableProperty;
import enviromine.utils.EnviroUtils;
import enviromine.utils.ModIdentification;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.config.Configuration;

import static net.minecraftforge.common.BiomeDictionary.Type.DRY;


public class BiomeProperties implements SerialisableProperty, PropertyBase
{
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

	public BiomeProperties(NBTTagCompound tags)
	{
		this.ReadFromNBT(tags);
	}

	public BiomeProperties()
	{
		// THIS CONSTRUCTOR IS FOR STATIC PURPOSES ONLY!

		if(base != null && base != this)
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
        float TemperatureShadeDecrease
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

        this.DAWN_TEMPERATURE = DAWN_TEMPERATURE;
        this.DAY_TEMPERATURE = DAY_TEMPERATURE;
        this.DUSK_TEMPERATURE = DUSK_TEMPERATURE;
        this.NIGHT_TEMPERATURE = NIGHT_TEMPERATURE;

        this.TemperatureRainDecrease = TemperatureRainDecrease;
        this.TemperatureThunderDecrease = TemperatureThunderDecrease;
        this.TemperatureRainBool = TemperatureRainBool;
        this.TemperatureThunderBool = TemperatureThunderBool;
        this.TemperatureShadeDecrease = TemperatureShadeDecrease;
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

	public int getWaterQualityId()
	{
		if(this.waterQuality.trim().equalsIgnoreCase("dirty"))
		{
			return 1;
		} else if(this.waterQuality.trim().equalsIgnoreCase("salty"))
		{
			return 2;
		} else if(this.waterQuality.trim().equalsIgnoreCase("cold"))
		{
			return 3;
		} else if(this.waterQuality.trim().equalsIgnoreCase("clean"))
		{
			return 0;
		} else
		{
			return -1;
		}
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
		String waterQ = config.get(category, BOName[2], "clean", "Water Quality: dirty, salty, cold, clean").getString();
		float ambTemp = (float)config.get(category, BOName[3], 25.00, "Biome temperature in celsius (Player body temp is offset by + 12C)").getDouble(25.00);
		float tempRate = (float)config.get(category, BOName[4], 0.0).getDouble(0.0);
		float sanRate = (float)config.get(category, BOName[5], 0.0).getDouble(0.0);
		float dehyRate = (float)config.get(category, BOName[6], 0.0).getDouble(0.0);
		float airRate = (float)config.get(category, BOName[7], 0.0).getDouble(0.0);
		String filename = config.getConfigFile().getName();
        boolean isDesertBiome = config.get(category, BOName[8], false).getBoolean(false);
        float DesertBiomeTemperatureMultiplier = (float)config.get(category, BOName[9], 1.0).getDouble(1.0);

        float DAWN_TEMPERATURE = (float)config.get(category, BOName[10], 4.0).getDouble(4.0);
        float DAY_TEMPERATURE = (float)config.get(category, BOName[11], 0.0).getDouble(0.0);
        float DUSK_TEMPERATURE = (float)config.get(category, BOName[12], 4.0).getDouble(4.0);
        float NIGHT_TEMPERATURE = (float)config.get(category, BOName[13], 8.0).getDouble(8.0);

        float TemperatureRainDecrease = (float)config.get(category, BOName[14], 6.0).getDouble(6.0);
        float TemperatureThunderDecrease = (float)config.get(category, BOName[15], 8.0).getDouble(8.0);

        boolean TemperatureRainBool = config.get(category, BOName[16], false).getBoolean(false);
        boolean TemperatureThunderBool = config.get(category, BOName[17], false).getBoolean(false);
        float TemperatureShadeDecrease = (float)config.get(category, BOName[18], 2.5).getDouble(2.5);

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
            TemperatureShadeDecrease
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
		config.get(category, BOName[2], this.waterQuality, "Water Quality: dirty, salty, cold, clean").getString();
		config.get(category, BOName[3], this.ambientTemp, "Biome temperature in celsius (Player body temp is offset by + 12C)").getDouble(this.ambientTemp);
		config.get(category, BOName[4], this.tempRate).getDouble(this.tempRate);
		config.get(category, BOName[5], this.sanityRate).getDouble(this.sanityRate);
		config.get(category, BOName[6], this.dehydrateRate).getDouble(this.dehydrateRate);
		config.get(category, BOName[7], this.airRate).getDouble(this.airRate);
        config.get(category, BOName[8], this.isDesertBiome, "Affects the temperature difference at night / day (useful for deserts)").getBoolean(this.isDesertBiome);
        config.get(category, BOName[9], this.DesertBiomeTemperatureMultiplier, "The temperatureChange will be multiplied by this number if isDesertBiome=true").getDouble(this.DesertBiomeTemperatureMultiplier);

        config.get(category, BOName[10], this.DAWN_TEMPERATURE, "The temperatureChange will be equal to this number at dawn").getDouble(this.DAWN_TEMPERATURE);
        config.get(category, BOName[11], this.DAY_TEMPERATURE, "The temperatureChange will be equal to this number at day").getDouble(this.DAY_TEMPERATURE);
        config.get(category, BOName[12], this.DUSK_TEMPERATURE, "The temperatureChange will be equal to this number at dusk").getDouble(this.DUSK_TEMPERATURE);
        config.get(category, BOName[13], this.NIGHT_TEMPERATURE, "The temperatureChange will be equal to this number at midnight").getDouble(this.NIGHT_TEMPERATURE);
//TODO change
        config.get(category, BOName[14], this.TemperatureRainDecrease, "Biome temperature decreases by n degrees if it rains").getDouble(this.TemperatureRainDecrease);
        config.get(category, BOName[15], this.TemperatureThunderDecrease, "Biome temperature decreases by n degrees if there is a thunderstorm").getDouble(this.TemperatureThunderDecrease);
        config.get(category, BOName[16], this.TemperatureRainBool, "Should the biome temperature decreases if it rains?").getBoolean(this.TemperatureRainBool);
        config.get(category, BOName[17], this.TemperatureThunderBool, "Should the biome temperature decreases if there is a thunderstorm?").getBoolean(this.TemperatureThunderBool);
        config.get(category, BOName[18], this.TemperatureShadeDecrease, "Biome temperature decreases by n degrees if player in the shadow").getDouble(this.TemperatureShadeDecrease);
	}

	@Override
	public void GenDefaults()
	{
		BiomeGenBase[] biomeArray = BiomeGenBase.getBiomeGenArray();

		for(int p = 0; p < biomeArray.length; p++)
		{
			BiomeGenBase biome = biomeArray[p];

			if(biome == null)
			{
				continue;
			}

			String modID = ModIdentification.idFromObject(biome);

			File file = new File(EM_ConfigHandler.loadedProfile + EM_ConfigHandler.customPath + EnviroUtils.SafeFilename(modID) + ".cfg");

			if(!file.exists())
			{
				try
				{
					file.createNewFile();
				} catch(Exception e)
				{
					if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.ERROR, "Failed to create file for biome '" + biome.biomeName + "'", e);
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
	public File GetDefaultFile()
	{
		return new File(EM_ConfigHandler.loadedProfile + EM_ConfigHandler.customPath + "Biomes.cfg");
	}

	@Override
	public void generateEmpty(Configuration config, Object obj)
	{
		if(obj == null || !(obj instanceof BiomeGenBase))
		{
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.NORMAL.getLevel()) EnviroMine.logger.log(Level.ERROR, "Tried to register config with non biome object!", new Exception());
			return;
		}

		BiomeGenBase biome = (BiomeGenBase)obj;

		ArrayList<Type> typeList = new ArrayList<Type>();
		Type[] typeArray = BiomeDictionary.getTypesForBiome(biome);
		for(int i = 0; i < typeArray.length; i++)
		{
			typeList.add(typeArray[i]);
		}

		double air = typeList.contains(Type.NETHER)? -0.1D : 0D;
		double sanity = typeList.contains(Type.NETHER)? -0.1D : 0D;
		double water = typeList.contains(Type.NETHER) || typeList.contains(Type.DRY)? 0.05D : 0D;
		double temp = typeList.contains(Type.NETHER) || typeList.contains(Type.DRY)? 0.005D : 0D;
        boolean isDesertBiome = typeList.contains(Type.DESERT) || typeList.contains(Type.DRY); //TODO change if needed
        double DesertBiomeTemperatureMultiplier = typeList.contains(Type.DESERT) || typeList.contains(Type.DRY)? 3D : 1D;

        double TemperatureRainDecrease = typeList.contains(Type.WATER) ? 10D : typeList.contains(Type.JUNGLE)? 8D : 6D;
        double TemperatureThunderDecrease = typeList.contains(Type.WATER) ? 12D : typeList.contains(Type.JUNGLE)? 10D : 8D;
        boolean TemperatureRainBool = typeList.contains(Type.WATER) || typeList.contains(Type.WET) || typeList.contains(Type.JUNGLE) || (typeList.contains(Type.FOREST) && typeList.contains(Type.WET)) || (typeList.contains(Type.FOREST) && !typeList.contains(Type.COLD)) || (typeList.contains(Type.PLAINS) && !typeList.contains(Type.HOT));
        boolean TemperatureThunderBool = typeList.contains(Type.WATER) || typeList.contains(Type.WET) || typeList.contains(Type.JUNGLE) || (typeList.contains(Type.FOREST) && typeList.contains(Type.WET)) || (typeList.contains(Type.FOREST) && !typeList.contains(Type.COLD));
        double TemperatureShadeDecrease = /*typeList.contains(Type.WATER) ? 12D : typeList.contains(Type.JUNGLE)? 10D :*/ 2.5D;

        double biomeTemp = 25;
        double DAWN_TEMPERATURE;
        double DAY_TEMPERATURE;
        double DUSK_TEMPERATURE;
        double NIGHT_TEMPERATURE;

//        if(EnviroMine.isHbmSpaceLoaded()) {
//            if (biome instanceof BiomeGenBaseMoho) {
//                DAWN_TEMPERATURE =  75D;
//                DAY_TEMPERATURE =  0D;
//                DUSK_TEMPERATURE = 75D;
//                NIGHT_TEMPERATURE = 100D;
//                biomeTemp = 300;
//            }
//            else if (biome instanceof BiomeGenBaseEve) {
//                DAWN_TEMPERATURE =  71.85D;
//                DAY_TEMPERATURE =  0D;
//                DUSK_TEMPERATURE = 71.85D;
//                NIGHT_TEMPERATURE = 259.98D;
//                biomeTemp = 146.85;
//            }
//            else if (biome instanceof BiomeGenMoon) {
//                DAWN_TEMPERATURE =  177D;
//                DAY_TEMPERATURE =  0D;
//                DUSK_TEMPERATURE = 177D;
//                NIGHT_TEMPERATURE = 300D;
//                biomeTemp = 127;
//                air = -10.0D;
//            } else if (biome instanceof BiomeGenBaseMinmus) {
//                DAWN_TEMPERATURE =  64D;
//                DAY_TEMPERATURE =  0D;
//                DUSK_TEMPERATURE = 64D;
//                NIGHT_TEMPERATURE = 121D;
//                biomeTemp = 14;
//            }
//            else if(biome instanceof BiomeGenBaseDuna) {
//                if(biome instanceof BiomeGenDunaPolar || biome instanceof BiomeGenDunaPolarHills) {
//                    DAWN_TEMPERATURE =  98D;
//                    DAY_TEMPERATURE =  0D;
//                    DUSK_TEMPERATURE = 98D;
//                    NIGHT_TEMPERATURE = 173D;
//                    biomeTemp = 20;
//                } else {
//                    DAWN_TEMPERATURE = 69D;
//                    DAY_TEMPERATURE = 0D;
//                    DUSK_TEMPERATURE = 69D;
//                    NIGHT_TEMPERATURE = 98D;
//                    biomeTemp = 35;
//                }
//            } else if (biome instanceof BiomeGenIke) { //From phobos
//                DAWN_TEMPERATURE =  88D;
//                DAY_TEMPERATURE =  0D;
//                DUSK_TEMPERATURE = 88D;
//                NIGHT_TEMPERATURE = 150D;
//                biomeTemp = 27;
//            } else {
//                DAWN_TEMPERATURE = 4D;
//                DAY_TEMPERATURE = 0D;
//                DUSK_TEMPERATURE = 4D;
//                NIGHT_TEMPERATURE = 8D;
//                biomeTemp = EnviroUtils.getBiomeTemp(biome);
//            }
//        } else {
            DAWN_TEMPERATURE = /*typeList.contains(Type.DESERT) || typeList.contains(Type.DRY)? 4D :*/ 4D;
            DAY_TEMPERATURE = /*typeList.contains(Type.DESERT) || typeList.contains(Type.DRY)? 0D :*/ 0D;
            DUSK_TEMPERATURE = /*typeList.contains(Type.DESERT) || typeList.contains(Type.DRY)? 4D :*/ 4D;
            NIGHT_TEMPERATURE = /*typeList.contains(Type.DESERT) || typeList.contains(Type.DRY)? 8D :*/ 8D;

            biomeTemp = EnviroUtils.getBiomeTemp(biome);
//        }

		String catName = this.categoryName() + "." + biome.biomeName;
//TODO configs not generating properly
		config.get(catName, BOName[0], biome.biomeID).getInt(biome.biomeID);
		config.get(catName, BOName[1], true).getBoolean(true);
		config.get(catName, BOName[2], EnviroUtils.getBiomeWater(biome), "Water Quality: dirty, salty, cold, clean").getString();
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
		BOName = new String[19];
		BOName[0] = "01.Biome ID";
		BOName[1] = "02.Allow Config Override";
		BOName[2] = "03.Water Quality";
		BOName[3] = "04.Ambient Temperature";
		BOName[4] = "05.Temp Rate";
		BOName[5] = "06.Sanity Rate";
		BOName[6] = "07.Dehydrate Rate";
		BOName[7] = "08.Air Quality Rate";
        BOName[8] = "09.Is desert biome?";
        BOName[9] = "10.Desert biome temperature multiplier";
        BOName[10] = "11.Dawn biome temperature decrease";
        BOName[11] = "12.Day biome temperature decrease";
        BOName[12] = "13.Dusk biome temperature decrease";
        BOName[13] = "14.Night biome temperature decrease";
        BOName[14] = "15.Biome temperature rain decrease";
        BOName[15] = "16.Biome temperature thunder decrease";
        BOName[16] = "17.Should biome temperature decrease when rain?";
        BOName[17] = "18.Should biome temperature decrease when thunder?";
        BOName[18] = "19.Biome temperature shadow decrease";
	}
}
