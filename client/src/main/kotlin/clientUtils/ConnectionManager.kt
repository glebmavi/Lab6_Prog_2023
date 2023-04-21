package clientUtils

import kotlinx.serialization.json.Json
import utils.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


class ConnectionManager(private var host: String, private var port: Int) {

    private val timeout = 5000
    private val datagramSocket = DatagramSocket(8080)
    private val outputManager = OutputManager()
    private var hostInetAddress = InetAddress.getByName(host)
    private var datagramPacket = DatagramPacket(ByteArray(4096), 4096, hostInetAddress, port)

    fun connect() : Boolean {
        datagramSocket.soTimeout = timeout
        return ping() < timeout
    }

    fun ping() : Double {
        val query = Query(QueryType.PING, "Ping", mapOf())
        try {
            send(query)
        } catch (e:Exception) {
            outputManager.println(e.message.toString())
            return timeout.toDouble()
        }

        val startTime = System.nanoTime()
        receive()
        val elapsedTimeInMs = (System.nanoTime() - startTime).toDouble() / 1000000
        outputManager.println("Ping with server: $elapsedTimeInMs ms")
        return elapsedTimeInMs
    }

    fun checkedSendReceive(query: Query) : Answer {
        try {
            send(query)
        } catch (e:Exception) {
            return Answer(AnswerType.ERROR, e.message.toString())
        }
        return receive()
    }

    fun send(query: Query) {
        val jsonQuery = Json.encodeToString(Query.serializer(), query)
        val data = jsonQuery.toByteArray()
        hostInetAddress = datagramPacket.address
        port = datagramPacket.port
        outputManager.println("Sending: $jsonQuery \n to $hostInetAddress:$port")
        datagramPacket = DatagramPacket(data, data.size, hostInetAddress, port)
        datagramSocket.send(datagramPacket)
    }

    fun receive(): Answer {
        val data = ByteArray(4096)
        val jsonAnswer : String
        datagramPacket = DatagramPacket(data, data.size)
        try {
            datagramSocket.receive(datagramPacket)
            jsonAnswer = data.decodeToString().replace("\u0000", "")
        } catch (e:Exception) {
            datagramPacket = DatagramPacket(ByteArray(4096), 4096, hostInetAddress, port)
            return Answer(AnswerType.ERROR, e.message.toString())
        }

        outputManager.println("Received: $jsonAnswer")
        return Json.decodeFromString(Answer.serializer(), jsonAnswer)
    }

}