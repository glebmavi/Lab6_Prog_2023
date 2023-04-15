package commands.consoleCommands

import commands.CommandReceiver
import serverUtils.Validator
import exceptions.InvalidArgumentException
import basicClasses.Command

/**
 * Update command
 *
 * Update values of the element with the provided id
 *
 * @constructor Create command Update
 */
class Update() : Command() {

    private lateinit var commandReceiver: CommandReceiver
    constructor(commandReceiver: CommandReceiver) : this() {
        this.commandReceiver = commandReceiver
    }
    override fun getInfo(): String {
        return "Updates values of the element with the provided id"
    }

    /**
     * Calls [CommandReceiver.updateByID] with provided id
     */
    override fun execute(args: Map<String, String>) {
        if (Validator.verifyArgs(10, args)) {
            try {
                commandReceiver.updateByID(args)
            } catch (e:Exception) {
                throw InvalidArgumentException("Expected an argument but it was not found")
            }
        } else throw InvalidArgumentException("Too many arguments were entered")
    }

}