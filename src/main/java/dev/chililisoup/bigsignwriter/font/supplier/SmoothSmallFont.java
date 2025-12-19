//? if >= 1.21.6 {
package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import java.util.Map;

import static java.util.Map.entry;

public class SmoothSmallFont implements FontSupplier {
    @Override
    public FontFile get() {
        return new FontFile("Smooth Small", 2, Map.<Character, String[]>ofEntries(
                entry(' ', new String[]{
                        "  ",
                        "  "
                }),
                entry('A', new String[]{
                        "\uD839\uDDEF╱╲\uD839\uDDEF",
                        "\uD83E\uDFD6‾‾\uD83E\uDFD4"
                }),
                entry('a', new String[]{
                        "ܼ︵ܼ\uD833\uDE9C",
                        "\uD833\uDC39\uD833\uDC3E┃"
                }),
                entry('B', new String[]{
                        "┃\uD83E\uDF80\uD833\uDC7A",
                        "┃▁\uD833\uDC7A"
                }),
                entry('b', new String[]{
                        "┃︵ٖ",
                        "┃\uD833\uDC3D\uD833\uDC3A"
                }),
                entry('C', new String[]{
                        "\uD833\uDC35▔'",
                        "\uD833\uDC39▁."
                }),
                entry('c', new String[]{
                        "ܼ︵ܼ",
                        "\uD833\uDC39\uD833\uDC3E"
                }),
                entry('D', new String[]{
                        "|▔\uD833\uDC36",
                        "|▁\uD833\uDC3A"
                }),
                entry('d', new String[]{
                        "ܼ︵ܼ┃",
                        "\uD833\uDC39\uD833\uDC3E┃"
                }),
                entry('E', new String[]{
                        "┃\uD83E\uDF80▔",
                        "┃▁▁"
                }),
                entry('e', new String[]{
                        "ܼ︵ܼ",
                        "\uD833\uDC39\uD83E\uDF80"
                }),
                entry('F', new String[]{
                        "┃\uD83E\uDF80▔",
                        "┃  "
                }),
                entry('f', new String[]{
                        " ܼ\uD833\uDC35\uD833\uDC32\uD836\uDE31",
                        "▔┃▔ "
                }),
                entry('G', new String[]{
                        "\uD833\uDC35\uD833\uDC32",
                        "\uD833\uDC39ܼﾌ"
                }),
                entry('g', new String[]{
                        "❨︡┃",
                        "\uD833\uDC3Dﾌ"
                }),
                entry('H', new String[]{
                        "┃▁▁┃",
                        "┃  ┃"
                }),
                entry('h', new String[]{
                        "┃︵ٖ",
                        "┃  ┃"
                }),
                entry('I', new String[]{
                        "▔┃▔",
                        "▁┃▁"
                }),
                entry('i', new String[]{
                        "\uD83D\uDF97",
                        "┃"
                }),
                entry('J', new String[]{
                        "  ╿",
                        "\uD833\uDC3D\uD833\uDC3A"
                }),
                entry('j', new String[]{
                        "  \uD805\uDC48",
                        "\uD833\uDC3Dܼ\uD833\uDC3A"
                }),
                entry('K', new String[]{
                        "┃\uD833\uDC3E\uD833\uDC3F",
                        "┃ \uD833\uDC36"
                }),
                entry('k', new String[]{
                        "┃ \uD83E\uDFD0",
                        "┃\uD83E\uDFD1╲"
                }),
                entry('L', new String[]{
                        "┃  ",
                        "┃▁▁"
                }),
                entry('l', new String[]{
                        "╿",
                        "⇂"
                }),
                entry('M', new String[]{
                        "┃╲╱┃",
                        "┃  ┃"
                }),
                entry('m', new String[]{
                        "\uD833\uDE9C︵︵",
                        "┃  ╹ ┃"
                }),
                entry('N', new String[]{
                        "┃╲ ┃",
                        "┃ ╲┃"
                }),
                entry('n', new String[]{
                        "\uD833\uDE9Cܼ︵ܼ",
                        "┃  ┃"
                }),
                entry('O', new String[]{
                        "\uD833\uDC35\uD833\uDC36",
                        "\uD833\uDC39\uD833\uDC3A"
                }),
                entry('o', new String[]{
                        "ܼ︵ܼ",
                        "\uD833\uDC39\uD833\uDC3A"
                }),
                entry('P', new String[]{
                        "┃\uD83E\uDF80\uD833\uDC7A",
                        "┃  "
                }),
                entry('p', new String[]{
                        "┃\uD833\uDC31\uD833\uDC36",
                        "┃︶ꥏ"
                }),
                entry('Q', new String[]{
                        "\uD833\uDC35\uD833\uDC36",
                        "\uD833\uDC39ꭕ഻"
                }),
                entry('q', new String[]{
                        "\uD833\uDC35\uD833\uDC32┃",
                        "ꠦ︶┃"
                }),
                entry('R', new String[]{
                        "┃\uD83E\uDF80\uD833\uDC7A",
                        "┃ \uD833\uDC36"
                }),
                entry('r', new String[]{
                        "\uD833\uDE9C︵",
                        "┃  "
                }),
                entry('S', new String[]{
                        "\uD833\uDC78\uD833\uDC32",
                        "\uD833\uDC3D\uD833\uDC7A"
                }),
                entry('s', new String[]{
                        "₍\uD807\uDF49⸴",
                        "\uD833\uDE22\uD833\uDE2A\uD833\uDE4A"
                }),
                entry('T', new String[]{
                        "▔┃▔",
                        " ┃ "
                }),
                entry('t', new String[]{
                        "▁┃▁",
                        " ┃ "
                }),
                entry('U', new String[]{
                        "\uD833\uDE16 \uD833\uDE18",
                        "\uD833\uDC39\uD833\uDC3A"
                }),
                entry('u', new String[]{
                        "\uD833\uDE9C  \uD833\uDE9C",
                        "\uD833\uDC39\uD833\uDC3E┃"
                }),
                entry('V', new String[]{
                        "\uD804\uDC81\uD83E\uDFD4  \uD83E\uDFD6\uD804\uDC81",
                        " ╲╱ "
                }),
                entry('v', new String[]{
                        "̩  ̩",
                        "╲╱"
                }),
                entry('W', new String[]{
                        "┃  ┃",
                        "┃╱╲┃"
                }),
                entry('w', new String[]{
                        "\uD833\uDE9C  \uD833\uDE9C",
                        "\uD83E\uDFD5\uD83E\uDFA3\uD83E\uDFA2\uD83E\uDFD6"
                }),
                entry('X', new String[]{
                        "╲╱",
                        "╱╲"
                }),
                entry('x', new String[]{
                        "\uD83E\uDFA2 \uD83E\uDFA3",
                        "\uD83E\uDFA3͐᷾\uD83E\uDFA2"
                }),
                entry('Y', new String[]{
                        "╲╱",
                        " ┃ "
                }),
                entry('y', new String[]{
                        "᭞︨\uD833\uDEA5",
                        "\uD833\uDC3D\uD833\uDC3A"
                }),
                entry('Z', new String[]{
                        "̅⧶",
                        "⎳"
                }),
                entry('z', new String[]{
                        "ᝓ▁",
                        "⎳"
                }),
                entry('1', new String[]{
                        "\uD83E\uDFD1┃ ",
                        "▁┃▁"
                }),
                entry('2', new String[]{
                        "\uD833\uDC31\uD833\uDC7A",
                        "⎳\uD833\uDE98"
                }),
                entry('3', new String[]{
                        "\uD833\uDC31\uD833\uDC7A",
                        "\uD833\uDC3D\uD833\uDC7A"
                }),
                entry('4', new String[]{
                        "⎳┃",
                        "  ┃"
                }),
                entry('5', new String[]{
                        "┃\uD83E\uDF80▔",
                        "\uD833\uDC3D᷺\uD833\uDC7A"
                }),
                entry('6', new String[]{
                        "\uD833\uDC35\uD833\uDC32",
                        "\uD833\uDC39\uD833\uDC7A"
                }),
                entry('7', new String[]{
                        "ᜲᜲ⧶",
                        "╱ "
                }),
                entry('8', new String[]{
                        "\uD833\uDC78\uD833\uDC7A",
                        "\uD833\uDC78\uD833\uDC7A"
                }),
                entry('9', new String[]{
                        "\uD833\uDC78\uD833\uDC36",
                        "\uD833\uDC3D\uD833\uDC3A"
                }),
                entry('0', new String[]{
                        "\uD833\uDC35◺",
                        "◹\uD833\uDC3A"
                }),
                entry('`', new String[]{
                        "╲",
                        " "
                }),
                entry('~', new String[]{
                        "\uD833\uDE2C\uD833\uDE49\uD833\uDE22 \uD833\uDE23",
                        "\uD833\uDE2D \uD833\uDE2B\uD833\uDE4F\uD833\uDE3B"
                }),
                entry('!', new String[]{
                        "▎",
                        "╻"
                }),
                entry('@', new String[]{
                        "ܼ︵\uD806\uDE5B",
                        "\uD83E\uDD07\uD833\uDC38\uD833\uDC3A"
                }),
                entry('#', new String[]{
                        "ᝓ\uD833\uDE1Cᝓ\uD833\uDE1Cᝓ",
                        "╺┃╺┃╺"
                }),
                entry('$', new String[]{
                        "\uD833\uDC78┃\uD833\uDC32",
                        "\uD833\uDC3D┃\uD833\uDC7A"
                }),
                entry('%', new String[]{
                        "꣐╱",
                        "╱꣐"
                }),
                entry('^', new String[]{
                        "╱╲",
                        "  "
                }),
                entry('&', new String[]{
                        "❨❩ ",
                        "\uD833\uDC78\uD833\uDC38\uD83E\uDFA0"
                }),
                entry('*', new String[]{
                        "\uD83D\uDFB6",
                        "  "
                }),
                entry('(', new String[]{
                        "\uD833\uDC35",
                        "\uD833\uDC39"
                }),
                entry(')', new String[]{
                        "\uD833\uDC36",
                        "\uD833\uDC3A"
                }),
                entry('-', new String[]{
                        "▁▁",
                        "  "
                }),
                entry('_', new String[]{
                        "  ",
                        "▁▁"
                }),
                entry('=', new String[]{
                        "▁▁",
                        "━━"
                }),
                entry('+', new String[]{
                        "▁\uD833\uDEA5▁",
                        " ╹ "
                }),
                entry('[', new String[]{
                        "┃▔",
                        "┃▁"
                }),
                entry(']', new String[]{
                        "▔┃",
                        "▁┃"
                }),
                entry('{', new String[]{
                        "\uD83E\uDFEE\uD833\uDC35",
                        "\uD83E\uDFEC\uD833\uDC39"
                }),
                entry('}', new String[]{
                        "\uD833\uDC36\uD83E\uDFED",
                        "\uD833\uDC3A\uD83E\uDFEF"
                }),
                entry(',', new String[]{
                        " ",
                        "❟"
                }),
                entry('.', new String[]{
                        " ",
                        "⚫"
                }),
                entry('<', new String[]{
                        "／",
                        "＼"
                }),
                entry('>', new String[]{
                        "＼",
                        "／"
                }),
                entry('/', new String[]{
                        " ╱",
                        "╱ "
                }),
                entry('\\', new String[]{
                        "╲ ",
                        " ╲"
                }),
                entry('?', new String[]{
                        "\uD833\uDC31\uD834\uDDC4\uD833\uDC7A",
                        " \uD83E\uDF03 "
                }),
                entry('|', new String[]{
                        "┃",
                        "┃"
                }),
                entry(';', new String[]{
                        "⦁",
                        "❟"
                }),
                entry(':', new String[]{
                        "⦁",
                        "⦁"
                }),
                entry('\'', new String[]{
                        "\uD83E\uDC37",
                        " "
                }),
                entry('"', new String[]{
                        "\uD83E\uDC37\uD83E\uDC37",
                        "  "
                })
        ));
    }
}
//?}