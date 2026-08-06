package dev.chililisoup.bigsignwriter.gui.config;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.font.FontInfo;
import dev.chililisoup.bigsignwriter.gui.AbstractLayoutElement;
import dev.chililisoup.bigsignwriter.gui.IconButton;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class AbstractFontsTab extends ConfigTab<AbstractFontsTab.AbstractFontsSidePanel> {
    private @Nullable FontButton selected = null;

    public AbstractFontsTab(BigSignWriterConfigScreen screen, Component title) {
        super(screen, title);
    }

    protected abstract Stream<FontInfo> getShownFonts();

    protected @Nullable Component getErrorMessage(FontInfo fontInfo) {
        return fontInfo.error();
    }

    @Override
    protected Layout buildContent() {
        GridLayout content = new GridLayout().spacing(4);
        GridLayout.RowHelper rowHelper = content.createRowHelper(1);
        this.getShownFonts().forEach(font -> rowHelper.addChild(new FontElement(font)));
        return content;
    }

    @Override
    public void arrangeElements(int contentWidth) {
        this.getContent().visitChildren(element -> {
            if (element instanceof FontElement fontElement)
                fontElement.setWidth(contentWidth);
        });
    }

    protected static Component infoLine(String key, Object... objects) {
        return Component.translatable(
                key,
                Arrays.stream(objects).map(object -> (
                                object instanceof Component component ?
                                        component.copy() :
                                        Component.literal(String.valueOf(object))
                        ).withStyle(ChatFormatting.AQUA)
                ).toArray()
        );
    }

    private final class FontElement extends AbstractLayoutElement {
        private final FontButton fontButton;
        private final Button fontVisibilityButton;

        private FontElement(FontInfo fontInfo) {
            this.fontButton = new FontButton(fontInfo);
            this.fontVisibilityButton = new FontVisibilityButton(fontInfo);
            this.height = 24;
        }

        @Override
        protected void arrangeElements() {
            this.fontButton.setPosition(this.x, this.y);
            this.fontButton.setWidth(this.width - 22);
            this.fontVisibilityButton.setPosition(this.x + this.width - 20, this.y + 2);
        }

        @Override
        public void visitWidgets(@NotNull Consumer<AbstractWidget> widgetVisitor) {
            widgetVisitor.accept(this.fontButton);
            widgetVisitor.accept(this.fontVisibilityButton);
        }
    }

    private class FontVisibilityButton extends IconButton {
        private static final Identifier VISIBLE_SPRITE = BigSignWriter.id("visible");
        private static final Identifier HIDDEN_SPRITE = BigSignWriter.id("hidden");

        private boolean fontVisible;

        public FontVisibilityButton(FontInfo fontInfo) {
            super(button -> {
                if (button instanceof FontVisibilityButton fontVisibilityButton) {
                    fontVisibilityButton.fontVisible = !fontVisibilityButton.fontVisible;
                    fontInfo.setVisible(
                            AbstractFontsTab.this.screen.workingConfig,
                            fontVisibilityButton.fontVisible
                    );
                }
            });
            this.fontVisible = fontInfo.isVisible(AbstractFontsTab.this.screen.workingConfig);
        }

        @Override
        protected Identifier getSprite() {
            return this.fontVisible ? VISIBLE_SPRITE : HIDDEN_SPRITE;
        }
    }

    private class FontButton extends AbstractButton {
        private final FontInfo fontInfo;
        private final Component name;
        private final Component[] fontPreview;

        public FontButton(FontInfo fontInfo) {
            super(0, 0, 0, 24, Component.literal(fontInfo.name()));
            this.fontInfo = fontInfo;
            this.name = Component.literal(fontInfo.name());
            this.fontPreview = fontInfo.getPreview();
        }

        private boolean selected() {
            return AbstractFontsTab.this.selected == this;
        }

        @Override
        public void onPress(@NotNull InputWithModifiers input) {
            AbstractFontsTab.this.selected = this;
            AbstractFontsTab.this.redoLayout();
        }

        @Override
        //? if >= 1.21.11 {
        protected void extractContents(
        //?} else
        //protected void extractWidgetRenderState(
                @NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick
        ) {
            int left = this.getX();
            int right = this.getRight();
            int top = this.getY();
            int bottom = this.getBottom();
            int width = this.getWidth();
            int height = this.getHeight();

            if (this.selected()) {
                guiGraphics.fill(left, top, right, bottom, this.isFocused() ? -1 : 0xFF808080);
                guiGraphics.fill(left + 1, top + 1, right - 1, bottom - 1, 0xFF000000);
            } else {
                if (this.isFocused()) guiGraphics.outline(left, top, width, height, -1);

                Screen.extractMenuBackgroundTexture(
                        guiGraphics,
                        Screen.MENU_BACKGROUND,
                        left + 2,
                        top + 2,
                        0.0F,
                        0.0F,
                        width - 4,
                        height - 4
                );
            }

            if (this.isHovered()) {
                guiGraphics.fill(
                        left + 2,
                        top + 2,
                        right - 2,
                        bottom - 2,
                        0x40FFFFFF
                );
                guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, this.name, mouseX, mouseY);
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
            }

            if (AbstractFontsTab.this.getErrorMessage(this.fontInfo) != null) {
                Font font = AbstractFontsTab.this.screen.font;
                Component errorMarker = Component.literal("⚠").withStyle(ChatFormatting.RED);
                int errorMarkerWidth = font.width(errorMarker);
                guiGraphics.text(
                        font,
                        errorMarker,
                        right - errorMarkerWidth - 4,
                        top + height / 2 - 4,
                        0xFFFF0000
                );

                GraphicsHelper.drawScrollingFontPreview(
                        guiGraphics,
                        Arrays.stream(this.fontPreview).map(
                                component -> component.copy().withStyle(ChatFormatting.RED)
                        ).toArray(Component[]::new),
                        left + 3,
                        top + 3,
                        width - errorMarkerWidth - 10,
                        height - 6
                );
            } else GraphicsHelper.drawScrollingFontPreview(
                    guiGraphics,
                    this.fontPreview,
                    left + 3,
                    top + 3,
                    width - 6,
                    height - 6
            );
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            output.add(NarratedElementType.TITLE, this.getMessage());
        }
    }

    protected abstract class AbstractFontsSidePanel extends SidePanel {
        private static final Identifier ZOOM_OUT_SPRITE = BigSignWriter.id("zoom_out");
        private static final Identifier ZOOM_IN_SPRITE = BigSignWriter.id("zoom_in");
        protected static int PREVIEW_LINE_HEIGHT = 20;

        private final ArrayList<Component> infoLines = new ArrayList<>();
        private @Nullable List<Component[]> wrappedFontPreview = null;

        private final Button zoomOutButton = IconButton.basic(ZOOM_OUT_SPRITE, button -> {
            PREVIEW_LINE_HEIGHT = Math.max(PREVIEW_LINE_HEIGHT - 4, 12);
            AbstractFontsTab.this.redoLayout();
        });

        private final Button zoomInButton = IconButton.basic(ZOOM_IN_SPRITE, button -> {
            PREVIEW_LINE_HEIGHT = Math.min(PREVIEW_LINE_HEIGHT + 4, 40);
            AbstractFontsTab.this.redoLayout();
        });

        protected final Button copyButton = Button.builder(Component.translatable("bigsignwriter.config.fonts.createCopy"), button -> {
            if (AbstractFontsTab.this.selected != null) {
                BigSignWriter.copyFontToFile(AbstractFontsTab.this.selected.fontInfo);
                AbstractFontsTab.this.screen.reload();
            }
        }).tooltip(Tooltip.create(Component.translatable("bigsignwriter.config.fonts.createCopy.desc"))).build();

        @Override
        protected void addExtraWidgets(Consumer<AbstractWidget> widgetConsumer) {
            widgetConsumer.accept(this.zoomOutButton);
            widgetConsumer.accept(this.zoomInButton);
            widgetConsumer.accept(this.copyButton);
        }

        private int gapMargin() {
            FontButton selected = AbstractFontsTab.this.selected;
            if (selected == null) return 0;

            return Mth.ceil(
                    PREVIEW_LINE_HEIGHT * (10F * selected.fontInfo.height() - 1F)
                            / (9F * selected.fontInfo.height()) - PREVIEW_LINE_HEIGHT
            );
        }

        private int previewGap() {
            return (int) (5F * PREVIEW_LINE_HEIGHT / 18F) + this.gapMargin();
        }

        private int afterInfoLines() {
            return this.getY() + 27 + this.infoLines.size() * 12;
        }

        private int buttonsHeight() {
            return this.copyButton.visible && this.copyButtonOccupiesFullRow() ? 52 : 30;
        }

        protected abstract void addInfoLines(FontInfo fontInfo, Consumer<Component> lines);

        protected abstract @NotNull List<Component[]> getWrappedFontPreview(FontInfo fontInfo);

        protected void updateButtonVisibility(boolean working, @Nullable FontInfo fontInfo) {
            this.zoomOutButton.visible = working;
            this.zoomInButton.visible = working;
            this.copyButton.visible = working
                    && fontInfo != null
                    && !fontInfo.isFromConfigFolder();
        }

        @Override
        protected void arrangeSelf() {
            FontButton selected = AbstractFontsTab.this.selected;
            this.updateButtonVisibility(
                    selected != null
                            && AbstractFontsTab.this.getErrorMessage(selected.fontInfo) == null,
                    selected != null ? selected.fontInfo : null
            );
            if (selected == null) {
                this.height = this.maxHeight;
                return;
            }
            FontInfo fontInfo = selected.fontInfo;

            this.infoLines.clear();
            this.addInfoLines(fontInfo, this.infoLines::add);
            this.wrappedFontPreview = this.getWrappedFontPreview(fontInfo);

            int previewGap = this.previewGap();
            int previewHeight = this.wrappedFontPreview.size() * (PREVIEW_LINE_HEIGHT + previewGap)
                    - previewGap + this.gapMargin();
            int bottom = this.afterInfoLines() + this.buttonsHeight() + previewGap + previewHeight;
            this.height = Math.max(bottom - this.getY(), this.maxHeight);
        }

        protected boolean copyButtonOccupiesFullRow() {
            return false;
        }

        protected void arrangeButtons(int y) {
            this.zoomOutButton.setPosition(this.getX(), y);
            this.zoomInButton.setPosition(this.getX() + 22, y);
        }

        @Override
        protected void arrangeOthers() {
            int y = this.afterInfoLines() + 10;

            if (this.copyButtonOccupiesFullRow()) {
                this.copyButton.setPosition(this.getX(), y);
                this.copyButton.setWidth(this.getWidth());

                if (this.copyButton.visible) y += 22;
            } else {
                this.copyButton.setPosition(this.getX() + 44, y);
                this.copyButton.setWidth(this.getWidth() - 44);
            }

            this.arrangeButtons(y);
        }

        @Override
        protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
            if (AbstractFontsTab.this.selected == null) return;
            FontInfo fontInfo = AbstractFontsTab.this.selected.fontInfo;

            GraphicsHelper.drawScrollingString(
                    guiGraphics,
                    AbstractFontsTab.this.selected.getMessage().copy().withStyle(ChatFormatting.BOLD),
                    this.getX(),
                    this.getRight(),
                    this.getY(),
                    this.getY() + 10
            );

            guiGraphics.horizontalLine(this.getX(), this.getRight() - 1, this.getY() + 15, -1);

            for (int i = 0; i < this.infoLines.size(); i++) GraphicsHelper.drawScrollingString(
                    guiGraphics,
                    this.infoLines.get(i),
                    this.getX(),
                    this.getX(),
                    this.getRight(),
                    this.getY() + 25 + i * 12,
                    this.getY() + 34 + i * 12
            );

            int afterInfoLines = this.afterInfoLines();
            guiGraphics.horizontalLine(this.getX(), this.getRight() - 1, afterInfoLines, -1);

            Component error = AbstractFontsTab.this.getErrorMessage(fontInfo);
            if (error != null) {
                guiGraphics.textWithWordWrap(
                        AbstractFontsTab.this.screen.font,
                        error.copy().withStyle(ChatFormatting.RED),
                        this.getX(),
                        afterInfoLines + 10,
                        this.width,
                        -1
                );

                return;
            }

            if (this.wrappedFontPreview != null) {
                int previewGap = this.previewGap();
                int y = afterInfoLines + this.buttonsHeight() + previewGap;
                for (int i = 0; i < this.wrappedFontPreview.size(); i++) GraphicsHelper.drawFontPreview(
                        guiGraphics,
                        this.wrappedFontPreview.get(i),
                        0F,
                        this.getX(),
                        y + i * (PREVIEW_LINE_HEIGHT + previewGap),
                        PREVIEW_LINE_HEIGHT,
                        1
                );
            }
        }
    }
}
