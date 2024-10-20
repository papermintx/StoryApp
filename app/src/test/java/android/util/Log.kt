package android.util


// kelas ini dibuat untuk menangani error pada saat testing
object Log {
    @JvmStatic
    fun d(tag: String, msg: String,  tr: Throwable?): Int {
        val exceptionMessage = tr?.message ?: "No exception"
        println("DEBUG: $tag: $msg, Exception: $exceptionMessage")
        return 0
    }

    @JvmStatic
    fun i(tag: String, msg: String): Int {
        println("INFO: $tag: $msg")
        return 0
    }

    @JvmStatic
    fun w(tag: String, msg: String): Int {
        println("WARN: $tag: $msg")
        return 0
    }

    @JvmStatic
    fun e(tag: String, msg: String): Int {
        println("ERROR: $tag: $msg")
        return 0
    }


    @JvmStatic
    fun v(tag: String, msg: String, tr: Throwable?): Int {
        val exceptionMessage = tr?.message ?: "No exception"
        println("VERBOSE: $tag: $msg, Exception: $exceptionMessage")
        return 0
    }

    @JvmStatic
    fun isLoggable(tag: String, level: Int): Boolean {
        return true
    }
}
