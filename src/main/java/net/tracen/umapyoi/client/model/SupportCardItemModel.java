package net.tracen.umapyoi.client.model;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;

import org.jetbrains.annotations.Nullable;

public class SupportCardItemModel extends DynamicItemBakedModel {
    public SupportCardItemModel(ItemModel original) {
        super(original);
    }

    @Override
    public ItemModel resolveModel(ItemModel original, ItemStack stack, @Nullable ClientLevel level,
                                  @Nullable ItemOwner owner, int seed) {
        if (!stack.isEmpty()) {
            if (stack.getItem() == ItemRegistry.SUPPORT_CARD) {
                var ranking = stack.get(DataComponentsTypeRegistry.GACHA_RANKING.get());
                if (ranking == null) return this.getOriginalModel();
                var modelPath = Umapyoi.id("support_card/support_card_"
                        + ranking.ranking().name().toLowerCase());
                return getModel(modelPath);
            }
        }
        return this.getOriginalModel();
    }
}
