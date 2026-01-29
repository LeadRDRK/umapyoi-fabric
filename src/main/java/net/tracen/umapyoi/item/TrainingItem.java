package net.tracen.umapyoi.item;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.registry.training.SupportContainer;
import net.tracen.umapyoi.registry.training.SupportStack;
import net.tracen.umapyoi.registry.training.SupportType;
import net.tracen.umapyoi.registry.training.TrainingSupport;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TrainingItem extends Item implements SupportContainer {
    private final SupportType type;
    private final Supplier<TrainingSupport> support;
    private final int level;

    public TrainingItem(SupportType type, Supplier<TrainingSupport> support, int level, Properties p) {
        super(p);
        this.type = type;
        this.support = support;
        this.level = level;
    }

    @Override
    public InteractionResult use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide())
            return super.use(pLevel, pPlayer, pUsedHand);
        ItemStack soul = UmapyoiAPI.getUmaSoul(pPlayer);
        ItemStack itemInHand = pPlayer.getItemInHand(pUsedHand);
        return learning(pLevel, pPlayer, soul, itemInHand);
    }

    private InteractionResult learning(Level pLevel, Player pPlayer, ItemStack soul,
                                                        ItemStack training) {
        if (soul.isEmpty()) {
            pPlayer.displayClientMessage(Component.translatable("umapyoi.no_umasoul_equiped"), true);
            return InteractionResult.FAIL;
        }

        if (UmaSoulUtils.getLearningTimes(soul) <= 0) {
            pPlayer.displayClientMessage(Component.translatable("umapyoi.learning.no_learning_time"), true);
            return InteractionResult.FAIL;
        }

        if (this.getSupport().applySupport(soul, pLevel.getRandom())) {
            UmaSoulUtils.downLearningTimes(soul);
            pPlayer.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.8F, 0.8F + pLevel.getRandom().nextFloat() * 0.4F);
            pPlayer.getCooldowns().addCooldown(training, 30);
            training.shrink(1);
            return InteractionResult.SUCCESS;
        } else {
            pPlayer.displayClientMessage(Component.translatable("umapyoi.learning.can_not_learn"), true);
            return InteractionResult.FAIL;
        }
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack pStack, Slot pSlot, ClickAction pAction, Player pPlayer) {
        if (pPlayer.level().isClientSide())
            return super.overrideStackedOnOther(pStack, pSlot, pAction, pPlayer);
        if(pAction != ClickAction.SECONDARY || pPlayer.getCooldowns().isOnCooldown(pStack)) return false;

        ItemStack soul = pSlot.getItem();
        if(this.learning(pPlayer.level(), pPlayer, soul, pStack) == InteractionResult.SUCCESS)
            return true;
        return false;
    }

    protected SupportStack getSupport() {
        return Suppliers.memoize(() -> new SupportStack(this.support.get(), this.level)).get();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        tooltipAdder.accept(this.getSupport().getDescription());
    }

    @Override
    public boolean isConsumable(Level level, ItemStack stack) {
        return true;
    }

    @Override
    public GachaRanking getSupportLevel(Level level, ItemStack stack) {
        return GachaRanking.values()[this.level - 1];
    }

    @Override
    public SupportType getSupportType(Level level, ItemStack stack) {
        return this.type;
    }

    @Override
    public List<SupportStack> getSupports(Level level, ItemStack stack) {
        return ImmutableList.of(this.getSupport());
    }

    @Override
    public Predicate<ItemStack> canSupport(Level level, ItemStack stack) {
        return soul -> true;
    }

}
