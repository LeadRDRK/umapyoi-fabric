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
        return Stream
                .<Supplier<Optional<AnvilUpdateCallback.Result>>>of(
                        () -> venusParkSoul(soul, material, itemName, baseCost, player),
                        () -> zhengSoul(soul, material, itemName, baseCost, player),
                        () -> dumnheintSoul(soul, material, itemName, baseCost, player),
                        () -> darleySoul(soul, material, itemName, baseCost, player),
                        () -> byerleySoul(soul, material, itemName, baseCost, player),
                        () -> godolphinSoul(soul, material, itemName, baseCost, player),
                        () -> miyaSoul(soul, material, itemName, baseCost, player),
                        () -> tycheSoul(soul, material, itemName, baseCost, player),
                        () -> suzunaSoul(soul, material, itemName, baseCost, player)
                )
                .map(Supplier::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElse(AnvilUpdateCallback.Result.empty());
    }

    private static Optional<AnvilUpdateCallback.Result> suzunaSoul(ItemStack soul, ItemStack material, String itemName, int baseCost, Player player) {
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

    private static Optional<AnvilUpdateCallback.Result> tycheSoul(ItemStack soul, ItemStack material, String itemName, int baseCost, Player player) {
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

    private static Optional<AnvilUpdateCallback.Result> miyaSoul(ItemStack soul, ItemStack material, String itemName, int baseCost, Player player) {
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

    public static Optional<AnvilUpdateCallback.Result> venusParkSoul(ItemStack soul, ItemStack material, String itemName, int baseCost, Player player) {
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
    
    public static Optional<AnvilUpdateCallback.Result> zhengSoul(ItemStack soul, ItemStack material, String itemName, int baseCost, Player player) {
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
    
    public static Optional<AnvilUpdateCallback.Result> dumnheintSoul(ItemStack soul, ItemStack material, String itemName, int baseCost, Player player) {
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
    
    public static Optional<AnvilUpdateCallback.Result> darleySoul(ItemStack soul, ItemStack material, String itemName, int baseCost, Player player) {
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
    
    public static Optional<AnvilUpdateCallback.Result> byerleySoul(ItemStack soul, ItemStack material, String itemName, int baseCost, Player player) {
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
    
    public static Optional<AnvilUpdateCallback.Result> godolphinSoul(ItemStack soul, ItemStack material, String itemName, int baseCost, Player player) {
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
