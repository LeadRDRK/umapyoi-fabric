package net.tracen.umapyoi.events.handler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.events.AnvilUpdateCallback;
import net.tracen.umapyoi.item.FadedUmaSoulItem;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.data.builtin.UmaDataRegistry;
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
            ResourceLocation name = soul.getOrCreateTag().contains("name") ?
                    ResourceLocation.tryParse(soul.getOrCreateTag().getString("name")) : UmaDataRegistry.COMMON_UMA.getId();
            if(!registry.containsKey(name) || registry.get(name).getGachaRanking() != GachaRanking.R) return Optional.empty();

            var id = UmaDataRegistry.SHENONE_SUZUNA.getId();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = FadedUmaSoulItem.genUmaSoul(id.toString(), registry.get(id));

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> tycheSoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(Items.FEATHER)) return Optional.empty();
            if(!itemName.equalsIgnoreCase("tyche")) return Optional.empty();
            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());
            ResourceLocation name = soul.getOrCreateTag().contains("name") ?
                    ResourceLocation.tryParse(soul.getOrCreateTag().getString("name")) : UmaDataRegistry.COMMON_UMA.getId();
            if(!registry.containsKey(name) || !registry.get(name).getIdentifier().equals(UmaDataRegistry.COMMON_UMA.getId())) return Optional.empty();

            var id = UmaDataRegistry.TYCHE.getId();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = FadedUmaSoulItem.genUmaSoul(id.toString(), registry.get(id));

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> miyaSoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(Items.BAMBOO)) return Optional.empty();
            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());
            ResourceLocation name = soul.getOrCreateTag().contains("name") ?
                    ResourceLocation.tryParse(soul.getOrCreateTag().getString("name")) : UmaDataRegistry.COMMON_UMA.getId();
            if(!registry.containsKey(name) || registry.get(name).getGachaRanking() != GachaRanking.R) return Optional.empty();

            var id = UmaDataRegistry.MIYA_YOMOGI.getId();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = FadedUmaSoulItem.genUmaSoul(id.toString(), registry.get(id));

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> venusParkSoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(Items.BREAD)) return Optional.empty();
            if(!itemName.equalsIgnoreCase("vivelafrance")) return Optional.empty();

            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());
            ResourceLocation name = soul.getOrCreateTag().contains("name") ?
                    ResourceLocation.tryParse(soul.getOrCreateTag().getString("name")) : UmaDataRegistry.COMMON_UMA.getId();
            if(!registry.containsKey(name) || registry.get(name).getGachaRanking() != GachaRanking.R) return Optional.empty();

            var id = UmaDataRegistry.VENUS_PARK.getId();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
            egg.getOrCreateTag().putString("name", id.toString());

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> zhengSoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(Items.FEATHER)) return Optional.empty();

            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());
            ResourceLocation name = soul.getOrCreateTag().contains("name") ?
                    ResourceLocation.tryParse(soul.getOrCreateTag().getString("name")) : UmaDataRegistry.COMMON_UMA.getId();
            if(!registry.containsKey(name) ||
                    !registry.get(name).getIdentifier().equals(UmaDataRegistry.AGNES_TACHYON.get().getIdentifier()))
                return Optional.empty();

            var id = UmaDataRegistry.SYAMEIMARU_ZHENG.getId();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
            egg.getOrCreateTag().putString("name", id.toString());

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> dumnheintSoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(Items.GUNPOWDER)) return Optional.empty();
            if(!itemName.equalsIgnoreCase("kino")) return Optional.empty();
            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());
            ResourceLocation name = soul.getOrCreateTag().contains("name") ?
                    ResourceLocation.tryParse(soul.getOrCreateTag().getString("name")) : UmaDataRegistry.COMMON_UMA.getId();
            if(!registry.containsKey(name) || registry.get(name).getGachaRanking() != GachaRanking.R) return Optional.empty();

            var id = UmaDataRegistry.DUMNHEINT.getId();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
            egg.getOrCreateTag().putString("name", id.toString());

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> darleySoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(ItemRegistry.THREE_GODDESS.get())) return Optional.empty();

            if(!itemName.equalsIgnoreCase("darley")) return Optional.empty();

            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());

            var id = UmaDataRegistry.DARLEY_ARABIAN.getId();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
            egg.getOrCreateTag().putString("name", id.toString());

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> byerleySoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(ItemRegistry.THREE_GODDESS.get())) return Optional.empty();

            if(!itemName.equalsIgnoreCase("byerley")) return Optional.empty();

            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());

            var id = UmaDataRegistry.BYERLEY_TURK.getId();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
            egg.getOrCreateTag().putString("name", id.toString());

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }

        public Optional<AnvilUpdateCallback.Result> godolphinSoul() {
            if(!soul.is(ItemRegistry.BLANK_UMA_SOUL.get())) return Optional.empty();
            if(!material.is(ItemRegistry.THREE_GODDESS.get())) return Optional.empty();

            if(!itemName.equalsIgnoreCase("godolphin")) return Optional.empty();

            var registry = UmapyoiAPI.getUmaDataRegistry(player.level());

            var id = UmaDataRegistry.GODOLPHIN_BARB.getId();
            if(!registry.containsKey(id)) return Optional.empty();
            ItemStack egg = ItemRegistry.BLANK_UMA_SOUL.get().getDefaultInstance();
            egg.getOrCreateTag().putString("name", id.toString());

            return Optional.of(AnvilUpdateCallback.Result.pass(egg.copy(), 5, 1));
        }
    }
}
