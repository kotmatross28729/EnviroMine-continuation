package enviromine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.client.gui.EM_GuiFakeDeath;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.handlers.EM_StatusManager;
import enviromine.handlers.EnviroAchievements;
import enviromine.trackers.EnviroDataTracker;
import enviromine.utils.EnviroUtils;
import enviromine.utils.RenderAssist;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class EnviroPotion extends Potion
{
	public static EnviroPotion hypothermia;
	public static EnviroPotion heatstroke;
	public static EnviroPotion frostbite;
	public static EnviroPotion dehydration;
	public static EnviroPotion insanity;

	public static ResourceLocation textureResource = new ResourceLocation("enviromine", "textures/gui/status_Gui.png");

	public EnviroPotion(int par1, boolean par2, int par3)
	{
		super(par1, par2, par3);
	}

	public static void RegisterPotions()
	{
		EnviroPotion.frostbite = ((EnviroPotion)new EnviroPotion(EM_Settings.frostBitePotionID, true, 8171462).setPotionName("potion.enviromine.frostbite")).setIconIndex(0, 0);
		EnviroPotion.dehydration = ((EnviroPotion)new EnviroPotion(EM_Settings.dehydratePotionID, true, 3035801).setPotionName("potion.enviromine.dehydration")).setIconIndex(1, 0);
		EnviroPotion.insanity = ((EnviroPotion)new EnviroPotion(EM_Settings.insanityPotionID, true, 5578058).setPotionName("potion.enviromine.insanity")).setIconIndex(2, 0);
		EnviroPotion.heatstroke = ((EnviroPotion)new EnviroPotion(EM_Settings.heatstrokePotionID, true, RenderAssist.getColorFromRGBA(255, 0, 0, 255)).setPotionName("potion.enviromine.heatstroke")).setIconIndex(3, 0);
		EnviroPotion.hypothermia = ((EnviroPotion)new EnviroPotion(EM_Settings.hypothermiaPotionID, true, 8171462).setPotionName("potion.enviromine.hypothermia")).setIconIndex(4, 0);
	}

	public static void checkAndApplyEffects(EntityLivingBase entityLiving)
	{
		if(entityLiving.worldObj.isRemote)
		{
			return;
		}

		EnviroDataTracker tracker = EM_StatusManager.lookupTracker(entityLiving);

		boolean isVampire = entityLiving instanceof EntityPlayer && EnviroUtils.isPlayerAVampire((EntityPlayer)entityLiving);
//		boolean isWerewolf = entityLiving instanceof EntityPlayer && EnviroUtils.isPlayerAWerewolf((EntityPlayer)entityLiving);
		boolean isCurrentlyAndroid = entityLiving instanceof EntityPlayer && EnviroUtils.isPlayerCurrentlyMOAndroid((EntityPlayer)entityLiving);
		boolean isCurrentlyWerewolf = entityLiving instanceof EntityPlayer && EnviroUtils.isPlayerCurrentlyWitcheryWerewolf((EntityPlayer)entityLiving);
		boolean isCurrentlyWolf = entityLiving instanceof EntityPlayer && EnviroUtils.isPlayerCurrentlyWitcheryWolf((EntityPlayer)entityLiving);
		int vampireLevel = entityLiving instanceof EntityPlayer ? EnviroUtils.getWitcheryVampireLevel((EntityPlayer)entityLiving) : 0;
		int werewolfLevel = entityLiving instanceof EntityPlayer ? EnviroUtils.getWitcheryWerewolfLevel((EntityPlayer)entityLiving) : 0;

		int vampireDuration = MathHelper.clamp_int(200 - (entityLiving instanceof EntityPlayer && EM_Settings.witcheryVampireImmunities ? vampireLevel : 0)*15, 0, 200);
		int werewolfDuration = MathHelper.clamp_int(200 - (entityLiving instanceof EntityPlayer && EM_Settings.witcheryWerewolfImmunities ? werewolfLevel : 0)*15, 0, 200);

		// === Heatstroke === //
		if(entityLiving.isPotionActive(heatstroke))
		{
			PotionEffect effect = entityLiving.getActivePotionEffect(heatstroke);

			// Remove effect if it's worn off
			if (effect.getDuration() <= 0)
			{
				entityLiving.removePotionEffect(heatstroke.id);
			}
			// Don't inflict further effects if the stat in question is not tracked
			else if(
					EM_Settings.enableBodyTemp
					&& (
							!EM_Settings.dimensionProperties.containsKey(entityLiving.dimension)
									|| (EM_Settings.dimensionProperties.containsKey(entityLiving.dimension)
											&& EM_Settings.dimensionProperties.get(entityLiving.dimension).trackTemp)
							)
					&& !(
							EM_Settings.matterOverdriveAndroidImmunities
							&& isCurrentlyAndroid
							)
				)
			{
				if(effect.getAmplifier() >= 2 && entityLiving.getRNG().nextInt(2) == 0)
				{
					entityLiving.attackEntityFrom(EnviroDamageSource.heatstroke, 4.0F);
				}

				if(effect.getAmplifier() >= 1)
				{
					entityLiving.addPotionEffect(new PotionEffect(Potion.weakness.id, 200, 0));
					entityLiving.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 200, 0));
					entityLiving.addPotionEffect(new PotionEffect(Potion.hunger.id, 200, 0));

					if(entityLiving.getRNG().nextInt(10) == 0)
					{
						if(EM_Settings.noNausea)
						{
							entityLiving.addPotionEffect(new PotionEffect(Potion.blindness.id, 200, 0));
						} else
						{
							entityLiving.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
						}
					}
				}
			}
		}
		// === Hypothermia === //
		if(entityLiving.isPotionActive(hypothermia))
		{
			PotionEffect effect = entityLiving.getActivePotionEffect(hypothermia);

			// Remove effect if it's worn off
			if (effect.getDuration() <= 0)
			{
				entityLiving.removePotionEffect(hypothermia.id);
			}
			// Don't inflict further effects if the stat in question is not tracked
			else if(
					EM_Settings.enableBodyTemp
					&& (
							!EM_Settings.dimensionProperties.containsKey(entityLiving.dimension)
									|| (EM_Settings.dimensionProperties.containsKey(entityLiving.dimension)
											&& EM_Settings.dimensionProperties.get(entityLiving.dimension).trackTemp)
							)
					&& !(
							EM_Settings.matterOverdriveAndroidImmunities
							&& isCurrentlyAndroid
							)
					&& !(
							EM_Settings.witcheryWerewolfImmunities
							&& (isCurrentlyWerewolf || isCurrentlyWolf)
							)
				)
			{
				if(effect.getAmplifier() >= 2 && entityLiving.getRNG().nextInt(2) == 0)
				{
					entityLiving.attackEntityFrom(EnviroDamageSource.organfailure, 4.0F * 5F / (((EM_Settings.witcheryWerewolfImmunities && entityLiving instanceof EntityPlayer) ? werewolfLevel : 0)+5));
				}

				if(effect.getAmplifier() >= 1)
				{
					entityLiving.addPotionEffect(new PotionEffect(Potion.weakness.id, werewolfDuration, 0));
					entityLiving.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, werewolfDuration, 0));
				}
			}
		}

		// === Frostbite === //
		if(entityLiving.isPotionActive(frostbite))
		{
			PotionEffect effect = entityLiving.getActivePotionEffect(frostbite);

			// Remove effect if it's worn off
			if (effect.getDuration() <= 0)
			{
				entityLiving.removePotionEffect(frostbite.id);
			}
			// Don't inflict further effects if the stat in question is not tracked
			else if(
					EM_Settings.enableBodyTemp
					&& (
							!EM_Settings.dimensionProperties.containsKey(entityLiving.dimension)
									|| (EM_Settings.dimensionProperties.containsKey(entityLiving.dimension)
											&& EM_Settings.dimensionProperties.get(entityLiving.dimension).trackTemp)
							)
					&& !(
							EM_Settings.matterOverdriveAndroidImmunities
							&& isCurrentlyAndroid
							)
					&& !(
							EM_Settings.witcheryWerewolfImmunities
							&& (isCurrentlyWerewolf || isCurrentlyWolf)
							)
				)
			{
				if(entityLiving.getRNG().nextInt(2) == 0 && effect.getAmplifier() >= 2)
				{
					entityLiving.attackEntityFrom(EnviroDamageSource.frostbite, 4.0F * 5F / (((EM_Settings.witcheryWerewolfImmunities && entityLiving instanceof EntityPlayer) ? werewolfLevel : 0)+5));
				}

				if(entityLiving.getHeldItem() != null)
				{
					if(effect != null)
					{
						if(entityLiving.getRNG().nextInt(20 + (entityLiving instanceof EntityPlayer && EM_Settings.witcheryVampireImmunities ? vampireLevel*4 : 0) + (entityLiving instanceof EntityPlayer && EM_Settings.witcheryWerewolfImmunities ? werewolfLevel*4 : 0)) == 0)
						{
							EntityItem item = entityLiving.entityDropItem(entityLiving.getHeldItem(), 0.0F);
							item.delayBeforeCanPickup = 40;
							entityLiving.setCurrentItemOrArmor(0, null);

							entityLiving.worldObj.playSoundAtEntity(entityLiving, "enviromine:shiver", 1f, 1f);

							if(entityLiving instanceof EntityPlayer)
							{
								((EntityPlayer)entityLiving).addStat(EnviroAchievements.iNeededThat, 1);
							}
						}
					}
				}
			}
		}

		// === Dehydration === //
		if(entityLiving.isPotionActive(dehydration.id))
		{
			PotionEffect effect = entityLiving.getActivePotionEffect(dehydration);

			// Remove effect if it's worn off
			if (effect.getDuration() <= 0)
			{
				entityLiving.removePotionEffect(dehydration.id);
			}
			// Don't inflict further effects if the stat in question is not tracked
			else if(
					EM_Settings.enableHydrate
					&& (
							!EM_Settings.dimensionProperties.containsKey(entityLiving.dimension)
									|| (EM_Settings.dimensionProperties.containsKey(entityLiving.dimension)
											&& EM_Settings.dimensionProperties.get(entityLiving.dimension).trackHydration)
							)
					&& !(
							EM_Settings.matterOverdriveAndroidImmunities
							&& isCurrentlyAndroid
							)
					&& !(
							EM_Settings.witcheryVampireImmunities
							&& isVampire
							)
				)
			{
				if(tracker != null)
				{
					tracker.dehydrate(1F + (effect.getAmplifier() * 1F));
				}
			}
		}

		// === Insanity === //
		if(entityLiving.isPotionActive(insanity.id))
		{
			PotionEffect effect = entityLiving.getActivePotionEffect(insanity);

			// Remove effect if it's worn off
			if (effect.getDuration() <= 0)
			{
				entityLiving.removePotionEffect(insanity.id);
			}
			// Don't inflict further effects if the stat in question is not tracked
			else if(
					EM_Settings.enableSanity
					&& (
							!EM_Settings.dimensionProperties.containsKey(entityLiving.dimension)
									|| (EM_Settings.dimensionProperties.containsKey(entityLiving.dimension)
											&& EM_Settings.dimensionProperties.get(entityLiving.dimension).trackSanity)
							)
					&& !(
							EM_Settings.matterOverdriveAndroidImmunities
							&& isCurrentlyAndroid
							)
					&& !(
							EM_Settings.witcheryVampireImmunities
							&& isVampire
							)
				)
			{
				int chance = 50 / (effect.getAmplifier() + 1);

				chance = chance > 0? chance : 1;
				chance += (entityLiving instanceof EntityPlayer && EM_Settings.witcheryWerewolfImmunities ? werewolfLevel*4 : 0);

				if(entityLiving.getRNG().nextInt(chance) == 0)
				{
					if(effect.getAmplifier() >= 1)
					{
						if(EM_Settings.noNausea)
						{
							entityLiving.addPotionEffect(new PotionEffect(Potion.blindness.id, werewolfDuration));
						} else
						{
							entityLiving.addPotionEffect(new PotionEffect(Potion.confusion.id, werewolfDuration));
						}
					}
				}

				if(EnviroMine.proxy.isClient() && effect.getAmplifier() >= 2 && entityLiving.getRNG().nextInt(1000 + (entityLiving instanceof EntityPlayer && EM_Settings.witcheryWerewolfImmunities ? werewolfLevel*200 : 0)) == 0)
				{
					displayFakeDeath();
				}

				String sound = "";
				if(entityLiving.getRNG().nextInt(chance) == 0 && entityLiving instanceof EntityPlayer)
				{
					switch(entityLiving.getRNG().nextInt(16))
					{
						case 0:
						{
							sound = "ambient.cave.cave";
							break;
						}
						case 1:
						{
							sound = "random.explode";
							break;
						}
						case 2:
						{
							sound = "creeper.primed";
							break;
						}
						case 3:
						{
							sound = "mob.zombie.say";
							break;
						}
						case 4:
						{
							sound = "mob.endermen.idle";
							break;
						}
						case 5:
						{
							sound = "mob.skeleton.say";
							break;
						}
						case 6:
						{
							sound = "mob.wither.idle";
							break;
						}
						case 7:
						{
							sound = "mob.spider.say";
							break;
						}
						case 8:
						{
							sound = "ambient.weather.thunder";
							break;
						}
						case 9:
						{
							sound = "liquid.lava";
							break;
						}
						case 10:
						{
							sound = "liquid.water";
							break;
						}
						case 11:
						{
							sound = "mob.ghast.moan";
							break;
						}
						case 12:
						{
							sound = "random.bowhit";
							break;
						}
						case 13:
						{
							sound = "game.player.hurt";
							break;
						}
						case 14:
						{
							sound = "mob.enderdragon.growl";
							break;
						}
						case 15:
						{
							sound = "mob.endermen.portal";
							break;
						}
					}

					EntityPlayer player = ((EntityPlayer)entityLiving);

					float rndX = (player.getRNG().nextInt(6) - 3) * player.getRNG().nextFloat();
					float rndY = (player.getRNG().nextInt(6) - 3) * player.getRNG().nextFloat();
					float rndZ = (player.getRNG().nextInt(6) - 3) * player.getRNG().nextFloat();

					S29PacketSoundEffect packet = new S29PacketSoundEffect(sound, entityLiving.posX + rndX, entityLiving.posY + rndY, entityLiving.posZ + rndZ, 1.0F, !EM_Settings.randomizeInsanityPitch ? 1F : player.getRNG().nextBoolean()? 0.2F : (player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.2F + 1.0F);

					if(!EnviroMine.proxy.isClient() && player instanceof EntityPlayerMP)
					{
						((EntityPlayerMP)player).playerNetServerHandler.sendPacket(packet);
					} else if(EnviroMine.proxy.isClient() && !player.worldObj.isRemote)
					{
						player.worldObj.playSoundEffect(entityLiving.posX + rndX, entityLiving.posY + rndY, entityLiving.posZ + rndZ, sound, 1.0F, !EM_Settings.randomizeInsanityPitch ? 1F : (player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.2F + 1.0F);
					}
				}
			}
		}

		// Award achievement for heatstroke and hypothermia together
		if(entityLiving.isPotionActive(heatstroke.id) && entityLiving.isPotionActive(hypothermia.id))
		{
			if(entityLiving instanceof EntityPlayer)
			{
				((EntityPlayer)entityLiving).addStat(EnviroAchievements.thermalShock, 1);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private static void displayFakeDeath()
	{
		if(Minecraft.getMinecraft().currentScreen == null)
		{
			Minecraft.getMinecraft().displayGuiScreen(new EM_GuiFakeDeath());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns true if the potion has a associated status icon to display in then inventory when active.
	 */
	public boolean hasStatusIcon()
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(textureResource);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * This method returns true if the potion effect is bad - negative - for the entity.
	 */
	public boolean isBadEffect()
	{
		return true;
	}

    /**
     * Sets the index for the icon displayed in the player's inventory when the status is active.
     */
	@Override
    public EnviroPotion setIconIndex(int p_76399_1_, int p_76399_2_)
    {
        return (EnviroPotion)super.setIconIndex(p_76399_1_, p_76399_2_);
    }
}
