package net.tracen.umapyoi.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.events.ApplySkillCallback;
import net.tracen.umapyoi.events.UseSkillCallback;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.skills.UmaSkill;
import net.tracen.umapyoi.utils.UmaSoulUtils;

public class UseSkillPacket implements FabricPacket {
    public UseSkillPacket() {
    }

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    public static final PacketType<UseSkillPacket> TYPE = PacketType.create(
            new ResourceLocation(Umapyoi.MODID, "packet/use_skill"),
            (buf) -> new UseSkillPacket()
    );

    @Override
    public PacketType<UseSkillPacket> getType() {
        return TYPE;
    }

    public static void handler(UseSkillPacket packet, ServerPlayer player, PacketSender responseSender) {
        if (player.isSpectator()) return;
        ItemStack umaSoul = UmapyoiAPI.getUmaSoul(player);

        if (!umaSoul.isEmpty()) {
            ResourceLocation selectedSkillName = UmaSoulUtils.getSelectedSkill(umaSoul);
            UmaSkill selectedSkill = UmaSkillRegistry.REGISTRY.get().get(selectedSkillName);
            if (selectedSkill == null) {
                player.displayClientMessage(Component.translatable("umapyoi.unknown_skill"), true);
                return;
            }

            var useEvent = new UseSkillCallback.Context(selectedSkillName, player.level(), player);
            if (UseSkillCallback.invoke(useEvent))
                return;

            int ap = UmaSoulUtils.getActionPoint(umaSoul);
            var evt = new UseSkillCallback.Context(selectedSkillName, player.level(), player, selectedSkill.getActionPoint());
            if (UseSkillCallback.invoke(evt))
                return;
            int apNeeded = evt.getAp();
            if (ap >= apNeeded) {
                player.connection.send(new ClientboundSoundPacket(Holder.direct(selectedSkill.getSound()), SoundSource.PLAYERS,
                        player.getX(), player.getY(), player.getZ(), 1F, 1F, 0L));
                selectedSkill.applySkill(player.level(), player);
                UmaSoulUtils.setActionPoint(umaSoul, ap - apNeeded);

                var applyEvent = new ApplySkillCallback.Context(UmaSkillRegistry.REGISTRY.get().getKey(selectedSkill), player.level(), player);
                ApplySkillCallback.invoke(applyEvent);
            } else {
                player.displayClientMessage(Component.translatable("umapyoi.not_enough_ap"), true);
            }
        }
    }
}
