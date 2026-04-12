package net.tracen.umapyoi.item;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
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
import net.tracen.umapyoi.client.renderer.BedrockModelRenderer;
import net.tracen.umapyoi.compat.FPMCompat;
import net.tracen.umapyoi.events.client.RenderingUmaSuitCallback;
import net.tracen.umapyoi.utils.ClientUtils;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import eu.pb4.trinkets.api.callback.TrinketCallback;
import eu.pb4.trinkets.api.client.TrinketRenderer;
import eu.pb4.trinkets.api.client.TrinketRendererRegistry;
import eu.pb4.trinkets.impl.TrinketUtilities;

public abstract class AbstractSuitItem extends Item implements TrinketCallback, TrinketRenderer {
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

    @Override
    @Environment(EnvType.CLIENT)
    public void submit(
            ItemStack itemStack, TrinketSlotAccess slotAccess, EntityModel<? extends LivingEntityRenderState> entityModel,
            PoseStack poseStack, SubmitNodeCollector nodeCollector, int light, LivingEntityRenderState entityState,
            float limbAngle, float limbDistance
    ) {
        if (!(entityState instanceof HumanoidRenderState state) || state.isInvisible)
            return;

        var baseModel = state.umapyoi$getSuitModel();
        if (baseModel == null) return;

        var renderType = RenderTypes.entityTranslucent(state.umapyoi$getSuitTexture());
        baseModel.setModelProperties(state);
        baseModel.head.visible = false;
        baseModel.tail.visible = false;
        baseModel.prepareMobModel(state, limbAngle, limbDistance);
        var callbackContext = new RenderingUmaSuitCallback.Context(slotAccess, state, baseModel,
                poseStack, nodeCollector, light);
        if (RenderingUmaSuitCallback.Pre.invoke(callbackContext))
            return;
        FPMCompat.hideHeadIfRendering(state, baseModel);

        if (entityModel instanceof HumanoidModel) {
            @SuppressWarnings("unchecked")
            var model = (HumanoidModel<HumanoidRenderState>)entityModel;

            baseModel.copyAnim(baseModel.head, model.head);
            baseModel.copyAnim(baseModel.body, model.body);
            baseModel.copyAnim(baseModel.leftArm, model.leftArm);
            baseModel.copyAnim(baseModel.leftLeg, model.leftLeg);
            baseModel.copyAnim(baseModel.rightArm, model.rightArm);
            baseModel.copyAnim(baseModel.rightLeg, model.rightLeg);
        }
        baseModel.setupAnim(state);

        var modelRenderer = new BedrockModelRenderer(baseModel, light,
                LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1);
        nodeCollector.submitCustomGeometry(poseStack, renderType, modelRenderer);
        RenderingUmaSuitCallback.Post.invoke(callbackContext);
    }

    public static void registerRenderer(Item item) {
        TrinketRendererRegistry.registerRenderer(item, (TrinketRenderer) item);
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
