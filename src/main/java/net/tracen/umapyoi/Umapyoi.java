package net.tracen.umapyoi;

import com.mojang.logging.LogUtils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.tracen.umapyoi.advancements.trigger.GrantBookOnFirstJoin;
import net.tracen.umapyoi.advancements.trigger.TriggerRegistry;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.block.entity.BlockEntityRegistry;
import net.tracen.umapyoi.command.CommandRegistry;
import net.tracen.umapyoi.container.ContainerRegistry;
import net.tracen.umapyoi.data.loot.AddLootTableModifier;
import net.tracen.umapyoi.effect.MobEffectRegistry;
import net.tracen.umapyoi.effect.PanickingEffect;
import net.tracen.umapyoi.events.AnvilUpdateCallback;
import net.tracen.umapyoi.events.DatapackEvents;
import net.tracen.umapyoi.events.ResumeActionPointCallback;
import net.tracen.umapyoi.events.handler.AnvilEvents;
import net.tracen.umapyoi.events.handler.CommonEvents;
import net.tracen.umapyoi.events.handler.PassiveSkillEvents;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.network.EmptyResultPacket;
import net.tracen.umapyoi.network.SelectSkillPacket;
import net.tracen.umapyoi.network.SetupResultPacket;
import net.tracen.umapyoi.network.UseSkillPacket;
import net.tracen.umapyoi.recipe.RecipeSerializerRegistry;
import net.tracen.umapyoi.registry.SoundRegistry;
import net.tracen.umapyoi.registry.TrainingSupportRegistry;
import net.tracen.umapyoi.registry.UmaFactorRegistry;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.registry.UmapyoiAttributesRegistry;
import net.tracen.umapyoi.villager.VillageRegistry;
import net.tracen.umapyoi.villager.VillagerTradeRegistry;

import org.slf4j.Logger;

public class Umapyoi implements ModInitializer {
    public static final String MODID = "umapyoi";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final net.tracen.umapyoi.UmapyoiConfig CONFIG = net.tracen.umapyoi.UmapyoiConfig.createAndLoad();

    public static Item.Properties defaultItemProperties() {
        return new Item.Properties();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }

    @Override
    public void onInitialize() {
        TrainingSupportRegistry.SUPPORTS.register();
        UmaSkillRegistry.SKILLS.register();
        UmaFactorRegistry.FACTORS.register();
        UmapyoiAttributesRegistry.register();
        MobEffectRegistry.EFFECTS.register();
        BlockRegistry.BLOCKS.register();
        BlockEntityRegistry.BLOCK_ENTITIES.register();
        ItemRegistry.ITEMS.register();
        ContainerRegistry.CONTAINER_TYPES.register();
        VillageRegistry.PROFESSIONS.register();
        VillageRegistry.registerPoi();
        VillagerTradeRegistry.register();
        RecipeSerializerRegistry.RECIPE_SERIALIZER.register();
        TriggerRegistry.registerAll();
        SoundRegistry.SOUNDS.register();
        CommandRegistry.register();

        CommonEvents.register();

        ResumeActionPointCallback.EVENT.register(PanickingEffect::onResumeAP);

        AnvilUpdateCallback.EVENT.register(AnvilEvents::onAnvilEgg);

        PassiveSkillEvents.register();

        ServerPlayNetworking.registerGlobalReceiver(UseSkillPacket.TYPE, UseSkillPacket::handler);
        ServerPlayNetworking.registerGlobalReceiver(SelectSkillPacket.TYPE, SelectSkillPacket::handler);
        ServerPlayNetworking.registerGlobalReceiver(SetupResultPacket.TYPE, SetupResultPacket::handler);
        ServerPlayNetworking.registerGlobalReceiver(EmptyResultPacket.TYPE, EmptyResultPacket::handler);
        GrantBookOnFirstJoin.PlayerJoinListener.register();

        DatapackEvents.registerDatapackRegistries();
        DatapackEvents.registerSerializers();

        AddLootTableModifier.registerListeners();
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
