package packets.serverbound

import io.ktor.utils.io.core.*
import packets.ServerboundPacket

data class PingRequestPacket(val payload: Long) : ServerboundPacket(0x01) {
    override fun deserialize(data: ByteReadPacket): ServerboundPacket {
        return deserialize(data)
    }

    companion object {
        fun deserialize(data: ByteReadPacket): PingRequestPacket {
            val payload = data.readLong()
            return PingRequestPacket(payload)
        }
    }
}