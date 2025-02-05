package enviromine.gases.types;

import java.awt.Color;

import api.hbm.item.IGasMask;
import com.hbm.util.ArmorRegistry;
import com.hbm.util.ArmorUtil;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.gases.EnviroGas;
import enviromine.handlers.ObjectHandler;
import enviromine.utils.EnviroUtils;
import enviromine.utils.misc.CompatDanger;
import mekanism.common.item.ItemGasMask;
import mekanism.common.item.ItemScubaTank;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

import static enviromine.core.EM_Settings.*;
import static enviromine.core.EnviroMine.isHbmLoaded;
import static enviromine.trackers.EnviroDataTracker.logger;

@CompatDanger
public class GasSulfurDioxide extends EnviroGas
{
	//TODO SHIT CODE
    boolean isCreative = false;
    boolean hasGasMask = false;

    boolean isHbmMask = false;
	
	public GasSulfurDioxide(String name, int ID)
	{
		super(name, ID);
		this.setColor(new Color(192, 192, 0, 0));
		this.setDensity(3F);
		this.setSuffocation(0.01F);
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
            } else {
                isCreative = false;
            }
        }

        if (entityLiving.worldObj.isRemote || entityLiving.isEntityUndead()) {
            return;
        }

        ItemStack helmet = entityLiving.getEquipmentInSlot(4);
        if (helmet != null && !isCreative && isHbmLoaded) {
            if (helmet.getItem() instanceof IGasMask mask) {  // Check if the helmet is a mask
                ItemStack filter = mask.getFilter(helmet, entityLiving);  // Get the filter of the mask
                if (filter != null) {
                    if (ArmorRegistry.hasProtection(entityLiving, 3, ArmorRegistry.HazardClass.GAS_LUNG)) {
                        hasGasMask = true;
                        isHbmMask = true;
                        // Random chance to damage the filter
                        if (entityLiving.getRNG().nextInt(Math.max(EM_Settings.HbmGasMaskBreakChanceNumber - 10, 1)) == 0) {
                            ArmorUtil.damageGasMaskFilter(entityLiving, MathHelper.floor_float(1.5F * EM_Settings.HbmGasMaskBreakMultiplier));
                        }
                    } else {
                        hasGasMask = false;
                        isHbmMask = false;
                    }
                } else {
                    hasGasMask = false;
                    isHbmMask = false;
                }
            }
            else if (ArmorRegistry.hasProtection(entityLiving, 3, ArmorRegistry.HazardClass.GAS_LUNG)) {
                hasGasMask = true;
                isHbmMask = true;
                // Random chance to damage the filter
                if (entityLiving.getRNG().nextInt(Math.max(EM_Settings.HbmGasMaskBreakChanceNumber - 10, 1)) == 0) {
                    ArmorUtil.damageGasMaskFilter(entityLiving, MathHelper.floor_float(1.5F * EM_Settings.HbmGasMaskBreakMultiplier));
                }
            }  else {
                hasGasMask = false;
                isHbmMask = false;
            }
        } else {
            hasGasMask = false;
            isHbmMask = false;
        }
		
        if (helmet != null && !isCreative && !isHbmMask) {
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
                    } else {
                        hasGasMask = false;
                    }
                } else {
                    hasGasMask = false;
                }
            } else {
                hasGasMask = false;
            }
        } else if (!isHbmMask) {
            hasGasMask = false;
        }
		
		if(!hasGasMask && helmet != null && !isCreative && EnviroMine.isMCELoaded) {
			if(helmet.getItem() instanceof ItemGasMask) {
				if(entityLiving.getEquipmentInSlot(3) != null && entityLiving.getEquipmentInSlot(3).getItem() instanceof ItemScubaTank tank)
				{
					hasGasMask = tank.getFlowing(entityLiving.getEquipmentInSlot(3)) && tank.getGas(entityLiving.getEquipmentInSlot(3)) != null;
				} else {
					hasGasMask = false;
				}
			} else {
				hasGasMask = false;
			}
		}
		
       if(SulfurDioxideGasDebugLogger) {
           logger.warn("SulfurDioxide: amplifier:  " + amplifier);
           logger.warn("SulfurDioxide: hasGasMask:  " + hasGasMask);
           logger.warn("SulfurDioxide: isCreative:  " + isCreative);
           logger.warn("SulfurDioxide: entityLiving.getRNG().nextInt(SulfurDioxidePoisoningChance) == 0?:  " + (entityLiving.getRNG().nextInt(SulfurDioxidePoisoningChance) == 0));
        }
		if(!hasGasMask && !isCreative &&
				amplifier >= SulfurDioxidePoisoningAmplifier + ((EM_Settings.witcheryVampireImmunities && entityLiving instanceof EntityPlayer) ? EnviroUtils.getWitcheryVampireLevel(entityLiving) : 0)
				&& entityLiving.getRNG().nextInt(SulfurDioxidePoisoningChance) == 0) //100
		{
			if(
					amplifier >= SulfurDioxideSeverePoisoningAmplifier + ((EM_Settings.witcheryVampireImmunities && entityLiving instanceof EntityPlayer) ? EnviroUtils.getWitcheryVampireLevel(entityLiving)*2 : 0)
					)
			{
				entityLiving.addPotionEffect(new PotionEffect(Potion.poison.id, SulfurDioxideSeverePoisoningTime, SulfurDioxideSeverePoisoningLevel));
			} else
			{
				entityLiving.addPotionEffect(new PotionEffect(Potion.poison.id, SulfurDioxidePoisoningTime, SulfurDioxidePoisoningLevel));
			}
		}
	}
}
