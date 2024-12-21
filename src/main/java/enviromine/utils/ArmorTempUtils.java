package enviromine.utils;

import enviromine.trackers.properties.ArmorProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ArmorTempUtils {

    public static boolean getTempResistance(EntityLivingBase entityLiving) {
        return checkArmorProperty(entityLiving, true);
    }

    public static boolean getTempSealing(EntityLivingBase entityLiving) {
        return checkArmorProperty(entityLiving, false);
    }

    private static boolean checkArmorProperty(EntityLivingBase entityLiving, boolean Resistance) {
        ArmorProperties helmetprops = getArmorProperties(entityLiving, 4);
        ArmorProperties plateprops = getArmorProperties(entityLiving, 3);
        ArmorProperties legsprops = getArmorProperties(entityLiving, 2);
        ArmorProperties bootsprops = getArmorProperties(entityLiving, 1);

        if (helmetprops != null && plateprops != null && legsprops != null && bootsprops != null) {
            if (Resistance) {
                return helmetprops.isTemperatureResistance && plateprops.isTemperatureResistance
                    && legsprops.isTemperatureResistance && bootsprops.isTemperatureResistance;
            } else {
                return helmetprops.isTemperatureSealed && plateprops.isTemperatureSealed
                    && legsprops.isTemperatureSealed && bootsprops.isTemperatureSealed;
            }
        }
        return false;
    }

    private static ArmorProperties getArmorProperties(EntityLivingBase entityLiving, int slot) {
        ItemStack armor = entityLiving.getEquipmentInSlot(slot);
        if (armor != null && ArmorProperties.base.hasProperty(armor)) {
            return ArmorProperties.base.getProperty(armor);
        }
        return null;
    }

}
