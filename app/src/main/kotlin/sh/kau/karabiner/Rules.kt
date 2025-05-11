package sh.kau.karabiner

import sh.kau.karabiner.Condition.DeviceIfCondition
import sun.security.util.SignatureUtil.fromKey

// Note: The final karabinerConfig construction and JSON writing will be in Main.kt

fun createMainRules(): List<KarabinerRule> {

  return listOf(
      karabinerRule(
          "Right Cmd (alone) -> Enter",
          ManipulatorBuilder()
              .from(KeyCode.RIGHT_COMMAND, optionalModifiers = listOf(ModifiersKeys.ANY))
              .to(keyCode = KeyCode.RIGHT_CONTROL) // When held
              .toIfAlone(keyCode = KeyCode.RETURN_OR_ENTER) // When pressed alone
              .withCondition(onlyAppleKeyboards()) // Only for Apple or built-in keyboards
              .build(),
      ),
      createCapsLockRules(),
      *createLayerKeyRules(),
      karabinerRule {
        description = "O + 0 -> Raycast Confetti"
        layerKey = KeyCode.O
        fromKey = KeyCode.NUM_0
        shellCommand = "open raycast://extensions/raycast/raycast/confetti"
      },
      karabinerRule {
        description = "O + 1 -> Obsidian"
        layerKey = KeyCode.O
        fromKey = KeyCode.NUM_1
        shellCommand = "open -a Obsidian.app"
      },
      karabinerRule {
        description = "O + 2 -> Google Chrome"
        layerKey = KeyCode.O
        fromKey = KeyCode.NUM_2
        shellCommand = "open -a 'Google Chrome.app'"
      },
      karabinerRule {
        description = "O + 3 -> Warp"
        layerKey = KeyCode.O
        fromKey = KeyCode.NUM_3
        shellCommand = "open -a 'Warp.app'"
      },
      karabinerRule {
        description = "O + 4 -> Cursor"
        layerKey = KeyCode.O
        fromKey = KeyCode.NUM_4
        shellCommand = "open -a 'Cursor.app'"
      },
  )
}

/** --- Caps Lock -> Escape (alone) -> Ctrl (on hold) -> hold + Vim keys -> Arrow/Mouse */
fun createCapsLockRules(): KarabinerRule {
  val manipulators = mutableListOf<Manipulator>()

  // Caps Lock alone -> Escape, held -> right_control
  manipulators.add(
      ManipulatorBuilder()
          .from(KeyCode.CAPS_LOCK, optionalModifiers = listOf(ModifiersKeys.ANY))
          .to(keyCode = KeyCode.RIGHT_CONTROL) // This is the "held" assignment
          .toIfAlone(keyCode = KeyCode.ESCAPE) // This is the "alone" assignment
          .build())

  manipulators.add(
      ManipulatorBuilder()
          .from(
              KeyCode.J,
              mandatoryModifiers = listOf(ModifiersKeys.LEFT_SHIFT, ModifiersKeys.RIGHT_CONTROL),
          )
          .to(keyCode = KeyCode.DOWN_ARROW, modifiers = listOf(ModifiersKeys.LEFT_SHIFT))
          .withCondition(unlessApp("com.google.android.studio", "^com\\\\.jetbrains\\\\..*$"))
          .build())

  manipulators.add(
      ManipulatorBuilder()
          .from(
              KeyCode.J,
              mandatoryModifiers = listOf(ModifiersKeys.LEFT_SHIFT, ModifiersKeys.RIGHT_CONTROL))
          .to(
              keyCode = KeyCode.J,
              modifiers = listOf(ModifiersKeys.LEFT_CONTROL, ModifiersKeys.LEFT_SHIFT))
          .withCondition(forApp("com.google.android.studio", "^com\\\\.jetbrains\\\\..*$"))
          .build())

  // CapLock + Vim keys -> quick arrow keys (along with modifier combinations)
  manipulators.addAll(createVimNavigationManipulators())

  // Mouse control with arrow keys
  listOf(
          Pair(KeyCode.DOWN_ARROW, MouseKey(y = 1536)),
          Pair(KeyCode.UP_ARROW, MouseKey(y = -1536)),
          Pair(KeyCode.LEFT_ARROW, MouseKey(x = -1536)),
          Pair(KeyCode.RIGHT_ARROW, MouseKey(x = 1536)),
      )
      .forEach { (key, mouseKeyValue) ->
        manipulators.add(
            ManipulatorBuilder()
                .from(key, mandatoryModifiers = listOf(ModifiersKeys.RIGHT_CONTROL))
                .to(mouseKey = mouseKeyValue)
                .build())
      }

  // Add return_or_enter + right_control to click mouse buttons
  manipulators.add(
      ManipulatorBuilder()
          .from(KeyCode.RETURN_OR_ENTER, mandatoryModifiers = listOf(ModifiersKeys.RIGHT_CONTROL))
          .to(pointingButton = "button1")
          .build())

  manipulators.add(
      ManipulatorBuilder()
          .from(
              KeyCode.RETURN_OR_ENTER,
              mandatoryModifiers = listOf(ModifiersKeys.LEFT_COMMAND, ModifiersKeys.RIGHT_CONTROL))
          .to(pointingButton = "button2")
          .build())

  return KarabinerRule("Caps Lock -> Escape (alone) | Ctrl (simple)", manipulators)
}

fun createLayerKeyRules(): Array<KarabinerRule> {

  val rules = mutableListOf<KarabinerRule>()

  data class LKM(
      val fromKey: KeyCode,
      val toKey: KeyCode,
      val toModifiers: List<ModifiersKeys?>? = null,
      val conditions: List<Condition>? = null
  )

  listOf(
          // --- mapped to right hand side Shift num keys -
          //   Y U I
          //   ^ & *
          LKM(KeyCode.Y, KeyCode.NUM_6, listOf(ModifiersKeys.LEFT_SHIFT)),
          LKM(KeyCode.U, KeyCode.NUM_7, listOf(ModifiersKeys.LEFT_SHIFT)),
          LKM(KeyCode.I, KeyCode.NUM_8, listOf(ModifiersKeys.LEFT_SHIFT)),

          // special one - \
          LKM(KeyCode.O, KeyCode.BACKSLASH, null),

          // special ones
          //  L ; '
          //  - + =
          LKM(KeyCode.L, KeyCode.HYPHEN, null),
          LKM(KeyCode.SEMICOLON, KeyCode.EQUAL_SIGN, listOf(ModifiersKeys.LEFT_SHIFT)),
          LKM(KeyCode.QUOTE, KeyCode.EQUAL_SIGN, null),

          // J K
          // ( )
          LKM(KeyCode.J, KeyCode.NUM_9, listOf(ModifiersKeys.LEFT_SHIFT)),
          LKM(KeyCode.K, KeyCode.NUM_0, listOf(ModifiersKeys.LEFT_SHIFT)),
          // M ,
          // [ ]
          LKM(KeyCode.M, KeyCode.OPEN_BRACKET, null),
          LKM(KeyCode.COMMA, KeyCode.CLOSE_BRACKET, null),
          // . /
          // { }
          LKM(KeyCode.PERIOD, KeyCode.OPEN_BRACKET, listOf(ModifiersKeys.LEFT_SHIFT)),
          LKM(KeyCode.SLASH, KeyCode.CLOSE_BRACKET, listOf(ModifiersKeys.LEFT_SHIFT)),
      )
      .forEach { (fromKeyP, toKeyP, mods) ->
        rules.add(
            karabinerRule {
              layerKey = KeyCode.F
              fromKey = fromKeyP
              toKey = toKeyP
              toKeyModifiers = mods
            },
        )
      }

  listOf(
          LKM(KeyCode.T, KeyCode.NUM_5, listOf(ModifiersKeys.LEFT_SHIFT)),
          LKM(KeyCode.R, KeyCode.NUM_4, listOf(ModifiersKeys.LEFT_SHIFT)),
          LKM(KeyCode.E, KeyCode.NUM_3, listOf(ModifiersKeys.LEFT_SHIFT)),
          LKM(KeyCode.W, KeyCode.NUM_2, listOf(ModifiersKeys.LEFT_SHIFT)),
          LKM(KeyCode.Q, KeyCode.NUM_1, listOf(ModifiersKeys.LEFT_SHIFT)),
          LKM(
              KeyCode.S,
              KeyCode.U,
              listOf(ModifiersKeys.LEFT_CONTROL),
              listOf(forApp("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$"))),
          LKM(
              KeyCode.S,
              KeyCode.DELETE_OR_BACKSPACE,
              listOf(ModifiersKeys.LEFT_COMMAND),
              listOf(unlessApp("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$"))),
          LKM(
              KeyCode.D,
              KeyCode.W,
              listOf(ModifiersKeys.LEFT_CONTROL),
              listOf(forApp("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$"))),
          LKM(
              KeyCode.D,
              KeyCode.DELETE_OR_BACKSPACE,
              listOf(ModifiersKeys.LEFT_OPTION),
              listOf(unlessApp("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$"))),
          LKM(KeyCode.F, KeyCode.DELETE_OR_BACKSPACE),
          LKM(
              KeyCode.X,
              KeyCode.OPEN_BRACKET,
              listOf(ModifiersKeys.LEFT_COMMAND, ModifiersKeys.LEFT_SHIFT)),
          LKM(
              KeyCode.C,
              KeyCode.CLOSE_BRACKET,
              listOf(ModifiersKeys.LEFT_COMMAND, ModifiersKeys.LEFT_SHIFT)),
      )
      .forEach { (fromKeyP, toKeyP, mods, conditionsP) ->
        rules.add(
            karabinerRule {
              layerKey = KeyCode.J
              fromKey = fromKeyP
              toKey = toKeyP
              toKeyModifiers = mods
              conditions = conditionsP
            },
        )
      }

  return rules.toTypedArray()
}

/** Creates manipulators for vim-style navigation with various modifier combinations */
fun createVimNavigationManipulators(): List<Manipulator> {
  // VIM_NAV_KEYS and ARROW_KEYS are from Constants.kt

  data class ModifierCombo(val from: List<ModifiersKeys>, val to: List<ModifiersKeys>?)

  return listOf(
          ModifierCombo(from = listOf(ModifiersKeys.RIGHT_CONTROL), to = null),
          ModifierCombo(
              from = listOf(ModifiersKeys.RIGHT_CONTROL, ModifiersKeys.LEFT_COMMAND),
              to = listOf(ModifiersKeys.LEFT_COMMAND)),
          ModifierCombo(
              from = listOf(ModifiersKeys.RIGHT_CONTROL, ModifiersKeys.LEFT_OPTION),
              to = listOf(ModifiersKeys.LEFT_OPTION)),
          ModifierCombo(
              from = listOf(ModifiersKeys.RIGHT_CONTROL, ModifiersKeys.LEFT_SHIFT),
              to = listOf(ModifiersKeys.LEFT_SHIFT)),
          ModifierCombo(
              from =
                  listOf(
                      ModifiersKeys.RIGHT_CONTROL,
                      ModifiersKeys.LEFT_COMMAND,
                      ModifiersKeys.LEFT_OPTION),
              to = listOf(ModifiersKeys.LEFT_COMMAND, ModifiersKeys.LEFT_OPTION)),
          ModifierCombo(
              from =
                  listOf(
                      ModifiersKeys.RIGHT_CONTROL,
                      ModifiersKeys.LEFT_COMMAND,
                      ModifiersKeys.LEFT_SHIFT),
              to = listOf(ModifiersKeys.LEFT_COMMAND, ModifiersKeys.LEFT_SHIFT)),
      )
      .flatMap { combo ->
        VIM_NAV_KEYS.mapIndexed { index, keyChar ->
          ManipulatorBuilder()
              .from(keyChar, mandatoryModifiers = combo.from)
              .to(keyCode = ARROW_KEYS[index], modifiers = combo.to)
              .build()
        }
      }
}

/** Creates a condition for Apple keyboards or built-in keyboards */
fun onlyAppleKeyboards(): Condition {
  return DeviceIfCondition(identifiers = DeviceIdentifier.APPLE_KEYBOARDS)
}
