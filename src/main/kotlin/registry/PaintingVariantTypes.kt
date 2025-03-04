package com.aznos.registry

import dev.dewy.nbt.tags.collection.CompoundTag

class PaintingVariantTypes : Registry<PaintingVariantType>("minecraft:painting_variant") {

    override fun asCompound(value: PaintingVariantType): CompoundTag {
        val result = CompoundTag()

        return result
    }

}

data class PaintingVariantType(
    val assetId: String,
    val height: Int,
    val width: Int,
)