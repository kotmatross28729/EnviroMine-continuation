package enviromine.utils;

public class WaterUtils {

    public enum WATER_TYPES {
        FROSTY, //5
        DIRTY_COLD, //4
        CLEAN_COLD, //3
        SALTY, //2
        DIRTY, //1
        CLEAN, //Vanilla water bottle  //0
        CLEAN_WARM, //-1
        DIRTY_WARM, //-2
        HOT //-3
    }
    public static WATER_TYPES heatUp(WATER_TYPES waterType) {
        if(waterType == WATER_TYPES.FROSTY) {
            return WATER_TYPES.CLEAN_COLD;
        }
        else if (waterType == WATER_TYPES.CLEAN_COLD) {
            return WATER_TYPES.CLEAN;
        } else if (waterType == WATER_TYPES.DIRTY_COLD) {
            return WATER_TYPES.DIRTY;
        }
        else if (waterType == WATER_TYPES.DIRTY) {
            return WATER_TYPES.CLEAN;
        }
        else if (waterType == WATER_TYPES.CLEAN) {
            return WATER_TYPES.CLEAN_WARM;
        }
        else if (waterType == WATER_TYPES.CLEAN_WARM) {
            return WATER_TYPES.HOT;
        }
        else if (waterType == WATER_TYPES.DIRTY_WARM) {
            return WATER_TYPES.HOT;
        } else if (waterType == WATER_TYPES.HOT) {
            return WATER_TYPES.HOT;
        } else {
            return WATER_TYPES.CLEAN;
        }
    }

    public static WATER_TYPES coolDown(WATER_TYPES waterType) {
        if (waterType == WATER_TYPES.FROSTY) {
            return WATER_TYPES.FROSTY;
        }
        else if (waterType == WATER_TYPES.CLEAN_COLD) {
            return WATER_TYPES.FROSTY;
        } else if (waterType == WATER_TYPES.DIRTY_COLD) {
            return WATER_TYPES.FROSTY;
        }
        else if (waterType == WATER_TYPES.DIRTY) {
            return WATER_TYPES.DIRTY_COLD;
        }
        else if (waterType == WATER_TYPES.CLEAN) {
            return WATER_TYPES.CLEAN_COLD;
        }
        else if (waterType == WATER_TYPES.CLEAN_WARM) {
            return WATER_TYPES.CLEAN;
        }
        else if (waterType == WATER_TYPES.DIRTY_WARM) {
            return WATER_TYPES.DIRTY;
        }
        else if (waterType == WATER_TYPES.HOT) {
            return WATER_TYPES.CLEAN_WARM;
        } else {
            return WATER_TYPES.CLEAN;
        }
    }


}
