package com.aznos.datatypes

import io.ktor.utils.io.core.*
import java.util.UUID

object UUID {
    fun BytePacketBuilder.writeUUID(uuid: UUID) {
        writeLong(uuid.mostSignificantBits)
        writeLong(uuid.leastSignificantBits)
    }

    public fun ByteReadPacket.readUUID(): UUID {
        val mostSignificantBits = readLong()
        val leastSignificantBits = readLong()

        return UUID(mostSignificantBits, leastSignificantBits)
    }
}