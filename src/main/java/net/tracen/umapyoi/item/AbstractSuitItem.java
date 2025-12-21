package net.tracen.umapyoi.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.client.model.UmaPlayerModel;
import net.tracen.umapyoi.data.tag.UmapyoiUmaDataTags;
import net.tracen.umapyoi.events.client.RenderingUmaSuitCallback;
import net.tracen.umapyoi.registry.umadata.Growth;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;

public abstract class AbstractSuitItem extends TrinketItem implements TrinketRenderer {
    private final UmaPlayerModel<LivingEntity> baseModel;

    public AbstractSuitItem() {
        super(Umapyoi.defaultItemProperties().stacksTo(1));
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (UmaSoulUtils.getGrowth(stack) == Growth.UNTRAINED) {
            return super.use(level, player, usedHand);
        }

        if (canEquip(player) && equipItem(player, stack)) {
            player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
            return InteractionResultHolder.success(stack);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel,
                       PoseStack poseStack, MultiBufferSource multiBufferSource, int light, LivingEntity entity,
                       float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw,
                       float headPitch)
    {
        if (entity.isInvisible())
            return;

        TrinketsApi.getTrinketComponent(entity).ifPresent(comp -> {
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

                flat_flag = ClientUtils.getClientUmaDataRegistry()
                        .getHolder(ResourceKey.create(UmaData.REGISTRY_KEY, UmaSoulUtils.getName(stackInSlot)))
                        .get().is(UmapyoiUmaDataTags.FLAT_CHEST);

                tanned = ClientUtils.getClientUmaDataRegistry()
                        .getHolder(ResourceKey.create(UmaData.REGISTRY_KEY, UmaSoulUtils.getName(stackInSlot)))
                        .get().is(UmapyoiUmaDataTags.TANNED_SKIN);
            }

            VertexConsumer vertexconsumer = multiBufferSource.getBuffer(
                    RenderType.entityTranslucentCull(flat_flag ? getFlatTexture(tanned) : getTexture(tanned)));

            var pojo = ClientUtils.getModelPOJO(flat_flag ? getFlatModel() : getModel());
            if (baseModel.needRefresh(pojo))
                baseModel.loadModel(pojo);
            if (RenderingUmaSuitCallback.Pre.invoke(entity, baseModel, tickDelta,
                    poseStack, multiBufferSource, light))
                return;
            baseModel.setModelProperties(entity);
            baseModel.head.visible = false;
            baseModel.tail.visible = false;
            baseModel.hat.visible = false;
            baseModel.prepareMobModel(entity, limbAngle, limbDistance, tickDelta);

            if (entityModel instanceof HumanoidModel) {
                @SuppressWarnings("unchecked")
                var model = (HumanoidModel<LivingEntity>)entityModel;

                baseModel.copyAnim(baseModel.head, model.head);
                baseModel.copyAnim(baseModel.body, model.body);
                baseModel.copyAnim(baseModel.leftArm, model.leftArm);
                baseModel.copyAnim(baseModel.leftLeg, model.leftLeg);
                baseModel.copyAnim(baseModel.rightArm, model.rightArm);
                baseModel.copyAnim(baseModel.rightLeg, model.rightLeg);
            }

            baseModel.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

            baseModel.renderToBuffer(poseStack, vertexconsumer, light,
                    LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1, 1, 1, 1);
            RenderingUmaSuitCallback.Post.invoke(entity, baseModel, tickDelta,
                    poseStack, multiBufferSource, light);
        });
    }

    public static void registerRenderer(Item item) {
        TrinketRendererRegistry.registerRenderer(item, (TrinketRenderer) item);
    }

    protected abstract ResourceLocation getModel();

    protected abstract ResourceLocation getTexture(boolean tanned);

    protected abstract ResourceLocation getFlatModel();

    protected abstract ResourceLocation getFlatTexture(boolean tanned);
}
