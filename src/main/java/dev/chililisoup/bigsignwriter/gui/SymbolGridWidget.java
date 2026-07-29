package dev.chililisoup.bigsignwriter.gui;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.chililisoup.bigsignwriter.font.SymbolGroup;
import dev.chililisoup.bigsignwriter.font.SymbolReference;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.ChatFormatting;
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
    private int contentHeight;

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
        this.replaceEntries(group.entrySet().stream()
                .map(entry -> new Entry(entry.getValue(), group.isMerged())).toList());
        this.repositionEntries();
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
            entry.visible = simplifyQueryString(entry.symbol.key()).contains(simpleQuery)
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
        return this.width - 20;
    }

    @Override
    protected int scrollBarX() {
        return this.getRight() - 8;
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
        int top = this.getY() + 2 - (int) this.scrollAmount();
        int columns = this.columns();
        int contentHeight = 0;

        List<Entry> children = this.children();
        int visibleIndex = 0;
        for (Entry entry : children) {
            if (!entry.isVisible()) continue;

            entry.setWidth(this.itemWidth);
            boolean wide = entry.shouldScrollPreview;
            if (wide) {
                entry.setWidth(this.itemWidth * 2);
                if (visibleIndex % columns == columns - 1) visibleIndex++;
            }

            int column = visibleIndex % columns;
            entry.setX(this.getRowLeft() + this.itemWidth * column);
            contentHeight = (visibleIndex / columns) * this.defaultEntryHeight;
            entry.setY(top + contentHeight);

            if (wide) visibleIndex++;
            visibleIndex++;
        }

        this.contentHeight = contentHeight + this.defaultEntryHeight + 4;
    }

    @Override
    protected int contentHeight() {
        return this.contentHeight;
    }

    public class Entry extends ObjectSelectionList.Entry<Entry> {
        private final SymbolReference symbol;
        private final Component[] symbolPreview;
        private final ArrayList<Component> tooltip;
        private boolean shouldScrollPreview = false;
        private boolean visible = true;

        public Entry(SymbolReference symbol, boolean addFontToTooltip) {
            this.symbol = symbol;
            this.symbolPreview = Arrays.stream(symbol.get())
                    .map(Component::literal)
                    .toArray(Component[]::new);

            this.tooltip = new ArrayList<>(List.of(
                    Component.literal(symbol.key()),
                    Component.translatable("bigsignwriter.font.info.height", this.symbol.height())
                            .withStyle(ChatFormatting.GRAY),
                    CommonComponents.EMPTY
            ));
            this.tooltip.addAll(List.of(this.symbolPreview));
            if (addFontToTooltip) this.tooltip.addAll(List.of(
                    CommonComponents.EMPTY,
                    Component.literal(symbol.sourceFont().name())
                            .withStyle(ChatFormatting.BLUE)
            ));
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", this.symbol.key());
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
                    this.getContentHeight()
            ));
            this.shouldScrollPreview = symbolWidth > this.getContentWidth();
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
                guiGraphics.fill(left - 1, top - 1, left + width + 1, top + height + 1, 0x40FFFFFF);
                guiGraphics.setTooltipForNextFrame(
                        SymbolGridWidget.this.minecraft.font, this.tooltip, Optional.empty(), mouseX, mouseY
                );
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
            }

            if (this.shouldScrollPreview) {
                GraphicsHelper.drawScrollingFontPreview(
                        guiGraphics,
                        this.symbolPreview,
                        left,
                        top,
                        width,
                        height
                );
            } else GraphicsHelper.drawFontPreview(
                    guiGraphics,
                    this.symbolPreview,
                    0.5F,
                    this.getContentXMiddle(),
                    top,
                    height
            );
        }
    }
}
