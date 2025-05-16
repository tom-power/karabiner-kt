package sh.kau.karabiner

import sh.kau.karabiner.ModifierKeyCode.LeftCommand
import sh.kau.karabiner.ModifierKeyCode.LeftControl
import sh.kau.karabiner.ModifierKeyCode.LeftOption
import sh.kau.karabiner.ModifierKeyCode.LeftShift
import sh.kau.karabiner.ModifierKeyCode.RightCommand
import sh.kau.karabiner.ModifierKeyCode.RightControl

// Note: The final karabinerConfig construction and JSON writing will be in Main.kt

fun createMainRules(): List<KarabinerRule> =
    listOf(
        karabinerRule {
          description = "Right Cmd (alone) -> Enter"
          mapping {
            fromKey = RightCommand
            toKey = RightControl
            toKeyIfAlone = KeyCode.ReturnOrEnter
            forDevice { identifiers = DeviceIdentifier.APPLE_KEYBOARDS }
          }
        },
        *createCapsLockRules(),
        *createLayerKeyRules(),
        *createVimNavigationRules(),
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

/** --- Caps Lock -> Escape (alone) -> Ctrl (on hold) -> hold + Vim keys -> Arrow/Mouse */
fun createCapsLockRules(): Array<KarabinerRule> {
  val rules = mutableListOf<KarabinerRule>()

  rules.addAll(
      listOf(
          karabinerRuleSingle {
            description = "Caps Lock alone -> Escape, held -> RightControl"
            fromKey = KeyCode.CapsLock
            toKey = RightControl
            toKeyIfAlone = KeyCode.Escape
          },
          // using the RightControl mapping above
          // we map vim movements to it
          karabinerRuleSingle {
            description = "CapsLock + Shift + J -> Shift + ↓"
            fromKey = KeyCode.J
            fromModifiers = FromModifiers(mandatory = listOf(LeftShift, RightControl))
            toKey = KeyCode.DownArrow
            toModifiers = listOf(LeftShift)
            unlessApp {
              bundleIds = listOf("com.google.android.studio", "^com\\\\.jetbrains\\\\..*$")
            }
          },
          karabinerRuleSingle {
            description = "CapsLock + Shift + J -> Shift + ↓"
            fromKey = KeyCode.J
            fromModifiers = FromModifiers(mandatory = listOf(LeftShift, RightControl))
            toKey = KeyCode.J
            toModifiers = listOf(LeftControl, LeftShift)
            forApp { bundleIds = listOf("com.google.android.studio", "^com\\\\.jetbrains\\\\..*$") }
          },
      ))

  // Mouse control with arrow keys
  listOf(
          Pair(KeyCode.DownArrow, MouseKey(y = 1536)),
          Pair(KeyCode.UpArrow, MouseKey(y = -1536)),
          Pair(KeyCode.LeftArrow, MouseKey(x = -1536)),
          Pair(KeyCode.RightArrow, MouseKey(x = 1536)),
      )
      .forEach { (fromKey, mouseKeyValue) ->
        rules.add(
            karabinerRuleSingle {
              description = "CapsLock + ${fromKey.name} -> Move Mouse Cursor"
              this.fromKey = fromKey
              fromModifiers = FromModifiers(mandatory = listOf(RightControl))
              mouseKey = mouseKeyValue
            },
        )
      }

  rules.add(
      karabinerRule {
        description = "CapsLock (+ Command) +  Enter -> Mouse (Secondary) Click Buttons"
        mapping {
          fromKey = KeyCode.ReturnOrEnter
          fromModifiers = FromModifiers(mandatory = listOf(RightControl))
          pointingButton = "button1"
        }
        mapping {
          fromKey = KeyCode.ReturnOrEnter
          fromModifiers = FromModifiers(mandatory = listOf(LeftCommand, RightControl))
          pointingButton = "button2"
        }
      })

  return rules.toTypedArray()
}

/** Creates manipulators for vim-style navigation with various modifier combinations */
fun createVimNavigationRules(): Array<KarabinerRule> {
  val ARROW_KEYS: List<KeyCode> =
      listOf(KeyCode.LeftArrow, KeyCode.DownArrow, KeyCode.UpArrow, KeyCode.RightArrow)

  val VIM_NAV_KEYS: List<KeyCode> = listOf(KeyCode.H, KeyCode.J, KeyCode.K, KeyCode.L)

  data class ModifierCombo(val from: List<ModifierKeyCode>, val to: List<ModifierKeyCode>?)

  return listOf(
          ModifierCombo(from = listOf(RightControl), to = null),
          ModifierCombo(from = listOf(RightControl, LeftCommand), to = listOf(LeftCommand)),
          ModifierCombo(from = listOf(RightControl, LeftOption), to = listOf(LeftOption)),
          ModifierCombo(from = listOf(RightControl, LeftShift), to = listOf(LeftShift)),
          ModifierCombo(
              from = listOf(RightControl, LeftCommand, LeftOption),
              to = listOf(LeftCommand, LeftOption)),
          ModifierCombo(
              from = listOf(RightControl, LeftCommand, LeftShift),
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
