package enviromine.mixins.early.waterCompat;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import enviromine.utils.WaterUtils;

@Mixin(value = World.class, priority = 1003)
public class MixinWorld {

    // Because I'm an idiot and I don't know how to fucking replace
    // `block == Blocks.water || block == Blocks.flowing_water` to `WaterUtils.isWater(block)`

    @Inject(method = "canBlockFreezeBody", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void canBlockFreezeBodyTransformer(int x, int y, int z, boolean byWater,
        CallbackInfoReturnable<Boolean> cir) {
        Block block = this.getBlock(x, y, z);
        if (WaterUtils.isWater(block, false)) {
            cir.setReturnValue(enviromine$canBlockFreezeBody(x, y, z, byWater));
        }
    }

    @Unique
    public boolean enviromine$canBlockFreezeBody(int x, int y, int z, boolean byWater) {
        BiomeGenBase biomegenbase = this.getBiomeGenForCoords(x, z);
        float f = biomegenbase.getFloatTemperature(x, y, z);

        if (f < 0.15F) {
            if (y >= 0 && y < 256 && this.getSavedLightValue(EnumSkyBlock.Block, x, y, z) < 10) {

                if (this.getBlockMetadata(x, y, z) == 0) {
                    if (!byWater) {
                        return true;
                    }

                    boolean flag1 = true;

                    if (flag1 && this.getBlock(x - 1, y, z)
                        .getMaterial() != Material.water) {
                        flag1 = false;
                    }

                    if (flag1 && this.getBlock(x + 1, y, z)
                        .getMaterial() != Material.water) {
                        flag1 = false;
                    }

                    if (flag1 && this.getBlock(x, y, z - 1)
                        .getMaterial() != Material.water) {
                        flag1 = false;
                    }

                    if (flag1 && this.getBlock(x, y, z + 1)
                        .getMaterial() != Material.water) {
                        flag1 = false;
                    }

                    return !flag1;
                }
            }

        }
        return false;
    }

    @Shadow
    @Final
    public WorldProvider provider;

    @Shadow
    public int getSavedLightValue(EnumSkyBlock p_72972_1_, int p_72972_2_, int p_72972_3_, int p_72972_4_) {
        return p_72972_1_.defaultLightValue;
    }

    @Shadow
    public BiomeGenBase getBiomeGenForCoords(final int x, final int z) {
        return provider.getBiomeGenForCoords(x, z);
    }

    @Shadow
    public int getBlockMetadata(int p_72805_1_, int p_72805_2_, int p_72805_3_) {
        return 0;
    }

    @Shadow
    public Block getBlock(int p_147439_1_, int p_147439_2_, int p_147439_3_) {
        return Blocks.air;
    }

}
