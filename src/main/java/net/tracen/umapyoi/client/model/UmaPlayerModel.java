package net.tracen.umapyoi.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ElytraItem;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.client.model.bedrock.BedrockPart;
import net.tracen.umapyoi.client.model.pojo.BedrockModelPOJO;

public class UmaPlayerModel<T extends LivingEntity> extends BedrockHumanoidModel<T> {
    public BedrockPart rightArmDown;
    public BedrockPart leftArmDown;
    public BedrockPart rightLegDown;
    public BedrockPart leftLegDown;
    public BedrockPart rightEar;
    public BedrockPart leftEar;
    public BedrockPart rightEarHideParts;
    public BedrockPart leftEarHideParts;
    public BedrockPart rightFoot;
    public BedrockPart leftFoot;
    public BedrockPart rightLegHideParts;
    public BedrockPart leftLegHideParts;
    public BedrockPart hat;
    public BedrockPart longHair;
    public BedrockPart hideParts;
    public BedrockPart tail;
    public BedrockPart tailDown;

    public BedrockPart cape;

    public UmaPlayerModel() {
        super();
    }

    public UmaPlayerModel(BedrockModelPOJO pojo) {
        super(pojo);

    }

    @Override
    public void loadModel(BedrockModelPOJO pojo) {
        super.loadModel(pojo);
        this.rightArmDown = this.getChild("right_arm_down");
        this.leftArmDown = this.getChild("left_arm_down");
        this.rightLegDown = this.getChild("right_leg_down");
        this.leftLegDown = this.getChild("left_leg_down");
        this.rightEar = this.getChild("right_ear");
        this.leftEar = this.getChild("left_ear");
        this.rightFoot = this.getChild("right_foot");
        this.leftFoot = this.getChild("left_foot");
        this.hat = this.getChild("hat") != null ? this.getChild("hat") : new BedrockPart();
        this.cape = this.getChild("cape") != null ? this.getChild("cape") : new BedrockPart();
        this.longHair = this.getChild("long_hair") != null ? this.getChild("long_hair") : new BedrockPart();
        this.hideParts = this.getChild("hide_parts") != null ? this.getChild("hide_parts") : new BedrockPart();
        this.rightEarHideParts = this.getChild("right_earmuffs");
        this.leftEarHideParts = this.getChild("left_earmuffs");
        this.rightLegHideParts = this.getChild("right_leg_hide_parts") != null ? this.getChild("right_leg_hide_parts")
                : new BedrockPart();
        this.leftLegHideParts = this.getChild("left_leg_hide_parts") != null ? this.getChild("left_leg_hide_parts")
                : new BedrockPart();
        this.tail = this.getChild("tail");
        this.tailDown = this.getChild("tail_down");
    }

    @Override
    public void setupAnim(T entityIn, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw,
                          float pHeadPitch) {
        if (entityIn instanceof ArmorStand) {
            ArmorStand entityarmorstand = (ArmorStand) entityIn;
            this.head.xRot = 0.017453292F * entityarmorstand.getHeadPose().getX();
            this.head.yRot = 0.017453292F * entityarmorstand.getHeadPose().getY();
            this.head.zRot = 0.017453292F * entityarmorstand.getHeadPose().getZ();
            this.head.setPos(0.0F, 1.0F, 0.0F);
            this.body.xRot = 0.017453292F * entityarmorstand.getBodyPose().getX();
            this.body.yRot = 0.017453292F * entityarmorstand.getBodyPose().getY();
            this.body.zRot = 0.017453292F * entityarmorstand.getBodyPose().getZ();
            this.leftArm.xRot = 0.017453292F * entityarmorstand.getLeftArmPose().getX();
            this.leftArm.yRot = 0.017453292F * entityarmorstand.getLeftArmPose().getY();
            this.leftArm.zRot = 0.017453292F * entityarmorstand.getLeftArmPose().getZ();
            this.rightArm.xRot = 0.017453292F * entityarmorstand.getRightArmPose().getX();
            this.rightArm.yRot = 0.017453292F * entityarmorstand.getRightArmPose().getY();
            this.rightArm.zRot = 0.017453292F * entityarmorstand.getRightArmPose().getZ();
            this.leftLeg.xRot = 0.017453292F * entityarmorstand.getLeftLegPose().getX();
            this.leftLeg.yRot = 0.017453292F * entityarmorstand.getLeftLegPose().getY();
            this.leftLeg.zRot = 0.017453292F * entityarmorstand.getLeftLegPose().getZ();
            this.leftLeg.setPos(1.9F, 11.0F, 0.0F);
            this.rightLeg.xRot = 0.017453292F * entityarmorstand.getRightLegPose().getX();
            this.rightLeg.yRot = 0.017453292F * entityarmorstand.getRightLegPose().getY();
            this.rightLeg.zRot = 0.017453292F * entityarmorstand.getRightLegPose().getZ();
            this.rightLeg.setPos(-1.9F, 11.0F, 0.0F);
        } else {
            this.tail.copyFrom(this.body);

            if (this.crouching) {
                this.tail.xRot = 1.0F + pLimbSwingAmount * 0.5F;
                this.tail.z = 3.125F;
                this.tail.y = 11.0F;
                this.cape.xRot = 1.0F + pLimbSwingAmount * 0.5F;
            } else {
                this.tail.xRot = pLimbSwingAmount * 1F;
                this.tail.z = 1.75F;
                this.tail.y = 8.0F;
                this.cape.xRot = pLimbSwingAmount * 1F;
            }
            if (this.head.xRot < 0)
                this.longHair.xRot = -this.head.xRot;
            else this.longHair.xRot = 0F;

            animationEarTail(entityIn, pAgeInTicks);
        }
        this.hat.copyFrom(head);

    }

    private void animationEarTail(T entityIn, float pAgeInTicks) {
        int ears_reminder = (int) ((pAgeInTicks + Math.abs(entityIn.getUUID().getLeastSignificantBits()) % 10)
                % Umapyoi.CONFIG.EAR_ANIMATION_INTERVAL());
        int tail_reminder = (int) ((pAgeInTicks + Math.abs(entityIn.getUUID().getLeastSignificantBits()) % 10)
                % Umapyoi.CONFIG.TAIL_ANIMATION_INTERVAL());
        float earRot = Mth.cos(ears_reminder) * 0.125F;
        if (0 < ears_reminder && ears_reminder < 8) {
            if (this.leftEarHideParts != null)
                this.leftEarHideParts.zRot = earRot;
            if (this.rightEarHideParts != null)
                this.rightEarHideParts.zRot = -earRot;
            this.leftEar.zRot = earRot;
            this.rightEar.zRot = -earRot;
        } else {
            if (this.leftEarHideParts != null)
                this.leftEarHideParts.zRot = 0F;
            if (this.rightEarHideParts != null)
                this.rightEarHideParts.zRot = 0F;
            this.leftEar.zRot = 0F;
            this.rightEar.zRot = 0F;
        }

        if (0 < tail_reminder && tail_reminder < 8) {
            this.tail.zRot = -Mth.cos(pAgeInTicks * 0.7F) * 0.5F;
            this.tail.yRot = Mth.cos(pAgeInTicks * 0.7F) * 0.5F;
        } else {
            this.tail.zRot = 0;
            this.tail.yRot = 0;
        }
    }

    public void setModelProperties(LivingEntity player) {
        this.setModelProperties(player, false);
    }

    public void setModelProperties(LivingEntity player, boolean render_head_only) {
        this.setModelProperties(player, render_head_only, false);
    }

    public void setModelProperties(LivingEntity player, boolean render_head_only, boolean hide_head) {

        boolean shouldSit = player.isPassenger()
                && (player.getVehicle() != null/* && player.getVehicle().shouldRiderSit()*/);
        this.riding = shouldSit;

        if (player.isSpectator()) {
            this.setAllVisible(false);
            this.head.visible = true;
        } else {
            this.setAllVisible(true);
            if (render_head_only) {
                this.setAllVisible(false);
                this.head.visible = true;
                this.tail.visible = true;
                this.hat.visible = !this.hat.isEmpty();
            } else if (hide_head) {
                this.head.visible = false;
                this.tail.visible = false;
                this.hat.visible = !this.hat.isEmpty();
            }

            this.crouching = player.isCrouching();
            if (Umapyoi.CONFIG.VANILLA_ARMOR_RENDER() && !Umapyoi.CONFIG.HIDE_PARTS_RENDER()) {

                if (!player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                    this.hat.visible = false;
                }

                if (!player.getItemBySlot(EquipmentSlot.CHEST).isEmpty()
                        && !(player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ElytraItem)) {
                    this.hideParts.visible = false;
                }

                if (!player.getItemBySlot(EquipmentSlot.LEGS).isEmpty()) {
                    this.rightLegHideParts.visible = false;
                    this.leftLegHideParts.visible = false;
                }

                if (!player.getItemBySlot(EquipmentSlot.FEET).isEmpty()) {
                    this.rightFoot.visible = false;
                    this.leftFoot.visible = false;
                }
            }
            if (this.hat.visible) {
                if (this.leftEarHideParts != null)
                    this.leftEar.visible = false;
                if (this.rightEarHideParts != null)
                    this.rightEar.visible = false;
            } else {
                if (this.leftEarHideParts != null)
                    this.leftEar.visible = true;
                if (this.rightEarHideParts != null)
                    this.rightEar.visible = true;
            }
        }
    }

    public void copyAnim(BedrockPart part, ModelPart old_part) {
        part.xRot = old_part.xRot;
        part.yRot = old_part.yRot;
        part.zRot = old_part.zRot;
        part.x = old_part.x;
        if (part == this.leftArm)
            part.x -= 1F;
        if (part == this.rightArm)
            part.x += 1F;

        if (part == this.leftLeg)
            part.x -= 0.125F;
        if (part == this.rightLeg)
            part.x += 0.125F;
        part.y = old_part.y;
        part.z = old_part.z;
        if (part == this.leftLeg)
            part.z -= 0.125F;
        if (part == this.rightLeg)
            part.z -= 0.125F;
    }
}
