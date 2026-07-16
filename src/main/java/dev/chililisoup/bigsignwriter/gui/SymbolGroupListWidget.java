package dev.chililisoup.bigsignwriter.gui;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.font.SymbolGroup;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SymbolGroupListWidget extends ObjectSelectionList<SymbolGroupListWidget.Entry> {
    private int maxHeight;
    private final Consumer<@Nullable SymbolGroup> onSelect;
    public @Nullable SymbolGroup allGroup;

    public SymbolGroupListWidget(
            Minecraft minecraft,
            int width,
            int height,
            int x,
            int y,
            int itemHeight,
            Consumer<@Nullable SymbolGroup> onSelect
    ) {
        super(minecraft, width, height, y, itemHeight);
        this.maxHeight = height;
        this.onSelect = onSelect;
        this.setX(x);
        this.updateEntries();
    }

    public void updateEntries() {
        List<SymbolGroup> visibleGroups = BigSignWriter.availableSymbolGroups().stream()
                .filter(SymbolGroup::isVisible)
                .toList();
        this.allGroup = SymbolGroup.ofMerged(Component.translatable("bigsignwriter.symbols.all").getString(), visibleGroups);

        ArrayList<SymbolGroup> groups = new ArrayList<>();
        if (this.allGroup != null) groups.add(this.allGroup);
        groups.addAll(visibleGroups);

        this.replaceEntries(groups.stream().map(Entry::new).toList());
        super.setSelected(null);

        this.updateHeight();
        this.setScrollAmount(0.0);
    }

    private void updateHeight() {
        this.setHeight(Math.min(this.maxHeight, this.contentHeight()));
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        this.updateHeight();
        this.refreshScrollAmount();
    }

    @Override
    public int getRowWidth() {
        return this.width - 32;
    }

    @Override
    public void setSelected(@Nullable Entry entry) {
        if (this.getSelected() == entry) return;
        this.playDownSound(this.minecraft.getSoundManager());
        super.setSelected(entry);
        this.onSelect.accept(getEntryGroup(entry));
    }

    public @Nullable SymbolGroup getSelectedGroup() {
        return getEntryGroup(this.getSelected());
    }

    private static @Nullable SymbolGroup getEntryGroup(@Nullable Entry entry) {
        return Optional.ofNullable(entry)
                .map(Entry::getGroup)
                .orElse(null);
    }

    public void setSelectedGroup(@Nullable SymbolGroup group) {
        if (group == null) {
            super.setSelected(null);
            return;
        }

        for (Entry entry : this.children()) {
            if (entry.group.equals(group)) {
                super.setSelected(entry);
                break;
            }
        }
    }

    public static class Entry extends ObjectSelectionList.Entry<Entry> {
        private final SymbolGroup group;
        private final Component name;

        public Entry(SymbolGroup group) {
            this.group = group;
            this.name = Component.literal(group.name());
        }

        public SymbolGroup getGroup() {
            return this.group;
        }

        public Component getName() {
            return this.name;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", this.name);
        }

        @Override
        //~ if >= 26.1 'renderContent' -> 'extractContent'
        public void extractContent(
                @NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, boolean hovered, float partialTick
        ) {
            int left = this.getContentX();
            int top = this.getContentY();
            int width = this.getContentWidth();
            int height = this.getContentHeight();

            if (hovered) {
                guiGraphics.fill(left, top, left + width, top + height, 0x40FFFFFF);
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
            }

            GraphicsHelper.drawScrollingString(
                    guiGraphics,
                    this.name,
                    left + 5,
                    left + 5,
                    left + width - 5,
                    top,
                    top + height
            );
        }
    }
}
