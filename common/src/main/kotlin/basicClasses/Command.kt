package basicClasses

import kotlinx.serialization.Serializable

/**
 * Command
 *
 * @constructor Create empty Command
 */
@Serializable
abstract class Command {
    private var executionFlag = true
    val name : String = "Command"
    val info : String = "Command Info"

    /**
     * K: Argument name
     * V: Argument type
     */
    val argsTypes = mapOf<String, String>()


    fun setFlag(flag:Boolean) {
        this.executionFlag = flag
    }

    fun getExecutionFlag(): Boolean {
        return executionFlag
    }

    /**
     * Get info
     *
     * @return
     */
    abstract fun getInfo(): String

    /**
     * Execute
     *
     * @return
     */
    abstract fun execute(args: Map<String, String>)
}