package enviromine.handlers;

import static enviromine.handlers.ObjectHandler.bucket_clean_cold_Water;
import static enviromine.handlers.ObjectHandler.bucket_clean_warm_Water;
import static enviromine.handlers.ObjectHandler.bucket_dirty_Water;
import static enviromine.handlers.ObjectHandler.bucket_dirty_cold_Water;
import static enviromine.handlers.ObjectHandler.bucket_dirty_warm_Water;
import static enviromine.handlers.ObjectHandler.bucket_hot_Water;
import static enviromine.handlers.ObjectHandler.bucket_radioactive_Water;
import static enviromine.handlers.ObjectHandler.bucket_radioactive_cold_Water;
import static enviromine.handlers.ObjectHandler.bucket_radioactive_hot_Water;
import static enviromine.handlers.ObjectHandler.bucket_radioactive_warm_Water;
import static enviromine.handlers.ObjectHandler.bucket_salty_Water;
import static enviromine.handlers.ObjectHandler.bucket_salty_cold_Water;
import static enviromine.handlers.ObjectHandler.bucket_salty_warm_Water;
import static enviromine.handlers.ObjectHandler.clean_cold_WaterBottle;
import static enviromine.handlers.ObjectHandler.clean_warm_WaterBottle;
import static enviromine.handlers.ObjectHandler.dirty_WaterBottle;
import static enviromine.handlers.ObjectHandler.dirty_cold_WaterBottle;
import static enviromine.handlers.ObjectHandler.dirty_warm_WaterBottle;
import static enviromine.handlers.ObjectHandler.frosty_WaterBottle;
import static enviromine.handlers.ObjectHandler.hot_WaterBottle;
import static enviromine.handlers.ObjectHandler.radioactive_WaterBottle;
import static enviromine.handlers.ObjectHandler.radioactive_cold_WaterBottle;
import static enviromine.handlers.ObjectHandler.radioactive_frosty_WaterBottle;
import static enviromine.handlers.ObjectHandler.radioactive_hot_WaterBottle;
import static enviromine.handlers.ObjectHandler.radioactive_warm_WaterBottle;
import static enviromine.handlers.ObjectHandler.salty_WaterBottle;
import static enviromine.handlers.ObjectHandler.salty_cold_WaterBottle;
import static enviromine.handlers.ObjectHandler.salty_warm_WaterBottle;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.hbm.inventory.FluidContainer;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.main.CraftingManager;

import cpw.mods.fml.common.registry.GameRegistry;
import enviromine.blocks.water.BlockEnviroMineWater;
import enviromine.core.EnviroMine;
import enviromine.items.EnviroItemPolymerWaterBottle;
import enviromine.items.EnviroItemWaterBottle;
import enviromine.items.ItemModBucket;
import enviromine.utils.WaterUtils;

public class ObjectHandlerCompat {

    // FOR HBM'S NTM
    public static Item waterBottle_polymer;

    public static Item radioactive_frosty_WaterBottle_polymer;
    public static Item frosty_WaterBottle_polymer;

    public static Item radioactive_cold_WaterBottle_polymer;
    public static Item dirty_cold_WaterBottle_polymer;
    public static Item salty_cold_WaterBottle_polymer;
    public static Item clean_cold_WaterBottle_polymer;

    public static Item radioactive_WaterBottle_polymer;
    public static Item dirty_WaterBottle_polymer;
    public static Item salty_WaterBottle_polymer;
    public static Item clean_WaterBottle_polymer;

    public static Item radioactive_warm_WaterBottle_polymer;
    public static Item dirty_warm_WaterBottle_polymer;
    public static Item salty_warm_WaterBottle_polymer;
    public static Item clean_warm_WaterBottle_polymer;

    public static Item radioactive_hot_WaterBottle_polymer;
    public static Item hot_WaterBottle_polymer;

    public static void initItems() {
        waterBottle_polymer = new Item().setMaxStackSize(64)
            .setUnlocalizedName("enviromine.water.polymer")
            .setCreativeTab(EnviroMine.enviroTab)
            .setTextureName("enviromine:bottle");

        // NAMES
        // [trait]_[temp]_WaterBottle_polymer

        radioactive_frosty_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(
            WaterUtils.WATER_TYPES.RADIOACTIVE_FROSTY).setMaxStackSize(1)
                .setUnlocalizedName("enviromine.radioactive_frosty_WaterBottle.polymer")
                .setCreativeTab(EnviroMine.enviroTab);
        frosty_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.FROSTY).setMaxStackSize(1)
            .setUnlocalizedName("enviromine.frosty_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);

        radioactive_cold_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.RADIOACTIVE_COLD)
            .setMaxStackSize(1)
            .setUnlocalizedName("enviromine.radioactive_cold_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);
        dirty_cold_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.DIRTY_COLD)
            .setMaxStackSize(1)
            .setUnlocalizedName("enviromine.dirty_cold_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);
        salty_cold_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.SALTY_COLD)
            .setMaxStackSize(1)
            .setUnlocalizedName("enviromine.salty_cold_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);
        clean_cold_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.CLEAN_COLD)
            .setMaxStackSize(1)
            .setUnlocalizedName("enviromine.clean_cold_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);

        radioactive_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.RADIOACTIVE)
            .setMaxStackSize(1)
            .setUnlocalizedName("enviromine.radioactive_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);
        dirty_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.DIRTY).setMaxStackSize(1)
            .setUnlocalizedName("enviromine.dirty_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);
        salty_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.SALTY).setMaxStackSize(1)
            .setUnlocalizedName("enviromine.salty_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);
        clean_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.CLEAN).setMaxStackSize(1)
            .setUnlocalizedName("enviromine.clean_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);

        radioactive_warm_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.RADIOACTIVE_WARM)
            .setMaxStackSize(1)
            .setUnlocalizedName("enviromine.radioactive_warm_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);
        dirty_warm_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.DIRTY_WARM)
            .setMaxStackSize(1)
            .setUnlocalizedName("enviromine.dirty_warm_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);
        salty_warm_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.SALTY_WARM)
            .setMaxStackSize(1)
            .setUnlocalizedName("enviromine.salty_warm_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);
        clean_warm_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.CLEAN_WARM)
            .setMaxStackSize(1)
            .setUnlocalizedName("enviromine.clean_warm_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);

        radioactive_hot_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.RADIOACTIVE_HOT)
            .setMaxStackSize(1)
            .setUnlocalizedName("enviromine.radioactive_hot_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);
        hot_WaterBottle_polymer = new EnviroItemPolymerWaterBottle(WaterUtils.WATER_TYPES.HOT).setMaxStackSize(1)
            .setUnlocalizedName("enviromine.hot_WaterBottle.polymer")
            .setCreativeTab(EnviroMine.enviroTab);
    }

    public static void registerItems() {
        GameRegistry.registerItem(waterBottle_polymer, "waterPolymerBottle");

        GameRegistry.registerItem(radioactive_frosty_WaterBottle_polymer, "radioactive_frosty_WaterBottle_polymer");
        GameRegistry.registerItem(frosty_WaterBottle_polymer, "frosty_WaterBottle_polymer");

        GameRegistry.registerItem(radioactive_cold_WaterBottle_polymer, "radioactive_cold_WaterBottle_polymer");
        GameRegistry.registerItem(dirty_cold_WaterBottle_polymer, "dirty_cold_WaterBottle_polymer");
        GameRegistry.registerItem(salty_cold_WaterBottle_polymer, "salty_cold_WaterBottle_polymer");
        GameRegistry.registerItem(clean_cold_WaterBottle_polymer, "clean_cold_WaterBottle_polymer");

        GameRegistry.registerItem(radioactive_WaterBottle_polymer, "radioactive_WaterBottle_polymer");
        GameRegistry.registerItem(dirty_WaterBottle_polymer, "dirty_WaterBottle_polymer");
        GameRegistry.registerItem(salty_WaterBottle_polymer, "salty_WaterBottle_polymer");
        GameRegistry.registerItem(clean_WaterBottle_polymer, "clean_WaterBottle_polymer");

        GameRegistry.registerItem(radioactive_warm_WaterBottle_polymer, "radioactive_warm_WaterBottle_polymer");
        GameRegistry.registerItem(dirty_warm_WaterBottle_polymer, "dirty_warm_WaterBottle_polymer");
        GameRegistry.registerItem(salty_warm_WaterBottle_polymer, "salty_warm_WaterBottle_polymer");
        GameRegistry.registerItem(clean_warm_WaterBottle_polymer, "clean_warm_WaterBottle_polymer");

        GameRegistry.registerItem(radioactive_hot_WaterBottle_polymer, "radioactive_hot_WaterBottle_polymer");
        GameRegistry.registerItem(hot_WaterBottle_polymer, "hot_WaterBottle_polymer");
    }

    public static ItemStack getItemStackFromWaterType(WaterUtils.WATER_TYPES type) {
        switch (type) {

            case RADIOACTIVE_FROSTY -> {
                return new ItemStack(radioactive_frosty_WaterBottle_polymer);
            }
            case FROSTY -> {
                return new ItemStack(frosty_WaterBottle_polymer);
            }

            case RADIOACTIVE_COLD -> {
                return new ItemStack(radioactive_cold_WaterBottle_polymer);
            }
            case DIRTY_COLD -> {
                return new ItemStack(dirty_cold_WaterBottle_polymer);
            }
            case SALTY_COLD -> {
                return new ItemStack(salty_cold_WaterBottle_polymer);
            }
            case CLEAN_COLD -> {
                return new ItemStack(clean_cold_WaterBottle_polymer);
            }

            case RADIOACTIVE -> {
                return new ItemStack(radioactive_WaterBottle_polymer);
            }
            case DIRTY -> {
                return new ItemStack(dirty_WaterBottle_polymer);
            }
            case SALTY -> {
                return new ItemStack(salty_WaterBottle_polymer);
            }

            case RADIOACTIVE_WARM -> {
                return new ItemStack(radioactive_warm_WaterBottle_polymer);
            }
            case DIRTY_WARM -> {
                return new ItemStack(dirty_warm_WaterBottle_polymer);
            }
            case SALTY_WARM -> {
                return new ItemStack(salty_warm_WaterBottle_polymer);
            }
            case CLEAN_WARM -> {
                return new ItemStack(clean_warm_WaterBottle_polymer);
            }

            case RADIOACTIVE_HOT -> {
                return new ItemStack(radioactive_hot_WaterBottle_polymer);
            }
            case HOT -> {
                return new ItemStack(hot_WaterBottle_polymer);
            }

            default -> {
                return new ItemStack(clean_WaterBottle_polymer);
            }
        }
    }

    // AUTOGEN GOD
    // ↑, not autogen actually
    // ↑, shut up
    public static void registerRecipes() {

        ItemStack[] bottles = { new ItemStack(radioactive_frosty_WaterBottle_polymer),
            new ItemStack(frosty_WaterBottle_polymer), new ItemStack(radioactive_cold_WaterBottle_polymer),
            new ItemStack(dirty_cold_WaterBottle_polymer), new ItemStack(salty_cold_WaterBottle_polymer),
            new ItemStack(clean_cold_WaterBottle_polymer), new ItemStack(radioactive_WaterBottle_polymer),
            new ItemStack(dirty_WaterBottle_polymer), new ItemStack(salty_WaterBottle_polymer),
            new ItemStack(clean_WaterBottle_polymer), new ItemStack(radioactive_warm_WaterBottle_polymer),
            new ItemStack(dirty_warm_WaterBottle_polymer), new ItemStack(salty_warm_WaterBottle_polymer),
            new ItemStack(clean_warm_WaterBottle_polymer), new ItemStack(radioactive_hot_WaterBottle_polymer),
            new ItemStack(hot_WaterBottle_polymer), };

        // HEATING
        for (ItemStack bottle : bottles) {
            WaterUtils.WATER_TYPES localType = WaterUtils.WATER_TYPES.CLEAN;
            if (bottle.getItem() instanceof EnviroItemPolymerWaterBottle enviroItemPolymerWaterBottle) {
                localType = enviroItemPolymerWaterBottle.getWaterType();
            }
            if (WaterUtils.heatUp(localType) != localType) {
                GameRegistry.addSmelting(bottle, getItemStackFromWaterType(WaterUtils.heatUp(localType)), 0.0F);
            }
        }

        // COOLING
        for (ItemStack bottle : bottles) {
            WaterUtils.WATER_TYPES localType = WaterUtils.WATER_TYPES.CLEAN;
            if (bottle.getItem() instanceof EnviroItemPolymerWaterBottle enviroItemPolymerWaterBottle) {
                localType = enviroItemPolymerWaterBottle.getWaterType();
            }
            if (WaterUtils.coolDown(localType) != localType) {
                GameRegistry.addShapelessRecipe(
                    getItemStackFromWaterType(WaterUtils.coolDown(localType)),
                    bottle,
                    new ItemStack(Items.snowball, 1));
            }
        }
        // SALTING
        for (ItemStack bottle : bottles) {
            WaterUtils.WATER_TYPES localType = WaterUtils.WATER_TYPES.CLEAN;
            if (bottle.getItem() instanceof EnviroItemPolymerWaterBottle enviroItemPolymerWaterBottle) {
                localType = enviroItemPolymerWaterBottle.getWaterType();
            }
            if (WaterUtils.saltDown(localType) != localType) {
                GameRegistry.addShapelessRecipe(
                    getItemStackFromWaterType(WaterUtils.saltDown(localType)),
                    bottle,
                    new ItemStack(Blocks.sand, 1));
                GameRegistry.addRecipe(
                    new ShapelessOreRecipe(
                        getItemStackFromWaterType(WaterUtils.saltDown(localType)),
                        bottle,
                        "dustSalt"));
            }
        }
        // POLLUTING or whatever is this
        for (ItemStack bottle : bottles) {
            WaterUtils.WATER_TYPES localType = WaterUtils.WATER_TYPES.CLEAN;
            if (bottle.getItem() instanceof EnviroItemPolymerWaterBottle enviroItemPolymerWaterBottle) {
                localType = enviroItemPolymerWaterBottle.getWaterType();
            }
            if (WaterUtils.pollute(localType) != localType) {
                GameRegistry.addShapelessRecipe(
                    getItemStackFromWaterType(WaterUtils.pollute(localType)),
                    bottle,
                    new ItemStack(Blocks.dirt, 1));
            }
        }

        GameRegistry.addShapelessRecipe(
            new ItemStack(clean_WaterBottle_polymer),
            new ItemStack(waterBottle_polymer),
            new ItemStack(Items.snowball));

        CraftingManager
            .addRecipeAuto(new ItemStack(waterBottle_polymer), "p p", " p ", 'p', OreDictManager.ANY_PLASTIC.ingot());
    }

    public static void registerNTMFluidContainers() {
        ItemStack[] bottles = { new ItemStack(radioactive_frosty_WaterBottle), new ItemStack(frosty_WaterBottle),
            new ItemStack(radioactive_cold_WaterBottle), new ItemStack(dirty_cold_WaterBottle),
            new ItemStack(salty_cold_WaterBottle), new ItemStack(clean_cold_WaterBottle),

            new ItemStack(radioactive_WaterBottle), new ItemStack(dirty_WaterBottle), new ItemStack(salty_WaterBottle),
            new ItemStack(Items.potionitem, 1, 0),

            new ItemStack(radioactive_warm_WaterBottle), new ItemStack(dirty_warm_WaterBottle),
            new ItemStack(salty_warm_WaterBottle), new ItemStack(clean_warm_WaterBottle),

            new ItemStack(radioactive_hot_WaterBottle), new ItemStack(hot_WaterBottle), };
        ItemStack[] bottles_polymer = { new ItemStack(radioactive_frosty_WaterBottle_polymer),
            new ItemStack(frosty_WaterBottle_polymer), new ItemStack(radioactive_cold_WaterBottle_polymer),
            new ItemStack(dirty_cold_WaterBottle_polymer), new ItemStack(salty_cold_WaterBottle_polymer),
            new ItemStack(clean_cold_WaterBottle_polymer), new ItemStack(radioactive_WaterBottle_polymer),
            new ItemStack(dirty_WaterBottle_polymer), new ItemStack(salty_WaterBottle_polymer),
            new ItemStack(clean_WaterBottle_polymer), new ItemStack(radioactive_warm_WaterBottle_polymer),
            new ItemStack(dirty_warm_WaterBottle_polymer), new ItemStack(salty_warm_WaterBottle_polymer),
            new ItemStack(clean_warm_WaterBottle_polymer), new ItemStack(radioactive_hot_WaterBottle_polymer),
            new ItemStack(hot_WaterBottle_polymer), };

        ItemStack[] buckets = { new ItemStack(bucket_radioactive_cold_Water), new ItemStack(bucket_dirty_cold_Water),
            new ItemStack(bucket_salty_cold_Water), new ItemStack(bucket_clean_cold_Water),
            new ItemStack(bucket_radioactive_Water), new ItemStack(bucket_dirty_Water),
            new ItemStack(bucket_salty_Water), new ItemStack(bucket_radioactive_warm_Water),
            new ItemStack(bucket_dirty_warm_Water), new ItemStack(bucket_salty_warm_Water),
            new ItemStack(bucket_clean_warm_Water), new ItemStack(bucket_radioactive_hot_Water),
            new ItemStack(bucket_hot_Water), };

        for (ItemStack bottle : bottles) {
            if (bottle.getItem() instanceof EnviroItemWaterBottle enviroItemWaterBottle) {
                FluidContainerRegistry.registerContainer(
                    new FluidContainer(
                        bottle,
                        new ItemStack(Items.glass_bottle),
                        Fluids.fromName(WaterUtils.getStringFromTypeNTMFluid(enviroItemWaterBottle.getWaterType())),
                        250));
            }
        }
        for (ItemStack bottle_polymer : bottles_polymer) {
            if (bottle_polymer.getItem() instanceof EnviroItemPolymerWaterBottle enviroItemPolymerWaterBottle) {
                FluidContainerRegistry.registerContainer(
                    new FluidContainer(
                        bottle_polymer,
                        new ItemStack(waterBottle_polymer),
                        Fluids.fromName(
                            WaterUtils.getStringFromTypeNTMFluid(enviroItemPolymerWaterBottle.getWaterType())),
                        500));
            }
        }
        for (ItemStack bucket : buckets) {
            if (bucket.getItem() instanceof ItemModBucket modBucket) {
                if (modBucket.containedFluid instanceof BlockEnviroMineWater enviroMineWater) {
                    FluidContainerRegistry.registerContainer(
                        new FluidContainer(
                            bucket,
                            new ItemStack(Items.bucket),
                            Fluids.fromName(
                                WaterUtils.getStringFromTypeNTMFluid(
                                    WaterUtils.getTypeFromFluid(enviroMineWater.getFluid()))),
                            1000));
                }
            }
        }
    }

}
