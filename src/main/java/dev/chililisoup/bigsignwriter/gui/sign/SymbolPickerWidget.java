package dev.chililisoup.bigsignwriter.gui.sign;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.chililisoup.bigsignwriter.font.SymbolGroup;
import dev.chililisoup.bigsignwriter.gui.ClickableSprite;
import dev.chililisoup.bigsignwriter.gui.ClickableText;
import dev.chililisoup.bigsignwriter.gui.ClickableWidgetPart;
import dev.chililisoup.bigsignwriter.gui.SimpleContainerWidget;
import dev.chililisoup.bigsignwriter.input.SignEditContext;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SymbolPickerWidget extends SimpleContainerWidget {
    private static final Identifier SAVE_SPRITE = BigSignWriter.id("save");
    private static final Identifier SEARCH_SPRITE = BigSignWriter.id("search");

    private static @Nullable Identifier LAST_OPEN_GROUP;

    private final Minecraft minecraft;
    private final SignEditContext context;
    private final Consumer<Boolean> onVisibilityToggle;

    private final SymbolGroupListWidget groupList;
    private final SymbolGridWidget symbolGrid;
    private final SymbolSaveWidget symbolSaver;
    private final SymbolSearchBar searchBar;

    private final ClickableText backButton;
    private final ClickableText symbolButton;
    private final ClickableSprite saveButton;
    private final ClickableSprite searchButton;

    public SymbolPickerWidget(
            Minecraft minecraft,
            SignEditContext context,
            int x,
            int y,
            int width,
            int height,
            Runnable onReload,
            Consumer<Boolean> onVisibilityToggle
    ) {
        super(x, y, width, height, Component.translatable("bigsignwriter.symbols"));
        this.minecraft = minecraft;
        this.context = context;
        this.onVisibilityToggle = onVisibilityToggle;

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
                this.context
        );
        this.symbolGrid.visible = false;
        this.symbolSaver = new SymbolSaveWidget(
                minecraft, x, y + 28, width, this::onSymbolSaverClose, onReload
        );
        this.symbolSaver.visible = false;
        this.searchBar = new SymbolSearchBar(
                minecraft.font, x, y + 26, width, 16, Component.translatable("bigsignwriter.symbols.search")
        );
        this.searchBar.setSuggestion(this.searchBar.getMessage().getString());
        this.searchBar.setResponder(this::onSearchQueryChanged);
        this.searchBar.visible = false;

        this.backButton = new ClickableText(
                Component.literal("<"),
                CommonComponents.GUI_BACK,
                x + 8,
                y + 6,
                4,
                this::back
        );
        this.symbolButton = new ClickableText(
                this.getMessage(),
                x + 24,
                y + 6,
                () -> this.openGroup(null)
        );
        this.saveButton = new ClickableSprite(
                SAVE_SPRITE,
                Component.translatable("bigsignwriter.symbols.save.hint"),
                this.getRight() - 36,
                y + 2,
                16,
                16,
                this.getRight() - 33,
                this.getRight() - 23,
                this.getY() + 16,
                this::openSymbolSaver
        );
        this.saveButton.visible = BigSignWriterConfig.MAIN_CONFIG.showSymbolSaveButton;
        this.searchButton = new ClickableSprite(
                SEARCH_SPRITE,
                Component.translatable("bigsignwriter.symbols.search.hint"),
                this.getRight() - 20,
                y + 2,
                16,
                16,
                this.getRight() - 17,
                this.getRight() - 7,
                this.getY() + 16,
                this::toggleSearchBar
        );
    }

    public void initForSignEditScreen() {
        this.visible = false;

        if (!BigSignWriterConfig.MAIN_CONFIG.rememberOpenSymbolGroup)
            LAST_OPEN_GROUP = null;
        if (LAST_OPEN_GROUP == null) return;

        SymbolGroup group = this.groupList.getListedGroup(LAST_OPEN_GROUP);
        if (group == null) {
            LAST_OPEN_GROUP = null;
            return;
        }

        this.openGroup(group);
        this.toggleVisibility();
    }

    @Override
    public @NotNull List<AbstractWidget> children() {
        return List.of(this.groupList, this.symbolGrid, this.symbolSaver, this.searchBar);
    }

    private List<ClickableWidgetPart> parts() {
        return List.of(this.backButton, this.symbolButton, this.saveButton, this.searchButton);
    }

    public boolean isControllingKeyboard() {
        return this.isActive() && this.getFocusedDescendant() instanceof EditBox;
    }

    public void toggleVisibility() {
        if (this.visible) {
            LAST_OPEN_GROUP = null;
            if (this.searchBar.visible)
                this.toggleSearchBar();
        }

        this.visible = !this.visible;
        this.onVisibilityToggle.accept(this.visible);
    }

    public void openGroup(@Nullable SymbolGroup group, boolean closeSearch) {
        LAST_OPEN_GROUP = group != null ? group.id() : null;
        this.symbolSaver.close();

        if (closeSearch) {
            this.searchBar.setVisible(false);
            if (this.searchBar.isFocused())
                this.setFocused(null);
        }

        this.groupList.setSelectedGroup(group);
        this.groupList.visible = group == null;
        this.symbolGrid.visible = group != null;
        if (group != null) this.symbolGrid.updateEntries(group);

        this.saveButton.visible = group == null
                && BigSignWriterConfig.MAIN_CONFIG.showSymbolSaveButton;
    }

    public void openGroup(@Nullable SymbolGroup group) {
        this.openGroup(group, true);
    }

    private void openSymbolSaver() {
        this.searchBar.setVisible(false);
        this.groupList.visible = false;
        this.symbolGrid.visible = false;
        this.symbolSaver.open(this.context.messages.clone());
        this.setFocused(this.symbolSaver);
    }

    private void onSymbolSaverClose() {
        if (this.getFocused() == this.symbolSaver) this.setFocused(null);
        this.openGroup(null);
    }

    public void toggleSearchBar() {
        boolean visible = !this.searchBar.isVisible();
        this.symbolSaver.close();
        this.searchBar.setVisible(visible);
        this.setFocused(visible ? this.searchBar : null);
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
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.searchBar.isFocused() || super.isMouseOver(mouseX, mouseY);
    }

    private void back() {
        if (this.groupList.getSelected() != null) {
            this.openGroup(null);
            return;
        }

        this.symbolSaver.close();
        this.toggleVisibility();
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent mouseButtonEvent, boolean doubleClick) {
        if (this.searchBar.isFocused() && !this.searchBar.isMouseOver(
                mouseButtonEvent.x(), mouseButtonEvent.y()
        )) this.setFocused(null);

        if (!this.isActive()) return false;

        if (this.parts().stream().anyMatch(part -> part.mouseClicked(mouseButtonEvent)))
            return true;

        return super.mouseClicked(mouseButtonEvent, doubleClick);
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        GraphicsHelper.drawCompleteMenuListBackground(guiGraphics, this.getX(), this.getY(), this.width, 20);
        super.extractWidgetRenderState(guiGraphics, mouseX, mouseY, partialTick);
        this.parts().forEach(part -> part.extractRenderState(guiGraphics, mouseX, mouseY));

        SymbolGroupListWidget.Entry selected = this.groupList.getSelected();
        if (selected == null) return;

        guiGraphics.text(
                this.minecraft.font,
                Component.literal(">"),
                this.getX() + 30 + this.symbolButton.getWidth(),
                this.getY() + 6,
                -1
        );
        GraphicsHelper.drawScrollingString(
                guiGraphics,
                selected.getName(),
                this.getX() + 40 + this.symbolButton.getWidth(),
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
