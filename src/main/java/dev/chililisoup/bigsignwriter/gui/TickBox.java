package dev.chililisoup.bigsignwriter.gui;

import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

//? if >= 1.21.3 {
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
//?}

//? if >= 1.21.9 {
import net.minecraft.client.input.InputWithModifiers;
//? if < 1.21.11 {
/*import com.mojang.blaze3d.platform.cursor.CursorTypes;
*///?}
//?}

public class TickBox extends AbstractButton {
    private static final Identifier CHECKBOX_SELECTED_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/checkbox_selected_highlighted");
    private static final Identifier CHECKBOX_SELECTED_SPRITE = Identifier.withDefaultNamespace("widget/checkbox_selected");
    private static final Identifier CHECKBOX_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/checkbox_highlighted");
    private static final Identifier CHECKBOX_SPRITE = Identifier.withDefaultNamespace("widget/checkbox");

    public boolean value;
    private final Consumer<Boolean> onChange;

    public TickBox(boolean value, Consumer<Boolean> onChange, Component message) {
        super(0, 0, 0, 20, message);
        this.value = value;
        this.onChange = onChange;
    }

    @Override
    public void onPress(
            //? if >= 1.21.9
            @NotNull InputWithModifiers input
    ) {
        this.value = !this.value;
        this.onChange.accept(this.value);
    }

    @Override
    //? if >= 1.21.11 {
    protected void extractContents(
    //?} else
    //protected void extractWidgetRenderState(
            @NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick
    ) {
        Identifier sprite = this.isFocused() ?
                (this.value ? CHECKBOX_SELECTED_HIGHLIGHTED_SPRITE : CHECKBOX_HIGHLIGHTED_SPRITE) :
                (this.value ? CHECKBOX_SELECTED_SPRITE : CHECKBOX_SPRITE);

        int boxSize = this.getHeight();
        guiGraphics.blitSprite(
                //? if >= 1.21.3 {
                RenderPipelines.GUI_TEXTURED, sprite, this.getX(), this.getY(), boxSize, boxSize, ARGB.white(this.alpha)
                //?} else
                //sprite, this.getX(), this.getY(), boxSize, boxSize
        );

        int labelX = this.getX() + boxSize + 2;
        Screen.extractMenuBackgroundTexture(
                guiGraphics,
                Screen.MENU_BACKGROUND,
                labelX,
                this.getY(),
                0.0F,
                0.0F,
                this.getRight() - labelX,
                this.getHeight()
        );
        if (this.isHovered()) guiGraphics.fill(
                labelX,
                this.getY(),
                this.getRight(),
                this.getBottom(),
                0x40FFFFFF
        );

        GraphicsHelper.drawScrollingString(
                guiGraphics,
                this.getMessage(),
                labelX + 2,
                labelX + 2,
                this.getRight() - 2,
                this.getY(),
                this.getBottom()
        );

        //? if >= 1.21.9 < 1.21.11 {
        /*if (this.isHovered()) {
            guiGraphics.requestCursor(this.isActive() ? CursorTypes.POINTING_HAND : CursorTypes.NOT_ALLOWED);
        }
        *///?}
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.active) output.add(NarratedElementType.USAGE, Component.translatable(
                this.isFocused() ?
                        (this.value ? "narration.checkbox.usage.focused.uncheck" : "narration.checkbox.usage.focused.check") :
                        (this.value ? "narration.checkbox.usage.hovered.uncheck" : "narration.checkbox.usage.hovered.check")
        ));
    }
}
