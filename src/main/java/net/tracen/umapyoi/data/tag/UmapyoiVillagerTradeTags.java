package net.tracen.umapyoi.data.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.VillagerTrade;
import net.tracen.umapyoi.Umapyoi;

public class UmapyoiVillagerTradeTags {
    public static final TagKey<VillagerTrade> TRAINER_LEVEL_1 = create("trainer/level_1");
    public static final TagKey<VillagerTrade> TRAINER_LEVEL_2 = create("trainer/level_2");
    public static final TagKey<VillagerTrade> TRAINER_LEVEL_3 = create("trainer/level_3");
    public static final TagKey<VillagerTrade> TRAINER_LEVEL_4 = create("trainer/level_4");
    public static final TagKey<VillagerTrade> TRAINER_LEVEL_5 = create("trainer/level_5");

    private UmapyoiVillagerTradeTags() {
    }

    private static TagKey<VillagerTrade> create(final String name) {
        return TagKey.create(Registries.VILLAGER_TRADE, Umapyoi.id(name));
    }
}