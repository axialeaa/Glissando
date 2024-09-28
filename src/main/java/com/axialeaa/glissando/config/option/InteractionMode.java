package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.GlissandoConfig;
import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.text.Text;

import java.util.Locale;

public enum InteractionMode implements NameableEnum {

    SOCIAL, RECLUSIVE;

    @Override
    public Text getDisplayName() {
        return this.getTranslation(false);
    }

    public String getString() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public Text getTranslation(boolean desc) {
        return Glissando.getOptionTranslation(GlissandoConfig.INTERACTION_MODE + "." + this.getString(), desc);
    }

}
