package net.tracen.umapyoi.client.model;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.item.UmaRaceTicketItem;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class UmaRaceTicketItemModel extends DynamicItemBakedModel {
    public UmaRaceTicketItemModel(ItemModel original) {
        super(original);
    }

    @Override
    public ItemModel resolveModel(ItemModel original, ItemStack stack, @Nullable ClientLevel level,
                                  @Nullable ItemOwner owner, int seed) {
        if (!stack.isEmpty()) {
            if (stack.getItem() == ItemRegistry.UMA_RACE_TICKET) {
                return Optional.ofNullable(UmaRaceTicketItem.getRace(stack, level))
                        .map(race -> race.texturePredicateOverride == null
                                ? race.ranking.textureSuffix
                                : race.texturePredicateOverride)
                        .map(suffix -> Umapyoi.id("race_ticket/race_ticket_" + suffix))
                        .map(this::getModel)
                        .orElse(this.getOriginalModel());
            }
        }
        return this.getOriginalModel();
    }
}
