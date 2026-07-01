package dev.chililisoup.bigsignwriter.gui;

import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.chililisoup.bigsignwriter.font.SymbolGroup;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

//? if >= 1.21.9 {
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.input.MouseButtonEvent;
//?}

public class SymbolPickerWidget extends SimpleContainerWidget {
    private static final Identifier INWORLD_MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");

    private final Minecraft minecraft;
    private final int symbolTextWidth;
    private final SymbolGroupListWidget groupList;
    private final SymbolGridWidget symbolGrid;
    private @Nullable Consumer<Boolean> onVisibilityToggle;

    public SymbolPickerWidget(Minecraft minecraft, int x, int y, int width, int height) {
        super(x, y, width, height, Component.translatable("bigsignwriter.symbols"));
        this.minecraft = minecraft;
        this.symbolTextWidth = Minecraft.getInstance().font.width(this.message);
        this.groupList = new SymbolGroupListWidget(
                minecraft, width, height - 28, x, y + 28, 14, this::openGroup
        );
        this.symbolGrid = new SymbolGridWidget(
                minecraft,
                width,
                height - 28,
                x,
                y + 28,
                BigSignWriterConfig.MAIN_CONFIG.largeSymbolPreviews ? 42 : 28,
                BigSignWriterConfig.MAIN_CONFIG.largeSymbolPreviews ? 30 : 18,
                this::pasteSymbol
        );
        this.symbolGrid.visible = false;
    }

    public void toggleVisibility() {
        this.visible = !this.visible;
        if (this.onVisibilityToggle != null)
            this.onVisibilityToggle.accept(this.visible);
    }

    public void setOnVisibilityToggle(@Nullable Consumer<Boolean> onVisibilityToggle) {
        this.onVisibilityToggle = onVisibilityToggle;
    }

    public void openGroup(@Nullable SymbolGroup group) {
        this.groupList.setSelectedGroup(group);
        this.groupList.visible = group == null;
        this.symbolGrid.visible = group != null;
        if (group != null) this.symbolGrid.updateEntries(group);
    }

    public void pasteSymbol(String[] symbol) {

    }

    @Override
    public @NotNull List<AbstractWidget> children() {
        return List.of(this.groupList, this.symbolGrid);
    }

    private boolean backHovered(int mouseX, int mouseY) {
        return mouseX >= this.getX() + 6 && mouseX < this.getX() + 16
                && mouseY >= this.getY() + 6 && mouseY < this.getY() + 16;
    }

    private boolean symbolTextHovered(int mouseX, int mouseY) {
        return mouseX >= this.getX() + 24 && mouseX < this.getX() + 24 + this.symbolTextWidth
                && mouseY >= this.getY() + 6 && mouseY < this.getY() + 16;
    }

    @Override
    //? if <= 1.21.6 {
    /*public boolean mouseClicked(double mouseX, double mouseY, int button) {
    *///?} else
    public boolean mouseClicked(@NotNull MouseButtonEvent mouseButtonEvent, boolean doubleClick) {
        if (!this.isActive()) return false;

        //? if >= 1.21.9 {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();
        int button = mouseButtonEvent.button();
        //?}

        if (button == 0) {
            if (this.symbolTextHovered((int) mouseX, (int) mouseY)) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                this.openGroup(null);
                return true;
            }

            if (this.backHovered((int) mouseX, (int) mouseY)) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());

                if (this.groupList.getSelected() != null) {
                    this.openGroup(null);
                    return true;
                }

                this.toggleVisibility();
                return true;
            }
        }

        return super.mouseClicked(mouseButtonEvent, doubleClick);
    }

    private void navigatorText(GuiGraphicsExtractor guiGraphics, Font font, Component text, int xOffset) {
        guiGraphics.text(font, text, this.getX() + xOffset, this.getY() + 6, -1);
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractWidgetRenderState(guiGraphics, mouseX, mouseY, partialTick);

        Screen.extractMenuBackgroundTexture(
                guiGraphics,
                INWORLD_MENU_LIST_BACKGROUND,
                this.getX(),
                this.getY(),
                0,
                0,
                this.width,
                20
        );
        GraphicsHelper.drawHeaderSeparator(
                guiGraphics,
                this.getX(),
                this.getY() - 2,
                this.width
        );
        GraphicsHelper.drawFooterSeparator(
                guiGraphics,
                this.getX(),
                this.getY() + 20,
                this.width
        );

        Font font = this.minecraft.font;

        boolean backHovered = this.backHovered(mouseX, mouseY);
        MutableComponent backText = Component.literal("<");
        this.navigatorText(
                guiGraphics,
                font,
                backHovered ? backText.withStyle(ChatFormatting.UNDERLINE) : backText,
                8
        );

        boolean symbolTextHovered = !backHovered && this.symbolTextHovered(mouseX, mouseY);
        this.navigatorText(
                guiGraphics,
                font,
                symbolTextHovered ? this.message.copy().withStyle(ChatFormatting.UNDERLINE) : this.message,
                24
        );

        if (backHovered) guiGraphics.setTooltipForNextFrame(
                Component.translatable("gui.back"), mouseX, mouseY
        );

        //? if >= 1.21.9
        if (backHovered || symbolTextHovered) guiGraphics.requestCursor(CursorTypes.POINTING_HAND);

        SymbolGroupListWidget.Entry selected = this.groupList.getSelected();
        if (selected == null) {
            this.navigatorText(guiGraphics, font, Component.literal("🖫"), this.width - 16);
            return;
        }

        this.navigatorText(guiGraphics, font, Component.literal(">"), 30 + this.symbolTextWidth);
        GraphicsHelper.drawScrollingString(
                guiGraphics,
                selected.getName(),
                this.getX() + 40 + this.symbolTextWidth,
                this.getRight() - 8,
                this.getY() + 6
        );
    }

    @Override
    // TODO: Implement
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {

    }


}
