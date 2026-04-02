// Backport of Vanilla 1.21.4+'s AbstractScrollArea

//? if <= 1.21.3 {
/*package dev.chililisoup.bigsignwriter.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

//? if >= 1.21.3 {
import net.minecraft.client.renderer.RenderPipelines;
//?}

public abstract class AbstractScrollArea extends AbstractContainerWidget {
    public static final int SCROLLBAR_WIDTH = 6;
    private double scrollAmount;
    private static final Identifier SCROLLER_SPRITE = Identifier.withDefaultNamespace("widget/scroller");
    private static final Identifier SCROLLER_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("widget/scroller_background");
    private boolean scrolling;

    public AbstractScrollArea(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (!this.visible) return false;
        this.setScrollAmount(this.scrollAmount() - scrollY * this.scrollRate());
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!this.scrolling) return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);

        if (mouseY < this.getY()) {
            this.setScrollAmount(0.0);
        } else if (mouseY > this.getBottom()) {
            this.setScrollAmount(this.maxScrollAmount());
        } else {
            double clampedMaxScroll = Math.max(1, this.maxScrollAmount());
            double invHeight = Math.max(1.0, clampedMaxScroll / (this.height - this.scrollerHeight()));
            this.setScrollAmount(this.scrollAmount() + dragY * invHeight);
        }

        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button)
                || this.updateScrolling(mouseX, mouseY, button);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        this.scrolling = false;
    }

    public double scrollAmount() {
        return this.scrollAmount;
    }

    public void setScrollAmount(double scrollAmount) {
        this.scrollAmount = Mth.clamp(scrollAmount, 0.0, this.maxScrollAmount());
    }

    public boolean updateScrolling(double mouseX, double mouseY, int button) {
        this.scrolling = this.scrollbarVisible()
                && this.isValidClickButton(button)
                && mouseX >= this.scrollBarX()
                && mouseX <= this.scrollBarX() + SCROLLBAR_WIDTH
                && mouseY >= this.getY()
                && mouseY < this.getBottom();
        return this.scrolling;
    }

    public void refreshScrollAmount() {
        this.setScrollAmount(this.scrollAmount);
    }

    public int maxScrollAmount() {
        return Math.max(0, this.contentHeight() - this.height);
    }

    protected boolean scrollbarVisible() {
        return this.maxScrollAmount() > 0;
    }

    protected int scrollerHeight() {
        return Mth.clamp((int) ((float) (this.height * this.height) / this.contentHeight()), 32, this.height - 8);
    }

    protected int scrollBarX() {
        return this.getRight() - SCROLLBAR_WIDTH;
    }

    protected int scrollBarY() {
        return Math.max(this.getY(), (int) this.scrollAmount * (this.height - this.scrollerHeight()) / this.maxScrollAmount() + this.getY());
    }

    protected void renderScrollbar(GuiGraphicsExtractor guiGraphics) {
        if (this.scrollbarVisible()) {
            guiGraphics.blitSprite(
                    //? if >= 1.21.3
                    RenderPipelines.GUI_TEXTURED,
                    SCROLLER_BACKGROUND_SPRITE,
                    this.scrollBarX(),
                    this.getY(),
                    SCROLLBAR_WIDTH,
                    this.getHeight()
            );
            guiGraphics.blitSprite(
                    //? if >= 1.21.3
                    RenderPipelines.GUI_TEXTURED,
                    SCROLLER_SPRITE,
                    this.scrollBarX(),
                    this.scrollBarY(),
                    SCROLLBAR_WIDTH,
                    this.scrollerHeight()
            );
        }
    }

    protected abstract int contentHeight();

    protected abstract double scrollRate();
}
*///?}
