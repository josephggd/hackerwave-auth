package com.hackerwave.auth.util

class CommonStrings {
    companion object {
        @JvmStatic val loggerMsg = "|| STATE:{} | DESCR: {} ||"
    }
    enum class FunctionState() {
        ATTEMPT,
        PERMIT,
        FAILURE
    }
}