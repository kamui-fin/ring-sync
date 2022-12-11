package com.kamui.phonesyncer.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

class IPAddressValidator {
    private val pattern: Pattern = Pattern.compile(IPADDRESS_PATTERN)
    private var matcher: Matcher? = null

    fun validate(ip: String): Boolean {
        return pattern.matcher(ip).matches()
    }

    companion object {
        private const val IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
    }
}
