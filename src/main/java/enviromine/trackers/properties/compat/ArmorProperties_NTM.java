package enviromine.trackers.properties.compat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.config.Configuration;

import com.hbm.items.ModItems;

public class ArmorProperties_NTM {

    public static void registerArmorNTM(Configuration config, String catName, String[] APName, ItemArmor armor) {
        if (armor == ModItems.steamsuit_helmet || armor == ModItems.steamsuit_plate
            || armor == ModItems.steamsuit_legs
            || armor == ModItems.steamsuit_boots) {
            config.get(catName, APName[0], Item.itemRegistry.getNameForObject(armor))
                .getString();
            config.get(catName, APName[1], 0.0D)
                .getDouble(0.0D);
            config.get(catName, APName[2], 0.0D)
                .getDouble(0.0D);
            config.get(catName, APName[3], 0.0D)
                .getDouble(0.0D);
            config.get(catName, APName[4], 1.0D)
                .getDouble(1.0D);
            config.get(catName, APName[5], 1.0D)
                .getDouble(1.0D);
            config.get(catName, APName[6], 0.9D)
                .getDouble(0.9D);
            config.get(catName, APName[7], 0.0D)
                .getDouble(0.0D);
            config.get(catName, APName[8], 0.0D)
                .getDouble(0.0D);
            config.get(catName, APName[10], true)
                .getBoolean(true);
            config.get(catName, APName[11], false)
                .getBoolean(false);
        } else if (armor == ModItems.t45_helmet || armor == ModItems.t45_plate
            || armor == ModItems.t45_legs
            || armor == ModItems.t45_boots) {
                config.get(catName, APName[0], Item.itemRegistry.getNameForObject(armor))
                    .getString();
                config.get(catName, APName[1], 0.0D)
                    .getDouble(0.0D);
                config.get(catName, APName[2], 0.0D)
                    .getDouble(0.0D);
                config.get(catName, APName[3], 0.0D)
                    .getDouble(0.0D);
                config.get(catName, APName[4], 1.0D)
                    .getDouble(1.0D);
                config.get(catName, APName[5], 1.0D)
                    .getDouble(1.0D);
                config.get(catName, APName[6], 0.9D)
                    .getDouble(0.9D);
                config.get(catName, APName[7], 0.0D)
                    .getDouble(0.0D);
                config.get(catName, APName[8], 0.0D)
                    .getDouble(0.0D);
                config.get(catName, APName[10], true)
                    .getBoolean(true);
                config.get(catName, APName[11], false)
                    .getBoolean(false);
            } else if (armor == ModItems.ajr_helmet || armor == ModItems.ajr_plate
                || armor == ModItems.ajr_legs
                || armor == ModItems.ajr_boots) {
                    config.get(catName, APName[0], Item.itemRegistry.getNameForObject(armor))
                        .getString();
                    config.get(catName, APName[1], 0.0D)
                        .getDouble(0.0D);
                    config.get(catName, APName[2], 0.0D)
                        .getDouble(0.0D);
                    config.get(catName, APName[3], 0.0D)
                        .getDouble(0.0D);
                    config.get(catName, APName[4], 1.0D)
                        .getDouble(1.0D);
                    config.get(catName, APName[5], 1.0D)
                        .getDouble(1.0D);
                    config.get(catName, APName[6], 0.9D)
                        .getDouble(0.9D);
                    config.get(catName, APName[7], 0.0D)
                        .getDouble(0.0D);
                    config.get(catName, APName[8], 0.0D)
                        .getDouble(0.0D);
                    config.get(catName, APName[10], true)
                        .getBoolean(true);
                    config.get(catName, APName[11], true)
                        .getBoolean(true);
                } else if (armor == ModItems.ajro_helmet || armor == ModItems.ajro_plate
                    || armor == ModItems.ajro_legs
                    || armor == ModItems.ajro_boots) {
                        config.get(catName, APName[0], Item.itemRegistry.getNameForObject(armor))
                            .getString();
                        config.get(catName, APName[1], 0.0D)
                            .getDouble(0.0D);
                        config.get(catName, APName[2], 0.0D)
                            .getDouble(0.0D);
                        config.get(catName, APName[3], 0.0D)
                            .getDouble(0.0D);
                        config.get(catName, APName[4], 1.0D)
                            .getDouble(1.0D);
                        config.get(catName, APName[5], 1.0D)
                            .getDouble(1.0D);
                        config.get(catName, APName[6], 0.9D)
                            .getDouble(0.9D);
                        config.get(catName, APName[7], 0.0D)
                            .getDouble(0.0D);
                        config.get(catName, APName[8], 0.0D)
                            .getDouble(0.0D);
                        config.get(catName, APName[10], true)
                            .getBoolean(true);
                        config.get(catName, APName[11], true)
                            .getBoolean(true);
                    }

        else if (armor == ModItems.rpa_helmet || armor == ModItems.rpa_plate
            || armor == ModItems.rpa_legs
            || armor == ModItems.rpa_boots) {
                config.get(catName, APName[0], Item.itemRegistry.getNameForObject(armor))
                    .getString();
                config.get(catName, APName[1], 0.0D)
                    .getDouble(0.0D);
                config.get(catName, APName[2], 0.0D)
                    .getDouble(0.0D);
                config.get(catName, APName[3], 0.0D)
                    .getDouble(0.0D);
                config.get(catName, APName[4], 1.0D)
                    .getDouble(1.0D);
                config.get(catName, APName[5], 1.0D)
                    .getDouble(1.0D);
                config.get(catName, APName[6], 0.9D)
                    .getDouble(0.9D);
                config.get(catName, APName[7], 0.0D)
                    .getDouble(0.0D);
                config.get(catName, APName[8], 0.0D)
                    .getDouble(0.0D);
                config.get(catName, APName[10], true)
                    .getBoolean(true);
                config.get(catName, APName[11], true)
                    .getBoolean(true);
            } else if (armor == ModItems.bj_plate || armor == ModItems.bj_plate_jetpack
                || armor == ModItems.bj_legs
                || armor == ModItems.bj_boots) {
                    config.get(catName, APName[0], Item.itemRegistry.getNameForObject(armor))
                        .getString();
                    config.get(catName, APName[1], 0.0D)
                        .getDouble(0.0D);
                    config.get(catName, APName[2], 0.0D)
                        .getDouble(0.0D);
                    config.get(catName, APName[3], 0.0D)
                        .getDouble(0.0D);
                    config.get(catName, APName[4], 1.0D)
                        .getDouble(1.0D);
                    config.get(catName, APName[5], 1.0D)
                        .getDouble(1.0D);
                    config.get(catName, APName[6], 0.9D)
                        .getDouble(0.9D);
                    config.get(catName, APName[7], 0.0D)
                        .getDouble(0.0D);
                    config.get(catName, APName[8], 0.0D)
                        .getDouble(0.0D);
                    config.get(catName, APName[10], true)
                        .getBoolean(true);
                    config.get(catName, APName[11], true)
                        .getBoolean(true);
                } else if (armor == ModItems.envsuit_helmet || armor == ModItems.envsuit_plate
                    || armor == ModItems.envsuit_legs
                    || armor == ModItems.envsuit_boots) {
                        config.get(catName, APName[0], Item.itemRegistry.getNameForObject(armor))
                            .getString();
                        config.get(catName, APName[1], 0.0D)
                            .getDouble(0.0D);
                        config.get(catName, APName[2], 0.0D)
                            .getDouble(0.0D);
                        config.get(catName, APName[3], 0.0D)
                            .getDouble(0.0D);
                        config.get(catName, APName[4], 1.0D)
                            .getDouble(1.0D);
                        config.get(catName, APName[5], 1.0D)
                            .getDouble(1.0D);
                        config.get(catName, APName[6], 0.9D)
                            .getDouble(0.9D);
                        config.get(catName, APName[7], 0.0D)
                            .getDouble(0.0D);
                        config.get(catName, APName[8], 0.0D)
                            .getDouble(0.0D);
                        config.get(catName, APName[10], true)
                            .getBoolean(true);
                        config.get(catName, APName[11], true)
                            .getBoolean(true);
                    } else if (armor == ModItems.hev_helmet || armor == ModItems.hev_plate
                        || armor == ModItems.hev_legs
                        || armor == ModItems.hev_boots) {
                            config.get(catName, APName[0], Item.itemRegistry.getNameForObject(armor))
                                .getString();
                            config.get(catName, APName[1], 0.0D)
                                .getDouble(0.0D);
                            config.get(catName, APName[2], 0.0D)
                                .getDouble(0.0D);
                            config.get(catName, APName[3], 0.0D)
                                .getDouble(0.0D);
                            config.get(catName, APName[4], 1.0D)
                                .getDouble(1.0D);
                            config.get(catName, APName[5], 1.0D)
                                .getDouble(1.0D);
                            config.get(catName, APName[6], 0.9D)
                                .getDouble(0.9D);
                            config.get(catName, APName[7], 0.0D)
                                .getDouble(0.0D);
                            config.get(catName, APName[8], 0.0D)
                                .getDouble(0.0D);
                            config.get(catName, APName[10], true)
                                .getBoolean(true);
                            config.get(catName, APName[11], true)
                                .getBoolean(true);
                        } else if (armor == ModItems.fau_helmet || armor == ModItems.fau_plate
                            || armor == ModItems.fau_legs
                            || armor == ModItems.fau_boots) {
                                config.get(catName, APName[0], Item.itemRegistry.getNameForObject(armor))
                                    .getString();
                                config.get(catName, APName[1], 0.0D)
                                    .getDouble(0.0D);
                                config.get(catName, APName[2], 0.0D)
                                    .getDouble(0.0D);
                                config.get(catName, APName[3], 0.0D)
                                    .getDouble(0.0D);
                                config.get(catName, APName[4], 1.0D)
                                    .getDouble(1.0D);
                                config.get(catName, APName[5], 1.0D)
                                    .getDouble(1.0D);
                                config.get(catName, APName[6], 0.9D)
                                    .getDouble(0.9D);
                                config.get(catName, APName[7], 0.0D)
                                    .getDouble(0.0D);
                                config.get(catName, APName[8], 0.0D)
                                    .getDouble(0.0D);
                                config.get(catName, APName[10], true)
                                    .getBoolean(true);
                                config.get(catName, APName[11], true)
                                    .getBoolean(true);
                            } else if (armor == ModItems.dns_helmet || armor == ModItems.dns_plate
                                || armor == ModItems.dns_legs
                                || armor == ModItems.dns_boots) {
                                    config.get(catName, APName[0], Item.itemRegistry.getNameForObject(armor))
                                        .getString();
                                    config.get(catName, APName[1], 0.0D)
                                        .getDouble(0.0D);
                                    config.get(catName, APName[2], 0.0D)
                                        .getDouble(0.0D);
                                    config.get(catName, APName[3], 0.0D)
                                        .getDouble(0.0D);
                                    config.get(catName, APName[4], 1.0D)
                                        .getDouble(1.0D);
                                    config.get(catName, APName[5], 1.0D)
                                        .getDouble(1.0D);
                                    config.get(catName, APName[6], 0.9D)
                                        .getDouble(0.9D);
                                    config.get(catName, APName[7], 0.0D)
                                        .getDouble(0.0D);
                                    config.get(catName, APName[8], 0.0D)
                                        .getDouble(0.0D);
                                    config.get(catName, APName[10], true)
                                        .getBoolean(true);
                                    config.get(catName, APName[11], true)
                                        .getBoolean(true);
                                } else if (armor == ModItems.trenchmaster_helmet || armor == ModItems.trenchmaster_plate
                                    || armor == ModItems.trenchmaster_legs
                                    || armor == ModItems.trenchmaster_boots) {
                                        config.get(catName, APName[0], Item.itemRegistry.getNameForObject(armor))
                                            .getString();
                                        config.get(catName, APName[1], 0.0D)
                                            .getDouble(0.0D);
                                        config.get(catName, APName[2], 0.0D)
                                            .getDouble(0.0D);
                                        config.get(catName, APName[3], 0.0D)
                                            .getDouble(0.0D);
                                        config.get(catName, APName[4], 1.0D)
                                            .getDouble(1.0D);
                                        config.get(catName, APName[5], 1.0D)
                                            .getDouble(1.0D);
                                        config.get(catName, APName[6], 0.9D)
                                            .getDouble(0.9D);
                                        config.get(catName, APName[7], 0.0D)
                                            .getDouble(0.0D);
                                        config.get(catName, APName[8], 0.0D)
                                            .getDouble(0.0D);
                                        config.get(catName, APName[10], true)
                                            .getBoolean(true);
                                        config.get(catName, APName[11], true)
                                            .getBoolean(true);
                                    } else if (armor == ModItems.asbestos_helmet || armor == ModItems.asbestos_plate
                                        || armor == ModItems.asbestos_legs
                                        || armor == ModItems.asbestos_boots) {
                                            config.get(catName, APName[0], Item.itemRegistry.getNameForObject(armor))
                                                .getString();
                                            config.get(catName, APName[1], 0.0D)
                                                .getDouble(0.0D);
                                            config.get(catName, APName[2], 0.0D)
                                                .getDouble(0.0D);
                                            config.get(catName, APName[3], 0.0D)
                                                .getDouble(0.0D);
                                            config.get(catName, APName[4], 1.0D)
                                                .getDouble(1.0D);
                                            config.get(catName, APName[5], 1.0D)
                                                .getDouble(1.0D);
                                            config.get(catName, APName[6], 0.9D)
                                                .getDouble(0.9D);
                                            config.get(catName, APName[7], 0.0D)
                                                .getDouble(0.0D);
                                            config.get(catName, APName[8], 0.0D)
                                                .getDouble(0.0D);
                                            config.get(catName, APName[10], true)
                                                .getBoolean(true);
                                            config.get(catName, APName[11], true)
                                                .getBoolean(true);
                                        } else
                                        if (armor == ModItems.schrabidium_helmet || armor == ModItems.schrabidium_plate
                                            || armor == ModItems.schrabidium_legs
                                            || armor == ModItems.schrabidium_boots) {
                                                config
                                                    .get(catName, APName[0], Item.itemRegistry.getNameForObject(armor))
                                                    .getString();
                                                config.get(catName, APName[1], 0.0D)
                                                    .getDouble(0.0D);
                                                config.get(catName, APName[2], 0.0D)
                                                    .getDouble(0.0D);
                                                config.get(catName, APName[3], 0.0D)
                                                    .getDouble(0.0D);
                                                config.get(catName, APName[4], 1.0D)
                                                    .getDouble(1.0D);
                                                config.get(catName, APName[5], 1.0D)
                                                    .getDouble(1.0D);
                                                config.get(catName, APName[6], 0.9D)
                                                    .getDouble(0.9D);
                                                config.get(catName, APName[7], 0.0D)
                                                    .getDouble(0.0D);
                                                config.get(catName, APName[8], 0.0D)
                                                    .getDouble(0.0D);
                                                config.get(catName, APName[10], true)
                                                    .getBoolean(true);
                                                config.get(catName, APName[11], true)
                                                    .getBoolean(true);
                                            } else
                                            if (armor == ModItems.bismuth_helmet || armor == ModItems.bismuth_plate
                                                || armor == ModItems.bismuth_legs
                                                || armor == ModItems.bismuth_boots) {
                                                    config
                                                        .get(
                                                            catName,
                                                            APName[0],
                                                            Item.itemRegistry.getNameForObject(armor))
                                                        .getString();
                                                    config.get(catName, APName[1], 0.0D)
                                                        .getDouble(0.0D);
                                                    config.get(catName, APName[2], 0.0D)
                                                        .getDouble(0.0D);
                                                    config.get(catName, APName[3], 0.0D)
                                                        .getDouble(0.0D);
                                                    config.get(catName, APName[4], 1.0D)
                                                        .getDouble(1.0D);
                                                    config.get(catName, APName[5], 1.0D)
                                                        .getDouble(1.0D);
                                                    config.get(catName, APName[6], 0.9D)
                                                        .getDouble(0.9D);
                                                    config.get(catName, APName[7], 0.0D)
                                                        .getDouble(0.0D);
                                                    config.get(catName, APName[8], 0.0D)
                                                        .getDouble(0.0D);
                                                    config.get(catName, APName[10], true)
                                                        .getBoolean(true);
                                                    config.get(catName, APName[11], true)
                                                        .getBoolean(true);
                                                }
    }

}
