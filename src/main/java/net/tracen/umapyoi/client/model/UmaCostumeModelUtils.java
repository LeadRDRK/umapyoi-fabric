package net.tracen.umapyoi.client.model;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.UmaCostumeItem;
import net.tracen.umapyoi.registry.cosmetics.CosmeticData;
import net.tracen.umapyoi.utils.ClientUtils;

public class UmaCostumeModelUtils {
    public static ResourceLocation getCostumeTexture(ItemStack stack, boolean tanned) {
        if(stack.is(ItemRegistry.SUMMER_UNIFORM))
            return tanned ? ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/summer_uniform_tanned.png")
                    : ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/summer_uniform.png");
        if(stack.is(ItemRegistry.WINTER_UNIFORM))
            return tanned ? ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/winter_uniform_tanned.png")
                    : ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/winter_uniform.png");
        if(stack.is(ItemRegistry.TRAINING_SUIT))
            return tanned ? ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/trainning_suit_tanned.png")
                    : ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/trainning_suit.png");
        if(stack.is(ItemRegistry.SWIMSUIT))
            return tanned ? ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/swimsuit_tanned.png")
                    : ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "textures/model/swimsuit.png");

        ResourceLocation loc = UmaCostumeItem.getCostumeID(stack);
        CosmeticData data = ClientUtils.getClientCosmeticDataRegistry().get(loc)
                .map(Holder::value).orElse(null);
        return data == null ? CosmeticData.DEFAULT_COSTUME.getTexture(tanned) : data.getTexture(tanned);

    }

    public static ResourceLocation getCostumeModel(ItemStack stack) {
        if(stack.is(ItemRegistry.SUMMER_UNIFORM))
            return ClientUtils.SUMMER_UNIFORM;
        if(stack.is(ItemRegistry.WINTER_UNIFORM))
            return ClientUtils.WINTER_UNIFORM;
        if(stack.is(ItemRegistry.TRAINING_SUIT))
            return ClientUtils.TRAINING_SUIT;
        if(stack.is(ItemRegistry.SWIMSUIT))
            return ClientUtils.SWIMSUIT;

        ResourceLocation loc = UmaCostumeItem.getCostumeID(stack);
        CosmeticData data = ClientUtils.getClientCosmeticDataRegistry().get(loc)
                .map(Holder::value).orElse(null);
        return data == null ? CosmeticData.DEFAULT_COSTUME.model() : data.model();
    }
}
