package dev.chililisoup.bigsignwriter;

import com.google.common.collect.ImmutableMap;
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
    private final Supplier<Integer> lineGetter;
    private final Consumer<Integer> lineSetter;
    private final String[] messages;
    private final Consumer<String> messageSetter;
    private final TextFieldHelper signField;

    public BigFontTyper(
            SignBlockEntity sign,
            Font font,
            Supplier<Integer> lineGetter,
            Consumer<Integer> lineSetter,
            String[] messages,
            Consumer<String> messageSetter,
            TextFieldHelper signField
    ) {
        this.sign = sign;
        this.font = font;
        this.lineGetter = lineGetter;
        this.lineSetter = lineSetter;
        this.messages = messages;
        this.messageSetter = messageSetter;
        this.signField = signField;
    }

    public void onFontSelected() {
        this.signField.setSelectionPos(this.signField.getCursorPos());
        if (!BigSignWriter.enabled()) return;

        int cursorPos = this.getCursorPos();
        int width = this.font.width(this.getMessage().substring(0, cursorPos));
        this.setLine(Math.min(this.getLine(), this.getEffectiveBottomLine()));

        TreeMap<Integer, Integer[]> splitIndices = this.getSplitIndices(true);
        if (splitIndices.containsKey(width)) this.signField.setCursorPos(splitIndices.get(width)[0], false);
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
            else this.signField.setCursorToEnd();
        }
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

    public int getEndLine() {
        return Math.min(BigSignWriter.getHeight() + this.getLine(), this.lineCount());
    }

    private int getEffectiveBottomLine() {
        return Math.max(this.lineCount() - BigSignWriter.getHeight(), 0);
    }

    private int getCursorPos() {
        return Math.min(this.signField.getCursorPos(), this.getMessage().length());
    }

    private TreeMap<Integer, Integer[]> getSplitIndices(boolean afterSeparator) {
        int firstWidth = this.font.width(this.getMessage());
        if (firstWidth == 0) return new TreeMap<>();
        int startLine = this.getLine();
        int endLine = this.getEndLine();

        TreeMap<Integer, Integer[]> splitMap = new TreeMap<>();
        splitMap.put(0, IntStream.of(new int[endLine - startLine]).boxed().toArray(Integer[]::new));

        for (int i = startLine + 1; i < endLine; i++) {
            if (firstWidth != this.font.width(this.messages[i]))
                return splitMap;
        }

        String characterSeparator = BigSignWriter.CHARACTER_SEPARATOR;
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

    private void deleteBigChar(KeyEvent keyEvent) {
        if (keyEvent.hasControlDown()) {
            this.clearSign();
            return;
        }

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

        Map.Entry<Integer, Integer[]> startSplit = splitIndices.lowerEntry(endSplit.getKey());
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

    public void charTyped(char chr) {
        String[] bigChar = BigSignWriter.getBigChar(chr).orElse(new String[]{});
        if (bigChar.length == 0) return;

        int startLine = this.getLine();
        int endLine = this.getEndLine();
        int endLength = this.getMessage().length();
        int cursorPos = this.getCursorPos();

        Integer[] split;
        if (cursorPos <= 0) {
            split = IntStream.of(new int[endLine - startLine]).boxed().toArray(Integer[]::new);
            cursorPos = 0;
        } else if (cursorPos == endLength) {
            split = Arrays.stream(
                    Arrays.copyOfRange(this.messages, startLine, endLine)
            ).map(String::length).toArray(Integer[]::new);
        } else {
            TreeMap<Integer, Integer[]> splitIndices = this.getSplitIndices(true);
            if (splitIndices.isEmpty()) {
                split = Arrays.stream(
                        Arrays.copyOfRange(this.messages, startLine, endLine)
                ).map(String::length).toArray(Integer[]::new);
                cursorPos = endLength;
            } else {
                int width = this.font.width(this.getMessage().substring(0, cursorPos));
                if (splitIndices.containsKey(width)) split = splitIndices.get(width);
                else {
                    split = splitIndices.lastEntry().getValue();
                    cursorPos = endLength;
                }
            }
        }

        boolean atEnd = split[0] == endLength;
        int newCursorPos = -1;

        for (int i = startLine; i < endLine; i++) {
            int charLine = i - startLine;
            if (charLine >= split.length || charLine >= bigChar.length || bigChar[charLine] == null) continue;

            String separator = this.messages[i].isEmpty() ? "" : BigSignWriter.CHARACTER_SEPARATOR;
            String addition = atEnd ? separator + bigChar[charLine] : bigChar[charLine] + separator;
            String message = this.messages[i].substring(0, split[charLine]) + addition + this.messages[i].substring(split[charLine]);

            if (this.font.width(message) > this.sign.getMaxTextLineWidth())
                continue;

            this.setLine(i);
            this.setMessage(message);
            if (charLine == 0) newCursorPos = cursorPos + addition.length();
        }

        this.setLine(startLine);
        if (newCursorPos >= 0)
            this.signField.setCursorPos(newCursorPos, false);
        else this.signField.setCursorPos(cursorPos, false);
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
                if (this.getLine() == 0 && BigSignWriter.getHeight() * 2 > this.messages.length)
                    this.setLine(this.getEffectiveBottomLine());
                else this.setLine(this.getLine() + BigSignWriter.getHeight());
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
