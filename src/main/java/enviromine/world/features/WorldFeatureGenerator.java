package enviromine.world.features;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import cpw.mods.fml.common.IWorldGenerator;
import enviromine.blocks.tiles.TileEntityGas;
import enviromine.core.EM_Settings;
import enviromine.gases.EnviroGasDictionary;
import enviromine.handlers.ObjectHandler;
import enviromine.trackers.properties.DimensionProperties;
import enviromine.world.features.mineshaft.MineshaftBuilder;

public class WorldFeatureGenerator implements IWorldGenerator {

    public static ArrayList<int[]> pendingMines = new ArrayList<int[]>();
    public static boolean disableMineScan = false;

    public WorldFeatureGenerator() {}

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
        IChunkProvider chunkProvider) {
        if (world.isRemote) {
            return;
        }

        DimensionProperties dimensionProp = null;
        boolean allowMines = true;

        if (EM_Settings.dimensionProperties.containsKey(world.provider.dimensionId)) {
            dimensionProp = EM_Settings.dimensionProperties.get(world.provider.dimensionId);
            allowMines = dimensionProp.mineshaftGen;
        }

        if (allowMines && EM_Settings.oldMineGen) {
            if (!disableMineScan) {
                MineshaftBuilder.scanGrids(world, chunkX, chunkZ, random);
            }

            for (int i = MineshaftBuilder.pendingBuilders.size() - 1; i >= 0; i--) {
                MineshaftBuilder builder = MineshaftBuilder.pendingBuilders.get(i);

                if (builder.checkAndBuildSegments(chunkX, chunkZ)) {
                    MineshaftBuilder.pendingBuilders.remove(i);
                    if (MineshaftBuilder.scannedGrids
                        .containsKey("" + (chunkX / 64) + "," + (chunkZ / 64) + "," + world.provider.dimensionId)) {
                        int remBuilders = MineshaftBuilder.scannedGrids
                            .get("" + (chunkX / 64) + "," + (chunkZ / 64) + "," + world.provider.dimensionId);
                        MineshaftBuilder.scannedGrids.put(
                            "" + (chunkX / 64) + "," + (chunkZ / 64) + "," + world.provider.dimensionId,
                            remBuilders - 1);
                    }
                }
            }
        }

        if (EM_Settings.gasGen && !EM_Settings.noGases) {
            for (int i = 4; i >= 0; i--) {
                // TODO: gas rework
                GenGasPocket(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
            }
        }
    }

    public void GenGasPocket(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
        IChunkProvider chunkProvider) {
        int rX = (chunkX * 16) + random.nextInt(16);
        int rY = 1 + random.nextInt(32);
        int rZ = (chunkZ * 16) + random.nextInt(16);

        while (world.getBlock(rX, rY - 1, rZ) == Blocks.air && rY > 1) {
            rY -= 1;
        }

        if (world.getSavedLightValue(EnumSkyBlock.Sky, rX, rY, rZ) > 1) {
            return;
        }

        if (world.getBlock(rX, rY, rZ) == Blocks.air) {
            Block bBlock = world.getBlock(rX, rY - 1, rZ);
            if ((rY < 16 && rY > 0) || world.provider.isHellWorld) {
                if (bBlock.getMaterial() == Material.water) {
                    world.setBlock(rX, rY, rZ, ObjectHandler.gasBlock, 0, 2);
                    TileEntity tile = world.getTileEntity(rX, rY, rZ);

                    if (tile instanceof TileEntityGas) {
                        TileEntityGas gasTile = (TileEntityGas) tile;
                        gasTile.addGas(EnviroGasDictionary.hydrogenSulfide.gasID, 10);
                        // EnviroMine.logger.log(Level.INFO, "Generating hydrogen sulfide at (" + rX + "," + rY + "," +
                        // rZ + ")");
                    }
                } else if (bBlock.getMaterial() == Material.lava || bBlock.getMaterial() == Material.fire) {
                    world.setBlock(rX, rY, rZ, ObjectHandler.gasBlock, 0, 2);
                    TileEntity tile = world.getTileEntity(rX, rY, rZ);

                    if (tile instanceof TileEntityGas) {
                        TileEntityGas gasTile = (TileEntityGas) tile;
                        gasTile.addGas(EnviroGasDictionary.carbonMonoxide.gasID, 25);
                        gasTile.addGas(EnviroGasDictionary.sulfurDioxide.gasID, 25);
                        // EnviroMine.logger.log(Level.INFO, "Generating carbon monoxide at (" + rX + "," + rY + "," +
                        // rZ + ")");
                    }
                } else {
                    world.setBlock(rX, rY, rZ, ObjectHandler.gasBlock, 0, 2);
                    TileEntity tile = world.getTileEntity(rX, rY, rZ);

                    if (tile instanceof TileEntityGas) {
                        TileEntityGas gasTile = (TileEntityGas) tile;
                        gasTile.addGas(EnviroGasDictionary.sulfurDioxide.gasID, 20);
                        gasTile.addGas(EnviroGasDictionary.carbonDioxide.gasID, 30);
                        // EnviroMine.logger.log(Level.INFO, "Generating sulfur dioxide at (" + rX + "," + rY + "," + rZ
                        // + ")");
                    }
                }
            } else {
                world.setBlock(rX, rY, rZ, ObjectHandler.gasBlock, 0, 2);
                TileEntity tile = world.getTileEntity(rX, rY, rZ);

                if (tile instanceof TileEntityGas) {
                    TileEntityGas gasTile = (TileEntityGas) tile;
                    gasTile.addGas(EnviroGasDictionary.carbonDioxide.gasID, 50);
                    // EnviroMine.logger.log(Level.INFO, "Generating carbon dioxide at (" + rX + "," + rY + "," + rZ +
                    // ")");
                }
            }
        }
    }
}
