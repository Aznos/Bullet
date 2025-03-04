package com.aznos.packets.play.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import com.aznos.player.GameMode
import dev.dewy.nbt.Nbt
import dev.dewy.nbt.tags.collection.CompoundTag
import kotlin.math.max

/**
 * Packet sent to the client that sends all the information needed to join the game
 *
 * @param entityID The players entity id
 * @param hardcore If the world is hardcore or not
 * @param dimensionNames Identifiers for all dimensions on the server
 * @param viewDistance The view distance set by the server
 * @param simulationDistance The distance that the client will process specific things, such as entities.
 * @param reducedDebugInfo Whether to have reduced debug info in f3 menu
 * @param enableRespawnScreen Set to false when the doImmediateRespawn gamerule is true.
 * @param dimensionType The ID of the type of dimension in the minecraft:dimension_type registry
 * @param dimensionName The name of the dimension
 * @param gameMode The players gamemode
 * @param isFlat Whether the world is flat or not
 */
class ServerJoinGamePacket(
    entityID: Int,
    hardcore: Boolean,
    dimensionNames: List<String>,
    viewDistance: Int,
    simulationDistance: Int,
    reducedDebugInfo: Boolean,
    enableRespawnScreen: Boolean,
    dimensionType: Int,
    dimensionName: String,
    gameMode: GameMode,
    isFlat: Boolean,
) : Packet(0x2C) {
    init {
        wrapper.writeInt(entityID)
        wrapper.writeBoolean(hardcore)

        wrapper.writeVarInt(dimensionNames.size)
        dimensionNames.forEach { wrapper.writeString(it) }

        wrapper.writeVarInt(0) // Max Players (Unused by the client)
        wrapper.writeVarInt(viewDistance)
        wrapper.writeVarInt(simulationDistance)
        wrapper.writeBoolean(reducedDebugInfo)
        wrapper.writeBoolean(enableRespawnScreen)
        wrapper.writeBoolean(false) // Do limited crafting (Unused by client according to wiki)
        wrapper.writeVarInt(dimensionType)
        wrapper.writeString(dimensionName)
        wrapper.writeLong(0L) // Hashed seed
        wrapper.writeByte(gameMode.id)
        wrapper.writeByte(-1) // Previous player gamemode
        wrapper.writeBoolean(isFlat)
        wrapper.writeBoolean(false)
        wrapper.writeBoolean(false)
        wrapper.writeVarInt(0)
        wrapper.writeVarInt(0)
        wrapper.writeBoolean(false)
    }
}
