package commands.consoleCommands

import commands.CommandReceiver
import clientUtils.Validator
import exceptions.InvalidArgumentException

/**
 * Help command
 *
 * Prints info about all commands or a provided command
 *
 * @constructor Create command Help
 */
class Help() : Command() {

    private lateinit var commandReceiver: CommandReceiver
    constructor(commandReceiver: CommandReceiver) : this() {
        this.commandReceiver = commandReceiver
    }
    override fun getInfo(): String {
        return "Prints info about all commands or a provided command"
    }

    /**
     * Calls [CommandReceiver.help]
     */
    override fun execute(args: List<String>) {
        if (Validator.verifyArgs(1, args)) {
            commandReceiver.help(args[0])

        } else if (Validator.verifyArgs(0, args)) {
                commandReceiver.help()

        } else throw InvalidArgumentException("Invalid arguments were entered. Use HELP command to check")
    }
}
