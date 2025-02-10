# 1.3.148

## NOW REQUIRES [UNIMIXINS](https://github.com/LegacyModdingMC/UniMixins/releases)

### DON'T FORGET TO RESET THE CONFIG BEFORE INSTALLING, IF YOU HAVE CHANGED IT, YOU NEED TO TRANSFER THE CHANGES TO THE NEW CONFIG!

-  **[NTM Space compat]**
   * Rebalanced temperature values for planets to be more in line with ~~reality~~ common sense
     * The terraformed values are made up of nothing, of course, changing the composition of the atmosphere is half of a work, there is also mass, radius, and a bunch of parameters related to the orbit, rotation period, solar day, etc. But let's not forget that this is just a game made of cubes, okay?
        * If NTM Space changes the way of terraforming, it will be a disaster for this compatibility
     * **You can see the values [here](https://github.com/kotmatross28729/EnviroMine-continuation/blob/main/DEBUG_SHEET/TEMPS.MD)**
   * Fixed "Hard temperatures" working even when the planet was terraformed

* All logic for the Enviromine torch/coal has been moved to mixins (instead of completely replacing them with their own variants)
  * [Torch] Added compatibility with netherlicious torches/lighters
  * [Coal] Fixed fixes VERY old (and rare) bugs, like `NullPointerException` when Enviromine tried to replace vanilla coal with its own
* Potentially fixed problems with `NoClassDefFoundError` crashing if mods for which there is compatibility are not installed
* Fixed configs not appearing for LOTR biomes
  * Also fixed LOTR biome identification
* Fixed Freezer Z-fighting on model
* Added compatibility for MrCrayfish's Furniture Mod / Cooking For Blockheads fridges
  * [Cooking For Blockheads] Requires [GTNH version](https://github.com/GTNewHorizons/CookingForBlockheads/releases)!
* Now cooling water uses snowball instead of ice (in favor of survival mode) [**Suggested by iamsoNewBee**]
* Golden apple/carrot no longer rot [**Suggested by iamsoNewBee**]
  * Minor food hydration balance changes [**Suggested by iamsoNewBee**]
* Hbm's NTM canned food no longer rot    [**Suggested by iamsoNewBee**]
* Hbm's NTM drinks now restore hydration (And some also reduce the temperature (except for vodka, it increases))
* Some HarvestCraft and JAFFA food (HarvestCraft addon) now restore hydration **[TODO]**

# 1.3.147
### Fixed
* Crash on a server with Hbm's NTM installed

### Added

* Compatibility between davy lamp / NTM gases
  * especially for NTM gases: the distance at which the davy lamp will react can be adjusted (`a-169_DavyLampGasDetectRange`)
* Compatibility with Mekanism's gas mask

# 1.3.146

### Fixed
* Crash with new version of Hbm's NTM
* Bug with playing gas mask breathing sound for some players (Requires Hbm's NTM loaded)
* Even more bugs with temperature-armor calculations

### Added
* When mining enviromine coal, Hbm's NTM coal dust will be released.
* More parameters related to temperature-armor calculations
* Added tooltips to armor according to its temperature protection
  * `Is Temperature Resistance` = `"This armor is resistant to average temperatures"` = protects against fire / normal NTM Space planets | doesn't protect against lava / extremely hot/cold NTM Space planets 
  * `Is Temperature Sealed` = `"This armor is resistant to extreme temperatures"` = protects against any temperatures



---
# 1.3.144
### DON'T FORGET TO RESET THE CONFIG BEFORE INSTALLING, IF YOU HAVE CHANGED IT, YOU NEED TO TRANSFER THE CHANGES TO THE NEW CONFIG! (concerns biome and general config)

-  **[NTM Space compat] More severe temperature conditions on planets.**
   + "Cyclic temperature rate":
      + The same as “n temperature decrease”, but for temperature rate, that is, separate configs for Dawn, Day, Dusk, Night.
   + Hard temperatures
      + Temperatures that can penetrate weak suits without fire resistance (HEV/MITTY)
   + Highly optimized armor checks for temperature, ~7.246 times faster
   + Air vents (finally) are working properly
   + Fixed a couple of errors in temperature calculations, depending on the position of the sun on the planets

-  **[NTM compat] Machines affect body temperature | Support for hbm's ntm blocks/items by default | Misc stuff**
    + Some machines will now increase the ambient temperature.
      + Сan be disabled in config
      + The divisors for the heat value can also be changed in config (ambient temperature of most machines is calculated from the heating of the machine, but we don't use "raw" values, since they can be around several thousand, so we use divisors so that the ambient temperature is in an acceptable range. Machines without heat (e.g. diesel generator) generate heat simply due to the fact that they are turned on, their heat values are constant, and can also be changed in the config)
    + Now items with certain hazards will affect player
      + "Pyrophoric / Hot" items increase ambient temperature
      + "Coal dust" / "Asbestos" items decrease air quality
      + "Digamma Radiation" items decrease sanity
    + Added plastic bottles, have 2 times more capacity than regular glass ones
      + bottles work with the hbm's liquid system, so you can draw it from the barrels

- Fixed several minor (and not so) bugs

+ Added more types of water, because I can
  + cold water can now be dirty
  + you can cool cold/cold dirty water with ice to frosty water
  + you can heat clean water to warm, and warm/warm dirty to hot
    + you can heat cold water to clean, and frosty to cold

+ Added texture for the extinguished torch
  + also fixed a bug due to which vanilla torches were consumed even in creative

+ Added the ability to change a huge number of hardcoded values
  + config now has 150+ options

---
# 1.3.143
* Added support for Serene Seasons
    * All relevant settings in the parameters of each biome
---


# 1.3.142

**Attention: for the correct operation of biome temperatures on planets, go to: `Enviromine menu - Custom Editor - Profile settings - default_settings.cfg - Config - Generate Defaults - true`**


* Added support for Hbm's NTM Space fork
  * Default temperature values on other planets
    * Temperature (depending on distance from the sun): closer than the Earth? very hot: further than the Earth? : very cold
  * Air vent allows you to breathe and maintains a comfortable temperature in the immediate area
  * Change in temperature values if the planet was terraformed (transition to more comfortable temperatures)
  * Improved compatibility for armor with temperatures (canSeal from space fork)
---

Excluded (broken for now)
+ If a player has a late stage of Mesothelioma/Black Lung, then his maximum airQuality = 50, 2 times less than normal (so he can spend less time in the cave without protection)
+ If the player has a high radiation dose, his temperature will rise (more radiation, higher the temperature, at very high doses player can even die from fever (and not from rad itself))

