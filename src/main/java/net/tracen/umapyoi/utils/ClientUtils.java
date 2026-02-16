package net.tracen.umapyoi.utils;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tracen.umapyoi.Umapyoi;
import net.tracen.umapyoi.client.model.bedrock.BedrockVersion;
import net.tracen.umapyoi.client.model.pojo.BedrockModelPOJO;
import net.tracen.umapyoi.data.tag.UmapyoiUmaDataTags;
import net.tracen.umapyoi.registry.cosmetics.CosmeticData;
import net.tracen.umapyoi.registry.training.card.SupportCard;
import net.tracen.umapyoi.registry.umadata.UmaData;

import java.util.HashMap;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientUtils {
    public static final Identifier TRAINING_SUIT = getModel("trainning_suit");
    public static final Identifier SUMMER_UNIFORM = getModel("summer_uniform");
    public static final Identifier WINTER_UNIFORM = getModel("winter_uniform");
    public static final Identifier KINDERGARTEN_UNIFORM = getModel("kindergarten_uniform");

    public static final Identifier TRAINING_SUIT_FLAT = getModel("trainning_suit_flat");
    public static final Identifier SUMMER_UNIFORM_FLAT = getModel("summer_uniform_flat");
    public static final Identifier WINTER_UNIFORM_FLAT = getModel("winter_uniform_flat");

    public static final Identifier THREE_GODDESS = getModel("three_goddesses");
    public static final Identifier UMA_STATUES = getModel("uma_statue");

    public static final Identifier SWIMSUIT = getModel("swimsuit");
    public static final Identifier SWIMSUIT_FLAT = getModel("swimsuit_flat");

    public static Identifier getModel(String name) {
        return getModel(Umapyoi.MODID, name);
    }

    public static Identifier getModel(String modid, String name) {
        return Identifier.fromNamespaceAndPath(modid, name);
    }

    public static Identifier getTexture(Identifier name) {
        return Identifier.fromNamespaceAndPath(name.getNamespace(), "textures/model/" + name.getPath() + ".png");
    }
    
    public static Identifier getEmissiveTexture(Identifier name) {
        return Identifier.fromNamespaceAndPath(name.getNamespace(), "textures/model/" + name.getPath() + "_emissive.png");
    }

    public static Registry<UmaData> getClientUmaDataRegistry() {
        return Minecraft.getInstance().getConnection().registryAccess().lookupOrThrow(UmaData.REGISTRY_KEY);
    }

    public static Registry<SupportCard> getClientSupportCardRegistry() {
        return Minecraft.getInstance().getConnection().registryAccess().lookupOrThrow(SupportCard.REGISTRY_KEY);
    }

    public static Registry<CosmeticData> getClientCosmeticDataRegistry() {
        return Minecraft.getInstance().getConnection().registryAccess().lookupOrThrow(CosmeticData.REGISTRY_KEY);
    }

    public static boolean isFlatUmamusume(ItemStack stack) {
        return ClientUtils.getClientUmaDataRegistry()
                .get(ResourceKey.create(UmaData.REGISTRY_KEY, UmaSoulUtils.getName(stack)))
                .get().is(UmapyoiUmaDataTags.FLAT_CHEST);
    }

    public static boolean isTannedSkin(ItemStack stack) {
        return ClientUtils.getClientUmaDataRegistry()
                .get(ResourceKey.create(UmaData.REGISTRY_KEY, UmaSoulUtils.getName(stack)))
                .get().is(UmapyoiUmaDataTags.TANNED_SKIN);
    }
    
    public static void addSummonParticle(Level pLevel, BlockPos pPos) {
        RandomSource pRand = pLevel.getRandom();
        List<BlockPos> posOffsets = BlockPos.betweenClosedStream(-1, -1, -1, 1, 0, 1).filter(pos -> {
            return Math.abs(pos.getX()) == 1 || Math.abs(pos.getZ()) == 1;
        }).map(BlockPos::immutable).toList();
        for (BlockPos spawnPos : posOffsets) {
            if (pRand.nextInt(32) == 0) {
                pLevel.addParticle(ParticleTypes.ENCHANT, (double) pPos.getX() + 0.5D, (double) pPos.getY() + 2.65D,
                        (double) pPos.getZ() + 0.5D, (double) ((float) spawnPos.getX() + pRand.nextFloat()) - 0.5D,
                        (double) ((float) spawnPos.getY() + 1D - pRand.nextFloat()),
                        (double) ((float) spawnPos.getZ() + pRand.nextFloat()) - 0.5D);
            }
        }
    }

    /****** MMLib ******/

    public static final HashMap<Identifier, BedrockModelPOJO> MODEL_MAP = Maps.newHashMap();

    public static void loadModel(Identifier modelLocation, JsonElement element) {
        BedrockModelPOJO pojo = DataGenUtils.DATA_GSON.fromJson(element, BedrockModelPOJO.class);

        if (pojo.getFormatVersion() == null) {
            Umapyoi.getLogger().error("Failed to load model: {}, it's not a Bedrock Model!", modelLocation);
            return;
        } else {
            // 先判断是不是 1.10.0 版本基岩版模型文件
            if (pojo.getFormatVersion().equals(BedrockVersion.LEGACY.getVersion())) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelLegacy() != null) {
                    Umapyoi.getLogger().info("Loaded 1.10.0 version model : {}", modelLocation);
                    MODEL_MAP.put(modelLocation, pojo);
                    return;
                } else {
                    // 否则日志给出提示
                    Umapyoi.getLogger().warn("{} model file don't have model field", modelLocation);
                    return;
                }
            }

            // 判定是不是 1.12.0 版本基岩版模型文件
            if (pojo.getFormatVersion().compareTo(BedrockVersion.NEW.getVersion()) >= 0) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelNew() != null) {
                    MODEL_MAP.put(modelLocation, pojo);
                    Umapyoi.getLogger().info("Loaded {} version model : {}", pojo.getFormatVersion(), modelLocation);
                    return;
                } else {
                    // 否则日志给出提示
                    Umapyoi.getLogger().warn("{} model file don't have model field", modelLocation);
                    return;
                }
            }

            Umapyoi.getLogger().error("{} model version is not 1.10.0 or new version bedrock model", modelLocation);
        }
    }

    public static BedrockModelPOJO getModelPOJO(Identifier modelLocation) {
        return MODEL_MAP.get(modelLocation);
    }

    public static float convertRotation(float degree) {
        return (float) (degree * Math.PI / 180);
    }
}
