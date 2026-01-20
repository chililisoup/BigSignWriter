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

    public class Entry extends ObjectSelectionList.Entry<FontSelectionWidget.Entry> {
        final @Nullable FontFile fontFile;
        final @Nullable String[] fontPreview;
        final Component name;

        public Entry(final @Nullable FontFile fontFile) {
            this.fontFile = fontFile;
            this.fontPreview = getFontPreview(fontFile);
            this.name = fontFile != null ?
                    Component.literal(fontFile.name) :
                    Component.translatableWithFallback("bigsignwriter.no_font", "None (Vanilla)");
        }

        private static @Nullable String[] getFontPreview(@Nullable FontFile fontFile) {
            if (fontFile == null) return null;

            int height = fontFile.height > 0 ? fontFile.height : 4;
            ArrayList<ArrayList<String>> lines = new ArrayList<>(height);
            for (int i = 0; i < height; i++) lines.add(new ArrayList<>());

            for (char chr : fontFile.name.toCharArray()) {
                String[] bigChar = BigSignWriter.getBigChar(chr, fontFile).orElse(new String[]{""});
                for (int i = 0; i < bigChar.length; i++)
                    lines.get(i).add(bigChar[i]);
            }

            if (lines.isEmpty()) return null;

            String[] preview = new String[height];
            String characterSeparator = fontFile.characterSeparator != null ?
                    fontFile.characterSeparator :
                    BigSignWriterConfig.MAIN_CONFIG.defaultCharacterSeparator;
            for (int i = 0; i < lines.size(); i++)
                preview[i] = String.join(characterSeparator, lines.get(i));

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

            if (this.fontPreview == null)
                guiGraphics.drawString(FontSelectionWidget.this.minecraft.font, this.name, left + 5, top + 4, -1);
            else {
                float scale = 1.5F / (float) this.fontPreview.length;

                //? if <= 1.21.3 {
                /*guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(left + 5, top + 2, 0);
                guiGraphics.pose().scale(scale, scale, scale);
                *///?} else {
                guiGraphics.pose().pushMatrix();
                guiGraphics.pose().translate(left + 5, top + 2);
                guiGraphics.pose().scale(scale);
                //?}

                for (int i = 0; i < this.fontPreview.length; i++)
                    guiGraphics.drawString(FontSelectionWidget.this.minecraft.font, this.fontPreview[i], 0, i * 8, -1);

                //? if <= 1.21.3 {
                /*guiGraphics.pose().popPose();
                *///?} else
                guiGraphics.pose().popMatrix();
            }
        }
    }
}
