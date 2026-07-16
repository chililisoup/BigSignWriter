package dev.chililisoup.bigsignwriter.gui;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.chililisoup.bigsignwriter.font.SymbolGroup;
import dev.chililisoup.bigsignwriter.font.SymbolReference;
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
    private int maxHeight;
    private final Consumer<SymbolReference> symbolConsumer;
    private final int itemWidth;

    public SymbolGridWidget(
            Minecraft minecraft,
            int width,
            int height,
            int x,
            int y,
            int itemWidth,
            int itemHeight,
            Consumer<SymbolReference> symbolConsumer
    ) {
        super(minecraft, width, height, y, itemHeight);
        this.maxHeight = height;
        this.symbolConsumer = symbolConsumer;
        this.setX(x);
        this.itemWidth = itemWidth;
    }

    public void updateEntries(SymbolGroup group) {
        this.replaceEntries(group.entrySet().stream().map(Entry::new).toList());
        this.updateHeight();
        this.setScrollAmount(0.0);
    }

    public void clearFilter() {
        this.children().forEach(entry -> entry.visible = true);
        this.repositionEntries();
        this.updateHeight();
        this.refreshScrollAmount();
    }

    public void filterEntries(String query) {
        String simpleQuery = simplifyQueryString(query);
        this.children().forEach(entry ->
            entry.visible = simplifyQueryString(entry.name).contains(simpleQuery)
        );

        this.repositionEntries();
        this.updateHeight();
        this.refreshScrollAmount();
    }

    private void updateHeight() {
        this.setHeight(Math.min(this.maxHeight, this.contentHeight()));
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        this.updateHeight();
        this.refreshScrollAmount();
    }

    private static String simplifyQueryString(String query) {
        String lower = query.toLowerCase();
        String filtered = query.replaceAll("[^a-z0-9]", "");
        return filtered.isEmpty() ? lower : filtered;
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
        int visibleIndex = 0;
        for (Entry entry : children) {
            int column = visibleIndex % columns;
            if (entry.isVisible()) visibleIndex++;

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
                (this.children().stream().filter(Entry::isVisible).count() / (float) this.columns())
        ) * this.defaultEntryHeight + 4;
    }

    public class Entry extends ObjectSelectionList.Entry<Entry> {
        private final SymbolReference symbol;
        private final Component[] symbolPreview;
        private final String name;
        private final List<Component> tooltip;
        private boolean shouldScrollPreview = false;
        private boolean visible = true;

        public Entry(Map.Entry<String, SymbolReference> symbolEntry) {
            this.symbol = symbolEntry.getValue();
            this.symbolPreview = Arrays.stream(symbolEntry.getValue().get())
                    .map(Component::literal)
                    .toArray(Component[]::new);
            this.name = symbolEntry.getKey();

            ArrayList<Component> tooltip = new ArrayList<>(List.of(
                    Component.literal(this.name),
                    Component.translatable("bigsignwriter.font.info.height", this.symbol.height()),
                    CommonComponents.EMPTY
            ));
            tooltip.addAll(List.of(this.symbolPreview));
            this.tooltip = List.copyOf(tooltip);
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", this.name);
        }

        public boolean isVisible() {
            return this.visible;
        }

        @Override
        public int getHeight() {
            return this.isVisible() ? super.getHeight() : 0;
        }

        @Override
        public void setHeight(int height) {
            super.setHeight(height);
            this.updateShouldScrollPreview();
        }

        @Override
        public int getWidth() {
            return this.isVisible() ? super.getWidth() : 0;
        }

        @Override
        public void setWidth(int width) {
            super.setWidth(width);
            this.updateShouldScrollPreview();
        }

        private void updateShouldScrollPreview() {
            int symbolWidth = Mth.ceil(GraphicsHelper.getScaledWidth(
                    SymbolGridWidget.this.minecraft.font,
                    this.symbol.get(),
                    this.getContentHeight() - 2
            ));
            this.shouldScrollPreview = symbolWidth > this.getContentWidth() - 2;
        }

        @Override
        //~ if >= 26.1 'renderContent' -> 'extractContent'
        public void extractContent(
                @NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, boolean hovered, float partialTick
        ) {
            if (!this.isVisible()) return;

            int left = this.getContentX();
            int top = this.getContentY();
            int width = this.getContentWidth();
            int height = this.getContentHeight();

            if (hovered) {
                guiGraphics.fill(left, top, left + width, top + height, 0x40FFFFFF);
                guiGraphics.setTooltipForNextFrame(
                        SymbolGridWidget.this.minecraft.font, this.tooltip, Optional.empty(), mouseX, mouseY
                );
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
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
