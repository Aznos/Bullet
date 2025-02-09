package com.aznos.util

import java.io.InputStream

object VarInt {
    /**
     * Reads VarInt from InputStream
     *
     * VarInt is a variable-length 7-bit integer encoding
     * The 7 least significant bits are used to encode the value and the most significant bit is used to indicate if there are more bytes to read
     * VarInts are never longer than 5 bytes
     *
     * @receiver InputStream from which the VarInt is read
     * @return the decoded integer
     * @throws RuntimeException if the VarInt is too big or the end of stream is reached
     */
    fun InputStream.readVarInt(): Int {
        var numRead = 0
        var result = 0

        while(true) {
            val read = this.read()
            if(read == -1) throw RuntimeException("End of stream reached while reading VarInt")

            val value = read and 0x7F
            result = result or (value shl(7 * numRead))
            numRead++

            if(numRead > 5) throw RuntimeException("VarInt is too big")
            if(read and 0x80 == 0) break
        }

        return result
    }

    /**
     * Reads a UTF-8 string from input stream
     * The string is prefixed by a VarInt that indicates the length (in bytes) of the UTF-8 encoded string
     *
     * @receiver InputStream from which the string is read
     * @return decoded string
     */
    fun InputStream.readString(): String {
        val length = this.readVarInt()
        val bytes = ByteArray(length)
        var totalRead = 0

        while(totalRead < length) {
            val read = this.read(bytes, totalRead, length - totalRead)
            if(read == -1) break
            totalRead += read
        }

        return String(bytes, Charsets.UTF_8)
    }
}