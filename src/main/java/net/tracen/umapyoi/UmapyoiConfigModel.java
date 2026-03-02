package net.tracen.umapyoi;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.SectionHeader;

@Modmenu(modId = "umapyoi")
@Config(name = "umapyoi", wrapperName = "UmapyoiConfig")
public class UmapyoiConfigModel {
    public static final int DEFAULT_GACHA_PROBABILITY_SUM = 100;
    public static final int DEFAULT_GACHA_PROBABILITY_R = 70;
    public static final int DEFAULT_GACHA_PROBABILITY_SR = 20;
    public static final int DEFAULT_GACHA_PROBABILITY_SSR = 10;

    /* General config */
    @SectionHeader("general")

    @RangeConstraint(min = 18, max = Integer.MAX_VALUE)
    public int STAT_LIMIT_VALUE = 18;
    @RangeConstraint(min = 0.0D, max = 1.0D)
    public double STAT_LIMIT_REDUCTION_RATE = 0.6D;

    @RangeConstraint(min = 0.0, max = 1.0)
    public double CHANCE_MOTIVATION_EFFECT = 0.5;
    @RangeConstraint(min = 0.0, max = Double.MAX_VALUE)
    public double DAMAGE_MOTIVATION_EFFECT = 4.0;

    @RangeConstraint(min = 3, max = Integer.MAX_VALUE)
    public int GACHA_PROBABILITY_SUM = DEFAULT_GACHA_PROBABILITY_SUM;
    @RangeConstraint(min = 1, max = Integer.MAX_VALUE)
    public int GACHA_PROBABILITY_R = DEFAULT_GACHA_PROBABILITY_R;
    @RangeConstraint(min = 1, max = Integer.MAX_VALUE)
    public int GACHA_PROBABILITY_SR = DEFAULT_GACHA_PROBABILITY_SR;
    @RangeConstraint(min = 1, max = Integer.MAX_VALUE)
    public int GACHA_PROBABILITY_SSR = DEFAULT_GACHA_PROBABILITY_SSR;

    @RangeConstraint(min = 0.0, max = Double.MAX_VALUE)
    public double UMASOUL_MAX_SPEED = 1.20;
    @RangeConstraint(min = 0.0, max = Double.MAX_VALUE)
    public double UMASOUL_MAX_STRENGTH_ATTACK = 2.0;
    @RangeConstraint(min = 0.0, max = Double.MAX_VALUE)
    public double UMASOUL_MAX_STAMINA_HEALTH = 20;
    @RangeConstraint(min = 0.0, max = Double.MAX_VALUE)
    public double UMASOUL_MAX_GUTS_ARMOR = 5.0;
    @RangeConstraint(min = 0.0, max = Double.MAX_VALUE)
    public double UMASOUL_MAX_GUTS_ARMOR_TOUGHNESS = 4.0;

    public boolean UMASOUL_SPEED_PRECENT_ENABLE = true;
    public boolean UMASOUL_STRENGTH_PRECENT_ENABLE = true;
    public boolean UMASOUL_STAMINA_PRECENT_ENABLE = false;
    public boolean UMASOUL_GUTS_PRECENT_ENABLE = false;

    @RangeConstraint(min = 0.0, max = 1.0)
    public double ACUPUNCTUIST_SUPPORT_CHANCE = 0.4;
    @RangeConstraint(min = 0.0, max = 1.0)
    public double SLOW_METABOLISM_PROBABILITY = 0.05d;
    @RangeConstraint(min = 0L, max = Long.MAX_VALUE)
    public long NIGHT_OWL_THRESHOLD = 72000L;
    @RangeConstraint(min = 0.0, max = 1.0)
    public double NIGHT_OWL_PROBABILITY_DOWN_MOTIVATION = 0.01d;

    public boolean GRANT_GUIDE_ON_FIRST_JOIN = true;

    /* Client config */
    @SectionHeader("client")

    public boolean VANILLA_ARMOR_RENDER = false;
    public boolean HIDE_PARTS_RENDER = false;
    public boolean ELYTRA_RENDER = true;

    @RangeConstraint(min = 10, max = Integer.MAX_VALUE)
    public int EAR_ANIMATION_INTERVAL = 100;
    @RangeConstraint(min = 10, max = Integer.MAX_VALUE)
    public int TAIL_ANIMATION_INTERVAL = 200;

    public boolean OVERLAY_SWITCH = true;
    public boolean TOOLTIP_SWITCH = true;

    public boolean DISPLAY_DETAIL = false;

    public int TOPLEFT_COORD_SKILL_X = 102;
    public int TOPLEFT_COORD_SKILL_Y = -21;
    public int TOPLEFT_COORD_MOTIVATION_X = 118;
    public int TOPLEFT_COORD_MOTIVATION_Y = -37;
}
