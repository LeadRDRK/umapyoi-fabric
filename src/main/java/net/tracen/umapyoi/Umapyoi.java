package net.tracen.umapyoi;

import com.mojang.logging.LogUtils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.item.Item;
import net.tracen.umapyoi.data.builtin.CostumeDataRegistry;
import net.tracen.umapyoi.events.DatapackEvents;
import net.tracen.umapyoi.registry.UmapyoiAttributesRegistry;
import net.tracen.umapyoi.block.BlockRegistry;
import net.tracen.umapyoi.block.entity.BlockEntityRegistry;
import net.tracen.umapyoi.container.ContainerRegistry;
import net.tracen.umapyoi.effect.MobEffectRegistry;
import net.tracen.umapyoi.effect.PanickingEffect;
import net.tracen.umapyoi.events.AnvilUpdateCallback;
import net.tracen.umapyoi.events.ApplyFactorCallback;
import net.tracen.umapyoi.events.ApplyTrainingSupportCallback;
import net.tracen.umapyoi.events.PlayerBreakSpeedCallback;
import net.tracen.umapyoi.events.PlayerTickCallback;
import net.tracen.umapyoi.events.ResumeActionPointCallback;
import net.tracen.umapyoi.events.handler.AnvilEvents;
import net.tracen.umapyoi.events.handler.CommonEvents;
import net.tracen.umapyoi.events.handler.PassiveSkillEvents;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.network.EmptyResultPacket;
import net.tracen.umapyoi.network.SetupResultPacket;
import net.tracen.umapyoi.network.SelectSkillPacket;
import net.tracen.umapyoi.network.UseSkillPacket;
import net.tracen.umapyoi.data.builtin.SupportCardRegistry;
import net.tracen.umapyoi.registry.TrainingSupportRegistry;
import net.tracen.umapyoi.data.builtin.UmaDataRegistry;
import net.tracen.umapyoi.registry.UmaFactorRegistry;
import net.tracen.umapyoi.registry.UmaSkillRegistry;
import net.tracen.umapyoi.villager.VillageRegistry;
import net.tracen.umapyoi.villager.VillagerTradeRegistry;
import net.tracen.umapyoi.UmapyoiConfig;

import org.slf4j.Logger;

public class Umapyoi implements ModInitializer {
    public static final String MODID = "umapyoi";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final UmapyoiConfig CONFIG = UmapyoiConfig.createAndLoad();

    public static Item.Properties defaultItemProperties() {
        return new Item.Properties();
    }

    @Override
    public void onInitialize() {
        TrainingSupportRegistry.SUPPORTS.register();
        UmaSkillRegistry.SKILLS.register();
        CostumeDataRegistry.COSTUME_DATA.register();
        SupportCardRegistry.SUPPORT_CARD.register();
        UmaDataRegistry.UMA_DATA.register();
        UmaFactorRegistry.FACTORS.register();
        MobEffectRegistry.EFFECTS.register();
        BlockRegistry.BLOCKS.register();
        BlockEntityRegistry.BLOCK_ENTITIES.register();
        ItemRegistry.ITEMS.register();
        ContainerRegistry.CONTAINER_TYPES.register();
        VillageRegistry.PROFESSIONS.register();
        VillageRegistry.registerPoi();
        VillagerTradeRegistry.register();
        UmapyoiAttributesRegistry.ATTRIBUTES.register();

        ServerLivingEntityEvents.ALLOW_DAMAGE.register(CommonEvents::onDamageDownMotivation);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(CommonEvents::onDamagePanicking);
        ApplyTrainingSupportCallback.Post.EVENT.register(CommonEvents::onTrainingFinished);
        ApplyFactorCallback.Post.EVENT.register(CommonEvents::onFactorFinished);
        ResumeActionPointCallback.EVENT.register(PanickingEffect::onResumeAP);

        // Forge hooks
        AnvilUpdateCallback.EVENT.register(AnvilEvents::onAnvilEgg);
        PlayerBreakSpeedCallback.EVENT.register(PassiveSkillEvents::testPassiveSkill_att);
        PlayerTickCallback.EVENT.register(PassiveSkillEvents::passiveStepHeight);
        PlayerTickCallback.EVENT.register(PassiveSkillEvents::passiveTurfRunner);
        PlayerTickCallback.EVENT.register(PassiveSkillEvents::passiveDirtRunner);
        PlayerTickCallback.EVENT.register(PassiveSkillEvents::passiveSnowRunner);

        ServerPlayNetworking.registerGlobalReceiver(UseSkillPacket.TYPE, UseSkillPacket::handler);
        ServerPlayNetworking.registerGlobalReceiver(SelectSkillPacket.TYPE, SelectSkillPacket::handler);
        ServerPlayNetworking.registerGlobalReceiver(SetupResultPacket.TYPE, SetupResultPacket::handler);
        ServerPlayNetworking.registerGlobalReceiver(EmptyResultPacket.TYPE, EmptyResultPacket::handler);

        DatapackEvents.registerDatapackRegistries();
        DatapackEvents.registerSerializers();
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
