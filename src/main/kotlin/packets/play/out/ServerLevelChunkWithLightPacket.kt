package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import com.aznos.packets.data.ChunkData
import com.aznos.packets.data.LightData
import dev.dewy.nbt.api.registry.TagTypeRegistry
import java.io.DataOutputStream
import java.util.*

/**
 * A packet from the server -> client that sends when a chunk comes into
 * the clients view distance, specifying its terrain, lighting, and block entities
 *
 * @param x The x coordinate of the chunk
 * @param z The z coordinate of the chunk
 */
class ServerLevelChunkWithLightPacket(
    x: Int,
    z: Int,
    chunkData: ChunkData,
    lightData: LightData,
    registry: TagTypeRegistry
) : Packet(0x28) {
    init {
        wrapper.writeInt(x)
        wrapper.writeInt(z)

        writeChunkData(wrapper, chunkData, registry)
        writeLightData(wrapper, lightData)
    }

    private fun writeChunkData(out: DataOutputStream, chunkData: ChunkData, registry: TagTypeRegistry) {
        chunkData.heightMaps.write(out, 0, registry)

        out.writeVarInt(chunkData.data.size)
        out.write(chunkData.data)

        out.writeVarInt(chunkData.blockEntities.size)
        for(be in chunkData.blockEntities) {
            out.writeVarInt(be.packedXZ)
            out.writeShort(be.y.toInt())
            out.writeVarInt(be.type)
            be.data.write(out, 0, registry)
        }
    }

    private fun writeLightData(out: DataOutputStream, lightData: LightData) {
        writeBitSet(out, lightData.skyLightMask)
        writeBitSet(out, lightData.blockLightMask)
        writeBitSet(out, lightData.emptySkyLightMask)
        writeBitSet(out, lightData.emptyBlockLightMask)

        out.writeVarInt(lightData.skyLightArrays.size)
        for(array in lightData.skyLightArrays) {
            out.writeVarInt(array.size)
            out.write(array)
        }

        out.writeVarInt(lightData.blockLightArrays.size)
        for(array in lightData.blockLightArrays) {
            out.writeVarInt(array.size)
            out.write(array)
        }
    }

    private fun writeBitSet(out: DataOutputStream, bitSet: BitSet) {
        val longs = bitSet.toLongArray()
        out.writeVarInt(longs.size)
        for(l in longs) {
            out.writeLong(l)
        }
    }
}