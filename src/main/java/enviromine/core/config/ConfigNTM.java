package enviromine.core.config;

import static enviromine.core.EM_ConfigHandler.CATEGORY_KOTMATROSS_FORK_CHANGES_NTM;

import net.minecraftforge.common.config.Configuration;

import enviromine.core.EM_Settings;

public class ConfigNTM {

    public static void init(Configuration config) {
        int i = 0;

        EM_Settings.HbmGasMaskBreakMultiplier = config.getInt(
            String.format("%03d" + "_HbmGasMaskFilterBreakMultiplier", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.HbmGasMaskBreakMultiplier,
            0,
            65536,
            "Multiplier for breaking a hbm gas mask filter");

        EM_Settings.HbmGasMaskBreakChanceNumber = config.getInt(
            String.format("%03d" + "_HbmGasMaskFilterBreakChance", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.HbmGasMaskBreakChanceNumber,
            0,
            100,
            "The number on which the chance of reducing the durability of the hbm filter depends. The lower the number, the greater the chance that the filter durability will decrease");

        EM_Settings.NTMSpaceAirQualityDecrease = config.getFloat(
            String.format("%03d" + "_NTMSpaceAirQualityDecrease", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.NTMSpaceAirQualityDecrease,
            -65536,
            65536,
            "If the player cannot breathe (not a suitable atmosphere) the air quality will begin to decrease by this value");
        EM_Settings.NTMSpaceAirVentAirQualityIncrease = config.getFloat(
            String.format("%03d" + "_NTMSpaceAirVentAirQualityIncrease", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.NTMSpaceAirVentAirQualityIncrease,
            -65536,
            65536,
            "Air vent will replenish this amount of air quality when running");

        EM_Settings.NTMSpaceAirVentTemperatureConstant = config.getFloat(
            String.format("%03d" + "_NTMSpaceAirVentTemperatureConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.NTMSpaceAirVentTemperatureConstant,
            -65536,
            65536,
            "Temperature created in the air vent's air pocket ");

        EM_Settings.EnableHBMMachinesHeat = config.getBoolean(
            String.format("%03d" + "_EnableHBMMachinesHeat", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.EnableHBMMachinesHeat,
            "Enable ambient temperature heating mechanics for hbm's ntm machines");

        EM_Settings.BurnerPressHeatDivisor = config.getFloat(
            String.format("%03d" + "_BurnerPressHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BurnerPressHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 1600 temperature");
        EM_Settings.BurnerPressHeatHardCap = config.getFloat(
            String.format("%03d" + "_BurnerPressHeatHardCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BurnerPressHeatHardCap,
            1,
            65536,
            "Hard temperature limit (cannot exceed this value)");
        EM_Settings.FireboxHeatDivisor = config.getFloat(
            String.format("%03d" + "_FireboxHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.FireboxHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 200 temperature");
        EM_Settings.HeaterOvenHeatDivisor = config.getFloat(
            String.format("%03d" + "_HeaterOvenHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.HeaterOvenHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 1000 temperature");
        EM_Settings.FluidBurnerHeatDivisor = config.getFloat(
            String.format("%03d" + "_FluidBurnerHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.FluidBurnerHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 100_000 temperature");

        EM_Settings.HeaterElectricHeatDivisor = config.getFloat(
            String.format("%03d" + "_HeaterElectricHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.HeaterElectricHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max (average) - 10_000 temperature");
        EM_Settings.HeaterElectricHeatHardCap = config.getFloat(
            String.format("%03d" + "_HeaterElectricHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.HeaterElectricHeatHardCap,
            1,
            65536,
            "Hard temperature limit (cannot exceed this value)");

        EM_Settings.IronFurnaceHeatDivisor = config.getFloat(
            String.format("%03d" + "_IronFurnaceHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.IronFurnaceHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 2000 temperature");
        EM_Settings.IronFurnaceHeatHardCap = config.getFloat(
            String.format("%03d" + "_IronFurnaceHeatHardCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.IronFurnaceHeatHardCap,
            1,
            65536,
            "Hard temperature limit (cannot exceed this value)");

        EM_Settings.SteelFurnaceHeatDivisor = config.getFloat(
            String.format("%03d" + "_SteelFurnaceHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.SteelFurnaceHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 100_000 temperature");
        EM_Settings.CombinationOvenHeatDivisor = config.getFloat(
            String.format("%03d" + "_CombinationOvenHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.CombinationOvenHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 100_000 temperature");

        EM_Settings.CrucibleHeatDivisor = config.getFloat(
            String.format("%03d" + "_CrucibleHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.CrucibleHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 100_000 temperature");

        EM_Settings.BoilerHeatDivisor = config.getFloat(
            String.format("%03d" + "_BoilerHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BoilerHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Boiler specifics - valid up to 100_000 TU");
        EM_Settings.BoilerHeaterOvenDivisorConstant = config.getFloat(
            String.format("%03d" + "_BoilerHeaterOvenDivisorConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BoilerHeaterOvenDivisorConstant,
            1,
            65536,
            "Divider value for temperature. Boiler specifics - valid up to 500_000 TU");
        EM_Settings.BoilerMAXDivisorConstant = config.getFloat(
            String.format("%03d" + "_BoilerMAXDivisorConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BoilerMAXDivisorConstant,
            1,
            65536,
            "Divider value for temperature. Boiler specifics - valid up to 3_200_000 TU (in fact up to 999_001 TU)");

        EM_Settings.BoilerIndustrialHeatDivisor = config.getFloat(
            String.format("%03d" + "_BoilerIndustrialHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BoilerIndustrialHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Boiler specifics - valid up to 100_000 TU");

        EM_Settings.BoilerIndustrialHeaterOvenDivisorConstant = config.getFloat(
            String.format("%03d" + "_BoilerIndustrialHeaterOvenDivisorConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BoilerIndustrialHeaterOvenDivisorConstant,
            1,
            65536,
            "Divider value for temperature. Boiler specifics - valid up to 500_000 TU");

        EM_Settings.BoilerIndustrialMAXDivisorConstant = config.getFloat(
            String.format("%03d" + "_BoilerIndustrialMAXDivisorConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BoilerIndustrialMAXDivisorConstant,
            1,
            65536,
            "Divider value for temperature. Boiler specifics - valid up to 12_800_000 TU (in fact up to 999_001 TU)");

        EM_Settings.FurnaceBrickHeatDivisor = config.getFloat(
            String.format("%03d" + "_FurnaceBrickHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.FurnaceBrickHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 1600 temperature");
        EM_Settings.FurnaceBrickHeatHardCap = config.getFloat(
            String.format("%03d" + "_FurnaceBrickHeatHardCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.FurnaceBrickHeatHardCap,
            1,
            65536,
            "Hard temperature limit (cannot exceed this value)");

        EM_Settings.DiFurnaceHeatDivisor = config.getFloat(
            String.format("%03d" + "_DiFurnaceHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.DiFurnaceHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 12_800 temperature");

        EM_Settings.DiFurnaceRTGHeatDivisor = config.getFloat(
            String.format("%03d" + "_DiFurnaceRTGHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.DiFurnaceRTGHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Power level (max) = 600 X6 = 3600 (temperature)");

        EM_Settings.NukeFurnaceHeatDivisor = config.getFloat(
            String.format("%03d" + "_NukeFurnaceHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.NukeFurnaceHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Operations (max) = 200");

        EM_Settings.RTGFurnaceHeatConstant = config.getFloat(
            String.format("%03d" + "_RTGFurnaceHeatConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.RTGFurnaceHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");

        EM_Settings.WoodBurningGenHeatDivisor = config.getFloat(
            String.format("%03d" + "_WoodBurningGenHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.WoodBurningGenHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 1600 temperature");

        EM_Settings.DieselGenHeatConstant = config.getFloat(
            String.format("%03d" + "_DieselGenHeatConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.DieselGenHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.ICEHeatConstant = config.getFloat(
            String.format("%03d" + "_ICEHeatConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.ICEHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");

        EM_Settings.CyclotronHeatConstant = config.getFloat(
            String.format("%03d" + "_CyclotronHeatConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.CyclotronHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");

        EM_Settings.GeothermalGenHeatDivisor = config.getFloat(
            String.format("%03d" + "_GeothermalGenHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.GeothermalGenHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine");

        EM_Settings.RBMKRodHeatDivisor = config.getFloat(
            String.format("%03d" + "_RBMKRodHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.RBMKRodHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine");
        EM_Settings.RBMKRodHeatHardCap = config.getFloat(
            String.format("%03d" + "_RBMKRodHeatHardCap", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.RBMKRodHeatHardCap,
            1,
            65536,
            "Hard temperature limit (cannot exceed this value)");

        EM_Settings.ArcFurnaceHeatConstant = config.getFloat(
            String.format("%03d" + "_ArcFurnaceHeatConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.ArcFurnaceHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.FlareStackHeatConstant = config.getFloat(
            String.format("%03d" + "_FlareStackHeatConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.FlareStackHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.CokerHeatDivisor = config.getFloat(
            String.format("%03d" + "_CokerHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.CokerHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine");

        EM_Settings.TurbofanHeatConstant = config.getFloat(
            String.format("%03d" + "_TurbofanHeatConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.TurbofanHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.TurbofanAfterburnerHeatConstant = config.getFloat(
            String.format("%03d" + "_TurbofanAfterburnerHeatConstant", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.TurbofanAfterburnerHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees). Turbofan specifics - temperature when using the \"Afterburner\" upgrade");

        EM_Settings.CCGasTurbineHeatDivisor = config.getFloat(
            String.format("%03d" + "_CCGasTurbineHeatDivisor", i++),
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.CCGasTurbineHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine");
    }

}
