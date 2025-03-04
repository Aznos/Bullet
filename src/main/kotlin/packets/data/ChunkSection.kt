package com.aznos.packets.data

import com.aznos.datatypes.VarInt.writeVarInt
import java.io.DataOutputStream

/**
 * Represents a single 16x16x16 chunk section
 *
 * @param bitsPerBlock The number of bits per block
 * @param palette The global block state ID palette
 * @param data Packed block data, if bits per block is 0 then the data array is empty
 * @param blockLight 2048 bytes, each byte holds two 4 bit light values
 * @param skyLight 2048 bytes, for the over-world each nibble = 0xF for full light
 */
data class ChunkSection(
    val bitsPerBlock: Int,
    val palette: IntArray,
    val data: LongArray,
    val blockLight: ByteArray,
    val skyLight: ByteArray
) {
    fun write(out: DataOutputStream) {
        out.writeByte(bitsPerBlock)
        if(bitsPerBlock > 0) {
            out.writeVarInt(palette.size)
            for(entry in palette) {
                out.writeVarInt(entry)
            }
        }

        out.writeVarInt(data.size)
        for(l in data) {
            out.writeLong(l)
        }

        out.write(blockLight)
        out.write(skyLight)
    }
}
