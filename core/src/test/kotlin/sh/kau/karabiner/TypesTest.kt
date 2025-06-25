package sh.kau.karabiner

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TypesTest {

    @ParameterizedTest
    @MethodSource("keyCodeArgs")
    fun `can get a KeyCode from a string`(keyCode: KeyCode, name: String) {
        assertEquals(
            expected = keyCode,
            actual = KeyCode.from(name),
        )
    }

    private fun keyCodeArgs() =
        KeyCode::class.sealedSubclasses.filter { it.objectInstance != null }
            .map {
                Arguments.of(it.objectInstance, it.simpleName!!.lowercase())
            }
}