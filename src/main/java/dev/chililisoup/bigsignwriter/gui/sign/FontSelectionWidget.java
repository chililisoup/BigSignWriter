package dev.chililisoup.bigsignwriter.gui.sign;

import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.config.BigSignWriterConfig;
import dev.chililisoup.bigsignwriter.font.FontInfo;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class FontSelectionWidget extends ObjectSelectionList<FontSelectionWidget.Entry> {
    private final int maxHeight;
    private final int lineCount;
    private final Runnable onSelect;
    private @Nullable Consumer<FontSelectionWidget> onOpenChanged;
    private boolean open = false;

    public FontSelectionWidget(
            Minecraft minecraft,
            int width,
            int height,
            int x,
            int y,
            int entryHeight,
            int lineCount,
            Runnable onSelect
    ) {
        super(minecraft, width, height, y, entryHeight);
        this.maxHeight = height;
        this.lineCount = lineCount;
        this.onSelect = onSelect;
        this.setX(x);
        this.updateEntries();
    }

    public void setOpen(boolean open) {
        if (this.open == open) return;
        this.open = open;
        if (this.onOpenChanged != null)
            this.onOpenChanged.accept(this);
        if (BigSignWriterConfig.MAIN_CONFIG.fontSelectorOpensScrolledUp)
            this.setScrollAmount(0.0);
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOnOpenChanged(@Nullable Consumer<FontSelectionWidget> onOpenChanged) {
        this.onOpenChanged = onOpenChanged;
    }

    public void updateEntries() {
        this.replaceEntries(BigSignWriter.availableFonts().stream()
                .filter(font -> font.isVisible()
                        && font.hasCharacters()
                        && font.height() <= this.lineCount
                )
                .map(Entry::new).toList()
        );
        this.addEntryToTop(new Entry(null));
        super.setSelected(null);

        List<Entry> children = this.children();
        children.forEach(entry -> {
            if (entry.fontInfo == BigSignWriter.selectedFont())
                super.setSelected(entry);
            entry.update(children);
        });

        this.setHeight(Math.min(this.maxHeight, this.contentHeight()));
        this.setScrollAmount(0.0);
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
    public void setSelected(Entry entry) {
        this.setOpen(false);
        this.playDownSound(this.minecraft.getSoundManager());
        if (this.getSelected() == entry) return;
        super.setSelected(entry);
        if (entry != null) BigSignWriter.selectFont(entry.fontInfo);
        this.onSelect.run();
    }

    @Override
    public void extractWidgetRenderState(
            @NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick
    ) {
        if (!this.open) {
            //~ if >= 26.1 'renderListBackground' -> 'extractListBackground'
            this.extractListBackground(guiGraphics);

            if (mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getRight() && mouseY < this.getBottom()) {
                guiGraphics.fill(
                        this.getX(),
                        this.getY(),
                        this.getRight(),
                        this.getBottom(),
                        0x40FFFFFF
                );
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
            }

            Entry selected = this.getSelected();
            GraphicsHelper.drawScrollingString(
                    guiGraphics,
                    CommonComponents.optionNameValue(
                            Component.translatable("bigsignwriter.font"),
                            selected != null ? selected.name : Component.translatable("bigsignwriter.font.unknown")
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

        super.extractWidgetRenderState(
                guiGraphics, mouseX, mouseY, partialTick
        );
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean doubleClick) {
        if (!this.open) {
            int button = event.button();
            if (button == 0) {
                this.setOpen(true);
                this.playDownSound(this.minecraft.getSoundManager());
            }
            return true;
        }

        return super.mouseClicked(event, doubleClick);
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

    public class Entry extends ObjectSelectionList.Entry<Entry> {
        private final @Nullable FontInfo fontInfo;
        private final Component[] fontPreview;
        private final Component name;
        private final List<FontInfo> children;
        private @Nullable Entry root = null;
        private boolean collapsed = true;

        public Entry(@Nullable FontInfo fontInfo) {
            this.fontInfo = fontInfo;
            this.fontPreview = fontInfo != null ? fontInfo.getPreview() : new Component[0];
            this.name = fontInfo != null ?
                    Component.literal(fontInfo.name()) :
                    Component.translatable("bigsignwriter.font.none");
            this.children = fontInfo != null ? fontInfo.visibleChildren() : List.of();
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", this.name);
        }

        @Override
        public int getHeight() {
            return this.isHidden() ? 0 : super.getHeight();
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            if (super.isMouseOver(mouseX, mouseY)) return true;
            return this.collapseButtonHovered((int) mouseX, (int) mouseY);
        }

        @Override
        public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean doubleClick) {
            int mouseX = (int) event.x();
            int mouseY = (int) event.y();
            if (this.collapseButtonHovered(mouseX, mouseY)) {
                this.collapsed = !this.collapsed;
                FontSelectionWidget.this.refreshScrollAmount();
                return false;
            }
            return super.mouseClicked(event, doubleClick);
        }

        private boolean collapseButtonHovered(int mouseX, int mouseY) {
            if (this.children.isEmpty()) return false;

            int left = this.getContentX();
            int top = this.getContentY();
            return mouseX >= left - 12 && mouseX < left - 3
                    && mouseY >= top + 4 && mouseY < top + 13;
        }

        private void update(List<Entry> others) {
            if (!this.children.isEmpty()) {
                for (FontInfo child : this.children) {
                    if (child == BigSignWriter.selectedFont()) {
                        this.collapsed = false;
                        break;
                    }
                }

                return;
            }

            if (this.fontInfo == null) return;
            FontInfo rootAncestor = this.fontInfo.rootAncestorFont();
            if (rootAncestor == null) return;
            if (!rootAncestor.isVisible()) return;

            for (Entry entry : others) {
                if (entry.fontInfo == rootAncestor) {
                    this.root = entry;
                    return;
                }
            }
        }

        private boolean hasRoot() {
            return this.root != null;
        }

        private boolean isHidden() {
            return this.hasRoot() && this.root.collapsed;
        }

        @Override
        //~ if >= 26.1 'renderContent' -> 'extractContent'
        public void extractContent(
                @NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, boolean anyHovered, float partialTick
        ) {
            if (this.isHidden()) return;

            int left = this.getContentX();
            int top = this.getContentY();
            int width = this.getContentWidth();
            int height = this.getContentHeight();

            boolean mainHovered = anyHovered && this.getRectangle().containsPoint(mouseX, mouseY);
            Font font = FontSelectionWidget.this.minecraft.font;
            
            if (mainHovered) {
                guiGraphics.fill(left, top, left + width, top + height, 0x40FFFFFF);
                guiGraphics.setTooltipForNextFrame(font, this.name, mouseX, mouseY);
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
            }

            if (this.fontInfo != null && BigSignWriterConfig.MAIN_CONFIG.displayFontHeights) {
                guiGraphics.text(
                        font,
                        String.valueOf(this.fontInfo.height()),
                        left + width + 2,
                        top + 4,
                        0xFFAAAAAA
                );
            }

            if (!this.children.isEmpty()) {
                boolean expandHovered = anyHovered && !mainHovered;
                MutableComponent text = Component.literal(this.collapsed ? "+" : "-");
                guiGraphics.text(
                        font,
                        expandHovered ? text.withStyle(ChatFormatting.UNDERLINE) : text,
                        left - 10,
                        top + 4,
                        -1
                );

                if (expandHovered) guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
            } else if (this.hasRoot()) {
                guiGraphics.horizontalLine(left - 8, left - 4, top + 7, -1);
                guiGraphics.verticalLine(left - 8, top - 7, top + 7, -1);
            }

            if (this.fontPreview.length == 0)
                GraphicsHelper.drawScrollingString(
                        guiGraphics,
                        this.name,
                        left + 5,
                        left + 5,
                        left + width - 5,
                        top,
                        top + height
                );
            else GraphicsHelper.drawScrollingFontPreview(
                    guiGraphics,
                    this.fontPreview,
                    left + 5,
                    top + 2,
                    width - 10,
                    height - 4
            );
        }
    }
}
