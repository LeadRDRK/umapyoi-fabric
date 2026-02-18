package net.tracen.umapyoi.compat.fpm;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.tracen.umapyoi.client.model.UmaPlayerModel;

import java.lang.reflect.Method;

@Environment(EnvType.CLIENT)
public final class FPMCompat {
    private static Method fpmIsRenderingPlayer;
    static {
        try {
            var fpmApi = Class.forName("dev.tr7zw.firstperson.api.FirstPersonAPI");
            fpmIsRenderingPlayer = fpmApi.getDeclaredMethod("isRenderingPlayer");
        }
        catch (Exception ignored) {
        }
    }

    public static void hideHeadIfRendering(UmaPlayerModel<?> model) {
        if (fpmIsRenderingPlayer == null) return;

        boolean isRendering;
        try {
            isRendering = (boolean) fpmIsRenderingPlayer.invoke(null);
        }
        catch (Exception e) {
            return;
        }

        if (isRendering) {
            model.head.visible = false;
            if(!model.hat.isEmpty()) model.hat.visible = false;
        }
    }
}
