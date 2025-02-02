package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.util.Note;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum TooltipType implements GlissandoNameableEnum {

    EMPTY (TooltipTextFactory.EMPTY),
    NOTE (TooltipTextFactory.NOTE),
    PITCH (TooltipTextFactory.PITCH);

    private final TooltipTextFactory factory;

    TooltipType(TooltipTextFactory factory) {
        this.factory = factory;
    }

    public MutableText getTextContent(int pitch, @NotNull SerializableNoteBlockInstrument instrument) {
        return this.factory.getTextContent(pitch, instrument).copy();
    }

    @Override
    public String getOptionName() {
        return GlissandoConfig.TOOLTIP_TYPE;
    }

    public static boolean isEmpty(@Nullable TooltipType tooltipType) {
        return tooltipType == null || tooltipType == EMPTY;
    }

    @FunctionalInterface
    public interface TooltipTextFactory {

        TooltipTextFactory EMPTY = (pitch, instrument) -> Text.empty();

        TooltipTextFactory NOTE = (pitch, instrument) -> {
            Note note = Note.byPitch(pitch);
            String name = note.getTranslatedName(pitch, instrument).getString();

            return Glissando.translate("tooltip.note", name);
        };

        TooltipTextFactory PITCH = (pitch, instrument) -> {
            String line = Glissando.translate("tooltip.pitch").getString();
            return Text.of(line.formatted(pitch));
        };

        Text getTextContent(int pitch, @NotNull SerializableNoteBlockInstrument instrument);

    }

}
