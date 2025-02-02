package com.axialeaa.glissando;

import com.axialeaa.glissando.data.registry.VanillaBlockTags;
import com.axialeaa.glissando.data.registry.VanillaNoteBlockInstrumentTags;
import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.data.registry.VanillaNoteBlockInstruments;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axialeaa.glissando.packet.TuneNoteBlockC2SPayload;

public class Glissando implements ModInitializer {

	public static final String MOD_ID = /*$ mod_id >>*/ "glissando" ;
	public static final String MOD_NAME = /*$ mod_name >>*/ "Glissando" ;

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static final FabricLoader LOADER = FabricLoader.getInstance();
	public static final ModContainer CONTAINER = LOADER.getModContainer(MOD_ID).orElseThrow(RuntimeException::new);

	public static final boolean MOD_MENU_LOADED = LOADER.isModLoaded("modmenu");

	@Override
	public void onInitialize() {
		LOGGER.info("{} initialized! I-vory much enjoy this news, and it means a lot on a large scale.", MOD_NAME);

		VanillaBlockTags.init();
		VanillaNoteBlockInstruments.init();
		VanillaNoteBlockInstrumentTags.init();

		TuneNoteBlockC2SPayload.register();
		DynamicRegistries.registerSynced(SerializableNoteBlockInstrument.REGISTRY_KEY, SerializableNoteBlockInstrument.CODEC);
	}

	public static Identifier id(String path) {
		return id(MOD_ID, path);
	}

	public static Identifier vanillaId(String path) {
		return id("minecraft", path);
	}

	public static Identifier id(String namespace, String path) {
		return /*$ identifier*/ Identifier.of(namespace, path);
	}

	public static Text translate(String name, Object... args) {
		return Text.translatable(MOD_ID + "." + name, args);
	}

}