package commands.consoleCommands

import commands.CommandReceiver
import clientUtils.Validator
import exceptions.InvalidArgumentException

/**
 * Info command
 *
 * Prints info about the collection (type, creation date, amount of elements)
 *
 * @property collection
 * @constructor Create command Info
 */
class Info() : Command() {

    private lateinit var commandReceiver: CommandReceiver
    constructor(commandReceiver: CommandReceiver) : this() {
        this.commandReceiver = commandReceiver
    }
    override fun getInfo(): String {
        return "Prints info about the collection (type, creation date, amount of elements)"
    }

    /**
     * Calls [CommandReceiver.info]
     */
    override fun execute(args: List<String>) {
        if (Validator.verifyArgs(0, args)) {
            commandReceiver.info()
        } else throw InvalidArgumentException("Invalid arguments were entered. Use HELP command to check")
    }
}