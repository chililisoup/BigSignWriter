package dev.chililisoup.bigsignwriter.gui.sign;

import com.mojang.datafixers.util.Either;
import dev.chililisoup.bigsignwriter.gui.ClickableText;
import dev.chililisoup.bigsignwriter.gui.ClickableWidgetPart;
import dev.chililisoup.bigsignwriter.gui.SimpleContainerWidget;
import dev.chililisoup.bigsignwriter.resources.UserSymbolsManager;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import dev.chililisoup.bigsignwriter.util.ModUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SymbolSaveWidget extends SimpleContainerWidget {
    private final Minecraft minecraft;
    private final Runnable onClose;
    private final Runnable onReload;
    private @Nullable Either<String[], Component> result = null;
    private final EditBox symbolNamingBox;
    private final ClickableText saveButton;
    private final ClickableText overwriteButton;
    private final ClickableText cancelButton;
    private final ClickableText backButton;
    private boolean isConfirmingOverwrite = false;

    public SymbolSaveWidget(Minecraft minecraft, int x, int y, int width, Runnable onClose, Runnable onReload) {
        super(x, y, width, 64, CommonComponents.EMPTY);
        this.minecraft = minecraft;
        this.onClose = onClose;
        this.onReload = onReload;

        this.symbolNamingBox = new EditBox(
                minecraft.font, x + 6, y + 6, width - 12, 16, Component.translatable("bigsignwriter.symbols.save.suggestion")
        );
        this.symbolNamingBox.setCanLoseFocus(false);
        this.symbolNamingBox.setSuggestion(this.symbolNamingBox.getMessage().getString());
        this.symbolNamingBox.setResponder(this::onSymbolKeyUpdated);
        this.symbolNamingBox.visible = false;

        this.saveButton = ClickableText.centered(
                Component.translatable("bigsignwriter.symbols.save").withStyle(ChatFormatting.AQUA),
                (int) (this.getX() + this.getWidth() * 0.33),
                this.getBottom() - 16,
                this::save
        );
        this.overwriteButton = ClickableText.centered(
                Component.translatable("bigsignwriter.symbols.save.overwrite").withStyle(ChatFormatting.AQUA),
                (int) (this.getX() + this.getWidth() * 0.33),
                this.getBottom() - 16,
                this::save
        );
        this.cancelButton = ClickableText.centered(
                Component.translatable("gui.cancel").withStyle(ChatFormatting.AQUA),
                (int) (this.getX() + this.getWidth() * 0.67),
                this.getBottom() - 16,
                this::close
        );
        this.backButton = ClickableText.centered(
                Component.translatable("gui.back").withStyle(ChatFormatting.AQUA),
                x + width / 2,
                this.getBottom() - 16,
                this::close
        );
    }

    @Override
    public @NotNull List<AbstractWidget> children() {
        return List.of(this.symbolNamingBox);
    }

    private List<ClickableWidgetPart> parts() {
        return List.of(this.saveButton, this.overwriteButton, this.cancelButton, this.backButton);
    }

    @Override
    public @Nullable GuiEventListener getFocused() {
        return this.isTyping() ? this.symbolNamingBox : null;
    }

    public void open(String[] lines) {
        this.visible = true;
        this.setResult(ModUtil.validateSymbolForSave(lines));
    }

    public void close() {
        if (!this.visible) return;
        this.visible = false;
        this.setResult(null);
        this.onClose.run();
    }

    private void save() {
        if (this.result == null || this.result.left().isEmpty()) return;

        String key = UserSymbolsManager.filterKey(this.symbolNamingBox.value);
        boolean willOverwrite = UserSymbolsManager.containsKey(key);
        if (willOverwrite && !this.isConfirmingOverwrite) {
            this.isConfirmingOverwrite = true;
            this.overwriteButton.visible = true;
            this.saveButton.visible = false;
            this.symbolNamingBox.visible = false;
            this.setMessage(Component.translatable(
                    "bigsignwriter.symbols.save.overwrite.message",
                    Component.literal(key).withStyle(ChatFormatting.YELLOW)
            ));
            return;
        }

        if (!UserSymbolsManager.put(key, this.result.left().get())) return;
        this.close();
        this.onReload.run();
    }

    public boolean isTyping() {
        return this.result != null && !this.isConfirmingOverwrite && this.result.left().isPresent();
    }

    private void setResult(@Nullable Either<String[], Component> result) {
        this.result = result;
        if (result == null) {
            this.setMessage(Component.empty());
            this.setFocused(null);
            return;
        }

        boolean errored = result.right().isPresent();
        this.setMessage(errored ?
                Component.translatable(
                        "bigsignwriter.symbols.save.error",
                        result.right().get().copy().withStyle(ChatFormatting.WHITE)
                ).withStyle(ChatFormatting.RED) :
                CommonComponents.EMPTY
        );

        this.backButton.visible = errored;
        this.saveButton.visible = !errored;
        this.cancelButton.visible = !errored;
        this.symbolNamingBox.setValue("");
        this.symbolNamingBox.visible = !errored;
        this.setFocused(errored ? null : this.symbolNamingBox);
        this.isConfirmingOverwrite = false;
        this.overwriteButton.visible = false;
    }

    private void onSymbolKeyUpdated(String key) {
        String filtered = UserSymbolsManager.filterKey(key);
        if (!filtered.equals(key)) {
            this.symbolNamingBox.setValue(filtered);
            return;
        }

        this.symbolNamingBox.setSuggestion(key.isEmpty() ?
                this.symbolNamingBox.getMessage().getString() : ""
        );
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent mouseButtonEvent, boolean doubleClick) {
        if (!this.isActive()) return false;

        if (this.parts().stream().anyMatch(part -> part.mouseClicked(mouseButtonEvent)))
            return true;

        return super.mouseClicked(mouseButtonEvent, doubleClick);
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.result == null) return;

        GraphicsHelper.drawCompleteMenuListBackground(guiGraphics, this.getX(), this.getY(), this.width, this.height);
        super.extractWidgetRenderState(guiGraphics, mouseX, mouseY, partialTick);
        this.parts().forEach(part -> part.extractRenderState(guiGraphics, mouseX, mouseY));

        if (!this.isTyping()) guiGraphics.textWithWordWrap(
                this.minecraft.font, this.getMessage(), this.getX() + 6, this.getY() + 6, this.getWidth() - 12, -1
        );
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, this.getMessage());
        this.children().forEach(widget -> widget.updateNarration(output));
    }
}
