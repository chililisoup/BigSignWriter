package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import java.util.Map;

import static java.util.Map.entry;

public class OutlinedFont implements FontSupplier {
    @Override
    public FontFile get() {
        return new FontFile("Outlined", " ", Map.<Character, String[]>ofEntries(
                entry(' ', new String[]{
                        "      ",
                        "      ",
                        "      ",
                        "      "
                }),
                entry('A', new String[]{
                        "╔═╗",
                        "╠═╣",
                        "║  ║",
                        "╙  ╜"
                }),
                entry('B', new String[]{
                        "╔═╕,",
                        "╠═╡,",
                        "║   │",
                        "╚═╛'"
                }),
                entry('C', new String[]{
                        "╔═╗",
                        "║    ",
                        "║    ",
                        "╚═╝"
                }),
                entry('D', new String[]{
                        "╔═╕,",
                        "║   │",
                        "║   │",
                        "╚═╛'"
                }),
                entry('E', new String[]{
                        "╔═╗",
                        "╠═  ",
                        "║    ",
                        "╚═╝"
                }),
                entry('F', new String[]{
                        "╔═╗",
                        "╠═  ",
                        "║    ",
                        "╙    "
                }),
                entry('G', new String[]{
                        "╔═╗",
                        "║    ",
                        "║  ╖",
                        "╚═╣"
                }),
                entry('H', new String[]{
                        "╓  ╖",
                        "╠═╣",
                        "║  ║",
                        "╙  ╜"
                }),
                entry('I', new String[]{
                        "╔╦╗",
                        "  ║  ",
                        "  ║  ",
                        "╚╩╝"
                }),
                entry('J', new String[]{
                        "    ╖",
                        "    ║",
                        "╓  ║",
                        "╚═╝"
                }),
                entry('K', new String[]{
                        "╓  ╖",
                        "╠╦╛'",
                        "║  ╕,",
                        "╙  ╜"
                }),
                entry('L', new String[]{
                        "╓    ",
                        "║    ",
                        "║    ",
                        "╚═╝"
                }),
                entry('M', new String[]{
                        "╔╦╗",
                        "║ܼ║ܼ║",
                        "║  ║",
                        "╙  ╜"
                }),
                entry('N', new String[]{
                        "╔═╕,",
                        "║  ║",
                        "║  ║",
                        "╙  ╜"
                }),
                entry('O', new String[]{
                        "╔═╗",
                        "║  ║",
                        "║  ║",
                        "╚═╝"
                }),
                entry('P', new String[]{
                        "╔═╕,",
                        "╠═╛'",
                        "║    ",
                        "╙    "
                }),
                entry('Q', new String[]{
                        "╔═╗",
                        "║   │",
                        "║ │,│",
                        "╚═╡'"
                }),
                entry('R', new String[]{
                        "╔═╕,",
                        "╠╦╛'",
                        "║  ╕,",
                        "╙  ╜"
                }),
                entry('S', new String[]{
                        "╔═╗",
                        "╚═╗",
                        "╓  ║",
                        "╚═╝"
                }),
                entry('T', new String[]{
                        "╔╦╗",
                        "  ║  ",
                        "  ║  ",
                        "  ╙  "
                }),
                entry('U', new String[]{
                        "╓  ╖",
                        "║  ║",
                        "║  ║",
                        "╚═╝"
                }),
                entry('V', new String[]{
                        "╓  ╖",
                        "║  ║",
                        "║  ║",
                        " ╚\uD836\uDE9D╝ "
                }),
                entry('W', new String[]{
                        "╓  ╖",
                        "║  ║",
                        "║ܼ║ܼ║",
                        "╚╩╝"
                }),
                entry('X', new String[]{
                        "╓  ╖",
                        " ╠\uD836\uDE9D╣ ",
                        "║  ║",
                        "╙  ╜"
                }),
                entry('Y', new String[]{
                        "╓  ╖",
                        "╚═╣",
                        "╓  ║",
                        "╚═╝"
                }),
                entry('Z', new String[]{
                        "╔═╗",
                        " ╔\uD836\uDE9D╝ ",
                        "║    ",
                        "╚═╝"
                })
        ));
    }
}
