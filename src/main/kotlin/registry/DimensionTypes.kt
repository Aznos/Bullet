package com.aznos.registry

import com.aznos.datatypes.putBoolean
import dev.dewy.nbt.tags.collection.CompoundTag

class DimensionTypes : Registry<DimensionType>("minecraft:dimension_type") {

    val overworld = register("minecraft:overworld", DimensionType(
        true,
        true,
        -64,
        384,
        "minecraft:overworld",
        0f,
        false
    ))

    val nether = register("minecraft:the_nether", DimensionType(
        false,
        false,
        0,
        256,
        "minecraft:the_nether",
        0.1f,
        true
    ))

    val end = register("minecraft:the_end", DimensionType(
        false,
        false,
        0,
        256,
        "minecraft:the_end",
        0f,
        false
    )
    )

    override fun asCompound(value: DimensionType): CompoundTag {
        val result = CompoundTag()

        result.putBoolean("has_skylight", value.hasSkylight)
        result.putBoolean("natural", value.natural)
        result.putInt("min_y", value.minY)
        result.putInt("height", value.height)
        result.putString("effects", value.effects)
        result.putFloat("ambient_light", value.ambientLight)
        result.putBoolean("piglin_safe", value.piglinSafe)

        // These have no effect on the client so it doesn't matter what it is
        result.putBoolean("has_ceiling", false)
        result.putBoolean("ultrawarm", false)
        result.putDouble("coordinate_scale", 1.0)
        result.putBoolean("bed_works", false)
        result.putBoolean("respawn_anchor_works", false)
        result.putInt("logical_height", value.height - 1)
        result.putString("infiniburn", "#minecraft:overworld")
        result.putBoolean("has_raids", false)
        result.putInt("monster_spawn_light_level", 0)
        result.putInt("monster_spawn_block_light_limit", 0)

        return result
    }

}

data class DimensionType( // Left out the attributes that has no effect on the client
    val hasSkylight: Boolean,
    val natural: Boolean,
    val minY: Int,
    val height: Int,
    val effects: String,
    val ambientLight: Float,
    val piglinSafe: Boolean
)