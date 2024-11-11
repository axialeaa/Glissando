package com.axialeaa.glissando.packet;

//? if >=1.20.5 {
import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.util.GlissandoUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Allows us to sends a packet to the server to tune a note block from a button press. <strong>Only works in versions since 1.20.5.</strong>
 * @param pos The position of the note block.
 * @param pitch The {@link net.minecraft.block.NoteBlock#NOTE note} to tune the note block to.
 * @param play Whether the note block should play a sound upon changing state.
 */
public record TuneNoteBlockC2SPayload(BlockPos pos, int pitch, boolean play) implements CustomPayload {

    public static final CustomPayload.Id<TuneNoteBlockC2SPayload> ID = new CustomPayload.Id<>(Glissando.TUNE_NOTE_BLOCK);

    public static final PacketCodec<RegistryByteBuf, TuneNoteBlockC2SPayload> CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TuneNoteBlockC2SPayload::pos,
        PacketCodecs.INTEGER, TuneNoteBlockC2SPayload::pitch,
        PacketCodecs.BOOL, TuneNoteBlockC2SPayload::play,
        TuneNoteBlockC2SPayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void sendNew(BlockPos pos, int pitch, boolean play) {
        ClientPlayNetworking.send(new TuneNoteBlockC2SPayload(pos, pitch, play));
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(ID, CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ID, (payload, ctx) -> {
            ServerPlayerEntity player = ctx.player();
            MinecraftServer server = /*$ payload_server >>*/ ctx.server();

            BlockPos blockPos = payload.pos;
            int pitch = payload.pitch;
            boolean playOnChange = payload.play;

            server.execute(() -> GlissandoUtils.tuneToPitch(blockPos, player, pitch, playOnChange));
        });
    }

}
//?}