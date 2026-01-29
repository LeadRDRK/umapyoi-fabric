package net.tracen.umapyoi.utils;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TrainingSupportUtils {
    public static Component getTranslatedSupportCardName(ResourceLocation name) {
        return Component.translatable(Util.makeDescriptionId("support_card", name) + ".name");
    }
}
