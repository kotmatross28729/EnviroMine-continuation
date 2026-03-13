package enviromine.handlers.compat;

import static enviromine.handlers.EM_StatusManager.calculateTemperatureChangeSpace;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import com.hbm.dim.CelestialBody;
import com.hbm.dim.trait.CBT_Atmosphere;
import com.hbm.handler.ThreeInts;
import com.hbm.handler.atmosphere.AtmosphereBlob;
import com.hbm.handler.atmosphere.ChunkAtmosphereManager;
import com.hbm.inventory.fluid.Fluids;

import enviromine.core.EM_Settings;
import enviromine.trackers.properties.BiomeProperties;
import enviromine.utils.CompatUtils;

public class EM_StatusManager_NTM_SPACE {

    // All compatibility with NTM:Space from the EM_StatusManager class goes here

    public static float getTempTerraformed(EntityLivingBase entityLiving, BiomeProperties biomeOverride) {
        CBT_Atmosphere atmosphere = CompatUtils.getAtmosphere(entityLiving.worldObj);
        if (CompatUtils.isTerraformed(atmosphere)) {
            return biomeOverride.ambientTemp_TERRAFORMED;
        }
        return biomeOverride.ambientTemp;
    }

    public static float TemperatureChangeSpace(EntityLivingBase entityLiving, float currentTime,
        float biome_DAWN_TEMPERATURE_TERRAFORMED, float biome_DAY_TEMPERATURE_TERRAFORMED,
        float biome_DUSK_TEMPERATURE_TERRAFORMED, float biome_NIGHT_TEMPERATURE_TERRAFORMED,
        float biome_DAWN_TEMPERATURE, float biome_DAY_TEMPERATURE, float biome_DUSK_TEMPERATURE,
        float biome_NIGHT_TEMPERATURE) {
        CelestialBody body = CelestialBody.getBody(entityLiving.worldObj);
        float fullCycle = Math.round(
            (float) (body.getRotationalPeriod() / (1 - (1 / body.getPlanet()
                .getOrbitalPeriod()))));
        float phasePeriod = fullCycle / 4F;

        CBT_Atmosphere atmosphere = CompatUtils.getAtmosphere(entityLiving.worldObj);

        float temperatureChange;
        if (CompatUtils.isTerraformed(atmosphere)) {
            temperatureChange = calculateTemperatureChangeSpace(
                currentTime % fullCycle,
                phasePeriod,
                biome_DAWN_TEMPERATURE_TERRAFORMED,
                biome_DAY_TEMPERATURE_TERRAFORMED,
                biome_DUSK_TEMPERATURE_TERRAFORMED,
                biome_NIGHT_TEMPERATURE_TERRAFORMED);
        } else {
            temperatureChange = calculateTemperatureChangeSpace(
                currentTime % fullCycle,
                phasePeriod,
                biome_DAWN_TEMPERATURE,
                biome_DAY_TEMPERATURE,
                biome_DUSK_TEMPERATURE,
                biome_NIGHT_TEMPERATURE);
        }

        return temperatureChange;
    }

    public static float TemperatureRateChangeSpace(EntityLivingBase entityLiving, float currentTime,
        float tempRate_DAWN, float tempRate_DAY, float tempRate_DUSK, float tempRate_NIGHT) {
        float temperatureRate;

        CelestialBody body = CelestialBody.getBody(entityLiving.worldObj);
        float phasePeriod = Math.round(
            (float) (body.getRotationalPeriod() / (1 - (1 / body.getPlanet()
                .getOrbitalPeriod()))) / 4F);
        temperatureRate = calculateTemperatureChangeSpace(
            currentTime,
            phasePeriod,
            tempRate_DAWN,
            tempRate_DAY,
            tempRate_DUSK,
            tempRate_NIGHT);

        return temperatureRate;
    }

    public static float getBiomeTemperatureBlobs(EntityLivingBase entityLiving, float biomeTemperature) {
        return checkBlobs(entityLiving) ? EM_Settings.NTMSpaceAirVentTemperatureConstant : biomeTemperature;
    }

    public static boolean getAirVentConst(EntityLivingBase entityLiving) {
        return CompatUtils.isTerraformed(CompatUtils.getAtmosphere(entityLiving.worldObj));
    }

    public static boolean getAirVentConstBlobs(EntityLivingBase entityLiving) {
        return checkBlobs(entityLiving);
    }

    private static boolean checkBlobs(EntityLivingBase entityLiving) {
        ThreeInts pos = new ThreeInts(
            MathHelper.floor_double(entityLiving.posX),
            MathHelper.floor_double(entityLiving.posY + entityLiving.getEyeHeight()),
            MathHelper.floor_double(entityLiving.posZ));
        List<AtmosphereBlob> currentBlobs = ChunkAtmosphereManager.proxy
            .getBlobs(entityLiving.worldObj, pos.x, pos.y, pos.z);
        for (AtmosphereBlob blob : currentBlobs) {
            if (blob.hasFluid(Fluids.AIR, 0.21) || blob.hasFluid(Fluids.OXYGEN, 0.09)) {
                return true;
            }
        }
        return false;
    }

}
