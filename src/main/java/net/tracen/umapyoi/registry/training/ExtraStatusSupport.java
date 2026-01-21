package net.tracen.umapyoi.registry.training;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.utils.UmaSoulUtils;

public class ExtraStatusSupport extends TrainingSupport {
    private final int statusType;

    public ExtraStatusSupport(int status) {
        super();
        this.statusType = status;
    }

    @Override
    public boolean applySupport(ItemStack soul, RandomSource rand, SupportStack stack) {
        // 0:physique, 1:talent, 2:memory, 3:ExtraAP
        int level = stack.getLevel();

        if (level != 0) {
            switch (this.statusType) {
                case 0 ->
                        UmaSoulUtils.setPhysique(soul, Math.min(5, UmaSoulUtils.getPhysique(soul) + level));
                case 1 -> UmaSoulUtils.setLearningTimes(soul, UmaSoulUtils.getLearningTimes(soul) + level);
                case 2 -> UmaSoulUtils.setSkillSlots(soul, UmaSoulUtils.getSkillSlots(soul)+ level);
                case 3 -> UmaSoulUtils.setExtraActionPoint(soul, UmaSoulUtils.getExtraActionPoint(soul) + level * 100);
                default ->
                        throw new IllegalArgumentException("Unexpected value: " + this.statusType);
            }
        }

        return true;
    }

}
