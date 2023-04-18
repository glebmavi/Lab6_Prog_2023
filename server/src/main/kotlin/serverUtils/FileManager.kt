package serverUtils

import basicClasses.SpaceMarine
import collection.CollectionManager
import com.charleskorn.kaml.Yaml
import exceptions.NoEnvironmentVariableFound
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.FileReader

/**
 * Class that contains environment variables and handles files
 * @property collectionFileName String containing file name
 */
class FileManager() {

    private val logger: Logger = LogManager.getLogger(FileManager::class.java)

    private val collectionFileName = try {
        System.getenv("COLLECTION")
    } catch (e:Exception) {
        throw NoEnvironmentVariableFound()
    }

    /**
     * Reads data from the file provided in [collectionFileName] and adds objects to [collection]
     * @param collectionManager Current collection
     */
    fun load(collectionManager: CollectionManager) {
        try {
            if (collectionFileName == null) {
                throw NoEnvironmentVariableFound()
            }

            val file = FileReader(collectionFileName)
            val datalist = file.readText().split("#ENDOFSPACEMARINE")
            for (data in datalist) {
                data.trim()
                if (data.isNotBlank()) {
                    val spaceMarine = Yaml.default.decodeFromString(SpaceMarine.serializer(), data)
                    collectionManager.add(spaceMarine)
                }
            }
            file.close()

            logger.trace("Loaded ${collectionManager.getCollection().size} elements successfully")
        } catch (e: Exception) {
            logger.trace(e.message.toString())
        }


    }

}