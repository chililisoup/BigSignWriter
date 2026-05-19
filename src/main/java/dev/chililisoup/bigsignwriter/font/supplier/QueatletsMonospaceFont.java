/*
 * Queatlets Monospace (c) 2026 by adhill
 * Licensed under CC BY-NC 4.0.
 * To view a copy of this license, visit https://creativecommons.org/licenses/by-nc/4.0/
 */

//? if >= 1.21.4 {
package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import static java.util.Map.entry;

public class QueatletsMonospaceFont extends AbstractFontSupplier {
    @Override
    public FontFile get() {
        return new FontFile("Queatlets Monospace", "adhillisepic")
                .parentFont("queatlets")
                .characters(
                        entry('A', new String[]{
                                " ▟▀▙ ",
                                "▟▊▄▊▙",
                                "█  █",
                                "      "
                        }),
                        entry('C', new String[]{
                                "𜷡▊▀▊𜷞",
                                "█  𜺢▂",
                                "𜴦▊█▊𜴗",
                                "      "
                        }),
                        entry('E', new String[]{
                                "█▀▀",
                                "█▀▀",
                                "███",
                                "      "
                        }),
                        entry('F', new String[]{
                                "█▀▀",
                                "█▀▀",
                                "█    ",
                                "      "
                        }),
                        entry('I', new String[]{
                                " 🮅█🮅 ",
                                "  █  ",
                                " ▆█▆ ",
                                "      "
                        }),
                        entry('J', new String[]{
                                "  🮅█🮅",
                                "▂𜺢 █ ",
                                "𜴦𜷴█𜴗 ",
                                "      "
                        }),
                        entry('L', new String[]{
                                " █   ",
                                " █   ",
                                " ██▌",
                                "      "
                        }),
                        entry('S', new String[]{
                                "𜷡▛▀𜴍𜴫",
                                "𜴅▘▀▜𜷞",
                                "𜴦▊█▊𜴗",
                                "      "
                        }),
                        entry('T', new String[]{
                                "▀█▀",
                                "  █  ",
                                "  █  ",
                                "      "
                        }),
                        entry('a', new String[]{
                                "      ",
                                " 𜵾𜶮𜴶▊𜷤",
                                "𜶪▌▖𜵡█",
                                "      "
                        }),
                        entry('c', new String[]{
                                "      ",
                                "𜷡▌▀𜴍𜴫",
                                "𜴦▌▄𜵈𜴔",
                                "      "
                        }),
                        entry('e', new String[]{
                                "      ",
                                "𜷡▌𜴺𜶺▊𜷞",
                                "𜴦▌▄𜵈𜴔",
                                "      "
                        }),
                        entry('f', new String[]{
                                " 𜷡▊𜴍🮅𜴫",
                                "🮅█𜴍🮅 ",
                                " █   ",
                                "      "
                        }),
                        entry('i', new String[]{
                                "  ▀  ",
                                " 🮅█  ",
                                " 𜵈𜵈█𜵈 ",
                                "      "
                        }),
                        entry('j', new String[]{
                                "   ▀ ",
                                "  🮅█ ",
                                "   █ ",
                                "𜶨▄▊𜴗 "
                        }),
                        entry('l', new String[]{
                                " █   ",
                                " █   ",
                                " 𜴦▊𜵈▆𜴔",
                                "      "
                        }),
                        entry('s', new String[]{
                                "      ",
                                "𜶪▊𜴶𜶮𜶶𜵂",
                                "𜴣𜷞▄𜷴𜵰",
                                "      "
                        }),
                        entry('t', new String[]{
                                " ▄   ",
                                "🮅█🮅𜴍 ",
                                " 𜴦▊𜵈▆𜴔",
                                "      "
                        }),
                        entry('0', new String[]{
                                " 𜷡𜷴🮅𜶫𜷞",
                                " 𜷴▌ │▌",
                                " 𜴦𜷴▆𜷥𜴗",
                                "      "
                        }),
                        entry('1', new String[]{
                                "  𜴯█ ",
                                "   █ ",
                                "  ▆█▆",
                                "      "
                        }),
                        entry('2', new String[]{
                                " 𜴯🮅𜶫𜷴𜷞",
                                "  𜺣𜷡𜷴𜴍𜴂",
                                " 🬻▌𜷤𜵈▆",
                                "      "
                        }),
                        entry('3', new String[]{
                                " 𜴯🮅𜴍𜶫𜷞",
                                "  🯦𜴳𜶪𜷂",
                                " 𜴣▆𜵈𜷥𜴗",
                                "      "
                        }),
                        entry('4', new String[]{
                                "  𜷋𜷥𜷴 ",
                                " 𜷥𜷛▌▙▖",
                                "   █ ",
                                "      "
                        }),
                        entry('5', new String[]{
                                " ▌𜵰🮅𜴍🮅",
                                " 🮅🮅𜴍𜶫𜷞",
                                " 𜴣𜵈▆𜷥𜴗",
                                "      "
                        }),
                        entry('6', new String[]{
                                "  𜷡𜵰𜴂 ",
                                " ▟𜵰𜴳𜵈𜶻",
                                " 𜴦𜷤▆𜷴𜴗",
                                "      "
                        }),
                        entry('7', new String[]{
                                " 🮅𜴍🮅▌▛",
                                "   ▟▛ ",
                                "  ▟▛  ",
                                "      "
                        }),
                        entry('8', new String[]{
                                " 𜷡𜷴🮅𜶫𜷞",
                                " 𜷖𜵮𜴳𜷴𜷂",
                                " 𜴦𜷴▆𜷥𜴗",
                                "      "
                        }),
                        entry('9', new String[]{
                                " 𜷡𜷴🮅𜶫𜷞",
                                " 𜴅𜴍𜴳𜷥▛",
                                "  𜷋𜷥𜴏 ",
                                "      "
                        }),
                        entry(' ', new String[]{
                                "      ",
                                "      ",
                                "      ",
                                "      "
                        }),
                        entry('=', new String[]{
                                "      ",
                                " 🯦𜴳🯦𜴳🯦 ",
                                " 🯦𜴳🯦𜴳🯦 ",
                                "      "
                        }),
                        entry('^', new String[]{
                                "  ▟▙  ",
                                " 𜴈𜺩𜴅𜴇 ",
                                "      ",
                                "      "
                        }),
                        entry('{', new String[]{
                                "  𜷥𜷴𜴂 ",
                                " 𜷥▊𜴂  ",
                                " 𜴅▊𜷤  ",
                                "  𜴅▘𜴇 "
                        }),
                        entry('|', new String[]{
                                "  █  ",
                                "  █  ",
                                "  █  ",
                                "  ▀  "
                        }),
                        entry('}', new String[]{
                                " 𜴅𜷴𜷤  ",
                                "  𜴅▊𜷤 ",
                                "  𜷥▊𜴂 ",
                                " 𜴈▘𜴂  "
                        }),
                        entry('£', new String[]{
                                "  𜷡🮅𜴍𜴴",
                                " 𜴳▌𜴳🯦 ",
                                " 𜷡▌▆𜵈▆",
                                "      "
                        }),
                        entry('€', new String[]{
                                "  𜷡🮅𜴍𜴴",
                                " 𜴪▌𜴊𜴪 ",
                                "  𜴦▆𜵈𜵭",
                                "      "
                        }),
                        entry('?', new String[]{
                                " 𜴵𜴍𜶫𜷞 ",
                                "  𜷓▊𜴂 ",
                                "  ▄  ",
                                "      "
                        }),
                        entry('!', new String[]{
                                "  █  ",
                                "  █  ",
                                "  ▄  ",
                                "      "
                        }),
                        entry('+', new String[]{
                                "      ",
                                " ▂▊𜷀𜺣 ",
                                " 𜺨𜶘▊🮂 ",
                                "      "
                        }),
                        entry('-', new String[]{
                                "      ",
                                " ▂𜺢▂▂ ",
                                " 🮂𜺩🮂🮂 ",
                                "      "
                        }),
                        entry('*', new String[]{
                                "      ",
                                "  𜶜▆𜵡 ",
                                "  𜴈🮅𜴇 ",
                                "      "
                        }),
                        entry('$', new String[]{
                                " 𜷋𜵈𜶪𜶨𜶻",
                                " 𜴅𜴴𜵮𜵈𜶻",
                                " 𜴅𜴍𜶪𜴵𜴂",
                                "      "
                        }),
                        entry('\\', new String[]{
                                " ▜▙   ",
                                "  ▜▙  ",
                                "   ▜▙ ",
                                "      "
                        }),
                        entry('/', new String[]{
                                "   ▟▛ ",
                                "  ▟▛  ",
                                " ▟▛   ",
                                "      "
                        }),
                        entry('[', new String[]{
                                " █▀ ",
                                " █   ",
                                " █   ",
                                " ▀▀ "
                        }),
                        entry(']', new String[]{
                                " ▀█ ",
                                "   █ ",
                                "   █ ",
                                " ▀▀ "
                        }),
                        entry(':', new String[]{
                                "      ",
                                "  ▀  ",
                                "  ▄  ",
                                "      "
                        }),
                        entry(';', new String[]{
                                "      ",
                                "  ▀  ",
                                "  ▄  ",
                                "  𜴀▘𜺨  "
                        }),
                        entry('<', new String[]{
                                "      ",
                                "  𜷋𜷥𜴍𜴂",
                                "  𜴅𜶫𜵈𜶻",
                                "      "
                        }),
                        entry('>', new String[]{
                                "      ",
                                "𜴅𜴍𜷤𜶻  ",
                                "𜷋𜵈𜵰𜴂  ",
                                "      "
                        }),
                        entry('(', new String[]{
                                " 𜺢𜷥𜷴𜴂 ",
                                " ▊𜷴𜵊  ",
                                " ▊𜷴𜷤  ",
                                "  𜴅▘𜴇 "
                        }),
                        entry(')', new String[]{
                                " 𜴅𜷴𜷤𜺢 ",
                                "  𜶘𜷴▊ ",
                                "  𜷥𜷴▊ ",
                                " 𜴈▘𜴂  "
                        }),
                        entry('"', new String[]{
                                "  ▌ ▌ ",
                                "  🮂 🮂 ",
                                "      ",
                                "      "
                        }),
                        entry('\'', new String[]{
                                "   ▌  ",
                                "   🮂  ",
                                "      ",
                                "      "
                        }),
                        entry('~', new String[]{
                                "      ",
                                "🯦𜴗▘𜶩▖𜷡🯦",
                                "      ",
                                "      "
                        }),
                        entry('`', new String[]{
                                "  𜴦𜷞  ",
                                "      ",
                                "      ",
                                "      "
                        }),
                        entry(',', new String[]{
                                "      ",
                                "      ",
                                "  ▄  ",
                                "  𜴀▘𜺨  "
                        }),
                        entry('.', new String[]{
                                "      ",
                                "      ",
                                "  ▄  ",
                                "      "
                        }),
                        entry('#', new String[]{
                                " ▂▊▂▊▂",
                                " 𜶮▊𜶮▊𜶮",
                                " 🮂▊🮂▊🮂",
                                "      "
                        })
                );
    }
}
//?}