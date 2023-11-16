package enviromine.gases.types;

import java.awt.Color;

import enviromine.core.EM_Settings;
import enviromine.gases.EnviroGas;
import enviromine.handlers.ObjectHandler;
import enviromine.utils.EnviroUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class GasHydrogenSulfide extends EnviroGas
{
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
		
		if(entityLiving.worldObj.isRemote || entityLiving.isEntityUndead() || (entityLiving.getEquipmentInSlot(4) != null && entityLiving.getEquipmentInSlot(4).getItem() == ObjectHandler.gasMask))
		{
			return;
		}
		
		if(
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
