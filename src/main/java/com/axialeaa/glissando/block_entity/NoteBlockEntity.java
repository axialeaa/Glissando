package com.axialeaa.glissando.block_entity;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.data.DataNoteBlockInstrument;
import com.axialeaa.glissando.payload.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NoteBlockEntity extends BlockEntity {

    private static final String INSTRUMENT = "instrument";
    private static final String LAST_INSTRUMENT_SET_TICKS = "last_instrument_set_ticks";

    public static final String PATH = "note_block";
    public static final BlockEntityType<NoteBlockEntity> TYPE = FabricBlockEntityTypeBuilder.create(NoteBlockEntity::new, Blocks.NOTE_BLOCK).build();

    public RegistryKey<DataNoteBlockInstrument> instrumentKey;
    public final InstrumentFormationValidationHandler instrumentFormationValidationHandler;

    public long lastInstrumentSetTicks;

    @Nullable
    private UUID editor;

    public long lastPoweredTicks;
    public long lastInstrumentCheckedTicks;

    public NoteBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
        this.instrumentFormationValidationHandler = new InstrumentFormationValidationHandler(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return this.createNbt(registryLookup);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);

        if (this.instrumentKey != null)
            view.putString(INSTRUMENT, this.instrumentKey.getValue().toString());

        view.putLong(LAST_INSTRUMENT_SET_TICKS, this.lastInstrumentSetTicks);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);

        String instrument = view.getString(INSTRUMENT, "");
        Identifier id = Identifier.tryParse(instrument);

        RegistryKey<DataNoteBlockInstrument> instrumentKey = RegistryKey.of(DataNoteBlockInstrument.REGISTRY_KEY, Objects.requireNonNull(id));

        this.instrumentKey = instrumentKey;
        this.lastInstrumentSetTicks = view.getLong(LAST_INSTRUMENT_SET_TICKS, 0);
    }

    public Optional<DataNoteBlockInstrument> getInstrument(World world) {
        Registry<DataNoteBlockInstrument> registry = world.getRegistryManager().getOrThrow(DataNoteBlockInstrument.REGISTRY_KEY);
        return registry == null || this.instrumentKey == null ? Optional.empty() : Optional.ofNullable(registry.get(this.instrumentKey));
    }

    public void updateInstrumentKey(ServerWorld world, BlockPos pos, RegistryKey<DataNoteBlockInstrument> registryKey, boolean showParticles) {
        if (registryKey.equals(this.instrumentKey))
            return;

        if (this.instrumentKey != null)
            this.kickEditor(world);

        this.instrumentKey = registryKey;
        this.lastInstrumentSetTicks = world.getTime();

        this.markDirty();

        if (showParticles)
            SpawnInstrumentFormedParticlesS2CPayload.sendToAllNearbyPlayers(world, pos, 32.0);
    }

    public void setEditor(@Nullable UUID editor) {
        this.editor = editor;
    }

    public void kickEditor(ServerWorld world) {
        if (this.editor == null)
            return;

        KickScreenPlayerS2CPayload.send((ServerPlayerEntity) world.getPlayerByUuid(this.editor));

        this.setEditor(null);
    }

    public static void tick(ServerWorld world, BlockPos pos, BlockState state, NoteBlockEntity blockEntity) {
        int mod = Glissando.getServerConfig(world).multiBlockInstrumentCheckDelay.get();
        long ticksSinceLastSet = world.getTime() - blockEntity.lastInstrumentSetTicks;

        if (ticksSinceLastSet % mod == 1 || mod == 1)
            tickMultiBlock(world, pos, state, blockEntity);
    }

    private static void tickMultiBlock(ServerWorld world, BlockPos pos, BlockState state, NoteBlockEntity blockEntity) {
        Glissando.printDebugLog(logger -> logger.info("Checking for multi-block instrument formations..."));
        AnimateNoteBlockS2CPayload.sendToAllNearbyPlayers(world, pos, AnimateNoteBlockS2CPayload.DEBUG_DATA_FLAG, 32.0);

        blockEntity.findAndUpdateInstrument(world, pos, state, true);
    }

    public void findAndUpdateInstrument(ServerWorld world, BlockPos pos, BlockState state, boolean showParticles) {
        Registry<DataNoteBlockInstrument> registry = world.getRegistryManager().getOrThrow(DataNoteBlockInstrument.REGISTRY_KEY);
        RegistryKey<DataNoteBlockInstrument> registryKey = this.instrumentFormationValidationHandler.findInstrument(world, pos, state, registry);

        this.updateInstrumentKey(world, pos, registryKey, showParticles);
    }

    private boolean isPlayerWithinRange(UUID uuid) {
        if (this.getWorld() == null)
            return false;

        PlayerEntity player = this.getWorld().getPlayerByUuid(uuid);

        return player != null && player.canInteractWithBlockAt(pos, 4.0);
    }

    public boolean canOpenNoteBlockScreen(ServerWorld world, BlockPos pos, BlockState state, UUID uuid) {
        if (!this.isPlayerWithinRange(uuid))
            return false;

        Optional<DataNoteBlockInstrument> optional = this.getInstrument(world);

        if (optional.isEmpty())
            return false;

        DataNoteBlockInstrument instrument = optional.get();

        if (instrument.isUntunable(world))
            return false;

        return instrument.formation().validate(world, pos, state, this);
    }

    public boolean isOtherPlayerEditing(PlayerEntity player) {
        return this.editor != null && !this.editor.equals(player.getUuid());
    }

    public void openScreen(ServerPlayerEntity player, BlockPos pos, boolean placed) {
        this.setEditor(player.getUuid());
        OpenScreenS2CPayload.send(player, pos, this.instrumentKey, placed);
    }

}
