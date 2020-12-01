package person.mikepatterson.common_utils.time

import java.util.concurrent.TimeUnit

/**
 * This reduces confusion around the usage of Long's for time values
 * The pattern was inspired by a BBC blog about building their standard media player
 * "Primitive Obsession" section: https://www.bbc.co.uk/blogs/internet/entries/768ae4f2-7933-42d7-9a12-c71dfacf12b6
 *
 * TimeUnit exists: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/TimeUnit.html
 * Personally, I find Video Playback involves so many time values being passed around that using 2 parameters instead of 1 is more frustrating/confusing
 *
 * Kotlin 1.3.50 released a new experimental API for Duration and Time Measurement
 * https://blog.jetbrains.com/kotlin/2019/08/kotlin-1-3-50-released/
 * Here's the KEEP: https://github.com/Kotlin/KEEP/issues/190
 * And source: https://github.com/JetBrains/kotlin/tree/aa69933ee0a4f46f611e75d1ae81a6dbb5018391/libraries/stdlib/src/kotlin/time
 *
 * Personally, I think that KEEP is good for inspiration but it's trivial to roll a custom class to define these
 * and avoid the term Duration cause these time values (or midst units) could apply to Progress or Seek Distance or Time To First Frame
 *
 * Name is cause I stumbled across this term while trying to avoid using TimeUnit or TimedValue
 * https://en.wikipedia.org/wiki/Unit_of_time
 */
sealed class MidstUnit(val value: Long) {

    fun asHours() = toHours().value

    fun asMinutes() = toMinutes().value

    fun asSeconds() = toSeconds().value

    fun asMilliseconds() = toMilliseconds().value

    fun asNanoseconds() = toNanoSeconds().value

    fun asHumanReadableString(): String {
        val hours = asHours()
        val minutes = (asMinutes() % SIXTY).toLong()
        val seconds = (asSeconds() % SIXTY).toLong()
        return when {
            hours > 0 -> {
                "$hours:${minutes.padForTimeFormat()}:${seconds.padForTimeFormat()}"
            }
            minutes > 0 -> {
                "$minutes:${seconds.padForTimeFormat()}"
            }
            seconds > 0 -> {
                return "00:${seconds.padForTimeFormat()}"
            }
            else -> {
                if (asMilliseconds() > 500) {
                    "00:01"
                } else {
                    "00:00"
                }
            }
        }
    }

    private fun Long.padForTimeFormat(): String {
        return if (this < 10) {
            "0$this"
        } else {
            toString()
        }
    }

    abstract fun toHours(): Hours

    abstract fun toMinutes(): Minutes

    abstract fun toSeconds(): Seconds

    abstract fun toMilliseconds(): Milliseconds

    abstract fun toNanoSeconds(): Nanoseconds

    abstract fun getTimeUnit(): TimeUnit

    companion object {
        private const val MS_PREFIX = "ms"
        internal const val SIXTY = 60f
        internal const val THOUSAND = 1000f
    }
}

data class Hours(private val hours: Long) : MidstUnit(hours) {

    constructor(hours: Float) : this(hours.toLong())

    override fun toHours(): Hours {
        return this
    }

    override fun toMinutes(): Minutes {
        return Minutes(hours * SIXTY)
    }

    override fun toSeconds(): Seconds {
        return Seconds(hours * SIXTY * SIXTY)
    }

    override fun toMilliseconds(): Milliseconds {
        return Milliseconds(hours * SIXTY * SIXTY * THOUSAND)
    }

    override fun toNanoSeconds(): Nanoseconds {
        return Nanoseconds(hours * SIXTY * SIXTY * THOUSAND * THOUSAND)
    }

    override fun getTimeUnit(): TimeUnit {
        return TimeUnit.DAYS
    }
}

data class Minutes(private val minutes: Long) : MidstUnit(minutes) {

    constructor(minutes: Float) : this(minutes.toLong())

    override fun toHours(): Hours {
        return Hours(minutes / SIXTY)
    }

    override fun toMinutes(): Minutes {
        return this
    }

    override fun toSeconds(): Seconds {
        return Seconds(minutes * SIXTY)
    }

    override fun toMilliseconds(): Milliseconds {
        return Milliseconds(minutes * SIXTY * THOUSAND)
    }

    override fun toNanoSeconds(): Nanoseconds {
        return Nanoseconds(minutes * SIXTY * THOUSAND * THOUSAND)
    }

    override fun getTimeUnit(): TimeUnit {
        return TimeUnit.MINUTES
    }
}

data class Seconds(private val seconds: Long) : MidstUnit(seconds) {

    constructor(seconds: Float) : this(seconds.toLong())

    override fun toHours(): Hours {
        return Hours(seconds / SIXTY / SIXTY)
    }

    override fun toMinutes(): Minutes {
        return Minutes(seconds / SIXTY)
    }

    override fun toSeconds(): Seconds {
        return this
    }

    override fun toMilliseconds(): Milliseconds {
        return Milliseconds(seconds * THOUSAND)
    }

    override fun toNanoSeconds(): Nanoseconds {
        return Nanoseconds(seconds * THOUSAND * THOUSAND)
    }

    override fun getTimeUnit(): TimeUnit {
        return TimeUnit.SECONDS
    }
}

data class Milliseconds(private val milliseconds: Long) : MidstUnit(milliseconds) {

    constructor(milliseconds: Float) : this(milliseconds.toLong())

    override fun toHours(): Hours {
        return Hours(milliseconds / SIXTY / SIXTY / THOUSAND)
    }

    override fun toMinutes(): Minutes {
        return Minutes(milliseconds / SIXTY / THOUSAND)
    }

    override fun toSeconds(): Seconds {
        return Seconds(milliseconds / THOUSAND)
    }

    override fun toMilliseconds(): Milliseconds {
        return this
    }

    override fun toNanoSeconds(): Nanoseconds {
        return Nanoseconds(milliseconds * THOUSAND)
    }

    override fun getTimeUnit(): TimeUnit {
        return TimeUnit.MILLISECONDS
    }
}

data class Nanoseconds(private val nanoseconds: Long) : MidstUnit(nanoseconds) {

    constructor(nanoseconds: Float) : this(nanoseconds.toLong())

    override fun toHours(): Hours {
        return Hours(nanoseconds / SIXTY / SIXTY / THOUSAND / THOUSAND)
    }

    override fun toMinutes(): Minutes {
        return Minutes(nanoseconds / SIXTY  / THOUSAND / THOUSAND)
    }

    override fun toSeconds(): Seconds {
        return Seconds(nanoseconds / THOUSAND / THOUSAND)
    }

    override fun toMilliseconds(): Milliseconds {
        return Milliseconds(nanoseconds / THOUSAND)
    }

    override fun toNanoSeconds(): Nanoseconds {
        return this
    }

    override fun getTimeUnit(): TimeUnit {
        return TimeUnit.NANOSECONDS
    }
}
