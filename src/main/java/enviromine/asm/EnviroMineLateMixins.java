package enviromine.asm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.launchwrapper.Launch;

import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.MixinEnvironment;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

import enviromine.core.EM_Settings;
import enviromine.core.config.mixins.ConfigMixinsLate;

@LateMixin
public class EnviroMineLateMixins implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.enviromine.late.json";
    }

    public static final MixinEnvironment.Side side = MixinEnvironment.getCurrentEnvironment()
        .getSide();

    @Override
    public List<String> getMixins(Set<String> loadedMods) {

        String configFolder = "config" + File.separator + EM_Settings.MOD_ID + File.separator;
        ConfigMixinsLate.init(new File(Launch.minecraftHome, configFolder + "mixins.cfg"));

        List<String> mixins = new ArrayList<>();

        if (loadedMods.contains("netherlicious")) {
            if (ConfigMixinsLate.MixinNetherliciousTorch) {
                mixins.add("netherlicious.MixinTorchSoul");
                mixins.add("netherlicious.MixinTorchFoxfire");
                mixins.add("netherlicious.MixinTorchShadow");
            }
        }

        if (loadedMods.contains("cfm")) {
            if (ConfigMixinsLate.MixinCFMFridge) {
                mixins.add("MrCrayfishFurnitureMod.MixinTileEntityFridge");
                mixins.add("MrCrayfishFurnitureMod.MixinTileEntityFreezer");
            }
        }

        if (loadedMods.contains("cookingforblockheads")) { // GTNH ONLY
            if (ConfigMixinsLate.MixinCookingforblockheadsFridge) {
                mixins.add("CookingForBlockheads.MixinTileFridge");
            }
        }

        boolean isHbmSpaceLoaded = false;

        try {
            if (Launch.classLoader.getClassBytes("com.hbm.dim.SolarSystem") != null) {
                isHbmSpaceLoaded = true;
            }
        } catch (IOException ignored) {}

        LogManager.getLogger()
            .info("isHbmSpaceLoaded: {}", isHbmSpaceLoaded);

        if (loadedMods.contains("hbm")) {

            if (ConfigMixinsLate.MixinNTMWaterTypes) {
                if (side == MixinEnvironment.Side.CLIENT) {
                    mixins.add("hbm.client.MixinRenderFluidTank");
                }
                mixins.add("hbm.MixinRadiolysisRecipes");
                mixins.add("hbm.MixinFluidContainerRegistry");
            }

            if (ConfigMixinsLate.MixinLiquefactionRecipes) {
                mixins.add("hbm.MixinLiquefactionRecipes");
            }

            if (ConfigMixinsLate.MixinTileEntityMachineDrain) {
                mixins.add("hbm.MixinTileEntityMachineDrain");
            }

            if (ConfigMixinsLate.MixinNTMWaterTypesCompat) {
                mixins.add("hbm.waterCompat.MixinTileEntityWasteDrum");
                mixins.add("hbm.waterCompat.MixinExplosionThermo");
                mixins.add("hbm.waterCompat.MixinLandmine");
                mixins.add("hbm.waterCompat.MixinTileEntityReactorResearch");
            }

            // mixins.add("hbm.MixinChunkRadiationHandlerSimple");

        }

        return mixins;
    }

}
