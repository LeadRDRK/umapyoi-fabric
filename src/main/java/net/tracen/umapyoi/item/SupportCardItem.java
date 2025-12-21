package net.tracen.umapyoi.item;

import com.google.common.base.Suppliers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.data.tag.UmapyoiItemTags;
import net.tracen.umapyoi.registry.SupportCardRegistry;
import net.tracen.umapyoi.registry.training.SupportContainer;
import net.tracen.umapyoi.registry.training.SupportStack;
import net.tracen.umapyoi.registry.training.SupportType;
import net.tracen.umapyoi.registry.training.card.SupportCard;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nullable;

public class SupportCardItem extends Item implements SupportContainer, CreativeModeTabFiller {
    private static final Comparator<Entry<ResourceKey<SupportCard>, SupportCard>> COMPARATOR = new CardDataComparator();

    public SupportCardItem() {
        super(Umapyoi.defaultItemProperties().stacksTo(1));
    }

    public static Stream<Entry<ResourceKey<SupportCard>, SupportCard>> sortedCardDataList() {
        return ClientUtils.getClientSupportCardRegistry().entrySet().stream().sorted(SupportCardItem.COMPARATOR);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void fillItemCategory(CreativeModeTab.Output entries) {
        SupportCardItem.sortedCardDataList().forEach(card -> {
            if (card.getKey().location().equals(SupportCardRegistry.BLANK_CARD.getId()))
                return;
            ItemStack result = getDefaultInstance();
            result.getOrCreateTag().putString("support_card", card.getKey().location().toString());
            result.getOrCreateTag().putString("ranking",
                    card.getValue().getGachaRanking().name().toLowerCase());
            result.getOrCreateTag().putInt("maxDamage",
                    card.getValue().getMaxDamage());
            entries.accept(result);
        });
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack result = super.getDefaultInstance();
        result.getOrCreateTag().putString("support_card", SupportCardRegistry.BLANK_CARD.getId().toString());
        result.getOrCreateTag().putString("ranking", GachaRanking.R.name().toLowerCase());
        result.getOrCreateTag().putInt("maxDamage", 0);
        return result;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.is(UmapyoiItemTags.HORSESHOE) || super.isValidRepairItem(pToRepair, pRepair);
    }

    @Override
    public Rarity getRarity(ItemStack pStack) {
        GachaRanking ranking = GachaRanking.getGachaRanking(pStack);
        return ranking == GachaRanking.SSR ? Rarity.EPIC : ranking == GachaRanking.SR ? Rarity.UNCOMMON : Rarity.COMMON;
    }

    @Override
    public String getDescriptionId(ItemStack pStack) {
        return Util.makeDescriptionId("support_card", this.getSupportCardID(pStack)) + ".name";
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        ResourceLocation cardID = this.getSupportCardID(stack);
        if (isEmptyCard(worldIn, cardID))
            return ;
        if(!this.getSupports(worldIn, stack).isEmpty()) {
            if (Screen.hasShiftDown() || !Umapyoi.CONFIG.TOOLTIP_SWITCH()) {
                tooltip.add(Component.translatable("tooltip.umapyoi.supports").withStyle(ChatFormatting.AQUA));
                this.getSupports(worldIn, stack)
                        .forEach(support -> tooltip.add(support.getDescription().copy().withStyle(ChatFormatting.GRAY)));
            } else {
                tooltip.add(Component.translatable("tooltip.umapyoi.support_card.press_shift_for_supports")
                        .withStyle(ChatFormatting.AQUA));
            }
        }

        List<ResourceLocation> supporters = ClientUtils.getClientSupportCardRegistry().get(cardID).getSupporters();
        if (!supporters.isEmpty()) {
            if (Screen.hasControlDown() || !Umapyoi.CONFIG.TOOLTIP_SWITCH()) {
                tooltip.add(Component.translatable("tooltip.umapyoi.supporters").withStyle(ChatFormatting.AQUA));
                supporters.forEach(name -> tooltip
                        .add(UmaSoulUtils.getTranslatedUmaName(name).copy().withStyle(ChatFormatting.GRAY)));
            } else {
                tooltip.add(Component.translatable("tooltip.umapyoi.support_card.press_ctrl_for_supporters")
                        .withStyle(ChatFormatting.AQUA));
            }
        }
    }

    public ResourceLocation getSupportCardID(ItemStack stack) {
        if (stack.getOrCreateTag().contains("support_card"))
            return ResourceLocation.tryParse(stack.getOrCreateTag().getString("support_card"));
        return SupportCardRegistry.BLANK_CARD.getId();
    }

    public SupportCard getSupportCard(Level level, ItemStack stack) {
        ResourceLocation cardID = this.getSupportCardID(stack);
        if (isEmptyCard(level, cardID))
            return SupportCardRegistry.BLANK_CARD.get();
        return UmapyoiAPI.getSupportCardRegistry(level).get(cardID);
    }

    private boolean isEmptyCard(Level level, ResourceLocation cardID) {
        return level == null || cardID.equals(SupportCardRegistry.BLANK_CARD.getId()) || !UmapyoiAPI.getSupportCardRegistry(level).containsKey(cardID);
    }

    @Override
    public boolean isConsumable(Level level, ItemStack stack) {
        return false;
    }

    @Override
    public GachaRanking getSupportLevel(Level level, ItemStack stack) {
        if (level == null)
            return GachaRanking.R;
        return this.getSupportCard(level, stack).getGachaRanking();
    }

    @Override
    public SupportType getSupportType(Level level, ItemStack stack) {
        return this.getSupportCard(level, stack).getSupportType();
    }

    @Override
    public List<SupportStack> getSupports(Level level, ItemStack stack) {
        return Suppliers.memoize(this.getSupportCard(level, stack)::getSupportStacks).get();
    }

    @Override
    public Predicate<ItemStack> canSupport(Level level, ItemStack stack) {
        return itemstack -> {
            if (level == null)
                return false;
            var item = itemstack.getItem();
            if (item instanceof UmaSoulItem) {
                UmaData data = UmapyoiAPI.getUmaDataRegistry(level).get(UmaSoulUtils.getName(itemstack));
                return !(this.getSupportCard(level, stack).getSupporters().contains(data.getIdentifier()));
            }
            if (item instanceof SupportCardItem) {
                return this.checkSupports(level, stack, itemstack);
            }
            return true;
        };
    }

    public boolean checkSupports(Level level, ItemStack stack, ItemStack other) {
        if (stack.getItem()instanceof SupportCardItem) {

            var supportCardID = this.getSupportCardID(stack);
            var otherCardID = this.getSupportCardID(other);
            if (supportCardID.equals(otherCardID))
                return false;

            var supportCard = this.getSupportCard(level, stack);
            var otherCard = this.getSupportCard(level, other);

            for (ResourceLocation name : supportCard.getSupporters()) {
                if (otherCard.getSupporters().contains(name))
                    return false;
            }
        }
        return true;
    }

    private static class CardDataComparator implements Comparator<Entry<ResourceKey<SupportCard>, SupportCard>> {
        @Override
        public int compare(Entry<ResourceKey<SupportCard>, SupportCard> left,
                Entry<ResourceKey<SupportCard>, SupportCard> right) {
            var leftRanking = left.getValue().getGachaRanking();
            var rightRanking = right.getValue().getGachaRanking();
            if (leftRanking == rightRanking) {
                String leftName = left.getKey().location().toString();
                String rightName = right.getKey().location().toString();
                return leftName.compareToIgnoreCase(rightName);
            }
            return leftRanking.compareTo(rightRanking);
        }
    }
}
