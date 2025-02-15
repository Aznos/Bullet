package packets

import Logger.logger
import PacketRegistry
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.datatypes.VarInt.writeVarInt
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.channels.ClosedChannelException

data class Session(private val socket: Socket) {
    private val input = socket.openReadChannel()
    private val output = socket.openWriteChannel(autoFlush = true)

    private var state: SessionState = SessionState.HANDSHAKE

    suspend fun sendPacket(packet: ClientboundPacket) {
        val data = packet.serialize()
        val packetSize = buildPacket {
            writeVarInt(data.size + 1)
        }.readBytes()

        logger.info("-------------------------")
        logger.info("Sending packet:")
        logger.info(" ID: ${packet.id}")
        logger.info(" Size: ${data.size} bytes")
        logger.info("-------------------------")

        withContext(Dispatchers.IO) {
            output.writeFully(packetSize, 0, packetSize.size)
            output.writeByte(packet.id.toByte())
            output.writeFully(data, 0, data.size)
            output.flush()
        }
    }

    suspend fun readPacket(): ServerboundPacket? {
        return withContext(Dispatchers.IO) {
            try {
                val packetSize = input.readVarInt()
                val packetData = input.readPacket(packetSize.toInt())
                val packetID = packetData.readVarInt().toInt()

                logger.info("-------------------------")
                logger.info("Received packet:")
                logger.info("  ID: $packetID")
                logger.info("  Size: ${packetSize.toInt()} bytes")
                logger.info("  State: $state")
                logger.info("-------------------------")

                PacketRegistry.getPacket(packetID, state, packetData)
            } catch(e: ClosedChannelException) {
                logger.warn("Connection closed while reading a packet.")
                null
            } catch(e: Exception) {
                logger.warn("An exception happened: ${e.message}")
                null
            }
        }
    }

    fun setState(newState: SessionState) {
        logger.info("Changing state to: $newState")
        this.state = newState
    }

    private var isClosed = false
    suspend fun close() {
        withContext(Dispatchers.IO) {
            if(isClosed) return@withContext
            isClosed = true

            try {
                output.flush()
                socket.close()
                logger.info("Closed connection")
            } catch(e: Exception) {
                logger.warn("Error while closing socket: ${e.message}")
            }
        }
    }
}