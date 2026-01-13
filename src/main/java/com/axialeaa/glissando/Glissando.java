package com.axialeaa.glissando;

import com.axialeaa.glissando.block_entity.NoteBlockEntity;
import com.axialeaa.glissando.data.instrument_formation.InstrumentFormationType;
import com.axialeaa.glissando.data.registry.*;
import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import com.axialeaa.glissando.config.ServerConfigHolder;
import com.axialeaa.glissando.item.component.CustomDiscComponent;
import com.axialeaa.glissando.item.MusicDiscItem;
import com.axialeaa.glissando.item.component.RecordingComponent;
import com.axialeaa.glissando.payload.*;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.function.Consumer;

public class Glissando implements ModInitializer {

	public static final String MOD_ID = "glissando";
	public static final String MOD_NAME = "Glissando";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	@Nullable
	public static GlissandoSyncedConfig syncedConfig;
	public static GlissandoClientConfig clientConfig = new GlissandoClientConfig();

    @Override
	public void onInitialize() {
		LOGGER.info("{} initialized! I-vory much enjoy this news, and it means a lot on a large scale.", MOD_NAME);

		clientConfig.file = new File(FabricLoader.getInstance().getConfigDir().toFile(), MOD_ID + "_client.json");
		clientConfig.load();

		GlissandoBlockTags.loadClass();
		GlissandoNoteBlockInstruments.loadClass();
		GlissandoNoteBlockInstrumentTags.loadClass();
		GlissandoJukeboxSongs.loadClass();
		InstrumentFormationType.loadClass();

		RegistryUtils.of(Registries.DATA_COMPONENT_TYPE, CustomDiscComponent.PATH, CustomDiscComponent.TYPE);
		RegistryUtils.of(Registries.DATA_COMPONENT_TYPE, RecordingComponent.PATH, RecordingComponent.TYPE);
		RegistryUtils.of(Registries.BLOCK_ENTITY_TYPE, NoteBlockEntity.PATH, NoteBlockEntity.TYPE);
		RegistryUtils.of(Registries.ITEM, MusicDiscItem.PATH, MusicDiscItem.REGISTRY_INSTANCE);
		RegistryUtils.of(Registries.SOUND_EVENT, GlissandoSoundEvents.MUSIC_DISC_SCRATCH_PATH, GlissandoSoundEvents.MUSIC_DISC_SCRATCH);
		RegistryUtils.of(Registries.PARTICLE_TYPE, GlissandoParticleTypes.INSTRUMENT_FORMED_PATH, GlissandoParticleTypes.INSTRUMENT_FORMED);
		RegistryUtils.of(Registries.RECIPE_SERIALIZER, GlissandoRecipes.CUSTOM_DISC_CLONING_PATH, GlissandoRecipes.CUSTOM_DISC_CLONING);

		AnimateNoteBlockS2CPayload.register();
		KickScreenPlayerS2CPayload.register();
		OpenScreenS2CPayload.register();
		SpawnInstrumentFormedParticlesS2CPayload.register();
		SyncConfigPayloads.register();
		TuneNoteBlockC2SPayload.register();
		EntitleCustomMusicDisc.register();

		DynamicRegistries.registerSynced(DataNoteBlockInstrument.REGISTRY_KEY, DataNoteBlockInstrument.CODEC);

		GlissandoEvents.register();
	}

	public static void printDebugLog(Consumer<Logger> logAction) { // takes in a consumer so that live string validation still works
		if (clientConfig.debugLogging.get())
			logAction.accept(LOGGER);
	}

	public static GlissandoSyncedConfig getServerConfig(ServerWorld world) {
		return getServerConfig(world.getServer());
	}

	public static GlissandoSyncedConfig getServerConfig(MinecraftServer server) {
		return ((ServerConfigHolder) server).getConfig();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static <T extends CustomPayload> CustomPayload.Id<T> payloadId(String path) {
		return new CustomPayload.Id<>(id(path));
	}

}