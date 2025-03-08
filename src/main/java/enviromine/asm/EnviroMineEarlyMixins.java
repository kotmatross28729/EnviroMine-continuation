package enviromine.asm;

import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import enviromine.core.EM_Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@IFMLLoadingPlugin.Name("EnviroMineEarlyMixins")
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class EnviroMineEarlyMixins implements IFMLLoadingPlugin, IEarlyMixinLoader {
		@Override
		public String getMixinConfig() {
			return "mixins.enviromine.early.json";
		}
		
		@Override
		public List<String> getMixins(Set<String> loadedCoreMods) {
			//String configFolder = "config" + File.separator + EM_Settings.MOD_ID + File.separator;

			List<String> mixins = new ArrayList<>();
			
			mixins.add("MixinBlockCauldron");
			mixins.add("MixinBlockTorch");
			mixins.add("MixinBlockOre");
			
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
