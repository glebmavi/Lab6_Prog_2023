package commands

import commands.consoleCommands.Command
import utils.Answer
import utils.AnswerType
import serverUtils.ConnectionManager
import utils.Query

/**
 * Class that handles commands and calls their execution.
 * @param connectionManager A [ConnectionManager] object to handle communication with the client.
 */
class CommandInvoker(private val connectionManager: ConnectionManager) {
    private var commandMap:Map<String, Command> = mapOf()
    private var commandsHistory:Array<String> = arrayOf()

    /**
     * Add commands to [commandMap]
     * @param name Name of the command in its console representation
     * @param command A [Command] object
     */
    fun register(name: String, command: Command) {
        commandMap += name to command
    }

    /**
     * Executes the command with the provided [query] argument.
     * @param query A [Query] object containing the command and its arguments.
     */
    fun executeCommand(query: Query) {
        try {
            val commandName = query.information
            commandsHistory += commandName

            val command: Command = commandMap[commandName]!!
            command.execute(query.args)

        } catch (e:Error) {
            val answer = Answer(AnswerType.ERROR, e.toString())
            connectionManager.send(answer)
        }
    }

    /**
     * Get command map.
     * @return The [commandMap].
     */
    fun getCommandMap() : Map<String, Command> {
        return commandMap
    }
}