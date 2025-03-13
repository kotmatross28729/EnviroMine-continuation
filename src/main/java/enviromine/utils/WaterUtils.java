package enviromine.utils;

import net.minecraftforge.fluids.Fluid;

import enviromine.core.EM_Settings;

/**
 *
 * All sorts of things related to Enviromine water
 *
 * @author kotmatross28729
 *
 */
public class WaterUtils {

    public enum WATER_TYPES {

        RADIOACTIVE_FROSTY(-2, true, false, false, EM_Settings.RADIOACTIVE_FROSTY_Hydration,
            EM_Settings.RADIOACTIVE_FROSTY_TemperatureInfluence),
        FROSTY(-2, false, false, false, EM_Settings.FROSTY_Hydration, EM_Settings.FROSTY_TemperatureInfluence),

        RADIOACTIVE_COLD(-1, true, false, false, EM_Settings.RADIOACTIVE_COLD_Hydration,
            EM_Settings.RADIOACTIVE_COLD_TemperatureInfluence),
        DIRTY_COLD(-1, false, true, false, EM_Settings.DIRTY_COLD_Hydration,
            EM_Settings.DIRTY_COLD_TemperatureInfluence),
        SALTY_COLD(-1, false, false, true, EM_Settings.SALTY_COLD_Hydration,
            EM_Settings.SALTY_COLD_TemperatureInfluence),
        CLEAN_COLD(-1, false, false, false, EM_Settings.CLEAN_COLD_Hydration,
            EM_Settings.CLEAN_COLD_TemperatureInfluence),

        RADIOACTIVE(0, true, false, false, EM_Settings.RADIOACTIVE_Hydration,
            EM_Settings.RADIOACTIVE_TemperatureInfluence),
        DIRTY(0, false, true, false, EM_Settings.DIRTY_Hydration, EM_Settings.DIRTY_TemperatureInfluence),
        SALTY(0, false, false, true, EM_Settings.SALTY_Hydration, EM_Settings.SALTY_TemperatureInfluence),
        CLEAN(0, false, false, false, EM_Settings.CLEAN_Hydration, EM_Settings.CLEAN_TemperatureInfluence),

        RADIOACTIVE_WARM(1, true, false, false, EM_Settings.RADIOACTIVE_WARM_Hydration,
            EM_Settings.RADIOACTIVE_WARM_TemperatureInfluence),
        DIRTY_WARM(1, false, true, false, EM_Settings.DIRTY_WARM_Hydration,
            EM_Settings.DIRTY_WARM_TemperatureInfluence),
        SALTY_WARM(1, false, false, true, EM_Settings.SALTY_WARM_Hydration,
            EM_Settings.SALTY_WARM_TemperatureInfluence),
        CLEAN_WARM(1, false, false, false, EM_Settings.CLEAN_WARM_Hydration,
            EM_Settings.CLEAN_WARM_TemperatureInfluence),

        RADIOACTIVE_HOT(2, true, false, false, EM_Settings.RADIOACTIVE_HOT_Hydration,
            EM_Settings.RADIOACTIVE_HOT_TemperatureInfluence),
        HOT(2, false, false, false, EM_Settings.HOT_Hydration, EM_Settings.HOT_TemperatureInfluence);

        public final int heatIndex;
        public final boolean isRadioactive;
        public final boolean isDirty;
        public final boolean isSalty;

        public final float hydration;
        public final float temperatureInfluence;

        WATER_TYPES(int heatIndex, boolean isRadioactive, boolean isDirty, boolean isSalty, float hydration,
            float temperatureInfluence) {
            this.heatIndex = heatIndex;
            this.isRadioactive = isRadioactive;
            this.isDirty = isDirty;
            this.isSalty = isSalty;
            this.hydration = hydration;
            this.temperatureInfluence = temperatureInfluence;
        }

        public static WATER_TYPES fromTraits(WATER_TYPES waterTypeInitial, int heatIndex, boolean isRadioactive,
            boolean isDirty, boolean isSalty) {
            for (WATER_TYPES type : WATER_TYPES.values()) {
                if (type.heatIndex == heatIndex && type.isRadioactive == isRadioactive
                    && type.isDirty == isDirty
                    && type.isSalty == isSalty) {
                    return type;
                }
            }
            return waterTypeInitial;
        }
    }

    public static WATER_TYPES heatUp(WATER_TYPES waterType) {
        boolean isDirty = waterType.isDirty;
        boolean isSalty = waterType.isSalty;
        int heatIndex = waterType.heatIndex;

        if (heatIndex >= 0) {
            isDirty = false;
            isSalty = false;
        }

        if (heatIndex < 2) {
            heatIndex += 1;
        }

        return WATER_TYPES.fromTraits(waterType, heatIndex, waterType.isRadioactive, isDirty, isSalty);
    }

    public static WATER_TYPES coolDown(WATER_TYPES waterType) {
        boolean isDirty = waterType.isDirty;
        boolean isSalty = waterType.isSalty;
        int heatIndex = waterType.heatIndex;

        if (heatIndex < 0) {
            isDirty = false;
            isSalty = false;
        }

        if (heatIndex > -2) {
            heatIndex -= 1;
        }

        return WATER_TYPES.fromTraits(waterType, heatIndex, waterType.isRadioactive, isDirty, isSalty);
    }

    public static WATER_TYPES saltDown(WATER_TYPES waterType) {
        return WATER_TYPES.fromTraits(waterType, waterType.heatIndex, waterType.isRadioactive, waterType.isDirty, true);
    }

    public static WATER_TYPES pollute(WATER_TYPES waterType) {
        return WATER_TYPES.fromTraits(waterType, waterType.heatIndex, waterType.isRadioactive, true, waterType.isSalty);
    }

    public static WaterUtils.WATER_TYPES getTypeFromFluid(Fluid fluid) {
        switch (fluid.getName()) {
            case "radioactive_frosty_water" -> {
                return WaterUtils.WATER_TYPES.RADIOACTIVE_FROSTY;
            }
            case "frosty_water" -> {
                return WaterUtils.WATER_TYPES.FROSTY;
            }

            case "radioactive_cold_water" -> {
                return WaterUtils.WATER_TYPES.RADIOACTIVE_COLD;
            }
            case "dirty_cold_water" -> {
                return WaterUtils.WATER_TYPES.DIRTY_COLD;
            }
            case "salty_cold_water" -> {
                return WaterUtils.WATER_TYPES.SALTY_COLD;
            }
            case "clean_cold_water" -> {
                return WaterUtils.WATER_TYPES.CLEAN_COLD;
            }

            case "radioactive_water" -> {
                return WaterUtils.WATER_TYPES.RADIOACTIVE;
            }
            case "dirty_water" -> {
                return WaterUtils.WATER_TYPES.DIRTY;
            }
            case "salty_water" -> {
                return WaterUtils.WATER_TYPES.SALTY;
            }

            case "radioactive_warm_water" -> {
                return WaterUtils.WATER_TYPES.RADIOACTIVE_WARM;
            }
            case "dirty_warm_water" -> {
                return WaterUtils.WATER_TYPES.DIRTY_WARM;
            }
            case "salty_warm_water" -> {
                return WaterUtils.WATER_TYPES.SALTY_WARM;
            }
            case "clean_warm_water" -> {
                return WaterUtils.WATER_TYPES.CLEAN_WARM;
            }

            case "radioactive_hot_water" -> {
                return WaterUtils.WATER_TYPES.RADIOACTIVE_HOT;
            }
            case "hot_water" -> {
                return WaterUtils.WATER_TYPES.HOT;
            }

            default -> {
                return WaterUtils.WATER_TYPES.CLEAN;
            }
        }
    }

    public static WaterUtils.WATER_TYPES getTypeFromString(String type) {
        return switch (type) {
            case "RADIOACTIVE_FROSTY" -> WaterUtils.WATER_TYPES.RADIOACTIVE_FROSTY;
            case "FROSTY" -> WaterUtils.WATER_TYPES.FROSTY;
            case "RADIOACTIVE_COLD" -> WaterUtils.WATER_TYPES.RADIOACTIVE_COLD;
            case "DIRTY_COLD" -> WaterUtils.WATER_TYPES.DIRTY_COLD;
            case "SALTY_COLD" -> WaterUtils.WATER_TYPES.SALTY_COLD;
            case "CLEAN_COLD" -> WaterUtils.WATER_TYPES.CLEAN_COLD;
            case "RADIOACTIVE" -> WaterUtils.WATER_TYPES.RADIOACTIVE;
            case "DIRTY" -> WaterUtils.WATER_TYPES.DIRTY;
            case "SALTY" -> WaterUtils.WATER_TYPES.SALTY;
            case "RADIOACTIVE_WARM" -> WaterUtils.WATER_TYPES.RADIOACTIVE_WARM;
            case "DIRTY_WARM" -> WaterUtils.WATER_TYPES.DIRTY_WARM;
            case "SALTY_WARM" -> WaterUtils.WATER_TYPES.SALTY_WARM;
            case "CLEAN_WARM" -> WaterUtils.WATER_TYPES.CLEAN_WARM;
            case "RADIOACTIVE_HOT" -> WaterUtils.WATER_TYPES.RADIOACTIVE_HOT;
            case "HOT" -> WaterUtils.WATER_TYPES.HOT;
            default -> WaterUtils.WATER_TYPES.CLEAN;
        };
    }

}
