package commands.consoleCommands

import commands.CommandReceiver
import serverUtils.Validator
import exceptions.InvalidArgumentException

/**
 * Clear command
 *
 * Clears all elements in the collection
 * 
 * @property collection
 * @constructor Create command Clear
 */
class Clear() : Command() {

    private lateinit var commandReceiver: CommandReceiver
    constructor(commandReceiver: CommandReceiver) : this() {
        this.commandReceiver = commandReceiver
    }
    override fun getInfo(): String {
        return "Clears all elements in the collection"
    }

    /**
     * Calls [CommandReceiver.clear]
     */
    override fun execute(args: Map<String, String>) {
        if (Validator.verifyArgs(0, args)) {
            commandReceiver.clear()
        } else throw InvalidArgumentException("Invalid arguments were entered. Use HELP command to check")
    }
}