package dev.chililisoup.bigsignwriter.util;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.font.FontInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

//? if < 1.21.4 {
/*import org.joml.Vector3f;
*///?}

//? if >= 1.21.11 {
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
//?} else {
/*import net.minecraft.util.Mth;
import net.minecraft.Util;
//? if > 1.21.3
import net.minecraft.client.gui.components.AbstractWidget;
*///?}

public final class GraphicsHelper {
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
                guiGraphics.text(Minecraft.getInstance().font, fontPreview[i], 0, i * 9, -1, false);
        } else {
            int scaledHeight = (int) (height / scale);
            int hiddenWidth = previewWidth - scaledWidth;
            double time = (double) Util.getMillis() / (500.0 * (double) scale);
            double speed = Math.max(hiddenWidth * 0.5, 3.0);
            double scrollEnd = Math.sin((Math.PI / 2.0) * Math.cos((Math.PI * 2.0) * time / speed)) / 2.0 + 0.5;
            double scrollPos = Mth.lerp(scrollEnd, 0.0, hiddenWidth);
            //? if >= 1.21.4 {
            guiGraphics.enableScissor(0, 0, scaledWidth, scaledHeight);
            //?} else {
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
                guiGraphics.text(Minecraft.getInstance().font, fontPreview[i], -(int) scrollPos, i * 9, -1, false);

            guiGraphics.disableScissor();
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
            guiGraphics.text(Minecraft.getInstance().font, fontPreview[i], -xOffset, i * 9, -1, false);

        //? if < 1.21.6 {
        /*guiGraphics.pose().popMatrix();
        *///?} else
        guiGraphics.pose().popMatrix();
    }

    public static List<Component[]> getWrappedFontPreview(FontInfo fontInfo, String text, int width, int lineHeight) {
        if (fontInfo.isBroken()) return List.of();

        Font font = Minecraft.getInstance().font;
        float scale = (lineHeight / 9F) / (float) fontInfo.height();
        String characterSeparator = fontInfo.characterSeparator();
        if (characterSeparator.isEmpty()) characterSeparator = " ";
        float separatorWidth = font.width(characterSeparator) * scale;

        ArrayList<Component[]> previewLines = new ArrayList<>();
        StringBuilder runningString = new StringBuilder();
        float runningWidth = 0F;
        for (char chr : text.toCharArray()) {
            String top = BigSignWriter.getBigChar(chr, fontInfo).orElse(new String[]{""})[0];

            float chrWidth = font.width(top) * scale;
            if (runningWidth > 0 && runningWidth + chrWidth > width) {
                if (!runningString.isEmpty())
                    previewLines.add(fontInfo.getPreview(runningString.toString(), characterSeparator));

                runningWidth = chrWidth + separatorWidth;
                runningString = new StringBuilder(String.valueOf(chr));
            } else {
                runningWidth += chrWidth + separatorWidth;
                runningString.append(chr);
            }
        }

        if (!runningString.isEmpty())
            previewLines.add(fontInfo.getPreview(runningString.toString(), characterSeparator));

        return previewLines;
    }
}
