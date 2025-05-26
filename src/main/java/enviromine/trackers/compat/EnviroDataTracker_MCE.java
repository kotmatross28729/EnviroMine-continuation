package enviromine.trackers.compat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import mekanism.common.item.ItemGasMask;
import mekanism.common.item.ItemScubaTank;

public class EnviroDataTracker_MCE {

    public static float checkMask(ItemStack helmet, boolean isCreative, EntityLivingBase trackedEntity, float airQuality) {
        if (!isCreative && helmet != null && helmet.getItem() instanceof ItemGasMask) {
            if (trackedEntity.getEquipmentInSlot(3) != null && trackedEntity.getEquipmentInSlot(3)
                .getItem() instanceof ItemScubaTank tank) {
                if (tank.getFlowing(trackedEntity.getEquipmentInSlot(3))
                    && tank.getGas(trackedEntity.getEquipmentInSlot(3)) != null) {
                    float airToFill = 100F - airQuality;
                    if (airToFill > 0F) {
                        return airToFill;
                    }
                }
            }
        }
        return 0;
    }
}
