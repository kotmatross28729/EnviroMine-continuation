package enviromine.core.config.mixins;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigMixinsLate {

    public static boolean MixinNetherliciousTorch = true;
    public static boolean MixinCFMFridge = true;
    public static boolean MixinCookingforblockheadsFridge = true;

    // All bullshit with water
    public static boolean MixinNTMWaterTypes = true;

    // Rotten food
    public static boolean MixinLiquefactionRecipes = true;

    // Radiation
    public static boolean MixinTileEntityMachineDrain = true;

    // Yeahg
    public static boolean MixinNTMWaterTypesCompat = true;
    public static final String CATEGORY_LATE_MIXINS = "Late mixins";

    public static void init(File configFile) {
        Configuration config = new Configuration(configFile);

        int i = 0;

        // TODO: comments

        MixinNetherliciousTorch = config.getBoolean(
            String.format("%03d" + "_MixinNetherliciousTorch", i++),
            CATEGORY_LATE_MIXINS,
            MixinNetherliciousTorch,
            "");

        MixinCFMFridge = config
            .getBoolean(String.format("%03d" + "_MixinCFMFridge", i++), CATEGORY_LATE_MIXINS, MixinCFMFridge, "");

        MixinCookingforblockheadsFridge = config.getBoolean(
            String.format("%03d" + "_MixinCookingforblockheadsFridge", i++),
            CATEGORY_LATE_MIXINS,
            MixinCookingforblockheadsFridge,
            "");

        MixinNTMWaterTypes = config.getBoolean(
            String.format("%03d" + "_MixinNTMWaterTypes", i++),
            CATEGORY_LATE_MIXINS,
            MixinNTMWaterTypes,
            "");

        MixinLiquefactionRecipes = config.getBoolean(
            String.format("%03d" + "_MixinLiquefactionRecipes", i++),
            CATEGORY_LATE_MIXINS,
            MixinLiquefactionRecipes,
            "");

        MixinTileEntityMachineDrain = config.getBoolean(
            String.format("%03d" + "_MixinTileEntityMachineDrain", i++),
            CATEGORY_LATE_MIXINS,
            MixinTileEntityMachineDrain,
            "");

        MixinNTMWaterTypesCompat = config.getBoolean(
            String.format("%03d" + "_MixinNTMWaterTypesCompat", i++),
            CATEGORY_LATE_MIXINS,
            MixinNTMWaterTypesCompat,
            "");

        if (config.hasChanged()) {
            config.save();
        }
    }

}
