package enviromine.utils;

import enviromine.core.EM_ConfigHandler.EnumLogVerbosity;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.handlers.ObjectHandler;
import enviromine.trackers.properties.StabilityType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockGlowstone;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.logging.log4j.Level;
import thaumcraft.common.blocks.BlockMagicalLeaves;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import static enviromine.core.EnviroMine.isTCLoaded;

public class EnviroUtils {

    public static final String[] reservedNames = new String[] {"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};
                               //  ↑ scary
    public static final char[] specialCharacters = new char[] {'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};
	private static final String IEEP_PLAYER_WITCHERY = "WitcheryExtendedPlayer";
	private static final String IEEP_PLAYER_WITCHERY_CREATURE_TYPE = "CreatureType";
	private static final String IEEP_PLAYER_WITCHERY_VAMPIRE_LEVEL = "VampireLevel";
	private static final String IEEP_PLAYER_WITCHERY_WEREWOLF_LEVEL = "WerewolfLevel";
	private static final String IEEP_PLAYER_WITCHERY_DEMON_LEVEL = "DemonLevel";
	private static final String IEEP_PLAYER_MO_ANDROID = "AndroidPlayer";
	private static final String IEEP_PLAYER_MO_ISANDROID = "isAndroid";

	
	public static int[] getAdjacentBlockCoordsFromSide(int x, int y, int z, int side)
	{
		int[] coords = new int[3];
		coords[0] = x;
		coords[1] = y;
		coords[2] = z;

		ForgeDirection dir = ForgeDirection.getOrientation(side);
		switch(dir)
		{
			case NORTH:
			{
				coords[2] -= 1;
				break;
			}
			case SOUTH:
			{
				coords[2] += 1;
				break;
			}
			case WEST:
			{
				coords[0] -= 1;
				break;
			}
			case EAST:
			{
				coords[0] += 1;
				break;
			}
			case UP:
			{
				coords[1] += 1;
				break;
			}
			case DOWN:
			{
				coords[1] -= 1;
				break;
			}
			case UNKNOWN:
			{
				break;
			}
		}

		return coords;
	}


	public static String replaceULN(String unlocalizedName)
	{
		unlocalizedName = unlocalizedName.replaceAll("[\\(\\)]", "");
		unlocalizedName = unlocalizedName.replaceAll("\\.+", "\\_");

		return unlocalizedName;
	}

	public static float convertToFarenheit(float num)
	{
		return convertToFarenheit(num, 2);
	}
	public static float convertToFarenheit(float num, int decimalPlace)
	{
		float newNum = (float) ((num * 1.8) + 32F);
		BigDecimal convert = new BigDecimal(Float.toString(newNum));
		convert.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);

		return convert.floatValue();
	}

	public static float convertToCelcius(float num)
	{
		return((num - 32F) * (5F / 9F));
	}


	public static float getBiomeTemp(BiomeGenBase biome)
	{
		return getBiomeTemp(biome.temperature);
	}
	public static float getBiomeTemp(int x, int y, int z, BiomeGenBase biome)
	{
		return getBiomeTemp(biome.getFloatTemperature(x, y, z));
	}
	private static float getBiomeTemp(float biomeTemp)
	{
		// You can calibrate temperatures using these
		// This does not take into account the time of day (These are the midday maximums)
		float maxTemp = 45F; // Desert
		float minTemp = -15F;

		// CALCULATE!
        return (float)(biomeTemp >= 0? Math.sin(Math.toRadians(biomeTemp*45F)) *maxTemp : Math.sin(Math.toRadians(biomeTemp*45F)) *minTemp) ;
	}

	/*
	 * This isn't accurate enough to
	 */
	public static String getBiomeWater(BiomeGenBase biome)
	{
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
		for(int i = 0; i < typeArray.length; i++)
		{
			typeList.add(typeArray[i]);
		}

        if(typeList.contains(Type.HOT) && !typeList.contains(Type.WET) && !typeList.contains(Type.SAVANNA)) {
            return "HOT";
        }
        else if(typeList.contains(Type.HOT) && (!typeList.contains(Type.WET) || looksBad)) {
            return "DIRTY_WARM";
        }
        else if(typeList.contains(Type.HOT) && typeList.contains(Type.WET)) {
            return "CLEAN_WARM";
        }
        else if(!typeList.contains(Type.COLD) && (typeList.contains(Type.SWAMP) || typeList.contains(Type.JUNGLE) || typeList.contains(Type.DEAD) || typeList.contains(Type.WASTELAND) || looksBad) )
        {
            return "DIRTY";
        }
        else if(typeList.contains(Type.OCEAN) || typeList.contains(Type.BEACH)) {
            return "SALTY";
        }
        else if(typeList.contains(Type.COLD) && ((!typeList.contains(Type.SNOWY) || typeList.contains(Type.CONIFEROUS) || biome.temperature < 0F) && !looksBad)) {
            return "CLEAN_COLD";
        }
        else if(typeList.contains(Type.COLD) && ((!typeList.contains(Type.SNOWY) || typeList.contains(Type.CONIFEROUS) || biome.temperature < 0F) && looksBad)) {
            return "DIRTY_COLD";
        }
        else if(typeList.contains(Type.COLD) && typeList.contains(Type.SNOWY)) {
            return "FROSTY";
        }
        else {
            return "CLEAN";
        }
	}

	public static StabilityType getDefaultStabilityType(Block block)
	{
		StabilityType type = null;

		Material material = block.getMaterial();

		if(block instanceof BlockMobSpawner || block instanceof BlockLadder || block instanceof BlockWeb || block instanceof BlockSign || block instanceof BlockBed || block instanceof BlockDoor || block instanceof BlockAnvil || block instanceof BlockGravel || block instanceof BlockPortal || block instanceof BlockEndPortal || block instanceof BlockEndPortalFrame || block == Blocks.grass || block == ObjectHandler.elevator || block == Blocks.end_stone || block.getMaterial() == Material.vine || !block.getMaterial().blocksMovement())
		{
			type = EM_Settings.stabilityTypes.get("none");
		} else if(block instanceof BlockGlowstone)
		{
			type = EM_Settings.stabilityTypes.get("glowstone");
		} else if(isTCLoaded && block instanceof BlockMagicalLeaves ){

            type = EM_Settings.stabilityTypes.get("average");
        } else if(block instanceof BlockFalling)
		{
			type = EM_Settings.stabilityTypes.get("sand-like");
		} else if(material == Material.iron || material == Material.wood || block instanceof BlockObsidian || block == Blocks.stonebrick || block == Blocks.brick_block || block == Blocks.quartz_block)
		{
			type = EM_Settings.stabilityTypes.get("strong");
		} else if(material == Material.rock || material == Material.glass || material == Material.ice || block instanceof BlockLeavesBase)
		{
			type = EM_Settings.stabilityTypes.get("average");
		}

        else
		{
			type = EM_Settings.stabilityTypes.get(EM_Settings.defaultStability);
		}

		if(type == null)
		{
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.ERROR, "Block " + block.getUnlocalizedName() + " has a null StabilityType. Crash imminent!");
		}

		return type;
	}

	public static String SafeFilename(String filename)
	{
		String safeName = filename;
		for(String reserved : reservedNames)
		{
			if(safeName.equalsIgnoreCase(reserved))
			{
				safeName = "_" + safeName + "_";
			}
		}

		for(char badChar : specialCharacters)
		{
			safeName = safeName.replace(badChar, '_');
		}

		return safeName;
	}

	/**
	 * Retrieves a tag compound for a specified IEEP identifier
	 */
	public static NBTTagCompound getIEEPTag(Entity entity, String identifier)
	{
		IExtendedEntityProperties ieep = entity.getExtendedProperties(identifier);

		if (ieep != null)
		{
			NBTTagCompound ieepnbt = new NBTTagCompound();
			ieep.saveNBTData(ieepnbt);

			if(ieepnbt.hasKey(identifier))
			{
				return ieepnbt.getCompoundTag(identifier);
			}
		}

		return null;
	}

	/**
	 * Returns the CreatureType value from Witchery's IExtendedEntityProperties
	 * Returns -1 if not found
	 */
	public static int getWitcheryCreatureType(Entity entity)
	{
		NBTTagCompound ieep_witchery = getIEEPTag(entity, IEEP_PLAYER_WITCHERY);
		return ieep_witchery!=null && ieep_witchery.hasKey(IEEP_PLAYER_WITCHERY_CREATURE_TYPE) ? ieep_witchery.getInteger(IEEP_PLAYER_WITCHERY_CREATURE_TYPE) : -1;
	}
	public static boolean isPlayerCurrentlyWitcheryWolf(EntityPlayer player) {return getWitcheryCreatureType(player)==1;}
	public static boolean isPlayerCurrentlyWitcheryWerewolf(EntityPlayer player) {return getWitcheryCreatureType(player)==2;}
	public static boolean isPlayerCurrentlyWitcheryBat(EntityPlayer player) {return getWitcheryCreatureType(player)==3;}

	/**
	 * Returns the DemonLevel value from Witchery's IExtendedEntityProperties
	 */
	public static int getWitcheryDemonLevel(Entity entity)
	{
		NBTTagCompound ieep_witchery = getIEEPTag(entity, IEEP_PLAYER_WITCHERY);
		return ieep_witchery!=null && ieep_witchery.hasKey(IEEP_PLAYER_WITCHERY_DEMON_LEVEL) ? ieep_witchery.getInteger(IEEP_PLAYER_WITCHERY_DEMON_LEVEL) : 0;
	}
	public static boolean isPlayerADemon(EntityPlayer player) {return getWitcheryDemonLevel(player)>0;}

	/**
	 * Returns the VampireLevel value from Witchery's IExtendedEntityProperties
	 */
	public static int getWitcheryVampireLevel(Entity entity)
	{
		NBTTagCompound ieep_witchery = getIEEPTag(entity, IEEP_PLAYER_WITCHERY);
		return ieep_witchery!=null && ieep_witchery.hasKey(IEEP_PLAYER_WITCHERY_VAMPIRE_LEVEL) ? ieep_witchery.getInteger(IEEP_PLAYER_WITCHERY_VAMPIRE_LEVEL) : 0;
	}
	public static boolean isPlayerAVampire(EntityPlayer player) {return getWitcheryVampireLevel(player)>0;}

	/**
	 * Returns the WerewolfLevel value from Witchery's IExtendedEntityProperties
	 */
	public static int getWitcheryWerewolfLevel(Entity entity)
	{
		NBTTagCompound ieep_witchery = getIEEPTag(entity, IEEP_PLAYER_WITCHERY);
		return ieep_witchery!=null && ieep_witchery.hasKey(IEEP_PLAYER_WITCHERY_WEREWOLF_LEVEL) ? ieep_witchery.getInteger(IEEP_PLAYER_WITCHERY_WEREWOLF_LEVEL) : 0;
	}
	public static boolean isPlayerAWerewolf(EntityPlayer player) {return getWitcheryWerewolfLevel(player)>0;}

	/**
	 * Returns whether a player is a Matter Overdrive Android
	 */
	public static boolean isPlayerCurrentlyMOAndroid(EntityPlayer player)
	{
		NBTTagCompound ieep_witchery = getIEEPTag(player, IEEP_PLAYER_MO_ANDROID);
		return ieep_witchery!=null && ieep_witchery.hasKey(IEEP_PLAYER_MO_ISANDROID) ? ieep_witchery.getBoolean(IEEP_PLAYER_MO_ISANDROID) : false;
	}
}
