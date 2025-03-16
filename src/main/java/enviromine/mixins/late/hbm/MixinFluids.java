package enviromine.mixins.late.hbm;

import static com.hbm.inventory.fluid.Fluids.HOTSTEAM;
import static com.hbm.inventory.fluid.Fluids.LIQUID;
import static com.hbm.inventory.fluid.Fluids.STEAM;
import static com.hbm.inventory.fluid.Fluids.SUPERHOTSTEAM;
import static com.hbm.inventory.fluid.Fluids.ULTRAHOTSTEAM;
import static com.hbm.inventory.fluid.Fluids.UNSIPHONABLE;
import static com.hbm.inventory.fluid.Fluids.customFluids;

import java.io.File;
import java.util.Locale;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.hbm.handler.pollution.PollutionHandler;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.trait.FT_Heatable;
import com.hbm.inventory.fluid.trait.FT_Polluting;
import com.hbm.inventory.fluid.trait.FT_VentRadiation;
import com.hbm.render.util.EnumSymbol;
import com.hbm.util.I18nUtil;

@Mixin(value = Fluids.class, priority = 999)
public class MixinFluids {

    @Unique
    private static FluidType RADIOACTIVE_FROSTY_WATER;
    @Unique
    private static FluidType FROSTY_WATER;
    @Unique
    private static FluidType RADIOACTIVE_COLD_WATER;
    @Unique
    private static FluidType DIRTY_COLD_WATER;
    @Unique
    private static FluidType SALTY_COLD_WATER;
    @Unique
    private static FluidType CLEAN_COLD_WATER;
    @Unique
    private static FluidType RADIOACTIVE_WATER;
    @Unique
    private static FluidType DIRTY_WATER;
    @Unique
    private static FluidType SALTY_WATER;
    @Unique
    private static FluidType RADIOACTIVE_WARM_WATER;
    @Unique
    private static FluidType DIRTY_WARM_WATER;
    @Unique
    private static FluidType SALTY_WARM_WATER;
    @Unique
    private static FluidType CLEAN_WARM_WATER;
    @Unique
    private static FluidType RADIOACTIVE_HOT_WATER;
    @Unique
    private static FluidType HOT_WATER;

    @Inject(method = "readCustomFluids", at = @At(value = "HEAD"), remap = false)
    private static void readCustomFluids(File file, CallbackInfo ci) {
        String RADIOACTIVE_FROSTY_NAME = "RADIOACTIVE_FROSTY_WATER";
        String FROSTY_NAME = "FROSTY_WATER";
        String RADIOACTIVE_COLD_NAME = "RADIOACTIVE_COLD_WATER";
        String DIRTY_COLD_NAME = "DIRTY_COLD_WATER";
        String SALTY_COLD_NAME = "SALTY_COLD_WATER";
        String CLEAN_COLD_NAME = "CLEAN_COLD_WATER";
        String RADIOACTIVE_NAME = "RADIOACTIVE_WATER";
        String DIRTY_NAME = "DIRTY_WATER";
        String SALTY_NAME = "SALTY_WATER";
        String RADIOACTIVE_WARM_NAME = "RADIOACTIVE_WARM_WATER";
        String DIRTY_WARM_NAME = "DIRTY_WARM_WATER";
        String SALTY_WARM_NAME = "SALTY_WARM_WATER";
        String CLEAN_WARM_NAME = "CLEAN_WARM_WATER";
        String RADIOACTIVE_HOT_NAME = "RADIOACTIVE_HOT_WATER";
        String HOT_NAME = "HOT_WATER";

        // Because [7732-18-5](Dihydrogen oxide)
        int id = 7732;

        RADIOACTIVE_FROSTY_WATER = enviromine$constructFluid(RADIOACTIVE_FROSTY_NAME, 3, EnumSymbol.RADIATION, id++)
            .setTemp(2);
        FROSTY_WATER = enviromine$constructFluid(FROSTY_NAME, 1, EnumSymbol.NONE, id++).setTemp(2);
        RADIOACTIVE_COLD_WATER = enviromine$constructFluid(RADIOACTIVE_COLD_NAME, 2, EnumSymbol.RADIATION, id++)
            .setTemp(10);
        DIRTY_COLD_WATER = enviromine$constructFluid(DIRTY_COLD_NAME, 1, EnumSymbol.NONE, id++).setTemp(10);
        SALTY_COLD_WATER = enviromine$constructFluid(SALTY_COLD_NAME, 1, EnumSymbol.NONE, id++).setTemp(10);
        CLEAN_COLD_WATER = enviromine$constructFluid(CLEAN_COLD_NAME, 0, EnumSymbol.NONE, id++).setTemp(10);
        RADIOACTIVE_WATER = enviromine$constructFluid(RADIOACTIVE_NAME, 2, EnumSymbol.RADIATION, id++).setTemp(20);
        DIRTY_WATER = enviromine$constructFluid(DIRTY_NAME, 1, EnumSymbol.NONE, id++).setTemp(20);
        SALTY_WATER = enviromine$constructFluid(SALTY_NAME, 1, EnumSymbol.NONE, id++).setTemp(20);
        RADIOACTIVE_WARM_WATER = enviromine$constructFluid(RADIOACTIVE_WARM_NAME, 2, EnumSymbol.RADIATION, id++)
            .setTemp(40);
        DIRTY_WARM_WATER = enviromine$constructFluid(DIRTY_WARM_NAME, 1, EnumSymbol.NONE, id++).setTemp(40);
        SALTY_WARM_WATER = enviromine$constructFluid(SALTY_WARM_NAME, 1, EnumSymbol.NONE, id++).setTemp(40);
        CLEAN_WARM_WATER = enviromine$constructFluid(CLEAN_WARM_NAME, 0, EnumSymbol.NONE, id++).setTemp(40);
        RADIOACTIVE_HOT_WATER = enviromine$constructFluid(RADIOACTIVE_HOT_NAME, 3, EnumSymbol.RADIATION, id++)
            .setTemp(54);
        HOT_WATER = enviromine$constructFluid(HOT_NAME, 1, EnumSymbol.NONE, id++).setTemp(54);

        double eff_steam_boil = 1.0D;
        double eff_steam_heatex = 0.25D;

        RADIOACTIVE_FROSTY_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1),
            new FT_VentRadiation(0.02F),
            new FT_Polluting().release(PollutionHandler.PollutionType.POISON, 2.0000001E-5F));
        FROSTY_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1));
        RADIOACTIVE_COLD_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1),
            new FT_VentRadiation(0.02F),
            new FT_Polluting().release(PollutionHandler.PollutionType.POISON, 2.0000001E-5F));
        DIRTY_COLD_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1),
            new FT_Polluting().release(PollutionHandler.PollutionType.POISON, 2.0000001E-5F));
        SALTY_COLD_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1));
        CLEAN_COLD_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1));
        RADIOACTIVE_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1),
            new FT_VentRadiation(0.02F),
            new FT_Polluting().release(PollutionHandler.PollutionType.POISON, 2.0000001E-5F));
        DIRTY_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1),
            new FT_Polluting().release(PollutionHandler.PollutionType.POISON, 2.0000001E-5F));
        SALTY_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1));
        RADIOACTIVE_WARM_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1),
            new FT_VentRadiation(0.02F),
            new FT_Polluting().release(PollutionHandler.PollutionType.POISON, 2.0000001E-5F));
        DIRTY_WARM_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1),
            new FT_Polluting().release(PollutionHandler.PollutionType.POISON, 2.0000001E-5F));
        SALTY_WARM_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1));
        CLEAN_WARM_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1));
        RADIOACTIVE_HOT_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1),
            new FT_VentRadiation(0.02F),
            new FT_Polluting().release(PollutionHandler.PollutionType.POISON, 2.0000001E-5F));
        HOT_WATER.addTraits(
            new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
                .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
                .addStep(200, 1, STEAM, 100)
                .addStep(220, 1, HOTSTEAM, 10)
                .addStep(238, 1, SUPERHOTSTEAM, 1)
                .addStep(2500, 10, ULTRAHOTSTEAM, 1));

        customFluids.add(RADIOACTIVE_FROSTY_WATER);
        customFluids.add(FROSTY_WATER);
        customFluids.add(RADIOACTIVE_COLD_WATER);
        customFluids.add(DIRTY_COLD_WATER);
        customFluids.add(SALTY_COLD_WATER);
        customFluids.add(CLEAN_COLD_WATER);
        customFluids.add(RADIOACTIVE_WATER);
        customFluids.add(DIRTY_WATER);
        customFluids.add(SALTY_WATER);
        customFluids.add(RADIOACTIVE_WARM_WATER);
        customFluids.add(DIRTY_WARM_WATER);
        customFluids.add(SALTY_WARM_WATER);
        customFluids.add(CLEAN_WARM_WATER);
        customFluids.add(RADIOACTIVE_HOT_WATER);
        customFluids.add(HOT_WATER);
    }

    @Unique
    private static FluidType enviromine$constructFluid(String name, int poison, EnumSymbol symbol, int id) {
        return new FluidType(
            name,
            0x3333FF,
            poison,
            0,
            0,
            symbol,
            name.toLowerCase(Locale.US),
            16777215,
            id,
            I18nUtil.resolveKey("hbmfluid." + name.toLowerCase(Locale.US), new Object[0]))
                .addTraits(LIQUID, UNSIPHONABLE);
    }
}
