package net.tracen.umapyoi.recipe;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.utils.GachaRanking;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.netty.buffer.ByteBuf;

public class RequestUma {
    private final Optional<Identifier> name;
    private final Optional<Identifier> identifier;
    private final List<GachaRanking> ranking;

    public static final Codec<RequestUma> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Identifier.CODEC.optionalFieldOf("name")
                            .forGetter(RequestUma::getName),
                    Identifier.CODEC.optionalFieldOf("identifier")
                            .forGetter(RequestUma::getIdentifier),
                    GachaRanking.CODEC.listOf().optionalFieldOf("ranking", Lists.newArrayList())
                            .forGetter(RequestUma::getRanking))
            .apply(instance, RequestUma::new));

    public static final StreamCodec<ByteBuf, RequestUma> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC.apply(ByteBufCodecs::optional), RequestUma::getName,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs::optional), RequestUma::getIdentifier,
            GachaRanking.STREAM_CODEC.apply(ByteBufCodecs.list()), RequestUma::getRanking,
            RequestUma::new
    );

    public RequestUma() {
        this.name = Optional.empty();
        this.identifier = Optional.empty();
        this.ranking = Lists.newArrayList();
    }

    public RequestUma(Optional<Identifier> name, Optional<Identifier> identifier, List<GachaRanking> ranking) {
        this.name = name;
        this.identifier = identifier;
        this.ranking = ranking;
    }

    public Optional<Identifier> getName() {
        return name;
    }

    public Optional<Identifier> getIdentifier() {
        return identifier;
    }

    public List<GachaRanking> getRanking() {
        return ranking;
    }

    public boolean test(ItemStack soul) {
        var name = this.name.isPresent() ? UmaSoulUtils.getName(soul).equals(this.name.get()) : true;
        var id = this.identifier.isPresent()
                ? Objects.equals(soul.get(DataComponentsTypeRegistry.IDENTIFIER.get()), this.identifier.get())
                : true;
        var ranking = !this.ranking.isEmpty() ? this.ranking.contains(GachaRanking.getGachaRanking(soul)) : true;
        return name && id && ranking;
    }

    public void initItemStack(ItemStack stack) {
        this.name.ifPresent(loc ->
                stack.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), loc));
        this.identifier.ifPresent(loc ->
                stack.set(DataComponentsTypeRegistry.IDENTIFIER.get(), loc));
    }
}
