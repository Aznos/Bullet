package com.aznos.packets.configuration.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.packets.Packet
import com.google.gson.JsonObject

/**
 * This packet is sent when you want to disconnect the client during the CONFIGURATION phase
 *
 * @param message The reason for the disconnection
 */
class ServerConfigDisconnectPacket(message: String) : Packet(0x02) {
    init {
        val jsonObj = JsonObject()
        jsonObj.addProperty("text", message)

        wrapper.writeString(jsonObj.toString())
    }
}
