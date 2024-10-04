package com.axialeaa.glissando.gui.widget;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.util.GlissandoUtils;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

import net.minecraft.block.enums. /*$ instrument >>*/ NoteBlockInstrument ;

public class NoteKeyWidgetTooltip {

    private final int pitch;
    private final /*$ instrument >>*/ NoteBlockInstrument instrument;

    private NoteKeyWidgetTooltip(int pitch, /*$ instrument >>*/ NoteBlockInstrument instrument) {
        this.pitch = pitch;
        this.instrument = instrument;
    }

    public static Tooltip of(int pitch, /*$ instrument >>*/ NoteBlockInstrument instrument) {
        return new NoteKeyWidgetTooltip(pitch, instrument).getTooltip();
    }

    /**
     * @return a {@link MutableText} object containing each line of the tooltip text for this widget, based on the {@link GlissandoConfig config}.
     */
    private MutableText getTooltipContents() {
        String contents = this.getNoteLine();

        contents = this.getPitchLine(contents);
        contents = this.addKeybindLine(contents);

        return (MutableText) Text.of(contents);
    }

    private String getNoteLine() {
        if (!GlissandoConfig.get().noteTooltips)
            return "";

        Text localizedTooltip = Glissando.translate("tooltip.note");

        String note = GlissandoUtils.getNote(this.pitch).asString();
        int octave = GlissandoUtils.getOctave(this.instrument, this.pitch);

        Text localizedPitch = Text.translatable("note.%s.%s".formatted(note, octave));

        return localizedTooltip.getString().formatted(localizedPitch.getString());
    }

    private String getPitchLine(String s) {
        if (!GlissandoConfig.get().pitchTooltips)
            return s;

        if (!s.isEmpty())
            s += "\n";

        String line = Glissando.translate("tooltip.pitch").getString();

        return s + line.formatted(this.pitch);
    }

    private String addKeybindLine(String s) {
        if (!GlissandoConfig.get().keybindInputs || !GlissandoConfig.get().keybindTooltips)
            return s;

        if (!s.isEmpty())
            s += "\n";

        String line = Glissando.translate("tooltip.press_key").getString();

        char c = GlissandoUtils.getChar(this.pitch);
        String character = Character.valueOf(c).toString().toUpperCase(Locale.ROOT);

        return s + line.formatted(character);
    }

    /**
     * @return a {@link Tooltip} object with the {@link #getTooltipContents() tooltip contents}, colored if the config allows it.
     */
    @Nullable
    private Tooltip getTooltip() {
        MutableText mutableText = this.getTooltipContents();

        if (mutableText == ScreenTexts.EMPTY)
            return null;

        if (GlissandoConfig.get().tooltipColors) {
            int color = GlissandoUtils.getRgbColor(this.pitch);
            mutableText = mutableText.fillStyle(Style.EMPTY.withColor(color));
        }

        return Tooltip.of(mutableText);
    }

}
