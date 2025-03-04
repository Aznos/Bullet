package com.aznos.datatypes

import dev.dewy.nbt.api.registry.TagTypeRegistry
import dev.dewy.nbt.tags.TagType
import dev.dewy.nbt.tags.collection.CompoundTag
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*

object NBTType {

    private val tagTypeRegistry = TagTypeRegistry()

    @Throws(IOException::class)
    fun DataInputStream.readNbtCompound(): CompoundTag {
        if (readByte() != TagType.COMPOUND.id)
            throw IOException("Root tag in NBT structure must be a compound tag.")

        val result = CompoundTag()
        result.name = readUTF()
        result.read(this, 0, tagTypeRegistry)

        return result;
    }

    @Throws(IOException::class)
    fun DataOutputStream.writeNbtCompound(tag: CompoundTag) {
        writeByte(TagType.COMPOUND.id.toInt())

        if (tag.name != null) {
            writeUTF(tag.name)
        }

        tag.write(this, 0, tagTypeRegistry)
    }

}

fun CompoundTag.putBoolean(name: String, value: Boolean) = this.putByte(name, if (value) 1 else 0)