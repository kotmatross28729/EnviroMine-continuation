package enviromine.utils.misc.mixins;

import net.minecraft.entity.EntityLivingBase;

import com.hbm.util.ContaminationUtil;

public class MixinBlockCauldron_NTM {

    public static void applyRadiation(EntityLivingBase entityLivingBase, float ammount) {
        ContaminationUtil.contaminate(
            entityLivingBase,
            ContaminationUtil.HazardType.RADIATION,
            ContaminationUtil.ContaminationType.RAD_BYPASS,
            ammount);
    }
}
