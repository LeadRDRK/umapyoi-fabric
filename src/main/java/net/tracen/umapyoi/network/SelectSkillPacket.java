package net.tracen.umapyoi.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.utils.UmaSoulUtils;

public class SelectSkillPacket implements FabricPacket {
    public static int LATTER_SLOT = 1;
    public static int FORMER_SLOT = 0;
    private final int slot;

    public SelectSkillPacket(int slot) {
        this.slot = slot;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(slot);
    }

    public static final PacketType<SelectSkillPacket> TYPE = PacketType.create(
            new ResourceLocation(Umapyoi.MODID, "packet/select_skill"),
            (buf) -> new SelectSkillPacket(buf.readInt())
    );

    @Override
    public PacketType<SelectSkillPacket> getType() {
        return TYPE;
    }

    public static void handler(SelectSkillPacket packet, ServerPlayer player, PacketSender responseSender) {
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
