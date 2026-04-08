package net.tracen.umapyoi.client.renderer.blockentity.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.tracen.umapyoi.client.model.UmaPlayerModel;

import org.jetbrains.annotations.Nullable;

public class UmaStatueBlockRenderState extends BlockEntityRenderState {
    public UmaPlayerModel<?> model = new UmaPlayerModel<>();
    public ResourceLocation texture;
    public ResourceLocation emissiveTexture;
    @Nullable public UmaPlayerModel<?> suitModel;
    public ResourceLocation suitTexture;
}
