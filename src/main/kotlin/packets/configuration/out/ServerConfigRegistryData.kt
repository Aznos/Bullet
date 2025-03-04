package com.aznos.packets.configuration.out

import com.aznos.datatypes.NBTType.writeNbtCompound
import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import com.aznos.registry.Registry
import dev.dewy.nbt.tags.collection.CompoundTag

/**
 * This packet is sent when you want to apply registries to the client
*/
class ServerConfigRegistryData private constructor(
    registry: Registry<*>? = null,
    key: String? = null,
    entries: List<RawEntry>?
) : Packet(0x07) {

    constructor(registry: Registry<*>): this(registry, null, null)
    constructor(key: String, entries: List<RawEntry>): this(null, key, entries)

    init {
        if (registry != null) {
            wrapper.write(registry.cachedNetworkPacket
                ?: error("Cannot serialize unlocked registry"))
        } else {
            wrapper.writeString(key!!)
            wrapper.writeVarInt(entries!!.size)

            for (entry in entries) {
                wrapper.writeString(entry.key)
                wrapper.writeBoolean(true)
                wrapper.writeNbtCompound(entry.value)
            }
        }
    }

    data class RawEntry(val key: String, val value: CompoundTag)
}