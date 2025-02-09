package com.aznos.session

import com.aznos.player.Player
import java.net.Socket
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * Manage player sessions
 */
object SessionManager {
    private val players = ConcurrentHashMap<Int, Player>()
    private val playerIDCounter = AtomicInteger(1)

    /**
     * Adds a new player session
     *
     * @param socket The [Socket] connection of the player
     * @return The [Player] object representing the new player
     */
    fun addPlayer(socket: Socket): Player {
        val id = playerIDCounter.getAndIncrement()
        val playerName = "Player$id"
        val player = Player(id, playerName, socket)

        players[id] = player
        println("Added player: $player")

        return player;
    }

    /**
     * Removes the player with the given [playerID] from the session
     *
     * @param playerID The ID of the player to remove
     */
    fun removePlayer(playerID: Int) {
        val removed = players.remove(playerID)
        println("Removed player: $removed")
    }

    /**
     * Retrieves a list of currently connected players
     *
     * @return a list of [Player] objects representing the connected players
     */
    fun getPlayers(): List<Player> = players.values.toList()
}