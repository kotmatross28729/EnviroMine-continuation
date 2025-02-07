package enviromine.handlers;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enviromine.EntityPhysicsBlock;
import enviromine.EnviroDamageSource;
import enviromine.EnviroPotion;
import enviromine.blocks.tiles.TileEntityGas;
import enviromine.client.gui.menu.config.EM_ConfigMenu;
import enviromine.core.EM_ConfigHandler;
import enviromine.core.EM_ConfigHandler.EnumLogVerbosity;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.gases.GasBuffer;
import enviromine.handlers.compat.EM_EventManager_NTM;
import enviromine.network.packet.PacketEnviroMine;
import enviromine.trackers.EnviroDataTracker;
import enviromine.trackers.Hallucination;
import enviromine.trackers.properties.BiomeProperties;
import enviromine.trackers.properties.CaveSpawnProperties;
import enviromine.trackers.properties.DimensionProperties;
import enviromine.trackers.properties.EntityProperties;
import enviromine.trackers.properties.ItemProperties;
import enviromine.trackers.properties.RotProperties;
import enviromine.utils.ArmorTempUtils;
import enviromine.utils.EnviroUtils;
import enviromine.utils.misc.CompatSafe;
import enviromine.world.Earthquake;
import enviromine.world.features.mineshaft.MineshaftBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox.TileEntityJukebox;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Unload;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

@CompatSafe
public class EM_EventManager
{
	private static final String BLOOD_BLOCK_BOP = "BiomesOPlenty:hell_blood";
	private static final String WATER_ROOT_STREAMS = "streams:river/tile.water";

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		boolean chunkPhys = true;

		DimensionProperties dProps = EM_Settings.dimensionProperties.get(event.world.provider.dimensionId);

		if(!event.world.isRemote)
		{
			if(EM_PhysManager.chunkDelay.containsKey(event.world.provider.dimensionId + "" + (MathHelper.floor_double(event.entity.posX) >> 4) + "," + (MathHelper.floor_double(event.entity.posZ) >> 4)))
			{
				chunkPhys = (EM_PhysManager.chunkDelay.get(event.world.provider.dimensionId + "" + (MathHelper.floor_double(event.entity.posX) >> 4) + "," + (MathHelper.floor_double(event.entity.posZ) >> 4)) < event.world.getTotalWorldTime());
			}
		}

		if((dProps != null && !dProps.physics) || !EM_Settings.enablePhysics)
		{
			chunkPhys = false;
		}

		if(EM_Settings.foodSpoiling)
		{
			if(event.entity instanceof EntityItem item)
			{
                ItemStack rotStack = RotHandler.doRot(event.world, item.getEntityItem()); 

				if(item.getEntityItem() != rotStack)
				{
					item.setEntityItemStack(rotStack);
				}
			} else if(event.entity instanceof EntityPlayer)
			{
				IInventory invo = ((EntityPlayer)event.entity).inventory;
				RotHandler.rotInvo(event.world, invo);	//For player	
			} else if(event.entity instanceof IInventory invo)
			{
                RotHandler.rotInvo(event.world, invo);	//For non-player entities 
			}
		}

		if(event.entity instanceof EntityLivingBase)
		{
			// Ensure that only one set of trackers are made per Minecraft instance.
			boolean allowTracker = !(event.world.isRemote && EnviroMine.proxy.isClient() && Minecraft.getMinecraft().isIntegratedServerRunning());

          if(EnviroDataTracker.isLegalType((EntityLivingBase) event.entity) && (event.entity instanceof EntityPlayer || EM_Settings.trackNonPlayer) && allowTracker)
{
				EnviroDataTracker tracker = EM_StatusManager.lookupTracker((EntityLivingBase)event.entity);
				boolean hasOld = tracker != null && !tracker.isDisabled;

				if(!hasOld)
				{
					EnviroDataTracker emTrack = new EnviroDataTracker((EntityLivingBase) event.entity);
					EM_StatusManager.addToManager(emTrack);
					emTrack.loadNBTTags();
					if(!EnviroMine.proxy.isClient() || EnviroMine.proxy.isOpenToLAN())
					{
						EM_StatusManager.syncMultiplayerTracker(emTrack);
					}
				} else
				{
					tracker.trackedEntity = (EntityLivingBase)event.entity;
				}

				if (event.entity instanceof EntityPlayerMP && !event.world.isRemote)
				{
					NBTTagCompound pData = new NBTTagCompound();
					pData.setInteger("id", 4);
					pData.setString("player", event.entity.getCommandSenderName());
					pData.setBoolean("enableAirQ", EM_Settings.enableAirQ);
					pData.setBoolean("enableBodyTemp", EM_Settings.enableBodyTemp);
					pData.setBoolean("enableHydrate", EM_Settings.enableHydrate);
					pData.setBoolean("enableSanity", EM_Settings.enableSanity);

					EnviroMine.instance.network.sendTo(new PacketEnviroMine(pData), (EntityPlayerMP) event.entity);

				}
			}
		} else if(event.entity instanceof EntityFallingBlock oldSand && !(event.entity instanceof EntityPhysicsBlock) && !event.world.isRemote && event.world.getTotalWorldTime() > EM_PhysManager.worldStartTime + EM_Settings.worldDelay && chunkPhys)
		{

            if(oldSand.func_145805_f() != Blocks.air)
			{
				NBTTagCompound oldTags = new NBTTagCompound();
				oldSand.writeToNBT(oldTags);

				EntityPhysicsBlock newSand = new EntityPhysicsBlock(oldSand.worldObj, oldSand.prevPosX, oldSand.prevPosY, oldSand.prevPosZ, oldSand.func_145805_f(), oldSand.field_145814_a, true);
				newSand.readFromNBT(oldTags);
				event.world.spawnEntityInWorld(newSand);
				event.setCanceled(true);
				event.entity.setDead();
				return;
			}
		}
	}

	@SubscribeEvent
	public void onLivingSpawn(LivingSpawnEvent.CheckSpawn event)
	{
		if(EM_Settings.enforceWeights)
		{
			if(EnviroMine.caves.totalSpawnWeight > 0 && event.world.provider.dimensionId == EM_Settings.caveDimID && EM_Settings.caveSpawnProperties.containsKey(EntityList.getEntityID(event.entity)))
			{
				CaveSpawnProperties props = EM_Settings.caveSpawnProperties.get(EntityList.getEntityID(event.entity));

				if(event.world.rand.nextInt(EnviroMine.caves.totalSpawnWeight) > props.weight)
				{
					event.setResult(Result.DENY);
					return;
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event)
	{
		EnviroDataTracker tracker = EM_StatusManager.lookupTracker(event.original);

		if(tracker != null && !tracker.isDisabled)
		{
			tracker.trackedEntity = event.entityPlayer;

			if(event.wasDeath && !EM_Settings.keepStatus)
			{
				tracker.resetData();
				EM_StatusManager.saveTracker(tracker);
			} else if(event.wasDeath)
			{
				tracker.ClampSafeRange();
				EM_StatusManager.saveTracker(tracker);
			}

			tracker.loadNBTTags();
		}

		if(event.wasDeath)
		{
			doDeath(event.entityPlayer);
			doDeath(event.original);
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event)
	{
		doDeath(event.entityLiving);

		if(event.entityLiving instanceof EntityMob && event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer player)
		{
            EnviroDataTracker tracker = EM_StatusManager.lookupTracker(player);

			if(player.isPotionActive(EnviroPotion.insanity) && player.getActivePotionEffect(EnviroPotion.insanity).getAmplifier() >= 2)
			{
				int val = player.getEntityData().getInteger("EM_MIND_MAT") + 1;
				player.getEntityData().setInteger("EM_MIND_MAT", val);

				if(val >= 5)
				{
					player.addStat(EnviroAchievements.mindOverMatter, 1);
				}
			}

			// If player kill mob give some sanity back
            if(tracker != null && tracker.sanity < 100 && !(event.entityLiving instanceof EntityAnimal)) //DONT TOUCH
			{
				tracker.sanity += event.entityLiving.worldObj.rand.nextInt(5);
			}


		}
	}

	public static void doDeath(EntityLivingBase entityLiving)
	{
		if(entityLiving instanceof EntityPlayer)
		{
			if(entityLiving.getEntityData().hasKey("EM_MINE_TIME"))
			{
				entityLiving.getEntityData().removeTag("EM_MINE_TIME");
			}

			if(entityLiving.getEntityData().hasKey("EM_WINTER"))
			{
				entityLiving.getEntityData().removeTag("EM_WINTER");
			}

			if(entityLiving.getEntityData().hasKey("EM_CAVE_DIST"))
			{
				entityLiving.getEntityData().removeTag("EM_CAVE_DIST");
			}

			if(entityLiving.getEntityData().hasKey("EM_SAFETY"))
			{
				entityLiving.getEntityData().removeTag("EM_SAFETY");
			}

			if(entityLiving.getEntityData().hasKey("EM_MIND_MAT"))
			{
				entityLiving.getEntityData().removeTag("EM_MIND_MAT");
			}

			if(entityLiving.getEntityData().hasKey("EM_THAT"))
			{
				entityLiving.getEntityData().removeTag("EM_THAT");
			}

			if(entityLiving.getEntityData().hasKey("EM_BOILED"))
			{
				entityLiving.getEntityData().removeTag("EM_BOILED");
			}

			if(entityLiving.getEntityData().hasKey("EM_PITCH"))
			{
				entityLiving.getEntityData().removeTag("EM_PITCH");
			}
		}
	}

	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent event)
	{
		if(event.entityLiving.worldObj.isRemote)
		{
			return;
		}

		Entity attacker = event.source.getEntity();

		// Special handler for when a block falls on a player who is wearing the HARD HAT
		if(
				(event.source == DamageSource.fallingBlock || event.source == DamageSource.anvil || event.source == EnviroDamageSource.landslide || event.source == EnviroDamageSource.avalanche)
				&& event.entityLiving.getEquipmentInSlot(4) != null && event.entityLiving.getEquipmentInSlot(4).getItem() == ObjectHandler.hardHat
				)
		{
			ItemStack hardHat = event.entityLiving.getEquipmentInSlot(4);
			int helmet_durability = (hardHat.getMaxDamage() + 1) - hardHat.getItemDamage();
			int block_damage_amount = MathHelper.ceiling_float_int(event.ammount);
			event.setCanceled(true);
			// Damage the hard hat by some amount
			hardHat.damageItem(block_damage_amount, event.entityLiving);

			if(helmet_durability >= block_damage_amount)
			{
				// Play stone dig sound but do no damage to the player
				event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving, "dig.stone", 1.0F, 1.0F);
				return;
			} else
			{
				// Damage the player
				event.entityLiving.attackEntityFrom(event.source, block_damage_amount - helmet_durability);
				return;
			}
		}

		if(event.source == DamageSource.fallingBlock && event.entityLiving instanceof EntityPlayer)
		{
			event.entityLiving.getEntityData().setLong("EM_SAFETY", event.entityLiving.worldObj.getTotalWorldTime());
		}

		if(event.source == EnviroDamageSource.gasfire && event.entityLiving instanceof EntityPlayer)
		{
			event.entityLiving.getEntityData().setLong("EM_THAT", event.entityLiving.worldObj.getTotalWorldTime());
		}

		if(event.entityLiving instanceof EntityPlayer && event.entityLiving.getEntityData().hasKey("EM_MIND_MAT"))
		{
			event.entityLiving.getEntityData().removeTag("EM_MIND_MAT");
		}

		if(attacker != null)
		{
			EnviroDataTracker tracker = EM_StatusManager.lookupTracker(event.entityLiving);

			if(event.entityLiving instanceof EntityPlayer player)
			{

                if(player.capabilities.disableDamage || player.capabilities.isCreativeMode)
				{
					return;
				}
			}

			if(tracker != null)
			{
				EntityProperties livingProps = null;

				if(EntityList.getEntityString(attacker) != null)
				{
					if(EntityProperties.base.hasProperty(attacker))
					{
						livingProps = EntityProperties.base.getProperty(attacker);
					}
				}

				if(livingProps != null)
				{
					tracker.sanity += livingProps.hitSanity;
					tracker.airQuality += livingProps.hitAir;
					tracker.hydration += livingProps.hitHydration;

					if(!livingProps.bodyTemp)
					{
						tracker.bodyTemp += livingProps.hitTemp;
					}
				}
			}
		}
	}
	
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if(EnviroMine.isHbmLoaded) {
			EM_EventManager_NTM.handleOnBlockBreakCoal(event);
        }
    }
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack item = event.entityPlayer.getCurrentEquippedItem();

		if(event.action == Action.RIGHT_CLICK_BLOCK && EM_Settings.foodSpoiling)
		{
			TileEntity tile = event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z);

			if(tile != null & tile instanceof IInventory) {
				//TODO: mixins
//				boolean blocked = false;
//				
//				if(EnviroMine.isCFBHLoaded) {
//					if(tile instanceof TileFridge frigde) {
//						blocked = true;
//						LogManager.getLogger().fatal("FRIDGE : " + frigde);
//					}
//				}
//				
//				if(EnviroMine.isCFMLoaded) {
//					if(tile instanceof TileEntityFreezer Freezer) {
//						blocked = true;
//						LogManager.getLogger().fatal("Freezer : " + Freezer);
//					}
//					if(tile instanceof TileEntityFridge frigde) {
//						blocked = true;
//						LogManager.getLogger().fatal("Fridge : " + frigde);
//					}
//				}
				
	//			if(!blocked)
					RotHandler.rotInvo(event.entityPlayer.worldObj, (IInventory) tile);    //For every tileentity with inventory
			}
		}
		
//		if(!event.entityPlayer.worldObj.isRemote)
//		{
//			EnviroMine.logger.info("CreatureType: " + EnviroUtils.getWitcheryCreatureType(event.entityPlayer));
//			EnviroMine.logger.info("Vampire level: " + EnviroUtils.getWitcheryVampireLevel(event.entityPlayer));
//			EnviroMine.logger.info("Werewolf level: " + EnviroUtils.getWitcheryWerewolfLevel(event.entityPlayer));
//			EnviroMine.logger.info("Is android: " + EnviroUtils.isPlayerMOAndroid(event.entityPlayer));

//			MovingObjectPosition mop = getMovingObjectPositionFromPlayer(event.entityPlayer.worldObj, event.entityPlayer, true);
//
//			if(mop != null)
//			{
//				if(mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
//				{
//					int i = mop.blockX;
//					int j = mop.blockY;
//					int k = mop.blockZ;
//
//					int[] hitBlock = EnviroUtils.getAdjacentBlockCoordsFromSide(i, j, k, mop.sideHit);
//
//					int x = hitBlock[0];
//					int y = hitBlock[1];
//					int z = hitBlock[2];
//
//					if(event.entityPlayer.worldObj.getBlock(i, j, k).getMaterial() != Material.water && event.entityPlayer.worldObj.getBlock(x, y, z).getMaterial() == Material.water)
//					{
//						i = x;
//						j = y;
//						k = z;
//					}
//
//					Block targetBlock = event.entityPlayer.worldObj.getBlock(i, j, k);
//					String targetBlockRegistryName = Block.blockRegistry.getNameForObject(targetBlock);
//
//					if (targetBlockRegistryName.contains(BLOOD_BLOCK_BOP))
//					{
//						event.entityPlayer.addChatComponentMessage(new ChatComponentText("MOP block: " + targetBlockRegistryName));
//
//						event.setCanceled(true);
//						return;
//					}
//				}
//			}
//		}

		if(event.getResult() != Result.DENY && event.action == Action.RIGHT_CLICK_BLOCK && item != null)
		{
			if(item.getItem() instanceof ItemBlock && !event.entityPlayer.worldObj.isRemote)
			{
				int[] adjCoords = EnviroUtils.getAdjacentBlockCoordsFromSide(event.x, event.y, event.z, event.face);
				EM_PhysManager.schedulePhysUpdate(event.entityPlayer.worldObj, adjCoords[0], adjCoords[1], adjCoords[2], true, "Normal");
			} else if( (item.getItem() == Items.glass_bottle || item.getItem() == ObjectHandlerCompat.waterBottle_polymer) && !event.entityPlayer.worldObj.isRemote)
			{
				if(event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) == Blocks.cauldron && event.entityPlayer.worldObj.getBlockMetadata(event.x, event.y, event.z) > 0)
				{
					fillBottle(event.entityPlayer.worldObj, event.entityPlayer, event.x, event.y, event.z, item, event, item.getItem() == ObjectHandlerCompat.waterBottle_polymer);
				}
			} else if(item.getItem() == Items.record_11)
			{
				RecordEasterEgg(event.entityPlayer, event.x, event.y, event.z);
			}
		} else if(event.getResult() != Result.DENY && event.action == Action.RIGHT_CLICK_BLOCK && item == null)
		{
			if(!event.entityPlayer.worldObj.isRemote)
			{
				drinkWater(event.entityPlayer, event);
			}
		} else if(event.getResult() != Result.DENY && event.action == Action.LEFT_CLICK_BLOCK)
		{
			EM_PhysManager.schedulePhysUpdate(event.entityPlayer.worldObj, event.x, event.y, event.z, true, "Normal");
		} else if(event.getResult() != Result.DENY && event.action == Action.RIGHT_CLICK_AIR && item != null)
		{
			if( (item.getItem() instanceof ItemGlassBottle || item.getItem() == ObjectHandlerCompat.waterBottle_polymer) && !event.entityPlayer.worldObj.isRemote)
			{
				// Is not a cauldron with water
				if(!(event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) == Blocks.cauldron && event.entityPlayer.worldObj.getBlockMetadata(event.x, event.y, event.z) > 0))
				{
					fillBottle(event.entityPlayer.worldObj, event.entityPlayer, event.x, event.y, event.z, item, event, item.getItem() == ObjectHandlerCompat.waterBottle_polymer);
				}
			}
		} else if(event.getResult() != Result.DENY && event.action == Action.RIGHT_CLICK_AIR && item == null)
		{
			NBTTagCompound pData = new NBTTagCompound();
			pData.setInteger("id", 1);
			pData.setString("player", event.entityPlayer.getCommandSenderName());
			EnviroMine.instance.network.sendToServer(new PacketEnviroMine(pData));
		}
	}

	@SubscribeEvent
	public void onEntityInteract(EntityInteractEvent event)
	{
		if(event.isCanceled() || event.entityPlayer.worldObj.isRemote)
		{
			return;
		}

		if(event.target instanceof EntityIronGolem && event.entityPlayer.getEquipmentInSlot(0) != null)
		{
			ItemStack stack = event.entityLiving.getEquipmentInSlot(0);

			if(stack.getItem() == Items.name_tag && stack.getDisplayName().toLowerCase().equals("siyliss"))
			{
				event.entityPlayer.addStat(EnviroAchievements.ironArmy, 1);
			}
		}

		if(!EM_Settings.foodSpoiling)
		{
			return;
		}

		if(event.target instanceof IInventory chest && EM_Settings.foodSpoiling)
		{
            RotHandler.rotInvo(event.entityPlayer.worldObj, chest);	//For every entity that is... inventory? (like chest mobs?)
		}
	}

	public void RecordEasterEgg(EntityPlayer player, int x, int y, int z)
	{
		if(player.worldObj.isRemote)
		{
			return;
		}

		MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(player.worldObj, player);

		if(movingobjectposition == null)
		{
			return;
		} else
		{
			if(movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
			{
				int i = movingobjectposition.blockX;
				int j = movingobjectposition.blockY;
				int k = movingobjectposition.blockZ;

				if(player.worldObj.getBlock(i, j, k) == Blocks.jukebox)
				{
					TileEntityJukebox recordplayer = (TileEntityJukebox)player.worldObj.getTileEntity(i, j, k);

					if (recordplayer != null)
					{
						if(recordplayer.func_145856_a() == null)
						{
							EnviroDataTracker tracker = EM_StatusManager.lookupTracker(player);

							if(tracker != null)
							{
								if(tracker.sanity >= 75F)
								{
									tracker.sanity -= 50F;
								}

								player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("msg.enviromine.RecordEasterEgg")));
								player.addStat(EnviroAchievements.ohGodWhy, 1);
							}
						}
					}
				}
			}
		}
	}

	public static void fillBottle(World world, EntityPlayer player, int x, int y, int z, ItemStack item, PlayerInteractEvent event, boolean isPolymer)
	{
		MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(world, player);

        if (movingobjectposition != null) {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                Block targetblock = world.getBlock(i, j, k);
                int targetmeta = world.getBlockMetadata(i, j, k);

                boolean isValidCauldron = (targetblock == Blocks.cauldron && targetmeta > 0);

                if (!world.canMineBlock(player, i, j, k)) {
                    return;
                }

                if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, item)) {
                    return;
                }

                boolean isWater;

                if (targetblock == Blocks.water || targetblock == Blocks.flowing_water) {
                    // if finite is on.. make sure player cant drink from infinite flowing water source
                    isWater = !(targetmeta > .2f) || !EM_Settings.finiteWater;
                } else {
                    isWater = false;
                }

                if (isWater || isValidCauldron) {
                    Item newItem;
                    if (isPolymer) {
                        newItem = ObjectHandlerCompat.cleanWaterBottle_polymer;
                    } else {
                        newItem = Items.potionitem;
                    }

                    switch (getWaterType(world, i, j, k)) {
                        case -3 -> // Hot
                        {
                            if (isPolymer) {
                                newItem = ObjectHandlerCompat.hotWaterBottle_polymer;
                            } else {
                                newItem = ObjectHandler.hotWaterBottle;
                            }
                            break;
                        }
                        case -2 -> // Dirty warm
                        {
                            if (isPolymer) {
                                newItem = ObjectHandlerCompat.badWarmWaterBottle_polymer;
                            } else {
                                newItem = ObjectHandler.badWarmWaterBottle;
                            }
                            break;
                        }
                        case -1 -> // Clean warm
                        {
                            if (isPolymer) {
                                newItem = ObjectHandlerCompat.warmWaterBottle_polymer;
                            } else {
                                newItem = ObjectHandler.warmWaterBottle;
                            }
                            break;
                        }
                        case 0 -> // Clean
                        {
                            if (isPolymer) {
                                newItem = ObjectHandlerCompat.cleanWaterBottle_polymer;
                            } else {
                                newItem = Items.potionitem;
                            }
                            break;
                        }
                        case 1 -> // Dirty
                        {
                            if (isPolymer) {
                                newItem = ObjectHandlerCompat.badWaterBottle_polymer;
                            } else {
                                newItem = ObjectHandler.badWaterBottle;
                            }
                            break;
                        }
                        case 2 -> // Salty
                        {
                            if (isPolymer) {
                                newItem = ObjectHandlerCompat.saltWaterBottle_polymer;
                            } else {
                                newItem = ObjectHandler.saltWaterBottle;
                            }
                            break;
                        }
                        case 3 -> // Clean cold
                        {
                            if (isPolymer) {
                                newItem = ObjectHandlerCompat.coldWaterBottle_polymer;
                            } else {
                                newItem = ObjectHandler.coldWaterBottle;
                            }
                            break;
                        }
                        case 4 -> // Dirty cold
                        {
                            if (isPolymer) {
                                newItem = ObjectHandlerCompat.badColdWaterBottle_polymer;
                            } else {
                                newItem = ObjectHandler.badColdWaterBottle;
                            }
                            break;
                        }
                        case 5 -> // Frosty
                        {
                            if (isPolymer) {
                                newItem = ObjectHandlerCompat.frostyWaterBottle_polymer;
                            } else {
                                newItem = ObjectHandler.frostyWaterBottle;
                            }
                            break;
                        }
                    }

                    if (isValidCauldron && isCauldronHeatingBlock(world.getBlock(i, j - 1, k), world.getBlockMetadata(i, j - 1, k))) {
                        if (getWaterType(world, i, j, k) == 5) {
                            if (isPolymer) {
                                newItem = ObjectHandlerCompat.coldWaterBottle_polymer;
                            } else {
                                newItem = ObjectHandler.coldWaterBottle;
                            }
                        } else if (getWaterType(world, i, j, k) == 1 || getWaterType(world, i, j, k) == 2 || getWaterType(world, i, j, k) == 3 || getWaterType(world, i, j, k) == 4) {
                            if (isPolymer) {
                                newItem = ObjectHandlerCompat.cleanWaterBottle_polymer;
                            } else {
                                newItem = Items.potionitem;
                            }
                        } else if (getWaterType(world, i, j, k) == 0) {
                            if (isPolymer) {
                                newItem = ObjectHandlerCompat.warmWaterBottle_polymer;
                            } else {
                                newItem = ObjectHandler.warmWaterBottle;
                            }
                        } else if (getWaterType(world, i, j, k) == -2 || getWaterType(world, i, j, k) == -1) {
                            if (isPolymer) {
                                newItem = ObjectHandlerCompat.hotWaterBottle_polymer;
                            } else {
                                newItem = ObjectHandler.hotWaterBottle;
                            }
                        }
                    }

                    if (isValidCauldron) {
                        player.worldObj.setBlockMetadataWithNotify(i, j, k, player.worldObj.getBlockMetadata(i, j, k) - 1, 2);
                    } else if (EM_Settings.finiteWater) {
                        player.worldObj.setBlock(i, j, k, Blocks.flowing_water, player.worldObj.getBlockMetadata(i, j, k) + 1, 2);
                    }

                    --item.stackSize;

                    if (item.stackSize <= 0) {
                        item = new ItemStack(newItem);
                        item.stackSize = 1;
                        item.setItemDamage(0);
                        player.setCurrentItemOrArmor(0, item);
                    } else if (!player.inventory.addItemStackToInventory(new ItemStack(newItem, 1, 0))) {
                        player.dropPlayerItemWithRandomChoice(new ItemStack(newItem, 1, 0), false);
                    }

                    //NEEDED TO RESYNC THE PLAYER CONTAINER
                    player.inventoryContainer.detectAndSendChanges();
                    event.setCanceled(true);
                }
            }

        }
        return;
    }

	public static void drinkWater(EntityPlayer entityPlayer, PlayerInteractEvent event)
	{
		// Skip drinking if hydration is not tracked
		if(
				(EM_Settings.dimensionProperties.containsKey(entityPlayer.dimension)
				&& !EM_Settings.dimensionProperties.get(entityPlayer.dimension).trackHydration)
				|| !EM_Settings.enableHydrate
				|| (EM_Settings.witcheryVampireImmunities && EnviroUtils.isPlayerAVampire(entityPlayer))
				|| (EM_Settings.matterOverdriveAndroidImmunities && EnviroUtils.isPlayerCurrentlyMOAndroid(entityPlayer))
				)
		{
			return;
		}

		// Don't allow drinking if you're in water
		if(entityPlayer.isInsideOfMaterial(Material.water))
		{
			return;
		}

		EnviroDataTracker tracker = EM_StatusManager.lookupTracker(entityPlayer);
		MovingObjectPosition mop = getMovingObjectPositionFromPlayer(entityPlayer.worldObj, entityPlayer);

		if(mop != null)
		{
			if(mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
			{
				int i = mop.blockX;
				int j = mop.blockY;
				int k = mop.blockZ;

				int[] hitBlock = EnviroUtils.getAdjacentBlockCoordsFromSide(i, j, k, mop.sideHit);

				int x = hitBlock[0];
				int y = hitBlock[1];
				int z = hitBlock[2];

				if(entityPlayer.worldObj.getBlock(i, j, k).getMaterial() != Material.water && entityPlayer.worldObj.getBlock(x, y, z).getMaterial() == Material.water)
				{
					i = x;
					j = y;
					k = z;
				}

				Block targetBlock = entityPlayer.worldObj.getBlock(i, j, k);
				String targetBlockRegistryName = Block.blockRegistry.getNameForObject(targetBlock);

				boolean isWater = (targetBlock == Blocks.flowing_water || targetBlock == Blocks.water
                    // Automatically make the block water if it's Streams water
                    || (EM_Settings.streamsDrink && targetBlockRegistryName.contains(WATER_ROOT_STREAMS))
                )
                    // If finite water is on, make sure player can't drink from an infinite flowing water source
                    && !(entityPlayer.worldObj.getBlockMetadata(i, j, k) > .2f && EM_Settings.finiteWater)
                    // Automatically make the block not-water if it's BoP blood
                    && !(targetBlockRegistryName.equals(BLOOD_BLOCK_BOP));

                boolean isValidCauldron = (entityPlayer.worldObj.getBlock(i, j, k) == Blocks.cauldron && entityPlayer.worldObj.getBlockMetadata(i, j, k) > 0);

				if(isWater || isValidCauldron)
				{
					if(tracker != null && tracker.hydration < 100F)
					{
						int type = 0;

						if(isValidCauldron && isCauldronHeatingBlock(entityPlayer.worldObj.getBlock(i, j-1, k), entityPlayer.worldObj.getBlockMetadata(i, j-1, k)))
						{
							type = 0;
						}
						else
						{
							type = getWaterType(entityPlayer.worldObj, i, j, k);
						}

						int werewolfLevel = EnviroUtils.getWitcheryWerewolfLevel(entityPlayer);
						int werewolfDuration200 = MathHelper.clamp_int(200 - (EM_Settings.witcheryWerewolfImmunities ? werewolfLevel : 0)*15, 0, 200);
						int werewolfDuration600 = MathHelper.clamp_int(600 - (EM_Settings.witcheryWerewolfImmunities ? werewolfLevel : 0)*45, 0, 600);

                        if(type == -3) // Hot
                        {
                            if(tracker.bodyTemp >= EM_Settings.WarmHotWaterReducesTemperatureStartingValue)
                            {
                                tracker.bodyTemp += EM_Settings.HotWarmWaterTemperatureInfluence;
                            }
                            if(EM_Settings.HotWaterHydrateWorld > 0F)
                            {
                                tracker.hydrate(EM_Settings.HotWaterHydrateWorld);
                            } else if(EM_Settings.HotWaterHydrateWorld < 0F)
                            {
                                tracker.dehydrate(Math.abs(EM_Settings.HotWaterHydrateWorld));
                            }
                        } else if(type == -2) // Dirty warm
                        {
                            if(!(EM_Settings.witcheryWerewolfImmunities && (EnviroUtils.isPlayerCurrentlyWitcheryWerewolf(entityPlayer) || EnviroUtils.isPlayerCurrentlyWitcheryWolf(entityPlayer))))
                            {
                                if(entityPlayer.getRNG().nextInt(2) == 0)
                                {
                                    entityPlayer.addPotionEffect(new PotionEffect(Potion.hunger.id, werewolfDuration200));
                                }
                                if(entityPlayer.getRNG().nextInt(4) == 0)
                                {
                                    entityPlayer.addPotionEffect(new PotionEffect(Potion.poison.id, werewolfDuration200));
                                }
                            }
                            if(tracker.bodyTemp >= EM_Settings.WarmHotWaterReducesTemperatureStartingValue)
                            {
                                tracker.bodyTemp += EM_Settings.DirtyWarmWaterTemperatureInfluence;
                            }
                            if(EM_Settings.DirtyWarmWaterHydrateWorld > 0F)
                            {
                                tracker.hydrate(EM_Settings.DirtyWarmWaterHydrateWorld);
                            } else if(EM_Settings.DirtyWarmWaterHydrateWorld < 0F)
                            {
                                tracker.dehydrate(Math.abs(EM_Settings.DirtyWarmWaterHydrateWorld));
                            }
                        } else if(type == -1) // Warm
                        {
                            if(tracker.bodyTemp >= EM_Settings.WarmHotWaterReducesTemperatureStartingValue)
                            {
                                tracker.bodyTemp += EM_Settings.CleanWarmWaterTemperatureInfluence;
                            }
                            if(EM_Settings.CleanWarmWaterHydrateWorld > 0F)
                            {
                                tracker.hydrate(EM_Settings.CleanWarmWaterHydrateWorld);
                            } else if(EM_Settings.CleanWarmWaterHydrateWorld < 0F)
                            {
                                tracker.dehydrate(Math.abs(EM_Settings.CleanWarmWaterHydrateWorld));
                            }
                        } else if(type == 0) // Clean
						{
							if(tracker.bodyTemp >= EM_Settings.WaterReducesTemperatureStartingValue)
							{
								tracker.bodyTemp += EM_Settings.CleanWaterTemperatureInfluence;
							}
                            if(EM_Settings.CleanWaterHydrateWorld > 0F)
                            {
                                tracker.hydrate(EM_Settings.CleanWaterHydrateWorld);
                            } else if(EM_Settings.CleanWaterHydrateWorld < 0F)
                            {
                                tracker.dehydrate(Math.abs(EM_Settings.CleanWaterHydrateWorld));
                            }
						} else if(type == 1) // Dirty
						{
							if(!(EM_Settings.witcheryWerewolfImmunities && (EnviroUtils.isPlayerCurrentlyWitcheryWerewolf(entityPlayer) || EnviroUtils.isPlayerCurrentlyWitcheryWolf(entityPlayer))))
							{
								if(entityPlayer.getRNG().nextInt(2) == 0)
								{
									entityPlayer.addPotionEffect(new PotionEffect(Potion.hunger.id, werewolfDuration200));
								}
								if(entityPlayer.getRNG().nextInt(4) == 0)
								{
									entityPlayer.addPotionEffect(new PotionEffect(Potion.poison.id, werewolfDuration200));
								}
							}

							if(tracker.bodyTemp >= EM_Settings.WaterReducesTemperatureStartingValue)
							{
								tracker.bodyTemp += EM_Settings.DirtyWaterTemperatureInfluence;
							}
                            if(EM_Settings.DirtyWaterHydrateWorld > 0F)
                            {
                                tracker.hydrate(EM_Settings.DirtyWaterHydrateWorld);
                            } else if(EM_Settings.DirtyWaterHydrateWorld < 0F)
                            {
                                tracker.dehydrate(Math.abs(EM_Settings.DirtyWaterHydrateWorld));
                            }
						} else if(type == 2) // Salty
						{
							if(!(EM_Settings.witcheryWerewolfImmunities && (EnviroUtils.isPlayerCurrentlyWitcheryWerewolf(entityPlayer) || EnviroUtils.isPlayerCurrentlyWitcheryWolf(entityPlayer))))
							{
                                entityPlayer.getRNG().nextInt(1);
                                if (entityPlayer.getActivePotionEffect(EnviroPotion.dehydration) != null && entityPlayer.getRNG().nextInt(5) == 0) {
                                    int amp = entityPlayer.getActivePotionEffect(EnviroPotion.dehydration).getAmplifier();
                                    entityPlayer.addPotionEffect(new PotionEffect(EnviroPotion.dehydration.id, werewolfDuration600, amp + 1));
                                } else {
                                    entityPlayer.addPotionEffect(new PotionEffect(EnviroPotion.dehydration.id, werewolfDuration600));
                                }
                            }

							if(tracker.bodyTemp >= EM_Settings.WaterReducesTemperatureStartingValue)
							{
								tracker.bodyTemp += EM_Settings.SaltyWaterTemperatureInfluence;
							}
                            if(EM_Settings.SaltyWaterHydrateWorld > 0F)
                            {
                                tracker.hydrate(EM_Settings.SaltyWaterHydrateWorld);
                            } else if(EM_Settings.SaltyWaterHydrateWorld < 0F)
                            {
                                tracker.dehydrate(Math.abs(EM_Settings.SaltyWaterHydrateWorld));
                            }
						} else if(type == 3) // Cold
						{
							if(tracker.bodyTemp >= EM_Settings.ColdFrostyWaterReducesTemperatureStartingValue)
							{
								tracker.bodyTemp += EM_Settings.CleanColdWaterTemperatureInfluence;
							}
                            if(EM_Settings.CleanColdWaterHydrateWorld > 0F)
                            {
                                tracker.hydrate(EM_Settings.CleanColdWaterHydrateWorld);
                            } else if(EM_Settings.CleanColdWaterHydrateWorld < 0F)
                            {
                                tracker.dehydrate(Math.abs(EM_Settings.CleanColdWaterHydrateWorld));
                            }
						} else if(type == 4) // Dirty cold
                        {
                            if(!(EM_Settings.witcheryWerewolfImmunities && (EnviroUtils.isPlayerCurrentlyWitcheryWerewolf(entityPlayer) || EnviroUtils.isPlayerCurrentlyWitcheryWolf(entityPlayer))))
                            {
                                entityPlayer.getRNG().nextInt(1);
                                if (entityPlayer.getActivePotionEffect(EnviroPotion.dehydration) != null && entityPlayer.getRNG().nextInt(5) == 0) {
                                    int amp = entityPlayer.getActivePotionEffect(EnviroPotion.dehydration).getAmplifier();
                                    entityPlayer.addPotionEffect(new PotionEffect(EnviroPotion.dehydration.id, werewolfDuration600, amp + 1));
                                } else {
                                    entityPlayer.addPotionEffect(new PotionEffect(EnviroPotion.dehydration.id, werewolfDuration600));
                                }
                            }
                            if(tracker.bodyTemp >= EM_Settings.ColdFrostyWaterReducesTemperatureStartingValue)
                            {
                                tracker.bodyTemp += EM_Settings.DirtyColdWaterTemperatureInfluence;
                            }
                            if(EM_Settings.DirtyColdWaterHydrateWorld > 0F)
                            {
                                tracker.hydrate(EM_Settings.DirtyColdWaterHydrateWorld);
                            } else if(EM_Settings.DirtyColdWaterHydrateWorld < 0F)
                            {
                                tracker.dehydrate(Math.abs(EM_Settings.DirtyColdWaterHydrateWorld));
                            }
                        } else if(type == 5) // Frosty
                        {
                            if(tracker.bodyTemp >= EM_Settings.ColdFrostyWaterReducesTemperatureStartingValue)
                            {
                                tracker.bodyTemp += EM_Settings.FrostyWaterTemperatureInfluence;
                            }
                            if(EM_Settings.FrostyWaterHydrateWorld > 0F)
                            {
                                tracker.hydrate(EM_Settings.FrostyWaterHydrateWorld);
                            } else if(EM_Settings.FrostyWaterHydrateWorld < 0F)
                            {
                                tracker.dehydrate(Math.abs(EM_Settings.FrostyWaterHydrateWorld));
                            }
                        }

						if(isValidCauldron)
						{
							entityPlayer.worldObj.setBlockMetadataWithNotify(i, j, k, entityPlayer.worldObj.getBlockMetadata(i, j, k) - 1, 2);
						}
						else if(EM_Settings.finiteWater)
						{
							entityPlayer.worldObj.setBlock(i, j, k, Blocks.flowing_water, entityPlayer.worldObj.getBlockMetadata(i, j, k) + 1, 2);
						}

						entityPlayer.worldObj.playSoundAtEntity(entityPlayer, "random.drink", 1.0F, 1.0F);

						if(event != null)
						{
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}

	public static int getWaterType(World world, int x, int y, int z)
	{
		BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
		DimensionProperties dimensionProperties = EM_Settings.dimensionProperties.get(world.provider.dimensionId);
		int seaLvl = dimensionProperties != null? dimensionProperties.sealevel : 64;

		if(biome == null)
		{
			return 0;
		}

		BiomeProperties biomeProperties = EM_Settings.biomeProperties.get(biome.biomeID);

		if(biomeProperties != null && biomeProperties.getWaterQualityId() != -1)
		{
			return biomeProperties.getWaterQualityId();
		}

		int waterColour = biome.getWaterColorMultiplier();
		boolean looksBad = false;

		if(waterColour != 16777215)
		{
			Color bColor = new Color(waterColour);

			if(bColor.getRed() < 200 || bColor.getGreen() < 200 || bColor.getBlue() < 200)
			{
				looksBad = true;
			}
		}

		ArrayList<Type> typeList = new ArrayList<Type>();
		Type[] typeArray = BiomeDictionary.getTypesForBiome(biome);
        Collections.addAll(typeList, typeArray);

        //-3 - hot
        //-2 - dirty warm
        //-1 - clean warm
        // 0 - clean normal
        // 1 - dirty normal
        // 2 - salty
        // 3 - clean cold
        // 4 - dirty cold
        // 5 - frosty

        if(typeList.contains(Type.HOT) && !typeList.contains(Type.WET) && !typeList.contains(Type.SAVANNA))
        {
            return -3; // hot
        }
        else if(typeList.contains(Type.HOT) && (!typeList.contains(Type.WET) || looksBad))
        {
            return -2; // dirty warm
        }
        else if(typeList.contains(Type.HOT) && typeList.contains(Type.WET))
        {
            return -2; // clean warm
        }
		else if(!typeList.contains(Type.COLD) && (typeList.contains(Type.SWAMP) || typeList.contains(Type.JUNGLE) || typeList.contains(Type.DEAD) || typeList.contains(Type.WASTELAND) || y < (float)seaLvl/0.75F || looksBad) )
		{
			return 1; // dirty
		}
		else if(typeList.contains(Type.OCEAN) || typeList.contains(Type.BEACH))
		{
			return 2; // salty
		}
		else if(typeList.contains(Type.COLD) && (!typeList.contains(Type.SNOWY) || typeList.contains(Type.CONIFEROUS) || biome.getFloatTemperature(x, y, z) < 0F || y > seaLvl * 2) && !looksBad)
		{
			return 3; // clean Cold
		}
        else if(typeList.contains(Type.COLD) && (!typeList.contains(Type.SNOWY) || typeList.contains(Type.CONIFEROUS) || biome.getFloatTemperature(x, y, z) < 0F || y > seaLvl * 2) && looksBad)
        {
            return 4; // dirty cold
        }
        else if(typeList.contains(Type.COLD) && typeList.contains(Type.SNOWY))
        {
            return 5; // frosty
        }
        else {
			return 0; // clean
		}
	}

	@SubscribeEvent
	public void onBreakBlock(HarvestDropsEvent event)
	{
		if(event.world.isRemote)
		{
			return;
		}

		if(event.harvester != null)
		{
			if(event.getResult() != Result.DENY && !event.harvester.capabilities.isCreativeMode)
			{
				EM_PhysManager.schedulePhysUpdate(event.world, event.x, event.y, event.z, true, "Normal");
			}
		} else
		{
			if(event.getResult() != Result.DENY)
			{
				EM_PhysManager.schedulePhysUpdate(event.world, event.x, event.y, event.z, true, "Normal");
			}
		}
	}

	@SubscribeEvent
	public void onPlayerUseItem(PlayerUseItemEvent.Finish event)
	{
		EnviroDataTracker tracker = EM_StatusManager.lookupTracker(event.entityPlayer);

		if(tracker == null || event.item == null)
		{
			return;
		}

		ItemStack item = event.item;

		if(EM_Settings.itemProperties.containsKey(Item.itemRegistry.getNameForObject(item.getItem())) || EM_Settings.itemProperties.containsKey(Item.itemRegistry.getNameForObject(item.getItem()) + "," + item.getItemDamage()))
		{
			ItemProperties itemProps;
			if(EM_Settings.itemProperties.containsKey(Item.itemRegistry.getNameForObject(item.getItem()) + "," + item.getItemDamage()))
			{
				itemProps = EM_Settings.itemProperties.get(Item.itemRegistry.getNameForObject(item.getItem()) + "," + item.getItemDamage());
			} else
			{
				itemProps = EM_Settings.itemProperties.get(Item.itemRegistry.getNameForObject(item.getItem()));
			}

			if(itemProps.effTemp > 0F)
			{
				if(tracker.bodyTemp + itemProps.effTemp > itemProps.effTempCap)
				{
					if(tracker.bodyTemp <= itemProps.effTempCap)
					{
						tracker.bodyTemp = itemProps.effTempCap;
					}
				} else
				{
					tracker.bodyTemp += itemProps.effTemp;
				}
			} else
			{
				if(tracker.bodyTemp + itemProps.effTemp < itemProps.effTempCap)
				{
					if(tracker.bodyTemp >= itemProps.effTempCap)
					{
						tracker.bodyTemp = itemProps.effTempCap;
					}
				} else
				{
					tracker.bodyTemp += itemProps.effTemp;
				}
			}

			if(tracker.sanity + itemProps.effSanity >= 100F)
			{
				tracker.sanity = 100F;
			} else if(tracker.sanity + itemProps.effSanity <= 0F)
			{
				tracker.sanity = 0F;
			} else
			{
				tracker.sanity += itemProps.effSanity;
			}

			if(itemProps.effHydration > 0F)
			{
				tracker.hydrate(itemProps.effHydration);
			} else if(itemProps.effHydration < 0F)
			{
				tracker.dehydrate(Math.abs(itemProps.effHydration));
			}

			if(tracker.airQuality + itemProps.effAir >= 100F)
			{
				tracker.airQuality = 100F;
			} else if(tracker.airQuality + itemProps.effAir <= 0F)
			{
				tracker.airQuality = 0F;
			} else
			{
				tracker.airQuality += itemProps.effAir;
			}
		}

		if(item.getItem() == Items.golden_apple)
		{
			if(item.isItemDamaged())
			{
				tracker.hydration = 100F;
				tracker.sanity = 100F;
				tracker.airQuality = 100F;
				tracker.bodyTemp = 36.6F;
				if(!EnviroMine.proxy.isClient() || EnviroMine.proxy.isOpenToLAN())
				{
					EM_StatusManager.syncMultiplayerTracker(tracker);
				}
			} else
			{
				tracker.sanity = 100F;
				tracker.hydrate(10F);
			}

			tracker.trackedEntity.removePotionEffect(EnviroPotion.frostbite.id);
			tracker.frostbiteLevel = 0;
		}
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		if(event.entityLiving.isDead)
		{
			if(!event.entityLiving.isEntityAlive())
			{
				doDeath(event.entityLiving);
			}
			return;
		}

		// Do hallucinations
		if(event.entityLiving.worldObj.isRemote)
		{
			if(event.entityLiving.getRNG().nextInt(5) == 0)
			{
				EM_StatusManager.createFX(event.entityLiving);
			}

			if(event.entityLiving instanceof EntityPlayer && event.entityLiving.worldObj.isRemote)
			{
				if(Minecraft.getMinecraft().thePlayer.isPotionActive(EnviroPotion.insanity))
				{
					int chance = 100 / (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(EnviroPotion.insanity).getAmplifier() + 1);

					chance = chance > 0? chance : 1;

					if(event.entityLiving.getRNG().nextInt(chance) == 0)
					{
						new Hallucination(event.entityLiving);
					}
				}

				Hallucination.update();
			}
			return;
		}
		
		if(event.entityLiving instanceof EntityPlayer)
		{
			InventoryPlayer invo = (InventoryPlayer)((EntityPlayer)event.entityLiving).inventory;
			
			//GASMASK SOUND
			if(EnviroMine.isHbmLoaded) {
				EM_EventManager_NTM.handleGasMaskSound(event, invo);
			}
			
			AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(event.entityLiving.posX - EM_Settings.DavyLampGasDetectRange, event.entityLiving.posY - EM_Settings.DavyLampGasDetectRange, event.entityLiving.posZ - EM_Settings.DavyLampGasDetectRange, event.entityLiving.posX + EM_Settings.DavyLampGasDetectRange, event.entityLiving.posY + EM_Settings.DavyLampGasDetectRange, event.entityLiving.posZ + EM_Settings.DavyLampGasDetectRange).expand(2D, 2D, 2D);
			if(event.entityLiving.worldObj.getEntitiesWithinAABB(TileEntityGas.class, boundingBox).size() <= 0)
			{
				ReplaceInvoItems(invo, Item.getItemFromBlock(ObjectHandler.davyLampBlock), 2, Item.getItemFromBlock(ObjectHandler.davyLampBlock), 1);
			}
			
			if(EnviroMine.isHbmLoaded) {
				EM_EventManager_NTM.handleNTMGas(event, boundingBox, invo);
			}
			
			if(EM_Settings.foodSpoiling)
			{
				RotHandler.rotInvo(event.entityLiving.worldObj, invo);	//For player's inventory
			}

			if(event.entityLiving.getEntityData().hasKey("EM_SAFETY"))
			{
				if(event.entityLiving.worldObj.getTotalWorldTime() - event.entityLiving.getEntityData().getLong("EM_SAFETY") >= 1000L)
				{
					((EntityPlayer)event.entityLiving).addStat(EnviroAchievements.funwaysFault, 1);
				}
			}

			if(event.entityLiving.getEntityData().hasKey("EM_THAT"))
			{
				if(event.entityLiving.worldObj.getTotalWorldTime() - event.entityLiving.getEntityData().getLong("EM_THAT") >= 1000L)
				{
					((EntityPlayer)event.entityLiving).addStat(EnviroAchievements.thatJustHappened, 1);
				}
			}

			if(event.entityLiving.worldObj.provider.dimensionId == EM_Settings.caveDimID && event.entityLiving.getEntityData().hasKey("EM_CAVE_DIST"))
			{
				int[] prePos = event.entityLiving.getEntityData().getIntArray("EM_CAVE_DIST");
				int distance = MathHelper.floor_double(event.entityLiving.getDistance(prePos[0], prePos[1], prePos[2]));

				if(distance > prePos[3])
				{
					prePos[3] = distance;
					event.entityLiving.getEntityData().setIntArray("EM_CAVE_DIST", prePos);
				}
			}

			if(!event.entityLiving.isPotionActive(EnviroPotion.hypothermia) && !event.entityLiving.isPotionActive(EnviroPotion.frostbite) && event.entityLiving.worldObj.getBiomeGenForCoords(MathHelper.floor_double(event.entityLiving.posX), MathHelper.floor_double(event.entityLiving.posZ)).getEnableSnow())
			{
				if(event.entityLiving.getEntityData().hasKey("EM_WINTER"))
				{
					if(event.entityLiving.worldObj.getTotalWorldTime() - event.entityLiving.getEntityData().getLong("EM_WINTER") > 24000L * 7)
					{
						((EntityPlayer)event.entityLiving).addStat(EnviroAchievements.winterIsComing, 1);
						event.entityLiving.getEntityData().removeTag("EM_WINTER");
					}
				} else
				{
					event.entityLiving.getEntityData().setLong("EM_WINTER", event.entityLiving.worldObj.getTotalWorldTime());
				}
			} else if(event.entityLiving.getEntityData().hasKey("EM_WINTER"))
			{
				event.entityLiving.getEntityData().removeTag("EM_WINTER");
			}

			if(event.entityLiving.isPotionActive(EnviroPotion.heatstroke) && event.entityLiving.getActivePotionEffect(EnviroPotion.heatstroke).getAmplifier() >= 2)
			{
				event.entityLiving.getEntityData().setBoolean("EM_BOILED", true);
			} else if(event.entityLiving.getEntityData().getBoolean("EM_BOILED") && !event.entityLiving.isPotionActive(EnviroPotion.heatstroke))
			{
				((EntityPlayer)event.entityLiving).addStat(EnviroAchievements.hardBoiled, 1);
				event.entityLiving.getEntityData().removeTag("EM_BOILED");
			} else if(event.entityLiving.getEntityData().hasKey("EM_BOILED"))
			{
				event.entityLiving.getEntityData().removeTag("EM_BOILED");
			}

			if(event.entityLiving.worldObj.provider.dimensionId == EM_Settings.caveDimID && event.entityLiving.worldObj.getBlockLightValue(MathHelper.floor_double(event.entityLiving.posX), MathHelper.floor_double(event.entityLiving.posY), MathHelper.floor_double(event.entityLiving.posZ)) < 1)
			{
				int x = MathHelper.floor_double(event.entityLiving.posX);
				int y = MathHelper.floor_double(event.entityLiving.posY);
				int z = MathHelper.floor_double(event.entityLiving.posZ);

				if(!event.entityLiving.getEntityData().hasKey("EM_PITCH"))
				{
					event.entityLiving.getEntityData().setIntArray("EM_PITCH", new int[]{x, y, z});
				}

				if(event.entityLiving.getDistance(x, y, z) >= 250)
				{
					((EntityPlayer)event.entityLiving).addStat(EnviroAchievements.itsPitchBlack, 1);
				}
			} else if(event.entityLiving.getEntityData().hasKey("EM_PITCH"))
			{
				event.entityLiving.getEntityData().removeTag("EM_PITCH");
			}

			if(EM_Settings.enableAirQ && EM_Settings.enableBodyTemp && EM_Settings.enableHydrate && EM_Settings.enableSanity && EM_Settings.enableLandslide && EM_Settings.enablePhysics && EM_Settings.enableQuakes)
			{
				int seaLvl = 48;

				if(EM_Settings.dimensionProperties.containsKey(event.entityLiving.worldObj.provider.dimensionId))
				{
					seaLvl = MathHelper.ceiling_double_int(EM_Settings.dimensionProperties.get(event.entityLiving.worldObj.provider.dimensionId).sealevel * 0.75F);
				} else if(event.entityLiving.worldObj.provider.dimensionId == EM_Settings.caveDimID)
				{
					seaLvl = 256;
				}

				if(event.entityLiving.posY < seaLvl)
				{
					long time = event.entityLiving.getEntityData().getLong("EM_MINE_TIME");
					long date = event.entityLiving.getEntityData().getLong("EM_MINE_DATE");
					time += event.entityLiving.worldObj.getTotalWorldTime() - date;
					event.entityLiving.getEntityData().setLong("EM_MINE_DATE", event.entityLiving.worldObj.getTotalWorldTime());
					event.entityLiving.getEntityData().setLong("EM_MINE_TIME", time);

					if(time > 24000L * 3L)
					{
						((EntityPlayer)event.entityLiving).addStat(EnviroAchievements.proMiner, 1);
					}
				} else
				{
					event.entityLiving.getEntityData().setLong("EM_MINE_DATE", event.entityLiving.worldObj.getTotalWorldTime());
				}
			} else
			{
				if(event.entityLiving.getEntityData().hasKey("EM_MINE_TIME"))
				{
					event.entityLiving.getEntityData().removeTag("EM_MINE_TIME");
				}
				if(event.entityLiving.getEntityData().hasKey("EM_MINE_DATE"))
				{
					event.entityLiving.getEntityData().removeTag("EM_MINE_DATE");
				}
			}
		}

		EnviroDataTracker tracker = EM_StatusManager.lookupTracker(event.entityLiving);

		if(tracker == null || tracker.isDisabled)
		{
			if((!EnviroMine.proxy.isClient() || EnviroMine.proxy.isOpenToLAN()) && (EM_Settings.enableAirQ || EM_Settings.enableBodyTemp || EM_Settings.enableHydrate || EM_Settings.enableSanity))
			{
				if(event.entityLiving instanceof EntityPlayer || (EM_Settings.trackNonPlayer && EnviroDataTracker.isLegalType(event.entityLiving)))
				{
					if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.WARN, "Server lost track of player! Attempting to re-sync...");
					EnviroDataTracker emTrack = new EnviroDataTracker((EntityLivingBase) event.entity);
					EM_StatusManager.addToManager(emTrack);
					emTrack.loadNBTTags();
					EM_StatusManager.syncMultiplayerTracker(emTrack);
					tracker = emTrack;
				} else
				{
					return;
				}
			} else
			{
				return;
			}
		}

		EM_StatusManager.updateTracker(tracker);

		UUID EM_DEHY1_ID = EM_Settings.DEHY1_UUID;

		if(tracker.hydration < 10F &&
				(!EM_Settings.dimensionProperties.containsKey(event.entityLiving.dimension)
						|| (EM_Settings.dimensionProperties.containsKey(event.entityLiving.dimension) && EM_Settings.dimensionProperties.get(event.entityLiving.dimension).trackHydration))
				)
		{
			event.entityLiving.addPotionEffect(new PotionEffect(Potion.weakness.id, 200, 0));
			event.entityLiving.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 200, 0));

			IAttributeInstance attribute = event.entityLiving.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			AttributeModifier mod = new AttributeModifier(EM_DEHY1_ID, "EM_Dehydrated", -0.25D, 2);

			if(attribute.getModifier(mod.getID()) == null)
			{
				attribute.applyModifier(mod);
			}
		}
		else
		{
			IAttributeInstance attribute = event.entityLiving.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

			if(attribute.getModifier(EM_DEHY1_ID) != null)
			{
				attribute.removeModifier(attribute.getModifier(EM_DEHY1_ID));
			}
		}

		UUID EM_FROST1_ID = EM_Settings.FROST1_UUID;
		UUID EM_FROST2_ID = EM_Settings.FROST2_UUID;
		UUID EM_FROST3_ID = EM_Settings.FROST3_UUID;
		UUID EM_HEAT1_ID = EM_Settings.HEAT1_UUID;

		if(event.entityLiving.isPotionActive(EnviroPotion.heatstroke))
		{
			IAttributeInstance attribute = event.entityLiving.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			AttributeModifier mod = new AttributeModifier(EM_HEAT1_ID, "EM_Heat", -0.25D, 2);

			if(attribute.getModifier(mod.getID()) == null)
			{
				attribute.applyModifier(mod);
			}
		} else
		{
			IAttributeInstance attribute = event.entityLiving.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

			if(attribute.getModifier(EM_HEAT1_ID) != null)
			{
				attribute.removeModifier(attribute.getModifier(EM_HEAT1_ID));
			}
		}

		if(event.entityLiving.isPotionActive(EnviroPotion.hypothermia) || event.entityLiving.isPotionActive(EnviroPotion.frostbite))
		{
			IAttributeInstance attribute = event.entityLiving.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			AttributeModifier mod = new AttributeModifier(EM_FROST1_ID, "EM_Frost_Cold", -0.25D, 2);
			String msg = "";

			if(event.entityLiving.isPotionActive(EnviroPotion.frostbite))
			{
				if(event.entityLiving.getActivePotionEffect(EnviroPotion.frostbite).getAmplifier() > 0)
				{
					mod = new AttributeModifier(EM_FROST3_ID, "EM_Frost_NOLEGS", -0.99D, 2);

					if(event.entityLiving instanceof EntityPlayer)
					{
						msg = "Your legs stiffen as they succumb to frostbite";
					}
				} else
				{
					mod = new AttributeModifier(EM_FROST2_ID, "EM_Frost_NOHANDS", -0.5D, 2);

					if(event.entityLiving instanceof EntityPlayer)
					{
						msg = "Your fingers start to feel numb and unresponsive";
					}
				}
			}
			if(attribute.getModifier(mod.getID()) == null)
			{
				attribute.applyModifier(mod);

				if(event.entityLiving instanceof EntityPlayer && mod.getID() != EM_FROST1_ID)
				{
					((EntityPlayer)event.entityLiving).addChatMessage(new ChatComponentText(msg));
				}
			}
		} else
		{
			IAttributeInstance attribute = event.entityLiving.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

			if(attribute.getModifier(EM_FROST1_ID) != null)
			{
				attribute.removeModifier(attribute.getModifier(EM_FROST1_ID));
			}
			if(attribute.getModifier(EM_FROST2_ID) != null && tracker.frostbiteLevel < 1)
			{
				attribute.removeModifier(attribute.getModifier(EM_FROST2_ID));
			}
			if(attribute.getModifier(EM_FROST3_ID) != null && tracker.frostbiteLevel < 2)
			{
				attribute.removeModifier(attribute.getModifier(EM_FROST3_ID));
			}
		}

		if(event.entityLiving instanceof EntityPlayer)
		{
			HandlingTheThing.stalkPlayer((EntityPlayer)event.entityLiving);
			if(event.entityLiving.isDead)
			{
				return;
			}

			if(((EntityPlayer) event.entityLiving).isPlayerSleeping() && !event.entityLiving.worldObj.isDaytime())
			{
				tracker.sleepState = "Asleep";
				tracker.lastSleepTime = (int)event.entityLiving.worldObj.getWorldInfo().getWorldTime() % 24000;
			} else if(event.entityLiving.worldObj.isDaytime())
			{
				int relitiveTime = (int)event.entityLiving.worldObj.getWorldInfo().getWorldTime() % 24000;

				if(tracker.sleepState.equals("Asleep") && tracker.lastSleepTime - relitiveTime > 100)
				{
					int timeSlept = MathHelper.floor_float(100F*(12000F - (tracker.lastSleepTime - 12000F))/12000F);

					if(tracker.sanity + timeSlept > 100F)
					{
						tracker.sanity = 100;
					} else if(timeSlept >= 0)
					{
						tracker.sanity += timeSlept;
					} else
					{
						if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.NORMAL.getLevel()) EnviroMine.logger.log(Level.ERROR, "Something went wrong while calculating sleep sanity gain! Result: " + timeSlept);
						tracker.sanity = 100;
						if(tracker.trackedEntity instanceof EntityPlayer)
						{
							((EntityPlayer)tracker.trackedEntity).addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[ENVIROMINE] Sleep state failed to detect sleep time properly!"));
							((EntityPlayer)tracker.trackedEntity).addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[ENVIROMINE] Defaulting to 100%"));
						}
					}
				}
				tracker.sleepState = "Awake";
			}
		}
	}

	
	public static boolean getBlockWithinAABB(AxisAlignedBB boundingBox, World world, Class<? extends Block> blockz){
		int minX = MathHelper.floor_double(boundingBox.minX);
		int minY = MathHelper.floor_double(boundingBox.minY);
		int minZ = MathHelper.floor_double(boundingBox.minZ);
		int maxX = MathHelper.floor_double(boundingBox.maxX);
		int maxY = MathHelper.floor_double(boundingBox.maxY);
		int maxZ = MathHelper.floor_double(boundingBox.maxZ);
		
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					Block block = world.getBlock(x, y, z);
					if (blockz.isInstance(block)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public static void ReplaceInvoItems(IInventory invo, Item fItem, int fDamage, Item rItem, int rDamage)
	{
		for(int i = 0; i < invo.getSizeInventory(); i++)
		{
			ItemStack stack = invo.getStackInSlot(i);

			if(stack != null)
			{
				if(stack.getItem() == fItem && (stack.getItemDamage() == fDamage || fDamage <= -1))
				{
					invo.setInventorySlotContents(i, new ItemStack(rItem, stack.stackSize, fDamage <= -1? stack.getItemDamage() : rDamage));
				}
			}
		}
	}

	@SubscribeEvent
	public void onJump(LivingJumpEvent event)
	{
		if(event.entityLiving.isPotionActive(EnviroPotion.frostbite))
		{
			if(event.entityLiving.getActivePotionEffect(EnviroPotion.frostbite).getAmplifier() > 0)
			{
				event.entityLiving.motionY = 0;
			}
		}
	}

	@SubscribeEvent
	public void onLand(LivingFallEvent event)
	{
		if(event.entityLiving.getRNG().nextInt(5) == 0)
		{
			EM_PhysManager.schedulePhysUpdate(event.entityLiving.worldObj, MathHelper.floor_double(event.entityLiving.posX), MathHelper.floor_double(event.entityLiving.posY - 1), MathHelper.floor_double(event.entityLiving.posZ), true, "Jump");
		}
	}

	private static boolean firstload = false;
	@SubscribeEvent
	public void onWorldLoad(Load event)
	{
		if(event.world.isRemote)
		{
			return;
		}

		//Load Custom Configs
		if (!firstload)
		{
			//EnviroMine.theWorldEM = EM_WorldData.get(event.world);
			EM_ConfigHandler.initProfile();
			firstload = true;
		}

		if(EM_PhysManager.worldStartTime < 0)
		{
			EM_PhysManager.worldStartTime = event.world.getTotalWorldTime();
		}

		MinecraftServer server = MinecraftServer.getServer();

		if(EM_Settings.worldDir == null && server.isServerRunning())
		{
			if(EnviroMine.proxy.isClient())
			{
				EM_Settings.worldDir = MinecraftServer.getServer().getFile("saves/" + server.getFolderName());
			} else
			{
				EM_Settings.worldDir = server.getFile(server.getFolderName());
			}

			MineshaftBuilder.loadBuilders(new File(EM_Settings.worldDir.getAbsolutePath(), "data/EnviroMineshafts"));
			Earthquake.loadQuakes(new File(EM_Settings.worldDir.getAbsolutePath(), "data/EnviroEarthquakes"));
		}
	}

	@SubscribeEvent
	public void onWorldUnload(Unload event)
	{
		EM_StatusManager.saveAndDeleteWorldTrackers(event.world);

		if(!event.world.isRemote)
		{
			if(!MinecraftServer.getServer().isServerRunning())
			{
				EM_PhysManager.physSchedule.clear();
				EM_PhysManager.excluded.clear();
				EM_PhysManager.usedSlidePositions.clear();
				EM_PhysManager.worldStartTime = -1;
				EM_PhysManager.chunkDelay.clear();

				if(EM_Settings.worldDir != null)
				{
					MineshaftBuilder.saveBuilders(new File(EM_Settings.worldDir.getAbsolutePath(), "data/EnviroMineshafts"));
					Earthquake.saveQuakes(new File(EM_Settings.worldDir.getAbsolutePath(), "data/EnviroEarthquakes"));
				}
				Earthquake.Reset();;
				MineshaftBuilder.clearBuilders();
				GasBuffer.reset();

				EM_Settings.worldDir = null;
			}
		}
	}

	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load event)
	{
		if(event.world.isRemote)
		{
			return;
		}

		if(!EM_PhysManager.chunkDelay.containsKey(event.world.provider.dimensionId + "" + event.getChunk().xPosition + "," + event.getChunk().zPosition))
		{
			EM_PhysManager.chunkDelay.put(event.world.provider.dimensionId + "" + event.getChunk().xPosition + "," + event.getChunk().zPosition, event.world.getTotalWorldTime() + EM_Settings.chunkDelay);
		}
	}

	@SubscribeEvent
	public void onWorldSave(Save event)
	{
		EM_StatusManager.saveAllWorldTrackers(event.world);
		if(EM_Settings.worldDir != null && event.world.provider.dimensionId == 0)
		{
			MineshaftBuilder.saveBuilders(new File(EM_Settings.worldDir.getAbsolutePath(), "data/EnviroMineshafts"));
		}
	}

	protected static MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer)
	{
		float f = 1.0F;
		float f1 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * f;
		float f2 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * f;
		double d0 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * (double)f;
		double d1 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * (double)f + (double)(par1World.isRemote ? par2EntityPlayer.getEyeHeight() - par2EntityPlayer.getDefaultEyeHeight() : par2EntityPlayer.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
		double d2 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * (double)f;
		Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = 5.0D;
		if(par2EntityPlayer instanceof EntityPlayerMP)
		{
			d3 = ((EntityPlayerMP)par2EntityPlayer).theItemInWorldManager.getBlockReachDistance();
		}
		Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
		return par1World.func_147447_a(vec3, vec31, true, !true, false);
	}

	/* Client only events */
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onEntitySoundPlay(PlaySoundAtEntityEvent event)
	{
		if(event.entity.getEntityData().getBoolean("EM_Hallucination"))
		{
			ResourceLocation resLoc = new ResourceLocation(event.name);
			if(new File(resLoc.getResourcePath()).exists())
			{
				Minecraft.getMinecraft().getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(event.name), 1.0F, (event.entity.worldObj.rand.nextFloat() - event.entity.worldObj.rand.nextFloat()) * 0.2F + 1.0F, (float)event.entity.posX, (float)event.entity.posY, (float)event.entity.posZ));
			}
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onMusicPlay(PlaySoundEvent17 event)
	{
		if(Minecraft.getMinecraft().thePlayer != null && event.category == SoundCategory.MUSIC && Minecraft.getMinecraft().thePlayer.dimension == EM_Settings.caveDimID)
		{
			// Replaces background music with cave ambience in the cave dimension
			event.result = PositionedSoundRecord.func_147673_a(new ResourceLocation("enviromine", "cave_ambience"));
		}
	}

	HashMap<String, EntityLivingBase> playerMob = new HashMap<String, EntityLivingBase>();

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRender(RenderPlayerEvent.Pre event)
	{
        if(EM_Settings.enablePlayerRandomMobRender) {
            if (Minecraft.getMinecraft().thePlayer.isPotionActive(EnviroPotion.insanity) && Minecraft.getMinecraft().thePlayer.getActivePotionEffect(EnviroPotion.insanity).getAmplifier() >= 2) {
                event.setCanceled(true);

                EntityLivingBase entity = playerMob.get(event.entityPlayer.getCommandSenderName());
                if (entity == null || entity.worldObj != event.entityPlayer.worldObj) {
                    BiomeGenBase biome = event.entityPlayer.worldObj.getBiomeGenForCoords(MathHelper.floor_double(event.entityPlayer.posX), MathHelper.floor_double(event.entityPlayer.posZ));
                    ArrayList<SpawnListEntry> spawnList = (ArrayList<SpawnListEntry>) biome.getSpawnableList(EnumCreatureType.monster);

                    if (spawnList.size() <= 0) {
                        entity = new EntityZombie(event.entityPlayer.worldObj);
                    } else {
                        int spawnIndex = event.entityPlayer.getRNG().nextInt(spawnList.size());
                        try {
                            entity = (EntityLiving) spawnList.get(spawnIndex).entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{event.entityPlayer.worldObj});
                        } catch (Exception e) {
                            entity = new EntityZombie(event.entityPlayer.worldObj);
                        }
                    }

                    playerMob.put(event.entityPlayer.getCommandSenderName(), entity);
                }
                entity.renderYawOffset = event.entityPlayer.renderYawOffset;
                entity.prevRenderYawOffset = event.entityPlayer.prevRenderYawOffset;
                entity.cameraPitch = event.entityPlayer.cameraPitch;
                entity.posX = event.entityPlayer.posX;
                entity.posY = event.entityPlayer.posY - event.entityPlayer.yOffset;
                entity.posZ = event.entityPlayer.posZ;
                entity.prevPosX = event.entityPlayer.prevPosX;
                entity.prevPosY = event.entityPlayer.prevPosY - event.entityPlayer.yOffset;
                entity.prevPosZ = event.entityPlayer.prevPosZ;
                entity.lastTickPosX = event.entityPlayer.lastTickPosX;
                entity.lastTickPosY = event.entityPlayer.lastTickPosY - event.entityPlayer.yOffset;
                entity.lastTickPosZ = event.entityPlayer.lastTickPosZ;
                entity.rotationPitch = event.entityPlayer.rotationPitch;
                entity.prevRotationPitch = event.entityPlayer.prevRotationPitch;
                entity.rotationYaw = event.entityPlayer.rotationYaw;
                entity.prevRotationYaw = event.entityPlayer.prevRotationYaw;
                entity.rotationYawHead = event.entityPlayer.rotationYawHead;
                entity.prevRotationYawHead = event.entityPlayer.prevRotationYawHead;
                entity.limbSwingAmount = event.entityPlayer.limbSwingAmount;
                entity.prevLimbSwingAmount = event.entityPlayer.prevLimbSwingAmount;
                entity.limbSwing = event.entityPlayer.limbSwing;
                entity.prevSwingProgress = event.entityPlayer.prevSwingProgress;
                entity.swingProgress = event.entityPlayer.swingProgress;
                entity.swingProgressInt = event.entityPlayer.swingProgressInt;
                ItemStack[] equipped = event.entityPlayer.getLastActiveItems();
                entity.setCurrentItemOrArmor(0, event.entityPlayer.getHeldItem());
                entity.setCurrentItemOrArmor(1, equipped[0]);
                entity.setCurrentItemOrArmor(2, equipped[1]);
                entity.setCurrentItemOrArmor(3, equipped[2]);
                entity.setCurrentItemOrArmor(4, equipped[3]);
                entity.motionX = event.entityPlayer.motionX;
                entity.motionY = event.entityPlayer.motionY;
                entity.motionZ = event.entityPlayer.motionZ;
                entity.ticksExisted = event.entityPlayer.ticksExisted;
                GL11.glPushMatrix();
                //GL11.glRotatef(180F, 0F, 1F, 0F);
                //GL11.glRotatef(180F - (event.entityPlayer.renderYawOffset + (event.entityPlayer.renderYawOffset - event.entityPlayer.prevRenderYawOffset) * partialTicks), 0F, 1F, 0F);
                RenderManager.instance.renderEntitySimple(entity, partialTicks);
                GL11.glPopMatrix();
            } else {
                playerMob.clear();
            }
        }
	}

	float partialTicks = 1F;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void RenderTickEvent(TickEvent.RenderTickEvent event)
	{
		partialTicks = event.renderTickTime;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onItemTooltip(ItemTooltipEvent event)
	{
		if (event.itemStack != null) {
			
			if(ArmorTempUtils.checkArmorPropertyItemStack(event.itemStack, false)){
				event.toolTip.add(EnumChatFormatting.GOLD + "[" + I18n.format("enviromine.tooltip.armor.sealed") + "]");
			} else if(ArmorTempUtils.checkArmorPropertyItemStack(event.itemStack, true)) {
				event.toolTip.add(EnumChatFormatting.YELLOW + "[" + I18n.format("enviromine.tooltip.armor.resistance") + "]");
			}
			
			if(event.itemStack.hasTagCompound()) 
			{
				if (event.itemStack.getTagCompound().hasKey(EM_Settings.CAMEL_PACK_FILL_TAG_KEY)) {
				int fill = event.itemStack.getTagCompound().getInteger(EM_Settings.CAMEL_PACK_FILL_TAG_KEY);
				int max = event.itemStack.getTagCompound().getInteger(EM_Settings.CAMEL_PACK_MAX_TAG_KEY);
				if (fill > max) {
					fill = max;
					event.itemStack.getTagCompound().setInteger(EM_Settings.CAMEL_PACK_FILL_TAG_KEY, fill);
				}

				int disp = fill <= 0 ? 0 : (int)((float)fill/(float)max *100);
				event.toolTip.add(new ChatComponentTranslation("misc.enviromine.tooltip.water", disp + "%",  fill, max).getUnformattedText());
				//event.toolTip.add("Water: " + disp + "% ("+fill+"/"+max+")");
			}
				
				if(event.itemStack.getTagCompound().getLong("EM_ROT_DATE") > 0 && EM_Settings.foodSpoiling) {
					double rotDate = event.itemStack.getTagCompound().getLong("EM_ROT_DATE");
					double rotTime = event.itemStack.getTagCompound().getLong("EM_ROT_TIME");
					double curTime = event.entity.worldObj.getTotalWorldTime();
	
					if(curTime - rotDate <= 0) {
						event.toolTip.add(new ChatComponentTranslation("misc.enviromine.tooltip.rot", "0%" , MathHelper.floor_double((curTime - rotDate)/24000L) , MathHelper.floor_double(rotTime/24000L)).getUnformattedText());
					} else {
						event.toolTip.add(new ChatComponentTranslation("misc.enviromine.tooltip.rot", MathHelper.floor_double((curTime - rotDate)/rotTime * 100D) + "%", MathHelper.floor_double((curTime - rotDate)/24000L), MathHelper.floor_double(rotTime/24000L)).getUnformattedText());
					}
	
				} 
			
				if(event.itemStack.getTagCompound().hasKey(EM_Settings.GAS_MASK_FILL_TAG_KEY)) {
					int i = event.itemStack.getTagCompound().getInteger(EM_Settings.GAS_MASK_FILL_TAG_KEY);
					int max = event.itemStack.getTagCompound().getInteger(EM_Settings.GAS_MASK_MAX_TAG_KEY);
					int disp = (i <= 0 ? 0 : i > max ? 100 : (int)(i/(max/100F)));
					event.toolTip.add(new ChatComponentTranslation("misc.enviromine.tooltip.filter", disp + "%", i, max).getUnformattedText());
				}
			}
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.modID.equals(EM_Settings.MOD_ID))
		{
			for(Configuration config : EM_ConfigMenu.tempConfigs)
			{
				config.save();
			}

			EM_ConfigHandler.ReloadConfig();
		}
	}

	@SubscribeEvent
	public void onCrafted(ItemCraftedEvent event) // Prevents exploit of making foods with almost rotten food to prolong total life of food supplies
	{
		if(event.player.worldObj.isRemote || event.crafting == null || event.crafting.getItem() == null)
		{
			return;
		}

		RotProperties rotProps = null;
		long rotTime = (long)(EM_Settings.foodRotTime * 24000L);

		if(EM_Settings.rotProperties.containsKey("" + Item.itemRegistry.getNameForObject(event.crafting.getItem())))
		{
			rotProps = EM_Settings.rotProperties.get("" + Item.itemRegistry.getNameForObject(event.crafting.getItem()));
			rotTime = (long)(rotProps.days * 24000L);
		} else if(EM_Settings.rotProperties.containsKey("" + Item.itemRegistry.getNameForObject(event.crafting.getItem()) + "," + event.crafting.getItemDamage()))
		{
			rotProps = EM_Settings.rotProperties.get("" + Item.itemRegistry.getNameForObject(event.crafting.getItem()) + "," + event.crafting.getItemDamage());
			rotTime = (long)(rotProps.days * 24000L);
		}

		if(rotProps == null)
		{
			return; // Crafted item is not a rotting food
		}

		long lowestDate = -1L;

		for(int i = 0; i < event.craftMatrix.getSizeInventory(); i++)
		{
			ItemStack stack = event.craftMatrix.getStackInSlot(i);

			if(stack == null || stack.getItem() == null || stack.getTagCompound() == null)
			{
				continue;
			}

			if(stack.getTagCompound().hasKey("EM_ROT_DATE") && (lowestDate < 0 || stack.getTagCompound().getLong("EM_ROT_DATE") < lowestDate))
			{
				lowestDate = stack.getTagCompound().getLong("EM_ROT_DATE");
			}
		}

		if(lowestDate >= 0)
		{
			if(event.crafting.getTagCompound() == null)
			{
				event.crafting.setTagCompound(new NBTTagCompound());
			}

			event.crafting.getTagCompound().setLong("EM_ROT_DATE", lowestDate);
			event.crafting.getTagCompound().setLong("EM_ROT_TIME", rotTime);
		}
	}

	/**
	 * Checks a block against the config list of blocks that can purify water in an above cauldron
	 */
	public static boolean isCauldronHeatingBlock(Block blockInWorld, int metaInWorld)
	{
		// Return clean water if the cauldron is being heated
		for (String cauldronBlock : EM_Settings.cauldronHeatingBlocks)
		{
			Block block; String blockname=cauldronBlock; int blockmeta=-1;
			// Break entry into namespace and meta
			String[] split_entry = cauldronBlock.split(":");

			if (split_entry.length <2 || split_entry.length>3) {return false;} // Illegal entry
			else if (split_entry.length==3) // A meta is provided
			{
				try {blockmeta = Integer.parseInt(split_entry[2]);}
				catch (Exception e) {blockmeta = -1;}

				blockname = split_entry[0]+":"+split_entry[1];
			}
			// Try to parse block
			block = Block.getBlockFromName(blockname);

			// This is a heating block that's on the list
			if (block != null && block==blockInWorld && (blockmeta<0 || (blockmeta==metaInWorld))) {return true;}
		}

		return false;
	}
}
