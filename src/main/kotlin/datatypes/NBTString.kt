package datatypes

import io.ktor.utils.io.core.*

object NBTString {
    fun BytePacketBuilder.writeNBTString(text: String) {
        writeByte(8)
        val bytes = text.toByteArray(Charsets.UTF_8)
        writeShort(bytes.size.toShort())
        writeFully(bytes)
    }
}