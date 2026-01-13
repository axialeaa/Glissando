package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import com.axialeaa.glissando.util.Note;
import com.axialeaa.glissando.util.SimpleEnumStringIdentifiable;
import com.mojang.serialization.Codec;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum TooltipType implements SimpleEnumStringIdentifiable {

    EMPTY((pitch, noteTranslation, instrument) -> Text.empty()),
    NOTE((pitch, noteTranslation, instrument) -> Text.translatable("glissando.tooltip.note", noteTranslation)),
    SOLMIZATED((pitch, noteTranslation, instrument) -> Text.translatable("glissando.tooltip.solmizated", noteTranslation)),
    PITCH((pitch, noteTranslation, instrument) -> Text.translatable("glissando.tooltip.pitch", pitch));

    private final TriFunction<Integer, Text, DataNoteBlockInstrument, Text> factory;

    public static final Codec<TooltipType> CODEC = StringIdentifiable.createCodec(TooltipType::values);

    TooltipType(TriFunction<Integer, Text, DataNoteBlockInstrument, Text> factory) {
        this.factory = factory;
    }

    public MutableText getTextContent(int pitch, @NotNull DataNoteBlockInstrument instrument) {
        return this.factory.apply(pitch, Note.byPitch(pitch).getTranslation(pitch, instrument), instrument).copy();
    }

    public static boolean isEmpty(@Nullable TooltipType tooltipType) {
        return tooltipType == null || tooltipType == EMPTY;
    }

}
