package net.tracen.umapyoi.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.tracen.umapyoi.block.entity.AbstractPedestalBlockEntity;

public abstract class AbstractPedestalBlock extends BaseEntityBlock
{
    public AbstractPedestalBlock(Properties properties) {
        super(properties);
    }

    protected InteractionResult interactBEWithoutItem(Level level, BlockPos pos, Player player, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return InteractionResult.PASS;
        }
        else {
            if (!player.getInventory().add(itemStack)) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                        itemStack
                );
            }

            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP,
                    SoundSource.BLOCKS, 0.25F, 0.5F
            );
            // Server needs consume
            return InteractionResult.CONSUME;
        }
    }

    protected InteractionResult interactBEWithItem(ItemStack stack, Level level, BlockPos pos, Player player, InteractionHand hand, AbstractPedestalBlockEntity blockEntity, boolean checkBook) {
        // Wtf why useItemOn can use EMPTY item???
        // sbmj
        if (stack.isEmpty()) {
            return interactBEWithoutItem(level, pos, player, blockEntity.removeItem());
        }
        if (blockEntity.isEmpty()) {
            if (hand == InteractionHand.MAIN_HAND && !player.getOffhandItem().isEmpty() && stack.getItem() instanceof BlockItem) {
                return InteractionResult.PASS;
            }

            if (checkBook && stack.is(Items.BOOK)) {
                if (player instanceof ServerPlayer serverPlayer) {
                    // need to trigger manually because block will be destroyed
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                transformOnBook(level, pos);
                return InteractionResult.CONSUME;
            }
            else if (blockEntity.addItem(player.getAbilities().instabuild ? stack.copy() : stack)) {
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.END_PORTAL_FRAME_FILL,
                        SoundSource.BLOCKS, 1.0F, 0.8F
                );
                return InteractionResult.CONSUME;
            }
            return InteractionResult.FAIL;
        }
        else {
            player.displayClientMessage(Component.translatable("umapyoi.uma_pedestal.cannot_add_item"), true);
            return InteractionResult.PASS;
        }
    }

    protected abstract void transformOnBook(Level level, BlockPos pos);
}