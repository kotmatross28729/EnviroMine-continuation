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
            EM_Settings.RADIOACTIVE_FROSTY_TemperatureInfluence, EM_Settings.RADIOACTIVE_FROSTY_TempInfluenceCap),
        FROSTY(-2, false, false, false, EM_Settings.FROSTY_Hydration, EM_Settings.FROSTY_TemperatureInfluence,
            EM_Settings.FROSTY_TempInfluenceCap),

        RADIOACTIVE_COLD(-1, true, false, false, EM_Settings.RADIOACTIVE_COLD_Hydration,
            EM_Settings.RADIOACTIVE_COLD_TemperatureInfluence, EM_Settings.RADIOACTIVE_COLD_TempInfluenceCap),
        DIRTY_COLD(-1, false, true, false, EM_Settings.DIRTY_COLD_Hydration,
            EM_Settings.DIRTY_COLD_TemperatureInfluence, EM_Settings.DIRTY_COLD_TempInfluenceCap),
        SALTY_COLD(-1, false, false, true, EM_Settings.SALTY_COLD_Hydration,
            EM_Settings.SALTY_COLD_TemperatureInfluence, EM_Settings.SALTY_COLD_TempInfluenceCap),
        CLEAN_COLD(-1, false, false, false, EM_Settings.CLEAN_COLD_Hydration,
            EM_Settings.CLEAN_COLD_TemperatureInfluence, EM_Settings.CLEAN_COLD_TempInfluenceCap),

        RADIOACTIVE(0, true, false, false, EM_Settings.RADIOACTIVE_Hydration,
            EM_Settings.RADIOACTIVE_TemperatureInfluence, EM_Settings.RADIOACTIVE_TempInfluenceCap),
        DIRTY(0, false, true, false, EM_Settings.DIRTY_Hydration, EM_Settings.DIRTY_TemperatureInfluence,
            EM_Settings.DIRTY_TempInfluenceCap),
        SALTY(0, false, false, true, EM_Settings.SALTY_Hydration, EM_Settings.SALTY_TemperatureInfluence,
            EM_Settings.SALTY_TempInfluenceCap),
        CLEAN(0, false, false, false, EM_Settings.CLEAN_Hydration, EM_Settings.CLEAN_TemperatureInfluence,
            EM_Settings.CLEAN_TempInfluenceCap),

        RADIOACTIVE_WARM(1, true, false, false, EM_Settings.RADIOACTIVE_WARM_Hydration,
            EM_Settings.RADIOACTIVE_WARM_TemperatureInfluence, EM_Settings.RADIOACTIVE_WARM_TempInfluenceCap),
        DIRTY_WARM(1, false, true, false, EM_Settings.DIRTY_WARM_Hydration, EM_Settings.DIRTY_WARM_TemperatureInfluence,
            EM_Settings.DIRTY_WARM_TempInfluenceCap),
        SALTY_WARM(1, false, false, true, EM_Settings.SALTY_WARM_Hydration, EM_Settings.SALTY_WARM_TemperatureInfluence,
            EM_Settings.SALTY_WARM_TempInfluenceCap),
        CLEAN_WARM(1, false, false, false, EM_Settings.CLEAN_WARM_Hydration,
            EM_Settings.CLEAN_WARM_TemperatureInfluence, EM_Settings.CLEAN_WARM_TempInfluenceCap),

        RADIOACTIVE_HOT(2, true, false, false, EM_Settings.RADIOACTIVE_HOT_Hydration,
            EM_Settings.RADIOACTIVE_HOT_TemperatureInfluence, EM_Settings.RADIOACTIVE_HOT_TempInfluenceCap),
        HOT(2, false, false, false, EM_Settings.HOT_Hydration, EM_Settings.HOT_TemperatureInfluence,
            EM_Settings.HOT_TempInfluenceCap);

        public final int heatIndex;
        public final boolean isRadioactive;
        public final boolean isDirty;
        public final boolean isSalty;

        public final float hydration;
        public final float temperatureInfluence;
        public final float temperatureInfluenceCap;

        WATER_TYPES(int heatIndex, boolean isRadioactive, boolean isDirty, boolean isSalty, float hydration,
            float temperatureInfluence, float temperatureInfluenceCap) {
            this.heatIndex = heatIndex;
            this.isRadioactive = isRadioactive;
            this.isDirty = isDirty;
            this.isSalty = isSalty;
            this.hydration = hydration;
            this.temperatureInfluence = temperatureInfluence;
            this.temperatureInfluenceCap = temperatureInfluenceCap;
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
        } else {
            heatIndex = 2;
        }

        return WATER_TYPES.fromTraits(waterType, heatIndex, waterType.isRadioactive, isDirty, isSalty);
    }

    public static WATER_TYPES heatUp(WATER_TYPES waterType, int iterations) {
        boolean isDirty = waterType.isDirty;
        boolean isSalty = waterType.isSalty;
        int heatIndex = waterType.heatIndex;

        if (heatIndex >= 0) {
            isDirty = false;
            isSalty = false;
        }

        if ((heatIndex + iterations) < 2) {
            heatIndex += iterations;
        } else {
            heatIndex = 2;
        }

        if (heatIndex == 2) {
            isDirty = false;
            isSalty = false;
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
        } else {
            heatIndex = -2;
        }

        return WATER_TYPES.fromTraits(waterType, heatIndex, waterType.isRadioactive, isDirty, isSalty);
    }

    public static WATER_TYPES coolDown(WATER_TYPES waterType, int iterations) {
        boolean isDirty = waterType.isDirty;
        boolean isSalty = waterType.isSalty;
        int heatIndex = waterType.heatIndex;

        if (heatIndex < 0) {
            isDirty = false;
            isSalty = false;
        }

        if ((heatIndex - iterations) > -2) {
            heatIndex -= iterations;
        } else {
            heatIndex = -2;
        }

        if (heatIndex == -2) {
            isDirty = false;
            isSalty = false;
        }

        return WATER_TYPES.fromTraits(waterType, heatIndex, waterType.isRadioactive, isDirty, isSalty);
    }

    public static WATER_TYPES saltDown(WATER_TYPES waterType) {
        return WATER_TYPES.fromTraits(waterType, waterType.heatIndex, waterType.isRadioactive, waterType.isDirty, true);
    }

    public static WATER_TYPES forceSaltDown(WATER_TYPES waterType) {
        int heatIndex = (waterType.heatIndex == 2 ? 1 : waterType.heatIndex == -2 ? -1 : waterType.heatIndex);

        return WATER_TYPES.fromTraits(waterType, heatIndex, false, false, true);
    }

    public static WATER_TYPES pollute(WATER_TYPES waterType) {
        return WATER_TYPES.fromTraits(waterType, waterType.heatIndex, waterType.isRadioactive, true, waterType.isSalty);
    }

    public static WATER_TYPES forcePollute(WATER_TYPES waterType) {
        int heatIndex = (waterType.heatIndex == 2 ? 1 : waterType.heatIndex == -2 ? -1 : waterType.heatIndex);

        return WATER_TYPES.fromTraits(waterType, heatIndex, false, true, false);
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

    public static String getStringFromType(WaterUtils.WATER_TYPES type) {
        return switch (type) {
            case RADIOACTIVE_FROSTY -> "RADIOACTIVE_FROSTY";
            case FROSTY -> "FROSTY";
            case RADIOACTIVE_COLD -> "RADIOACTIVE_COLD";
            case DIRTY_COLD -> "DIRTY_COLD";
            case SALTY_COLD -> "SALTY_COLD";
            case CLEAN_COLD -> "CLEAN_COLD";
            case RADIOACTIVE -> "RADIOACTIVE";
            case DIRTY -> "DIRTY";
            case SALTY -> "SALTY";
            case RADIOACTIVE_WARM -> "RADIOACTIVE_WARM";
            case DIRTY_WARM -> "DIRTY_WARM";
            case SALTY_WARM -> "SALTY_WARM";
            case CLEAN_WARM -> "CLEAN_WARM";
            case RADIOACTIVE_HOT -> "RADIOACTIVE_HOT";
            case HOT -> "HOT";
            default -> "CLEAN";
        };
    }

}
