package enviromine.handlers.compat;

import com.hbm.tileentity.machine.TileEntityCrucible;
import com.hbm.tileentity.machine.TileEntityDiFurnace;
import com.hbm.tileentity.machine.TileEntityDiFurnaceRTG;
import com.hbm.tileentity.machine.TileEntityFurnaceBrick;
import com.hbm.tileentity.machine.TileEntityFurnaceCombination;
import com.hbm.tileentity.machine.TileEntityFurnaceIron;
import com.hbm.tileentity.machine.TileEntityFurnaceSteel;
import com.hbm.tileentity.machine.TileEntityHeatBoiler;
import com.hbm.tileentity.machine.TileEntityHeatBoilerIndustrial;
import com.hbm.tileentity.machine.TileEntityHeaterElectric;
import com.hbm.tileentity.machine.TileEntityHeaterFirebox;
import com.hbm.tileentity.machine.TileEntityHeaterOilburner;
import com.hbm.tileentity.machine.TileEntityHeaterOven;
import com.hbm.tileentity.machine.TileEntityMachineArcFurnaceLarge;
import com.hbm.tileentity.machine.TileEntityMachineCombustionEngine;
import com.hbm.tileentity.machine.TileEntityMachineCyclotron;
import com.hbm.tileentity.machine.TileEntityMachineDiesel;
import com.hbm.tileentity.machine.TileEntityMachineHephaestus;
import com.hbm.tileentity.machine.TileEntityMachinePress;
import com.hbm.tileentity.machine.TileEntityMachineTurbineGas;
import com.hbm.tileentity.machine.TileEntityMachineTurbofan;
import com.hbm.tileentity.machine.TileEntityMachineWoodBurner;
import com.hbm.tileentity.machine.oil.TileEntityMachineCoker;
import com.hbm.tileentity.machine.oil.TileEntityMachineGasFlare;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKBase;
import enviromine.core.EM_Settings;
import net.minecraft.tileentity.TileEntity;

import java.lang.reflect.Field;

import static enviromine.handlers.EM_StatusManager.getTempFalloff;

public class EM_StatusManager_NTM {
	
	// All compatibility with NTM from the EM_StatusManager class goes here
	
	public static float machinesTempInfluence(TileEntity tileentity, float dist, int cubeRadius){
		float blockAndItemTempInfluence = 0;
		
		float FireboxMax = TileEntityHeaterFirebox.maxHeatEnergy;
		float HeaterOvenMax = TileEntityHeaterOven.maxHeatEnergy;
		
		//Calculation accuracy:
		//✅ - close to the expected value
		//🟧 - different from the expected value, but not much
		//❌ - very different from the expected value
		//Expected - ((value/DIV) / 2) ≈ temp in deg
		if (tileentity instanceof TileEntityMachinePress press) {
			if (press.burnTime > 0 && press.speed > 0) {
				//Coal - 1600/16 = 64℃ (expected 50) ✅
				//Bale - 32000/16 = 514℃ (expected 1000) - 500℃ hard-cap ✅
				//Works in space - ✅
				blockAndItemTempInfluence += getTempFalloff(Math.min((press.burnTime / EM_Settings.BurnerPressHeatDivisor), EM_Settings.BurnerPressHeatHardCap*EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if (tileentity instanceof TileEntityHeaterFirebox firebox) {
			if (firebox.burnTime > 0 && firebox.heatEnergy > 0) {
				//Coal - 200/2 = 52(60)℃ (expected 50) ✅
				//Bale - 1500/2 = 350(309)℃ (expected 375) ✅
				//Works in space - ❌
				blockAndItemTempInfluence += getTempFalloff(firebox.burnHeat / EM_Settings.FireboxHeatDivisor, dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if (tileentity instanceof TileEntityHeaterOven heaterOven) {
			if (heaterOven.burnTime > 0 && heaterOven.heatEnergy > 0) {
				//Coal - 1000/4 = 129(112)℃  (expected 125) ✅
				//Bale - 7500/4 = 869(877)℃ (expected 937,5) 🟧
				//Works in space - ❌
				blockAndItemTempInfluence += getTempFalloff(heaterOven.burnHeat / EM_Settings.HeaterOvenHeatDivisor, dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if (tileentity instanceof TileEntityHeaterOilburner oilburner) {
			if (oilburner.isOn && oilburner.heatEnergy > 0) {
				//Max - 100_000/200 = 245℃ (expected 250) ✅
				//Works in space - ❌
				blockAndItemTempInfluence += getTempFalloff(oilburner.heatEnergy / EM_Settings.FluidBurnerHeatDivisor, dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if (tileentity instanceof TileEntityHeaterElectric heaterElectric) {
			if (heaterElectric.isOn && heaterElectric.heatEnergy > 0) {
				//Max (no) - 10_000/20 = 244(249)℃ (expected 250) - 250℃ hard-cap ✅
				//Works in space - ✅
				blockAndItemTempInfluence += getTempFalloff(Math.min(heaterElectric.heatEnergy / EM_Settings.HeaterElectricHeatDivisor, (EM_Settings.HeaterElectricHeatHardCap*EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider)) , dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if (tileentity instanceof TileEntityFurnaceIron furnaceIron) {
			if (furnaceIron.wasOn) {
				//Coal - 2000/2 = 458℃ (expected 500) ✅
				//Bale - 64000/2 = 15343℃ (expected 16000) - 1000℃ hard-cap ✅
				//Works in space - ❌
				blockAndItemTempInfluence += getTempFalloff(Math.min(furnaceIron.burnTime / EM_Settings.IronFurnaceHeatDivisor, (EM_Settings.IronFurnaceHeatHardCap*EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider)), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if (tileentity instanceof TileEntityFurnaceSteel furnaceSteel) {
			if (furnaceSteel.wasOn) {
				//Max - 100_000 (35000)/200 = 85℃ (expected 87,5) ✅
				//Works in space - ❌
				blockAndItemTempInfluence += getTempFalloff(furnaceSteel.heat / EM_Settings.SteelFurnaceHeatDivisor, dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if (tileentity instanceof TileEntityFurnaceCombination furnaceCombination) {
			if (furnaceCombination.wasOn) {
				//Max - 100_000/200 = 228℃ (expected 250) ✅
				//Works in space - ❌
				blockAndItemTempInfluence += getTempFalloff(furnaceCombination.heat / EM_Settings.CombinationOvenHeatDivisor, dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if (tileentity instanceof TileEntityCrucible crucible) {
			if(crucible.heat > 0) {
				//Max - 100_000/1000 = 101℃ (expected 100) ✅
				//Works in space - ✅
				blockAndItemTempInfluence += getTempFalloff(crucible.heat / EM_Settings.CrucibleHeatDivisor, dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if (tileentity instanceof TileEntityHeatBoiler boiler) {
			float heat = boiler.heat;
			
			if (heat <= FireboxMax) {
				heat = boiler.heat / EM_Settings.BoilerHeatDivisor;
			} else if (heat <= HeaterOvenMax) {
				heat = Math.max(boiler.heat / (EM_Settings.BoilerHeatDivisor * EM_Settings.BoilerHeaterOvenDivisorConstant), ((FireboxMax / EM_Settings.BoilerHeatDivisor)));
			} else if (heat <= TileEntityHeatBoiler.maxHeat) {
				heat = Math.max(boiler.heat / (EM_Settings.BoilerHeatDivisor * EM_Settings.BoilerMAXDivisorConstant), ((HeaterOvenMax / (EM_Settings.BoilerHeatDivisor * EM_Settings.BoilerHeaterOvenDivisorConstant))));
			}
			//Max (real) - 3_200_000/(200*10) - actually 999_001/(200*10) = 510℃
			//Max (HO)     - 500_000/(200*2)  = 592℃ (expected 625) ✅
			//Max (FB)     - 100_000/200      = 245℃ (expected 250) ✅
			//Works in space - ✅
			blockAndItemTempInfluence += getTempFalloff(heat, dist, cubeRadius, EM_Settings.blockTempDropoffPower);
		}
		else if (tileentity instanceof TileEntityHeatBoilerIndustrial boilerIndustrial) {
			float heat = boilerIndustrial.heat;
			
			if (heat <= FireboxMax) {
				heat = boilerIndustrial.heat / EM_Settings.BoilerIndustrialHeatDivisor;
			} else if (heat <= HeaterOvenMax) {
				heat = Math.max(boilerIndustrial.heat / (EM_Settings.BoilerIndustrialHeatDivisor * EM_Settings.BoilerIndustrialHeaterOvenDivisorConstant), ((FireboxMax / EM_Settings.BoilerIndustrialHeatDivisor)));
			} else if (heat <= TileEntityHeatBoilerIndustrial.maxHeat) {
				heat = Math.max(boilerIndustrial.heat / (EM_Settings.BoilerIndustrialHeatDivisor * EM_Settings.BoilerIndustrialMAXDivisorConstant), ((HeaterOvenMax / (EM_Settings.BoilerIndustrialHeatDivisor * EM_Settings.BoilerIndustrialHeaterOvenDivisorConstant))));
			}
			
			//Max (real)   - 12_800_000/(200*10) - actually 999_001/(200*10) = 506℃
			//Max (HO)        - 500_000/(200*2)  = 510℃ (expected 625) 🟧
			//Max (FB)        - 100_000/200      = 210℃ (expected 250) ✅
			//Works in space - ✅
			blockAndItemTempInfluence += getTempFalloff(heat, dist, cubeRadius, EM_Settings.blockTempDropoffPower);
		}
		else if(tileentity instanceof TileEntityFurnaceBrick furnaceBrick) {
			if (furnaceBrick.burnTime > 0) {
				//Coal - 1600/16  = 58℃ (expected 50) ✅
				//Bale - 32000/16 = 475℃ (expected 1000) - 500℃ hard-cap ✅
				//Works in space - ❌
				blockAndItemTempInfluence += getTempFalloff(Math.min((furnaceBrick.burnTime / EM_Settings.FurnaceBrickHeatDivisor), EM_Settings.FurnaceBrickHeatHardCap*EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if(tileentity instanceof TileEntityDiFurnace diFurnace) {
			if(diFurnace.progress > 0) {
				//FUEL - 12800/64 = 105℃ (expected 100) ✅
				//Works in space - ❌
				blockAndItemTempInfluence += getTempFalloff((diFurnace.fuel / EM_Settings.DiFurnaceHeatDivisor), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if(tileentity instanceof TileEntityDiFurnaceRTG diFurnaceRTG) {
			if(diFurnaceRTG.progress > 0) {
				//Power level (max) = 600 X6 = 3600/2 = who knows, TO FUCKING FAST℃ (expected 900) ❔
				//Power level (min) = 3 X6 =     18/2 = too small amount, not even shown℃ (expected 4.5) ❔
				//Works in space - ✅
				blockAndItemTempInfluence += getTempFalloff((diFurnaceRTG.getPower() / EM_Settings.DiFurnaceRTGHeatDivisor), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		//!"The nuclear and RTG furnaces will be retired, their recipes have been removed but they remain operational for now"
//                            else if(tileentity instanceof TileEntityNukeFurnace nukeFurnace) {
//                                if(nukeFurnace.isProcessing()) {
//                                    //Operations (max) = 200/0.2 = 114℃ (expected 500) ❌
//                                    //Operations (min) =   5/0.2 = not enough to see℃ (expected 12,5) ❔
//                                    //Works in space - ✅
//                                    blockAndItemTempInfluence += getTempFalloff((nukeFurnace.dualPower / EM_Settings.NukeFurnaceHeatDivisor), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
//                                }
//                            }
//                            else if(tileentity instanceof TileEntityRtgFurnace rtgFurnace) {
//                                //this shouldn't really be a constant, but I don't give a fuck
//                                if(rtgFurnace.isProcessing()){
//                                    //Works in space - ✅
//                                    blockAndItemTempInfluence += getTempFalloff((EM_Settings.RTGFurnaceHeatConstant*EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
//                                }
//                            }
		else if(tileentity instanceof TileEntityMachineWoodBurner woodBurner) {
			int powerGen = 0;
			try {
				Field powerGenz = TileEntityMachineWoodBurner.class.getDeclaredField("powerGen"); //куадусешщт
				powerGenz.setAccessible(true);
				powerGen = (int) powerGenz.get(woodBurner);
			} catch (NoSuchFieldException | IllegalAccessException ignored) {}
			
			if(woodBurner.isOn && powerGen > 0 ) {
				//Coal - 1600/16 = 59℃ (expected 50) ✅
				//Works in space - ❌
				blockAndItemTempInfluence += getTempFalloff((woodBurner.burnTime / EM_Settings.WoodBurningGenHeatDivisor), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if(tileentity instanceof TileEntityMachineDiesel diesel) {
			if(diesel.tank.getFill() > 0 && TileEntityMachineDiesel.getHEFromFuel(diesel.tank.getTankType()) > 0L) {
				//Works in space - ❌
				blockAndItemTempInfluence += getTempFalloff((EM_Settings.DieselGenHeatConstant*EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if(tileentity instanceof TileEntityMachineCombustionEngine combustionEngine) {
			if(combustionEngine.wasOn) {
				//Works in space - ❌
				blockAndItemTempInfluence += getTempFalloff((EM_Settings.ICEHeatConstant*EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if(tileentity instanceof TileEntityMachineCyclotron cyclotron) {
			if(cyclotron.progress > 0) {
				//Works in space - ✅
				blockAndItemTempInfluence += getTempFalloff((EM_Settings.CyclotronHeatConstant*EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if(tileentity instanceof TileEntityMachineHephaestus hephaestus) { //GeoThermal
			if(hephaestus.getTotalHeat() > 0) {
				//Max - 10_000/10 = 493℃ (expected 500) ✅
				//Works in space - ✅
				blockAndItemTempInfluence += getTempFalloff((hephaestus.getTotalHeat() / EM_Settings.GeothermalGenHeatDivisor), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if(tileentity instanceof TileEntityRBMKBase rbmkBase) {
			if(rbmkBase.heat > 0) {
				//Max - 1500/5 = 293℃ (expected 300) ✅
				//Works in space - ✅
				blockAndItemTempInfluence += getTempFalloff(Math.min(((float)rbmkBase.heat / EM_Settings.RBMKRodHeatDivisor), EM_Settings.RBMKRodHeatHardCap*2), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if(tileentity instanceof TileEntityMachineArcFurnaceLarge arcFurnaceLarge) {
			if(arcFurnaceLarge.isProgressing) {
				//Works in space - ✅
				blockAndItemTempInfluence += getTempFalloff((EM_Settings.ArcFurnaceHeatConstant*EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if(tileentity instanceof TileEntityMachineGasFlare gasFlare) {
			int powerGen = 0;
			try {
				Field output = TileEntityMachineGasFlare.class.getDeclaredField("output");
				output.setAccessible(true);
				powerGen = (int) output.get(gasFlare);
			} catch (NoSuchFieldException | IllegalAccessException ignored) {}
			if(gasFlare.doesBurn && powerGen > 0) {
				//Works in space - ❌
				blockAndItemTempInfluence += getTempFalloff((EM_Settings.FlareStackHeatConstant*EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if(tileentity instanceof TileEntityMachineCoker coker) {
			if(coker.wasOn && coker.heat > 0) {
				//Max - 100_000/1000 = 58℃ (expected 50)✅
				//Works in space - ✅
				blockAndItemTempInfluence += getTempFalloff((coker.heat / EM_Settings.CokerHeatDivisor), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if(tileentity instanceof TileEntityMachineTurbofan turbofan) {
			if(turbofan.wasOn) {
				//Works in space - ❌
				blockAndItemTempInfluence += getTempFalloff((turbofan.afterburner > 0 ? EM_Settings.TurbofanAfterburnerHeatConstant*EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider : EM_Settings.TurbofanHeatConstant*EM_Settings.AmbientTemperatureblockAndItemTempInfluenceDivider), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		else if(tileentity instanceof TileEntityMachineTurbineGas turbineGas) {
			if(turbineGas.temp > 0) {
				//Works in space - ✅
				blockAndItemTempInfluence += getTempFalloff((turbineGas.temp / EM_Settings.CCGasTurbineHeatDivisor), dist, cubeRadius, EM_Settings.blockTempDropoffPower);
			}
		}
		
		return blockAndItemTempInfluence;
	}
	
	
	
}

