package com.aznos.packets.play.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import com.aznos.player.GameMode
import dev.dewy.nbt.Nbt
import dev.dewy.nbt.tags.collection.CompoundTag
import kotlin.math.max

/**
 * Packet sent to the client to tell the client about the center position of the clients chunk loading area
 *
 * @param x X coordinate of the loading area center
 * @param z Z coordinate of the loading area center
 */
class ServerSetChunkCacheCenterPacket(
    x: Int = 0,
    z: Int = 0
) : Packet(0x58) {
    init {
        wrapper.writeVarInt(x)
        wrapper.writeVarInt(z)
    }
}
