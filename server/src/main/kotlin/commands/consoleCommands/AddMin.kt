package commands.consoleCommands

import commands.CommandReceiver
import serverUtils.Validator
import exceptions.InvalidArgumentException

/**
 * Add min command
 *
 * Adds a new element into the collection if its value is lower than the lowest element in the collection
 *
 * @property collection
 * @constructor Create command Add min
 */
class AddMin() : Command() {

    private lateinit var commandReceiver: CommandReceiver
    constructor(commandReceiver: CommandReceiver) : this() {
        this.commandReceiver = commandReceiver
    }

    override fun getInfo(): String {
        return "Adds a new element into the collection if its value is lower than the lowest element in the collection"
    }

    /**
     * Calls [CommandReceiver.addMin]
     */
    override fun execute(args: Map<String, String>) {
        if (Validator.verifyArgs(9, args)) {
            commandReceiver.addMin(args)
        } else throw InvalidArgumentException("Too many arguments were entered")
    }
}