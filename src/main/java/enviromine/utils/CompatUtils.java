package enviromine.utils;

import com.hbm.dim.CelestialBody;
import com.hbm.dim.orbit.WorldProviderOrbit;
import com.hbm.dim.trait.CBT_Atmosphere;
import com.hbm.inventory.fluid.Fluids;
import net.minecraft.world.World;

/**
 *
 * Used for NTM:Space compatibility 
 *
 * @author kotmatross28729
 *
 */
public class CompatUtils {
	public static CBT_Atmosphere getAtmosphere(World world) {
		return world.provider instanceof WorldProviderOrbit ? null : CelestialBody.getTrait(world, CBT_Atmosphere.class);
	}
	public static boolean isTerraformed(CBT_Atmosphere atmosphere) {
		if(atmosphere != null) {
			//TODO: Make it use common sense. 
			// In its current state, you can pump 0.21 bar of air into Eve's atmosphere and the temperature will magically drop from 460 to 30
			// Maybe `CBT_Atmosphere.getPressure()` ?
			
			
			return atmosphere.hasFluid(Fluids.AIR, 0.21) || atmosphere.hasFluid(Fluids.OXYGEN, 0.09);
		}
		return false;
	}
}

