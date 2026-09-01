package dev.chililisoup.bigsignwriter.input;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SignEditContext {
    public final int maxLineWidth;
    public final int signColor;
    public final int textColor;

    public final Font font;
    private final Supplier<Boolean> isInSymbolPickerGetter;
    private final Supplier<Integer> lineGetter;
    private final Consumer<Integer> lineSetter;
    public final String[] messages;
    private final Consumer<String> messageSetter;
    public final TextFieldHelper signField;

    public final BigFontTyper fontTyper;

    public SignEditContext(
            SignBlockEntity sign,
            SignText text,
            Font font,
            Supplier<Boolean> isInSymbolPickerGetter,
            Supplier<Integer> lineGetter,
            Consumer<Integer> lineSetter,
            String[] messages,
            Consumer<String> messageSetter,
            TextFieldHelper signField
    ) {
        this.maxLineWidth = sign.getMaxTextLineWidth();
        this.signColor = sign.getBlockState().getBlock().defaultMapColor().col | 0xFF000000;
        this.textColor = text.hasGlowingText() ?
                text.getColor().getTextColor() :
                AbstractSignRenderer.getDarkColor(text);

        this.font = font;
        this.isInSymbolPickerGetter = isInSymbolPickerGetter;
        this.lineGetter = lineGetter;
        this.lineSetter = lineSetter;
        this.messages = messages;
        this.messageSetter = messageSetter;
        this.signField = signField;

        this.fontTyper = new BigFontTyper(this);
    }

    public boolean isInSymbolPicker() {
        return this.isInSymbolPickerGetter.get();
    }

    public int cursorHeight() {
        return this.isInSymbolPicker() ? 1 : BigSignWriter.height();
    }

    public int getLine() {
        return this.lineGetter.get();
    }

    public void setLine(int line) {
        this.lineSetter.accept(line);
    }

    public void setCurrentLineMessage(String message) {
        this.messageSetter.accept(message);
    }

    public int lineCount() {
        return this.messages.length;
    }

    public int getClampedLine(int height, int line) {
        return Math.clamp(this.lineCount() - height, 0, line);
    }

    public int getClampedLine(int height) {
        return this.getClampedLine(height, this.getLine());
    }

    public int getClampedLine() {
        return this.getClampedLine(BigSignWriter.height());
    }

    public int getEndLine(int height, int startLine) {
        return Math.min(height + startLine, this.lineCount());
    }

    public int getEndLine(int height) {
        return this.getEndLine(height, this.getLine());
    }

    public int getEndLine() {
        return this.getEndLine(BigSignWriter.height());
    }

    public String getWidestMessage(int startLine, int endLine) {
        String widestMessage = this.messages[startLine];
        int maxWidth = this.font.width(widestMessage);

        for (int i = startLine + 1; i < endLine; i++) {
            String message = this.messages[i];
            int width = this.font.width(message);
            if (width > maxWidth) {
                widestMessage = message;
                maxWidth = width;
            }
        }

        return widestMessage;
    }

    public String getWidestMessage() {
        return this.getWidestMessage(this.getLine(), this.getEndLine());
    }
}
