package dev.chililisoup.bigsignwriter;

import com.google.common.collect.ImmutableMap;
import com.ibm.icu.impl.Pair;
import dev.chililisoup.bigsignwriter.font.SymbolReference;
import dev.chililisoup.bigsignwriter.util.ModUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class BigFontTyper {
    private final SignBlockEntity sign;
    private final Font font;
    private final Supplier<Integer> cursorHeightGetter;
    private final Supplier<Integer> lineGetter;
    private final Consumer<Integer> lineSetter;
    private final String[] messages;
    private final Consumer<String> messageSetter;
    private final TextFieldHelper signField;

    public BigFontTyper(
            SignBlockEntity sign,
            Font font,
            Supplier<Integer> cursorHeightGetter,
            Supplier<Integer> lineGetter,
            Consumer<Integer> lineSetter,
            String[] messages,
            Consumer<String> messageSetter,
            TextFieldHelper signField
    ) {
        this.sign = sign;
        this.font = font;
        this.cursorHeightGetter = cursorHeightGetter;
        this.lineGetter = lineGetter;
        this.lineSetter = lineSetter;
        this.messages = messages;
        this.messageSetter = messageSetter;
        this.signField = signField;
    }

    private int getClampedLine() {
        return Math.clamp(this.lineCount() - BigSignWriter.height(), 0, this.getLine());
    }

    public void clampLine() {
        this.setLine(this.getClampedLine());
    }

    public void onFontSelected() {
        this.signField.setSelectionPos(this.signField.getCursorPos());
        if (BigSignWriter.isVanillaTyping()) return;

        int cursorPos = this.getCursorPos();
        int width = this.font.width(this.getMessage().substring(0, cursorPos));
        this.clampLine();

        TreeMap<Integer, Integer[]> splitIndices = this.getSplitIndices(true);
        if (splitIndices.isEmpty()) this.signField.setCursorToEnd();
        else if (splitIndices.containsKey(width)) this.signField.setCursorPos(splitIndices.get(width)[0], false);
        else {
            Map.Entry<Integer, Integer[]> lowerSplit = splitIndices.lowerEntry(width);
            Map.Entry<Integer, Integer[]> higherSplit = splitIndices.higherEntry(width);

            if (lowerSplit != null && higherSplit != null) {
                int lowerDist = width - lowerSplit.getKey();
                int higherDist = higherSplit.getKey() - width;
                this.signField.setCursorPos((lowerDist < higherDist ?
                        lowerSplit :
                        higherSplit
                ).getValue()[0], false);
            } else if (lowerSplit != null) this.signField.setCursorPos(lowerSplit.getValue()[0], false);
            else if (higherSplit != null) this.signField.setCursorPos(higherSplit.getValue()[0], false);
        }
    }

    private int cursorHeight() {
        return this.cursorHeightGetter.get();
    }

    private int lineCount() {
        return this.messages.length;
    }

    private int getLine() {
        return this.lineGetter.get();
    }

    private void setLine(int line) {
        this.lineSetter.accept(line);
    }

    private String getMessage() {
        return this.messages[this.getLine()];
    }

    public String[] getMessages() {
        return this.messages.clone();
    }

    private void setMessage(String message) {
        this.messageSetter.accept(message);
    }

    private void clearSign() {
        int startLine = this.getLine();
        int endLine = this.getEndLine();
        for (int i = startLine; i < endLine; i++) {
            this.setLine(i);
            this.setMessage("");
        }
        this.setLine(startLine);
        this.signField.setCursorToEnd();
    }

    public int getEndLine(int height) {
        return Math.min(height + this.getLine(), this.lineCount());
    }

    public int getEndLine() {
        return this.getEndLine(BigSignWriter.height());
    }

    private int getEffectiveBottomLine() {
        return Math.max(this.lineCount() - this.cursorHeight(), 0);
    }

    private int getCursorPos() {
        return Math.min(this.signField.getCursorPos(), this.getMessage().length());
    }

    private TreeMap<Integer, Integer[]> getSplitIndices(
            boolean afterSeparator, int startLine, int endLine, String characterSeparator
    ) {
        int firstWidth = this.font.width(this.getMessage());
        if (firstWidth == 0) return new TreeMap<>();

        for (int i = startLine + 1; i < endLine; i++) {
            if (firstWidth != this.font.width(this.messages[i]))
                return new TreeMap<>();
        }

        TreeMap<Integer, Integer[]> splitMap = new TreeMap<>();
        splitMap.put(0, IntStream.of(new int[endLine - startLine]).boxed().toArray(Integer[]::new));

        HashSet<Integer> widths = new HashSet<>();
        Stream<String> messageStream = Arrays.stream(
                Arrays.copyOfRange(this.messages, startLine, endLine)
        );
        List<ImmutableMap<Integer, Integer>> separatorMap;

        if (characterSeparator.isEmpty()) {
            separatorMap = messageStream.map(message -> {
                ImmutableMap.Builder<Integer, Integer> indices = ImmutableMap.builder();

                List<String> splitMessage = this.font.getSplitter()
                        .splitLines(message.replace(" ", "\u0000"), 0, Style.EMPTY)
                        .stream().map(FormattedText::getString).toList();

                int runningIndex = 0;
                for (String chr : splitMessage) {
                    runningIndex += chr.length();

                    int width = this.font.width(message.substring(0, runningIndex));
                    if (width == 0) continue;
                    indices.put(width, runningIndex);
                    widths.add(width);
                }

                return indices.build();
            }).toList();
        } else {
            separatorMap = messageStream.map(message -> {
                ImmutableMap.Builder<Integer, Integer> indices = ImmutableMap.builder();

                int endWidth = this.font.width(message);
                indices.put(endWidth, message.length());
                widths.add(endWidth);

                for (int index = message.indexOf(characterSeparator);
                     index >= 0;
                     index = message.indexOf(characterSeparator, index + 1)
                ) {
                    int usedIndex = afterSeparator ? index + characterSeparator.length() : index;
                    int width = this.font.width(message.substring(0, usedIndex));
                    if (width == endWidth || width == 0) continue;
                    indices.put(width, usedIndex);
                    widths.add(width);
                }

                return indices.build();
            }).toList();
        }

        for (int width : widths) {
            Integer[] indices = new Integer[separatorMap.size()];

            boolean matching = true;
            for (int i = 0; i < separatorMap.size(); i++) {
                ImmutableMap<Integer, Integer> splits = separatorMap.get(i);
                if (splits.containsKey(width)) indices[i] = splits.get(width);
                else {
                    matching = false;
                    break;
                }
            }

            if (matching) splitMap.put(width, indices);
        }

        return splitMap;
    }

    private TreeMap<Integer, Integer[]> getSplitIndices(boolean afterSeparator, int startLine, int endLine) {
        return this.getSplitIndices(afterSeparator, startLine, endLine, BigSignWriter.characterSeparator());
    }

    private TreeMap<Integer, Integer[]> getSplitIndices(boolean afterSeparator) {
        return this.getSplitIndices(afterSeparator, this.getLine(), this.getEndLine());
    }

    private Pair<Integer[], Integer> getSplit(int startLine, int endLine, String characterSeparator) {
        int cursorPos = this.getCursorPos();
        int endLength = this.messages[startLine].length();

        if (cursorPos <= 0) return Pair.of(
                IntStream.of(new int[endLine - startLine]).boxed().toArray(Integer[]::new),
                0
        );

        if (cursorPos == endLength) {
            return Pair.of(
                    Arrays.stream(
                            Arrays.copyOfRange(this.messages, startLine, endLine)
                    ).map(String::length).toArray(Integer[]::new),
                    cursorPos
            );
        }

        TreeMap<Integer, Integer[]> splitIndices = this.getSplitIndices(
                true, startLine, endLine, characterSeparator
        );

        if (splitIndices.isEmpty()) return Pair.of(
                Arrays.stream(
                        Arrays.copyOfRange(this.messages, startLine, endLine)
                ).map(String::length).toArray(Integer[]::new),
                endLength
        );

        int width = this.font.width(this.getMessage().substring(0, cursorPos));
        return splitIndices.containsKey(width) ?
                Pair.of(splitIndices.get(width), cursorPos) :
                Pair.of(splitIndices.lastEntry().getValue(), endLength);
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

    private Pair<Integer, Integer> getMaxWidths(Integer[] split, int startLine, int endLine) {
        int maxPrefixWidth = 0;
        int maxSuffixWidth = 0;

        for (int i = startLine; i < endLine; i++) {
            int splitLine = i - startLine;

            maxPrefixWidth = Math.max(
                    this.font.width(splitLine >= split.length ?
                            this.messages[i] :
                            this.messages[i].substring(0, split[splitLine])),
                    maxPrefixWidth
            );

            if (splitLine < split.length) maxSuffixWidth = Math.max(
                    this.font.width(this.messages[i].substring(split[splitLine])),
                    maxSuffixWidth
            );
        }

        return Pair.of(maxPrefixWidth, maxSuffixWidth);
    }

    private void deleteBigChar(KeyEvent keyEvent) {
        int cursorPos = this.getCursorPos();
        boolean atEnd = cursorPos == this.getMessage().length();
        if (cursorPos == 0) {
            if (atEnd) this.clearSign();
            return;
        }

        TreeMap<Integer, Integer[]> splitIndices = this.getSplitIndices(!atEnd);
        if (splitIndices.isEmpty()) {
            this.clearSign();
            return;
        }

        int width = this.font.width(this.getMessage().substring(0, cursorPos));
        Map.Entry<Integer, Integer[]> endSplit = splitIndices.lowerEntry(width + 1);
        if (endSplit == null) {
            this.clearSign();
            return;
        }

        Map.Entry<Integer, Integer[]> startSplit = keyEvent.hasControlDown() ?
                splitIndices.firstEntry() :
                splitIndices.lowerEntry(endSplit.getKey());
        if (startSplit == null) {
            startSplit = endSplit;
            endSplit = splitIndices.higherEntry(width);
            if (endSplit == null) {
                this.clearSign();
                return;
            }
        }

        int startLine = this.getLine();
        int endLine = this.getEndLine();
        for (int i = startLine; i < endLine; i++) {
            this.setLine(i);
            this.setMessage(
                    this.messages[i].substring(0, startSplit.getValue()[i - startLine])
                            + this.messages[i].substring(endSplit.getValue()[i - startLine])
            );
        }
        this.setLine(startLine);
        this.signField.setCursorPos(
                cursorPos - (endSplit.getValue()[0] - startSplit.getValue()[0]),
                false
        );
    }

    private void moveCursor(boolean higher) {
        TreeMap<Integer, Integer[]> splitIndices = this.getSplitIndices(true);
        int cursorPos = this.getCursorPos();
        int width = this.font.width(this.getMessage().substring(0, cursorPos));

        Map.Entry<Integer, Integer[]> split = higher ?
                splitIndices.higherEntry(width) :
                splitIndices.lowerEntry(width);

        if (split != null) this.signField.setCursorPos(split.getValue()[0], false);
        else if (higher) this.signField.setCursorToEnd();
        else this.signField.setCursorToStart();
    }

    public void typeLines(String[] lines, String characterSeparator) {
        if (lines.length == 0) return;

        int currentLine = this.getLine();
        int startLine = this.getClampedLine();
        int endLine = Math.min(
                Math.max(BigSignWriter.height(), lines.length) + startLine,
                this.lineCount()
        );
        int endLength = this.messages[startLine].length();

        Pair<Integer[], Integer> splitPair = this.getSplit(startLine, endLine, characterSeparator);
        Integer[] split = splitPair.first;
        int cursorPos = splitPair.second;

        Pair<Integer, Integer> maxWidths = this.getMaxWidths(split, startLine, endLine);
        String bigCharFiller = ModUtil.getGapFiller(this.font.width(lines[0]));
        String separator = Math.max(maxWidths.first, maxWidths.second) > 0 ? characterSeparator : "";
        boolean atEnd = endLength != 0 && split[0] == endLength;
        int newCursorPos = -1;

        for (int i = startLine; i < endLine; i++) {
            int splitLine = i - startLine;
            if (splitLine >= split.length) continue;

            int charLine = i - currentLine;
            String charText = charLine >= 0 && charLine < lines.length && lines[charLine] != null ?
                    lines[charLine] :
                    bigCharFiller;

            String prefix = this.messages[i].substring(0, split[splitLine]);
            String prefixFiller = ModUtil.getGapFiller(maxWidths.first - this.font.width(prefix));
            String addition = atEnd ? separator + charText : charText + separator;
            String suffix = this.messages[i].substring(split[splitLine]);
            String suffixFiller = ModUtil.getGapFiller(maxWidths.second - this.font.width(suffix));
            String message = prefix + prefixFiller + addition + suffixFiller + suffix;

            if (this.font.width(message) > this.sign.getMaxTextLineWidth())
                continue;

            this.setLine(i);
            this.setMessage(message);
            if (charLine == 0) newCursorPos = cursorPos + prefixFiller.length() + addition.length();
        }

        this.setLine(currentLine);
        if (newCursorPos >= 0)
            this.signField.setCursorPos(newCursorPos, false);
        else this.signField.setCursorPos(cursorPos, false);
    }

    public void typeLines(String[] lines) {
        this.typeLines(lines, BigSignWriter.characterSeparator());
    }

    public void typeSymbol(SymbolReference symbol) {
        this.typeLines(symbol.get(), symbol.characterSeparator());
    }

    public void charTyped(char chr) {
        this.typeLines(BigSignWriter.getBigChar(chr).orElse(new String[]{}));
    }

    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.isUp()) {
            this.setLine(this.getLine() - 1);
            if (this.getLine() < 0)
                this.setLine(this.getEffectiveBottomLine());
            this.signField.setCursorToEnd();
            return true;
        } else if (keyEvent.isDown() || keyEvent.isConfirmation()) {
            if (keyEvent.isDown()) this.setLine(this.getLine() + 1);
            else {
                if (this.getLine() == 0 && this.cursorHeight() * 2 > this.messages.length)
                    this.setLine(this.getEffectiveBottomLine());
                else this.setLine(this.getLine() + this.cursorHeight());
            }
            if (this.getLine() > this.getEffectiveBottomLine())
                this.setLine(0);
            this.signField.setCursorToEnd();
            return true;
        } else if (keyEvent.isLeft() || keyEvent.isRight()) {
            if (keyEvent.hasControlDown()) {
                if (keyEvent.isLeft()) this.signField.setCursorToStart();
                else this.signField.setCursorToEnd();
            } else this.moveCursor(keyEvent.isRight());
            return true;
        }

        if (keyEvent.key() != 259) return false;
        this.deleteBigChar(keyEvent);
        return true;
    }
}
