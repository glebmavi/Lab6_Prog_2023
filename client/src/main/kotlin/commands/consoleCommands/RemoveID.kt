package commands.consoleCommands

import commands.CommandReceiver
import clientUtils.Validator
import exceptions.InvalidArgumentException

/**
 * Remove id command
 *
 * Deletes the element with the provided id
 *
 * @property collection
 * @constructor Create command remove_id
 */
class RemoveID() : Command() {

    private lateinit var commandReceiver: CommandReceiver
    constructor(commandReceiver: CommandReceiver) : this() {
        this.commandReceiver = commandReceiver
    }
    override fun getInfo(): String {
        return "Deletes the element with the provided id"
    }

    /**
     * Calls [CommandReceiver.removeByID]
     */
    override fun execute(args: List<String>) {
        if (Validator.verifyArgs(1, args)) {
            try {
                commandReceiver.removeByID(args[0])
            } catch (e:Exception) {
                throw InvalidArgumentException("Expected an argument but it was not found")
            }
        } else throw InvalidArgumentException("Invalid arguments were entered. Use HELP command to check")
    }


}