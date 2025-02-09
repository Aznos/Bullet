package com.aznos.util

import java.io.ByteArrayOutputStream
import java.io.OutputStream

/**
 * Contains functions for sending minecraft packets to a client
 */
object PacketSender {
    /**
     * Sends a login success packet to the client
     *
     * Packet structure (Serverbound, Login Success, Packet ID 0x02)
     * - UUID (String): The UUID of the player
     * - Username (String): The display name of the player
     *
     * @param output The [OutputStream] to which the packet is sent to
     * @param playerName The display name of the player
     */
    fun sendLoginSuccess(output: OutputStream, playerName: String) {
        val packetID = 0x02
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
     * Packet structure (Serverbound, Join Game, Packet ID 0x26)
     * - Entity ID (Int)
     * - Gamemode (Unsigned Byte)
     * - Dimension (Int)
     * - Hashed Seed (Long)
     * - Max Players (VarInt)
     * - Level Type (String)
     * - View Distance (VarInt)
     * - Reduced Debug Info (Boolean)
     * - Enable Respawn Screen (Boolean)
     *
     * @param output The [OutputStream] to which the packet is sent to
     * @param playerEntityID The entity ID of the player
     */
    fun sendJoinGame(output: OutputStream, playerEntityID: Int) {
        //TODO: Store things like dimension, difficulty, etc in an enum
        val packetID = 0x26
        val gamemode: Byte = 1
        val dimension: Int = 0
        val hashedSeed: Long = 0
        val maxPlayers = 100
        val levelType = "default"
        val viewDistance = 10
        val reducedDebugInfo = false
        val enableRespawnScreen = true

        val buffer = ByteArrayOutputStream()
        with(PacketWriter) {
            buffer.writeInt(playerEntityID)
            buffer.writeByte(gamemode.toInt())
            buffer.writeInt(dimension)
            buffer.writeLong(hashedSeed)
            buffer.writeVarInt(maxPlayers)
            buffer.writeString(levelType)
            buffer.writeVarInt(viewDistance)
            buffer.writeBoolean(reducedDebugInfo)
            buffer.writeBoolean(enableRespawnScreen)
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
     * Packet structure (Serverbound, Spawn Position, Packet ID 0x39)
     * - X (Double)
     * - Y (Double)
     * - Z (Double)
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
     * Packet structure (Serverbound, Chunk Data, Packet ID 0x22)
     * - Chunk X (Int)
     * - Chunk Z (Int)
     * - Full Chunk (Boolean)
     * - Primary Bit Mask (VarInt)
     * - HeightMaps (NBT Compound): (Not implemented)
     * - Data Length (VarInt)
     * - Data (Byte Array): The raw data of the chunk
     * - Block Entities Count (VarInt): (Not implemented)
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

        //Minimal NBT compound: TAG_Compound (0x0a), empty name (short 0), then TAG_END (0x00)
        val emptyHeightmaps = byteArrayOf(0x0A.toByte(), 0x00.toByte(), 0x00.toByte())

        val buffer = ByteArrayOutputStream()
        with(PacketWriter) {
            buffer.writeInt(chunkX)
            buffer.writeInt(chunkZ)
            buffer.writeBoolean(fullChunk)
            buffer.writeVarInt(primaryBitMask)
            buffer.write(emptyHeightmaps)
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