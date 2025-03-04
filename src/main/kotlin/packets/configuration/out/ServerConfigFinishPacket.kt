package com.aznos.packets.configuration.out

import com.aznos.packets.Packet

/**
 * This packet is sent when you want to finish the CONFIGURATION phase and switches to the PLAY phase
 */
class ServerConfigFinishPacket : Packet(0x03)