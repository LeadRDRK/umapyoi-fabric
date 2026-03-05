package net.tracen.umapyoi.registry.factors;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils.StatusType;

import java.util.Random;

public class WhiteStatusFactor extends UmaFactor {

    private final StatusType statusType;

    public WhiteStatusFactor(StatusType status) {
        super(FactorType.OTHER);
        this.statusType = status;
    }

    @Override
    public void applyFactor(ItemStack soul, UmaFactorStack stack) {
        int statusLevel = stack.getLevel();
        int maxStatusLevel = stack.getLevel();
        var chance = stack.getLevel() * 0.25;
        Random rand = new Random();

        for (int roll = 0; roll < stack.getLevel(); roll++) {
            if (rand.nextFloat() > chance)
                maxStatusLevel--;
        }
        int finalMaxStatusLevel = maxStatusLevel;
        UmaSoulUtils.updateMaxPropertyAsArray(soul, prop ->
                prop[statusType.getId()] += finalMaxStatusLevel);

        for (int roll = 0; roll < stack.getLevel(); roll++) {
            if (rand.nextFloat() > chance)
                statusLevel--;
        }
        int finalStatusLevel = statusLevel;
        UmaSoulUtils.updatePropertyAsArray(soul, prop ->
                prop[statusType.getId()] += Math.min(finalMaxStatusLevel, finalStatusLevel + stack.getLevel()));
    }

    @Override
    public Component getDescription(UmaFactorStack stack) {
        return this.getFullDescription(stack.getLevel());
    }

}
