package sh.kau.karabiner

import sh.kau.karabiner.ModifierKeyCode.*

// Note: The final karabinerConfig construction and JSON writing will be in Main.kt

fun createSampleRules(): List<KarabinerRule> {
  return listOf(
      karabinerRule {
        description = "Right Cmd -> Ctrl Enter (alone)"
        mapping {
          fromKey = RightCommand
          toKey = RightControl
          toKeyIfAlone = KeyCode.ReturnOrEnter
          forDevice { identifiers = DeviceIdentifier.APPLE_KEYBOARDS }
        }
      },
  )
}
