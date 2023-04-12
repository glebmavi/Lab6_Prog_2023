package commands

import basicClasses.*
import commands.consoleCommands.Command
import clientUtils.Creator
import clientUtils.readers.EnumReader
import clientUtils.*
import utils.*

class CommandReceiver(private val commandInvoker: CommandInvoker,
                      private val outputManager: OutputManager,
                      private val inputManager: InputManager,
                      private val connectionManager: ConnectionManager
) {

    private val creator = Creator(outputManager, inputManager)
    private val enumReader = EnumReader(outputManager, inputManager)
    private val jsonCreator = JsonCreator()

    /**
     * Gets a command map from [commandInvoker], and prints each command's info or info of provided command in arg
     */
    fun help() {
        val commands = commandInvoker.getCommandMap()

        outputManager.println("Help is available for the following commands:")
        for (key in commands.keys) {
            outputManager.println("- ${key.uppercase()}")
        }

        outputManager.println("For information on a command, type HELP {command name}")
        outputManager.println("To get information about each available command, type HELP ALL")
    }


    fun help(arg:String) {
        val commands = commandInvoker.getCommandMap()

        if (arg.lowercase() == "all") {
            commandInvoker.getCommandMap().forEach { (name: String?, command: Command) -> outputManager.println(name.uppercase() + " - " + command.getInfo()) }

        } else {
            outputManager.println(commands[arg.lowercase()]?.getInfo().toString())
        }
    }

    /**
     * Prints retrieved info from collection
     */
    fun info() {
        val query = Query(QueryType.COMMAND_EXEC, "info", mapOf())
        connectionManager.send(query)

        val answer = connectionManager.receive()
        outputManager.println(answer.message)
    }

    /**
     * Prints each element in collection
     */
    fun show() {
        val query = Query(QueryType.COMMAND_EXEC, "show", mapOf())
        connectionManager.send(query)

        val answer = connectionManager.receive()
        outputManager.println(answer.message)
    }

    fun add() {
        val spaceMarine = creator.createSpaceMarine()
        val query = Query(QueryType.COMMAND_EXEC, "add", mapOf("spaceMarine" to jsonCreator.objectToString(spaceMarine)))
        connectionManager.send(query)

        val answer = connectionManager.receive()
        outputManager.println(answer.message)
    }

    /**
     * Searches for a Space Marine with provided id and updates its values
     */
    fun updateByID(id:String) {
        val spaceMarine = creator.createSpaceMarine()

        val query = Query(QueryType.COMMAND_EXEC, "update_id", mapOf("id" to id, "spaceMarine" to jsonCreator.objectToString(spaceMarine)))
        connectionManager.send(query)

        val answer = connectionManager.receive()
        outputManager.println(answer.message)
    }

    fun removeByID(id:String) {
        val query = Query(QueryType.COMMAND_EXEC, "remove_by_id", mapOf("id" to id))
        connectionManager.send(query)

        val answer = connectionManager.receive()
        outputManager.println(answer.message)
    }

    fun clear() {
        val query = Query(QueryType.COMMAND_EXEC, "clear", mapOf())
        connectionManager.send(query)

        val answer = connectionManager.receive()
        outputManager.println(answer.message)
    }

    fun save() {
        val query = Query(QueryType.COMMAND_EXEC, "save", mapOf())
        connectionManager.send(query)

        val answer = connectionManager.receive()
        outputManager.println(answer.message)
    }

    fun executeScript(filepath: String) {
        inputManager.startScriptReader(filepath)
    }

    fun addMin() {
        val spaceMarine = creator.createSpaceMarine()
        val query = Query(QueryType.COMMAND_EXEC, "add_if_min", mapOf("spaceMarine" to jsonCreator.objectToString(spaceMarine)))
        connectionManager.send(query)

        val answer = connectionManager.receive()
        outputManager.println(answer.message)
    }

    /**
     * Removes all elements greater than provided
     */
    fun removeGreater(id: String) {
        val query = Query(QueryType.COMMAND_EXEC, "remove_greater", mapOf("id" to id))
        connectionManager.send(query)

        val answer = connectionManager.receive()
        outputManager.println(answer.message)
    }

    /**
     * Removes all elements lower than provided
     */
    fun removeLower(id: String) {
        val query = Query(QueryType.COMMAND_EXEC, "remove_lower", mapOf("id" to id))
        connectionManager.send(query)

        val answer = connectionManager.receive()
        outputManager.println(answer.message)
    }

    /**
     * Removes first found element with [Chapter] equal to provided
     */
    fun removeByChapter() {
        val chapter = creator.createChapter()
        val query = Query(QueryType.COMMAND_EXEC, "remove_by_chapter", mapOf("chapter" to jsonCreator.objectToString(chapter)))
        connectionManager.send(query)

        val answer = connectionManager.receive()
        outputManager.println(answer.message)
    }

    fun countByWeapon() {
        val weapon = enumReader.read<MeleeWeapon>("Enter Weapon category from the list: ", true)
        val query = Query(QueryType.COMMAND_EXEC, "count_by_melee_weapon", mapOf("weapon" to jsonCreator.objectToString(weapon)))
        connectionManager.send(query)

        val answer = connectionManager.receive()
        outputManager.println(answer.message)
    }

    fun filterByChapter() {
        val chapter = creator.createChapter()
        val query = Query(QueryType.COMMAND_EXEC, "filter_by_chapter", mapOf("chapter" to jsonCreator.objectToString(chapter)))
        connectionManager.send(query)

        val answer = connectionManager.receive()
        outputManager.println(answer.message)
    }

    fun unknownCommand(args: List<String>) {
        outputManager.println("Unknown command")
    }
}