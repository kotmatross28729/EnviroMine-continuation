package enviromine.world.chunk;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SHROOM;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import cpw.mods.fml.common.eventhandler.Event.Result;
import enviromine.core.EM_Settings;
import enviromine.trackers.properties.CaveBaseProperties;
import enviromine.trackers.properties.CaveGenProperties;

public class ChunkProviderCaves implements IChunkProvider {

    private Random hellRNG;
    /** A NoiseGeneratorOctaves used in generating nether terrain */
    private NoiseGeneratorOctaves netherNoiseGen1;
    private NoiseGeneratorOctaves netherNoiseGen2;
    private NoiseGeneratorOctaves netherNoiseGen3;
    /** Determines whether slowsand or gravel can be generated at a location */
    private NoiseGeneratorOctaves slowsandGravelNoiseGen;
    /**
     * Determines whether something other than nettherack can be generated at a location
     */
    private NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
    public NoiseGeneratorOctaves netherNoiseGen6;
    public NoiseGeneratorOctaves netherNoiseGen7;
    /** Is the world that the nether is getting generated. */
    private World worldObj;
    private double[] noiseField;
    /**
     * Holds the noise used to determine whether slowsand can be generated at a location
     */
    private double[] slowsandNoise = new double[256];
    private double[] gravelNoise = new double[256];
    /**
     * Holds the noise used to determine whether something other than netherrack can be generated at a location
     */
    private double[] netherrackExclusivityNoise = new double[256];
    private MapGenModifiedCaves netherCaveGenerator = new MapGenModifiedCaves();
    private MapGenModifiedRavine ravineGenerator = new MapGenModifiedRavine();
    double[] noiseData1;
    double[] noiseData2;
    double[] noiseData3;
    double[] noiseData4;
    double[] noiseData5;

    public ChunkProviderCaves(World worldIn, long seed) {
        this.worldObj = worldIn;
        this.hellRNG = new Random(seed);
        this.netherNoiseGen1 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.netherNoiseGen2 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.netherNoiseGen3 = new NoiseGeneratorOctaves(this.hellRNG, 16); // Normally 8
        this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.netherNoiseGen6 = new NoiseGeneratorOctaves(this.hellRNG, 16); // Normally 10
        this.netherNoiseGen7 = new NoiseGeneratorOctaves(this.hellRNG, 16);

        NoiseGenerator[] noiseGens = { netherNoiseGen1, netherNoiseGen2, netherNoiseGen3, slowsandGravelNoiseGen,
            netherrackExculsivityNoiseGen, netherNoiseGen6, netherNoiseGen7 };
        noiseGens = TerrainGen.getModdedNoiseGenerators(worldIn, this.hellRNG, noiseGens);
        this.netherNoiseGen1 = (NoiseGeneratorOctaves) noiseGens[0];
        this.netherNoiseGen2 = (NoiseGeneratorOctaves) noiseGens[1];
        this.netherNoiseGen3 = (NoiseGeneratorOctaves) noiseGens[2];
        this.slowsandGravelNoiseGen = (NoiseGeneratorOctaves) noiseGens[3];
        this.netherrackExculsivityNoiseGen = (NoiseGeneratorOctaves) noiseGens[4];
        this.netherNoiseGen6 = (NoiseGeneratorOctaves) noiseGens[5];
        this.netherNoiseGen7 = (NoiseGeneratorOctaves) noiseGens[6];
    }

    // prepareHeights
    public void func_147419_a(int noise_x, int noise_z, Block[] blockList, byte[] metaList) {
        byte b0 = 4;
        byte b1 = (byte) EM_Settings.caveLiquidY; // Liquid height
        int k = b0 + 1;
        byte size_y = 33;// 17;
        int l = b0 + 1;
        this.noiseField = this.initializeNoiseField(
            this.noiseField,
            noise_x * b0,
            0,
            noise_z * b0, // Positions
            k,
            size_y,
            l // Size
        );

        for (int i1 = 0; i1 < b0; ++i1) {
            for (int j1 = 0; j1 < b0; ++j1) {
                for (int k1 = 0; k1 < 32; ++k1) {
                    double d0 = 0.125D;
                    double d1 = this.noiseField[((i1 + 0) * l + j1 + 0) * size_y + k1 + 0];
                    double d2 = this.noiseField[((i1 + 0) * l + j1 + 1) * size_y + k1 + 0];
                    double d3 = this.noiseField[((i1 + 1) * l + j1 + 0) * size_y + k1 + 0];
                    double d4 = this.noiseField[((i1 + 1) * l + j1 + 1) * size_y + k1 + 0];
                    double d5 = (this.noiseField[((i1 + 0) * l + j1 + 0) * size_y + k1 + 1] - d1) * d0;
                    double d6 = (this.noiseField[((i1 + 0) * l + j1 + 1) * size_y + k1 + 1] - d2) * d0;
                    double d7 = (this.noiseField[((i1 + 1) * l + j1 + 0) * size_y + k1 + 1] - d3) * d0;
                    double d8 = (this.noiseField[((i1 + 1) * l + j1 + 1) * size_y + k1 + 1] - d4) * d0;

                    for (int l1 = 0; l1 < 8; ++l1) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int i2 = 0; i2 < 4; ++i2) {
                            int j2 = i2 + i1 * 4 << 12 | 0 + j1 * 4 << 8 | k1 * 8 + l1;
                            short short1 = 256;
                            j2 -= short1;
                            double d14 = 0.25D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;

                            for (int k2 = 0; k2 < 4; ++k2) {
                                Block block = null;
                                byte meta = 0;

                                if (k1 * 8 + l1 < b1) {
                                    block = EM_Settings.caveLava ? Blocks.lava : Blocks.water;
                                }

                                if (d15 > 0.0D) {
                                    block = CaveBaseProperties.caveStone_block;
                                    meta = CaveBaseProperties.caveStone_meta;
                                }

                                blockList[j2 += short1] = block;
                                metaList[j2] = meta;
                                // j2 += short1;
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    @Deprecated
    // You should provide meatadata and biome data in the below method
    public void func_147418_b(int p_147418_1_, int p_147418_2_, Block[] p_147418_3_) {
        replaceBiomeBlocks(p_147418_1_, p_147418_2_, p_147418_3_, new byte[p_147418_3_.length], null);
    }

    public void replaceBiomeBlocks(int chunkX, int chunkZ, Block[] blockList, byte[] metaList, BiomeGenBase[] biomes) {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(
            this,
            chunkX,
            chunkZ,
            blockList,
            metaList,
            biomes,
            this.worldObj);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.DENY) return;

        byte b0 = 64;
        double d0 = 0.03125D;
        this.slowsandNoise = this.slowsandGravelNoiseGen
            .generateNoiseOctaves(this.slowsandNoise, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, d0, d0, 1.0D);
        this.gravelNoise = this.slowsandGravelNoiseGen
            .generateNoiseOctaves(this.gravelNoise, chunkX * 16, 109, chunkZ * 16, 16, 1, 16, d0, 1.0D, d0);
        this.netherrackExclusivityNoise = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(
            this.netherrackExclusivityNoise,
            chunkX * 16,
            chunkZ * 16,
            0,
            16,
            16,
            1,
            d0 * 2.0D,
            d0 * 2.0D,
            d0 * 2.0D);

        for (int k = 0; k < 16; ++k) {
            for (int l = 0; l < 16; ++l) {
                boolean apply_slowsand = this.slowsandNoise[k + l * 16] + this.hellRNG.nextDouble() * 0.2D > 0.0D;
                boolean apply_gravel = this.gravelNoise[k + l * 16] + this.hellRNG.nextDouble() * 0.2D > 0.0D;
                int i1 = (int) (this.netherrackExclusivityNoise[k + l * 16] / 3.0D + 3.0D
                    + this.hellRNG.nextDouble() * 0.25D);
                int j1 = -1;

                Block block = CaveBaseProperties.caveStone_block;
                byte meta = CaveBaseProperties.caveStone_meta;
                Block block1 = CaveBaseProperties.caveStone_block;
                byte meta1 = CaveBaseProperties.caveStone_meta;

                for (int k1 = 255; k1 >= 0; --k1) {
                    int l1 = (l * 16 + k) * 256 + k1;

                    if (k1 < 255 - this.hellRNG.nextInt(5) && k1 > 0 + this.hellRNG.nextInt(5)) {
                        Block block2 = blockList[l1];
                        int meta2 = metaList[l1];

                        if (block2 != null && block2.getMaterial() != Material.air) {
                            if (block2 == CaveBaseProperties.caveStone_block
                                && meta2 == CaveBaseProperties.caveStone_meta) {
                                if (j1 == -1) {
                                    if (i1 <= 0) {
                                        block = null;
                                        meta = 0;
                                        block1 = CaveBaseProperties.caveStone_block;
                                        meta1 = CaveBaseProperties.caveStone_meta;
                                    } else if (k1 >= b0 - 4 && k1 <= b0 + 1) {
                                        block = CaveBaseProperties.caveStone_block;
                                        meta = CaveBaseProperties.caveStone_meta;
                                        block1 = CaveBaseProperties.caveStone_block;
                                        meta1 = CaveBaseProperties.caveStone_meta;

                                        if (apply_gravel) {
                                            block = CaveBaseProperties.caveGravel_block;
                                            meta = CaveBaseProperties.caveGravel_meta;
                                            block1 = CaveBaseProperties.caveStone_block;
                                            meta1 = CaveBaseProperties.caveStone_meta;
                                        }

                                        if (apply_slowsand) {
                                            block = CaveBaseProperties.caveDirt_block;
                                            meta = CaveBaseProperties.caveDirt_meta;
                                            block1 = CaveBaseProperties.caveDirt_block;
                                            meta1 = CaveBaseProperties.caveDirt_meta;
                                        }
                                    }

                                    if (k1 < b0 && (block == null || block.getMaterial() == Material.air)) {
                                        block = EM_Settings.caveLava ? Blocks.lava : Blocks.water;
                                    }

                                    j1 = i1;

                                    if (k1 >= b0 - 1) {
                                        blockList[l1] = block;
                                        metaList[l1] = meta;
                                    } else {
                                        blockList[l1] = block1;
                                        metaList[l1] = meta1;
                                    }
                                } else if (j1 > 0) {
                                    --j1;
                                    blockList[l1] = block1;
                                    metaList[l1] = meta1;
                                }
                            }
                        } else {
                            j1 = -1;
                        }
                    } else {
                        blockList[l1] = Blocks.bedrock;
                        metaList[l1] = 0;
                    }
                }
            }
        }
    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    @Override
    public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
        return this.provideChunk(p_73158_1_, p_73158_2_);
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    @Override
    public Chunk provideChunk(int chunkX, int chunkZ) {
        this.hellRNG.setSeed((long) chunkX * 341873128712L + (long) chunkZ * 132897987541L);
        Block[] ablock = new Block[32768 * 2];
        byte[] meta = new byte[ablock.length];
        BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager()
            .loadBlockGeneratorData((BiomeGenBase[]) null, chunkX * 16, chunkZ * 16, 16, 16); // Forge Move up to allow
                                                                                              // for passing to
                                                                                              // replaceBiomeBlocks
        this.func_147419_a(chunkX, chunkZ, ablock, meta);

        this.replaceBiomeBlocks(chunkX, chunkZ, ablock, meta, abiomegenbase);
        this.netherCaveGenerator.func_151539_a(
            this,
            this.worldObj,
            (long) chunkX * 341873128712L + (long) chunkZ * 132897987541L,
            chunkX,
            chunkZ,
            ablock);
        this.ravineGenerator.func_151539_a(
            this,
            this.worldObj,
            (long) chunkX * 341873128712L + (long) chunkZ * 132897987541L,
            chunkX,
            chunkZ,
            ablock);
        Chunk chunk = new Chunk(this.worldObj, ablock, meta, chunkX, chunkZ);
        byte[] abyte = chunk.getBiomeArray();

        for (int k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) abiomegenbase[k].biomeID;
        }

        chunk.resetRelightChecks();
        return chunk;
    }

    /**
     * generates a subset of the level's terrain data. Takes 7 arguments: the [empty] noise array, the position, and the
     * size.
     */
    private double[] initializeNoiseField(double[] p_73164_1_, int p_73164_2_, int p_73164_3_, int p_73164_4_,
        int p_73164_5_, int p_73164_6_, int p_73164_7_) {
        ChunkProviderEvent.InitNoiseField event = new ChunkProviderEvent.InitNoiseField(
            this,
            p_73164_1_,
            p_73164_2_,
            p_73164_3_,
            p_73164_4_,
            p_73164_5_,
            p_73164_6_,
            p_73164_7_);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.DENY) return event.noisefield;

        if (p_73164_1_ == null) {
            p_73164_1_ = new double[p_73164_5_ * p_73164_6_ * p_73164_7_];
        }

        double d0 = 684.412D;
        double d1 = 2053.236D;
        this.noiseData4 = this.netherNoiseGen6.generateNoiseOctaves(
            this.noiseData4,
            p_73164_2_,
            p_73164_3_,
            p_73164_4_,
            p_73164_5_,
            1,
            p_73164_7_,
            1.0D,
            0.0D,
            1.0D);
        this.noiseData5 = this.netherNoiseGen7.generateNoiseOctaves(
            this.noiseData5,
            p_73164_2_,
            p_73164_3_,
            p_73164_4_,
            p_73164_5_,
            1,
            p_73164_7_,
            100.0D,
            0.0D,
            100.0D);
        this.noiseData1 = this.netherNoiseGen3.generateNoiseOctaves(
            this.noiseData1,
            p_73164_2_,
            p_73164_3_,
            p_73164_4_,
            p_73164_5_,
            p_73164_6_,
            p_73164_7_,
            d0 / 80.0D,
            d1 / 60.0D,
            d0 / 80.0D);
        this.noiseData2 = this.netherNoiseGen1.generateNoiseOctaves(
            this.noiseData2,
            p_73164_2_,
            p_73164_3_,
            p_73164_4_,
            p_73164_5_,
            p_73164_6_,
            p_73164_7_,
            d0,
            d1,
            d0);
        this.noiseData3 = this.netherNoiseGen2.generateNoiseOctaves(
            this.noiseData3,
            p_73164_2_,
            p_73164_3_,
            p_73164_4_,
            p_73164_5_,
            p_73164_6_,
            p_73164_7_,
            d0,
            d1,
            d0);
        int k1 = 0;
        int l1 = 0;
        double[] adouble1 = new double[p_73164_6_];
        int i2;

        for (i2 = 0; i2 < p_73164_6_; ++i2) {
            adouble1[i2] = Math.cos((double) i2 * Math.PI * 6.0D / (double) p_73164_6_) * 2.0D;
            double d2 = (double) i2;

            if (i2 > p_73164_6_ / 2) {
                d2 = (double) (p_73164_6_ - 1 - i2);
            }

            if (d2 < 4.0D) {
                d2 = 4.0D - d2;
                adouble1[i2] -= d2 * d2 * d2 * 10.0D;
            }
        }

        for (i2 = 0; i2 < p_73164_5_; ++i2) {
            for (int k2 = 0; k2 < p_73164_7_; ++k2) {
                double d3 = (this.noiseData4[l1] + 256.0D) / 512.0D;

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                double d4 = 0.0D;
                double d5 = this.noiseData5[l1] / 8000.0D;

                if (d5 < 0.0D) {
                    d5 = -d5;
                }

                d5 = d5 * 3.0D - 3.0D;

                if (d5 < 0.0D) {
                    d5 /= 2.0D;

                    if (d5 < -1.0D) {
                        d5 = -1.0D;
                    }

                    d5 /= 1.4D;
                    d5 /= 2.0D;
                    d3 = 0.0D;
                } else {
                    if (d5 > 1.0D) {
                        d5 = 1.0D;
                    }

                    d5 /= 6.0D;
                }

                d3 += 0.5D;
                d5 = d5 * (double) p_73164_6_ / 16.0D;
                ++l1;

                for (int j2 = 0; j2 < p_73164_6_; ++j2) {
                    double d6 = 0.0D;
                    double d7 = adouble1[j2];
                    double d8 = this.noiseData2[k1] / 512.0D;
                    double d9 = this.noiseData3[k1] / 512.0D;
                    double d10 = (this.noiseData1[k1] / 10.0D + 1.0D) / 2.0D;

                    if (d10 < 0.0D) {
                        d6 = d8;
                    } else if (d10 > 1.0D) {
                        d6 = d9;
                    } else {
                        d6 = d8 + (d9 - d8) * d10;
                    }

                    d6 -= d7;
                    double d11;

                    if (j2 > p_73164_6_ - 4) {
                        d11 = (double) ((float) (j2 - (p_73164_6_ - 4)) / 3.0F);
                        d6 = d6 * (1.0D - d11) + -10.0D * d11;
                    }

                    if ((double) j2 < d4) {
                        d11 = (d4 - (double) j2) / 4.0D;

                        if (d11 < 0.0D) {
                            d11 = 0.0D;
                        }

                        if (d11 > 1.0D) {
                            d11 = 1.0D;
                        }

                        d6 = d6 * (1.0D - d11) + -10.0D * d11;
                    }

                    p_73164_1_[k1] = d6;
                    ++k1;
                }
            }
        }

        return p_73164_1_;
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    @Override
    public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
        return true;
    }

    /**
     * Populates chunk with ores etc etc
     */
    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        BlockFalling.fallInstantly = true;

        if (EM_Settings.caveOreEvent) {
            MinecraftForge.EVENT_BUS
                .post(new PopulateChunkEvent.Pre(p_73153_1_, worldObj, hellRNG, p_73153_2_, p_73153_3_, false));
        }

        int k = p_73153_2_ * 16;
        int l = p_73153_3_ * 16;
        int j1;
        int k1;
        int l1;

        // boolean doGen = TerrainGen.populate(p_73153_1_, worldObj, hellRNG, p_73153_2_, p_73153_3_, false,
        // NETHER_LAVA);
        int i2;

        if (EM_Settings.caveOreEvent) {
            MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(worldObj, hellRNG, k, l));
        }

        if (TerrainGen.populate(p_73153_1_, worldObj, hellRNG, p_73153_2_, p_73153_3_, false, LAVA)
            && this.hellRNG.nextInt(8) == 0) {
            k1 = k + this.hellRNG.nextInt(16) + 8;
            l1 = this.hellRNG.nextInt(this.hellRNG.nextInt(248) + 8);
            i2 = l + this.hellRNG.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.lava)).generate(this.worldObj, this.hellRNG, k1, l1, i2);
        }

        boolean doGen = TerrainGen.populate(p_73153_1_, worldObj, hellRNG, p_73153_2_, p_73153_3_, false, DUNGEON);
        for (k1 = 0; doGen && k1 < EM_Settings.caveDungeons; ++k1) {
            l1 = k + this.hellRNG.nextInt(16) + 8;
            i2 = this.hellRNG.nextInt(256);
            int j2 = l + this.hellRNG.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(this.worldObj, this.hellRNG, l1, i2, j2);
        }

        doGen = TerrainGen.decorate(worldObj, hellRNG, k, l, SHROOM);
        if (doGen && this.hellRNG.nextInt(1) == 0) {
            j1 = k + this.hellRNG.nextInt(16) + 8;
            k1 = this.hellRNG.nextInt(256);
            l1 = l + this.hellRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Blocks.brown_mushroom)).generate(this.worldObj, this.hellRNG, j1, k1, l1);
        }

        if (doGen && this.hellRNG.nextInt(1) == 0) {
            j1 = k + this.hellRNG.nextInt(16) + 8;
            k1 = this.hellRNG.nextInt(256);
            l1 = l + this.hellRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Blocks.red_mushroom)).generate(this.worldObj, this.hellRNG, j1, k1, l1);
        }

        int j2;

        for (int index = 0; index < EM_Settings.caveGenProperties.size(); index++) {
            CaveGenProperties oreProps = EM_Settings.caveGenProperties.get(index);

            WorldGenModifiedMinable worldgenminable = new WorldGenModifiedMinable(
                oreProps.ore,
                oreProps.oreMeta,
                oreProps.size,
                oreProps.source,
                oreProps.srcMeta);
            for (k1 = 0; k1 < oreProps.veins; ++k1) {
                l1 = k + this.hellRNG.nextInt(16);
                i2 = this.hellRNG.nextInt(oreProps.maxY - oreProps.minY) + oreProps.minY;
                j2 = l + this.hellRNG.nextInt(16);
                worldgenminable.generate(this.worldObj, this.hellRNG, l1, i2, j2);
            }
        }

        if (EM_Settings.caveOreEvent) {
            MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(worldObj, hellRNG, k, l));
            MinecraftForge.EVENT_BUS
                .post(new PopulateChunkEvent.Post(p_73153_1_, worldObj, hellRNG, p_73153_2_, p_73153_3_, false));
        }

        BlockFalling.fallInstantly = false;
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go. If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        return true;
    }

    /**
     * Save extra data not associated with any Chunk. Not saved during autosave, only during world unload. Currently
     * unimplemented.
     */
    @Override
    public void saveExtraData() {}

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    @Override
    public boolean canSave() {
        return true;
    }

    /**
     * Converts the instance data to a readable string.
     */
    @Override
    public String makeString() {
        return "CaveRandomLevelSource";
    }

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(p_73155_2_, p_73155_4_);
        return biomegenbase.getSpawnableList(p_73155_1_);
    }

    @Override
    public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_,
        int p_147416_5_) {
        return null;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public void recreateStructures(int p_82695_1_, int p_82695_2_) {}
}
