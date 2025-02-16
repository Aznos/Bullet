package packets.clientbound

import com.aznos.datatypes.String.writeString
import com.aznos.datatypes.UUID.writeUUID
import com.aznos.datatypes.VarInt.writeVarInt
import io.ktor.utils.io.core.*
import packets.ClientboundPacket
import java.util.*

data class LoginSuccessPacket(
    override val id: Int = 0x02,
    val uuid: UUID,
    val username: String,
    val properties: List<Property>
) : ClientboundPacket() {
    override fun serialize(): ByteArray {
        val packet = buildPacket {
            writeUUID(uuid)
            writeString(username)
            writeVarInt(properties.size)

            properties.forEach { property ->
                writeString(property.name)
                writeString(property.value)

                if(property.signature != null) {
                    writeString(property.signature)
                } else {
                    writeVarInt(-1)
                }
            }
        }

        return packet.readBytes()
    }

    data class Property(val name: String, val value: String, val signature: String?)

    companion object {
        fun createDefault(): LoginSuccessPacket {
            return LoginSuccessPacket(
                uuid = UUID.randomUUID(),
                username = "Player",
                properties = emptyList()
            )
        }
    }
}