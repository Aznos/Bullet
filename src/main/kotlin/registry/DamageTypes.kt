package com.aznos.registry

import com.google.gson.JsonParser
import dev.dewy.nbt.tags.collection.CompoundTag
import java.io.InputStreamReader

// We are doing a shortcut to create these, TODO: do them correctly later
class DamageTypes : Registry<DamageType>("minecraft:damage_type") {

    init {
        javaClass.getResourceAsStream("/damageTypes.json")?.let {
            for (entry in JsonParser.parseReader(InputStreamReader(it)).asJsonArray) {
                val value = DamageType(entry.asString)
                register(value.messageId, value)
            }
        }
    }

    override fun asCompound(value: DamageType): CompoundTag {
        val result = CompoundTag()

        result.putString("message_id", value.messageId)
        result.putString("scaling", "never")
        result.putFloat("exhaustion", 0f)

        return result
    }

}

data class DamageType(val messageId: String)