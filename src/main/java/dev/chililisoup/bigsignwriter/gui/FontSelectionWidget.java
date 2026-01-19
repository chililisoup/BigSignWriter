package dev.chililisoup.bigsignwriter.gui;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.font.FontFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//? if > 1.21.6 {
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.input.MouseButtonEvent;
//?}

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
        this.replaceEntries(BigSignWriter.AVAILABLE_FONTS.stream().map(Entry::new).toList());
        this.addEntryToTop(new Entry(null));
        this.setSelected(this
                .children()
                .stream()
                .filter(entry -> entry.fontFile == BigSignWriter.SELECTED_FONT)
                .findFirst()
                .orElse(null)
        );
        this.setHeight(Math.min(this.maxHeight, this.contentHeight()));
    }

    //? if <= 1.21.3 {
    /*private int contentHeight() {
        return this.getItemCount() * this.itemHeight;
    }
    *///?}

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
        if (entry != null) BigSignWriter.selectFont(entry.fontFile);
        this.onSelect.run();
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.open) {
            this.renderListBackground(guiGraphics);
            Entry selected = this.getSelected();
            //? if < 1.21.11 {
            /*renderScrollingString(
                    guiGraphics,
                    this.minecraft.font,
            *///?} else
            guiGraphics.textRenderer().acceptScrolling(
                    CommonComponents.optionNameValue(
                            Component.translatableWithFallback("bigsignwriter.font", "Font"),
                            selected != null ? selected.name : Component.literal("Unknown")
                    ),
                    this.getX() + 19,
                    this.getX() + 19,
                    //? if < 1.21.11 {
                    /*this.getY(),
                    this.getRight() - 3,
                    this.getBottom(),
                    -1
                    *///?} else {
                    this.getRight() - 3,
                    this.getY(),
                    this.getBottom()
                    //?}
            );
            return;
        }

        //? if > 1.21.6
        if (this.isHovered()) guiGraphics.requestCursor(CursorType.DEFAULT);
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        //? if > 1.21.6 {
        Entry entry = this.getEntryAtPosition(mouseX, mouseY);
        if (entry != null && entry != this.getSelected()) guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
        //?}
    }

    //? if > 1.21.6 {
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
    //?}

    //? if <= 1.21.6 {
    /*@Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.open && super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.open && super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return this.open && super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        return this.open && super.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
    }
    *///?}

    public class Entry extends ObjectSelectionList.Entry<FontSelectionWidget.Entry> {
        final @Nullable FontFile fontFile;
        final Component name;

        public Entry(final @Nullable FontFile fontFile) {
            this.fontFile = fontFile;
            this.name = fontFile != null ?
                    Component.literal(fontFile.name) :
                    Component.translatableWithFallback("bigsignwriter.no_font", "None (Vanilla)");
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", this.name);
        }

        @Override
        //? if <= 1.21.6 {
        /*public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick) {
        *///?} else
        public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
            guiGraphics.drawString(
                    FontSelectionWidget.this.minecraft.font,
                    this.name,
                    /*? <= 1.21.6 {*/ /*left *//*?} else {*/ this.getContentX() /*?}*/ + 5,
                    /*? <= 1.21.6 {*/ /*top *//*?} else {*/ this.getContentY() /*?}*/ + 2,
                    -1
            );
        }

        @Override
        //? if <= 1.21.6 {
        /*public boolean mouseClicked(double mouseX, double mouseY, int i) {
        *///?} else
        public boolean mouseClicked(@NotNull MouseButtonEvent mouseButtonEvent, boolean bl) {
            BigSignWriter.selectFont(this.fontFile);
            FontSelectionWidget.this.setSelected(this);
            //? if <= 1.21.6 {
            /*return super.mouseClicked(mouseX, mouseY, i);
            *///?} else
            return super.mouseClicked(mouseButtonEvent, bl);
        }
    }
}
