package com.axialeaa.glissando.cca;

import com.axialeaa.glissando.Glissando;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class RecordingComponent implements AutoSyncedComponent, ServerTickingComponent {

    private static final String RECORDING_KEY = "recording";
    public static final ComponentKey<RecordingComponent> KEY = ComponentRegistry.getOrCreate(Glissando.id("recording"), RecordingComponent.class);

    private final PlayerEntity player;
    private boolean recording = false;

    public RecordingComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void serverTick() {

    }

    @Override
    public void readData(ReadView readView) {
        this.recording = readView.getBoolean(RECORDING_KEY, false);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putBoolean(RECORDING_KEY, this.recording);
    }

    public void sync() {
        KEY.sync(this.player);
    }

}
