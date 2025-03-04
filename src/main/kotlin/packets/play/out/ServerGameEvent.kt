package com.aznos.packets.play.out

import com.aznos.packets.Packet

class ServerGameEvent(event: Byte, value: Float) : Packet(0x23) {
    init {
        wrapper.writeByte(event.toInt())
        wrapper.writeFloat(value)
    }
}