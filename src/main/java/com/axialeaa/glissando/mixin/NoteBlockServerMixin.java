package com.axialeaa.glissando.mixin;

import com.axialeaa.glissando.registries.NoteBlockInstrument;
import com.axialeaa.glissando.util.GlissandoUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

//? if <=1.20.4
/*import net.minecraft.util.Hand;*/

@Mixin(NoteBlock.class)
public abstract class NoteBlockServerMixin {

	@Shadow protected abstract void playNote(@Nullable Entity entity, BlockState state, World world, BlockPos pos);

	@Shadow @Nullable protected abstract Identifier getCustomSound(World world, BlockPos pos);

	@Shadow @Final public static IntProperty NOTE;

    @Shadow
    public static float getNotePitch(int note) {
        throw new AssertionError();
    }

    @WrapMethod(method = "onUse")
	private ActionResult openScreenOnUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
		//? if <=1.20.4
		/*Hand hand,*/
		BlockHitResult hit, Operation<ActionResult> original
	) {
		if (!GlissandoUtils.isValidNoteBlock(world, pos)) {
			this.playNote(player, state, world, pos);
			player.incrementStat(Stats.PLAY_NOTEBLOCK);
		}

        return ActionResult.CONSUME;
	}

	@WrapMethod(method = "onSyncedBlockEvent")
	private boolean test(BlockState state, World world, BlockPos pos, int type, int data, Operation<Boolean> original) {
		NoteBlockInstrument instrument;

		BlockState upState = world.getBlockState(pos.up());
		Block up = upState.getBlock();

		if (NoteBlockInstrument.Manager.TOP_INSTRUMENTS.containsKey(up))
			instrument = NoteBlockInstrument.Manager.TOP_INSTRUMENTS.get(up);
		else {
			BlockState downState = world.getBlockState(pos.down());
			Block down = downState.getBlock();

			instrument = NoteBlockInstrument.Manager.ALL_INSTRUMENTS.get(down);
		}

		if (instrument == null)
			return false;

		RegistryEntry<SoundEvent> soundEvent = this.getSoundEvent(world, pos, instrument);

		if (soundEvent == null)
			return false;

		playSound(world, pos, state, instrument, soundEvent);

		return true;
	}

	@Unique
	private static void playSound(World world, BlockPos pos, BlockState state, NoteBlockInstrument instrument, RegistryEntry<SoundEvent> soundEvent) {
		world.playSound(
			null,
			pos.getX() + 0.5,
			pos.getY() + 0.5,
			pos.getZ() + 0.5,
			soundEvent,
			SoundCategory.RECORDS,
			3.0F,
			getPitch(world, pos, state, instrument),
			world.random.nextLong()
		);
	}

	@Unique
	@Nullable
	private RegistryEntry<SoundEvent> getSoundEvent(World world, BlockPos pos, NoteBlockInstrument instrument) {
		if (!instrument.isCustom())
			return instrument.soundEvent();

		Identifier id = this.getCustomSound(world, pos);

		if (id == null)
			return null;

		return RegistryEntry.of(SoundEvent.of(id));
	}

	@Unique
	@SuppressWarnings("UnreachableCode")
    private static float getPitch(World world, BlockPos pos, BlockState state, NoteBlockInstrument instrument) {
		if (!instrument.isBase())
			return 1.0F;

		double pitch = getNotePitch(state.get(NOTE));

		world.addParticle(
			ParticleTypes.NOTE,
			pos.getX() + 0.5,
			pos.getY() + 1.2,
			pos.getZ() + 0.5,
			pitch / 24.0,
			0.0,
			0.0
		);

		return (float) pitch;
	}

}