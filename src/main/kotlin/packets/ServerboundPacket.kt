package packets

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable

@Serializable
abstract class ServerboundPacket(val id: Int) {
    abstract fun deserialize(data: ByteReadPacket): ServerboundPacket
}