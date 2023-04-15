package commands.consoleCommands

import commands.CommandReceiver
import serverUtils.Validator
import exceptions.InvalidArgumentException


/**
 * Add command
 */
class Add() : Command() {

    private lateinit var commandReceiver: CommandReceiver
    constructor(commandReceiver: CommandReceiver) : this() {
        this.commandReceiver = commandReceiver
    }

    override fun getInfo(): String {
        return "Adds a new element into the collection"
    }

    /**
     * Calls [CommandReceiver.add]
     */
    override fun execute(args: Map<String, String>) {
        if (Validator.verifyArgs(9, args)) {
            commandReceiver.add(args)
        } else throw InvalidArgumentException("Invalid arguments were entered. Use HELP command to check")
    }
}
