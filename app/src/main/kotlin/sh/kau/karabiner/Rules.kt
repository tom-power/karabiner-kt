package sh.kau.karabiner

import sh.kau.karabiner.ModifierKeyCode.LEFT_COMMAND
import sh.kau.karabiner.ModifierKeyCode.LEFT_CONTROL
import sh.kau.karabiner.ModifierKeyCode.LEFT_OPTION
import sh.kau.karabiner.ModifierKeyCode.LEFT_SHIFT
import sh.kau.karabiner.ModifierKeyCode.RIGHT_CONTROL

// Note: The final karabinerConfig construction and JSON writing will be in Main.kt

fun createMainRules(): List<KarabinerRule> {

  return listOf(
      karabinerRule {
        description = "Right Cmd (alone) -> Enter"
        fromKey = KeyCode.RIGHT_COMMAND
        toKey = KeyCode.RIGHT_CONTROL
        toKeyIfAlone = KeyCode.RETURN_OR_ENTER
        forDevice { identifiers = DeviceIdentifier.APPLE_KEYBOARDS }
      },
      *createCapsLockRules(),
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

fun createLayerKeyRules(): Array<KarabinerRule> =
    mutableListOf<KarabinerRule>()
        .apply {
          add(
              karabinerRuleLayer {
                description = "F-key layer mappings"
                layerKey = KeyCode.F

                // --- mapped to right hand side Shift num keys -
                //   Y U I
                //   ^ & *
                mapping {
                  fromKey = KeyCode.Y
                  toKey = KeyCode.NUM_6
                  toModifiers = listOf(LEFT_SHIFT)
                }
                mapping {
                  fromKey = KeyCode.U
                  toKey = KeyCode.NUM_7
                  toModifiers = listOf(LEFT_SHIFT)
                }
                mapping {
                  fromKey = KeyCode.I
                  toKey = KeyCode.NUM_8
                  toModifiers = listOf(LEFT_SHIFT)
                }

                // special one - \
                mapping {
                  fromKey = KeyCode.O
                  toKey = KeyCode.BACKSLASH
                }

                // special ones
                //  L ; '
                //  - = +
                mapping {
                  fromKey = KeyCode.L
                  toKey = KeyCode.HYPHEN
                }
                mapping {
                  fromKey = KeyCode.SEMICOLON
                  toKey = KeyCode.EQUAL_SIGN
                }
                mapping {
                  fromKey = KeyCode.QUOTE
                  toKey = KeyCode.EQUAL_SIGN
                  toModifiers = listOf(LEFT_SHIFT)
                }

                // J K
                // ( )
                mapping {
                  fromKey = KeyCode.J
                  toKey = KeyCode.NUM_9
                  toModifiers = listOf(LEFT_SHIFT)
                }
                mapping {
                  fromKey = KeyCode.K
                  toKey = KeyCode.NUM_0
                  toModifiers = listOf(LEFT_SHIFT)
                }

                // M ,
                // [ ]
                mapping {
                  fromKey = KeyCode.M
                  toKey = KeyCode.OPEN_BRACKET
                }
                mapping {
                  fromKey = KeyCode.COMMA
                  toKey = KeyCode.CLOSE_BRACKET
                }

                // . /
                // { }
                mapping {
                  fromKey = KeyCode.PERIOD
                  toKey = KeyCode.OPEN_BRACKET
                  toModifiers = listOf(LEFT_SHIFT)
                }
                mapping {
                  fromKey = KeyCode.SLASH
                  toKey = KeyCode.CLOSE_BRACKET
                  toModifiers = listOf(LEFT_SHIFT)
                }
              })

          add(
              karabinerRuleLayer {
                description = "J-key layer mappings"
                layerKey = KeyCode.J

                // T R E W Q
                // % $ # @ !
                mapping {
                  fromKey = KeyCode.T
                  toKey = KeyCode.NUM_5
                  toModifiers = listOf(LEFT_SHIFT)
                }
                mapping {
                  fromKey = KeyCode.R
                  toKey = KeyCode.NUM_4
                  toModifiers = listOf(LEFT_SHIFT)
                }
                mapping {
                  fromKey = KeyCode.E
                  toKey = KeyCode.NUM_3
                  toModifiers = listOf(LEFT_SHIFT)
                }

                mapping {
                  fromKey = KeyCode.W
                  toKey = KeyCode.NUM_2
                  toModifiers = listOf(LEFT_SHIFT)
                }
                mapping {
                  fromKey = KeyCode.Q
                  toKey = KeyCode.NUM_1
                  toModifiers = listOf(LEFT_SHIFT)
                }

                // Delete sequences

                // delete line
                mapping {
                  fromKey = KeyCode.S
                  toKey = KeyCode.U
                  toModifiers = listOf(LEFT_CONTROL)
                  forApp {
                    bundleIds = listOf("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$")
                  }
                }
                mapping {
                  fromKey = KeyCode.S
                  toKey = KeyCode.DELETE_OR_BACKSPACE
                  toModifiers = listOf(LEFT_COMMAND)
                  unlessApp {
                    bundleIds = listOf("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$")
                  }
                }

                // delete word
                mapping {
                  fromKey = KeyCode.D
                  toKey = KeyCode.W
                  toModifiers = listOf(LEFT_CONTROL)
                  forApp {
                    bundleIds = listOf("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$")
                  }
                }
                mapping {
                  fromKey = KeyCode.D
                  toKey = KeyCode.DELETE_OR_BACKSPACE
                  toModifiers = listOf(LEFT_OPTION)
                  unlessApp {
                    bundleIds = listOf("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$")
                  }
                }

                // delete character
                mapping {
                  fromKey = KeyCode.F
                  toKey = KeyCode.DELETE_OR_BACKSPACE
                }

                // cmd shift [ + ] - for quick tab switching
                mapping {
                  fromKey = KeyCode.X
                  toKey = KeyCode.OPEN_BRACKET
                  toModifiers = listOf(LEFT_COMMAND, LEFT_SHIFT)
                }
                mapping {
                  fromKey = KeyCode.C
                  toKey = KeyCode.CLOSE_BRACKET
                  toModifiers = listOf(LEFT_COMMAND, LEFT_SHIFT)
                }
              })
        }
        .toTypedArray()

/** --- Caps Lock -> Escape (alone) -> Ctrl (on hold) -> hold + Vim keys -> Arrow/Mouse */
fun createCapsLockRules(): Array<KarabinerRule> {
  val rules = mutableListOf<KarabinerRule>()

  rules.add(
      karabinerRule {
        description = "Caps Lock alone -> Escape, held -> right_control"
        fromKey = KeyCode.CAPS_LOCK
        toKey = KeyCode.RIGHT_CONTROL
        toKeyIfAlone = KeyCode.ESCAPE
      })

  val manipulators = mutableListOf<Manipulator>()


//  karabinerRule {
//    fromKey = KeyCode.J
//    fromModifiers = FromModifiers(mandatory = listOf(LEFT_SHIFT, RIGHT_CONTROL))
//    toKey = KeyCode.DOWN_ARROW
//    toKeyModifiers = listOf(LEFT_SHIFT)
//    unlessApp {
//      bundleIds = listOf("com.google.android.studio", "^com\\\\.jetbrains\\\\..*$", "^com\\.googlecode\\.iterm2$")
//    }
//  }

  manipulators.add(
      ManipulatorBuilder()
          .from(
              KeyCode.J,
              mandatoryModifiers =
                  listOf(LEFT_SHIFT, RIGHT_CONTROL),
          )
          .to(keyCode = KeyCode.DOWN_ARROW, modifiers = listOf(LEFT_SHIFT))
          .withCondition(unlessApp("com.google.android.studio", "^com\\\\.jetbrains\\\\..*$"))
          .build())

  manipulators.add(
      ManipulatorBuilder()
          .from(
              KeyCode.J,
              mandatoryModifiers =
                  listOf(LEFT_SHIFT, RIGHT_CONTROL))
          .to(
              keyCode = KeyCode.J,
              modifiers = listOf(LEFT_CONTROL, LEFT_SHIFT))
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
                .from(key, mandatoryModifiers = listOf(RIGHT_CONTROL))
                .to(mouseKey = mouseKeyValue)
                .build())
      }

  // Add return_or_enter + right_control to click mouse buttons
  manipulators.add(
      ManipulatorBuilder()
          .from(KeyCode.RETURN_OR_ENTER, mandatoryModifiers = listOf(RIGHT_CONTROL))
          .to(pointingButton = "button1")
          .build())

  manipulators.add(
      ManipulatorBuilder()
          .from(
              KeyCode.RETURN_OR_ENTER,
              mandatoryModifiers =
                  listOf(LEFT_COMMAND, RIGHT_CONTROL))
          .to(pointingButton = "button2")
          .build())

  rules.add(KarabinerRule("Caps Lock -> Escape (alone) | Ctrl (simple)", manipulators))

  return rules.toTypedArray()
}

/** Creates manipulators for vim-style navigation with various modifier combinations */
fun createVimNavigationManipulators(): List<Manipulator> {
  // VIM_NAV_KEYS and ARROW_KEYS are from Constants.kt

  data class ModifierCombo(val from: List<ModifierKeyCode>, val to: List<ModifierKeyCode>?)

  return listOf(
          ModifierCombo(from = listOf(RIGHT_CONTROL), to = null),
          ModifierCombo(
              from = listOf(RIGHT_CONTROL, LEFT_COMMAND),
              to = listOf(LEFT_COMMAND)),
          ModifierCombo(
              from = listOf(RIGHT_CONTROL, LEFT_OPTION),
              to = listOf(LEFT_OPTION)),
          ModifierCombo(
              from = listOf(RIGHT_CONTROL, LEFT_SHIFT),
              to = listOf(LEFT_SHIFT)),
          ModifierCombo(
              from =
                  listOf(
                    RIGHT_CONTROL,
                    LEFT_COMMAND,
                    LEFT_OPTION
                  ),
              to = listOf(LEFT_COMMAND, LEFT_OPTION)),
          ModifierCombo(
              from =
                  listOf(
                    RIGHT_CONTROL,
                    LEFT_COMMAND,
                    LEFT_SHIFT
                  ),
              to = listOf(LEFT_COMMAND, LEFT_SHIFT)),
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
