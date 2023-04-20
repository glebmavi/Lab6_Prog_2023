package clientUtils

import kotlinx.serialization.json.Json
import utils.Answer
import utils.AnswerType
import utils.Query
import utils.QueryType
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

class ConnectionManager {
    private var port = 6789
    private var host = InetAddress.getLocalHost()
    private val timeout = 5000

    private val datagramChannel = DatagramChannel.open()

    fun connect(host: String, port: Int) : Boolean {
        this.host = InetAddress.getByName(host)
        this.port = port
        datagramChannel.configureBlocking(false)

        return ping() < timeout
    }

    fun ping() : Double {
        val query = Query(QueryType.PING, "Ping", mapOf())
        send(query)
        val startTime = System.nanoTime()
        val data = ByteBuffer.wrap(ByteArray(4096))
        var elapsedTimeInMs: Double
        do {
            val received = datagramChannel.receive(data)
            elapsedTimeInMs = (System.nanoTime() - startTime).toDouble() / 1000000
        } while ((received == null) and (elapsedTimeInMs < timeout))

        println("Ping with server: $elapsedTimeInMs ms")
        return elapsedTimeInMs
    }

    fun checkedSendReceive(query: Query) : Answer{
        send(query)
        val startTime = System.nanoTime()
        val data = ByteBuffer.wrap(ByteArray(4096))
        var elapsedTimeInMs: Double
        var received: SocketAddress?
        do {
            received = datagramChannel.receive(data)
            elapsedTimeInMs = (System.nanoTime() - startTime).toDouble() / 1000000
        } while ((received == null) and (elapsedTimeInMs < timeout))

        if (received == null) {
            return Answer(AnswerType.ERROR, "No server connection")
        }
        val jsonAnswer = data.array().decodeToString().replace("\u0000", "")
        println("Received: $jsonAnswer")
        return Json.decodeFromString(Answer.serializer(), jsonAnswer)
    }

    fun send(query: Query) {
        println("Sending query to $host:$port")
        val jsonQuery = Json.encodeToString(Query.serializer(), query)
        println("Sending: $jsonQuery")
        val data = ByteBuffer.wrap(jsonQuery.toByteArray())
        val address = InetSocketAddress(host, port)
        datagramChannel.send(data, address)
    }

    fun receive(): Answer {
        val data = ByteBuffer.wrap(ByteArray(4096))
        datagramChannel.receive(data)
        val jsonAnswer = data.array().decodeToString().replace("\u0000", "")
        if (jsonAnswer == "") {
            return Answer(AnswerType.ERROR, "")
        }
        println("Received: $jsonAnswer")
        return Json.decodeFromString(Answer.serializer(), jsonAnswer)
    }

}