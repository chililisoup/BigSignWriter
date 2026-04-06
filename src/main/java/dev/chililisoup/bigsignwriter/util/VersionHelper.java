//? if < 1.21.9 {
/*package dev.chililisoup.bigsignwriter.util;

import net.minecraft.client.gui.screens.Screen;

public final class VersionHelper {
    public record KeyEvent(int key, int scancode, int modifiers) {
        public boolean isConfirmation() {
            return this.key == 257 || this.key == 335;
        }

        public boolean isLeft() {
            return this.key == 263;
        }

        public boolean isRight() {
            return this.key == 262;
        }

        public boolean isUp() {
            return this.key == 265;
        }

        public boolean isDown() {
            return this.key == 264;
        }

        public boolean hasControlDown() {
            return Screen.hasControlDown();
        }
    }
}
*///?}