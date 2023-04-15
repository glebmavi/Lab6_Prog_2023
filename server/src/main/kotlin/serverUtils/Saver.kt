package serverUtils

import basicClasses.SpaceMarine
import collection.CollectionManager
import com.charleskorn.kaml.Yaml
import exceptions.NoEnvironmentVariableFound
import utils.*
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class Saver(private val connectionManager: ConnectionManager) {
    /**
     * Saves elements of collection into file with provided [filename]
     * @param filename Name of the file
     * @return true if elements were saved, false an exception occurred
     */
    fun save(filename: String, collectionManager: CollectionManager) {
        try {
            val file = if (filename == "") {
                try {
                    FileOutputStream(System.getenv("COLLECTION"))
                } catch (e:Exception) {
                    throw NoEnvironmentVariableFound()
                }
            } else {
                FileOutputStream(filename)
            }

            val output = OutputStreamWriter(file)
            for (element in collectionManager.getCollection()) {
                output.write(Yaml.default.encodeToString(SpaceMarine.serializer(), element))
                output.write("\n#ENDOFSPACEMARINE\n")
            }
            output.close()
        } catch (e: Exception) {
            val answer = Answer(AnswerType.ERROR, e.toString())
            connectionManager.send(answer)
        }
    }
}