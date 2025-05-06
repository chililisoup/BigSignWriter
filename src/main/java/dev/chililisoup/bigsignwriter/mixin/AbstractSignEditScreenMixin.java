package dev.chililisoup.bigsignwriter.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.chililisoup.bigsignwriter.ClickableButtonWidget;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.chililisoup.bigsignwriter.BigSignWriterConfig.SELECTED_FONT;
import static dev.chililisoup.bigsignwriter.BigSignWriterConfig.MAIN_CONFIG;

@Mixin(AbstractSignEditScreen.class)
public abstract class AbstractSignEditScreenMixin {
    @Unique
    private static Text createToggleButtonText() {
        return ScreenTexts.composeToggleText(Text.translatableWithFallback("bigsignwriter.enabled", "Big Text"), BigSignWriter.ENABLED);
    }
    @Unique
    private static Text createFontButtonText() {
        String fontName = (SELECTED_FONT.name == null) ? "Unknown" : SELECTED_FONT.name;
        return ScreenTexts.composeGenericOptionText(
                Text.translatableWithFallback("bigsignwriter.font", "Font"),
                Text.literal(fontName)
        );
    }


    @Shadow protected @Final SignBlockEntity blockEntity;
    @Shadow private @Final String[] messages;
    @Shadow private int currentRow;
    @Shadow private void setCurrentRowMessage(String message) {}

    @Shadow @Nullable private SelectionManager selectionManager;

    @Inject(method = "init", at = @At("HEAD"))
    private void addButtons(CallbackInfo ci) {
        AbstractSignEditScreen editScreen = (AbstractSignEditScreen) (Object) this;

        ClickableButtonWidget toggleButton = new ClickableButtonWidget(
                editScreen.width / 2 + MAIN_CONFIG.toggleButtonX - 100,
                editScreen.height / 4 + MAIN_CONFIG.toggleButtonY,
                75,
                20,
                createToggleButtonText(),
                button -> {
                    BigSignWriter.toggleEnabled();
                    button.setMessage(createToggleButtonText());
                }
        );

        ClickableButtonWidget fontButton = new ClickableButtonWidget(
                editScreen.width / 2 + MAIN_CONFIG.toggleButtonX - 25,
                editScreen.height / 4 + MAIN_CONFIG.toggleButtonY,
                80,
                20,
                createFontButtonText(),
                button -> {
                    BigSignWriterConfig.getNextFont();
                    BigSignWriterConfig.reloadFonts();
                    button.setMessage(createFontButtonText());
                }
        );

        ClickableButtonWidget reloadButton = new ClickableButtonWidget(
                editScreen.width / 2 + MAIN_CONFIG.toggleButtonX + 55,
                editScreen.height / 4 + MAIN_CONFIG.toggleButtonY,
                45,
                20,
                Text.translatableWithFallback("bigsignwriter.reload", "Reload"),
                button -> {
                    BigSignWriterConfig.reloadConfig();
                    BigSignWriterConfig.reloadFonts();
                    fontButton.setMessage(createFontButtonText());
                }
        );

        editScreen.addDrawableChild(toggleButton);
        editScreen.addDrawableChild(fontButton);
        editScreen.addDrawableChild(reloadButton);
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void charTypedInject(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!BigSignWriter.ENABLED) return;
        if (SELECTED_FONT.characters == null) {
            cir.setReturnValue(true);
            return;
        }

        if (!SELECTED_FONT.characters.containsKey(chr))
            chr = Character.toUpperCase(chr);

        if (SELECTED_FONT.characters.containsKey(chr)) {
            String[] bigChar = SELECTED_FONT.characters.get(chr);
            AbstractSignEditScreen editScreen = (AbstractSignEditScreen) (Object) this;

            for (int i = 0; i < this.messages.length; i++) {
                if (SELECTED_FONT.characterSeparator == null){
                    SELECTED_FONT.characterSeparator = MAIN_CONFIG.characterSeparator;
                }
                if (i >= bigChar.length || bigChar[i] == null) continue;
                String line = this.messages[i].concat(
                        (this.messages[i].isEmpty() ? "" : SELECTED_FONT.characterSeparator).concat(bigChar[i])
                );

                if (editScreen.getTextRenderer().getWidth(line) > this.blockEntity.getMaxTextWidth())
                    continue;

                this.currentRow = i;
                this.setCurrentRowMessage(line);
            }

            if (this.selectionManager != null)
                this.selectionManager.putCursorAtEnd();
        }

        cir.setReturnValue(true);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void clearSign(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!BigSignWriter.ENABLED) return;
        if (keyCode != 259) return;

        for (int i = 0; i < this.messages.length; i++) {
            this.currentRow = i;
            this.setCurrentRowMessage("");
        }

        cir.setReturnValue(true);
    }

    @Inject(
            method = "renderSignText",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/ingame/AbstractSignEditScreen;messages:[Ljava/lang/String;", ordinal = 2),
            cancellable = true
    )
    private void drawExtraLines(
            DrawContext context,
            CallbackInfo ci,
            @Local boolean blink,
            @Local(ordinal = 0) int color
    ) {
        if (!BigSignWriter.ENABLED) return;

        AbstractSignEditScreen editScreen = (AbstractSignEditScreen) (Object) this;
        int opaqueColor = ColorHelper.fullAlpha(color);
        int lineHeight = this.blockEntity.getTextLineHeight();

        for (int i = 0; i < messages.length; i++) {
            if (!blink) continue;

            String string = messages[i] == null ? "" : messages[i];
            int lineX = editScreen.getTextRenderer().getWidth(string) / 2;
            int lineY = (i - 2) * lineHeight;

            context.fill(lineX, lineY - 1, lineX + 1, lineY + lineHeight, opaqueColor);
        }

        ci.cancel();
    }

    @WrapWithCondition(
            method = "renderSignText",
            at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I")
    )
    private boolean hideUnderscore(DrawContext instance, TextRenderer textRenderer, String text, int x, int y, int color, boolean shadow) {
        return !BigSignWriter.ENABLED;
    }
}
