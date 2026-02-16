package net.tracen.umapyoi.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.client.model.UmaPlayerModel;
import net.tracen.umapyoi.client.renderer.entity.state.UmapyoiRenderState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements UmapyoiRenderState {
    @Unique private ItemStack umaSoul = ItemStack.EMPTY;
    @Unique private int earTailAnimationOffset = -1;
    @Unique private UmaPlayerModel<HumanoidRenderState> umaModel;
    @Unique private UmaPlayerModel<HumanoidRenderState> suitModel;
    @Unique private Identifier umaTexture;
    @Unique private Identifier suitTexture;
    @Unique private Identifier umaEmissiveTexture;

    @Override
    public ItemStack umapyoi$getUmaSoul() {
        return umaSoul;
    }

    @Override
    public void umapyoi$setUmaSoul(ItemStack umaSoul) {
        this.umaSoul = umaSoul;
    }

    @Override
    public int umapyoi$getEarTailAnimationOffset() {
        return earTailAnimationOffset;
    }

    @Override
    public void umapyoi$setEarTailAnimationOffset(int earTailAnimationOffset) {
        this.earTailAnimationOffset = earTailAnimationOffset;
    }

    @Override
    public UmaPlayerModel<HumanoidRenderState> umapyoi$getUmaModel() {
        return umaModel;
    }

    @Override
    public void umapyoi$setUmaModel(UmaPlayerModel<HumanoidRenderState> umaModel) {
        this.umaModel = umaModel;
    }

    @Override
    public UmaPlayerModel<HumanoidRenderState> umapyoi$getSuitModel() {
        return suitModel;
    }

    @Override
    public void umapyoi$setSuitModel(UmaPlayerModel<HumanoidRenderState> suitModel) {
        this.suitModel = suitModel;
    }

    public Identifier umapyoi$getUmaTexture() {
        return umaTexture;
    }

    public void umapyoi$setUmaTexture(Identifier umaTexture) {
        this.umaTexture = umaTexture;
    }

    public Identifier umapyoi$getSuitTexture() {
        return suitTexture;
    }

    public void umapyoi$setSuitTexture(Identifier suitTexture) {
        this.suitTexture = suitTexture;
    }

    public Identifier umapyoi$getUmaEmissiveTexture() {
        return umaEmissiveTexture;
    }

    public void umapyoi$setUmaEmissiveTexture(Identifier umaEmissiveTexture) {
        this.umaEmissiveTexture = umaEmissiveTexture;
    }
}
