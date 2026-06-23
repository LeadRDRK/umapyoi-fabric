package net.tracen.umapyoi.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.events.FindUmaSoulCallback;
import net.tracen.umapyoi.item.AbstractSuitItem;
import net.tracen.umapyoi.item.UmaSoulItem;
import net.tracen.umapyoi.registry.cosmetics.CosmeticData;
import net.tracen.umapyoi.registry.races.Race;
import net.tracen.umapyoi.registry.races.field.RaceField;
import net.tracen.umapyoi.registry.races.tags.RaceTag;
import net.tracen.umapyoi.registry.training.card.SupportCard;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.Optional;

import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketsApi;

public class UmapyoiAPI {
    @Environment(EnvType.CLIENT)
    public static ItemStack getRenderingUmaSoul(LivingEntity entity) {
        // STUB
        return getUmaSoulFromTrinkets(entity);
    }

    public static ItemStack getUmaSoul(LivingEntity entity) {
        var preEvent = new FindUmaSoulCallback.Pre.Context(entity);
        FindUmaSoulCallback.Pre.invoke(preEvent);

        var result = preEvent.getUmaSoul();
        if (!result.isEmpty())
            return result;

        var postEvent = new FindUmaSoulCallback.Post.Context(entity, getUmaSoulFromTrinkets(entity));
        FindUmaSoulCallback.Post.invoke(postEvent);
        return postEvent.getUmaSoul();
    }

    private static ItemStack getUmaSoulFromTrinkets(LivingEntity entity) {
        var comp = TrinketsApi.getAttachment(entity);
        return getUmaSoulFromTrinkets(comp);
    }

    private static ItemStack getUmaSoulFromTrinkets(TrinketAttachment comp) {
        return Optional.ofNullable(comp.getInventory("umapyoi/uma_soul"))
                .map(inventory -> {
                    if (inventory.getContainerSize() <= 0)
                        return ItemStack.EMPTY;

                    var stack = inventory.getItem(0);
                    if (stack.getItem() instanceof UmaSoulItem)
                        return stack;

                    return ItemStack.EMPTY;
                })
                .orElse(ItemStack.EMPTY);
    }

    public static ItemStack getUmaSuit(LivingEntity entity) {
        var comp = TrinketsApi.getAttachment(entity);
        return getUmaSuit(comp);
    }

    public static ItemStack getUmaSuit(TrinketAttachment comp) {
        return Optional.ofNullable(comp.getInventory("umapyoi/uma_suit"))
                .map(inventory -> {
                    if (inventory.getContainerSize() <= 0)
                        return ItemStack.EMPTY;

                    var stack = inventory.getItem(0);
                    if (stack.getItem() instanceof AbstractSuitItem)
                        return stack;

                    return ItemStack.EMPTY;
                })
                .orElse(ItemStack.EMPTY);
    }

    public static boolean isUmaSuitRendering(LivingEntity player) {
        // STUB
        return !getUmaSuit(player).isEmpty();
    }

    public static boolean isUmaSuitRendering(TrinketAttachment comp) {
        // STUB
        return !getUmaSuit(comp).isEmpty();
    }

    public static boolean isUmaSuitHasHat(LivingEntityRenderState state) {
        var suitModel = state.umapyoi$getSuitModel();
        if (suitModel != null) {
            return !suitModel.getChild("hat").isEmpty();
        }
        return false;
    }

    public static boolean isSpecifyUmamusumeSoul(ItemStack soul, Identifier name) {
        return UmaSoulUtils.getName(soul).equals(name);
    }

    public static boolean isSpecifyUmamusume(ItemStack soul, Identifier identifier, Level level) {
        return UmapyoiAPI.getUmaDataRegistry(level).get(UmaSoulUtils.getName(soul))
                .orElseThrow().value().identifier().equals(identifier);
    }

    public static Registry<UmaData> getUmaDataRegistry(Level level) {
        if (level.isClientSide())
            return ClientUtils.getClientUmaDataRegistry();
        return level.registryAccess().lookupOrThrow(UmaData.REGISTRY_KEY);
    }

    public static Registry<SupportCard> getSupportCardRegistry(Level level) {
        if (level.isClientSide())
            return ClientUtils.getClientSupportCardRegistry();
        return level.registryAccess().lookupOrThrow(SupportCard.REGISTRY_KEY);
    }

    public static Registry<Race> getRaceRegistry(Level level) {
        if (level.isClientSide()) return ClientUtils.getRaceRegistry();
        return level.registryAccess().lookupOrThrow(Race.REGISTRY_KEY);
    }

    public static Registry<RaceTag> getRaceTagRegistry(Level level) {
        if (level.isClientSide()) return ClientUtils.getRaceTagRegistry();
        return level.registryAccess().lookupOrThrow(RaceTag.REGISTRY_KEY);
    }

    public static Registry<RaceField> getRaceFieldRegistry(Level level) {
        if (level.isClientSide()) return ClientUtils.getRaceFieldRegistry();
        return level.registryAccess().lookupOrThrow(RaceField.REGISTRY_KEY);
    }

    public static HolderLookup.RegistryLookup<Race> getRaceRegistry(HolderLookup.Provider provider) {
        return provider.lookupOrThrow(Race.REGISTRY_KEY);
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
