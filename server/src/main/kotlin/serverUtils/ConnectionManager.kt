package serverUtils

import kotlinx.serialization.json.Json
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import utils.Answer
import utils.Query
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class ConnectionManager {

    private val logger: Logger = LogManager.getLogger(ConnectionManager::class.java)
    private var port = 6789
    private var host = InetAddress.getLocalHost()

    private val datagramSocket = DatagramSocket(port)
    private var datagramPacket = DatagramPacket(ByteArray(4096), 4096)

    fun startServer(host: String, port: Int) {
        this.host = InetAddress.getByName(host)
        this.port = port
    }

    /**
     * reads one line
     * @return the line that was read
     */
    fun receive(): Query {
        val data = ByteArray(4096)
        datagramPacket = DatagramPacket(data, data.size)
        datagramSocket.receive(datagramPacket)
        val jsonQuery = data.decodeToString().replace("\u0000", "")
        logger.trace("Received: $jsonQuery")
        return Json.decodeFromString(Query.serializer(), jsonQuery)
    }

    fun send(answer: Answer) {
        logger.trace("Sending answer to {}:{}", host, port)
        logger.trace("Sending: ${Json.encodeToString(Answer.serializer(), answer)}")
        val data = Json.encodeToString(Answer.serializer(), answer).toByteArray()

        host = datagramPacket.address
        port = datagramPacket.port
        datagramPacket = DatagramPacket(data, data.size, host, port)
        datagramSocket.send(datagramPacket)
    }
}