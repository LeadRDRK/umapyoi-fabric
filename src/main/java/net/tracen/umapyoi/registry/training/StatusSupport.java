package net.tracen.umapyoi.registry.training;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.umadata.UmaDataBasicStatus;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils.StatusType;

public class StatusSupport extends TrainingSupport {
    private final StatusType statusType;

    public StatusSupport(StatusType status) {
        super();
        this.statusType = status;
    }

    @Override
    public boolean applySupport(ItemStack soul, RandomSource rand, SupportStack stack) {
        var maxPropertyArray = UmaSoulUtils.getMaxProperty(soul).array();
        var propertyArray = UmaSoulUtils.getProperty(soul).array();
        int id = statusType.getId();
        if (maxPropertyArray[id] > propertyArray[id]) {
            propertyArray[id] = Math.min(maxPropertyArray[id], propertyArray[id] + stack.getLevel());
            soul.set(DataComponentsTypeRegistry.UMADATA_BASIC_STATUS.get(), UmaDataBasicStatus.init(propertyArray));
            return true;
        }
        return false;
    }

}
