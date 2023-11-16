package enviromine.handlers;

import enviromine.core.EM_Settings;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;

public class EnviroAchievements
{
	public static AchievementPage page;
	
	// @ == Done
	
	// Caving related
	public static Achievement funwaysFault; 	 //@ Survive a cave in
	public static Achievement thatJustHappened;  //@ Survive a gas fire
	public static Achievement proMiner; 		 //@ 1 Hour total mining time (with physics & all effects)
	public static Achievement boreToTheCore; 	 //@ Enter the cave dimension
	public static Achievement intoTheDarkness; 	 //@ Travel 1K from cave dimension entrance and make it back alive
	public static Achievement itsPitchBlack; 	 //@ Travel 250 blocks in complete darkness in the cave dimension or encounter the Grue
	
	// Status effect related
	public static Achievement tradingFavours; 	 //@ Receive villager assistance
	public static Achievement thermalShock_dummy;//@ Used to place fire behind snowball
	public static Achievement thermalShock;	     //@ Have heatstroke and hypothermia at the same time
	public static Achievement winterIsComing; 	 //@ Survive 7 days in the snow without getting Hypothermia
	public static Achievement iNeededThat; 		 //@ Fumble a tool under Frostbite I
	public static Achievement hardBoiled; 		 //@ Survive Heat Stroke III
	public static Achievement mindOverMatter; 	 //@ Kill 5 mobs with Insanity III without getting hit
	
	// Crafting related
	public static Achievement safetyFirst; 		 //@ Craft a hardhat
	public static Achievement breatheEasy; 		 //@ Craft a respirator
	public static Achievement keepYourCool;		 //@ Craft a camel pack
	
	// Misc
	public static Achievement tenSecondRule; 	 //@ Eat rotten food
	public static Achievement ohGodWhy; 		 //@ Play disk 11
	public static Achievement ironArmy; 		 //@ Name an iron golem Siyliss
	
	//public static Achievement medicalMarvels; 	// Cure any infection/disease
	//public static Achievement suckItUpPrincess; // Attack & kill any hostile mob with one or more broken limbs
	
	public static void InitAchievements()
	{
		// Achievement Field name			Lang title?					   Land description?              1,0 is centered		Icon			Parent
		
		// Crafting
		safetyFirst =        new Achievement("enviromine.SafetyFirst",       "enviromine.SafetyFirst",       -2,-2, ObjectHandler.hardHat,    AchievementList.buildWorkBench).registerStat();
		breatheEasy = 	     new Achievement("enviromine.BreatheEasy",       "enviromine.BreatheEasy",       -1,-2, ObjectHandler.gasMask,    AchievementList.buildWorkBench).registerStat();
		keepYourCool = 	     new Achievement("enviromine.KeepYourCool",      "enviromine.KeepYourCool",       0,-2, ObjectHandler.camelPack,  AchievementList.buildWorkBench).registerStat();
		// Status effects
		tradingFavours =     new Achievement("enviromine.TradingFavours",    "enviromine.TradingFavours",    -2,-1, Items.emerald,            (Achievement)null).registerStat();
		thermalShock_dummy = new Achievement("enviromine.ThermalShockDummy", "enviromine.ThermalShockDummy", -1,-1, Blocks.flowing_lava,      (Achievement)null).registerStat();
		thermalShock =		 new Achievement("enviromine.ThermalShock",      "enviromine.ThermalShock",	     -1,-1, Items.snowball,           (Achievement)null).registerStat();
		winterIsComing =     new Achievement("enviromine.WinterIsComing",    "enviromine.WinterIsComing",     0,-1, Blocks.snow,              (Achievement)null).registerStat();
		iNeededThat =        new Achievement("enviromine.INeededThat",       "enviromine.INeededThat",        1,-1, Items.shears,             (Achievement)null).registerStat();
		hardBoiled =         new Achievement("enviromine.HardBoiled",        "enviromine.HardBoiled",         2,-1, Items.egg,                (Achievement)null).registerStat();
		mindOverMatter =     new Achievement("enviromine.MindOverMatter",    "enviromine.MindOverMatter",     3,-1, Items.ender_eye,          AchievementList.buildSword).registerStat();
		// Mining/Caving
		funwaysFault =       new Achievement("enviromine.FunwaysFault",      "enviromine.FunwaysFault",      -2, 0, Blocks.cobblestone,       AchievementList.buildPickaxe).registerStat();
		thatJustHappened =   new Achievement("enviromine.ThatJustHappened",  "enviromine.ThatJustHappened",  -1, 0, Blocks.fire,              AchievementList.buildPickaxe).registerStat();
		proMiner =           new Achievement("enviromine.ProMiner",          "enviromine.ProMiner",           0, 0, Items.diamond_pickaxe,    AchievementList.buildPickaxe).registerStat();
		boreToTheCore =      new Achievement("enviromine.BoreToTheCore",     "enviromine.BoreToTheCore",      1, 0, ObjectHandler.elevator,   AchievementList.diamonds).registerStat();
		intoTheDarkness =    new Achievement("enviromine.IntoTheDarkness",   "enviromine.IntoTheDarkness",    2, 0, Blocks.torch,             boreToTheCore).registerStat();
		itsPitchBlack =      new Achievement("enviromine.ItsPitchBlack",     "enviromine.ItsPitchBlack",      3, 0, Items.skull,              boreToTheCore).registerStat();
		// Miscellaneous
		tenSecondRule =      new Achievement("enviromine.TenSecondRule",     "enviromine.TenSecondRule",     -2, 1, ObjectHandler.rottenFood, (Achievement)null).registerStat();
		ohGodWhy =           new Achievement("enviromine.OhGodWhy",          "enviromine.OhGodWhy",          -1, 1, Items.record_11,          AchievementList.diamonds).registerStat();
		ironArmy =           new Achievement("enviromine.IronArmy",          "enviromine.IronArmy",           0, 1, Items.iron_ingot,         AchievementList.acquireIron).registerStat();
		
		// Unimplemented
		//medicalMarvels =   new Achievement("enviromine.MedicalMarvels",   "enviromine.MedicalMarvels",   0, 3, Items.potionitem,                              null).registerStat();
		//suckItUpPrincess = new Achievement("enviromine.SuckItUpPrincess", "enviromine.SuckItUpPrincess", 1, 3, Items.bone,               AchievementList.buildSword).registerStat();
		
		page = new AchievementPage(EM_Settings.MOD_NAME,
				funwaysFault,   mindOverMatter,  proMiner,         hardBoiled,    ironArmy,
				tradingFavours, iNeededThat,     winterIsComing,   ohGodWhy,      safetyFirst,
				boreToTheCore,  intoTheDarkness, thatJustHappened, itsPitchBlack, tenSecondRule,
				breatheEasy,    keepYourCool,    thermalShock_dummy,thermalShock//, keepYourCool, medicalMarvels, suckItUpPrincess
				);
		AchievementPage.registerAchievementPage(page);
	}
}
