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
        switch (waterType) {
            case FROSTY -> {return WATER_TYPES.CLEAN_COLD;}
            case DIRTY_COLD -> {return WATER_TYPES.DIRTY;}
            case CLEAN -> {return WATER_TYPES.CLEAN_WARM;}
            case CLEAN_WARM, DIRTY_WARM, HOT -> {return WATER_TYPES.HOT;}
            default -> {return WATER_TYPES.CLEAN;}
        }
    }

    public static WATER_TYPES coolDown(WATER_TYPES waterType) {
        switch (waterType) {
            case FROSTY, CLEAN_COLD, DIRTY_COLD -> {return WATER_TYPES.FROSTY;}
            case DIRTY -> {return WATER_TYPES.DIRTY_COLD;}
            case CLEAN -> {return WATER_TYPES.CLEAN_COLD;}
            case DIRTY_WARM -> {return WATER_TYPES.DIRTY;}
            case HOT -> {return WATER_TYPES.CLEAN_WARM;}
            default -> {return WATER_TYPES.CLEAN;}
        }
    }


}
