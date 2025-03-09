package enviromine.handlers.compat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.biome.BiomeGenBase;

import lotr.common.world.LOTRWorldChunkManager;
import lotr.common.world.biome.LOTRBiome;

public class EM_StatusManager_LOTR {

    // All compatibility with LOTR from the EM_StatusManager class goes here

    public static BiomeGenBase findLOTRBiome(EntityLivingBase entityLiving, int i, int k) {
        if (entityLiving.worldObj.getWorldChunkManager() instanceof LOTRWorldChunkManager) {
            return (LOTRBiome) entityLiving.worldObj.getBiomeGenForCoords(i, k);
        }
        return null;
    }
}
