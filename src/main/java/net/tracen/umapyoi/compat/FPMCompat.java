package net.tracen.umapyoi.compat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.tracen.umapyoi.client.model.UmaPlayerModel;

import java.lang.reflect.Method;

@Environment(EnvType.CLIENT)
public final class FPMCompat {
    private static Method fpmIsCameraEntity;
    static {
        try {
            //noinspection JavaReflectionMemberAccess
            fpmIsCameraEntity = LivingEntityRenderState.class.getDeclaredMethod("isCameraEntity");
        }
        catch (Exception ignored) {
        }
    }

    public static void hideHeadIfRendering(LivingEntityRenderState renderState, UmaPlayerModel<?> model) {
        if (fpmIsCameraEntity == null) return;

        boolean isRendering;
        try {
            isRendering = (boolean) fpmIsCameraEntity.invoke(renderState);
        }
        catch (Exception e) {
            return;
        }

        if (isRendering) {
            model.head.visible = false;
            model.hat.visible = false;
        }
    }
}