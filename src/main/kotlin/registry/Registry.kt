package com.aznos.registry

import com.aznos.datatypes.NBTType.writeNbtCompound
import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import dev.dewy.nbt.tags.collection.CompoundTag
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

abstract class Registry<T>(val key: String) {
    var cachedNetworkPacket: ByteArray? = null; private set
    private val map = mutableMapOf<String, T>()
    private var locked: Boolean = false

    abstract fun asCompound(value: T): CompoundTag

    fun lock() {
        if (locked) return
        locked = true

        val buffer = ByteArrayOutputStream()
        val wrapper = DataOutputStream(buffer)

        wrapper.writeString(key)
        wrapper.writeVarInt(map.size)

        for ((key, value) in map) {
            wrapper.writeString(key)
            wrapper.writeBoolean(true)
            wrapper.writeNbtCompound(asCompound(value))
        }

        cachedNetworkPacket = buffer.toByteArray()
    }

    private fun checkLocked() {
        check(!locked) {
            "Cannot operate on a locked registry"
        }
    }

    fun register(key: String, value: T): T {
        checkLocked()
        map[key] = value
        return value
    }

    fun unregister(key: String): Boolean {
        checkLocked()
        return map.remove(key) != null
    }

    fun lookup(key: String): T? = map[key]
    fun getMap(): Map<String, T> = map.toMap()

}