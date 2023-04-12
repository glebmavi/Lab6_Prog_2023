package clientUtils

import java.lang.reflect.Type

class Validator {
    companion object {
        fun verifyArgs (size:Int, args: List<String>):Boolean {
            return args.size == size
        }

        fun verifyArgs (size:Int, argsTypes:Map<String, Type>, args: List<String>):Boolean { //TODO Используется правильный Type?
            if (args.size != size) return false

            for (i in args.indices) {
                if (argsTypes[argsTypes.keys.toList()[i]] != args[i]::class.java) {
                    return false
                }
            }

            return true
        }

        fun verifyList (array: List<String>): Boolean {
            return try {
                (array[0].isNotEmpty()
                        && array[1].isNotEmpty()
                        && array[2].isNotEmpty()
                        && array[3].isNotEmpty()
                        && array[4].isNotEmpty()
                        && array[5].isNotEmpty()
                        && array[6].isNotEmpty())
            } catch (e:Exception) {
                false
            }
        }
    }

}