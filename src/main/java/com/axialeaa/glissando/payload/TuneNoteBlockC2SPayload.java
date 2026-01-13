package com.axialeaa.glissando.payload;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.block_entity.NoteBlockEntity;
import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Optional;

/**
* Allows us to sends a packet to the server to tune a note block from a button press. Supports the pre-1.20.5 implementation.
* @param pos The position of the note block.
* @param pitch The {@link net.minecraft.block.NoteBlock#NOTE note} to tune the note block to.
* @param play Whether the note block should play a sound upon changing state.
*/
public record TuneNoteBlockC2SPayload(BlockPos pos, short pitch, boolean play) implements CustomPayload {

    private static final Id<TuneNoteBlockC2SPayload> PAYLOAD_ID = Glissando.payloadId("tune_note_block");

    public static final PacketCodec<RegistryByteBuf, TuneNoteBlockC2SPayload> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TuneNoteBlockC2SPayload::pos,
        PacketCodecs.SHORT, TuneNoteBlockC2SPayload::pitch,
        PacketCodecs.BOOLEAN, TuneNoteBlockC2SPayload::play,
        TuneNoteBlockC2SPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    public static void send(BlockPos pos, short pitch, boolean play) {
        ClientPlayNetworking.send(new TuneNoteBlockC2SPayload(pos, pitch, play));
    }

    public static void register() {
        PayloadRegistryUtils.registerServerbound(PAYLOAD_ID, PACKET_CODEC, (payload, ctx) -> {
            ServerPlayerEntity player = ctx.player();
            ServerWorld world = player.getWorld();

            ctx.server().execute(() -> payload.tuneNoteBlock(player, world));
        }, payload -> List.of(payload.pos, payload.pitch, payload.play));
    }

    private void tuneNoteBlock(ServerPlayerEntity player, ServerWorld world) {
        BlockPos pos = this.pos;

        if (!(world.getBlockEntity(pos) instanceof NoteBlockEntity blockEntity))
            return;

        short pitch = this.pitch;
        BlockState blockState = blockEntity.getCachedState().with(NoteBlock.NOTE, (int) pitch);

        if (!blockEntity.canOpenNoteBlockScreen(world, pos, blockState, player.getUuid()))
            return;

        world.setBlockState(pos, blockState);

        if (this.play)
            onPlay(world, pos, blockState, blockEntity, pitch);

        world.emitGameEvent(player, GameEvent.NOTE_BLOCK_PLAY, pos);
        player.incrementStat(Stats.TUNE_NOTEBLOCK);
    }

    public static void onPlay(ServerWorld world, BlockPos pos, BlockState state, NoteBlockEntity blockEntity, short pitch) {
        Optional<DataNoteBlockInstrument> optional = blockEntity.getInstrument(world);

        if (optional.isEmpty()) {
            Glissando.LOGGER.warn("empty optional");
            return;
        }

        DataNoteBlockInstrument instrument = optional.get();
        RegistryEntry<SoundEvent> soundEvent = instrument.getSoundEvent(blockEntity.instrumentKey, world, pos, state);

        if (soundEvent == null)
            return;

        if (world.getBlockState(pos.up()).isAir())
            AnimateNoteBlockS2CPayload.sendToAllNearbyPlayers(world, pos, pitch, 32.0);

        if (instrument.formation().validate(world, pos, state, blockEntity))
            playSound(world, soundEvent, pos.toCenterPos(), pitch, instrument.range() / 16.0F, instrument.isUntunable(world));
    }

    private static void playSound(ServerWorld world, RegistryEntry<SoundEvent> soundEvent, Vec3d vec3d, int pitch, float volume, boolean untunable) {
        world.playSound(
            null,
            vec3d.getX(),
            vec3d.getY(),
            vec3d.getZ(),
            soundEvent,
            SoundCategory.RECORDS,
            volume,
            untunable ? 1.0F : NoteBlock.getNotePitch(pitch),
            world.getRandom().nextLong()
        );
    }

}