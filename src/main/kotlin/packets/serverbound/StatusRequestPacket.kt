package packets.serverbound

import io.ktor.utils.io.core.*
import packets.ServerboundPacket

class StatusRequestPacket: ServerboundPacket(0x00) {
    override fun deserialize(data: ByteReadPacket): ServerboundPacket {
        return StatusRequestPacket()
    }

    companion object {
        fun deserialize(data: ByteReadPacket): StatusRequestPacket {
            return StatusRequestPacket()
        }
    }
}