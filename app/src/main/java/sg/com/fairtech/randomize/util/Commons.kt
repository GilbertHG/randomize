package sg.com.fairtech.randomize.util

import java.util.*

object Commons {
    fun pickNRandom(lst: List<String?>?, n: Int): List<String?>? {
        val copy: List<String?> = ArrayList(lst)
        Collections.shuffle(copy)
        return if (n > copy.size) copy.subList(0, copy.size) else copy.subList(0, n)
    }
}