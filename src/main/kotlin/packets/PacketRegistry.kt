package com.aznos.packets

import com.aznos.GameState
import com.aznos.packets.login.`in`.ClientLoginStartPacket
import com.aznos.packets.play.`in`.ClientChatMessagePacket
import com.aznos.packets.play.`in`.ClientKeepAlivePacket
import com.aznos.packets.status.`in`.ClientStatusPingPacket
import com.aznos.packets.status.`in`.ClientStatusRequestPacket
import packets.handshake.HandshakePacket
import java.util.WeakHashMap
import java.util.concurrent.ConcurrentHashMap

/**
 * Maintains a registry of packet classes mapped by the game state and packet ID
 */
object PacketRegistry {
    private val packets: MutableMap<GameState, MutableMap<Int, Class<out Packet>>> = ConcurrentHashMap()

    init {
        //HANDSHAKE
        val handshakePackets = ConcurrentHashMap<Int, Class<out Packet>>().apply {
            this[0x00] = HandshakePacket::class.java
        }

        packets[GameState.HANDSHAKE] = handshakePackets

        //STATUS
        val statusPackets = ConcurrentHashMap<Int, Class<out Packet>>().apply {
            this[0x00] = ClientStatusRequestPacket::class.java
            this[0x01] = ClientStatusPingPacket::class.java
        }

        packets[GameState.STATUS] = statusPackets

        //LOGIN
        val loginPackets = ConcurrentHashMap<Int, Class<out Packet>>().apply {
            this[0x00] = ClientLoginStartPacket::class.java
        }

        packets[GameState.LOGIN] = loginPackets

        //CONFIGURATION
        val configurationPackets = ConcurrentHashMap<Int, Class<out Packet>>().apply {

        }

        packets[GameState.CONFIGURATION] = configurationPackets

        //PLAY
        val playPackets = ConcurrentHashMap<Int, Class<out Packet>>().apply {
            this[0x03] = ClientChatMessagePacket::class.java
            this[0x10] = ClientKeepAlivePacket::class.java
        }

        packets[GameState.PLAY] = playPackets
    }

    /**
     * Get the packet by the packet ID
     *
     * @param gameState The current gamestate
     * @param id The ID of the packet
     * @return The class of the packet
     */
    fun getPacket(gameState: GameState, id: Int): Class<out Packet>? {
        return packets.getOrDefault(gameState, WeakHashMap())[id]
    }
}