package dev.chililisoup.bigsignwriter.mixin;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.gui.ClickableButtonWidget;
import dev.chililisoup.bigsignwriter.gui.FontSelectionWidget;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >= 1.21.9 {
import net.minecraft.client.input.CharacterEvent;
//?}

//? if >= 26.1 {
import org.joml.Vector2f;
//?}

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static dev.chililisoup.bigsignwriter.BigSignWriterConfig.*;

@Mixin(value = AbstractSignEditScreen.class, priority = 999)
public abstract class AbstractSignEditScreenMixin extends Screen {
    protected AbstractSignEditScreenMixin(Component title) { super(title); }

    @Unique
    private static Component bigSignWriter$getDropdownLabel(boolean open) {
        return Component.literal(open ? "▼" : "▶");
    }

    @Unique
    private static int bigSignWriter$getHeight() {
        return BigSignWriter.SELECTED_FONT != null ? BigSignWriter.SELECTED_FONT.height() : 4;
    }

    @Unique
    private void bigSignWriter$clearSign() {
        int startLine = this.line;
        int endLine = this.bigSignWriter$getEndLine();
        for (int i = startLine; i < endLine; i++) {
            //noinspection UnusedAssignment
            this.line = i;
            this.setMessage("");
        }
        this.line = startLine;

        if (this.signField != null)
            this.signField.setCursorToEnd();
    }

    @Unique
    private int bigSignWriter$getEndLine() {
        return Math.min(bigSignWriter$getHeight() + this.line, this.messages.length);
    }

    @Unique
    private int bigSignWriter$getEffectiveBottomLine() {
        return Math.max(this.messages.length - bigSignWriter$getHeight(), 0);
    }

    @Unique
    private int bigSignWriter$getCursorPos() {
        return this.signField != null ?
                Math.min(this.signField.getCursorPos(), this.messages[this.line].length()) :
                0;
    }

    @Unique
    private TreeMap<Integer, Integer[]> bigSignWriter$getSplitIndices(boolean afterSeparator) {
        int startLine = this.line;
        int firstWidth = this.font.width(this.messages[startLine]);
        if (firstWidth == 0) return new TreeMap<>();
        int endLine = this.bigSignWriter$getEndLine();

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

    @Unique
    private void bigSignWriter$deleteBigChar(KeyEvent keyEvent) {
        if (this.signField == null) return;

        if (keyEvent.hasControlDown()) {
            this.bigSignWriter$clearSign();
            return;
        }

        int cursorPos = this.bigSignWriter$getCursorPos();
        if (cursorPos == 0) return;

        boolean atEnd = cursorPos == this.messages[this.line].length();
        TreeMap<Integer, Integer[]> splitIndices = this.bigSignWriter$getSplitIndices(!atEnd);
        if (splitIndices.isEmpty()) {
            this.bigSignWriter$clearSign();
            return;
        }

        int startLine = this.line;
        int endLine = this.bigSignWriter$getEndLine();
        int width = this.font.width(this.messages[this.line].substring(0, cursorPos));

        Map.Entry<Integer, Integer[]> endSplit = splitIndices.lowerEntry(width + 1);
        if (endSplit == null) {
            this.bigSignWriter$clearSign();
            return;
        }
        Map.Entry<Integer, Integer[]> startSplit = splitIndices.lowerEntry(endSplit.getKey());
        if (startSplit == null) {
            startSplit = endSplit;
            endSplit = splitIndices.higherEntry(width);
            if (endSplit == null) {
                this.bigSignWriter$clearSign();
                return;
            }
        }

        for (int i = startLine; i < endLine; i++) {
            //noinspection UnusedAssignment
            this.line = i;
            this.setMessage(
                    this.messages[i].substring(0, startSplit.getValue()[i - startLine])
                    + this.messages[i].substring(endSplit.getValue()[i - startLine])
            );
        }
        this.line = startLine;
        if (this.signField != null) this.signField.setCursorPos(
                cursorPos - (endSplit.getValue()[0] - startSplit.getValue()[0]),
                false
        );
    }

    @Unique
    private void bigSignWriter$moveCursor(boolean higher) {
        if (this.signField == null) return;

        TreeMap<Integer, Integer[]> splitIndices = this.bigSignWriter$getSplitIndices(true);
        int cursorPos = this.bigSignWriter$getCursorPos();
        int width = this.font.width(this.messages[this.line].substring(0, cursorPos));

        Map.Entry<Integer, Integer[]> split = higher ?
                splitIndices.higherEntry(width) :
                splitIndices.lowerEntry(width);

        if (split != null) this.signField.setCursorPos(split.getValue()[0], false);
    }

    @Unique private @Nullable Button doneButton;

    @Shadow /*? if >= 1.21.2 {*/ protected /*?} else {*/ /*private *//*?}*/ @Final SignBlockEntity sign;
    @Shadow private @Final String[] messages;
    @Shadow private void setMessage(String message) {}
    @Shadow private int line;
    @Shadow private @Nullable TextFieldHelper signField;

    @WrapOperation(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/AbstractSignEditScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"
            )
    )
    private GuiEventListener grabDoneButton(AbstractSignEditScreen instance, GuiEventListener guiEventListener, Operation<GuiEventListener> original) {
        GuiEventListener doneButton = original.call(instance, guiEventListener);
        if (doneButton instanceof Button button) this.doneButton = button;
        return doneButton;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addButtons(CallbackInfo ci) {
        int x = (int) (this.width * MAIN_CONFIG.buttonsAlignmentX + MAIN_CONFIG.buttonsX);
        int y = (int) (this.height * MAIN_CONFIG.buttonsAlignmentY + MAIN_CONFIG.buttonsY);

        FontSelectionWidget fontSelector = new FontSelectionWidget(
                this.minecraft,
                MAIN_CONFIG.showReloadButton ? 179 : 200,
                Math.min(200, this.height - y - 5),
                x - 100,
                y,
                20,
                () -> {
                    if (this.signField == null) return;
                    this.signField.setSelectionPos(this.signField.getCursorPos());

                    if (BigSignWriter.enabled()) {
                        int cursorPos = this.bigSignWriter$getCursorPos();
                        int width = this.font.width(this.messages[this.line].substring(0, cursorPos));
                        this.line = Math.min(this.line, this.bigSignWriter$getEffectiveBottomLine());

                        TreeMap<Integer, Integer[]> splitIndices = this.bigSignWriter$getSplitIndices(true);
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
                }
        );

        ClickableButtonWidget fontSelectorToggleButton = new ClickableButtonWidget(
                x - 99,
                y + 3,
                14,
                14,
                bigSignWriter$getDropdownLabel(fontSelector.isOpen()),
                button -> fontSelector.setOpen(!fontSelector.isOpen())
        );

        fontSelector.setOnOpenChanged(instance -> {
            fontSelectorToggleButton.setMessage(bigSignWriter$getDropdownLabel(instance.isOpen()));
            if (this.doneButton != null) this.doneButton.active =
                    !MAIN_CONFIG.fontSelectorCoversDoneButton || !instance.isOpen();
        });

        this.addWidget(fontSelectorToggleButton);
        this.addRenderableWidget(fontSelector);
        this.addRenderableOnly(fontSelectorToggleButton);

        if (MAIN_CONFIG.showReloadButton) {
            ClickableButtonWidget reloadButton = new ClickableButtonWidget(
                    x + 80,
                    y,
                    20,
                    20,
                    Component.literal("\uD83D\uDDD8"),
                    button -> {
                        reloadConfig();
                        BigSignWriter.reloadFonts();
                        fontSelector.updateEntries();
                    }
            );
            reloadButton.setTooltip(Tooltip.create(Component.translatable("bigsignwriter.reload")));
            this.addRenderableWidget(reloadButton);
        }
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    //? if < 1.21.9 {
    /*private void charTypedInject(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
    *///?} else
    private void charTypedInject(CharacterEvent characterEvent, CallbackInfoReturnable<Boolean> cir) {
        if (!BigSignWriter.enabled() || this.signField == null) return;
        cir.setReturnValue(true);

        //? if >= 1.21.9
        char chr = Character.toChars(characterEvent.codepoint())[0];
        String[] bigChar = BigSignWriter.getBigChar(chr).orElse(new String[]{});
        if (bigChar.length == 0) return;

        int startLine = this.line;
        int endLine = this.bigSignWriter$getEndLine();
        int endLength = this.messages[this.line].length();
        int cursorPos = this.bigSignWriter$getCursorPos();

        Integer[] split;
        if (cursorPos <= 0) {
            split = IntStream.of(new int[endLine - startLine]).boxed().toArray(Integer[]::new);
            cursorPos = 0;
        } else if (cursorPos == endLength) {
            split = Arrays.stream(
                    Arrays.copyOfRange(this.messages, startLine, endLine)
            ).map(String::length).toArray(Integer[]::new);
        } else {
            TreeMap<Integer, Integer[]> splitIndices = this.bigSignWriter$getSplitIndices(true);
            if (splitIndices.isEmpty()) {
                split = Arrays.stream(
                        Arrays.copyOfRange(this.messages, startLine, endLine)
                ).map(String::length).toArray(Integer[]::new);
                cursorPos = endLength;
            } else {
                int width = this.font.width(this.messages[this.line].substring(0, cursorPos));
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

            this.line = i;
            this.setMessage(message);
            if (charLine == 0) newCursorPos = cursorPos + addition.length();
        }

        this.line = startLine;
        if (this.signField != null) {
            if (newCursorPos >= 0)
                this.signField.setCursorPos(newCursorPos, false);
            else this.signField.setCursorPos(cursorPos, false);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    //? if < 1.21.9 {
    /*private void keyPressedInject(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
    *///?} else
    private void keyPressedInject(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
        if (!BigSignWriter.enabled() || this.signField == null) return;
        //? if < 1.21.9
        //KeyEvent keyEvent = new KeyEvent(keyCode, scanCode, modifiers);

        if (keyEvent.isUp()) {
            this.line--;
            if (this.line < 0)
                this.line = this.bigSignWriter$getEffectiveBottomLine();
            this.signField.setCursorToEnd();
            cir.setReturnValue(true);
            return;
        } else if (keyEvent.isDown() || keyEvent.isConfirmation()) {
            if (keyEvent.isDown()) this.line++;
            else {
                if (this.line == 0 && bigSignWriter$getHeight() * 2 > this.messages.length)
                    this.line = this.bigSignWriter$getEffectiveBottomLine();
                else this.line += bigSignWriter$getHeight();
            }
            if (this.line > this.bigSignWriter$getEffectiveBottomLine())
                this.line = 0;
            this.signField.setCursorToEnd();
            cir.setReturnValue(true);
            return;
        } else if (keyEvent.isLeft() || keyEvent.isRight()) {
            if (keyEvent.hasControlDown()) {
                if (keyEvent.isLeft()) this.signField.setCursorToStart();
                else this.signField.setCursorToEnd();
            } else this.bigSignWriter$moveCursor(keyEvent.isRight());
            cir.setReturnValue(true);
            return;
        }

        if (keyEvent.key() != 259) return;
        cir.setReturnValue(true);
        this.bigSignWriter$deleteBigChar(keyEvent);
    }

    //? if >= 26.1
    @SuppressWarnings("LocalMayUseName")
    @Inject(
            method = /*? if >= 26.1 {*/ "extractSignText" /*?} else {*/ /*"renderSignText" *//*?}*/,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screens/inventory/AbstractSignEditScreen;messages:[Ljava/lang/String;",
                    ordinal = 2,
                    opcode = Opcodes.GETFIELD
            ),
            cancellable = true
    )
    private void drawBigCursor(
            GuiGraphicsExtractor guiGraphics,
            //? if >= 26.1
            Vector2f cursorPosOutput,
            CallbackInfo ci,
            @Local(ordinal = 0) int color,
            @Local(ordinal = 0) boolean showCursor,
            @Local(ordinal = 1) int cursorPos
    ) {
        if (!BigSignWriter.enabled() || this.signField == null) return;

        if (!showCursor) {
            ci.cancel();
            return;
        }

        String topLine = this.messages[this.line] == null ? "" : this.messages[this.line];
        int lineHeight = this.sign.getTextLineHeight();
        int opaqueColor = 0xFF000000 | color;

        if (cursorPos <= 0 || topLine.length() == cursorPos) {
            for (int i = this.line; i < this.bigSignWriter$getEndLine(); i++) {
                String message = this.messages[i] == null ? "" : this.messages[i];
                int cursorX = this.font.width(message) / 2;
                if (cursorPos <= 0) cursorX *= -1;
                int cursorY = (i - 2) * lineHeight;

                guiGraphics.fill(cursorX, cursorY - 1, cursorX + 1, cursorY + lineHeight, opaqueColor);
            }
        } else {
            int cursorPosition = this.font.width(topLine.substring(0, Math.min(cursorPos, topLine.length())));
            int cursorX = cursorPosition - this.font.width(topLine) / 2;
            int cursorY = (this.line - 2) * lineHeight;

            guiGraphics.fill(cursorX, cursorY - 1, cursorX + 1, cursorY + lineHeight * bigSignWriter$getHeight(), opaqueColor);
        }

        ci.cancel();
    }

    @WrapWithCondition(
            method = /*? if >= 26.1 {*/ "extractSignText" /*?} else {*/ /*"renderSignText" *//*?}*/,
            at = @At(
                    value = "INVOKE",
                    //? if < 21.6
                    //ordinal = 1,
                    //? if >= 21.6 {
                    target = "Lnet/minecraft/client/gui/components/TextCursorUtils;extractAppendCursor(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;IIIZ)V"
                    //?} elif >= 1.21.6 {
                    /*target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V"
                    *///?} else
                    //target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"
            )
    )
    private boolean hideUnderscore(
            GuiGraphicsExtractor guiGraphics,
            Font font,
            //? if < 21.6
            //String string,
            int cursorX,
            int cursorY,
            int color,
            boolean shadow
    ) {
        return !BigSignWriter.enabled();
    }
}
