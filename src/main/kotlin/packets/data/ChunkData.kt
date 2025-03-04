package com.aznos.packets.data

import com.aznos.palette.Blocks
import dev.dewy.nbt.tags.collection.CompoundTag
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import kotlin.math.ceil

data class ChunkData(
    val heightMaps: CompoundTag,
    val data: ByteArray,
    val blockEntities: List<BlockEntity>
) {
    data class BlockEntity(
        val packedXZ: Int,
        val y: Short,
        val type: Int,
        val data: CompoundTag
    )

    companion object {
        fun createHeightmapData(): LongArray {
            val result = LongArray(37) { 0L }
            for(i in 0 until 256) {
                val longIndex = i / 7
                val offset = (i % 7) * 9
                result[longIndex] = result[longIndex] or (2L shl offset)
            }

            return result
        }

        fun buildChunkData(): ByteArray {
            val sectionCount = 24
            val baos = ByteArrayOutputStream()
            val dos = DataOutputStream(baos)

            val nonEmptySection = createNonEmptySection()
            val emptySection = createEmptySection()

            for(sectionY in 0 until sectionCount) {
                val section = if(sectionY == 0) nonEmptySection else emptySection
                section.write(dos)
            }

            for(i in 0 until 256) {
                dos.writeByte(0)
            }

            return baos.toByteArray()
        }

        private fun createNonEmptySection(): ChunkSection {
            val bitsPerBlock = 1
            val palette = intArrayOf(0, Blocks.STONE.id)
            val totalBlocks = 16 * 16 * 16
            val dataLength = ceil(totalBlocks * bitsPerBlock / 64.0).toInt()
            val data = LongArray(dataLength) { 0L }

            for(i in 0 until totalBlocks) {
                val localY = i / (16 * 16)
                val value = if(localY == 0) 1 else 0
                val bitIndex = i * bitsPerBlock
                val longIndex = bitIndex / 64
                val offset = bitIndex % 64
                data[longIndex] = data[longIndex] or (value.toLong() shl offset)
            }

            val blockLight = ByteArray(2048) { 0xFF.toByte() }
            val skyLight = ByteArray(2048) { 0xFF.toByte() }
            return ChunkSection(bitsPerBlock, palette, data, blockLight, skyLight)
        }

        private fun createEmptySection(): ChunkSection {
            val blockLight = ByteArray(2048) { 0xFF.toByte() }
            val skyLight = ByteArray(2048) { 0xFF.toByte() }
            return ChunkSection(0, intArrayOf(0), LongArray(0), blockLight, skyLight)
        }
    }
}
