package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import java.util.Map;

import static java.util.Map.entry;

public class DefaultFont implements FontSupplier {
    @Override
    public FontFile get() {
        return new FontFile("Default", Map.<Character, String[]>ofEntries(
                entry(' ', new String[]{
                        "      ",
                        "      ",
                        "      ",
                        "      "
                }),
                entry('A', new String[]{
                        "▟▉█▉▙",
                        "█  █",
                        "█▀█",
                        "█  █"
                }),
                entry('B', new String[]{
                        "██▆▖",
                        "█▄▌▘",
                        "█▀▌▖",
                        "██🮅▘"
                }),
                entry('C', new String[]{
                        "▟▉█▉▙",
                        "█  ▀",
                        "█  ▄",
                        "▜▉█▉▛"
                }),
                entry('D', new String[]{
                        "██▆▖",
                        "█  █",
                        "█  █",
                        "██🮅▘"
                }),
                entry('E', new String[]{
                        "███",
                        "█▄  ",
                        "█▀  ",
                        "███"
                }),
                entry('F', new String[]{
                        "███",
                        "█▄  ",
                        "█▀  ",
                        "█    "
                }),
                entry('G', new String[]{
                        "▟▉█▉▙",
                        "█  ▀",
                        "█ ▘▜▌",
                        "▜▉█▉▛"
                }),
                entry('H', new String[]{
                        "█  █",
                        "█▄█",
                        "█▀█",
                        "█  █"
                }),
                entry('I', new String[]{
                        "███",
                        "  █  ",
                        "  █  ",
                        "███"
                }),
                entry('J', new String[]{
                        "    █",
                        "    █",
                        "▄  █",
                        "▜▉█▉▛"
                }),
                entry('K', new String[]{
                        "█  ▉▉",
                        "█▄▌▘",
                        "█▀▌▖",
                        "█  ▉▉"
                }),
                entry('L', new String[]{
                        "█    ",
                        "█    ",
                        "█    ",
                        "███"
                }),
                entry('M', new String[]{
                        "▟▍▙,▟▍▙",
                        "█'▉'█",
                        "█  █",
                        "█  █"
                }),
                entry('N', new String[]{
                        "█, █",
                        "█▉,█",
                        "█'▉█",
                        "█ '█"
                }),
                entry('O', new String[]{
                        "▟▉█▉▙",
                        "█  █",
                        "█  █",
                        "▜▉█▉▛"
                }),
                entry('P', new String[]{
                        "██▆▖",
                        "█▄▌▋",
                        "█▀🮃🬀",
                        "█    "
                }),
                entry('Q', new String[]{
                        "▟▉█▉▙",
                        "█  █",
                        "█ ▜▊▛",
                        "▜▋▋▛▜▏▙"
                }),
                entry('R', new String[]{
                        "██▆▖",
                        "█▄▌▘",
                        "█▀▌▖",
                        "█  ▉▉"
                }),
                entry('S', new String[]{
                        "▗▆█▆▖",
                        "🬉▌▄🬏▔",
                        "▁🬁▀▌🬓",
                        "▝🮅█🮅▘"
                }),
                entry('T', new String[]{
                        "███",
                        "  █  ",
                        "  █  ",
                        "  █  "
                }),
                entry('U', new String[]{
                        "█  █",
                        "█  █",
                        "█  █",
                        "▜▉█▉▛"
                }),
                entry('V', new String[]{
                        "█  █",
                        "█  █",
                        "▜▋▙ܼ▟▋▛",
                        " '▘█▘' "
                }),
                entry('W', new String[]{
                        "█  █",
                        "█  █",
                        "█,▉,█",
                        "▜▍▛'▜▍▛"
                }),
                entry('X', new String[]{
                        "█  █",
                        "'▜▏▙▟▏▛'",
                        ",▟▏▛▜▏▙,",
                        "█  █"
                }),
                entry('Y', new String[]{
                        "█  █",
                        "▜▋▙ܼ▟▋▛",
                        " '▘█▘' ",
                        "  █  "
                }),
                entry('Z', new String[]{
                        "██▉🬝",
                        "  ,▟▛ ",
                        " ▟▛'  ",
                        "🬻▉██"
                }),
                entry('0', new String[]{
                        "▟▉█▉▙",
                        "█ ▟█",
                        "█▛ █",
                        "▜▉█▉▛"
                }),
                entry('1', new String[]{
                        "🬻▉█  ",
                        "  █  ",
                        "  █  ",
                        "███"
                }),
                entry('2', new String[]{
                        "▗▆█▆▖",
                        "▔🬏▄▌🬉",
                        "🬓▌▀🬁 ",
                        "█▋▏▋█"
                }),
                entry('3', new String[]{
                        "▗▆█▆▖",
                        "▔🬏▄▌🬉",
                        "▁🬁▀▌🬓",
                        "▝🮅█🮅▘"
                }),
                entry('4', new String[]{
                        " █ █",
                        "█▄█",
                        "▀▀█",
                        "    █"
                }),
                entry('5', new String[]{
                        "█▋▋█",
                        "▉▉▄🬏 ",
                        "'▘▘▀▌🬓",
                        "▝🮅█🮅▘"
                }),
                entry('6', new String[]{
                        "▗▆█▆▖",
                        "▉▉▄🬏▔",
                        "▉▉▀▌🬓",
                        "▝🮅█🮅▘"
                }),
                entry('7', new String[]{
                        "██▉🬝",
                        "  ,▟▛ ",
                        " ▟▛'  ",
                        " █   "
                }),
                entry('8', new String[]{
                        "▗▆█▆▖",
                        "🬉▌▄▌🬉",
                        "🬓▌▀▌🬓",
                        "▝🮅█🮅▘"
                }),
                entry('9', new String[]{
                        "▗▆█▆▖",
                        "🬉▌▄▉▉",
                        "▁🬁▀▉▉",
                        "▝🮅█🮅▘"
                }),
                entry('?', new String[]{
                        "▗▆█▆▖",
                        "'' ▄▌🬉",
                        "  ▀  ",
                        "  █  "
                }),
                entry('!', new String[]{
                        "█",
                        "█",
                        "▀",
                        "█"
                }),
                entry('-', new String[]{
                        "      ",
                        "▄▄▄",
                        "▀▀▀",
                        "      "
                }),
                entry('_', new String[]{
                        "      ",
                        "      ",
                        "      ",
                        "███"
                }),
                entry('=', new String[]{
                        "▄▄▄",
                        "▀▀▀",
                        "▄▄▄",
                        "▀▀▀"
                }),
                entry('+', new String[]{
                        "  ▄  ",
                        "▄█▄",
                        "▀█▀",
                        "  ▀  "
                }),
                entry('*', new String[]{
                        "'▜▏▙▟▏▛'",
                        ",▟▏▛▜▏▙,",
                        "      ",
                        "      "
                }),
                entry('/', new String[]{
                        "   ,▟🬝",
                        "  ,▟▛ ",
                        " ▟▛'  ",
                        "▟▛'   "
                }),
                entry('\\', new String[]{
                        "▜▙,   ",
                        " ▜▙,  ",
                        "  '▜▙ ",
                        "   '▜▙"
                }),
                entry('[', new String[]{
                        "██",
                        "█  ",
                        "█  ",
                        "██"
                }),
                entry(']', new String[]{
                        "██",
                        "  █",
                        "  █",
                        "██"
                }),
                entry('$', new String[]{
                        "▗▆ ▎ ▆▖",
                        "🬉▌ ▎ 🬏▔",
                        "▁🬁 ▎ ▌🬓",
                        "▝🮅 ▎ 🮅▘"
                }),
                entry('(', new String[]{
                        "▟▉▛",
                        "█ ",
                        "█ ",
                        "▜▉▙"
                }),
                entry(')', new String[]{
                        "▜▉▙",
                        " █",
                        " █",
                        "▟▉▛"
                }),
                entry('^', new String[]{
                        " ,▖█▖, ",
                        "▟▋▛ܼ▜▋▙",
                        "      ",
                        "      "
                }),
                entry(':', new String[]{
                        "▄",
                        "▀",
                        "▄",
                        "▀"
                }),
                entry('.', new String[]{
                        "  ",
                        "  ",
                        "  ",
                        "█"
                }),
                entry('|', new String[]{
                        "█",
                        "█",
                        "█",
                        "█"
                }),
                entry(',', new String[]{
                        "  ",
                        "  ",
                        "▄",
                        "\uD83E\uDF0F▋\uD83E\uDF04"
                }),
                entry(';', new String[]{
                        "  ",
                        "█",
                        "▄",
                        "\uD83E\uDF0F▋\uD83E\uDF04"
                }),
                entry('\'', new String[]{
                        "█",
                        "▀",
                        "  ",
                        "  "
                }),
                entry('"', new String[]{
                        "██",
                        "▀▀",
                        "    ",
                        "    "
                }),
                entry('`', new String[]{
                        "▜▙ ",
                        " ▀",
                        "   ",
                        "   "
                }),
                entry('<', new String[]{
                        "  \uD83E\uDF0F\uD83E\uDF13▋▋\uD83E\uDF04\uD83E\uDF00",
                        "\uD83E\uDF0F\uD83E\uDF13▋▋\uD83E\uDF04\uD83E\uDF00  ",
                        "\uD83E\uDF00\uD83E\uDF04▋▋\uD83E\uDF13\uD83E\uDF0F  ",
                        "  \uD83E\uDF00\uD83E\uDF04▋▋\uD83E\uDF13\uD83E\uDF0F"
                }),
                entry('>', new String[]{
                        "\uD83E\uDF00\uD83E\uDF04▋▋\uD83E\uDF13\uD83E\uDF0F  ",
                        "  \uD83E\uDF00\uD83E\uDF04▋▋\uD83E\uDF13\uD83E\uDF0F",
                        "  \uD83E\uDF0F\uD83E\uDF13▋▋\uD83E\uDF04\uD83E\uDF00",
                        "\uD83E\uDF0F\uD83E\uDF13▋▋\uD83E\uDF04\uD83E\uDF00  "
                }),
                entry('{', new String[]{
                        "▟▉▛",
                        "\uD83E\uDF37▉ ",
                        "\uD83E\uDF28▉ ",
                        "▜▉▙"
                }),
                entry('}', new String[]{
                        "▜▉▙",
                        " ▉\uD83E\uDF32",
                        " ▉\uD83E\uDF15",
                        "▟▉▛"
                }),
                entry('~', new String[]{
                        "      ",
                        "\uD83E\uDF0F▄\uD83E\uDF0F,,,\uD83E\uDF0F▖",
                        "▘\uD83E\uDF01'''\uD83E\uDF01▀\uD83E\uDF01",
                        "      "
                }),
                entry('#', new String[]{
                        " \uD83E\uDF13\uD83E\uDF13 \uD83E\uDF13\uD83E\uDF13 ",
                        "\uD83E\uDF0E▋▋\uD83E\uDF0E▋▋\uD83E\uDF0E",
                        "\uD83E\uDF39▋▋\uD83E\uDF39▋▋\uD83E\uDF39",
                        " \uD83E\uDF04\uD83E\uDF04 \uD83E\uDF04\uD83E\uDF04 "
                }),
                entry('%', new String[]{
                        "\uD83E\uDF14\uD83E\uDF27 ,▟\uD83E\uDF1D",
                        "\uD83E\uDF08\uD83E\uDF05,▟▛ ",
                        " ▟▛'\uD83E\uDF16\uD83E\uDF22",
                        "▟▛' \uD83E\uDF23\uD83E\uDF18"
                }),
                entry('@', new String[]{
                        "▟▉█▉▙",
                        "▉ ▄ ▉",
                        "▉ ▜▄▉",
                        "▜▙▄  "
                }),
                entry('&', new String[]{
                        "▟▘▘▙  ",
                        "▜▖▖▛ ▟",
                        "▟▛▜▌▛",
                        "▜▙▟▜▙"
                })
        ));
    }
}
