package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

public class MonospaceFont extends AbstractFontSupplier {
    @Override
    public FontFile get() {
        return new FontFile("Default Monospace", "chililisoup")
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

    @Override
    public Map<Character, Set<PatchCharacter>> patches() {
        return new HashMap<>(Map.ofEntries(
                entry('I', Set.of(
                        PatchCharacter.of(
                                "███",
                                "  █  ",
                                "  █  ",
                                "███"
                        )
                )),
                entry('L', Set.of(
                        PatchCharacter.of(
                                "█    ",
                                "█    ",
                                "█    ",
                                "███"
                        )
                )),
                entry('(', Set.of(
                        PatchCharacter.of(
                                " ▟▉▛ ",
                                " █  ",
                                " █  ",
                                " ▜▉▙ "
                        )
                )),
                entry(')', Set.of(
                        PatchCharacter.of(
                                " ▜▉▙ ",
                                "  █ ",
                                "  █ ",
                                " ▟▉▛ "
                        )
                )),
                entry('{', Set.of(
                        PatchCharacter.of(
                                " ▟▉▛ ",
                                " \uD83E\uDF37▉  ",
                                " \uD83E\uDF28▉  ",
                                " ▜▉▙ "
                        )
                )),
                entry('}', Set.of(
                        PatchCharacter.of(
                                " ▜▉▙ ",
                                "  ▉\uD83E\uDF32 ",
                                "  ▉\uD83E\uDF15 ",
                                " ▟▉▛ "
                        )
                ))
        ));
    }
}
