package packets.serverbound

import com.aznos.datatypes.String.readString
import com.aznos.datatypes.UUID.readUUID
import io.ktor.utils.io.core.*
import packets.ServerboundPacket
import java.util.*

data class LoginStartPacket(
    val name: String,
    val playeruuid: UUID,
) : ServerboundPacket(0x00) {
    override fun deserialize(data: ByteReadPacket): ServerboundPacket {
        return deserialize(data)
    }

    companion object {
        fun deserialize(data: ByteReadPacket): LoginStartPacket {
            val name = data.readString()
            val playeruuid = data.readUUID()

            return LoginStartPacket(name, playeruuid)
        }
    }
}