package com.aznos.packet.serverbound

import com.aznos.util.VarInt.readString
import com.aznos.util.VarInt.readVarInt
import java.io.InputStream

/**
 * Represents the login start packet (0x00)
 *
 * @property name Players display name
 */
data class LoginStartPacket(val name: String)

/**
 * Reads login start packet from an InputStream
 *
 * Packet structure:
 *  - Packet Length (VarInt)
 *  - Packet ID (VarInt) (0x00)
 *  - Player Name (String)
 */
fun InputStream.readLoginStartPacket(): LoginStartPacket? {
    val packetLen = readVarInt()
    val packetID = readVarInt()

    if(packetID != 0x00) return null

    val name = readString()
    return LoginStartPacket(name)
}