package net.tracen.umapyoi.client.renderer.blockentity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.tracen.umapyoi.block.entity.AbstractPedestalBlockEntity;
import net.tracen.umapyoi.client.renderer.blockentity.state.PedestalBlockRenderState;

public class UmaPedestalBlockRenderer extends AbstractPedestalBlockRenderer<AbstractPedestalBlockEntity, PedestalBlockRenderState> {
    public UmaPedestalBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public PedestalBlockRenderState createRenderState() {
        return new PedestalBlockRenderState();
    }
}
