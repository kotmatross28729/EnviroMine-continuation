package enviromine.gases.types;

import java.awt.Color;

import api.hbm.item.IGasMask;
import com.hbm.util.ArmorUtil;
import enviromine.core.EM_Settings;
import enviromine.gases.EnviroGas;
import enviromine.handlers.ObjectHandler;
import enviromine.utils.EnviroUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;


import static enviromine.trackers.EnviroDataTracker.isHbmLoaded;

public class GasHydrogenSulfide extends EnviroGas
{
    boolean isCreative = false;

	public GasHydrogenSulfide(String name, int ID)
	{
		super(name, ID);
		this.setColor(new Color(255, 255, 0, 64));
		this.setDensity(1F);
		this.setVolitility(100F, 0F, 1F);
	}

	@Override
	public void applyEffects(EntityLivingBase entityLiving, int amplifier)
	{
		super.applyEffects(entityLiving, amplifier);

        if(entityLiving instanceof EntityPlayer)
        {
            if(((EntityPlayer)entityLiving).capabilities.isCreativeMode)
            {
                isCreative = true;
            }
        }

        if (entityLiving.worldObj.isRemote || entityLiving.isEntityUndead()) {
            return;
        }
        boolean hasGasMask = false;
        ItemStack helmet = entityLiving.getEquipmentInSlot(4);
        if (helmet != null && !isCreative && isHbmLoaded()) {
            if (helmet.getItem() instanceof IGasMask mask) {  // Check if the helmet is a mask
                ItemStack filter = mask.getFilter(helmet, entityLiving);  // Get the filter of the mask
                if (filter != null) {
                    hasGasMask = true;
                    // Random chance to damage the filter
                    if (entityLiving.getRNG().nextInt(Math.max(EM_Settings.HbmGasMaskBreakChanceNumber - 10, 1)) == 0) {
                        ArmorUtil.damageGasMaskFilter(entityLiving, MathHelper.floor_float(1.5F * EM_Settings.HbmGasMaskBreakMultiplier));
                    }
                }
            }
        }
        if (helmet != null && !isCreative) {
            if (entityLiving.getEquipmentInSlot(4).getItem() == ObjectHandler.gasMask ) {  // Check if the helmet is a mask
                if (helmet.hasTagCompound() && helmet.getTagCompound().hasKey(EM_Settings.GAS_MASK_FILL_TAG_KEY)) {
                    NBTTagCompound tag = helmet.getTagCompound();
                    int maskFill = tag.getInteger(EM_Settings.GAS_MASK_FILL_TAG_KEY);
                    if (maskFill > 0) {
                        hasGasMask = true;
                        if (entityLiving.ticksExisted % 20 == 0) {  // Every second
                            int newMaskQuality = maskFill - MathHelper.floor_float(1.0f * EM_Settings.EnviromineGasMaskBreakMultiplier); //should be 10, I think
                            if (newMaskQuality < 0) {newMaskQuality = 0;}  // Sanitize to ensure we do not have negative fill
                            tag.setInteger(EM_Settings.GAS_MASK_FILL_TAG_KEY, newMaskQuality);
                        }
                    }
                }
            }
        }

		if(!hasGasMask && !isCreative &&
				amplifier >= 5 + ((EM_Settings.witcheryVampireImmunities && entityLiving instanceof EntityPlayer) ? EnviroUtils.getWitcheryVampireLevel(entityLiving) : 0)
				&& entityLiving.getRNG().nextInt(100) == 0)
		{
			if(
					amplifier >= 10 + ((EM_Settings.witcheryVampireImmunities && entityLiving instanceof EntityPlayer) ? EnviroUtils.getWitcheryVampireLevel(entityLiving)*2 : 0)
					)
			{
				entityLiving.addPotionEffect(new PotionEffect(Potion.poison.id, 600));
			} else
			{
				entityLiving.addPotionEffect(new PotionEffect(Potion.poison.id, 200));
			}
		}
	}
}
