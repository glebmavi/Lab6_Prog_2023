package commands.consoleCommands

import commands.CommandReceiver
import clientUtils.Validator
import exceptions.InvalidArgumentException

/**
 * Filter by chapter command
 *
 * Prints elements with the provided chapter
 *
 * @property collection
 * @constructor Create command Filter by chapter
 */
class FilterByChapter() : Command() {

    private lateinit var commandReceiver: CommandReceiver
    constructor(commandReceiver: CommandReceiver) : this() {
        this.commandReceiver = commandReceiver
    }

    override fun getInfo(): String {
        return "Prints elements with the provided chapter"
    }

    /**
     * Calls [CommandReceiver.filterByChapter]
     */
    override fun execute(args: List<String>) {
        if (Validator.verifyArgs(0, args)) {
            commandReceiver.filterByChapter()
        } else throw InvalidArgumentException("Invalid arguments were entered. Use HELP command to check")
    }
}
