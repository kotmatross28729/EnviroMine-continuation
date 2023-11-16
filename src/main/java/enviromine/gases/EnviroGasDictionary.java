package enviromine.gases;

import org.apache.logging.log4j.Level;

import enviromine.core.EM_ConfigHandler.EnumLogVerbosity;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;
import enviromine.gases.types.GasCarbonDioxide;
import enviromine.gases.types.GasCarbonMonoxide;
import enviromine.gases.types.GasFire;
import enviromine.gases.types.GasHydrogenSulfide;
import enviromine.gases.types.GasMethane;
import enviromine.gases.types.GasNUKE;
import enviromine.gases.types.GasNitrogenDioxide;
import enviromine.gases.types.GasSulfurDioxide;

public class EnviroGasDictionary
{
	public static EnviroGas[] gasList = new EnviroGas[128];
	
	public static EnviroGas gasFire = new GasFire("Gas Fire", 0);
	public static EnviroGas carbonMonoxide = new GasCarbonMonoxide("Carbon Monoxide", 1);
	public static EnviroGas carbonDioxide = new GasCarbonDioxide("Carbon Dioxide", 2);
	public static EnviroGas hydrogenSulfide = new GasHydrogenSulfide("Hydrogen Sulfide", 3);
	public static EnviroGas methane = new GasMethane("Methane", 4);
	public static EnviroGas sulfurDioxide = new GasSulfurDioxide("Sulfur Dioxide", 5);
	public static EnviroGas nitrogenDioxide = new GasNitrogenDioxide("Nitrogen Dioxide", 6);
	public static EnviroGas NUKE = new GasNUKE("Nuclear Gas", 7);
	
	public static void addNewGas(EnviroGas newGas, int id)
	{
		if(id < 128 && id >= 0)
		{
			if(gasList[id] == null)
			{
				gasList[id] = newGas;
				if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.ALL.getLevel()) EnviroMine.logger.log(Level.INFO, "Registered gas " + newGas.name);
			} else
			{
				if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.NORMAL.getLevel()) EnviroMine.logger.log(Level.WARN, "Unable to add gas " + newGas.name + " to dictionary: ID " + id + " is used!");
			}
		} else
		{
			if (EM_Settings.loggerVerbosity >= EnumLogVerbosity.NORMAL.getLevel()) EnviroMine.logger.log(Level.WARN, "Unable to add gas " + newGas.name + " to dictionary: ID out of bounds!");
		}
	}
}
