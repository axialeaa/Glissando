package com.axialeaa.glissando.mixin.custom_disc;

import com.axialeaa.glissando.item.component.CustomDiscComponent;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.jukebox.JukeboxSongs;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.JukeboxPlayableComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(JukeboxPlayableComponent.class)
public class JukeboxPlayableComponentMixin {

    @WrapOperation(method = "tryPlayStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"))
    private static Object test(ItemStack instance, ComponentType<JukeboxPlayableComponent> componentType, Operation<JukeboxPlayableComponent> original) {
        return instance.get(CustomDiscComponent.TYPE) != null ? new JukeboxPlayableComponent(new LazyRegistryEntryReference<>(JukeboxSongs.CAT)) : original.call(instance, componentType);
    }

}
