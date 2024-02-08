package net.tracen.umapyoi.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.item.ItemRegistry;

import java.util.function.Consumer;

public class UmapyoiAdvancementsDataProvider extends FabricAdvancementProvider {
    public UmapyoiAdvancementsDataProvider(FabricDataOutput output) {
        super(output);
    }
    
    @Override
    public void generateAdvancement(Consumer<AdvancementHolder> consumer) {
        AdvancementHolder root = findItem(getRoot(new ItemStack(ItemRegistry.JEWEL.get()),
                        new ResourceLocation("minecraft:textures/block/granite.png"), AdvancementType.TASK, true, false, false),
                new ItemStack(ItemRegistry.JEWEL.get())).save(consumer, "umapyoi:root");

        AdvancementHolder three_goddesses = findItem(getAdvancement(root, new ItemStack(ItemRegistry.THREE_GODDESS.get()),
                "three_goddesses", AdvancementType.TASK, true, false, false), new ItemStack(ItemRegistry.THREE_GODDESS.get()))
                .save(consumer, "umapyoi:three_goddesses");

        AdvancementHolder summon_pedestal = findItem(
                getAdvancement(root, new ItemStack(ItemRegistry.SILVER_UMA_PEDESTAL.get()), "summon_pedestal",
                        AdvancementType.TASK, true, false, false),
                new ItemStack(ItemRegistry.SILVER_UMA_PEDESTAL.get())).save(consumer, "umapyoi:summon_pedestal");

        getAdvancement(summon_pedestal, new ItemStack(ItemRegistry.SUPPORT_CARD.get()), "support_pedestal", AdvancementType.TASK, true,
                false, false)
                .addCriterion("buildSupport",
                        ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                                LocationPredicate.Builder.location()
                                        .setBlock(BlockPredicate.Builder.block().of(BlockRegistry.SILVER_UMA_PEDESTAL.get())),
                                ItemPredicate.Builder.item().of(Items.BOOK)))
                .save(consumer, "umapyoi:support_pedestal");

        findItem(getAdvancement(summon_pedestal, new ItemStack(ItemRegistry.UMA_PEDESTAL.get()), "gold_pedestal", AdvancementType.TASK,
                true, false, false), new ItemStack(ItemRegistry.UMA_PEDESTAL.get())).save(consumer,
                "umapyoi:gold_pedestal");

        findItem(getAdvancement(summon_pedestal, new ItemStack(ItemRegistry.BLANK_UMA_SOUL.get()),
                "blank_uma_soul", AdvancementType.TASK, true, false, false), new ItemStack(ItemRegistry.BLANK_UMA_SOUL.get()))
                .save(consumer, "umapyoi:blank_uma_soul");

        AdvancementHolder uma_soul = findItem(getAdvancement(three_goddesses, new ItemStack(ItemRegistry.UMA_SOUL.get()),
                "uma_soul", AdvancementType.TASK, true, false, false), new ItemStack(ItemRegistry.UMA_SOUL.get()))
                .save(consumer, "umapyoi:uma_soul");

        AdvancementHolder training = findItem(getAdvancement(uma_soul, new ItemStack(ItemRegistry.TRAINING_FACILITY.get()),
                "training", AdvancementType.TASK, true, false, false), new ItemStack(ItemRegistry.TRAINING_FACILITY.get()))
                .save(consumer, "umapyoi:training");

        AdvancementHolder register_lectern = findItem(
                getAdvancement(training, new ItemStack(ItemRegistry.REGISTER_LECTERN.get()), "register_lectern",
                        AdvancementType.TASK, true, false, false),
                new ItemStack(ItemRegistry.REGISTER_LECTERN.get())).save(consumer, "umapyoi:register_lectern");

        findItem(getAdvancement(register_lectern, new ItemStack(ItemRegistry.UMA_FACTOR_ITEM.get()), "inheritance",
                AdvancementType.TASK, true, false, false), new ItemStack(ItemRegistry.UMA_FACTOR_ITEM.get())).save(consumer,
                "umapyoi:inheritance");

        findItem(getAdvancement(register_lectern, new ItemStack(ItemRegistry.DISASSEMBLY_BLOCK.get()), "transfer",
                AdvancementType.TASK, true, false, false), new ItemStack(ItemRegistry.DISASSEMBLY_BLOCK.get())).save(consumer,
                "umapyoi:transfer");

        AdvancementHolder skill_learning_table = findItem(
                getAdvancement(root, new ItemStack(ItemRegistry.SKILL_LEARNING_TABLE.get()), "skill_learning_table",
                        AdvancementType.TASK, true, false, false),
                new ItemStack(ItemRegistry.SKILL_LEARNING_TABLE.get())).save(consumer, "umapyoi:skill_learning_table");

        findItem(getAdvancement(skill_learning_table, new ItemStack(ItemRegistry.SKILL_BOOK.get()), "skill_book",
                AdvancementType.TASK, true, false, false), new ItemStack(ItemRegistry.SKILL_BOOK.get())).save(consumer,
                "umapyoi:skill_book");

        findItem(getAdvancement(summon_pedestal, new ItemStack(ItemRegistry.BLANK_TICKET.get()),
                "uma_ticket", AdvancementType.TASK, true, false, false), new ItemStack(ItemRegistry.BLANK_TICKET.get()))
                .save(consumer, "umapyoi:uma_ticket");
    }

    private static Advancement.Builder findItem(Advancement.Builder builder, ItemStack item) {
        return builder.addCriterion("item", InventoryChangeTrigger.TriggerInstance.hasItems(item.getItem()));
    }

    private static Advancement.Builder getRoot(ItemStack display, ResourceLocation bg, AdvancementType type, boolean showToast, boolean announceToChat, boolean hidden) {
        return Advancement.Builder.advancement().display(display,
                Component.translatable("umapyoi.advancement.root"),
                Component.translatable("umapyoi.advancement.root.desc"),
                bg, type, showToast, announceToChat, hidden);
    }

    public static Advancement.Builder getAdvancement(AdvancementHolder parent, ItemStack display, String name, AdvancementType type, boolean showToast, boolean announceToChat, boolean hidden) {
        return Advancement.Builder.advancement().parent(parent).display(display,
                Component.translatable("umapyoi.advancement." + name),
                Component.translatable("umapyoi.advancement." + name + ".desc"),
                null, type, showToast, announceToChat, hidden);
    }
}
