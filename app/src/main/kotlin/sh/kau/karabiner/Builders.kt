package sh.kau.karabiner

import kotlinx.serialization.json.JsonPrimitive
import sh.kau.karabiner.Condition.FrontmostApplicationIfCondition
import sh.kau.karabiner.Condition.FrontmostApplicationUnlessCondition
import sh.kau.karabiner.Condition.VariableIfCondition

fun karabinerRule(description: String, vararg manipulators: Manipulator): KarabinerRule {
  return KarabinerRule(description, manipulators.toList())
}

fun forApp(vararg bundleIdentifiers: String): Condition {
  return FrontmostApplicationIfCondition(bundleIdentifiers = bundleIdentifiers.toList())
}

fun unlessApp(vararg bundleIdentifiers: String): Condition {
  return FrontmostApplicationUnlessCondition(bundleIdentifiers = bundleIdentifiers.toList())
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
    val mods =
        if (optionalModifiers != null || mandatoryModifiers != null) {
          Modifiers(optional = optionalModifiers, mandatory = mandatoryModifiers)
        } else {
          // Default to optional: [ANY] for dual-role keys
          Modifiers(optional = listOf(ModifiersKeys.ANY))
        }
    this.from = From(keyCode = keyCode, modifiers = mods)
  }

  fun from(
      simultaneousKeys: List<KeyCode>,
      simultaneousOptions: SimultaneousOptions? = null
  ): ManipulatorBuilder = apply {
    this.from = From(simultaneous = simultaneousKeys, simultaneousOptions = simultaneousOptions)
  }

  private fun addToEventList(eventList: MutableList<To>, command: To): ManipulatorBuilder = apply {
    eventList.add(command)
  }

  fun to(command: To): ManipulatorBuilder = addToEventList(this.to, command)

  fun to(
      keyCode: KeyCode,
      modifiers: List<ModifiersKeys>? = null,
  ): ManipulatorBuilder = to(To(keyCode = keyCode, modifiers = modifiers))

  fun toSetVariable(name: String, value: Number): ManipulatorBuilder =
      to(To(setVariable = SetVariable(name, JsonPrimitive(value))))

  fun toIfAlone(command: To): ManipulatorBuilder = addToEventList(this.toIfAlone, command)

  fun toIfAlone(keyCode: KeyCode, modifiers: List<ModifiersKeys>?): ManipulatorBuilder =
      toIfAlone(To(keyCode = keyCode, modifiers = modifiers))

  fun toIfAlone(keyCode: KeyCode, vararg modifiers: ModifiersKeys): ManipulatorBuilder {
    val modifiersList = if (modifiers.isEmpty()) null else modifiers.toList()
    return toIfAlone(keyCode, modifiers = modifiersList)
  }

  fun withCondition(condition: Condition): ManipulatorBuilder = apply {
    this.conditions.add(condition)
  }

  fun ifVariable(name: String, value: Number): ManipulatorBuilder =
      withCondition(VariableIfCondition(name = name, value = JsonPrimitive(value)))

  fun withParameters(parameters: Parameters): ManipulatorBuilder = apply {
    this.parameters = parameters
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
        description = this.description)
  }
}

// Define these similar to how they were in builders.ts if needed for Layer logic
// For now, assuming they are not directly used by the LayerBuilder structure itself
// but might be used in the rule definitions later.
// const val ARROW_KEYS: List<KeyCode> = listOf(KeyCode.LEFT_ARROW, KeyCode.DOWN_ARROW,
// KeyCode.UP_ARROW, KeyCode.RIGHT_ARROW)
// const val VIM_NAV_KEYS: List<KeyCode> = listOf(KeyCode.H, KeyCode.J, KeyCode.K, KeyCode.L)

fun layer(triggerKey: KeyCode): LayerBuilder {
  return LayerBuilder(triggerKey)
}

/** Represents a binding for a key within a layer. */
data class LayerBinding(
    val targetKey: KeyCode,
    val targetModifiers: List<ModifiersKeys>?,
    val appTarget: String? = null // e.g., "terminal", "other"
)

class LayerBuilder(private val triggerKey: KeyCode) {
  private val bindings =
      mutableMapOf<String, LayerBinding>() // Key is "sourceKey_appTarget" or "sourceKey"
  private val conditions = mutableListOf<Condition>()
  private var threshold: Long = 250L // ms

  inner class LayerKeyBinder(private val sourceKey: KeyCode) {
    fun to(
        targetKey: KeyCode,
        targetModifiers: List<ModifiersKeys>?,
        appTarget: String? = null
    ): LayerBuilder {
      val bindingKey = appTarget?.let { "${sourceKey.name}_${it}" } ?: sourceKey.name
      bindings[bindingKey] = LayerBinding(targetKey, targetModifiers, appTarget)
      return this@LayerBuilder
    }

    fun to(
        targetKey: KeyCode,
        vararg targetModifiers: ModifiersKeys,
        appTarget: String? = null
    ): LayerBuilder {
      val modifierList = if (targetModifiers.isEmpty()) null else targetModifiers.toList()
      return to(targetKey, modifierList, appTarget)
    }
  }

  fun bind(sourceKey: KeyCode): LayerKeyBinder {
    return LayerKeyBinder(sourceKey)
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
              simultaneousThreshold = threshold))
    }
    return result.toList()
  }
}

/**
 * Internal helper to create manipulators for a single layer binding. This combines the logic from
 * the original createKeyLayer and parts of LayerBuilder.build().
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

  val toOutput = To(keyCode = layerBinding.targetKey, modifiers = layerBinding.targetModifiers)

  // 1. Mode-based manipulator (e.g., if "f-mode" is 1, then j -> paren)
  val modeManipulatorBuilder =
      ManipulatorBuilder()
          .from(sourceKey)
          .to(toOutput)
          .ifVariable(variableName, 1) // Assuming variable value is Int/Long for mode

  layerConditions.forEach { modeManipulatorBuilder.withCondition(it) }
  applyAppTargetCondition(modeManipulatorBuilder, layerBinding.appTarget)
  manipulators.add(modeManipulatorBuilder.build())

  // 2. Simultaneous manipulator (e.g., f+j simultaneously -> paren and set "f-mode" = 1)
  val simultaneousManipulatorBuilder =
      ManipulatorBuilder()
          .from(
              simultaneousKeys = listOf(triggerKey, sourceKey),
              simultaneousOptions =
                  SimultaneousOptions(
                      detectKeyDownUninterruptedly = true,
                      keyDownOrder = "strict",
                      keyUpOrder = "strict_inverse",
                      keyUpWhen = "any",
                      toAfterKeyUp =
                          listOf(To(setVariable = SetVariable(variableName, JsonPrimitive(0))))))
          .toSetVariable(variableName, 1) // Set mode to 1
          .to(toOutput) // Then perform the action
          .withParameters(
              Parameters(
                  simultaneousThresholdMilliseconds = simultaneousThreshold,
                  toDelayedActionDelayMilliseconds = 10,
                  toIfAloneTimeoutMilliseconds = 250,
                  toIfHeldDownThresholdMilliseconds = 500))

  layerConditions.forEach { simultaneousManipulatorBuilder.withCondition(it) }
  applyAppTargetCondition(simultaneousManipulatorBuilder, layerBinding.appTarget)
  manipulators.add(simultaneousManipulatorBuilder.build())

  return manipulators.toList()
}

/** Helper to apply app-specific conditions based on appTarget string. */
private fun applyAppTargetCondition(builder: ManipulatorBuilder, appTarget: String?) {
  appTarget ?: return
  when (appTarget.lowercase()) {
    "terminal" ->
        builder.withCondition(forApp("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$"))

    "other" ->
        builder.withCondition(unlessApp("^com\\.apple\\.Terminal$", "^com\\.googlecode\\.iterm2$"))
  // Add more app targets if needed
  }
}

// TODO: Review ARROW_KEYS and VIM_NAV_KEYS constants - might be better as part of a companion
// object or top-level consts if needed.
