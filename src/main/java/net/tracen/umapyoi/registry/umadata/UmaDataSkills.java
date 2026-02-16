package net.tracen.umapyoi.registry.umadata;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.tracen.umapyoi.registry.UmaSkillRegistry;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;

public record UmaDataSkills(int skillSlot, int selectedSkill, List<Identifier> skills) {
    public static final Codec<UmaDataSkills> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("skill_slot").forGetter(UmaDataSkills::skillSlot),
            Codec.INT.fieldOf("selected_skill").forGetter(UmaDataSkills::selectedSkill),
            Identifier.CODEC.listOf().fieldOf("skills").forGetter(UmaDataSkills::skills)
    ).apply(instance, UmaDataSkills::new));

    public static final StreamCodec<ByteBuf, UmaDataSkills> STREAM = StreamCodec.composite(
            ByteBufCodecs.INT, UmaDataSkills::skillSlot,
            ByteBufCodecs.INT, UmaDataSkills::selectedSkill,
            ByteBufCodecs.collection(ArrayList::new, Identifier.STREAM_CODEC), UmaDataSkills::skills,
            UmaDataSkills::new
    );

    public static final UmaDataSkills DEFAULT = new UmaDataSkills(5, 0, Lists.newArrayList(UmaSkillRegistry.BASIC_PACE.getId()));
}