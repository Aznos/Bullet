package com.aznos.registry

import com.aznos.datatypes.putBoolean
import dev.dewy.nbt.tags.collection.CompoundTag

class BiomeTypes : Registry<BiomeType>("minecraft:worldgen/biome") {

    val plains = register("minecraft:plains", BiomeType(
        true,
        0f,
        null,
        0f,
        BiomeEffectsType(0, 0, 0, 0))
    )

    override fun asCompound(value: BiomeType): CompoundTag {
        val result = CompoundTag()
        val effects = CompoundTag()

        result.putBoolean("has_precipitation", value.hasPrecipitation)
        result.putFloat("temperature", value.temperature)
        value.temperatureModifier?.let { result.putString("temperature_modifier", it) }
        result.putFloat("downfall", value.downfall)

        effects.putInt("fog_color", value.effects.fogColor)
        effects.putInt("water_color", value.effects.waterColor)
        effects.putInt("water_fog_color", value.effects.waterFogColor)
        effects.putInt("sky_color", value.effects.skyColor)

        result.put<CompoundTag>("effects", effects)

        return result
    }

}

data class BiomeType(
    val hasPrecipitation: Boolean,
    val temperature: Float,
    val temperatureModifier: String? = null,
    val downfall: Float,
    val effects: BiomeEffectsType,
)

data class BiomeEffectsType(
    val fogColor: Int,
    val waterColor: Int,
    val waterFogColor: Int,
    val skyColor: Int,
    // TODO: add the rest of the optional types https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Registry_Data#Biome
)