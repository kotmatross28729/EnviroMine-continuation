package enviromine.handlers.compat;

import static enviromine.handlers.ObjectHandler.clean_cold_WaterBottle;
import static enviromine.handlers.ObjectHandler.clean_warm_WaterBottle;
import static enviromine.handlers.ObjectHandler.dirty_WaterBottle;
import static enviromine.handlers.ObjectHandler.dirty_cold_WaterBottle;
import static enviromine.handlers.ObjectHandler.dirty_warm_WaterBottle;
import static enviromine.handlers.ObjectHandler.frosty_WaterBottle;
import static enviromine.handlers.ObjectHandler.getItemStackFromWaterType;
import static enviromine.handlers.ObjectHandler.hot_WaterBottle;
import static enviromine.handlers.ObjectHandler.radioactive_WaterBottle;
import static enviromine.handlers.ObjectHandler.radioactive_cold_WaterBottle;
import static enviromine.handlers.ObjectHandler.radioactive_frosty_WaterBottle;
import static enviromine.handlers.ObjectHandler.radioactive_hot_WaterBottle;
import static enviromine.handlers.ObjectHandler.radioactive_warm_WaterBottle;
import static enviromine.handlers.ObjectHandler.salty_WaterBottle;
import static enviromine.handlers.ObjectHandler.salty_cold_WaterBottle;
import static enviromine.handlers.ObjectHandler.salty_warm_WaterBottle;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.mrcrayfish.furniture.api.RecipeRegistry;

import enviromine.items.EnviroItemWaterBottle;
import enviromine.utils.WaterUtils;

public class ObjectHandler_MCF {

    public static void register() {
        ItemStack[] bottles = { new ItemStack(radioactive_frosty_WaterBottle), new ItemStack(frosty_WaterBottle),

            new ItemStack(radioactive_cold_WaterBottle), new ItemStack(dirty_cold_WaterBottle),
            new ItemStack(salty_cold_WaterBottle), new ItemStack(clean_cold_WaterBottle),

            new ItemStack(radioactive_WaterBottle), new ItemStack(dirty_WaterBottle), new ItemStack(salty_WaterBottle),
            new ItemStack(Items.potionitem, 1, 0),

            new ItemStack(radioactive_warm_WaterBottle), new ItemStack(dirty_warm_WaterBottle),
            new ItemStack(salty_warm_WaterBottle), new ItemStack(clean_warm_WaterBottle),

            new ItemStack(radioactive_hot_WaterBottle), new ItemStack(hot_WaterBottle), };

        // COOLING
        for (ItemStack bottle : bottles) {
            WaterUtils.WATER_TYPES localType = WaterUtils.WATER_TYPES.CLEAN;
            if (bottle.equals(new ItemStack(Items.potionitem, 1, 0))) {
                localType = WaterUtils.WATER_TYPES.CLEAN;
            } else if (bottle.getItem() instanceof EnviroItemWaterBottle enviroItemWaterBottle) {
                localType = enviroItemWaterBottle.getWaterType();
            }
            if (WaterUtils.coolDown(localType) != localType) {
                RecipeRegistry.getInstance()
                    .registerFreezerRecipe(bottle, getItemStackFromWaterType(WaterUtils.coolDown(localType)));
            }
        }
    }
}
