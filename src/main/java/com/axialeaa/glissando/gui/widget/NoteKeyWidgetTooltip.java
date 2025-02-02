package com.axialeaa.glissando.gui.widget;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.config.option.TooltipType;
import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.util.Note;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NoteKeyWidgetTooltip {

    private final int pitch;
    private final @NotNull SerializableNoteBlockInstrument instrument;

    private NoteKeyWidgetTooltip(int pitch, @NotNull SerializableNoteBlockInstrument instrument) {
        this.pitch = pitch;
        this.instrument = instrument;
    }

    @Nullable
    public static Tooltip of(int pitch, @NotNull SerializableNoteBlockInstrument instrument) {
        return new NoteKeyWidgetTooltip(pitch, instrument).getTooltip();
    }

    @Nullable
    private Tooltip getTooltip() {
        if (!GlissandoConfig.get().tooltips)
            return null;

        String character = GlissandoConfig.get().keyboardLayout.getFormattedChar(this.pitch);
        Text titleLine = Glissando.translate("tooltip.title", character);

        int titleColor = GlissandoConfig.get().tooltipTitleColors ? Note.getRgbColor(this.pitch) : Colors.WHITE;
        MutableText mutableText = titleLine.copy().fillStyle(Style.EMPTY.withColor(titleColor));

        if (GlissandoConfig.get().tooltipLineArrangement.isEmpty())
            return Tooltip.of(mutableText);

        for (TooltipType tooltipType : GlissandoConfig.get().tooltipLineArrangement) {
            if (!TooltipType.isEmpty(tooltipType)) {
                mutableText.append(ScreenTexts.LINE_BREAK);
                mutableText.append(tooltipType.getTextContent(this.pitch, this.instrument).fillStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            }
        }

        return Tooltip.of(mutableText);
    }

}
