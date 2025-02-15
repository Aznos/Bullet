package packets.clientbound

import io.ktor.utils.io.core.*
import packets.ClientboundPacket

data class PongResponsePacket(
    override val id: Int = 0x01,
    val payload: Long
) : ClientboundPacket() {
    override fun serialize(): ByteArray {
        val packet = buildPacket {
            writeLong(payload)
        }

        return packet.readBytes()
    }
}