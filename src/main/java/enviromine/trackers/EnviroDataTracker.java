package enviromine.trackers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Timer;
import java.util.TimerTask;

import api.hbm.item.IGasMask;
import com.hbm.util.ArmorRegistry;
import com.hbm.util.ArmorUtil;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.EnviroDamageSource;
import enviromine.EnviroPotion;
import enviromine.client.gui.UI_Settings;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.handlers.EM_StatusManager;
import enviromine.trackers.properties.DimensionProperties;
import enviromine.trackers.properties.EntityProperties;
import enviromine.utils.EnviroUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static enviromine.core.EM_Settings.DeathFromHeartAttack;
import static enviromine.core.EM_Settings.HeartAttackTimeToDie;


public class EnviroDataTracker
{
    public static final Logger logger = LogManager.getLogger("ENVIROMINE_DEBUG_LOGGER");
	public EntityLivingBase trackedEntity;

	public float prevBodyTemp = 36.6F;
	public float prevHydration = 100F;
	public float prevAirQuality = 100;
	public float prevSanity = 100F;

	public float gasAirDiff = 0F;

	public float airQuality;

	public float bodyTemp;
	public float airTemp;

	public float hydration;

	public float sanity;

	public int attackDelay = 1;
	public int curAttackTime = 0;
	public boolean isDisabled = false;

	public int frostbiteLevel = 0;
	public boolean frostIrreversible = false;

	public boolean brokenLeg = false;
	public boolean brokenArm = false;
	public boolean bleedingOut = false;

	public String sleepState = "Awake";
	public int lastSleepTime = 0;

	public int timeBelow10 = 0;

    public int heartattacktimer =0;

	public int updateTimer = 0;


    public static boolean isHbmLoaded() {
        return Loader.isModLoaded("hbm");
    }

	private Side side = FMLCommonHandler.instance().getSide();

	//Sound Time
	public long chillPrevTime = 0;
	public long sizzlePrevTime = 0;

	public EnviroDataTracker(EntityLivingBase entity)
	{
		trackedEntity = entity;
		airQuality = 100F;
		bodyTemp = 36.6F;
		hydration = 100F;
		sanity = 100F;
	}

	public void updateData()
	{
		prevBodyTemp = bodyTemp;
		prevAirQuality = airQuality;
		prevHydration = hydration;
		prevSanity = sanity;

		updateTimer = 0;

		if(trackedEntity == null || isDisabled)
		{
			EM_StatusManager.removeTracker(this);
			return;
		}

		if(trackedEntity.isDead)
		{
			return;
		}

		if(!(trackedEntity instanceof EntityPlayer) && !EM_Settings.trackNonPlayer
				|| (!EM_Settings.enableAirQ && !EM_Settings.enableBodyTemp && !EM_Settings.enableHydrate && !EM_Settings.enableSanity))
		{
			EM_StatusManager.saveAndRemoveTracker(this);
			return;
		}

		int i = MathHelper.floor_double(trackedEntity.posX);
		int k = MathHelper.floor_double(trackedEntity.posZ);

		if(!trackedEntity.worldObj.getChunkFromBlockCoords(i, k).isChunkLoaded)
		{
			return;
		}

		float[] enviroData = EM_StatusManager.getSurroundingData(trackedEntity, EM_Settings.scanRadius);
		boolean isCreative = false;

		if(trackedEntity instanceof EntityPlayer)
		{
			if(((EntityPlayer)trackedEntity).capabilities.isCreativeMode)
			{
				isCreative = true;
			}
		}

		if((trackedEntity.getHealth() <= 2F || bodyTemp >= 41F) && enviroData[EM_StatusManager.SANITY_DELTA_INDEX] > (float)(-1F * EM_Settings.sanityMult))
		{
			enviroData[EM_StatusManager.SANITY_DELTA_INDEX] = (float)(-1F * EM_Settings.sanityMult);
		}
		else if(
				trackedEntity.getHealth() >= trackedEntity.getMaxHealth()
				&& enviroData[EM_StatusManager.SANITY_DELTA_INDEX] < (0.1F * EM_Settings.sanityMult)
				&& trackedEntity.worldObj.isDaytime()
				&& !trackedEntity.worldObj.provider.hasNoSky
				&& trackedEntity.worldObj.canBlockSeeTheSky(MathHelper.floor_double(trackedEntity.posX), MathHelper.floor_double(trackedEntity.posY), MathHelper.floor_double(trackedEntity.posZ))
				)
		{
			enviroData[EM_StatusManager.SANITY_DELTA_INDEX] = (float)(0.1F * EM_Settings.sanityMult);
		}

		// Air checks
		enviroData[EM_StatusManager.AIR_QUALITY_DELTA_INDEX] += gasAirDiff;
		gasAirDiff = 0F;
		airQuality += enviroData[EM_StatusManager.AIR_QUALITY_DELTA_INDEX];

		// Gas mask stuff specifically
		ItemStack helmet = trackedEntity.getEquipmentInSlot(4);
		if(helmet != null && !isCreative)
		{
			if(helmet.hasTagCompound() && helmet.getTagCompound().hasKey(EM_Settings.GAS_MASK_FILL_TAG_KEY))
			{
				NBTTagCompound tag = helmet.getTagCompound();
				int gasMaskFill = tag.getInteger(EM_Settings.GAS_MASK_FILL_TAG_KEY);

				if(EM_Settings.airMult > 0F && gasMaskFill > 0 && (100F-airQuality) >= EM_Settings.airMult) // If the gas mask has some filter durability left and your air isn't perfect
				{
					float airToFill = 100F - airQuality;

					// Scale the amount of air to be filled by config factor
					airToFill *= EM_Settings.gasMaskUpdateRestoreFraction;

					if (airToFill > gasMaskFill) {airToFill = (float)gasMaskFill;} // Can't heal more air than there is quality left in the mask

					if(airToFill > 0F)
					{
						// Increase air quality
						airQuality += airToFill;
						// Reduce the mask quality
						int newMaskQuality = gasMaskFill - MathHelper.floor_float(airToFill+this.trackedEntity.worldObj.rand.nextFloat());
						if (newMaskQuality<0) {newMaskQuality=0;} // Just in case
						tag.setInteger(EM_Settings.GAS_MASK_FILL_TAG_KEY, newMaskQuality);
					}
				}
			}
		}

        if (isHbmLoaded()) {
            if (helmet != null && !isCreative) {

                if (helmet.getItem() instanceof IGasMask) { // Check if the helmet is a mask
                    IGasMask mask = (IGasMask) helmet.getItem();
                    ItemStack filter = mask.getFilter(helmet, trackedEntity); // Get the filter of the mask

                    if (filter != null) {

                        if (EM_Settings.airMult > 0F && (100F - airQuality) >= EM_Settings.airMult) // If your air isn't perfect
                        {
                            float airToFill = 100F - airQuality;

                            // Scale the amount of air to be filled by config factor
                            airToFill *= EM_Settings.gasMaskUpdateRestoreFraction;

                            if (airToFill > 0F) {
                                // Check whether the mask provides protection against coarse/exact type of pollution you want to handle
                                if (ArmorRegistry.hasProtection(trackedEntity, 3, ArmorRegistry.HazardClass.PARTICLE_COARSE)) {

                                    // Increase air quality
                                    airQuality += airToFill;
                                    // Random chance to damage the filter proportional to the amount of air restored
                                    /*if (trackedEntity.getRNG().nextInt(Math.max(EM_Settings.HbmGasMaskBreakChanceNumber - MathHelper.floor_float(airToFill), 1)) == 0) {*/
                                        ArmorUtil.damageGasMaskFilter(trackedEntity, MathHelper.floor_float(airToFill*EM_Settings.HbmGasMaskBreakMultiplier));
                                   // }

                                }
                            }
                        }
                    }
                }
                else if (ArmorRegistry.hasProtection(trackedEntity, 3, ArmorRegistry.HazardClass.PARTICLE_COARSE)) {
                        if (EM_Settings.airMult > 0F && (100F - airQuality) >= EM_Settings.airMult)
                        {
                            float airToFill = 100F - airQuality;
                            // Scale the amount of air to be filled by config factor
                            airToFill *= EM_Settings.gasMaskUpdateRestoreFraction;
                            if (airToFill > 0F) {
                                // Check whether the mask provides protection against coarse/exact type of pollution you want to handle
                                // Increase air quality
                                airQuality += airToFill;
                                // Random chance to damage the filter proportional to the amount of air restored
                                /*if (trackedEntity.getRNG().nextInt(Math.max(EM_Settings.HbmGasMaskBreakChanceNumber - MathHelper.floor_float(airToFill), 1)) == 0) {*/
                                    ArmorUtil.damageGasMaskFilter(trackedEntity, MathHelper.floor_float(airToFill*EM_Settings.HbmGasMaskBreakMultiplier));
                                //}
                            }
                        }
                     }
            }
        }


		airQuality = MathHelper.clamp_float(airQuality, 0F, 100F);

		// Temperature checks
		airTemp = enviroData[EM_StatusManager.AMBIENT_TEMP_INDEX];
		float temperatureDropSpeed = enviroData[EM_StatusManager.BODY_TEMP_DROP_SPEED_INDEX];
		float temperatureRiseSpeed = enviroData[EM_StatusManager.BODY_TEMP_RISE_SPEED_INDEX];

		float relTemp = airTemp + 12F;

		boolean isVampire = false;
//		boolean isWerewolf = false;
		boolean isCurrentlyAndroid = false;
		boolean isCurrentlyWerewolf = false;
		boolean isCurrentlyWolf = false;
		int vampireLevel = 0;
		int werewolfLevel = 0;

		if (trackedEntity instanceof EntityPlayer) {
			isVampire = EnviroUtils.isPlayerAVampire((EntityPlayer)trackedEntity);
//			isWerewolf = EnviroUtils.isPlayerAWerewolf((EntityPlayer)trackedEntity);
			isCurrentlyAndroid = EnviroUtils.isPlayerCurrentlyMOAndroid((EntityPlayer)trackedEntity);
			isCurrentlyWerewolf = EnviroUtils.isPlayerCurrentlyWitcheryWerewolf((EntityPlayer)trackedEntity);
			isCurrentlyWolf = EnviroUtils.isPlayerCurrentlyWitcheryWolf((EntityPlayer)trackedEntity);
			vampireLevel = EnviroUtils.getWitcheryVampireLevel((EntityPlayer)trackedEntity);
			werewolfLevel = EnviroUtils.getWitcheryWerewolfLevel((EntityPlayer)trackedEntity);
		}

		if (trackedEntity.isPotionActive(Potion.fireResistance) && bodyTemp > 36.6F) {bodyTemp = 36.6F;} // IF you have fire resistance, ambient temperature can never "feel" higher than 37

		if(bodyTemp - relTemp > 0) // Cold
		{
			float temperatureSpeedAmplification = Math.abs(bodyTemp - relTemp) > 10F? Math.abs(bodyTemp - relTemp)/10F : 1F;
			if(bodyTemp - relTemp >= temperatureDropSpeed * temperatureSpeedAmplification)
			{
				bodyTemp -= temperatureDropSpeed * temperatureSpeedAmplification * (((EM_Settings.witcheryVampireImmunities & isVampire) | (EM_Settings.witcheryWerewolfImmunities & (isCurrentlyWerewolf | isCurrentlyWolf))) & bodyTemp <= 37F ? 0.5F : 1F);
			} else
			{
				bodyTemp = relTemp;
			}
		} else if(bodyTemp - relTemp < 0) // Hot
		{
			float temperatureSpeedAmplification = Math.abs(bodyTemp - relTemp) > 10F? Math.abs(bodyTemp - relTemp)/10F : 1F;
			if(bodyTemp - relTemp <= -temperatureRiseSpeed * temperatureSpeedAmplification)
			{
				bodyTemp += temperatureRiseSpeed * temperatureSpeedAmplification;
			} else
			{
				bodyTemp = relTemp;
			}
		}

		// Hydration checks
		if(hydration > 0F && (enviroData[EM_StatusManager.ANIMAL_HOSTILITY_INDEX] == 1 || !(trackedEntity instanceof EntityAnimal)))
		{
			if(bodyTemp >= 38.02F)
			{
				dehydrate(0.1F);

				if(hydration >= 75F)
				{
					bodyTemp -= 0.01F;
				}
			}

			if(enviroData[EM_StatusManager.DEHYDRATION_DELTA_INDEX] > 0F)
			{
				dehydrate(0.05F + enviroData[EM_StatusManager.DEHYDRATION_DELTA_INDEX]);
			} else
			{
				if(enviroData[EM_StatusManager.DEHYDRATION_DELTA_INDEX] < 0F)
				{
					hydrate(-enviroData[EM_StatusManager.DEHYDRATION_DELTA_INDEX]);
				}
				dehydrate(0.05F);
			}
		} else if(enviroData[EM_StatusManager.ANIMAL_HOSTILITY_INDEX] == -1 && trackedEntity instanceof EntityAnimal)
		{
			hydrate(0.05F);
		} else if(hydration <= 0F)
		{
			hydration = 0;
		}

		// Sanity checks
		if(sanity < 0F)
		{
			sanity = 0F;
		}

		if(enviroData[EM_StatusManager.SANITY_DELTA_INDEX] < 0F)
		{
			if(sanity + enviroData[EM_StatusManager.SANITY_DELTA_INDEX] >= 0F)
			{
				sanity += enviroData[EM_StatusManager.SANITY_DELTA_INDEX];
			} else
			{
				sanity = 0F;
			}
		} else if(enviroData[EM_StatusManager.SANITY_DELTA_INDEX] > 0F)
		{
			if(sanity + enviroData[EM_StatusManager.SANITY_DELTA_INDEX] <= 100F)
			{
				sanity += enviroData[EM_StatusManager.SANITY_DELTA_INDEX];
			} else
			{
				sanity = 100F;
			}
		}

		//Check for custom properties
		boolean enableAirQ = true;
		boolean enableBodyTemp = true;
		boolean enableHydrate = true;
		boolean enableFrostbite = true;
		boolean enableHeat = true;
		boolean enableSanity = true; // Added for transformation purposes
		int id = 0;

//		if(EntityList.getEntityID(trackedEntity) > 0)
//		{
//			id = EntityList.getEntityID(trackedEntity);
//		}
//		else if(EntityRegistry.instance().lookupModSpawn(trackedEntity.getClass(), false) != null)
//		{
//			id = EntityRegistry.instance().lookupModSpawn(trackedEntity.getClass(), false).getModEntityId() + 128;
//		}

		if(EntityProperties.base.hasProperty(trackedEntity))
		{
			EntityProperties livingProps = EntityProperties.base.getProperty(trackedEntity);
			enableHydrate = livingProps.dehydration;
			enableBodyTemp = livingProps.bodyTemp;
			enableAirQ = livingProps.airQ;
			enableFrostbite = !livingProps.immuneToFrost;
			enableHeat = !livingProps.immuneToHeat;
		} else if((trackedEntity instanceof EntitySheep) || (trackedEntity instanceof EntityWolf))
		{
			enableFrostbite = false;
		} else if(trackedEntity instanceof EntityChicken)
		{
			enableHeat = false;
		}

		// Add immunities based on transformation status
		if(trackedEntity instanceof EntityPlayer)
		{
			if(EM_Settings.matterOverdriveAndroidImmunities && isCurrentlyAndroid)
			{
				enableHydrate = false;
				enableBodyTemp = false;
				enableAirQ = false;
				enableFrostbite = false;
				enableHeat = false;
				enableSanity = false;
			}

			if(EM_Settings.witcheryVampireImmunities && isVampire)
			{
				enableHydrate = false;
				enableSanity = false;
			}

			if(EM_Settings.witcheryWerewolfImmunities && (isCurrentlyWerewolf || isCurrentlyWolf))
			{
				enableFrostbite = false;
			}
		}

		//Reset Disabled Values
		if(!EM_Settings.enableAirQ || !enableAirQ)
		{
			airQuality = 100F;
		}
		if(!EM_Settings.enableBodyTemp || !enableBodyTemp)
		{
			bodyTemp = 37F;
		}
		if(!EM_Settings.enableHydrate || !enableHydrate)
		{
			hydration = 100F;
		}
		if(!EM_Settings.enableSanity || !(trackedEntity instanceof EntityPlayer) || !enableSanity)
		{
			sanity = 100F;
		}

		// Camel Pack Stuff
		ItemStack plate = trackedEntity.getEquipmentInSlot(3);

		if(plate != null && !isCreative)
		{
			if (plate.hasTagCompound() && plate.getTagCompound().hasKey(EM_Settings.CAMEL_PACK_FILL_TAG_KEY))
			{
				NBTTagCompound tag = plate.getTagCompound();
				int camelPackFill = tag.getInteger(EM_Settings.CAMEL_PACK_FILL_TAG_KEY);

				if(EM_Settings.hydrationMult > 0F && camelPackFill > 0 && (100F - hydration) >= EM_Settings.hydrationMult) // If the camel pack has some durability left and your hydration isn't perfect
				{
					tag.setInteger(EM_Settings.CAMEL_PACK_FILL_TAG_KEY, camelPackFill-1);
					hydrate((float)EM_Settings.hydrationMult);

					if(bodyTemp >= 37F + EM_Settings.tempMult/10F)
					{
						bodyTemp -= EM_Settings.tempMult/10F;
					}
				}
			}
		}

		// Fix floating point errors
		this.fixFloatingPointErrors();

		if(trackedEntity instanceof EntityPlayer)
		{
			if(((EntityPlayer)trackedEntity).capabilities.isCreativeMode)
			{
				bodyTemp = prevBodyTemp;
				airQuality = prevAirQuality;
				hydration = prevHydration;
				sanity = prevSanity;
			}

		}

		// Apply side effects


		if(airTemp <= 10F && bodyTemp <= 35F || bodyTemp <= 30F)
		{
			timeBelow10 += 1;
		} else
		{
			timeBelow10 = 0;
		}

		if(curAttackTime >= attackDelay)
		{
			// Prevent a whole bunch of stuff if you're an android
			if(!(EM_Settings.matterOverdriveAndroidImmunities && trackedEntity instanceof EntityPlayer && isCurrentlyAndroid))
			{
				// Air Check
				if(airQuality <= 0)
				{
					trackedEntity.attackEntityFrom(EnviroDamageSource.suffocate,
							4.0F * 5F / (((EM_Settings.witcheryVampireImmunities && trackedEntity instanceof EntityPlayer) ? vampireLevel : 0)+5)
							);

					trackedEntity.worldObj.playSoundAtEntity(trackedEntity, "enviromine:gag", 1f, 1f);
	     		}

				int vampireDuration = MathHelper.clamp_int(200 - (trackedEntity instanceof EntityPlayer && EM_Settings.witcheryVampireImmunities ? vampireLevel : 0)*15, 0, 200);

				if(
						airQuality <= 10F
						&& !(trackedEntity instanceof EntityPlayer && EM_Settings.witcheryVampireImmunities && isVampire)
						)
				{
					trackedEntity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, vampireDuration, 1));
					trackedEntity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, vampireDuration, 1));
				}
				else if(
						airQuality <= ((trackedEntity instanceof EntityPlayer && EM_Settings.witcheryVampireImmunities && isVampire) ? 10F : 25F)
						)
				{
					trackedEntity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, vampireDuration, 0));
					trackedEntity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, vampireDuration, 0));
				}

				// Hot temp checks
				if(!trackedEntity.isPotionActive(Potion.fireResistance))
				{
					if(bodyTemp >= 39F && enableHeat && EM_Settings.enableHeatstrokeGlobal && (enviroData[EM_StatusManager.ANIMAL_HOSTILITY_INDEX] == 1 || !(trackedEntity instanceof EntityAnimal)))
					{
                        if(bodyTemp >= 1000F)
                        {
                            trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.heatstroke.id, 200, 10));
                        }
                        if(bodyTemp >= 100F)
                        {
                            trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.heatstroke.id, 200, 5));
                        }
                        else if(bodyTemp >= 80F)
                        {
                            trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.heatstroke.id, 200, 4));
                        }
                        else if(bodyTemp >= 60F)
                        {
                            trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.heatstroke.id, 200, 3));
                        }
                        else if(bodyTemp >= 43F)
						{
							trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.heatstroke.id, 200, 2));
						}
						else if(bodyTemp >= 41F)
						{
							trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.heatstroke.id, 200, 1));
						}
						else
						{
							trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.heatstroke.id, 200, 0));
						}
					}
				} else if(trackedEntity.isPotionActive(EnviroPotion.heatstroke))
				{
					trackedEntity.removePotionEffect(EnviroPotion.heatstroke.id);
				}

				if(EM_Settings.catchFireAtHighTemps && ((bodyTemp >= 45F && enviroData[EM_StatusManager.NEAR_LAVA_INDEX] == 1 )|| bodyTemp >= 50F))
				{
					trackedEntity.setFire(10);
				}

				// Cold temp checks
				if(
						bodyTemp <= ((trackedEntity instanceof EntityPlayer && EM_Settings.witcheryVampireImmunities && isVampire) ? 32F : 35F)
						&& enableFrostbite && EM_Settings.enableHypothermiaGlobal && (enviroData[EM_StatusManager.ANIMAL_HOSTILITY_INDEX] == 1 || !(trackedEntity instanceof EntityAnimal))
						)
				{
					if(
							bodyTemp <= 30F
							&& !(trackedEntity instanceof EntityPlayer && EM_Settings.witcheryVampireImmunities && isVampire)
							)
					{
						trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.hypothermia.id, vampireDuration, 2));
					}
					else if(bodyTemp <= ((trackedEntity instanceof EntityPlayer && EM_Settings.witcheryVampireImmunities && isVampire) ? 30F : 32F))
					{
						trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.hypothermia.id, vampireDuration, 1));
					}
					else
					{
						trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.hypothermia.id, vampireDuration, 0));
					}

//					if (this.side.isClient())
//					{
//						// This sounds like someone blowing into a mic
//						//playSoundWithTimeCheck(17000, "enviromine:chill",  UI_Settings.breathVolume, 1.0F);
//					}
				}

				if(enableFrostbite
						&& EM_Settings.enableFrostbiteGlobal && (timeBelow10 >= 120 + (EM_Settings.witcheryVampireImmunities && isVampire ? vampireLevel*24 : 0)
						|| (frostbiteLevel >= 1 && frostIrreversible)))
				{
					if(timeBelow10 >= 240 + (EM_Settings.witcheryVampireImmunities && isVampire ? vampireLevel*48 : 0) || frostbiteLevel >= 2)
					{
						trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.frostbite.id, vampireDuration, 1));

						if(frostbiteLevel <= 2)
						{
							frostbiteLevel = 2;
						}
					}
					else
					{
						trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.frostbite.id, vampireDuration, 0));

						if(frostbiteLevel <= 1)
						{
							frostbiteLevel = 1;
						}
					}

					// If frostbite is treated before this time then you can save your limbs!
					if(timeBelow10 > 360 + (EM_Settings.witcheryVampireImmunities && isVampire ? vampireLevel*72 : 0)
							&& EM_Settings.frostbitePermanent && !frostIrreversible && EM_Settings.enableFrostbiteGlobal)
					{
						frostIrreversible = true;

						if(trackedEntity instanceof EntityPlayer)
						{
							((EntityPlayer)trackedEntity).addChatComponentMessage(new ChatComponentText("The flesh in your limbs have gone rock hard!"));
							((EntityPlayer)trackedEntity).addChatComponentMessage(new ChatComponentText("Your condition is now permanent!"));
						}
					}


					if (this.side.isClient())
					{
						playSoundWithTimeCheck(1700, "enviromine:chill",  UI_Settings.breathVolume, 1.0F);
					}
				}
				else if(!frostIrreversible || !enableFrostbite || !EM_Settings.enableFrostbiteGlobal)
				{
					frostbiteLevel = 0;
				}

				// Hydration checks
				if(hydration <= 0F && !(EM_Settings.witcheryVampireImmunities && isVampire))
				{
					trackedEntity.attackEntityFrom(EnviroDamageSource.dehydrate, 4.0F);
				}
                // Sanity checks
				int werewolfDuration = MathHelper.clamp_int(600 - (trackedEntity instanceof EntityPlayer && EM_Settings.witcheryWerewolfImmunities ? werewolfLevel : 0)*45, 0, 600);
				/**if(
						sanity <= ((trackedEntity instanceof EntityPlayer && EM_Settings.witcheryWerewolfImmunities) ? 10F - (float)werewolfLevel*0.75F : 10F)
						&& !(EM_Settings.witcheryVampireImmunities && isVampire))
				{
					trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.insanity.id, werewolfDuration, 2));
				}*/
                /**else*/ if(!isCreative && sanity <= 0F - (EM_Settings.witcheryWerewolfImmunities ? werewolfLevel : 0 ))
                {
                    if (DeathFromHeartAttack){
                        heartattacktimer += 1;
                        if (heartattacktimer >= HeartAttackTimeToDie/2){
                            trackedEntity.attackEntityFrom(EnviroDamageSource.heartattack, 25000.0F);
                            heartattacktimer = 0;
                        }
                    } else {
                        heartattacktimer = 0;
                        trackedEntity.addPotionEffect(new PotionEffect(Potion.hunger.id, werewolfDuration, 3));
                        trackedEntity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, werewolfDuration, 3));
                    }

                    //logger.fatal("Сurrent heartattacktimer :" + heartattacktimer + "/" + HeartAttackTimeToDie/2);

                    trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.insanity.id, werewolfDuration, 4));

                  trackedEntity.addPotionEffect(new PotionEffect(Potion.weakness.id, werewolfDuration, 4));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.hunger.id, werewolfDuration, 2));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.confusion.id, 60, 1));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, werewolfDuration, 2));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, werewolfDuration, 3));
                }					//trackedEntity.attackEntityFrom(EnviroDamageSource.heartattack, 25000.0F);
                else if(!isCreative && sanity > 0F - (EM_Settings.witcheryWerewolfImmunities ? werewolfLevel : 0) && sanity <= 5F - (EM_Settings.witcheryWerewolfImmunities ? werewolfLevel : 0) )
                {
                    if (DeathFromHeartAttack) {
                        heartattacktimer += 1;
                        if (heartattacktimer >= HeartAttackTimeToDie) {
                            trackedEntity.attackEntityFrom(EnviroDamageSource.heartattack, 25000.0F);
                            heartattacktimer = 0;
                        }
                    } else {
                        heartattacktimer = 0;
                        trackedEntity.addPotionEffect(new PotionEffect(Potion.hunger.id, werewolfDuration, 2));
                        trackedEntity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, werewolfDuration, 2));
                    }
                    //logger.fatal("Сurrent heartattacktimer :" + heartattacktimer + "/" + HeartAttackTimeToDie);

                    trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.insanity.id, werewolfDuration, 3));

                  trackedEntity.addPotionEffect(new PotionEffect(Potion.weakness.id, werewolfDuration, 3));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.hunger.id, werewolfDuration, 1));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.confusion.id, 60, 0));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, werewolfDuration, 1));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, werewolfDuration, 2));

                }
                else if(!isCreative && sanity > 5F - (EM_Settings.witcheryWerewolfImmunities ? werewolfLevel : 0) && sanity <= 25F - (EM_Settings.witcheryWerewolfImmunities ? werewolfLevel : 0) )
                {
                    heartattacktimer = 0;
                    //logger.fatal("Сurrent heartattacktimer :" + heartattacktimer + "/" + HeartAttackTimeToDie);
                    trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.insanity.id, werewolfDuration, 2));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.weakness.id, werewolfDuration, 2));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, werewolfDuration, 1));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, werewolfDuration, 1));
                }
                else if(!isCreative && sanity > 25F - (EM_Settings.witcheryWerewolfImmunities ? werewolfLevel : 0) && sanity <= 50F - (EM_Settings.witcheryWerewolfImmunities ? werewolfLevel : 0))
                {
                    heartattacktimer = 0;
                    //logger.fatal("Сurrent heartattacktimer :" + heartattacktimer + "/" + HeartAttackTimeToDie);
                    trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.insanity.id, werewolfDuration, 1));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.weakness.id, werewolfDuration, 1));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, werewolfDuration, 0));
                }
				else if(!isCreative && sanity > 50F - (EM_Settings.witcheryWerewolfImmunities ? werewolfLevel : 0) && sanity <= 75F - (EM_Settings.witcheryWerewolfImmunities ? werewolfLevel*2 : 0) )
				{
                    heartattacktimer = 0;
                    //logger.fatal("Сurrent heartattacktimer :" + heartattacktimer + "/" + HeartAttackTimeToDie);
                    trackedEntity.addPotionEffect(new PotionEffect(EnviroPotion.insanity.id, werewolfDuration, 0));
                  trackedEntity.addPotionEffect(new PotionEffect(Potion.weakness.id, werewolfDuration, 0));

				}
			}

			curAttackTime = 0;
		}
		else
		{
			curAttackTime += 1;
		}

		EnviroPotion.checkAndApplyEffects(trackedEntity);

		if(isCreative)
		{
			bodyTemp = prevBodyTemp;
			airQuality = prevAirQuality;
			hydration = prevHydration;
			sanity = prevSanity;
		}

		DimensionProperties dimensionProp = null;

		if(EM_Settings.dimensionProperties.containsKey(trackedEntity.worldObj.provider.dimensionId))
		{
			dimensionProp = EM_Settings.dimensionProperties.get(trackedEntity.worldObj.provider.dimensionId);
			if(dimensionProp != null && dimensionProp.override)
			{
				if(!dimensionProp.trackTemp && EM_Settings.enableBodyTemp) {bodyTemp = prevBodyTemp;}
				if(!dimensionProp.trackAirQuality && EM_Settings.enableAirQ) {airQuality = prevAirQuality;}
				if(!dimensionProp.trackHydration && EM_Settings.enableHydrate) {hydration = prevHydration;}
				if(!dimensionProp.trackSanity && EM_Settings.enableSanity) {sanity = prevSanity;}
			}
		}




		this.fixFloatingPointErrors();
		EM_StatusManager.saveTracker(this);
	}

	@SideOnly(Side.CLIENT)
	private void playSoundWithTimeCheck(int time, String sound, float volume, float pitch)
	{
		if ((Minecraft.getSystemTime() - chillPrevTime) > 17000)
		{
			Minecraft.getMinecraft().thePlayer.playSound("enviromine:chill",  UI_Settings.breathVolume, 1.0F);
			chillPrevTime = Minecraft.getSystemTime();
		}
	}

	public void fixFloatingPointErrors()
	{
		airQuality = new BigDecimal(String.valueOf(airQuality)).setScale(2, RoundingMode.HALF_UP).floatValue();
		bodyTemp = new BigDecimal(String.valueOf(bodyTemp)).setScale(3, RoundingMode.HALF_UP).floatValue();
		airTemp = new BigDecimal(String.valueOf(airTemp)).setScale(3, RoundingMode.HALF_UP).floatValue();
		hydration = new BigDecimal(String.valueOf(hydration)).setScale(2, RoundingMode.HALF_UP).floatValue();
		sanity = new BigDecimal(String.valueOf(sanity)).setScale(3, RoundingMode.HALF_UP).floatValue();
	}

	public static boolean isLegalType(EntityLivingBase entity)
	{
		String name = EntityList.getEntityString(entity);

		if(EntityProperties.base.hasProperty(entity))
		{
			return EntityProperties.base.getProperty(entity).shouldTrack;
		}

		if(entity.isEntityUndead() || entity instanceof EntityMob)
		{
			return false;
		} else if(name == "Enderman")
		{
			return false;
		} else if(name == "Villager")
		{
			return false;
		} else if(name == "Slime")
		{
			return false;
		} else if(name == "Ghast")
		{
			return false;
		} else if(name == "Squid")
		{
			return false;
		} else if(name == "Blaze")
		{
			return false;
		} else if(name == "LavaSlime")
		{
			return false;
		} else if(name == "SnowMan")
		{
			return false;
		} else if(name == "MushroomCow")
		{
			return false;
		} else if(name == "WitherBoss")
		{
			return false;
		} else if(name == "EnderDragon")
		{
			return false;
		} else if(name == "VillagerGolem")
		{
			return false;
		} else
		{
			EnviroDataTracker tracker = EM_StatusManager.lookupTracker(entity);

			if(tracker != null && !tracker.isDisabled && tracker.trackedEntity == entity)
			{
				return false;
			} else
			{
				return true;
			}
		}
	}

	public void hydrate(float amount)
	{
		float MAmount = (float)(amount * EM_Settings.hydrationMult);

		if(hydration >= 100F - MAmount)
		{
			hydration = 100.0F;
		} else
		{
			hydration += MAmount;
		}

		this.fixFloatingPointErrors();

		if(!EnviroMine.proxy.isClient() || EnviroMine.proxy.isOpenToLAN())
		{
			EM_StatusManager.syncMultiplayerTracker(this);
		}
	}

	public void dehydrate(float amount)
	{
		float MAmount = (float)(amount * EM_Settings.hydrationMult);

		if(hydration >= MAmount)
		{
			hydration -= MAmount;
		} else
		{
			hydration = 0F;
		}

		this.fixFloatingPointErrors();

		if(!EnviroMine.proxy.isClient() || EnviroMine.proxy.isOpenToLAN())
		{
			EM_StatusManager.syncMultiplayerTracker(this);
		}
	}

	public void loadNBTTags()
	{
		NBTTagCompound tags = trackedEntity.getEntityData();

		if(tags.hasKey("ENVIRO_AIR"))
		{
			airQuality = tags.getFloat("ENVIRO_AIR");
		}
		if(tags.hasKey("ENVIRO_HYD"))
		{
			hydration = tags.getFloat("ENVIRO_HYD");
		}
		if(tags.hasKey("ENVIRO_TMP"))
		{
			bodyTemp = tags.getFloat("ENVIRO_TMP");
		}
		if(tags.hasKey("ENVIRO_SAN"))
		{
			sanity = tags.getFloat("ENVIRO_SAN");
		}
	}

	public void resetData()
	{
		airQuality = 100F;
		bodyTemp = 37F;
		hydration = 100F;
		sanity = 100F;

		// Added by AstroTibs to ensure no BS
		trackedEntity.clearActivePotions();
		frostbiteLevel = 0;
		lastSleepTime = 0;
		timeBelow10 = 0;
	}

	public void ClampSafeRange()
	{
		airQuality = MathHelper.clamp_float(airQuality, 25F, 100F);
		bodyTemp = MathHelper.clamp_float(bodyTemp, 35F, 39F);
		hydration = MathHelper.clamp_float(hydration, 25F, 100F);
		sanity = MathHelper.clamp_float(sanity, 50F, 100F);
	}
}
