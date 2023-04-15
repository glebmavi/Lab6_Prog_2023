package commands

import basicClasses.*
import collection.CollectionManager
import exceptions.InvalidArgumentException
import serverUtils.*
import utils.*

class CommandReceiver(private val collectionManager: CollectionManager,
                      private val connectionManager: ConnectionManager
) {

    private val creator = Creator()

    /**
     * Prints retrieved info from [collectionManager]
     */
    fun info() {
        try {
            val answer = Answer(AnswerType.OK, collectionManager.getInfo())
            connectionManager.send(answer)
        } catch (e: Exception) {
            val answer = Answer(AnswerType.ERROR, e.message!!)
            connectionManager.send(answer)
        }
    }

    /**
     * Prints each element in [collectionManager]
     */
    fun show() {
        try {
            val answer = Answer(AnswerType.OK, collectionManager.show().joinToString("\n"))
            connectionManager.send(answer)
        } catch (e: Exception) {
            val answer = Answer(AnswerType.ERROR, e.message!!)
            connectionManager.send(answer)
        }
    }

    /**
     * Creates new Space Marine and add it into collection
     */
    fun add(args: Map<String, String>) {
        try {
            val spaceMarine = creator.createSpaceMarine(args)
            collectionManager.add(spaceMarine)
            val answer = Answer(AnswerType.OK, "Space Marine ${spaceMarine.getName()} has been created and added to the collection")
            connectionManager.send(answer)
        } catch (e: Exception) {
            val answer = Answer(AnswerType.ERROR, e.message!!)
            connectionManager.send(answer)
        }
    }

    /**
     * Searches for a Space Marine with provided id and updates its values
     */
    fun updateByID(args: Map<String, String>) {
        val id = args["id"]!!

        try {
            val oldSpaceMarine = collectionManager.getByID(id.toLong())
                ?: throw InvalidArgumentException("No Space Marine with id: $id was found")
            val newSpaceMarine = creator.createSpaceMarine(args)
            collectionManager.update(oldSpaceMarine, newSpaceMarine)
            val answer = Answer(AnswerType.OK, "Space Marine ${oldSpaceMarine.getName()} has been updated")
            connectionManager.send(answer)

        } catch (e: Exception) {
            val answer = Answer(AnswerType.ERROR, e.toString())
            connectionManager.send(answer)
        }
    }

    fun removeByID(args: Map<String, String>) {
        val id = args["id"]!!

        try {
            val spaceMarine = collectionManager.getByID(id.toLong())
                ?: throw InvalidArgumentException("No Space Marine with id: $id was found")

            collectionManager.remove(spaceMarine)
            val answer = Answer(AnswerType.OK, "Space Marine ${spaceMarine.getName()} has been deleted")
            connectionManager.send(answer)

        } catch (e: Exception) {
            val answer = Answer(AnswerType.ERROR, e.toString())
            connectionManager.send(answer)
        }
    }

    fun clear() {
        if (collectionManager.getCollection().size > 0) {
            try {
                collectionManager.clear()
                val answer = Answer(AnswerType.OK, "Collection has been cleared")
                connectionManager.send(answer)
            } catch (e: Exception) {
                val answer = Answer(AnswerType.ERROR, e.toString())
                connectionManager.send(answer)
            }
        } else {
            val answer = Answer(AnswerType.ERROR, "The collection is already empty")
            connectionManager.send(answer)
        }
    }

    fun save(filepath:String) {
        try {
            Saver(connectionManager).save(filepath, collectionManager)
            val answer = Answer(AnswerType.OK, "Collection was saved successfully")
            connectionManager.send(answer)
        } catch (e:Exception) {
            val answer = Answer(AnswerType.ERROR, e.toString())
            connectionManager.send(answer)
        }
    }

    fun addMin(args: Map<String, String>) {
        try {
            val spaceMarine = creator.createSpaceMarine(args)
            if (spaceMarine < collectionManager.getCollection().first()) {
                collectionManager.add(spaceMarine)
                val answer = Answer(AnswerType.OK, "Space Marine ${spaceMarine.getName()} has been created and added to the collection")
                connectionManager.send(answer)
            } else {
                val answer = Answer(AnswerType.ERROR, "Space Marine ${spaceMarine.getName()} has not been added to the collection, because it is not the smallest")
                connectionManager.send(answer)
            }
        } catch (e: Exception) {
            val answer = Answer(AnswerType.ERROR, e.toString())
            connectionManager.send(answer)
        }
    }

    /**
     * Removes all elements greater than provided
     */
    fun removeGreater(args: Map<String, String>) {
        val id = args["id"]
        try {
            val collection = collectionManager.getCollection()
            val spaceMarine = collectionManager.getByID(id!!.toLong())
                ?: throw InvalidArgumentException("No Space Marine with id: $id was found")
            var count = 0

            while (collection.isNotEmpty()) {
                if (collection.last() > spaceMarine) {
                    collectionManager.remove(collection.last())
                    count++
                }
            }
            val answer = Answer(AnswerType.OK, "$count Space Marines have been deleted")
            connectionManager.send(answer)

        } catch (e: Exception) {
            val answer = Answer(AnswerType.ERROR, e.toString())
            connectionManager.send(answer)
        }
    }

    /**
     * Removes all elements lower than provided
     */
    fun removeLower(args: Map<String, String>) {
        val id = args["id"]
        try {
            val collection = collectionManager.getCollection()
            val spaceMarine = collectionManager.getByID(id!!.toLong())
                ?: throw InvalidArgumentException("No Space Marine with id: $id was found")
            var count = 0

            while (collection.isNotEmpty()) {
                if (collection.last() < spaceMarine) {
                    collectionManager.remove(collection.last())
                    count++
                }
            }
            val answer = Answer(AnswerType.OK, when (count) {
                0 -> { "No Space Marines were deleted" }
                1 -> { "Only 1 Space Marine was deleted" }
                else -> { "$count Space Marines have been deleted" }
            })

            connectionManager.send(answer)

        } catch (e: Exception) {
            val answer = Answer(AnswerType.ERROR, e.toString())
            connectionManager.send(answer)
        }
    }

    /**
     * Removes first found element with [Chapter] equal to provided
     */
    fun removeByChapter(args: Map<String, String>) {
        try {
            val chapter = creator.createChapter(args["chapter_name"]!!, args["marines_count"]!!.toLong())

            val collection = collectionManager.getCollection()
            var count = 0

            for (spaceMarine in collection) {
                if (spaceMarine.getChapter() == chapter) {
                    collectionManager.remove(spaceMarine)
                    count++
                }
            }

            val answer = Answer(AnswerType.OK, when (count) {
                0 -> { "No Space Marines with chapter == $chapter was found" }
                1 -> { "Only 1 Space Marine with chapter == $chapter was found and deleted" }
                else -> { "$count Space Marines with chapter == $chapter were found and deleted" }
            })

            connectionManager.send(answer)

        } catch (e: Exception) {
            val answer = Answer(AnswerType.ERROR, e.toString())
            connectionManager.send(answer)
        }

    }

    fun countByWeapon(args: Map<String, String>) {
        try {
            val weapon = MeleeWeapon.valueOf(args["weapon"]!!)
            val collection = collectionManager.getCollection()
            var count = 0

            if (collection.isNotEmpty()) {
                for (spaceMarine in collection) {
                    if (spaceMarine.getWeapon() == weapon) {
                        count++
                    }
                }
                val answer = Answer(AnswerType.OK, when (count) {
                    0 -> { "No Space Marines with weapon == $weapon were found" }
                    1 -> { "Only 1 Space Marine with weapon == $weapon was found" }
                    else -> { "$count Space Marines with weapon == $weapon were found" }
                })

                connectionManager.send(answer)
            } else {
                throw Exception("The collection is empty")
            }
        } catch (e: Exception) {
            val answer = Answer(AnswerType.ERROR, e.toString())
            connectionManager.send(answer)
        }
    }

    fun filterByChapter(args: Map<String, String>) {
        try {
            val chapter = creator.createChapter(args["chapter_name"]!!, args["marines_count"]!!.toLong())
            val filteredList = mutableListOf<SpaceMarine>()

            for (i in collectionManager.filter { e -> e.getChapter()!! == chapter }) {
                filteredList.add(i)
            }

            val answer = Answer(AnswerType.OK, filteredList.joinToString("\n"))
            connectionManager.send(answer)

        } catch (e: Exception) {
            val answer = Answer(AnswerType.ERROR, e.toString())
            connectionManager.send(answer)
        }
    }

}
