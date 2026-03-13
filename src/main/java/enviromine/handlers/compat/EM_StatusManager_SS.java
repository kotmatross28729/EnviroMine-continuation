package enviromine.handlers.compat;

import net.minecraft.entity.EntityLivingBase;

import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

public class EM_StatusManager_SS {

    // All compatibility with SereneSeasons from the EM_StatusManager class goes here

    public static float getTempDecreaseSeason(EntityLivingBase entityLiving, float biomeTemperature,
        float biome_EARLY_SPRING_TEMPERATURE_DECREASE, float biome_MID_SPRING_TEMPERATURE_DECREASE,
        float biome_LATE_SPRING_TEMPERATURE_DECREASE, float biome_EARLY_SUMMER_TEMPERATURE_DECREASE,
        float biome_MID_SUMMER_TEMPERATURE_DECREASE, float biome_LATE_SUMMER_TEMPERATURE_DECREASE,
        float biome_EARLY_AUTUMN_TEMPERATURE_DECREASE, float biome_MID_AUTUMN_TEMPERATURE_DECREASE,
        float biome_LATE_AUTUMN_TEMPERATURE_DECREASE, float biome_EARLY_WINTER_TEMPERATURE_DECREASE,
        float biome_MID_WINTER_TEMPERATURE_DECREASE, float biome_LATE_WINTER_TEMPERATURE_DECREASE) {
        Season.SubSeason currentSubSeason = SeasonHelper.getSeasonState(entityLiving.worldObj)
            .getSubSeason();
        if (currentSubSeason != null) {
            switch (currentSubSeason) {
                case EARLY_SPRING -> biomeTemperature -= biome_EARLY_SPRING_TEMPERATURE_DECREASE;
                case MID_SPRING -> biomeTemperature -= biome_MID_SPRING_TEMPERATURE_DECREASE;
                case LATE_SPRING -> biomeTemperature -= biome_LATE_SPRING_TEMPERATURE_DECREASE;

                case EARLY_SUMMER -> biomeTemperature -= biome_EARLY_SUMMER_TEMPERATURE_DECREASE;
                case MID_SUMMER -> biomeTemperature -= biome_MID_SUMMER_TEMPERATURE_DECREASE;
                case LATE_SUMMER -> biomeTemperature -= biome_LATE_SUMMER_TEMPERATURE_DECREASE;

                case EARLY_AUTUMN -> biomeTemperature -= biome_EARLY_AUTUMN_TEMPERATURE_DECREASE;
                case MID_AUTUMN -> biomeTemperature -= biome_MID_AUTUMN_TEMPERATURE_DECREASE;
                case LATE_AUTUMN -> biomeTemperature -= biome_LATE_AUTUMN_TEMPERATURE_DECREASE;

                case EARLY_WINTER -> biomeTemperature -= biome_EARLY_WINTER_TEMPERATURE_DECREASE;
                case MID_WINTER -> biomeTemperature -= biome_MID_WINTER_TEMPERATURE_DECREASE;
                case LATE_WINTER -> biomeTemperature -= biome_LATE_WINTER_TEMPERATURE_DECREASE;
            }
        }
        return biomeTemperature;
    }
}
