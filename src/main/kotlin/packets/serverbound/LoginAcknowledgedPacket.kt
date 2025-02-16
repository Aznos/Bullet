package packets.serverbound

import io.ktor.utils.io.core.*
import packets.ServerboundPacket

data class LoginAcknowledgedPacket(val payload: Long) : ServerboundPacket(0x03) {
    override fun deserialize(data: ByteReadPacket): ServerboundPacket {
        return deserialize(data)
    }

    companion object {
        fun deserialize(data: ByteReadPacket): LoginAcknowledgedPacket {
            val payload = data.readLong()
            return LoginAcknowledgedPacket(payload)
        }
    }
}