package com.axialeaa.glissando.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.BiConsumer;

@Mixin(Entity.class)
public abstract class EntityImplMixin {

    @Shadow public abstract World getWorld();

    @Shadow public abstract float getStandingEyeHeight();

    @WrapMethod(method = "updateEventHandler")
    public void updateEventHandlerImpl(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback, Operation<Void> original) {
        original.call(callback);
    }

}
