package enviromine.items.compat;

import com.hbm.util.ContaminationUtil;
import net.minecraft.entity.player.EntityPlayer;

public class EnviroItemWaterBottle_NTM {
	
	public static void applyRadiation(EntityPlayer par3EntityPlayer, float ammount) {
		ContaminationUtil.contaminate(par3EntityPlayer, ContaminationUtil.HazardType.RADIATION, ContaminationUtil.ContaminationType.RAD_BYPASS, ammount);
	}
	
}
