package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Teleports the client, e.g. during login or throwing an ender pearl or /teleport
 *
 * @param x The X position
 * @param y The Y position
 * @param z The Z position
 * @param yaw The player yaw (left-right)
 * @param pitch The player pitch (up-dpwn)
 */
class ServerSyncPlayerPosition(
    id: Int,
    x: Double, y: Double, z: Double,
    vx: Double, vy: Double, vz: Double,
    yaw: Float, pitch: Float
) : Packet(0x42) {
    init {
        wrapper.writeVarInt(id)

        wrapper.writeDouble(x)
        wrapper.writeDouble(y)
        wrapper.writeDouble(z)

        wrapper.writeDouble(vx)
        wrapper.writeDouble(vy)
        wrapper.writeDouble(vz)

        wrapper.writeFloat(yaw)
        wrapper.writeFloat(pitch)

        wrapper.writeInt(0)
    }
}