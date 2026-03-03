package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

public interface PlayerSpawnPhantomsCallback {
    void callback(ServerPlayer player);

    Event<PlayerSpawnPhantomsCallback> EVENT = EventFactory.createArrayBacked(PlayerSpawnPhantomsCallback.class,
            (listeners) -> (player) -> {
                for (PlayerSpawnPhantomsCallback listener : listeners) {
                    listener.callback(player);
                }
            });

    static void invoke(ServerPlayer player) {
        EVENT.invoker().callback(player);
    }
}
