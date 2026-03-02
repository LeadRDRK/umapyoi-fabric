package net.tracen.umapyoi.advancements.trigger;

import net.minecraft.advancements.CriteriaTriggers;

public class TriggerRegistry {
    public static GrantBookOnFirstJoin GRANT_BOOK_ON_FIRST_JOIN = CriteriaTriggers.register(
            GrantBookOnFirstJoin.ID.toString(), new GrantBookOnFirstJoin());

    // dummy
    public static void registerAll() {}
}
