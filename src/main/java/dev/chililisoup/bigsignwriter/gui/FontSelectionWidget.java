package dev.chililisoup.bigsignwriter.gui;

import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.chililisoup.bigsignwriter.font.FontFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class FontSelectionWidget extends ObjectSelectionList<FontSelectionWidget.Entry> {
    private final int maxHeight;
    private final Runnable onSelect;
    public boolean open = false;

    public FontSelectionWidget(Minecraft minecraft, int width, int height, int x, int y, int entryHeight, Runnable onSelect) {
        super(minecraft, width, height, y, entryHeight);
        this.maxHeight = height;
        this.onSelect = onSelect;
        this.setX(x);
        this.updateEntries();
    }

    public void updateEntries() {
        this.replaceEntries(BigSignWriterConfig.AVAILABLE_FONTS.stream().map(Entry::new).toList());
        this.setSelected(this
                .children()
                .stream()
                .filter(entry -> entry.fontFile == BigSignWriterConfig.SELECTED_FONT)
                .findFirst()
                .orElse(null)
        );
        this.setHeight(Math.min(this.maxHeight, this.contentHeight()));
    }

    @Override
    public int getHeight() {
        return this.open ? this.height : 20;
    }

    @Override
    public int getRowWidth() {
        return this.width - 32;
    }

    @Override
    public void setSelected(FontSelectionWidget.Entry entry) {
        super.setSelected(entry);
        if (entry != null) BigSignWriterConfig.selectFont(entry.fontFile);
        this.onSelect.run();
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.open) {
            this.renderListBackground(guiGraphics);
            Entry selected = this.getSelected();
            guiGraphics.textRenderer().acceptScrolling(
                    CommonComponents.optionNameValue(
                            Component.translatableWithFallback("bigsignwriter.font", "Font"),
                            selected != null ? selected.name : Component.literal("Unknown")
                    ),
                    this.getX() + 19,
                    this.getX() + 19,
                    this.getRight() - 3,
                    this.getY(),
                    this.getBottom()
            );
            return;
        }

        if (this.isHovered()) guiGraphics.requestCursor(CursorType.DEFAULT);
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        Entry entry = this.getEntryAtPosition(mouseX, mouseY);
        if (entry != null && entry != this.getSelected()) guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent mouseButtonEvent, boolean bl) {
        return this.open && super.mouseClicked(mouseButtonEvent, bl);
    }

    @Override
    public boolean mouseReleased(@NotNull MouseButtonEvent mouseButtonEvent) {
        return this.open && super.mouseReleased(mouseButtonEvent);
    }

    @Override
    public boolean mouseDragged(@NotNull MouseButtonEvent mouseButtonEvent, double d, double e) {
        return this.open && super.mouseDragged(mouseButtonEvent, d, e);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f, double g) {
        return this.open && super.mouseScrolled(d, e, f, g);
    }

    public class Entry extends ObjectSelectionList.Entry<FontSelectionWidget.Entry> {
        final FontFile fontFile;
        final Component name;

        public Entry(final FontFile fontFile) {
            this.fontFile = fontFile;
            this.name = Component.literal(fontFile.name);
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", this.name);
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
            guiGraphics.drawString(FontSelectionWidget.this.minecraft.font, this.name, this.getContentX() + 5, this.getContentY() + 2, -1);
        }

        @Override
        public boolean mouseClicked(@NotNull MouseButtonEvent mouseButtonEvent, boolean bl) {
            BigSignWriterConfig.selectFont(this.fontFile);
            FontSelectionWidget.this.setSelected(this);
            return super.mouseClicked(mouseButtonEvent, bl);
        }
    }
}
