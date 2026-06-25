package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import static java.util.Map.entry;

public final class DefaultMonoFont {
    public static FontFile get() {
        return new FontFile("Default Mono", "chililisoup")
                .parentFont("default")
                .characters(
                        entry('i', new String[]{
                                "  █  ",
                                "  ▄  ",
                                "  █  ",
                                "  █  "
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
                                "  ▟▉▛ ",
                                "  █  ",
                                "  █  ",
                                "  ▜▉▙ "
                        }),
                        entry(')', new String[]{
                                " ▜▉▙  ",
                                "  █  ",
                                "  █  ",
                                " ▟▉▛  "
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
                                "  ▟▉▛ ",
                                "  \uD83E\uDF37▉  ",
                                "  \uD83E\uDF28▉  ",
                                "  ▜▉▙ "
                        }),
                        entry('}', new String[]{
                                " ▜▉▙  ",
                                "  ▉\uD83E\uDF32  ",
                                "  ▉\uD83E\uDF15  ",
                                " ▟▉▛  "
                        })
                );
    }
}
