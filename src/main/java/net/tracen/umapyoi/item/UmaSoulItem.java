package net.tracen.umapyoi.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.attributes.ExtraAttributes;
import net.tracen.umapyoi.client.EmissiveRenderType;
import net.tracen.umapyoi.client.model.UmaPlayerModel;
import net.tracen.umapyoi.events.ResumeActionPointCallback;
import net.tracen.umapyoi.events.client.RenderingUmaSoulCallback;
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
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;

public class UmaSoulItem extends TrinketItem implements TrinketRenderer, CreativeModeTabFiller {
    private static final Comparator<Entry<ResourceKey<UmaData>, UmaData>> COMPARATOR = new UmaDataComparator();

    private final UmaPlayerModel<LivingEntity> baseModel;

    public UmaSoulItem() {
        super(Umapyoi.defaultItemProperties().stacksTo(1));
        baseModel = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? new UmaPlayerModel<>() : null;
    }

    public static Stream<Entry<ResourceKey<UmaData>, UmaData>> sortedUmaDataList() {
        return ClientUtils.getClientUmaDataRegistry().entrySet().stream().sorted(UmaSoulItem.COMPARATOR);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void fillItemCategory(CreativeModeTab.Output entries) {
        sortedUmaDataList().forEach(entry -> {
            var initUmaSoul = UmaSoulUtils.initUmaSoul(getDefaultInstance(),
                    entry.getKey().location(),
                    entry.getValue());
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
    
    @Override
    public boolean isBarVisible(ItemStack pStack) {
        var physique = UmaSoulUtils.getPhysique(pStack);
        return UmaSoulUtils.getGrowth(pStack) != Growth.RETIRED && physique != 5;
    }
    
    @Override
    public int getBarWidth(ItemStack pStack) {
        var physique = UmaSoulUtils.getPhysique(pStack);
        return Math.round(13.0F - (5 - physique) * 13.0F / 5);
    }
    
    @Override
    public int getBarColor(ItemStack pStack) {
        float stackMaxDamage = 5;
        var physique = UmaSoulUtils.getPhysique(pStack);
        float f = Math.max(0.0F, physique / stackMaxDamage);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }


    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        int ranking = ResultRankingUtils.getRanking(stack);
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

        atts.put(Attributes.MOVEMENT_SPEED,
                new AttributeModifier(uuid, "speed_running_bonus",
                        getExactProperty(stack, StatusType.SPEED.getId(), Umapyoi.CONFIG.UMASOUL_MAX_SPEED()),
                        Umapyoi.CONFIG.UMASOUL_SPEED_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                                : AttributeModifier.Operation.ADDITION));

        atts.put(ExtraAttributes.SWIM_SPEED,
                new AttributeModifier(uuid, "speed_swiming_bonus",
                        getExactProperty(stack, StatusType.SPEED.getId(), Umapyoi.CONFIG.UMASOUL_MAX_SPEED()),
                        Umapyoi.CONFIG.UMASOUL_SPEED_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                                : AttributeModifier.Operation.ADDITION));

        atts.put(ExtraAttributes.STEP_HEIGHT_ADDITION,
                new AttributeModifier(uuid, "speed_step_bonus",
                        getExactProperty(stack, StatusType.SPEED.getId(), Umapyoi.CONFIG.UMASOUL_MAX_SPEED()),
                        Umapyoi.CONFIG.UMASOUL_SPEED_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                                : AttributeModifier.Operation.ADDITION));

        atts.put(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(uuid, "strength_attack_bonus",
                        getExactProperty(stack, StatusType.STRENGTH.getId(), Umapyoi.CONFIG.UMASOUL_MAX_STRENGTH_ATTACK()),
                        Umapyoi.CONFIG.UMASOUL_STRENGTH_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                                : AttributeModifier.Operation.ADDITION));
        atts.put(Attributes.MAX_HEALTH,
                new AttributeModifier(uuid, "strength_attack_bonus",
                        getExactProperty(stack, StatusType.STAMINA.getId(), Umapyoi.CONFIG.UMASOUL_MAX_STAMINA_HEALTH()),
                        Umapyoi.CONFIG.UMASOUL_STAMINA_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                                : AttributeModifier.Operation.ADDITION));
        atts.put(Attributes.ARMOR,
                new AttributeModifier(uuid, "guts_armor_bonus",
                        getExactProperty(stack, StatusType.GUTS.getId(), Umapyoi.CONFIG.UMASOUL_MAX_GUTS_ARMOR()),
                        Umapyoi.CONFIG.UMASOUL_GUTS_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                                : AttributeModifier.Operation.ADDITION));
        atts.put(Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(uuid, "guts_armor_toughness_bonus",
                        getExactProperty(stack, StatusType.GUTS.getId(), Umapyoi.CONFIG.UMASOUL_MAX_GUTS_ARMOR_TOUGHNESS()),
                        Umapyoi.CONFIG.UMASOUL_GUTS_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                                : AttributeModifier.Operation.ADDITION));

        return atts;
    }

    public double getExactProperty(ItemStack stack, int num, double limit) {
        var retiredValue = UmaSoulUtils.getGrowth(stack) == Growth.RETIRED ? 1.0D : 0.25D;
        var propertyRate = 1.0D + (UmaSoulUtils.getPropertyRate(stack)[num] / 100.0D);
        var totalProperty = propertyPercentage(stack, num);
        return UmaSoulUtils.getMotivation(stack).getMultiplier() * limit * propertyRate * retiredValue * totalProperty;
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
        if (!commandSenderWorld.isClientSide && entity instanceof Player player) {
            applyStaminaEffect(stack, player);
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

    private void applyStaminaEffect(ItemStack stack, Player player) {
        FoodData foodData = player.getFoodData();
        boolean isPlayerHealingWithSaturation = player.level().getGameRules()
                .getBoolean(GameRules.RULE_NATURAL_REGENERATION) && player.isHurt()
                && foodData.getSaturationLevel() > 0.0F && foodData.getFoodLevel() >= 20;
        if (!isPlayerHealingWithSaturation) {
            float exhaustion = foodData.getExhaustionLevel();
            float reduction = getStaminaExhaustion(
                    UmaSoulUtils.getProperty(stack)[StatusType.STRENGTH.getId()])
                    * UmaSoulUtils.getMotivation(stack).getMultiplier();
            if (exhaustion > 0.01F) {
                player.causeFoodExhaustion(-reduction);
            }
        }
    }

    public float getStaminaExhaustion(int stamina) {
        return Math.max(1, stamina) * 0.00075f;
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

        boolean suit_flag = false;
        var compOpt = TrinketsApi.getTrinketComponent(entity);
        if (compOpt.isPresent()) {
            var comp = compOpt.get();
            var entityInventory = comp.getInventory();
            if (entityInventory.containsKey("umapyoi")) {
                var group = entityInventory.get("umapyoi");
                if (group.containsKey("uma_suit")) {
                    var inventory = group.get("uma_suit");
                    if (inventory.getContainerSize() > 0 && inventory.getItem(0).getItem() instanceof AbstractSuitItem) {
                        suit_flag = true;
                    }
                }
            }
        }

        ResourceLocation renderTarget = suit_flag
                ? ClientUtils.getClientUmaDataRegistry().get(UmaSoulUtils.getName(itemStack)).getIdentifier()
                : UmaSoulUtils.getName(itemStack);
        var pojo = ClientUtils.getModelPOJO(renderTarget);
        if (baseModel.needRefresh(pojo))
            baseModel.loadModel(pojo);

        VertexConsumer vertexConsumer = multiBufferSource
                .getBuffer(RenderType.entityTranslucentCull(ClientUtils.getTexture(renderTarget)));
        baseModel.setModelProperties(entity, suit_flag, false);
        baseModel.prepareMobModel(entity, limbAngle, limbDistance, tickDelta);

        if (RenderingUmaSoulCallback.Pre.invoke(entity, baseModel, tickDelta, poseStack, multiBufferSource, light))
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
                    .getBuffer(EmissiveRenderType.emissive(ClientUtils.getEmissiveTexture(renderTarget)));
            baseModel.renderEmissiveParts(poseStack, emissiveConsumer, light,
                    LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1, 1, 1, 1);
        }

        RenderingUmaSoulCallback.Post.invoke(entity, baseModel, tickDelta, poseStack, multiBufferSource, light);
    }

    public static void registerRenderer() {
        Item item = ItemRegistry.UMA_SOUL.get();
        TrinketRendererRegistry.registerRenderer(item, (TrinketRenderer) item);
    }

    private static class UmaDataComparator implements Comparator<Entry<ResourceKey<UmaData>, UmaData>> {
        @Override
        public int compare(Entry<ResourceKey<UmaData>, UmaData> left, Entry<ResourceKey<UmaData>, UmaData> right) {
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
