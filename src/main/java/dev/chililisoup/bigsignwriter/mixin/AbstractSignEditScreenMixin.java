package dev.chililisoup.bigsignwriter.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.ClickableButtonWidget;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.network.chat.CommonComponents;
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
    private static Component bigSignWriter$createToggleButtonText() {
        return CommonComponents.optionStatus(Component.translatableWithFallback("bigsignwriter.enabled", "Big Text"), BigSignWriter.ENABLED);
    }

    @Unique
    private static Component bigSignWriter$createFontButtonText() {
        String fontName = (SELECTED_FONT == null || SELECTED_FONT.name == null) ? "Unknown" : SELECTED_FONT.name;
        return CommonComponents.optionNameValue(
                Component.translatableWithFallback("bigsignwriter.font", "Font"),
                Component.literal(fontName)
        );
    }

    @Unique
    private static int bigSignWriter$getHeight() {
        return (SELECTED_FONT == null || SELECTED_FONT.height <= 0) ?
                4 : SELECTED_FONT.height;
    }

    @Unique
    private static Optional<String[]> bigSignWriter$getBigChar(char chr) {
        if (SELECTED_FONT == null)
            return Optional.empty();

        if (SELECTED_FONT.characters == null)
            return Optional.empty();

        if (SELECTED_FONT.characters.containsKey(chr))
            return Optional.of(SELECTED_FONT.characters.get(chr));

        char upper = Character.toUpperCase(chr);
        if (SELECTED_FONT.characters.containsKey(upper))
            return Optional.of(SELECTED_FONT.characters.get(upper));

        if (DEFAULT_FONT == null || SELECTED_FONT.name.equals("Default") || (SELECTED_FONT.height > 0 && SELECTED_FONT.height != 4))
            return Optional.empty();

        if (DEFAULT_FONT.characters.containsKey(chr))
            return Optional.of(DEFAULT_FONT.characters.get(chr));

        if (DEFAULT_FONT.characters.containsKey(upper))
            return Optional.of(DEFAULT_FONT.characters.get(upper));

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

    @Shadow /*? if >= 1.21.2 {*/ protected /*?} else {*/ /*private *//*?}*/ @Final SignBlockEntity sign;
    @Shadow private @Final String[] messages;
    @Shadow private void setMessage(String string) {}
    @Shadow private int line;
    @Shadow @Nullable private TextFieldHelper signField;

    @Inject(method = "init", at = @At("HEAD"))
    private void addButtons(CallbackInfo ci) {
        ClickableButtonWidget toggleButton = new ClickableButtonWidget(
                (int) (this.width * MAIN_CONFIG.buttonsAlignmentX + MAIN_CONFIG.buttonsX - 100),
                (int) (this.height * MAIN_CONFIG.buttonsAlignmentY + MAIN_CONFIG.buttonsY),
                75,
                20,
                bigSignWriter$createToggleButtonText(),
                button -> {
                    BigSignWriter.toggleEnabled();
                    if (BigSignWriter.ENABLED)
                        this.line = Math.min(this.line, this.bigSignWriter$getEffectiveBottomLine());
                    else if (this.signField != null)
                        this.signField.setCursorToEnd();
                    button.setMessage(bigSignWriter$createToggleButtonText());
                }
        );

        ClickableButtonWidget fontButton = new ClickableButtonWidget(
                (int) (this.width * MAIN_CONFIG.buttonsAlignmentX + MAIN_CONFIG.buttonsX - 25),
                (int) (this.height * MAIN_CONFIG.buttonsAlignmentY + MAIN_CONFIG.buttonsY),
                80,
                20,
                bigSignWriter$createFontButtonText(),
                button -> {
                    getNextFont();
                    if (BigSignWriter.ENABLED)
                        this.line = Math.min(this.line, this.bigSignWriter$getEffectiveBottomLine());
                    button.setMessage(bigSignWriter$createFontButtonText());
                }
        );

        ClickableButtonWidget reloadButton = new ClickableButtonWidget(
                (int) (this.width * MAIN_CONFIG.buttonsAlignmentX + MAIN_CONFIG.buttonsX + 55),
                (int) (this.height * MAIN_CONFIG.buttonsAlignmentY + MAIN_CONFIG.buttonsY),
                45,
                20,
                Component.translatableWithFallback("bigsignwriter.reload", "Reload"),
                button -> {
                    reloadConfig();
                    reloadFonts();
                    fontButton.setMessage(bigSignWriter$createFontButtonText());
                }
        );

        this.addRenderableWidget(toggleButton);
        this.addRenderableWidget(fontButton);
        this.addRenderableWidget(reloadButton);
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    //? if < 1.21.9 {
    /*private void charTypedInject(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
    *///?} else
    private void charTypedInject(CharacterEvent characterEvent, CallbackInfoReturnable<Boolean> cir) {
        if (!BigSignWriter.ENABLED) return;

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
                    (this.messages[i].isEmpty() ? "" : CHARACTER_SEPARATOR).concat(bigChar[charLine])
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
        if (!BigSignWriter.ENABLED) return;
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

        if (
                //? if < 1.21.9 {
                /*Screen.hasControlDown() ||
                *///?} else
                keyEvent.hasControlDown() ||
                CHARACTER_SEPARATOR.isEmpty()
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

            for (int index = message.indexOf(CHARACTER_SEPARATOR);
                 index >= 0;
                 index = message.indexOf(CHARACTER_SEPARATOR, index + 1)
            ) {
                indices.put(this.font.width(message.substring(index)), index);
            }

            return indices;
        }).toList();

        List<Integer> matchingSeparatorLengths = separatorIndices./*? if >=1.20.5 {*/ getFirst() /*?} else {*/ /*get(0) *//*?}*/
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
        if (!BigSignWriter.ENABLED) return;

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
        return !BigSignWriter.ENABLED;
    }
}
