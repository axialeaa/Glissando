package com.axialeaa.glissando.config.option;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.GlissandoConfig;
import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Locale;

@SuppressWarnings("unused")
public enum OpenScreenMode implements NameableEnum {

    NEVER (false),
    ALWAYS (true),
    TUNABLE ((world, pos, state) -> state.get(NoteBlock.INSTRUMENT) /*$ can_be_pitched >>*/ .canBePitched() && world.getBlockState(pos.up()).isAir());

    private final OpenScreenPredicate predicate;

    OpenScreenMode(OpenScreenPredicate predicate) {
        this.predicate = predicate;
    }

    OpenScreenMode(boolean openScreen) {
        this.predicate = (world, pos, state) -> openScreen;
    }

    public boolean canOpenScreen(World world, BlockPos pos, BlockState state) {
        return this.predicate.canOpenScreen(world, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return this.getTranslation(false);
    }

    public String getString() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public Text getTranslation(boolean desc) {
        return Glissando.getOptionTranslation(GlissandoConfig.OPEN_SCREEN_PREDICATE + "." + this.getString(), desc);
    }

    public interface OpenScreenPredicate {

        boolean canOpenScreen(World world, BlockPos pos, BlockState state);

    }

}
