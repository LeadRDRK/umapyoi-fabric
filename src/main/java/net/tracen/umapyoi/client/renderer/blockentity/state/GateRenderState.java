package net.tracen.umapyoi.client.renderer.blockentity.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.tracen.umapyoi.client.model.SimpleBedrockModel;

public class GateRenderState extends BlockEntityRenderState {
    public final SimpleBedrockModel model = new SimpleBedrockModel();
    public Direction direction;
}
