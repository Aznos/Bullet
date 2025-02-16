package packets.clientbound

import datatypes.NBTString.writeNBTString
import io.ktor.utils.io.core.*
import packets.ClientboundPacket

data class ConfigurationDisconnectPacket(
    override val id: Int = 0x02,
    val reason: String
) : ClientboundPacket() {
    override fun serialize(): ByteArray {
        val builder = BytePacketBuilder()
        builder.writeNBTString(reason)

        return builder.build().readBytes()
    }
}