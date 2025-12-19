package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import java.util.Map;

import static java.util.Map.entry;

public class SmallFont implements FontSupplier {
    @Override
    public FontFile get() {
        return new FontFile("Small", 2, "", Map.<Character, String[]>ofEntries(
                entry(' ', new String[]{
                        "  ",
                        "  "
                }),
                entry('A', new String[]{
                        "\uD83E\uDF33\uD83E\uDF36",
                        "❙ ❙"
                }),
                entry('a', new String[]{
                        "\uD83E\uDF0B\uD83E\uDF22",
                        "\uD83E\uDF24\uD83E\uDF38"
                }),
                entry('B', new String[]{
                        "\uD83E\uDF34\uD83E\uDF17",
                        "\uD83E\uDF32\uD83E\uDF18"
                }),
                entry('b', new String[]{
                        "\uD83E\uDF32\uD83E\uDF0Fܼܼ",
                        "\uD83E\uDF32\uD83E\uDF18"
                }),
                entry('C', new String[]{
                        "\uD83E\uDF14\uD83E\uDF08",
                        "\uD83E\uDF23\uD83E\uDF16"
                }),
                entry('c', new String[]{
                        "\uD83E\uDF16\uD83E\uDF22",
                        "\uD83E\uDF23\uD83E\uDF16"
                }),
                entry('D', new String[]{
                        "\uD83E\uDF15\uD83E\uDF27",
                        "\uD83E\uDF32\uD83E\uDF18"
                }),
                entry('d', new String[]{
                        "ܼܼ\uD83E\uDF1E\uD83E\uDF37",
                        "\uD83E\uDF23\uD83E\uDF37"
                }),
                entry('E', new String[]{
                        "\uD83E\uDF34\uD83E\uDF12",
                        "\uD83E\uDF32\uD83E\uDF2D"
                }),
                entry('e', new String[]{
                        "\uD83E\uDF16\uD83E\uDF22",
                        "\uD83E\uDF25\uD83E\uDF30"
                }),
                entry('F', new String[]{
                        "\uD83E\uDF34\uD83E\uDF12",
                        "▍  "
                }),
                entry('f', new String[]{
                        "\uD83E\uDF35\uD83E\uDF30",
                        "᤺❙ "
                }),
                entry('G', new String[]{
                        "\uD83E\uDF14\uD83E\uDF08",
                        "\uD83E\uDF23\uD83E\uDF19"
                }),
                entry('g', new String[]{
                        "\uD83E\uDF16\uD83E\uDF29",
                        "\uD83E\uDF2F\uD83E\uDF19"
                }),
                entry('H', new String[]{
                        "\uD83E\uDF32\uD83E\uDF37",
                        "❙ ❙"
                }),
                entry('h', new String[]{
                        "\uD83E\uDF32\uD83E\uDF0Fܼܼ",
                        "❙ ❙"
                }),
                entry('I', new String[]{
                        "\uD83E\uDF28\uD83E\uDF02",
                        "\uD83E\uDF37\uD83E\uDF2D"
                }),
                entry('i', new String[]{
                        "\uD83E\uDF2F ",
                        "\uD83E\uDF37\uD83E\uDF2D"
                }),
                entry('J', new String[]{
                        "  ▍",
                        "\uD83E\uDF22\uD83E\uDF18"
                }),
                entry('j', new String[]{
                        " \uD83E\uDF2F",
                        "\uD83E\uDF22\uD83E\uDF18"
                }),
                entry('K', new String[]{
                        "\uD83E\uDF32\uD83E\uDF05",
                        "❙᤺\uD83E\uDF27"
                }),
                entry('k', new String[]{
                        "❙ܼܼ\uD83E\uDF16",
                        "\uD83E\uDF15\uD83E\uDF22"
                }),
                entry('L', new String[]{
                        "▍  ",
                        "\uD83E\uDF32\uD83E\uDF2D"
                }),
                entry('l', new String[]{
                        "\uD83E\uDF28 ",
                        "ܼܼ\uD83E\uDF09\uD83E\uDF2D"
                }),
                entry('M', new String[]{
                        "\uD83E\uDF3A\uD83E\uDF3B",
                        "❙ ❙"
                }),
                entry('m', new String[]{
                        "\uD83E\uDF2D\uD83E\uDF2D",
                        "\uD83E\uDF15\uD83E\uDF28"
                }),
                entry('N', new String[]{
                        "\uD83E\uDF3Aܼܼ❙",
                        "❙᤺\uD83E\uDF2C"
                }),
                entry('n', new String[]{
                        "\uD83E\uDF2D\uD83E\uDF0Fܼܼ",
                        "❙ ❙"
                }),
                entry('O', new String[]{
                        "\uD83E\uDF14\uD83E\uDF27",
                        "\uD83E\uDF23\uD83E\uDF18"
                }),
                entry('o', new String[]{
                        "ܼܼ\uD83E\uDF1E\uD83E\uDF0Fܼܼ",
                        "\uD83E\uDF23\uD83E\uDF18"
                }),
                entry('P', new String[]{
                        "\uD83E\uDF34\uD83E\uDF17",
                        "▍  "
                }),
                entry('p', new String[]{
                        "\uD83E\uDF1A\uD83E\uDF22",
                        "\uD83E\uDF15\uD83E\uDF00᤺"
                }),
                entry('Q', new String[]{
                        "\uD83E\uDF14\uD83E\uDF27",
                        "\uD83E\uDF23\uD83E\uDF24"
                }),
                entry('q', new String[]{
                        "\uD83E\uDF16\uD83E\uDF29",
                        "᤺\uD83E\uDF01\uD83E\uDF28"
                }),
                entry('R', new String[]{
                        "\uD83E\uDF34\uD83E\uDF17",
                        "❙ ❙"
                }),
                entry('r', new String[]{
                        "\uD83E\uDF2D\uD83E\uDF0Fܼܼ",
                        "❙ \uD83E\uDF01"
                }),
                entry('S', new String[]{
                        "\uD83E\uDF24\uD83E\uDF12",
                        "\uD83E\uDF22\uD83E\uDF18"
                }),
                entry('s', new String[]{
                        "\uD83E\uDF16\uD83E\uDF0B",
                        "\uD83E\uDF2F\uD83E\uDF17"
                }),
                entry('T', new String[]{
                        "\uD83E\uDF28\uD83E\uDF02",
                        "ܼܼ❙ "
                }),
                entry('t', new String[]{
                        "\uD83E\uDF37\uD83E\uDF2D",
                        "᤺\uD83E\uDF09\uD83E\uDF2D"
                }),
                entry('U', new String[]{
                        "❙ ❙",
                        "\uD83E\uDF23\uD83E\uDF18"
                }),
                entry('u', new String[]{
                        "\uD83E\uDF0F \uD83E\uDF1E",
                        "\uD83E\uDF23\uD83E\uDF37"
                }),
                entry('V', new String[]{
                        "❙ ❙",
                        "\uD83E\uDF23\uD83E\uDF05"
                }),
                entry('v', new String[]{
                        "\uD83E\uDF0F \uD83E\uDF1E",
                        "\uD83E\uDF23\uD83E\uDF05"
                }),
                entry('W', new String[]{
                        "❙ ❙",
                        "\uD83E\uDF1D\uD83E\uDF2C"
                }),
                entry('w', new String[]{
                        "\uD83E\uDF0F \uD83E\uDF1E",
                        "\uD83E\uDF3A\uD83E\uDF3B"
                }),
                entry('X', new String[]{
                        "❙ܼܼ\uD83E\uDF18",
                        "\uD83E\uDF14᤺❙"
                }),
                entry('x', new String[]{
                        "\uD83E\uDF0F \uD83E\uDF1E",
                        "\uD83E\uDF17\uD83E\uDF27"
                }),
                entry('Y', new String[]{
                        "❙ܼܼ\uD83E\uDF18",
                        "᤺❙ "
                }),
                entry('y', new String[]{
                        "\uD83E\uDF13 \uD83E\uDF26",
                        "\uD83E\uDF2F\uD83E\uDF19"
                }),
                entry('Z', new String[]{
                        "\uD83E\uDF02\uD83E\uDF19",
                        "\uD83E\uDF33\uD83E\uDF2D"
                }),
                entry('z', new String[]{
                        "\uD83E\uDF2D\uD83E\uDF2D",
                        "\uD83E\uDF35\uD83E\uDF2E"
                }),
                entry('1', new String[]{
                        "\uD83E\uDF2B ",
                        "\uD83E\uDF37\uD83E\uDF2D"
                }),
                entry('2', new String[]{
                        "\uD83E\uDF05\uD83E\uDF17",
                        "\uD83E\uDF33\uD83E\uDF35"
                }),
                entry('3', new String[]{
                        "\uD83E\uDF05\uD83E\uDF17",
                        "\uD83E\uDF22\uD83E\uDF18"
                }),
                entry('4', new String[]{
                        "\uD83E\uDF33\uD83E\uDF37",
                        "  ▍"
                }),
                entry('5', new String[]{
                        "\uD83E\uDF34\uD83E\uDF12",
                        "\uD83E\uDF22\uD83E\uDF18"
                }),
                entry('6', new String[]{
                        "\uD83E\uDF33\uD83E\uDF10ܼܼ",
                        "\uD83E\uDF23\uD83E\uDF18"
                }),
                entry('7', new String[]{
                        "\uD83E\uDF06\uD83E\uDF19",
                        "\uD83E\uDF14 "
                }),
                entry('8', new String[]{
                        "\uD83E\uDF24\uD83E\uDF17",
                        "\uD83E\uDF23\uD83E\uDF18"
                }),
                entry('9', new String[]{
                        "\uD83E\uDF24\uD83E\uDF36",
                        "\uD83E\uDF22\uD83E\uDF18"
                }),
                entry('0', new String[]{
                        "\uD83E\uDF14\uD83E\uDF36",
                        "\uD83E\uDF25\uD83E\uDF18"
                }),
                entry('`', new String[]{
                        "\uD83E\uDF08",
                        " "
                }),
                entry('~', new String[]{
                        "ܼܼ\uD83E\uDF1Eܼܼ\uD83E\uDF1E",
                        "\uD83E\uDF00᤺\uD83E\uDF00᤺"
                }),
                entry('!', new String[]{
                        "❙",
                        "\uD83E\uDF03"
                }),
                entry('@', new String[]{
                        "\uD83E\uDF14\uD83E\uDF27",
                        "\uD83E\uDF23\uD83E\uDF2E"
                }),
                entry('#', new String[]{
                        "\uD83E\uDF2B\uD83E\uDF1C",
                        "\uD83E\uDF06\uD83E\uDF06"
                }),
                entry('$', new String[]{
                        "\uD83E\uDF24\uD83E\uDF15",
                        "\uD83E\uDF22\uD83E\uDF1D"
                }),
                entry('%', new String[]{
                        "\uD83E\uDF03ܼܼ\uD83E\uDF14",
                        "\uD83E\uDF18᤺\uD83E\uDF07"
                }),
                entry('^', new String[]{
                        "\uD83E\uDF05\uD83E\uDF08",
                        "  "
                }),
                entry('&', new String[]{
                        "\uD83E\uDF24\uD83E\uDF04ܼܼ",
                        "\uD83E\uDF32\uD83E\uDF25"
                }),
                entry('*', new String[]{
                        "\uD83E\uDF17\uD83E\uDF24",
                        "  "
                }),
                entry('(', new String[]{
                        "\uD83E\uDF16\uD83E\uDF02",
                        "\uD83E\uDF08\uD83E\uDF2D"
                }),
                entry(')', new String[]{
                        "\uD83E\uDF02\uD83E\uDF22",
                        "\uD83E\uDF2D\uD83E\uDF05"
                }),
                entry('-', new String[]{
                        "\uD83E\uDF2D\uD83E\uDF2D",
                        "  "
                }),
                entry('_', new String[]{
                        "  ",
                        "\uD83E\uDF2D\uD83E\uDF2D"
                }),
                entry('=', new String[]{
                        "\uD83E\uDF0B\uD83E\uDF0B",
                        "\uD83E\uDF02\uD83E\uDF02"
                }),
                entry('+', new String[]{
                        "\uD83E\uDF2B\uD83E\uDF03",
                        "  "
                }),
                entry('[', new String[]{
                        "\uD83E\uDF15\uD83E\uDF02",
                        "\uD83E\uDF32\uD83E\uDF2D"
                }),
                entry(']', new String[]{
                        "\uD83E\uDF02\uD83E\uDF28",
                        "\uD83E\uDF2D\uD83E\uDF37"
                }),
                entry('{', new String[]{
                        "\uD83E\uDF35\uD83E\uDF02",
                        "\uD83E\uDF0A\uD83E\uDF2D"
                }),
                entry('}', new String[]{
                        "\uD83E\uDF02\uD83E\uDF31",
                        "\uD83E\uDF2D\uD83E\uDF06"
                }),
                entry(',', new String[]{
                        " ",
                        "\uD83E\uDF19"
                }),
                entry('.', new String[]{
                        " ",
                        "\uD83E\uDF39"
                }),
                entry('<', new String[]{
                        "ܼܼ\uD83E\uDF1E\uD83E\uDF05",
                        " \uD83E\uDF08"
                }),
                entry('>', new String[]{
                        "\uD83E\uDF08\uD83E\uDF1Eܼܼ",
                        "\uD83E\uDF05 "
                }),
                entry('/', new String[]{
                        " \uD83E\uDF14",
                        "\uD83E\uDF18 "
                }),
                entry('\\', new String[]{
                        "\uD83E\uDF27 ",
                        " \uD83E\uDF23"
                }),
                entry('?', new String[]{
                        "\uD83E\uDF05\uD83E\uDF17",
                        "ܼܼ\uD83E\uDF20 "
                }),
                entry('|', new String[]{
                        "❙",
                        "❙"
                }),
                entry(';', new String[]{
                        "\uD83E\uDF0E",
                        "\uD83E\uDF19"
                }),
                entry(':', new String[]{
                        "\uD83E\uDF0E",
                        "\uD83E\uDF0E"
                }),
                entry('\'', new String[]{
                        "\uD83E\uDF0E",
                        " "
                }),
                entry('"', new String[]{
                        "\uD83E\uDF0E\uD83E\uDF0E",
                        "  "
                })
        ));
    }
}
