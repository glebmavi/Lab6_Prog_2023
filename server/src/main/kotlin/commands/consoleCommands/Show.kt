package commands.consoleCommands

import commands.CommandReceiver
import serverUtils.Validator
import exceptions.InvalidArgumentException
import basicClasses.Command

/**
 * Show command
 *
 * Prints all elements of the collection
 *
 * @constructor Create command Show
 */
class Show() : Command() {

    private lateinit var commandReceiver: CommandReceiver
    constructor(commandReceiver: CommandReceiver) : this() {
        this.commandReceiver = commandReceiver
    }
    override fun getInfo(): String {
        return "Prints all elements of the collection"
    }

    /**
     * Calls [CommandReceiver.show]
     */
    override fun execute(args: Map<String, String>) {
        if (Validator.verifyArgs(0, args)) {
            commandReceiver.show()
        } else throw InvalidArgumentException("Too many arguments were entered")
    }

}