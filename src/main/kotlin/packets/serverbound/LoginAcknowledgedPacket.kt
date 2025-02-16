package packets.serverbound

import io.ktor.utils.io.core.*
import packets.ServerboundPacket

class LoginAcknowledgedPacket() : ServerboundPacket(0x03) {
    override fun deserialize(data: ByteReadPacket): ServerboundPacket {
        return deserialize(data)
    }

    companion object {
        fun deserialize(data: ByteReadPacket): LoginAcknowledgedPacket {
            return LoginAcknowledgedPacket()
        }
    }
}