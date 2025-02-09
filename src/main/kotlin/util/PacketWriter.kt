package com.aznos.util

import java.io.OutputStream

/**
 * Utility class for writing Minecraft protocol data types to an [OutputStream].
 */
object PacketWriter {
    /**
     * Writes an [Int] to the [OutputStream] in big-endian order.
     *
     * @param value The integer to write.
     */
    fun OutputStream.writeInt(value: Int) {
        this.write((value shr 24) and 0xFF)
        this.write((value shr 16) and 0xFF)
        this.write((value shr 8) and 0xFF)
        this.write(value and 0xFF)
    }

    /**
     * Writes a [Boolean] to the [OutputStream].
     *
     * @param value The boolean to write.
     */
    fun OutputStream.writeBoolean(value: Boolean) {
        this.write(if(value) 1 else 0)
    }

    /**
     * Writes a VarInt to the [OutputStream]
     *
     * @see VarInt
     * @param value The integer to write.
     */
    fun OutputStream.writeVarInt(value: Int) {
        var v = value
        while(true) {
            if((v and 0xFFFFFF80.toInt()) == 0) {
                this.write(v)
                return
            }

            this.write((v and 0x7F) or 0x80)
            v = v ushr 7
        }
    }

    /**
     * Writes a UTF-8 [String] to the [OutputStream], prefixed by its VarInt length
     *
     * @param s The string to write.
     */
    fun OutputStream.writeString(s: String) {
        val bytes = s.toByteArray(Charsets.UTF_8)
        this.writeVarInt(bytes.size)
        this.write(bytes)
    }

    /**
     * Writes a [Double] to the [OutputStream] in big-endian order.
     *
     * @param value The double to write.
     */
    fun OutputStream.writeDouble(value: Double) {
        val longBits = java.lang.Double.doubleToLongBits(value)
        this.writeInt(((longBits shr 32) and 0xFFFFFFFFL).toInt())
        this.writeInt((longBits and 0xFFFFFFFFL).toInt())
    }

    /**
     * Writes a single [Byte] to the [OutputStream].
     *
     * @param value The byte to write.
     */
    fun OutputStream.writeByte(value: Int) {
        this.write(value)
    }
}