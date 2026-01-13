package com.axialeaa.glissando.mixin.custom_disc;

import com.axialeaa.glissando.item.CustomDiscJukebox;
import com.axialeaa.glissando.item.DataUnit;
import com.axialeaa.glissando.item.component.CustomDiscComponent;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.shorts.Short2ObjectArrayMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.block.jukebox.JukeboxManager;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Debug(export = true)
@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin extends BlockEntity implements CustomDiscJukebox {

    @Shadow public abstract void onManagerChange();
    @Shadow private ItemStack recordStack;

    @Unique private Short2ObjectArrayMap<List<DataUnit>> tickTimeOffsetToDataMap = new Short2ObjectArrayMap<>();
    @Unique private short ticksSinceSongStarted = 0;

    public JukeboxBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private static void test(World world, BlockPos pos, BlockState state, JukeboxBlockEntity blockEntity, CallbackInfo ci) {
        CustomDiscComponent component = blockEntity.getStack().get(CustomDiscComponent.TYPE);

        if (component == null || component.data().isEmpty())
            return;

        CustomDiscJukebox iJukebox = (CustomDiscJukebox) blockEntity;

        Short2ObjectArrayMap<List<DataUnit>> tickTimeOffsetToDataMap = iJukebox.getTickTimeOffsetToDataMap();
        List<DataUnit> data = tickTimeOffsetToDataMap.get(iJukebox.getTicksSinceSongStarted());

        if (data != null)
            component.readData(world, pos, state, data);

        iJukebox.incrementTicksSinceSongStarted();
    }

    @Inject(method = "writeData", at = @At("TAIL"))
    private void test(WriteView view, CallbackInfo ci) {
        if (!this.tickTimeOffsetToDataMap.isEmpty() && !this.tickTimeOffsetToDataMap.containsKey((short) (this.ticksSinceSongStarted + 1)))
            view.putLong(JukeboxBlockEntity.TICKS_SINCE_SONG_STARTED_NBT_KEY, this.ticksSinceSongStarted);
    }

    @Inject(method = "readData", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/jukebox/JukeboxManager;stopPlaying(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/block/BlockState;)V"))
    private void test(ReadView view, CallbackInfo ci) {
        this.tickTimeOffsetToDataMap = new Short2ObjectArrayMap<>();
        this.ticksSinceSongStarted = 0;

        if (this.getWorld() != null)
            this.getWorld().emitGameEvent(GameEvent.JUKEBOX_STOP_PLAY, this.getPos(), GameEvent.Emitter.of(this.getCachedState()));
    }

    @WrapOperation(method = "method_68287", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"))
    private void test(Optional<RegistryEntry<JukeboxSong>> instance, Consumer<? super RegistryEntry<JukeboxSong>> action, Operation<Void> original, @Local(argsOnly = true) Long ticksSinceSongStarted) {
        if (instance.isPresent())
            original.call(instance, action);
        else if (this.recordStack.get(CustomDiscComponent.TYPE) != null)
            this.ticksSinceSongStarted = ticksSinceSongStarted.shortValue();
    }

    @WrapOperation(method = "setStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/jukebox/JukeboxManager;stopPlaying(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/block/BlockState;)V"))
    private void test(JukeboxManager instance, WorldAccess world, BlockState state, Operation<Void> original, @Local boolean hasRecord) {
        CustomDiscComponent component = this.recordStack.get(CustomDiscComponent.TYPE);

        if (component == null) {
            original.call(instance, world, state);
            return;
        }

        if (hasRecord) {
            this.tickTimeOffsetToDataMap = component.createTickTimeOffsetToDataMap();
            this.ticksSinceSongStarted = 0;
        }
        else {
            this.tickTimeOffsetToDataMap = new Short2ObjectArrayMap<>();
            this.ticksSinceSongStarted = 0;
            world.emitGameEvent(GameEvent.JUKEBOX_STOP_PLAY, this.getPos(), GameEvent.Emitter.of(state));
        }

        this.onManagerChange();
    }

    @Override
    public Short2ObjectArrayMap<List<DataUnit>> getTickTimeOffsetToDataMap() {
        return this.tickTimeOffsetToDataMap;
    }

    @Override
    public short getTicksSinceSongStarted() {
        return this.ticksSinceSongStarted;
    }

    @Override
    public void incrementTicksSinceSongStarted() {
        this.ticksSinceSongStarted++;
    }

}
