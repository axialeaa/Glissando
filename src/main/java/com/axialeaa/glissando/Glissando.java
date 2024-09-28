package com.axialeaa.glissando;

import com.axialeaa.glissando.packet.UpdateNoteBlockC2SPayload;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Glissando implements ModInitializer {

	public static final String MOD_ID = "glissando";

	public static final FabricLoader LOADER = FabricLoader.getInstance();
	public static final ModContainer CONTAINER = LOADER.getModContainer(MOD_ID).orElseThrow(RuntimeException::new);

	public static final String MOD_NAME = CONTAINER.getMetadata().getName();
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static final Identifier UPDATE_NOTE_BLOCK = Glissando.id("update_note_block");

	@Override
	public void onInitialize() {
		LOGGER.info("{} initialized! Insert funny joke here.", MOD_NAME);
		UpdateNoteBlockC2SPayload.register();
	}

	public static Identifier id(String name) {
		return /*$ identifier*/ Identifier.of(MOD_ID, name);
	}

	public static Text getOptionTranslation(String name, boolean desc) {
		return Text.translatable("%s.config.option.%s.%s".formatted(MOD_ID, name, desc ? "desc" : "name"));
	}

}