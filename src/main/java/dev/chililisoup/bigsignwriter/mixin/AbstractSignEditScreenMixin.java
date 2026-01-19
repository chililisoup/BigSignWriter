package dev.chililisoup.bigsignwriter.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.gui.ClickableButtonWidget;
import dev.chililisoup.bigsignwriter.gui.FontSelectionWidget;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.network.chat.Component;
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
import net.minecraft.client.input.KeyEvent;
//?}

import java.util.*;

import static dev.chililisoup.bigsignwriter.BigSignWriterConfig.*;

@Mixin(value = AbstractSignEditScreen.class, priority = 999)
public abstract class AbstractSignEditScreenMixin extends Screen {
    protected AbstractSignEditScreenMixin(Component title) { super(title); }

    @Unique
    private static int bigSignWriter$getHeight() {
        return (BigSignWriter.SELECTED_FONT == null || BigSignWriter.SELECTED_FONT.height <= 0) ?
                4 : BigSignWriter.SELECTED_FONT.height;
    }

    @Unique
    private static Optional<String[]> bigSignWriter$getBigChar(char chr) {
        if (BigSignWriter.SELECTED_FONT == null)
            return Optional.empty();

        if (BigSignWriter.SELECTED_FONT.characters == null)
            return Optional.empty();

        if (BigSignWriter.SELECTED_FONT.characters.containsKey(chr))
            return Optional.of(BigSignWriter.SELECTED_FONT.characters.get(chr));

        char upper = Character.toUpperCase(chr);
        if (BigSignWriter.SELECTED_FONT.characters.containsKey(upper))
            return Optional.of(BigSignWriter.SELECTED_FONT.characters.get(upper));

        if (BigSignWriter.DEFAULT_FONT == null || BigSignWriter.SELECTED_FONT.name.equals("Default") || (BigSignWriter.SELECTED_FONT.height > 0 && BigSignWriter.SELECTED_FONT.height != 4))
            return Optional.empty();

        if (BigSignWriter.DEFAULT_FONT.characters.containsKey(chr))
            return Optional.of(BigSignWriter.DEFAULT_FONT.characters.get(chr));

        if (BigSignWriter.DEFAULT_FONT.characters.containsKey(upper))
            return Optional.of(BigSignWriter.DEFAULT_FONT.characters.get(upper));

        return Optional.empty();
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

    @Unique private @Nullable Button doneButton;

    @Shadow /*? if >= 1.21.2 {*/ protected /*?} else {*/ /*private *//*?}*/ @Final SignBlockEntity sign;
    @Shadow private @Final String[] messages;
    @Shadow private void setMessage(String string) {}
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
                Math.min(150, this.height - y - 5),
                x - 100,
                y,
                15,
                () -> {
                    if (BigSignWriter.enabled())
                        this.line = Math.min(this.line, this.bigSignWriter$getEffectiveBottomLine());
                    else if (this.signField != null)
                        this.signField.setCursorToEnd();
                }
        );

        ClickableButtonWidget fontSelectorToggleButton = new ClickableButtonWidget(
                x - 99,
                y + 2,
                14,
                15,
                Component.literal("▶"),
                button -> {
                    fontSelector.open = !fontSelector.open;
                    button.setMessage(Component.literal(fontSelector.open ? "▼" : "▶"));
                    if (this.doneButton != null) this.doneButton.active = !fontSelector.open;
                }
        );

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
            reloadButton.setTooltip(Tooltip.create(Component.translatableWithFallback("bigsignwriter.reload", "Reload Fonts")));
            this.addRenderableWidget(reloadButton);
        }
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    //? if < 1.21.9 {
    /*private void charTypedInject(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
    *///?} else
    private void charTypedInject(CharacterEvent characterEvent, CallbackInfoReturnable<Boolean> cir) {
        if (!BigSignWriter.enabled()) return;

        cir.setReturnValue(true);

        //? if >= 1.21.9
        char chr = Character.toChars(characterEvent.codepoint())[0];
        String[] bigChar = bigSignWriter$getBigChar(chr).orElse(new String[]{});
        if (bigChar.length == 0) return;

        int startLine = this.line;
        int endLine = this.bigSignWriter$getEndLine();
        for (int i = startLine; i < endLine; i++) {
            int charLine = i - startLine;
            if (charLine >= bigChar.length || bigChar[charLine] == null) continue;
            String string = this.messages[i].concat(
                    (this.messages[i].isEmpty() ? "" : BigSignWriter.CHARACTER_SEPARATOR).concat(bigChar[charLine])
            );

            if (this.font.width(string) > this.sign.getMaxTextLineWidth())
                continue;

            this.line = i;
            this.setMessage(string);
        }
        this.line = startLine;

        if (this.signField != null)
            this.signField.setCursorToEnd();
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    //? if < 1.21.9 {
    /*private void deleteOrClamp(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
    *///?} else
    private void deleteOrClamp(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
        if (!BigSignWriter.enabled()) return;
        //? if >= 1.21.9
        int keyCode = keyEvent.key();

        if (keyCode == 265) {
            this.line--;
            if (this.line < 0)
                this.line = this.bigSignWriter$getEffectiveBottomLine();
            if (this.signField != null)
                this.signField.setCursorToEnd();
            cir.setReturnValue(true);
            return;
        } else if (keyCode == 264 || keyCode == 257 || keyCode == 335) {
            this.line += keyCode == 264 ?
                    1 : // Arrow key down
                    bigSignWriter$getHeight(); // Enter
            if (this.line > this.bigSignWriter$getEffectiveBottomLine())
                this.line = 0;
            if (this.signField != null)
                this.signField.setCursorToEnd();
            cir.setReturnValue(true);
            return;
        }

        if (keyCode != 259) return;
        cir.setReturnValue(true);

        String characterSeparator = BigSignWriter.CHARACTER_SEPARATOR.isEmpty() ?
                bigSignWriter$getBigChar(' ').orElse(new String[]{""})[0] :
                BigSignWriter.CHARACTER_SEPARATOR;

        if (
                //? if < 1.21.9 {
                /*Screen.hasControlDown() ||
                *///?} else
                keyEvent.hasControlDown() ||
                characterSeparator.isEmpty()
        ) {
            this.bigSignWriter$clearSign();
            return;
        }

        int startLine = this.line;
        int endLine = this.bigSignWriter$getEndLine();
        int firstWidth = this.font.width(this.messages[startLine]);
        for (int i = startLine + 1; i < endLine; i++) {
            if (firstWidth != this.font.width(this.messages[i])) {
                this.bigSignWriter$clearSign();
                return;
            }
        }

        List<HashMap<Integer, Integer>> separatorIndices = Arrays.stream(
                Arrays.copyOfRange(this.messages, startLine, endLine)
        ).map(message -> {
            HashMap<Integer, Integer> indices = new HashMap<>();

            for (int index = message.indexOf(characterSeparator);
                 index >= 0;
                 index = message.indexOf(characterSeparator, index + 1)
            ) {
                indices.put(this.font.width(message.substring(index)), index);
            }

            return indices;
        }).toList();

        List<Integer> matchingSeparatorLengths = separatorIndices.getFirst()
                .keySet().stream().filter(length -> separatorIndices.stream().allMatch(
                        map -> map.containsKey(length)
                )
        ).toList();

        if (matchingSeparatorLengths.isEmpty()) {
            this.bigSignWriter$clearSign();
            return;
        }

        int index = Collections.min(matchingSeparatorLengths);

        for (int i = startLine; i < endLine; i++) {
            String message = this.messages[i];

            this.line = i;
            this.setMessage(message.substring(
                    0,
                    separatorIndices.get(i - startLine).get(index)
            ));
        }
        this.line = startLine;

        if (this.signField != null)
            this.signField.setCursorToEnd();
    }

    @Inject(
            method = "renderSignText",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screens/inventory/AbstractSignEditScreen;messages:[Ljava/lang/String;",
                    ordinal = 2,
                    opcode = Opcodes.GETFIELD
            ),
            cancellable = true
    )
    private void drawExtraLines(
            GuiGraphics guiGraphics,
            CallbackInfo ci,
            @Local boolean blink,
            @Local(ordinal = 0) int color
    ) {
        if (!BigSignWriter.enabled()) return;

        int lineHeight = this.sign.getTextLineHeight();
        int opaqueColor = -16777216 | color;

        for (int i = this.line; i < this.bigSignWriter$getEndLine(); i++) {
            if (!blink) continue;

            String string = this.messages[i] == null ? "" : this.messages[i];
            int lineX = this.font.width(string) / 2;
            int lineY = (i - 2) * lineHeight;

            guiGraphics.fill(lineX, lineY - 1, lineX + 1, lineY + lineHeight, opaqueColor);
        }

        ci.cancel();
    }

    @WrapWithCondition(
            method = "renderSignText",
            at = @At(
                    value = "INVOKE",
                    ordinal = 1,
                    //? if >= 1.21.6 {
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V"
                    //?} else
                    //target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"
            )
    )
    private boolean hideUnderscore(GuiGraphics instance, Font font, String string, int i, int j, int k, boolean bl) {
        return !BigSignWriter.enabled();
    }
}
