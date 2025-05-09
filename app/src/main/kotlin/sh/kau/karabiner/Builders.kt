package sh.kau.karabiner

import kotlinx.serialization.json.JsonPrimitive

// --- Global Helper Functions ---

fun rule(description: String, manipulators: List<Manipulator>): KarabinerRules {
    return KarabinerRules(description, manipulators)
}

fun rule(description: String, vararg manipulators: Manipulator): KarabinerRules {
    return KarabinerRules(description, manipulators.toList())
}

fun forApp(vararg bundleIdentifiers: String): Condition {
    return FrontmostApplicationIfCondition(bundleIdentifiers = bundleIdentifiers.toList())
}

fun unlessApp(vararg bundleIdentifiers: String): Condition {
    return FrontmostApplicationUnlessCondition(bundleIdentifiers = bundleIdentifiers.toList())
}


// --- ManipulatorBuilder ---

fun manipulator(): ManipulatorBuilder {
    return ManipulatorBuilder()
}

class ManipulatorBuilder {
    private var type: String = "basic"
    private var from: From? = null
    private val to: MutableList<To> = mutableListOf()
    private val toIfAlone: MutableList<To> = mutableListOf()
    private val toAfterKeyUp: MutableList<To> = mutableListOf()
    private val toIfHeldDown: MutableList<To> = mutableListOf() // Added based on your Types.kt
    private var parameters: Parameters? = null
    private val conditions: MutableList<Condition> = mutableListOf()
    private var description: String? = null

    fun from(
        keyCode: KeyCode,
        optionalModifiers: List<ModifiersKeys>? = null,
        mandatoryModifiers: List<ModifiersKeys>? = null
    ): ManipulatorBuilder = apply {
        val mods = if (optionalModifiers != null || mandatoryModifiers != null) {
            Modifiers(optional = optionalModifiers, mandatory = mandatoryModifiers)
        } else null
        this.from = From(keyCode = keyCode, modifiers = mods)
    }

    fun from(
        simultaneousKeys: List<KeyCode>,
        simultaneousOptions: SimultaneousOptions? = null
    ): ManipulatorBuilder = apply {
        this.from = From(
            simultaneous = simultaneousKeys.map { SimultaneousFrom(it) },
            simultaneousOptions = simultaneousOptions
        )
    }

    // Convenience for single mandatory modifier
    fun from(keyCode: KeyCode, mandatoryModifier: ModifiersKeys): ManipulatorBuilder =
        from(keyCode, mandatoryModifiers = listOf(mandatoryModifier))

    // Convenience for multiple mandatory modifiers
    fun from(keyCode: KeyCode, vararg mandatoryModifiers: ModifiersKeys): ManipulatorBuilder =
        from(keyCode, mandatoryModifiers = mandatoryModifiers.toList())


    private fun addToEventList(eventList: MutableList<To>, command: To): ManipulatorBuilder = apply {
        eventList.add(command)
    }

    private fun addAllToEventList(eventList: MutableList<To>, commands: List<To>): ManipulatorBuilder = apply {
        eventList.addAll(commands)
    }

    fun to(command: To): ManipulatorBuilder = addToEventList(this.to, command)
    fun to(commands: List<To>): ManipulatorBuilder = addAllToEventList(this.to, commands)
    fun to(vararg commands: To): ManipulatorBuilder = addAllToEventList(this.to, commands.toList())

    fun to(
        keyCode: KeyCode,
        modifiers: List<ModifiersKeys> = emptyList()
    ): ManipulatorBuilder = to(To(keyCode = keyCode, modifiers = modifiers))

    // Convenience for single 'to' modifier
    fun to(keyCode: KeyCode, modifier: ModifiersKeys): ManipulatorBuilder =
        to(keyCode, modifiers = listOf(modifier))

    // Convenience for multiple 'to' modifiers
    fun to(keyCode: KeyCode, vararg modifiers: ModifiersKeys): ManipulatorBuilder =
        to(keyCode, modifiers = modifiers.toList())

    fun toShellCommand(command: String): ManipulatorBuilder = to(To(shellCommand = command))

    fun toSetVariable(name: String, value: Boolean): ManipulatorBuilder =
        to(To(setVariable = SetVariable(name, JsonPrimitive(value))))

    fun toSetVariable(name: String, value: Number): ManipulatorBuilder =
        to(To(setVariable = SetVariable(name, JsonPrimitive(value))))

    fun toSetVariable(name: String, value: String): ManipulatorBuilder =
        to(To(setVariable = SetVariable(name, JsonPrimitive(value))))

    fun toMouseKey(
        x: Int? = null,
        y: Int? = null,
        verticalWheel: Int? = null,
        horizontalWheel: Int? = null,
        speedMultiplier: Double? = null
    ): ManipulatorBuilder = to(
        To(
            mouseKey = MouseKey(
                x = x,
                y = y,
                verticalWheel = verticalWheel,
                horizontalWheel = horizontalWheel,
                speedMultiplier = speedMultiplier
            )
        )
    )

    // --- toIfAlone ---
    fun toIfAlone(command: To): ManipulatorBuilder = addToEventList(this.toIfAlone, command)
    fun toIfAlone(commands: List<To>): ManipulatorBuilder = addAllToEventList(this.toIfAlone, commands)
    fun toIfAlone(vararg commands: To): ManipulatorBuilder = addAllToEventList(this.toIfAlone, commands.toList())

    fun toIfAlone(
        keyCode: KeyCode,
        modifiers: List<ModifiersKeys> = emptyList()
    ): ManipulatorBuilder = toIfAlone(To(keyCode = keyCode, modifiers = modifiers))

    // Convenience for single 'toIfAlone' modifier
    fun toIfAlone(keyCode: KeyCode, modifier: ModifiersKeys): ManipulatorBuilder =
        toIfAlone(keyCode, modifiers = listOf(modifier))

    // Convenience for multiple 'toIfAlone' modifiers
    fun toIfAlone(keyCode: KeyCode, vararg modifiers: ModifiersKeys): ManipulatorBuilder =
        toIfAlone(keyCode, modifiers = modifiers.toList())

    // --- toAfterKeyUp ---
    fun toAfterKeyUp(command: To): ManipulatorBuilder = addToEventList(this.toAfterKeyUp, command)
    fun toAfterKeyUp(commands: List<To>): ManipulatorBuilder = addAllToEventList(this.toAfterKeyUp, commands)
    fun toAfterKeyUp(vararg commands: To): ManipulatorBuilder = addAllToEventList(this.toAfterKeyUp, commands.toList())

    fun toAfterKeyUp(
        keyCode: KeyCode,
        modifiers: List<ModifiersKeys> = emptyList()
    ): ManipulatorBuilder = toAfterKeyUp(To(keyCode = keyCode, modifiers = modifiers))

    // Convenience for single 'toAfterKeyUp' modifier
    fun toAfterKeyUp(keyCode: KeyCode, modifier: ModifiersKeys): ManipulatorBuilder =
        toAfterKeyUp(keyCode, modifiers = listOf(modifier))

    // Convenience for multiple 'toAfterKeyUp' modifiers
    fun toAfterKeyUp(keyCode: KeyCode, vararg modifiers: ModifiersKeys): ManipulatorBuilder =
        toAfterKeyUp(keyCode, modifiers = modifiers.toList())

    // --- toIfHeldDown ---
    fun toIfHeldDown(command: To): ManipulatorBuilder = addToEventList(this.toIfHeldDown, command)
    fun toIfHeldDown(commands: List<To>): ManipulatorBuilder = addAllToEventList(this.toIfHeldDown, commands)
    fun toIfHeldDown(vararg commands: To): ManipulatorBuilder = addAllToEventList(this.toIfHeldDown, commands.toList())

    fun toIfHeldDown(
        keyCode: KeyCode,
        modifiers: List<ModifiersKeys> = emptyList()
    ): ManipulatorBuilder = toIfHeldDown(To(keyCode = keyCode, modifiers = modifiers))

    // Convenience for single 'toIfHeldDown' modifier
    fun toIfHeldDown(keyCode: KeyCode, modifier: ModifiersKeys): ManipulatorBuilder =
        toIfHeldDown(keyCode, modifiers = listOf(modifier))

    // Convenience for multiple 'toIfHeldDown' modifiers
    fun toIfHeldDown(keyCode: KeyCode, vararg modifiers: ModifiersKeys): ManipulatorBuilder =
        toIfHeldDown(keyCode, modifiers = modifiers.toList())


    fun withCondition(condition: Condition): ManipulatorBuilder = apply {
        this.conditions.add(condition)
    }

    fun withConditions(vararg conditions: Condition): ManipulatorBuilder = apply {
        this.conditions.addAll(conditions.toList())
    }

    fun forDevices(vararg deviceIdentifiers: Identifiers): ManipulatorBuilder = apply {
        withCondition(DeviceIfCondition(identifiers = deviceIdentifiers.toList()))
    }

    fun forDevices(deviceIdentifierList: List<Identifiers>): ManipulatorBuilder = apply {
         withCondition(DeviceIfCondition(identifiers = deviceIdentifierList))
    }

    fun ifVariable(name: String, value: Boolean): ManipulatorBuilder =
        withCondition(VariableIfCondition(name = name, value = JsonPrimitive(value)))

    fun ifVariable(name: String, value: Number): ManipulatorBuilder =
        withCondition(VariableIfCondition(name = name, value = JsonPrimitive(value)))

    fun ifVariable(name: String, value: String): ManipulatorBuilder =
        withCondition(VariableIfCondition(name = name, value = JsonPrimitive(value)))

    fun unlessVariable(name: String, value: Boolean): ManipulatorBuilder =
        withCondition(VariableUnlessCondition(name = name, value = JsonPrimitive(value)))

    fun unlessVariable(name: String, value: Number): ManipulatorBuilder =
        withCondition(VariableUnlessCondition(name = name, value = JsonPrimitive(value)))

    fun unlessVariable(name: String, value: String): ManipulatorBuilder =
        withCondition(VariableUnlessCondition(name = name, value = JsonPrimitive(value)))

    fun withParameters(parameters: Parameters): ManipulatorBuilder = apply {
        this.parameters = parameters
    }

    fun withDescription(description: String): ManipulatorBuilder = apply {
        this.description = description
    }

    fun type(type: String): ManipulatorBuilder = apply {
        this.type = type
    }

    fun build(): Manipulator {
        return Manipulator(
            type = this.type,
            from = this.from ?: throw IllegalStateException("Manipulator 'from' must be defined."),
            to = if (this.to.isNotEmpty()) this.to.toList() else null,
            toIfAlone = if (this.toIfAlone.isNotEmpty()) this.toIfAlone.toList() else null,
            toAfterKeyUp = if (this.toAfterKeyUp.isNotEmpty()) this.toAfterKeyUp.toList() else null,
            toIfHeldDown = if (this.toIfHeldDown.isNotEmpty()) this.toIfHeldDown.toList() else null,
            parameters = this.parameters,
            conditions = if (this.conditions.isNotEmpty()) this.conditions.toList() else null,
            description = this.description
        )
    }
}

// --- LayerBuilder and createKeyLayer ---

// Define these similar to how they were in builders.ts if needed for Layer logic
// For now, assuming they are not directly used by the LayerBuilder structure itself
// but might be used in the rule definitions later.
// const val ARROW_KEYS: List<KeyCode> = listOf(KeyCode.LEFT_ARROW, KeyCode.DOWN_ARROW, KeyCode.UP_ARROW, KeyCode.RIGHT_ARROW)
// const val VIM_NAV_KEYS: List<KeyCode> = listOf(KeyCode.H, KeyCode.J, KeyCode.K, KeyCode.L)

fun layer(triggerKey: KeyCode): LayerBuilder {
    return LayerBuilder(triggerKey)
}

/**
 * Represents a binding for a key within a layer.
 */
data class LayerBinding(
    val targetKey: KeyCode,
    val targetModifiers: List<ModifiersKeys> = emptyList(),
    val appTarget: String? = null // e.g., "terminal", "other"
)

class LayerBuilder(private val triggerKey: KeyCode) {
    private val bindings = mutableMapOf<String, LayerBinding>() // Key is "sourceKey_appTarget" or "sourceKey"
    private val conditions = mutableListOf<Condition>()
    private var threshold: Long = 250L //ms

    inner class LayerKeyBinder(private val sourceKey: KeyCode) {
        fun to(targetKey: KeyCode, targetModifiers: List<ModifiersKeys> = emptyList(), appTarget: String? = null): LayerBuilder {
            val bindingKey = appTarget?.let { "${sourceKey.name}_${it}" } ?: sourceKey.name
            bindings[bindingKey] = LayerBinding(targetKey, targetModifiers, appTarget)
            return this@LayerBuilder
        }

        fun to(targetKey: KeyCode, vararg targetModifiers: ModifiersKeys, appTarget: String? = null): LayerBuilder {
            return to(targetKey, targetModifiers.toList(), appTarget)
        }
    }

    fun bind(sourceKey: KeyCode): LayerKeyBinder {
        return LayerKeyBinder(sourceKey)
    }

    fun whenCondition(condition: Condition): LayerBuilder = apply {
        conditions.add(condition)
    }

    fun forApps(vararg bundleIds: String): LayerBuilder = apply {
        conditions.add(forApp(*bundleIds))
    }

    fun unlessApps(vararg bundleIds: String): LayerBuilder = apply {
        conditions.add(unlessApp(*bundleIds))
    }

    fun withThreshold(milliseconds: Long): LayerBuilder = apply {
        threshold = milliseconds
    }

    fun build(): List<Manipulator> {
        val result = mutableListOf<Manipulator>()

        bindings.forEach { (bindingKey, binding) ->
            val sourceKeyName = bindingKey.split("_")[0]
            val sourceKeyCode = KeyCode.valueOf(sourceKeyName) // Assumes sourceKeyName matches enum entry

            result.addAll(
                createLayerManipulators(
                    triggerKey = triggerKey,
                    sourceKey = sourceKeyCode,
                    layerBinding = binding,
                    layerConditions = conditions,
                    simultaneousThreshold = threshold
                )
            )
        }
        return result.toList()
    }
}

/**
 * Internal helper to create manipulators for a single layer binding.
 * This combines the logic from the original createKeyLayer and parts of LayerBuilder.build().
 */
private fun createLayerManipulators(
    triggerKey: KeyCode,
    sourceKey: KeyCode,
    layerBinding: LayerBinding,
    layerConditions: List<Condition>,
    simultaneousThreshold: Long
): List<Manipulator> {
    val variableName = "${triggerKey.name.lowercase()}-mode"
    val manipulators = mutableListOf<Manipulator>()

    val toOutput = To(
        keyCode = layerBinding.targetKey,
        modifiers = layerBinding.targetModifiers
    )

    // 1. Mode-based manipulator (e.g., if "f-mode" is 1, then j -> paren)
    val modeManipulatorBuilder = manipulator()
        .from(sourceKey)
        .to(toOutput)
        .ifVariable(variableName, 1) // Assuming variable value is Int/Long for mode

    layerConditions.forEach { modeManipulatorBuilder.withCondition(it) }
    applyAppTargetCondition(modeManipulatorBuilder, layerBinding.appTarget)
    manipulators.add(modeManipulatorBuilder.build())

    // 2. Simultaneous manipulator (e.g., f+j simultaneously -> paren and set "f-mode" = 1)
    val simultaneousManipulatorBuilder = manipulator()
        .from(
            simultaneousKeys = listOf(triggerKey, sourceKey),
            simultaneousOptions = SimultaneousOptions(
                detectKeyDownUninterruptedly = true,
                keyDownOrder = "strict",
                keyUpOrder = "strict_inverse",
                keyUpWhen = "any",
                toAfterKeyUp = listOf(
                    To(setVariable = SetVariable(variableName, JsonPrimitive(0)))
                )
            )
        )
        .toSetVariable(variableName, 1) // Set mode to 1
        .to(toOutput) // Then perform the action
        .withParameters(
            Parameters(
                simultaneousThresholdMilliseconds = simultaneousThreshold,
                toDelayedActionDelayMilliseconds = 10,
                toIfAloneTimeoutMilliseconds = 250,
                toIfHeldDownThresholdMilliseconds = 500
            )
        )

    layerConditions.forEach { simultaneousManipulatorBuilder.withCondition(it) }
    applyAppTargetCondition(simultaneousManipulatorBuilder, layerBinding.appTarget)
    manipulators.add(simultaneousManipulatorBuilder.build())

    return manipulators.toList()
}

/**
 * Helper to apply app-specific conditions based on appTarget string.
 */
private fun applyAppTargetCondition(builder: ManipulatorBuilder, appTarget: String?) {
    appTarget ?: return
    when (appTarget.lowercase()) {
        "terminal" -> builder.withCondition(forApp("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$"))
        "other" -> builder.withCondition(unlessApp("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$"))
        // Add more app targets if needed
    }
}


// --- KeyMapBuilder ---

fun keymap(description: String): KeyMapBuilder {
    return KeyMapBuilder(description)
}

class KeyMapBuilder(private val description: String) {
    private val manipulators = mutableListOf<Manipulator>()
    private var currentConditions = mutableListOf<Condition>() // Conditions active for subsequent remaps

    inner class RemapToBuilder(private val fromKey: KeyCode, private val fromModifiers: List<ModifiersKeys>?) {
        fun to(targetKey: KeyCode, targetModifiers: List<ModifiersKeys> = emptyList()): RemapFinalizer {
            val baseManipulatorBuilder = manipulator()
                .from(fromKey, mandatoryModifiers = fromModifiers) // Assuming direct remap modifiers are mandatory
                .to(targetKey, modifiers = targetModifiers)

            currentConditions.forEach { baseManipulatorBuilder.withCondition(it) }

            // Add the manipulator provisionally. It might be replaced by whenAlone/otherwise.
            val provisionalManipulator = baseManipulatorBuilder.build()
            manipulators.add(provisionalManipulator)
            val lastAddedIndex = manipulators.lastIndex

            return RemapFinalizer(lastAddedIndex, provisionalManipulator, fromKey, fromModifiers, targetKey, targetModifiers)
        }

        fun to(targetKey: KeyCode, vararg targetModifiers: ModifiersKeys): RemapFinalizer {
            return to(targetKey, targetModifiers.toList())
        }
    }

    inner class RemapFinalizer(
        private val manipulatorIndex: Int,
        private val originalToManipulator: Manipulator, // The simple .to() manipulator
        private val fromKey: KeyCode,
        private val fromModifiers: List<ModifiersKeys>?,
        private val toKeyIfAlone: KeyCode, // In this context, the .to() key becomes the if_alone key
        private val toModifiersIfAlone: List<ModifiersKeys>
    ) {
        /**
         * Configures the remapping to trigger `toKeyIfAlone` when `fromKey` is pressed alone,
         * and a different key (the `heldKey`) when `fromKey` is held.
         */
        fun otherwise(heldKey: KeyCode, heldModifiers: List<ModifiersKeys> = emptyList()): KeyMapBuilder {
            val dualRoleManipulator = manipulator()
                .from(fromKey, mandatoryModifiers = fromModifiers)
                .to(heldKey, modifiers = heldModifiers) // This is the primary action when held
                .toIfAlone(toKeyIfAlone, modifiers = toModifiersIfAlone) // This is the action if alone
                .build()
            manipulators[manipulatorIndex] = applyConditionsToExisting(dualRoleManipulator, originalToManipulator.conditions)
            return this@KeyMapBuilder
        }

        fun otherwise(heldKey: KeyCode, vararg heldModifiers: ModifiersKeys): KeyMapBuilder {
            return otherwise(heldKey, heldModifiers.toList())
        }

        /**
         * Configures the remapping to trigger `toKeyIfAlone` when `fromKey` is pressed alone.
         * This implies that the original `.to()` mapping is for the held state, which is what
         * the `keymap("Right Cmd (alone) -> Enter").remap("right_command").to("return_or_enter").whenAlone()` implies.
         * So, the original `to()` becomes the held state, and `whenAlone()` specifies the alone state.
         */
        fun whenAlone(): KeyMapBuilder {
            // The original .to() key in RemapToBuilder now becomes the key when held.
            // The .toIfAlone() is the key specified in the initial .to() call.
            val originalToTarget = originalToManipulator.to?.firstOrNull()?.keyCode ?: throw IllegalStateException("Original .to() target key not found for whenAlone logic")
            val originalToModifiers = originalToManipulator.to?.firstOrNull()?.modifiers ?: emptyList()

            val dualRoleManipulator = manipulator()
                .from(fromKey, mandatoryModifiers = fromModifiers)
                .to(originalToTarget, modifiers = originalToModifiers) // This is the held action
                .toIfAlone(toKeyIfAlone, modifiers = toModifiersIfAlone) // This is the alone action specified by the initial .to()
                .build()
            manipulators[manipulatorIndex] = applyConditionsToExisting(dualRoleManipulator, originalToManipulator.conditions)
            return this@KeyMapBuilder
        }
    }

    private fun applyConditionsToExisting(manipulator: Manipulator, conditions: List<Condition>?): Manipulator {
        return if (conditions.isNullOrEmpty()) manipulator else manipulator.copy(conditions = (manipulator.conditions ?: emptyList()) + conditions)
    }

    fun remap(key: KeyCode, modifiers: List<ModifiersKeys>? = null): RemapToBuilder {
        return RemapToBuilder(key, modifiers)
    }

    fun remap(key: KeyCode, vararg modifiers: ModifiersKeys): RemapToBuilder {
        return RemapToBuilder(key, modifiers.toList())
    }

    fun forApps(vararg bundleIds: String): KeyMapBuilder = apply {
        currentConditions.add(forApp(*bundleIds))
    }

    fun unlessApps(vararg bundleIds: String): KeyMapBuilder = apply {
        currentConditions.add(unlessApp(*bundleIds))
    }

    fun clearConditions(): KeyMapBuilder = apply {
        currentConditions.clear()
    }

    fun build(): KarabinerRules {
        // Before building, ensure no provisional manipulators are left hanging if .whenAlone() or .otherwise() wasn't called
        // This is implicitly handled by the design: .to() always adds a manipulator.
        // .whenAlone() or .otherwise() replaces it.
        return rule(description, manipulators.toList()) // Return a copy of the list
    }
}

// TODO: Review ARROW_KEYS and VIM_NAV_KEYS constants - might be better as part of a companion object or top-level consts if needed.