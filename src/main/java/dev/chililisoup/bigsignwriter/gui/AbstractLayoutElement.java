package dev.chililisoup.bigsignwriter.gui;

import net.minecraft.client.gui.layouts.LayoutElement;

public abstract class AbstractLayoutElement implements LayoutElement {
    protected int x = 0;
    protected int y = 0;
    protected int width = 150;
    protected int height = 20;

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
        this.arrangeElements();
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
        this.arrangeElements();
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.arrangeElements();
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
        this.arrangeElements();
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
        this.arrangeElements();
    }

    abstract protected void arrangeElements();
}
