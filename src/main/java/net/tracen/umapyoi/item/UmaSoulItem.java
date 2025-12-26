package net.tracen.umapyoi.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.client.model.UmaPlayerModel;
import net.tracen.umapyoi.data.tag.UmapyoiUmaDataTags;
import net.tracen.umapyoi.events.ApplyUmasoulAttributeCallback;
import net.tracen.umapyoi.events.ResumeActionPointCallback;
import net.tracen.umapyoi.events.SettingPropertyCallback;
import net.tracen.umapyoi.events.client.RenderingUmaSoulCallback;
import net.tracen.umapyoi.registry.UmapyoiAttributesRegistry;
import net.tracen.umapyoi.registry.umadata.Growth;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.ResultRankingUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils.StatusType;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;

public class UmaSoulItem extends TrinketItem implements TrinketRenderer, CreativeModeTabFiller, Equipable {
    private static final Comparator<Holder.Reference<UmaData>> COMPARATOR = new UmaDataComparator();

    private final UmaPlayerModel<LivingEntity> baseModel;

    public UmaSoulItem() {
        super(Umapyoi.defaultItemProperties().stacksTo(1));
        baseModel = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? new UmaPlayerModel<>() : null;
    }

    public static Stream<Holder.Reference<UmaData>> sortedUmaDataList(HolderLookup.Provider provider) {
        return UmapyoiAPI.getUmaDataRegistry(provider).listElements().sorted(UmaSoulItem.COMPARATOR);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void fillItemCategory(FabricItemGroupEntries entries) {
        sortedUmaDataList(entries.getContext().holders()).forEach(entry -> {
            var initUmaSoul = UmaSoulUtils.initUmaSoul(getDefaultInstance(),
                    entry.key().location(),
                    entry.value());
            UmaSoulUtils.setPhysique(initUmaSoul, 5);
            entries.accept(initUmaSoul);
        });
    }
    
    @Override
    public Component getName(ItemStack pStack) {
        GachaRanking ranking = GachaRanking.getGachaRanking(pStack);
        if(ranking == GachaRanking.EASTER_EGG) return super.getName(pStack).copy().withStyle(ChatFormatting.GREEN);
        return super.getName(pStack);
    }

    @Override
    public Rarity getRarity(ItemStack pStack) {
        GachaRanking ranking = GachaRanking.getGachaRanking(pStack);
        return ranking == GachaRanking.SSR || ranking == GachaRanking.EASTER_EGG ? Rarity.EPIC : ranking == GachaRanking.SR ? Rarity.UNCOMMON : Rarity.COMMON;
    }

    @Override
    public String getDescriptionId(ItemStack pStack) {
        return Util.makeDescriptionId("umadata", UmaSoulUtils.getName(pStack));
    }
    
    @Override
    public boolean isFoil(ItemStack pStack) {
        return UmaSoulUtils.getGrowth(pStack) == Growth.RETIRED;
    }

//    TODO: Temporarily removed until a better version is released or the durability bar is completely phased out.。
//    @Override
//    public boolean isBarVisible(ItemStack pStack) {
//        var physique = UmaSoulUtils.getPhysique(pStack);
//        return UmaSoulUtils.getGrowth(pStack) != Growth.RETIRED && physique != 5;
//    }
//
//    @Override
//    public int getBarWidth(ItemStack pStack) {
//        var physique = UmaSoulUtils.getPhysique(pStack);
//        return Math.round(13.0F - (5 - physique) * 13.0F / 5);
//    }
//
//    @Override
//    public int getBarColor(ItemStack pStack) {
//        float stackMaxDamage = 5;
//        var physique = UmaSoulUtils.getPhysique(pStack);
//        float f = Math.max(0.0F, physique / stackMaxDamage);
//        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
//    }


    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        int ranking = ResultRankingUtils.getRanking(stack);
        if(UmaSoulUtils.getGrowth(stack) == Growth.TRAINED && UmaSoulUtils.getPhysique(stack) <= 0)
            tooltip.add(Component.translatable("tooltip.umapyoi.uma_soul.should_retire", UmaStatusUtils.getStatusLevel(ranking))
                    .withStyle(ChatFormatting.GRAY));

        if(UmaSoulUtils.getGrowth(stack) == Growth.RETIRED)
            tooltip.add(Component.translatable("tooltip.umapyoi.uma_soul.ranking", UmaStatusUtils.getStatusLevel(ranking))
                    .withStyle(ChatFormatting.GOLD));
        if (Screen.hasShiftDown() || !Umapyoi.CONFIG.TOOLTIP_SWITCH()) {
            tooltip.add(
                    Component.translatable("tooltip.umapyoi.uma_soul.soul_details").withStyle(ChatFormatting.AQUA));
            int[] property = UmaSoulUtils.getProperty(stack);
            int[] maxProperty = UmaSoulUtils.getMaxProperty(stack);

            tooltip.add(Component.translatable("tooltip.umapyoi.uma_soul.speed_details",
                    UmaStatusUtils.getStatusLevel(property[StatusType.SPEED.getId()]),
                    UmaStatusUtils.getStatusLevel(maxProperty[StatusType.SPEED.getId()]))
                            .withStyle(ChatFormatting.DARK_GREEN));
            tooltip.add(Component.translatable("tooltip.umapyoi.uma_soul.stamina_details",
                    UmaStatusUtils.getStatusLevel(property[StatusType.STAMINA.getId()]),
                    UmaStatusUtils.getStatusLevel(maxProperty[StatusType.STAMINA.getId()]))
                            .withStyle(ChatFormatting.DARK_GREEN));
            tooltip.add(Component.translatable("tooltip.umapyoi.uma_soul.strength_details",
                    UmaStatusUtils.getStatusLevel(property[StatusType.STRENGTH.getId()]),
                    UmaStatusUtils.getStatusLevel(maxProperty[StatusType.STRENGTH.getId()]))
                            .withStyle(ChatFormatting.DARK_GREEN));
            tooltip.add(Component.translatable("tooltip.umapyoi.uma_soul.guts_details",
                    UmaStatusUtils.getStatusLevel(property[StatusType.GUTS.getId()]),
                    UmaStatusUtils.getStatusLevel(maxProperty[StatusType.GUTS.getId()]))
                            .withStyle(ChatFormatting.DARK_GREEN));
            tooltip.add(Component.translatable("tooltip.umapyoi.uma_soul.wisdom_details",
                    UmaStatusUtils.getStatusLevel(property[StatusType.WISDOM.getId()]),
                    UmaStatusUtils.getStatusLevel(maxProperty[StatusType.WISDOM.getId()]))
                            .withStyle(ChatFormatting.DARK_GREEN));
        } else {
            tooltip.add(Component.translatable("tooltip.umapyoi.press_shift_for_details")
                    .withStyle(ChatFormatting.AQUA));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (UmaSoulUtils.getGrowth(stack) == Growth.UNTRAINED) {
            return super.use(level, player, usedHand);
        }

        if (equipItem(player, stack)) {
            player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
            return InteractionResultHolder.success(stack);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        SlotAttributes.addSlotModifier(atts, "umapyoi/uma_suit", uuid, 1.0, AttributeModifier.Operation.ADDITION);
        if (UmaSoulUtils.getGrowth(stack) == Growth.UNTRAINED)
            return atts;

        atts.put(UmapyoiAttributesRegistry.SPRINT_SPEED,
                new AttributeModifier(uuid, "sprint_speed_running_bonus",
                        getExactProperty(stack, entity, StatusType.SPEED, Umapyoi.CONFIG.UMASOUL_MAX_SPEED()),
                        Umapyoi.CONFIG.UMASOUL_SPEED_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                                : AttributeModifier.Operation.ADDITION));

        atts.put(UmapyoiAttributesRegistry.SWIM_SPEED,
                new AttributeModifier(uuid, "speed_swiming_bonus",
                        getExactProperty(stack, entity, StatusType.SPEED, Umapyoi.CONFIG.UMASOUL_MAX_SPEED()),
                        Umapyoi.CONFIG.UMASOUL_SPEED_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                                : AttributeModifier.Operation.ADDITION));

        atts.put(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(uuid, "strength_attack_bonus",
                        getExactProperty(stack, entity, StatusType.STRENGTH, Umapyoi.CONFIG.UMASOUL_MAX_STRENGTH_ATTACK()),
                        Umapyoi.CONFIG.UMASOUL_STRENGTH_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                                : AttributeModifier.Operation.ADDITION));

        atts.put(Attributes.MAX_HEALTH,
                new AttributeModifier(uuid, "strength_attack_bonus",
                        getExactProperty(stack, entity, StatusType.STAMINA, Umapyoi.CONFIG.UMASOUL_MAX_STAMINA_HEALTH()),
                        Umapyoi.CONFIG.UMASOUL_STAMINA_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                                : AttributeModifier.Operation.ADDITION));

        atts.put(Attributes.ARMOR,
                new AttributeModifier(uuid, "guts_armor_bonus",
                        getExactProperty(stack, entity, StatusType.GUTS, Umapyoi.CONFIG.UMASOUL_MAX_GUTS_ARMOR()),
                        Umapyoi.CONFIG.UMASOUL_GUTS_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                                : AttributeModifier.Operation.ADDITION));

        atts.put(Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(uuid, "guts_armor_toughness_bonus",
                        getExactProperty(stack, entity, StatusType.GUTS, Umapyoi.CONFIG.UMASOUL_MAX_GUTS_ARMOR_TOUGHNESS()),
                        Umapyoi.CONFIG.UMASOUL_GUTS_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                                : AttributeModifier.Operation.ADDITION));

        var event = new ApplyUmasoulAttributeCallback.Context(stack, slot, uuid, atts);
        ApplyUmasoulAttributeCallback.invoke(event);
        return event.getAttributes();
    }

    public double getExactProperty(ItemStack stack, LivingEntity user, StatusType status, double limit) {
        int num = status.getId();
        var retiredValue = UmaSoulUtils.getGrowth(stack) == Growth.RETIRED ? 1.0D : 0.25D;
        var propertyRate = 1.0D + (UmaSoulUtils.getPropertyRate(stack)[num] / 100.0D);
        var totalProperty = propertyPercentage(stack, num);
        var event = new SettingPropertyCallback.Context(user, stack, retiredValue, propertyRate, totalProperty);
        SettingPropertyCallback.invoke(event);
        return event.getResultProperty() * limit;
    }

    private double propertyPercentage(ItemStack stack, int num) {
        var x = UmaSoulUtils.getProperty(stack)[num];
        var statLimit = Umapyoi.CONFIG.STAT_LIMIT_VALUE();
        var denominator = 1 + Math.pow(Math.E,
                (x > statLimit ? (-0.125 * Umapyoi.CONFIG.STAT_LIMIT_REDUCTION_RATE()) : -0.125) *
                        (x - statLimit));
        return 1 / denominator;
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (stack.isEmpty()) return;

        Level commandSenderWorld = entity.getCommandSenderWorld();
        if (!commandSenderWorld.isClientSide()) {
            resumeActionPoint(stack, entity);
        }
    }

    private void resumeActionPoint(ItemStack stack, LivingEntity entity) {
        if (ResumeActionPointCallback.invoke(entity, stack))
            return;
        if (UmaSoulUtils.getActionPoint(stack) != UmaSoulUtils.getMaxActionPoint(stack)) {
            UmaSoulUtils.setActionPoint(stack, Math.min(UmaSoulUtils.getActionPoint(stack) + 1,
                    UmaSoulUtils.getMaxActionPoint(stack)));
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel,
                       PoseStack poseStack, MultiBufferSource multiBufferSource, int light, LivingEntity entity,
                       float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw,
                       float headPitch)
    {
        if ((entity instanceof ArmorStand) || (entity.isInvisible() && !entity.isSpectator()))
            return;

        ResourceLocation renderTarget = getRenderTarget(itemStack, entity);
        var pojo = ClientUtils.getModelPOJO(renderTarget);
        if (baseModel.needRefresh(pojo))
            baseModel.loadModel(pojo);

        VertexConsumer vertexConsumer = multiBufferSource
                .getBuffer(RenderType.entityTranslucentCull(ClientUtils.getTexture(renderTarget)));
        baseModel.setModelProperties(entity);
        baseModel.prepareMobModel(entity, limbAngle, limbDistance, tickDelta);

        var callbackContext = new RenderingUmaSoulCallback.Context(entity, baseModel, tickDelta,
                poseStack, multiBufferSource, light);
        if (RenderingUmaSoulCallback.Pre.invoke(callbackContext))
            return;

        if (entityModel instanceof HumanoidModel<?> humanoidModel) {
            baseModel.copyAnim(baseModel.head, humanoidModel.head);
            baseModel.copyAnim(baseModel.body, humanoidModel.body);
            baseModel.copyAnim(baseModel.leftArm, humanoidModel.leftArm);
            baseModel.copyAnim(baseModel.leftLeg, humanoidModel.leftLeg);
            baseModel.copyAnim(baseModel.rightArm, humanoidModel.rightArm);
            baseModel.copyAnim(baseModel.rightLeg, humanoidModel.rightLeg);
        }
        baseModel.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        baseModel.renderToBuffer(poseStack, vertexConsumer, light,
                LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1, 1, 1, 1);
        if (baseModel.isEmissive()) {
            VertexConsumer emissiveConsumer = multiBufferSource
                    .getBuffer(RenderType.entityTranslucentEmissive(ClientUtils.getEmissiveTexture(renderTarget)));
            baseModel.renderEmissiveParts(poseStack, emissiveConsumer, light,
                    LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1, 1, 1, 1);
        }

        RenderingUmaSoulCallback.Post.invoke(callbackContext);
    }

    public static ResourceLocation getRenderTarget(ItemStack stack, LivingEntity entity) {
        boolean suit_flag = false;
        boolean alter_flag = false;
        var compOpt = TrinketsApi.getTrinketComponent(entity);
        if (compOpt.isPresent()) {
            var comp = compOpt.get();
            var entityInventory = comp.getInventory();
            if (entityInventory.containsKey("umapyoi")) {
                var group = entityInventory.get("umapyoi");
                if (group.containsKey("uma_suit")) {
                    var inventory = group.get("uma_suit");
                    if (inventory.getContainerSize() > 0 && (inventory.getItem(0).getItem() instanceof AbstractSuitItem ||
                            inventory.getItem(0).getItem() instanceof UmaCostumeItem)) {
                        suit_flag = true;

                        alter_flag = ClientUtils.getClientUmaDataRegistry()
                                .getHolder(ResourceKey.create(UmaData.REGISTRY_KEY, UmaSoulUtils.getName(stack)))
                                .get().is(UmapyoiUmaDataTags.ALTER_MODEL);
                    }
                }
            }
        }

        ResourceLocation renderTarget = suit_flag ? getSuitTarget(stack, alter_flag) : UmaSoulUtils.getName(stack);
        return renderTarget;
    }

    private static ResourceLocation getSuitTarget(ItemStack stack, boolean alter) {
        ResourceLocation identifier = ClientUtils.getClientUmaDataRegistry().get(UmaSoulUtils.getName(stack)).getIdentifier();
        if(alter)
            identifier = new ResourceLocation(identifier.getNamespace(), identifier.getPath()+"_alter");
        return identifier;
    }


    public static void registerRenderer() {
        Item item = ItemRegistry.UMA_SOUL.get();
        TrinketRendererRegistry.registerRenderer(item, (TrinketRenderer) item);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.byName("uma_soul");
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    private static class UmaDataComparator implements Comparator<Holder.Reference<UmaData>> {
        @Override
        public int compare(Holder.Reference<UmaData> left, Holder.Reference<UmaData> right) {
            var leftRanking = left.value().getGachaRanking();
            var rightRanking = right.value().getGachaRanking();
            if(leftRanking == rightRanking) {
                String leftName = left.key().location().toString();
                String rightName = right.key().location().toString();
                return leftName.compareToIgnoreCase(rightName);
            }
            return leftRanking.compareTo(rightRanking);
        }
    }
}
