package util

import kotlin.math.pow
import kotlin.math.sqrt

enum class Color(val code: String, val hex: String, val rgb: Int) {
    BLACK("§0", "#000000", 0x000000),
    DARK_BLUE("§1", "#0000AA", 0x0000AA),
    DARK_GREEN("§2", "#00AA00", 0x00AA00),
    DARK_AQUA("§3", "#00AAAA", 0x00AAAA),
    DARK_RED("§4", "#AA0000", 0xAA0000),
    DARK_PURPLE("§5", "#AA00AA", 0xAA00AA),
    GOLD("§6", "#FFAA00", 0xFFAA00),
    GRAY("§7", "#AAAAAA", 0xAAAAAA),
    DARK_GRAY("§8", "#555555", 0x555555),
    BLUE("§9", "#5555FF", 0x5555FF),
    GREEN("§a", "#55FF55", 0x55FF55),
    AQUA("§b", "#55FFFF", 0x55FFFF),
    RED("§c", "#FF5555", 0xFF5555),
    LIGHT_PURPLE("§d", "#FF55FF", 0xFF55FF),
    YELLOW("§e", "#FFFF55", 0xFFFF55),
    WHITE("§f", "#FFFFFF", 0xFFFFFF),

    OBFUSCATED("§k", "#000000", 0x000000),
    BOLD("§l", "#000000", 0x000000),
    STRIKETHROUGH("§m", "#000000", 0x000000),
    UNDERLINE("§n", "#000000", 0x000000),
    ITALIC("§o", "#000000", 0x000000),
    RESET("§r", "#FFFFFF", 0xFFFFFF);

    fun toMinecraftCode(): String = code

    companion object {
        fun fromHex(hex: String): Color? {
            val cleanedHex = hex.replace("#", "").uppercase()
            return entries.find { it.hex.equals("#$cleanedHex", ignoreCase = true) }
        }

        fun fromRGB(rgb: Int): Color {
            return entries.toTypedArray().minByOrNull { it.distanceTo(rgb) } ?: WHITE
        }

        private fun Color.distanceTo(rgb: Int): Double {
            val r1 = (this.rgb shr 16) and 0xFF
            val g1 = (this.rgb shr 8) and 0xFF
            val b1 = this.rgb and 0xFF

            val r2 = (rgb shr 16) and 0xFF
            val g2 = (rgb shr 8) and 0xFF
            val b2 = rgb and 0xFF

            return sqrt((r1 - r2).toDouble().pow(2) + (g1 - g2).toDouble().pow(2) + (b1 - b2).toDouble().pow(2))
        }

        fun fromName(name: String): Color? {
            return entries.find { it.name.equals(name, ignoreCase = true) }
        }
    }
}