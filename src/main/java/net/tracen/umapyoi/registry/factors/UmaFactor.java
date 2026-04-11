package net.tracen.umapyoi.registry.factors;

import com.mojang.serialization.Codec;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.RegistryNameHolder;
import net.tracen.umapyoi.registry.RegistryObject;
import net.tracen.umapyoi.registry.UmaFactorRegistry;

import java.util.Comparator;

public class UmaFactor extends RegistryNameHolder {
    public static class UmaFactorComparator implements Comparator<RegistryObject<UmaFactor>> {
        public static UmaFactorComparator INSTANCE = new UmaFactorComparator();
        @Override
        public int compare(RegistryObject<UmaFactor> o1, RegistryObject<UmaFactor> o2) {
            return compare(o1.get(), o1.getId(), o2.get(), o2.getId());
        }

        public static int compare(UmaFactor leftFactor, ResourceLocation leftLocation, UmaFactor rightFactor, ResourceLocation rightLocation) {
            if (leftFactor.type != rightFactor.type) return leftFactor.type.compareTo(rightFactor.type);
            return leftLocation == null || rightLocation == null ? 0 : leftLocation.compareTo(rightLocation);
        }
    }

    private final FactorType type;
    private String descriptionId;
    private String detailId;

    public static final ResourceKey<Registry<UmaFactor>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(Identifier.fromNamespaceAndPath(Umapyoi.MODID, "factor"));

    public static final Codec<UmaFactor> CODEC = Identifier.CODEC
            .xmap(loc -> UmaFactorRegistry.REGISTRY.get().get(loc).orElseThrow().value(),
                    RegistryNameHolder::getRegistryName);

    public UmaFactor(FactorType type) {
        this.type = type;
    }

    public void applyFactor(ItemStack soul, UmaFactorStack stack) {
    }

    public FactorType getFactorType() {
        return type;
    }

    @Override
    public int hashCode() {
        return this.getRegistryName().hashCode();
    }

    public String toString() {
        return this.getRegistryName().toString();
    }

    public Component getDescription() {
        return Component.translatable(this.getDescriptionId());
    }

    public Component getDescription(UmaFactorStack stack) {
        return this.getDescription();
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("uma_factor", this.getRegistryName());
        }
        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getFullDescription(int pLevel) {
        MutableComponent mutablecomponent = this.getDescription().copy();
        mutablecomponent.withStyle(ChatFormatting.GRAY);
        mutablecomponent.append(" ").append(Component.translatable("enchantment.level." + pLevel));
        return mutablecomponent;
    }

    public int getMaxLevel() {
        return this.getFactorType().getMaxLevel();
    }

    public Component getDescriptionDetail(UmaFactorStack stack) {
        return Component.translatable(this.getDetailDescriptionId());
    }

    protected String getOrCreateDescriptionDetail() {
        if (this.detailId == null) {
            this.detailId =  this.getDescriptionId() + ".desc";
        }
        return this.detailId;
    }

    public String getDetailDescriptionId() {
        return this.getOrCreateDescriptionDetail();
    }

    public boolean withStackEquals(UmaFactorStack left, UmaFactorStack right) {
        return left.getFactor() == right.getFactor();
    }

    public int hashCode(UmaFactorStack stack) {
        return this.hashCode();
    }
}
