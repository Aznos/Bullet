package com.aznos.packets

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.GameState
import com.aznos.events.*
import com.aznos.packets.configuration.out.ServerConfigFinishPacket
import com.aznos.packets.data.ChunkData
import com.aznos.packets.data.ServerStatusResponse
import com.aznos.packets.data.buildLightData
import com.aznos.packets.login.`in`.ClientLoginStartPacket
import com.aznos.packets.login.out.ServerLoginSuccessPacket
import com.aznos.packets.play.`in`.ClientChatMessagePacket
import com.aznos.packets.play.`in`.ClientKeepAlivePacket
import com.aznos.packets.play.out.ServerGameEvent
import com.aznos.packets.play.out.ServerJoinGamePacket
import com.aznos.packets.play.out.ServerLevelChunkWithLightPacket
import com.aznos.packets.play.out.ServerSetChunkCacheCenterPacket
import com.aznos.packets.play.out.ServerSyncPlayerPosition
import com.aznos.packets.status.`in`.ClientStatusPingPacket
import com.aznos.packets.status.`in`.ClientStatusRequestPacket
import com.aznos.packets.status.out.ServerStatusPongPacket
import com.aznos.player.GameMode
import dev.dewy.nbt.api.registry.TagTypeRegistry
import dev.dewy.nbt.tags.collection.CompoundTag
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import packets.handshake.HandshakePacket
import packets.status.out.ServerStatusResponsePacket
import java.util.*

/**
 * Handles all incoming packets by dispatching them to the appropriate handler methods
 *
 * @property client The clients session
 */
@Suppress("UnusedParameter")
class PacketHandler(
    private val client: ClientSession
) {
    /**
     * Handles when a chat message is received
     */
    @PacketReceiver
    fun onChatMessage(packet: ClientChatMessagePacket) {
        val message = packet.message

        if(message.length > 255) {
            client.disconnect("Message too long")
            return
        }

        val formattedMessage = message.replace('&', '§')

        val event = PlayerChatEvent(client.username!!, formattedMessage)
        EventManager.fire(event)
        if(event.isCancelled) return

        val textComponent = Component.text()
            .append(Component.text().content("<").color(NamedTextColor.GRAY))
            .append(Component.text().content(client.username!!).color(TextColor.color(0x55FFFF)))
            .append(Component.text().content("> ").color(NamedTextColor.GRAY))
            .append(Component.text().content(formattedMessage).color(TextColor.color(0xFFFFFF)))
            .build()

        client.sendMessage(textComponent)
    }

    /**
     * Handles when the client responds to the server keep alive packet to tell the server the client is still online
     */
    @PacketReceiver
    fun onKeepAlive(packet: ClientKeepAlivePacket) {
        val event = PlayerHeartbeatEvent(client.username!!)
        EventManager.fire(event)
        if(event.isCancelled) return

        client.respondedToKeepAlive = true
    }

    /**
     * Handles when the client tells the server it's ready to log in
     *
     * The server first checks for a valid version and uuid, then sends a login success packet
     * It'll then transition the game state into play mode
     * and send a join game and player position/look packet to get past all loading screens
     */
    @PacketReceiver
    fun onLoginStart(packet: ClientLoginStartPacket) {
        val preJoinEvent = PlayerPreJoinEvent()
        EventManager.fire(preJoinEvent)
        if(preJoinEvent.isCancelled) return

        client.isClientValid(packet)

        val uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:${packet.username}").toByteArray())
        client.username = packet.username
        client.uuid = uuid

        client.sendPacket(ServerLoginSuccessPacket(uuid, packet.username))
        client.state = GameState.CONFIGURATION

        client.sendRegistries()

        client.sendPacket(ServerConfigFinishPacket())
        client.state = GameState.PLAY

        client.sendPacket(ServerJoinGamePacket(
            0,
            false,
            listOf("minecraft:overworld"),
            0,
            8,
            false,
            true,
            0,
            "minecraft:overworld",
            GameMode.SPECTATOR,
            false
        ))

        client.sendPacket(ServerGameEvent(13, 0f))
        client.sendPacket(ServerSyncPlayerPosition(0, 8.5, 0.0, 8.5, 0.0, 5.0, 0.0, 0f, 90f))
        client.sendPacket(ServerSetChunkCacheCenterPacket())

        val heightmapTag = CompoundTag().apply {
            putLongArray("MOTION_BLOCKING", ChunkData.createHeightmapData())
            putLongArray("WORLD_SURFACE", ChunkData.createHeightmapData())
        }

        val chunkDataBytes = ChunkData.buildChunkData()
        val chunkData = ChunkData(
            heightMaps = heightmapTag,
            data = chunkDataBytes,
            blockEntities = emptyList()
        )

        val lightData = buildLightData()
        client.sendPacket(ServerLevelChunkWithLightPacket(
            x = 0,
            z = 0,
            chunkData,
            lightData,
            TagTypeRegistry()
        ))

        client.scheduleKeepAlive()
    }

    /**
     * Handles a ping packet by sending a pong response and closing the connection
     */
    @PacketReceiver
    fun onPing(packet: ClientStatusPingPacket) {
        client.sendPacket(ServerStatusPongPacket(packet.payload))
        client.close()
    }

    /**
     * Handles a status request packet by sending a server status response
     */
    @PacketReceiver
    fun onStatusRequest(packet: ClientStatusRequestPacket) {
        val event = StatusRequestEvent(Bullet.MAX_PLAYERS, 0, Bullet.DESCRIPTION)
        EventManager.fire(event)
        if(event.isCancelled) return

        val response = ServerStatusResponse(
            ServerStatusResponse.Version(Bullet.VERSION, Bullet.PROTOCOL),
            ServerStatusResponse.Players(event.maxPlayers, event.onlinePlayers),
            event.motd,
            false
        )

        client.sendPacket(ServerStatusResponsePacket(Json.encodeToString(response)))
    }

    /**
     * Handles a handshake packet by updating the client state and protocol
     */
    @PacketReceiver
    fun onHandshake(packet: HandshakePacket) {
        client.state = if(packet.state == 2) GameState.LOGIN else GameState.STATUS
        client.protocol = packet.protocol ?: -1

        val event = HandshakeEvent(client.state, client.protocol)
        EventManager.fire(event)
        if(event.isCancelled) return
    }

    /**
     * Dispatches the given packet to the corresponding handler method based on its type
     *
     * @param packet The packet to handle
     */
    fun handle(packet: Packet) {
        for(method in javaClass.methods) {
            if(method.isAnnotationPresent(PacketReceiver::class.java)) {
                val params: Array<Class<*>> = method.parameterTypes
                if(params.size == 1 && params[0] == packet.javaClass) {
                    method.invoke(this, packet)
                }
            }
        }
    }
}