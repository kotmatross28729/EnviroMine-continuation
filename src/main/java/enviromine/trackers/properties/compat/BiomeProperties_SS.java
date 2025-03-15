package enviromine.trackers.properties.compat;

import net.minecraft.world.World;

import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

public class BiomeProperties_SS {

    public static String checkSeasonWater(World world, String SPRING_waterQuality, String SUMMER_waterQuality,
        String AUTUMN_waterQuality, String WINTER_waterQuality) {

        Season currentSeason = SeasonHelper.getSeasonState(world)
            .getSeason();

        if (currentSeason != null) {
            switch (currentSeason) {
                case SPRING -> {
                    return SPRING_waterQuality;
                }
                case SUMMER -> {
                    return SUMMER_waterQuality;
                }
                case AUTUMN -> {
                    return AUTUMN_waterQuality;
                }
                case WINTER -> {
                    return WINTER_waterQuality;
                }
            }
        }
        return null;
    }
}
