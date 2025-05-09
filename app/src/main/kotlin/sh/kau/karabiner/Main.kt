package sh.kau.karabiner

import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.modifiers
import kotlinx.serialization.SerialName
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.Long

fun main() {
  val mainRules = createMainRules()

  // Create fn function keys
  val fnFunctionKeys = listOf(
    To(keyCode = KeyCode.MISSION_CONTROL, modifiers = null),
    To(keyCode = KeyCode.LAUNCHPAD, modifiers = null),
    To(keyCode = KeyCode.ILLUMINATION_DECREMENT, modifiers = null),
    To(keyCode = KeyCode.ILLUMINATION_INCREMENT, modifiers = null),
    To(keyCode = KeyCode.FASTFORWARD, modifiers = null)
  ).mapIndexed { index, to ->
    FnFunctionKey(
      from = KeyCode.values().find { it.name == "F${index + 3}" } ?: throw IllegalStateException("F${index + 3} key not found"),
      to = listOf(to)
    )
  }

  // Create device configurations
  val devices = listOf(
    DeviceSpecificSettings(
      identifiers = Identifiers(
        isKeyboard = true,
        isPointingDevice = true,
        productId = 45919,
        vendorId = 1133
      ),
      ignore = false,
      manipulateCapsLockLed = false
    ),
    DeviceSpecificSettings(
      identifiers = Identifiers(
        isPointingDevice = true
      ),
      simpleModifications = listOf(
        SimpleModification(
          from = SimpleModificationKey(keyCode = KeyCode.RIGHT_COMMAND),
          to = listOf(SimpleModificationValue(keyCode = KeyCode.RIGHT_CONTROL))
        )
      )
    ),
    DeviceSpecificSettings(
      identifiers = Identifiers(
        isKeyboard = true,
        productId = 50475,
        vendorId = 1133
      ),
      ignore = true
    )
  )

  val defaultProfile = Profile(
    name = "Default",
    complexModifications = ComplexModifications(
      parameters = Parameters(
        simultaneousThresholdMilliseconds = 250,
        toDelayedActionDelayMilliseconds = 10,
        toIfAloneTimeoutMilliseconds = 250,
        toIfHeldDownThresholdMilliseconds = 500,
      ),
      rules = mainRules
    ),
    fnFunctionKeys = fnFunctionKeys,
    devices = devices,
    selected = true,
    virtualHidKeyboard = VirtualHidKeyboard(
      countryCode = 0,
      keyboardType = "ansi"
    )
  )

  val targetConfig = KarabinerConfig(
    global = GlobalSettings(showInMenuBar = false),
    profiles = listOf(defaultProfile)
  )

  val json = Json {
    prettyPrint = true
    encodeDefaults = true
    explicitNulls = false  // Don't serialize null values
  }
  val jsonString = json.encodeToString(targetConfig)

  val outputFile =
    File("karabiner.json") // This will be relative to where it's run (project root for gradle run)
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

// Add missing classes for the new features
@kotlinx.serialization.Serializable
data class FnFunctionKey(
  val from: KeyCode,
  val to: List<To>
)

@kotlinx.serialization.Serializable
data class SimpleModification(
  val from: SimpleModificationKey,
  val to: List<SimpleModificationValue>
)

@kotlinx.serialization.Serializable
data class SimpleModificationKey(
  @SerialName("key_code")
  val keyCode: KeyCode
)

@kotlinx.serialization.Serializable
data class SimpleModificationValue(
  @SerialName("key_code")
  val keyCode: KeyCode
)