package enviromine.core.config;

import static enviromine.core.EM_ConfigHandler.CATEGORY_KOTMATROSS_FORK_CHANGES_WATER;

import net.minecraftforge.common.config.Configuration;

import enviromine.core.EM_Settings;

public class ConfigWater {

    public static void init(Configuration config) {
        int i = 0;
//TODO: remaining ones
        EM_Settings.RADIOACTIVE_FROSTY_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_FrostyWaterTemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_FROSTY_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");
    }

}
