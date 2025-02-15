package packets.serverbound

import com.aznos.datatypes.String.readString
import com.aznos.datatypes.VarInt.readVarInt
import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import packets.ServerboundPacket

@Serializable
data class HandshakePacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: UShort,
    val nextState: Int
) : ServerboundPacket(0x00) {

    override fun deserialize(data: ByteReadPacket): ServerboundPacket {
        return Companion.deserialize(data)
    }

    companion object {
        fun deserialize(data: ByteReadPacket): HandshakePacket {
            val protocolVersion = data.readVarInt().toInt()
            val serverAddress = data.readString()
            val serverPort = data.readShort().toUShort()
            val nextState = data.readVarInt().toInt()

            return HandshakePacket(
                protocolVersion = protocolVersion,
                serverAddress = serverAddress,
                serverPort = serverPort,
                nextState = nextState
            )
        }
    }
}