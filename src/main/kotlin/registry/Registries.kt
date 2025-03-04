package com.aznos.registry

object Registries {
    private val registries = mutableListOf<Registry<*>>()

    val dimension_type = DimensionTypes()
    val biomes = BiomeTypes()
    val wolf_variant = WolfVariantTypes()
    val damage_type = DamageTypes()

    init {
        register(dimension_type)
        register(biomes)
        register(wolf_variant)
        register(damage_type)
    }

    private fun register(registry: Registry<*>) {
        registries.add(registry)
        registry.lock()
    }
}