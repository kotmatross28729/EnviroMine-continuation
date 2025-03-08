package enviromine.mixins.early;

import com.hbm.util.ContaminationUtil;
import enviromine.utils.WaterUtils;
import enviromine.utils.misc.mixins.ICauldronAccessor;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.block.BlockCauldron.func_150027_b;

@Mixin(value = BlockCauldron.class, priority = 1003)
public class MixinBlockCauldron implements ICauldronAccessor {
	//TODO
	@Unique
	WaterUtils.WATER_TYPES enviromine$waterType;
	
	@Override
	public WaterUtils.WATER_TYPES enviromine$getWaterType() {
		return enviromine$waterType;
	}
	
	@Override
	public void enviromine$setWaterType(WaterUtils.WATER_TYPES waterType) {
		this.enviromine$waterType = waterType;
	}
	
//	@Inject(method = "onBlockActivated", at = @At(value = "HEAD"))
//	public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ, CallbackInfo ci) {
//		///TODO set type
//	}
	
	
	@Inject(method = "onEntityCollidedWithBlock", at = @At(value = "HEAD"))
	public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn, CallbackInfo ci) {
		int l = func_150027_b(worldIn.getBlockMetadata(x, y, z));
		float f = (float) y + (6.0F + (float) (3 * l)) / 16.0F;
		if (!worldIn.isRemote && l > 0 && entityIn.boundingBox.minY <= (double) f) {
			if (enviromine$waterType.isRadioactive) {
				if (entityIn instanceof EntityLivingBase entityLivingBase) {
					//TODO
					entityLivingBase.addPotionEffect(new PotionEffect(Potion.poison.id, 100));
					//ContaminationUtil.contaminate(entityLivingBase, ContaminationUtil.HazardType.RADIATION, ContaminationUtil.ContaminationType.CREATIVE, 5F);
				}
			}
		}
	}
	
	
	
}
