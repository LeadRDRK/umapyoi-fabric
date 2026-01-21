package net.tracen.umapyoi.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.utils.UmaSoulUtils;

public record SelectSkillPacket(int slot) implements CustomPacketPayload {
    public static int LATTER_SLOT = 1;
    public static int FORMER_SLOT = 0;

    public static final CustomPacketPayload.Type<SelectSkillPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Umapyoi.MODID, "packet/select_skill"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SelectSkillPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            SelectSkillPacket::slot,
            SelectSkillPacket::new
    );

    @Override
    @MethodsReturnNonnullByDefault
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(SelectSkillPacket packet, ServerPlayNetworking.Context context) {
        var player = context.player();
        if (player.isSpectator()) return;
        ItemStack umaSoul = UmapyoiAPI.getUmaSoul(player);
        if (!umaSoul.isEmpty()) {
            if (packet.slot == LATTER_SLOT) {
                UmaSoulUtils.selectLatterSkill(umaSoul);
            } else if (packet.slot == FORMER_SLOT) {
                UmaSoulUtils.selectFormerSkill(umaSoul);
            } else {
                Umapyoi.getLogger().warn("Some one send a weird packet.");
            }
        }
    }
}
