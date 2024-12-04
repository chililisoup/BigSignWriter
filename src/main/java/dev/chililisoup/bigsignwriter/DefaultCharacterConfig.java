package dev.chililisoup.bigsignwriter;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultCharacterConfig {
    public static final Map<Character, String[]> DEFAULT_CHARACTERS = new LinkedHashMap<>();

    private static void addChar(char baseChar, String... bigChar) {
        DEFAULT_CHARACTERS.put(baseChar, bigChar);
    }

    static {
        addChar(' ',
                "      ",
                "      ",
                "      ",
                "      "
        );
        addChar('A',
                "▟▉█▉▙",
                "█  █",
                "█▀█",
                "█  █"
        );
        addChar('B',
                "██▆▖",
                "█▄▌▘",
                "█▀▌▖",
                "██\uD83E\uDF85▘"
        );
        addChar('C',
                "▟▉█▉▙",
                "█  ▀",
                "█  ▄",
                "▜▉█▉▛"
        );
        addChar('D',
                "██▆▖",
                "█  █",
                "█  █",
                "██\uD83E\uDF85▘"
        );
        addChar('E',
                "███",
                "█▄  ",
                "█▀  ",
                "███"
        );
        addChar('F',
                "███",
                "█▄  ",
                "█▀  ",
                "█    "
        );
        addChar('G',
                "▟▉█▉▙",
                "█  ▀",
                "█ ▘▜▌",
                "▜▉█▉▛" // "▜▋█▛▜" alt style
        );
        addChar('H',
                "█  █",
                "█▄█",
                "█▀█",
                "█  █"
        );
        addChar('I',
                "███",
                "  █  ",
                "  █  ",
                "███"
        );
        addChar('J',
                "    █",
                "    █",
                "▄  █",
                "▜▉█▉▛"
        );
        addChar('K',
                "█  ▉▉",
                "█▄▌▘",
                "█▀▌▖",
                "█  ▉▉"
        );
        addChar('L',
                "█    ",
                "█    ",
                "█    ",
                "███"
        );
        addChar('M',
                "▟▍▙,▟▍▙",
                "█'▉'█",
                "█  █",
                "█  █"
        );
        addChar('N',
                "█, █",
                "█▉,█",
                "█'▉█",
                "█ '█"
        );
        addChar('O',
                "▟▉█▉▙",
                "█  █",
                "█  █",
                "▜▉█▉▛"
        );
        addChar('P',
                "██▆▖",
                "█▄▌▋",
                "█▀\uD83E\uDF83\uD83E\uDF00",
                "█    "
        );
        addChar('Q',
                "▟▉█▉▙",
                "█  █",
                "█ ▜▊▛",
                "▜▋▋▛▜▏▙"
        );
        addChar('R',
                "██▆▖",
                "█▄▌▘",
                "█▀▌▖",
                "█  ▉▉"
        );
        addChar('S',
                "▗▆█▆▖",
                "\uD83E\uDF09▌▄\uD83E\uDF0F▔",
                "▁\uD83E\uDF01▀▌\uD83E\uDF13",
                "▝\uD83E\uDF85█\uD83E\uDF85▘"
        );
        addChar('T',
                "███",
                "  █  ",
                "  █  ",
                "  █  "
        );
        addChar('U',
                "█  █",
                "█  █",
                "█  █",
                "▜▉█▉▛"
        );
        addChar('V',
                "█  █",
                "█  █",
                "▜▋▙ܼ▟▋▛",
                " '▘█▘' "
        );
        addChar('W',
                "█  █",
                "█  █",
                "█,▉,█",
                "▜▍▛'▜▍▛"
        );
        addChar('X',
                "█  █",
                "'▜▏▙▟▏▛'",
                ",▟▏▛▜▏▙,",
                "█  █"
        );
        addChar('Y',
                "█  █",
                "▜▋▙ܼ▟▋▛",
                " '▘█▘' ",
                "  █  "
        );
        addChar('Z',
                "██▉\uD83E\uDF1D",
                "  ,▟▛ ",
                " ▟▛'  ",
                "\uD83E\uDF3B▉██"
        );
        addChar('0',
                "▟▉█▉▙",
                "█ ▟█",
                "█▛ █",
                "▜▉█▉▛"
        );
        addChar('1',
                "\uD83E\uDF3B▉█  ",
                "  █  ",
                "  █  ",
                "███"
        );
        addChar('2',
                "▗▆█▆▖",
                "▔\uD83E\uDF0F▄▌\uD83E\uDF09",
                "\uD83E\uDF13▌▀\uD83E\uDF01 ",
                "█▋▋█"
        );
        addChar('3',
                "▗▆█▆▖",
                "▔\uD83E\uDF0F▄▌\uD83E\uDF09",
                "▁\uD83E\uDF01▀▌\uD83E\uDF13",
                "▝\uD83E\uDF85█\uD83E\uDF85▘"
        );
        addChar('4',
                " █ █",
                "█▄█",
                "▀▀█",
                "    █"
        );
        addChar('5',
                "█▋▋█",
                "▉▉▄\uD83E\uDF0F ",
                "'▘▘▀▌\uD83E\uDF13",
                "▝\uD83E\uDF85█\uD83E\uDF85▘"
        );
        addChar('6',
                "▗▆█▆▖",
                "▉▉▄\uD83E\uDF0F▔",
                "▉▉▀▌\uD83E\uDF13",
                "▝\uD83E\uDF85█\uD83E\uDF85▘"
        );
        addChar('7',
                "██▉\uD83E\uDF1D",
                "  ,▟▛ ",
                " ▟▛'  ",
                " █   "
        );
        addChar('8',
                "▗▆█▆▖",
                "\uD83E\uDF09▌▄▌\uD83E\uDF09",
                "\uD83E\uDF13▌▀▌\uD83E\uDF13",
                "▝\uD83E\uDF85█\uD83E\uDF85▘"
        );
        addChar('9',
                "▗▆█▆▖",
                "\uD83E\uDF09▌▄▉▉",
                "▁\uD83E\uDF01▀▉▉",
                "▝\uD83E\uDF85█\uD83E\uDF85▘"
        );
        addChar('?',
                "▗▆█▆▖",
                "'' ▄▌\uD83E\uDF09",
                "  ▀  ",
                "  █  "
        );
        addChar('!',
                "█",
                "█",
                "▀",
                "█"
        );
        addChar('-',
                "      ",
                "▄▄▄",
                "▀▀▀",
                "      "
        );
        addChar('_',
                "",
                "",
                "",
                ""
        );
        addChar('=',
                "▄▄▄",
                "▀▀▀",
                "▄▄▄",
                "▀▀▀"
        );
        addChar('+',
                "  ▄  ",
                "▄█▄",
                "▀█▀",
                "  ▀  "
        );
        addChar('*',
                "'▜▏▙▟▏▛'",
                ",▟▏▛▜▏▙,",
                "      ",
                "      "
        );
        addChar('/',
                "   ,▟\uD83E\uDF1D",
                "  ,▟▛ ",
                " ▟▛'  ",
                "▟▛'   "
        );
        addChar('\\',
                "▜▙,   ",
                " ▜▙,  ",
                "  '▜▙ ",
                "   '▜▙"
        );
        addChar('[',
                "██",
                "█  ",
                "█  ",
                "██"
        );
        addChar(']',
                "██",
                "  █",
                "  █",
                "██"
        );
        addChar('$',
                "▗▆ ▎ ▆▖",
                "\uD83E\uDF09▌ ▎ \uD83E\uDF0F▔",
                "▁\uD83E\uDF01 ▎ ▌\uD83E\uDF13",
                "▝\uD83E\uDF85 ▎ \uD83E\uDF85▘"
        );
    }
}
