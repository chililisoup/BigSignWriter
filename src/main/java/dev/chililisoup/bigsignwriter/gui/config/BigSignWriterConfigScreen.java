package dev.chililisoup.bigsignwriter.gui.config;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.font.FontFile;
import dev.chililisoup.bigsignwriter.font.FontInfo;
import dev.chililisoup.bigsignwriter.gui.*;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static dev.chililisoup.bigsignwriter.BigSignWriterConfig.*;

//? if >= 1.21.11 {
import net.minecraft.util.Util;
//?} else {
/*import net.minecraft.Util;
*///?}

//? if < 1.21.3 {
/*import com.mojang.blaze3d.systems.RenderSystem;
*///?} else {
import net.minecraft.client.renderer.RenderPipelines;
//?}

//? if >= 1.21.9 {
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.input.InputWithModifiers;
//?}

public class BigSignWriterConfigScreen extends Screen {
    private static final Component SCREEN_TITLE = Component.translatable("bigsignwriter.config");
    private static final int MARGIN = 16;

    private final MainConfig workingConfig = MAIN_CONFIG.createCopy();
    private final MainConfig defaults = new MainConfig();
    private final Component[] title;

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 33, 60);
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    private @Nullable ConfigTabNavigationBar tabNavigationBar;
    private final Screen parent;

    public BigSignWriterConfigScreen(Screen parent) {
        super(SCREEN_TITLE);
        this.parent = parent;
        this.tabManager.setTabArea(ScreenRectangle.empty());

        List<FontInfo> workingFonts = BigSignWriter.AVAILABLE_FONTS.stream().filter(FontInfo::isWorking).toList();
        this.title = workingFonts.get(Mth.floor(Math.random() * workingFonts.size()))
                .getPreview("Big Sign Writer");
    }

    @Override
    public void onClose() {
        if (!this.workingConfig.equals(MAIN_CONFIG)) {
            MAIN_CONFIG.copyFrom(this.workingConfig);
            saveConfig();
        }
        this.minecraft.setScreen(this.parent);
    }

    @Override
    protected void init() {
        this.tabNavigationBar = new ConfigTabNavigationBar(
                MARGIN,
                this.layout.getHeaderHeight(),
                this.columnWidth(),
                this.tabManager,
                List.of(new ConfigTab[]{
                        new OptionsTab(), new FontsTab()
                })
        );
        this.tabNavigationBar.selectTab(0, false);
        this.addRenderableWidget(this.tabNavigationBar);

        GridLayout footerButtons = new GridLayout().columnSpacing(8).rowSpacing(4);
        footerButtons.addChild(Button.builder(Component.translatable("bigsignwriter.config.openFontsFolder"), button ->
                Util.getPlatform().openPath(BigSignWriter.getFontsDir())
        ).width(120).build(), 0, 0);
        footerButtons.addChild(Button.builder(Component.translatable("bigsignwriter.config.reloadFonts"), button -> {
            BigSignWriter.reloadFonts();
            this.reload();
        }).width(120).build(), 0, 1);
        footerButtons.addChild(Button.builder(CommonComponents.GUI_DONE, button ->
                this.onClose()
        ).width(248).build(), 1, 0, 1, 2);
        this.layout.addToFooter(footerButtons);

        this.layout.visitWidgets(button -> {
            button.setTabOrderGroup(1);
            this.addRenderableWidget(button);
        });
        this.repositionElements();
    }

    private void reload() {
        if (this.tabNavigationBar == null) return;

        int selectedTab = this.tabNavigationBar.tabs.indexOf(this.tabManager.getCurrentTab());
        if (selectedTab < 0) return;

        this.rebuildWidgets();
        if (this.tabNavigationBar != null)
            this.tabNavigationBar.selectTab(selectedTab, false);
    }

    @Override
    //? if >= 26.1 {
    public void extractRenderState(
    //?} else
    //public void render(
            @NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick
    ) {
        if (this.tabNavigationBar == null) return;

        //? if < 1.21.6 {
        /*this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        *///?}

        int iconSize = 40;
        int iconX = this.width - MARGIN - iconSize;
        guiGraphics.blit(
                //? if >= 1.21.3
                RenderPipelines.GUI_TEXTURED,
                BigSignWriter.ICON,
                iconX,
                8,
                0F,
                0F,
                iconSize,
                iconSize,
                iconSize,
                iconSize
        );

        GraphicsHelper.drawFontPreview(guiGraphics, this.title, 1F, iconX - 4, 12, 18);
        String versionString = "§o" + BigSignWriter.VERSION;
        guiGraphics.text(this.font, versionString, iconX - 4 - font.width(versionString), 34, 0xFFAAAAAA);

        int tabHeight = this.tabNavigationBar.layout.getHeight();
        int tabY = this.layout.getHeaderHeight() + tabHeight;
        int tabWidth = this.columnWidth();
        int contentHeight = this.layout.getContentHeight() - tabHeight;
        int leftTabX = MARGIN;
        int rightTabX = this.width - leftTabX - tabWidth;

        this.extractDivider(guiGraphics, leftTabX, tabY, tabWidth, contentHeight);
        this.extractDivider(guiGraphics, rightTabX, tabY, tabWidth, contentHeight);

        //? if < 1.21.3
        //RenderSystem.enableBlend();
        guiGraphics.blit(
                //? if >= 1.21.3
                RenderPipelines.GUI_TEXTURED,
                Screen.HEADER_SEPARATOR,
                rightTabX,
                tabY - 2,
                0.0F,
                0.0F,
                tabWidth,
                2,
                32,
                2
        );
        //? if < 1.21.3
        //RenderSystem.disableBlend();

        //? if < 1.21.6 {
        /*for (Renderable renderable : this.renderables) renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        *///?} else {
        //? if >= 26.1 {
        super.extractRenderState(
        //?} else
        //super.render(
                guiGraphics, mouseX, mouseY, partialTick
        );
        //?}
    }

    private void extractDivider(GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height) {
        this.extractMenuBackground(guiGraphics, x, y, width, height - 2);

        //? if < 1.21.3
        //RenderSystem.enableBlend();

        guiGraphics.blit(
                //? if >= 1.21.3
                RenderPipelines.GUI_TEXTURED,
                Screen.FOOTER_SEPARATOR,
                x,
                y + height - 2,
                0.0F,
                0.0F,
                width,
                2,
                32,
                2
        );

        //? if < 1.21.3
        //RenderSystem.disableBlend();
    }

    @Override
    public void repositionElements() {
        if (this.tabNavigationBar == null) return;

        int columnWidth = this.columnWidth();
        this.tabNavigationBar.updateWidth(columnWidth);
        int tabHeight = this.tabNavigationBar.getRectangle().height();
        this.tabManager.setTabArea(new ScreenRectangle(
                MARGIN * 2,
                this.layout.getHeaderHeight() + tabHeight + MARGIN,
                columnWidth - MARGIN * 2,
                this.layout.getContentHeight() - tabHeight - MARGIN * 2
        ));
        this.layout.arrangeElements();
    }

    private int columnWidth() {
        return this.width / 2 - (MARGIN * 3 / 2);
    }

    private abstract class ConfigTab<T extends ConfigTab.SidePanel> implements Tab {
        private final Component title;
        private final ScrollableLayout layout;
        protected final T sidePanel;
        final ScrollableLayout sidePanelLayout;

        private @Nullable ScreenRectangle cachedScreenRectangle = null;

        public ConfigTab(Component title) {
            this.title = title;
            this.layout = new ScrollableLayout(
                    BigSignWriterConfigScreen.this.minecraft,
                    this.buildContent(),
                    0
            );
            this.sidePanel = this.buildSidePanel();
            GridLayout sidePanelLayout = new GridLayout();
            sidePanelLayout.addChild(this.sidePanel, 0, 0);
            this.sidePanel.addExtraWidgets(
                    widget -> sidePanelLayout.addChild(widget, 0, 0)
            );
            this.sidePanelLayout = new ScrollableLayout(
                    BigSignWriterConfigScreen.this.minecraft,
                    sidePanelLayout,
                    0
            );
        }

        protected abstract Layout buildContent();

        protected abstract T buildSidePanel();

        public abstract void arrangeElements(int contentWidth);

        protected final Layout getContent() {
            return this.layout.content;
        }

        //? if >= 1.21.6 {
        @Override
        public @NotNull Component getTabExtraNarration() {
            return Component.empty();
        }
        //?}

        @Override
        public @NotNull Component getTabTitle() {
            return this.title;
        }

        @Override
        public void visitChildren(@NotNull Consumer<AbstractWidget> childrenConsumer) {
            this.layout.visitWidgets(childrenConsumer);
            this.sidePanelLayout.visitWidgets(childrenConsumer);
        }

        public void redoLayout() {
            if (this.cachedScreenRectangle != null)
                this.doLayout(this.cachedScreenRectangle);
        }

        @Override
        public void doLayout(@NotNull ScreenRectangle screenRectangle) {
            this.cachedScreenRectangle = screenRectangle;

            int contentWidth = BigSignWriterConfigScreen.this.columnWidth() - MARGIN * 2;
            this.arrangeElements(contentWidth);
            this.sidePanel.setWidth(contentWidth);
            this.sidePanel.maxHeight = screenRectangle.height();
            this.sidePanel.arrangeSelf();

            doScrollableLayout(screenRectangle, this.layout);

            ScreenRectangle sideRectangle = new ScreenRectangle(
                    BigSignWriterConfigScreen.this.width - screenRectangle.left() - screenRectangle.width(),
                    screenRectangle.top(),
                    screenRectangle.width(),
                    screenRectangle.height()
            );
            doScrollableLayout(sideRectangle, this.sidePanelLayout);

            this.sidePanel.arrangeOthers();
        }

        private static void doScrollableLayout(ScreenRectangle screenRectangle, ScrollableLayout layout) {
            layout.arrangeElements();
            layout.setMaxHeight(Math.max(screenRectangle.height(), 0));
            int scrollbarReserve =
                    //? if >= 26.1 {
                    layout.container.scrollbarReserve();
                    //?} else
                    //10;
            layout.container.setWidth(screenRectangle.width() + 2 * scrollbarReserve);
            FrameLayout.alignInRectangle(layout, screenRectangle, 0.5F, 0F);
            layout.container.refreshScrollAmount();
        }

        protected abstract static class SidePanel extends AbstractWidget {
            int maxHeight = 0;

            public SidePanel() {
                super(0, 0, 0, 0, CommonComponents.EMPTY);
                this.active = false;
            }

            protected void addExtraWidgets(Consumer<AbstractWidget> widgetConsumer) {}

            protected abstract void arrangeSelf();

            protected void arrangeOthers() {}

            @Override
            protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}
        }
    }

    private class OptionsTab extends ConfigTab<OptionsTab.OptionsSidePanel> {
        public OptionsTab() {
            super(Component.translatable("bigsignwriter.config.options"));
        }

        @Override
        protected Layout buildContent() {
            GridLayout content = new GridLayout().spacing(8);
            GridLayout.RowHelper rowHelper = content.createRowHelper(1);
            rowHelper.addChild(new OptionElement<>(
                    workingConfig.buttonsX,
                    defaults.buttonsX,
                    value -> workingConfig.buttonsX = value,
                    option -> new OptionController.IntegerController(option, -500, 500, 5),
                    Component.translatable("bigsignwriter.config.buttonsX"),
                    Component.translatable("bigsignwriter.config.buttonsX.desc")
            ));
            rowHelper.addChild(new OptionElement<>(
                    workingConfig.buttonsY,
                    defaults.buttonsY,
                    value -> workingConfig.buttonsY = value,
                    option -> new OptionController.IntegerController(option, -500, 500, 5),
                    Component.translatable("bigsignwriter.config.buttonsY"),
                    Component.translatable("bigsignwriter.config.buttonsY.desc")
            ));
            rowHelper.addChild(new OptionElement<>(
                    workingConfig.buttonsAlignmentX,
                    defaults.buttonsAlignmentX,
                    value -> workingConfig.buttonsAlignmentX = value,
                    option -> new OptionController.DoubleController(option, 0.0, 1.0, 0.01)
                            .withValueFormatter(value -> (int) Math.round(value * 100) + "%"),
                    Component.translatable("bigsignwriter.config.buttonsAlignmentX"),
                    Component.translatable("bigsignwriter.config.buttonsAlignmentX.desc")
            ));
            rowHelper.addChild(new OptionElement<>(
                    workingConfig.buttonsAlignmentY,
                    defaults.buttonsAlignmentY,
                    value -> workingConfig.buttonsAlignmentY = value,
                    option -> new OptionController.DoubleController(option, 0.0, 1.0, 0.01)
                            .withValueFormatter(value -> (int) Math.round(value * 100) + "%"),
                    Component.translatable("bigsignwriter.config.buttonsAlignmentY"),
                    Component.translatable("bigsignwriter.config.buttonsAlignmentY.desc")
            ));
            rowHelper.addChild(new OptionElement<>(
                    workingConfig.fontSelectorCoversDoneButton,
                    defaults.fontSelectorCoversDoneButton,
                    value -> workingConfig.fontSelectorCoversDoneButton = value,
                    OptionController.BooleanController::new,
                    Component.translatable("bigsignwriter.config.fontSelectorCoversDoneButton"),
                    Component.translatable("bigsignwriter.config.fontSelectorCoversDoneButton.desc")
            ));
            rowHelper.addChild(new OptionElement<>(
                    workingConfig.showConfigButton,
                    defaults.showConfigButton,
                    value -> workingConfig.showConfigButton = value,
                    OptionController.BooleanController::new,
                    Component.translatable("bigsignwriter.config.showConfigButton"),
                    Component.translatable("bigsignwriter.config.showConfigButton.desc")
            ));
            rowHelper.addChild(new OptionElement<>(
                    workingConfig.characterSeparatorOverrideEnabled,
                    defaults.characterSeparatorOverrideEnabled,
                    value -> workingConfig.characterSeparatorOverrideEnabled = value,
                    OptionController.BooleanController::new,
                    Component.translatable("bigsignwriter.config.characterSeparatorOverrideEnabled"),
                    Component.translatable("bigsignwriter.config.characterSeparatorOverrideEnabled.desc")
            ));
            rowHelper.addChild(new OptionElement<>(
                    workingConfig.characterSeparatorOverride,
                    defaults.characterSeparatorOverride,
                    value -> workingConfig.characterSeparatorOverride = value,
                    OptionController.StringController::new,
                    Component.translatable("bigsignwriter.config.characterSeparatorOverride"),
                    Component.translatable("bigsignwriter.config.characterSeparatorOverride.desc")
            ));
            return content;
        }

        @Override
        protected OptionsSidePanel buildSidePanel() {
            return new OptionsSidePanel();
        }

        @Override
        public void arrangeElements(int contentWidth) {
            this.getContent().visitChildren(element -> {
                if (element instanceof OptionElement<?> optionElement)
                    optionElement.setWidth(contentWidth);
            });
        }

        private final class OptionsSidePanel extends SidePanel {
            @Override
            protected void arrangeSelf() {
                this.height = this.maxHeight;
            }

            @Override
            protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
                AtomicReference<OptionElement<?>> hoveredOption = new AtomicReference<>();
                AtomicReference<OptionElement<?>> focusedOption = new AtomicReference<>();

                OptionsTab.this.getContent().visitChildren(element -> {
                    if (element instanceof OptionElement<?> optionElement) {
                        int left = optionElement.getX();
                        int right = left + optionElement.getWidth();
                        int top = optionElement.getY();
                        int bottom = top + optionElement.getHeight();
                        if (mouseX >= left && mouseX < right && mouseY >= top && mouseY < bottom)
                            hoveredOption.set(optionElement);

                        AtomicBoolean focused = new AtomicBoolean(false);
                        optionElement.visitWidgets(widget -> {
                            if (widget.isFocused()) focused.set(true);
                        });
                        if (focused.get()) focusedOption.set(optionElement);
                    }
                });

                OptionElement<?> optionElement = hoveredOption.get();
                if (optionElement == null) optionElement = focusedOption.get();
                if (optionElement != null) {
                    GraphicsHelper.drawScrollingString(
                            guiGraphics,
                            optionElement.name.copy().withStyle(ChatFormatting.BOLD),
                            this.getX(),
                            this.getRight(),
                            this.getY(),
                            this.getY() + 10
                    );

                    guiGraphics.fill(this.getX(), this.getY() + 15, this.getRight(), this.getY() + 16, 0xFFFFFFFF);

                    guiGraphics.textWithWordWrap(
                            BigSignWriterConfigScreen.this.font,
                            optionElement.description,
                            this.getX(),
                            this.getY() + 25,
                            this.width,
                            -1
                    );
                }
            }
        }
    }

    private class FontsTab extends ConfigTab<FontsTab.FontsSidePanel> {
        private @Nullable BigSignWriterConfigScreen.FontsTab.FontButton selected = null;

        public FontsTab() {
            super(Component.translatable("bigsignwriter.config.fonts"));
        }

        @Override
        protected Layout buildContent() {
            GridLayout content = new GridLayout().spacing(4);
            GridLayout.RowHelper rowHelper = content.createRowHelper(1);
            BigSignWriter.AVAILABLE_FONTS.forEach(
                    fontFile -> rowHelper.addChild(new FontElement(fontFile))
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
                if (element instanceof FontElement fontElement)
                    fontElement.setWidth(contentWidth);
            });
        }

        private static Component infoLine(String key, Object object) {
            return Component.translatable(
                    key,
                    (object instanceof Component component ?
                            component.copy() :
                            Component.literal(String.valueOf(object))
                    ).withStyle(ChatFormatting.AQUA)
            );
        }

        private class FontElement extends AbstractLayoutElement {
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
                                BigSignWriterConfigScreen.this.workingConfig,
                                fontVisibilityButton.fontVisible
                        );
                    }
                });
                this.fontVisible = fontInfo.isVisible(BigSignWriterConfigScreen.this.workingConfig);
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
                    //? if >= 1.21.9
                    guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
                }

                if (this.fontInfo.error() != null) {
                    Font font = BigSignWriterConfigScreen.this.font;
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

        private final class FontsSidePanel extends SidePanel {
            private static int PREVIEW_LINE_HEIGHT = 18;

            private final ArrayList<Component> infoLines = new ArrayList<>();
            private @Nullable List<Component[]> wrappedFontPreview = null;
            private boolean showInheritedCharacters = false;

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
                    BigSignWriter.saveFontToFile(FontsTab.this.selected.fontInfo);
                    BigSignWriterConfigScreen.this.reload();
                }
            }).tooltip(Tooltip.create(Component.translatable("bigsignwriter.config.fonts.createCopy.desc"))).build();

            private final TickBox inheritedCharactersToggle = new TickBox(
                    false,
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
                FontButton selected = FontsTab.this.selected;
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
                FontButton selected = FontsTab.this.selected;
                this.zoomOutButton.visible = selected != null && selected.fontInfo.isWorking();
                this.zoomInButton.visible = this.zoomOutButton.visible;
                this.copyButton.visible = this.zoomOutButton.visible
                        && selected.fontInfo.isBuiltIn();
                this.inheritedCharactersToggle.visible = selected != null
                        && !selected.fontInfo.parentIsImplicit()
                        && selected.fontInfo.parentFont() != null;
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
                infoLines.add(infoLine("bigsignwriter.font.info.characterCount", fontInfo.characters().size()));
                if (fontInfo.isWorking()) infoLines.add(infoLine("bigsignwriter.font.info.width", fontInfo.widthInfo()));

                Set<Character> charSet;
                if (this.showInheritedCharacters && this.inheritedCharactersToggle.visible) {
                    charSet = new TreeSet<>(FontFile.COMPARATOR);
                    FontInfo nextFont = fontInfo;
                    while (nextFont != null) {
                        charSet.addAll(nextFont.characters().keySet());
                        nextFont = nextFont.parentFont();
                    }
                } else charSet = fontInfo.characters().keySet();

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
                            BigSignWriterConfigScreen.this.font,
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
}
