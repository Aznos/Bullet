package com.aznos.util

import java.io.ByteArrayOutputStream
import java.io.OutputStream

/**
 * Contains functions for sending minecraft packets to a client
 */
object PacketSender {
    /**
     * Sends a login success packet to the client
     * For now just dummy data ia used, but this will be changed later
     *
     * @param output The [OutputStream] to which the packet is sent to
     * @param playerName The display name of the player
     */
    fun sendLoginSuccess(output: OutputStream, playerName: String) {
        val packetID = 0x00
        val uuid = "00000000-0000-0000-0000-000000000000"

        val buffer = ByteArrayOutputStream()
        with(PacketWriter) {
            buffer.writeString(uuid)
            buffer.writeString(playerName)
        }

        val packetData = buffer.toByteArray()
        with(PacketWriter) {
            output.writeVarInt(packetData.size)
            output.writeVarInt(packetID)
        }

        output.write(packetData)
        output.flush()
    }

    /**
     * Sends a join game packet to the client
     *
     * @param output The [OutputStream] to which the packet is sent to
     * @param playerEntityID The entity ID of the player
     */
    fun sendJoinGame(output: OutputStream, playerEntityID: Int) {
        //TODO: Store things like dimension, difficulty, etc in an enum
        val packetID = 0x24
        val dimension = 0
        val difficulty = 1
        val gameMode = 1
        val maxPlayers = 100

        val buffer = ByteArrayOutputStream()
        with(PacketWriter) {
            buffer.writeInt(playerEntityID)
            buffer.writeByte(dimension)
            buffer.writeByte(difficulty)
            buffer.writeByte(gameMode)
            buffer.writeByte(maxPlayers)
            buffer.writeString("default")
        }

        val packetData = buffer.toByteArray()
        with(PacketWriter) {
            output.writeVarInt(packetData.size)
            output.writeVarInt(packetID)
        }

        output.write(packetData)
        output.flush()
    }

    /**
     * Sends a spawn position packet to the client
     *
     * @param output The [OutputStream] to which the packet is sent to
     * @param x The x coordinate of the spawn position
     * @param y The y coordinate of the spawn position
     * @param z The z coordinate of the spawn position
     */
    fun sendSpawnPosition(output: OutputStream, x: Double, y: Double, z: Double) {
        val packetID = 0x39

        val buffer = ByteArrayOutputStream()
        with(PacketWriter) {
            buffer.writeDouble(x)
            buffer.writeDouble(y)
            buffer.writeDouble(z)
        }

        val packetData = buffer.toByteArray()
        with(PacketWriter) {
            output.writeVarInt(packetData.size)
            output.writeVarInt(packetID)
        }

        output.write(packetData)
        output.flush()
    }

    /**
     * Sends a chunk data packet to the client
     * In this case, the chunk data is just a 16x16x1 layer of grass
     *
     * @param output The [OutputStream] to which the packet is sent to
     * @param chunkX The x coordinate of the chunk
     * @param chunkZ The z coordinate of the chunk
     * @param chunkData The raw data of the chunk
     */
    fun sendChunkData(output: OutputStream, chunkX: Int, chunkZ: Int, chunkData: ByteArray) {
        val packetID = 0x22
        val fullChunk = true
        val primaryBitMask = 1

        val buffer = ByteArrayOutputStream()
        with(PacketWriter) {
            buffer.writeInt(chunkX)
            buffer.writeInt(chunkZ)
            buffer.writeBoolean(fullChunk)
            buffer.writeVarInt(primaryBitMask)
            buffer.writeVarInt(chunkData.size)
            buffer.write(chunkData)
            buffer.writeVarInt(0)
        }

        val packetData = buffer.toByteArray()
        with(PacketWriter) {
            output.writeVarInt(packetData.size)
            output.writeVarInt(packetID)
        }

        output.write(packetData)
        output.flush()
    }
}