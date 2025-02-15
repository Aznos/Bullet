package packets

abstract class ClientboundPacket {
    abstract val id: Int
    abstract fun serialize(): ByteArray
}