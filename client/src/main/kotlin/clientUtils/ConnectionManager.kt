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
        val data = ByteBuffer.wrap(Json.encodeToString(Query.serializer(), query).toByteArray())
        println("Sending query to $host:$port")
        println(query)
        val address = InetSocketAddress(host, port)
        datagramChannel.send(data, address)
    }

    fun receive(): Answer {
        val data = ByteBuffer.wrap(ByteArray(4096))
        datagramChannel.receive(data)
        return Json.decodeFromString(Answer.serializer(), data.array().decodeToString().trim('\u0000'))
    }

}