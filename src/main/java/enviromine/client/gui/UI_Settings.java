package enviromine.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;

@SideOnly(Side.CLIENT)
public class UI_Settings {

	public static String enviroSettingsFile = "UI_Settings";

	public static boolean ShowGuiIcons = true;
	public static float guiScale = 1.0F;
	public static boolean sweatParticals = true;
	public static boolean insaneParticals = true;
	public static boolean useFarenheit = false;
	public static boolean ShowText = false;
	public static boolean ShowDebug = false;
	public static boolean breathSound = true;
	public static int breathPause = 300;
	public static float breathVolume = 0.75F;
	public static boolean minimalHud =  false;
	public static int screenWidth;
	public static int screenHeight;
	public static boolean overlay = true;

	public static void writeToNBT(NBTTagCompound nbt)
	{

    	nbt.setBoolean("ShowGuiIcons", ShowGuiIcons);
    	nbt.setFloat("guiScale", guiScale);
    	nbt.setBoolean("sweatParticals", sweatParticals);
    	nbt.setBoolean("insaneParticals", insaneParticals);
    	nbt.setBoolean("useFarenheit", useFarenheit);
    	nbt.setBoolean("ShowText", ShowText);
    	nbt.setBoolean("ShowDebug", ShowDebug);
    	nbt.setBoolean("breathSound", breathSound);
    	nbt.setInteger("breathPause", breathPause);
    	nbt.setFloat("breathVolume", breathVolume);
    	nbt.setBoolean("minimalHud",minimalHud);
    	nbt.setBoolean("overlay", overlay);
	}

	public static void readFromNBT(NBTTagCompound nbt)
	{

		ShowGuiIcons = nbt.hasKey("ShowGuiIcons") ? nbt.getBoolean("ShowGuiIcons") : ShowGuiIcons;
		guiScale = nbt.hasKey("guiScale") ? nbt.getFloat("guiScale") : guiScale;
		sweatParticals = nbt.hasKey("sweatParticals") ? nbt.getBoolean("sweatParticals") : sweatParticals;
		insaneParticals = nbt.hasKey("insaneParticals") ? nbt.getBoolean("insaneParticals") : insaneParticals;
		useFarenheit = nbt.hasKey("useFarenheit") ? nbt.getBoolean("useFarenheit") : useFarenheit;
		ShowText = nbt.hasKey("ShowText") ? nbt.getBoolean("ShowText") : ShowText;
		ShowDebug = nbt.hasKey("ShowDebug") ? nbt.getBoolean("ShowDebug") : ShowDebug;
		breathSound = nbt.hasKey("breathSound") ? nbt.getBoolean("breathSound") : breathSound;
		breathPause = nbt.hasKey("breathPause") ? nbt.getInteger("breathPause") : breathPause;
		breathVolume = nbt.hasKey("breathVolume") ? nbt.getFloat("breathVolume") : breathVolume;
		minimalHud = nbt.hasKey("minimalHud") ? nbt.getBoolean("minimalHud") : minimalHud;
		overlay = nbt.hasKey("overlay") ? nbt.getBoolean("overlay") : overlay;
	}
}
