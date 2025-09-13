package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import java.util.Map;

import static java.util.Map.entry;

public class MonospaceFont implements FontSupplier {
    @Override
    public FontFile get() {
        return new FontFile("Monospace", Map.<Character, String[]>ofEntries(
                entry('I', new String[]{
                        "███",
                        "  █  ",
                        "  █  ",
                        "███"
                }),
                entry('i', new String[]{
                        "  █  ",
                        "  ▄  ",
                        "  █  ",
                        "  █  "
                }),
                entry('L', new String[]{
                        "█    ",
                        "█    ",
                        "█    ",
                        "███"
                }),
                entry('l', new String[]{
                        " █   ",
                        " █   ",
                        " █   ",
                        " ▜▉▄ "
                }),
                entry('!', new String[]{
                        "  █  ",
                        "  █  ",
                        "  ▀  ",
                        "  █  "
                }),
                entry('[', new String[]{
                        " ██ ",
                        " █   ",
                        " █   ",
                        " ██ "
                }),
                entry(']', new String[]{
                        " ██ ",
                        "   █ ",
                        "   █ ",
                        " ██ "
                }),
                entry('(', new String[]{
                        " ▟▉▛ ",
                        " █  ",
                        " █  ",
                        " ▜▉▙ "
                }),
                entry(')', new String[]{
                        " ▜▉▙ ",
                        "  █ ",
                        "  █ ",
                        " ▟▉▛ "
                }),
                entry(':', new String[]{
                        "  ▄  ",
                        "  ▀  ",
                        "  ▄  ",
                        "  ▀  "
                }),
                entry('.', new String[]{
                        "      ",
                        "      ",
                        "      ",
                        "  █  "
                }),
                entry('|', new String[]{
                        "  █  ",
                        "  █  ",
                        "  █  ",
                        "  █  "
                }),
                entry(',', new String[]{
                        "      ",
                        "      ",
                        "  ▄  ",
                        "  \uD83E\uDF0F▋\uD83E\uDF04  "
                }),
                entry(';', new String[]{
                        "      ",
                        "  █  ",
                        "  ▄  ",
                        "  \uD83E\uDF0F▋\uD83E\uDF04  "
                }),
                entry('\'', new String[]{
                        "  █  ",
                        "  ▀  ",
                        "      ",
                        "      "
                }),
                entry('"', new String[]{
                        " ██ ",
                        " ▀▀ ",
                        "      ",
                        "      "
                }),
                entry('`', new String[]{
                        "  ▜▙  ",
                        "   ▀ ",
                        "      ",
                        "      "
                }),
                entry('{', new String[]{
                        " ▟▉▛ ",
                        " \uD83E\uDF37▉  ",
                        " \uD83E\uDF28▉  ",
                        " ▜▉▙ "
                }),
                entry('}', new String[]{
                        " ▜▉▙ ",
                        "  ▉\uD83E\uDF32 ",
                        "  ▉\uD83E\uDF15 ",
                        " ▟▉▛ "
                })
        ));
    }
}
