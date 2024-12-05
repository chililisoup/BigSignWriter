<p align="center"><img width="128" alt="Big Sign Writer Logo" src="https://github.com/user-attachments/assets/3e093f83-022e-42f4-96b5-39fac32d204c"></p>

<h1 align="center">Big Sign Writer - Fabric Mod</h1>

A client-side utitility mod that eases inputting text onto signs that forms larger letters! Just turn the toggle on in the sign editing UI, and type as normal! Each large character is made up of smaller, normal unicode characters from the vanilla Minecraft font, allowing this mod to work on most servers.

**Font resource packs may cause issues.**

<p align="center">
 <a href="https://modrinth.com/mod/bigsignwriter"><img src="https://img.shields.io/badge/Modrinth-00AF5C?style=for-the-badge&logo=modrinth&labelColor=16181C" alt="Modrinth downloads"></a>
</p>

<hr>

<p align="center">
<img alt="'BIG SIGN WRITER' written across several signs in large text" src="https://cdn.modrinth.com/data/cached_images/e349049404a0248aae271832dce2551e29134458_0.webp">
</p>

<hr>

A button is added to the sign editing GUI to switch between large letter typing and normal. This button's position can be changed in the config file. (`config/bigsignwriter-config.json`)

With the default configuration, normal signs can support 3 of these large characters, while hanging signs can show 2.

The default configuration has large characters for all uppercase letters, all numbers, and a few additional symbols. Each large character can be modified, and new ones can be added using the config.

<hr>
<details>
<summary>Configuration</summary>
<br>
There are two config files, one for general mod settings, and one containing each large character. They are loaded once at runtime, so to make any changes, you'll have to restart your game.

<hr>

### `config/bigsignwriter-config.json`
Contains coordinates to render the 'large text' button at.
Also contains `characterSeparator`, which dictates what should separate each of the large characters, which is a single space by default.

<hr>

### `config/bigsignwriter-characters.json`
Contains every large character and the normal character they represent.
You may add additional entries, as long as what you want replaced is a single, typeable character.

When editing/creating these large characters, it is important that each line is the exact same width to maintain alignment. Different fonts will have different widths for different characters, so it is a good idea to make these directly inside Minecraft, and then copy each line into the config file.

The default 'T' is a good example, the top line is 3 blocks, which are 9 pixels wide each, making for a total of 27 pixels. Each subsequent line has one block, then on both sides of the block there is 1 space (4 pixels wide), and 1 "thin space" (5 pixels wide), bringing the total in line at 27.

To check that your alignment is good, I like to copy three blocks `███` onto both sides of each line, then modify each line until all the blocks are in line with each other.

A massively helpful tool to aid in this process is [Symbol Chat](https://modrinth.com/mod/symbol-chat), which adds a menu where you can filter every available unicode character by their width, among other things.

</details>

