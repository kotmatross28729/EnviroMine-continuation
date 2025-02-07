package enviromine.handlers;

import com.hbm.inventory.FluidContainer;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.main.CraftingManager;
import cpw.mods.fml.common.registry.GameRegistry;
import enviromine.core.EnviroMine;
import enviromine.items.EnviroItemPolymerWaterBottle;
import enviromine.utils.WaterUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ObjectHandlerCompat {
    //FOR HBM'S NTM
    public static Item waterBottle_polymer;
    public static Item cleanWaterBottle_polymer;
    public static Item frostyWaterBottle_polymer;
    public static Item coldWaterBottle_polymer;
    public static Item badColdWaterBottle_polymer;
    public static Item saltWaterBottle_polymer;
    public static Item badWaterBottle_polymer;
    public static Item warmWaterBottle_polymer;
    public static Item badWarmWaterBottle_polymer;
    public static Item hotWaterBottle_polymer;

    public static void initItems() {
        waterBottle_polymer = new Item().setMaxStackSize(64).setUnlocalizedName("enviromine.water.polymer").setCreativeTab(EnviroMine.enviroTab).setTextureName("enviromine:bottle");

        cleanWaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.CLEAN).setMaxStackSize(1).setUnlocalizedName("enviromine.cleanwater.polymer").setCreativeTab(EnviroMine.enviroTab);
        frostyWaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.FROSTY).setMaxStackSize(1).setUnlocalizedName("enviromine.frostywater.polymer").setCreativeTab(EnviroMine.enviroTab);
        coldWaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.CLEAN_COLD).setMaxStackSize(1).setUnlocalizedName("enviromine.coldwater.polymer").setCreativeTab(EnviroMine.enviroTab);
        badColdWaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.DIRTY_COLD).setMaxStackSize(1).setUnlocalizedName("enviromine.badcoldwater.polymer").setCreativeTab(EnviroMine.enviroTab);
        saltWaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.SALTY).setMaxStackSize(1).setUnlocalizedName("enviromine.saltwater.polymer").setCreativeTab(EnviroMine.enviroTab);
        badWaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.DIRTY).setMaxStackSize(1).setUnlocalizedName("enviromine.badwater.polymer").setCreativeTab(EnviroMine.enviroTab);
        warmWaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.CLEAN_WARM).setMaxStackSize(1).setUnlocalizedName("enviromine.warmwater.polymer").setCreativeTab(EnviroMine.enviroTab);
        badWarmWaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.DIRTY_WARM).setMaxStackSize(1).setUnlocalizedName("enviromine.badwarmwater.polymer").setCreativeTab(EnviroMine.enviroTab);
        hotWaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.HOT).setMaxStackSize(1).setUnlocalizedName("enviromine.hotwater.polymer").setCreativeTab(EnviroMine.enviroTab);
    }

    public static void registerItems() {
        GameRegistry.registerItem(waterBottle_polymer, "waterPolymerBottle");
        GameRegistry.registerItem(cleanWaterBottle_polymer, "cleanWaterPolymerBottle");

        GameRegistry.registerItem(frostyWaterBottle_polymer, "frostyWaterPolymerBottle");
        GameRegistry.registerItem(coldWaterBottle_polymer, "coldWaterPolymerBottle");
        GameRegistry.registerItem(badColdWaterBottle_polymer, "badColdWaterPolymerBottle");
        GameRegistry.registerItem(saltWaterBottle_polymer, "saltWaterPolymerBottle");
        GameRegistry.registerItem(badWaterBottle_polymer, "badWaterPolymerBottle");
        GameRegistry.registerItem(warmWaterBottle_polymer, "warmWaterPolymerBottle");
        GameRegistry.registerItem(badWarmWaterBottle_polymer, "badWarmWaterPolymerBottle");
        GameRegistry.registerItem(hotWaterBottle_polymer, "hotWaterPolymerBottle");
    }

    public static ItemStack getItemStackFromWaterType(WaterUtils.WATER_TYPES type) {
        switch (type) {
            case FROSTY -> {return new ItemStack(frostyWaterBottle_polymer);}
            case DIRTY_COLD -> {return new ItemStack(badColdWaterBottle_polymer);}
            case CLEAN_COLD -> {return new ItemStack(coldWaterBottle_polymer);}
            case SALTY -> {return new ItemStack(saltWaterBottle_polymer);}
            case DIRTY -> {return new ItemStack(badWaterBottle_polymer);}
            case CLEAN_WARM -> {return new ItemStack(warmWaterBottle_polymer);}
            case DIRTY_WARM -> {return new ItemStack(badWarmWaterBottle_polymer);}
            case HOT -> {return new ItemStack(hotWaterBottle_polymer);}
            default -> {return new ItemStack(cleanWaterBottle_polymer);}
        }
    }
    public static void registerRecipes() {

        ItemStack[] bottles = {
            new ItemStack(frostyWaterBottle_polymer),
            new ItemStack(badColdWaterBottle_polymer),
            new ItemStack(coldWaterBottle_polymer),
            new ItemStack(saltWaterBottle_polymer),
            new ItemStack(badWaterBottle_polymer),
            new ItemStack(cleanWaterBottle_polymer),
            new ItemStack(warmWaterBottle_polymer),
            new ItemStack(badWarmWaterBottle_polymer),
            new ItemStack(hotWaterBottle_polymer),
        };

        //HEATING
        for(ItemStack bottle : bottles) {
            if(bottle.getItem() != hotWaterBottle_polymer) { //How can you heat already hot water?
                WaterUtils.WATER_TYPES localType = WaterUtils.WATER_TYPES.CLEAN;
                if (bottle.getItem() instanceof EnviroItemPolymerWaterBottle enviroItemPolymerWaterBottle) {
                    localType = enviroItemPolymerWaterBottle.getWaterType();
                }
                GameRegistry.addSmelting(bottle, getItemStackFromWaterType(WaterUtils.heatUp(localType)), 0.0F);
            }
        }

        //COOLING
        for(ItemStack bottle : bottles) {
            if(bottle.getItem() != frostyWaterBottle_polymer) { //Same
                WaterUtils.WATER_TYPES localType = WaterUtils.WATER_TYPES.CLEAN;
                if (bottle.getItem() instanceof EnviroItemPolymerWaterBottle enviroItemPolymerWaterBottle) {
                    localType = enviroItemPolymerWaterBottle.getWaterType();
                }
                GameRegistry.addShapelessRecipe(getItemStackFromWaterType(WaterUtils.coolDown(localType)), bottle, new ItemStack(Items.snowball, 1));
            }
        }

        GameRegistry.addShapelessRecipe(new ItemStack(badColdWaterBottle_polymer, 1, 0), new ItemStack(coldWaterBottle_polymer, 1, 0), new ItemStack(Blocks.dirt, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(badWarmWaterBottle_polymer, 1, 0), new ItemStack(warmWaterBottle_polymer, 1, 0), new ItemStack(Blocks.dirt, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(badWaterBottle_polymer, 1, 0), new ItemStack(cleanWaterBottle_polymer), new ItemStack(Blocks.dirt, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(saltWaterBottle_polymer, 1, 0), new ItemStack(cleanWaterBottle_polymer), new ItemStack(Blocks.sand, 1));


        CraftingManager.addRecipeAuto(new ItemStack(waterBottle_polymer), "p p", " p ", 'p', OreDictManager.ANY_PLASTIC.ingot());

        FluidContainerRegistry.registerContainer(new FluidContainer(new ItemStack(cleanWaterBottle_polymer), new ItemStack(waterBottle_polymer), Fluids.WATER, 500));
    }

}
