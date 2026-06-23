package net.tracen.umapyoi.utils;

import net.tracen.umapyoi.Umapyoi;

public class GachaUtils {
    public static boolean checkGachaConfig() {
        return (Umapyoi.CONFIG.GACHA_PROBABILITY_R
                + Umapyoi.CONFIG.GACHA_PROBABILITY_SR
                + Umapyoi.CONFIG.GACHA_PROBABILITY_SSR) == Umapyoi.CONFIG.GACHA_PROBABILITY_SUM;
    }
    
}
