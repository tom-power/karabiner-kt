@file:Suppress("unused")

package sh.kau.karabiner

import kotlinx.serialization.json.Json

fun json(): Json {
    return Json {
        prettyPrint = true
        encodeDefaults = true
        explicitNulls = false // Don't serialize null values
    }
}