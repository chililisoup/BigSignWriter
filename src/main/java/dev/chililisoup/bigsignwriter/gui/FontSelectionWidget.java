package dev.chililisoup.bigsignwriter.gui;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.chililisoup.bigsignwriter.font.FontInfo;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

//? if > 1.21.6 {
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
//?}

//? if >= 1.21.9 {
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.input.MouseButtonEvent;
//?}

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
        this.replaceEntries(BigSignWriter.AVAILABLE_FONTS.stream()
                .filter(font -> font.isVisible() && font.height() <= this.lineCount)
                .map(Entry::new).toList()
        );
        this.addEntryToTop(new Entry(null));
        super.setSelected(null);

        List<Entry> children = this.children();
        children.forEach(entry -> {
            if (entry.fontInfo == BigSignWriter.SELECTED_FONT)
                super.setSelected(entry);
            //? if > 1.21.6
            entry.update(children);
        });

        this.setHeight(Math.min(this.maxHeight, this.contentHeight()));
        this.setScrollAmount(0.0);
    }

    //? if <= 1.21.3 {
    /*private int contentHeight() {
        return this.getItemCount() * this.itemHeight + 4;
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
        this.setOpen(false);
        this.playDownSound(this.minecraft.getSoundManager());
        if (this.getSelected() == entry) return;
        super.setSelected(entry);
        if (entry != null) BigSignWriter.selectFont(entry.fontInfo);
        this.onSelect.run();
    }

    @Override
    //? if >= 26.1 {
    public void extractWidgetRenderState(
    //?} else
    //public void extractWidgetRenderState(
            @NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick
    ) {
        if (!this.open) {
            //? if >= 26.1 {
            this.extractListBackground(guiGraphics);
            //?} else
            //this.renderListBackground(guiGraphics);

            if (mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getRight() && mouseY < this.getBottom()) {
                guiGraphics.fill(
                        this.getX(),
                        this.getY(),
                        this.getRight(),
                        this.getBottom(),
                        0x40FFFFFF
                );
                //? if >= 1.21.9
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

        //? if >= 1.21.9
        if (this.isHovered()) guiGraphics.requestCursor(CursorType.DEFAULT);

        //? if >= 26.1 {
        super.extractWidgetRenderState(
        //?} else
        //super.extractWidgetRenderState(
                guiGraphics, mouseX, mouseY, partialTick
        );
    }

    @Override
    //? if <= 1.21.6 {
    /*public boolean mouseClicked(double mouseX, double mouseY, int button) {
    *///?} else
    public boolean mouseClicked(@NotNull MouseButtonEvent mouseButtonEvent, boolean bl) {
        //? if > 1.21.3 {
        if (!this.open) {
        //?} else
        //if (!this.open && this.isMouseOver(mouseX, mouseY)) {
            //? if >= 1.21.9
            int button = mouseButtonEvent.button();
            if (button == 0) {
                this.setOpen(true);
                this.playDownSound(this.minecraft.getSoundManager());
            }
            return true;
        }

        //? if <= 1.21.6 {
        /*return super.mouseClicked(mouseX, mouseY, button);
        *///?} else
        return super.mouseClicked(mouseButtonEvent, bl);
    }

    //? if >= 1.21.9 {
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
        private final @Nullable FontInfo fontInfo;
        private final Component[] fontPreview;
        private final Component name;

        //? if > 1.21.6 {
        private @Nullable Entry root = null;
        private boolean collapsed = true;
        private final List<FontInfo> children;
        //?}

        public Entry(final @Nullable FontInfo fontInfo) {
            this.fontInfo = fontInfo;
            this.fontPreview = fontInfo != null ? fontInfo.getPreview() : new Component[0];
            this.name = fontInfo != null ?
                    Component.literal(fontInfo.name()) :
                    Component.translatable("bigsignwriter.font.none");
            //? if > 1.21.6
            this.children = fontInfo != null ? fontInfo.visibleChildren() : List.of();
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", this.name);
        }

        //? if > 1.21.6 {
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
                    if (child == BigSignWriter.SELECTED_FONT) {
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
        //?}

        @Override
        //? if <= 1.21.6 {
        /*public void render(GuiGraphicsExtractor guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean anyHovered, float partialTick) {
        *///?} else {
        //? if >= 26.1 {
        public void extractContent(
        //?} else
        //public void renderContent(
                @NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, boolean anyHovered, float partialTick
        ) {
        //?}
            //? if <= 1.21.6 {
            /*width -= 4;
            *///?} else {
            if (this.isHidden()) return;

            int left = this.getContentX();
            int top = this.getContentY();
            int width = this.getContentWidth();
            int height = this.getContentHeight();
            //?}

            boolean mainHovered = anyHovered && this.getRectangle().containsPoint(mouseX, mouseY);
            Font font = FontSelectionWidget.this.minecraft.font;
            
            if (mainHovered) {
                guiGraphics.fill(left, top, left + width, top + height, 0x40FFFFFF);
                guiGraphics.setTooltipForNextFrame(font, this.name, mouseX, mouseY);
                //? if >= 1.21.9 {
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
                //?} elif <= 1.21.1
                //guiGraphics.flush();
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

            //? if > 1.21.6 {
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

                //? if >= 1.21.9
                if (expandHovered) guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
            } else if (this.hasRoot()) {
                guiGraphics.horizontalLine(left - 8, left - 4, top + 7, -1);
                guiGraphics.verticalLine(left - 8, top - 7, top + 7, -1);
            }
            //?}

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
