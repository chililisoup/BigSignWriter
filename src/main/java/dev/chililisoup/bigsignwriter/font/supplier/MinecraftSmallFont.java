//? if >= 1.21.4 {
package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import java.util.Map;

import static java.util.Map.entry;

public class MinecraftSmallFont implements FontSupplier {
    @Override
    public FontFile get() {
        return new FontFile("Minecraft Small", 2, "", Map.<Character, String[]>ofEntries(
                entry(' ', new String[]{
                        "  ",
                        "  "
                }),
                entry('A', new String[]{
                        "\uD833\uDD67\uD833\uDD1A\uD833\uDDA5",
                        "\uD833\uDD0D\uD836\uDE9D \uD836\uDE9D\uD833\uDD0D"
                }),
                entry('B', new String[]{
                        "\uD833\uDD68\uD833\uDD1A\uD833\uDD89",
                        "\uD833\uDD2C\uD833\uDD18\uD833\uDD12"
                }),
                entry('C', new String[]{
                        "\uD833\uDD49\uD833\uDEA8\uD833\uDD04",
                        "\uD833\uDD1D\uD833\uDD18\uD833\uDD10"
                }),
                entry('D', new String[]{
                        "\uD833\uDD4A\uD833\uDEA8\uD833\uDD97",
                        "\uD833\uDD2C\uD833\uDD18\uD833\uDD12"
                }),
                entry('E', new String[]{
                        "\uD833\uDD68\uD833\uDD1A\uD83E\uDF82",
                        "\uD833\uDD2C\uD833\uDD18\uD833\uDD27"
                }),
                entry('F', new String[]{
                        "\uD833\uDD68\uD833\uDD1A\uD83E\uDF82",
                        "\uD833\uDD0D  "
                }),
                entry('G', new String[]{
                        "\uD833\uDD49\uD833\uDEA8\uD833\uDD9F",
                        "\uD833\uDD1D\uD833\uDD18\uD833\uDD12"
                }),
                entry('H', new String[]{
                        "\uD833\uDD66\uD833\uDD09\uD833\uDDA6",
                        "\uD833\uDD0D\uD836\uDE9D \uD836\uDE9D\uD833\uDD0D"
                }),
                entry('I', new String[]{
                        "\uD833\uDEA8❙\uD833\uDEA8",
                        "\uD833\uDD18\uD833\uDD0D\uD833\uDD18"
                }),
                entry('J', new String[]{
                        "  ❙",
                        "\uD833\uDD1C\uD833\uDD18\uD833\uDD12"
                }),
                entry('K', new String[]{
                        "\uD833\uDD66\uD833\uDD09\uD833\uDD3B",
                        "\uD833\uDD0D\uD836\uDE9D \uD836\uDE9D\uD833\uDD0D"
                }),
                entry('L', new String[]{
                        "❙  ",
                        "\uD833\uDD2C\uD833\uDD18\uD833\uDD27"
                }),
                entry('M', new String[]{
                        "\uD833\uDD4F\uD833\uDD09\uD833\uDD9B",
                        "\uD833\uDD0D\uD836\uDE9D \uD836\uDE9D\uD833\uDD0D"
                }),
                entry('N', new String[]{
                        "\uD833\uDD4F\uD833\uDD09\uD833\uDDD5",
                        "\uD833\uDD0D\uD836\uDE9D \uD836\uDE9D\uD833\uDD0D"
                }),
                entry('O', new String[]{
                        "\uD833\uDD49\uD833\uDEA8\uD833\uDD97",
                        "\uD833\uDD1D\uD833\uDD18\uD833\uDD12"
                }),
                entry('P', new String[]{
                        "\uD833\uDD68\uD833\uDD1A\uD833\uDD11",
                        "\uD833\uDD0D  "
                }),
                entry('Q', new String[]{
                        "\uD833\uDD49\uD833\uDEA8\uD833\uDD97",
                        "\uD833\uDD1D\uD833\uDD18\uD833\uDD1E"
                }),
                entry('R', new String[]{
                        "\uD833\uDD68\uD833\uDD1A\uD833\uDD89",
                        "\uD833\uDD0D\uD836\uDE9D \uD836\uDE9D\uD833\uDD0D"
                }),
                entry('S', new String[]{
                        "\uD833\uDD1E\uD833\uDD1A\uD833\uDD83",
                        "\uD833\uDD1C\uD833\uDD18\uD833\uDD12"
                }),
                entry('T', new String[]{
                        "\uD83E\uDF82❙\uD83E\uDF82",
                        " \uD833\uDD0D "
                }),
                entry('U', new String[]{
                        "❙ܼ ܼ❙",
                        "\uD833\uDD1D\uD833\uDD18\uD833\uDD12"
                }),
                entry('V', new String[]{
                        "❙ܼ ܼ❙",
                        "᤺\uD83E\uDFE4\uD833\uDD18\uD83E\uDFE4᤺"
                }),
                entry('W', new String[]{
                        "❙ܼ ܼ❙",
                        "\uD833\uDD15\uD833\uDEA8\uD833\uDD25"
                }),
                entry('X', new String[]{
                        "\uD833\uDD79\uD833\uDD09\uD833\uDD3B",
                        "\uD833\uDD0D\uD836\uDE9D \uD836\uDE9D\uD833\uDD0D"
                }),
                entry('Y', new String[]{
                        "\uD833\uDD04\uD83E\uDFE5\uD833\uDD01",
                        " \uD833\uDD0D "
                }),
                entry('Z', new String[]{
                        "\uD83E\uDF82\uD833\uDD36\uD833\uDD13",
                        "\uD833\uDD2D\uD833\uDD18\uD833\uDD27"
                }),
                entry('a', new String[]{
                        "ܼܼ\uD833\uDD09\uD833\uDD09\uD833\uDD80",
                        "\uD833\uDD1E\uD833\uDD1A\uD833\uDD32"
                }),
                entry('b', new String[]{
                        "\uD833\uDDC0\uD833\uDD09\uD833\uDD80",
                        "\uD833\uDD2C\uD833\uDD18\uD833\uDD12"
                }),
                entry('c', new String[]{
                        "\uD833\uDD51\uD833\uDD09\uD833\uDD80",
                        "\uD833\uDD1D\uD833\uDD18\uD833\uDD10"
                }),
                entry('d', new String[]{
                        "\uD833\uDD51\uD833\uDD09\uD833\uDDD5",
                        "\uD833\uDD1D\uD833\uDD18\uD833\uDD31"
                }),
                entry('e', new String[]{
                        "\uD833\uDD51\uD833\uDD09\uD833\uDD80",
                        "\uD833\uDD1F\uD833\uDD1A\uD833\uDD2A"
                }),
                entry('f', new String[]{
                        "\uD833\uDDA4\uD833\uDD2A",
                        "᤺\uD833\uDD0D "
                }),
                entry('g', new String[]{
                        "\uD833\uDD51\uD833\uDD09\uD833\uDD9C",
                        "\uD833\uDDB4\uD833\uDD39\uD833\uDD5F"
                }),
                entry('h', new String[]{
                        "\uD833\uDDC0\uD833\uDD09\uD833\uDD80",
                        "\uD833\uDD0D\uD836\uDE9D \uD836\uDE9D\uD833\uDD0D"
                }),
                entry('i', new String[]{
                        "\uD833\uDD45",
                        "\uD833\uDD0D"
                }),
                entry('j', new String[]{
                        "  \uD833\uDD45",
                        "\uD833\uDD84\uD833\uDEA0\uD833\uDD5B"
                }),
                entry('k', new String[]{
                        "❙ܼܼ\uD833\uDD51",
                        "\uD833\uDD0F\uD833\uDD1C"
                }),
                entry('l', new String[]{
                        "❙᤺",
                        "\uD833\uDD1D"
                }),
                entry('m', new String[]{
                        "▂ܼܼܼ\uD833\uDEA0ܼܼ",
                        "\uD833\uDD0D᤺\uD833\uDEA8᤺\uD833\uDD0D"
                }),
                entry('n', new String[]{
                        "▂\uD833\uDEA0\uD833\uDEA0ܼܼ",
                        "\uD833\uDD0D\uD836\uDE9D \uD836\uDE9D\uD833\uDD0D"
                }),
                entry('o', new String[]{
                        "\uD833\uDD51\uD833\uDD09\uD833\uDD80",
                        "\uD833\uDD1D\uD833\uDD18\uD833\uDD12"
                }),
                entry('p', new String[]{
                        "\uD833\uDDBB\uD833\uDD09\uD833\uDD80",
                        "\uD833\uDD4F\uD833\uDD00\uD833\uDD01"
                }),
                entry('q', new String[]{
                        "\uD833\uDD51\uD833\uDD09\uD833\uDDCB",
                        "\uD833\uDD04\uD833\uDD00\uD833\uDD9B"
                }),
                entry('r', new String[]{
                        "\uD833\uDDBB\uD833\uDD09\uD833\uDD80",
                        "\uD833\uDD0D  "
                }),
                entry('s', new String[]{
                        "\uD833\uDD51\uD833\uDD09\uD833\uDD27",
                        "\uD833\uDD29\uD833\uDD1A\uD833\uDD11"
                }),
                entry('t', new String[]{
                        "\uD833\uDD09❙\uD833\uDD09",
                        "ܼ᤺\uD833\uDD0D\uD833\uDEA0"
                }),
                entry('u', new String[]{
                        "\uD83E\uDFE5ܼ ܼ\uD83E\uDFE5",
                        "\uD833\uDD1D\uD833\uDD18\uD833\uDD31"
                }),
                entry('v', new String[]{
                        "\uD83E\uDFE5ܼ ܼ\uD83E\uDFE5",
                        "\uD833\uDD04\uD833\uDD18\uD833\uDD01"
                }),
                entry('w', new String[]{
                        "\uD83E\uDFE5ܼ ܼ\uD83E\uDFE5",
                        "\uD833\uDD1D\uD833\uDD0D\uD833\uDD31"
                }),
                entry('x', new String[]{
                        "\uD833\uDD80ܼܼܼ\uD833\uDD51",
                        "\uD833\uDD10\uD833\uDEA8\uD833\uDD1C"
                }),
                entry('y', new String[]{
                        "\uD83E\uDFE5ܼ ܼ\uD83E\uDFE5",
                        "\uD833\uDDB4\uD833\uDD39\uD833\uDD5F"
                }),
                entry('z', new String[]{
                        "\uD833\uDD27\uD833\uDD18\uD833\uDD61",
                        "\uD833\uDD2F\uD833\uDD1A\uD833\uDD27"
                }),
                entry('`', new String[]{
                        "\uD833\uDD04",
                        " "
                }),
                entry('1', new String[]{
                        "\uD834\uDD6D\uD834\uDD6D\uD833\uDD00❙ ",
                        "\uD833\uDD27\uD833\uDD0D\uD833\uDD27"
                }),
                entry('2', new String[]{
                        "\uD833\uDD01\uD833\uDD36\uD833\uDD5A",
                        "\uD833\uDD2D\uD833\uDD18\uD833\uDD2F"
                }),
                entry('3', new String[]{
                        "\uD833\uDD01\uD833\uDD36\uD833\uDD5A",
                        "\uD833\uDD1C\uD833\uDD18\uD833\uDD12"
                }),
                entry('4', new String[]{
                        "\uD833\uDD51\uD833\uDD00\uD833\uDD98",
                        "\uD83E\uDF82\uD833\uDEA8\uD833\uDD22"
                }),
                entry('5', new String[]{
                        "\uD833\uDD2E\uD833\uDD1A\uD833\uDD83",
                        "\uD833\uDD1C\uD833\uDD18\uD833\uDD12"
                }),
                entry('6', new String[]{
                        "\uD833\uDDC3\uD833\uDD36\uD833\uDD36ܼܼ",
                        "\uD833\uDD1D\uD833\uDD18\uD833\uDD12"
                }),
                entry('7', new String[]{
                        "\uD833\uDD02\uD833\uDEA8\uD833\uDD5C",
                        " \uD833\uDD0D "
                }),
                entry('8', new String[]{
                        "\uD833\uDD86\uD833\uDD36\uD833\uDD5A",
                        "\uD833\uDD1D\uD833\uDD18\uD833\uDD12"
                }),
                entry('9', new String[]{
                        "\uD833\uDD86\uD833\uDD36\uD833\uDDD4",
                        "\uD834\uDD6D\uD834\uDD6D\uD833\uDD18\uD833\uDD18\uD833\uDD01"
                }),
                entry('0', new String[]{
                        "\uD833\uDD49\uD833\uDD36\uD833\uDDA5",
                        "\uD833\uDD1F\uD833\uDD18\uD833\uDD12"
                }),
                entry('-', new String[]{
                        "▂\uD833\uDEA0▂",
                        "   "
                }),
                entry('=', new String[]{
                        "\uD833\uDD27\uD833\uDD18\uD833\uDD27",
                        "\uD833\uDD06\uD833\uDD00\uD833\uDD06"
                }),
                entry('[', new String[]{
                        "\uD833\uDD4A\uD833\uDEA8",
                        "\uD833\uDD2C\uD833\uDD18"
                }),
                entry(']', new String[]{
                        "\uD833\uDEA8\uD833\uDD98",
                        "\uD833\uDD18\uD833\uDD31"
                }),
                entry(',', new String[]{
                        " ",
                        "▆"
                }),
                entry('.', new String[]{
                        " ",
                        "\uD833\uDD33"
                }),
                entry('/', new String[]{
                        " \uD833\uDEA0\uD833\uDD0E",
                        "\uD833\uDD12  "
                }),
                entry('\\', new String[]{
                        "\uD833\uDD20\uD833\uDEA0 ",
                        "  \uD833\uDD1D"
                }),
                entry(';', new String[]{
                        "\uD833\uDD33",
                        "▆"
                }),
                entry('\'', new String[]{
                        "\uD83E\uDF85",
                        " "
                }),
                entry('~', new String[]{
                        "\uD833\uDD01\uD833\uDD04\uD833\uDD01",
                        "   "
                }),
                entry('!', new String[]{
                        "❙",
                        "\uD833\uDD00"
                }),
                entry('@', new String[]{
                        "\uD833\uDD4B\uD833\uDDB7\uD833\uDD93",
                        "\uD833\uDD85\uD833\uDDB8\uD833\uDD43"
                }),
                entry('#', new String[]{
                        "\uD833\uDDA6\uD833\uDD09\uD833\uDD66",
                        "\uD833\uDD22\uD833\uDEA8\uD833\uDD0F"
                }),
                entry('$', new String[]{
                        "\uD833\uDD88\uD833\uDD3A\uD833\uDD41",
                        "\uD833\uDD06\uD83E\uDFE6\uD833\uDD01"
                }),
                entry('%', new String[]{
                        "\uD83E\uDFE4ܼܼ\uD833\uDEA0\uD833\uDD0E",
                        "\uD833\uDD12 \uD83E\uDFE6"
                }),
                entry('^', new String[]{
                        "\uD833\uDD10\uD833\uDEA8\uD833\uDD1C",
                        "   "
                }),
                entry('&', new String[]{
                        "ܼܼ\uD833\uDD39\uD833\uDD45\uD833\uDD74",
                        "\uD833\uDD1D\uD833\uDD0A\uD833\uDD1D"
                }),
                entry('*', new String[]{
                        "\uD833\uDD0A\uD833\uDD00\uD833\uDD0A",
                        "  "
                }),
                entry('(', new String[]{
                        "\uD833\uDD4B\uD833\uDEA8",
                        "\uD833\uDD04\uD833\uDD09"
                }),
                entry(')', new String[]{
                        "\uD833\uDEA8\uD833\uDD93",
                        "\uD833\uDD09\uD833\uDD01"
                }),
                entry('_', new String[]{
                        "   ",
                        "▂\uD833\uDEA0▂"
                }),
                entry('+', new String[]{
                        "▂\uD833\uDD48▂",
                        " \uD83E\uDFE4 "
                }),
                entry('{', new String[]{
                        "\uD833\uDD59\uD833\uDEA8",
                        "᤺\uD83E\uDFE4\uD833\uDD18"
                }),
                entry('}', new String[]{
                        "\uD833\uDEA8\uD833\uDD84",
                        "\uD833\uDD18\uD83E\uDFE4᤺"
                }),
                entry('<', new String[]{
                        "\uD833\uDD51\uD833\uDD01",
                        "᤺\uD833\uDEA8\uD833\uDD1C"
                }),
                entry('>', new String[]{
                        "\uD833\uDD04\uD833\uDD80",
                        "\uD833\uDD10\uD833\uDEA8᤺"
                }),
                entry('?', new String[]{
                        "\uD833\uDD01\uD833\uDEA8\uD833\uDD5A",
                        " \uD833\uDD0A "
                }),
                entry('|', new String[]{
                        "❙",
                        "\uD833\uDD0D"
                }),
                entry(':', new String[]{
                        "\uD833\uDD33",
                        "\uD833\uDD33"
                }),
                entry('"', new String[]{
                        "\uD83E\uDF85\uD83E\uDF85",
                        "  "
                })
        ));
    }
}
//?}