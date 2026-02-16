package net.tracen.umapyoi.registry.training.card;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.item.data.GachaRankingData;
import net.tracen.umapyoi.registry.RegistryNameHolder;
import net.tracen.umapyoi.registry.TrainingSupportRegistry;
import net.tracen.umapyoi.registry.training.SupportStack;
import net.tracen.umapyoi.registry.training.SupportType;
import net.tracen.umapyoi.utils.GachaRanking;

import java.util.List;

public class SupportCard extends RegistryNameHolder {
    public static final Codec<SupportCard> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GachaRanking.CODEC.optionalFieldOf("ranking", GachaRanking.EASTER_EGG)
                    .forGetter(SupportCard::getGachaRanking),
            SupportType.CODEC.fieldOf("type").forGetter(SupportCard::getSupportType),
            SupportEntry.CODEC.listOf().fieldOf("supports").forGetter(SupportCard::getSupports), Identifier.CODEC
                    .listOf().optionalFieldOf("supporters", Lists.newArrayList()).forGetter(SupportCard::getSupporters),
            Codec.INT.fieldOf("max_damage").forGetter(SupportCard::getMaxDamage)        
            )
            .apply(instance, SupportCard::new));

    public static final ResourceKey<Registry<SupportCard>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(Identifier.fromNamespaceAndPath(Umapyoi.MODID, "support_card"));

    public static final Identifier EMPTY_ID = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "blank_card");
    public static final SupportCard EMPTY = SupportCard.Builder.create().ranking(GachaRanking.EASTER_EGG).supportType(SupportType.GROUP).build();

    private final GachaRanking ranking;
    private final SupportType type;
    private final List<SupportEntry> supports;
    private final List<Identifier> supporters;
    private final int maxDamage;

    private SupportCard(GachaRanking level, SupportType type, List<SupportEntry> supports,
            List<Identifier> supporters, int maxDamage) {
        this.ranking = level;
        this.type = type;
        this.supports = supports;
        this.supporters = supporters;
        this.maxDamage = maxDamage;
    }

    public static ItemStack init(Identifier name, SupportCard card) {
        ItemStack result = new ItemStack(ItemRegistry.SUPPORT_CARD);
        result.set(DataComponents.MAX_DAMAGE, card.getMaxDamage());
        result.set(DataComponents.DAMAGE, 0);
        result.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), name);
        GachaRanking ranking = card.getGachaRanking();
        result.set(DataComponentsTypeRegistry.GACHA_RANKING.get(), new GachaRankingData(ranking));
        result.set(DataComponents.RARITY,
                ranking == GachaRanking.SSR ? Rarity.EPIC : ranking == GachaRanking.SR ? Rarity.UNCOMMON : Rarity.COMMON
        );
        return result;
    }

    public GachaRanking getGachaRanking() {
        return ranking;
    }

    public List<SupportEntry> getSupports() {
        return supports;
    }
    
    public int getMaxDamage() {
        return maxDamage;
    }

    public List<SupportStack> getSupportStacks() {
        List<SupportStack> result = Lists.newArrayList();
        this.getSupports().forEach(
                sp -> result.add(new SupportStack(TrainingSupportRegistry.REGISTRY.get()
                        .get(sp.getFactor()).orElseThrow().value(), sp.getLevel(), sp.getTag())));
        return result;
    }

    public List<Identifier> getSupporters() {
        return supporters;
    }

    public SupportType getSupportType() {
        return type;
    }

    public static class Builder {
        private GachaRanking level = GachaRanking.R;
        private SupportType type = SupportType.SPEED;
        private List<SupportEntry> supports = Lists.newArrayList();
        private List<Identifier> supporters = Lists.newArrayList();
        private int damage = 3;
        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder ranking(GachaRanking level) {
            this.level = level;
            return this;
        }
        
        public Builder maxDamage(int damage) {
            this.damage = damage;
            return this;
        }

        public Builder supportType(SupportType type) {
            this.type = type;
            return this;
        }

        public Builder addSupport(SupportEntry support) {
            this.supports.add(support);
            return this;
        }

        public Builder addSupporter(Identifier name) {
            this.supporters.add(name);
            return this;
        }

        public SupportCard build() {
            return new SupportCard(level, type, supports, supporters, damage);
        }
    }
}
