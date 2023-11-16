package enviromine.version;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import enviromine.core.EM_ConfigHandler.EnumLogVerbosity;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeHooks;

/**
 * Adapted from Jabelar's tutorials
 * http://jabelarminecraft.blogspot.com/p/minecraft-forge-1721710-making-mod.html
 * Parallel threading provided by Roadhog360
 * @author AstroTibs
 */
public class VersionChecker extends Thread
{
	public static VersionChecker instance = new VersionChecker();
	
	private static boolean isLatestVersion = false;
	private static boolean warnaboutfailure = false;
    private static String latestVersion = "";
    private static boolean isUpdateCheckFinished = false;
    private static boolean quitChecking = false;
    private static boolean hasThreadStarted = false;
    
    private static final String CHECK_FOR_VERSIONS_AT_URL = "You can check for new versions at "+EM_Settings.URL;
    
	@Override
	public void run()
	{
        InputStream in = null;
        
        try
        {
        	URL url = new URL(EM_Settings.VERSION_CHECKER_URL);
            in = url.openStream();
        } 
        catch (Exception e)
        {
        	if (!warnaboutfailure)
        	{
        		if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) 
        		{
	            	EnviroMine.logger.log(Level.ERROR, "Could not connect with server to compare " + EM_Settings.MOD_NAME + " version");
	        		EnviroMine.logger.log(Level.ERROR, CHECK_FOR_VERSIONS_AT_URL);
        		}
            	warnaboutfailure=true;
        	}
        }
        
        try
        {
            latestVersion = IOUtils.readLines(in, Charset.defaultCharset()).get(0);
        }
        catch (Exception e)
        {
        	if (!warnaboutfailure)
        	{
        		if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) 
        		{
	        		EnviroMine.logger.log(Level.ERROR, "Failed to compare " + EM_Settings.MOD_NAME + " version");
	        		EnviroMine.logger.log(Level.ERROR, CHECK_FOR_VERSIONS_AT_URL);
        		}
        		warnaboutfailure=true;
        	}
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
        
        isLatestVersion = EM_Settings.VERSION.equals(latestVersion);
        
        if (!this.isLatestVersion() && !latestVersion.equals("") && !latestVersion.equals(null))
        {
        	if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) EnviroMine.logger.log(Level.INFO, "This version of "+EM_Settings.MOD_NAME_COLORIZED+" (" + EM_Settings.VERSION + ") differs from the latest version: " + latestVersion);
        }
        
        isUpdateCheckFinished = true;
    }
	
    public boolean isLatestVersion()
    {
    	return isLatestVersion;
    }
    
    public String getLatestVersion()
    {
    	return latestVersion;
    }
	
    /**
     * PlayerTickEvent is going to be used for version checking.
     * @param event
     */
    
    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onPlayerTickEvent(PlayerTickEvent event)
    {
    	// Used to repeat the version check
    	if (
    			(latestVersion.equals(null) || latestVersion.equals(""))
    			&& !warnaboutfailure // Skip the "run" if a failure was detected
    			&& !hasThreadStarted
    			)
    	{
    		start();
    		hasThreadStarted=true;
    	}
    	
    	if (
    			event.player.ticksExisted>=200
    			&& !quitChecking
    			&& isUpdateCheckFinished) 
    	{
    		if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.LOW.getLevel()) 
    		{
    			EnviroMine.logger.log(Level.ERROR, EM_Settings.MOD_NAME+" version check failed.");
        		EnviroMine.logger.log(Level.ERROR, CHECK_FOR_VERSIONS_AT_URL);
    		}
    		
    		quitChecking=true;
    	}
    	
        if (
        		event.player.worldObj.isRemote
        		&& event.phase == Phase.END // Stops doubling the checks unnecessarily
        		&& event.player.ticksExisted>=30
        		&& isUpdateCheckFinished
        		&& !quitChecking
        		)
        {
        	// Ordinary version checker
        	if (
            		EM_Settings.versionChecker
            		&& !instance.isLatestVersion()
            		&& !latestVersion.equals(null)
            		&& !latestVersion.equals("")
            		&& !(EM_Settings.VERSION).contains("DEV")
        			)
        	{
                quitChecking=true;
                
                event.player.addChatComponentMessage(
                		new ChatComponentText(
                				EM_Settings.MOD_NAME_COLORIZED + 
                				EnumChatFormatting.RESET + " version " + EnumChatFormatting.YELLOW + this.getLatestVersion() + EnumChatFormatting.RESET +
                				" is available! Get it at:"
                		 ));
                event.player.addChatComponentMessage(ForgeHooks.newChatWithLinks(EM_Settings.URL));
        	}
        }
        
        if (quitChecking || !EM_Settings.versionChecker)
        {
        	FMLCommonHandler.instance().bus().unregister(instance);
        	FMLCommonHandler.instance().bus().unregister(VersionChecker.instance);
            return;
        }
    }
}