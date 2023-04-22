package clientUtils

class Validator {
    companion object {
        fun verifyArgs (size:Int, args: List<String>):Boolean {
            return args.size == size
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