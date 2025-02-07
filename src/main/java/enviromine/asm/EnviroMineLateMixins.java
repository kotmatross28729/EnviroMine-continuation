package enviromine.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

@LateMixin
public class EnviroMineLateMixins implements ILateMixinLoader {
	@Override
	public String getMixinConfig() {
		return "mixins.enviromine.late.json";
	}
	
	@Override
	public List<String> getMixins(Set<String> loadedMods) {
		List<String> mixins = new ArrayList<>();
		//mixins.add("");
		
		return mixins;
	}
		
		
}
