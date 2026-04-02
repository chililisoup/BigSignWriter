package dev.chililisoup.bigsignwriter.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

//? if < 1.21.4 {
/*import org.joml.Vector3f;
*///?}

//? if >= 1.21.11 {
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
//?} else {
/*import net.minecraft.util.Mth;
import net.minecraft.Util;
//? if > 1.21.3
import net.minecraft.client.gui.components.AbstractWidget;
*///?}

public final class VersionHelper {
    public static void drawScrollingString(GuiGraphicsExtractor guiGraphics, Component text, int centerX, int left, int right, int top, int bottom) {
        // <= 1.21.3 doesn't use AbstractWidget.renderScrollingString
        // cause its scissor doesn't care for the pose transform

        //? if <= 1.21.3 {
        /*Font font = Minecraft.getInstance().font;
        int width = font.width(text);
        int middleY = (top + bottom - 9) / 2 + 1;
        int maxWidth = right - left;
        if (width <= maxWidth)
            guiGraphics.drawCenteredString(
                    font,
                    text,
                    Mth.clamp(centerX, left + width / 2, right - width / 2),
                    middleY,
                    -1
            );
        else {
            int hiddenWidth = width - maxWidth;
            double time = Util.getMillis() / 1000.0;
            double speed = Math.max(hiddenWidth * 0.5, 3.0);
            double scrollEnd = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * time / speed)) / 2.0 + 0.5;
            double scrollPos = Mth.lerp(scrollEnd, 0.0, hiddenWidth);

            Vector3f scale = guiGraphics.pose().last().pose().getScale(new Vector3f());
            Vector3f translation = guiGraphics.pose().last().pose().getTranslation(new Vector3f());

            guiGraphics.enableScissor(
                    (int) (left * scale.x + translation.x),
                    (int) (top * scale.y + translation.y),
                    (int) (right * scale.x + translation.x),
                    (int) (bottom * scale.y + translation.y)
            );

            guiGraphics.text(font, text, left - (int) scrollPos, middleY, -1);
            guiGraphics.disableScissor();
        }
        *///?} else {
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
        //?}
    }

    public static void drawScrollingString(GuiGraphicsExtractor guiGraphics, Component text, int left, int right, int top, int bottom) {
        drawScrollingString(guiGraphics, text, (left + right) / 2, left, right, top, bottom);
    }

    public static void drawScrollingFontPreview(GuiGraphicsExtractor guiGraphics, Component[] fontPreview, int x, int y, int width, int height) {
        float scale = (height / 9F) / (float) fontPreview.length;
        int scaledWidth = (int) (width / scale);

        //? if < 1.21.6 {
        /*guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, scale);
        *///?} else {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        guiGraphics.pose().scale(scale);
        //?}

        Font font = Minecraft.getInstance().font;
        int previewWidth = font.width(fontPreview[0]);
        if (previewWidth <= scaledWidth) {
            for (int i = 0; i < fontPreview.length; i++)
                //? if >= 1.21.11 {
                guiGraphics.textRenderer().accept(TextAlignment.LEFT, 0, i * 9, fontPreview[i]);
                //?} else
                //guiGraphics.text(font, fontPreview[i], 0, i * 9, -1);
        } else {
            int scaledHeight = (int) (height / scale);
            int hiddenWidth = previewWidth - scaledWidth;
            double time = (double) Util.getMillis() / (500.0 * (double) scale);
            double speed = Math.max(hiddenWidth * 0.5, 3.0);
            double scrollEnd = Math.sin((Math.PI / 2.0) * Math.cos((Math.PI * 2.0) * time / speed)) / 2.0 + 0.5;
            double scrollPos = Mth.lerp(scrollEnd, 0.0, hiddenWidth);
            //? if >= 1.21.11 {
            ActiveTextCollector.Parameters parameters = guiGraphics
                    .textRenderer()
                    .defaultParameters()
                    .withScissor(0, scaledWidth, 0, scaledHeight);
            //?} else if >= 1.21.4 {
            /*guiGraphics.enableScissor(0, 0, scaledWidth, scaledHeight);
            *///?} else {
            /*Vector3f poseScale = guiGraphics.pose().last().pose().getScale(new Vector3f());
            Vector3f poseTranslation = guiGraphics.pose().last().pose().getTranslation(new Vector3f());

            guiGraphics.enableScissor(
                    (int) (0 * poseScale.x + poseTranslation.x),
                    (int) (0 * poseScale.y + poseTranslation.y),
                    (int) (scaledWidth * poseScale.x + poseTranslation.x),
                    (int) (scaledHeight * poseScale.y + poseTranslation.y)
            );
            *///?}

            for (int i = 0; i < fontPreview.length; i++)
                //? if >= 1.21.11 {
                guiGraphics.textRenderer().accept(TextAlignment.LEFT, -(int) scrollPos, i * 9, parameters, fontPreview[i]);
                //?} else
                //guiGraphics.text(font, fontPreview[i], -(int) scrollPos, i * 9, -1);

            //? if < 1.21.11
            //guiGraphics.disableScissor();
        }

        //? if < 1.21.6 {
        /*guiGraphics.pose().popMatrix();
        *///?} else
        guiGraphics.pose().popMatrix();
    }

    public static void drawFontPreview(GuiGraphicsExtractor guiGraphics, Component[] fontPreview, float anchorX, int x, int y, int height) {
        float scale = (height / 9F) / (float) fontPreview.length;

        //? if < 1.21.6 {
        /*guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, scale);
        *///?} else {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        guiGraphics.pose().scale(scale);
        //?}

        Font font = Minecraft.getInstance().font;
        int previewWidth = font.width(fontPreview[0]);
        int xOffset = (int) (anchorX * previewWidth);
        for (int i = 0; i < fontPreview.length; i++)
            //? if >= 1.21.11 {
            guiGraphics.textRenderer().accept(TextAlignment.LEFT, -xOffset, i * 9, fontPreview[i]);
            //?} else
            //guiGraphics.text(font, fontPreview[i], -xOffset, i * 9, -1);

        //? if < 1.21.6 {
        /*guiGraphics.pose().popMatrix();
        *///?} else
        guiGraphics.pose().popMatrix();
    }
}
