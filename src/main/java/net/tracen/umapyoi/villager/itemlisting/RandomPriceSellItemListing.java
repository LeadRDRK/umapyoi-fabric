package net.tracen.umapyoi.villager.itemlisting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.VillagerTrades.ItemListing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.tracen.umapyoi.item.ItemRegistry;

import org.jetbrains.annotations.Nullable;

public class RandomPriceSellItemListing implements ItemListing {

    private final ItemStack itemStack;
    private final int baseEmeraldCost;
    private final int minCount;
    private final int maxCount;
    private final int maxUses;
    private final int villagerXp;
    private final float priceMultiplier;

    public RandomPriceSellItemListing(ItemStack itemStack, int pBaseEmeraldCost, int pMaxUses, int pVillagerXp) {
        this(itemStack, pBaseEmeraldCost, 1, 1, pMaxUses, pVillagerXp, 0.05F);
     }
     
     public RandomPriceSellItemListing(ItemStack itemStack, int pBaseEmeraldCost, int pMaxUses, int pVillagerXp, float pPriceMultiplier) {
        this(itemStack, pBaseEmeraldCost, 1, 1, pMaxUses, pVillagerXp, pPriceMultiplier);
     }

     public RandomPriceSellItemListing(ItemStack itemStack, int pBaseEmeraldCost, int pMinCount, int pMaxCount, int pMaxUses, int pVillagerXp) {
        this(itemStack, pBaseEmeraldCost, pMinCount, pMaxCount, pMaxUses, pVillagerXp, 0.05F);
     }
     
     public RandomPriceSellItemListing(ItemStack itemStack, int pBaseEmeraldCost, int pMinCount, int pMaxCount, int pMaxUses, int pVillagerXp, float pPriceMultiplier) {
        this.itemStack = itemStack;
        this.baseEmeraldCost = pBaseEmeraldCost;
        this.minCount = pMinCount;
        this.maxCount = pMaxCount;
        this.maxUses = pMaxUses;
        this.villagerXp = pVillagerXp;
        this.priceMultiplier = pPriceMultiplier;
     }

    @Nullable
    @Override
    public MerchantOffer getOffer(ServerLevel level, Entity entity, RandomSource random) {
        int j = Math.min(this.baseEmeraldCost, 64);

        int count = Math.min(random.nextInt(minCount, maxCount + 1), 64);
        ItemCost cost = new ItemCost(itemStack.getItem(), count);
        ItemStack itemstack1 = new ItemStack(ItemRegistry.JEWEL, j);
        return new MerchantOffer(cost, itemstack1, this.maxUses, this.villagerXp, this.priceMultiplier);
    }
}
