package dev.chililisoup.bigsignwriter.gui;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.font.SymbolGroup;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class SymbolGroupListWidget extends ObjectSelectionList<SymbolGroupListWidget.Entry> {
    private final int maxHeight;
    private final Consumer<@Nullable SymbolGroup> onSelect;

    public SymbolGroupListWidget(
            Minecraft minecraft,
            int width,
            int height,
            int x,
            int y,
            int itemHeight,
            Consumer<@Nullable SymbolGroup> onSelect
    ) {
        super(minecraft, width, height, y, itemHeight);
        this.maxHeight = height;
        this.onSelect = onSelect;
        this.setX(x);
        this.updateEntries();
    }

    public void updateEntries() {
        this.replaceEntries(BigSignWriter.availableSymbolGroups().stream()
                .map(Entry::new).toList()
        );
        super.setSelected(null);

        this.setHeight(Math.min(this.maxHeight, this.contentHeight()));
        this.setScrollAmount(0.0);
    }

    @Override
    public int getRowWidth() {
        return this.width - 32;
    }

    @Override
    public void setSelected(@Nullable Entry entry) {
        if (this.getSelected() == entry) return;
        this.playDownSound(this.minecraft.getSoundManager());
        super.setSelected(entry);
        this.onSelect.accept(getEntryGroup(entry));
    }

    public @Nullable SymbolGroup getSelectedGroup() {
        return getEntryGroup(this.getSelected());
    }

    private static @Nullable SymbolGroup getEntryGroup(@Nullable Entry entry) {
        return Optional.ofNullable(entry)
                .map(Entry::getGroup)
                .orElse(null);
    }

    public void setSelectedGroup(@Nullable SymbolGroup group) {
        if (group == null) {
            super.setSelected(null);
            return;
        }

        for (Entry entry : this.children()) {
            if (entry.group.equals(group)) {
                super.setSelected(entry);
                break;
            }
        }
    }

    public static class Entry extends ObjectSelectionList.Entry<Entry> {
        private final SymbolGroup group;
        private final Component name;

        public Entry(SymbolGroup group) {
            this.group = group;
            this.name = Component.literal(group.name());
        }

        public SymbolGroup getGroup() {
            return this.group;
        }

        public Component getName() {
            return this.name;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", this.name);
        }

        @Override
        //? if <= 1.21.6 {
        /*public void render(GuiGraphicsExtractor guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick) {
        *///?} else {
        //? if >= 26.1 {
        public void extractContent(
        //?} else
        //public void renderContent(
                @NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, boolean hovered, float partialTick
        ) {
        //?}
            //? if <= 1.21.6 {
            /*width -= 4;
            *///?} else {
            int left = this.getContentX();
            int top = this.getContentY();
            int width = this.getContentWidth();
            int height = this.getContentHeight();
            //?}

            if (hovered) {
                guiGraphics.fill(left, top, left + width, top + height, 0x40FFFFFF);
                //? if >= 1.21.9
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
            }

            GraphicsHelper.drawScrollingString(
                    guiGraphics,
                    this.name,
                    left + 5,
                    left + 5,
                    left + width - 5,
                    top,
                    top + height
            );
        }
    }
}
