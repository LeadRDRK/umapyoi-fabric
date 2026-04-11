package net.tracen.umapyoi.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.api.UmapyoiAPI;
import net.tracen.umapyoi.item.data.DataComponentsTypeRegistry;
import net.tracen.umapyoi.item.data.GachaRankingData;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.umadata.Growth;
import net.tracen.umapyoi.registry.umadata.Motivations;
import net.tracen.umapyoi.registry.umadata.UmaData;
import net.tracen.umapyoi.registry.umadata.UmaDataBasicStatus;
import net.tracen.umapyoi.registry.umadata.UmaDataExtraStatus;
import net.tracen.umapyoi.registry.umadata.UmaDataRace;
import net.tracen.umapyoi.registry.umadata.UmaDataRaceStatus;
import net.tracen.umapyoi.registry.umadata.UmaDataSkills;
import net.tracen.umapyoi.registry.umadata.UmaDataTraining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class UmaSoulUtils {

    public static Component getTranslatedUmaName(ItemStack stack) {
        return UmaSoulUtils.getTranslatedUmaName(UmaSoulUtils.getName(stack));
    }

    public static Component getTranslatedUmaName(Identifier name) {
        return Component.translatable(Util.makeDescriptionId("umadata", name));
    }

    public static ItemStack initUmaSoul(ItemStack stack, Identifier name, UmaData data) {
        ItemStack result = stack.copy();
        result.set(DataComponentsTypeRegistry.DATA_LOCATION.get(), name);
        GachaRanking ranking = data.ranking();
        result.set(DataComponentsTypeRegistry.GACHA_RANKING.get(), new GachaRankingData(ranking));
        result.set(DataComponentsTypeRegistry.UMADATA_BASIC_STATUS.get(), UmaDataBasicStatus.init(data.property()));
        result.set(DataComponentsTypeRegistry.UMADATA_MAX_BASIC_STATUS.get(), UmaDataBasicStatus.init(data.maxProperty()));
        result.set(DataComponentsTypeRegistry.UMADATA_STATUS_RATE.get(), UmaDataBasicStatus.init(data.propertyRate()));
        result.set(DataComponentsTypeRegistry.UMADATA_SKILLS.get(), new UmaDataSkills(UmaDataSkills.DEFAULT.skillSlot(), 0,
                List.of(data.uniqueSkill())
        ));
        result.set(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get(),
                new UmaDataExtraStatus(data.property()[4] * 200, 0,
                        ResultRankingUtils.generateRanking(result), Motivations.NORMAL));
        result.set(DataComponents.RARITY,
                ranking == GachaRanking.SSR ? Rarity.EPIC : ranking == GachaRanking.SR ? Rarity.UNCOMMON : Rarity.COMMON
        );
        result.set(DataComponentsTypeRegistry.UMADATA_TRAINING.get(), new UmaDataTraining(1, 6));
        result.set(DataComponentsTypeRegistry.GROWTH.get(), Growth.UNTRAINED);
        result.set(DataComponentsTypeRegistry.UMADATA_RACE.get(), new UmaDataRace(data.surfaceAptitude(), data.distanceAptitude()));
        result.set(DataComponentsTypeRegistry.UMADATA_RACE_STATUS.get(), UmaDataRaceStatus.DEFAULT);
        return result;
    }

    public static Position getPosition(ItemStack stack, Level world) {
        UmaData umaData = UmapyoiAPI.getUmaDataRegistry(world).getOptional(UmaSoulUtils.getName(stack)).orElseGet(() -> {
            Umapyoi.getLogger().info("Warning: {} doesn't exist.", UmaSoulUtils.getName(stack));
            return UmaData.DEFAULT_UMA;
        });
        return umaData.position();
    }

    @Environment(EnvType.CLIENT)
    public static Position getPosition(ItemStack stack) {
        return getPosition(stack, Minecraft.getInstance().level);
    }


    public static List<Aptitude> getSurfaceAptitude(ItemStack stack) {
        return Optional.ofNullable(stack.get(DataComponentsTypeRegistry.UMADATA_RACE.get()))
                .map(UmaDataRace::surfaceAptitude)
                .filter(surfaceAptitude -> surfaceAptitude.size() >= 3)
                .orElseGet(() -> Arrays.stream(UmaData.DEFAULT_SURFACE_APTITUDE).toList());
    }

    public static List<Aptitude> getDistanceAptitude(ItemStack stack) {
        return Optional.ofNullable(stack.get(DataComponentsTypeRegistry.UMADATA_RACE.get()))
                .map(UmaDataRace::distanceAptitude)
                .filter(distanceAptitude -> distanceAptitude.size() >= 4)
                .orElseGet(() -> Arrays.stream(UmaData.DEFAULT_DISTANCE_APTITUDE).toList());
    }

    public static Identifier getName(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.DATA_LOCATION.get(), UmaData.DEFAULT_UMA_ID);
    }

    public static UmaDataBasicStatus getProperty(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.UMADATA_BASIC_STATUS.get(),
                UmaDataBasicStatus.DEFAULT_STATUS);
    }

    public static void setProperty(ItemStack stack, UmaDataBasicStatus props) {
        stack.set(DataComponentsTypeRegistry.UMADATA_BASIC_STATUS.get(), props);
    }

    public static UmaDataBasicStatus updatePropertyAsArray(ItemStack stack, Consumer<int[]> updater) {
        return stack.update(
                DataComponentsTypeRegistry.UMADATA_BASIC_STATUS.get(),
                UmaDataBasicStatus.DEFAULT_STATUS,
                data -> {
                    var array = data.array();
                    updater.accept(array);
                    return UmaDataBasicStatus.init(array);
                }
        );
    }

    public static UmaDataBasicStatus getPropertyRate(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.UMADATA_STATUS_RATE.get(),
                new UmaDataBasicStatus(0, 0, 0, 0, 0));
    }

    public static void setPropertyRate(ItemStack stack, UmaDataBasicStatus props) {
        stack.set(DataComponentsTypeRegistry.UMADATA_STATUS_RATE.get(), props);
    }

    public static UmaDataBasicStatus updatePropertyRateAsArray(ItemStack stack, Consumer<int[]> updater) {
        return stack.update(
                DataComponentsTypeRegistry.UMADATA_STATUS_RATE.get(),
                new UmaDataBasicStatus(0, 0, 0, 0, 0),
                data -> {
                    var array = data.array();
                    updater.accept(array);
                    return UmaDataBasicStatus.init(array);
                }
        );
    }

    public static UmaDataExtraStatus getExtraProperty(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get(),
                UmaDataExtraStatus.DEFAULT);
    }

    public static void setExtraProperty(ItemStack stack, UmaDataExtraStatus props) {
        stack.set(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get(), props);
    }

    public static UmaDataBasicStatus getMaxProperty(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.UMADATA_MAX_BASIC_STATUS.get(),
                UmaDataBasicStatus.DEFAULT_MAX_STATUS);
    }

    public static void setMaxProperty(ItemStack stack, UmaDataBasicStatus props) {
        stack.set(DataComponentsTypeRegistry.UMADATA_MAX_BASIC_STATUS.get(), props);
    }

    public static UmaDataBasicStatus updateMaxPropertyAsArray(ItemStack stack, Consumer<int[]> updater) {
        return stack.update(
                DataComponentsTypeRegistry.UMADATA_MAX_BASIC_STATUS.get(),
                UmaDataBasicStatus.DEFAULT_MAX_STATUS,
                data -> {
                    var array = data.array();
                    updater.accept(array);
                    return UmaDataBasicStatus.init(array);
                }
        );
    }

    public static Motivations getMotivation(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get(), UmaDataExtraStatus.DEFAULT).motivation();
    }

    public static Growth getGrowth(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.GROWTH.get(), Growth.UNTRAINED);
    }

    public static void setMotivation(ItemStack stack, Motivations motivation) {
        stack.update(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get(), UmaDataExtraStatus.DEFAULT,
                data->new UmaDataExtraStatus(data.actionPoint(), data.extraActionPoint(), data.resultRanking(), motivation));
    }

    public static void setGrowth(ItemStack stack, Growth growth) {
        stack.set(DataComponentsTypeRegistry.GROWTH.get(), growth);
    }

    public static List<Identifier> getSkills(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.UMADATA_SKILLS.get(), UmaDataSkills.DEFAULT).skills();
    }

    public static boolean hasSkill(ItemStack stack, Identifier skill) {
        for(var loc : stack.getOrDefault(DataComponentsTypeRegistry.UMADATA_SKILLS.get(), UmaDataSkills.DEFAULT).skills()) {
            if(skill.equals(loc))
                return true;
        }

        return false;
    }

    public static void setSkill(ItemStack stack, int index, Identifier skill) {
        stack.update(DataComponentsTypeRegistry.UMADATA_SKILLS.get(), UmaDataSkills.DEFAULT,
                data->{
                    List<Identifier> skills = new ArrayList<Identifier>();
                    skills.addAll(data.skills());
                    skills.set(index, skill);
                    return new UmaDataSkills(data.skillSlot(), data.selectedSkill(), skills);
                });
    }

    public static void addSkill(ItemStack stack, Identifier skill) {
        stack.update(DataComponentsTypeRegistry.UMADATA_SKILLS.get(), UmaDataSkills.DEFAULT,
                data->{
                    List<Identifier> skills = new ArrayList<Identifier>();
                    skills.addAll(data.skills());
                    skills.add(skill);
                    return new UmaDataSkills(data.skillSlot(), data.selectedSkill(), skills);
                });
    }

    public static int getSelectedSkillIndex(ItemStack stack) {
        return stack.getOrDefault(DataComponentsTypeRegistry.UMADATA_SKILLS.get(), UmaDataSkills.DEFAULT).selectedSkill();
    }

    public static void setSelectedSkill(ItemStack stack, int slot) {
        stack.update(DataComponentsTypeRegistry.UMADATA_SKILLS.get(), UmaDataSkills.DEFAULT,
                data->new UmaDataSkills(data.skillSlot(), slot, data.skills()));
    }

    public static Identifier getSelectedSkill(ItemStack stack) {
        Identifier skill = UmaSoulUtils.getSkills(stack).get(getSelectedSkillIndex(stack));
        return skill == null ? UmaSkillRegistry.BASIC_PACE.getId() : skill;
    }

    public static int getSkillSlots(ItemStack stack) {
        return stack.get(DataComponentsTypeRegistry.UMADATA_SKILLS.get()).skillSlot();
    }

    public static void setSkillSlots(ItemStack stack, int slots) {
        stack.update(DataComponentsTypeRegistry.UMADATA_SKILLS.get(), UmaDataSkills.DEFAULT,
                data->new UmaDataSkills(slots, data.selectedSkill(), data.skills()));
    }

    public static boolean hasEmptySkillSlot(ItemStack stack) {
        var slots = stack.get(DataComponentsTypeRegistry.UMADATA_SKILLS.get()).skillSlot();
        var learned = stack.get(DataComponentsTypeRegistry.UMADATA_SKILLS.get()).skills().size();
        return learned < slots;
    }

    public static void selectFormerSkill(ItemStack stack) {
        if (UmaSoulUtils.getSkills(stack).size() <= 1)
            return;
        int slot = UmaSoulUtils.getSelectedSkillIndex(stack);
        UmaSoulUtils.setSelectedSkill(stack, slot == 0 ? UmaSoulUtils.getSkills(stack).size() - 1 : slot - 1);
    }

    public static void selectLatterSkill(ItemStack stack) {
        if (UmaSoulUtils.getSkills(stack).size() <= 1)
            return;
        int slot = UmaSoulUtils.getSelectedSkillIndex(stack);
        UmaSoulUtils.setSelectedSkill(stack, slot == UmaSoulUtils.getSkills(stack).size() - 1 ? 0 : slot + 1);
    }

    public static int getActionPoint(ItemStack stack) {
        return Math.max(stack.get(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get()).actionPoint(), 0);
    }

    public static void setActionPoint(ItemStack stack, int ap) {
        stack.update(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get(), UmaDataExtraStatus.DEFAULT,
                data-> new UmaDataExtraStatus(ap, data.extraActionPoint(), data.resultRanking(), data.motivation()));
    }

    public static void addActionPoint(ItemStack stack, int ap) {
        UmaSoulUtils.setActionPoint(stack,
                Math.min(UmaSoulUtils.getActionPoint(stack) + ap, UmaSoulUtils.getMaxActionPoint(stack)));
    }

    public static int getMaxActionPoint(ItemStack stack) {
        return stack.get(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get()).extraActionPoint()
                + getProperty(stack).wisdom() * (int) (200 * (1.0D + (UmaSoulUtils.getPropertyRate(stack).wisdom() / 100.0D)));
    }

    public static int getExtraActionPoint(ItemStack stack) {
        return Math.max(stack.get(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get()).extraActionPoint(), 0);
    }

    public static void setExtraActionPoint(ItemStack stack, int ap) {
        stack.update(DataComponentsTypeRegistry.UMADATA_EXTRA_STATUS.get(), UmaDataExtraStatus.DEFAULT,
                data-> new UmaDataExtraStatus(data.actionPoint(), ap, data.resultRanking(), data.motivation()));
    }

    public static int getPhysique(ItemStack stack) {
        return stack.get(DataComponentsTypeRegistry.UMADATA_TRAINING.get()).physique();
    }

    public static void setPhysique(ItemStack stack, int phy) {
        stack.update(DataComponentsTypeRegistry.UMADATA_TRAINING.get(), new UmaDataTraining(1, 6),
                data-> new UmaDataTraining(phy, data.talent()));
    }

    public static void downPhysique(ItemStack stack) {
        int phy = Math.max(getPhysique(stack) - 1, 0);
        setPhysique(stack, phy);
    }

    public static int getLearningTimes(ItemStack stack) {
        return stack.get(DataComponentsTypeRegistry.UMADATA_TRAINING.get()).talent();
    }

    public static void setLearningTimes(ItemStack stack, int learns) {
        stack.update(DataComponentsTypeRegistry.UMADATA_TRAINING.get(), new UmaDataTraining(1, 6),
                data-> new UmaDataTraining(data.physique(), learns));
    }

    public static void downLearningTimes(ItemStack stack) {
        int learns = Math.max(getLearningTimes(stack) - 1, 0);
        setLearningTimes(stack, learns);
    }

    public static boolean hasUmaSoulDebut(ItemStack soul) {
        return Optional.ofNullable(soul.get(DataComponentsTypeRegistry.UMADATA_RACE_STATUS.get()))
                .map(UmaDataRaceStatus::hasDebut)
                .orElse(false);
    }

    public static UmaDataRaceStatus getRaceStatus(ItemStack soul) {
        return soul.getOrDefault(DataComponentsTypeRegistry.UMADATA_RACE_STATUS.get(), UmaDataRaceStatus.DEFAULT);
    }

    public static void setRaceStatus(ItemStack soul, UmaDataRaceStatus data) {
        soul.set(DataComponentsTypeRegistry.UMADATA_RACE_STATUS.get(), data);
    }
}
