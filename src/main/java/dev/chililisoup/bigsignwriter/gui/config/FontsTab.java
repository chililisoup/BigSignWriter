package dev.chililisoup.bigsignwriter.gui.config;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.font.FontInfo;
import dev.chililisoup.bigsignwriter.gui.AbstractLayoutElement;
import dev.chililisoup.bigsignwriter.gui.IconButton;
import dev.chililisoup.bigsignwriter.gui.TickBox;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

//? if >= 1.21.9 {
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.input.InputWithModifiers;
//?}

public class FontsTab extends ConfigTab<FontsTab.FontsSidePanel> {
    private @Nullable FontButton selected = null;

    public FontsTab(BigSignWriterConfigScreen screen) {
        super(screen, Component.translatable("bigsignwriter.config.fonts"));
    }

    @Override
    protected Layout buildContent() {
        GridLayout content = new GridLayout().spacing(4);
        GridLayout.RowHelper rowHelper = content.createRowHelper(1);
        BigSignWriter.availableFonts().forEach(
                fontFile -> rowHelper.addChild(new FontsTab.FontElement(fontFile))
        );
        return content;
    }

    @Override
    protected FontsSidePanel buildSidePanel() {
        return new FontsSidePanel();
    }

    @Override
    public void arrangeElements(int contentWidth) {
        this.getContent().visitChildren(element -> {
            if (element instanceof FontsTab.FontElement fontElement)
                fontElement.setWidth(contentWidth);
        });
    }

    private static Component infoLine(String key, Object... objects) {
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

    private class FontElement extends AbstractLayoutElement {
        private final FontsTab.FontButton fontButton;
        private final Button fontVisibilityButton;

        private FontElement(FontInfo fontInfo) {
            this.fontButton = new FontsTab.FontButton(fontInfo);
            this.fontVisibilityButton = new FontsTab.FontVisibilityButton(fontInfo);
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
                if (button instanceof FontsTab.FontVisibilityButton fontVisibilityButton) {
                    fontVisibilityButton.fontVisible = !fontVisibilityButton.fontVisible;
                    fontInfo.setVisible(
                            FontsTab.this.screen.workingConfig,
                            fontVisibilityButton.fontVisible
                    );
                }
            });
            this.fontVisible = fontInfo.isVisible(FontsTab.this.screen.workingConfig);
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
            return FontsTab.this.selected == this;
        }

        @Override
        public void onPress(
                //? if >= 1.21.9
                @NotNull InputWithModifiers input
        ) {
            FontsTab.this.selected = this;
            FontsTab.this.redoLayout();
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
                //? if >= 1.21.9 {
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
                //?} elif <= 1.21.1
                //guiGraphics.flush();
            }

            if (this.fontInfo.error() != null) {
                Font font = FontsTab.this.screen.font;
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
                        left + 4,
                        top + 4,
                        width - errorMarkerWidth - 10,
                        height - 8
                );
            } else GraphicsHelper.drawScrollingFontPreview(
                    guiGraphics,
                    this.fontPreview,
                    left + 4,
                    top + 4,
                    width - 8,
                    height - 8
            );
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            output.add(NarratedElementType.TITLE, this.getMessage());
        }
    }

    protected final class FontsSidePanel extends ConfigTab.SidePanel {
        private static int PREVIEW_LINE_HEIGHT = 18;

        private final ArrayList<Component> infoLines = new ArrayList<>();
        private @Nullable List<Component[]> wrappedFontPreview = null;
        private boolean showInheritedCharacters = true;

        private final Button zoomOutButton = Button.builder(Component.literal("\uD83D\uDD0D-"), button -> {
            PREVIEW_LINE_HEIGHT = Math.max(PREVIEW_LINE_HEIGHT - 6, 12);
            FontsTab.this.redoLayout();
        }).width(20).build();

        private final Button zoomInButton = Button.builder(Component.literal("\uD83D\uDD0D+"), button -> {
            PREVIEW_LINE_HEIGHT = Math.min(PREVIEW_LINE_HEIGHT + 6, 36);
            FontsTab.this.redoLayout();
        }).width(20).build();

        private final Button copyButton = Button.builder(Component.translatable("bigsignwriter.config.fonts.createCopy"), button -> {
            if (FontsTab.this.selected != null) {
                BigSignWriter.copyFontToFile(FontsTab.this.selected.fontInfo);
                FontsTab.this.screen.reload();
            }
        }).tooltip(Tooltip.create(Component.translatable("bigsignwriter.config.fonts.createCopy.desc"))).build();

        private final TickBox inheritedCharactersToggle = new TickBox(
                true,
                value -> {
                    this.showInheritedCharacters = value;
                    FontsTab.this.redoLayout();
                },
                Component.translatable("bigsignwriter.config.fonts.showInheritedCharacters")
        );

        @Override
        protected void addExtraWidgets(Consumer<AbstractWidget> widgetConsumer) {
            widgetConsumer.accept(this.zoomOutButton);
            widgetConsumer.accept(this.zoomInButton);
            widgetConsumer.accept(this.copyButton);
            widgetConsumer.accept(this.inheritedCharactersToggle);
        }

        private int gapMargin() {
            FontsTab.FontButton selected = FontsTab.this.selected;
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
            return this.copyButton.visible && this.inheritedCharactersToggle.visible ?
                    52 : 30;
        }

        @Override
        protected void arrangeSelf() {
            FontsTab.FontButton selected = FontsTab.this.selected;
            this.zoomOutButton.visible = selected != null && selected.fontInfo.isWorking();
            this.zoomInButton.visible = this.zoomOutButton.visible;
            this.copyButton.visible = this.zoomOutButton.visible
                    && selected.fontInfo.isBuiltIn();
            this.inheritedCharactersToggle.visible = this.zoomOutButton.visible
                    && selected.fontInfo.hasExplicitParent();
            if (selected == null) {
                this.height = this.maxHeight;
                return;
            }
            FontInfo fontInfo = selected.fontInfo;

            infoLines.clear();
            Optional.ofNullable(fontInfo.credits()).ifPresent(
                    credits -> infoLines.add(infoLine("bigsignwriter.font.info.credits", credits))
            );
            infoLines.add(infoLine("bigsignwriter.font.info.source", fontInfo.source));
            FontInfo parentFont = fontInfo.parentFont();
            if (parentFont != null) infoLines.add(infoLine(
                    fontInfo.parentIsImplicit() ?
                            "bigsignwriter.font.info.parentFont.implicit" :
                            "bigsignwriter.font.info.parentFont",
                    parentFont.name()
            ));
            infoLines.add(fontInfo.hasExplicitParent() ?
                    infoLine(
                            "bigsignwriter.font.info.characterCount.cumulative",
                            fontInfo.cumulativeCharacters().size(),
                            fontInfo.characters().size()
                    ) :
                    infoLine("bigsignwriter.font.info.characterCount", fontInfo.characters().size())
            );
            if (fontInfo.isWorking()) {
                String cumulativeWidthInfo = fontInfo.cumulativeWidthInfo();
                infoLines.add(cumulativeWidthInfo == null ?
                        infoLine("bigsignwriter.font.info.width", fontInfo.widthInfo()) :
                        infoLine(
                                "bigsignwriter.font.info.width.cumulative",
                                cumulativeWidthInfo,
                                fontInfo.widthInfo()
                        )
                );
                infoLines.add(infoLine("bigsignwriter.font.info.height", fontInfo.height()));
            }

            Set<Character> charSet = this.showInheritedCharacters ?
                    fontInfo.cumulativeCharacters() :
                    fontInfo.characters().keySet();
            this.wrappedFontPreview = GraphicsHelper.getWrappedFontPreview(
                    fontInfo,
                    String.join("", charSet.stream().map(String::valueOf).toArray(String[]::new)),
                    this.width,
                    PREVIEW_LINE_HEIGHT
            );

            int previewGap = this.previewGap();
            int previewHeight = this.wrappedFontPreview.size() * (PREVIEW_LINE_HEIGHT + previewGap)
                    - previewGap + this.gapMargin();
            int bottom = this.afterInfoLines() + this.buttonsHeight() + previewGap + previewHeight;
            this.height = Math.max(bottom - this.getY(), this.maxHeight);
        }

        @Override
        protected void arrangeOthers() {
            int y = this.afterInfoLines() + 10;

            if (this.inheritedCharactersToggle.visible) {
                this.copyButton.setPosition(this.getX(), y);
                this.copyButton.setWidth(this.getWidth());

                if (this.copyButton.visible) y += 22;
            } else {
                this.copyButton.setPosition(this.getX() + 44, y);
                this.copyButton.setWidth(this.getWidth() - 44);
            }

            this.zoomOutButton.setPosition(this.getX(), y);
            this.zoomInButton.setPosition(this.getX() + 22, y);
            this.inheritedCharactersToggle.setPosition(this.getX() + 44, y);
            this.inheritedCharactersToggle.setWidth(this.getWidth() - 44);
        }

        @Override
        protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
            if (FontsTab.this.selected == null) return;
            FontInfo fontInfo = FontsTab.this.selected.fontInfo;

            GraphicsHelper.drawScrollingString(
                    guiGraphics,
                    FontsTab.this.selected.getMessage().copy().withStyle(ChatFormatting.BOLD),
                    this.getX(),
                    this.getRight(),
                    this.getY(),
                    this.getY() + 10
            );

            guiGraphics.fill(this.getX(), this.getY() + 15, this.getRight(), this.getY() + 16, 0xFFFFFFFF);

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
            guiGraphics.fill(this.getX(), afterInfoLines, this.getRight(), afterInfoLines + 1, 0xFFFFFFFF);

            Component error = fontInfo.error();
            if (error != null) {
                guiGraphics.textWithWordWrap(
                        FontsTab.this.screen.font,
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
