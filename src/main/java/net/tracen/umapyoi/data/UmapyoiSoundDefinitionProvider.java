package net.tracen.umapyoi.data;

import static net.tracen.umapyoi.registry.SoundRegistry.SOUNDS;

import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.util.Util;

import java.util.concurrent.CompletableFuture;

public class UmapyoiSoundDefinitionProvider extends FabricSoundsProvider {
    public UmapyoiSoundDefinitionProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registryLookup, SoundExporter exporter) {
        SOUNDS.getEntries().forEach(
                soundEventRegistryObj -> {
                    var id = soundEventRegistryObj.getId();
                    exporter.add(soundEventRegistryObj.getHolder(),
                            SoundTypeBuilder.of()
                                    .subtitle(Util.makeDescriptionId("sound", id))
                                    .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(id))
                    );
                }
        );
    }

    @Override
    public String getName() {
        return "UmapyoiSoundDefinitionProvider";
    }
}
