package dev.chililisoup.bigsignwriter.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.chililisoup.bigsignwriter.BigFontTyper;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.gui.ClickableButtonWidget;
import dev.chililisoup.bigsignwriter.gui.FontSelectionWidget;
import dev.chililisoup.bigsignwriter.gui.SymbolPickerWidget;
import dev.chililisoup.bigsignwriter.gui.config.BigSignWriterConfigScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
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

//? if >= 26.1 {
import org.joml.Vector2f;
//?}

import static dev.chililisoup.bigsignwriter.BigSignWriterConfig.*;

@Mixin(value = AbstractSignEditScreen.class, priority = 999)
public abstract class AbstractSignEditScreenMixin extends Screen {
    protected AbstractSignEditScreenMixin(Component title) { super(title); }

    @Unique
    private static Component bigSignWriter$getDropdownLabel(boolean open) {
        return Component.literal(open ? "▼" : "▶");
    }

    @Unique private @Nullable Button bigSignWriter$doneButton;
    @Unique private @Nullable BigFontTyper bigSignWriter$fontTyper;
    @Unique private boolean bigSignWriter$inSymbolPicker = false;
    @Unique private boolean bigSignWriter$ignoreNextRemoval = false;

    @Shadow protected @Final SignBlockEntity sign;
    @Shadow private @Final String[] messages;
    @Shadow private void setMessage(String message) {}
    @Shadow private int line;
    @Shadow private @Nullable TextFieldHelper signField;

    @Unique private int bigSignWriter$cursorHeight() {
        return this.bigSignWriter$inSymbolPicker ? 1 : BigSignWriter.height();
    }

    @WrapOperation(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/AbstractSignEditScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"
            )
    )
    private GuiEventListener grabDoneButton(AbstractSignEditScreen instance, GuiEventListener guiEventListener, Operation<GuiEventListener> original) {
        GuiEventListener doneButton = original.call(instance, guiEventListener);
        if (doneButton instanceof Button button) this.bigSignWriter$doneButton = button;
        return doneButton;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void bigSignWriterInit(CallbackInfo ci) {
        if (this.signField == null) return;

        this.bigSignWriter$fontTyper = new BigFontTyper(
                this.sign,
                this.font,
                this::bigSignWriter$cursorHeight,
                () -> this.line,
                i -> this.line = i,
                this.messages,
                this::setMessage,
                this.signField
        );

        int x = (int) (this.width * MAIN_CONFIG.buttonsAlignmentX + MAIN_CONFIG.buttonsX);
        int y = (int) (this.height * MAIN_CONFIG.buttonsAlignmentY + MAIN_CONFIG.buttonsY);

        FontSelectionWidget fontSelector = new FontSelectionWidget(
                this.minecraft,
                200,
                Math.min(200, this.height - y - 5),
                x - 100,
                y,
                20,
                this.messages.length,
                this.bigSignWriter$fontTyper::onFontSelected
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
            if (this.bigSignWriter$doneButton != null) this.bigSignWriter$doneButton.visible =
                    !MAIN_CONFIG.fontSelectorCoversDoneButton || !instance.isOpen();
        });

        this.addWidget(fontSelectorToggleButton);
        this.addRenderableWidget(fontSelector);
        this.addRenderableOnly(fontSelectorToggleButton);

        if (MAIN_CONFIG.showConfigButton) {
            ClickableButtonWidget configButton = new ClickableButtonWidget(
                    x - 118,
                    y + 3,
                    14,
                    14,
                    Component.literal("☰"),
                    button -> {
                        this.bigSignWriter$ignoreNextRemoval = true;
                        this.minecraft.gui.setScreen(new BigSignWriterConfigScreen(this));
                        this.bigSignWriter$ignoreNextRemoval = false;
                    }
            );
            configButton.setTooltip(Tooltip.create(Component.translatable("bigsignwriter.config")));
            this.addRenderableWidget(configButton);
        }

        if (MAIN_CONFIG.showSymbolsButton) {
            SymbolPickerWidget symbolPicker = new SymbolPickerWidget(
                    this.minecraft,
                    x - 100,
                    y,
                    200,
                    Math.min(200, this.height - y - 5),
                    this.bigSignWriter$fontTyper::typeLines
            );
            symbolPicker.visible = false;

            symbolPicker.setOnVisibilityToggle(visible -> {
                this.bigSignWriter$inSymbolPicker = visible;
                if (!visible) this.bigSignWriter$fontTyper.clampLine();

                if (fontSelector.isOpen()) fontSelector.setOpen(false);
                fontSelector.visible = !visible;
                fontSelectorToggleButton.visible = !visible;
                if (this.bigSignWriter$doneButton != null) this.bigSignWriter$doneButton.visible =
                        !MAIN_CONFIG.fontSelectorCoversDoneButton || !visible;
            });

            ClickableButtonWidget symbolsButton = new ClickableButtonWidget(
                    x + 104,
                    y + 3,
                    14,
                    14,
                    Component.literal("❤"),
                    button -> symbolPicker.toggleVisibility()
            );
            symbolsButton.setTooltip(Tooltip.create(Component.translatable("bigsignwriter.symbols")));

            this.addRenderableWidget(symbolsButton);
            this.addRenderableWidget(symbolPicker);
        }
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void charTypedInject(CharacterEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (BigSignWriter.isVanillaTyping() || this.bigSignWriter$fontTyper == null) return;
        cir.setReturnValue(true);

        char chr = Character.toChars(event.codepoint())[0];
        this.bigSignWriter$fontTyper.charTyped(chr);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void keyPressedInject(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (BigSignWriter.isVanillaTyping() || this.bigSignWriter$fontTyper == null) return;

        if (this.bigSignWriter$fontTyper.keyPressed(event))
            cir.setReturnValue(true);
    }

    //? if >= 26.1
    @SuppressWarnings("LocalMayUseName")
    @Inject(
            method = "extractSignText",
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
        if (BigSignWriter.isVanillaTyping() || this.bigSignWriter$fontTyper == null) return;

        if (!showCursor) {
            ci.cancel();
            return;
        }

        String topLine = this.messages[this.line] == null ? "" : this.messages[this.line];
        int lineHeight = this.sign.getTextLineHeight();
        int cursorHeight = this.bigSignWriter$cursorHeight();
        int fullHeight = Math.min(BigSignWriter.height(), this.messages.length - this.line);
        int opaqueColor = 0xFF000000 | color;

        int cursorPosition = this.font.width(topLine.substring(0, Math.min(cursorPos, topLine.length())));
        int cursorX = cursorPosition - this.font.width(topLine) / 2;
        int cursorY = (this.line - 2) * lineHeight;

        int endY = cursorY + lineHeight * cursorHeight;
        guiGraphics.fill(cursorX, cursorY - 1, cursorX + 1, endY, opaqueColor);

        if (fullHeight > cursorHeight)
            for (int y = endY + 2; y < cursorY + lineHeight * fullHeight; y += 4)
                guiGraphics.fill(cursorX, y, cursorX + 1, y + 2, opaqueColor);

        ci.cancel();
    }

    @WrapWithCondition(
            method = "extractSignText",
            at = @At(
                    value = "INVOKE",
                    //? if < 21.6
                    //ordinal = 1,
                    //? if >= 21.6 {
                    target = "Lnet/minecraft/client/gui/components/TextCursorUtils;extractAppendCursor(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;IIIZ)V"
                    //?} else
                    //target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V"
            )
    )
    private boolean hideUnderscore(
            GuiGraphicsExtractor guiGraphics,
            Font font,
            //? if < 21.6
            //String string,
            int x,
            int y,
            int color,
            boolean shadow
    ) {
        return BigSignWriter.isVanillaTyping();
    }

    @WrapWithCondition(
            method = "removed", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"
    ))
    private boolean skipPacket(ClientPacketListener instance, Packet<?> packet) {
        return !this.bigSignWriter$ignoreNextRemoval;
    }
}
