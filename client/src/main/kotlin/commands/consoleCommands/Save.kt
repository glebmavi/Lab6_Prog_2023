package commands.consoleCommands

import commands.CommandReceiver
import clientUtils.Validator
import exceptions.InvalidArgumentException

/**
 * Save command
 *
 * Saves collection data into a file
 *
 * @constructor Create command Save
 */
class Save(
    val commandReceiver: CommandReceiver
) : Command() {


    override fun getInfo(): String {
        return "Сохраняет коллекцию в файл"
    }

    /**
     * Calls [CommandReceiver.save]
     */
    override fun execute(args: List<String>) {
        if (Validator.verifyArgs(0, args)) {
            commandReceiver.save()
        } else throw InvalidArgumentException("Invalid arguments were entered. Use HELP command to check")
    }
}