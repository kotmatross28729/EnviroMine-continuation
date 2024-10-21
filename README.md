## EnviroMine continuation
#### based on [EnviroMine for Galaxy Odyssey](https://gitgud.io/AstroTibs/enviromine-for-galaxy-odyssey) by [AstroTibs](https://gitgud.io/AstroTibs)

## IMPORTANT:
**For most things in this mod to work correctly, do the following: `Enviromine menu - Custom Editor - Profile settings - default_settings.cfg - Config - Generate Defaults - true`**

**Please note, this is a very long process! may take up to 30 minutes of real time if you are using a large modpack**


## Physics for blocks is disabled by default!
**To use it, go to: `Enviromine menu -> Custom editor -> Profile settings -> default_settings.cfg -> general -> Enable Physics -> true`,**

## Differences from the Galaxy Odyssey version:



- **1)** more sanity debuffs (due to the fact that sanity in the original enviromine does almost nothing dangerous (+, from 1.3.134 version, if your sanity is less than or equal to 5 (but greater than 0) you will die from a heart attack after 1 minute, but if your sanity is 0, then you will die after 30 seconds, and, if more than 30 seconds have passed and your sanity has JUST dropped to 0, you will die instantly(configurable + you can disable death, but instead, some debuffs will become of a higher level))

- **2)** **Compatibility with Hbm's Nuclear Tech filters/masks/protective helmets**

- **3)** **Updated build script, thanks to GTNH!**

- **4)** Fixed a bug where the sound of the gas mask was played even when the game was paused

- **5)** Added the Hardcore gases option, with it, all gases will become invisible (disabled by default)

- **6)** Fixed a bug due to which, when specifying a change in the parameter of an item when using it, this parameter changed 2 times more than needed (config thing)

- **7)** Added compatibility with thaumcraft leaves

- **8)** Fixed a bug that caused `/envirostat set air` didn't work

- **9)** **A lot of things related to armor from Hbm's Nuclear Tech Mod and enviromine temperature**

- **10)** **Fix a bunch of logic errors with gases, this includes bugs where poisoning was not applied to the player, even if he didn't have any protection (the mod considered that the player was in creative, even if he was in survival, or he considered that the player already has a mask, although there was none)**

- **11)** Added a bunch of configuration options related to the characteristics of poisoning (how long the effect is applied, what level the effect is, how dense the gas should be for poisoning, etc.)

- **12)** **Biome temperature system has been completely rewritten depending on the weather | day/night cycle ([github, changelog 1.3.139](https://github.com/kotmatross28729/EnviroMine-continuation/releases/tag/1.3.139))**

- **13)** **Added compat with Hbm's NTM Space (fork)**

- **14)** **Added compat with Serene Seasons**


## Current TODO:
- **1)** **[NTM Space compat] More severe temperature conditions on planets.**
   + At the moment, low/high temperatures don't pose a particular threat, both because of the suit and because of the low “temperature rate” (even at -200/200, the body cools/heats for a very long time)
      + "Cyclic temperature rate":
         + The same as “n temperature decrease”, but for temperature rate, that is, separate configs for Dawn, Day, Dusk, Night. Thus, on the moon without a suit, during the day you will get fried very quickly, and at night you will become icy without even realizing it
         + Should probably be compensated by Heaters/coolers
      + Hard temperatures
         + Temperatures that can penetrate weak suits without fire resistance (HEV/MITTY)
         + It may not be necessary with "Cyclic temperature rate", but who knows

- **2)** **Ability to change parameters for mobs from other mods.**
   + Mobs that aren't registered in the vanilla tab aren't displayed in the enviromine configuration. I plan to fix this (if I can)

- **3)** **Сode refactoring.**
   + The name speaks for itself

- **4)** **UNHARDCODE ALL HARDCODED VALUES.**
   + There are a lot of values ​​in enviromine that could be changed in some situations. This is the last thing I plan to do



# Credits:

[Funwayguy,](https://github.com/Funwayguy)
[GenDeathrow,](https://github.com/GenDeathrow)
[thislooksfun](https://github.com/thislooksfun) - [original mod](https://github.com/EnviroMine/EnviroMine-1.7)


[AstroTibs](https://gitgud.io/AstroTibs) - [fork of the original mod, this one is based on](https://gitgud.io/AstroTibs/enviromine-for-galaxy-odyssey)

[GTNH](https://github.com/orgs/GTNewHorizons/repositories) - [ExampleMod1.7.10](https://github.com/GTNewHorizons/ExampleMod1.7.10)
