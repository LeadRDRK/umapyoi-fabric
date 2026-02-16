package net.tracen.umapyoi.client.renderer.blockentity.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.Identifier;
import net.tracen.umapyoi.client.model.SimpleBedrockModel;

public class UmaStatueBlockRenderState extends BlockEntityRenderState {
    public SimpleBedrockModel model = new SimpleBedrockModel();
    public Identifier texture;
    public Identifier emissiveTexture;
}
