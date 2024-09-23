package com.axialeaa.modid;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {

	public static final String MOD_ID = "mod-id";

	public static final FabricLoader LOADER = FabricLoader.getInstance();
	public static final ModContainer CONTAINER = LOADER.getModContainer(MOD_ID).orElseThrow(RuntimeException::new);

	public static final String MOD_NAME = CONTAINER.getMetadata().getName();
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	@Override
	public void onInitialize() {
		LOGGER.info("{} initialized! Insert funny joke here.", MOD_NAME);
	}

	public static Identifier id(String name) {
		return /*$ identifier*/ Identifier.of(MOD_ID, name);
	}

}