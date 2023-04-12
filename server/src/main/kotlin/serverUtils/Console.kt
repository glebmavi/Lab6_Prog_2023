package serverUtils

import collection.CollectionManager
import commands.CommandInvoker
import commands.CommandReceiver
import commands.consoleCommands.*
import utils.*

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
    private val fileManager = FileManager(connectionManager)

    private val collectionManager = CollectionManager()

    private val commandInvoker = CommandInvoker(connectionManager)
    private val commandReceiver = CommandReceiver(collectionManager, connectionManager)

    /**
     * Registers commands and waits for user prompt
     */
    fun initialize() {
        commandInvoker.register("info", Info(commandReceiver))
        commandInvoker.register("show", Show(commandReceiver))
        commandInvoker.register("add", Add(commandReceiver))
        commandInvoker.register("update_id", Update(commandReceiver))
        commandInvoker.register("remove_by_id", RemoveID(commandReceiver))
        commandInvoker.register("clear", Clear(commandReceiver))
        commandInvoker.register("save", Save(commandReceiver))
        commandInvoker.register("exit", Exit(commandReceiver))
        commandInvoker.register("add_if_min", AddMin(commandReceiver))
        commandInvoker.register("remove_greater", RemoveGreater(commandReceiver))
        commandInvoker.register("remove_lower", RemoveLower(commandReceiver))
        commandInvoker.register("remove_any_by_chapter", RemoveAnyChapter(commandReceiver))
        commandInvoker.register("count_by_melee_weapon", CountByMeleeWeapon(commandReceiver))
        commandInvoker.register("filter_by_chapter", FilterByChapter(commandReceiver))

        fileManager.load(collectionManager)

        connectionManager.startServer("localhost", 6789)
    }

    fun startInteractiveMode() {
        var executeFlag:Boolean? = true

        do {
            try {
                val query = connectionManager.receive()

                if (query.queryType == QueryType.COMMAND_EXEC) {
                    commandInvoker.executeCommand(query)
                    executeFlag = commandInvoker.getCommandMap()[query.info]?.getExecutionFlag()

                } else if (query.queryType == QueryType.INITIALIZATION) {
                    val answer = Answer(AnswerType.SYSTEM, commandInvoker.getCommandMap().keys.joinToString(" "))
                    connectionManager.send(answer)
                }
            } catch (e:Exception) {
                val answer = Answer(AnswerType.ERROR, e.message.toString())
                connectionManager.send(answer)
            }

        } while (executeFlag != false)
    }
}
