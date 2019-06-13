package com.blub.sofi.extensions

inline fun <T> T.runIf(value: Boolean, block: T.() -> T): T {
    val result = if (value) {
        block()
    } else {
        this
    }

    return result
}