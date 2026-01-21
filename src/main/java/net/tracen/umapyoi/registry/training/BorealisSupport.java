package net.tracen.umapyoi.registry.training;

import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.umadata.UmaDataBasicStatus;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils;

public class BorealisSupport extends TrainingSupport {

    public BorealisSupport() {
        super();
    }

    @Override
    public Component getDescription(SupportStack stack) {
        return this.getDescription();
    }

    @Override
    public boolean applySupport(ItemStack soul, RandomSource rand, SupportStack stack) {
        var chance = rand.nextFloat();
        if(chance < Umapyoi.CONFIG.ACUPUNCTUIST_SUPPORT_CHANCE())
            this.applySuccessEvent(soul, rand);
        else
            UmaStatusUtils.downMotivation(soul);

        return true;
    }

    public void applySuccessEvent(ItemStack soul, RandomSource rand) {
        switch (AcupuncturistEventTypes.getRandomType(rand)) {
            case STATUS ->{
                for(int i = 0; i < 5;i++) {
                    var maxPropertyArray = UmaSoulUtils.getMaxProperty(soul).array();
                    var propertyArray = UmaSoulUtils.getProperty(soul).array();
                    maxPropertyArray[i] = Math.min(39, maxPropertyArray[i] + 1);
                    soul.set(DataComponentsTypeRegistry.UMADATA_MAX_BASIC_STATUS.get(), UmaDataBasicStatus.init(maxPropertyArray));
                    if (maxPropertyArray[i] > propertyArray[i]) {
                        propertyArray[i] = Math.min(maxPropertyArray[i], propertyArray[i] + 1);
                        soul.set(DataComponentsTypeRegistry.UMADATA_BASIC_STATUS.get(), UmaDataBasicStatus.init(propertyArray));
                    }
                }
            }

            case PHYSIQUE ->{
                int phy = Math.min(UmaSoulUtils.getPhysique(soul) + 1, 5);
                UmaSoulUtils.setPhysique(soul, phy);
            }

            case MOTIVATION ->{
                UmaStatusUtils.addMotivation(soul);
            }

            default ->
                    throw new IllegalArgumentException("Unexpected value: " + AcupuncturistEventTypes.getRandomType(rand));
        }
    }

    public static enum AcupuncturistEventTypes {
        STATUS, PHYSIQUE, MOTIVATION;

        public static AcupuncturistEventTypes getRandomType(RandomSource rand) {
            return AcupuncturistEventTypes.values()[rand.nextInt(AcupuncturistEventTypes.values().length)];
        }
    }
}
