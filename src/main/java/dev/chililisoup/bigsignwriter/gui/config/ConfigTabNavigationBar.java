package dev.chililisoup.bigsignwriter.gui.config;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.screens.Screen;

//? if < 1.21.3 {
/*import com.mojang.blaze3d.systems.RenderSystem;
*///?} else {
import net.minecraft.client.renderer.RenderPipelines;
//?}

//? if >= 26.2 {
import net.minecraft.client.gui.components.tabs.MenuTabBar;
//?} else {
/*import net.minecraft.client.gui.components.tabs.TabNavigationBar;
*///?}

public class ConfigTabNavigationBar extends
        //? if >= 26.2 {
        MenuTabBar
        //?} else
        //TabNavigationBar
{
    private final int x;
    private final int y;

    public ConfigTabNavigationBar(int x, int y, int width, TabManager tabManager, ImmutableList<Tab> tabs) {
        //? if >= 26.2 {
        super(
                0,
                0,
                width,
                24,
                tabManager,
                ImmutableList.copyOf(tabs.stream().map(
                        tab -> new MenuTabBar.MenuTabButton(tabManager, tab, 0, 24)
                ).toList()),
                tabs
        );
        //?} else
        //super(width, tabManager, tabs);
        this.x = x;
        this.y = y;
    }

    //? if < 26.1 {
    /*public void updateWidth(int width) {
        this.width = width;
        this.arrangeElements();
    }
    *///?}

    @Override
    public void arrangeElements(
            //? if >= 26.2
            int width
    ) {
        super.arrangeElements(
                //? if >= 26.2
                width
        );
        this.layout.setX(this.layout.getX() + this.x);
        this.layout.setY(this.y);
    }

    @Override
    //? if >= 26.2 {
    public void extractWidgetRenderState(
    //?} elif >= 26.1 {
    /*public void extractRenderState(
    *///?} else
    //public void render(
            GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick
    ) {
        //? if < 1.21.3
        //RenderSystem.enableBlend();

        guiGraphics.blit(
                //? if >= 1.21.3
                RenderPipelines.GUI_TEXTURED,
                Screen.HEADER_SEPARATOR,
                this.x,
                this.layout.getY() + this.layout.getHeight() - 2,
                0.0F,
                0.0F,
                ((TabButton) this.children().getFirst()).getX() - this.x,
                2,
                32,
                2
        );

        int afterLastTab = ((TabButton) this.children().getLast()).getRight();
        guiGraphics.blit(
                //? if >= 1.21.3
                RenderPipelines.GUI_TEXTURED,
                Screen.HEADER_SEPARATOR,
                afterLastTab,
                this.layout.getY() + this.layout.getHeight() - 2,
                0.0F,
                0.0F,
                this.x + this.width - afterLastTab,
                2,
                32,
                2
        );

        //? if < 1.21.3
        //RenderSystem.disableBlend();

        for (GuiEventListener child : this.children())
            ((TabButton) child)
                    //? if >= 26.1 {
                    .extractRenderState(
                    //?} else {
                    /*.render(
                    *///?}
                            guiGraphics, mouseX, mouseY, partialTick
                    );
    }
}
