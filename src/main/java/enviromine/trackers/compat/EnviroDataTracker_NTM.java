package enviromine.trackers.compat;

import api.hbm.item.IGasMask;
import com.hbm.util.ArmorRegistry;
import com.hbm.util.ArmorUtil;
import enviromine.core.EM_Settings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class EnviroDataTracker_NTM {
	public static float handleGasMaskNTM(ItemStack helmet, boolean isCreative, EntityLivingBase trackedEntity, float airQuality){
		if (helmet != null && !isCreative) {
			if (helmet.getItem() instanceof IGasMask mask) {
				ItemStack filter = mask.getFilter(helmet, trackedEntity);
				if (filter != null) {
					if (EM_Settings.airMult > 0F && (100F - airQuality) >= EM_Settings.airMult) {
						float airToFill = 100F - airQuality;
						
						airToFill *= EM_Settings.gasMaskUpdateRestoreFraction;
						
						if (airToFill > 0F) {
							if (ArmorRegistry.hasProtection(trackedEntity, 3, ArmorRegistry.HazardClass.PARTICLE_COARSE)) {
								ArmorUtil.damageGasMaskFilter(trackedEntity, MathHelper.floor_float(airToFill*EM_Settings.HbmGasMaskBreakMultiplier));
								return airToFill;
							}
						}
					}
				}
			} else if (ArmorRegistry.hasProtection(trackedEntity, 3, ArmorRegistry.HazardClass.PARTICLE_COARSE)) {
				if (EM_Settings.airMult > 0F && (100F - airQuality) >= EM_Settings.airMult) {
					float airToFill = 100F - airQuality;
					airToFill *= EM_Settings.gasMaskUpdateRestoreFraction;
					if (airToFill > 0F) {
						ArmorUtil.damageGasMaskFilter(trackedEntity, MathHelper.floor_float(airToFill*EM_Settings.HbmGasMaskBreakMultiplier));
						return airToFill;
					}
				}
			}
		}
		return airQuality;
	}
	
	
	
	
}
