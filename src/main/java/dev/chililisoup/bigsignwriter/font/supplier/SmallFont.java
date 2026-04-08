package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

public class SmallFont extends AbstractFontSupplier {
    private static final String VERT_3PX =
            //? if >= 1.21.4 {
            "𜷴";
            //?} else
            //"❙";
    
    @Override
    public FontFile get() {
        return new FontFile("Small", "chililisoup", 2, "", Map.<Character, String[]>ofEntries(
                entry(' ', new String[]{
                        "  ",
                        "  "
                }),
                entry('A', new String[]{
                        "🬳🬶",
                        VERT_3PX + " " + VERT_3PX
                }),
                entry('a', new String[]{
                        "🬋🬢",
                        "🬤🬸"
                }),
                entry('B', new String[]{
                        "🬴🬗",
                        "🬲🬘"
                }),
                entry('b', new String[]{
                        "🬲🬏ܼܼ",
                        "🬲🬘"
                }),
                entry('C', new String[]{
                        "🬔🬈",
                        "🬣🬖"
                }),
                entry('c', new String[]{
                        "🬖🬢",
                        "🬣🬖"
                }),
                entry('D', new String[]{
                        "🬕🬧",
                        "🬲🬘"
                }),
                entry('d', new String[]{
                        "ܼܼ🬞🬷",
                        "🬣🬷"
                }),
                entry('E', new String[]{
                        "🬴🬒",
                        "🬲🬭"
                }),
                entry('e', new String[]{
                        "🬖🬢",
                        "🬥🬰"
                }),
                entry('F', new String[]{
                        "🬴🬒",
                        //? if >= 1.21.4 {
                        "𜷴᤺ "
                        //?} else
                        //"▍  "
                }),
                entry('f', new String[]{
                        "🬵🬰",
                        "᤺" + VERT_3PX + " "
                }),
                entry('G', new String[]{
                        "🬔🬈",
                        "🬣🬙"
                }),
                entry('g', new String[]{
                        "🬖🬩",
                        "🬯🬙"
                }),
                entry('H', new String[]{
                        "🬲🬷",
                        VERT_3PX + " " + VERT_3PX
                }),
                entry('h', new String[]{
                        "🬲🬏ܼܼ",
                        VERT_3PX + " " + VERT_3PX
                }),
                entry('I', new String[]{
                        "🬨🬂",
                        "🬷🬭"
                }),
                entry('i', new String[]{
                        "🬯 ",
                        "🬷🬭"
                }),
                entry('J', new String[]{
                        //? if >= 1.21.4 {
                        " ᤺𜷴",
                        //?} else
                        //"  ▍",
                        "🬢🬘"
                }),
                entry('j', new String[]{
                        " 🬯",
                        "🬢🬘"
                }),
                entry('K', new String[]{
                        "🬲🬅",
                        VERT_3PX + "᤺🬧"
                }),
                entry('k', new String[]{
                        VERT_3PX + "ܼܼ🬖",
                        "🬕🬢"
                }),
                entry('L', new String[]{
                        //? if >= 1.21.4 {
                        "𜷴᤺ ",
                        //?} else
                        //"▍  ",
                        "🬲🬭"
                }),
                entry('l', new String[]{
                        "🬨 ",
                        "ܼܼ🬉🬭"
                }),
                entry('M', new String[]{
                        "🬺🬻",
                        VERT_3PX + " " + VERT_3PX
                }),
                entry('m', new String[]{
                        "🬭🬭",
                        "🬕🬨"
                }),
                entry('N', new String[]{
                        "🬺ܼܼ" + VERT_3PX,
                        VERT_3PX + "᤺🬬"
                }),
                entry('n', new String[]{
                        "🬭🬏ܼܼ",
                        VERT_3PX + " " + VERT_3PX
                }),
                entry('O', new String[]{
                        "🬔🬧",
                        "🬣🬘"
                }),
                entry('o', new String[]{
                        "ܼܼ🬞🬏ܼܼ",
                        "🬣🬘"
                }),
                entry('P', new String[]{
                        "🬴🬗",
                        //? if >= 1.21.4 {
                        "𜷴᤺ "
                        //?} else
                        //"▍  "
                }),
                entry('p', new String[]{
                        "🬚🬢",
                        "🬕🬀᤺"
                }),
                entry('Q', new String[]{
                        "🬔🬧",
                        "🬣🬤"
                }),
                entry('q', new String[]{
                        "🬖🬩",
                        "᤺🬁🬨"
                }),
                entry('R', new String[]{
                        "🬴🬗",
                        VERT_3PX + " " + VERT_3PX
                }),
                entry('r', new String[]{
                        "🬭🬏ܼܼ",
                        VERT_3PX + " 🬁"
                }),
                entry('S', new String[]{
                        "🬤🬒",
                        "🬢🬘"
                }),
                entry('s', new String[]{
                        "🬖🬋",
                        "🬯🬗"
                }),
                entry('T', new String[]{
                        "🬨🬂",
                        "ܼܼ" + VERT_3PX + " "
                }),
                entry('t', new String[]{
                        "🬷🬭",
                        "᤺🬉🬭"
                }),
                entry('U', new String[]{
                        VERT_3PX + " " + VERT_3PX,
                        "🬣🬘"
                }),
                entry('u', new String[]{
                        "🬏 🬞",
                        "🬣🬷"
                }),
                entry('V', new String[]{
                        VERT_3PX + " " + VERT_3PX,
                        "🬣🬅"
                }),
                entry('v', new String[]{
                        "🬏 🬞",
                        "🬣🬅"
                }),
                entry('W', new String[]{
                        VERT_3PX + " " + VERT_3PX,
                        "🬝🬬"
                }),
                entry('w', new String[]{
                        "🬏 🬞",
                        "🬺🬻"
                }),
                entry('X', new String[]{
                        VERT_3PX + "ܼܼ🬘",
                        "🬔᤺" + VERT_3PX
                }),
                entry('x', new String[]{
                        "🬏 🬞",
                        "🬗🬧"
                }),
                entry('Y', new String[]{
                        VERT_3PX + "ܼܼ🬘",
                        "᤺" + VERT_3PX + " "
                }),
                entry('y', new String[]{
                        "🬓 🬦",
                        "🬯🬙"
                }),
                entry('Z', new String[]{
                        "🬂🬙",
                        "🬳🬭"
                }),
                entry('z', new String[]{
                        "🬭🬭",
                        "🬵🬮"
                }),
                entry('1', new String[]{
                        "🬫 ",
                        "🬷🬭"
                }),
                entry('2', new String[]{
                        "🬅🬗",
                        "🬳🬵"
                }),
                entry('3', new String[]{
                        "🬅🬗",
                        "🬢🬘"
                }),
                entry('4', new String[]{
                        "🬳🬷",
                        //? if >= 1.21.4 {
                        " ᤺𜷴"
                        //?} else
                        //"  ▍"
                }),
                entry('5', new String[]{
                        "🬴🬒",
                        "🬢🬘"
                }),
                entry('6', new String[]{
                        "🬳🬐ܼܼ",
                        "🬣🬘"
                }),
                entry('7', new String[]{
                        "🬆🬙",
                        "🬔 "
                }),
                entry('8', new String[]{
                        "🬤🬗",
                        "🬣🬘"
                }),
                entry('9', new String[]{
                        "🬤🬶",
                        "🬢🬘"
                }),
                entry('0', new String[]{
                        "🬔🬶",
                        "🬥🬘"
                }),
                entry('`', new String[]{
                        "🬈",
                        " "
                }),
                entry('~', new String[]{
                        "ܼܼ🬞ܼܼ🬞",
                        "🬀᤺🬀᤺"
                }),
                entry('!', new String[]{
                        VERT_3PX,
                        "🬃"
                }),
                entry('@', new String[]{
                        "🬔🬧",
                        "🬣🬮"
                }),
                entry('#', new String[]{
                        "🬫🬜",
                        "🬆🬆"
                }),
                entry('$', new String[]{
                        "🬤🬕",
                        "🬢🬝"
                }),
                entry('%', new String[]{
                        "🬃ܼܼ🬔",
                        "🬘᤺🬇"
                }),
                entry('^', new String[]{
                        "🬅🬈",
                        "  "
                }),
                entry('&', new String[]{
                        "🬤🬄ܼܼ",
                        "🬲🬥"
                }),
                entry('*', new String[]{
                        "🬗🬤",
                        "  "
                }),
                entry('(', new String[]{
                        "🬖🬂",
                        "🬈🬭"
                }),
                entry(')', new String[]{
                        "🬂🬢",
                        "🬭🬅"
                }),
                entry('-', new String[]{
                        "🬭🬭",
                        "  "
                }),
                entry('_', new String[]{
                        "  ",
                        "🬭🬭"
                }),
                entry('=', new String[]{
                        "🬋🬋",
                        "🬂🬂"
                }),
                entry('+', new String[]{
                        "🬫🬃",
                        "  "
                }),
                entry('[', new String[]{
                        "🬕🬂",
                        "🬲🬭"
                }),
                entry(']', new String[]{
                        "🬂🬨",
                        "🬭🬷"
                }),
                entry('{', new String[]{
                        "🬵🬂",
                        "🬊🬭"
                }),
                entry('}', new String[]{
                        "🬂🬱",
                        "🬭🬆"
                }),
                entry(',', new String[]{
                        " ",
                        "🬙"
                }),
                entry('.', new String[]{
                        " ",
                        "🬹"
                }),
                entry('<', new String[]{
                        "ܼܼ🬞🬅",
                        " 🬈"
                }),
                entry('>', new String[]{
                        "🬈🬞ܼܼ",
                        "🬅 "
                }),
                entry('/', new String[]{
                        " 🬔",
                        "🬘 "
                }),
                entry('\\', new String[]{
                        "🬧 ",
                        " 🬣"
                }),
                entry('?', new String[]{
                        "🬅🬗",
                        "ܼܼ🬠 "
                }),
                entry('|', new String[]{
                        VERT_3PX,
                        VERT_3PX
                }),
                entry(';', new String[]{
                        "🬎",
                        "🬙"
                }),
                entry(':', new String[]{
                        "🬎",
                        "🬎"
                }),
                entry('\'', new String[]{
                        "🬎",
                        " "
                }),
                entry('"', new String[]{
                        "🬎🬎",
                        "  "
                })
        ));
    }

    //? if >= 1.21.4 {
    @Override
    public Map<Character, Set<PatchCharacter>> patches() {
        HashMap<Character, Set<PatchCharacter>> baseMap = new HashMap<>(Map.ofEntries(
                entry('F', Set.of(
                        PatchCharacter.of(
                                "🬴🬒",
                                "▍  "
                        )
                )),
                entry('J', Set.of(
                        PatchCharacter.of(
                                "  ▍",
                                "🬢🬘"
                        )
                )),
                entry('L', Set.of(
                        PatchCharacter.of(
                                "▍  ",
                                "🬲🬭"
                        )
                )),
                entry('P', Set.of(
                        PatchCharacter.of(
                                "🬴🬗",
                                "▍  "
                        )
                )),
                entry('4', Set.of(
                        PatchCharacter.of(
                                "🬳🬷",
                                "  ▍"
                        )
                ))
        ));

        this.appendMediumVerticalBarAnnihilator(baseMap);
        return baseMap;
    }
    //?}
}
