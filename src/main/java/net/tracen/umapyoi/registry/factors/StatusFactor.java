package net.tracen.umapyoi.registry.factors;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils.StatusType;

public class StatusFactor extends UmaFactor {

    private final StatusType statusType;

    public StatusFactor(StatusType status) {
        super(FactorType.STATUS);
        this.statusType = status;
    }

    public StatusType getStatusType() {
        return statusType;
    }
    
    @Override
    public void applyFactor(ItemStack soul, UmaFactorStack stack) {
        var maxProp = UmaSoulUtils.updateMaxPropertyAsArray(soul, prop ->
                prop[statusType.getId()] = Math.min(39, prop[statusType.getId()] + 1)).array();
        int maxStatusLevel = maxProp[statusType.getId()];
        UmaSoulUtils.updatePropertyAsArray(soul, prop ->
                prop[statusType.getId()] = Math.min(maxStatusLevel, prop[statusType.getId()] + stack.getLevel()));
    }

    @Override
    public Component getDescription(UmaFactorStack stack) {
        return this.getFullDescription(stack.getLevel());
    }

}
