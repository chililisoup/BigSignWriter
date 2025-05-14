<p align="center"><img width="128" alt="Big Sign Writer Logo" src="https://github.com/user-attachments/assets/3e093f83-022e-42f4-96b5-39fac32d204c"></p>

<h1 align="center">Big Sign Writer - Fabric Mod</h1>

A client-side utility mod that eases inputting text onto signs that forms larger letters! Just turn the toggle on in the sign editing UI, and type as normal! Each large character is made up of smaller, normal Unicode characters from the vanilla Minecraft font, allowing this mod to work on most servers.

**Font resource packs may cause issues.**

<p align="center">
 <a href="https://modrinth.com/mod/bigsignwriter"><img src="https://img.shields.io/badge/Modrinth-00AF5C?style=for-the-badge&logo=modrinth&labelColor=16181C" alt="Modrinth downloads"></a>
 <a href="https://curseforge.com/minecraft/mc-mods/big-sign-writer"><img src="https://img.shields.io/badge/CurseForge-F16436?style=for-the-badge&logo=curseforge&labelColor=0D0D0D" alt="CurseForge downloads"></a>
</p>

<hr>

<p align="center">
<img alt="'BIG SIGN WRITER' written across several signs in large text" src="https://cdn.modrinth.com/data/cached_images/e349049404a0248aae271832dce2551e29134458_0.webp">
</p>

<hr>

A button is added to the sign editing GUI to switch between large letter typing and normal. This button's position can be changed in the config file. (`config/bigsignwriter/config.json`)

The default fonts have large characters for all uppercase letters, all numbers, and a few additional symbols. Each character can be modified, and new fonts can be added in the 'fonts' folder. (`config/bigsignwriter/fonts/`)

<hr>
<details>
<summary>Configuration</summary>
<br>
The config and font files are loaded once at runtime, and are reloaded every time you hit the 'Reload' button in the sign editing screen, or when you save with YACL.

<hr>

### `config/bigsignwriter/config.json`
Contains coordinates for where to place this mod's buttons in the sign edit gui, offset from the center of the screen.

Also contains `defaultCharacterSeparator`, which sets the default separator string to place between characters, and is a single space by default. May be overridden by fonts.

These settings can be modified in-game when [YACL](https://modrinth.com/mod/yacl) (and [Mod Menu](https://modrinth.com/mod/modmenu) for the Fabric version) are installed.

<hr>

### `config/bigsignwriter/fonts/`
Three fonts are bundled in by default: `default.json`, `retro.json` and `sharp.json`.
These contain a `"name":""` field, an optional `"characterSeparator":""` field, which can override which character(s) to use in between letters, and a `"characters":{}` field containing every large character and the normal character they represent. You may add additional characters, as long as what you want replaced is a single, type-able character. You may also add new fonts as jsons inside `config/bigsignwriter/fonts/`.

When editing/creating large characters, it is important that each line is the exact same width to maintain alignment. Different fonts will have different widths for different characters, so it is a good idea to make these directly inside Minecraft, and then copy each line into the config file.

The default 'T' is a good example, the top line is 3 blocks, which are 9 pixels wide each, making for a total of 27 pixels. Each subsequent line has one block, then on both sides of the block there is 1 space (4 pixels wide), and 1 "thin space" (5 pixels wide), bringing the total in line at 27.

To check that your alignment is good, I like to copy three blocks `███` onto both sides of each line, then modify each line until all the blocks are in line with each other.

A massively helpful tool to aid in this process is [Symbol Chat](https://modrinth.com/mod/symbol-chat), which adds a menu where you can filter every available Unicode character by their width, among other things.

</details>

