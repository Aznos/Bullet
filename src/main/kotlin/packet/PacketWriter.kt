package com.aznos.packet

import java.io.OutputStream

/**
 * Provides utility functions for writing primitive types  to an OutputStream
 */
object PacketWriter {
    fun OutputStream.writeInt(value: Int) {
        this.write((value shr 24) and 0xFF)
        this.write((value shr 16) and 0xFF)
        this.write((value shr 8) and 0xFF)
        this.write(value and 0xFF)
    }

    fun OutputStream.writeLong(value: Long) {
        this.writeInt((value shr 32).toInt())
        this.writeInt((value and 0xFFFFFFFFL).toInt())
    }

    fun OutputStream.writeByte(value: Int) {
        this.write(value)
    }

    fun OutputStream.writeBoolean(value: Boolean) {
        this.write(if(value) 1 else 0)
    }

    fun OutputStream.writeVarInt(value: Int) {
        var v = value
        while(true) {
            if((v and 0xFFFFFF80.toInt()) == 0) {
                this.write(v)
                return
            }

            this.write((v and 0x7F) or 0x80)
            v = v ushr 8
        }
    }

    fun OutputStream.writeString(s: String) {
        val bytes = s.toByteArray(Charsets.UTF_8)
        this.writeVarInt(bytes.size)
        this.write(bytes)
    }

    fun OutputStream.writeDouble(value: Double) {
        val longBits = java.lang.Double.doubleToLongBits(value)
        this.writeLong(longBits)
    }
}