package dev.chililisoup.bigsignwriter.gui;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.chililisoup.bigsignwriter.font.SymbolGroup;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class SymbolGridWidget extends ObjectSelectionList<SymbolGridWidget.Entry> {
    private final int maxHeight;
    private final Consumer<String[]> symbolConsumer;
    private final int itemWidth;

    public SymbolGridWidget(
            Minecraft minecraft,
            int width,
            int height,
            int x,
            int y,
            int itemWidth,
            int itemHeight,
            Consumer<String[]> symbolConsumer
    ) {
        super(minecraft, width, height, y, itemHeight);
        this.maxHeight = height;
        this.symbolConsumer = symbolConsumer;
        this.setX(x);
        this.itemWidth = itemWidth;
    }

    public void updateEntries(SymbolGroup group) {
        this.replaceEntries(group.entrySet().stream().map(Entry::new).toList());
        this.setHeight(Math.min(this.maxHeight, this.contentHeight()));
        this.setScrollAmount(0.0);
    }

    @Override
    public int getRowWidth() {
        return this.width - 32;
    }

    @Override
    public void setSelected(@Nullable Entry entry) {
        if (entry == null) return;
        this.playDownSound(this.minecraft.getSoundManager());
        this.symbolConsumer.accept(entry.symbol);
    }

    private int columns() {
        return this.getRowWidth() / this.itemWidth;
    }

    @Override
    public void repositionEntries() {
        int y = this.getY() + 2 - (int) this.scrollAmount();
        int columns = this.columns();

        List<Entry> children = this.children();
        for (int i = 0; i < children.size(); i++) {
            Entry entry = children.get(i);
            int column = i % columns;

            entry.setY(y);
            if (column == columns - 1)
                y += entry.getHeight();

            entry.setX(this.getRowLeft() + this.itemWidth * column);
            entry.setWidth(this.itemWidth);
        }
    }

    @Override
    protected int contentHeight() {
        return Mth.ceil(
                (this.children().size() / (float) this.columns())
        ) * this.defaultEntryHeight + 4;
    }

    public class Entry extends ObjectSelectionList.Entry<Entry> {
        private final String[] symbol;
        private final Component[] symbolPreview;
        private final Component name;
        private final List<Component> tooltip;
        private boolean shouldScrollPreview = false;

        public Entry(Map.Entry<String, String[]> symbol) {
            this.symbol = symbol.getValue();
            this.symbolPreview = Arrays.stream(symbol.getValue())
                    .map(Component::literal)
                    .toArray(Component[]::new);
            this.name = Component.literal(symbol.getKey());

            ArrayList<Component> tooltip = new ArrayList<>(List.of(
                    this.name,
                    Component.translatable("bigsignwriter.font.info.height", this.symbol.length),
                    CommonComponents.EMPTY
            ));
            tooltip.addAll(List.of(this.symbolPreview));
            this.tooltip = List.copyOf(tooltip);
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", this.name);
        }

        @Override
        public void setHeight(int height) {
            super.setHeight(height);
            this.updateShouldScrollPreview();
        }

        @Override
        public void setWidth(int width) {
            super.setWidth(width);
            this.updateShouldScrollPreview();
        }

        private void updateShouldScrollPreview() {
            int symbolWidth = Mth.ceil(GraphicsHelper.getScaledWidth(
                    SymbolGridWidget.this.minecraft.font,
                    this.symbol,
                    this.getContentHeight() - 2
            ));
            this.shouldScrollPreview = symbolWidth > this.getContentWidth() - 2;
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

                //? if <= 1.21.6
                //guiGraphics.disableScissor();
                guiGraphics.setTooltipForNextFrame(
                        SymbolGridWidget.this.minecraft.font, this.tooltip, Optional.empty(), mouseX, mouseY
                );

                //? if >= 1.21.9 {
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
                //?} elif <= 1.21.1
                //guiGraphics.flush();

                //? if <= 1.21.6
                //FontSelectionWidget.this.enableScissor(guiGraphics);
            }

            if (this.shouldScrollPreview) {
                GraphicsHelper.drawScrollingFontPreview(
                        guiGraphics,
                        this.symbolPreview,
                        left + 1,
                        top + 1,
                        width - 2,
                        height - 2
                );
            } else GraphicsHelper.drawFontPreview(
                    guiGraphics,
                    this.symbolPreview,
                    0.5F,
                    this.getContentXMiddle(),
                    top + 1,
                    height - 2
            );
        }
    }
}
