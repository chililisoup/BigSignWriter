package dev.chililisoup.bigsignwriter.gui.config;

import net.minecraft.client.gui.components.ScrollableLayout;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

//? if <= 1.21.6 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
*///?}

public abstract class ConfigTab<T extends ConfigTab.SidePanel> implements Tab {
    protected final BigSignWriterConfigScreen screen;
    private final Component title;
    private final ScrollableLayout layout;
    protected final T sidePanel;
    final ScrollableLayout sidePanelLayout;

    private @Nullable ScreenRectangle cachedScreenRectangle = null;

    public ConfigTab(BigSignWriterConfigScreen screen, Component title) {
        this.screen = screen;
        this.title = title;

        this.layout = new ScrollableLayout(
                this.screen.minecraft,
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
                this.screen.minecraft,
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

    //? if <= 1.21.6 {
    /*protected final void enableScissor(GuiGraphicsExtractor guiGraphics) {
        //? if 1.21.6 {
        /^ScrollableLayout.Container container = this.layout.container;
        guiGraphics.enableScissor(
                container.getX(),
                container.getY(),
                container.getX() + container.getWidth(),
                container.getY() + container.getHeight()
        );
        ^///?} else
        this.layout.container.enableScissor(guiGraphics);
    }
    *///?}

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

        int contentWidth = this.screen.columnWidth() - BigSignWriterConfigScreen.MARGIN * 2;
        this.arrangeElements(contentWidth);
        this.sidePanel.setWidth(contentWidth);
        this.sidePanel.maxHeight = screenRectangle.height();
        this.sidePanel.arrangeSelf();

        doScrollableLayout(screenRectangle, this.layout);

        ScreenRectangle sideRectangle = new ScreenRectangle(
                this.screen.width - screenRectangle.left() - screenRectangle.width(),
                screenRectangle.top(),
                screenRectangle.width(),
                screenRectangle.height()
        );
        doScrollableLayout(sideRectangle, this.sidePanelLayout);

        this.sidePanel.arrangeOthers();
    }

    //? if >= 26.2 {
    @Override
    public @NotNull Layout getLayout() {
        return this.layout;
    }
    //?}

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
