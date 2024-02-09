package net.tracen.umapyoi.client.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.tracen.umapyoi.client.model.bedrock.BedrockModel;
import net.tracen.umapyoi.client.model.bedrock.BedrockPart;
import net.tracen.umapyoi.client.model.pojo.BedrockModelPOJO;
import net.tracen.umapyoi.client.model.pojo.BonesItem;

import java.util.HashMap;
import java.util.List;
/**
 * Simple Bedrock Entity Model, No need for other change.
 */
public class BedrockEntityModel<T extends Entity> extends EntityModel<T> implements BedrockModel {

    protected final HashMap<String, BedrockPart> modelMap;
    private final HashMap<String, BonesItem> indexBones;
    private final List<BedrockPart> shouldRender;
    private BedrockModelPOJO modelPOJO;
    private AABB renderBoundingBox;

    private boolean emissive;

    public BedrockEntityModel() {
        super(RenderType::entityTranslucent);
        modelMap = Maps.newHashMap();
        indexBones = Maps.newHashMap();
        shouldRender = Lists.newLinkedList();
        renderBoundingBox = new AABB(-1, 0, -1, 1, 2, 1);
    }

    public BedrockEntityModel(BedrockModelPOJO pojo) {
        this();
        loadModel(pojo);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.renderBedrockModel(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public HashMap<String, BedrockPart> getModelMap() {
        return this.modelMap;
    }

    @Override
    public HashMap<String, BonesItem> getIndexBones() {
        return this.indexBones;
    }

    @Override
    public List<BedrockPart> getShouldRender() {
        return this.shouldRender;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return this.renderBoundingBox;
    }

    @Override
    public void setRenderBoundingBox(AABB aabb) {
        this.renderBoundingBox = aabb;
    }

    @Override
    public boolean isEmissive() {
        return emissive;
    }

    @Override
    public void setEmissive() {
        this.emissive = true;
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        
    }
    
    @Override
    public BedrockModelPOJO getBedrockModelPOJO() {
        return this.modelPOJO;
    }

    @Override
    public void setBedrockModelPOJO(BedrockModelPOJO pojo) {
        this.modelPOJO = pojo;
    }
}
