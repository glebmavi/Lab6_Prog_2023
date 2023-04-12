package clientUtils

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class JsonCreator {
    inline fun <reified T> objectToString(clazz: T): String {
        return Json.encodeToString(serializer(), clazz)
    }
}