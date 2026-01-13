package com.axialeaa.glissando.data;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.data.instrument_formation.InstrumentFormation;
import com.axialeaa.glissando.data.instrument_formation.InstrumentFormationType;
import com.axialeaa.glissando.data.instrument_formation.SimpleInstrumentFormation;
import com.axialeaa.glissando.data.registry.GlissandoNoteBlockInstrumentTags;
import com.axialeaa.glissando.mixin.accessor.NoteBlockAccessor;
import com.axialeaa.glissando.util.scale.Scale;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil;
import net.minecraft.block.BlockState;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record DataNoteBlockInstrument(RegistryEntry<SoundEvent> soundEvent, float range, InstrumentFormation formation, Text description, int octave) {

    private static final RegistryEntry<SoundEvent> DEFAULT_SOUND_EVENT = SoundEvents.UI_BUTTON_CLICK;
    private static final float DEFAULT_RANGE = 48.0F;
    private static final InstrumentFormation DEFAULT_FORMATION = SimpleInstrumentFormation.createBuilder().build();
    private static final Text DEFAULT_DESCRIPTION = Text.translatable("glissando.note_block_instrument.unknown");
    private static final int DEFAULT_OCTAVE = 3;

    public static final int FIRST_C_ORDINAL = 6;
    public static final int SECOND_C_ORDINAL = FIRST_C_ORDINAL + Scale.NOTES_IN_OCTAVE;

    public static final RegistryKey<Registry<DataNoteBlockInstrument>> REGISTRY_KEY = RegistryKey.ofRegistry(Glissando.id("note_block_instrument"));

    public static final Codec<DataNoteBlockInstrument> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            SoundEvent.ENTRY_CODEC.optionalFieldOf("sound_event", DEFAULT_SOUND_EVENT).forGetter(DataNoteBlockInstrument::soundEvent),
            Codecs.NON_NEGATIVE_FLOAT.optionalFieldOf("range", DEFAULT_RANGE).forGetter(DataNoteBlockInstrument::range),
            InstrumentFormation.TYPE_CODEC.optionalFieldOf("formation", DEFAULT_FORMATION).forGetter(DataNoteBlockInstrument::formation),
            TextCodecs.CODEC.optionalFieldOf("description", DEFAULT_DESCRIPTION).forGetter(DataNoteBlockInstrument::description),
            Codec.INT.optionalFieldOf("octave", DEFAULT_OCTAVE).forGetter(DataNoteBlockInstrument::octave)
        )
        .apply(instance, DataNoteBlockInstrument::new)
    );

    /**
     * Serves as a "placeholder instrument" in the preview screen, giving all the behaviors of a valid instrument outside of a world.
     */
    public static final DataNoteBlockInstrument UNKNOWN = createBuilder().soundEvent(SoundEvents.BLOCK_NOTE_BLOCK_HARP).build();

    /**
     * @return a new builder for a {@link DataNoteBlockInstrument}.
     */
    public static Builder createBuilder() {
        return new Builder();
    }

    @Nullable
    public static DataNoteBlockInstrument get(World world, RegistryKey<DataNoteBlockInstrument> instrumentKey) {
        Registry<DataNoteBlockInstrument> registry = world.getRegistryManager().getOrThrow(DataNoteBlockInstrument.REGISTRY_KEY);
        return registry == null || instrumentKey == null ? null : registry.get(instrumentKey);
    }

    /**
     * Untunable instruments cannot be pitched via the note block screen and alwaysValidate play a sound with a pitch value of {@code 1.0F}.
     * @return true if this instrument should be considered "tunable".
     */
    public boolean isUntunable(WorldAccess world) {
        return this.isIn(world, GlissandoNoteBlockInstrumentTags.UNTUNABLE);
    }

    public boolean isIn(WorldAccess world, TagKey<DataNoteBlockInstrument> tagKey) {
        return TagUtil.isIn(world.getRegistryManager(), tagKey, this);
    }

    public boolean isMultiBlock() {
        return this.formation.getType() == InstrumentFormationType.MULTI_BLOCK;
    }

    /**
     * @return the octave of {@code pitch} formatted as a subscript string.
     */
    public String getSubscriptOctaveOf(int pitch) {
        StringBuilder builder = new StringBuilder();
        int octave = this.getOctaveOf(pitch);

        for (char c : String.valueOf(octave).toCharArray())
            builder.append((char) ('₀' + (c - '0')));

        return builder.toString();
    }

    /**
     * Some instruments are tuned to higher octaves than others. The octave of the first F sharp is determined via {@link DataNoteBlockInstrument#octave}.
     * @return the octave of {@code pitch} for this instrument.
     */
    private int getOctaveOf(int pitch) {
        int startOctave = this.octave();

        if (pitch < FIRST_C_ORDINAL)
            return startOctave;

        return startOctave + (pitch >= SECOND_C_ORDINAL ? 2 : 1);
    }

    public @Nullable RegistryEntry<SoundEvent> getSoundEvent(RegistryKey<DataNoteBlockInstrument> registryKey, World world, BlockPos pos, BlockState state) {
        if (registryKey == null || this.formation.getType() != InstrumentFormationType.CUSTOM_HEAD)
            return this.soundEvent;

        Identifier id = ((NoteBlockAccessor) state.getBlock()).invokeGetCustomSound(world, pos);

        return id == null ? null : RegistryEntry.of(SoundEvent.of(id));
    }

    @SuppressWarnings("unused")
    public static class Builder {

        private RegistryEntry<SoundEvent> soundEvent = DEFAULT_SOUND_EVENT;
        private float range = DEFAULT_RANGE;
        private InstrumentFormation formation = DEFAULT_FORMATION;
        private Text description = DEFAULT_DESCRIPTION;
        private int octave = DEFAULT_OCTAVE;

        public Builder soundEvent(@NotNull RegistryEntry<SoundEvent> soundEvent) {
            this.soundEvent = soundEvent;
            return this;
        }

        public Builder range(float range) {
            this.range = range;
            return this;
        }

        public final Builder formation(InstrumentFormation formation) {
            this.formation = formation;
            return this;
        }

        public Builder description(@NotNull Text description) {
            this.description = description;
            return this;
        }

        public Builder octave(int octave) {
            this.octave = octave;
            return this;
        }

        public DataNoteBlockInstrument build() {
            return new DataNoteBlockInstrument(
                this.soundEvent,
                this.range,
                this.formation,
                this.description,
                this.octave
            );
        }

    }

}