package commands

import commands.consoleCommands.Command
import utils.Answer
import utils.AnswerType
import serverUtils.ConnectionManager
import utils.Query

/**
 * Command invoker
 * 
 * Class that handles commands and call its execution
 *
 * @constructor Create Command invoker
 */
class CommandInvoker(private val connectionManager: ConnectionManager) {
    private var commandMap:Map<String, Command> = mapOf()
    private var commandsHistory:Array<String> = arrayOf()

    /**
     * Register
     *
     * Add commands to [commandMap]
     *
     * @param name Name of the command in its console representation
     * @param command A [Command] object
     */
    fun register(name: String, command: Command) {
        commandMap += name to command
    }

    /**
     * Execute command
     *
     * Executes command with provided arguments
     *
     * @param query A single line string split into command and argument
     */
    fun executeCommand(query: Query) {
        try {
            val commandName = query.info
            commandsHistory += commandName

            val command: Command = commandMap[commandName]!!
            command.execute(query.args)

        } catch (e:Error) {
            val answer = Answer(AnswerType.ERROR, e.toString())
            connectionManager.send(answer)
        }
    }

    /**
     * Get commands list
     *
     * @return
     */
    fun getCommandMap() : Map<String, Command> {
        return commandMap
    }
}