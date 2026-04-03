//? if >= 1.21.4 {
package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import java.util.Map;

import static java.util.Map.entry;

public class HappyGhastFont implements FontSupplier {
    @Override
    public FontFile get() {
        return new FontFile("Happy Ghast", "chililisoup", 2, "", Map.<Character, String[]>ofEntries(
                entry(' ', new String[]{
                        "   ",
                        "   ",
                }),
                entry('A', new String[]{
                        "𜷥◚𜶚",
                        "▌🮂𜶘"
                }),
                entry('B', new String[]{
                        "▌◚𜶚",
                        "▌𜶮𜵚"
                }),
                entry('C', new String[]{
                        "𜷥◚𜴤",
                        "𜶫▂𜵙"
                }),
                entry('D', new String[]{
                        "▌◚𜶚",
                        "▌▂𜵛"
                }),
                entry('E', new String[]{
                        "▌◚◚",
                        "▌𜶮▂"
                }),
                entry('F', new String[]{
                        "▌◚◚",
                        "▌🮂 "
                }),
                entry('G', new String[]{
                        "𜷥◚𜴤",
                        "𜶫▂𜵜"
                }),
                entry('H', new String[]{
                        "▌ܼ ܼ❙",
                        "▌🮂𜶘"
                }),
                entry('I', new String[]{
                        "◚▜◚",
                        "▂𜷕▂"
                }),
                entry('J', new String[]{
                        "◚◚▜",
                        "𜶨▂𜵛"
                }),
                entry('K', new String[]{
                        "▌ 𜵛",
                        "▌🮂𜶓"
                }),
                entry('L', new String[]{
                        "▌  ",
                        "▌▂▂"
                }),
                entry('M', new String[]{
                        "𜷥𜶚𜶚",
                        "▌ܼܼ𜺨ܼܼ❙"
                }),
                entry('N', new String[]{
                        "▌𜶄ܼܼ❙",
                        "▌᤺𜺨𜶪"
                }),
                entry('O', new String[]{
                        "𜷥◚𜶚",
                        "𜶫▂𜵛"
                }),
                entry('P', new String[]{
                        "▌◚𜶚",
                        "▌🮂𜺨᤺"
                }),
                entry('Q', new String[]{
                        "𜷥◚𜶚",
                        "𜶫𜶳𜶊"
                }),
                entry('R', new String[]{
                        "▌◚𜶚",
                        "▌🮂𜶗"
                }),
                entry('S', new String[]{
                        "𜷥◚𜴤",
                        "𜶞𜶮𜵚"
                }),
                entry('T', new String[]{
                        "◚▜◚",
                        " ܼܼ❙ "
                }),
                entry('U', new String[]{
                        "▌ ᤺❙",
                        "𜶫▂𜵛"
                }),
                entry('V', new String[]{
                        "▌ ᤺❙",
                        "𜴅𜶀𜴒"
                }),
                entry('W', new String[]{
                        "▌ ᤺❙",
                        "𜶫𜵛𜵛"
                }),
                entry('X', new String[]{
                        "𜶫 𜵛",
                        "𜷡🮂𜶓"
                }),
                entry('Y', new String[]{
                        "𜶫 𜵛",
                        " 𜶘 "
                }),
                entry('Z', new String[]{
                        "◚𜵿𜴗",
                        "𜷡𜶬▂"
                }),
                entry('1', new String[]{
                        "𜴈▜ ",
                        "▂𜷕▂"
                }),
                entry('2', new String[]{
                        "𜴵◚𜵞",
                        "𜷡𜶮𜷋"
                }),
                entry('3', new String[]{
                        "𜴵◚𜶚",
                        "𜶜𜶭𜵚"
                }),
                entry('4', new String[]{
                        "𜷥🯤 ❙",
                        "🮂🮂𜶘"
                }),
                entry('5', new String[]{
                        "▌◚◚",
                        "𜶟𜶮𜵚"
                }),
                entry('6', new String[]{
                        "𜷥◚𜴤",
                        "𜶫𜶮𜵚"
                }),
                entry('7', new String[]{
                        "◚𜵿𜴗",
                        "𜷡𜺨᤺ "
                }),
                entry('8', new String[]{
                        "𜷥◚𜶚",
                        "𜶪𜶮𜵚"
                }),
                entry('9', new String[]{
                        "𜷥◚𜶚",
                        "𜶞𜶮𜴓"
                }),
                entry('0', new String[]{
                        "𜷥◚𜷘",
                        "𜶫𜶱𜵛"
                })
        ));
    }
}
//?}