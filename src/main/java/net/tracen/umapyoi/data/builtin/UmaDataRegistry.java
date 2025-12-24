package net.tracen.umapyoi.data.builtin;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.registry.LazyRegistrar;
import net.tracen.umapyoi.registry.RegistryObject;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.utils.GachaRanking;

import java.util.function.Supplier;

public class UmaDataRegistry {
    public static final LazyRegistrar<UmaData> UMA_DATA = LazyRegistrar.create(UmaData.REGISTRY_KEY,
            Umapyoi.MODID);
    public static final Supplier<Registry<UmaData>> UMA_DATA_REGISTRY = UMA_DATA.makeRegistry();

    public static final RegistryObject<UmaData> AGNES_DIGITAL = UMA_DATA.register("agnes_digital",
            () -> UmaDataRegistry.createNewUmamusume("agnes_digital", GachaRanking.SR, new int[] {8, 8, 7, 0, 7}));
    
    public static final RegistryObject<UmaData> COMMON_UMA = UMA_DATA.register("common_uma",
            () -> UmaDataRegistry.createNewUmamusume("common_uma", GachaRanking.R));
    
    public static final RegistryObject<UmaData> COMMON_UMA_A = UMA_DATA.register("common_uma_a",
            () -> UmaDataRegistry.createNewUmamusume("common_uma_a", GachaRanking.R));
    
    public static final RegistryObject<UmaData> COMMON_UMA_B = UMA_DATA.register("common_uma_b",
            () -> UmaDataRegistry.createNewUmamusume("common_uma_b", GachaRanking.R));
    
    public static final RegistryObject<UmaData> COMMON_UMA_C = UMA_DATA.register("common_uma_c",
            () -> UmaDataRegistry.createNewUmamusume("common_uma_c", GachaRanking.R));

    public static final RegistryObject<UmaData> GOLD_SHIP = UMA_DATA.register("gold_ship",
            () -> UmaDataRegistry.createNewUmamusume("gold_ship", GachaRanking.SR, new int[] {0, 20, 10, 0, 0}));

    public static final RegistryObject<UmaData> SPECIAL_WEEK = UMA_DATA.register("special_week",
            () -> UmaDataRegistry.createNewUmamusume("special_week", GachaRanking.SR, new int[] {0, 20, 0, 0, 10}));

    public static final RegistryObject<UmaData> TOKAI_TEIO = UMA_DATA.register("tokai_teio",
            () -> UmaDataRegistry.createNewUmamusume("tokai_teio", GachaRanking.SR, new int[] {20, 10, 0, 0, 0}));

    public static final RegistryObject<UmaData> OGURI_CAP = UMA_DATA.register("oguri_cap",
            () -> UmaDataRegistry.createNewUmamusume("oguri_cap", GachaRanking.SR, new int[] {20, 0, 10, 0, 0}));

    public static final RegistryObject<UmaData> SAKURA_CHIYONO_O = UMA_DATA.register("sakura_chiyono_o",
            () -> UmaDataRegistry.createNewUmamusume("sakura_chiyono_o", GachaRanking.SR, new int[] {10, 0, 0, 10, 10}));

    public static final RegistryObject<UmaData> OGURI_CAP_XMAS = UMA_DATA.register("oguri_cap_xmas",
            () -> UmaDataRegistry.createNewUmamusume("oguri_cap", GachaRanking.SSR, new int[] {15, 15, 0, 0, 10}));

    public static final RegistryObject<UmaData> AGNES_TACHYON = UMA_DATA.register("agnus_tachyon",
            () -> UmaDataRegistry.createNewUmamusume("agnus_tachyon", GachaRanking.SR, new int[] {20, 0, 0, 10, 0}));

    public static final RegistryObject<UmaData> HARU_URARA = UMA_DATA.register("haru_urara",
            () -> UmaDataRegistry.createNewUmamusume("haru_urara", GachaRanking.SR, new int[] {0, 0, 10, 20, 0}));

    public static final RegistryObject<UmaData> TAMAMO_CROSS = UMA_DATA.register("tamamo_cross",
            () -> UmaDataRegistry.createNewUmamusume("tamamo_cross", GachaRanking.SR, new int[] {0, 20, 10, 0, 0}));

    public static final RegistryObject<UmaData> SEIUN_SKY = UMA_DATA.register("seiun_sky",
            () -> UmaDataRegistry.createNewUmamusume("seiun_sky", GachaRanking.SR, new int[] {0, 10, 0, 0, 20}));

    public static final RegistryObject<UmaData> MATIKANEFUKUKITARU = UMA_DATA.register("matikanefukukitaru",
            () -> UmaDataRegistry.createNewUmamusume("matikanefukukitaru", GachaRanking.SR, new int[] {0, 20, 10, 0, 0}));

    public static final RegistryObject<UmaData> RICE_SHOWER = UMA_DATA.register("rice_shower",
            () -> UmaDataRegistry.createNewUmamusume("rice_shower", GachaRanking.SR, new int[] {0, 10, 0, 20, 0}));

    public static final RegistryObject<UmaData> VODKA = UMA_DATA.register("vodka",
            () -> UmaDataRegistry.createNewUmamusume("vodka", GachaRanking.SR, new int[] {10, 0, 20, 0, 0}));

    public static final RegistryObject<UmaData> SAKURA_BAKUSHIN_O = UMA_DATA.register("sakura_bakushin_o",
            () -> UmaDataRegistry.createNewUmamusume("sakura_bakushin_o", GachaRanking.SR, new int[] {20, 0, 0, 0, 10}));

    public static final RegistryObject<UmaData> MANHATTAN_CAFE = UMA_DATA.register("manhattan_cafe",
            () -> UmaDataRegistry.createNewUmamusume("manhattan_cafe", GachaRanking.SR, new int[] {0, 30, 0, 0, 0}));

    public static final RegistryObject<UmaData> MEJIRO_ARDAN = UMA_DATA.register("mejiro_ardan",
            () -> UmaDataRegistry.createNewUmamusume("mejiro_ardan", GachaRanking.SR, new int[] {10, 0, 0, 0, 20}));

    public static final RegistryObject<UmaData> DAITAKU_HELIOS = UMA_DATA.register("daitaku_helios",
            () -> UmaDataRegistry.createNewUmamusume("daitaku_helios", GachaRanking.SR, new int[] {15, 0, 15, 0, 0}));

    public static final RegistryObject<UmaData> SWEEP_TOSHO = UMA_DATA.register("sweep_tosho",
            () -> UmaDataRegistry.createNewUmamusume("sweep_tosho", GachaRanking.SR, new int[] {10, 0, 20, 0, 0}));

    public static final RegistryObject<UmaData> GOLD_CITY = UMA_DATA.register("gold_city",
            () -> UmaDataRegistry.createNewUmamusume("gold_city", GachaRanking.SR, new int[] {0, 0, 10, 20, 0}));

    public static final RegistryObject<UmaData> GOLD_SHIP_WATER = UMA_DATA.register("gold_ship_water",
            () -> UmaDataRegistry.createNewUmamusume("gold_ship", GachaRanking.SSR, new int[] {0, 0, 20, 0, 20}));

    public static final RegistryObject<UmaData> MR_CB = UMA_DATA.register("mr_cb",
            () -> UmaDataRegistry.createNewUmamusume("mr_cb", GachaRanking.SR, new int[] {10, 10, 0, 0, 10}));

    public static final RegistryObject<UmaData> GRASS_WONDER = UMA_DATA.register("grass_wonder",
            () -> UmaDataRegistry.createNewUmamusume("grass_wonder", GachaRanking.SR, new int[] {20, 0, 10, 0, 0}));

    public static final RegistryObject<UmaData> CURREN_CHAN = UMA_DATA.register("curren_chan",
            () -> UmaDataRegistry.createNewUmamusume("curren_chan", GachaRanking.SR, new int[] {10, 0, 20, 0, 0}));

    public static final RegistryObject<UmaData> SILENCE_SUZUKA = UMA_DATA.register("silence_suzuka",
            () -> UmaDataRegistry.createNewUmamusume("silence_suzuka", GachaRanking.SR, new int[] {20, 0, 0, 10, 0}));

    public static final RegistryObject<UmaData> TAMAMO_CROSS_FESTIVAL = UMA_DATA.register("tamamo_cross_festival",
            () -> UmaDataRegistry.createNewUmamusume("tamamo_cross", GachaRanking.SSR, new int[] {15, 10, 0, 15, 0}));

    public static final RegistryObject<UmaData> ASTON_MACHAN = UMA_DATA.register("aston_machan",
            () -> UmaDataRegistry.createNewUmamusume("aston_machan", GachaRanking.SR, new int[] {20, 0, 0, 10, 0}));
    
    public static final RegistryObject<UmaData> KITASAN_BLACK = UMA_DATA.register("kitasan_black",
            () -> UmaDataRegistry.createNewUmamusume("kitasan_black", GachaRanking.SR, new int[] {20, 10, 0, 0, 0}));

    public static final RegistryObject<UmaData> SATONO_DIAMOND = UMA_DATA.register("satono_diamond",
            () -> UmaDataRegistry.createNewUmamusume("satono_diamond", GachaRanking.SR, new int[] {0, 15, 0, 0, 15}));

    public static final RegistryObject<UmaData> NICE_NATURE = UMA_DATA.register("nice_nature",
            () -> UmaDataRegistry.createNewUmamusume("nice_nature", GachaRanking.SR, new int[] {0, 0, 20, 0, 10}));

    public static final RegistryObject<UmaData> MAYANO_TOP_GUN = UMA_DATA.register("mayano_top_gun",
            () -> UmaDataRegistry.createNewUmamusume("mayano_top_gun", GachaRanking.SR, new int[] {0, 20, 0, 10, 0}));
    
    public static final RegistryObject<UmaData> NEO_UNIVERSE = UMA_DATA.register("neo_universe",
            () -> UmaDataRegistry.createNewUmamusume("neo_universe", GachaRanking.SR, new int[] {0, 0, 0, 0, 30}));
    
    public static final RegistryObject<UmaData> MEISHO_DOTOU = UMA_DATA.register("meisho_dotou",
            () -> UmaDataRegistry.createNewUmamusume("meisho_dotou", GachaRanking.SR, new int[] {0, 20, 0, 10, 0}));
    
    public static final RegistryObject<UmaData> TAIKI_SHUTTLE = UMA_DATA.register("taiki_shuttle",
            () -> UmaDataRegistry.createNewUmamusume("taiki_shuttle", GachaRanking.SR, new int[] {20, 0, 0, 0, 10}));
    
    public static final RegistryObject<UmaData> CURREN_CHAN_DRESS = UMA_DATA.register("curren_chan_dress",
            () -> UmaDataRegistry.createNewUmamusume("curren_chan", GachaRanking.SSR, new int[] {10, 0, 20, 0, 10}));
    
    public static final RegistryObject<UmaData> MEJIRO_MCQUEEN = UMA_DATA.register("mejiro_mcqueen",
            () -> UmaDataRegistry.createNewUmamusume("mejiro_mcqueen", GachaRanking.SR, new int[] {0, 20, 0, 0, 10}));
    
    public static final RegistryObject<UmaData> COPANO_RICKEY = UMA_DATA.register("copano_rickey",
            () -> UmaDataRegistry.createNewUmamusume("copano_rickey", GachaRanking.SR, new int[] {0, 0, 10, 0, 20}));
    
    public static final RegistryObject<UmaData> SYMBOLI_RUDOLF = UMA_DATA.register("symboli_rudolf",
            () -> UmaDataRegistry.createNewUmamusume("symboli_rudolf", GachaRanking.SR, new int[] {0, 20, 0, 10, 0}));
    
    public static final RegistryObject<UmaData> NARITA_TOP_ROAD = UMA_DATA.register("narita_top_road",
            () -> UmaDataRegistry.createNewUmamusume("narita_top_road", GachaRanking.SR, new int[] {20, 10, 0, 0, 0}));
    
    public static final RegistryObject<UmaData> VENUS_PARK = UMA_DATA.register("venus_park",
            () -> UmaDataRegistry.createNewUmamusume("venus_park", GachaRanking.EASTER_EGG, new int[] {10, 10, 10, 10, 10}));
    
    public static final RegistryObject<UmaData> AGNES_TACHYON_SWIM = UMA_DATA.register("agnus_tachyon_swim",
            () -> UmaDataRegistry.createNewUmamusume("agnus_tachyon", GachaRanking.SSR, new int[] {15, 0, 10, 0, 15}));
    
    public static final RegistryObject<UmaData> MIHONO_BOURBON = UMA_DATA.register("mihono_bourbon",
            () -> UmaDataRegistry.createNewUmamusume("mihono_bourbon", GachaRanking.SR, new int[] {0, 20, 10, 0, 0}));
    
    public static final RegistryObject<UmaData> MATIKANETANNHAUSER = UMA_DATA.register("matikanetannhauser",
            () -> UmaDataRegistry.createNewUmamusume("matikanetannhauser", GachaRanking.SR, new int[] {0, 20, 0, 10, 0}));
    
    public static final RegistryObject<UmaData> KAWAKAMI_PRINCESS = UMA_DATA.register("kawakami_princess",
            () -> UmaDataRegistry.createNewUmamusume("kawakami_princess", GachaRanking.SR, new int[] {0, 10, 0, 20, 0}));
    
    public static final RegistryObject<UmaData> TWIN_TURBO = UMA_DATA.register("twinturbo",
            () -> UmaDataRegistry.createNewUmamusume("twinturbo", GachaRanking.SR, new int[] {30, 0, 0, 0, 0}));
    
    public static final RegistryObject<UmaData> LITTLE_COCON = UMA_DATA.register("little_cocon",
            () -> UmaDataRegistry.createNewUmamusume("little_cocon", GachaRanking.SR, new int[] {10, 0, 10, 0, 10}));
    
    public static final RegistryObject<UmaData> SAKURA_LAUREL = UMA_DATA.register("sakura_laurel",
            () -> UmaDataRegistry.createNewUmamusume("sakura_laurel", GachaRanking.SR, new int[] {0, 20, 10, 0, 0}));
    
    public static final RegistryObject<UmaData> FINE_MOTION = UMA_DATA.register("fine_motion",
            () -> UmaDataRegistry.createNewUmamusume("fine_motion", GachaRanking.SR, new int[] {0, 0, 15, 0, 15}));
    
    public static final RegistryObject<UmaData> TM_OPERA_O = UMA_DATA.register("tm_opera_o",
            () -> UmaDataRegistry.createNewUmamusume("tm_opera_o", GachaRanking.SR, new int[] {0, 20, 0, 0, 10}));
    
    public static final RegistryObject<UmaData> ADMIRE_VEGA = UMA_DATA.register("admire_vega",
            () -> UmaDataRegistry.createNewUmamusume("admire_vega", GachaRanking.SR, new int[] {10, 0, 20, 0, 0}));
    
    public static final RegistryObject<UmaData> JUNGLE_POCKET = UMA_DATA.register("jungle_pocket",
            () -> UmaDataRegistry.createNewUmamusume("jungle_pocket", GachaRanking.SR, new int[] {10, 0, 20, 0, 0}));
    
    public static final RegistryObject<UmaData> NARITA_TAISHIN = UMA_DATA.register("narita_taishin",
            () -> UmaDataRegistry.createNewUmamusume("narita_taishin", GachaRanking.SR, new int[] {10, 0, 0, 20, 0}));
    
    public static final RegistryObject<UmaData> GOLD_CITY_AUTUMN = UMA_DATA.register("gold_city_autumn",
            () -> UmaDataRegistry.createNewUmamusume("gold_city", GachaRanking.SSR, new int[] {10, 0, 15, 0, 15}));
    
    public static final RegistryObject<UmaData> GRASS_WONDER_UMANET = UMA_DATA.register("grass_wonder_umanet",
            () -> UmaDataRegistry.createNewUmamusume("grass_wonder", GachaRanking.SSR, new int[] {15, 0, 10, 0, 15}));
    
    public static final RegistryObject<UmaData> SATONO_DIAMOND_FRENCH = UMA_DATA.register("satono_diamond_french",
            () -> UmaDataRegistry.createNewUmamusume("satono_diamond", GachaRanking.SSR, new int[] {10, 15, 0, 15, 0}));
    
    public static final RegistryObject<UmaData> SYAMEIMARU_ZHENG = UMA_DATA.register("syameimaru_zheng",
            () -> UmaDataRegistry.createNewUmamusume("syameimaru_zheng", GachaRanking.EASTER_EGG, new int[] {20, 0, 0, 10, 0}));
    
    public static final RegistryObject<UmaData> DUMNHEINT = UMA_DATA.register("dumnheint",
            () -> UmaDataRegistry.createNewUmamusume("dumnheint", GachaRanking.EASTER_EGG, new int[] {10, 0, 20, 0, 0}));
    
    public static final RegistryObject<UmaData> DARLEY_ARABIAN = UMA_DATA.register("darley_arabian",
            () -> UmaDataRegistry.createNewUmamusume("darley_arabian", GachaRanking.EASTER_EGG, new int[] {10, 10, 10, 10, 10}));
    
    public static final RegistryObject<UmaData> GODOLPHIN_BARB = UMA_DATA.register("godolphin_barb",
            () -> UmaDataRegistry.createNewUmamusume("godolphin_barb", GachaRanking.EASTER_EGG, new int[] {25, 0, 0, 0, 25}));
    
    public static final RegistryObject<UmaData> BYERLEY_TURK = UMA_DATA.register("byerley_turk",
            () -> UmaDataRegistry.createNewUmamusume("byerley_turk", GachaRanking.EASTER_EGG, new int[] {0, 15, 20, 15, 0}));

    public static final RegistryObject<UmaData> SMART_FALCON = UMA_DATA.register("smart_falcon",
            () -> UmaDataRegistry.createNewUmamusume("smart_falcon", GachaRanking.SR, new int[] {20, 0, 10, 0, 0}));

    public static final RegistryObject<UmaData> MANHATTAN_CAFE_VALENTINE = UMA_DATA.register("manhattan_cafe_valentine",
            () -> UmaDataRegistry.createNewUmamusume("manhattan_cafe", GachaRanking.SSR, new int[] {0, 20, 20, 0, 0}));

    public static final RegistryObject<UmaData> HISHI_MIRACLE = UMA_DATA.register("hishi_miracle",
            () -> UmaDataRegistry.createNewUmamusume("hishi_miracle", GachaRanking.SR, new int[] {7, 8, 7, 8, 0}));

    public static final RegistryObject<UmaData> DAIWA_SCARLET = UMA_DATA.register("daiwa_scarlet",
            () -> UmaDataRegistry.createNewUmamusume("daiwa_scarlet", GachaRanking.SR, new int[] {10, 0, 0, 20, 0}));

    public static final RegistryObject<UmaData> WIN_VARIATION = UMA_DATA.register("win_variation",
            () -> UmaDataRegistry.createNewUmamusume("win_variation", GachaRanking.SR, new int[] {10, 0, 0, 20, 0}));

    public static final RegistryObject<UmaData> EL_CONDOR_PASA = UMA_DATA.register("el_condor_pasa",
            () -> UmaDataRegistry.createNewUmamusume("el_condor_pasa", GachaRanking.SR, new int[] {20, 0, 0, 0, 10}));

    public static final RegistryObject<UmaData> HOKKO_TARUMAE = UMA_DATA.register("hokko_tarumae",
            () -> UmaDataRegistry.createNewUmamusume("hokko_tarumae", GachaRanking.SR, new int[] {0, 20, 10, 0, 0}));

    public static final RegistryObject<UmaData> KING_HALO = UMA_DATA.register("king_halo",
            () -> UmaDataRegistry.createNewUmamusume("king_halo", GachaRanking.SR, new int[] {20, 0, 10, 0, 0}));

    public static final RegistryObject<UmaData> KING_HALO_WEDDING = UMA_DATA.register("king_halo_wedding",
            () -> UmaDataRegistry.createNewUmamusume("king_halo", GachaRanking.SSR, new int[] {10, 0, 10, 10, 10}));

    public static final RegistryObject<UmaData> MATIKANETANNHAUSER_SPORTS = UMA_DATA.register("matikanetannhauser_sports",
            () -> UmaDataRegistry.createNewUmamusume("matikanetannhauser", GachaRanking.SSR, new int[] {20, 0, 20, 0, 0}));

    public static final RegistryObject<UmaData> CHEVAL_GRAND = UMA_DATA.register("cheval_grand",
            () -> UmaDataRegistry.createNewUmamusume("cheval_grand", GachaRanking.SR, new int[] {0, 10, 0, 10, 10}));

    public static final RegistryObject<UmaData> VERXINA = UMA_DATA.register("verxina",
            () -> UmaDataRegistry.createNewUmamusume("verxina", GachaRanking.SR, new int[] {10, 0, 0, 10, 10}));

    public static final RegistryObject<UmaData> VIVLOS = UMA_DATA.register("vivlos",
            () -> UmaDataRegistry.createNewUmamusume("vivlos", GachaRanking.SR, new int[] {10, 0, 10, 0, 10}));

    public static final RegistryObject<UmaData> FUJI_KISEKI = UMA_DATA.register("fuji_kiseki",
            () -> UmaDataRegistry.createNewUmamusume("fuji_kiseki", GachaRanking.SR, new int[] {0, 0, 20, 0, 10}));

    public static final RegistryObject<UmaData> FUJIMASA_MARCH = UMA_DATA.register("fujimasa_march",
            () -> UmaDataRegistry.createNewUmamusume("fujimasa_march", GachaRanking.SR, new int[] {6, 6, 6, 6, 6}));

    public static final RegistryObject<UmaData> HOKKO_TARUMAE_SWIM = UMA_DATA.register("hokko_tarumae_swim",
            () -> UmaDataRegistry.createNewUmamusume("hokko_tarumae", GachaRanking.SSR, new int[] {20, 10, 0, 0, 10}));

    public static final RegistryObject<UmaData> MEJIRO_PALMER = UMA_DATA.register("mejiro_palmer",
            () -> UmaDataRegistry.createNewUmamusume("mejiro_palmer", GachaRanking.SR, new int[] {10, 10, 0, 10, 0}));

    public static final RegistryObject<UmaData> TRANSCEND = UMA_DATA.register("transcend",
            () -> UmaDataRegistry.createNewUmamusume("transcend", GachaRanking.SR, new int[] {10, 0, 10, 0, 10}));

    public static final RegistryObject<UmaData> DURANDAL = UMA_DATA.register("durandal",
            () -> UmaDataRegistry.createNewUmamusume("durandal", GachaRanking.SR, new int[] {10, 0, 20, 0, 0}));

    public static final RegistryObject<UmaData> CALSTONE_LIGHT_O = UMA_DATA.register("calstone_light_o",
            () -> UmaDataRegistry.createNewUmamusume("calstone_light_o", GachaRanking.SR, new int[] {15, 0, 15, 0, 0}));

    public static final RegistryObject<UmaData> DAIICHI_RUBY = UMA_DATA.register("daiichi_ruby",
            () -> UmaDataRegistry.createNewUmamusume("daiichi_ruby", GachaRanking.SR, new int[] {0, 0, 20, 0, 10}));

    public static final RegistryObject<UmaData> KATSURAGI_ACE = UMA_DATA.register("katsuragi_ace",
            () -> UmaDataRegistry.createNewUmamusume("katsuragi_ace", GachaRanking.SR, new int[] {10, 0, 10, 10, 0}));

    public static final RegistryObject<UmaData> HAPPY_MEEK = UMA_DATA.register("happy_meek",
            () -> UmaDataRegistry.createNewUmamusume("happy_meek", GachaRanking.SR, new int[] {6, 6, 6, 6, 6}));

    public static final RegistryObject<UmaData> STILL_IN_LOVE = UMA_DATA.register("still_in_love",
            () -> UmaDataRegistry.createNewUmamusume("still_in_love", GachaRanking.SR, new int[] {20, 0, 0, 10, 0}));

    public static final RegistryObject<UmaData> RHEIN_KRAFT = UMA_DATA.register("rhein_kraft",
            () -> UmaDataRegistry.createNewUmamusume("rhein_kraft", GachaRanking.SR, new int[] {0, 0, 15, 15, 0}));

    public static final RegistryObject<UmaData> BUENA_VISTA = UMA_DATA.register("buena_vista",
            () -> UmaDataRegistry.createNewUmamusume("buena_vista", GachaRanking.SR, new int[] {0, 0, 15, 15, 0}));

    public static final RegistryObject<UmaData> KS_MIRACLE = UMA_DATA.register("ks_miracle",
            () -> UmaDataRegistry.createNewUmamusume("ks_miracle", GachaRanking.SR, new int[] {15, 0, 0, 15, 0}));

    public static final RegistryObject<UmaData> EISHIN_FLASH = UMA_DATA.register("eishin_flash",
            () -> UmaDataRegistry.createNewUmamusume("eishin_flash", GachaRanking.SR, new int[] {0, 0, 10, 0, 20}));

    public static final RegistryObject<UmaData> MIYA_YOMOGI = UMA_DATA.register("miya_yomogi",
            () -> UmaDataRegistry.createNewUmamusume("miya_yomogi", GachaRanking.EASTER_EGG, new int[] {10, 0, 0, 20, 20}));

    public static final RegistryObject<UmaData> YAMANIN_ZEPHYR = UMA_DATA.register("yamanin_zephyr",
            () -> UmaDataRegistry.createNewUmamusume("yamanin_zephyr", GachaRanking.SR, new int[] {10, 0, 0, 10, 10}));

    public static final RegistryObject<UmaData> SATONO_CROWN = UMA_DATA.register("satono_crown",
            () -> UmaDataRegistry.createNewUmamusume("satono_crown", GachaRanking.SR, new int[] {0, 0, 15, 15, 0}));

    public static final RegistryObject<UmaData> ALMOND_EYE = UMA_DATA.register("almond_eye",
            () -> UmaDataRegistry.createNewUmamusume("almond_eye", GachaRanking.SSR, new int[] {10, 5, 10, 10, 5}));

    public static final RegistryObject<UmaData> FUSAICHI_PANDORA = UMA_DATA.register("fusaichi_pandora",
            () -> UmaDataRegistry.createNewUmamusume("fusaichi_pandora", GachaRanking.SR, new int[] {0, 0, 15, 15, 0}));

    public static final RegistryObject<UmaData> MEJIRO_RYAN = UMA_DATA.register("mejiro_ryan",
            () -> UmaDataRegistry.createNewUmamusume("mejiro_ryan", GachaRanking.SR, new int[] {0, 0, 20, 0, 10}));

    public static final RegistryObject<UmaData> TYCHE = UMA_DATA.register("tyche",
            () -> UmaDataRegistry.createNewUmamusume("tyche", GachaRanking.EASTER_EGG, new int[] {20, 20, 0, 10, 0}));

    public static final RegistryObject<UmaData> NICE_NATURE_CHEER = UMA_DATA.register("nice_nature_cheer",
            () -> UmaDataRegistry.createNewUmamusume("nice_nature", GachaRanking.SSR, new int[] {0, 10, 20, 0, 10}));

    public static final RegistryObject<UmaData> HISHI_AKEBONO = UMA_DATA.register("hishi_akebono",
            () -> UmaDataRegistry.createNewUmamusume("hishi_akebono", GachaRanking.SR, new int[] {0, 0, 20, 10, 0}));

    public static final RegistryObject<UmaData> SHENONE_SUZUNA = UMA_DATA.register("shenone_suzuna",
            () -> UmaDataRegistry.createNewUmamusume("shenone_suzuna", GachaRanking.EASTER_EGG, new int[] {20, 10, 5, 0, 10}));

    public static final RegistryObject<UmaData> VIVLOS_SWIM = UMA_DATA.register("vivlos_swim",
            () -> UmaDataRegistry.createNewUmamusume("vivlos", GachaRanking.SSR, new int[] {10, 0, 0, 10, 20}));

    public static final RegistryObject<UmaData> MARUZENSKY = UMA_DATA.register("maruzensky",
            () -> UmaDataRegistry.createNewUmamusume("maruzensky", GachaRanking.SR, new int[] {10, 0, 0, 0, 20}));

    public static UmaData createNewUmamusume(String name, GachaRanking ranking) {
        return new UmaData(new ResourceLocation(Umapyoi.MODID, name), ranking, new int[] { 1, 1, 1, 1, 1 },
                new int[] { 18, 18, 18, 18, 18 }, new int[] { 0, 0, 0, 0, 0 }, new ResourceLocation(Umapyoi.MODID, "basic_pace"));
    }
    
    public static UmaData createNewUmamusume(String name, GachaRanking ranking, int[] rate) {
        return new UmaData(new ResourceLocation(Umapyoi.MODID, name), ranking, new int[] { 1, 1, 1, 1, 1 },
                new int[] { 18, 18, 18, 18, 18 }, rate,new ResourceLocation(Umapyoi.MODID, "basic_pace"));
    }
}
