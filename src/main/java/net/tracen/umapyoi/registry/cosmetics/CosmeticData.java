package net.tracen.umapyoi.registry.cosmetics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.utils.ClientUtils;

import java.util.Optional;

public record CosmeticData(Identifier model, Optional<Identifier> flatModel,
                           Optional<Identifier> texture, Optional<Identifier> flatTexture) {

    public static final Codec<CosmeticData> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(Identifier.CODEC.fieldOf("model").forGetter(CosmeticData::model),
                    Identifier.CODEC.optionalFieldOf("flatModel").forGetter(CosmeticData::flatModel),
                    Identifier.CODEC.optionalFieldOf("texture").forGetter(CosmeticData::texture),
                    Identifier.CODEC.optionalFieldOf("flatTexture").forGetter(CosmeticData::flatTexture))
            .apply(instance, CosmeticData::new));

    public static final Identifier COMMON_COSTUME = Identifier.fromNamespaceAndPath(Umapyoi.MODID, "common_costume");

    public static final CosmeticData DEFAULT_COSTUME = new CosmeticData(COMMON_COSTUME);

    public CosmeticData(Identifier model) {
        this(model, Optional.of(model), Optional.of(model), Optional.of(model));
    }

    public CosmeticData(Identifier model, Identifier texture) {
        this(model, Optional.of(model), Optional.of(texture), Optional.of(texture));
    }

    public CosmeticData(Identifier model, Identifier flatModel,
                        Identifier texture, Identifier flatTexture) {
        this(model, Optional.of(flatModel), Optional.of(texture), Optional.of(flatTexture));
    }

    public static final ResourceKey<Registry<CosmeticData>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(Identifier.fromNamespaceAndPath(Umapyoi.MODID, "cosmetic_data"));

    public Identifier getFlatModel() {
        return this.flatModel.orElse(this.model);
    }

    public Identifier getTexture(boolean tanned) {
        Identifier result = this.texture.orElse(this.model);
        if(tanned)
            result = Identifier.fromNamespaceAndPath(result.getNamespace(), result.getPath()+"_tanned");

        return ClientUtils.getTexture(result);
    }

    public Identifier getFlatTexture(boolean tanned) {
        Identifier result = this.flatTexture.orElse(this.texture.orElse(this.model));
        if(tanned)
            result = Identifier.fromNamespaceAndPath(result.getNamespace(), result.getPath()+"_tanned");

        return ClientUtils.getTexture(result);
    }

}