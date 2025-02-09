package com.aznos

import com.aznos.util.VarInt
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import kotlin.math.exp

fun main() {
    val varIntUtil = VarInt()

    //VAR INT TEST
    val expectedVarInt = 300
    val varIntBytes = byteArrayOf(0xAC.toByte(), 0x02.toByte())
    val varIntInput = ByteArrayInputStream(varIntBytes)
    val readVarInt = with(varIntUtil) { varIntInput.readVarInt() }

    println("Expected VarInt: $expectedVarInt, Read VarInt: $readVarInt")
    if(readVarInt != expectedVarInt) {
        println("VarInt test failed")
    } else {
        println("VarInt test passed")
    }

    //READ STRING TEST
    val expectedString = "Hello"
    val stringBytes = expectedString.toByteArray(Charsets.UTF_8)
    val encodedString = byteArrayOf(0x05) + stringBytes
    val stringInput = ByteArrayInputStream(encodedString)
    val readString = with(varIntUtil) { stringInput.readString() }

    println("Expected String: $expectedString, Read String: $readString")
    if(readString != expectedString) {
        println("Read string test failed")
    } else {
        println("Read string test passed")
    }
}