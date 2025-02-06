package enviromine.trackers.compat;

import com.hbm.dim.trait.CBT_Atmosphere;
import com.hbm.handler.ThreeInts;
import com.hbm.handler.atmosphere.AtmosphereBlob;
import com.hbm.handler.atmosphere.ChunkAtmosphereManager;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.util.ArmorUtil;
import enviromine.core.EM_Settings;
import enviromine.utils.CompatUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import java.util.List;

public class EnviroDataTracker_NTM_SPACE {
	public static float handleAirVent(EntityLivingBase trackedEntity, float airQuality) {
		CBT_Atmosphere atmosphere = ChunkAtmosphereManager.proxy.getAtmosphere(trackedEntity);
		if (!ArmorUtil.checkForOxy(trackedEntity, atmosphere)) {
			return EM_Settings.NTMSpaceAirQualityDecrease;
		}
		ThreeInts pos = new ThreeInts(MathHelper.floor_double(trackedEntity.posX), MathHelper.floor_double(trackedEntity.posY + trackedEntity.getEyeHeight()), MathHelper.floor_double(trackedEntity.posZ));
		List<AtmosphereBlob> currentBlobs = ChunkAtmosphereManager.proxy.getBlobs(trackedEntity.worldObj, pos.x, pos.y, pos.z);
		for (AtmosphereBlob blob : currentBlobs) {
			if (blob.hasFluid(Fluids.AIR, 0.19) || blob.hasFluid(Fluids.OXYGEN, 0.09)) {
				return EM_Settings.NTMSpaceAirVentAirQualityIncrease;
			}
		}
		return airQuality;
	}
	
	public static boolean getHardTempTerra(EntityLivingBase trackedEntity) {
		CBT_Atmosphere atmosphere = CompatUtils.getAtmosphere(trackedEntity.worldObj);
		return !CompatUtils.isTerraformed(atmosphere);
	}
	
	
}
