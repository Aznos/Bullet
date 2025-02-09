package com.aznos.protocol

import com.aznos.util.VarInt
import java.io.InputStream

/**
 * Represents a Minecraft handshake packet
 *
 * @property protocolVersion Protocol version used by the client
 * @property serverAddress The address the client is connecting to
 * @property serverPort The port number for the connection
 * @property nextState The next state (1 for status, 2 for login)
 */
data class HandshakePacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: Int,
    val nextState: Int
)

/**
 * Reads a handshake packet from the [InputStream]
 *
 * The handshake packet format is:
 * - Packet Length (VarInt)
 * - Packet ID (VarInt) - should usually be 0x00 for handshake packets
 * - Protocol Version (VarInt)
 * - Server Address (String, prefixed with its VarInt length)
 * - Server Port (Unsigned short, 2 bytes, big-endian)
 * - Next State (VarInt)
 *
 * @receiver Input stream from which to read
 * @receiver A [HandshakePacket] if the packet is a handshake (packet ID 0x00), or "null" if the packet ID is not 0x00
 * @throws RuntimeException if an error occurs during reading
 */
fun InputStream.readHandshakePacket(): HandshakePacket? {
    val packetLen = VarInt.run { this@readHandshakePacket.readVarInt() }

    val packetData = ByteArray(packetLen)
    var bytesRead = 0
    while(bytesRead < packetLen) {
        val read = this.read(packetData, bytesRead, packetLen - bytesRead)
        if(read == -1) throw RuntimeException("Unexpected end of stream while reading handshake packet")
        bytesRead += read
    }

    val packetStream = packetData.inputStream()
    val packetID = VarInt.run { packetStream.readVarInt() }
    if(packetID != 0x00) return null; //Not a handshake packet

    val protocolVersion = VarInt.run { packetStream.readVarInt() }
    val serverAddress = VarInt.run { packetStream.readString() }

    val portBytes = ByteArray(2)
    if(packetStream.read(portBytes) != 2) throw RuntimeException("Could not read server port")
    val serverPort = ((portBytes[0].toInt() and 0xFF) shl 8) or (portBytes[1].toInt() and 0xFF)

    val nextState = VarInt.run { packetStream.readVarInt() }

    return HandshakePacket(protocolVersion, serverAddress, serverPort, nextState)
}