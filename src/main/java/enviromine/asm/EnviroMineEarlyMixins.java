package enviromine.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name("EnviroMineEarlyMixins")
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class EnviroMineEarlyMixins implements IFMLLoadingPlugin, IEarlyMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.enviromine.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        // String configFolder = "config" + File.separator + EM_Settings.MOD_ID + File.separator;

        List<String> mixins = new ArrayList<>();

        mixins.add("MixinBlockCauldron");
        mixins.add("MixinBlockTorch");
        mixins.add("MixinBlockOre");

        mixins.add("waterCompat.MixinBlockLilyPad");
        mixins.add("waterCompat.MixinWorld");

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
