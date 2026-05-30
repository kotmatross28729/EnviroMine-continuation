package enviromine.handlers.compat;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import cpw.mods.fml.common.eventhandler.Event.Result;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.common.item.ItemWaterBag;
import enviromine.blocks.water.BlockEnviroMineWater;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.handlers.EM_EventManager;
import enviromine.items.compat.EnviroItemWaterBottle_NTM;
import enviromine.trackers.EnviroDataTracker;
import enviromine.utils.EnviroUtils;
import enviromine.utils.WaterUtils;
import enviromine.EnviroPotion;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class EM_EventManager_Growthcraft {

    // Directly use Growthcraft's ItemWaterBag type
    public static boolean isWaterBagItem(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemWaterBag;
    }

    // Get dosage from Growthcraft config (no reflection)
    public static int getWaterBagDosage() {
        return GrowthCraftCellar.getConfig().waterBagDosage;
    }

    // Handle water bag filling
    public static void handleWaterBagFill(EntityPlayer player, int x, int y, int z, int side, ItemStack stack, PlayerInteractEvent event) {
        World world = player.worldObj;
        if (world.isRemote) return;

        // Determine actual water source coordinates (same logic as water bottle)
        int i = x, j = y, k = z;
        Block block = world.getBlock(i, j, k);
        if (block.getMaterial() != Material.water && block != Blocks.cauldron) {
            int[] adj = EnviroUtils.getAdjacentBlockCoordsFromSide(i, j, k, side);
            int ni = adj[0], nj = adj[1], nk = adj[2];
            Block adjBlock = world.getBlock(ni, nj, nk);
            if (adjBlock.getMaterial() == Material.water || adjBlock == Blocks.cauldron) {
                i = ni; j = nj; k = nk;
                block = adjBlock;
            } else {
                return;
            }
        }

        if (!world.canMineBlock(player, i, j, k) || !player.canPlayerEdit(i, j, k, side, stack)) return;

        boolean isValidCauldron = (block == Blocks.cauldron && world.getBlockMetadata(i, j, k) > 0);
        boolean isWaterBlock = (block == Blocks.water || block == Blocks.flowing_water) &&
            !(world.getBlockMetadata(i, j, k) > .2f && EM_Settings.finiteWater);

        if (!isWaterBlock && !isValidCauldron) return;

        // Get EnviroMine water type
        WaterUtils.WATER_TYPES waterType = EM_EventManager.getWaterType(world, i, j, k);
        if (waterType == null) return;

        // Cauldron heating
        if (isValidCauldron && EM_EventManager.isCauldronHeatingBlock(world.getBlock(i, j-1, k), world.getBlockMetadata(i, j-1, k))) {
            waterType = WaterUtils.heatUp(waterType);
        }

        // Get Fluid
        Fluid fluid = null;
        if (waterType == WaterUtils.WATER_TYPES.CLEAN) {
            fluid = FluidRegistry.WATER;
        } else {
            Block blockType = WaterUtils.getBlockFromType(waterType);
            if (blockType instanceof BlockEnviroMineWater) {
                fluid = ((BlockEnviroMineWater) blockType).getFluid();
            }
        }
        if (fluid == null) return;

        int dosage = getWaterBagDosage();
        FluidStack fluidStack = new FluidStack(fluid, dosage);

        if (stack.getItem() instanceof IFluidContainerItem) {
            IFluidContainerItem container = (IFluidContainerItem) stack.getItem();
            int filled = container.fill(stack, fluidStack, false);
            if (filled <= 0) return;

            // Consume water source
            if (isValidCauldron) {
                world.setBlockMetadataWithNotify(i, j, k, world.getBlockMetadata(i, j, k) - 1, 2);
            } else if (isWaterBlock && EM_Settings.finiteWater) {
                world.setBlock(i, j, k, Blocks.flowing_water, world.getBlockMetadata(i, j, k) + 1, 2);
            }

            container.fill(stack, fluidStack, true);
            world.playSoundAtEntity(player, "random.drink", 1.0F, 1.0F);

            event.setCanceled(true);
            event.useItem = Result.DENY;
            event.useBlock = Result.DENY;
        }
    }

    // Handle water bag drinking effects
    public static void applyWaterBagDrinkEffects(EntityPlayer player, ItemStack stack, EnviroDataTracker tracker) {
        if (!(stack.getItem() instanceof IFluidContainerItem)) return;
        IFluidContainerItem container = (IFluidContainerItem) stack.getItem();
        FluidStack fluidStack = container.getFluid(stack);
        if (fluidStack == null || fluidStack.amount <= 0) return;

        Fluid fluid = fluidStack.getFluid();
        WaterUtils.WATER_TYPES type;
        if (fluid == FluidRegistry.WATER) {
            type = WaterUtils.WATER_TYPES.CLEAN;
        } else {
            type = WaterUtils.getTypeFromFluid(fluid);
            if (type == null) type = WaterUtils.WATER_TYPES.CLEAN;
        }

        if (type.isRadioactive && EnviroMine.isHbmLoaded) {
            EnviroItemWaterBottle_NTM.applyRadiation(player, 5.0F);
        }

        if (type.isDirty) {
            if (player.getRNG().nextInt(4) == 0) {
                player.addPotionEffect(new PotionEffect(Potion.hunger.id, 600));
            }
            if (player.getRNG().nextInt(4) == 0) {
                player.addPotionEffect(new PotionEffect(Potion.poison.id, 200));
            }
        }

        if (type.isSalty) {
            if (player.getActivePotionEffect(EnviroPotion.dehydration) != null && player.getRNG().nextInt(5) == 0) {
                int amp = player.getActivePotionEffect(EnviroPotion.dehydration).getAmplifier();
                player.addPotionEffect(new PotionEffect(EnviroPotion.dehydration.id, 600, amp + 1));
            } else {
                player.addPotionEffect(new PotionEffect(EnviroPotion.dehydration.id, 600));
            }
        }

        if (type.temperatureInfluence != 0.0F) {
            tracker.bodyTemp += type.temperatureInfluence;
        }

        if (type.hydration > 0.0F) {
            tracker.hydrate(type.hydration);
        } else if (type.hydration < 0.0F) {
            tracker.dehydrate(Math.abs(type.hydration));
        }
    }
}
