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


//region Simultaneous Key List
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
    val serialName =
        value::class.java.getField(value.name).getAnnotation(SerialName::class.java)?.value
            ?: value.name.lowercase()
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
    return KeyCode.valueOf(keyCodeValue!!.uppercase())
  }
}
//endregion
