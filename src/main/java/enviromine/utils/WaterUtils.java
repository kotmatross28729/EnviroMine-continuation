package enviromine.utils;

import net.minecraft.block.BlockLiquid;
import net.minecraftforge.fluids.BlockFluidClassic;

/**
 *
 * All sorts of things related to Enviromine water  
 *
 * @author kotmatross28729
 *
 */
public class WaterUtils {
    public enum WATER_TYPES {
        RADIOACTIVE_FROSTY(-2, true, false, false),
        FROSTY(-2, false, false, false),
        
        RADIOACTIVE_COLD(-1, true, false, false),
        DIRTY_COLD(-1, false, true, false),
        SALTY_COLD(-1, false, false,true),
        CLEAN_COLD(-1, false, false, false),
        
        RADIOACTIVE(0, true, false, false),
        DIRTY(0, false, true, false),
        SALTY(0, false, false,true),
        CLEAN(0, false, false, false),
        
        RADIOACTIVE_WARM(1, true, false, false),
        DIRTY_WARM(1, false, true, false),
        SALTY_WARM(1, false, false,true),
        CLEAN_WARM(1, false, false, false),
        
        RADIOACTIVE_HOT(2, true, false, false),
        HOT(2, false, false, false);
        public final int heatIndex;
        public final boolean isRadioactive;
        public final boolean isDirty;
        public final boolean isSalty;
        WATER_TYPES(int heatIndex, boolean isRadioactive, boolean isDirty, boolean isSalty) {
            this.heatIndex = heatIndex;
            this.isRadioactive = isRadioactive;
            this.isDirty = isDirty;
            this.isSalty = isSalty;
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
}
