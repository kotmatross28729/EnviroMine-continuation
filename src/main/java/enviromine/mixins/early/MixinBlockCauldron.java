package enviromine.mixins.early;

import static net.minecraft.block.BlockCauldron.func_150027_b;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import enviromine.blocks.tiles.TileEntityCauldron;
import enviromine.blocks.water.BlockEnviroMineWater;
import enviromine.items.ItemModBucket;
import enviromine.utils.WaterUtils;

@Mixin(value = BlockCauldron.class, priority = 1003)
public class MixinBlockCauldron implements ITileEntityProvider {

    @Shadow
    public void func_150024_a(World worldIn, int x, int y, int z, int level) {}

    @Inject(method = "onBlockActivated", at = @At(value = "HEAD"), cancellable = true)
    public void onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ, CallbackInfoReturnable<Boolean> cir) {
        if (worldIn.isRemote) {
            cir.setReturnValue(true);
        } else {
            ItemStack itemstack = player.inventory.getCurrentItem();
            if (itemstack == null) {
                cir.setReturnValue(true);
            } else {
                TileEntity te = worldIn.getTileEntity(x, y, z);
                int i1 = worldIn.getBlockMetadata(x, y, z);
                int j1 = func_150027_b(i1);

                if (te instanceof TileEntityCauldron cauldron) {
                    if (itemstack.getItem() instanceof ItemModBucket bucket
                        && bucket.containedFluid instanceof BlockEnviroMineWater enviroMineWater) {
                        if (j1 < 3) {
                            if (!player.capabilities.isCreativeMode) {
                                player.inventory.setInventorySlotContents(
                                    player.inventory.currentItem,
                                    new ItemStack(Items.bucket));
                            }
                            cauldron.setWaterType(WaterUtils.getTypeFromFluid(enviroMineWater.getFluid()));
                            func_150024_a(worldIn, x, y, z, 3);
                        }
                        cir.setReturnValue(true);
                    } else if (itemstack.getItem() == Items.snowball) {
                        if (!player.capabilities.isCreativeMode) {
                            itemstack.stackSize--;
                        }
                        cauldron.setWaterType(WaterUtils.coolDown(cauldron.getWaterType()));
                        cir.setReturnValue(true);
                    } else {
                        for (int id : OreDictionary.getOreIDs(itemstack)) {
                            if (OreDictionary.getOreID("dustSalt") == id) {
                                if (!player.capabilities.isCreativeMode) {
                                    itemstack.stackSize--;
                                }
                                cauldron.setWaterType(WaterUtils.saltDown(cauldron.getWaterType()));
                                cir.setReturnValue(true);
                                break;
                            }
                        }
                    }

                }
            }
        }
    }

    @Inject(
        method = "onBlockActivated",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockCauldron;func_150024_a(Lnet/minecraft/world/World;IIII)V",
            ordinal = 0,
            shift = At.Shift.BEFORE))
    public void onBlockActivated2(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ, CallbackInfoReturnable<Boolean> cir) {
        TileEntity te = worldIn.getTileEntity(x, y, z);
        if (te instanceof TileEntityCauldron cauldron) {
            cauldron.setWaterType(WaterUtils.WATER_TYPES.CLEAN);
        }
    }

    @Inject(
        method = "fillWithRain",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlockMetadataWithNotify(IIIII)Z",
            shift = At.Shift.BEFORE))
    public void fillWithRain(World worldIn, int x, int y, int z, CallbackInfo ci) {
        TileEntity te = worldIn.getTileEntity(x, y, z);
        if (te instanceof TileEntityCauldron cauldron) {
            cauldron.setWaterType(WaterUtils.WATER_TYPES.DIRTY);
        }
    }

    @Inject(method = "onEntityCollidedWithBlock", at = @At(value = "HEAD"))
    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn, CallbackInfo ci) {
        int l = func_150027_b(worldIn.getBlockMetadata(x, y, z));
        float f = (float) y + (6.0F + (float) (3 * l)) / 16.0F;
        TileEntity te = worldIn.getTileEntity(x, y, z);
        if (!worldIn.isRemote && te instanceof TileEntityCauldron cauldron) {
            if (l > 0 && entityIn.boundingBox.minY <= (double) f) {
                if (cauldron.getWaterType().isRadioactive) {
                    if (entityIn instanceof EntityLivingBase entityLivingBase) {
                        // TODO rad
                        entityLivingBase.addPotionEffect(new PotionEffect(Potion.poison.id, 100));
                        // ContaminationUtil.contaminate(entityLivingBase, ContaminationUtil.HazardType.RADIATION,
                        // ContaminationUtil.ContaminationType.CREATIVE, 5F);
                    }
                }
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCauldron();
    }

}
