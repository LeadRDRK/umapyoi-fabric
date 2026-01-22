package net.tracen.umapyoi.client.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
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
 * Ported from MMLib
 */
public class BedrockEntityModel<T extends EntityRenderState> implements BedrockModel {

    protected final HashMap<String, BedrockPart> modelMap;
    private final HashMap<String, BonesItem> indexBones;
    private final List<BedrockPart> shouldRender;
    private BedrockModelPOJO modelPOJO;
    private AABB renderBoundingBox;
    private boolean emissive;

    public BedrockEntityModel() {
        modelMap = Maps.newHashMap();
        indexBones = Maps.newHashMap();
        shouldRender = Lists.newLinkedList();
        renderBoundingBox = new AABB(-1, 0, -1, 1, 2, 1);
        emissive = false;
    }

    public BedrockEntityModel(BedrockModelPOJO pojo) {
        this();
        loadModel(pojo);
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.renderBedrockModel(poseStack, buffer, packedLight, packedOverlay, color);
    }

    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount) {

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

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount) {
        
    }

    @Override
    public BedrockModelPOJO getBedrockModelPOJO() {
        return this.modelPOJO;
    }

    @Override
    public void setBedrockModelPOJO(BedrockModelPOJO pojo) {
        this.modelPOJO = pojo;
    }

    @Override
    public boolean isEmissive() {
        return emissive;
    }

    @Override
    public void setEmissive(boolean emissive) {
        this.emissive = emissive;
    }
}
