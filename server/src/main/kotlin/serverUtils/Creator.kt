package serverUtils

import basicClasses.*

/**
 * Creator
 *
 * @constructor Create Creator
 */
class Creator() {
    /**
     * provides to readers/creators. Then creates a Space Marine with user prompt
     *
     * @return [SpaceMarine] object
     */

    fun createSpaceMarine(args:Map<String, String>): SpaceMarine {
        val name = args["name"]!!.trim()
        val coordinates = createCoordinates(args["x"]!!.trim().toDouble(), args["y"]!!.trim().toInt())
        val health = args["health"]!!.trim().toFloat()
        val loyal = args["loyal"]!!.toBooleanStrict()

        val category = enumValueOf<AstartesCategory>(args["category"]!!.uppercase())

        val weapon = enumValueOf<MeleeWeapon>(args["weapon"]!!.uppercase())

        val chapter = createChapter(args["chapter_name"]!!, args["marines_count"]!!.toLong())

        return SpaceMarine(name, coordinates, health, loyal, category, weapon, chapter)
    }

    /**
     * Creates and returns a new [Chapter] object
     * @return [Chapter] from entered values
     */

    fun createChapter(name:String, marinesCount:Long) : Chapter {
        return Chapter(name, marinesCount)
    }

    /**
     * Creates and returns a new [Coordinates] object
     * @return [Coordinates] from entered values
     */

    fun createCoordinates(x:Double, y:Int) : Coordinates {
        return Coordinates(x, y)
    }
}
