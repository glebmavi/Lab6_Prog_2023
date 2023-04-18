package clientUtils

import kotlinx.serialization.json.Json
import utils.Answer
import utils.Query
import utils.QueryType
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

class ConnectionManager {
    private var port = 6789
    private var host = InetAddress.getLocalHost()

    private val datagramChannel = DatagramChannel.open()

    fun connect(host: String, port: Int) : Boolean{
        this.host = InetAddress.getByName(host)
        this.port = port

        return ping() <= 5
    }

    fun ping() : Double {
        val query = Query(QueryType.PING, "Ping", mapOf())
        send(query)
        val startTime = System.nanoTime()
        datagramChannel.configureBlocking(false)
        val data = ByteBuffer.wrap(ByteArray(4096))
        var elapsedTimeInSeconds: Double = -0.1
        do {
            val received = datagramChannel.receive(data)
            elapsedTimeInSeconds = (System.nanoTime() - startTime).toDouble() / 1000000000
        } while ((received == null) and (elapsedTimeInSeconds < 5))

        println("CLIENT: Ping with server: $elapsedTimeInSeconds")
        datagramChannel.configureBlocking(true)
        return elapsedTimeInSeconds
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