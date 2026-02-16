package net.tracen.umapyoi.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

public class TrainingSupportUtils {
    public static Component getTranslatedSupportCardName(Identifier name) {
        return Component.translatable(Util.makeDescriptionId("support_card", name) + ".name");
    }
}
