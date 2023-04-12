package commands.consoleCommands

import commands.CommandReceiver
import clientUtils.Validator
import exceptions.InvalidArgumentException
import java.lang.reflect.Type

class UnknownCommand() : Command() {
    private lateinit var commandReceiver: CommandReceiver
    private var info = "This scope will be replaced with the info"
    private var numberOfArgs = 0
    private var argsTypes = mapOf<String, Type>()

    constructor(commandReceiver: CommandReceiver, info: String, numberOfArgs: Int, argsTypes:Map<String, Type>) : this() {
        this.commandReceiver = commandReceiver
        this.info = info
        this.numberOfArgs = numberOfArgs
        this.argsTypes = argsTypes
    }

    override fun getInfo(): String {
        return info
    }

    override fun execute(args: List<String>) {
        if (Validator.verifyArgs(numberOfArgs, argsTypes, args)) {
            commandReceiver.unknownCommand(args)
        } else throw InvalidArgumentException("Invalid arguments were entered. Use HELP command to check")
    }
}