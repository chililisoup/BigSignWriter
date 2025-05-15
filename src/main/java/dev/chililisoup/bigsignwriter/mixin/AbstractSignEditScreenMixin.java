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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

import static dev.chililisoup.bigsignwriter.BigSignWriterConfig.*;

@Mixin(AbstractSignEditScreen.class)
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

        if (DEFAULT_FONT == null || SELECTED_FONT.name.equals("Default"))
            return Optional.empty();

        if (DEFAULT_FONT.characters.containsKey(chr))
            return Optional.of(DEFAULT_FONT.characters.get(chr));

        if (DEFAULT_FONT.characters.containsKey(upper))
            return Optional.of(DEFAULT_FONT.characters.get(upper));

        return Optional.empty();
    }

    @Shadow /*? if >=1.21.2 {*/ protected /*?} else {*/ /*private *//*?}*/ @Final SignBlockEntity sign;
    @Shadow private @Final String[] messages;
    @Shadow private void setMessage(String string) {}
    @Shadow private int line;
    @Shadow @Nullable private TextFieldHelper signField;

    @Inject(method = "init", at = @At("HEAD"))
    private void addButtons(CallbackInfo ci) {
        ClickableButtonWidget toggleButton = new ClickableButtonWidget(
                this.width / 2 + MAIN_CONFIG.buttonsX - 100,
                this.height / 4 + MAIN_CONFIG.buttonsY,
                75,
                20,
                bigSignWriter$createToggleButtonText(),
                button -> {
                    BigSignWriter.toggleEnabled();
                    button.setMessage(bigSignWriter$createToggleButtonText());
                }
        );

        ClickableButtonWidget fontButton = new ClickableButtonWidget(
                this.width / 2 + MAIN_CONFIG.buttonsX - 25,
                this.height / 4 + MAIN_CONFIG.buttonsY,
                80,
                20,
                bigSignWriter$createFontButtonText(),
                button -> {
                    getNextFont();
                    button.setMessage(bigSignWriter$createFontButtonText());
                }
        );

        ClickableButtonWidget reloadButton = new ClickableButtonWidget(
                this.width / 2 + MAIN_CONFIG.buttonsX + 55,
                this.height / 4 + MAIN_CONFIG.buttonsY,
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
    private void charTypedInject(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!BigSignWriter.ENABLED) return;

        cir.setReturnValue(true);

        String[] bigChar = bigSignWriter$getBigChar(chr).orElse(new String[]{});
        if (bigChar.length == 0) return;

        String characterSeparator = SELECTED_FONT.characterSeparator == null ?
                MAIN_CONFIG.defaultCharacterSeparator :
                SELECTED_FONT.characterSeparator;

        for (int i = 0; i < this.messages.length; i++) {
            if (i >= bigChar.length || bigChar[i] == null) continue;
            String string = this.messages[i].concat(
                    (this.messages[i].isEmpty() ? "" : characterSeparator).concat(bigChar[i])
            );

            if (this.font.width(string) > this.sign.getMaxTextLineWidth())
                continue;

            this.line = i;
            this.setMessage(string);
        }

        if (this.signField != null)
            this.signField.setCursorToEnd();
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void deleteChar(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!BigSignWriter.ENABLED) return;
        if (keyCode != 259) return;

        cir.setReturnValue(true);

        String characterSeparator = SELECTED_FONT.characterSeparator == null ?
                MAIN_CONFIG.defaultCharacterSeparator :
                SELECTED_FONT.characterSeparator;

        boolean allMatch = true;
        if (Screen.hasControlDown() || characterSeparator.isEmpty()) allMatch = false;
        else {
            int firstWidth = 0;
            for (int i = 0; i < this.messages.length; i++) {
                int width = this.font.width(this.messages[i]);
                if (i == 0) firstWidth = width;
                else if (firstWidth != width) {
                    allMatch = false;
                    break;
                }
            }
        }

        if (!allMatch) {
            for (int i = 0; i < this.messages.length; i++) {
                this.line = i;
                this.setMessage("");
            }

            if (this.signField != null)
                this.signField.setCursorToEnd();
            return;
        }

        HashMap<Integer, HashMap<Integer, Integer>> separatorIndices = new HashMap<>();
        for (int i = 0; i < this.messages.length; i++) {
            String message = this.messages[i];
            HashMap<Integer, Integer> indices = new HashMap<>();

            for (
                    int index = message.indexOf(characterSeparator);
                    index >= 0;
                    index = message.indexOf(characterSeparator, index + 1)
            ) {
                indices.put(this.font.width(message.substring(index)), index);
            }

            separatorIndices.put(i, indices);
        }

        ArrayList<Integer> matchingSeparatorLengths = new ArrayList<>();
        for (int length : separatorIndices.get(0).keySet()) {
            if (separatorIndices.values().stream().allMatch(
                    map -> map.containsKey(length)
            )) matchingSeparatorLengths.add(length);
        }

        if (matchingSeparatorLengths.isEmpty()) {
            for (int i = 0; i < this.messages.length; i++) {
                this.line = i;
                this.setMessage("");
            }

            if (this.signField != null)
                this.signField.setCursorToEnd();
            return;
        }

        int index = Collections.min(matchingSeparatorLengths);
        for (int i = 0; i < this.messages.length; i++) {
            String message = this.messages[i];

            this.line = i;
            this.setMessage(message.substring(
                    0,
                    separatorIndices.get(i).get(index)
            ));
        }

        if (this.signField != null)
            this.signField.setCursorToEnd();
    }

    @Inject(
            method = "renderSignText",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractSignEditScreen;messages:[Ljava/lang/String;", ordinal = 2),
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

        for (int i = 0; i < this.messages.length; i++) {
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
            at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I")
    )
    private boolean hideUnderscore(GuiGraphics instance, Font font, String string, int i, int j, int k, boolean bl) {
        return !BigSignWriter.ENABLED;
    }
}
