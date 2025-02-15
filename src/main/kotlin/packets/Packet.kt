package packets

import kotlinx.serialization.Serializable

@Serializable
open class Packet(open val id: Int) {
    open fun serialize(): ByteArray {
        return byteArrayOf()
    }
}