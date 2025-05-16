package sh.kau.karabiner

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// region KeyCode Type Serializers
object KeyCodeAsStringSerializer : KSerializer<KeyCode> {
    override val descriptor = PrimitiveSerialDescriptor("KeyCode", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: KeyCode) {
        val kClass = value::class
        val serialName = kClass.annotations.filterIsInstance<SerialName>().firstOrNull()?.value
            ?: kClass.simpleName?.lowercase()
            ?: error("Could not determine serial name for KeyCode")
        encoder.encodeString(serialName)
    }
    override fun deserialize(decoder: Decoder): KeyCode {
        val value = decoder.decodeString()
        return KeyCode.from(value)
    }
}

object ModifierKeyCodeAsStringSerializer : KSerializer<ModifierKeyCode> {
    override val descriptor = PrimitiveSerialDescriptor("ModifierKeyCode", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: ModifierKeyCode) {
        val kClass = value::class
        val serialName = kClass.annotations.filterIsInstance<SerialName>().firstOrNull()?.value
            ?: kClass.simpleName?.lowercase()
            ?: error("Could not determine serial name for ModifierKeyCode")
        encoder.encodeString(serialName)
    }
    override fun deserialize(decoder: Decoder): ModifierKeyCode {
        val value = decoder.decodeString()
        return ModifierKeyCode::class.sealedSubclasses
            .firstNotNullOfOrNull { it.objectInstance }
            ?.takeIf { it::class.simpleName.equals(value, ignoreCase = true) }
            ?: error("Unknown ModifierKeyCode: $value")
    }
}
// endregion

// region Simultaneous Key List
object SimultaneousKeyCodeListSerializer : KSerializer<List<KeyCode>> {
    override val descriptor = ListSerializer(KeyCode.serializer()).descriptor

    override fun serialize(encoder: Encoder, value: List<KeyCode>) {
        val out = encoder.beginCollection(descriptor, value.size)
        for (keyCode in value) {
            out.encodeSerializableElement(descriptor, 0, SimultaneousKeyCodeSerializer, keyCode)
        }
        out.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): List<KeyCode> {
        val inp = decoder.beginStructure(descriptor)
        val list = mutableListOf<KeyCode>()
        while (true) {
            val index = inp.decodeElementIndex(descriptor)
            if (index == CompositeDecoder.Companion.DECODE_DONE) break
            list.add(inp.decodeSerializableElement(descriptor, index, SimultaneousKeyCodeSerializer))
        }
        inp.endStructure(descriptor)
        return list
    }
}

@OptIn(InternalSerializationApi::class)
object SimultaneousKeyCodeSerializer : KSerializer<KeyCode> {
    override val descriptor =
        buildClassSerialDescriptor("SimultaneousKeyCode") {
            element("key_code", PrimitiveSerialDescriptor("key_code", PrimitiveKind.STRING))
        }

    override fun serialize(encoder: Encoder, value: KeyCode) {
        val composite = encoder.beginStructure(descriptor)
        val kClass = value::class
        val serialName =
            kClass.annotations.filterIsInstance<SerialName>().firstOrNull()?.value
                ?: kClass.simpleName?.lowercase()
                ?: error("Could not determine serial name for KeyCode")

        composite.encodeStringElement(descriptor, 0, serialName)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): KeyCode {
        val dec = decoder.beginStructure(descriptor)
        var keyCodeValue: String? = null
        loop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> keyCodeValue = dec.decodeStringElement(descriptor, 0)
                CompositeDecoder.Companion.DECODE_DONE -> break@loop
                else -> throw SerializationException("Unexpected index $index")
            }
        }
        dec.endStructure(descriptor)
        return KeyCode.from(keyCodeValue!!.uppercase())
    }
}
// endregion

// region ModifierKeyCode List Serializer
object ModifierKeyCodeListSerializer : KSerializer<List<ModifierKeyCode?>> {
    private val delegate = ListSerializer(ModifierKeyCodeAsStringSerializer as KSerializer<ModifierKeyCode?>)
    override val descriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: List<ModifierKeyCode?>) = delegate.serialize(encoder, value)
    override fun deserialize(decoder: Decoder): List<ModifierKeyCode?> = delegate.deserialize(decoder)
}
// endregion