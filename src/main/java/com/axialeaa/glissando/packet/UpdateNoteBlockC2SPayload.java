package com.axialeaa.glissando.packet;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.util.NoteBlockUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.math.BlockPos;

//? >=1.20.6 {
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public record UpdateNoteBlockC2SPayload(BlockPos pos, int pitch) implements CustomPayload {

    public static final CustomPayload.Id<UpdateNoteBlockC2SPayload> ID = new CustomPayload.Id<>(Glissando.UPDATE_NOTE_BLOCK);

    public static final PacketCodec<RegistryByteBuf, UpdateNoteBlockC2SPayload> CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, UpdateNoteBlockC2SPayload::pos,
        PacketCodecs.INTEGER, UpdateNoteBlockC2SPayload::pitch,
        UpdateNoteBlockC2SPayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void sendNew(BlockPos pos, int pitch) {
        ClientPlayNetworking.send(new UpdateNoteBlockC2SPayload(pos, pitch));
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(ID, CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ID, (payload, ctx) -> {
            ServerPlayerEntity player = ctx.player();
            MinecraftServer server = /*$ payload_server >>*/ ctx.server();

            BlockPos blockPos = payload.pos();
            int pitch = payload.pitch();

            server.execute(() -> NoteBlockUtils.tuneToPitch(blockPos, player, pitch));
        });
    }

}
//?} else {
/*import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class UpdateNoteBlockC2SPayload {

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(Glissando.UPDATE_NOTE_BLOCK, (server, player, handler, buf, responseSender) -> {
            BlockPos blockPos = buf.readBlockPos();
            int pitch = buf.readInt();

            server.execute(() -> NoteBlockUtils.tuneToPitch(blockPos, player, pitch));
        });
    }

    public static void sendNew(BlockPos pos, int pitch) {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeBlockPos(pos);
        buf.writeInt(pitch);

        ClientPlayNetworking.send(Glissando.UPDATE_NOTE_BLOCK, buf);
    }

}
*///?}