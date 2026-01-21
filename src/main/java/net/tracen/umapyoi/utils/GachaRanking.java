package net.tracen.umapyoi.utils;

import com.mojang.serialization.Codec;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;

import io.netty.buffer.ByteBuf;

public enum GachaRanking {
    R, SR, SSR, EASTER_EGG;

    public static final Codec<GachaRanking> CODEC = Codec.STRING
            .xmap(string -> GachaRanking.valueOf(string.toUpperCase()), instance -> instance.name().toLowerCase());

    private static final GachaRanking[] values = values();

    public static final StreamCodec<ByteBuf, GachaRanking> STREAM_CODEC = ByteBufCodecs.idMapper(
            ord -> values[ord],
            GachaRanking::ordinal
    );

    public static GachaRanking getGachaRanking(ItemStack stack) {
        return !stack.has(DataComponentsTypeRegistry.GACHA_RANKING.get()) ? GachaRanking.R
                : stack.get(DataComponentsTypeRegistry.GACHA_RANKING.get()).ranking();
    }
}