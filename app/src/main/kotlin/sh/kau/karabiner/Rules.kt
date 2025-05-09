package sh.kau.karabiner

// Note: The final karabinerConfig construction and JSON writing will be in Main.kt

fun createMainRules(): List<KarabinerRules> {
    return listOf(
        // --- Right Cmd (alone) -> Enter ---
        keymap("Right Cmd (alone) -> Enter")
            .remap(KeyCode.RIGHT_COMMAND).to(KeyCode.RETURN_OR_ENTER).whenAlone()
            .build(),

        // --- Caps Lock -> Escape (alone) | Ctrl (simple) + Vim/Arrow/Mouse ---
        createCapsLockRule(),

        // --- Special characters enabled with shift + numkey ---
        rule(
            "special characters enabled with shift + numkey",
            layer(KeyCode.F)
                .bind(KeyCode.I).to(KeyCode.NUM_8, ModifiersKeys.LEFT_SHIFT)  // *
                .bind(KeyCode.U).to(KeyCode.NUM_7, ModifiersKeys.LEFT_SHIFT)  // &
                .bind(KeyCode.Y).to(KeyCode.NUM_6, ModifiersKeys.LEFT_SHIFT)  // ^
                .bind(KeyCode.O).to(KeyCode.BACKSLASH)                       // \
                .bind(KeyCode.L).to(KeyCode.HYPHEN)                          // -
                .bind(KeyCode.SEMICOLON).to(KeyCode.EQUAL_SIGN, ModifiersKeys.LEFT_SHIFT)  // +
                .bind(KeyCode.QUOTE).to(KeyCode.EQUAL_SIGN)                  // =
                .build()
        ),

        // --- J-key special character combinations ---
        rule(
            "J-key special character combinations",
            layer(KeyCode.J)
                .bind(KeyCode.T).to(KeyCode.NUM_5, ModifiersKeys.LEFT_SHIFT)  // %
                .bind(KeyCode.R).to(KeyCode.NUM_4, ModifiersKeys.LEFT_SHIFT)  // $
                .bind(KeyCode.E).to(KeyCode.NUM_3, ModifiersKeys.LEFT_SHIFT)  // #
                .bind(KeyCode.W).to(KeyCode.NUM_2, ModifiersKeys.LEFT_SHIFT)  // @
                .bind(KeyCode.Q).to(KeyCode.NUM_1, ModifiersKeys.LEFT_SHIFT)  // !
                .build()
        ),

        // --- Bracket combinations ---
        rule(
            "bracket combos",
            layer(KeyCode.F)
                .bind(KeyCode.J).to(KeyCode.NUM_9, ModifiersKeys.LEFT_SHIFT)      // (
                .bind(KeyCode.K).to(KeyCode.NUM_0, ModifiersKeys.LEFT_SHIFT)      // )
                .bind(KeyCode.M).to(KeyCode.OPEN_BRACKET)                       // [
                .bind(KeyCode.COMMA).to(KeyCode.CLOSE_BRACKET)                  // ]
                .bind(KeyCode.PERIOD).to(KeyCode.OPEN_BRACKET, ModifiersKeys.LEFT_SHIFT) // {
                .bind(KeyCode.SLASH).to(KeyCode.CLOSE_BRACKET, ModifiersKeys.LEFT_SHIFT) // }
                .build()
        ),

        // --- Delete sequences ---
        rule(
            "delete sequences",
            layer(KeyCode.J)
                // J key combinations for terminal and other apps
                .bind(KeyCode.S).to(KeyCode.U, ModifiersKeys.LEFT_CONTROL, appTarget = "terminal") // Clear line in terminal
                .bind(KeyCode.S).to(KeyCode.DELETE_OR_BACKSPACE, ModifiersKeys.LEFT_COMMAND, appTarget = "other") // Delete to line start in other apps
                .bind(KeyCode.D).to(KeyCode.W, ModifiersKeys.LEFT_CONTROL, appTarget = "terminal") // Delete word in terminal
                .bind(KeyCode.D).to(KeyCode.DELETE_OR_BACKSPACE, ModifiersKeys.LEFT_OPTION, appTarget = "other") // Delete word in other apps
                .bind(KeyCode.F).to(KeyCode.DELETE_OR_BACKSPACE) // Delete character (all apps)
                .build()
        ),

        // --- Command next/prev tab ---
        rule(
            "cmd next/prev tab",
            layer(KeyCode.J)
                .bind(KeyCode.X).to(KeyCode.OPEN_BRACKET, ModifiersKeys.LEFT_COMMAND, ModifiersKeys.LEFT_SHIFT)   // previous tab
                .bind(KeyCode.C).to(KeyCode.CLOSE_BRACKET, ModifiersKeys.LEFT_COMMAND, ModifiersKeys.LEFT_SHIFT)  // next tab
                .build()
        )
    )
}

/**
 * Creates the Caps Lock rule with vim navigation
 */
fun createCapsLockRule(): KarabinerRules {
    val manipulators = mutableListOf<Manipulator>()

    // Caps Lock alone -> Escape, held -> right_control
    manipulators.add(
        manipulator()
            .from(KeyCode.CAPS_LOCK, optionalModifiers = listOf(ModifiersKeys.ANY))
            .to(KeyCode.RIGHT_CONTROL) // This is the "held" assignment
            .toIfAlone(KeyCode.ESCAPE) // This is the "alone" assignment
            .build()
    )

    // j with Shift+Ctrl, with app-specific conditions
    manipulators.add(
        manipulator()
            .from(KeyCode.J, mandatoryModifiers = listOf(ModifiersKeys.LEFT_SHIFT, ModifiersKeys.RIGHT_CONTROL))
            .to(KeyCode.DOWN_ARROW, modifiers = listOf(ModifiersKeys.LEFT_SHIFT))
            .withCondition(unlessApp("com.google.android.studio", "^com\\\\.jetbrains\\\\..*$"))
            .build()
    )
    manipulators.add(
        manipulator()
            .from(KeyCode.J, mandatoryModifiers = listOf(ModifiersKeys.LEFT_SHIFT, ModifiersKeys.RIGHT_CONTROL))
            .to(KeyCode.J, modifiers = listOf(ModifiersKeys.LEFT_CONTROL, ModifiersKeys.LEFT_SHIFT))
            .withCondition(forApp("com.google.android.studio", "^com\\\\.jetbrains\\\\..*$"))
            .build()
    )

    // CapLock + Vim keys -> quick arrow keys (along with modifier combinations)
    manipulators.addAll(createVimNavigationManipulators())

    // Mouse control with arrow keys
    val mouseKeyMappings = listOf(
        Pair(KeyCode.DOWN_ARROW, MouseKey(y = 1536)),
        Pair(KeyCode.UP_ARROW, MouseKey(y = -1536)),
        Pair(KeyCode.LEFT_ARROW, MouseKey(x = -1536)),
        Pair(KeyCode.RIGHT_ARROW, MouseKey(x = 1536))
    )

    mouseKeyMappings.forEach { (key, mouseKeyValue) ->
        manipulators.add(
            manipulator()
                .from(key, mandatoryModifier = ModifiersKeys.RIGHT_CONTROL)
                .to(To(mouseKey = mouseKeyValue))
                .build()
        )
    }

    // Add return_or_enter + right_control to click mouse buttons
    manipulators.add(
        manipulator()
            .from(KeyCode.RETURN_OR_ENTER, mandatoryModifier = ModifiersKeys.RIGHT_CONTROL)
            .to(To(pointingButton = "button1"))
            .build()
    )

    manipulators.add(
        manipulator()
            .from(KeyCode.RETURN_OR_ENTER, mandatoryModifiers = listOf(ModifiersKeys.LEFT_COMMAND, ModifiersKeys.RIGHT_CONTROL))
            .to(To(pointingButton = "button2"))
            .build()
    )

    return rule("Caps Lock -> Escape (alone) | Ctrl (simple)", manipulators)
}

/**
 * Creates manipulators for vim-style navigation with various modifier combinations
 */
fun createVimNavigationManipulators(): List<Manipulator> {
    // VIM_NAV_KEYS and ARROW_KEYS are from Constants.kt

    data class ModifierCombo(val from: List<ModifiersKeys>, val to: List<ModifiersKeys>?)

    val modifierCombos = listOf(
        ModifierCombo(from = listOf(ModifiersKeys.RIGHT_CONTROL), to = null),
        ModifierCombo(from = listOf(ModifiersKeys.RIGHT_CONTROL, ModifiersKeys.LEFT_COMMAND), to = listOf(ModifiersKeys.LEFT_COMMAND)),
        ModifierCombo(from = listOf(ModifiersKeys.RIGHT_CONTROL, ModifiersKeys.LEFT_OPTION), to = listOf(ModifiersKeys.LEFT_OPTION)),
        ModifierCombo(from = listOf(ModifiersKeys.RIGHT_CONTROL, ModifiersKeys.LEFT_SHIFT), to = listOf(ModifiersKeys.LEFT_SHIFT)),
        ModifierCombo(from = listOf(ModifiersKeys.RIGHT_CONTROL, ModifiersKeys.LEFT_COMMAND, ModifiersKeys.LEFT_OPTION), to = listOf(ModifiersKeys.LEFT_COMMAND, ModifiersKeys.LEFT_OPTION)),
        ModifierCombo(from = listOf(ModifiersKeys.RIGHT_CONTROL, ModifiersKeys.LEFT_COMMAND, ModifiersKeys.LEFT_SHIFT), to = listOf(ModifiersKeys.LEFT_COMMAND, ModifiersKeys.LEFT_SHIFT))
    )

    return modifierCombos.flatMap { combo ->
        VIM_NAV_KEYS.mapIndexed { index, keyChar ->
            manipulator()
                .from(keyChar, mandatoryModifiers = combo.from)
                .to(ARROW_KEYS[index], modifiers = combo.to)
                .build()
        }
    }
}