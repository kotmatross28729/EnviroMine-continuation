# 1.3.144
-  **[NTM Space compat] More severe temperature conditions on planets.**
   + "Cyclic temperature rate":
      + The same as “n temperature decrease”, but for temperature rate, that is, separate configs for Dawn, Day, Dusk, Night.
   + Hard temperatures
      + Temperatures that can penetrate weak suits without fire resistance (HEV/MITTY)
   + Highly optimized armor checks for temperature, ~7.246 times faster

-  **[NTM compat] Machines affect body temperature | Support for hbm's ntm blocks/items by default**
    + Some machines will now increase the ambient temperature.
      + Pro-tip: don't lean against a working hbm's ntm furnace, the ambient temperature can reach ~X degrees
    + Now items with certain hazards will affect you
      + "Pyrophoric / Hot" items increase ambient temperature
      + "Coal dust" / "Asbestos" items decrease air quality
      + "Digamma Radiation" items decrease sanity

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
