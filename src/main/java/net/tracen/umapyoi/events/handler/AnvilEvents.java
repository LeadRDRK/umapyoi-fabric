package net.tracen.umapyoi.events.handler;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.data.builtin.UmaDataRegistry;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;
import net.tracen.umapyoi.events.AnvilUpdateCallback;
import net.tracen.umapyoi.item.FadedUmaSoulItem;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.utils.GachaRanking;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class AnvilEvents {
    public static AnvilUpdateCallback.Result onAnvilEgg(ItemStack soul, ItemStack material, String itemName, int baseCost, Player player) {
        var suppliers = new AnvilOutputSuppliers(soul, material, itemName, baseCost, player);
        return suppliers.stream()
                .map(Supplier::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElse(AnvilUpdateCallback.Result.empty());
    }

    static class AnvilOutputSuppliers {
        private final ItemStack soul;
        private final ItemStack material;
        private final String itemName;
        private final int baseCost;
        private final Player player;

        public AnvilOutputSuppliers(ItemStack soul, ItemStack material, String itemName, int baseCost, Player player) {
            this.soul = soul;
            this.material = material;
            this.itemName = itemName;
            this.baseCost = baseCost;
            this.player = player;
        }

        public Stream<Supplier<Optional<AnvilUpdateCallback.Result>>> stream() {
            return Stream.of(
                    this::venusParkSoul,
                    this::zhengSoul,
                    this::dumnheintSoul,
                    this::stardustSoul,
                    this::darleySoul,
                    this::byerleySoul,
                    this::godolphinSoul,
                    this::miyaSoul,
                    this::tycheSoul,
                    this::suzunaSoul
            );
        }

        public Optional<AnvilUpdateCallback.Result> suzunaSoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(Items.DIAMOND_SWORD)) return Optional.empty();
            if(!itemName.equalsIgnoreCase("priconne")) return Optional.empty();
            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());
            ResourceLocation name = soul.has(DataComponentsTypeRegistry.DATA_LOCATION.get()) ?
        		soul.get(DataComponentsTypeRegistry.DATA_LOCATION.get()) : UmaDataRegistry.COMMON_UMA.location();
            if(!registry.containsKey(name) || registry.get(name).ranking() != GachaRanking.R) return Optional.empty();

            var id = UmaDataRegistry.SHENONE_SUZUNA.location();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = FadedUmaSoulItem.genUmaSoul(id, registry.get(id));

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> tycheSoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(Items.FEATHER)) return Optional.empty();
            if(!itemName.equalsIgnoreCase("tyche")) return Optional.empty();
            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());
            ResourceLocation name = soul.has(DataComponentsTypeRegistry.DATA_LOCATION.get()) ?
        		soul.get(DataComponentsTypeRegistry.DATA_LOCATION.get()) : UmaDataRegistry.COMMON_UMA.location();
            if(!registry.containsKey(name) || !registry.get(name).identifier().equals(UmaDataRegistry.COMMON_UMA.location())) return Optional.empty();

            var id = UmaDataRegistry.TYCHE.location();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = FadedUmaSoulItem.genUmaSoul(id, registry.get(id));

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> miyaSoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(UmapyoiItemTags.BAMBOO)) return Optional.empty();
            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());
            ResourceLocation name = soul.has(DataComponentsTypeRegistry.DATA_LOCATION.get()) ?
        		soul.get(DataComponentsTypeRegistry.DATA_LOCATION.get()) : UmaDataRegistry.COMMON_UMA.location();
            if(!registry.containsKey(name) || registry.get(name).ranking() != GachaRanking.R) return Optional.empty();

            var id = UmaDataRegistry.MIYA_YOMOGI.location();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = FadedUmaSoulItem.genUmaSoul(id, registry.get(id));

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> venusParkSoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(ConventionalItemTags.BREADS_FOODS)) return Optional.empty();
            if(!itemName.equalsIgnoreCase("vivelafrance")) return Optional.empty();

            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());
            ResourceLocation name = soul.has(DataComponentsTypeRegistry.DATA_LOCATION.get()) ?
        		soul.get(DataComponentsTypeRegistry.DATA_LOCATION.get()) : UmaDataRegistry.COMMON_UMA.location();
            if(!registry.containsKey(name) || registry.get(name).ranking() != GachaRanking.R) return Optional.empty();

            var id = UmaDataRegistry.VENUS_PARK.location();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
            egg.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), id);

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> zhengSoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(Items.FEATHER)) return Optional.empty();

            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());
            ResourceLocation name = soul.has(DataComponentsTypeRegistry.DATA_LOCATION.get()) ?
        		soul.get(DataComponentsTypeRegistry.DATA_LOCATION.get()) : UmaDataRegistry.COMMON_UMA.location();
            if(!registry.containsKey(name) ||
                    !registry.get(name).identifier().equals(UmaDataRegistry.AGNUS_TACHYON.location())) // meant to compare with identifier, but it's the same here
                return Optional.empty();

            var id = UmaDataRegistry.SYAMEIMARU_ZHENG.location();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
            egg.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), id);

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> dumnheintSoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(Items.GUNPOWDER)) return Optional.empty();
            if(!itemName.equalsIgnoreCase("kino")) return Optional.empty();
            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());
            ResourceLocation name = soul.has(DataComponentsTypeRegistry.DATA_LOCATION.get()) ?
        		soul.get(DataComponentsTypeRegistry.DATA_LOCATION.get()) : UmaDataRegistry.COMMON_UMA.location();
            if(!registry.containsKey(name) || registry.get(name).ranking() != GachaRanking.R) return Optional.empty();

            var id = UmaDataRegistry.DUMNHEINT.location();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
            egg.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), id);

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        private Optional<AnvilUpdateCallback.Result> stardustSoul() {
            if (!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();

            PotionContents potionContents = material.get(DataComponents.POTION_CONTENTS);
            if (potionContents == null) return Optional.empty();

            boolean flag1 = false;
            for (var eff : potionContents.getAllEffects()) {
                if (eff.getEffect().equals(MobEffects.MOVEMENT_SPEED)) {
                    flag1 = true;
                    break;
                }
            }
            boolean flag2 = Optional.ofNullable(material.get(DataComponents.FOOD))
                    .map(FoodProperties::effects)
                    .map(p -> p.stream()
                            .anyMatch(eff -> eff.effect().equals(MobEffects.MOVEMENT_SPEED))
                    )
                    .orElse(false);
            if (!(flag1 || flag2)) return Optional.empty();
            if (!itemName.equalsIgnoreCase("synchro")) return Optional.empty();

            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());
            ResourceLocation name = soul.has(DataComponentsTypeRegistry.DATA_LOCATION.get()) ?
                    soul.get(DataComponentsTypeRegistry.DATA_LOCATION.get()) : UmaDataRegistry.COMMON_UMA.location();
            if(!registry.containsKey(name) ||
                    !registry.get(name).identifier().equals(UmaDataRegistry.SILENCE_SUZUKA.location())) // meant to compare with identifier, but it's the same here
                return Optional.empty();

            var id = UmaDataRegistry.STARDUST.location();
            if (!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
            egg.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), id);

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> darleySoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(ItemRegistry.THREE_GODDESS.get())) return Optional.empty();

            if(!itemName.equalsIgnoreCase("darley")) return Optional.empty();

            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());

            var id = UmaDataRegistry.DARLEY_ARABIAN.location();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
            egg.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), id);

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> byerleySoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(ItemRegistry.THREE_GODDESS.get())) return Optional.empty();

            if(!itemName.equalsIgnoreCase("byerley")) return Optional.empty();

            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());

            var id = UmaDataRegistry.BYERLEY_TURK.location();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
            egg.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), id);

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> godolphinSoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(ItemRegistry.THREE_GODDESS.get())) return Optional.empty();

            if(!itemName.equalsIgnoreCase("godolphin")) return Optional.empty();

            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());

            var id = UmaDataRegistry.GODOLPHIN_BARB.location();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
            egg.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), id);

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }
    }
}
