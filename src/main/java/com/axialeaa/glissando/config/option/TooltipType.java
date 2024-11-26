package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.util.GlissandoUtils;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public enum TooltipType implements GlissandoNameableEnum {

    EMPTY((pitch, instrument) -> Text.empty()),
    NOTE((pitch, instrument) -> {
        Text localizedTooltip = Glissando.translate("tooltip.note");

        String note = GlissandoUtils.getNote(pitch).asString();
        int octave = instrument.getOctaveOf(pitch);

        Text localizedPitch = Glissando.translate("note.%s.%s".formatted(note, octave));

        return Text.of(localizedTooltip.getString().formatted(localizedPitch.getString()));
    }),
    PITCH((pitch, instrument) -> {
        String line = Glissando.translate("tooltip.pitch").getString();
        return Text.of(line.formatted(pitch));
    });

    private final TooltipTextFactory factory;

    TooltipType(TooltipTextFactory factory) {
        this.factory = factory;
    }

    public Text getTextContent(int pitch, @NotNull SerializableNoteBlockInstrument instrument) {
        return this.factory.getTextContent(pitch, instrument);
    }

    @Override
    public String getOptionName() {
        return GlissandoConfig.TOOLTIP_TYPE;
    }

    @FunctionalInterface
    public interface TooltipTextFactory {

        Text getTextContent(@Range(from = 0, to = 25) int pitch, @NotNull SerializableNoteBlockInstrument instrument);

    }

}
