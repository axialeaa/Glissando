package com.axialeaa.glissando.payload;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.data.registry.GlissandoParticleTypes;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public record SpawnInstrumentFormedParticlesS2CPayload(BlockPos pos) implements CustomPayload {

    private static final int COUNT = 30;
    private static final float OFFSET = 0.6F;

    private static final Id<SpawnInstrumentFormedParticlesS2CPayload> PAYLOAD_ID = Glissando.payloadId("spawn_instrument_formed_particles");

    public static final PacketCodec<RegistryByteBuf, SpawnInstrumentFormedParticlesS2CPayload> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, SpawnInstrumentFormedParticlesS2CPayload::pos,
        SpawnInstrumentFormedParticlesS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    public static void send(ServerPlayerEntity player, BlockPos pos) {
        ServerPlayNetworking.send(player, new SpawnInstrumentFormedParticlesS2CPayload(pos));
    }

    public static void sendToAllNearbyPlayers(ServerWorld world, BlockPos pos, double distance) {
        for (ServerPlayerEntity serverPlayer : world.getPlayers(player -> player.getBlockPos().isWithinDistance(pos, distance)))
            send(serverPlayer, pos);
    }

    public static void register() {
        PayloadRegistryUtils.registerClientbound(PAYLOAD_ID, PACKET_CODEC, (payload, ctx) -> {
            MinecraftClient client = ctx.client();
            ClientWorld world = client.world;

            client.execute(() -> payload.spawnInstrumentFormedParticles(Objects.requireNonNull(world)));
        }, payload -> List.of(payload.pos));
    }

    private void spawnInstrumentFormedParticles(@NotNull ClientWorld world) {
        Random random = world.getRandom();
        BlockPos pos = this.pos;

        for (int i = 0; i < COUNT; i++) {
            Direction direction = Direction.random(random);
            BlockPos blockPos = pos.offset(direction);

            if (world.getBlockState(blockPos).isOpaqueFullCube())
                continue;

            Direction.Axis axis = direction.getAxis();
            double x = pos.getX(), y = pos.getY(), z = pos.getZ();

            x += axis == Direction.Axis.X ? 0.5 + OFFSET * direction.getOffsetX() : random.nextFloat();
            y += axis == Direction.Axis.Y ? 0.5 + OFFSET * direction.getOffsetY() : random.nextFloat();
            z += axis == Direction.Axis.Z ? 0.5 + OFFSET * direction.getOffsetZ() : random.nextFloat();

            world.addParticleClient(GlissandoParticleTypes.INSTRUMENT_FORMED, x, y, z, 0.0, 0.0, 0.0);
        }
    }

}