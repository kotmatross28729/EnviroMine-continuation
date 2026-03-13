package enviromine.core.config;

import static enviromine.core.EM_ConfigHandler.CATEGORY_KOTMATROSS_FORK_CHANGES_GASES;

import net.minecraftforge.common.config.Configuration;

import enviromine.core.EM_Settings;

public class ConfigGases {

    public static void init(Configuration config) {
        int i = 0;

        EM_Settings.hardcoregases = config.getBoolean(
            String.format("%03d" + "_HardcoreGases", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.hardcoregases,
            "If true, then all gases will be invisible");

        EM_Settings.EnviromineGasMaskBreakMultiplier = config.getInt(
            String.format("%03d" + "_GasMaskBreakMultiplier", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.EnviromineGasMaskBreakMultiplier,
            0,
            65536,
            "A number that is subtracted from the current enviromine mask filter durability if the player is in a gas block, that is suffocating");

        EM_Settings.SulfurDioxideGasDebugLogger = config.getBoolean(
            String.format("%03d" + "_SulfurDioxideGasDebugLogger", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.SulfurDioxideGasDebugLogger,
            "Don't use unless you are a developer");

        EM_Settings.CarbonMonoxideGasDebugLogger = config.getBoolean(
            String.format("%03d" + "_CarbonMonoxideGasDebugLogger", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.CarbonMonoxideGasDebugLogger,
            "Don't use unless you are a developer");

        EM_Settings.HydrogenSulfideGasDebugLogger = config.getBoolean(
            String.format("%03d" + "_HydrogenSulfideGasDebugLogger", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.HydrogenSulfideGasDebugLogger,
            "Don't use unless you are a developer");

        EM_Settings.SulfurDioxidePoisoningAmplifier = config.getInt(
            String.format("%03d" + "_SulfurDioxidePoisoningAmplifier", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.SulfurDioxidePoisoningAmplifier,
            1,
            65536,
            "How dense the Sulfur Dioxide gas should be, in order for it to have the opportunity to poison you?");

        EM_Settings.SulfurDioxideSeverePoisoningAmplifier = config.getInt(
            String.format("%03d" + "_SulfurDioxideSeverePoisoningAmplifier", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.SulfurDioxideSeverePoisoningAmplifier,
            1,
            65536,
            "How dense the Sulfur Dioxide gas should be, in order for it to have the opportunity to severe poison you?");

        EM_Settings.SulfurDioxidePoisoningTime = config.getInt(
            String.format("%03d" + "_Sulfur Dioxide Poisoning Time", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.SulfurDioxidePoisoningTime,
            1,
            65536,
            "How long does Sulfur Dioxide poisoning last?");

        EM_Settings.SulfurDioxideSeverePoisoningTime = config.getInt(
            String.format("%03d" + "_SulfurDioxideSeverePoisoningTime", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.SulfurDioxideSeverePoisoningTime,
            1,
            65536,
            "How long does severe Sulfur Dioxide poisoning last?");

        EM_Settings.SulfurDioxidePoisoningLevel = config.getInt(
            String.format("%03d" + "_SulfurDioxidePoisoningLevel", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.SulfurDioxidePoisoningLevel,
            0,
            65536,
            "What level of poisoning applies when player is Sulfur Dioxide poisoned?");

        EM_Settings.SulfurDioxideSeverePoisoningLevel = config.getInt(
            String.format("%03d" + "_SulfurDioxideSeverePoisoningLevel", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.SulfurDioxideSeverePoisoningLevel,
            0,
            65536,
            "What level of poisoning applies when player is severe Sulfur Dioxide poisoned?");

        EM_Settings.SulfurDioxidePoisoningChance = config.getInt(
            String.format("%03d" + "_Sulfur DioxidePoisoningChance", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.SulfurDioxidePoisoningChance,
            1,
            65536,
            "What is the chance of Sulfur Dioxide poisoning if the player has no protection?");

        EM_Settings.CarbonMonoxidePoisoningAmplifier = config.getInt(
            String.format("%03d" + "_CarbonMonoxidePoisoningAmplifier", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.CarbonMonoxidePoisoningAmplifier,
            1,
            65536,
            "How dense the Carbon Monoxide gas should be, in order for it to have the opportunity to poison you?");

        EM_Settings.CarbonMonoxidePoisoningTime = config.getInt(
            String.format("%03d" + "_CarbonMonoxidePoisoningTime", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.CarbonMonoxidePoisoningTime,
            1,
            65536,
            "How long does Carbon Monoxide poisoning last?");

        EM_Settings.CarbonMonoxidePoisoningLevel = config.getInt(
            String.format("%03d" + "_CarbonMonoxidePoisoningLevel", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.CarbonMonoxidePoisoningLevel,
            0,
            65536,
            "What level of poisoning applies when player is Carbon Monoxide poisoned?");

        EM_Settings.CarbonMonoxidePoisoningChance = config.getInt(
            String.format("%03d" + "_CarbonMonoxidePoisoningChance", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.CarbonMonoxidePoisoningChance,
            1,
            65536,
            "What is the chance of Carbon Monoxide poisoning if the player has no protection?");

        EM_Settings.HydrogenSulfidePoisoningAmplifier = config.getInt(
            String.format("%03d" + "_HydrogenSulfidePoisoningAmplifier", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.HydrogenSulfidePoisoningAmplifier,
            1,
            65536,
            "How dense the Hydrogen Sulfide gas should be, in order for it to have the opportunity to poison you?");

        EM_Settings.HydrogenSulfideSeverePoisoningAmplifier = config.getInt(
            String.format("%03d" + "_HydrogenSulfideSeverePoisoningAmplifier", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.HydrogenSulfideSeverePoisoningAmplifier,
            1,
            65536,
            "How dense the Hydrogen Sulfide gas should be, in order for it to have the opportunity to severe poison you?");

        EM_Settings.HydrogenSulfidePoisoningTime = config.getInt(
            String.format("%03d" + "_HydrogenSulfidePoisoning Time", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.HydrogenSulfidePoisoningTime,
            1,
            65536,
            "How long does Hydrogen Sulfide poisoning last?");

        EM_Settings.HydrogenSulfideSeverePoisoningTime = config.getInt(
            String.format("%03d" + "_HydrogenSulfideSeverePoisoningTime", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.HydrogenSulfideSeverePoisoningTime,
            1,
            65536,
            "How long does severe Hydrogen Sulfide poisoning last?");

        EM_Settings.HydrogenSulfidePoisoningLevel = config.getInt(
            String.format("%03d" + "_Hydrogen Sulfide Poisoning Level", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.HydrogenSulfidePoisoningLevel,
            0,
            65536,
            "What level of poisoning applies when player is Hydrogen Sulfide poisoned?");

        EM_Settings.HydrogenSulfideSeverePoisoningLevel = config.getInt(
            String.format("%03d" + "_Hydrogen Sulfide Severe Poisoning Level", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.HydrogenSulfideSeverePoisoningLevel,
            0,
            65536,
            "What level of poisoning applies when player is severe Hydrogen Sulfide poisoned?");

        EM_Settings.HydrogenSulfidePoisoningChance = config.getInt(
            String.format("%03d" + "_ChanceHydrogenSulfidePoisoning", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_GASES,
            EM_Settings.HydrogenSulfidePoisoningChance,
            1,
            65536,
            "What is the chance of Hydrogen Sulfide poisoning if the player has no protection?");
    }

}
