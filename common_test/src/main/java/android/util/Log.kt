@file:JvmName("Log")

package android.util

fun d(tag: String, msg: String, t: Throwable): Int {
    println("DEBUG: $tag: $msg")
    return 0
}

fun d(tag: String, msg: String): Int {
    println("DEBUG: $tag: $msg")
    return 0
}

fun e(tag: String, msg: String, t: Throwable): Int {
    println("ERROR: $tag: $msg")
    return 0
}

fun e(tag: String, msg: String): Int {
    println("ERROR: $tag: $msg")
    return 0
}