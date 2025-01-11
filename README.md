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

- **6)** Added compatibility with thaumcraft leaves

- **7)** Fixed a bug that caused `/envirostat set air` didn't work

- **8)** **A lot of things related to armor from Hbm's Nuclear Tech Mod and enviromine temperature**

- **9)** **Fix a bunch of logic errors with gases, this includes bugs where poisoning was not applied to the player, even if he didn't have any protection (the mod considered that the player was in creative, even if he was in survival, or he considered that the player already has a mask, although there was none)**

- **10)** Added a bunch of configuration options related to the characteristics of poisoning (how long the effect is applied, what level the effect is, how dense the gas should be for poisoning, etc.)

- **11)** **Biome temperature system has been completely rewritten depending on the weather | day/night cycle ([github, changelog 1.3.139](https://github.com/kotmatross28729/EnviroMine-continuation/releases/tag/1.3.139))**

- **12)** **Added compat with Hbm's NTM Space (fork)**

- **13)** **Added compat with Serene Seasons**

- **14)** **Added the ability to change a huge number of hardcoded values**

- **15)** **Compatibility with Mekanism's gas mask**

## Current TODO:
- **1)** **Ability to change parameters for mobs from other mods.**
   + Mobs that aren't registered in the vanilla tab aren't displayed in the enviromine configuration. I plan to fix this (if I can)

- **2)** **Сode refactoring.**
   + The name speaks for itself

# Credits:

[Funwayguy,](https://github.com/Funwayguy)
[GenDeathrow,](https://github.com/GenDeathrow)
[thislooksfun](https://github.com/thislooksfun) - [original mod](https://github.com/EnviroMine/EnviroMine-1.7)


[AstroTibs](https://gitgud.io/AstroTibs) - [fork of the original mod, this one is based on](https://gitgud.io/AstroTibs/enviromine-for-galaxy-odyssey)

[GTNH](https://github.com/orgs/GTNewHorizons/repositories) - [ExampleMod1.7.10](https://github.com/GTNewHorizons/ExampleMod1.7.10)
