package enviromine.handlers.compat;

import static enviromine.handlers.ObjectHandlerCompat.clean_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.clean_cold_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.clean_warm_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.dirty_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.dirty_cold_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.dirty_warm_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.frosty_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.getItemStackFromWaterType;
import static enviromine.handlers.ObjectHandlerCompat.hot_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.radioactive_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.radioactive_cold_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.radioactive_frosty_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.radioactive_hot_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.radioactive_warm_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.salty_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.salty_cold_WaterBottle_polymer;
import static enviromine.handlers.ObjectHandlerCompat.salty_warm_WaterBottle_polymer;

import net.minecraft.item.ItemStack;

import com.mrcrayfish.furniture.api.RecipeRegistry;

import enviromine.items.EnviroItemPolymerWaterBottle;
import enviromine.utils.WaterUtils;

public class ObjectHandler_MCF_NTM {

    public static void register() {

        ItemStack[] bottles = { new ItemStack(radioactive_frosty_WaterBottle_polymer),
            new ItemStack(frosty_WaterBottle_polymer), new ItemStack(radioactive_cold_WaterBottle_polymer),
            new ItemStack(dirty_cold_WaterBottle_polymer), new ItemStack(salty_cold_WaterBottle_polymer),
            new ItemStack(clean_cold_WaterBottle_polymer), new ItemStack(radioactive_WaterBottle_polymer),
            new ItemStack(dirty_WaterBottle_polymer), new ItemStack(salty_WaterBottle_polymer),
            new ItemStack(clean_WaterBottle_polymer), new ItemStack(radioactive_warm_WaterBottle_polymer),
            new ItemStack(dirty_warm_WaterBottle_polymer), new ItemStack(salty_warm_WaterBottle_polymer),
            new ItemStack(clean_warm_WaterBottle_polymer), new ItemStack(radioactive_hot_WaterBottle_polymer),
            new ItemStack(hot_WaterBottle_polymer), };

        // COOLING
        for (ItemStack bottle : bottles) {
            WaterUtils.WATER_TYPES localType = WaterUtils.WATER_TYPES.CLEAN;
            if (bottle.getItem() instanceof EnviroItemPolymerWaterBottle enviroItemPolymerWaterBottle) {
                localType = enviroItemPolymerWaterBottle.getWaterType();
            }
            if (WaterUtils.coolDown(localType) != localType) {
                RecipeRegistry.getInstance()
                    .registerFreezerRecipe(bottle, getItemStackFromWaterType(WaterUtils.coolDown(localType)));
            }
        }
    }
}
