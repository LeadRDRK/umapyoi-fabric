package net.tracen.umapyoi.client.model;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.client.model.bedrock.BedrockPart;
import net.tracen.umapyoi.client.model.pojo.BedrockModelPOJO;
import net.tracen.umapyoi.data.tag.UmapyoiUmaDataTags;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.ClientUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.List;

public class UmaPlayerModel<T extends HumanoidRenderState> extends BedrockHumanoidModel<T> {
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
    public BedrockPart hat = new BedrockPart();
    public BedrockPart hideParts;
    public BedrockPart tail;
    public BedrockPart tailDown;

    public BedrockPart cape;

    public List<BedrockPart> longHairParts = Lists.newArrayList();
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
        this.hideParts = this.getChild("hide_parts") != null ? this.getChild("hide_parts") : new BedrockPart();
        this.rightEarHideParts = this.getChild("right_earmuffs");
        this.leftEarHideParts = this.getChild("left_earmuffs");
        this.rightLegHideParts = this.getChild("right_leg_hide_parts") != null ? this.getChild("right_leg_hide_parts")
                : new BedrockPart();
        this.leftLegHideParts = this.getChild("left_leg_hide_parts") != null ? this.getChild("left_leg_hide_parts")
                : new BedrockPart();
        this.tail = this.getChild("tail");
        this.tailDown = this.getChild("tail_down");
        this.longHairParts = Lists.newArrayList();
        this.getModelMap().forEach((name,part)->{
            if(name.startsWith("long_hair_") || name.equals("long_hair"))
                this.longHairParts.add(part);
        });
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entityIn) {
        if (entityIn instanceof ArmorStandRenderState entityarmorstand) {
            this.head.xRot = 0.017453292F * entityarmorstand.headPose.x();
            this.head.yRot = 0.017453292F * entityarmorstand.headPose.y();
            this.head.zRot = 0.017453292F * entityarmorstand.headPose.z();
            this.head.setPos(0.0F, 1.0F, 0.0F);
            this.body.xRot = 0.017453292F * entityarmorstand.bodyPose.x();
            this.body.yRot = 0.017453292F * entityarmorstand.bodyPose.y();
            this.body.zRot = 0.017453292F * entityarmorstand.bodyPose.z();
            this.leftArm.xRot = 0.017453292F * entityarmorstand.leftArmPose.x();
            this.leftArm.yRot = 0.017453292F * entityarmorstand.leftArmPose.y();
            this.leftArm.zRot = 0.017453292F * entityarmorstand.leftArmPose.z();
            this.rightArm.xRot = 0.017453292F * entityarmorstand.rightArmPose.x();
            this.rightArm.yRot = 0.017453292F * entityarmorstand.rightArmPose.y();
            this.rightArm.zRot = 0.017453292F * entityarmorstand.rightArmPose.z();
            this.leftLeg.xRot = 0.017453292F * entityarmorstand.leftLegPose.x();
            this.leftLeg.yRot = 0.017453292F * entityarmorstand.leftLegPose.y();
            this.leftLeg.zRot = 0.017453292F * entityarmorstand.leftLegPose.z();
            this.leftLeg.setPos(1.9F, 11.0F, 0.0F);
            this.rightLeg.xRot = 0.017453292F * entityarmorstand.rightLegPose.x();
            this.rightLeg.yRot = 0.017453292F * entityarmorstand.rightLegPose.y();
            this.rightLeg.zRot = 0.017453292F * entityarmorstand.rightLegPose.z();
            this.rightLeg.setPos(-1.9F, 11.0F, 0.0F);
        } else {
            this.tail.copyFrom(this.body);

            if (this.crouching) {
                this.tail.xRot = 1.0F + entityIn.walkAnimationSpeed * 0.5F;
                this.tail.z = 3.125F;
                this.tail.y = 11.0F;
                this.cape.xRot = 1.0F + entityIn.walkAnimationSpeed * 0.5F;
            } else {
                this.tail.xRot = entityIn.walkAnimationSpeed;
                this.tail.z = 1.75F;
                this.tail.y = 8.0F;
                this.cape.xRot = entityIn.walkAnimationSpeed;
            }
            if (this.head.xRot < 0) {
                this.longHairParts.forEach(part -> part.xRot = -this.head.xRot);
            }
            else
                this.longHairParts.forEach(part -> part.xRot = 0F);

            ItemStack renderingUmaSoul = entityIn.umapyoi$getUmaSoul();
            boolean isStucked = ClientUtils.getClientUmaDataRegistry()
                    .get(ResourceKey.create(UmaData.REGISTRY_KEY, UmaSoulUtils.getName(renderingUmaSoul)))
                    .map(uma -> uma.is(UmapyoiUmaDataTags.STUCK_MODEL))
                    .orElse(false);
            if(!isStucked)
                animationEarTail(entityIn, entityIn.ageInTicks);
        }
        this.hat.copyFrom(head);

    }

    private void animationEarTail(T entityIn, float pAgeInTicks) {
        int earTailAnimationOffset = entityIn.umapyoi$getEarTailAnimationOffset();
        int ears_reminder = (int) ((pAgeInTicks + earTailAnimationOffset)
                % Umapyoi.CONFIG.EAR_ANIMATION_INTERVAL());
        int tail_reminder = (int) ((pAgeInTicks + earTailAnimationOffset)
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

    public void setModelProperties(T state) {
        if (state instanceof AvatarRenderState playerState && playerState.isSpectator) {
            this.setAllVisible(false);
            this.head.visible = true;
        } else {
            this.setAllVisible(true);

            this.crouching = state.isCrouching;
            if (Umapyoi.CONFIG.VANILLA_ARMOR_RENDER() && !Umapyoi.CONFIG.HIDE_PARTS_RENDER()) {

                if (!state.headItem.isEmpty()) {
                    this.hideHat();
                }else {
                    this.showHat();
                }

                if (!state.chestEquipment.isEmpty()
                        && !(state.chestEquipment.getItem() == Items.ELYTRA)) {
                    this.hideParts.visible = false;
                    this.cape.visible = false;
                }else {
                    this.hideParts.visible = true;
                    this.cape.visible = true;
                }

                if (!state.legsEquipment.isEmpty()) {
                    this.rightLegHideParts.visible = false;
                    this.leftLegHideParts.visible = false;
                }else {
                    this.rightLegHideParts.visible = true;
                    this.leftLegHideParts.visible = true;
                }

                if (!state.feetEquipment.isEmpty()) {
                    this.rightFoot.visible = false;
                    this.leftFoot.visible = false;
                }else {
                    this.rightFoot.visible = true;
                    this.leftFoot.visible = true;
                }
            }

            this.showEars();
        }
    }

    public void showEars() {
        if (this.hat.visible) {
            if (this.leftEarHideParts != null && !this.leftEarHideParts.isEmpty())
                this.leftEar.visible = false;
            if (this.rightEarHideParts != null && !this.rightEarHideParts.isEmpty())
                this.rightEar.visible = false;
        } else {
            if (this.leftEarHideParts != null && !this.leftEarHideParts.isEmpty())
                this.leftEar.visible = true;
            if (this.rightEarHideParts != null && !this.rightEarHideParts.isEmpty())
                this.rightEar.visible = true;
        }
    }

    @Override
    public void setAllVisible(boolean pVisible) {
        super.setAllVisible(pVisible);
        this.hat.visible = pVisible;
        this.cape.visible = pVisible;
        this.tail.visible = pVisible;
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

    public void showHat() {
        this.hat.visible = true;
    }

    public void hideHat() {
        this.hat.visible = false;
    }
}
