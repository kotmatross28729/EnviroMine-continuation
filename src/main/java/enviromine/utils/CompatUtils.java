package enviromine.utils;

import net.minecraft.world.World;

import com.hbm.dim.CelestialBody;
import com.hbm.dim.orbit.WorldProviderOrbit;
import com.hbm.dim.trait.CBT_Atmosphere;
import com.hbm.inventory.fluid.Fluids;

/**
 *
 * Used for NTM:Space compatibility
 *
 * @author kotmatross28729
 *
 */
public class CompatUtils {

    public static CBT_Atmosphere getAtmosphere(World world) {
        return world.provider instanceof WorldProviderOrbit ? null
            : CelestialBody.getTrait(world, CBT_Atmosphere.class);
    }

    public static boolean isTerraformed(CBT_Atmosphere atmosphere) {
        if (atmosphere != null) {
            if (atmosphere.getPressure() > 3) { // TODO: hard plank (h?)
                return false;
            }
            return atmosphere.hasFluid(Fluids.AIR, 0.21) || atmosphere.hasFluid(Fluids.OXYGEN, 0.09);
        }
        return false;
    }
}
