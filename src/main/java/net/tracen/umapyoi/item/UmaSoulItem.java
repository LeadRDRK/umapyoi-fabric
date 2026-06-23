package net.tracen.umapyoi.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.client.model.UmaPlayerModel;
import net.tracen.umapyoi.client.renderer.BedrockModelRenderer;
import net.tracen.umapyoi.compat.FPMCompat;
import net.tracen.umapyoi.data.tag.UmapyoiUmaDataTags;
import net.tracen.umapyoi.effect.MobEffectRegistry;
import net.tracen.umapyoi.events.ApplyUmasoulAttributeCallback;
import net.tracen.umapyoi.events.ResumeActionPointCallback;
import net.tracen.umapyoi.events.SettingPropertyCallback;
import net.tracen.umapyoi.events.client.RenderingUmaSoulCallback;
import net.tracen.umapyoi.registry.UmapyoiAttributesRegistry;
import net.tracen.umapyoi.registry.umadata.Growth;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.registry.umadata.UmaDataBasicStatus;
import net.tracen.umapyoi.utils.Aptitude;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.ResultRankingUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils;
import net.tracen.umapyoi.utils.UmaStatusUtils.StatusType;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;

public class UmaSoulItem extends TrinketItem implements TrinketRenderer, CreativeModeTabFiller {
    private static final Comparator<Holder.Reference<UmaData>> COMPARATOR = new UmaDataComparator();

    public UmaSoulItem(Properties p) {
        super(p);
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
        var name = Component.translatable(Util.makeDescriptionId("umadata", UmaSoulUtils.getName(pStack)));
        GachaRanking ranking = GachaRanking.getGachaRanking(pStack);
        if(ranking == GachaRanking.EASTER_EGG) return name.withStyle(ChatFormatting.GREEN);
        return name;
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
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        int ranking = ResultRankingUtils.getRanking(stack);
        if(UmaSoulUtils.getGrowth(stack) == Growth.TRAINED && UmaSoulUtils.getPhysique(stack) <= 0)
            tooltipAdder.accept(Component.translatable("tooltip.umapyoi.uma_soul.should_retire", UmaStatusUtils.getStatusLevel(ranking))
                    .withStyle(ChatFormatting.GRAY));

        if(UmaSoulUtils.getGrowth(stack) == Growth.RETIRED)
            tooltipAdder.accept(Component.translatable("tooltip.umapyoi.uma_soul.ranking", UmaStatusUtils.getStatusLevel(ranking))
                    .withStyle(ChatFormatting.GOLD));
        if (Minecraft.getInstance().hasShiftDown() || !Umapyoi.CONFIG.TOOLTIP_SWITCH) {
            tooltipAdder.accept(
                    Component.translatable("tooltip.umapyoi.uma_soul.soul_details").withStyle(ChatFormatting.AQUA));
            UmaDataBasicStatus property = UmaSoulUtils.getProperty(stack);
            UmaDataBasicStatus maxProperty = UmaSoulUtils.getMaxProperty(stack);

            tooltipAdder.accept(Component.translatable("tooltip.umapyoi.uma_soul.speed_details",
                            UmaStatusUtils.getStatusLevel(property.speed()),
                            UmaStatusUtils.getStatusLevel(maxProperty.speed()))
                    .withStyle(ChatFormatting.DARK_GREEN));
            tooltipAdder.accept(Component.translatable("tooltip.umapyoi.uma_soul.stamina_details",
                            UmaStatusUtils.getStatusLevel(property.stamina()),
                            UmaStatusUtils.getStatusLevel(maxProperty.stamina()))
                    .withStyle(ChatFormatting.DARK_GREEN));
            tooltipAdder.accept(Component.translatable("tooltip.umapyoi.uma_soul.strength_details",
                            UmaStatusUtils.getStatusLevel(property.strength()),
                            UmaStatusUtils.getStatusLevel(maxProperty.strength()))
                    .withStyle(ChatFormatting.DARK_GREEN));
            tooltipAdder.accept(Component.translatable("tooltip.umapyoi.uma_soul.guts_details",
                            UmaStatusUtils.getStatusLevel(property.guts()),
                            UmaStatusUtils.getStatusLevel(maxProperty.guts()))
                    .withStyle(ChatFormatting.DARK_GREEN));
            tooltipAdder.accept(Component.translatable("tooltip.umapyoi.uma_soul.wisdom_details",
                            UmaStatusUtils.getStatusLevel(property.wisdom()),
                            UmaStatusUtils.getStatusLevel(maxProperty.wisdom()))
                    .withStyle(ChatFormatting.DARK_GREEN));
            tooltipAdder.accept(Component.literal(""));

            tooltipAdder.accept(Component.translatable("tooltip.umapyoi.uma_soul.aptitude.details").withStyle(ChatFormatting.AQUA));
            List<Aptitude> surfaceAptitudes = UmaSoulUtils.getSurfaceAptitude(stack);
            tooltipAdder.accept(
                    Component.translatable("tooltip.umapyoi.uma_soul.aptitude.turf", surfaceAptitudes.get(0).styledComponent()).withStyle(ChatFormatting.GREEN)
                            .append(" / ").withStyle(ChatFormatting.RESET)
                            .append(Component.translatable("tooltip.umapyoi.uma_soul.aptitude.dirt", surfaceAptitudes.get(1).styledComponent()).withStyle(ChatFormatting.GOLD))
                            .append(" / ").withStyle(ChatFormatting.RESET)
                            .append(Component.translatable("tooltip.umapyoi.uma_soul.aptitude.synthetic", surfaceAptitudes.get(2).styledComponent()).withStyle(ChatFormatting.GOLD))
            );
            List<Aptitude> distanceAptitudes = UmaSoulUtils.getDistanceAptitude(stack);
            tooltipAdder.accept(
                    Component.translatable("tooltip.umapyoi.uma_soul.aptitude.short", distanceAptitudes.get(0).styledComponent())
                            .append(" / ")
                            .append(Component.translatable("tooltip.umapyoi.uma_soul.aptitude.miles", distanceAptitudes.get(1).styledComponent())
                                    .append(" / ")
                                    .append(Component.translatable("tooltip.umapyoi.uma_soul.aptitude.medium", distanceAptitudes.get(2).styledComponent()))
                                    .append(" / ")
                                    .append(Component.translatable("tooltip.umapyoi.uma_soul.aptitude.long", distanceAptitudes.get(3).styledComponent())))
            );
            tooltipAdder.accept(
                    Component.translatable("tooltip.umapyoi.uma_soul.aptitude.strategy", Component.translatable("tooltip.umapyoi.uma_soul.aptitude.strategy." + UmaSoulUtils.getPosition(stack).name().toLowerCase()))
            );
        } else {
            tooltipAdder.accept(Component.translatable("tooltip.umapyoi.press_shift_for_details")
                    .withStyle(ChatFormatting.AQUA));
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (UmaSoulUtils.getGrowth(stack) == Growth.UNTRAINED) {
            return super.use(level, player, usedHand);
        }

        if (equipItem(player, stack)) {
            player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER.value(), 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity user, ResourceLocation slotIdentifier) {
        Multimap<Holder<Attribute>, AttributeModifier> atts = LinkedHashMultimap.create();
        SlotAttributes.addSlotModifier(atts, "umapyoi/uma_suit", slotIdentifier, 1.0, AttributeModifier.Operation.ADD_VALUE);
        if (UmaSoulUtils.getGrowth(stack) == Growth.UNTRAINED)
            return atts;

        boolean hasFatique = user.hasEffect(MobEffectRegistry.SLOW_METABOLISM.getHolder());

        atts.put(UmapyoiAttributesRegistry.SPRINT_SPEED,
                new AttributeModifier(slotIdentifier,
                        hasFatique ? 0 : getExactProperty(stack, user, StatusType.SPEED, Umapyoi.CONFIG.UMASOUL_MAX_SPEED),
                        Umapyoi.CONFIG.UMASOUL_SPEED_PRECENT_ENABLE ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                : AttributeModifier.Operation.ADD_VALUE));

        atts.put(Attributes.WATER_MOVEMENT_EFFICIENCY,
                new AttributeModifier(slotIdentifier,
                        hasFatique ? 0 : getExactProperty(stack, user, StatusType.SPEED, Umapyoi.CONFIG.UMASOUL_MAX_SPEED),
                        Umapyoi.CONFIG.UMASOUL_SPEED_PRECENT_ENABLE ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                : AttributeModifier.Operation.ADD_VALUE));

        atts.put(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(slotIdentifier,
                        getExactProperty(stack, user, StatusType.STRENGTH, Umapyoi.CONFIG.UMASOUL_MAX_STRENGTH_ATTACK),
                        Umapyoi.CONFIG.UMASOUL_STRENGTH_PRECENT_ENABLE ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                : AttributeModifier.Operation.ADD_VALUE));

        atts.put(Attributes.MAX_HEALTH,
                new AttributeModifier(slotIdentifier,
                        getExactProperty(stack, user, StatusType.STAMINA, Umapyoi.CONFIG.UMASOUL_MAX_STAMINA_HEALTH) * (hasFatique ? 1.05 : 1),
                        Umapyoi.CONFIG.UMASOUL_STAMINA_PRECENT_ENABLE ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                : AttributeModifier.Operation.ADD_VALUE));

        atts.put(Attributes.ARMOR,
                new AttributeModifier(slotIdentifier,
                        getExactProperty(stack, user, StatusType.GUTS, Umapyoi.CONFIG.UMASOUL_MAX_GUTS_ARMOR) * (hasFatique ? 1.05 : 1),
                        Umapyoi.CONFIG.UMASOUL_GUTS_PRECENT_ENABLE ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                : AttributeModifier.Operation.ADD_VALUE));

        atts.put(Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(slotIdentifier,
                        getExactProperty(stack, user, StatusType.GUTS, Umapyoi.CONFIG.UMASOUL_MAX_GUTS_ARMOR_TOUGHNESS),
                        Umapyoi.CONFIG.UMASOUL_GUTS_PRECENT_ENABLE ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                : AttributeModifier.Operation.ADD_VALUE));

        var event = new ApplyUmasoulAttributeCallback.Context(user, stack, slot, slotIdentifier, atts);
        ApplyUmasoulAttributeCallback.invoke(event);
        return event.getAttributes();
    }

    public static double getExactProperty(ItemStack stack, LivingEntity user, StatusType status, double limit) {
        var retiredValue = UmaSoulUtils.getGrowth(stack) == Growth.RETIRED ? 1.0D : 0.25D;
        var propertyRate = 1.0D + (UmaSoulUtils.getPropertyRate(stack).get(status) / 100.0D);
        var totalProperty = propertyPercentage(stack, status);
        var event = new SettingPropertyCallback.Context(user, stack, retiredValue, propertyRate, totalProperty, status);
        SettingPropertyCallback.invoke(event);
        return event.getResultProperty() * limit;
    }

    public static double propertyPercentageByValue(int x) {
        var statLimit = Umapyoi.CONFIG.STAT_LIMIT_VALUE;
        var denominator = 1 + Math.pow(Math.E,
                (x > statLimit ? (-0.125 * Umapyoi.CONFIG.STAT_LIMIT_REDUCTION_RATE) : -0.125) *
                        (x - statLimit));
        return 1 / denominator;
    }

    private static double propertyPercentage(ItemStack stack, StatusType status) {
        var x = UmaSoulUtils.getProperty(stack).get(status);
        return propertyPercentageByValue(x);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (stack.isEmpty()) return;

        Level level = entity.level();
        if (!level.isClientSide()) {
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
    public void render(ItemStack itemStack, SlotReference slotReference, EntityModel<? extends LivingEntityRenderState> entityModel,
                       PoseStack poseStack, SubmitNodeCollector nodeCollector, int light, LivingEntityRenderState entityState,
                       float limbAngle, float limbDistance)
    {
        // match AvatarRenderState directly (disallow ArmorStandRenderState)
        if (!(entityState instanceof AvatarRenderState state) || (state.isInvisible && !state.isSpectator))
            return;

        var baseModel = state.umapyoi$getUmaModel();
        if (baseModel == null) return;

        var comp = slotReference.inventory().getComponent();
        var entity = comp.getEntity();

        var renderType = RenderType.entityTranslucent(state.umapyoi$getUmaTexture());
        baseModel.setModelProperties(state);
        baseModel.prepareMobModel(state, limbAngle, limbDistance);

        var callbackContext = new RenderingUmaSoulCallback.Context(entity, state, baseModel,
                poseStack, nodeCollector, light);
        if (RenderingUmaSoulCallback.Pre.invoke(callbackContext))
            return;
        FPMCompat.hideHeadIfRendering(state, baseModel);

        if (entityModel instanceof HumanoidModel<?> humanoidModel) {
            baseModel.copyAnim(baseModel.head, humanoidModel.head);
            baseModel.copyAnim(baseModel.body, humanoidModel.body);
            baseModel.copyAnim(baseModel.leftArm, humanoidModel.leftArm);
            baseModel.copyAnim(baseModel.leftLeg, humanoidModel.leftLeg);
            baseModel.copyAnim(baseModel.rightArm, humanoidModel.rightArm);
            baseModel.copyAnim(baseModel.rightLeg, humanoidModel.rightLeg);
        }
        baseModel.setupAnim(state);
        var modelRenderer = new BedrockModelRenderer(baseModel, light,
                LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1);
        nodeCollector.submitCustomGeometry(poseStack, renderType, modelRenderer);
        if (baseModel.isEmissive()) {
            var emissiveRenderType = RenderType.entityTranslucentEmissive(state.umapyoi$getUmaEmissiveTexture());
            var emissiveRenderer = new BedrockModelRenderer(baseModel, light,
                    LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1, true);
            nodeCollector.order(1)
                    .submitCustomGeometry(poseStack, emissiveRenderType, emissiveRenderer);
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
                                .get(ResourceKey.create(UmaData.REGISTRY_KEY, UmaSoulUtils.getName(stack)))
                                .map(uma -> uma.is(UmapyoiUmaDataTags.ALTER_MODEL))
                                .orElse(false);
                    }
                }
            }
        }

        ResourceLocation renderTarget = suit_flag ? getSuitTarget(stack, alter_flag) : UmaSoulUtils.getName(stack);
        return renderTarget;
    }

    public static ResourceLocation getSuitTarget(ItemStack stack, boolean alter) {
        ResourceLocation identifier = ClientUtils.getClientUmaDataRegistry().get(UmaSoulUtils.getName(stack)).get().value().identifier();
        if(alter)
            identifier = ResourceLocation.fromNamespaceAndPath(identifier.getNamespace(), identifier.getPath()+"_alter");
        return identifier;
    }

    public static void registerRenderer() {
        Item item = ItemRegistry.UMA_SOUL;
        TrinketRendererRegistry.registerRenderer(item, (TrinketRenderer) item);
    }

    public static void extractRenderState(ItemStack soul, LivingEntity entity,
                                          LivingEntityRenderState state) {
        if (!soul.isEmpty()) {
            ResourceLocation renderTarget = UmaSoulItem.getRenderTarget(soul, entity);
            var pojo = ClientUtils.getModelPOJO(renderTarget);
            var model = state.umapyoi$getUmaModel();
            if (model == null) {
                model = new UmaPlayerModel<>();
                state.umapyoi$setUmaModel(model);
            }
            if (model.needRefresh(pojo)) {
                model.loadModel(pojo);

                state.umapyoi$setUmaTexture(ClientUtils.getTexture(renderTarget));
                state.umapyoi$setUmaEmissiveTexture(ClientUtils.getEmissiveTexture(renderTarget));
            }
        }
        else {
            state.umapyoi$setUmaModel(null);
            state.umapyoi$setUmaTexture(null);
            state.umapyoi$setUmaEmissiveTexture(null);
        }
    }

    private static class UmaDataComparator implements Comparator<Holder.Reference<UmaData>> {
        @Override
        public int compare(Holder.Reference<UmaData> left, Holder.Reference<UmaData> right) {
            var leftRanking = left.value().ranking();
            var rightRanking = right.value().ranking();
            if(leftRanking == rightRanking) {
                String leftName = left.key().location().toString();
                String rightName = right.key().location().toString();
                return leftName.compareToIgnoreCase(rightName);
            }
            return leftRanking.compareTo(rightRanking);
        }
    }
}
