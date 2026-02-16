package net.tracen.umapyoi.villager.itemlisting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.VillagerTrades.ItemListing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.skills.UmaSkill;
import net.tracen.umapyoi.utils.UmaSkillUtils;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SkillBooksItemListing implements ItemListing {

    private final int villagerXp;

    public SkillBooksItemListing(int pVillagerXp) {
        this.villagerXp = pVillagerXp;
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(ServerLevel level, Entity entity, RandomSource random) {
        List<UmaSkill> list = UmaSkillRegistry.REGISTRY.get().stream().toList();
        UmaSkill skill = list.get(random.nextInt(list.size()));
        int i = skill.getSkillLevel() * 2;
        ItemStack itemstack = UmaSkillUtils.getSkillBook(skill);
        int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
        if (j > 64) {
            j = 64;
        }

        return new MerchantOffer(new ItemCost(ItemRegistry.JEWEL, j), itemstack, 12,
                this.villagerXp, 0.2F);
    }
}
