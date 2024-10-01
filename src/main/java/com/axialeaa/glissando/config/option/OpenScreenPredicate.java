package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.config.GlissandoConfig;
import com.axialeaa.glissando.config.GlissandoNameableEnum;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public enum OpenScreenPredicate implements GlissandoNameableEnum {

    ALWAYS  (true),
    NEVER   (false),
    TUNABLE ((world, pos, state) -> state.get(NoteBlock.INSTRUMENT) /*$ can_be_pitched >>*/ .canBePitched() && world.getBlockState(pos.up()).isAir());

    private final OpenScreenPredicateFunction function;

    OpenScreenPredicate(OpenScreenPredicateFunction function) {
        this.function = function;
    }

    OpenScreenPredicate(boolean openScreen) {
        this.function = (world, pos, state) -> openScreen;
    }

    public boolean canOpenScreen(World world, BlockPos pos, BlockState state) {
        return this.function.canOpenScreen(world, pos, state);
    }

    @Override
    public String getOptionName() {
        return GlissandoConfig.OPEN_SCREEN_PREDICATE;
    }

    public interface OpenScreenPredicateFunction {

        boolean canOpenScreen(World world, BlockPos pos, BlockState state);

    }

}
