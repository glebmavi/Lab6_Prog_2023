package serverUtils

import collection.CollectionManager
import commands.CommandInvoker
import commands.CommandReceiver
import commands.consoleCommands.*
import utils.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Class that handles commands and provides them all needed parameters
 * @property connectionManager
 * @property fileManager Used for loading data to collection
 * @property collectionManager
 * @property commandInvoker
 * @property commandReceiver
 */
class Console {
    private val connectionManager = ConnectionManager()
    private val fileManager = FileManager()

    private val collectionManager = CollectionManager()

    private val commandInvoker = CommandInvoker(connectionManager)
    private val commandReceiver = CommandReceiver(collectionManager, connectionManager)

    private val logger: Logger = LogManager.getLogger(Console::class.java)

    /**
     * Registers commands and waits for user prompt
     */
    fun initialize() {
        logger.trace("Initializing the server")

        commandInvoker.register("info", Info(commandReceiver))
        logger.trace("Command 'info' registered")

        commandInvoker.register("show", Show(commandReceiver))
        logger.trace("Command 'show' registered")

        commandInvoker.register("add", Add(commandReceiver))
        logger.trace("Command 'add' registered")

        commandInvoker.register("update_id", Update(commandReceiver))
        logger.trace("Command 'update_id' registered")

        commandInvoker.register("remove_by_id", RemoveID(commandReceiver))
        logger.trace("Command 'remove_by_id' registered")

        commandInvoker.register("clear", Clear(commandReceiver))
        logger.trace("Command 'clear' registered")

        commandInvoker.register("add_if_min", AddMin(commandReceiver))
        logger.trace("Command 'add_if_min' registered")

        commandInvoker.register("remove_greater", RemoveGreater(commandReceiver))
        logger.trace("Command 'remove_greater' registered")

        commandInvoker.register("remove_lower", RemoveLower(commandReceiver))
        logger.trace("Command 'remove_lower' registered")

        commandInvoker.register("remove_any_by_chapter", RemoveAnyChapter(commandReceiver))
        logger.trace("Command 'remove_any_by_chapter' registered")

        commandInvoker.register("count_by_melee_weapon", CountByMeleeWeapon(commandReceiver))
        logger.trace("Command 'count_by_melee_weapon' registered")

        commandInvoker.register("filter_by_chapter", FilterByChapter(commandReceiver))
        logger.trace("Command 'filter_by_chapter' registered")

        fileManager.load(collectionManager)
        logger.trace("Collection loaded")

        connectionManager.startServer("localhost", 6789)
        logger.trace("Server started")
    }

    fun startInteractiveMode() {
        logger.trace("The server is ready to receive commands")
        var executeFlag:Boolean? = true

        do {
            try {
                val query = connectionManager.receive()

                when (query.queryType) {
                    QueryType.COMMAND_EXEC -> {
                        logger.trace("Received command: ${query.information}")
                        commandInvoker.executeCommand(query)
                        executeFlag = commandInvoker.getCommandMap()[query.information]?.getExecutionFlag()

                    }
                    QueryType.INITIALIZATION -> {
                        logger.trace("Received initialization request")
                        val answer = Answer(AnswerType.INIT, commandInvoker.getCommandMap().keys.joinToString(" "))
                        connectionManager.send(answer)
                    }
                    QueryType.PING -> {
                        logger.trace("Received ping request")
                        val answer = Answer(AnswerType.SYSTEM, "Pong")
                        connectionManager.send(answer)
                    }
                }
            } catch (e:Exception) {
                logger.error("Error while executing command: ${e.message}")
                val answer = Answer(AnswerType.ERROR, e.message.toString())
                connectionManager.send(answer)
            }

        } while (executeFlag != false)
    }
}
