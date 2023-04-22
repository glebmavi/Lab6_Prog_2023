import serverUtils.Console
import kotlin.concurrent.thread

/**
 * Main
 */
fun main() {
    val console = Console()

    console.initialize()

    val thread = thread {
        while (true) {
            when (readlnOrNull()) {
                "exit" -> {
                    console.save()
                    console.stop()
                    break
                }
                "save" -> {
                    console.save()
                }
            }
        }
    }

    console.startInteractiveMode()
    thread.join()
}