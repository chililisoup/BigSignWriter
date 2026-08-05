package dev.chililisoup.bigsignwriter.gui;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.chililisoup.bigsignwriter.font.SymbolGroup;
import dev.chililisoup.bigsignwriter.font.SymbolReference;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SymbolPickerWidget extends SimpleContainerWidget {
    private static final Identifier INWORLD_MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");
    private static final Identifier SEARCH_SPRITE = BigSignWriter.id("search");
    private static final Identifier SAVE_SPRITE = BigSignWriter.id("save");

    private final Minecraft minecraft;
    private final int symbolTextWidth;
    private final SymbolGroupListWidget groupList;
    private final SymbolGridWidget symbolGrid;
    private final SymbolSearchBar searchBar;
    private @Nullable Consumer<Boolean> onVisibilityToggle;

    public SymbolPickerWidget(Minecraft minecraft, int x, int y, int width, int height, Consumer<SymbolReference> symbolConsumer) {
        super(x, y, width, height, Component.translatable("bigsignwriter.symbols"));
        this.minecraft = minecraft;
        this.symbolTextWidth = Minecraft.getInstance().font.width(this.getMessage());
        this.groupList = new SymbolGroupListWidget(
                minecraft, width, height - 28, x, y + 28, 14, this::openGroup
        );
        this.symbolGrid = new SymbolGridWidget(
                minecraft,
                width,
                height - 28,
                x,
                y + 28,
                BigSignWriterConfig.MAIN_CONFIG.largeSymbolPreviews ? 30 : 20,
                BigSignWriterConfig.MAIN_CONFIG.largeSymbolPreviews ? 22 : 16,
                symbolConsumer
        );
        this.symbolGrid.visible = false;
        this.searchBar = new SymbolSearchBar(minecraft.font, x, y + 26, width, 16, Component.translatable("bigsignwriter.symbols.search"));
        this.searchBar.setSuggestion(this.searchBar.getMessage().getString());
        this.searchBar.setResponder(this::onSearchQueryChanged);
        this.searchBar.visible = false;
    }

    public boolean isControllingKeyboard() {
        return this.getFocused() == this.searchBar;
    }

    public void toggleVisibility() {
        if (this.visible && this.searchBar.visible)
            this.toggleSearchBarVisibility();
        this.visible = !this.visible;
        if (this.onVisibilityToggle != null)
            this.onVisibilityToggle.accept(this.visible);
    }

    public void setOnVisibilityToggle(@Nullable Consumer<Boolean> onVisibilityToggle) {
        this.onVisibilityToggle = onVisibilityToggle;
    }

    public void openGroup(@Nullable SymbolGroup group, boolean closeSearch) {
        if (closeSearch) {
            this.searchBar.setVisible(false);
            if (this.searchBar.isFocused())
                this.setFocused(null);
            this.searchBar.setValue("");
        }

        this.groupList.setSelectedGroup(group);
        this.groupList.visible = group == null;
        this.symbolGrid.visible = group != null;
        if (group != null) this.symbolGrid.updateEntries(group);
    }

    public void openGroup(@Nullable SymbolGroup group) {
        this.openGroup(group, true);
    }

    public void setSearchBarVisibility(boolean visible) {
        this.searchBar.setVisible(visible);
        this.setFocused(visible ? this.searchBar : null);
    }

    public void toggleSearchBarVisibility() {
        this.setSearchBarVisibility(!this.searchBar.isVisible());
    }

    private void onSearchBarVisibilityChanged(boolean visible) {
        int contentOffset = visible ? 48 : 28;
        int contentY = this.getY() + contentOffset;
        int contentHeight = this.getHeight() - contentOffset;

        this.groupList.setY(contentY);
        this.groupList.setMaxHeight(contentHeight);
        this.symbolGrid.setY(contentY);
        this.symbolGrid.setMaxHeight(contentHeight);
    }

    private void onSearchQueryChanged(String query) {
        this.searchBar.setSuggestion(query.isEmpty() ?
                this.searchBar.getMessage().getString() : ""
        );

        if (query.isBlank()) {
            this.symbolGrid.clearFilter();
            return;
        }

        if (this.groupList.getSelected() == null)
            this.openGroup(this.groupList.allGroup, false);

        this.symbolGrid.filterEntries(query);
    }

    @Override
    public @NotNull List<AbstractWidget> children() {
        return List.of(this.groupList, this.symbolGrid, this.searchBar);
    }

    private boolean navigatorAreaHovered(int x, int width, int mouseX, int mouseY) {
        return mouseX >= this.getX() + x && mouseX < this.getX() + x + width
                && mouseY >= this.getY() + 6 && mouseY < this.getY() + 16;
    }

    private boolean backHovered(int mouseX, int mouseY) {
        return this.navigatorAreaHovered(6, 10, mouseX, mouseY);
    }

    private boolean symbolTextHovered(int mouseX, int mouseY) {
        return this.navigatorAreaHovered(24, this.symbolTextWidth, mouseX, mouseY);
    }

    private boolean saveHovered(int mouseX, int mouseY) {
        return this.navigatorAreaHovered(this.width - 33, 10, mouseX, mouseY);
    }

    private boolean searchHovered(int mouseX, int mouseY) {
        return this.navigatorAreaHovered(this.width - 17, 10, mouseX, mouseY);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.searchBar.isFocused() || super.isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent mouseButtonEvent, boolean doubleClick) {
        if (this.searchBar.isFocused() && !this.searchBar.isMouseOver(
                mouseButtonEvent.x(), mouseButtonEvent.y()
        )) this.setFocused(null);

        if (!this.isActive()) return false;

        int mouseX = (int) mouseButtonEvent.x();
        int mouseY = (int) mouseButtonEvent.y();
        int button = mouseButtonEvent.button();

        if (button == 0) {
            if (this.symbolTextHovered(mouseX, mouseY)) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                this.openGroup(null);
                return true;
            }

            if (this.backHovered(mouseX, mouseY)) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());

                if (this.groupList.getSelected() != null) {
                    this.openGroup(null);
                    return true;
                }

                this.toggleVisibility();
                return true;
            }

            if (this.searchHovered(mouseX, mouseY)) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                this.toggleSearchBarVisibility();
                return true;
            }

            if (this.groupList.getSelected() == null && this.saveHovered(mouseX, mouseY)) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                return true;
            }
        }

        return super.mouseClicked(mouseButtonEvent, doubleClick);
    }

    private void navigatorText(GuiGraphicsExtractor guiGraphics, Component text, int xOffset) {
        guiGraphics.text(this.minecraft.font, text, this.getX() + xOffset, this.getY() + 6, -1);
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

        boolean backHovered = this.backHovered(mouseX, mouseY);
        MutableComponent backText = Component.literal("<");
        this.navigatorText(
                guiGraphics,
                backHovered ? backText.withStyle(ChatFormatting.UNDERLINE) : backText,
                8
        );
        if (backHovered) guiGraphics.setTooltipForNextFrame(
                Component.translatable("gui.back"), mouseX, mouseY
        );

        boolean symbolTextHovered = !backHovered && this.symbolTextHovered(mouseX, mouseY);
        this.navigatorText(
                guiGraphics,
                symbolTextHovered ? this.getMessage().copy().withStyle(ChatFormatting.UNDERLINE) : this.getMessage(),
                24
        );

        boolean searchHovered = !symbolTextHovered && this.searchHovered(mouseX, mouseY);
        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                SEARCH_SPRITE,
                this.getRight() - 20,
                this.getY() + 2,
                16,
                16
        );
        if (searchHovered) {
            guiGraphics.horizontalLine(
                    this.getRight() - 17,
                    this.getRight() - 8,
                    this.getY() + 16,
                    -1
            );
            guiGraphics.setTooltipForNextFrame(
                    Component.translatable("bigsignwriter.symbols.search_hint"), mouseX, mouseY
            );
        }

        SymbolGroupListWidget.Entry selected = this.groupList.getSelected();
        boolean saveHovered = selected == null && this.saveHovered(mouseX, mouseY);
        if (backHovered || symbolTextHovered || saveHovered || searchHovered)
            guiGraphics.requestCursor(CursorTypes.POINTING_HAND);

        if (selected == null) {
            guiGraphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    SAVE_SPRITE,
                    this.getRight() - 36,
                    this.getY() + 2,
                    16,
                    16
            );
            if (saveHovered) {
                guiGraphics.horizontalLine(
                        this.getRight() - 33,
                        this.getRight() - 24,
                        this.getY() + 16,
                        -1
                );
                guiGraphics.setTooltipForNextFrame(
                        Component.translatable("bigsignwriter.symbols.save"), mouseX, mouseY
                );
            }
            return;
        }

        this.navigatorText(guiGraphics, Component.literal(">"), 30 + this.symbolTextWidth);
        GraphicsHelper.drawScrollingString(
                guiGraphics,
                selected.getName(),
                this.getX() + 40 + this.symbolTextWidth,
                this.getRight() - 20,
                this.getY() + 6
        );
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {
        this.children().forEach(widget -> widget.updateNarration(output));
    }

    private class SymbolSearchBar extends EditBox {
        public SymbolSearchBar(Font font, int x, int y, int width, int height, Component narration) {
            super(font, x, y, width, height, narration);
        }

        @Override
        public void setVisible(boolean visible) {
            if (this.isVisible() == visible) return;
            super.setVisible(visible);
            SymbolPickerWidget.this.onSearchBarVisibilityChanged(visible);
            if (!visible) this.setValue("");
        }

        @Override
        public boolean keyPressed(KeyEvent event) {
            if (event.isConfirmation() && this.isFocused()) {
                SymbolPickerWidget.this.setFocused(null);
                return true;
            }

            return super.keyPressed(event);
        }
    }
}
