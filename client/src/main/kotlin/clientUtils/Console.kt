package clientUtils

import commands.*
import commands.consoleCommands.*
import exceptions.InvalidInputException
import utils.*

/**
 * Class that handles commands and provides them all needed parameters
 * @property outputManager
 * @property inputManager
 * @property commandInvoker
 * @property commandReceiver
 */

class Console {
    private val connectionManager = ConnectionManager()

    private val outputManager = OutputManager()
    private val inputManager = InputManager(outputManager)

    private val commandInvoker = CommandInvoker(outputManager)
    private val commandReceiver = CommandReceiver(commandInvoker, outputManager, inputManager, connectionManager)

    private var availableCommands = mapOf(
        "info" to Info(commandReceiver),
        "show" to Show(commandReceiver),
        "add" to Add(commandReceiver),
        "update_id" to Update(commandReceiver),
        "remove_by_id" to RemoveID(commandReceiver),
        "clear" to Clear(commandReceiver),
        "save" to Save(commandReceiver),
        "execute_script" to ScriptFromFile(commandReceiver),
        "exit" to Exit(commandReceiver),
        "add_if_min" to AddMin(commandReceiver),
        "remove_greater" to RemoveGreater(commandReceiver),
        "remove_lower" to RemoveLower(commandReceiver),
        "remove_any_by_chapter" to RemoveAnyChapter(commandReceiver),
        "count_by_melee_weapon" to CountByMeleeWeapon(commandReceiver),
        "filter_by_chapter" to FilterByChapter(commandReceiver),
        "help" to Help(commandReceiver)
    )

    fun getConnection() {
        val connected = connectionManager.connect("localhost", 6789)
        if (connected) {
            println("CLIENT:Connected to server")
        } else getConnection()
    }

    /**
     * Registers commands and waits for user prompt
     */
    fun initialize() {
        val query = Query(QueryType.INITIALIZATION, "", mapOf())
        connectionManager.send(query)
        println("CLIENT: Sent initialization query")
        val serverCommands = connectionManager.receive().message.split(" ")
        println("CLIENT: Received commands from server: $serverCommands")

        for (i in serverCommands) {
            if (i in availableCommands.keys) {
                commandInvoker.register(i, availableCommands.getValue(i))
                println("CLIENT: Registered command $i")
            } else {
                commandInvoker.register(i, UnknownCommand(commandReceiver, "This scope will be replaced with the info", 0, mapOf()))
            }
        }

        commandInvoker.register("help", availableCommands.getValue("help"))
        commandInvoker.register("exit", availableCommands.getValue("exit"))
        commandInvoker.register("execute_script", availableCommands.getValue("execute_script"))
    }

    fun startInteractiveMode() {
        var executeFlag:Boolean? = true
        outputManager.surePrint("Waiting for user prompt ...")

        do {
            outputManager.print("$ ")
            try {
                val query = inputManager.read().trim().split(" ")
                if (query[0] != "") {
                    commandInvoker.executeCommand(query)
                    executeFlag = commandInvoker.getCommandMap()[query[0]]?.getExecutionFlag()
                }

            } catch (e: InvalidInputException) {
                outputManager.surePrint(e.message)
                break
            }

            catch (e:Exception) {
                outputManager.surePrint(e.message.toString())
            }

        } while (executeFlag != false)
    }
}
