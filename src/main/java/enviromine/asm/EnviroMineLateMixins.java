package enviromine.asm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.launchwrapper.Launch;

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

        if (loadedMods.contains("hbm")) {

            if (ConfigMixinsLate.MixinNTMWaterTypes) {
                if (side == MixinEnvironment.Side.CLIENT) {
                    mixins.add("hbm.client.MixinRenderFluidTank");
                }
                mixins.add("hbm.MixinFluids");
                mixins.add("hbm.MixinRadiolysisRecipes");
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

            // Someone, fucking kill this, please, thanks
            if (ConfigMixinsLate.FUCKING_ABOMINATION) {
                mixins.add("hbm.MixinChunkRadiationHandlerSimple");
            }

        }

        return mixins;
    }

}
