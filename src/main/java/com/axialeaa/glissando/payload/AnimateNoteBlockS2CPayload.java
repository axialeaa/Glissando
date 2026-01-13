package com.axialeaa.glissando.payload;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.block_entity.NoteBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public record AnimateNoteBlockS2CPayload(BlockPos pos, short data) implements CustomPayload {

    private static final double NOTE_PARTICLE_OFFSET = 0.2F;
    public static final short DEBUG_DATA_FLAG = -1;

    private static final Id<AnimateNoteBlockS2CPayload> PAYLOAD_ID = Glissando.payloadId("animate_note_block");

    public static final PacketCodec<RegistryByteBuf, AnimateNoteBlockS2CPayload> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, AnimateNoteBlockS2CPayload::pos,
        PacketCodecs.SHORT, AnimateNoteBlockS2CPayload::data,
        AnimateNoteBlockS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    public static void send(ServerPlayerEntity player, BlockPos pos, short data) {
        ServerPlayNetworking.send(player, new AnimateNoteBlockS2CPayload(pos, data));
    }

    public static void sendToAllNearbyPlayers(ServerWorld world, BlockPos pos, short data, double distance) {
        for (ServerPlayerEntity serverPlayer : world.getPlayers(player -> player.getBlockPos().isWithinDistance(pos, distance)))
            send(serverPlayer, pos, data);
    }

    public static void register() {
        PayloadRegistryUtils.registerClientbound(PAYLOAD_ID, PACKET_CODEC, (payload, ctx) -> {
            MinecraftClient client = ctx.client();
            ClientWorld world = client.world;

            if (world != null)
                client.execute(() -> payload.animate(world));
        }, payload -> List.of(payload.pos, payload.data));
    }

    private void animate(ClientWorld world) {
        if (!(world.getBlockEntity(this.pos) instanceof NoteBlockEntity noteBlockEntity))
            return;

        if (this.data == DEBUG_DATA_FLAG)
            animateDebug(world, noteBlockEntity);
        else animatePlay(world, this.pos, noteBlockEntity, this.data);
    }

    private static void animateDebug(ClientWorld world, NoteBlockEntity noteBlockEntity) {
        noteBlockEntity.lastInstrumentCheckedTicks = world.getTime();
    }

    public static void animatePlay(ClientWorld world, BlockPos noteBlockPos, NoteBlockEntity noteBlockEntity, int pitch) {
        noteBlockEntity.lastPoweredTicks = world.getTime();

        Vec3d vec3d = noteBlockPos.up().toBottomCenterPos().add(0.0, NOTE_PARTICLE_OFFSET,0.0);
        world.addParticleClient(ParticleTypes.NOTE, vec3d.x, vec3d.y, vec3d.z, pitch / 24.0, 0.0, 0.0);
    }

}