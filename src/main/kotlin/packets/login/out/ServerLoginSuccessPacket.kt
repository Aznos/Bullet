package com.aznos.packets.login.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.UUIDType.writeUUID
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import java.util.UUID

/**
 * This packet is sent once the server receives the client login start packet and
 * confirms there are no issues with the login
 *
 * @param uuid The UUID of the player who joined
 * @param username The username of the player who joined
 */
class ServerLoginSuccessPacket(uuid: UUID, username: String) : Packet(0x02) {
    init {
        wrapper.writeUUID(uuid)
        wrapper.writeString(username)
        wrapper.writeVarInt(0)
    }
}
