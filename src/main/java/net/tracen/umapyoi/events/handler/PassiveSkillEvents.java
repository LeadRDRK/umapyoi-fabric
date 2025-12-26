package net.tracen.umapyoi.events.handler;

import net.minecraft.core.BlockPos;
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
import net.tracen.umapyoi.utils.UmaStatusUtils;

import java.util.UUID;

public class PassiveSkillEvents {

    public static final UUID PASSIVEUUID = UUID.fromString("306e284a-8a74-11ee-b9d1-0242ac120002");
    public static final UUID SPRINTUUID = UUID.fromString("0591c346-7c25-4171-b2bd-66e9824f1c90");

    public static void testPassiveSkill_im(ApplyUmasoulAttributeCallback.Context event) {
        var soul = event.getUmaSoul();
        if (UmaSoulUtils.hasSkill(soul, UmaSkillRegistry.INQUISITIVE_MIND.getId())) {
            var speedFlag = UmaSoulUtils.getProperty(soul)[UmaStatusUtils.StatusType.SPEED.getId()] >= 12;
            var wisdomFlag = UmaSoulUtils.getProperty(soul)[UmaStatusUtils.StatusType.WISDOM.getId()] >= 12;
            event.getAttributes().put(Attributes.ATTACK_SPEED, new AttributeModifier(PASSIVEUUID, "passive_speed_bonus",
                    speedFlag && wisdomFlag ? 0.075D :0.05D, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    public static float testPassiveSkill_att(Player player, float origSpeed) {
        var soul = UmapyoiAPI.getUmaSoul(player);
        if (UmaSoulUtils.hasSkill(soul, UmaSkillRegistry.DIG_SPEED.getId()))
            return origSpeed * 1.1F;
        else
            return origSpeed;
    }

    public static void sprintSpeedTick(Player player) {
        AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);

        var speedModifier = new AttributeModifier(SPRINTUUID,
                "sprint_speed_bonus", player.getAttributeValue(UmapyoiAttributesRegistry.SPRINT_SPEED),
                Umapyoi.CONFIG.UMASOUL_SPEED_PRECENT_ENABLE() ? AttributeModifier.Operation.MULTIPLY_TOTAL
                        : AttributeModifier.Operation.ADDITION);
        if (UmapyoiAPI.getUmaSoul(player).isEmpty()) {
            movementSpeed.removeModifier(speedModifier);
            return;
        }

        if (player.isSprinting()) {
            if (!movementSpeed.hasModifier(speedModifier))
                movementSpeed.addTransientModifier(speedModifier);
        } else {
            movementSpeed.removeModifier(speedModifier);
        }
    }

    public static void passiveStepHeight(Player player) {
        AttributeInstance stepHeight = player.getAttribute(UmapyoiAttributesRegistry.STEP_HEIGHT_ADDITION);
        var heightModifier = new AttributeModifier(PASSIVEUUID,
                "passive_skill_height", 0.5D, Operation.ADDITION);
        if (UmapyoiAPI.getUmaSoul(player).isEmpty()) {
            stepHeight.removeModifier(heightModifier);
            return;
        }

        if (UmaSoulUtils.hasSkill(UmapyoiAPI.getUmaSoul(player), UmaSkillRegistry.MOUNTAIN_CLIMBER.getId())) {
            if (!stepHeight.hasModifier(heightModifier))
                stepHeight.addTransientModifier(heightModifier);
        } else {
            stepHeight.removeModifier(heightModifier);
        }
    }

    public static void passiveTurfRunner(Player player) {
        AttributeInstance movementSpeed = player.getAttribute(UmapyoiAttributesRegistry.SPRINT_SPEED);

        var test_speed = new AttributeModifier(PASSIVEUUID,
                "passive_skill_turf", 0.1D, Operation.MULTIPLY_TOTAL);
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

        var test_speed = new AttributeModifier(PASSIVEUUID,
                "passive_skill_dirt", 0.1D, Operation.MULTIPLY_TOTAL);
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

        var test_speed = new AttributeModifier(PASSIVEUUID,
                "passive_skill_snow", 0.1D, Operation.MULTIPLY_TOTAL);
        if (UmapyoiAPI.getUmaSoul(player).isEmpty()) {
            movementSpeed.removeModifier(test_speed);
            return;
        }

        BlockPos groundPos = player.getY() % 1 < 0.5 ? player.blockPosition().below() : player.blockPosition();
        BlockState groundBlock = player.level().getBlockState(groundPos);

        if (UmaSoulUtils.hasSkill(UmapyoiAPI.getUmaSoul(player), UmaSkillRegistry.SNOW_RUNNER.getId())) {
            handleMovementModifier(movementSpeed, test_speed, groundBlock, player.getFeetBlockState(), UmapyoiBlockTags.TRACK_SNOW);
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
            if (attribute.hasModifier(modifier))
                attribute.removeModifier(modifier);
            return ;
        }
        if (!attribute.hasModifier(modifier))
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
