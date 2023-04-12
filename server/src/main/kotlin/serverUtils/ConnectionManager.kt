package serverUtils

import kotlinx.serialization.json.Json
import utils.Answer
import utils.Query
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class ConnectionManager {
    private var data = ByteArray(4096)

    private var port = 6789
    private var host = InetAddress.getLocalHost()

    private val datagramSocket = DatagramSocket(port)
    private var datagramPacket = DatagramPacket(data, data.size)

    fun startServer(host: String, port: Int) {
        this.host = InetAddress.getByName(host)
        this.port = port
    }

    /**
     * reads one line
     * @return the line that was read
     */
    fun receive(): Query {
        datagramPacket = DatagramPacket(data, data.size)
        datagramSocket.receive(datagramPacket)

        return Json.decodeFromString(Query.serializer(), data.decodeToString().trim('\u0000'))
    }

    fun send(answer: Answer) {
        println("Sending answer to $host:$port")
        println(answer.message)
        data = Json.encodeToString(Answer.serializer(), answer).toByteArray()

        host = datagramPacket.address
        port = datagramPacket.port
        datagramPacket = DatagramPacket(data, data.size, host, port)
        datagramSocket.send(datagramPacket)
    }
}