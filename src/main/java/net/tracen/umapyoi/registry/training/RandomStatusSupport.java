package net.tracen.umapyoi.registry.training;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.registry.umadata.UmaDataBasicStatus;
import net.tracen.umapyoi.utils.UmaSoulUtils;

public class RandomStatusSupport extends TrainingSupport {

    public RandomStatusSupport() {
        super();
    }

    @Override
    public boolean applySupport(ItemStack soul, RandomSource rand, SupportStack stack) {
        for(int i = 0; i < stack.getLevel(); i++) {
            int id = rand.nextInt(5);
            var maxPropertyArray = UmaSoulUtils.getMaxProperty(soul).array();
            var propertyArray = UmaSoulUtils.getProperty(soul).array();
            if (maxPropertyArray[id] > propertyArray[id]) {
                propertyArray[id] = Math.min(maxPropertyArray[id], propertyArray[id] + stack.getLevel());
                UmaSoulUtils.setProperty(soul, UmaDataBasicStatus.init(propertyArray));
                return true;
            }
        }
        return false;
    }

}
