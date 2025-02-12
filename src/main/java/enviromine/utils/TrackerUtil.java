package enviromine.utils;

import enviromine.core.EM_Settings;
import enviromine.handlers.EM_StatusManager;
import enviromine.trackers.EnviroDataTracker;
import net.minecraft.entity.EntityLivingBase;

/**
 *
 * Helper class needed to get data outside enviromine 
 *
 * @author kotmatross28729
 *
 */
public class TrackerUtil {
	
	/**
	 * Example of use:
	 * <p>
	 * <pre>
	 * {@code
	 * 
	 * ...
	 * 
	 * 	EnviroDataTracker tracker = TrackerUtil.getTracker(entity);
	 * 	if(tracker != null) {
	 * 			float bodyTemp   = tracker.bodyTemp;
	 * 			float hydration  = tracker.hydration;
	 * 			float airQuality = tracker.airQuality;
	 * 			float sanity     = tracker.sanity;
	 * 		...
	 * 	}
	 * 
	 * ...
	 * 
	 * }
	 * </pre>
	 */
	public static EnviroDataTracker getTracker(EntityLivingBase entity) {
		EnviroDataTracker tracker = null;
		
		if (EM_Settings.enableAirQ || EM_Settings.enableBodyTemp ||
				EM_Settings.enableHydrate || EM_Settings.enableSanity) {
			tracker = EM_StatusManager.lookupTracker(entity);
		}
		return tracker;
	}
}
