package com.aznos.packets.data

import java.util.*

data class LightData(
    val skyLightMask: BitSet,
    val blockLightMask: BitSet,
    val emptySkyLightMask: BitSet,
    val emptyBlockLightMask: BitSet,
    val skyLightArrays: List<ByteArray>,
    val blockLightArrays: List<ByteArray>
)

fun buildLightData(): LightData {
    val sections = 24
    val skyLightMask = BitSet()
    val blockLightMask = BitSet()

    for(i in 0 until sections) {
        skyLightMask.set(i)
        blockLightMask.set(i)
    }

    val emptySkyLightMask = BitSet()
    val emptyBlockLightMask = BitSet()

    val skyLightArrays = List(sections) {
        ByteArray(2048) { 0xFF.toByte() }
    }

    val blockLightArrays = List(sections) {
        ByteArray(2048) { 0xFF.toByte() }
    }

    return LightData(
        skyLightMask, blockLightMask, emptySkyLightMask,
        emptyBlockLightMask, skyLightArrays, blockLightArrays
    )
}