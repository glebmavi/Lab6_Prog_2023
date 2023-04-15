package clientUtils

import kotlinx.serialization.json.Json
import utils.Answer
import utils.Query
import java.net.*
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

class ConnectionManager {
    private var port = 6789
    private var host = InetAddress.getLocalHost()

    private val datagramChannel = DatagramChannel.open()

    fun connect(host: String, port: Int) {
        this.host = InetAddress.getByName(host)
        this.port = port
    }

    fun send(query: Query) {
        println("Sending query to $host:$port")
        val jsonQuery = Json.encodeToString(Query.serializer(), query)
        println("CLIENT: Sending: $jsonQuery")
        val data = ByteBuffer.wrap(jsonQuery.toByteArray())
        val address = InetSocketAddress(host, port)
        datagramChannel.send(data, address)
    }

    fun receive(): Answer {
        val data = ByteBuffer.wrap(ByteArray(4096))
        datagramChannel.receive(data)
        val jsonAnswer = data.array().decodeToString().replace("\u0000", "")
        println("CLIENT: Received: $jsonAnswer")
        return Json.decodeFromString(Answer.serializer(), jsonAnswer)
    }

}