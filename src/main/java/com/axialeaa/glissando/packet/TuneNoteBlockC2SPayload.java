package com.axialeaa.glissando.packet;

import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.mixin.accessor.NoteBlockAccessor;
import com.axialeaa.glissando.util.GlissandoConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;

//? if >=1.20.5 {
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
//?} else {
/*import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
*///?}

/**
* Allows us to sends a packet to the server to tune a note block from a button press. Supports the pre-1.20.5 implementation.
* @param pos The position of the note block.
* @param pitch The {@link net.minecraft.block.NoteBlock#NOTE note} to tune the note block to.
* @param play Whether the note block should play a sound upon changing state.
*/
public record TuneNoteBlockC2SPayload(BlockPos pos, int pitch, boolean play) implements /*$ packet_interface >>*/ CustomPayload {

    //? if >=1.20.5 {
    private static final CustomPayload.Id<TuneNoteBlockC2SPayload> ID = new CustomPayload.Id<>(GlissandoConstants.TUNE_NOTE_BLOCK_PAYLOAD);

    public static final PacketCodec<RegistryByteBuf, TuneNoteBlockC2SPayload> CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TuneNoteBlockC2SPayload::pos,
        PacketCodecs.INTEGER, TuneNoteBlockC2SPayload::pitch,
        PacketCodecs./*$ bool_packet_codec >>*/ BOOLEAN , TuneNoteBlockC2SPayload::play,
        TuneNoteBlockC2SPayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
    //?} else {
    /*private static final Identifier ID = GlissandoConstants.TUNE_NOTE_BLOCK_PAYLOAD;
    private static final PacketType<TuneNoteBlockC2SPayload> PACKET_TYPE = PacketType.create(ID, TuneNoteBlockC2SPayload::new);

    public TuneNoteBlockC2SPayload(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt(), buf.readBoolean());
    }

    @Override
    public PacketType<?> getType() {
        return PACKET_TYPE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.pitch);
        buf.writeBoolean(this.play);
    }
    *///?}

    public static void sendNew(BlockPos pos, int pitch, boolean play) {
        ClientPlayNetworking.send(new TuneNoteBlockC2SPayload(pos, pitch, play));
    }

    public static void register() {
        //? if >=1.20.5
         PayloadTypeRegistry.playC2S().register(ID, CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ID,
            //? if >=1.20.5 {
            (payload, ctx) -> {
                ServerPlayerEntity player = ctx.player();
                MinecraftServer server = /*$ payload_server >>*/ ctx.server();

                BlockPos pos = payload.pos;
                int pitch = payload.pitch;
                boolean play = payload.play;
            //?} else {
            /*(server, player, handler, buf, responseSender) -> {
                // Saving these as local variables first is absolutely imperative because otherwise it'll try to
                // read from a payload that has long since been discarded, rejecting the packet and throwing a log error.
                // This is only necessary before 1.20.5.
                BlockPos pos = buf.readBlockPos();
                int pitch = buf.readInt();
                boolean play = buf.readBoolean();
            *///?}
                server.execute(() -> tuneToPitch(player, pos, pitch, play));
            });
    }

    /**
     * Tunes the note block to a specified pitch.
     * @param pos The block position of the note block.
     * @param player The player who sent the packet.
     * @param pitch The pitch to tune the note block to.
     * @param play Whether the note block should play a sound when it has been tuned.
     */
    public static void tuneToPitch(ServerPlayerEntity player, BlockPos pos, int pitch, boolean play) {
        ServerWorld world = player.getServerWorld();
        SerializableNoteBlockInstrument instrument = SerializableNoteBlockInstrument.get(world, pos);

        if (!instrument.canOpenNoteBlockScreen(world, pos))
            return;

        BlockState blockState = world.getBlockState(pos).with(NoteBlock.NOTE, pitch);
        world.setBlockState(pos, blockState, Block.NOTIFY_ALL);

        if (play) {
            NoteBlockAccessor accessor = (NoteBlockAccessor) blockState.getBlock();
            accessor.invokePlayNote(player, blockState, world, pos);
        }

        player.incrementStat(Stats.TUNE_NOTEBLOCK);
    }

}