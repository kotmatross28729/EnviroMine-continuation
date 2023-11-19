package enviromine.handlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.base.Stopwatch;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import enviromine.EnviroPotion;
import enviromine.client.gui.UI_Settings;
import enviromine.client.gui.hud.items.Debug_Info;
import enviromine.core.EM_ConfigHandler.EnumLogVerbosity;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.network.packet.PacketEnviroMine;
import enviromine.trackers.EnviroDataTracker;
import enviromine.trackers.properties.ArmorProperties;
import enviromine.trackers.properties.BiomeProperties;
import enviromine.trackers.properties.BlockProperties;
import enviromine.trackers.properties.DimensionProperties;
import enviromine.trackers.properties.EntityProperties;
import enviromine.trackers.properties.ItemProperties;
import enviromine.utils.EnviroUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.EnumPlantType;

public class EM_StatusManager
{
	public static final int AIR_QUALITY_DELTA_INDEX = 0;
	public static final int AMBIENT_TEMP_INDEX = 1;
	public static final int NEAR_LAVA_INDEX = 2;
	public static final int DEHYDRATION_DELTA_INDEX = 3;
	public static final int BODY_TEMP_DROP_SPEED_INDEX = 4;
	public static final int BODY_TEMP_RISE_SPEED_INDEX = 5;
	public static final int ANIMAL_HOSTILITY_INDEX = 6;
	public static final int SANITY_DELTA_INDEX = 7;

    public static final int BLOOD_DELTA_INDEX = 8;

	public static HashMap<String,EnviroDataTracker> trackerList = new HashMap<String,EnviroDataTracker>();

	public static void addToManager(EnviroDataTracker tracker)
	{
		if(tracker.trackedEntity instanceof EntityPlayer)
		{
			trackerList.put("" + tracker.trackedEntity.getCommandSenderName(), tracker);
		} else
		{
			trackerList.put("" + tracker.trackedEntity.getEntityId(), tracker);
		}
	}

	public static void updateTracker(EnviroDataTracker tracker)
	{
		if(tracker == null)
		{
			return;
		}

		if(EnviroMine.proxy.isClient() && Minecraft.getMinecraft().isIntegratedServerRunning())
		{
			if(Minecraft.getMinecraft().isGamePaused() && !EnviroMine.proxy.isOpenToLAN())
			{
				return;
			}
		}

		tracker.updateTimer += 1;

		if(tracker.updateTimer >= 30)
		{
			tracker.updateData();

			if(!EnviroMine.proxy.isClient() || EnviroMine.proxy.isOpenToLAN())
			{
				syncMultiplayerTracker(tracker);
			}
		}
	}

	public static void syncMultiplayerTracker(EnviroDataTracker tracker)
	{
		if(!(tracker.trackedEntity instanceof EntityPlayer))
		{
			return;
		}

		tracker.fixFloatingPointErrors(); // Shortens data as much as possible before sending
		NBTTagCompound pData = new NBTTagCompound();
		pData.setInteger("id", 0);
		pData.setString("player", tracker.trackedEntity.getCommandSenderName());
		pData.setFloat("airQuality", tracker.airQuality);
		pData.setFloat("bodyTemp", tracker.bodyTemp);
		pData.setFloat("hydration", tracker.hydration);
		pData.setFloat("sanity", tracker.sanity);
        pData.setFloat("blood", tracker.blood);
		pData.setFloat("airTemp", tracker.airTemp);

		EnviroMine.instance.network.sendToAllAround(new PacketEnviroMine(pData), new TargetPoint(tracker.trackedEntity.worldObj.provider.dimensionId, tracker.trackedEntity.posX, tracker.trackedEntity.posY, tracker.trackedEntity.posZ, 128D));
	}

	public static EnviroDataTracker lookupTracker(EntityLivingBase entity)
	{
		if(entity instanceof EntityPlayer)
		{
			if(trackerList.containsKey("" + entity.getCommandSenderName()))
			{
				return trackerList.get("" + entity.getCommandSenderName());
			} else
			{
				return null;
			}
		} else
		{
			if(trackerList.containsKey("" + entity.getEntityId()))
			{
				return trackerList.get("" + entity.getEntityId());
			} else
			{
				return null;
			}
		}
	}

	public static EnviroDataTracker lookupTrackerFromUsername(String username)
	{
		if(trackerList.containsKey(username))
		{
			return trackerList.get(username);
		} else
		{
			return null;
		}
	}

	private static Stopwatch timer = Stopwatch.createUnstarted();

	public static float[] getSurroundingData(EntityLivingBase entityLiving, int cubeRadius)
	{
		if(EnviroMine.proxy.isClient() && entityLiving.getCommandSenderName().equals(Minecraft.getMinecraft().thePlayer.getCommandSenderName()) && !timer.isRunning())
		{
			timer.start();
		}

		float[] data = new float[8];

		float sanityRate = 0F;
		float sanityStartRate = sanityRate;

		float quality = 0;
		double leaves = 0;
		float sanityBoost = 0;

		float dropSpeed = 0.001F;
		float riseSpeed = 0.001F;

		float blockAndItemTempInfluence = -999F;
		float cooling = 0;
		float dehydrateBonus = 0.0F;
		int animalHostility = 0;
		boolean nearLava = false;
		float dist = 0;
		float solidBlocks = 0;
		float totalBlocks = 0;

		int i = MathHelper.floor_double(entityLiving.posX);
		int j = MathHelper.floor_double(entityLiving.posY);
		int k = MathHelper.floor_double(entityLiving.posZ);

		if(entityLiving.worldObj == null)
		{
			return data;
		}

		Chunk chunk = entityLiving.worldObj.getChunkFromBlockCoords(i, k);

		if(chunk == null)
		{
			return data;
		}

		BiomeGenBase biome = chunk.getBiomeGenForWorldCoords(i & 15, k & 15, entityLiving.worldObj.getWorldChunkManager());

		if(biome == null)
		{
			return data;
		}

		DimensionProperties dimensionProp = null;
		if(DimensionProperties.base.hasProperty(entityLiving.worldObj.provider.dimensionId))
		{
			dimensionProp = DimensionProperties.base.getProperty(entityLiving.worldObj.provider.dimensionId);
		}


		float surroundingBiomeTempSamplesSum = 0;
		int surroundingBiomeTempSamplesCount = 0;

		boolean isDay = entityLiving.worldObj.isDaytime();

		//Note: This is offset slightly so that heat peaks after noon.
		float scale = 1.25F; // Anything above 1 forces the maximum and minimum temperatures to plateau when they're reached
		float dayPercent = MathHelper.clamp_float((float)(Math.sin(Math.toRadians(((entityLiving.worldObj.getWorldTime()%24000L)/24000D)*360F - 30F))*0.5F + 0.5F)*scale, 0F, 1F);

		if(entityLiving.worldObj.provider.hasNoSky)
		{
			isDay = false;
		}

		int lightLev = 0;
		int blockLightLev = 0;

		if(j > 0)
		{
			if(j >= 256)
			{
				lightLev = 15;
				blockLightLev = 15;
			} else
			{
				lightLev = chunk.getSavedLightValue(EnumSkyBlock.Sky, i & 0xf, j, k & 0xf);
				blockLightLev = chunk.getSavedLightValue(EnumSkyBlock.Block, i & 0xf, j, k & 0xf);
			}
		}

		if(!isDay && blockLightLev <= 1 && entityLiving.getActivePotionEffect(Potion.nightVision) == null)
		{
			if(dimensionProp == null || !dimensionProp.override || dimensionProp.darkAffectSanity)
			{
			   sanityStartRate = -0.01F;
			   sanityRate = -0.01F;
			}
		}

		// Scan a cube around the player
		int cubeRadius_squared = cubeRadius*cubeRadius;

		for(int y = -cubeRadius; y <= cubeRadius; y++)
		{
			float radiusAtVerticalSlice = MathHelper.sqrt_float(cubeRadius_squared - (y*y));
			int radiusAtVerticalSlice_floored = MathHelper.floor_float(radiusAtVerticalSlice);
			float radiusAtVerticalSlice_squared = radiusAtVerticalSlice*radiusAtVerticalSlice;

			// East-West position
			for(int x = -radiusAtVerticalSlice_floored; x <= radiusAtVerticalSlice_floored; x++)
			{
				float radiusAtXSlice = MathHelper.sqrt_float(radiusAtVerticalSlice_squared - (x*x));
				int radiusAtXSlice_floored = MathHelper.floor_float(radiusAtXSlice);

				// South-North position
				for(int z = -radiusAtXSlice_floored; z <= radiusAtXSlice_floored; z++)
				{
					if(y == 0)
					{
						Chunk testChunk = entityLiving.worldObj.getChunkFromBlockCoords((i + x), (k + z));
						BiomeGenBase checkBiome = testChunk.getBiomeGenForWorldCoords((i + x) & 15, (k + z) & 15, entityLiving.worldObj.getWorldChunkManager());

						if(checkBiome != null)
						{
							BiomeProperties biomeOverride = null;
							if(BiomeProperties.base.hasProperty(checkBiome))
							{
								biomeOverride = BiomeProperties.base.getProperty(checkBiome);
							}

							if(biomeOverride != null && biomeOverride.biomeOveride)
							{
								surroundingBiomeTempSamplesSum += biomeOverride.ambientTemp;
							}
							else
							{
								//surBiomeTemps += EnviroUtils.getBiomeTemp(checkBiome);
								surroundingBiomeTempSamplesSum += EnviroUtils.getBiomeTemp((i + x),(j + y), (k + z), checkBiome);
							}

							surroundingBiomeTempSamplesCount += 1;
						}
					}

					if(!EM_PhysManager.blockNotSolid(entityLiving.worldObj, x + i, y + j, z + k, false))
					{
						solidBlocks += 1;
					}
					totalBlocks += 1;

					dist = (float)entityLiving.getDistance(i + x, j + y, k + z);

					Block block = Blocks.air;
					int meta = 0;

					block = entityLiving.worldObj.getBlock(i + x, j + y, k + z);

					if(block != Blocks.air)
					{
						meta = entityLiving.worldObj.getBlockMetadata(i + x, j + y, k + z);
					}

					if(BlockProperties.base.hasProperty(block, meta))
					{
						BlockProperties blockProps = BlockProperties.base.getProperty(block, meta);

						if(blockProps.air > 0F)
						{
							leaves += (blockProps.air/0.1F);
						} else if(quality >= blockProps.air && blockProps.air < 0 && quality <= 0)
						{
							quality = blockProps.air;
						}
						if(blockProps.enableTemp)
						{
							if(blockAndItemTempInfluence <= getTempFalloff(blockProps.temp, dist, cubeRadius, EM_Settings.blockTempDropoffPower) && blockProps.temp > 0F)
							{
								blockAndItemTempInfluence = getTempFalloff(blockProps.temp, dist, cubeRadius, EM_Settings.blockTempDropoffPower);
							} else if(blockProps.temp < 0F)
							{
								cooling += getTempFalloff(-blockProps.temp, dist, cubeRadius, EM_Settings.blockTempDropoffPower);
							}
						}
						if(sanityRate >= blockProps.sanity && blockProps.sanity < 0 && sanityRate <= 0)
						{
							sanityRate = blockProps.sanity;
						} else if(sanityRate <= blockProps.sanity && blockProps.sanity > 0F)
						{
							if(block instanceof BlockFlower)
							{
								if(isDay || entityLiving.worldObj.provider.hasNoSky)
								{
									if(sanityBoost < blockProps.sanity)
									{
										sanityBoost = blockProps.sanity;
									}
								}
							} else
							{
								if(sanityBoost < blockProps.sanity)
								{
									sanityBoost = blockProps.sanity;
								}
							}
						}
					}

					if(block.getMaterial() == Material.lava)
					{
						nearLava = true;
					}
				}
			}
		}

		if(entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entityLiving;

			for(int slot = 0; slot < 9; slot++)
			{
				ItemStack stack = player.inventory.mainInventory[slot];

				if(stack == null)
				{
					continue;
				}

				float stackMult = 1F;

				if(stack.stackSize > 1)
				{
					stackMult = (stack.stackSize-1F)/63F + 1F;
				}

				if(ItemProperties.base.hasProperty(stack))
				{
					ItemProperties itemProps = ItemProperties.base.getProperty(stack);

					if(itemProps.ambAir > 0F)
					{
						leaves += (itemProps.ambAir/0.1F) * stackMult;
					} else if(quality >= itemProps.ambAir * stackMult && itemProps.ambAir < 0 && quality <= 0)
					{
						quality = itemProps.ambAir * stackMult;
					}
					if(blockAndItemTempInfluence <= itemProps.ambTemp * stackMult && itemProps.enableTemp && itemProps.ambTemp > 0F)
					{
						blockAndItemTempInfluence = itemProps.ambTemp * stackMult;
					} else if(itemProps.enableTemp && itemProps.ambTemp < 0F)
					{
						cooling += -itemProps.ambTemp * stackMult;
					}
					if(sanityRate >= itemProps.ambSanity * stackMult && itemProps.ambSanity < 0 && sanityRate <= 0)
					{
						sanityRate = itemProps.ambSanity * stackMult;
					} else if(sanityBoost <= itemProps.ambSanity * stackMult && itemProps.ambSanity > 0F)
					{
						if(stack.getItem() instanceof ItemBlock)
						{
							if(((ItemBlock)stack.getItem()).field_150939_a instanceof BlockFlower)
							{
								if(isDay || entityLiving.worldObj.provider.hasNoSky)
								{
									sanityBoost = itemProps.ambSanity * stackMult;
								}
							} else
							{
								sanityBoost = itemProps.ambSanity * stackMult;
							}
						} else
						{
							sanityBoost = itemProps.ambSanity * stackMult;
						}
					}
				} else if(stack.getItem() instanceof ItemBlock)
				{
					ItemBlock itemBlock = (ItemBlock)stack.getItem();
					if(itemBlock.field_150939_a instanceof BlockFlower && (isDay || entityLiving.worldObj.provider.hasNoSky) && sanityBoost <= 0.1F)
					{
						if(((BlockFlower)itemBlock.field_150939_a).getPlantType(entityLiving.worldObj, i, j, k) == EnumPlantType.Plains)
						{
							sanityBoost = 0.1F;
						}
					}
				}
			}
		}

		if(lightLev > 1 && !entityLiving.worldObj.provider.hasNoSky)
		{
			quality = 2F;
			sanityRate = 0.5F;
		} else if(sanityRate <= sanityStartRate && sanityRate > -0.1F && blockLightLev <= 1 && entityLiving.getActivePotionEffect(Potion.nightVision) == null)
		{
			sanityRate = -0.1F;
		}

		if(dimensionProp != null && entityLiving.posY > dimensionProp.sealevel * 0.75 && !entityLiving.worldObj.provider.hasNoSky)
		{
			quality = 2F;
		}


		float biomeTemperature = (surroundingBiomeTempSamplesSum / surroundingBiomeTempSamplesCount);
		float maxHighAltitudeTemp = -30F; // Max temp at high altitude
		float minLowAltitudeTemp = 30F; // Min temp at low altitude (Geothermal Heating)

		if(!entityLiving.worldObj.provider.hasNoSky)
		{
			if(entityLiving.posY < 48)
			{
				if(minLowAltitudeTemp - biomeTemperature > 0)
				{
					biomeTemperature += (minLowAltitudeTemp - biomeTemperature) * (1F - (entityLiving.posY / 48F));
				}
			} else if(entityLiving.posY > 90 && entityLiving.posY < 256)
			{
				if(maxHighAltitudeTemp - biomeTemperature < 0)
				{
					biomeTemperature -= MathHelper.abs(maxHighAltitudeTemp - biomeTemperature) * ((entityLiving.posY - 90F)/166F);
				}
			} else if(entityLiving.posY >= 256)
			{
				biomeTemperature = biomeTemperature < maxHighAltitudeTemp? biomeTemperature : maxHighAltitudeTemp;
			}
		}

		biomeTemperature -= cooling;

		if(entityLiving instanceof EntityPlayer)
		{
			if(((EntityPlayer)entityLiving).isPlayerSleeping())
			{
				biomeTemperature += 10F;
			}
		}

		if (dimensionProp != null && dimensionProp.override && !dimensionProp.weatherAffectsTemp)
		{

		}
		else
		{
			if(entityLiving.worldObj.isRaining() && biome.rainfall != 0.0F)
			{
				biomeTemperature -= 10F;
				animalHostility = -1;

				if(entityLiving.worldObj.canBlockSeeTheSky(i, j, k))
				{
					dropSpeed = 0.01F;
				}
			}

		} // Dimension Overrides End

		// 	Shade
		if(!entityLiving.worldObj.canBlockSeeTheSky(i, j, k) && isDay && !entityLiving.worldObj.isRaining())
		{
			biomeTemperature -= 2.5F;
		}

		if ((!entityLiving.worldObj.provider.hasNoSky && dimensionProp == null) || (dimensionProp != null && dimensionProp.override && dimensionProp.dayNightTemp))
		{
			biomeTemperature -= (1F-dayPercent) * 10F;

			if(biome.rainfall <= 0F)
			{
				biomeTemperature -= (1F-dayPercent) * 30F;
			}
		}

		@SuppressWarnings("unchecked")
		List<Entity> mobList = entityLiving.worldObj.getEntitiesWithinAABBExcludingEntity(entityLiving, AxisAlignedBB.getBoundingBox(entityLiving.posX - 2, entityLiving.posY - 2, entityLiving.posZ - 2, entityLiving.posX + 3, entityLiving.posY + 3, entityLiving.posZ + 3));

		Iterator<Entity> iterator = mobList.iterator();

		float avgEntityTemp = 0.0F;
		int validEntities = 0;

		EnviroDataTracker tracker = lookupTracker(entityLiving);

		if(tracker == null)
		{
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.ERROR, "Tracker updating as null! Crash imminent!");
		}

		while(iterator.hasNext())
		{
			Entity mob = (Entity)iterator.next();

			if(!(mob instanceof EntityLivingBase))
			{
				continue;
			}

			EnviroDataTracker mobTrack = lookupTracker((EntityLivingBase)mob);
			EntityProperties livingProps = null;


			if(EntityProperties.base.hasProperty(mob))
			{
				livingProps = EntityProperties.base.getProperty(mob);
			}

//			if(EntityList.getEntityID(mob) > 0)
//			{
//				if(EM_Settings.livingProperties.containsKey(EntityList.getEntityID(mob)))
//				{
//					livingProps = EM_Settings.livingProperties.get(EntityList.getEntityID(mob));
//				}
//			} else if(EntityRegistry.instance().lookupModSpawn(mob.getClass(), false) != null)
//			{
//				if(EM_Settings.livingProperties.containsKey(EntityRegistry.instance().lookupModSpawn(mob.getClass(), false).getModEntityId() + 128))
//				{
//					livingProps = EM_Settings.livingProperties.get(EntityRegistry.instance().lookupModSpawn(mob.getClass(), false).getModEntityId() + 128);
//				}
//			}

			// Villager assistance. Once per day, certain villagers will heal your sanity, hydration, high body temp; or will feed you.
			if(mob instanceof EntityVillager && entityLiving instanceof EntityPlayer && entityLiving.canEntityBeSeen(mob) && EM_Settings.villageAssist)
			{
				EntityVillager villager = (EntityVillager)mob;
				Village village = entityLiving.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(villager.posX), MathHelper.floor_double(villager.posY), MathHelper.floor_double(villager.posZ), 32);

				long assistTime = villager.getEntityData().getLong("Enviro_Assist_Time");
				long worldTime = entityLiving.worldObj.provider.getWorldTime();

				if(village != null && village.getReputationForPlayer(entityLiving.getCommandSenderName()) >= 5 && !villager.isChild() && Math.abs(worldTime - assistTime) > 24000)
				{
					if(villager.getProfession() == 2) // Priest
					{
						if(sanityBoost < 5F)
						{
							sanityBoost = 5F;
						}

						((EntityPlayer)entityLiving).addStat(EnviroAchievements.tradingFavours, 1);
					} else if(villager.getProfession() == 0 && isDay) // Farmer
					{
						if(tracker.hydration < 50F)
						{
							tracker.hydration = 100F;

							if(tracker.bodyTemp >= 38F)
							{
								tracker.bodyTemp -= 1F;
							}
							entityLiving.worldObj.playSoundAtEntity(entityLiving, "random.drink", 1.0F, 1.0F);
							villager.playSound("mob.villager.yes", 1.0F, 1.0F);
							villager.getEntityData().setLong("Enviro_Assist_Time", worldTime);

							((EntityPlayer)entityLiving).addStat(EnviroAchievements.tradingFavours, 1);
						}
					} else if(villager.getProfession() == 4 && isDay) // Butcher
					{
						FoodStats food = ((EntityPlayer)entityLiving).getFoodStats();
						if(food.getFoodLevel() <= 10)
						{
							food.setFoodLevel(20);
							entityLiving.worldObj.playSoundAtEntity(entityLiving, "random.burp", 0.5F, entityLiving.worldObj.rand.nextFloat() * 0.1F + 0.9F);
							villager.playSound("mob.villager.yes", 1.0F, 1.0F);
							villager.getEntityData().setLong("Enviro_Assist_Time", worldTime);

							((EntityPlayer)entityLiving).addStat(EnviroAchievements.tradingFavours, 1);
						}
					}
				}
			}

			if(livingProps != null && entityLiving.canEntityBeSeen(mob))
			{
				if(sanityRate >= livingProps.ambSanity && livingProps.ambSanity < 0 && sanityRate <= 0)
				{
					sanityRate = livingProps.ambSanity;
				} else if(sanityRate <= livingProps.ambSanity && livingProps.ambSanity > 0F)
				{
					if(sanityBoost < livingProps.ambSanity)
					{
						sanityBoost = livingProps.ambSanity;
					}
				}

				if(livingProps.ambAir > 0F)
				{
					leaves += (livingProps.ambAir/0.1F);
				} else if(quality >= livingProps.ambAir && livingProps.ambAir < 0 && quality <= 0)
				{
					quality = livingProps.ambAir;
				}

				dehydrateBonus -= livingProps.ambHydration;
			}

//			else if(mob instanceof EntityBat && entityLiving instanceof EntityPlayer && entityLiving.canEntityBeSeen(mob))
//			{
//				if(sanityRate <= sanityStartRate && sanityRate > -0.01F)
//				{
//					sanityRate = -0.01F;
//				}
//			} else if(mob.getCommandSenderName().toLowerCase().contains("ender") && entityLiving instanceof EntityPlayer && entityLiving.canEntityBeSeen(mob))
//			{
//				if(sanityRate <= sanityStartRate && sanityRate > -0.1F)
//				{
//					sanityRate = -0.1F;
//				}
//			} else if(((EntityLivingBase)mob).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD && entityLiving.canEntityBeSeen(mob))
//			{
//				if(sanityRate <= sanityStartRate && sanityRate > -0.01F)
//				{
//					sanityRate = -0.01F;
//				}
//			}

			if(mobTrack != null)
			{
				if(livingProps != null)
				{
					if(!livingProps.bodyTemp || !livingProps.shouldTrack)
					{
						avgEntityTemp += livingProps.ambTemp;
					} else
					{
						avgEntityTemp += mobTrack.bodyTemp;
					}
				} else
				{
					avgEntityTemp += mobTrack.bodyTemp;
				}
				validEntities += 1;
			} else
			{
				if(livingProps != null)
				{
					if(!livingProps.bodyTemp || !livingProps.shouldTrack)
					{
						avgEntityTemp += livingProps.ambTemp;
					} else
					{
						avgEntityTemp += 37F;
					}
					validEntities += 1;
				} else if(!(mob instanceof EntityMob))
				{
					avgEntityTemp += 37F;
					validEntities += 1;
				}
			}
		}

		if(validEntities > 0)
		{
			avgEntityTemp /= validEntities;

			if(biomeTemperature < avgEntityTemp - 12F)
			{
				biomeTemperature = (biomeTemperature + (avgEntityTemp - 12F)) / 2;
			}
		}

		float fireProt = 0;

		{
			ItemStack helmet = entityLiving.getEquipmentInSlot(4);
			ItemStack plate = entityLiving.getEquipmentInSlot(3);
			ItemStack legs = entityLiving.getEquipmentInSlot(2);
			ItemStack boots = entityLiving.getEquipmentInSlot(1);

			float tempMultTotal = 0F;
			float addTemp = 0F;

			if(helmet != null)
			{
				NBTTagList enchTags = helmet.getEnchantmentTagList();

				if(enchTags != null)
				{
					for(int index = 0; index < enchTags.tagCount(); index++)
					{
						int enID = ((NBTTagCompound)enchTags.getCompoundTagAt(index)).getShort("id");
						int enLV = ((NBTTagCompound)enchTags.getCompoundTagAt(index)).getShort("lvl");

						if(enID == Enchantment.respiration.effectId)
						{
							leaves += 3F * enLV;
						} else if(enID == Enchantment.fireProtection.effectId)
						{
							fireProt += enLV;
						}
					}
				}

				if(ArmorProperties.base.hasProperty(helmet))
				{
					ArmorProperties props = ArmorProperties.base.getProperty(helmet);

					if(isDay)
					{
						if(entityLiving.worldObj.canBlockSeeTheSky(i, j, k) && biomeTemperature > 0F)
						{
							tempMultTotal += (props.sunMult - 1.0F);
							addTemp += props.sunTemp;
						} else
						{
							tempMultTotal += (props.shadeMult - 1.0F);
							addTemp += props.shadeTemp;
						}
					} else
					{
						tempMultTotal += (props.nightMult - 1.0F);
						addTemp += props.nightTemp;
					}

					if(props.air > 0F)
					{
						leaves += (props.air/0.1F);
					} else if(quality >= props.air && props.air < 0 && quality <= 0)
					{
						quality = props.air;
					}

					if(sanityRate >= props.sanity && props.sanity < 0 && sanityRate <= 0)
					{
						sanityRate = props.sanity;
					} else if(sanityBoost <= props.sanity && props.sanity > 0F)
					{
						sanityBoost = props.sanity;
					}
				}
			}
			if(plate != null)
			{
				NBTTagList enchTags = plate.getEnchantmentTagList();

				if(enchTags != null)
				{
					for(int index = 0; index < enchTags.tagCount(); index++)
					{
						int enID = ((NBTTagCompound)enchTags.getCompoundTagAt(index)).getShort("id");
						int enLV = ((NBTTagCompound)enchTags.getCompoundTagAt(index)).getShort("lvl");

						if(enID == Enchantment.fireProtection.effectId)
						{
							fireProt += enLV;
						}
					}
				}

				if(ArmorProperties.base.hasProperty(plate))
				{
					ArmorProperties props = ArmorProperties.base.getProperty(plate);

					if(isDay)
					{
						if(entityLiving.worldObj.canBlockSeeTheSky(i, j, k) && biomeTemperature > 0F)
						{
							tempMultTotal += (props.sunMult - 1.0F);
							addTemp += props.sunTemp;
						} else
						{
							tempMultTotal += (props.shadeMult - 1.0F);
							addTemp += props.shadeTemp;
						}
					} else
					{
						tempMultTotal += (props.nightMult - 1.0F);
						addTemp += props.nightTemp;
					}

					if((quality <= props.air && props.air > 0F) || (quality >= props.air && props.air < 0 && quality <= 0))
					{
						quality = props.air;
					}

					if(sanityRate >= props.sanity && props.sanity < 0 && sanityRate <= 0)
					{
						sanityRate = props.sanity;
					} else if(sanityBoost <= props.sanity && props.sanity > 0F)
					{
						sanityBoost = props.sanity;
					}
				}
			}
			if(legs != null)
			{
				NBTTagList enchTags = legs.getEnchantmentTagList();

				if(enchTags != null)
				{
					for(int index = 0; index < enchTags.tagCount(); index++)
					{
						int enID = ((NBTTagCompound)enchTags.getCompoundTagAt(index)).getShort("id");
						int enLV = ((NBTTagCompound)enchTags.getCompoundTagAt(index)).getShort("lvl");

						if(enID == Enchantment.fireProtection.effectId)
						{
							fireProt += enLV;
						}
					}
				}

				if(ArmorProperties.base.hasProperty(legs))
				{
					ArmorProperties props = ArmorProperties.base.getProperty(legs);

					if(isDay)
					{
						if(entityLiving.worldObj.canBlockSeeTheSky(i, j, k) && biomeTemperature > 0F)
						{
							tempMultTotal += (props.sunMult - 1.0F);
							addTemp += props.sunTemp;
						} else
						{
							tempMultTotal += (props.shadeMult - 1.0F);
							addTemp += props.shadeTemp;
						}
					} else
					{
						tempMultTotal += (props.nightMult - 1.0F);
						addTemp += props.nightTemp;
					}

					if((quality <= props.air && props.air > 0F) || (quality >= props.air && props.air < 0 && quality <= 0))
					{
						quality = props.air;
					}

					if(sanityRate >= props.sanity && props.sanity < 0 && sanityRate <= 0)
					{
						sanityRate = props.sanity;
					} else if(sanityBoost <= props.sanity && props.sanity > 0F)
					{
						sanityBoost = props.sanity;
					}
				}
			}
			if(boots != null)
			{
				NBTTagList enchTags = boots.getEnchantmentTagList();

				if(enchTags != null)
				{
					for(int index = 0; index < enchTags.tagCount(); index++)
					{
						int enID = ((NBTTagCompound)enchTags.getCompoundTagAt(index)).getShort("id");
						int enLV = ((NBTTagCompound)enchTags.getCompoundTagAt(index)).getShort("lvl");

						if(enID == Enchantment.fireProtection.effectId)
						{
							fireProt += enLV;
						}
					}
				}

				if(ArmorProperties.base.hasProperty(boots))
				{
					ArmorProperties props = ArmorProperties.base.getProperty(boots);

					if(isDay)
					{
						if(entityLiving.worldObj.canBlockSeeTheSky(i, j, k) && biomeTemperature > 0F)
						{
							tempMultTotal += (props.sunMult - 1.0F);
							addTemp += props.sunTemp;
						} else
						{
							tempMultTotal += (props.shadeMult - 1.0F);
							addTemp += props.shadeTemp;
						}
					} else
					{
						tempMultTotal += (props.nightMult - 1.0F);
						addTemp += props.nightTemp;
					}

					if((quality <= props.air && props.air > 0F) || (quality >= props.air && props.air < 0 && quality <= 0))
					{
						quality = props.air;
					}

					if(sanityRate >= props.sanity && props.sanity < 0 && sanityRate <= 0)
					{
						sanityRate = props.sanity;
					} else if(sanityBoost <= props.sanity && props.sanity > 0F)
					{
						sanityBoost = props.sanity;
					}
				}
			}

			biomeTemperature *= (1F + tempMultTotal);
			biomeTemperature += addTemp;
			fireProt = 1F - fireProt/18F;
		}

		if(entityLiving.isInWater())
		{
			if(biomeTemperature > 25F)
			{
				if(biomeTemperature > 50F)
				{
					biomeTemperature -= 50F;
				} else
				{
					biomeTemperature = 25F;
				}
			}
			dropSpeed = 0.01F;
		}

		float ambientTemperature = 0F;

		if(blockAndItemTempInfluence > biomeTemperature)
		{
			ambientTemperature = (biomeTemperature + blockAndItemTempInfluence) / 2;
			if(blockAndItemTempInfluence > (biomeTemperature + 5F))
			{
				riseSpeed = 0.005F;
			}
		} else
		{
			ambientTemperature = biomeTemperature;
		}

		if(entityLiving.getActivePotionEffect(Potion.hunger) != null)
		{
			dehydrateBonus += 0.1F;
		}

		if(nearLava)
		{
			if(riseSpeed <= 0.005F)
			{
				riseSpeed = 0.005F;
			}
			dehydrateBonus += 0.05F;
			if(animalHostility == 0)
			{
				animalHostility = 1;
			}
		}

		BiomeProperties biomeProp = null;
		if(BiomeProperties.base.hasProperty(biome))
		{
			 biomeProp = BiomeProperties.base.getProperty(biome);

			 if(biomeProp != null && biomeProp.biomeOveride)
				{
					dehydrateBonus += biomeProp.dehydrateRate;

					if(biomeProp.tempRate > 0)
					{
						riseSpeed += biomeProp.tempRate;
					} else
					{
						dropSpeed += biomeProp.tempRate;
					}

					sanityRate += biomeProp.sanityRate;
				}

		}

		if(biome.getIntRainfall() == 0 && isDay)
		{
			dehydrateBonus += 0.05F;
			if(animalHostility == 0)
			{
				animalHostility = 1;
			}
		}

		if(!entityLiving.isPotionActive(Potion.fireResistance))
		{
			if(entityLiving.worldObj.getBlock(i, j, k) == Blocks.lava || entityLiving.worldObj.getBlock(i, j, k) == Blocks.flowing_lava)
			{
				ambientTemperature = 200F;
				riseSpeed = 1.0F;
			} else if(entityLiving.isBurning())
			{
				if(ambientTemperature <= 75F)
				{
					ambientTemperature = 75F;
				}

				if(riseSpeed < 0.1F)
				{
					riseSpeed = 0.1F;
				}
			}
		}

		quality += (leaves * 0.1F);
		sanityRate += sanityBoost;

		if(quality < 0)
		{
			quality *= ((float)solidBlocks)/totalBlocks;
		}

		if(entityLiving.isSprinting())
		{
			dehydrateBonus += 0.05F;
			if(riseSpeed < 0.01F)
			{
				riseSpeed = 0.01F;
			}
			ambientTemperature += 2F;
		}

		if(dimensionProp != null && dimensionProp.override)
		{
			quality = quality * (float) dimensionProp.airMulti + dimensionProp.airRate;
			riseSpeed = riseSpeed * (float) dimensionProp.tempMulti + dimensionProp.tempRate;
			dropSpeed = dropSpeed * (float) dimensionProp.tempMulti + dimensionProp.tempRate;
			sanityRate = sanityRate * (float) dimensionProp.sanityMulti + dimensionProp.sanityRate;
			dehydrateBonus = dehydrateBonus * (float) dimensionProp.hydrationMulti + dimensionProp.hydrationRate;
		}

		// Air quality delta
		data[AIR_QUALITY_DELTA_INDEX] = quality * (float)EM_Settings.airMult;
		// Air temp
		data[AMBIENT_TEMP_INDEX] = entityLiving.isPotionActive(Potion.fireResistance) && ambientTemperature > 37F? 37F : (ambientTemperature > 37F? 37F + ((ambientTemperature-37F) * fireProt): ambientTemperature);
		// Is "near lava"?
		data[NEAR_LAVA_INDEX] = nearLava? 1 : 0;
		// Dehydration
		data[DEHYDRATION_DELTA_INDEX] = dehydrateBonus * (float)EM_Settings.hydrationMult;
		data[BODY_TEMP_DROP_SPEED_INDEX] = dropSpeed * (float)EM_Settings.tempMult;
		data[BODY_TEMP_RISE_SPEED_INDEX] = riseSpeed * (float)EM_Settings.tempMult * (tracker.bodyTemp < 37F? 1F : fireProt);
		data[ANIMAL_HOSTILITY_INDEX] = animalHostility;
		data[SANITY_DELTA_INDEX] = sanityRate * (float)EM_Settings.sanityMult;

		if(EnviroMine.proxy.isClient() && entityLiving.getCommandSenderName().equals(Minecraft.getMinecraft().thePlayer.getCommandSenderName()) && timer.isRunning())
		{
			timer.stop();
			Debug_Info.DB_timer = timer.toString();
			timer.reset();
		}
		return data;
	}

	public static float getBiomeTemprature(int x, int y, BiomeGenBase biome)
	{
		float temp= 0F;


		return temp;

	}

	public static void removeTracker(EnviroDataTracker tracker)
	{
		if(trackerList.containsValue(tracker))
		{
			tracker.isDisabled = true;
			if(tracker.trackedEntity instanceof EntityPlayer)
			{
				trackerList.remove(tracker.trackedEntity.getCommandSenderName());
			} else
			{
				trackerList.remove("" + tracker.trackedEntity.getEntityId());
			}
		}
	}

	public static void saveAndRemoveTracker(EnviroDataTracker tracker)
	{
		if(trackerList.containsValue(tracker))
		{
			tracker.isDisabled = true;
			NBTTagCompound tags = tracker.trackedEntity.getEntityData();
			tags.setFloat("ENVIRO_AIR", tracker.airQuality);
			tags.setFloat("ENVIRO_HYD", tracker.hydration);
			tags.setFloat("ENVIRO_TMP", tracker.bodyTemp);
			tags.setFloat("ENVIRO_SAN", tracker.sanity);
            tags.setFloat("ENVIRO_BLD", tracker.blood);
			if(tracker.trackedEntity instanceof EntityPlayer)
			{
				trackerList.remove(tracker.trackedEntity.getCommandSenderName());
			} else
			{
				trackerList.remove("" + tracker.trackedEntity.getEntityId());
			}
		}
	}

	public static void saveTracker(EnviroDataTracker tracker)
	{
		NBTTagCompound tags = tracker.trackedEntity.getEntityData();
		tags.setFloat("ENVIRO_AIR", tracker.airQuality);
		tags.setFloat("ENVIRO_HYD", tracker.hydration);
		tags.setFloat("ENVIRO_TMP", tracker.bodyTemp);
		tags.setFloat("ENVIRO_SAN", tracker.sanity);
        tags.setFloat("ENVIRO_BLD", tracker.blood);
	}

	public static void removeAllTrackers()
	{
		Iterator<EnviroDataTracker> iterator = trackerList.values().iterator();
		while(iterator.hasNext())
		{
			EnviroDataTracker tracker = iterator.next();
			tracker.isDisabled = true;
		}

		trackerList.clear();
	}

	public static void saveAndDeleteAllTrackers()
	{
		Iterator<EnviroDataTracker> iterator = trackerList.values().iterator();
		while(iterator.hasNext())
		{
			EnviroDataTracker tracker = iterator.next();
			tracker.isDisabled = true;
			NBTTagCompound tags = tracker.trackedEntity.getEntityData();
			tags.setFloat("ENVIRO_AIR", tracker.airQuality);
			tags.setFloat("ENVIRO_HYD", tracker.hydration);
			tags.setFloat("ENVIRO_TMP", tracker.bodyTemp);
			tags.setFloat("ENVIRO_SAN", tracker.sanity);
            tags.setFloat("ENVIRO_BLD", tracker.blood);
		}
		trackerList.clear();
	}

	public static void saveAndDeleteWorldTrackers(World world)
	{
		HashMap<String,EnviroDataTracker> tempList = new HashMap<String,EnviroDataTracker>(trackerList);
		Iterator<EnviroDataTracker> iterator = tempList.values().iterator();
		while(iterator.hasNext())
		{
			EnviroDataTracker tracker = iterator.next();

			if(tracker.trackedEntity.worldObj == world)
			{
				NBTTagCompound tags = tracker.trackedEntity.getEntityData();
				tags.setFloat("ENVIRO_AIR", tracker.airQuality);
				tags.setFloat("ENVIRO_HYD", tracker.hydration);
				tags.setFloat("ENVIRO_TMP", tracker.bodyTemp);
				tags.setFloat("ENVIRO_SAN", tracker.sanity);
                tags.setFloat("ENVIRO_BLD", tracker.blood);
				tracker.isDisabled = true;
				if(tracker.trackedEntity instanceof EntityPlayer)
				{
					trackerList.remove(tracker.trackedEntity.getCommandSenderName());
				} else
				{
					trackerList.remove("" + tracker.trackedEntity.getEntityId());
				}
			}
		}
	}

	public static void saveAllWorldTrackers(World world)
	{
		HashMap<String,EnviroDataTracker> tempList = new HashMap<String,EnviroDataTracker>(trackerList);
		Iterator<EnviroDataTracker> iterator = tempList.values().iterator();
		while(iterator.hasNext())
		{
			EnviroDataTracker tracker = iterator.next();

			if(tracker.trackedEntity.worldObj == world)
			{
				NBTTagCompound tags = tracker.trackedEntity.getEntityData();
				tags.setFloat("ENVIRO_AIR", tracker.airQuality);
				tags.setFloat("ENVIRO_HYD", tracker.hydration);
				tags.setFloat("ENVIRO_TMP", tracker.bodyTemp);
				tags.setFloat("ENVIRO_SAN", tracker.sanity);
                tags.setFloat("ENVIRO_BLD", tracker.blood);
			}
		}
	}

	public static EntityPlayer findPlayer(String username)
	{
		World[] worlds = new World[1];

		if(EnviroMine.proxy.isClient())
		{
			if(Minecraft.getMinecraft().isIntegratedServerRunning())
			{
				worlds = MinecraftServer.getServer().worldServers;
			} else
			{
				worlds[0] = Minecraft.getMinecraft().thePlayer.worldObj;
			}
		} else
		{
			worlds = MinecraftServer.getServer().worldServers;
		}

		for(int i = worlds.length - 1; i >= 0; i -= 1)
		{
			if(worlds[i] == null)
			{
				continue;
			}
			EntityPlayer player = worlds[i].getPlayerEntityByName(username);

			if(player != null)
			{
				if(player.isEntityAlive())
				{
					return player;
				}
			}
		}

		return null;
	}

	public static void createFX(EntityLivingBase entityLiving)
	{
		float rndX = (entityLiving.getRNG().nextFloat() * entityLiving.width * 2) - entityLiving.width;
		float rndY = entityLiving.getRNG().nextFloat() * entityLiving.height;
		float rndZ = (entityLiving.getRNG().nextFloat() * entityLiving.width * 2) - entityLiving.width;
		EnviroDataTracker tracker = lookupTracker(entityLiving);

		if(entityLiving instanceof EntityPlayer && !(entityLiving instanceof EntityPlayerMP))
		{
			rndY = -rndY;
		}

		if(tracker != null)
		{
			if(tracker.bodyTemp >= 38F && UI_Settings.sweatParticals)
			{
				entityLiving.worldObj.spawnParticle("dripWater", entityLiving.posX + rndX, entityLiving.posY + rndY, entityLiving.posZ + rndZ, 0.0D, 0.0D, 0.0D);
			}

			if(tracker.trackedEntity.isPotionActive(EnviroPotion.insanity) && UI_Settings.insaneParticals)
			{
				entityLiving.worldObj.spawnParticle("portal", entityLiving.posX + rndX, entityLiving.posY + rndY, entityLiving.posZ + rndZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	public static float getTempFalloff(float blockTemperature, float cartesianDistance, int scanRadius, float dropoffPower)
	{
		return cartesianDistance <= scanRadius ? (float) (blockTemperature * (1 - Math.pow(MathHelper.clamp_float(cartesianDistance-EM_Settings.auraRadius, 0, scanRadius)/scanRadius, 1F/dropoffPower))) : 0F;
	}
}
