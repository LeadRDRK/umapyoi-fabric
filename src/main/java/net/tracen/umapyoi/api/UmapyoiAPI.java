package net.tracen.umapyoi.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.events.FindUmaSoulCallback;
import net.tracen.umapyoi.item.AbstractSuitItem;
import net.tracen.umapyoi.item.UmaSoulItem;
import net.tracen.umapyoi.registry.cosmetics.CosmeticData;
import net.tracen.umapyoi.registry.training.card.SupportCard;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import dev.emi.trinkets.api.TrinketsApi;

public class UmapyoiAPI {
    @Environment(EnvType.CLIENT)
    public static ItemStack getRenderingUmaSoul(LivingEntity entity) {
        // STUB
        return getUmaSoulFromTrinkets(entity);
    }

    public static ItemStack getUmaSoul(LivingEntity entity) {
        var result = FindUmaSoulCallback.Pre.invoke(entity);
        if (!result.isEmpty())
            return result;

        return FindUmaSoulCallback.Post.invoke(entity, getUmaSoulFromTrinkets(entity));
    }

    private static ItemStack getUmaSoulFromTrinkets(LivingEntity entity) {
        var compOpt = TrinketsApi.getTrinketComponent(entity);
        if (compOpt.isPresent()) {
            var comp = compOpt.get();
            var entityInventory = comp.getInventory();
            if (entityInventory.containsKey("umapyoi")) {
                var group = entityInventory.get("umapyoi");
                if (group.containsKey("uma_soul")) {
                    var inventory = group.get("uma_soul");
                    if (inventory.getContainerSize() <= 0)
                        return ItemStack.EMPTY;

                    var stack = inventory.getItem(0);
                    if (stack.getItem() instanceof UmaSoulItem)
                        return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getUmaSuit(LivingEntity entity) {
        var compOpt = TrinketsApi.getTrinketComponent(entity);
        if (compOpt.isPresent()) {
            var comp = compOpt.get();
            var entityInventory = comp.getInventory();
            if (entityInventory.containsKey("umapyoi")) {
                var group = entityInventory.get("umapyoi");
                if (group.containsKey("uma_suit")) {
                    var inventory = group.get("uma_suit");
                    if (inventory.getContainerSize() <= 0)
                        return ItemStack.EMPTY;

                    var stack = inventory.getItem(0);
                    if (stack.getItem() instanceof UmaSoulItem)
                        return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean isUmaSuitRendering(LivingEntity player) {
        // STUB
        return !getUmaSuit(player).isEmpty();
    }

    public static boolean isUmaSuitHasHat(LivingEntity player) {
        var item = UmapyoiAPI.getUmaSuit(player).getItem();
        if (item instanceof AbstractSuitItem suit) {
            return !suit.getBaseModel().getChild("hat").isEmpty();
        }
        return false;
    }

    public static boolean isSpecifyUmamusumeSoul(ItemStack soul, ResourceLocation name) {
        return UmaSoulUtils.getName(soul).equals(name);
    }

    public static boolean isSpecifyUmamusume(ItemStack soul, ResourceLocation identifier, Level level) {
        return UmapyoiAPI.getUmaDataRegistry(level).get(UmaSoulUtils.getName(soul)).getIdentifier().equals(identifier);
    }

    public static Registry<UmaData> getUmaDataRegistry(Level level) {
        if (level.isClientSide())
            return ClientUtils.getClientUmaDataRegistry();
        return level.registryAccess().registryOrThrow(UmaData.REGISTRY_KEY);
    }

    public static Registry<SupportCard> getSupportCardRegistry(Level level) {
        if (level.isClientSide())
            return ClientUtils.getClientSupportCardRegistry();
        return level.registryAccess().registryOrThrow(SupportCard.REGISTRY_KEY);
    }

    public static HolderLookup.RegistryLookup<UmaData> getUmaDataRegistry(HolderLookup.Provider provider) {
        return provider.lookupOrThrow(UmaData.REGISTRY_KEY);
    }

    public static HolderLookup.RegistryLookup<SupportCard> getSupportCardRegistry(HolderLookup.Provider provider) {
        return provider.lookupOrThrow(SupportCard.REGISTRY_KEY);
    }

    public static HolderLookup.RegistryLookup<CosmeticData> getCosmeticDataRegistry(HolderLookup.Provider provider) {
        return provider.lookupOrThrow(CosmeticData.REGISTRY_KEY);
    }
}
