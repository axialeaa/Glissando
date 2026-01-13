package com.axialeaa.glissando.mixin.custom_disc;

import com.axialeaa.glissando.item.CustomDiscJukebox;
import com.axialeaa.glissando.item.component.CustomDiscComponent;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.block.jukebox.JukeboxManager;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(JukeboxBlock.class)
public class JukeboxBlockMixin {

    @WrapOperation(method = "getWeakRedstonePower", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/jukebox/JukeboxManager;isPlaying()Z"))
    private boolean test(JukeboxManager instance, Operation<Boolean> original, @Local JukeboxBlockEntity jukeboxBlockEntity) {
        if (original.call(instance))
            return true;

        ItemStack disc = jukeboxBlockEntity.getStack();
        CustomDiscComponent component = disc.getOrDefault(CustomDiscComponent.TYPE, CustomDiscComponent.DEFAULT);

        return !component.shouldStopPlaying(((CustomDiscJukebox) jukeboxBlockEntity).getTicksSinceSongStarted());
    }

}
