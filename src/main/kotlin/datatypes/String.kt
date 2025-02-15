package com.aznos.datatypes

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.datatypes.VarInt.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.String

object String {
    fun BytePacketBuilder.writeString(string: String) {
        val bytes = string.toByteArray()
        writeVarInt(bytes.size)
        writeFully(bytes)
    }

    fun ByteReadPacket.readString(max: Int = -1): String {
        val size = readVarInt().toInt()
        return when {
            max == -1 -> readBytes(size).decodeToString()
            size > max -> error("The string size is larger than the supported: $max")
            else -> readBytes(size).decodeToString()
        }
    }
}