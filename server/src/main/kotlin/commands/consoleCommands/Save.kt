package commands.consoleCommands

import commands.CommandReceiver
import serverUtils.Validator
import exceptions.InvalidArgumentException
import basicClasses.Command

/**
 * Save command
 *
 * Saves collection data into a file
 *
 * @constructor Create command Save
 */
class Save() : Command() {

    private lateinit var commandReceiver: CommandReceiver
    constructor(commandReceiver: CommandReceiver) : this() {
        this.commandReceiver = commandReceiver
    }

    override fun getInfo(): String {
        return "Сохраняет коллекцию в файл"
    }

    /**
     * Calls [CommandReceiver.save]
     */
    override fun execute(args: Map<String, String>) {
        if (Validator.verifyArgs(0, args)) {
            commandReceiver.save("")
            TODO()
        } else throw InvalidArgumentException("Too many arguments were entered")
    }
}