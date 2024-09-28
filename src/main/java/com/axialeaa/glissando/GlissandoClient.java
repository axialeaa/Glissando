package com.axialeaa.glissando;

import com.axialeaa.glissando.config.GlissandoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GlissandoClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerPack("32x_upscale");
        GlissandoConfig.load();
    }

    private static void registerPack(String name) {
        Identifier id = Glissando.id(name);
        Text translated = Text.translatable("resourcePack.%s.name".formatted(id));
        ResourceManagerHelper.registerBuiltinResourcePack(id, Glissando.CONTAINER, translated, ResourcePackActivationType.NORMAL);

        Glissando.LOGGER.info("Registered pack {}!", id);
    }

}
