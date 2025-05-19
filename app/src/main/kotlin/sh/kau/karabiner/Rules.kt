package sh.kau.karabiner

import sh.kau.karabiner.KeyCode.DownArrow
import sh.kau.karabiner.KeyCode.LeftArrow
import sh.kau.karabiner.KeyCode.RightArrow
import sh.kau.karabiner.KeyCode.UpArrow
import sh.kau.karabiner.ModifierKeyCode.*

// Note: The final karabinerConfig construction and JSON writing will be in Main.kt

fun createMainRules(): List<KarabinerRule> {

  // explicitly mapping to right side modifiers
  // as i find myself using the (left) capslock + left modifiers
  // if i already use left-modifier for capslock hyper
  // karabiner won't see those
  val newCapsLockModifiers = listOf(RightControl, RightCommand, RightOption, RightShift)

  return listOf(
      karabinerRule {
        description = "Right Cmd -> Ctrl Enter (alone)"
        mapping {
          fromKey = RightCommand
          toKey = RightControl
          // toKeyIfAlone = KeyCode.ReturnOrEnter
          forDevice { identifiers = DeviceIdentifier.APPLE_KEYBOARDS }
        }
      },
      *createCapsLockRules(newCapsLockModifiers),
      *createLayerKeyRules(),
      *createVimNavigationRules(newCapsLockModifiers),
      karabinerRuleSingle {
        description = "O + 0 -> Raycast Confetti"
        layerKey = KeyCode.O
        fromKey = KeyCode.Num0
        shellCommand = "open raycast://extensions/raycast/raycast/confetti"
      },
      karabinerRuleSingle {
        description = "O + 1 -> Obsidian"
        layerKey = KeyCode.O
        fromKey = KeyCode.Num1
        shellCommand = "open -a Obsidian.app"
      },
      karabinerRuleSingle {
        description = "O + 2 -> Google Chrome"
        layerKey = KeyCode.O
        fromKey = KeyCode.Num2
        shellCommand = "open -a 'Google Chrome.app'"
      },
      karabinerRuleSingle {
        description = "O + 3 -> Warp"
        layerKey = KeyCode.O
        fromKey = KeyCode.Num3
        shellCommand = "open -a 'Warp.app'"
      },
      karabinerRuleSingle {
        description = "O + 4 -> Cursor"
        layerKey = KeyCode.O
        fromKey = KeyCode.Num4
        shellCommand = "open -a 'Cursor.app'"
      },
  )
}

/** --- Caps Lock -> Escape (alone) -> Ctrl (on hold) -> hold + Vim keys -> Arrow/Mouse */
fun createCapsLockRules(newCapsLockModifiers: List<ModifierKeyCode>): Array<KarabinerRule> {
  val rules = mutableListOf<KarabinerRule>()

  rules.addAll(
      listOf(
          karabinerRuleSingle {
            description = "Caps Lock alone -> Escape, held -> Hyper"
            fromKey = KeyCode.CapsLock
            toKey = newCapsLockModifiers.first()
            toModifiers = newCapsLockModifiers.drop(1).takeIf { it.isNotEmpty() }
            toKeyIfAlone = KeyCode.Escape
          },
          // using the newCapsLockModifier mapping above
          // we map vim movements to it
          karabinerRuleSingle {
            description = "CapsLock + Shift + J -> Shift + ↓"
            fromKey = KeyCode.J
            fromModifiers = FromModifiers(mandatory = listOf(LeftShift) + newCapsLockModifiers)
            toKey = DownArrow
            toModifiers = listOf(LeftShift)
          },
      ))
  return rules.toTypedArray()
}

fun createLayerKeyRules(): Array<KarabinerRule> =
    mutableListOf<KarabinerRule>()
        .apply {
          add(
              karabinerRule {
                description = "F-key layer mappings"
                layerKey = KeyCode.F

                // --- mapped to right hand side Shift num keys -
                //   Y U I
                //   ^ & *
                mapping {
                  fromKey = KeyCode.Y
                  toKey = KeyCode.Num6
                  toModifiers = listOf(LeftShift)
                }
                mapping {
                  fromKey = KeyCode.U
                  toKey = KeyCode.Num7
                  toModifiers = listOf(LeftShift)
                }
                mapping {
                  fromKey = KeyCode.I
                  toKey = KeyCode.Num8
                  toModifiers = listOf(LeftShift)
                }

                // special one - \
                mapping {
                  fromKey = KeyCode.O
                  toKey = KeyCode.Backslash
                }

                // special ones
                //  L ; '
                //  - = +
                mapping {
                  fromKey = KeyCode.L
                  toKey = KeyCode.Hyphen
                }
                mapping {
                  fromKey = KeyCode.Semicolon
                  toKey = KeyCode.EqualSign
                  toModifiers = listOf(LeftShift)
                }
                mapping {
                  fromKey = KeyCode.Quote
                  toKey = KeyCode.EqualSign
                }

                // J K
                // ( )
                mapping {
                  fromKey = KeyCode.J
                  toKey = KeyCode.Num9
                  toModifiers = listOf(LeftShift)
                }
                mapping {
                  fromKey = KeyCode.K
                  toKey = KeyCode.Num0
                  toModifiers = listOf(LeftShift)
                }

                // M ,
                // [ ]
                mapping {
                  fromKey = KeyCode.M
                  toKey = KeyCode.OpenBracket
                }
                mapping {
                  fromKey = KeyCode.Comma
                  toKey = KeyCode.CloseBracket
                }

                // . /
                // { }
                mapping {
                  fromKey = KeyCode.Period
                  toKey = KeyCode.OpenBracket
                  toModifiers = listOf(LeftShift)
                }
                mapping {
                  fromKey = KeyCode.Slash
                  toKey = KeyCode.CloseBracket
                  toModifiers = listOf(LeftShift)
                }
              })

          add(
              karabinerRule {
                description = "J-key layer mappings"
                layerKey = KeyCode.J

                // T R E W Q
                // % $ # @ !
                mapping {
                  fromKey = KeyCode.T
                  toKey = KeyCode.Num5
                  toModifiers = listOf(LeftShift)
                }
                mapping {
                  fromKey = KeyCode.R
                  toKey = KeyCode.Num4
                  toModifiers = listOf(LeftShift)
                }
                mapping {
                  fromKey = KeyCode.E
                  toKey = KeyCode.Num3
                  toModifiers = listOf(LeftShift)
                }

                mapping {
                  fromKey = KeyCode.W
                  toKey = KeyCode.Num2
                  toModifiers = listOf(LeftShift)
                }
                mapping {
                  fromKey = KeyCode.Q
                  toKey = KeyCode.Num1
                  toModifiers = listOf(LeftShift)
                }

                // Delete sequences

                // delete line
                mapping {
                  fromKey = KeyCode.S
                  toKey = KeyCode.U
                  toModifiers = listOf(LeftControl)
                  forApp {
                    bundleIds = listOf("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$")
                  }
                }
                mapping {
                  fromKey = KeyCode.S
                  toKey = KeyCode.DeleteOrBackspace
                  toModifiers = listOf(LeftCommand)
                  unlessApp {
                    bundleIds = listOf("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$")
                  }
                }

                // delete word
                mapping {
                  fromKey = KeyCode.D
                  toKey = KeyCode.W
                  toModifiers = listOf(LeftControl)
                  forApp {
                    bundleIds = listOf("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$")
                  }
                }
                mapping {
                  fromKey = KeyCode.D
                  toKey = KeyCode.DeleteOrBackspace
                  toModifiers = listOf(LeftOption)
                  unlessApp {
                    bundleIds = listOf("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$")
                  }
                }

                // delete character
                mapping {
                  fromKey = KeyCode.F
                  toKey = KeyCode.DeleteOrBackspace
                }

                // cmd shift [ + ] - for quick tab switching
                mapping {
                  fromKey = KeyCode.X
                  toKey = KeyCode.OpenBracket
                  toModifiers = listOf(LeftCommand, LeftShift)
                }
                mapping {
                  fromKey = KeyCode.C
                  toKey = KeyCode.CloseBracket
                  toModifiers = listOf(LeftCommand, LeftShift)
                }
              })
        }
        .toTypedArray()

/**
 * temporarily disabled as i don't use it as much and would rather use it for more prevalent
 * commands
 */
fun capsLockMouseRules(newCapsLockModifiers: List<ModifierKeyCode>): Array<KarabinerRule> {
  val rules = mutableListOf<KarabinerRule>()
  // Mouse control with arrow keys
  listOf(
          Pair(DownArrow, MouseKey(y = 1536)),
          Pair(UpArrow, MouseKey(y = -1536)),
          Pair(LeftArrow, MouseKey(x = -1536)),
          Pair(RightArrow, MouseKey(x = 1536)),
      )
      .forEach { (fromKey, mouseKeyValue) ->
        rules.add(
            karabinerRuleSingle {
              description = "CapsLock + ${fromKey.name} -> Move Mouse Cursor"
              this.fromKey = fromKey
              fromModifiers = FromModifiers(mandatory = newCapsLockModifiers)
              mouseKey = mouseKeyValue
            },
        )
      }

  rules.add(
      karabinerRule {
        description = "CapsLock (+ Command) +  Enter -> Mouse (Secondary) Click Buttons"
        mapping {
          fromKey = KeyCode.ReturnOrEnter
          fromModifiers = FromModifiers(mandatory = newCapsLockModifiers)
          pointingButton = "button1"
        }
        mapping {
          fromKey = KeyCode.ReturnOrEnter
          fromModifiers = FromModifiers(mandatory = listOf(LeftCommand) + newCapsLockModifiers)
          pointingButton = "button2"
        }
      })

  return rules.toTypedArray()
}

/** Creates manipulators for vim-style navigation with various modifier combinations */
fun createVimNavigationRules(newCapsLockModifiers: List<ModifierKeyCode>): Array<KarabinerRule> {
  val ARROW_KEYS: List<KeyCode> = listOf(LeftArrow, DownArrow, UpArrow, RightArrow)
  val VIM_NAV_KEYS: List<KeyCode> = listOf(KeyCode.H, KeyCode.J, KeyCode.K, KeyCode.L)

  data class ModifierCombo(val from: List<ModifierKeyCode>, val to: List<ModifierKeyCode>?)

  return listOf(
          ModifierCombo(from = newCapsLockModifiers, to = null),
          ModifierCombo(
              from = newCapsLockModifiers + listOf(LeftCommand), to = listOf(LeftCommand)),
          ModifierCombo(from = newCapsLockModifiers + listOf(LeftOption), to = listOf(LeftOption)),
          ModifierCombo(from = newCapsLockModifiers + listOf(LeftShift), to = listOf(LeftShift)),
          ModifierCombo(
              from = newCapsLockModifiers + listOf(LeftCommand, LeftOption),
              to = listOf(LeftCommand, LeftOption)),
          ModifierCombo(
              from = newCapsLockModifiers + listOf(LeftCommand, LeftShift),
              to = listOf(LeftCommand, LeftShift)),
      )
      .flatMap { combo ->
        VIM_NAV_KEYS.mapIndexed { index, keyChar ->
          karabinerRuleSingle {
            fromKey = keyChar
            fromModifiers = FromModifiers(mandatory = combo.from)
            toKey = ARROW_KEYS[index]
            toModifiers = combo.to
          }
        }
      }
      .toTypedArray()
}
