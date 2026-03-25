package net.tracen.umapyoi.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.cosmetics.CosmeticData;
import net.tracen.umapyoi.utils.ClientUtils;

import java.util.Comparator;
import java.util.stream.Stream;

public class UmaCostumeItem extends AbstractSuitItem implements CreativeModeTabFiller {
    private static final Comparator<Reference<CosmeticData>> COMPARATOR = new DataComparator();

    public UmaCostumeItem(Properties p) {
        super(p);
    }

    public static Stream<Reference<CosmeticData>> sortedCosmeticDataList(HolderLookup.Provider provider) {
        return UmapyoiAPI.getCosmeticDataRegistry(provider).listElements().sorted(UmaCostumeItem.COMPARATOR);
    }

    @Override
    @MethodsReturnNonnullByDefault
    public Component getName(ItemStack pStack) {
        return Component.translatable(
                Util.makeDescriptionId("item", UmaCostumeItem.getCostumeID(pStack)) + ".name");
    }

    public static ResourceLocation getCostumeID(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.DATA_LOCATION.get(), CosmeticData.COMMON_COSTUME);
    }

    public static ItemStack getCostume(ResourceLocation loc) {
        ItemStack defaultInstance = ItemRegistry.UMA_COSTUME.getDefaultInstance();
        defaultInstance.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), loc);
        return defaultInstance;
    }

    @Override
    public void fillItemCategory(FabricItemGroupEntries entries) {
        UmaCostumeItem.sortedCosmeticDataList(entries.getContext().holders()).forEach(
                entry -> {
                    ItemStack result = ItemRegistry.UMA_COSTUME.getDefaultInstance();
                    result.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), entry.key().location());
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
    public ResourceLocation getModel(ItemStack stack) {
        ResourceLocation loc = stack.get(DataComponentsTypeRegistry.DATA_LOCATION.get());

        CosmeticData data = ClientUtils.getClientCosmeticDataRegistry().get(loc)
                .map(Reference::value).orElse(null);

        return data == null ? CosmeticData.DEFAULT_COSTUME.model() : data.model();
    }

    @Override
    public ResourceLocation getTexture(ItemStack stack, boolean tanned) {
        ResourceLocation loc = stack.get(DataComponentsTypeRegistry.DATA_LOCATION.get());
        CosmeticData data = ClientUtils.getClientCosmeticDataRegistry().get(loc)
                .map(Reference::value).orElse(null);
        return data == null ? CosmeticData.DEFAULT_COSTUME.getTexture(tanned) : data.getTexture(tanned);
    }

    @Override
    public ResourceLocation getFlatModel(ItemStack stack) {
        ResourceLocation loc = stack.get(DataComponentsTypeRegistry.DATA_LOCATION.get());
        CosmeticData data = ClientUtils.getClientCosmeticDataRegistry().get(loc)
                .map(Reference::value).orElse(null);
        return data == null ? CosmeticData.DEFAULT_COSTUME.flatModel().orElse(CosmeticData.DEFAULT_COSTUME.model())
                : data.flatModel().orElse(data.model());
    }

    @Override
    public ResourceLocation getFlatTexture(ItemStack stack, boolean tanned) {
        ResourceLocation loc = stack.get(DataComponentsTypeRegistry.DATA_LOCATION.get());
        CosmeticData data = ClientUtils.getClientCosmeticDataRegistry().get(loc)
                .map(Reference::value).orElse(null);
        return data == null ? CosmeticData.DEFAULT_COSTUME.getFlatTexture(tanned) : data.getFlatTexture(tanned);
    }
}