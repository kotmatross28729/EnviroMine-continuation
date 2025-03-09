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

        if (loadedMods.contains("netherlicious")) {
            mixins.add("netherlicious.MixinTorchSoul");
            mixins.add("netherlicious.MixinTorchFoxfire");
            mixins.add("netherlicious.MixinTorchShadow");
        }

        if (loadedMods.contains("cfm")) {
            mixins.add("MrCrayfishFurnitureMod.MixinTileEntityFridge");
            mixins.add("MrCrayfishFurnitureMod.MixinTileEntityFreezer");
        }

        if (loadedMods.contains("cookingforblockheads")) { // GTNH ONLY
            mixins.add("CookingForBlockheads.MixinTileFridge");
        }

        if (loadedMods.contains("hbm")) {
            mixins.add("hbm.MixinLiquefactionRecipes");
        }

        return mixins;
    }

}
