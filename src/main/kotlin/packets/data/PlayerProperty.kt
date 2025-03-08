package com.aznos.packets.data

/**
 * Represents a player property (ex: skin textures)
 */
data class PlayerProperty(
    val name: String,
    val value: String,
    val isSigned: Boolean = false,
    val signature: String? =  null
)
