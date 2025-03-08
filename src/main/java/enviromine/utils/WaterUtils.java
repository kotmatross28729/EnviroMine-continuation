package enviromine.utils;

import net.minecraftforge.fluids.Fluid;

/**
 *
 * All sorts of things related to Enviromine water  
 *
 * @author kotmatross28729
 *
 */
public class WaterUtils {
    //TODO config for values
    public enum WATER_TYPES {
        RADIOACTIVE_FROSTY(-2, true, false, false, 25.0F, -0.1F),
        FROSTY(-2, false, false, false, 25.0F, -0.1F),
        
        RADIOACTIVE_COLD(-1, true, false, false, 25.0F, -0.05F),
        DIRTY_COLD(-1, false, true, false, 25.0F, -0.05F),
        SALTY_COLD(-1, false, false,true, 25.0F, -0.05F),
        CLEAN_COLD(-1, false, false, false, 25.0F, -0.05F),
        
        RADIOACTIVE(0, true, false, false, 25.0F, 0.0F),
        DIRTY(0, false, true, false, 25.0F, 0.0F),
        SALTY(0, false, false,true, 25.0F, 0.0F),
        CLEAN(0, false, false, false, 25.0F, 0.0F),
        
        RADIOACTIVE_WARM(1, true, false, false, 25.0F, 0.05F),
        DIRTY_WARM(1, false, true, false, 25.0F, 0.05F),
        SALTY_WARM(1, false, false,true, 25.0F, 0.05F),
        CLEAN_WARM(1, false, false, false, 25.0F, 0.05F),
        
        RADIOACTIVE_HOT(2, true, false, false, 25.0F, 0.1F),
        HOT(2, false, false, false, 25.0F, 0.1F);
        public final int heatIndex;
        public final boolean isRadioactive;
        public final boolean isDirty;
        public final boolean isSalty;
    
        public final float hydration;
    
        public final float temperatureInfluence;
        WATER_TYPES(int heatIndex, boolean isRadioactive, boolean isDirty, boolean isSalty, float hydration, float temperatureInfluence) {
            this.heatIndex = heatIndex;
            this.isRadioactive = isRadioactive;
            this.isDirty = isDirty;
            this.isSalty = isSalty;
            this.hydration = hydration;
            this.temperatureInfluence = temperatureInfluence;
        }
        
        public static WATER_TYPES fromTraits(WATER_TYPES waterTypeInitial, int heatIndex, boolean isRadioactive, boolean isDirty, boolean isSalty) {
            for (WATER_TYPES type : WATER_TYPES.values()) {
                if (type.heatIndex == heatIndex &&
                        type.isRadioactive == isRadioactive &&
                        type.isDirty == isDirty &&
                        type.isSalty == isSalty) {
                    return type;
                }
            }
            return waterTypeInitial;
        }
    }
    
    public static WATER_TYPES heatUp(WATER_TYPES waterType) {
        boolean isDirty = waterType.isDirty;
        boolean isSalty = waterType.isSalty;
        int heatIndex   = waterType.heatIndex;
        
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
        int heatIndex   = waterType.heatIndex;
        
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
            case "radioactive_frosty_water" -> {return WaterUtils.WATER_TYPES.RADIOACTIVE_FROSTY;}
            case "frosty_water" -> {return WaterUtils.WATER_TYPES.FROSTY;}
            
            case "radioactive_cold_water" -> {return WaterUtils.WATER_TYPES.RADIOACTIVE_COLD;}
            case "dirty_cold_water" -> {return WaterUtils.WATER_TYPES.DIRTY_COLD;}
            case "salty_cold_water" -> {return WaterUtils.WATER_TYPES.SALTY_COLD;}
            case "clean_cold_water" -> {return WaterUtils.WATER_TYPES.CLEAN_COLD;}
            
            case "radioactive_water" -> {return WaterUtils.WATER_TYPES.RADIOACTIVE;}
            case "dirty_water" -> {return WaterUtils.WATER_TYPES.DIRTY;}
            case "salty_water" -> {return WaterUtils.WATER_TYPES.SALTY;}
            
            case "radioactive_warm_water" -> {return WaterUtils.WATER_TYPES.RADIOACTIVE_WARM;}
            case "dirty_warm_water" -> {return WaterUtils.WATER_TYPES.DIRTY_WARM;}
            case "salty_warm_water" -> {return WaterUtils.WATER_TYPES.SALTY_WARM;}
            case "clean_warm_water" -> {return WaterUtils.WATER_TYPES.CLEAN_WARM;}
            
            case "radioactive_hot_water" -> {return WaterUtils.WATER_TYPES.RADIOACTIVE_HOT;}
            case "hot_water" -> {return WaterUtils.WATER_TYPES.HOT;}
            
            default -> {return WaterUtils.WATER_TYPES.CLEAN;}
        }
    }
}
