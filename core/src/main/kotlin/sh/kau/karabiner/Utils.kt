@file:Suppress("unused")

package sh.kau.karabiner

import kotlinx.serialization.json.Json

fun jsonEncoder(complexModifications: ComplexModifications): String {
    val jsonEncoder = Json {
        prettyPrint = true
        encodeDefaults = true
        explicitNulls = false // Don't serialize null values
    }

    return jsonEncoder.encodeToString<ComplexModifications>(complexModifications)
}