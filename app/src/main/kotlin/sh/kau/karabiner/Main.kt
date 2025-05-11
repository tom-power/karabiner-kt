package sh.kau.karabiner

import java.io.File
import kotlinx.serialization.json.Json

fun main() {

  val jsonEncoder by lazy {
    Json {
      prettyPrint = true
      encodeDefaults = true
      explicitNulls = false // Don't serialize null values
    }
  }

  val defaultProfile =
      Profile(
          name = "Default",
          selected = true,
          //          fnFunctionKeys = functionKeys(),
          complexModifications = ComplexModifications(rules = createMainRules()),
          virtualHidKeyboard = VirtualHidKeyboard(countryCode = 0, keyboardType = "ansi"),
          devices = deviceSpecificConfigs(),
          parameters =
              Parameters(
                  simultaneousThresholdMilliseconds = 250,
                  toDelayedActionDelayMilliseconds = 10,
                  toIfAloneTimeoutMilliseconds = 250,
                  toIfHeldDownThresholdMilliseconds = 500,
              ),
      )

  val karabinerJson =
      jsonEncoder.encodeToString(
          KarabinerConfig(
              global = GlobalSettings(showInMenuBar = false),
              profiles = listOf(defaultProfile),
          ),
      )

  // This will be relative to where it's run (project root for gradle run)
  // To ensure it's in karabiner-kt, you might want to adjust the path if running from workspace
  // root
  // For now, let's assume it's run from karabiner-kt directory or handled by gradle paths.
  val outputFile = File("karabiner.json")

  try {
    outputFile.writeText(karabinerJson)
    println("Successfully wrote karabiner.json to ${outputFile.absolutePath}")
  } catch (e: Exception) {
    System.err.println("Error writing karabiner.json: ${e.message}")
    e.printStackTrace()
  }
}

private fun deviceSpecificConfigs(): List<DeviceConfiguration> {

  return listOf(
      DeviceConfiguration(
          identifiers =
              DeviceIdentifier(
                  isKeyboard = true,
                  isPointingDevice = true,
                  productId = 45919,
                  vendorId = 1133,
              ),
          ignore = false,
          manipulateCapsLockLed = false,
      ),
      DeviceConfiguration(
          identifiers = DeviceIdentifier(isPointingDevice = true),
          simpleModifications =
              listOf(
                  SimpleModification(
                      from =
                          SimpleModificationKey(
                              keyCode = KeyCode.RIGHT_COMMAND,
                          ),
                      to =
                          listOf(
                              SimpleModificationValue(
                                  keyCode = KeyCode.RIGHT_CONTROL,
                              ))))),
      DeviceConfiguration(
          identifiers = DeviceIdentifier(isKeyboard = true, productId = 50475, vendorId = 1133),
          ignore = true))
}

// Create fn function keys
internal fun functionKeys(): List<FnFunctionKey> =
    listOf(
        FnFunctionKey(
            from = FromFnKey(KeyCode.F3), to = listOf(To(keyCode = KeyCode.MISSION_CONTROL))),
        FnFunctionKey(from = FromFnKey(KeyCode.F4), to = listOf(To(keyCode = KeyCode.LAUNCHPAD))),
        FnFunctionKey(
            from = FromFnKey(KeyCode.F5),
            to = listOf(To(keyCode = KeyCode.ILLUMINATION_DECREMENT))),
        FnFunctionKey(
            from = FromFnKey(KeyCode.F6),
            to = listOf(To(keyCode = KeyCode.ILLUMINATION_INCREMENT))),
        FnFunctionKey(
            from = FromFnKey(KeyCode.F9), to = listOf(To(consumerKeyCode = "fastforward"))))
