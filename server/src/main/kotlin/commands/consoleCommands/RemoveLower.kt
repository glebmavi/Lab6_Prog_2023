package commands.consoleCommands

import commands.CommandReceiver
import serverUtils.Validator
import exceptions.InvalidArgumentException
import basicClasses.Command

/**
 * Remove lower command
 *
 * Deletes from collection all elements lower than provided
 *
 * @property collection
 * @constructor Create command Remove lower
 */
class RemoveLower() : Command() {

    private lateinit var commandReceiver: CommandReceiver
    constructor(commandReceiver: CommandReceiver) : this() {
        this.commandReceiver = commandReceiver
    }

    override fun getInfo(): String {
        return "Deletes from collection all elements lower than provided"
    }

    /**
     * Calls [CommandReceiver.removeLower]
     */
    override fun execute(args: Map<String, String>) {
        if (Validator.verifyArgs(1, args)) {
            try {
                commandReceiver.removeLower(args)
            } catch (e:Exception) {
                throw InvalidArgumentException("Expected an argument but it was not found")
            }
        } else throw InvalidArgumentException("Too many arguments were entered")
    }
}