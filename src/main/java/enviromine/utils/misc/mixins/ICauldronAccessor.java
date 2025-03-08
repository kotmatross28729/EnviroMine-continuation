package enviromine.utils.misc.mixins;

import enviromine.utils.WaterUtils;

public interface ICauldronAccessor {
	WaterUtils.WATER_TYPES enviromine$getWaterType();
	void enviromine$setWaterType(WaterUtils.WATER_TYPES waterType);
}
