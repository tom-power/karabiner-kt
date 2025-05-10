package sh.kau.karabiner

import java.lang.ProcessBuilder.Redirect.to
import kotlinx.serialization.json.JsonPrimitive
import sh.kau.karabiner.Condition.FrontmostApplicationIfCondition
import sh.kau.karabiner.Condition.FrontmostApplicationUnlessCondition
import sh.kau.karabiner.Condition.VariableIfCondition
import sun.security.util.SignatureUtil.fromKey

typealias ShellCmd = String

fun karabinerRule(description: String, vararg manipulators: Manipulator): KarabinerRule {
  return KarabinerRule(description, manipulators.toList())
}

fun karabinerRule(
    block: SimpleRuleBuilder.() -> Unit,
): KarabinerRule {

  val builder = SimpleRuleBuilder().apply(block)

  if (builder.layerKey != null) {
    val variableName = "${builder.layerKey!!.name.lowercase()}-layer"

    return KarabinerRule(
        builder.description,
        listOf(
            Manipulator(
                from =
                    From(
                        builder.fromKey!!,
                        modifiers = Modifiers(optional = listOf(ModifiersKeys.ANY))),
                to = listOf(To(shellCommand = builder.shellCommand)),
                conditions = listOf(VariableIfCondition(variableName, JsonPrimitive(1))),
            ),
            Manipulator(
                from =
                    From(
                        simultaneous = listOf(builder.layerKey!!, builder.fromKey!!),
                        simultaneousOptions =
                            SimultaneousOptions(
                                detectKeyDownUninterruptedly = true,
                                keyDownOrder = "strict",
                                keyUpOrder = "strict_inverse",
                                keyUpWhen = "any",
                                toAfterKeyUp =
                                    listOf(
                                        To(
                                            setVariable =
                                                SetVariable(variableName, JsonPrimitive(0)))),
                            ),
                    ),
                to =
                    listOf(
                        To(setVariable = SetVariable(variableName, JsonPrimitive(1))),
                        To(shellCommand = builder.shellCommand),
                    ),
                parameters = Parameters(simultaneousThresholdMilliseconds = 250)
            ),
        ))
  }

  throw IllegalStateException("Not implemented")
}

class SimpleRuleBuilder(
    var description: String = "",
    var layerKey: KeyCode? = null,
    var fromKey: KeyCode? = null,
    var shellCommand: ShellCmd? = null,
)

fun forApp(vararg bundleIdentifiers: String): Condition {
  return FrontmostApplicationIfCondition(bundleIdentifiers = bundleIdentifiers.toList())
}

fun unlessApp(vararg bundleIdentifiers: String): Condition {
  return FrontmostApplicationUnlessCondition(bundleIdentifiers = bundleIdentifiers.toList())
}

/** Represents a shell command to be executed by Karabiner. */
data class ShellCommand(val cmd: String)

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
  private var layerSimultaneousThreshold: Long = 250L

  fun layerKey(triggerKey: KeyCode, simultaneousThreshold: Long = 250L): ManipulatorBuilder =
      apply {
        this.layerTriggerKey = triggerKey
        this.layerSimultaneousThreshold = simultaneousThreshold
      }

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

  fun to(
      keyCode: KeyCode? = null,
      modifiers: List<ModifiersKeys>? = null,
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

  fun to(
      shellCommand: ShellCommand,
  ): ManipulatorBuilder {
    val toEvent = To(shellCommand = shellCommand.cmd)
    addToEventList(this.to, toEvent)
    return this
  }

  fun toIfAlone(
      keyCode: KeyCode? = null,
      modifiers: List<ModifiersKeys>? = null,
      setVariable: SetVariable? = null,
      toObj: To? = null,
      mouseKey: MouseKey? = null,
      pointingButton: String? = null,
  ): ManipulatorBuilder =
      to(keyCode, modifiers, setVariable, toObj, mouseKey, pointingButton, ToType.IF_ALONE)

  private fun addToEventList(eventList: MutableList<To>, command: To): ManipulatorBuilder = apply {
    eventList.add(command)
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

  fun buildLayer(): List<Manipulator> {
    if (layerTriggerKey == null)
        throw IllegalStateException("buildLayer() requires layerKey to be set")
    val triggerKey = layerTriggerKey!!
    val variableName = "${triggerKey.name.lowercase()}-mode"
    val fromKey =
        this.from?.keyCode
            ?: throw IllegalStateException("'from' keyCode must be set for layerKey usage")
    val fromModifiers = this.from?.modifiers
    val toList = if (this.to.isNotEmpty()) this.to.toList() else null
    val toIfAloneList = if (this.toIfAlone.isNotEmpty()) this.toIfAlone.toList() else null
    val toAfterKeyUpList = if (this.toAfterKeyUp.isNotEmpty()) this.toAfterKeyUp.toList() else null
    val toIfHeldDownList = if (this.toIfHeldDown.isNotEmpty()) this.toIfHeldDown.toList() else null
    val params = this.parameters
    val conds = if (this.conditions.isNotEmpty()) this.conditions.toList() else null
    // 1. Mode manipulator (when variable is set)
    val modeManipulator =
        Manipulator(
            type = this.type,
            from = From(keyCode = fromKey, modifiers = fromModifiers),
            to = toList,
            toIfAlone = toIfAloneList,
            toAfterKeyUp = toAfterKeyUpList,
            toIfHeldDown = toIfHeldDownList,
            parameters = params,
            conditions =
                (conds ?: emptyList()) +
                    listOf(Condition.VariableIfCondition(variableName, JsonPrimitive(1))),
            description = this.description)
    // 2. Simultaneous manipulator (triggerKey + fromKey)
    val simOptions =
        SimultaneousOptions(
            detectKeyDownUninterruptedly = true,
            keyDownOrder = "strict",
            keyUpOrder = "strict_inverse",
            keyUpWhen = "any",
            toAfterKeyUp = listOf(To(setVariable = SetVariable(variableName, JsonPrimitive(0)))))
    val simFrom = From(simultaneous = listOf(triggerKey, fromKey), simultaneousOptions = simOptions)
    val simTo = mutableListOf<To>(To(setVariable = SetVariable(variableName, JsonPrimitive(1))))
    if (toList != null) simTo.addAll(toList)
    val simManipulator =
        Manipulator(
            type = this.type,
            from = simFrom,
            to = simTo,
            toIfAlone = toIfAloneList,
            toAfterKeyUp = toAfterKeyUpList,
            toIfHeldDown = toIfHeldDownList,
            parameters =
                Parameters(
                    simultaneousThresholdMilliseconds = layerSimultaneousThreshold,
                    toDelayedActionDelayMilliseconds = 10,
                    toIfAloneTimeoutMilliseconds = 250,
                    toIfHeldDownThresholdMilliseconds = 500),
            conditions = conds,
            description = this.description)
    return listOf(modeManipulator, simManipulator)
  }
}
