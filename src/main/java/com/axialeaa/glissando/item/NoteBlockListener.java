package com.axialeaa.glissando.item;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.block_entity.NoteBlockEntity;
import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import com.axialeaa.glissando.item.component.CustomDiscComponent;
import com.axialeaa.glissando.item.component.RecordingComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoteBlockListener implements GameEventListener {

    private static final int RANGE = 32;

    private final PositionSource positionSource;
    private final DiscHolder holder;

    public NoteBlockListener(PositionSource positionSource, DiscHolder holder) {
        this.positionSource = positionSource;
        this.holder = holder;
    }

    @Override
    public PositionSource getPositionSource() {
        return this.positionSource;
    }

    @Override
    public int getRange() {
        return RANGE;
    }

    @Override
    public boolean listen(ServerWorld world, RegistryEntry<GameEvent> event, GameEvent.Emitter emitter, Vec3d emitterPos) {
        if (!event.equals(GameEvent.NOTE_BLOCK_PLAY) || this.holder.getRecordingStacks().isEmpty())
            return false;

        BlockPos blockPos = BlockPos.ofFloored(emitterPos);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);

        if (!(blockEntity instanceof NoteBlockEntity noteBlockEntity))
            return false;

        Glissando.LOGGER.info("listen");

        RegistryKey<DataNoteBlockInstrument> instrumentKey = noteBlockEntity.instrumentKey;

        for (ItemStack musicDiscStack : this.holder.getRecordingStacks()) {
            long lastStartedRecordingTicks = musicDiscStack.getOrDefault(RecordingComponent.TYPE, RecordingComponent.DEFAULT).lastStartedRecordingTicks();
            musicDiscStack.apply(CustomDiscComponent.TYPE, CustomDiscComponent.DEFAULT, component -> modifyComponent(component, world, blockPos, instrumentKey, lastStartedRecordingTicks));
        }

        return true;
    }

    private static CustomDiscComponent modifyComponent(CustomDiscComponent component, ServerWorld world, BlockPos emitterBlockPos, RegistryKey<DataNoteBlockInstrument> instrumentKey, long lastStartedRecordingTicks) {
        List<RegistryKey<DataNoteBlockInstrument>> instruments = new ArrayList<>(component.instruments());

        if (!instruments.contains(instrumentKey))
            instruments.add(instrumentKey);

        BlockState blockState = world.getBlockState(emitterBlockPos);
        short ticksSinceStartedRecording = (short) (world.getTime() - lastStartedRecordingTicks);

        DataUnit unit = new DataUnit((byte) component.instruments().indexOf(instrumentKey), blockState.get(NoteBlock.NOTE).byteValue(), ticksSinceStartedRecording);

        List<DataUnit> data = new ArrayList<>(component.data());
        data.add(unit);

        return new CustomDiscComponent(instruments, data, component.producer(), component.title());
    }

}