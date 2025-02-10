package enviromine.trackers.properties.compat;

import com.hbm.items.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class RotProperties_NTM {
	public static void registerFoodNTM(Configuration config, String category, String[] RPName, Item item) {
		if(item == ModItems.bio_wafer || item == ModItems.canned_conserve) {
			config.get(category, RPName[0], Item.itemRegistry.getNameForObject(item)).getString();
			config.get(category, RPName[1], -1).getInt(-1);
			config.get(category, RPName[2], "", "Set blank to rot into nothing").getString();
			config.get(category, RPName[3], 0).getInt(0);
			config.get(category, RPName[4], -1, "Set this to -1 to disable rotting on this item").getInt(-1);
		}
	}
}
