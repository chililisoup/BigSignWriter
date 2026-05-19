/*
 * Queatlets Old Style (c) 2026 by adhill
 * Licensed under CC BY-NC 4.0.
 * To view a copy of this license, visit https://creativecommons.org/licenses/by-nc/4.0/
 */

//? if >= 1.21.4 {
package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import static java.util.Map.entry;

public class QueatletsOldStyleFont extends AbstractFontSupplier {
    @Override
    public FontFile get() {
        return new FontFile("Queatlets Old Style", "adhillisepic")
                .parentFont("queatlets_monospace")
                .characters(
                        entry('0', new String[]{
                                "      ",
                                " 𜷡▛▘▜𜷞",
                                " 𜴦▙▖▟𜴗",
                                "      "
                        }),
                        entry('1', new String[]{
                                "      ",
                                "  𜴯█ ",
                                "  ▆█▆",
                                "      "
                        }),
                        entry('2', new String[]{
                                "      ",
                                " 𜴯▘▘𜷴𜵮 ",
                                " 𜷋𜷥𜷤𜵈▆",
                                "      "
                        }),
                        entry('3', new String[]{
                                "      ",
                                " 𜴯🮅𜴍𜶫𜷞",
                                "   ▘▜𜷂",
                                " 𜴣▆𜵈𜷥𜴗"
                        }),
                        entry('4', new String[]{
                                "      ",
                                "  𜷋𜷥𜷴 ",
                                " 𜷥𜷛▌▙▖",
                                "   █ "
                        }),
                        entry('5', new String[]{
                                "      ",
                                " ▌𜵰🮅𜴍🮅",
                                " 🮅🮅𜴍𜶫𜷞",
                                " 𜴣𜵈▆𜷥𜴗"
                        }),
                        entry('6', new String[]{
                                "  𜷡𜵰𜴂 ",
                                " ▟𜵰𜴳𜵈𜶻",
                                " 𜴦𜷤▆𜷴𜴗",
                                "      "
                        }),
                        entry('7', new String[]{
                                "      ",
                                " 🮅𜴍🮅▌▛",
                                "   ▟▛ ",
                                "  ▟▛  "
                        }),
                        entry('8', new String[]{
                                " 𜷡𜷴🮅𜶫𜷞",
                                " 𜷖𜵮𜴳𜷴𜷂",
                                " 𜴦𜷴▆𜷥𜴗",
                                "      "
                        }),
                        entry('9', new String[]{
                                "      ",
                                " 𜷡𜷴🮅𜶫𜷞",
                                " 𜴅𜴍𜴳𜷥▛",
                                "  𜷋𜷥𜴏 "
                        })
                );
    }
}
//?}