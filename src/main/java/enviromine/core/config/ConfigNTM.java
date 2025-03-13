package enviromine.core.config;

import static enviromine.core.EM_ConfigHandler.CATEGORY_KOTMATROSS_FORK_CHANGES_NTM;

import net.minecraftforge.common.config.Configuration;

import enviromine.core.EM_Settings;

public class ConfigNTM {

    public static void init(Configuration config) {
        int i = 0;

        EM_Settings.HbmGasMaskBreakMultiplier = config.getInt(
            "a23_Hbm Gas Mask filter break multiplier",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.HbmGasMaskBreakMultiplier,
            0,
            65536,
            "Multiplier for breaking a hbm gas mask filter");

        EM_Settings.HbmGasMaskBreakChanceNumber = config.getInt(
            "18_Hbm Gas Mask filter break chance number",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.HbmGasMaskBreakChanceNumber,
            0,
            100,
            "The number on which the chance of reducing the durability of the hbm filter depends. The lower the number, the greater the chance that the filter durability will decrease");

        EM_Settings.NTMSpaceAirQualityDecrease = config.getFloat(
            "a-112_NTMSpaceAirQualityDecrease",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.NTMSpaceAirQualityDecrease,
            -65536,
            65536,
            "If the player cannot breathe (not a suitable atmosphere) the air quality will begin to decrease by this value");
        EM_Settings.NTMSpaceAirVentAirQualityIncrease = config.getFloat(
            "a-113_NTMSpaceAirVentAirQualityIncrease",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.NTMSpaceAirVentAirQualityIncrease,
            -65536,
            65536,
            "Air vent will replenish this amount of air quality when running");
        
        EM_Settings.NTMSpaceAirVentTemperatureConstant = config.getFloat(
                "a-158_NTMSpaceAirVentTemperatureConstant",
                CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
                EM_Settings.NTMSpaceAirVentTemperatureConstant,
                -65536,
                65536,
                "Temperature created in the air vent's air pocket ");
        
        
        
        
        EM_Settings.EnableHBMMachinesHeat = config.getBoolean(
            "42-0X_Enable HBM Machines Heat",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.EnableHBMMachinesHeat,
            "Enable ambient temperature heating mechanics for hbm's ntm machines");

        EM_Settings.BurnerPressHeatDivisor = config.getFloat(
            "42-1_BurnerPressHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BurnerPressHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 1600 temperature");
        EM_Settings.BurnerPressHeatHardCap = config.getFloat(
            "42-2_BurnerPressHeatHardCap",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BurnerPressHeatHardCap,
            1,
            65536,
            "Hard temperature limit (cannot exceed this value)");
        EM_Settings.FireboxHeatDivisor = config.getFloat(
            "43_Firebox Heat Divisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.FireboxHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 200 temperature");
        EM_Settings.HeaterOvenHeatDivisor = config.getFloat(
            "44_Heater Oven Heat Divisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.HeaterOvenHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 1000 temperature");
        EM_Settings.FluidBurnerHeatDivisor = config.getFloat(
            "45_FluidBurnerHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.FluidBurnerHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 100_000 temperature");

        EM_Settings.HeaterElectricHeatDivisor = config.getFloat(
            "46-1_HeaterElectricHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.HeaterElectricHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max (average) - 10_000 temperature");
        EM_Settings.HeaterElectricHeatHardCap = config.getFloat(
            "46-2_HeaterElectricHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.HeaterElectricHeatHardCap,
            1,
            65536,
            "Hard temperature limit (cannot exceed this value)");

        EM_Settings.IronFurnaceHeatDivisor = config.getFloat(
            "47-1_IronFurnaceHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.IronFurnaceHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 2000 temperature");
        EM_Settings.IronFurnaceHeatHardCap = config.getFloat(
            "47-2_IronFurnaceHeatHardCap",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.IronFurnaceHeatHardCap,
            1,
            65536,
            "Hard temperature limit (cannot exceed this value)");

        EM_Settings.SteelFurnaceHeatDivisor = config.getFloat(
            "48_SteelFurnaceHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.SteelFurnaceHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 100_000 temperature");
        EM_Settings.CombinationOvenHeatDivisor = config.getFloat(
            "49_CombinationOvenHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.CombinationOvenHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 100_000 temperature");

        EM_Settings.CrucibleHeatDivisor = config.getFloat(
            "50_CrucibleHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.CrucibleHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 100_000 temperature");

        EM_Settings.BoilerHeatDivisor = config.getFloat(
            "51-1_BoilerHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BoilerHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Boiler specifics - valid up to 100_000 TU");
        EM_Settings.BoilerHeaterOvenDivisorConstant = config.getFloat(
            "51-2_BoilerHeaterOvenDivisorConstant",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BoilerHeaterOvenDivisorConstant,
            1,
            65536,
            "Divider value for temperature. Boiler specifics - valid up to 500_000 TU");
        EM_Settings.BoilerMAXDivisorConstant = config.getFloat(
            "51-3_BoilerMAXDivisorConstant",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BoilerMAXDivisorConstant,
            1,
            65536,
            "Divider value for temperature. Boiler specifics - valid up to 3_200_000 TU (in fact up to 999_001 TU)");

        EM_Settings.BoilerIndustrialHeatDivisor = config.getFloat(
            "52-1_BoilerIndustrialHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BoilerIndustrialHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Boiler specifics - valid up to 100_000 TU");
        EM_Settings.BoilerIndustrialHeaterOvenDivisorConstant = config.getFloat(
            "52-2_BoilerIndustrialHeaterOvenDivisorConstant",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BoilerIndustrialHeaterOvenDivisorConstant,
            1,
            65536,
            "Divider value for temperature. Boiler specifics - valid up to 500_000 TU");
        EM_Settings.BoilerIndustrialMAXDivisorConstant = config.getFloat(
            "52-3_BoilerIndustrialMAXDivisorConstant",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.BoilerIndustrialMAXDivisorConstant,
            1,
            65536,
            "Divider value for temperature. Boiler specifics - valid up to 12_800_000 TU (in fact up to 999_001 TU)");

        EM_Settings.FurnaceBrickHeatDivisor = config.getFloat(
            "53-1_FurnaceBrickHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.FurnaceBrickHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 1600 temperature");
        EM_Settings.FurnaceBrickHeatHardCap = config.getFloat(
            "53-2_FurnaceBrickHeatHardCap",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.FurnaceBrickHeatHardCap,
            1,
            65536,
            "Hard temperature limit (cannot exceed this value)");

        EM_Settings.DiFurnaceHeatDivisor = config.getFloat(
            "54_DiFurnaceHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.DiFurnaceHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Max - 12_800 temperature");
        EM_Settings.DiFurnaceRTGHeatDivisor = config.getFloat(
            "55_DiFurnaceRTGHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.DiFurnaceRTGHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Power level (max) = 600 X6 = 3600 (temperature)");
        EM_Settings.NukeFurnaceHeatDivisor = config.getFloat(
            "56_NukeFurnaceHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.NukeFurnaceHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Operations (max) = 200");

        EM_Settings.RTGFurnaceHeatConstant = config.getFloat(
            "57_RTGFurnaceHeatConstant",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.RTGFurnaceHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.WoodBurningGenHeatDivisor = config.getFloat(
            "58_WoodBurningGenHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.WoodBurningGenHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine. Coal - 1600 temperature");
        EM_Settings.DieselGenHeatConstant = config.getFloat(
            "59_DieselGenHeatConstant",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.DieselGenHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.ICEHeatConstant = config.getFloat(
            "60_ICEHeatConstant",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.ICEHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.CyclotronHeatConstant = config.getFloat(
            "61_CyclotronHeatConstant",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.CyclotronHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.GeothermalGenHeatDivisor = config.getFloat(
            "62_GeothermalGenHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.GeothermalGenHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine");

        EM_Settings.RBMKRodHeatDivisor = config.getFloat(
            "63-1_RBMKRodHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.RBMKRodHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine");
        EM_Settings.RBMKRodHeatHardCap = config.getFloat(
            "63-2_RBMKRodHeatHardCap",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.RBMKRodHeatHardCap,
            1,
            65536,
            "Hard temperature limit (cannot exceed this value)");

        EM_Settings.ArcFurnaceHeatConstant = config.getFloat(
            "64_ArcFurnaceHeatConstant",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.ArcFurnaceHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.FlareStackHeatConstant = config.getFloat(
            "65_FlareStackHeatConstant",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.FlareStackHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.CokerHeatDivisor = config.getFloat(
            "66_CokerHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.CokerHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine");

        EM_Settings.TurbofanHeatConstant = config.getFloat(
            "67-1_TurbofanHeatConstant",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.TurbofanHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees)");
        EM_Settings.TurbofanAfterburnerHeatConstant = config.getFloat(
            "67-2_TurbofanAfterburnerHeatConstant",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.TurbofanAfterburnerHeatConstant,
            -65536,
            65536,
            "The ambient temperature from this machine is a constant you can specify (in Celsius degrees). Turbofan specifics - temperature when using the \"Afterburner\" upgrade");

        EM_Settings.CCGasTurbineHeatDivisor = config.getFloat(
            "68_CCGasTurbineHeatDivisor",
            CATEGORY_KOTMATROSS_FORK_CHANGES_NTM,
            EM_Settings.CCGasTurbineHeatDivisor,
            1,
            65536,
            "Divider value for temperature. The temperature itself depends on the specifics of the machine");
    }

}
