// Backport of Vanilla 1.21.6+'s ScrollableLayout

//? if < 1.21.6 {
/*package dev.chililisoup.bigsignwriter.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

//? if > 1.21.3 {
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.navigation.ScreenDirection;

import java.util.Collection;
//?}

public class ScrollableLayout implements Layout {
    private static final int SCROLLBAR_SPACING = 4;
    private static final int SCROLLBAR_RESERVE = 10;
    public final Layout content;
    public final ScrollableLayout.Container container;
    private int maxHeight;

    public ScrollableLayout(Minecraft minecraft, Layout content, int height) {
        this.content = content;
        this.container = new ScrollableLayout.Container(minecraft, 0, height);
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        this.container.setHeight(Math.min(this.content.getHeight(), maxHeight));
        this.container.refreshScrollAmount();
    }

    @Override
    public void arrangeElements() {
        this.content.arrangeElements();
        this.container.setWidth(this.content.getWidth() + 20);
        this.container.setHeight(Math.min(this.content.getHeight(), this.maxHeight));
        this.container.refreshScrollAmount();
    }

    @Override
    public void visitChildren(Consumer<LayoutElement> visitor) {
        visitor.accept(this.container);
    }

    @Override
    public void setX(int x) {
        this.container.setX(x);
    }

    @Override
    public void setY(int y) {
        this.container.setY(y);
    }

    @Override
    public int getX() {
        return this.container.getX();
    }

    @Override
    public int getY() {
        return this.container.getY();
    }

    @Override
    public int getWidth() {
        return this.container.getWidth();
    }

    @Override
    public int getHeight() {
        return this.container.getHeight();
    }

    //? if > 1.21.3 {
    public class Container extends AbstractContainerWidget {
    //?} else
    //public class Container extends AbstractScrollArea {
        private final Minecraft minecraft;
        private final List<AbstractWidget> children = new ArrayList<>();

        public Container(final Minecraft minecraft, final int width, final int height) {
            super(0, 0, width, height, CommonComponents.EMPTY);
            this.minecraft = minecraft;
            ScrollableLayout.this.content.visitWidgets(this.children::add);
        }

        @Override
        protected int contentHeight() {
            return ScrollableLayout.this.content.getHeight();
        }

        @Override
        protected double scrollRate() {
            return 10.0;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.enableScissor(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height);

            for (AbstractWidget abstractWidget : this.children)
                abstractWidget.render(guiGraphics, mouseX, mouseY, partialTick);

            guiGraphics.disableScissor();
            this.renderScrollbar(guiGraphics);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

        //? if > 1.21.3 {
        @Override
        public @NotNull ScreenRectangle getBorderForArrowNavigation(ScreenDirection direction) {
            return new ScreenRectangle(this.getX(), this.getY(), this.width, this.contentHeight());
        }

        @Override
        public @NotNull Collection<? extends NarratableEntry> getNarratables() {
            return this.children;
        }
        //?}

        @Override
        public void setFocused(@Nullable GuiEventListener focused) {
            super.setFocused(focused);
            if (focused == null || !this.minecraft.getLastInputType().isKeyboard())
                return;

            ScreenRectangle thisRect = this.getRectangle();
            ScreenRectangle focusRect = focused.getRectangle();
            int topDiff = focusRect.top() - thisRect.top();
            int bottomDiff = focusRect.bottom() - thisRect.bottom();
            if (topDiff < 0)
                this.setScrollAmount(this.scrollAmount() + topDiff - SCROLLBAR_SPACING - SCROLLBAR_RESERVE);
            else if (bottomDiff > 0)
                this.setScrollAmount(this.scrollAmount() + bottomDiff + SCROLLBAR_SPACING + SCROLLBAR_RESERVE);
        }

        @Override
        public void setX(int x) {
            super.setX(x);
            ScrollableLayout.this.content.setX(x + SCROLLBAR_RESERVE);
        }

        @Override
        public void setY(int y) {
            super.setY(y);
            ScrollableLayout.this.content.setY(y - (int) this.scrollAmount());
        }

        @Override
        public void setScrollAmount(double scrollAmount) {
            super.setScrollAmount(scrollAmount);
            ScrollableLayout.this.content.setY(this.getRectangle().top() - (int) this.scrollAmount());
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return this.children;
        }
    }
}
*///?}