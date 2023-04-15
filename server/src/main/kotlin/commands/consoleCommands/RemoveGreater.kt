package commands.consoleCommands

import commands.CommandReceiver
import serverUtils.Validator
import exceptions.InvalidArgumentException

/**
 * Remove greater command
 *
 * Deletes from collection all elements greater than provided
 *
 * @property collection
 * @constructor Create command Remove greater
 */
class RemoveGreater() : Command() {

    private lateinit var commandReceiver: CommandReceiver
    constructor(commandReceiver: CommandReceiver) : this() {
        this.commandReceiver = commandReceiver
    }

    override fun getInfo(): String {
        return "Deletes from collection all elements greater than provided"
    }

    /**
     * Calls [CommandReceiver.removeGreater]
     */
    override fun execute(args: Map<String, String>) {
        if (Validator.verifyArgs(1, args)) {
            try {
                commandReceiver.removeGreater(args)
            } catch (e:Exception) {
                throw InvalidArgumentException("Expected an argument but it was not found")
            }

        } else throw InvalidArgumentException("Too many arguments were entered")
    }
}