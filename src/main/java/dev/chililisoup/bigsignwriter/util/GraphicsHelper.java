package dev.chililisoup.bigsignwriter.util;

import dev.chililisoup.bigsignwriter.font.FontInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;

import java.util.List;

//? if < 1.21.11 {
/*import net.minecraft.client.gui.components.AbstractWidget;
*///?}

public final class GraphicsHelper {
    public static final Identifier INWORLD_MENU_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_background.png");
    public static final Identifier MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/menu_list_background.png");
    public static final Identifier INWORLD_MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");

    public static void drawScrollingString(GuiGraphicsExtractor guiGraphics, Component text, int centerX, int left, int right, int top, int bottom) {
        //? if < 1.21.11 {
        /*AbstractWidget.renderScrollingString(
                guiGraphics,
                Minecraft.getInstance().font,
        *///?} else
        guiGraphics.textRenderer().acceptScrolling(
                text,
                centerX,
                left,
                //? if < 1.21.11 {
                /*top,
                right,
                bottom,
                -1
                *///?} else {
                right,
                top,
                bottom
                //?}
        );
    }

    public static void drawScrollingString(GuiGraphicsExtractor guiGraphics, Component text, int left, int right, int top, int bottom) {
        drawScrollingString(guiGraphics, text, (left + right) / 2, left, right, top, bottom);
    }

    public static void drawScrollingString(GuiGraphicsExtractor guiGraphics, Component text, int left, int right, int y) {
        drawScrollingString(guiGraphics, text, left, left, right, y, y + 8);
    }

    private static float getScale(int lineCount, int height, int gap) {
        return (height / (9F + gap)) / (float) lineCount;
    }

    public static float getScaledWidth(Font font, String[] lines, int height, int gap) {
        return lines.length != 0 ?
                font.width(lines[0]) * getScale(lines.length, height, gap) :
                0;
    }

    public static float getScaledWidth(Font font, String[] lines, int height) {
        return getScaledWidth(font, lines, height, 0);
    }

    public static void drawScrollingFontPreview(GuiGraphicsExtractor guiGraphics, Component[] fontPreview, int x, int y, int width, int height, int gap) {
        float scale = getScale(fontPreview.length, height, gap);
        int scaledWidth = (int) (width / scale);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        guiGraphics.pose().scale(scale);

        Font font = Minecraft.getInstance().font;
        int previewWidth = font.width(fontPreview[0]);
        if (previewWidth <= scaledWidth) {
            for (int i = 0; i < fontPreview.length; i++)
                guiGraphics.text(Minecraft.getInstance().font, fontPreview[i], 0, i * (9 + gap), -1, false);
        } else {
            int scaledHeight = (int) (height / scale);
            int hiddenWidth = previewWidth - scaledWidth;
            double time = (double) Util.getMillis() / (500.0 * (double) scale);
            double speed = Math.max(hiddenWidth * 0.5, 3.0);
            double scrollEnd = Math.sin((Math.PI / 2.0) * Math.cos((Math.PI * 2.0) * time / speed)) / 2.0 + 0.5;
            double scrollPos = Mth.lerp(scrollEnd, 0.0, hiddenWidth);
            guiGraphics.enableScissor(0, 0, scaledWidth, scaledHeight);

            for (int i = 0; i < fontPreview.length; i++)
                guiGraphics.text(Minecraft.getInstance().font, fontPreview[i], -(int) scrollPos, i * (9 + gap), -1, false);

            guiGraphics.disableScissor();
        }

        guiGraphics.pose().popMatrix();
    }

    public static void drawScrollingFontPreview(GuiGraphicsExtractor guiGraphics, Component[] fontPreview, int x, int y, int width, int height) {
        drawScrollingFontPreview(guiGraphics, fontPreview, x, y, width, height, 0);
    }

    public static void drawFontPreview(GuiGraphicsExtractor guiGraphics, Component[] fontPreview, float anchorX, int x, int y, int height, int gap) {
        float scale = getScale(fontPreview.length, height, gap);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        guiGraphics.pose().scale(scale);

        Font font = Minecraft.getInstance().font;
        int previewWidth = font.width(fontPreview[0]);
        int xOffset = (int) (anchorX * previewWidth);
        for (int i = 0; i < fontPreview.length; i++)
            guiGraphics.text(Minecraft.getInstance().font, fontPreview[i], -xOffset, i * (9 + gap), -1, false);

        guiGraphics.pose().popMatrix();
    }

    public static void drawFontPreview(GuiGraphicsExtractor guiGraphics, Component[] fontPreview, float anchorX, int x, int y, int height) {
        drawFontPreview(guiGraphics, fontPreview, anchorX, x, y, height, 0);
    }

    public static List<Component[]> getWrappedFontPreview(FontInfo fontInfo, String text, int width, int lineHeight) {
        if (fontInfo.isBroken()) return List.of();
        float scale = getScale(fontInfo.height(), lineHeight, 1);
        return fontInfo.getWrappedFontPreview(text, (int) (width / scale));
    }

    public static List<Component[]> getWrappedSymbolsPreview(FontInfo fontInfo, int width, int lineHeight) {
        if (fontInfo.areSymbolsBroken()) return List.of();
        float scale = getScale(fontInfo.height(), lineHeight, 1);
        return fontInfo.getWrappedSymbolsPreview((int) (width / scale));
    }

    private static void drawSeparator(GuiGraphicsExtractor guiGraphics, int x, int y, int width, Identifier texture) {
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                texture,
                x,
                y,
                0.0F,
                0.0F,
                width,
                2,
                32,
                2
        );
    }

    public static void drawHeaderSeparator(GuiGraphicsExtractor guiGraphics, int x, int y, int width) {
        drawSeparator(guiGraphics, x, y, width, Minecraft.getInstance().level == null ?
                Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR
        );
    }

    public static void drawFooterSeparator(GuiGraphicsExtractor guiGraphics, int x, int y, int width) {
        drawSeparator(guiGraphics, x, y, width, Minecraft.getInstance().level == null ?
                Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR
        );
    }

    public static void drawCompleteMenuBackground(GuiGraphicsExtractor guiGraphics, Identifier menuBackground, int x, int y, int width, int height) {
        Screen.extractMenuBackgroundTexture(
                guiGraphics,
                menuBackground,
                x,
                y,
                0,
                0,
                width,
                height
        );
        drawHeaderSeparator(guiGraphics, x, y - 2, width);
        drawFooterSeparator(guiGraphics, x, y + height, width);
    }

    public static void drawCompleteMenuBackground(GuiGraphicsExtractor guiGraphics, boolean inWorld, int x, int y, int width, int height) {
        drawCompleteMenuBackground(guiGraphics, inWorld ? INWORLD_MENU_BACKGROUND : Screen.MENU_BACKGROUND, x, y, width, height);
    }

    public static void drawCompleteMenuBackground(GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height) {
        drawCompleteMenuBackground(guiGraphics, true, x, y, width, height);
    }

    public static void drawCompleteMenuListBackground(GuiGraphicsExtractor guiGraphics, boolean inWorld, int x, int y, int width, int height) {
        drawCompleteMenuBackground(guiGraphics, inWorld ? INWORLD_MENU_LIST_BACKGROUND : MENU_LIST_BACKGROUND, x, y, width, height);
    }

    public static void drawCompleteMenuListBackground(GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height) {
        drawCompleteMenuListBackground(guiGraphics, true, x, y, width, height);
    }
}
