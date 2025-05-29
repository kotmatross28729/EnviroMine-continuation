package enviromine.core.config.mixins;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigMixinsEarly {

    public static boolean MixinBlockCauldron = true;
    public static boolean MixinBlockTorch = true;
    public static boolean MixinBlockOre = true;
    public static boolean MixinBlockLilyPad = true;
    public static boolean MixinWorld = true;

    public static final String CATEGORY_EARLY_MIXINS = "Early mixins";

    public static void init(File configFile) {
        Configuration config = new Configuration(configFile);

        int i = 0;

        // TODO: comments

        MixinBlockCauldron = config.getBoolean(
            String.format("%03d" + "_MixinBlockCauldron", i++),
            CATEGORY_EARLY_MIXINS,
            MixinBlockCauldron,
            "");

        MixinBlockTorch = config
            .getBoolean(String.format("%03d" + "_MixinBlockTorch", i++), CATEGORY_EARLY_MIXINS, MixinBlockTorch, "");

        MixinBlockOre = config
            .getBoolean(String.format("%03d" + "_MixinBlockOre", i++), CATEGORY_EARLY_MIXINS, MixinBlockOre, "");

        MixinBlockLilyPad = config.getBoolean(
            String.format("%03d" + "_MixinBlockLilyPad", i++),
            CATEGORY_EARLY_MIXINS,
            MixinBlockLilyPad,
            "");

        MixinWorld = config
            .getBoolean(String.format("%03d" + "_MixinWorld", i++), CATEGORY_EARLY_MIXINS, MixinWorld, "");

        if (config.hasChanged()) {
            config.save();
        }
    }

}
