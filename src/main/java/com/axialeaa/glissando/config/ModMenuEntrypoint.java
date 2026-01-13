package com.axialeaa.glissando.config;

import com.axialeaa.glissando.config.gui.screen.MainConfigurationScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuEntrypoint implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return MainConfigurationScreen::new;
    }

}
