package enviromine.trackers.properties.compat;

import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.HazardSystem;
import com.hbm.items.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class ItemProperties_NTM {
	public static void registerItemsNTM(Configuration config, String category, String[] IPName, Item item) {
			ItemStack HazardStack = new ItemStack(item);
			float HotlevelCelc = (HazardSystem.getHazardLevelFromStack(HazardStack, HazardRegistry.HOT)) * 100F;
			float Asbestoslevel = -(HazardSystem.getHazardLevelFromStack(HazardStack, HazardRegistry.ASBESTOS));
			float Coallevel = -((HazardSystem.getHazardLevelFromStack(HazardStack, HazardRegistry.COAL)) / 2);
			float Digammalevel = -((HazardSystem.getHazardLevelFromStack(HazardStack, HazardRegistry.DIGAMMA)) * 5);
			
			if(item == ModItems.canteen_vodka || item == ModItems.canteen_fab) {
				config.get(category, IPName[0], Item.itemRegistry.getNameForObject(item)).getString();
				config.get(category, IPName[1], -1).getInt(-1);
				config.get(category, IPName[2], false).getBoolean(false);
				config.get(category, IPName[3], 0D).getDouble(0D);
				config.get(category, IPName[4], 0D).getDouble(0D);
				config.get(category, IPName[5], 0D).getDouble(0D);
				config.get(category, IPName[6], 0.3D).getDouble(0.3D);	//It's 40/60, so
				config.get(category, IPName[7], 0D).getDouble(0D);
				config.get(category, IPName[8], 30D).getDouble(30D); //guh
				config.get(category, IPName[9], 5D).getDouble(5D);
				config.get(category, IPName[10], 38.0D).getDouble(38.0D);
				config.get(category, IPName[11], 0).getInt(0);
				config.get(category, IPName[12], "").getString();
				config.get(category, IPName[13], 0).getInt(0);
			} else if(item == ModItems.mucho_mango 		   || item == ModItems.can_smart 		 		 ||
				item == ModItems.can_creature      		   || item == ModItems.can_redbomb 	 	 		 ||
				item == ModItems.can_mrsugar       		   || item == ModItems.can_overcharge 	 		 ||
		        item == ModItems.can_luna          		   || item == ModItems.can_bepis		 		 ||
				item == ModItems.can_breen	       		   ||
				item == ModItems.bottle_nuka       		   || item == ModItems.bottle_cherry 	 		 ||
				item == ModItems.bottle_quantum    		   || item == ModItems.bottle_sparkle 	 		 ||
				item == ModItems.bottle_rad    	   		   || item == ModItems.bottle2_korl	 	 		 ||
				item == ModItems.bottle2_fritz     		   || item == ModItems.bottle2_korl_special	 	 ||
				item == ModItems.bottle2_fritz_special
			) {
				config.get(category, IPName[0], Item.itemRegistry.getNameForObject(item)).getString();
				config.get(category, IPName[1], -1).getInt(-1);
				config.get(category, IPName[2], false).getBoolean(false);
				config.get(category, IPName[3], 0D).getDouble(0D);
				config.get(category, IPName[4], 0D).getDouble(0D);
				config.get(category, IPName[5], 0D).getDouble(0D);
				config.get(category, IPName[6], -0.1D).getDouble(-0.1D);
				config.get(category, IPName[7], 0D).getDouble(0D);
				config.get(category, IPName[8], 0D).getDouble(0D);
				config.get(category, IPName[9], 25D).getDouble(25D);
				config.get(category, IPName[10], 36.6D).getDouble(36.6D);
				config.get(category, IPName[11], 0).getInt(0);
				config.get(category, IPName[12], "").getString();
				config.get(category, IPName[13], 0).getInt(0);
			} else if(item == ModItems.can_mug) {
				config.get(category, IPName[0], Item.itemRegistry.getNameForObject(item)).getString();
				config.get(category, IPName[1], -1).getInt(-1);
				config.get(category, IPName[2], false).getBoolean(false);
				config.get(category, IPName[3], 0D).getDouble(0D);
				config.get(category, IPName[4], 0D).getDouble(0D);
				config.get(category, IPName[5], 0D).getDouble(0D);
				config.get(category, IPName[6], -0.15D).getDouble(-0.15D);
				config.get(category, IPName[7], 0D).getDouble(0D);
				config.get(category, IPName[8], 20D).getDouble(20D); //beer sanity boost
				config.get(category, IPName[9], 20D).getDouble(20D);
				config.get(category, IPName[10], 36.6D).getDouble(36.6D);
				config.get(category, IPName[11], 0).getInt(0);
				config.get(category, IPName[12], "").getString();
				config.get(category, IPName[13], 0).getInt(0);
			} else if(item == ModItems.coffee || item == ModItems.coffee_radium) {
				config.get(category, IPName[0], Item.itemRegistry.getNameForObject(item)).getString();
				config.get(category, IPName[1], -1).getInt(-1);
				config.get(category, IPName[2], false).getBoolean(false);
				config.get(category, IPName[3], 0D).getDouble(0D);
				config.get(category, IPName[4], 0D).getDouble(0D);
				config.get(category, IPName[5], 0D).getDouble(0D);
				config.get(category, IPName[6], item == ModItems.coffee ? 0.1D : 0.6D).getDouble(item == ModItems.coffee ? 0.1D : 0.6D);
				config.get(category, IPName[7], 0D).getDouble(0D);
				config.get(category, IPName[8], item == ModItems.coffee ? 5D : 10D).getDouble(item == ModItems.coffee ? 5D : 10D); //I know this from myself
				config.get(category, IPName[9], 25D).getDouble(25D);
				config.get(category, IPName[10], 37D).getDouble(37D);
				config.get(category, IPName[11], 0).getInt(0);
				config.get(category, IPName[12], "").getString();
				config.get(category, IPName[13], 0).getInt(0);
			} else if(item == ModItems.crackpipe || item == ModItems.cigarette) {
				config.get(category, IPName[0], Item.itemRegistry.getNameForObject(item)).getString();
				config.get(category, IPName[1], -1).getInt(-1);
				config.get(category, IPName[2], false).getBoolean(false);
				config.get(category, IPName[3], 0D).getDouble(0D);
				config.get(category, IPName[4], 0D).getDouble(0D);
				config.get(category, IPName[5], 0D).getDouble(0D);
				config.get(category, IPName[6], 0D).getDouble(0D);
				config.get(category, IPName[7], item == ModItems.crackpipe ? -1D : -10D).getDouble(item == ModItems.crackpipe ? -1D : -10D);
				config.get(category, IPName[8], item == ModItems.crackpipe ? 40D : 30D).getDouble(item == ModItems.crackpipe ? 40D : 30D);
				config.get(category, IPName[9], 0D).getDouble(0D);
				config.get(category, IPName[10], 37D).getDouble(37D);
				config.get(category, IPName[11], 0).getInt(0);
				config.get(category, IPName[12], "").getString();
				config.get(category, IPName[13], 0).getInt(0);
			} else if(item == ModItems.xanax || item == ModItems.fmn || item == ModItems.five_htp) {
				config.get(category, IPName[0], Item.itemRegistry.getNameForObject(item)).getString();
				config.get(category, IPName[1], -1).getInt(-1);
				config.get(category, IPName[2], false).getBoolean(false);
				config.get(category, IPName[3], 0D).getDouble(0D);
				config.get(category, IPName[4], 0D).getDouble(0D);
				config.get(category, IPName[5], 0D).getDouble(0D);
				config.get(category, IPName[6], 0D).getDouble(0D);
				config.get(category, IPName[7], 0D).getDouble(0D);
				config.get(category, IPName[8], item == ModItems.xanax ? 10D : item == ModItems.fmn ? 50D : 100D).getDouble(item == ModItems.xanax ? 10D : item == ModItems.fmn ? 50D : 100D);
				config.get(category, IPName[9], 0D).getDouble(0D);
				config.get(category, IPName[10], 37D).getDouble(37D);
				config.get(category, IPName[11], 0).getInt(0);
				config.get(category, IPName[12], "").getString();
				config.get(category, IPName[13], 0).getInt(0);
			} else if(HotlevelCelc != 0 || Asbestoslevel != 0 || Coallevel != 0 || Digammalevel != 0) {
				config.get(category, IPName[0], Item.itemRegistry.getNameForObject(item)).getString();
				config.get(category, IPName[1], -1).getInt(-1);
				config.get(category, IPName[2], HotlevelCelc > 0).getBoolean(HotlevelCelc > 0);
				config.get(category, IPName[3], HotlevelCelc > 0 ? HotlevelCelc : 37D).getDouble(HotlevelCelc > 0 ? HotlevelCelc : 37D);
				double aDefault = Asbestoslevel < 0 ? Asbestoslevel : Coallevel < 0 ? Coallevel : 0D;
				config.get(category, IPName[4], aDefault).getDouble(aDefault);
				config.get(category, IPName[5], Digammalevel < 0 ? Digammalevel : 0D).getDouble(Digammalevel < 0 ? Digammalevel : 0D);
				config.get(category, IPName[6], 0D).getDouble(0D);
				config.get(category, IPName[7], 0D).getDouble(0D);
				config.get(category, IPName[8], 0D).getDouble(0D);
				config.get(category, IPName[9], 0D).getDouble(0D);
				config.get(category, IPName[10], 37D).getDouble(37D);
				config.get(category, IPName[11], 0).getInt(0);
				config.get(category, IPName[12], "").getString();
				config.get(category, IPName[13], 0).getInt(0);
			}
	}
	
}
