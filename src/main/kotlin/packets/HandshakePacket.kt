package packets

import com.aznos.datatypes.String.readString
import com.aznos.datatypes.VarInt.readVarInt
import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable

@Serializable
data class HandshakePacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: UShort,
    val nextState: Int
) : Packet(0x00) {
    companion object {
        fun deserialize(data: ByteReadPacket): HandshakePacket {
            val protocolVersion = data.readVarInt().toInt()
            val serverAddress = data.readString()
            val serverPort = data.readShort().toUShort()
            val nextState = data.readVarInt().toInt()

            return HandshakePacket(protocolVersion, serverAddress, serverPort, nextState)
        }

        fun register() {
            PacketRegistry.register(0x00, ::deserialize)
        }
    }
}