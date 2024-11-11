package com.axialeaa.glissando;

import com.axialeaa.glissando.registries.GlissandoRegistries;
import com.axialeaa.glissando.registries.NoteBlockInstrument;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axialeaa.glissando.packet. /*$ payload >>*/ TuneNoteBlockC2SPayload ;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class Glissando implements ModInitializer {

	public static final String MOD_ID = "glissando";

	public static final FabricLoader LOADER = FabricLoader.getInstance();
	public static final ModContainer CONTAINER = LOADER.getModContainer(MOD_ID).orElseThrow(RuntimeException::new);

	public static final String MOD_NAME = CONTAINER.getMetadata().getName();
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static final Identifier TUNE_NOTE_BLOCK = Glissando.id("tune_note_block");
	public static final boolean MOD_MENU_LOADED = LOADER.isModLoaded("modmenu");

	@Override
	public void onInitialize() {
		LOGGER.info("{} initialized! I-vory much enjoy this news, and it means a lot on a large scale.", MOD_NAME);

		/*$ payload >>*/ TuneNoteBlockC2SPayload .register();
		GlissandoRegistries.init();
		registerReloadListener(ResourceType.SERVER_DATA, id("note_block_instruments"), new NoteBlockInstrument.Manager());
	}

	public void registerReloadListener(ResourceType type, Identifier id, ResourceReloader reloader) {
		ResourceManagerHelper.get(type).registerReloadListener(new IdentifiableResourceReloadListener() {

			@Override
			public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Executor prepareExecutor, Executor applyExecutor) {
				return reloader.reload(synchronizer, manager, prepareExecutor, applyExecutor);
			}

			@Override
			public Identifier getFabricId() {
				return id;
			}

		});
	}

	public static Identifier id(String name) {
		return /*$ identifier*/ Identifier.of(MOD_ID, name);
	}

	public static Text translate(String name) {
		return Text.translatable(MOD_ID + "." + name);
	}

}