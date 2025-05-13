package sh.kau.karabiner

import kotlinx.serialization.json.JsonPrimitive
import sh.kau.karabiner.Condition.FrontmostApplicationIfCondition
import sh.kau.karabiner.Condition.FrontmostApplicationUnlessCondition
import sh.kau.karabiner.Condition.VariableIfCondition

typealias ShellCmd = String

// region Rule Datastructures
// -----------------------------------------
open class MappingRule(
    var fromKey: KeyCode? = null,
    var fromModifiers: FromModifiers? = null,
    var toKey: KeyCode? = null,
    var toKeyIfAlone: KeyCode? = null,
    var shellCommand: ShellCmd? = null,
    var toModifiers: List<ModifierKeyCode?>? = null,
    var mouseKey: MouseKey? = null,
    var pointingButton: String? = null,
) {
  var conditions = mutableListOf<Condition>()

  fun forDevice(block: Condition.DeviceIfCondition.() -> Unit) {
    val cond = Condition.DeviceIfCondition()
    cond.block()
    conditions.add(cond)
  }

  fun forApp(block: FrontmostApplicationIfCondition.() -> Unit) {
    val cond = FrontmostApplicationIfCondition()
    cond.block()
    conditions.add(cond)
  }

  fun unlessApp(block: FrontmostApplicationUnlessCondition.() -> Unit) {
    val cond = FrontmostApplicationUnlessCondition()
    cond.block()
    conditions.add(cond)
  }
}

class SingleRule(
    var description: String = "",
    var layerKey: KeyCode? = null,
) : MappingRule()

/** Notice Layer Key Rule allows nested [mappings] whereas [SingleRule] allows just one mapping */
class LayerKeyRule(
    var description: String = "",
    var layerKey: KeyCode? = null,
) {
  internal var mappings = mutableListOf<MappingRule>()

  fun mapping(block: MappingRule.() -> Unit) {
    mappings.add(MappingRule().apply(block))
  }

  fun variableName() = "${layerKey!!.name.lowercase()}-layer"
}

// endregion

// region Karabiner Rule DSL
// --------------------------

@Deprecated("uses more complicated builder")
fun karabinerRule(description: String, vararg manipulators: Manipulator): KarabinerRule {
  return KarabinerRule(description, manipulators.toList())
}

/** Just allows for a nicer API (without mapping nesting) for Single Rules */
fun karabinerRuleSingle(
    block: SingleRule.() -> Unit,
): KarabinerRule {
  val singleRule = SingleRule().apply(block)
  return karabinerRule {
    description = singleRule.description
    layerKey = singleRule.layerKey
    mapping {
      fromKey = singleRule.fromKey
      fromModifiers = singleRule.fromModifiers
      toKey = singleRule.toKey
      toModifiers = singleRule.toModifiers
      toKeyIfAlone = singleRule.toKeyIfAlone
      shellCommand = singleRule.shellCommand
      mouseKey = singleRule.mouseKey
      pointingButton = singleRule.pointingButton
      conditions = singleRule.conditions
    }
  }
}

fun karabinerRule(
    block: LayerKeyRule.() -> Unit,
): KarabinerRule {
  val layerKeyRule = LayerKeyRule().apply(block)
  val manipulators = mutableListOf<Manipulator>()

  layerKeyRule.mappings.forEach { keyMapping ->
    val fromModifier =
        From.with(
            keyMapping.fromKey!!,
            keyMapping.fromModifiers,
        )

    val toModifier =
        To.with(
            keyMapping.toKey,
            keyMapping.toModifiers,
            keyMapping.shellCommand,
            keyMapping.mouseKey,
            keyMapping.pointingButton
        )

    if (layerKeyRule.layerKey == null) {
      val toAloneModifer = keyMapping.toKeyIfAlone?.let { To.with(keyMapping.toKeyIfAlone) }

      manipulators +=
          Manipulator(
              from = fromModifier,
              to = toModifier,
              toIfAlone = toAloneModifer,
              conditions = if (keyMapping.conditions.isEmpty()) null else keyMapping.conditions,
          )
    } else {
      val variableName = layerKeyRule.variableName()
      // Layer Keys will need an onPress manipulator and an onRelease manipulator
      manipulators +=
          Manipulator(
              from = fromModifier,
              to = toModifier,
              conditions = ifVarSet(variableName) + keyMapping.conditions)

      manipulators +=
          Manipulator(
              from =
                  From(
                      simultaneous = listOf(layerKeyRule.layerKey!!, keyMapping.fromKey!!),
                      simultaneousOptions = buildSimultaneousOptionsVar(variableName),
                  ),
              to = setVarOn(toModifier, variableName),
              parameters = Parameters(simultaneousThresholdMilliseconds = 250),
              conditions = keyMapping.conditions)
    }
  }

  return KarabinerRule(layerKeyRule.description, manipulators)
}

// endregion

// region builder instructions
// ---------------------------

fun buildSimultaneousOptionsVar(variableName: String) =
    SimultaneousOptions(
        detectKeyDownUninterruptedly = true,
        keyDownOrder = "strict",
        keyUpOrder = "strict_inverse",
        keyUpWhen = "any",
        toAfterKeyUp = unsetVar(variableName),
    )

fun setVarOn(to: List<To>, variableName: String): List<To> =
    listOf(To(setVariable = SetVariable(variableName, JsonPrimitive(1)))) + to

fun unsetVar(variableName: String): List<To> =
    listOf(To(setVariable = SetVariable(variableName, JsonPrimitive(0))))

fun ifVarSet(variableName: String) = listOf(VariableIfCondition(variableName, JsonPrimitive(1)))

// endregion

fun forApp(vararg bundleIdentifiers: String): Condition {
  return FrontmostApplicationIfCondition(bundleIds = bundleIdentifiers.toList())
}

fun unlessApp(vararg bundleIdentifiers: String): Condition {
  return FrontmostApplicationUnlessCondition(bundleIds = bundleIdentifiers.toList())
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

  // --- LayerKey additions ---
  private var layerTriggerKey: KeyCode? = null

  //  private var layerSimultaneousThreshold: Long = 250L

  fun from(
      keyCode: KeyCode,
      optionalModifiers: List<ModifierKeyCode>? = null,
      mandatoryModifiers: List<ModifierKeyCode>? = null
  ): ManipulatorBuilder = apply {
    val mods =
        if (optionalModifiers != null || mandatoryModifiers != null) {
          FromModifiers(optional = optionalModifiers, mandatory = mandatoryModifiers)
        } else {
          // Default to optional: [ANY] for dual-role keys
          FromModifiers(optional = listOf(ModifierKeyCode.ANY))
        }
    this.from = From(keyCode = keyCode, modifiers = mods)
  }

  fun to(
      keyCode: KeyCode? = null,
      modifiers: List<ModifierKeyCode?>? = null,
      setVariable: SetVariable? = null,
      toObj: To? = null,
      mouseKey: MouseKey? = null,
      pointingButton: String? = null,
      type: ToType = ToType.NORMAL
  ): ManipulatorBuilder {
    val nSet = listOfNotNull(keyCode, setVariable, toObj, mouseKey, pointingButton).size
    if (nSet == 0)
        throw IllegalArgumentException(
            "You must specify one of toObj, keyCode, setVariable, mouseKey, or pointingButton")
    if (listOfNotNull(toObj).isNotEmpty() &&
        listOfNotNull(keyCode, setVariable, mouseKey, pointingButton).isNotEmpty()) {
      throw IllegalArgumentException(
          "Specify only one of toObj, keyCode, setVariable, mouseKey, or pointingButton")
    }
    val toEvent =
        toObj
            ?: To(
                keyCode = keyCode,
                modifiers = modifiers,
                setVariable = setVariable,
                mouseKey = mouseKey,
                pointingButton = pointingButton)
    when (type) {
      ToType.NORMAL -> addToEventList(this.to, toEvent)
      ToType.IF_ALONE -> addToEventList(this.toIfAlone, toEvent)
      ToType.AFTER_KEY_UP -> addToEventList(this.toAfterKeyUp, toEvent)
      ToType.IF_HELD_DOWN -> addToEventList(this.toIfHeldDown, toEvent)
    }
    return this
  }

  //  fun toIfAlone(
  //      keyCode: KeyCode? = null,
  //      modifiers: List<ModifierKeyCode>? = null,
  //      setVariable: SetVariable? = null,
  //      toObj: To? = null,
  //      mouseKey: MouseKey? = null,
  //      pointingButton: String? = null,
  //  ): ManipulatorBuilder =
  //      to(keyCode, modifiers, setVariable, toObj, mouseKey, pointingButton, ToType.IF_ALONE)

  private fun addToEventList(eventList: MutableList<To>, command: To): ManipulatorBuilder = apply {
    eventList.add(command)
  }

  fun withCondition(condition: Condition): ManipulatorBuilder = apply {
    this.conditions.add(condition)
  }

  fun build(): Manipulator {
    // If not a layerKey, return a single manipulator as before
    if (layerTriggerKey == null) {
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
    throw IllegalStateException("Use buildLayer() for layerKey usage")
  }
}
