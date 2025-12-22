package enviromine.core.config;

import static enviromine.core.EM_ConfigHandler.CATEGORY_KOTMATROSS_FORK_CHANGES;

import net.minecraftforge.common.config.Configuration;

import enviromine.core.EM_Settings;

public class ConfigGeneral {

    public static void init(Configuration config) {
        int i = 0;

        EM_Settings.oldTorchLogic = config.getBoolean(
            String.format("%03d" + "_oldTorchLogic", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.oldTorchLogic,
            "Enables old torch logic without mixins, enable this if you don't want to see burned villages");

        EM_Settings.disableWaterTypes = config.getBoolean(
            String.format("%03d" + "_disableWaterTypes", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.disableWaterTypes,
            "Disables water types, leave only clean (vanilla) water");

        EM_Settings.convertToVanilla = config.getBoolean(
            String.format("%03d" + "_convertToVanilla", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.convertToVanilla,
            "Whether to automatically convert custom water to vanilla water when it comes into contact with vanilla water");

        EM_Settings.conversionChance = config.getInt(
            String.format("%03d" + "_conversionChance", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.conversionChance,
            1, 100,
            "Conversion check probability denominator (1/N). For example, 5 means check every 5 updates. Higher values result in slower conversion");

        EM_Settings.chainReaction = config.getBoolean(
            String.format("%03d" + "_chainReaction", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.chainReaction,
            "Whether to enable chain reaction (when a water block converts, it will attempt to convert adjacent custom water blocks)");

        EM_Settings.maxConversionsPerTick = config.getInt(
            String.format("%03d" + "_maxConversionsPerTick", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.maxConversionsPerTick,
            1, 100,
            "Maximum number of custom water block conversions per tick (prevents performance issues)");

        EM_Settings.rotTimeValidation = config.getBoolean(
            String.format("%03d" + "_rotTimeValidation", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.rotTimeValidation,
            "Should rotting take into account the exact time, or just the day? "
                + " [+] If false, all food obtained during the day will have different rot rates."
                + " [-] However, food cannot be stacked together, even if there is a 1 tick gap in time."
                + " [+] If true, food obtained during the same day can be stacked together."
                + " [-] However, food obtained at the beginning of the day and at the end will have the same rot rate.");

        EM_Settings.enablePlayerRandomMobRender = config.getBoolean(
            String.format("%03d" + "_PlayerInsanityRandomMobRender", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.enablePlayerRandomMobRender,
            "If true, then if a player has the insanity effect, his model is replaced with a random mob. Causes bugs with the model");

        EM_Settings.enableItemPropsDivideByTwo = config.getBoolean(
            String.format("%03d" + "_enableItemPropsDivideByTwo", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.EnableHBMMachinesHeat,
            "If true, then divides ItemProperties \"Effect n\" parameters by 2, since for some reason they are applied 2 times. If your values specified in \"Effect n\" are not applied as they are, but are 2 times smaller, then disable this option (you may have some kind of bugfix mod installed)");

        EM_Settings.BodyTempSleep = config.getFloat(
            String.format("%03d" + "_BodyTempSleep", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTempSleep,
            -65536F,
            65536F,
            "Ambient temperature will increase by this amount when the player is sleeping");

        EM_Settings.StrongArmorMaxTemp = config.getFloat(
            String.format("%03d" + "_StrongArmorMaxTemp", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.StrongArmorMaxTemp,
            -65536F,
            65536F,
            "If the armor has 12.Is Temperature Sealed = true, and body temperature < this number, body temperature will be maintained at 36.6");

        EM_Settings.StrongArmorMaxTempRise = config.getFloat(
            String.format("%03d" + "_StrongArmorMaxTempRise", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.StrongArmorMaxTempRise,
            -65536F,
            65536F,
            "If the armor has 12.Is Temperature Sealed = true, and temperature rise < this number, body temperature will be maintained at 36.6");

        EM_Settings.StrongArmorMinTemp = config.getFloat(
            String.format("%03d" + "_StrongArmorMinTemp", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.StrongArmorMinTemp,
            -65536F,
            65536F,
            "If the armor has 12.Is Temperature Sealed = true, and body temperature > this number, body temperature will be maintained at 36.6");

        EM_Settings.StrongArmorMinTempDrop = config.getFloat(
            String.format("%03d" + "_StrongArmorMinTempDrop", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.StrongArmorMinTempDrop,
            -65536F,
            65536F,
            "If the armor has 12.Is Temperature Sealed = true, and temperature drop > this number, body temperature will be maintained at 36.6");

        EM_Settings.LightArmorMaxTemp = config.getFloat(
            String.format("%03d" + "_LightArmorMaxTemp", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.LightArmorMaxTemp,
            -65536F,
            65536F,
            "If the armor has 11.Is Temperature Resistance = true, and body temperature < this number, body temperature will be maintained at 36.6");

        EM_Settings.LightArmorMaxTempRise = config.getFloat(
            String.format("%03d" + "_LightArmorMaxTempRise", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.LightArmorMaxTempRise,
            -65536F,
            65536F,
            "If the armor has 11.Is Temperature Resistance = true, and temperature rise < this number, body temperature will be maintained at 36.6");

        EM_Settings.LightArmorMinTemp = config.getFloat(
            String.format("%03d" + "_LightArmorMinTemp", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.LightArmorMinTemp,
            -65536F,
            65536F,
            "If the armor has 11.Is Temperature Resistance = true, and body temperature > this number, body temperature will be maintained at 36.6");

        EM_Settings.LightArmorMinTempDrop = config.getFloat(
            String.format("%03d" + "_LightArmorMinTempDrop", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.LightArmorMinTempDrop,
            -65536F,
            65536F,
            "If the armor has 11.Is Temperature Resistance = true, and temperature drop > this number, body temperature will be maintained at 36.6");

        EM_Settings.LavaBlockAmbientTemperature = config.getFloat(
            String.format("%03d" + "_LavaBlockAmbientTemperature", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.LavaBlockAmbientTemperature,
            -65536F,
            65536F,
            "Ambient Temperature will be equal to this number if the player is in lava (if the armor has 12.Is Temperature Sealed = true, then no)");

        EM_Settings.BurningambientTemperature = config.getFloat(
            String.format("%03d" + "_BurningambientTemperature", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BurningambientTemperature,
            -65536F,
            65536F,
            "Ambient Temperature will be equal to this number if the player is on fire (or in lava if11.Is Temperature Resistance = true, if the armor has 11.Is Temperature Resistance = true, then no)");

        EM_Settings.RiseSpeedMin = config.getFloat(
            String.format("%03d" + "_RiseSpeedMin", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.RiseSpeedMin,
            -65536F,
            65536F,
            "Minimum RiseSpeed if player is on fire");

        EM_Settings.RiseSpeedLava = config.getFloat(
            String.format("%03d" + "_RiseSpeedLava", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.RiseSpeedLava,
            -65536F,
            65536F,
            "RiseSpeed if player in lava");

        EM_Settings.RiseSpeedLavaDecr = config.getFloat(
            String.format("%03d" + "_RiseSpeedLavaDecr", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.RiseSpeedLavaDecr,
            -65536F,
            65536F,
            "RiseSpeed if player in lava and wearing hev/env suit or armor has 11.Is Temperature Resistance = true");

        EM_Settings.SprintambientTemperature = config.getFloat(
            String.format("%03d" + "_SprintAmbientTemperature", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SprintambientTemperature,
            -65536F,
            65536F,
            "The player's temperature will increase by this number when he runs");

        EM_Settings.SweatTemperature = config.getFloat(
            String.format("%03d" + "_SweatTemperature", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SweatTemperature,
            -65536F,
            65536F,
            "The player will begin to sweat starting at this temperature");

        EM_Settings.SweatDehydrate = config.getFloat(
            String.format("%03d" + "_SweatDehydrate", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SweatDehydrate,
            -65536F,
            65536F,
            "When a player sweats, he will lose n% of water per second");

        EM_Settings.SweatHydration = config.getFloat(
            String.format("%03d" + "_SweatHydration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SweatHydration,
            -65536F,
            65536F,
            "The player needs n (n or more)% of water to decrease temperature (when sweats)");

        EM_Settings.SweatBodyTemp = config.getFloat(
            String.format("%03d" + "_SweatBodyTemp", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SweatBodyTemp,
            -65536F,
            65536F,
            "If the player has enough %water (see SweatHydration), then the body temperature will decrease by n every second (when sweats)");

        EM_Settings.DeathFromHeartAttack = config.getBoolean(
            String.format("%03d" + "_DeathFromHeartAttack", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.DeathFromHeartAttack,
            "Should a player die from a heart attack if sanity < 5?");

        EM_Settings.HeartAttackTimeToDie = config.getInt(
            String.format("%03d" + "_HeartAttackTimeToDie", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.HeartAttackTimeToDie,
            1,
            65536,
            "Time after which the player dies from a heart attack (for sanity > 0 but < 5, it will be the same number. For sanity = 0, it will be this number / 2). To calculate the time, multiply this number by 3, this will be the time in seconds");

        EM_Settings.SanityDropHealth = config.getFloat(
            String.format("%03d" + "_SanityDropHealth", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityDropHealth,
            0,
            65536,
            "When the player's health <= this value, sanity will begin to decrease");
        EM_Settings.SanityDropTemperatureHigh = config.getFloat(
            String.format("%03d" + "_SanityDropTemperatureHigh", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityDropTemperatureHigh,
            -65536,
            65536,
            "When player's body temperature >= this value, sanity will begin to decrease");
        EM_Settings.SanityDropTemperatureLow = config.getFloat(
            String.format("%03d" + "_SanityDropTemperatureLow", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityDropTemperatureLow,
            -65536,
            65536,
            "When player's body temperature <= this value, sanity will begin to decrease");

        EM_Settings.RealTemperatureConstant = config.getFloat(
            String.format("%03d" + "_RealTemperatureConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.RealTemperatureConstant,
            -65536,
            65536,
            "A constant used to calculate the player's body temperature. Ambient temperature + this constant = player's expected body temperature");

        EM_Settings.TimeBelow10_Air_Temp_Const_Air = config.getFloat(
            String.format("%03d" + "_TimeBelow10_Air_Temp_Const_Air", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.TimeBelow10_Air_Temp_Const_Air,
            -65536,
            65536,
            "When the air temperature <= this value and the player's body temperature <= TimeBelow10_Air_Temp_Const_BodyTemp, a countdown will begin, at the end of which the player will be permanently frostbite");

        EM_Settings.TimeBelow10_Air_Temp_Const_BodyTemp = config.getFloat(
            String.format("%03d" + "_TimeBelow10_Air_Temp_Const_BodyTemp", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.TimeBelow10_Air_Temp_Const_BodyTemp,
            -65536,
            65536,
            "When the player's body temperature <= this value and the air temperature <= TimeBelow10_Air_Temp_Const_Air, a countdown will begin, at the end of which the player will be permanently frostbite");

        EM_Settings.TimeBelow10_BodyTemp_Const = config.getFloat(
            String.format("%03d" + "_TimeBelow10_BodyTemp_Const", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.TimeBelow10_BodyTemp_Const,
            -65536,
            65536,
            "When the player's body temperature <= this value, a countdown will begin, at the end of which the player will be permanently frostbite");

        EM_Settings.BodyTemperatureHeatStartValue = config.getFloat(
            String.format("%03d" + "_BodyTemperatureHeatStartValue", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureHeatStartValue,
            -65536,
            65536,
            "From this temperature value, the player will receive the effect of heatstroke (1 lvl)");
        EM_Settings.BodyTemperatureHeatInstantDeath = config.getFloat(
            String.format("%03d" + "_BodyTemperatureHeatInstantDeath", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureHeatInstantDeath,
            -65536,
            65536,
            "If the player's body temperature is >= this value, they will die instantly");

        EM_Settings.BodyTemperatureHeatstroke6 = config.getFloat(
            String.format("%03d" + "_BodyTemperatureHeatstroke6", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureHeatstroke6,
            -65536,
            65536,
            "From this temperature value, the player will receive the effect of heatstroke (6 lvl)");
        EM_Settings.BodyTemperatureHeatstroke5 = config.getFloat(
            String.format("%03d" + "_BodyTemperatureHeatstroke5", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureHeatstroke5,
            -65536,
            65536,
            "From this temperature value, the player will receive the effect of heatstroke (5 lvl)");
        EM_Settings.BodyTemperatureHeatstroke4 = config.getFloat(
            String.format("%03d" + "_BodyTemperatureHeatstroke4", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureHeatstroke4,
            -65536,
            65536,
            "From this temperature value, the player will receive the effect of heatstroke (4 lvl)");
        EM_Settings.BodyTemperatureHeatstroke3 = config.getFloat(
            String.format("%03d" + "_BodyTemperatureHeatstroke3", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureHeatstroke3,
            -65536,
            65536,
            "From this temperature value, the player will receive the effect of heatstroke (3 lvl)");
        EM_Settings.BodyTemperatureHeatstroke2 = config.getFloat(
            String.format("%03d" + "_BodyTemperatureHeatstroke2", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureHeatstroke2,
            -65536,
            65536,
            "From this temperature value, the player will receive the effect of heatstroke (2 lvl)");

        EM_Settings.BodyTemperatureCatchFireMin = config.getFloat(
            String.format("%03d" + "_BodyTemperatureCatchFireMin", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureCatchFireMin,
            -65536,
            65536,
            "If the player's body temperature is >= this value, and there is lava nearby, the player will catch fire");
        EM_Settings.BodyTemperatureCatchFireMax = config.getFloat(
            String.format("%03d" + "_BodyTemperatureCatchFireMax", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureCatchFireMax,
            -65536,
            65536,
            "If the player's body temperature is >= this value, the player will catch fire");
        EM_Settings.BodyTemperatureCatchFireDuration = config.getFloat(
            String.format("%03d" + "_BodyTemperatureCatchFireDuration", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureCatchFireDuration,
            1,
            65536,
            "When the player is on fire (body temperature), the duration of the effect will be this value in seconds");

        EM_Settings.BodyTemperatureColdStartValue = config.getFloat(
            String.format("%03d" + "_BodyTemperatureColdStartValue", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureColdStartValue,
            -65536,
            65536,
            "From this temperature value, the player will receive the effect of hypothermia (1 lvl)");
        EM_Settings.BodyTemperatureColdStartValueVampire = config.getFloat(
            String.format("%03d" + "_BodyTemperatureColdStartValueVampire", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureColdStartValueVampire,
            -65536,
            65536,
            "From this temperature value, (if the player is a vampire) the player will receive the effect of hypothermia (1 lvl)");
        EM_Settings.BodyTemperatureHypothermia3 = config.getFloat(
            String.format("%03d" + "_BodyTemperatureHypothermia3", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureHypothermia3,
            -65536,
            65536,
            "From this temperature value, the player will receive the effect of hypothermia (3 lvl)");
        EM_Settings.BodyTemperatureHypothermia2 = config.getFloat(
            String.format("%03d" + "_BodyTemperatureHypothermia2", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureHypothermia2,
            -65536,
            65536,
            "From this temperature value, the player will receive the effect of hypothermia (2 lvl)");
        EM_Settings.BodyTemperatureHypothermia2Vampire = config.getFloat(
            String.format("%03d" + "_BodyTemperatureHypothermia2Vampire", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.BodyTemperatureHypothermia2Vampire,
            -65536,
            65536,
            "From this temperature value, (if the player is a vampire) the player will receive the effect of hypothermia (2 lvl)");

        EM_Settings.TimeBelow10StartValue = config.getFloat(
            String.format("%03d" + "_TimeBelow10StartValue", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.TimeBelow10StartValue,
            1,
            65536,
            "From this counter value, the player will receive the effect of frostbite (1 lvl), in seconds");
        EM_Settings.TimeBelow10Frostbite2 = config.getFloat(
            String.format("%03d" + "_TimeBelow10Frostbite2", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.TimeBelow10Frostbite2,
            1,
            65536,
            "From this counter value, the player will receive the effect of frostbite (2 lvl), in seconds");
        EM_Settings.TimeBelow10FrostbitePermanent = config.getFloat(
            String.format("%03d" + "_TimeBelow10FrostbitePermanent", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.TimeBelow10FrostbitePermanent,
            1,
            65536,
            "From this counter value, the player will receive the effect of permanent frostbite, in seconds");

        EM_Settings.SanityStage0UpperBound = config.getFloat(
            String.format("%03d" + "_SanityStage0UpperBound", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityStage0UpperBound,
            -65536,
            65536,
            "If current sanity <= this value, and > SanityStage0LowerBound, the player will receive the effect of insanity (1 lvl)");
        EM_Settings.SanityStage0LowerBound = config.getFloat(
            String.format("%03d" + "_SanityStage0LowerBound", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityStage0LowerBound,
            -65536,
            65536,
            "See SanityStage0UpperBound");
        EM_Settings.SanityStage1UpperBound = config.getFloat(
            String.format("%03d" + "_SanityStage1UpperBound", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityStage1UpperBound,
            -65536,
            65536,
            "If current sanity <= this value, and > SanityStage1LowerBound, the player will receive the effect of insanity (2 lvl)");
        EM_Settings.SanityStage1LowerBound = config.getFloat(
            String.format("%03d" + "_SanityStage1LowerBound", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityStage1LowerBound,
            -65536,
            65536,
            "See SanityStage1UpperBound");
        EM_Settings.SanityStage2UpperBound = config.getFloat(
            String.format("%03d" + "_SanityStage2UpperBound", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityStage2UpperBound,
            -65536,
            65536,
            "If current sanity <= this value, and > SanityStage2LowerBound, the player will receive the effect of insanity (3 lvl)");
        EM_Settings.SanityStage2LowerBound = config.getFloat(
            String.format("%03d" + "_SanityStage2LowerBound", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityStage2LowerBound,
            -65536,
            65536,
            "See SanityStage2UpperBound");
        EM_Settings.SanityStage3UpperBound = config.getFloat(
            String.format("%03d" + "_SanityStage3UpperBound", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityStage3UpperBound,
            -65536,
            65536,
            "If current sanity <= this value, and > SanityStage3LowerBound, the player will receive the effect of insanity (4 lvl), and the countdown to a heart attack will begin");
        EM_Settings.SanityStage3LowerBound = config.getFloat(
            String.format("%03d" + "_SanityStage3LowerBound", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityStage3LowerBound,
            -65536,
            65536,
            "See SanityStage2UpperBound");

        // EM_StatusManager

        EM_Settings.TrackerUpdateTimer = config.getFloat(
            String.format("%03d" + "_TrackerUpdateTimer", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.TrackerUpdateTimer,
            1,
            65536,
            "How often should the tracker be updated (in ticks)");
        EM_Settings.SanityRateDecreaseDark = config.getFloat(
            String.format("%03d" + "_SanityRateDecreaseDark", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityRateDecreaseDark,
            -65536,
            65536,
            "Sanity Rate will decrease by this number if the player is in the dark");
        EM_Settings.SanityBoostFlowers = config.getFloat(
            String.format("%03d" + "_SanityBoostFlowers", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityBoostFlowers,
            -65536,
            65536,
            "Sanity Boost, which is given by flowers in the inventory");

        EM_Settings.AirQualityIncreaseLight = config.getFloat(
            String.format("%03d" + "_AirQualityIncreaseLight", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.AirQualityIncreaseLight,
            -65536,
            65536,
            "Air quality will increase by this value if the player is in a lit area");
        EM_Settings.SanityRateIncreaseLight = config.getFloat(
            String.format("%03d" + "_SanityRateIncreaseLight", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityRateIncreaseLight,
            -65536,
            65536,
            "Sanity Rate will increase by this value if the player is in a lit area");
        EM_Settings.SanityRateDecreaseLight = config.getFloat(
            String.format("%03d" + "_SanityRateDecreaseLight", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SanityRateDecreaseLight,
            -65536,
            65536,
            "Sanity Rate will decrease by this value if the player is NOT in a lit area AND current Sanity Rate < 0");
        EM_Settings.SurfaceYPositionMultiplier = config.getFloat(
            String.format("%03d" + "_SurfaceYPositionMultiplier", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SurfaceYPositionMultiplier,
            -65536,
            65536,
            "The surface is defined as: sea level in dimension * this number = Y surface. For overworld it is 64*0.75=48");
        EM_Settings.AirQualityIncreaseSurface = config.getFloat(
            String.format("%03d" + "_AirQualityIncreaseSurface", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.AirQualityIncreaseSurface,
            -65536,
            65536,
            "Air quality will increase by this value if the player is on the surface");

        EM_Settings.MaxHighAltitudeTemp = config.getFloat(
            String.format("%03d" + "_MaxHighAltitudeTemp", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.MaxHighAltitudeTemp,
            -65536,
            65536,
            "Temperature that will be at maximum altitude");
        EM_Settings.MinLowAltitudeTemp = config.getFloat(
            String.format("%03d" + "_MinLowAltitudeTemp", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.MinLowAltitudeTemp,
            -65536,
            65536,
            "Temperature that will be at minimum altitude");
        EM_Settings.SurfaceYPosition = config.getFloat(
            String.format("%03d" + "_SurfaceYPosition", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SurfaceYPosition,
            1,
            65536,
            "Y position, which will be considered as a surface, for calculating temperature from height");
        EM_Settings.SkyYPositionLowerBound = config.getFloat(
            String.format("%03d" + "_SkyYPositionLowerBound", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SkyYPositionLowerBound,
            -65536,
            65536,
            "Y position from which temperature reduction begins");
        EM_Settings.SkyYPositionLowerBoundDivider = config.getFloat(
            String.format("%03d" + "_SkyYPositionLowerBoundDivider", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SkyYPositionLowerBoundDivider,
            1,
            65536,
            "Divider for temperature calculations ( > SkyYPositionLowerBound, < SkyYPositionUpperBound) ");
        EM_Settings.SkyYPositionUpperBound = config.getFloat(
            String.format("%03d" + "_SkyYPositionUpperBound", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SkyYPositionUpperBound,
            -65536,
            65536,
            "Maximum altitude, at which the temperature will be equal to MaxHighAltitudeTemp (if the biome temperature is not lower)");

        EM_Settings.AvgEntityTempDivider = config.getFloat(
            String.format("%03d" + "_AvgEntityTempDivider", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.AvgEntityTempDivider,
            -65536,
            65536,
            "Divider for the ambient temperature of nearby mobs");

        EM_Settings.AmbTemp_Bl_It_TempInf_Div = config.getFloat(
            String.format("%03d" + "_AmbTemp_Bl_It_TempInf_Div", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.AmbTemp_Bl_It_TempInf_Div,
            1,
            65536,
            "The influence of blocks/items from the world/inventory will be divided by this number");

        EM_Settings.AmbTemp_Bl_It_TempInf_For_RiseSpeedConst = config.getFloat(
            String.format("%03d" + "_AmbTemp_Bl_It_TempInf_For_RiseSpeedConst", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.AmbTemp_Bl_It_TempInf_For_RiseSpeedConst,
            -65536,
            65536,
            "If influence of blocks/items > biomeTemperature + this value, then riseSpeed = AmbTemp_Bl_It_TempInf_RiseSpeedConstant");

        EM_Settings.AmbTemp_Bl_It_TempInf_RiseSpeedConst = config.getFloat(
            String.format("%03d" + "_AmbTemp_Bl_It_TempInf_RiseSpeedConst", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.AmbTemp_Bl_It_TempInf_RiseSpeedConst,
            -65536,
            65536,
            "See AmbTemp_Bl_It_TempInf_For_RiseSpeedConstant");

        EM_Settings.HungerEffectDehydrateBonus = config.getFloat(
            String.format("%03d" + "_HungerEffectDehydrateBonus", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.HungerEffectDehydrateBonus,
            -65536,
            65536,
            "Bonus to dehydration if the player is under hunger effect");
        EM_Settings.NearLavaMinRiseSpeed = config.getFloat(
            String.format("%03d" + "_NearLavaMinRiseSpeed", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.NearLavaMinRiseSpeed,
            -65536,
            65536,
            "Minimum riseSpeed if the player is near to lava");
        EM_Settings.NearLavaDehydrateBonus = config.getFloat(
            String.format("%03d" + "_NearLavaDehydrateBonus", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.NearLavaDehydrateBonus,
            -65536,
            65536,
            "Bonus to dehydration if the player is near to lava");
        EM_Settings.NoBiomeRainfallDayDehydrateBonus = config.getFloat(
            String.format("%03d" + "_NoBiomeRainfallDayDehydrateBonus", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.NoBiomeRainfallDayDehydrateBonus,
            -65536,
            65536,
            "Bonus to dehydration if BiomeRainfall = 0 and it is daytime");

        EM_Settings.SprintDehydrateBonus = config.getFloat(
            String.format("%03d" + "_SprintDehydrateBonus", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SprintDehydrateBonus,
            -65536,
            65536,
            "Bonus to dehydration when the player is running");
        EM_Settings.SprintMinRiseSpeed = config.getFloat(
            String.format("%03d" + "_SprintMinRiseSpeed", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.SprintMinRiseSpeed,
            -65536,
            65536,
            "Minimum riseSpeed when the player is running");

        EM_Settings.DavyLampGasDetectRange = config.getFloat(
            String.format("%03d" + "_DavyLampGasDetectRange", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES,
            EM_Settings.DavyLampGasDetectRange,
            -65536,
            65536,
            "Distance from the player at which the davy lamp reacts to gases");
    }

}
