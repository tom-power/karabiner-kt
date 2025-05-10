package sh.kau.karabiner

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class KarabinerRule(
    val description: String? = null,
    val manipulators: List<Manipulator>? = null
)

@Serializable
data class Manipulator(
    val type: String = "basic", // Typically "basic"
    val from: From,
    val to: List<To>? = null,
    @SerialName("to_if_alone") val toIfAlone: List<To>? = null,
    @SerialName("to_after_key_up") val toAfterKeyUp: List<To>? = null,
    @SerialName("to_if_held_down") // This was missing in TS types but present in JSON
    val toIfHeldDown: List<To>? = null,
    val parameters: Parameters? = null,
    val conditions: List<Condition>? = null,
    val description: String? = null
)

@Serializable
data class From(
    @SerialName("key_code") val keyCode: KeyCode? = null,
    val modifiers: Modifiers? = null,
    @Serializable(with = SimultaneousKeyCodeListSerializer::class)
    val simultaneous: List<KeyCode>? = null,
    @SerialName("simultaneous_options") val simultaneousOptions: SimultaneousOptions? = null,
)

@Serializable
data class Modifiers(
    val optional: List<ModifiersKeys>? = null,
    val mandatory: List<ModifiersKeys>? = null
)

@Serializable
data class To(
    @SerialName("key_code") val keyCode: KeyCode? = null,
    val modifiers: List<ModifiersKeys>? = null,
    @SerialName("consumer_key_code") val consumerKeyCode: String? = null,
    @SerialName("shell_command") val shellCommand: String? = null,
    @SerialName("set_variable") val setVariable: SetVariable? = null,
    @SerialName("mouse_key") val mouseKey: MouseKey? = null,
    @SerialName("pointing_button") val pointingButton: String? = null,
    @SerialName("software_function") val softwareFunction: SoftwareFunction? = null
    // `hold_down_milliseconds` is sometimes seen with `to` events. Add if needed.
)

@Serializable
data class Parameters(
    @SerialName("basic.simultaneous_threshold_milliseconds")
    val simultaneousThresholdMilliseconds: Long?,
    @SerialName("basic.to_delayed_action_delay_milliseconds")
    val toDelayedActionDelayMilliseconds: Long?,
    @SerialName("basic.to_if_alone_timeout_milliseconds") val toIfAloneTimeoutMilliseconds: Long?,
    @SerialName("basic.to_if_held_down_threshold_milliseconds")
    val toIfHeldDownThresholdMilliseconds: Long?,
    // Potentially add other parameters if they exist, or allow for a map
    // For now, sticking to explicitly defined ones
)

@Serializable
data class SimultaneousOptions(
    @SerialName("key_down_order")
    val keyDownOrder: String? = null, // Consider enum: "insensitive", "strict", "strict_inverse"
    @SerialName("detect_key_down_uninterruptedly")
    val detectKeyDownUninterruptedly: Boolean? = null,
    @SerialName("key_up_order")
    val keyUpOrder: String? = null, // Consider enum: "insensitive", "strict", "strict_inverse"
    @SerialName("key_up_when") val keyUpWhen: String? = null, // Consider enum: "any", "all"
    @SerialName("to_after_key_up") val toAfterKeyUp: List<To>? = null
)

@Serializable data class SetVariable(val name: String, val value: JsonPrimitive)

@Serializable
data class MouseKey(
    val x: Int? = null,
    val y: Int? = null,
    @SerialName("speed_multiplier") val speedMultiplier: Double? = null,
    @SerialName("vertical_wheel") val verticalWheel: Int? = null,
    @SerialName("horizontal_wheel") val horizontalWheel: Int? = null
)

@Serializable
data class SoftwareFunction(
    @SerialName("iokit_power_management_sleep_system")
    val iokitPowerManagementSleepSystem: JsonObject? = null // Representing an empty object {}
)

@Serializable
enum class KeyCode {
  @SerialName("caps_lock") CAPS_LOCK,
  @SerialName("left_control") LEFT_CONTROL,
  @SerialName("left_shift") LEFT_SHIFT,
  @SerialName("left_option") LEFT_OPTION,
  @SerialName("left_command") LEFT_COMMAND,
  @SerialName("right_control") RIGHT_CONTROL,
  @SerialName("right_shift") RIGHT_SHIFT,
  @SerialName("right_option") RIGHT_OPTION,
  @SerialName("right_command") RIGHT_COMMAND,
  @SerialName("fn") FN,
  @SerialName("return_or_enter") RETURN_OR_ENTER,
  @SerialName("escape") ESCAPE,
  @SerialName("delete_or_backspace") DELETE_OR_BACKSPACE,
  @SerialName("delete_forward") DELETE_FORWARD,
  @SerialName("tab") TAB,
  @SerialName("spacebar") SPACEBAR,
  @SerialName("hyper") HYPER,
  @SerialName("hyphen") HYPHEN,
  @SerialName("equal_sign") EQUAL_SIGN,
  @SerialName("open_bracket") OPEN_BRACKET,
  @SerialName("close_bracket") CLOSE_BRACKET,
  @SerialName("backslash") BACKSLASH,
  @SerialName("non_us_pound") NON_US_POUND,
  @SerialName("semicolon") SEMICOLON,
  @SerialName("quote") QUOTE,
  @SerialName("grave_accent_and_tilde") GRAVE_ACCENT_AND_TILDE,
  @SerialName("comma") COMMA,
  @SerialName("period") PERIOD,
  @SerialName("slash") SLASH,
  @SerialName("non_us_backslash") NON_US_BACKSLASH,
  @SerialName("up_arrow") UP_ARROW,
  @SerialName("down_arrow") DOWN_ARROW,
  @SerialName("left_arrow") LEFT_ARROW,
  @SerialName("right_arrow") RIGHT_ARROW,
  @SerialName("page_up") PAGE_UP,
  @SerialName("page_down") PAGE_DOWN,
  @SerialName("home") HOME,
  @SerialName("end") END,
  @SerialName("a") A,
  @SerialName("b") B,
  @SerialName("c") C,
  @SerialName("d") D,
  @SerialName("e") E,
  @SerialName("f") F,
  @SerialName("g") G,
  @SerialName("h") H,
  @SerialName("i") I,
  @SerialName("j") J,
  @SerialName("k") K,
  @SerialName("l") L,
  @SerialName("m") M,
  @SerialName("n") N,
  @SerialName("o") O,
  @SerialName("p") P,
  @SerialName("q") Q,
  @SerialName("r") R,
  @SerialName("s") S,
  @SerialName("t") T,
  @SerialName("u") U,
  @SerialName("v") V,
  @SerialName("w") W,
  @SerialName("x") X,
  @SerialName("y") Y,
  @SerialName("z") Z,
  @SerialName("1") NUM_1,
  @SerialName("2") NUM_2,
  @SerialName("3") NUM_3,
  @SerialName("4") NUM_4,
  @SerialName("5") NUM_5,
  @SerialName("6") NUM_6,
  @SerialName("7") NUM_7,
  @SerialName("8") NUM_8,
  @SerialName("9") NUM_9,
  @SerialName("0") NUM_0,
  @SerialName("f1") F1,
  @SerialName("f2") F2,
  @SerialName("f3") F3,
  @SerialName("f4") F4,
  @SerialName("f5") F5,
  @SerialName("f6") F6,
  @SerialName("f7") F7,
  @SerialName("f8") F8,
  @SerialName("f9") F9,
  @SerialName("f10") F10,
  @SerialName("f11") F11,
  @SerialName("f12") F12,
  @SerialName("f13") F13,
  @SerialName("f14") F14,
  @SerialName("f15") F15,
  @SerialName("f16") F16,
  @SerialName("f17") F17,
  @SerialName("f18") F18,
  @SerialName("f19") F19,
  @SerialName("f20") F20,
  @SerialName("f21") F21,
  @SerialName("f22") F22,
  @SerialName("f23") F23,
  @SerialName("f24") F24,
  @SerialName("display_brightness_decrement") DISPLAY_BRIGHTNESS_DECREMENT,
  @SerialName("display_brightness_increment") DISPLAY_BRIGHTNESS_INCREMENT,
  @SerialName("mission_control") MISSION_CONTROL,
  @SerialName("launchpad") LAUNCHPAD,
  @SerialName("dashboard") DASHBOARD,
  @SerialName("illumination_decrement") ILLUMINATION_DECREMENT,
  @SerialName("illumination_increment") ILLUMINATION_INCREMENT,
  @SerialName("rewind") REWIND,
  @SerialName("play_or_pause") PLAY_OR_PAUSE,
  @SerialName("fastforward") FASTFORWARD,
  @SerialName("mute") MUTE,
  @SerialName("volume_decrement") VOLUME_DECREMENT,
  @SerialName("volume_increment") VOLUME_INCREMENT,
  @SerialName("eject") EJECT,
  @SerialName("apple_display_brightness_decrement") APPLE_DISPLAY_BRIGHTNESS_DECREMENT,
  @SerialName("apple_display_brightness_increment") APPLE_DISPLAY_BRIGHTNESS_INCREMENT,
  @SerialName("apple_top_case_display_brightness_decrement")
  APPLE_TOP_CASE_DISPLAY_BRIGHTNESS_DECREMENT,
  @SerialName("apple_top_case_display_brightness_increment")
  APPLE_TOP_CASE_DISPLAY_BRIGHTNESS_INCREMENT,
  @SerialName("keypad_num_lock") KEYPAD_NUM_LOCK,
  @SerialName("keypad_slash") KEYPAD_SLASH,
  @SerialName("keypad_asterisk") KEYPAD_ASTERISK,
  @SerialName("keypad_hyphen") KEYPAD_HYPHEN,
  @SerialName("keypad_plus") KEYPAD_PLUS,
  @SerialName("keypad_enter") KEYPAD_ENTER,
  @SerialName("keypad_1") KEYPAD_1,
  @SerialName("keypad_2") KEYPAD_2,
  @SerialName("keypad_3") KEYPAD_3,
  @SerialName("keypad_4") KEYPAD_4,
  @SerialName("keypad_5") KEYPAD_5,
  @SerialName("keypad_6") KEYPAD_6,
  @SerialName("keypad_7") KEYPAD_7,
  @SerialName("keypad_8") KEYPAD_8,
  @SerialName("keypad_9") KEYPAD_9,
  @SerialName("keypad_0") KEYPAD_0,
  @SerialName("keypad_period") KEYPAD_PERIOD,
  @SerialName("keypad_equal_sign") KEYPAD_EQUAL_SIGN,
  @SerialName("keypad_comma") KEYPAD_COMMA,
  @SerialName("vk_none") VK_NONE,
  @SerialName("print_screen") PRINT_SCREEN,
  @SerialName("scroll_lock") SCROLL_LOCK,
  @SerialName("pause") PAUSE,
  @SerialName("insert") INSERT,
  @SerialName("application") APPLICATION,
  @SerialName("help") HELP,
  @SerialName("power") POWER,
  @SerialName("execute") EXECUTE,
  @SerialName("menu") MENU,
  @SerialName("select") SELECT,
  @SerialName("stop") STOP,
  @SerialName("again") AGAIN,
  @SerialName("undo") UNDO,
  @SerialName("cut") CUT,
  @SerialName("copy") COPY,
  @SerialName("paste") PASTE,
  @SerialName("find") FIND,
  @SerialName("international1") INTERNATIONAL1,
  @SerialName("international2") INTERNATIONAL2,
  @SerialName("international3") INTERNATIONAL3,
  @SerialName("international4") INTERNATIONAL4,
  @SerialName("international5") INTERNATIONAL5,
  @SerialName("international6") INTERNATIONAL6,
  @SerialName("international7") INTERNATIONAL7,
  @SerialName("international8") INTERNATIONAL8,
  @SerialName("international9") INTERNATIONAL9,
  @SerialName("lang1") LANG1,
  @SerialName("lang2") LANG2,
  @SerialName("lang3") LANG3,
  @SerialName("lang4") LANG4,
  @SerialName("lang5") LANG5,
  @SerialName("lang6") LANG6,
  @SerialName("lang7") LANG7,
  @SerialName("lang8") LANG8,
  @SerialName("lang9") LANG9,
  @SerialName("japanese_eisuu") JAPANESE_EISUU,
  @SerialName("japanese_kana") JAPANESE_KANA,
  @SerialName("japanese_pc_nfer") JAPANESE_PC_NFER,
  @SerialName("japanese_pc_xfer") JAPANESE_PC_XFER,
  @SerialName("japanese_pc_katakana") JAPANESE_PC_KATAKANA,
  @SerialName("keypad_equal_sign_as400") KEYPAD_EQUAL_SIGN_AS400,
  @SerialName("locking_caps_lock") LOCKING_CAPS_LOCK,
  @SerialName("locking_num_lock") LOCKING_NUM_LOCK,
  @SerialName("locking_scroll_lock") LOCKING_SCROLL_LOCK,
  @SerialName("alternate_erase") ALTERNATE_ERASE,
  @SerialName("sys_req_or_attention") SYS_REQ_OR_ATTENTION,
  @SerialName("cancel") CANCEL,
  @SerialName("clear") CLEAR,
  @SerialName("prior") PRIOR,
  @SerialName("return") RETURN,
  @SerialName("separator") SEPARATOR,
  @SerialName("out") OUT,
  @SerialName("oper") OPER,
  @SerialName("clear_or_again") CLEAR_OR_AGAIN,
  @SerialName("cr_sel_or_props") CR_SEL_OR_PROPS,
  @SerialName("ex_sel") EX_SEL,
  @SerialName("left_alt") LEFT_ALT, // alias for left_option
  @SerialName("left_gui") LEFT_GUI, // alias for left_command
  @SerialName("right_alt") RIGHT_ALT, // alias for right_option
  @SerialName("right_gui") RIGHT_GUI, // alias for right_command
  @SerialName("vk_consumer_brightness_down") VK_CONSUMER_BRIGHTNESS_DOWN,
  @SerialName("vk_consumer_brightness_up") VK_CONSUMER_BRIGHTNESS_UP,
  @SerialName("vk_mission_control") VK_MISSION_CONTROL,
  @SerialName("vk_launchpad") VK_LAUNCHPAD,
  @SerialName("vk_dashboard") VK_DASHBOARD,
  @SerialName("vk_consumer_illumination_down") VK_CONSUMER_ILLUMINATION_DOWN,
  @SerialName("vk_consumer_illumination_up") VK_CONSUMER_ILLUMINATION_UP,
  @SerialName("vk_consumer_previous") VK_CONSUMER_PREVIOUS,
  @SerialName("vk_consumer_play") VK_CONSUMER_PLAY,
  @SerialName("vk_consumer_next") VK_CONSUMER_NEXT,
  @SerialName("volume_down") VOLUME_DOWN, // alias for volume_decrement
  @SerialName("volume_up") VOLUME_UP // alias for volume_increment
}

@Serializable
enum class ModifiersKeys {
  @SerialName("caps_lock") CAPS_LOCK,
  @SerialName("left_command") LEFT_COMMAND,
  @SerialName("left_control") LEFT_CONTROL,
  @SerialName("left_option") LEFT_OPTION,
  @SerialName("left_shift") LEFT_SHIFT,
  @SerialName("right_command") RIGHT_COMMAND,
  @SerialName("right_control") RIGHT_CONTROL,
  @SerialName("right_option") RIGHT_OPTION,
  @SerialName("right_shift") RIGHT_SHIFT,
  @SerialName("fn") FN,
  @SerialName("command") COMMAND,
  @SerialName("control") CONTROL,
  @SerialName("option") OPTION,
  @SerialName("shift") SHIFT,
  @SerialName("left_alt") LEFT_ALT,
  @SerialName("left_gui") LEFT_GUI,
  @SerialName("right_alt") RIGHT_ALT,
  @SerialName("right_gui") RIGHT_GUI,
  @SerialName("any") ANY
}

@Serializable
data class Identifiers(
    @SerialName("vendor_id") val vendorId: Long? = null,
    @SerialName("product_id") val productId: Long? = null,
    @SerialName("location_id") val locationId: Long? = null,
    @SerialName("is_keyboard") val isKeyboard: Boolean? = null,
    @SerialName("is_pointing_device") val isPointingDevice: Boolean? = null,
    @SerialName("is_touch_bar") val isTouchBar: Boolean? = null,
    @SerialName("is_built_in_keyboard") val isBuiltInKeyboard: Boolean? = null
)

@Serializable
sealed interface Condition {
  val description: String?

  @Serializable
  @SerialName("frontmost_application_if")
  data class FrontmostApplicationIfCondition(
      @SerialName("bundle_identifiers") val bundleIdentifiers: List<String>? = null,
      @SerialName("file_paths") val filePaths: List<String>? = null,
      override val description: String? = null
  ) : Condition

  @Serializable
  @SerialName("frontmost_application_unless")
  data class FrontmostApplicationUnlessCondition(
      @SerialName("bundle_identifiers") val bundleIdentifiers: List<String>? = null,
      @SerialName("file_paths") val filePaths: List<String>? = null,
      override val description: String? = null
  ) : Condition

  @Serializable
  @SerialName("device_if")
  data class DeviceIfCondition(
      val identifiers: List<Identifiers>,
      override val description: String? = null
  ) : Condition

  @Serializable
  @SerialName("device_unless")
  data class DeviceUnlessCondition(
      val identifiers: List<Identifiers>,
      override val description: String? = null
  ) : Condition

  @Serializable
  @SerialName("device_exists_if")
  data class DeviceExistsIfCondition(
      val identifiers: List<Identifiers>,
      override val description: String? = null
  ) : Condition

  @Serializable
  @SerialName("device_exists_unless")
  data class DeviceExistsUnlessCondition(
      val identifiers: List<Identifiers>,
      override val description: String? = null
  ) : Condition

  @Serializable
  @SerialName("keyboard_type_if")
  data class KeyboardTypeIfCondition(
      @SerialName("keyboard_types") val keyboardTypes: List<String>,
      override val description: String? = null
  ) : Condition

  @Serializable
  @SerialName("keyboard_type_unless")
  data class KeyboardTypeUnlessCondition(
      @SerialName("keyboard_types") val keyboardTypes: List<String>,
      override val description: String? = null
  ) : Condition

  @Serializable
  data class InputSourceSpec(
      val language: String? = null,
      @SerialName("input_source_id") val inputSourceId: String? = null,
      @SerialName("input_mode_id") val inputModeId: String? = null
  )

  @Serializable
  @SerialName("input_source_if")
  data class InputSourceIfCondition(
      @SerialName("input_sources") val inputSources: List<InputSourceSpec>,
      override val description: String? = null
  ) : Condition

  @Serializable
  @SerialName("input_source_unless")
  data class InputSourceUnlessCondition(
      @SerialName("input_sources") val inputSources: List<InputSourceSpec>,
      override val description: String? = null
  ) : Condition

  @Serializable
  @SerialName("variable_if")
  data class VariableIfCondition(
      val name: String,
      val value: JsonPrimitive,
      override val description: String? = null
  ) : Condition

  @Serializable
  @SerialName("variable_unless")
  data class VariableUnlessCondition(
      val name: String,
      val value: JsonPrimitive,
      override val description: String? = null
  ) : Condition

  @Serializable
  @SerialName("event_changed_if")
  data class EventChangedIfCondition(val value: Boolean, override val description: String? = null) :
      Condition

  @Serializable
  @SerialName("event_changed_unless")
  data class EventChangedUnlessCondition(
      val value: Boolean,
      override val description: String? = null
  ) : Condition
}

object DEVICE {
  val APPLE_BUILT_IN = Identifiers(isBuiltInKeyboard = true)
  val APPLE = Identifiers(vendorId = 1452L) // Kotlin Long for vendorId/productId
  val KEYCHRON = Identifiers(vendorId = 76L)

  val ANNE_PRO2 = Identifiers(vendorId = 1241L, productId = 41618L)
  val MS_SCULPT = Identifiers(vendorId = 1118L, productId = 1957L)
  val TADA68 = Identifiers(vendorId = 65261L, productId = 4611L)
  val KINESIS = Identifiers(vendorId = 10730L)
  val LOGITECH_G915 = Identifiers(vendorId = 1133L)

  val LOGITECH_DEVICE =
      Identifiers(isKeyboard = true, isPointingDevice = true, productId = 45919L, vendorId = 1133L)

  val POINTING_DEVICE = Identifiers(isPointingDevice = true)

  val LOGITECH_IGNORED = Identifiers(isKeyboard = true, productId = 50475L, vendorId = 1133L)

  val APPLE_ALL = listOf(DEVICE.APPLE, DEVICE.KEYCHRON, DEVICE.APPLE_BUILT_IN)

  val ALL_KEYBOARDS =
      listOf(
          DEVICE.APPLE,
          DEVICE.KEYCHRON,
          DEVICE.APPLE_BUILT_IN,
          DEVICE.ANNE_PRO2,
          DEVICE.MS_SCULPT,
          DEVICE.TADA68,
          DEVICE.KINESIS,
          DEVICE.LOGITECH_G915)

  val ALL_EXCEPT_KINESIS =
      listOf(
          DEVICE.APPLE,
          DEVICE.KEYCHRON,
          DEVICE.APPLE_BUILT_IN,
          DEVICE.ANNE_PRO2,
          DEVICE.MS_SCULPT,
          DEVICE.TADA68,
          DEVICE.LOGITECH_G915)
}

// Root structure for the final JSON (approximated from karabiner.json)
@Serializable
data class KarabinerConfig(
    val global: GlobalSettings? = null, // Made nullable to match some karabiner.json examples
    val profiles: List<Profile>
)

@Serializable
data class GlobalSettings(
    @SerialName("check_for_updates_on_startup") val checkForUpdatesOnStartup: Boolean? = null,
    @SerialName("show_in_menu_bar") val showInMenuBar: Boolean? = null,
    @SerialName("show_profile_name_in_menu_bar") val showProfileNameInMenuBar: Boolean? = null,
    @SerialName("unsafe_ui") val unsafeUi: Boolean? = null
    // Add other global settings as needed
)

@Serializable
data class Profile(
    val name: String,
    @SerialName("complex_modifications") val complexModifications: ComplexModifications,
    @SerialName("fn_function_keys") val fnFunctionKeys: List<FnFunctionKey>? = null,
    @SerialName("simple_modifications") val simpleModifications: List<SimpleModification>? = null,
    val selected: Boolean? = null,
    @SerialName("virtual_hid_keyboard")
    val virtualHidKeyboard: VirtualHidKeyboard = VirtualHidKeyboard(),
    @SerialName("devices") val devices: List<DeviceSpecificSettings>? = null,
    @SerialName("parameters") val parameters: Parameters? = null,
)

@Serializable
data class ComplexModifications(
    val parameters: Parameters? = null, // Re-using Parameters from above
    val rules: List<KarabinerRule>
)

@Serializable
data class VirtualHidKeyboard(
    @SerialName("country_code") val countryCode: Int = 0,
    @SerialName("mouse_key_xy_scale") val mouseKeyXyScale: Double? = null,
    @SerialName("indicate_sticky_modifier_keys_state")
    val indicateStickyModifierKeysState: Boolean? = null,
    @SerialName("keyboard_type_v2") // Original TS used keyboard_type_v2, JSON uses keyboard_type
    val keyboardType: String = "ansi" // e.g., "ansi"
)

@Serializable
data class DeviceSpecificSettings(
    val identifiers: Identifiers,
    @SerialName("disable_built_in_keyboard_if_exists")
    val disableBuiltInKeyboardIfExists: Boolean? = null,
    @SerialName("fn_function_keys") val fnFunctionKeys: List<FnFunctionKey>? = null,
    @SerialName("ignore") val ignore: Boolean? = null,
    @SerialName("manipulate_caps_lock_led") val manipulateCapsLockLed: Boolean? = null,
    @SerialName("simple_modifications") val simpleModifications: List<SimpleModification>? = null,
    @SerialName("treat_as_built_in_keyboard") val treatAsBuiltInKeyboard: Boolean? = null
)

@Serializable data class FromFnKey(@SerialName("key_code") val keyCode: KeyCode)

@Serializable data class FnFunctionKey(val from: FromFnKey, val to: List<To>)

@Serializable
data class SimpleModification(
    val from: SimpleModificationKey,
    val to: List<SimpleModificationValue>
)

@Serializable data class SimpleModificationKey(@SerialName("key_code") val keyCode: KeyCode)

@Serializable data class SimpleModificationValue(@SerialName("key_code") val keyCode: KeyCode)
