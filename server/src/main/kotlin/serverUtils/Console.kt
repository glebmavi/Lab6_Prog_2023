package serverUtils

import collection.CollectionManager
import commands.CommandInvoker
import commands.CommandReceiver
import commands.consoleCommands.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import utils.*
import java.nio.channels.DatagramChannel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector


/**
 * Class that handles user commands and provides them all the necessary parameters
 * @property connectionManager Manages connections to the server
 * @property fileManager Used for loading data to the collection
 * @property collectionManager Manages the collection of objects
 * @property commandInvoker Invokes commands that operate on the collection
 * @property commandReceiver Receives commands and executes them
 */
class Console {
    private val connectionManager = ConnectionManager("localhost", 6789)
    private val fileManager = FileManager()
    private val collectionManager = CollectionManager()

    private val commandInvoker = CommandInvoker(connectionManager)
    private val commandReceiver = CommandReceiver(collectionManager, connectionManager)

    private val logger: Logger = LogManager.getLogger(Console::class.java)
    private var executeFlag = true

    val selector = Selector.open()
    //private val inputChannel = newChannel(inputManager.inputStream)

    /**
     * Registers commands and waits for user prompt
     */
    fun initialize() {
        logger.info("Initializing the server")

        commandInvoker.register("info", Info(commandReceiver))
        logger.debug("Command 'info' registered")

        commandInvoker.register("show", Show(commandReceiver))
        logger.debug("Command 'show' registered")

        commandInvoker.register("add", Add(commandReceiver))
        logger.debug("Command 'add' registered")

        commandInvoker.register("update_id", Update(commandReceiver))
        logger.debug("Command 'update_id' registered")

        commandInvoker.register("remove_by_id", RemoveID(commandReceiver))
        logger.debug("Command 'remove_by_id' registered")

        commandInvoker.register("clear", Clear(commandReceiver))
        logger.debug("Command 'clear' registered")

        commandInvoker.register("add_if_min", AddMin(commandReceiver))
        logger.debug("Command 'add_if_min' registered")

        commandInvoker.register("remove_greater", RemoveGreater(commandReceiver))
        logger.debug("Command 'remove_greater' registered")

        commandInvoker.register("remove_lower", RemoveLower(commandReceiver))
        logger.debug("Command 'remove_lower' registered")

        commandInvoker.register("remove_any_by_chapter", RemoveAnyChapter(commandReceiver))
        logger.debug("Command 'remove_any_by_chapter' registered")

        commandInvoker.register("count_by_melee_weapon", CountByMeleeWeapon(commandReceiver))
        logger.debug("Command 'count_by_melee_weapon' registered")

        commandInvoker.register("filter_by_chapter", FilterByChapter(commandReceiver))
        logger.debug("Command 'filter_by_chapter' registered")

        fileManager.load(collectionManager)
        logger.info("Collection loaded")

        connectionManager.startServer("localhost", 6789)

        //connectionManager.datagramChannel.register(selector, SelectionKey.OP_READ)
    }

    /**
     * Enters interactive mode and waits for incoming queries
     */
    fun startInteractiveMode() {
        logger.info("The server is ready to receive commands")
        connectionManager.datagramChannel.register(selector, SelectionKey.OP_READ)

        while (executeFlag) {
            selector.select()
            val selectedKeys = selector.selectedKeys()
            val iter = selectedKeys.iterator()
            while (iter.hasNext()) {
                val key = iter.next()
                if (key.isReadable) {
                    val client = key.channel() as DatagramChannel
                    try {
                        connectionManager.datagramChannel = client
                        val query = connectionManager.receive()

                        when (query.queryType) {
                            QueryType.COMMAND_EXEC -> {
                                logger.info("Received command: ${query.information}")
                                commandInvoker.executeCommand(query)
                                //executeFlag = commandInvoker.getCommandMap()[query.information]?.getExecutionFlag()

                            }
                            QueryType.INITIALIZATION -> {
                                logger.info("Received initialization request")
                                val answer = Answer(AnswerType.INIT, commandInvoker.getCommandMap().keys.joinToString(" "))
                                connectionManager.send(answer)
                            }
                            QueryType.PING -> {
                                logger.info("Received ping request")
                                val answer = Answer(AnswerType.SYSTEM, "Pong")
                                connectionManager.send(answer)
                            }
                        }
                    } catch (e:Exception) {
                        logger.error("Error while executing command: ${e.message}")
                        val answer = Answer(AnswerType.ERROR, e.message.toString())
                        connectionManager.send(answer)
                    }
                }
                iter.remove()
            }
        }
        connectionManager.datagramChannel.close()
        selector.close()
        logger.info("Closing server")
    }

    fun stop() {
        executeFlag = false
        selector.wakeup()
    }

    fun save() {
        val saver = Saver()
        try {
            saver.save("", collectionManager)
            logger.info("Collection saved successfully")
        } catch (e:Exception) {
            logger.warn("Collection was not saved: ${e.message}")
        }
    }
}
