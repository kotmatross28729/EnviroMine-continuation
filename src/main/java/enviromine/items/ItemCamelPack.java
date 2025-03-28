package enviromine.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import enviromine.trackers.EnviroDataTracker;
import enviromine.utils.TrackerUtil;
import enviromine.utils.WaterUtils;

public class ItemCamelPack extends Item {

    private float fill;
    private WaterUtils.WATER_TYPES waterType;

    public ItemCamelPack(float fill) {
        this.fill = fill;
        this.setMaxStackSize(1);
        // this.setCreativeTab(EnviroMine.enviroTab);
    }

    // TODO: NBT?
    // SETTERS
    public void setFill(ItemStack stack, float fill) {
        this.fill = fill;
    }

    public void setWaterType(ItemStack stack, WaterUtils.WATER_TYPES waterType) {
        this.waterType = waterType;
    }

    // GETTERS
    public float getFill(ItemStack stack) {
        return fill;
    }

    public WaterUtils.WATER_TYPES getWaterType(ItemStack stack) {
        return waterType;
    }

    // TODO: yes
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int p_77663_4_, boolean p_77663_5_) {
        if (worldIn.getTotalWorldTime() % 20 == 0) { // Every second
            if (this.fill > 0F) {
                if (entityIn instanceof EntityLivingBase entityLivingBase) {
                    EnviroDataTracker tracker = TrackerUtil.getTracker(entityLivingBase);
                    if (tracker != null) {
                        if (tracker.hydration < 100F) {
                            float toHydrate = 100F - tracker.hydration;
                            if (this.fill > toHydrate) {
                                tracker.hydrate(toHydrate);
                                this.fill -= toHydrate;
                            }
                        }
                    }
                }
            }
        }
    }

    // TODO

}
