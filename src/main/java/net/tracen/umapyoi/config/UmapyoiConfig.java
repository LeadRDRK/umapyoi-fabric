package net.tracen.umapyoi.config;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.config.helper.*;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

public class UmapyoiConfig {
    public static ConfigClassHandler<UmapyoiConfig> HANDLER = ConfigClassHandler.createBuilder(UmapyoiConfig.class)
            .id(Umapyoi.id("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("umapyoi.json5"))
                    .setJson5(true)
                    .build())
            .build();

    public static final int DEFAULT_GACHA_PROBABILITY_SUM = 100;
    public static final int DEFAULT_GACHA_PROBABILITY_R = 70;
    public static final int DEFAULT_GACHA_PROBABILITY_SR = 20;
    public static final int DEFAULT_GACHA_PROBABILITY_SSR = 10;

    @SuppressWarnings("unused")
    public static Component percentFormatter(Double value) {
        return Component.literal(Math.round(value * 100.D) + "%");
    }

    /* General config */
    @Category("general")

    @SerialEntry
    @IntegerField(min = 18)
    public int STAT_LIMIT_VALUE = 18;
    @SerialEntry
    @DoubleSlider(min = 0.0, max = 1.0, step = 0.01, valueFormatter = "percentFormatter")
    public double STAT_LIMIT_REDUCTION_RATE = 0.6D;

    @SerialEntry
    @DoubleSlider(min = 0.0, max = 1.0, step = 0.01, valueFormatter = "percentFormatter")
    public double CHANCE_MOTIVATION_EFFECT = 0.5;
    @SerialEntry
    @DoubleField(min = 0.0)
    public double DAMAGE_MOTIVATION_EFFECT = 4.0;

    @SerialEntry
    @IntegerField(min = 3)
    public int GACHA_PROBABILITY_SUM = DEFAULT_GACHA_PROBABILITY_SUM;
    @SerialEntry
    @IntegerField(min = 1)
    public int GACHA_PROBABILITY_R = DEFAULT_GACHA_PROBABILITY_R;
    @SerialEntry
    @IntegerField(min = 1)
    public int GACHA_PROBABILITY_SR = DEFAULT_GACHA_PROBABILITY_SR;
    @SerialEntry
    @IntegerField(min = 1)
    public int GACHA_PROBABILITY_SSR = DEFAULT_GACHA_PROBABILITY_SSR;

    @SerialEntry
    @DoubleField(min = 0.0)
    public double UMASOUL_MAX_SPEED = 1.20;
    @SerialEntry
    @DoubleField(min = 0.0)
    public double UMASOUL_MAX_STRENGTH_ATTACK = 2.0;
    @SerialEntry
    @DoubleField(min = 0.0)
    public double UMASOUL_MAX_STAMINA_HEALTH = 20;
    @SerialEntry
    @DoubleField(min = 0.0)
    public double UMASOUL_MAX_GUTS_ARMOR = 5.0;
    @SerialEntry
    @DoubleField(min = 0.0)
    public double UMASOUL_MAX_GUTS_ARMOR_TOUGHNESS = 4.0;

    @SerialEntry
    public boolean UMASOUL_SPEED_PRECENT_ENABLE = true;
    @SerialEntry
    public boolean UMASOUL_STRENGTH_PRECENT_ENABLE = true;
    @SerialEntry
    public boolean UMASOUL_STAMINA_PRECENT_ENABLE = false;
    @SerialEntry
    public boolean UMASOUL_GUTS_PRECENT_ENABLE = false;

    @SerialEntry
    @DoubleSlider(min = 0.0, max = 1.0, step = 0.01, valueFormatter = "percentFormatter")
    public double ACUPUNCTUIST_SUPPORT_CHANCE = 0.4;
    @SerialEntry
    @DoubleSlider(min = 0.0, max = 1.0, step = 0.01, valueFormatter = "percentFormatter")
    public double SLOW_METABOLISM_PROBABILITY = 0.05d;
    @SerialEntry
    @LongField(min = 0L)
    public long NIGHT_OWL_THRESHOLD = 72000L;
    @SerialEntry
    @DoubleSlider(min = 0.0, max = 1.0, step = 0.01, valueFormatter = "percentFormatter")
    public double NIGHT_OWL_PROBABILITY_DOWN_MOTIVATION = 0.01d;

    @SerialEntry
    public boolean GRANT_GUIDE_ON_FIRST_JOIN = true;

    /* Client config */
    @Category("client")

    @SerialEntry
    public boolean VANILLA_ARMOR_RENDER = false;
    @SerialEntry
    public boolean HIDE_PARTS_RENDER = false;
    @SerialEntry
    public boolean ELYTRA_RENDER = true;

    @SerialEntry
    @IntegerField(min = 10)
    public int EAR_ANIMATION_INTERVAL = 100;
    @SerialEntry
    @IntegerField(min = 10)
    public int TAIL_ANIMATION_INTERVAL = 200;

    @SerialEntry
    public boolean OVERLAY_SWITCH = true;
    @SerialEntry
    public boolean TOOLTIP_SWITCH = true;

    @SerialEntry
    public boolean DISPLAY_DETAIL = false;

    @SerialEntry
    public int TOPLEFT_COORD_SKILL_X = 102;
    @SerialEntry
    public int TOPLEFT_COORD_SKILL_Y = -21;
    @SerialEntry
    public int TOPLEFT_COORD_MOTIVATION_X = 118;
    @SerialEntry
    public int TOPLEFT_COORD_MOTIVATION_Y = -37;
}
