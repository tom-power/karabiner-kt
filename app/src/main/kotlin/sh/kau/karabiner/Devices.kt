package sh.kau.karabiner

// Using the Identifiers data class from Types.kt
// If DEVICE entries are directly serialized as part of DEVICE_CONFIGS,
// then Identifiers and its fields should be @Serializable and use @SerialName if needed.
// For now, assuming they are primarily for constructing DeviceIfCondition etc.

object DEVICE {
    val APPLE_BUILT_IN = Identifiers(isBuiltInKeyboard = true)
    val APPLE = Identifiers(vendorId = 1452L) // Kotlin Long for vendorId/productId
    val KEYCHRON = Identifiers(vendorId = 76L)

    val ANNE_PRO2 = Identifiers(vendorId = 1241L, productId = 41618L)
    val MS_SCULPT = Identifiers(vendorId = 1118L, productId = 1957L)
    val TADA68 = Identifiers(vendorId = 65261L, productId = 4611L)
    val KINESIS = Identifiers(vendorId = 10730L)
    val LOGITECH_G915 = Identifiers(vendorId = 1133L)

    val LOGITECH_DEVICE = Identifiers(
        isKeyboard = true,
        isPointingDevice = true,
        productId = 45919L,
        vendorId = 1133L
    )
    val POINTING_DEVICE = Identifiers(
        isPointingDevice = true
    )
    val LOGITECH_IGNORED = Identifiers(
        isKeyboard = true,
        productId = 50475L,
        vendorId = 1133L
    )
}

// We'll define DEVICE_COMBO and DEVICE_CONFIGS next.
// For DEVICE_CONFIGS, we'll need a new data class.
