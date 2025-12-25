package net.tracen.umapyoi.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.registry.cosmetics.CosmeticData;
import net.tracen.umapyoi.utils.ClientUtils;

import java.util.Comparator;
import java.util.stream.Stream;

public class UmaCostumeItem extends AbstractSuitItem implements CreativeModeTabFiller {
    private static final Comparator<Reference<CosmeticData>> COMPARATOR = new DataComparator();

    public static Stream<Reference<CosmeticData>> sortedCosmeticDataList(HolderLookup.Provider provider) {
        return UmapyoiAPI.getCosmeticDataRegistry(provider).listElements().sorted(UmaCostumeItem.COMPARATOR);
    }

    @Override
    @MethodsReturnNonnullByDefault
    public String getDescriptionId(ItemStack pStack) {
        return Util.makeDescriptionId("item", UmaCostumeItem.getCostumeID(pStack)) + ".name";
    }

    public static ResourceLocation getCostumeID(ItemStack stack) {
        if (stack.getOrCreateTag().contains("cosmetic"))
            return ResourceLocation.tryParse(stack.getOrCreateTag().getString("cosmetic"));
        return CosmeticData.COMMON_COSTUME;
    }

    public static ItemStack getCostume(ResourceLocation loc) {
        ItemStack defaultInstance = ItemRegistry.UMA_COSTUME.get().getDefaultInstance();
        defaultInstance.getOrCreateTag().putString("cosmetic", loc.toString());
        return defaultInstance;
    }

    @Override
    public void fillItemCategory(FabricItemGroupEntries entries) {
        UmaCostumeItem.sortedCosmeticDataList(entries.getContext().holders()).forEach(
                entry -> {
                    ItemStack result = ItemRegistry.UMA_COSTUME.get().getDefaultInstance();
                    result.getOrCreateTag().putString("cosmetic", entry.key().location().toString());
                    entries.accept(result);
                }
        );
    }

    private static class DataComparator implements Comparator<Reference<CosmeticData>> {
        @Override
        public int compare(Reference<CosmeticData> left, Reference<CosmeticData> right) {
            String leftName = left.key().location().toString();
            String rightName = right.key().location().toString();
            return leftName.compareToIgnoreCase(rightName);
        }
    }

    @Override
    protected ResourceLocation getModel(ItemStack stack) {
        ResourceLocation loc = ResourceLocation.tryParse(stack.getOrCreateTag().getString("cosmetic"));

        CosmeticData data = ClientUtils.getClientCosmeticDataRegistry().get(loc);

        return data == null ? CosmeticData.DEFAULT_COSTUME.model() : data.model();
    }

    @Override
    protected ResourceLocation getTexture(ItemStack stack, boolean tanned) {
        ResourceLocation loc = ResourceLocation.tryParse(stack.getOrCreateTag().getString("cosmetic"));
        CosmeticData data = ClientUtils.getClientCosmeticDataRegistry().get(loc);
        return data == null ? CosmeticData.DEFAULT_COSTUME.getTexture(tanned) : data.getTexture(tanned);
    }

    @Override
    protected ResourceLocation getFlatModel(ItemStack stack) {
        ResourceLocation loc = ResourceLocation.tryParse(stack.getOrCreateTag().getString("cosmetic"));
        CosmeticData data = ClientUtils.getClientCosmeticDataRegistry().get(loc);
        return data == null ? CosmeticData.DEFAULT_COSTUME.flatModel().orElse(CosmeticData.DEFAULT_COSTUME.model())
                : data.flatModel().orElse(data.model());
    }

    @Override
    protected ResourceLocation getFlatTexture(ItemStack stack, boolean tanned) {
        ResourceLocation loc = ResourceLocation.tryParse(stack.getOrCreateTag().getString("cosmetic"));
        CosmeticData data = ClientUtils.getClientCosmeticDataRegistry().get(loc);
        return data == null ? CosmeticData.DEFAULT_COSTUME.getFlatTexture(tanned) : data.getFlatTexture(tanned);
    }
}