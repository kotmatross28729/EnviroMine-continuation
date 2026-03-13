package enviromine.asm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.launchwrapper.Launch;

import org.apache.logging.log4j.LogManager;

import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import enviromine.core.EM_Settings;
import enviromine.core.config.mixins.ConfigMixinsEarly;

@IFMLLoadingPlugin.Name("EnviroMineEarlyMixins")
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class EnviroMineEarlyMixins implements IFMLLoadingPlugin, IEarlyMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.enviromine.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        LogManager.getLogger()
            .info("Starting EnviroMine engine...");
        String configFolder = "config" + File.separator + EM_Settings.MOD_ID + File.separator;
        ConfigMixinsEarly.init(new File(Launch.minecraftHome, configFolder + "mixinsEarly.cfg"));

        List<String> mixins = new ArrayList<>();

        if (ConfigMixinsEarly.MixinBlockCauldron) mixins.add("MixinBlockCauldron");
        if (ConfigMixinsEarly.MixinBlockTorch) mixins.add("MixinBlockTorch");
        if (ConfigMixinsEarly.MixinBlockOre) mixins.add("MixinBlockOre");

        if (ConfigMixinsEarly.MixinBlockLilyPad) mixins.add("waterCompat.MixinBlockLilyPad");
        if (ConfigMixinsEarly.MixinWorld) mixins.add("waterCompat.MixinWorld");

        return mixins;
    }

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
