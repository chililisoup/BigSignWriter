package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import java.util.Map;

import static java.util.Map.entry;

public class ThinFont implements FontSupplier {
    @Override
    public FontFile get() {
        return new FontFile("Thin", 3, " ", Map.<Character, String[]>ofEntries(
                entry(' ', new String[]{
                        "    ",
                        "    ",
                        "    "
                }),
                entry('A', new String[]{
                        ",╻·-·╻,",
                        "┃-·-┃",
                        "╹   ╹"
                }),
                entry('B', new String[]{
                        "╻--╻,",
                        "┃--⸵,",
                        "╹--╹'"
                }),
                entry('C', new String[]{
                        ",╻·-·╻,",
                        "┃    ",
                        "'╹·-·╹'"
                }),
                entry('D', new String[]{
                        "╻--╻,",
                        "┃   ┃",
                        "╹--╹'"
                }),
                entry('E', new String[]{
                        "╻-·-·",
                        "┃-·  ",
                        "╹-·-·"
                }),
                entry('F', new String[]{
                        "╻-·-·",
                        "┃-·  ",
                        "╹    "
                }),
                entry('G', new String[]{
                        ",╻·-·╻,",
                        "┃  -╻",
                        "'╹·-·╹'"
                }),
                entry('H', new String[]{
                        "╻   ╻",
                        "┃-·-┃",
                        "╹   ╹"
                }),
                entry('I', new String[]{
                        "·-╻-·",
                        "  ┃  ",
                        "·-╹-·"
                }),
                entry('J', new String[]{
                        "·-╻-·",
                        "  ┃  ",
                        "'-╹  "
                }),
                entry('K', new String[]{
                        "╻  ᷺.·",
                        "┃-< ",
                        "╹  \uD804\uDC81˙·"
                }),
                entry('L', new String[]{
                        "╻    ",
                        "┃    ",
                        "╹-·-·"
                }),
                entry('M', new String[]{
                        "╻\uD83E\uDFA2  \uD83E\uDFA3╻",
                        "┃ \uD83E\uDFA1\uD83E\uDFA0 ┃",
                        "╹   ╹"
                }),
                entry('N', new String[]{
                        "╻\uD83E\uDFA2  ╻",
                        "┃ ╲ ┃",
                        "╹  \uD83E\uDFA1╹"
                }),
                entry('O', new String[]{
                        ",╻·-·╻,",
                        "┃   ┃",
                        "'╹·-·╹'"
                }),
                entry('P', new String[]{
                        "╻--╻,",
                        "┃--╹'",
                        "╹    "
                }),
                entry('Q', new String[]{
                        ",╻·-·╻,",
                        "┃  , ┃",
                        "'╹·-'╹·"
                }),
                entry('R', new String[]{
                        "╻--╻,",
                        "┃-·╻;╹'",
                        "╹  '╹·"
                }),
                entry('S', new String[]{
                        ",╻·-·╻,",
                        "'╹·-·╻,",
                        "'╹·-·╹'"
                }),
                entry('T', new String[]{
                        "·-╻-·",
                        "  ┃  ",
                        "  ╹  "
                }),
                entry('U', new String[]{
                        "╻   ╻",
                        "┃   ┃",
                        "'╹·-·╹'"
                }),
                entry('V', new String[]{
                        "╻   ╻",
                        "╹,  ,╹",
                        " '╹·╹' "
                }),
                entry('W', new String[]{
                        "╻   ╻",
                        "┃ \uD83E\uDFA3\uD83E\uDFA2 ┃",
                        "╹\uD83E\uDFA0  \uD83E\uDFA1╹"
                }),
                entry('X', new String[]{
                        "·╻  ╻·",
                        " }·{ ",
                        "·╹  ╹·"
                }),
                entry('Y', new String[]{
                        "·╻  ╻·",
                        " '·╻·' ",
                        "  ╹  "
                }),
                entry('Z', new String[]{
                        "·--╻·",
                        " ᷺.·˙\uD804\uDC81 ",
                        "·╹--·"
                })
        ));
    }
}
