package sh.kau.karabiner

import sh.kau.karabiner.Condition.DeviceIfCondition

// Note: The final karabinerConfig construction and JSON writing will be in Main.kt

fun createMainRules(): List<KarabinerRule> {

  return listOf(
      // --- Right Cmd (alone) -> Enter ---
      karabinerRule(
          "Right Cmd (alone) -> Enter",
          ManipulatorBuilder()
              .from(KeyCode.RIGHT_COMMAND, optionalModifiers = listOf(ModifiersKeys.ANY))
              .to(keyCode = KeyCode.RIGHT_CONTROL) // When held
              .toIfAlone(keyCode = KeyCode.RETURN_OR_ENTER) // When pressed alone
              .withCondition(forAppleKeyboards()) // Only for Apple or built-in keyboards
              .build()),

      // --- Caps Lock -> Escape (alone) | Ctrl (simple) + Vim/Arrow/Mouse ---
      createCapsLockRule(),

      // --- Special characters enabled with shift + numkey ---
      karabinerRule(
          "special characters enabled with shift + numkey",
          *ManipulatorBuilder().layerKey(KeyCode.F).from(KeyCode.I, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.NUM_8, modifiers = listOf(ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.F).from(KeyCode.U, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.NUM_7, modifiers = listOf(ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.F).from(KeyCode.Y, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.NUM_6, modifiers = listOf(ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.F).from(KeyCode.O, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.BACKSLASH).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.F).from(KeyCode.L, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.HYPHEN).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.F).from(KeyCode.SEMICOLON, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.EQUAL_SIGN, modifiers = listOf(ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.F).from(KeyCode.QUOTE, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.EQUAL_SIGN).buildLayer().toTypedArray(),
      ),

      // --- J-key special character combinations ---
      karabinerRule(
          "J-key special character combinations",
          *ManipulatorBuilder().layerKey(KeyCode.J).from(KeyCode.T, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.NUM_5, modifiers = listOf(ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.J).from(KeyCode.R, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.NUM_4, modifiers = listOf(ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.J).from(KeyCode.E, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.NUM_3, modifiers = listOf(ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.J).from(KeyCode.W, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.NUM_2, modifiers = listOf(ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.J).from(KeyCode.Q, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.NUM_1, modifiers = listOf(ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
      ),

      // --- Bracket combinations ---
      karabinerRule(
          "bracket combos",
          *ManipulatorBuilder().layerKey(KeyCode.F).from(KeyCode.J, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.NUM_9, modifiers = listOf(ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.F).from(KeyCode.K, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.NUM_0, modifiers = listOf(ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.F).from(KeyCode.M, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.OPEN_BRACKET).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.F).from(KeyCode.COMMA, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.CLOSE_BRACKET).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.F).from(KeyCode.PERIOD, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.OPEN_BRACKET, modifiers = listOf(ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.F).from(KeyCode.SLASH, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.CLOSE_BRACKET, modifiers = listOf(ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
      ),

      // --- Delete sequences ---
      karabinerRule(
          "delete sequences",
          *ManipulatorBuilder().layerKey(KeyCode.J).from(KeyCode.S, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.U, modifiers = listOf(ModifiersKeys.LEFT_CONTROL)).withCondition(forApp("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$")).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.J).from(KeyCode.S, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.DELETE_OR_BACKSPACE, modifiers = listOf(ModifiersKeys.LEFT_COMMAND)).withCondition(unlessApp("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$")).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.J).from(KeyCode.D, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.W, modifiers = listOf(ModifiersKeys.LEFT_CONTROL)).withCondition(forApp("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$")).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.J).from(KeyCode.D, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.DELETE_OR_BACKSPACE, modifiers = listOf(ModifiersKeys.LEFT_OPTION)).withCondition(unlessApp("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$")).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.J).from(KeyCode.F, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.DELETE_OR_BACKSPACE).buildLayer().toTypedArray(),
      ),

      // --- Command next/prev tab ---
      karabinerRule(
          "cmd next/prev tab",
          *ManipulatorBuilder().layerKey(KeyCode.J).from(KeyCode.X, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.OPEN_BRACKET, modifiers = listOf(ModifiersKeys.LEFT_COMMAND, ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
          *ManipulatorBuilder().layerKey(KeyCode.J).from(KeyCode.C, optionalModifiers = listOf(ModifiersKeys.ANY)).to(KeyCode.CLOSE_BRACKET, modifiers = listOf(ModifiersKeys.LEFT_COMMAND, ModifiersKeys.LEFT_SHIFT)).buildLayer().toTypedArray(),
      ),
  )
}

/** Creates the Caps Lock rule with vim navigation */
fun createCapsLockRule(): KarabinerRule {
  val manipulators = mutableListOf<Manipulator>()

  // Caps Lock alone -> Escape, held -> right_control
  manipulators.add(
      ManipulatorBuilder()
          .from(KeyCode.CAPS_LOCK, optionalModifiers = listOf(ModifiersKeys.ANY))
          .to(keyCode = KeyCode.RIGHT_CONTROL) // This is the "held" assignment
          .toIfAlone(keyCode = KeyCode.ESCAPE) // This is the "alone" assignment
          .build())

  // j with Shift+Ctrl, with app-specific conditions
  manipulators.add(
      ManipulatorBuilder()
          .from(
              KeyCode.J,
              mandatoryModifiers = listOf(ModifiersKeys.LEFT_SHIFT, ModifiersKeys.RIGHT_CONTROL))
          .to(keyCode = KeyCode.DOWN_ARROW, modifiers = listOf(ModifiersKeys.LEFT_SHIFT))
          .withCondition(unlessApp("com.google.android.studio", "^com\\\\.jetbrains\\\\..*$"))
          .build())
  manipulators.add(
      ManipulatorBuilder()
          .from(
              KeyCode.J,
              mandatoryModifiers = listOf(ModifiersKeys.LEFT_SHIFT, ModifiersKeys.RIGHT_CONTROL))
          .to(keyCode = KeyCode.J, modifiers = listOf(ModifiersKeys.LEFT_CONTROL, ModifiersKeys.LEFT_SHIFT))
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
fun forAppleKeyboards(): Condition {
  return DeviceIfCondition(
      identifiers =
          listOf(
              Identifiers(vendorId = 1452L), // Apple
              Identifiers(vendorId = 76L), // Another Apple keyboard ID
              Identifiers(isBuiltInKeyboard = true)))
}
