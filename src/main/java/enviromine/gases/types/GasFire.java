package enviromine.gases.types;

import java.awt.Color;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import enviromine.gases.EnviroGas;
import enviromine.gases.EnviroGasDictionary;
import enviromine.utils.ArmorTempUtils;

public class GasFire extends EnviroGas {
    //TODO: BURN ALL THIS
    public GasFire(String name, int ID) {
        super(name, ID);
        this.setColor(new Color(255, 128, 0, 64));
        this.setDensity(-10F);
        this.setDecayRates(1, 1, 1, 100, 100, 100);
    }

    @Override
    public void applyEffects(EntityLivingBase entityLiving, int amplifier) {
        super.applyEffects(entityLiving, amplifier);
        boolean ImmunityBurning = ArmorTempUtils.getTempResistance(entityLiving);
        if (!ImmunityBurning) {
            entityLiving.attackEntityFrom(DamageSource.onFire, 3F);
            entityLiving.setFire(10);
        }
    }

    @Override
    public int getGasOnDeath(World world, int i, int j, int k) {
        return EnviroGasDictionary.carbonMonoxide.gasID;
    }
}
