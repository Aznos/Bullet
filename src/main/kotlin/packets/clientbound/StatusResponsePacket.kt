package packets.clientbound

import com.aznos.datatypes.VarInt.writeVarInt
import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import packets.ClientboundPacket
import util.Color

@Serializable
data class StatusResponsePacket(
    override val id: Int = 0x00,
    val version: Version,
    val players: Players,
    val description: Description,
    val favicon: String? = null,
    val enforcesSecureChat: Boolean
) : ClientboundPacket() {
    override fun serialize(): ByteArray {
        val jsonResponse = Json.encodeToString(serializer(), this)
        val jsonBytes = jsonResponse.toByteArray()

        val packet = buildPacket {
            writeVarInt(jsonBytes.size)
            writeFully(jsonBytes)
        }
        return packet.readBytes()
    }

    @Serializable
    data class Version(val name: String, val protocol: Int)

    @Serializable
    data class Players(val max: Int, val online: Int, val sample: List<Sample> = emptyList())

    @Serializable
    data class Sample(val name: String, val id: String)

    @Serializable
    data class Description(val text: String)

    companion object {
        fun createDefault(): StatusResponsePacket {
            return StatusResponsePacket(
                version = Version(name = "1.21.4", protocol = 769),
                players = Players(max = 10, online = 0),
                description = Description(text = "${Color.GOLD.toMinecraftCode()}This server runs as fast as a ${Color.UNDERLINE.toMinecraftCode()}${Color.BOLD.toMinecraftCode()}Bullet"),
                enforcesSecureChat = false
            )
        }
    }
}