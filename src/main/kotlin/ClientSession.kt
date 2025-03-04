package com.aznos

import com.aznos.datatypes.VarInt
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.events.EventManager
import com.aznos.events.PlayerQuitEvent
import com.aznos.packets.Packet
import com.aznos.packets.PacketHandler
import com.aznos.packets.PacketRegistry
import com.aznos.packets.configuration.out.ServerConfigRegistryData
import com.aznos.packets.login.`in`.ClientLoginStartPacket
import com.aznos.packets.login.out.ServerLoginDisconnectPacket
import com.aznos.packets.play.out.ServerChatMessagePacket
import com.aznos.packets.play.out.ServerKeepAlivePacket
import com.aznos.packets.play.out.ServerPlayDisconnectPacket
import com.aznos.player.ChatPosition
import com.aznos.registry.Registries
import dev.dewy.nbt.tags.collection.CompoundTag
import net.kyori.adventure.text.TextComponent
import java.io.DataInputStream
import java.net.Socket
import java.util.*
import kotlin.time.Duration.Companion.seconds

/**
 * Represents a session between a connected client and the server
 *
 * @param socket The clients socket connection
 * @property out The output stream to send packets to the client
 * @property input The input stream to read packets from the client
 * @property handler The packet handler to handle incoming packets
 */
class ClientSession(
    private val socket: Socket,
) : AutoCloseable {
    private val out = socket.getOutputStream()
    private val input = DataInputStream(socket.getInputStream())
    private val handler = PacketHandler(this)

    var state = GameState.HANDSHAKE
    var protocol = -1

    var username: String? = null
    var uuid: UUID? = null

    /**
     * This timer will keep track of when to send the keep alive packet to the client
     */
    private var keepAliveTimer: Unit? = null
    var respondedToKeepAlive: Boolean = true

    /**
     * Reads and processes incoming packets from the client
     * It reads the packet length, ID, and data, then dispatches the packet to the handler
     */
    fun handle() {
        while (!isClosed()) {
            val len = input.readVarInt()
            val id = input.readVarInt()
            val dataLength = len - VarInt.getVarIntSize(id)
            val data = ByteArray(dataLength)
            input.readFully(data)

            val packetClass = PacketRegistry.getPacket(state, id)
            if (packetClass != null) {
                val packet: Packet = packetClass
                    .getConstructor(ByteArray::class.java)
                    .newInstance(data)
                handler.handle(packet)
            } else {
                println("No registered packet for state $state with id $id")
            }
        }
    }

    fun scheduleKeepAlive() {
        keepAliveTimer = Timer(true).scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if(!respondedToKeepAlive) {
                    disconnect("Timed out")
                    cancel()
                    return
                }
                sendPacket(ServerKeepAlivePacket(System.currentTimeMillis()))
                respondedToKeepAlive = true
            }
        }, 10.seconds.inWholeMilliseconds, 10.seconds.inWholeMilliseconds)
    }

    /**
     * Sends a chat message to the client
     *
     * @param message The message to be sent to the client
     */
    fun sendMessage(message: TextComponent) {
        sendPacket(ServerChatMessagePacket(message, ChatPosition.CHAT, null))
    }

    /**
     * Disconnects the client that is in the play state with the server play disconnect packet
     *
     * @param message The message to be sent to the client
     */
    fun disconnect(message: String) {
        if(state == GameState.PLAY) {
            sendPacket(ServerPlayDisconnectPacket(message))
        } else if(state == GameState.LOGIN) {
            sendPacket(ServerLoginDisconnectPacket(message))
        }

        EventManager.fire(PlayerQuitEvent(username!!))
        close()
    }

    fun sendRegistries() {
        sendPacket(ServerConfigRegistryData(Registries.dimension_type))
        sendPacket(ServerConfigRegistryData(Registries.biomes))
        sendPacket(ServerConfigRegistryData(Registries.wolf_variant))
        sendPacket(ServerConfigRegistryData(Registries.damage_type))

        sendPacket(
            ServerConfigRegistryData("minecraft:painting_variant", listOf(
                ServerConfigRegistryData.RawEntry("minecraft:alban", CompoundTag().apply {
                    putString("asset_id", "minecraft:alban")
                    putInt("height", 1)
                    putInt("width", 1)
                    putString("title", "gg")
                    putString("author", "gg")
                })
            ))
        )
    }

    fun isClientValid(packet: ClientLoginStartPacket): Boolean {
        if(protocol > Bullet.PROTOCOL) {
            disconnect("Please downgrade your minecraft version to " + Bullet.VERSION)
            return false
        } else if(protocol < Bullet.PROTOCOL) {
            disconnect("Your client is outdated, please upgrade to minecraft version " + Bullet.VERSION)
            return false
        }

        val username = packet.username
        if(!username.matches(Regex("^[a-zA-Z0-9]{3,16}$"))) { // Alphanumeric and 3-16 characters
            disconnect("Invalid username")
            return false
        }

        return true
    }

    /**
     * Sends a packet to the client
     *
     * @param packet The packet to be sent
     */
    fun sendPacket(packet: Packet) {
        out.write(packet.retrieveData())
        out.flush()
    }

    /**
     * Checks if the clients socket is closed or not bound
     *
     * @return true If the socket is closed
     */
    private fun isClosed(): Boolean {
        return socket.let {
            it.isClosed || !it.isBound
        }
    }

    /**
     * Closes connection
     */
    override fun close() {
        keepAliveTimer = null
        socket.close()
    }
}