package com.aznos.player

import java.net.Socket

/**
 * Represents a connected players session
 *
 * @property id Unique identifier for the player, incremented for each new player
 * TODO: Add mojang auth and turn this into a UUID
 * @property name The display name of the player
 * @property socket The [Socket] connection for the player
 */
data class Player(
    val id: Int,
    val name: String,
    val socket: Socket
)