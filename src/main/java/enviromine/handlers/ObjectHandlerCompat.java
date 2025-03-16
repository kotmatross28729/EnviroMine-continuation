package enviromine.handlers;

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
import enviromine.core.EnviroMine;
import enviromine.items.EnviroItemPolymerWaterBottle;
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

    /// -------------------------

    // TODO: BOB WHAT HAVE YOU DONE

    // public static FluidType RADIOACTIVE_FROSTY;
    // public static FluidType FROSTY;
    // public static FluidType RADIOACTIVE_COLD;
    // public static FluidType DIRTY_COLD;
    // public static FluidType SALTY_COLD;
    // public static FluidType CLEAN_COLD;
    // public static FluidType RADIOACTIVE;
    // public static FluidType DIRTY;
    // public static FluidType SALTY;
    // public static FluidType RADIOACTIVE_WARM;
    // public static FluidType DIRTY_WARM;
    // public static FluidType SALTY_WARM;
    // public static FluidType CLEAN_WARM;
    // public static FluidType RADIOACTIVE_HOT;
    // public static FluidType HOT;

    // public static void initFluids() {
    // String RADIOACTIVE_FROSTY_NAME = "RADIOACTIVE_FROSTY";
    // String FROSTY_NAME = "FROSTY";
    // String RADIOACTIVE_COLD_NAME = "RADIOACTIVE_COLD";
    // String DIRTY_COLD_NAME = "DIRTY_COLD";
    // String SALTY_COLD_NAME = "SALTY_COLD";
    // String CLEAN_COLD_NAME = "CLEAN_COLD";
    // String RADIOACTIVE_NAME = "RADIOACTIVE";
    // String DIRTY_NAME = "DIRTY";
    // String SALTY_NAME = "SALTY";
    // String RADIOACTIVE_WARM_NAME = "RADIOACTIVE_WARM";
    // String DIRTY_WARM_NAME = "DIRTY_WARM";
    // String SALTY_WARM_NAME = "SALTY_WARM";
    // String CLEAN_WARM_NAME = "CLEAN_WARM";
    // String RADIOACTIVE_HOT_NAME = "RADIOACTIVE_HOT";
    // String HOT_NAME = "HOT";
    //
    // int id = 1393;
    //
    // RADIOACTIVE_FROSTY = constructFluid(RADIOACTIVE_FROSTY_NAME, 3, EnumSymbol.RADIATION, id++);
    // FROSTY = constructFluid(FROSTY_NAME, 1, EnumSymbol.NONE, id++);
    // RADIOACTIVE_COLD = constructFluid(RADIOACTIVE_COLD_NAME, 2, EnumSymbol.RADIATION, id++);
    // DIRTY_COLD = constructFluid(DIRTY_COLD_NAME, 1, EnumSymbol.NONE, id++);
    // SALTY_COLD = constructFluid(SALTY_COLD_NAME, 1, EnumSymbol.NONE, id++);
    // CLEAN_COLD = constructFluid(CLEAN_COLD_NAME, 0, EnumSymbol.NONE, id++);
    // RADIOACTIVE = constructFluid(RADIOACTIVE_NAME, 2, EnumSymbol.RADIATION, id++);
    // DIRTY = constructFluid(DIRTY_NAME, 1, EnumSymbol.NONE, id++);
    // SALTY = constructFluid(SALTY_NAME, 1, EnumSymbol.NONE, id++);
    // RADIOACTIVE_WARM = constructFluid(RADIOACTIVE_WARM_NAME, 2, EnumSymbol.RADIATION, id++);
    // DIRTY_WARM = constructFluid(DIRTY_WARM_NAME, 1, EnumSymbol.NONE, id++);
    // SALTY_WARM = constructFluid(SALTY_WARM_NAME, 1, EnumSymbol.NONE, id++);
    // CLEAN_WARM = constructFluid(CLEAN_WARM_NAME, 0, EnumSymbol.NONE, id++);
    // RADIOACTIVE_HOT = constructFluid(RADIOACTIVE_HOT_NAME, 3, EnumSymbol.RADIATION, id++);
    // HOT = constructFluid(HOT_NAME, 1, EnumSymbol.NONE, id++);

    // double eff_steam_boil = 1.0D;
    // double eff_steam_heatex = 0.25D;
    //
    // RADIOACTIVE_FROSTY.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // FROSTY.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // RADIOACTIVE_COLD.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // DIRTY_COLD.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // SALTY_COLD.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // CLEAN_COLD.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // RADIOACTIVE.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // DIRTY.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // SALTY.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // RADIOACTIVE_WARM.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // DIRTY_WARM.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // SALTY_WARM.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // CLEAN_WARM.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // RADIOACTIVE_HOT.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // HOT.addTraits(
    // new FT_Heatable().setEff(FT_Heatable.HeatingType.BOILER, eff_steam_boil)
    // .setEff(FT_Heatable.HeatingType.HEATEXCHANGER, eff_steam_heatex)
    // .addStep(200, 1, STEAM, 100)
    // .addStep(220, 1, HOTSTEAM, 10)
    // .addStep(238, 1, SUPERHOTSTEAM, 1)
    // .addStep(2500, 10, ULTRAHOTSTEAM, 1));
    // }

    // public static FluidType constructFluid(String name, int poison, EnumSymbol symbol, int id) {
    // String water = "_WATER";
    //
    // return new FluidType(
    // name + water,
    // 0x3333FF,
    // poison,
    // 0,
    // 0,
    // symbol,
    // name.toLowerCase(Locale.US),
    // 16777215,
    // id,
    // I18nUtil.resolveKey("hbmfluid." + name.toLowerCase(Locale.US), new Object[0]))
    // .addTraits(LIQUID, UNSIPHONABLE);
    // }

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

        // TODO more bob

        FluidContainerRegistry.registerContainer(
            new FluidContainer(
                new ItemStack(clean_WaterBottle_polymer),
                new ItemStack(waterBottle_polymer),
                Fluids.WATER,
                500));
    }

}
