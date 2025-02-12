package enviromine.trackers.properties.compat;

import com.pam.harvestcraft.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class ItemProperties_HarvestCraft {
	
	/**
	
	public static void registerItemsHarvestCraft(Configuration config, String category, String[] IPName, Item item) {
		//TODO 25
		if (
				item == ItemRegistry.teaItem        || item == ItemRegistry.coffeeItem 			|| item == ItemRegistry.applejuiceItem ||
				item == ItemRegistry.melonjuiceItem || item == ItemRegistry.melonsmoothieItem   || item == ItemRegistry.carrotjuiceItem               ||
				item == ItemRegistry. || item == ItemRegistry. || item == ItemRegistry. ||
				item == ItemRegistry. || item == ItemRegistry. || item == ItemRegistry. ||
		) {
			config.get(category, IPName[0], Item.itemRegistry.getNameForObject(item)).getString();
			config.get(category, IPName[1], -1).getInt(-1);
			config.get(category, IPName[2], false).getBoolean(false);
			config.get(category, IPName[3], 0D).getDouble(0D);
			config.get(category, IPName[4], 0D).getDouble(0D);
			config.get(category, IPName[5], 0D).getDouble(0D);
			config.get(category, IPName[6],
					
					(item == ItemRegistry.teaItem || item == ItemRegistry.coffeeItem) ? 0.1D :
					(item == ItemRegistry.melonsmoothieItem) ? -0.1D :
							0.0D)
					
					.getDouble(
							(item == ItemRegistry.teaItem || item == ItemRegistry.coffeeItem) ? 0.1D :
							(item == ItemRegistry.melonsmoothieItem) ? -0.1D :
									0.0D
					);
			config.get(category, IPName[7], 0D).getDouble(0D);
			config.get(category, IPName[8], 0D).getDouble(0D);
			config.get(category, IPName[9], 25D).getDouble(25D);
			config.get(category, IPName[10], 36.6D).getDouble(36.6D);
			config.get(category, IPName[11], 0).getInt(0);
			config.get(category, IPName[12], "").getString();
			config.get(category, IPName[13], 0).getInt(0);
		}
		//TODO 10
		else if (
				item == ItemRegistry.cucumberItem   || item == ItemRegistry.lettuceItem  || item == ItemRegistry.bellpepperItem   ||
				item == ItemRegistry.tomatoItem     || item == ItemRegistry.coconutItem  || item == ItemRegistry.dragonfruitItem  ||
				item == ItemRegistry.grapefruitItem || item == ItemRegistry.icecreamItem || item == ItemRegistry. 				  ||
				item == ItemRegistry. || item == ItemRegistry. || item == ItemRegistry. 									 ||
		) {
			config.get(category, IPName[0], Item.itemRegistry.getNameForObject(item)).getString();
			config.get(category, IPName[1], -1).getInt(-1);
			config.get(category, IPName[2], false).getBoolean(false);
			config.get(category, IPName[3], 0D).getDouble(0D);
			config.get(category, IPName[4], 0D).getDouble(0D);
			config.get(category, IPName[5], 0D).getDouble(0D);
			config.get(category, IPName[6], 
					
					item == ItemRegistry.icecreamItem ? -0.1 : 0.0D)
					
					.getDouble(
							item == ItemRegistry.icecreamItem ? -0.1 : 0.0D
					);
			config.get(category, IPName[7], 0D).getDouble(0D);
			config.get(category, IPName[8], 0D).getDouble(0D);
			config.get(category, IPName[9], 10D).getDouble(10D);
			config.get(category, IPName[10], 36.6D).getDouble(36.6D);
			config.get(category, IPName[11], 0).getInt(0);
			config.get(category, IPName[12], "").getString();
			config.get(category, IPName[13], 0).getInt(0);
		}
		//TODO 5
		else if (
				item == ItemRegistry.cabbageItem 	 	   || item == ItemRegistry.okraItem 	  || item == ItemRegistry.durianItem			 ||
				item == ItemRegistry.mangoItem 		 	   || item == ItemRegistry.peachItem 	  || item == ItemRegistry.pearItem 				 ||
				item == ItemRegistry.pomegranateItem 	   || item == ItemRegistry.starfruitItem  || item == ItemRegistry.applesauceItem 		 ||
				item == ItemRegistry.pumpkinsoupItem 	   || item == ItemRegistry.carrotsoupItem || item == ItemRegistry.potatosoupItem 		 ||
				item == ItemRegistry.chickennoodlesoupItem || item == ItemRegistry.potroastItem   || item == ItemRegistry.spidereyesoupItem		 ||
				item == ItemRegistry.hotchocolateItem      || item == ItemRegistry.   || item == ItemRegistry.		 ||
		) {
			config.get(category, IPName[0], Item.itemRegistry.getNameForObject(item)).getString();
			config.get(category, IPName[1], -1).getInt(-1);
			config.get(category, IPName[2], false).getBoolean(false);
			config.get(category, IPName[3], 0D).getDouble(0D);
			config.get(category, IPName[4], 0D).getDouble(0D);
			config.get(category, IPName[5], 0D).getDouble(0D);
			config.get(category, IPName[6],
					
					item == ItemRegistry.hotchocolateItem ? 0.1 :
							
							0.0D)
					
					.getDouble(
							item == ItemRegistry.hotchocolateItem ? 0.1 : 0.0D
					);
			config.get(category, IPName[7], 0D).getDouble(0D);
			config.get(category, IPName[8], 0D).getDouble(0D);
			config.get(category, IPName[9], 5D).getDouble(5D);
			config.get(category, IPName[10], 36.6D).getDouble(36.6D);
			config.get(category, IPName[11], 0).getInt(0);
			config.get(category, IPName[12], "").getString();
			config.get(category, IPName[13], 0).getInt(0);
		}
		//TODO 2
		else if (
				item == ItemRegistry.apricotItem || item == ItemRegistry.lemonItem || item == ItemRegistry.limeItem					 ||
				item == ItemRegistry.maplesyrupItem || item == ItemRegistry.orangeItem || item == ItemRegistry.papayaItem 			 ||
				item == ItemRegistry.persimmonItem || item == ItemRegistry.plumItem || item == ItemRegistry. 						 ||
				item == ItemRegistry. || item == ItemRegistry. || item == ItemRegistry. 									 		 ||
		
		) {
			config.get(category, IPName[0], Item.itemRegistry.getNameForObject(item)).getString();
			config.get(category, IPName[1], -1).getInt(-1);
			config.get(category, IPName[2], false).getBoolean(false);
			config.get(category, IPName[3], 0D).getDouble(0D);
			config.get(category, IPName[4], 0D).getDouble(0D);
			config.get(category, IPName[5], 0D).getDouble(0D);
			config.get(category, IPName[6], 0.0D).getDouble(0.0D);
			config.get(category, IPName[7], 0D).getDouble(0D);
			config.get(category, IPName[8], 0D).getDouble(0D);
			config.get(category, IPName[9], 2D).getDouble(2D);
			config.get(category, IPName[10], 36.6D).getDouble(36.6D);
			config.get(category, IPName[11], 0).getInt(0);
			config.get(category, IPName[12], "").getString();
			config.get(category, IPName[13], 0).getInt(0);
		}
	}
	
	*/
	
	
	
	
}

