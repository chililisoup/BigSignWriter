/*
 * Queats Lazy Sunday (c) 2026 by adhill
 * Licensed under CC BY-NC 4.0.
 * To view a copy of this license, visit https://creativecommons.org/licenses/by-nc/4.0/
 */

//? if >= 1.21.4 {
package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import static java.util.Map.entry;

public class QueatsLazySundayFont extends AbstractFontSupplier {
    @Override
    public FontFile get() {
        return new FontFile("Queats Lazy Sunday", "adhill")
                .parentFont("queats_pro_monospace")
                .characters(
                        entry('J', new String[]{
                                " ██▊",
                                "   █ ",
                                "▄ █ ",
                                "🬊▊▌▊🬆 "
                        }),
                        entry('Q', new String[]{
                                "🬵▊█▊🬱",
                                "█  █",
                                "█ 🬺▊🬝",
                                "🬊▊▊🬆▊🬱"
                        }),
                        entry('S', new String[]{
                                "🬵▊█▊🬺",
                                "🬬▊▄𜺡 ",
                                " 𜺪▀▊🬺",
                                "🬬▊█▊🬆"
                        }),
                        entry('W', new String[]{
                                "█  █",
                                "█  █",
                                "█🬓𜷴🬓█",
                                "🬊▊🬄🬀🬄▊🬆"
                        }),
                        entry('a', new String[]{
                                "      ",
                                " 🬍🬄🬎▊🬱",
                                "🬵▌🬄🬎█",
                                "🬊▊▊🬝█"
                        }),
                        entry('b', new String[]{
                                "█    ",
                                "█🬻▊▊🬱",
                                "█  █",
                                "██▊🬆"
                        }),
                        entry('d', new String[]{
                                "    █",
                                "🬵▊▊🬺█",
                                "█  █",
                                "🬊▊██"
                        }),
                        entry('e', new String[]{
                                "      ",
                                "🬵▊🬎🬄▌🬱",
                                "█🬎🬄🬎🬎",
                                "🬊▊█▊🬆"
                        }),
                        entry('f', new String[]{
                                " 🬻▊🬄🬎🬌",
                                "▌██ ",
                                " █   ",
                                " █   "
                        }),
                        entry('g', new String[]{
                                "      ",
                                "🬻▌🬄🬌█",
                                "🬊▌🬓🬹█",
                                "🬩🬹🬓🬹▊🬆"
                        }),
                        entry('h', new String[]{
                                "█    ",
                                "█🬻▊▊🬱",
                                "█  █",
                                "█  █"
                        }),
                        entry('i', new String[]{
                                "   ▀ ",
                                "  ▌█ ",
                                "   █ ",
                                " ██▌"
                        }),
                        entry('j', new String[]{
                                "   ▀ ",
                                " ██ ",
                                "   █ ",
                                "🬩🬓🬹▊🬆 "
                        }),
                        entry('l', new String[]{
                                " █   ",
                                " █   ",
                                " █   ",
                                " 🬊▊█🬝"
                        }),
                        entry('n', new String[]{
                                "      ",
                                "█🬻▊▊🬱",
                                "█  █",
                                "█  █"
                        }),
                        entry('q', new String[]{
                                "      ",
                                "🬻▌🬄🬌█",
                                "🬊▌🬓🬹█",
                                "    █"
                        }),
                        entry('r', new String[]{
                                "      ",
                                "█🬻▊▊🬱",
                                "█  ▀",
                                "█    "
                        }),
                        entry('s', new String[]{
                                "      ",
                                "🬵▌🬎🬄🬌 ",
                                "🬊🬎🬎🬄▊🬱",
                                "🬊▌🬹🬓▊🬆"
                        }),
                        entry('t', new String[]{
                                " ▄   ",
                                "▊██ ",
                                " █   ",
                                " 🬊▊█🬝"
                        }),
                        entry('w', new String[]{
                                "      ",
                                "𜷴▊ ▌ ▊𜷴",
                                "🬬🬺▊🬺▌🬄",
                                " 🬬▊🬬🬝 "
                        }),
                        entry('y', new String[]{
                                "      ",
                                "▌▌  █",
                                "🬬▌🬓🬚█",
                                "🬩🬹🬓🬹▊🬆"
                        })
                );
    }
}
//?}