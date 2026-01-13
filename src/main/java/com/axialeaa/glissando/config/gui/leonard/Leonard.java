package com.axialeaa.glissando.config.gui.leonard;

import com.axialeaa.glissando.Glissando;
import com.axialeaa.glissando.config.gui.screen.MainConfigurationScreen;
import com.axialeaa.glissando.gui.widget.note_key.NoteKeyWidget;
import com.axialeaa.glissando.gui.widget.note_key.WidgetHoverArea;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2f;

import static com.axialeaa.glissando.config.gui.widget.WidgetHelper.*;

public class Leonard {

    private static final SystemToast.Type TOAST_TYPE = new SystemToast.Type();

    private static final Text ACTIVATED_TITLE = Text.translatable("glissando.leonard_mode.activated.title");
    private static final Text ACTIVATED_DESCRIPTION = Text.translatable("glissando.leonard_mode.activated.description");
    private static final Text DEACTIVATED_TITLE = Text.translatable("glissando.leonard_mode.deactivated.title");
    private static final Identifier TEXTURE = Glissando.id("config/leonard");

    private static final float SPEED = 100.0F;
    private static final float ANIMATION_FRAME_TIME = 4;
    // the degree to which one wiggles their butt
    private static final float BUTT_WIGGLE_MAGNITUDE = 0.06F;
    private static final Vector2f UP_VECTOR = new Vector2f(0, -1);
    private static final int INPUT_ACTIVATION_THRESHOLD = 3;

    private static Vector2f targetPos;
    private static Vector2f lastPos;
    private static Vector2f homePos;
    private static float rotation;
    private static long ticksActive;
    private static long ticksSinceLastTargetSet;

    private static int inputsCount;

    public static Vector2f currentPos;
    public static boolean headingHome;
    public static boolean active;

    private static void addToast(boolean active) {
        Text title = active ? ACTIVATED_TITLE : DEACTIVATED_TITLE;
        Text description = active ? ACTIVATED_DESCRIPTION : null;

        SystemToast.show(MinecraftClient.getInstance().getToastManager(), TOAST_TYPE, title, description);
    }

    public static void onTick(MainConfigurationScreen screen) {
        ticksActive++;

        if (!isAtTarget())
            move();
        else if (headingHome) {
            active = false;
            headingHome = false;
        }

        pressKeys(screen);
    }

    private static void pressKeys(MainConfigurationScreen screen) {
        for (NoteKeyWidget noteKeyWidget : screen.noteKeyWidgets) {
            int x = (int) currentPos.x - HALF_COMMON_TEXTURE_SIZE;
            int y = (int) currentPos.y - HALF_COMMON_TEXTURE_SIZE;

            ScreenRect rect = new ScreenRect(x, y, COMMON_TEXTURE_SIZE, COMMON_TEXTURE_SIZE);
            WidgetHoverArea hoverArea = noteKeyWidget.note.textures.hoverArea();

            if (!hoverArea.intersects(noteKeyWidget.getX(), noteKeyWidget.getY(), rect))
                continue;

            if (screen.selectedWidget != noteKeyWidget) {
                screen.setSelectedWidget(noteKeyWidget);
                noteKeyWidget.playDownSound(MinecraftClient.getInstance().getSoundManager());
                noteKeyWidget.onPress();
            }

            break;
        }
    }

    private static boolean isAtTarget() {
        return currentPos.distance(targetPos) < HALF_COMMON_TEXTURE_SIZE;
    }

    private static void move() {
        float mod = (ticksActive - ticksSinceLastTargetSet) % SPEED;

        if (mod == 0 || lastPos == null)
            lastPos = currentPos;

        float delta = mod / SPEED;

        currentPos = new Vector2f(
            MathHelper.lerp(delta, lastPos.x, targetPos.x),
            MathHelper.lerp(delta, lastPos.y, targetPos.y)
        );
    }

    private static void updateRotation(float tickProgress) {
        Vector2f vec = new Vector2f(-targetPos.x + currentPos.x, targetPos.y - currentPos.y);
        float angle = vec.angle(UP_VECTOR) + getButtWiggleAngleDeviation();

        rotation = MathHelper.lerpAngleRadians(tickProgress, rotation, angle);
    }

    private static float getButtWiggleAngleDeviation() {
        return MathHelper.cos(MathHelper.PI * ticksActive / ANIMATION_FRAME_TIME / 2) * BUTT_WIGGLE_MAGNITUDE;
    }

    public static void render(DrawContext context, int mouseX, int mouseY, float tickProgress) {
        if (!active)
            return;

        MinecraftClient client = MinecraftClient.getInstance();

        if (!headingHome)
            setTargetPos(mouseX, mouseY);

        updateRotation(tickProgress);

        Sprite sprite = client.getGuiAtlasManager().getSprite(TEXTURE);

        Identifier id = sprite.getAtlasId();
        GpuTextureView view = client.getTextureManager().getTexture(id).getGlTextureView();

        context.state.addSimpleElement(new FloatTexturedQuadGuiElementRenderState(
            RenderPipelines.GUI_TEXTURED,
            TextureSetup.withoutGlTexture(view),
            context.getMatrices(),
            currentPos.x - HALF_COMMON_TEXTURE_SIZE,
            currentPos.y - HALF_COMMON_TEXTURE_SIZE,
            currentPos.x + HALF_COMMON_TEXTURE_SIZE,
            currentPos.y + HALF_COMMON_TEXTURE_SIZE,
            sprite.getFrameU(0),
            sprite.getFrameU(1),
            sprite.getFrameV(0),
            sprite.getFrameV(1),
            -1,
            rotation,
            context.scissorStack.peekLast()
        ));
    }

    public static void onScreenInit(float buttonX, float buttonY) {
        homePos = new Vector2f(buttonX, buttonY);
    }

    public static void activate() {
        active = true;
        currentPos = homePos;
        headingHome = false;
        inputsCount = 0;
        addToast(true);
        playSound();
    }

    public static void sendHome() {
        headingHome = true;
        setTargetPos(homePos.x, homePos.y);
        addToast(false);
        playSound();
    }

    public static void close() {
        active = false;
        headingHome = false;
    }
    
    public static void processInput() {
        if (headingHome)
            return;

        if (active) {
            sendHome();
            return;
        }

        float lerpedPitch = NoteBlock.getNotePitch(MathHelper.lerpPositive((float) inputsCount / INPUT_ACTIVATION_THRESHOLD, 2, 25));
        playSound(SoundEvents.BLOCK_NOTE_BLOCK_COW_BELL.value(), lerpedPitch);

        if (++inputsCount == INPUT_ACTIVATION_THRESHOLD)
            activate();
    }

    private static void playSound() {
        playSound(SoundEvents.ENTITY_ENDERMITE_AMBIENT, 1.0F);
    }

    private static void playSound(SoundEvent soundEvent, float pitch) {
        SoundManager manager = MinecraftClient.getInstance().getSoundManager();
        manager.play(PositionedSoundInstance.master(soundEvent, pitch));
    }

    private static void setTargetPos(float x, float y) {
        Vector2f pos = new Vector2f(x, y);

        if (targetPos != null && targetPos.equals(pos))
            return;

        ticksSinceLastTargetSet = ticksActive;
        lastPos = currentPos;
        targetPos = pos;
    }

}
