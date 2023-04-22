package commands.consoleCommands

import commands.CommandReceiver


class UnknownCommand() : Command() {
    private lateinit var commandReceiver: CommandReceiver
    private var name = "unknown_command"
    private var info = "This scope will be replaced with the info"
    private var argsTypes = mapOf<String, String>()

    constructor(commandReceiver: CommandReceiver, name: String, info: String, argsTypes:Map<String, String>) : this() {
        this.commandReceiver = commandReceiver
        this.name = name
        this.info = info
        this.argsTypes = argsTypes
    }

    override fun getInfo(): String {
        return info
    }

    override fun execute(args: List<String>) {
            commandReceiver.unknownCommand(name, argsTypes)
    }
}
