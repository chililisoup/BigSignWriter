package dev.chililisoup.bigsignwriter.gui;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.chililisoup.bigsignwriter.font.FontFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Consumer;

//? if > 1.21.6 {
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.input.MouseButtonEvent;
//?} else if < 1.21.4 {
/*import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.util.Mth;
import org.joml.Vector3f;
*///?}

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
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOnOpenChanged(@Nullable Consumer<FontSelectionWidget> onOpenChanged) {
        this.onOpenChanged = onOpenChanged;
    }

    public void updateEntries() {
        this.replaceEntries(BigSignWriter.AVAILABLE_FONTS.stream().map(Entry::new).toList());
        this.addEntryToTop(new Entry(null));
        super.setSelected(this
                .children()
                .stream()
                .filter(entry -> entry.fontFile == BigSignWriter.SELECTED_FONT)
                .findFirst()
                .orElse(null)
        );
        this.setHeight(Math.min(this.maxHeight, this.contentHeight()));
        this.setScrollAmount(0.0);
    }

    private static void drawScrollingString(GuiGraphics guiGraphics, Component text, int left, int top, int right, int bottom) {
        //? if <= 1.21.3 {
        /*Font font = Minecraft.getInstance().font;
        int width = font.width(text);
        int middleY = (top + bottom - 9) / 2 + 1;
        int maxWidth = right - left;
        if (width <= maxWidth) guiGraphics.drawString(font, text, left, middleY, -1);
        else  {
            int hiddenWidth = width - maxWidth;
            double time = Util.getMillis() / 1000.0;
            double speed = Math.max(hiddenWidth * 0.5, 3.0);
            double scrollEnd = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * time / speed)) / 2.0 + 0.5;
            double scrollPos = Mth.lerp(scrollEnd, 0.0, hiddenWidth);

            Vector3f scale = guiGraphics.pose().last().pose().getScale(new Vector3f());
            Vector3f translation = guiGraphics.pose().last().pose().getTranslation(new Vector3f());

            guiGraphics.enableScissor(
                    (int) (left * scale.x + translation.x),
                    (int) (top * scale.y + translation.y),
                    (int) (right * scale.x + translation.x),
                    (int) (bottom * scale.y + translation.y)
            );
            guiGraphics.drawString(font, text, left - (int) scrollPos, middleY, -1);
            guiGraphics.disableScissor();
        }
        *///?} else {
        //? if < 1.21.11 {
        /*renderScrollingString(
                guiGraphics,
                Minecraft.getInstance().font,
        *///?} else
        guiGraphics.textRenderer().acceptScrolling(
                text,
                left,
                left,
                //? if < 1.21.11 {
                /*top,
                right,
                bottom,
                -1
                *///?} else {
                right,
                top,
                bottom
                //?}
        );
        //?}
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
        if (entry != null) BigSignWriter.selectFont(entry.fontFile);
        this.onSelect.run();
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.open) {
            this.renderListBackground(guiGraphics);

            if (mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getRight() && mouseY < this.getBottom()) {
                guiGraphics.fill(
                        this.getX(),
                        this.getY(),
                        this.getRight(),
                        this.getBottom(),
                        0x40FFFFFF
                );
                //? if > 1.21.6
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
            }

            Entry selected = this.getSelected();
            drawScrollingString(
                    guiGraphics,
                    CommonComponents.optionNameValue(
                            Component.translatableWithFallback("bigsignwriter.font", "Font"),
                            selected != null ? selected.name : Component.literal("Unknown")
                    ),
                    this.getX() + 19,
                    this.getY(),
                    this.getRight() - 3,
                    this.getBottom()
            );
            return;
        }

        //? if > 1.21.6
        if (this.isHovered()) guiGraphics.requestCursor(CursorType.DEFAULT);
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    //? if <= 1.21.6 {
    /*public boolean mouseClicked(double mouseX, double mouseY, int button) {
    *///?} else
    public boolean mouseClicked(@NotNull MouseButtonEvent mouseButtonEvent, boolean bl) {
        if (!this.open) {
            //? if > 1.21.6
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

    //? if > 1.21.6 {
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
        final @Nullable FontFile fontFile;
        final Component[] fontPreview;
        final Component name;

        public Entry(final @Nullable FontFile fontFile) {
            this.fontFile = fontFile;
            this.fontPreview = getFontPreview(fontFile);
            this.name = fontFile != null ?
                    Component.literal(fontFile.name) :
                    Component.translatableWithFallback("bigsignwriter.no_font", "None (Vanilla)");
        }

        private static Component[] getFontPreview(@Nullable FontFile fontFile) {
            if (fontFile == null) return new Component[0];

            int height = fontFile.height > 0 ? fontFile.height : 4;
            ArrayList<ArrayList<String>> lines = new ArrayList<>(height);
            for (int i = 0; i < height; i++) lines.add(new ArrayList<>());

            for (char chr : fontFile.name.toCharArray()) {
                String[] bigChar = BigSignWriter.getBigChar(chr, fontFile).orElse(new String[]{""});
                for (int i = 0; i < bigChar.length; i++)
                    lines.get(i).add(bigChar[i]);
            }

            if (lines.isEmpty()) return new Component[0];

            Component[] preview = new Component[height];
            String characterSeparator = fontFile.characterSeparator != null ?
                    fontFile.characterSeparator :
                    BigSignWriterConfig.MAIN_CONFIG.defaultCharacterSeparator;
            for (int i = 0; i < lines.size(); i++)
                preview[i] = Component.literal(String.join(characterSeparator, lines.get(i)));

            return preview;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", this.name);
        }

        @Override
        //? if <= 1.21.6 {
        /*public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick) {
        *///?} else
        public void renderContent(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
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
                //? if > 1.21.6
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
            }

            if (this.fontPreview.length == 0)
                drawScrollingString(
                        guiGraphics, this.name, left + 5, top, left + width - 5, top + height
                );
            else {
                float scale = 1.5F / (float) this.fontPreview.length;
                int scaledWidth = (int) ((width - 10) / scale);

                //? if < 1.21.6 {
                /*guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(left + 5, top, 10);
                guiGraphics.pose().scale(scale, scale, scale);
                *///?} else {
                guiGraphics.pose().pushMatrix();
                guiGraphics.pose().translate(left + 5, top);
                guiGraphics.pose().scale(scale);
                //?}

                for (int i = 0; i < this.fontPreview.length; i++)
                    drawScrollingString(
                            guiGraphics, this.fontPreview[i], 0, i * 8, scaledWidth, height + i * 8
                    );

                //? if < 1.21.6 {
                /*guiGraphics.pose().popPose();
                *///?} else
                guiGraphics.pose().popMatrix();
            }
        }
    }
}
