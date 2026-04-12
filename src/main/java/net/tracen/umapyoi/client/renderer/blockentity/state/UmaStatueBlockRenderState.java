package net.tracen.umapyoi.client.renderer.blockentity.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.tracen.umapyoi.client.model.UmaPlayerModel;

import org.jetbrains.annotations.Nullable;

public class UmaStatueBlockRenderState extends BlockEntityRenderState {
    public Direction direction;
    public UmaPlayerModel<?> model = new UmaPlayerModel<>();
    public Identifier texture;
    public Identifier emissiveTexture;
    @Nullable public UmaPlayerModel<?> suitModel;
    public Identifier suitTexture;
}
