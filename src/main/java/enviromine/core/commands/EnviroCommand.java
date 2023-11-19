package enviromine.core.commands;

import java.util.ArrayList;
import java.util.List;

import enviromine.handlers.EM_StatusManager;
import enviromine.trackers.EnviroDataTracker;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;

public class EnviroCommand extends CommandBase
{

	private String add = StatCollector.translateToLocal("commands.enviromine.envirostat.add");
	private String set = StatCollector.translateToLocal("commands.enviromine.envirostat.set");
	private String temp = StatCollector.translateToLocal("commands.enviromine.envirostat.temp");
	private String sanity = StatCollector.translateToLocal("commands.enviromine.envirostat.sanity");

    private String blood = StatCollector.translateToLocal("commands.enviromine.envirostat.blood");
	private String water = StatCollector.translateToLocal("commands.enviromine.envirostat.water");
	private String air = StatCollector.translateToLocal("commands.enviromine.envirostat.air");

	@Override
	public String getCommandName()
	{
		return "envirostat";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "/envirostat <playername> <"+add+", "+set+"> <"+temp+", "+sanity+", "+blood+", "+water+", "+air+"> <float>";
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

	@Override
	public void processCommand(ICommandSender sender, String[] astring)
	{

		if(astring.length != 4)
		{
			this.ShowUsage(sender);
			return;
		}

		EntityPlayerMP player = getPlayer(sender, astring[0]);

		String target = player.getCommandSenderName();

		EnviroDataTracker tracker = EM_StatusManager.lookupTrackerFromUsername(target);

		if(tracker == null)
		{
			this.ShowNoTracker(sender);
			return;
		}

		try
		{
			float value = Float.parseFloat(astring[3]);

			if(astring[1].equalsIgnoreCase(add))
			{
				if(astring[2].equalsIgnoreCase(temp))
				{
					tracker.bodyTemp += value;
				} else if(astring[2].equalsIgnoreCase(sanity))
				{
					tracker.sanity += value;
				} else if(astring[2].equalsIgnoreCase(blood))
                {
                    tracker.blood += value;
                } else if(astring[2].equalsIgnoreCase(water))
				{
					tracker.hydration += value;
				} else if(astring[2].equalsIgnoreCase(air))
				{
					tracker.airQuality += value;
				} else
				{
					this.ShowUsage(sender);
					return;
				}
			} else if(astring[1].equalsIgnoreCase(set))
			{
				if(astring[2].equalsIgnoreCase(temp))
				{
					tracker.bodyTemp = value;
				} else if(astring[2].equalsIgnoreCase(sanity))
				{
					tracker.sanity = value;

				} else if(astring[2].equalsIgnoreCase(blood))
                {
                    tracker.blood = value;

                } else if(astring[2].equalsIgnoreCase(water))
				{
					tracker.hydration = value;
				} else if(astring[2].equalsIgnoreCase("air"))
				{
					tracker.airQuality = value;
				} else
				{
					this.ShowUsage(sender);
					return;
				}
			} else
			{
				this.ShowUsage(sender);
				return;
			}

			tracker.fixFloatingPointErrors();
			return;
		} catch(Exception e)
		{
			this.ShowUsage(sender);
			return;
		}
	}

	public void ShowUsage(ICommandSender sender)
	{
		sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
	}

	public void ShowNoTracker(ICommandSender sender)
	{
		sender.addChatMessage(new ChatComponentTranslation("commands.enviromine.envirostat.noTracker"));
	}

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
	@SuppressWarnings("unchecked")
	@Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] strings)
    {
        if(strings.length == 1)
        {
        	return getListOfStringsMatchingLastWord(strings, MinecraftServer.getServer().getAllUsernames());
        } else if(strings.length == 2)
        {
        	return getListOfStringsMatchingLastWord(strings, new String[]{add, set});
        } else if(strings.length == 3)
        {
        	return getListOfStringsMatchingLastWord(strings, new String[]{temp, sanity, blood, water, air});
        } else
        {
        	return new ArrayList<String>();
        }
    }
}
