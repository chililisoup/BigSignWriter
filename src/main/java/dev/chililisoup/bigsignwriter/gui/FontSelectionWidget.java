package dev.chililisoup.bigsignwriter.gui;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.font.FontInfo;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

//? if >= 1.21.9 {
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.input.MouseButtonEvent;
//?}

public class FontSelectionWidget extends ObjectSelectionList<FontSelectionWidget.Entry> {
    private final int maxHeight;
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
            Runnable onSelect
    ) {
        super(minecraft, width, height, y, entryHeight);
        this.maxHeight = height;
        this.onSelect = onSelect;
        this.setX(x);
        this.updateEntries();
    }

    public void setOpen(boolean open) {
        if (this.open == open) return;
        this.open = open;
        if (this.onOpenChanged != null)
            this.onOpenChanged.accept(this);
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
                .filter(FontInfo::isVisible).map(Entry::new).toList()
        );
        this.addEntryToTop(new Entry(null));
        super.setSelected(this
                .children()
                .stream()
                .filter(entry -> entry.fontInfo == BigSignWriter.SELECTED_FONT)
                .findFirst()
                .orElse(null)
        );
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

    public static class Entry extends ObjectSelectionList.Entry<FontSelectionWidget.Entry> {
        final @Nullable FontInfo fontInfo;
        final Component[] fontPreview;
        final Component name;

        public Entry(final @Nullable FontInfo fontInfo) {
            this.fontInfo = fontInfo;
            this.fontPreview = fontInfo == null ? new Component[0] : fontInfo.getPreview();
            this.name = fontInfo != null ?
                    Component.literal(fontInfo.name()) :
                    Component.translatable("bigsignwriter.font.none");
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
                guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, this.name, mouseX, mouseY);
                //? if >= 1.21.9 {
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
                //?} elif <= 1.21.1
                //guiGraphics.flush();
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
                    fontPreview,
                    left + 5,
                    top + 2,
                    width - 10,
                    height - 4
            );
        }
    }
}
