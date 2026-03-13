package enviromine.core.config;

import static enviromine.core.EM_ConfigHandler.CATEGORY_KOTMATROSS_FORK_CHANGES_WATER;

import net.minecraftforge.common.config.Configuration;

import enviromine.core.EM_Settings;

public class ConfigWater {

    public static void init(Configuration config) {
        int i = 0;

        EM_Settings.RADIOACTIVE_FROSTY_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_FROSTY_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_FROSTY_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.FROSTY_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_FROSTY_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.FROSTY_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.RADIOACTIVE_COLD_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_COLD_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_COLD_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.DIRTY_COLD_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_DIRTY_COLD_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.DIRTY_COLD_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.SALTY_COLD_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_SALTY_COLD_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.SALTY_COLD_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.CLEAN_COLD_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_CLEAN_COLD_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.CLEAN_COLD_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.RADIOACTIVE_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.DIRTY_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_DIRTY_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.DIRTY_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.SALTY_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_SALTY_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.SALTY_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.CLEAN_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_CLEAN_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.CLEAN_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.RADIOACTIVE_WARM_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_WARM_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_WARM_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.DIRTY_WARM_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_DIRTY_WARM_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.DIRTY_WARM_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.SALTY_WARM_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_SALTY_WARM_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.SALTY_WARM_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.CLEAN_WARM_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_CLEAN_WARM_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.CLEAN_WARM_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.RADIOACTIVE_HOT_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_HOT_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_HOT_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.HOT_TemperatureInfluence = config.getFloat(
            String.format("%03d" + "_HOT_TemperatureInfluence", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.HOT_TemperatureInfluence,
            -65536,
            65536,
            "When the player drinks this type of water, their temperature changes by this value");

        EM_Settings.RADIOACTIVE_FROSTY_Hydration = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_FROSTY_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_FROSTY_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.FROSTY_Hydration = config.getFloat(
            String.format("%03d" + "_FROSTY_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.FROSTY_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.RADIOACTIVE_COLD_Hydration = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_COLD_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_COLD_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.DIRTY_COLD_Hydration = config.getFloat(
            String.format("%03d" + "_DIRTY_COLD_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.DIRTY_COLD_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.SALTY_COLD_Hydration = config.getFloat(
            String.format("%03d" + "_SALTY_COLD_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.SALTY_COLD_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.CLEAN_COLD_Hydration = config.getFloat(
            String.format("%03d" + "_CLEAN_COLD_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.CLEAN_COLD_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.RADIOACTIVE_Hydration = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.DIRTY_Hydration = config.getFloat(
            String.format("%03d" + "_DIRTY_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.DIRTY_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.SALTY_Hydration = config.getFloat(
            String.format("%03d" + "_SALTY_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.SALTY_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.CLEAN_Hydration = config.getFloat(
            String.format("%03d" + "_CLEAN_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.CLEAN_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.RADIOACTIVE_WARM_Hydration = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_WARM_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_WARM_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.DIRTY_WARM_Hydration = config.getFloat(
            String.format("%03d" + "_DIRTY_WARM_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.DIRTY_WARM_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.SALTY_WARM_Hydration = config.getFloat(
            String.format("%03d" + "_SALTY_WARM_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.SALTY_WARM_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.CLEAN_WARM_Hydration = config.getFloat(
            String.format("%03d" + "_CLEAN_WARM_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.CLEAN_WARM_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.RADIOACTIVE_HOT_Hydration = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_HOT_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_HOT_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.HOT_Hydration = config.getFloat(
            String.format("%03d" + "_HOT_Hydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.HOT_Hydration,
            -65536,
            65536,
            "When a player drinks this type of water, their hydration will increase by that amount");

        EM_Settings.RADIOACTIVE_FROSTY_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_FROSTY_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_FROSTY_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.FROSTY_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_FROSTY_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.FROSTY_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.RADIOACTIVE_COLD_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_COLD_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_COLD_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.DIRTY_COLD_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_DIRTY_COLD_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.DIRTY_COLD_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.SALTY_COLD_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_SALTY_COLD_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.SALTY_COLD_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.CLEAN_COLD_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_CLEAN_COLD_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.CLEAN_COLD_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.RADIOACTIVE_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.DIRTY_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_DIRTY_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.DIRTY_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.SALTY_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_SALTY_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.SALTY_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.CLEAN_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_CLEAN_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.CLEAN_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.RADIOACTIVE_WARM_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_WARM_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_WARM_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.DIRTY_WARM_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_DIRTY_WARM_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.DIRTY_WARM_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.SALTY_WARM_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_SALTY_WARM_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.SALTY_WARM_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.CLEAN_WARM_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_CLEAN_WARM_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.CLEAN_WARM_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.RADIOACTIVE_HOT_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_RADIOACTIVE_HOT_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.RADIOACTIVE_HOT_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.HOT_TempInfluenceCap = config.getFloat(
            String.format("%03d" + "_HOT_TempInfluenceCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.HOT_TempInfluenceCap,
            -65536,
            65536,
            "");

        EM_Settings.finiteEMWater = config.getBoolean(
            "Finite EM Water",
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.finiteEMWater,
            "If true, EnviroMine's custom water will not regenerate sources (infinite water disabled).");

        EM_Settings.enableHighTempCooling = config.getBoolean(
            "Enable High Temp Cooling",
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.enableHighTempCooling,
            "If true, when player's body temperature exceeds the threshold and they are in water, their temperature will be reduced.");

        EM_Settings.highTempThreshold = config.getFloat(
            "High Temp Threshold",
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.highTempThreshold,
            0.0f,
            100.0f,
            "Body temperature above this value (in Celsius) will trigger cooling when in water.");

        EM_Settings.coolTempNormal = config.getFloat(
            "Cool Temp Normal Water",
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.coolTempNormal,
            0.0f,
            100.0f,
            "Body temperature set to this value when cooling in normal water (vanilla water, or ambient radioactive/dirty/salty water).");

        EM_Settings.coolTempCold = config.getFloat(
            "Cool Temp Cold Water",
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.coolTempCold,
            0.0f,
            100.0f,
            "Body temperature set to this value when cooling in cold water (radioactive_cold, dirty_cold, salty_cold).");

        EM_Settings.coolTempFrosty = config.getFloat(
            "Cool Temp Frosty Water",
            CATEGORY_KOTMATROSS_FORK_CHANGES_WATER,
            EM_Settings.coolTempFrosty,
            0.0f,
            100.0f,
            "Body temperature set to this value when cooling in frosty water (radioactive_frosty, frosty).");
    }
}


