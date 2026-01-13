package com.axialeaa.glissando.gui.widget.note_key;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.option.TooltipType;
import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import com.axialeaa.glissando.util.ColorUtils;
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
    private final @NotNull DataNoteBlockInstrument instrument;

    private NoteKeyWidgetTooltip(int pitch, @NotNull DataNoteBlockInstrument instrument) {
        this.pitch = pitch;
        this.instrument = instrument;
    }

    @Nullable
    public static Tooltip of(int pitch, @NotNull DataNoteBlockInstrument instrument) {
        return new NoteKeyWidgetTooltip(pitch, instrument).getTooltip();
    }

    @Nullable
    private Tooltip getTooltip() {
        if (!Glissando.clientConfig.enableTooltips.get())
            return null;

        String character = Glissando.clientConfig.keyboardLayout.get().getFormattedChar(this.pitch);
        Text titleLine = Text.translatable("glissando.tooltip.title", character);

        int titleColor = Glissando.clientConfig.tooltipTitleColors.get() ? ColorUtils.getArgbPitchColor(this.pitch) : Colors.WHITE;
        MutableText mutableText = titleLine.copy().fillStyle(Style.EMPTY.withColor(titleColor));

        if (Glissando.clientConfig.tooltipLineArrangement.get().isEmpty())
            return Tooltip.of(mutableText);

        for (TooltipType tooltipType : Glissando.clientConfig.tooltipLineArrangement.get()) {
            if (TooltipType.isEmpty(tooltipType))
                continue;

            mutableText.append(ScreenTexts.LINE_BREAK);
            mutableText.append(tooltipType.getTextContent(this.pitch, this.instrument).fillStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        }

        return Tooltip.of(mutableText);
    }

}
