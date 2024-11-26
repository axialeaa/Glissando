package com.axialeaa.glissando.data;

import com.axialeaa.glissando.mixin.accessor.NoteBlockAccessor;
import com.axialeaa.glissando.util.CommonIdentifiers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;

//? if >1.20.1
import net.minecraft.text.TextCodecs;
import net.fabricmc.fabric.api.tag.convention. /*$ convention_tag_package_ver >>*/ v2 .TagUtil;

public record SerializableNoteBlockInstrument(RegistryEntry<SoundEvent> soundEvent, float range, RegistryEntryList<Block> blocks, Text description, int octave) {

    private static final RegistryEntry<SoundEvent> DEFAULT_SOUND_EVENT = SoundEvents.UI_BUTTON_CLICK;
    private static final float DEFAULT_RANGE = 48.0F;
    private static final RegistryEntryList<Block> DEFAULT_BLOCKS = RegistryEntryList.of();
    private static final Text DEFAULT_DESCRIPTION = Text.translatable("note_block_instrument.unknown");
    private static final int DEFAULT_OCTAVE = 3;

    public static final RegistryKey<Registry<SerializableNoteBlockInstrument>> REGISTRY_KEY = RegistryKey.ofRegistry(CommonIdentifiers.NOTE_BLOCK_INSTRUMENT_REGISTRY);

    /**
     * Serves as a "placeholder instrument" in the preview screen, giving all the behaviors of a valid instrument outside of a world.
     */
    public static final SerializableNoteBlockInstrument UNKNOWN = createBuilder().setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_HARP).build();

    public static final Codec<SerializableNoteBlockInstrument> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            SoundEvent.ENTRY_CODEC.optionalFieldOf("sound_event", DEFAULT_SOUND_EVENT).forGetter(SerializableNoteBlockInstrument::soundEvent),
            Codecs.POSITIVE_FLOAT.optionalFieldOf("range", DEFAULT_RANGE).forGetter(SerializableNoteBlockInstrument::range),
            RegistryCodecs.entryList(RegistryKeys.BLOCK).optionalFieldOf("blocks", DEFAULT_BLOCKS).forGetter(SerializableNoteBlockInstrument::blocks),
            /*$ text_codec >>*/ TextCodecs.CODEC .optionalFieldOf("description", DEFAULT_DESCRIPTION).forGetter(SerializableNoteBlockInstrument::description),
            Codec.INT.optionalFieldOf("octave", DEFAULT_OCTAVE).forGetter(SerializableNoteBlockInstrument::octave)
        )
        .apply(instance, SerializableNoteBlockInstrument::new)
    );

    /**
     * Top instruments do not suppress the note block sound when a matching block is placed above, and always override a potential base instrument.
     * @return true if this instrument should be considered a "top instrument".
     */
    public boolean isTop(World world) {
        return this.isIn(world, NoteBlockInstrumentTags.TOP);
    }

    public boolean isTunable(World world) {
        return !this.isIn(world, NoteBlockInstrumentTags.UNTUNABLE);
    }

    public boolean isIn(World world, TagKey<SerializableNoteBlockInstrument> tagKey) {
        return TagUtil.isIn(world.getRegistryManager(), tagKey, this);
    }

    /**
     * @return a new builder for a {@link SerializableNoteBlockInstrument}.
     */
    public static Builder createBuilder() {
        return new Builder();
    }

    private static Registry<SerializableNoteBlockInstrument> getRegistry(World world) {
        return world.getRegistryManager(). /*$ get_registry >>*/ get (REGISTRY_KEY);
    }

    private static SerializableNoteBlockInstrument byKey(World world, RegistryKey<SerializableNoteBlockInstrument> key) {
        return getRegistry(world).get(key);
    }

    /**
     * Some instruments are tuned to higher octaves than others. The octave of the first F sharp is determined via {@link SerializableNoteBlockInstrument#octave}.
     * @return the octave of {@code pitch} for this instrument.
     */
    public int getOctaveOf(@Range(from = 0, to = 25) int pitch) {
        int startOctave = this.octave();

        if (pitch < 6) // First C
            return startOctave;

        return startOctave + (pitch >= 18 ? 2 : 1); // Second C
    }

    /**
     * @return a note block instrument associated with the blocks above and below the note block. If no instrument can be found, it will default to Harp.
     */
    public static SerializableNoteBlockInstrument get(World world, BlockPos pos) {
        SerializableNoteBlockInstrument instrument = find(world, pos);
        return Objects.requireNonNullElse(instrument, byKey(world, VanillaNoteBlockInstruments.HARP));
    }

    @Nullable
    public static SerializableNoteBlockInstrument find(World world, BlockPos pos) {
        BlockState upState = world.getBlockState(pos.up());
        boolean air = upState.isAir();

        BlockState blockState = air ? world.getBlockState(pos.down()) : upState;

        for (SerializableNoteBlockInstrument instrument : getRegistry(world)) {
            if (air != instrument.isTop(world) && blockState.isIn(instrument.blocks))
                return instrument;
        }

        return null;
    }

    /**
     * @return true if the note block screen can be opened.
     */
    public static boolean canOpenNoteBlockScreen(World world, BlockPos pos, SerializableNoteBlockInstrument instrument) {
        if (!instrument.isTunable(world))
            return false;

        BlockState blockState = world.getBlockState(pos);

        if (!(blockState.getBlock() instanceof NoteBlock))
            return false;

        if (instrument.isTop(world))
            return true;
        else {
            BlockState upState = world.getBlockState(pos.up());
            return upState.isAir();
        }
    }

    public boolean playSoundAndAddParticle(World world, BlockPos pos, BlockState state) {
        RegistryEntry<SoundEvent> soundEvent = this.getSoundEvent(world, pos, state);

        if (soundEvent == null)
            return false;

        double
            x = pos.getX() + 0.5,
            y = pos.getY() + 0.5,
            z = pos.getZ() + 0.5;

        float range = this.range / 16.0F;

        int note = state.get(NoteBlock.NOTE);
        float pitch = this.isTunable(world) ? NoteBlock.getNotePitch(note) : 1.0F;

        world.playSound(null, x, y, z, soundEvent, SoundCategory.RECORDS, range, pitch, world.random.nextLong());

        if (!this.isTop(world) && this.isTunable(world))
            world.addParticle(ParticleTypes.NOTE, x, y + 0.7, z, (double) note / 24.0, 0.0, 0.0);

        return true;
    }

    @Nullable
    public RegistryEntry<SoundEvent> getSoundEvent(World world, BlockPos pos, BlockState state) {
        if (this != byKey(world, VanillaNoteBlockInstruments.CUSTOM_HEAD))
            return this.soundEvent;

        Identifier id = ((NoteBlockAccessor) state.getBlock()).invokeGetCustomSound(world, pos);

        return id == null ? null : RegistryEntry.of(SoundEvent.of(id));
    }

    public static class Builder {

        private RegistryEntry<SoundEvent> soundEvent = DEFAULT_SOUND_EVENT;
        private float range = DEFAULT_RANGE;
        private RegistryEntryList<Block> blocks = DEFAULT_BLOCKS;
        private Text description = DEFAULT_DESCRIPTION;
        private int octave = DEFAULT_OCTAVE;

        public Builder setSoundEvent(RegistryEntry<SoundEvent> soundEvent) {
            this.soundEvent = soundEvent;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder setRange(float range) {
            this.range = range;
            return this;
        }

        public final Builder setBlocks(Registerable<SerializableNoteBlockInstrument> registerable, TagKey<Block> tagKey) {
            return setBlocks(registerable.getRegistryLookup(RegistryKeys.BLOCK), tagKey);
        }

        public final Builder setBlocks(RegistryEntryLookup<Block> lookup, TagKey<Block> tagKey) {
            return setBlocks(lookup.getOrThrow(tagKey));
        }

        public final Builder setBlocks(RegistryEntryList<Block> blocks) {
            this.blocks = blocks;
            return this;
        }

        public Builder setDescription(Text description) {
            this.description = description;
            return this;
        }

        public Builder setOctave(int octave) {
            this.octave = octave;
            return this;
        }

        public SerializableNoteBlockInstrument build() {
            return new SerializableNoteBlockInstrument(this.soundEvent, this.range, this.blocks, this.description, this.octave);
        }

    }

}