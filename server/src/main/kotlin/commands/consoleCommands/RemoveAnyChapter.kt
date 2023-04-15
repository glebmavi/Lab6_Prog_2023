package commands.consoleCommands

import commands.CommandReceiver
import serverUtils.Validator
import exceptions.InvalidArgumentException

/**
 * Remove any chapter command
 *
 * Deletes an element with a provided chapter value
 *
 * @property collection
 * @constructor Create command Remove any chapter
 */
class RemoveAnyChapter() : Command() {

    private lateinit var commandReceiver: CommandReceiver
    constructor(commandReceiver: CommandReceiver) : this() {
        this.commandReceiver = commandReceiver
    }
    override fun getInfo(): String {
        return "Deletes an element with a provided chapter value"
    }

    /**
     * Calls [CommandReceiver.removeByChapter]
     */
    override fun execute(args: Map<String, String>) {
        if (Validator.verifyArgs(2, args)) {
            commandReceiver.removeByChapter(args)
        } else throw InvalidArgumentException("Too many arguments were entered")
    }
}
