package com.axialeaa.glissando.packet;

//? if <1.20.5 {
/*import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.util.GlissandoUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.math.BlockPos;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

/^*
 * Allows us to send a packet to the server to tune a note block from a button press. <strong>Only works in versions below 1.20.5.</strong>
 ^/
public class TuneNoteBlockC2SPayloadPre1205 {

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(Glissando.TUNE_NOTE_BLOCK, (server, player, handler, buf, responseSender) -> {
            BlockPos blockPos = buf.readBlockPos();
            int pitch = buf.readInt();
            boolean play = buf.readBoolean();

            server.execute(() -> GlissandoUtils.tuneToPitch(blockPos, player, pitch, play));
        });
    }

    public static void sendNew(BlockPos pos, int pitch, boolean play) {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeBlockPos(pos);
        buf.writeInt(pitch);
        buf.writeBoolean(play);

        ClientPlayNetworking.send(Glissando.TUNE_NOTE_BLOCK, buf);
    }

}
*///?}