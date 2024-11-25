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

import java.util.*;

//? if >1.20.1
import net.minecraft.text.TextCodecs;
import net.fabricmc.fabric.api.tag.convention.
    //? if >=1.20.6 {
    v2
    //?} else
    /*v1*/
    .TagUtil;

public record SerializableNoteBlockInstrument(Optional<RegistryEntry<SoundEvent>> soundEvent, float range, RegistryEntryList<Block> blocks, Text description, int octave) {

    public static final RegistryKey<Registry<SerializableNoteBlockInstrument>> REGISTRY_KEY = RegistryKey.ofRegistry(CommonIdentifiers.NOTE_BLOCK_INSTRUMENT_REGISTRY);

    public static final SerializableNoteBlockInstrument UNKNOWN = createBuilder()
        .setSoundEvent(SoundEvents.BLOCK_NOTE_BLOCK_HARP)
        .setDescription(Text.translatable("note_block_instrument.unknown"))
        .build();

    public static final Codec<SerializableNoteBlockInstrument> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            SoundEvent.ENTRY_CODEC.optionalFieldOf("sound_event").forGetter(SerializableNoteBlockInstrument::soundEvent),
            Codecs.POSITIVE_FLOAT.optionalFieldOf("range", 48.0F).forGetter(SerializableNoteBlockInstrument::range),
            RegistryCodecs.entryList(RegistryKeys.BLOCK).optionalFieldOf("blocks", RegistryEntryList.of()).forGetter(SerializableNoteBlockInstrument::blocks),
            /*$ text_codec >>*/ TextCodecs.CODEC .optionalFieldOf("description", Text.empty()).forGetter(SerializableNoteBlockInstrument::description),
            Codec.INT.optionalFieldOf("octave", 3).forGetter(SerializableNoteBlockInstrument::octave)
        )
        .apply(instance, SerializableNoteBlockInstrument::new)
    );

    public boolean isBase() {
        return !this.isIn(NoteBlockInstrumentTags.TOP_INSTRUMENTS);
    }

    public boolean isIn(TagKey<SerializableNoteBlockInstrument> tagKey) {
        return TagUtil.isIn(tagKey, this);
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    private static Registry<SerializableNoteBlockInstrument> getRegistry(World world) {
        return world.getRegistryManager(). /*$ get_registry >>*/ getOrThrow (REGISTRY_KEY);
    }

    private static SerializableNoteBlockInstrument byKey(World world, RegistryKey<SerializableNoteBlockInstrument> key) {
        return getRegistry(world).get(key);
    }

    public int getOctaveOf(int pitch) {
        int startOctave = this.octave();

        if (pitch < 6) // First C
            return startOctave;

        return startOctave + (pitch >= 18 ? 2 : 1); // Second C
    }

    public static SerializableNoteBlockInstrument get(World world, BlockPos pos) {
        SerializableNoteBlockInstrument instrument = findForBlock(world, pos);
        return Objects.requireNonNullElse(instrument, byKey(world, VanillaNoteBlockInstruments.HARP));
    }

    @Nullable
    public static SerializableNoteBlockInstrument findForBlock(World world, BlockPos pos) {
        for (SerializableNoteBlockInstrument instrument : getRegistry(world)) {
            BlockPos blockPos = instrument.isBase() ? pos.down() : pos.up();
            BlockState blockState = world.getBlockState(blockPos);

            if (blockState.isIn(instrument.blocks))
                return instrument;
        }

        return null;
    }

    public static boolean canOpenNoteBlockScreen(World world, BlockPos pos, SerializableNoteBlockInstrument instrument) {
        BlockState blockState = world.getBlockState(pos);

        if (!(blockState.getBlock() instanceof NoteBlock))
            return false;

        BlockState upState = world.getBlockState(pos.up());

        return instrument.isBase() && upState.isAir();
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
        float pitch = this.isBase() ? NoteBlock.getNotePitch(note) : 1.0F;

        world.playSound(null, x, y, z, soundEvent, SoundCategory.RECORDS, range, pitch, world.random.nextLong());

        if (this.isBase())
            world.addParticle(ParticleTypes.NOTE, x, y + 0.7, z, (double) note / 24.0, 0.0, 0.0);

        return true;
    }

    @Nullable
    public RegistryEntry<SoundEvent> getSoundEvent(World world, BlockPos pos, BlockState state) {
        if (this != byKey(world, VanillaNoteBlockInstruments.CUSTOM_HEAD) && this.soundEvent.isPresent())
            return this.soundEvent.get();

        Identifier id = ((NoteBlockAccessor) state.getBlock()).invokeGetCustomSound(world, pos);

        return id == null ? null : RegistryEntry.of(SoundEvent.of(id));
    }

    public static class Builder {

        private Optional<RegistryEntry<SoundEvent>> soundEvent = Optional.of(SoundEvents.UI_BUTTON_CLICK);
        private float range = 48.0F;
        private RegistryEntryList<Block> blocks = RegistryEntryList.of();
        private Text description = Text.empty();
        private int octave = 3;

        public Builder setSoundEvent(RegistryEntry<SoundEvent> soundEvent) {
            this.soundEvent = Optional.of(soundEvent);
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