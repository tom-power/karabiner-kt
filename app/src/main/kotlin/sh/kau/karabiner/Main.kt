package sh.kau.karabiner

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

fun main() {
    val mainRules = createMainRules()

    val karabinerConfig = KarabinerConfig(
        global = GlobalSettings(
            showInMenuBar = false
        ),
        profiles = listOf(
            Profile(
                name = "Default",
                complexModifications = ComplexModifications(
                    parameters = Parameters(
                        simultaneousThresholdMilliseconds = 25L,       // Original had 25
                        toDelayedActionDelayMilliseconds = 10L,    // Original had 10
                        toIfAloneTimeoutMilliseconds = 250L,       // Original had 250
                        toIfHeldDownThresholdMilliseconds = 500L   // Original had 500
                    ),
                    rules = mainRules
                ),
                virtualHidKeyboard = VirtualHidKeyboard(
                    countryCode = 0,
                    keyboardType = "ansi" // from original karabiner.json example, keyboard_type_v2 mapped to keyboardType
                )
                // selected = true, // Add if needed, was commented out in original TS
                // fn_function_keys = listOf(), // Add if needed
                // simple_modifications = listOf() // Add if needed
            )
        )
    )

    val json = Json {
        prettyPrint = true
        encodeDefaults = true // Ensure default values are included if not null
    }
    val jsonString = json.encodeToString(karabinerConfig)

    // Output to karabiner-kt/karabiner.json to keep it within the Kotlin project dir
    val outputFile = File("karabiner.json") // This will be relative to where it's run (project root for gradle run)
    // To ensure it's in karabiner-kt, you might want to adjust the path if running from workspace root
    // For now, let's assume it's run from karabiner-kt directory or handled by gradle paths.

    try {
        outputFile.writeText(jsonString)
        println("Successfully wrote karabiner.json to ${outputFile.absolutePath}")
    } catch (e: Exception) {
        System.err.println("Error writing karabiner.json: ${e.message}")
        e.printStackTrace()
    }
}