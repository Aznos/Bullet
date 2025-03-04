package com.aznos.registry

import dev.dewy.nbt.tags.collection.CompoundTag

class WolfVariantTypes : Registry<WolfVariantType>("minecraft:wolf_variant") {

    val ashen = register("minecraft:ashen", WolfVariantType(
        "minecraft:entity/wolf/wolf_ashen",
        "minecraft:entity/wolf/wolf_ashen_tame",
        "minecraft:entity/wolf/wolf_ashen_angry"
    ))

    val black = register("minecraft:black", WolfVariantType(
        "minecraft:entity/wolf/wolf_black",
        "minecraft:entity/wolf/wolf_black_tame",
        "minecraft:entity/wolf/wolf_black_angry"
    ))

    val chestnut = register("minecraft:chestnut", WolfVariantType(
        "minecraft:entity/wolf/wolf_chestnut",
        "minecraft:entity/wolf/wolf_chestnut_tame",
        "minecraft:entity/wolf/wolf_chestnut_angry"
    ))

    val pale = register("minecraft:pale", WolfVariantType(
        "minecraft:entity/wolf/wolf_pale",
        "minecraft:entity/wolf/wolf_pale_tame",
        "minecraft:entity/wolf/wolf_pale_angry"
    ))

    val rusty = register("minecraft:rusty", WolfVariantType(
        "minecraft:entity/wolf/wolf_rusty",
        "minecraft:entity/wolf/wolf_rusty_tame",
        "minecraft:entity/wolf/wolf_rusty_angry"
    ))

    val snowy = register("minecraft:snowy", WolfVariantType(
        "minecraft:entity/wolf/wolf_snowy",
        "minecraft:entity/wolf/wolf_snowy_tame",
        "minecraft:entity/wolf/wolf_snowy_angry"
    ))

    val spotted = register("minecraft:spotted", WolfVariantType(
        "minecraft:entity/wolf/wolf_spotted",
        "minecraft:entity/wolf/wolf_spotted_tame",
        "minecraft:entity/wolf/wolf_spotted_angry"
    ))

    val striped = register("minecraft:striped", WolfVariantType(
        "minecraft:entity/wolf/wolf_striped",
        "minecraft:entity/wolf/wolf_striped_tame",
        "minecraft:entity/wolf/wolf_striped_angry"
    ))

    val woods = register("minecraft:woods", WolfVariantType(
        "minecraft:entity/wolf/wolf_woods",
        "minecraft:entity/wolf/wolf_woods_tame",
        "minecraft:entity/wolf/wolf_woods_angry"
    ))

    override fun asCompound(value: WolfVariantType): CompoundTag {
        val result = CompoundTag()

        result.putString("wild_texture", value.wildTexture)
        result.putString("tame_texture", value.tameTexture)
        result.putString("angry_texture", value.angryTexture)
        result.putString("biomes", "minecraft:plains")

        return result
    }

}

data class WolfVariantType(
    val wildTexture: String,
    val tameTexture: String,
    val angryTexture: String
)