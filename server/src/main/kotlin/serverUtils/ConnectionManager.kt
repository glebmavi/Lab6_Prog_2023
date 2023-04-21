package serverUtils

import kotlinx.serialization.json.Json
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import utils.Answer
import utils.Query
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

/**
 * Class responsible for managing network connections
 */
class ConnectionManager(private var host: String, private var port: Int) {

    private val logger: Logger = LogManager.getLogger(ConnectionManager::class.java)

    private var address = InetSocketAddress(host, port)

    var datagramChannel = DatagramChannel.open()
    private var buffer = ByteBuffer.allocate(4096)
    private var remoteAddress = InetSocketAddress(port)

    /**
     * Starts the server at given host and port
     */
    fun startServer(host: String, port: Int) {
        this.host = host
        this.port = port
        this.address = InetSocketAddress(host, port)
        datagramChannel.bind(address)
        datagramChannel.configureBlocking(false)
    }

    /**
     * Reads and decodes the incoming query
     * @return Query object
     */
    fun receive(): Query {
        buffer = ByteBuffer.allocate(4096)
        remoteAddress = datagramChannel.receive(buffer) as InetSocketAddress
        val jsonQuery = buffer.array().decodeToString().replace("\u0000", "")
        logger.trace("Received: $jsonQuery")
        return Json.decodeFromString(Query.serializer(), jsonQuery)
    }

    /**
     * Encodes and sends the answer to the client
     */
    fun send(answer: Answer) {
        buffer = ByteBuffer.allocate(4096)
        logger.trace("Sending answer to {}", remoteAddress)
        logger.trace("Sending: ${Json.encodeToString(Answer.serializer(), answer)}")
        val jsonAnswer = Json.encodeToString(Answer.serializer(), answer).toByteArray()
        val data = ByteBuffer.wrap(jsonAnswer)

        datagramChannel.send(data, remoteAddress)
    }
}