package net.tracen.umapyoi.data.tag;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.utils.TagUtils;

public class UmapyoiItemTags {
    public static final TagKey<Item> SHOULD_RENDER = TagUtils.modItemTag(Umapyoi.MODID, "should_render");

    public static final TagKey<Item> COMMON_GACHA_ITEM = TagUtils.modItemTag(Umapyoi.MODID, "common_gacha_item");
    
    public static final TagKey<Item> HORSESHOE = TagUtils.modItemTag(Umapyoi.MODID, "horseshoe");
    
    public static final TagKey<Item> UMA_TICKET = TagUtils.modItemTag(Umapyoi.MODID, "uma_ticket");
    public static final TagKey<Item> SR_UMA_TICKET = TagUtils.modItemTag(Umapyoi.MODID, "sr_uma_ticket");
    public static final TagKey<Item> SSR_UMA_TICKET = TagUtils.modItemTag(Umapyoi.MODID, "ssr_uma_ticket");

    public static final TagKey<Item> CARD_TICKET = TagUtils.modItemTag(Umapyoi.MODID, "card_ticket");
    public static final TagKey<Item> SR_CARD_TICKET = TagUtils.modItemTag(Umapyoi.MODID, "sr_card_ticket");
    public static final TagKey<Item> SSR_CARD_TICKET = TagUtils.modItemTag(Umapyoi.MODID, "ssr_card_ticket");

    // FIXME: these tags are fabric api builtins on version >= 1.20.5

    public static final TagKey<Item> WATER = TagUtils.cItemTag("water");
    public static final TagKey<Item> SUGAR = TagUtils.cItemTag("sugar");
    public static final TagKey<Item> MILK = TagUtils.cItemTag("milk");
    
    public static final TagKey<Item> BREAD = TagUtils.cItemTag("bread");
    public static final TagKey<Item> BREAD_WHEAT = TagUtils.cItemTag("bread/wheat");

    public static final TagKey<Item> BAMBOO = TagUtils.cItemTag("bamboo");

    public static final TagKey<Item> VEGETABLES_CARROT = TagUtils.cItemTag("vegetables/carrot");

    public static final TagKey<Item> STONES = TagUtils.cItemTag("stones");
}
