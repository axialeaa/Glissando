package com.axialeaa.glissando.mixin;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.data.SerializableNoteBlockInstrument;
import com.axialeaa.glissando.gui.screen.NoteBlockScreen;
import com.axialeaa.glissando.util.NoteBlockScreenOpener;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if <=1.20.4
/*import net.minecraft.util.Hand;*/

@Mixin(NoteBlock.class)
public class NoteBlockClientMixin extends BlockMixin {

	@Inject(method = "onUse", at = @At(value = "RETURN", ordinal = /*? if >=1.20.6 {*/ 0 /*?} else {*/ /*1 *//*?}*/))
	private void openScreenOnUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
		 //? if <=1.20.4
		/*Hand hand,*/
		 BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir
	) {
		SerializableNoteBlockInstrument instrument = SerializableNoteBlockInstrument.get(world, pos);

		if (!SerializableNoteBlockInstrument.canOpenNoteBlockScreen(world, pos, instrument))
			return;

		if (world.isClient && world instanceof ClientWorld clientWorld)
			((NoteBlockScreenOpener) player).openScreen(new NoteBlockScreen(clientWorld, pos, instrument));
	}

	@Override
	public void onPlacedImpl(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, Operation<Void> original) {
		if (!world.isClient || !(placer instanceof NoteBlockScreenOpener screenOpener) || !GlissandoConfig.get().openScreenWhenPlaced) {
			super.onPlacedImpl(world, pos, state, placer, itemStack, original);
			return;
		}

		SerializableNoteBlockInstrument instrument = SerializableNoteBlockInstrument.get(world, pos);

		if (SerializableNoteBlockInstrument.canOpenNoteBlockScreen(world, pos, instrument) && world instanceof ClientWorld clientWorld)
			screenOpener.openScreen(new NoteBlockScreen(clientWorld, pos, instrument));

		super.onPlacedImpl(world, pos, state, placer, itemStack, original);
	}

}