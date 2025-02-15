package com.aznos.datatypes

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlin.experimental.and

object VarInt {
    class VarInt(private val value: Int) {
        fun toInt(): Int = value
    }

    private inline fun writeVarInt(varint: VarInt, writeByte: (Byte) -> Unit) {
        var value = varint.toInt()

        while(true) {
            if((value and 0xFFFFFF80.toInt()) == 0) {
                writeByte(value.toByte())
                return
            }

            writeByte(((value and 0x7F) or 0x80).toByte())
            value = value ushr 7
        }
    }

    private inline fun readVarInt(readByte: () -> Byte): VarInt {
        var offset = 0
        var value = 0L
        var byte: Byte

        do {
            if(offset == 35) error("VarInt is too big")

            byte = readByte()
            value = value or ((byte.toLong() and 0x7FL) shl offset)

            offset += 7
        } while((byte and 0x80.toByte()) != 0.toByte())

        return VarInt(value.toInt())
    }

    fun BytePacketBuilder.writeVarInt(varint: Int): Unit = writeVarInt(VarInt(varint))
    fun BytePacketBuilder.writeVarInt(varint: VarInt): Unit = writeVarInt(varint) { writeByte(it) }
    fun ByteReadPacket.readVarInt(): VarInt = readVarInt { readByte() }
    suspend fun ByteWriteChannel.writeVarInt(varInt: VarInt): Unit = writeVarInt(varInt) { writeByte(it) }
    suspend fun ByteReadChannel.readVarInt(): VarInt = readVarInt { readByte() }
}