//? if >= 1.21.4 {
package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

public class MinecraftSmallFont extends AbstractFontSupplier {
    @Override
    public FontFile get() {
        return new FontFile("Minecraft Small", "Mojang, chililisoup", 2, "", Map.<Character, String[]>ofEntries(
                entry(' ', new String[]{
                        "  ",
                        "  "
                }),
                entry('A', new String[]{
                        "𜵧𜴚𜶥",
                        "𜴍\uD836\uDE9D \uD836\uDE9D𜴍"
                }),
                entry('B', new String[]{
                        "𜵨𜴚𜶉",
                        "𜴬𜴘𜴒"
                }),
                entry('C', new String[]{
                        "𜵉𜺨𜴄",
                        "𜴝𜴘𜴐"
                }),
                entry('D', new String[]{
                        "𜵊𜺨𜶗",
                        "𜴬𜴘𜴒"
                }),
                entry('E', new String[]{
                        "𜵨𜴚🮂",
                        "𜴬𜴘𜴧"
                }),
                entry('F', new String[]{
                        "𜵨𜴚🮂",
                        "𜴍  "
                }),
                entry('G', new String[]{
                        "𜵉𜺨𜶟",
                        "𜴝𜴘𜴒"
                }),
                entry('H', new String[]{
                        "𜵦𜴉𜶦",
                        "𜴍\uD836\uDE9D \uD836\uDE9D𜴍"
                }),
                entry('I', new String[]{
                        "𜺨𜷴𜺨",
                        "𜴘𜴍𜴘"
                }),
                entry('J', new String[]{
                        "  𜷴",
                        "𜴜𜴘𜴒"
                }),
                entry('K', new String[]{
                        "𜵦𜴉𜴻",
                        "𜴍\uD836\uDE9D \uD836\uDE9D𜴍"
                }),
                entry('L', new String[]{
                        "𜷴  ",
                        "𜴬𜴘𜴧"
                }),
                entry('M', new String[]{
                        "𜵏𜴉𜶛",
                        "𜴍\uD836\uDE9D \uD836\uDE9D𜴍"
                }),
                entry('N', new String[]{
                        "𜵏𜴉𜷕",
                        "𜴍\uD836\uDE9D \uD836\uDE9D𜴍"
                }),
                entry('O', new String[]{
                        "𜵉𜺨𜶗",
                        "𜴝𜴘𜴒"
                }),
                entry('P', new String[]{
                        "𜵨𜴚𜴑",
                        "𜴍  "
                }),
                entry('Q', new String[]{
                        "𜵉𜺨𜶗",
                        "𜴝𜴘𜴞"
                }),
                entry('R', new String[]{
                        "𜵨𜴚𜶉",
                        "𜴍\uD836\uDE9D \uD836\uDE9D𜴍"
                }),
                entry('S', new String[]{
                        "𜴞𜴚𜶃",
                        "𜴜𜴘𜴒"
                }),
                entry('T', new String[]{
                        "🮂𜷴🮂",
                        " 𜴍 "
                }),
                entry('U', new String[]{
                        "𜷴ܼ ܼ𜷴",
                        "𜴝𜴘𜴒"
                }),
                entry('V', new String[]{
                        "𜷴ܼ ܼ𜷴",
                        "᤺🯤𜴘🯤᤺"
                }),
                entry('W', new String[]{
                        "𜷴ܼ ܼ𜷴",
                        "𜴕𜺨𜴥"
                }),
                entry('X', new String[]{
                        "𜵹𜴉𜴻",
                        "𜴍\uD836\uDE9D \uD836\uDE9D𜴍"
                }),
                entry('Y', new String[]{
                        "𜴄🯥𜴁",
                        " 𜴍 "
                }),
                entry('Z', new String[]{
                        "🮂𜴶𜴓",
                        "𜴭𜴘𜴧"
                }),
                entry('a', new String[]{
                        "ܼܼ𜴉𜴉𜶀",
                        "𜴞𜴚𜴲"
                }),
                entry('b', new String[]{
                        "𜷀𜴉𜶀",
                        "𜴬𜴘𜴒"
                }),
                entry('c', new String[]{
                        "𜵑𜴉𜶀",
                        "𜴝𜴘𜴐"
                }),
                entry('d', new String[]{
                        "𜵑𜴉𜷕",
                        "𜴝𜴘𜴱"
                }),
                entry('e', new String[]{
                        "𜵑𜴉𜶀",
                        "𜴟𜴚𜴪"
                }),
                entry('f', new String[]{
                        "𜶤𜴪",
                        "᤺𜴍 "
                }),
                entry('g', new String[]{
                        "𜵑𜴉𜶜",
                        "𜶴𜴹𜵟"
                }),
                entry('h', new String[]{
                        "𜷀𜴉𜶀",
                        "𜴍\uD836\uDE9D \uD836\uDE9D𜴍"
                }),
                entry('i', new String[]{
                        "𜵅",
                        "𜴍"
                }),
                entry('j', new String[]{
                        "  𜵅",
                        "𜶄𜺠𜵛"
                }),
                entry('k', new String[]{
                        "𜷴ܼܼ𜵑",
                        "𜴏𜴜"
                }),
                entry('l', new String[]{
                        "𜷴᤺",
                        "𜴝"
                }),
                entry('m', new String[]{
                        "𜵡𜺠𜶀",
                        "𜴍᤺𜺨᤺𜴍"
                }),
                entry('n', new String[]{
                        "𜵡𜴘𜶀",
                        "𜴍\uD836\uDE9D \uD836\uDE9D𜴍"
                }),
                entry('o', new String[]{
                        "𜵑𜴉𜶀",
                        "𜴝𜴘𜴒"
                }),
                entry('p', new String[]{
                        "𜶻𜴉𜶀",
                        "𜵏𜴀𜴁"
                }),
                entry('q', new String[]{
                        "𜵑𜴉𜷋",
                        "𜴄𜴀𜶛"
                }),
                entry('r', new String[]{
                        "𜶻𜴉𜶀",
                        "𜴍  "
                }),
                entry('s', new String[]{
                        "𜵑𜴉𜴧",
                        "𜴩𜴚𜴑"
                }),
                entry('t', new String[]{
                        "𜴉𜷴𜴉",
                        "ܼ᤺𜴍𜺠"
                }),
                entry('u', new String[]{
                        "🯥ܼ ܼ🯥",
                        "𜴝𜴘𜴱"
                }),
                entry('v', new String[]{
                        "🯥ܼ ܼ🯥",
                        "𜴄𜴘𜴁"
                }),
                entry('w', new String[]{
                        "🯥ܼ ܼ🯥",
                        "𜴝𜴍𜴱"
                }),
                entry('x', new String[]{
                        "𜶀ܼܼܼ𜵑",
                        "𜴐𜺨𜴜"
                }),
                entry('y', new String[]{
                        "🯥ܼ ܼ🯥",
                        "𜶴𜴹𜵟"
                }),
                entry('z', new String[]{
                        "𜴧𜴘𜵡",
                        "𜴯𜴚𜴧"
                }),
                entry('`', new String[]{
                        "𜴄",
                        " "
                }),
                entry('1', new String[]{
                        "\uD834\uDD6D\uD834\uDD6D𜴀𜷴 ",
                        "𜴧𜴍𜴧"
                }),
                entry('2', new String[]{
                        "𜴁𜴶𜵚",
                        "𜴭𜴘𜴯"
                }),
                entry('3', new String[]{
                        "𜴁𜴶𜵚",
                        "𜴜𜴘𜴒"
                }),
                entry('4', new String[]{
                        "𜵑𜴀𜶘",
                        "🮂𜺨𜴢"
                }),
                entry('5', new String[]{
                        "𜴮𜴚𜶃",
                        "𜴜𜴘𜴒"
                }),
                entry('6', new String[]{
                        "𜷃𜴶𜴶ܼܼ",
                        "𜴝𜴘𜴒"
                }),
                entry('7', new String[]{
                        "𜴂𜺨𜵜",
                        " 𜴍 "
                }),
                entry('8', new String[]{
                        "𜶆𜴶𜵚",
                        "𜴝𜴘𜴒"
                }),
                entry('9', new String[]{
                        "𜶆𜴶𜷔",
                        "\uD834\uDD6D\uD834\uDD6D𜴘𜴘𜴁"
                }),
                entry('0', new String[]{
                        "𜵉𜴶𜶥",
                        "𜴟𜴘𜴒"
                }),
                entry('-', new String[]{
                        "▂𜺠▂",
                        "   "
                }),
                entry('=', new String[]{
                        "𜴧𜴘𜴧",
                        "𜴆𜴀𜴆"
                }),
                entry('[', new String[]{
                        "𜵊𜺨",
                        "𜴬𜴘"
                }),
                entry(']', new String[]{
                        "𜺨𜶘",
                        "𜴘𜴱"
                }),
                entry(',', new String[]{
                        " ",
                        "▆"
                }),
                entry('.', new String[]{
                        " ",
                        "𜴳"
                }),
                entry('/', new String[]{
                        " 𜺠𜴎",
                        "𜴒  "
                }),
                entry('\\', new String[]{
                        "𜴠𜺠 ",
                        "  𜴝"
                }),
                entry(';', new String[]{
                        "𜴳",
                        "▆"
                }),
                entry('\'', new String[]{
                        "🮅",
                        " "
                }),
                entry('~', new String[]{
                        "𜴁𜴄𜴁",
                        "   "
                }),
                entry('!', new String[]{
                        "𜷴",
                        "𜴀"
                }),
                entry('@', new String[]{
                        "𜵋𜶷𜶓",
                        "𜶅𜶸𜵃"
                }),
                entry('#', new String[]{
                        "𜶦𜴉𜵦",
                        "𜴢𜺨𜴏"
                }),
                entry('$', new String[]{
                        "𜶈𜴺𜵁",
                        "𜴆🯦𜴁"
                }),
                entry('%', new String[]{
                        "🯤ܼܼ𜺠𜴎",
                        "𜴒 🯦"
                }),
                entry('^', new String[]{
                        "𜴐𜺨𜴜",
                        "   "
                }),
                entry('&', new String[]{
                        "ܼܼ𜴹𜵅𜵴",
                        "𜴝𜴊𜴝"
                }),
                entry('*', new String[]{
                        "𜴊𜴀𜴊",
                        "  "
                }),
                entry('(', new String[]{
                        "𜵋𜺨",
                        "𜴄𜴉"
                }),
                entry(')', new String[]{
                        "𜺨𜶓",
                        "𜴉𜴁"
                }),
                entry('_', new String[]{
                        "   ",
                        "▂𜺠▂"
                }),
                entry('+', new String[]{
                        "▂𜵈▂",
                        " 🯤 "
                }),
                entry('{', new String[]{
                        "𜵙𜺨",
                        "᤺🯤𜴘"
                }),
                entry('}', new String[]{
                        "𜺨𜶄",
                        "𜴘🯤᤺"
                }),
                entry('<', new String[]{
                        "𜵑𜴁",
                        "᤺𜺨𜴜"
                }),
                entry('>', new String[]{
                        "𜴄𜶀",
                        "𜴐𜺨᤺"
                }),
                entry('?', new String[]{
                        "𜴁𜺨𜵚",
                        " 𜴊 "
                }),
                entry('|', new String[]{
                        "𜷴",
                        "𜴍"
                }),
                entry(':', new String[]{
                        "𜴳",
                        "𜴳"
                }),
                entry('"', new String[]{
                        "🮅🮅",
                        "  "
                })
        ));
    }

    @Override
    public Map<Character, Set<PatchCharacter>> patches() {
        HashMap<Character, Set<PatchCharacter>> baseMap = new HashMap<>(Map.ofEntries(
                entry('m', Set.of(
                        PatchCharacter.of(
                                "▂ܼܼܼ𜺠ܼܼ",
                                "𜴍᤺𜺨᤺𜴍"
                        )
                )),
                entry('n', Set.of(
                        PatchCharacter.of(
                                "▂𜺠𜺠ܼܼ",
                                "𜴍\uD836\uDE9D \uD836\uDE9D𜴍"
                        )
                ))
        ));
        
        this.appendMediumVerticalBarAnnihilator(baseMap);
        return baseMap;
    }
}
//?}