package net.tracen.umapyoi.item;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.client.model.UmaPlayerModel;
import net.tracen.umapyoi.utils.ClientUtils;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import eu.pb4.trinkets.api.callback.TrinketCallback;
import eu.pb4.trinkets.impl.TrinketUtilities;

public abstract class AbstractSuitItem extends Item implements TrinketCallback {
    public AbstractSuitItem(Properties p) {
        super(p);
    }

    private boolean canEquip(LivingEntity entity) {
        var comp = TrinketsApi.getAttachment(entity);
        var entityInventory = comp.getInventory();
        if (entityInventory.containsKey("umapyoi")) {
            var group = entityInventory.get("umapyoi");
            if (group.containsKey("uma_soul")) {
                var inventory = group.get("uma_soul");
                return inventory.getContainerSize() > 0
                        && inventory.getItem(0).getItem() instanceof UmaSoulItem;
            }
        }
        return false;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (canEquip(player) && TrinketUtilities.swapWithEquipmentSlot(stack, player) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public Holder<SoundEvent> getEquipSound(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity) {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    public void extractRenderState(ItemStack soul, ItemStack suit, LivingEntityRenderState state) {
        var isFlat = ClientUtils.isFlatUmamusume(soul);
        var pojo = ClientUtils.getModelPOJO(isFlat ? getFlatModel(suit) : getModel(suit));

        var model = state.umapyoi$getSuitModel();
        if (model == null) {
            model = new UmaPlayerModel<>();
            state.umapyoi$setSuitModel(model);
        }
        if (model.needRefresh(pojo)) {
            model.loadModel(pojo);

            var isTanned = ClientUtils.isTannedSkin(soul);
            state.umapyoi$setSuitTexture(isFlat
                    ? getFlatTexture(suit, isTanned)
                    : getTexture(suit, isTanned));
        }
    }

    public abstract Identifier getModel(ItemStack stack);

    public abstract Identifier getTexture(ItemStack stack, boolean tanned);

    public abstract Identifier getFlatModel(ItemStack stack);

    public abstract Identifier getFlatTexture(ItemStack stack, boolean tanned);
}
