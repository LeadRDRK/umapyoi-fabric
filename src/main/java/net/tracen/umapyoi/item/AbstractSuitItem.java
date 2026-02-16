package net.tracen.umapyoi.item;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
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
import net.tracen.umapyoi.events.client.RenderingUmaSuitCallback;
import net.tracen.umapyoi.registry.umadata.Growth;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;

public abstract class AbstractSuitItem extends TrinketItem implements TrinketRenderer {
    private final UmaPlayerModel<HumanoidRenderState> baseModel;

    public UmaPlayerModel<HumanoidRenderState> getBaseModel() {
        return baseModel;
    }

    public AbstractSuitItem(Properties p) {
        super(p);
        baseModel = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? new UmaPlayerModel<>() : null;
    }

    private boolean canEquip(LivingEntity entity) {
        var compOpt = TrinketsApi.getTrinketComponent(entity);
        if (compOpt.isPresent()) {
            var comp = compOpt.get();
            var entityInventory = comp.getInventory();
            if (entityInventory.containsKey("umapyoi")) {
                var group = entityInventory.get("umapyoi");
                if (group.containsKey("uma_soul")) {
                    var inventory = group.get("uma_soul");
                    return inventory.getContainerSize() > 0 &&
                            inventory.getItem(0).getItem() instanceof UmaSoulItem;
                }
            }
        }
        return false;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (UmaSoulUtils.getGrowth(stack) == Growth.UNTRAINED) {
            return super.use(level, player, usedHand);
        }

        if (canEquip(player) && equipItem(player, stack)) {
            player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER.value(), 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        return super.use(level, player, usedHand);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntityRenderState> entityModel,
                       PoseStack poseStack, SubmitNodeCollector nodeCollector, int light, LivingEntityRenderState entityState,
                       float limbAngle, float limbDistance)
    {
        if (!(entityState instanceof HumanoidRenderState state) || state.isInvisible)
            return;

        var comp = slotReference.inventory().getComponent();
        var entity = comp.getEntity();
        var entityInventory = comp.getInventory();
        TrinketInventory inventory = null;
        if (entityInventory.containsKey("umapyoi")) {
            var group = entityInventory.get("umapyoi");
            if (group.containsKey("uma_soul")) {
                inventory = group.get("uma_soul");
            }
        }
        if (inventory == null) return;

        boolean flat_flag = false;
        boolean tanned = false;
        if (inventory.getContainerSize() > 0) {
            ItemStack stackInSlot = inventory.getItem(0);
            if (stackInSlot.isEmpty() || !(stackInSlot.getItem() instanceof UmaSoulItem))
                return;

            flat_flag = ClientUtils.isFlatUmamusume(stackInSlot);

            tanned = ClientUtils.isTannedSkin(stackInSlot);
        }

        var renderType = RenderTypes.entityTranslucent(flat_flag
                ? getFlatTexture(itemStack, tanned)
                : getTexture(itemStack, tanned));

        var pojo = ClientUtils.getModelPOJO(flat_flag ? getFlatModel(itemStack) : getModel(itemStack));
        if (baseModel.needRefresh(pojo))
            baseModel.loadModel(pojo);
        baseModel.setModelProperties(state);
        baseModel.head.visible = false;
        baseModel.tail.visible = false;
        baseModel.prepareMobModel(state, limbAngle, limbDistance);
        var callbackContext = new RenderingUmaSuitCallback.Context(entity, state, baseModel,
                poseStack, nodeCollector, light);
        if (RenderingUmaSuitCallback.Pre.invoke(callbackContext))
            return;

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

    protected abstract Identifier getModel(ItemStack stack);

    protected abstract Identifier getTexture(ItemStack stack, boolean tanned);

    protected abstract Identifier getFlatModel(ItemStack stack);

    protected abstract Identifier getFlatTexture(ItemStack stack, boolean tanned);
}
