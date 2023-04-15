package commands.consoleCommands

import commands.CommandReceiver
import serverUtils.Validator
import exceptions.InvalidArgumentException
import basicClasses.Command

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
    override fun execute(args: Map<String, String>) {
        if (Validator.verifyArgs(2, args)) {
            commandReceiver.filterByChapter(args)
        } else throw InvalidArgumentException("Too many arguments were entered")
    }
}
