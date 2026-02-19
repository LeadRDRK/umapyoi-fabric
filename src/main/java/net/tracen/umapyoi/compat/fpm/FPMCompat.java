package net.tracen.umapyoi.compat.fpm;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.tracen.umapyoi.client.model.UmaPlayerModel;

import java.lang.reflect.Method;

@Environment(EnvType.CLIENT)
public final class FPMCompat {
    private static Method fpmIsEnabled;
    static {
        try {
            var fpmApi = Class.forName("dev.tr7zw.firstperson.api.FirstPersonAPI");
            fpmIsEnabled = fpmApi.getDeclaredMethod("isEnabled");
        }
        catch (Exception ignored) {
        }
    }

    public static void hideHeadIfRendering(UmaPlayerModel<?> model) {
        if (fpmIsEnabled == null
                || Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON) return;

        boolean isEnabled;
        try {
            isEnabled = (boolean) fpmIsEnabled.invoke(null);
        }
        catch (Exception e) {
            return;
        }

        if (isEnabled) {
            model.head.visible = false;
            model.hat.visible = false;
        }
    }
}
