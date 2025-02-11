package com.aznos.util

/**
 * Utility class for colors
 *
 * Provides commonly used colors, as well as a helper method to convert hex colors to their minecraft chat color counterparts
 */
object Color {
    const val BLACK = "§0"
    const val DARK_BLUE = "§1"
    const val DARK_GREEN = "§2"
    const val DARK_AQUA = "§3"
    const val DARK_RED = "§4"
    const val DARK_PURPLE = "§5"
    const val GOLD = "§6"
    const val GRAY = "§7"
    const val DARK_GRAY = "§8"
    const val BLUE = "§9"
    const val GREEN = "§a"
    const val AQUA = "§b"
    const val RED = "§c"
    const val LIGHT_PURPLE = "§d"
    const val YELLOW = "§e"
    const val WHITE = "§f"

    const val BOLD = "§l"
    const val UNDERLINE = "§n"
    const val STRIKETHROUGH = "§m"
    const val ITALIC = "§o"
    const val RESET = "§r"

    /**
     * Converts a hex color (ex. #FF5733) to minecrafts rgb format (§x§f§f§5§7§3§3)
     *
     * @param hex The hex color to convert
     * @return The formatted minecraft chat color string
     * @throws IllegalArgumentException if the hex string is invalid
     */
    fun hex(hex: String): String {
        val cleanHex = hex.removePrefix("#")
        require(cleanHex.length == 6) { "Invalid hex color: $hex" }

        return "§x" + cleanHex.chunked(1).joinToString("") { "§$it" } + RESET
    }

    /**
     * Replaces color placeholders with minecraft color codes
     *
     * Usage example:
     * val text = "&cHello &bworld!"
     *
     * @param text The raw text containing color placeholders
     * @return The text formatted with minecraft color codes
     */
    fun colorize(text: String): String {
        var formattedText = text
            .replace("&0", BLACK)
            .replace("&1", DARK_BLUE)
            .replace("&2", DARK_GREEN)
            .replace("&3", DARK_AQUA)
            .replace("&4", DARK_RED)
            .replace("&5", DARK_PURPLE)
            .replace("&6", GOLD)
            .replace("&7", GRAY)
            .replace("&8", DARK_GRAY)
            .replace("&9", BLUE)
            .replace("&a", GREEN)
            .replace("&b", AQUA)
            .replace("&c", RED)
            .replace("&d", LIGHT_PURPLE)
            .replace("&e", YELLOW)
            .replace("&f", WHITE)
            .replace("&l", BOLD)
            .replace("&n", UNDERLINE)
            .replace("&m", STRIKETHROUGH)
            .replace("&o", ITALIC)
            .replace("&r", RESET)

        val hexPattern = Regex("&hex\\(#([a-fA-F0-9]{6})\\)")
        formattedText = hexPattern.replace(formattedText) {
            hex("#${it.groupValues[1]}")
        }

        return formattedText
    }
}