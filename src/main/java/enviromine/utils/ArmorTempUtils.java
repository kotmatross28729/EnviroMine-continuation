package enviromine.utils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import enviromine.trackers.properties.ArmorProperties;

/**
 *
 * Used for checking armor temperature resistance
 *
 * @author kotmatross28729
 *
 */
public class ArmorTempUtils {

    // Average resistance (fire level)
    public static boolean getTempResistance(EntityLivingBase entityLiving) {
        return checkArmorProperty(entityLiving, true);
    }

    // High resistance (lava level)
    public static boolean getTempSealing(EntityLivingBase entityLiving) {
        return checkArmorProperty(entityLiving, false);
    }

    public static boolean checkArmorPropertyItemStack(ItemStack stack, boolean Resistance) {
        ArmorProperties props = getArmorProperties(stack);
        if (props != null) {
            if (Resistance) {
                return props.isTemperatureResistance;
            } else {
                return props.isTemperatureSealed;
            }
        }
        return false;
    }

    private static boolean checkArmorProperty(EntityLivingBase entityLiving, boolean Resistance) {
        ArmorProperties helmetprops = getArmorProperties(entityLiving, 4);
        ArmorProperties plateprops = getArmorProperties(entityLiving, 3);
        ArmorProperties legsprops = getArmorProperties(entityLiving, 2);
        ArmorProperties bootsprops = getArmorProperties(entityLiving, 1);

        if (helmetprops != null && plateprops != null && legsprops != null && bootsprops != null) {
            if (Resistance) {
                return helmetprops.isTemperatureResistance && plateprops.isTemperatureResistance
                    && legsprops.isTemperatureResistance
                    && bootsprops.isTemperatureResistance;
            } else {
                return helmetprops.isTemperatureSealed && plateprops.isTemperatureSealed
                    && legsprops.isTemperatureSealed
                    && bootsprops.isTemperatureSealed;
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

    private static ArmorProperties getArmorProperties(ItemStack stack) {
        if (stack != null && ArmorProperties.base.hasProperty(stack)) {
            return ArmorProperties.base.getProperty(stack);
        }
        return null;
    }
}
