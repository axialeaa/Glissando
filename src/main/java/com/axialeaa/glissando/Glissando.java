package com.axialeaa.glissando;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axialeaa.glissando.packet. /*$ payload >>*/ TuneNoteBlockC2SPayload ;

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
	}

	public static Identifier id(String name) {
		return /*$ identifier*/ Identifier.of(MOD_ID, name);
	}

	public static Text translate(String name) {
		return Text.translatable(MOD_ID + "." + name);
	}

}