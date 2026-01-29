package net.tracen.umapyoi.utils;

import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.registry.UmaSkillRegistry;

public final class ResultRankingUtils {
    public static int getRanking(ItemStack soul) {
        if(soul.has(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get()))
            return soul.get(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get()).resultRanking();
        return 0;
    }

    public static int generateRanking(ItemStack soul) {
        int[] property = UmaSoulUtils.getProperty(soul).array();
        int skills = 0;
        
        for(var skill : UmaSoulUtils.getSkills(soul)) {
            skills += UmaSkillRegistry.REGISTRY.get().get(skill).orElseThrow().value().getSkillLevel();
        }
        
        return ResultRankingUtils
                .generateRanking(property[0] + property[1] + property[2] + property[3] + property[4] + skills);
    }

    public static int generateRanking(int score) {
        if (score >= 191)
            return 38;
        for (int i = 1; i < 39; i++) {
            if (score < (i * 5 + 1))
                return i - 1;
        }
        return 0;
    }

}
