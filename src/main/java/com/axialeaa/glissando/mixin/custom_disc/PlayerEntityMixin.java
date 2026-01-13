package com.axialeaa.glissando.mixin.custom_disc;

import com.axialeaa.glissando.item.DiscHolder;
import com.axialeaa.glissando.item.NoteBlockListener;
import com.axialeaa.glissando.mixin.impl.EntityImplMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends EntityImplMixin implements DiscHolder {

    @Unique private final List<ItemStack> recordingStacks = new ArrayList<>(List.of());
    @Unique @Nullable private EntityGameEventHandler<NoteBlockListener> noteBlockGameEventHandler;

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "<init>", at = @At("TAIL"))
    private void initGameEventHandler(World world, GameProfile profile, CallbackInfo ci) {
        PositionSource positionSource = new EntityPositionSource((Entity) (Object) this, this.getStandingEyeHeight());
        this.noteBlockGameEventHandler = new EntityGameEventHandler<>(new NoteBlockListener(positionSource, this));
    }

    @Override
    public void updateEventHandlerImpl(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback, Operation<Void> original) {
        if (this.getWorld() instanceof ServerWorld serverWorld)
            callback.accept(this.noteBlockGameEventHandler, serverWorld);

        super.updateEventHandlerImpl(callback, original);
    }

    @Override
    public List<ItemStack> getRecordingStacks() {
        return this.recordingStacks;
    }

}
