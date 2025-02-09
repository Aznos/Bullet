package com.aznos.world

object World {
    const val CHUNK_SIZE = 16
    const val GRASS_BLOCK: Byte = 2 //TODO: Make enums of blocks

    /**
     * Generates a chunk of grass blocks
     *
     * @return A [ByteArray] representing the chunks raw block data
     */
    fun generateGrassChunk(): ByteArray {
        return ByteArray(CHUNK_SIZE * CHUNK_SIZE) { GRASS_BLOCK }
    }
}