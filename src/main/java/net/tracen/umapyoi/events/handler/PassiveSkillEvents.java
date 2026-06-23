package net.tracen.umapyoi.events.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.data.tag.UmapyoiBlockTags;
import net.tracen.umapyoi.events.ApplyUmasoulAttributeCallback;
import net.tracen.umapyoi.events.PlayerBreakSpeedCallback;
import net.tracen.umapyoi.events.PlayerTickCallback;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.UmapyoiAttributesRegistry;
import net.tracen.umapyoi.utils.UmaSoulUtils;

public class PassiveSkillEvents {

    public static final Identifier PASSIVE_ID = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "passive_speed_bonus");
    public static final Identifier SPRINT_ID = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "sprint_speed_bonus");

    public static void testPassiveSkill_im(ApplyUmasoulAttributeCallback.Context event) {
        var soul = event.getUmaSoul();
        if (UmaSoulUtils.hasSkill(soul, UmaSkillRegistry.INQUISITIVE_MIND.getId())) {
            var speedFlag = UmaSoulUtils.getProperty(soul).speed() >= 12;
            var wisdomFlag = UmaSoulUtils.getProperty(soul).wisdom() >= 12;
            event.getAttributes().put(Attributes.ATTACK_SPEED, new AttributeModifier(PASSIVE_ID,
                    speedFlag && wisdomFlag ? 0.075D :0.05D, Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    public static float testPassiveSkill_att(Player player, float origSpeed) {
        var soul = UmapyoiAPI.getUmaSoul(player);
        if (UmaSoulUtils.hasSkill(soul, UmaSkillRegistry.DIG_SPEED.getId()))
            return origSpeed * 1.1F;
        else
            return origSpeed;
    }

    public static void sprintSpeedTick(Player living) {
        AttributeInstance sprintSpeed = living.getAttribute(UmapyoiAttributesRegistry.SPRINT_SPEED);

        if(sprintSpeed == null)
            return;

        AttributeInstance movementSpeed = living.getAttribute(Attributes.MOVEMENT_SPEED);


        var speedModifier = new AttributeModifier(SPRINT_ID,
                sprintSpeed.getValue() - sprintSpeed.getBaseValue(),
                Umapyoi.CONFIG.UMASOUL_SPEED_PRECENT_ENABLE ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        : AttributeModifier.Operation.ADD_VALUE);

        if (UmapyoiAPI.getUmaSoul(living).isEmpty()) {
            movementSpeed.removeModifier(speedModifier);
            return;
        }

        if (living.isSprinting()) {
            if (movementSpeed.hasModifier(speedModifier.id())) {
                AttributeModifier oldModifier = movementSpeed.getModifier(speedModifier.id());
                if (oldModifier != null) {
                    if (oldModifier.amount() == speedModifier.amount() && oldModifier.operation() == speedModifier.operation()) {
                        return;
                    } else {
                        movementSpeed.removeModifier(speedModifier.id());
                    }
                }
            }
            movementSpeed.addTransientModifier(speedModifier);
        } else {
            movementSpeed.removeModifier(speedModifier);
        }
    }

    public static void passiveStepHeight(Player player) {
        AttributeInstance stepHeight = player.getAttribute(UmapyoiAttributesRegistry.STEP_HEIGHT_ADDITION);
        var heightModifier = new AttributeModifier(PASSIVE_ID, 0.5D, Operation.ADD_VALUE);
        if (UmapyoiAPI.getUmaSoul(player).isEmpty()) {
            stepHeight.removeModifier(heightModifier);
            return;
        }

        if (UmaSoulUtils.hasSkill(UmapyoiAPI.getUmaSoul(player), UmaSkillRegistry.MOUNTAIN_CLIMBER.getId())) {
            if (!stepHeight.hasModifier(PASSIVE_ID))
                stepHeight.addTransientModifier(heightModifier);
        } else {
            stepHeight.removeModifier(heightModifier);
        }
    }

    public static void passiveTurfRunner(Player player) {
        AttributeInstance movementSpeed = player.getAttribute(UmapyoiAttributesRegistry.SPRINT_SPEED);

        var test_speed = new AttributeModifier(PASSIVE_ID, 0.1D, Operation.ADD_MULTIPLIED_TOTAL);
        if (UmapyoiAPI.getUmaSoul(player).isEmpty()) {
            movementSpeed.removeModifier(test_speed);
            return;
        }

        BlockPos groundPos = player.getY() % 1 < 0.5 ? player.blockPosition().below() : player.blockPosition();
        BlockState groundBlock = player.level().getBlockState(groundPos);

        if (UmaSoulUtils.hasSkill(UmapyoiAPI.getUmaSoul(player), UmaSkillRegistry.TURF_RUNNER.getId())) {
            handleMovementModifier(movementSpeed, test_speed, groundBlock, UmapyoiBlockTags.TRACK_TURF);
        }
    }

    public static void passiveDirtRunner(Player player) {
        AttributeInstance movementSpeed = player.getAttribute(UmapyoiAttributesRegistry.SPRINT_SPEED);

        var test_speed = new AttributeModifier(PASSIVE_ID, 0.1D, Operation.ADD_MULTIPLIED_TOTAL);
        if (UmapyoiAPI.getUmaSoul(player).isEmpty()) {
            movementSpeed.removeModifier(test_speed);
            return;
        }

        BlockPos groundPos = player.getY() % 1 < 0.5 ? player.blockPosition().below() : player.blockPosition();
        BlockState groundBlock = player.level().getBlockState(groundPos);

        if (UmaSoulUtils.hasSkill(UmapyoiAPI.getUmaSoul(player), UmaSkillRegistry.DIRT_RUNNER.getId())) {
            handleMovementModifier(movementSpeed, test_speed, groundBlock, UmapyoiBlockTags.TRACK_DIRT);
        }
    }

    public static void passiveSnowRunner(Player player) {
        AttributeInstance movementSpeed = player.getAttribute(UmapyoiAttributesRegistry.SPRINT_SPEED);

        var test_speed = new AttributeModifier(PASSIVE_ID, 0.1D, Operation.ADD_MULTIPLIED_TOTAL);
        if (UmapyoiAPI.getUmaSoul(player).isEmpty()) {
            movementSpeed.removeModifier(test_speed);
            return;
        }

        BlockPos groundPos = player.getY() % 1 < 0.5 ? player.blockPosition().below() : player.blockPosition();
        BlockState groundBlock = player.level().getBlockState(groundPos);

        if (UmaSoulUtils.hasSkill(UmapyoiAPI.getUmaSoul(player), UmaSkillRegistry.SNOW_RUNNER.getId())) {
            handleMovementModifier(movementSpeed, test_speed, groundBlock, player.getBlockStateOn(), UmapyoiBlockTags.TRACK_SNOW);
        }
    }

    private static void handleMovementModifier(AttributeInstance attribute, AttributeModifier modifier,
            BlockState groundBlock, TagKey<Block> tagIn) {
        handleMovementModifier(attribute, modifier, groundBlock, groundBlock, tagIn);
    }

    private static void handleMovementModifier(AttributeInstance attribute, AttributeModifier modifier,
            BlockState groundBlock, BlockState feetblock, TagKey<Block> tagIn) {
        if (groundBlock.isAir() && feetblock.isAir())
            return ;
        if (!groundBlock.is(tagIn) && !feetblock.is(tagIn)) {
            if (attribute.hasModifier(modifier.id()))
                attribute.removeModifier(modifier);
            return ;
        }
        if (!attribute.hasModifier(modifier.id()))
            attribute.addTransientModifier(modifier);
    }

    public static void register() {
        ApplyUmasoulAttributeCallback.EVENT.register(PassiveSkillEvents::testPassiveSkill_im);
        PlayerBreakSpeedCallback.EVENT.register(PassiveSkillEvents::testPassiveSkill_att);
        PlayerTickCallback.EVENT.register(PassiveSkillEvents::sprintSpeedTick);
        PlayerTickCallback.EVENT.register(PassiveSkillEvents::passiveStepHeight);
        PlayerTickCallback.EVENT.register(PassiveSkillEvents::passiveTurfRunner);
        PlayerTickCallback.EVENT.register(PassiveSkillEvents::passiveDirtRunner);
        PlayerTickCallback.EVENT.register(PassiveSkillEvents::passiveSnowRunner);
    }
}
