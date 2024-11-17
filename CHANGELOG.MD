# 1.3.144
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
    + If a player has a late stage of Mesothelioma/Black Lung, then his maximum airQuality = 50, 2 times less than normal (so he can spend less time in the cave without protection)
    + If the player has a high radiation dose, his temperature will rise (more radiation, higher the temperature, at very high doses player can even die from fever (and not from rad itself))

- Fixed several minor (and not so) bugs

# 1.3.143
* Added support for Serene Seasons
    * All relevant settings in the parameters of each biome

# 1.3.142

**Attention: for the correct operation of biome temperatures on planets, go to: `Enviromine menu - Custom Editor - Profile settings - default_settings.cfg - Config - Generate Defaults - true`**


* Added support for Hbm's NTM Space fork
  * Default temperature values on other planets
    * Temperature (depending on distance from the sun): closer than the Earth? very hot: further than the Earth? : very cold
  * Air vent allows you to breathe and maintains a comfortable temperature in the immediate area
  * Change in temperature values if the planet was terraformed (transition to more comfortable temperatures)
  * Improved compatibility for armor with temperatures (canSeal from space fork)
