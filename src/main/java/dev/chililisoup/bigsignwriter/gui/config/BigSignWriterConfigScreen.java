package dev.chililisoup.bigsignwriter.gui.config;

import com.google.common.collect.ImmutableList;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.font.FontInfo;
import dev.chililisoup.bigsignwriter.gui.*;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static dev.chililisoup.bigsignwriter.BigSignWriterConfig.*;

//? if >= 1.21.11 {
import net.minecraft.util.Util;
//?} else {
/*import net.minecraft.Util;
*///?}

//? if >= 1.21.3 {
import net.minecraft.client.renderer.RenderPipelines;
//?}

public class BigSignWriterConfigScreen extends Screen {
    private static final Component SCREEN_TITLE = Component.translatable("bigsignwriter.config");
    public static final int MARGIN = 16;

    protected final MainConfig workingConfig = MAIN_CONFIG.createCopy();
    protected final MainConfig defaults = new MainConfig();
    private final Component[] title;

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 33, 60);
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    private @Nullable ConfigTabNavigationBar tabNavigationBar;
    private final Screen parent;

    public BigSignWriterConfigScreen(Screen parent) {
        super(SCREEN_TITLE);
        this.parent = parent;
        this.tabManager.setTabArea(ScreenRectangle.empty());

        List<FontInfo> workingFonts = BigSignWriter.availableFonts().stream().filter(FontInfo::isWorking).toList();
        this.title = workingFonts.isEmpty() ?
                new Component[]{ Component.empty() } :
                workingFonts.get(Mth.floor(Math.random() * workingFonts.size()))
                        .getPreview("Big Sign Writer");
    }

    @Override
    public void onClose() {
        if (!this.workingConfig.equals(MAIN_CONFIG)) {
            MAIN_CONFIG.copyFrom(this.workingConfig);
            saveConfig();
        }
        this.minecraft.gui.setScreen(this.parent);
    }

    @Override
    protected void init() {
        this.tabNavigationBar = new ConfigTabNavigationBar(
                MARGIN,
                this.layout.getHeaderHeight(),
                this.columnWidth(),
                this.tabManager,
                ImmutableList.of(new OptionsTab(this), new FontsTab(this))
        );
        this.tabNavigationBar.selectTab(0, false);
        this.addRenderableWidget(this.tabNavigationBar);

        GridLayout footerButtons = new GridLayout().columnSpacing(8).rowSpacing(4);
        footerButtons.addChild(Button.builder(Component.translatable("bigsignwriter.config.openFontsFolder"), button ->
                Util.getPlatform().openPath(BigSignWriter.getFontsDir())
        ).width(120).build(), 0, 0);
        footerButtons.addChild(Button.builder(Component.translatable("bigsignwriter.config.reloadFonts"), button -> {
            BigSignWriter.forceReload();
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

    protected void reload() {
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
        GraphicsHelper.drawHeaderSeparator(
                guiGraphics,
                rightTabX,
                tabY - 2,
                tabWidth
        );

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
        GraphicsHelper.drawFooterSeparator(
                guiGraphics,
                x,
                y + height - 2,
                width
        );
    }

    @Override
    public void repositionElements() {
        if (this.tabNavigationBar == null) return;

        int columnWidth = this.columnWidth();
        //? if >= 26.2 {
        this.tabNavigationBar.arrangeElements(columnWidth);
        //?} else
        //this.tabNavigationBar.updateWidth(columnWidth);
        int tabHeight = this.tabNavigationBar.getRectangle().height();
        this.tabManager.setTabArea(new ScreenRectangle(
                MARGIN * 2,
                this.layout.getHeaderHeight() + tabHeight + MARGIN,
                columnWidth - MARGIN * 2,
                this.layout.getContentHeight() - tabHeight - MARGIN * 2
        ));
        this.layout.arrangeElements();
    }

    public int columnWidth() {
        return this.width / 2 - (MARGIN * 3 / 2);
    }
}
