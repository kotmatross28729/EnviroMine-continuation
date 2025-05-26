package enviromine.handlers.compat;

import com.hbm.handler.threading.PacketThreading;
import static enviromine.handlers.EM_EventManager.ReplaceInvoItems;
import static enviromine.handlers.EM_EventManager.getBlockWithinAABB;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.gas.BlockGasFlammable;
import com.hbm.blocks.gas.BlockGasMonoxide;
import com.hbm.blocks.gas.BlockGasRadonDense;
import com.hbm.blocks.gas.BlockGasRadonTomb;
import com.hbm.blocks.gas.BlockVacuum;
import com.hbm.main.MainRegistry;
import com.hbm.packet.toclient.AuxParticlePacketNT;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.ContaminationUtil;

import cpw.mods.fml.common.network.NetworkRegistry;
import enviromine.core.EM_Settings;
import enviromine.handlers.ObjectHandler;

public class EM_EventManager_NTM {

    public static void handleOnBlockBreakCoal(BlockEvent.BreakEvent event) {
        if (event.block == ObjectHandler.burningCoal) {
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {

                int x = event.x + dir.offsetX;
                int y = event.y + dir.offsetY;
                int z = event.z + dir.offsetZ;

                if (event.world.rand.nextInt(2) == 0 && event.world.getBlock(x, y, z) == Blocks.air)
                    event.world.setBlock(x, y, z, ModBlocks.gas_coal);
            }
        }
    }

    private static AudioWrapper audioBreathing;

    public static void handleGasMaskSound(LivingEvent.LivingUpdateEvent event, InventoryPlayer invo) {
        ItemStack mask = invo.armorItemInSlot(3);
        if (mask != null && mask.getItem() != null && mask.getItem() == ObjectHandler.gasMask) {
            if (mask.hasTagCompound() && mask.getTagCompound()
                .hasKey(EM_Settings.GAS_MASK_FILL_TAG_KEY)) {
                if ((audioBreathing == null || !audioBreathing.isPlaying())) {
                    audioBreathing = MainRegistry.proxy.getLoopedSound(
                        "enviromine:breathing",
                        (float) event.entityLiving.posX,
                        (float) event.entityLiving.posY,
                        (float) event.entityLiving.posZ,
                        0.1F,
                        5.0F,
                        1.0F,
                        5);
                    audioBreathing.startSound();
                }
                audioBreathing.updatePosition(
                    (float) event.entityLiving.posX,
                    (float) event.entityLiving.posY,
                    (float) event.entityLiving.posZ);
                audioBreathing.keepAlive();
            } else {
                if (audioBreathing != null) {
                    audioBreathing.stopSound();
                    audioBreathing = null;
                }
            }
        } else if (audioBreathing != null) {
            audioBreathing.stopSound();
            audioBreathing = null;
        }
    }

    public static void handleNTMGas(LivingEvent.LivingUpdateEvent event, AxisAlignedBB boundingBox,
        InventoryPlayer invo) {
        if (getBlockWithinAABB(boundingBox, event.entityLiving.worldObj, BlockGasFlammable.class)) {
            // Fire -> Blue fire
            ReplaceInvoItems(
                invo,
                Item.getItemFromBlock(ObjectHandler.davyLampBlock),
                1,
                Item.getItemFromBlock(ObjectHandler.davyLampBlock),
                2);
        } else if (getBlockWithinAABB(boundingBox, event.entityLiving.worldObj, BlockGasMonoxide.class)
            || getBlockWithinAABB(boundingBox, event.entityLiving.worldObj, BlockGasRadonDense.class)
            || getBlockWithinAABB(boundingBox, event.entityLiving.worldObj, BlockGasRadonTomb.class)
            || getBlockWithinAABB(boundingBox, event.entityLiving.worldObj, BlockVacuum.class)) {
                // Fire -> No fire
                ReplaceInvoItems(
                    invo,
                    Item.getItemFromBlock(ObjectHandler.davyLampBlock),
                    1,
                    Item.getItemFromBlock(ObjectHandler.davyLampBlock),
                    0);
            }
    }

    public static void applyRadiation(EntityPlayer par3EntityPlayer, float ammount) {
        ContaminationUtil.contaminate(
            par3EntityPlayer,
            ContaminationUtil.HazardType.RADIATION,
            ContaminationUtil.ContaminationType.RAD_BYPASS,
            ammount);
    }
    
    public static void handleVomit(EntityLivingBase entityLivingBase) {
        if (entityLivingBase instanceof EntityPlayer && ((EntityPlayer) entityLivingBase).capabilities.isCreativeMode)
            return;

        int ix = MathHelper.floor_double(entityLivingBase.posX);
        int iy = MathHelper.floor_double(entityLivingBase.posY);
        int iz = MathHelper.floor_double(entityLivingBase.posZ);
        Random rand = new Random(entityLivingBase.getEntityId());

        int offset = rand.nextInt(EM_Settings.vomitTickFullCycle);

        if (entityLivingBase.isPotionActive(Potion.wither) && EM_Settings.enableWitherVomit) {
            int amplifier = entityLivingBase.getActivePotionEffect(Potion.wither)
                .getAmplifier();

            // (worldTime + offset) % totalTicks < activeTicks
            if ((entityLivingBase.worldObj.getTotalWorldTime() + offset) % EM_Settings.vomitTickFullCycle
                < (EM_Settings.vomitDuration + ((long) amplifier * EM_Settings.vomitWitherAmplifierMultiplier))
                && canVomit(entityLivingBase)) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setString("type", "vomit");
                nbt.setString("mode", "blood");
                nbt.setInteger("count", 25);
                nbt.setInteger("entity", entityLivingBase.getEntityId());
                PacketThreading.createAllAroundThreadedPacket(
                    new AuxParticlePacketNT(nbt, 0, 0, 0),
                    new NetworkRegistry.TargetPoint(
                        entityLivingBase.dimension,
                        entityLivingBase.posX,
                        entityLivingBase.posY,
                        entityLivingBase.posZ,
                        25));

                if ((entityLivingBase.worldObj.getTotalWorldTime() + offset) % EM_Settings.vomitTickFullCycle == 1) {
                    entityLivingBase.worldObj.playSoundEffect(ix, iy, iz, "hbm:player.vomit", 1.0F, 1.0F);
                    entityLivingBase.addPotionEffect(
                        new PotionEffect(
                            Potion.hunger.id,
                            EM_Settings.vomitHungerDuration,
                            EM_Settings.vomitHungerAmplifier));
                }
            }
        } else if ((entityLivingBase.isPotionActive(Potion.poison) || entityLivingBase.isPotionActive(Potion.confusion))
            && EM_Settings.enablePoisonNauseaVomit) {
                int amplifier = entityLivingBase
                    .getActivePotionEffect(
                        entityLivingBase.isPotionActive(Potion.poison) ? Potion.poison : Potion.confusion)
                    .getAmplifier();

                if ((entityLivingBase.worldObj.getTotalWorldTime() + offset) % EM_Settings.vomitTickFullCycle
                    < (EM_Settings.vomitDuration
                        + ((long) amplifier * EM_Settings.vomitPoisonNauseaAmplifierMultiplier))
                    && canVomit(entityLivingBase)) {
                    NBTTagCompound nbt = new NBTTagCompound();
                    nbt.setString("type", "vomit");
                    nbt.setString("mode", "normal");
                    nbt.setInteger("count", 15);
                    nbt.setInteger("entity", entityLivingBase.getEntityId());
                    PacketThreading.createAllAroundThreadedPacket(
                        new AuxParticlePacketNT(nbt, 0, 0, 0),
                        new NetworkRegistry.TargetPoint(
                            entityLivingBase.dimension,
                            entityLivingBase.posX,
                            entityLivingBase.posY,
                            entityLivingBase.posZ,
                            25));

                    if ((entityLivingBase.worldObj.getTotalWorldTime() + offset) % EM_Settings.vomitTickFullCycle
                        == 1) {
                        entityLivingBase.worldObj.playSoundEffect(ix, iy, iz, "hbm:player.vomit", 1.0F, 1.0F);
                        entityLivingBase.addPotionEffect(
                            new PotionEffect(
                                Potion.hunger.id,
                                EM_Settings.vomitHungerDuration,
                                EM_Settings.vomitHungerAmplifier));
                    }
                }
            }
    }

    private static boolean canVomit(Entity e) {
        return !e.isCreatureType(EnumCreatureType.waterCreature, false);
    }

}
