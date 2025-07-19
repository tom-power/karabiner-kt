package sh.kau.karabiner

import kotlin.test.Test
import kotlin.test.assertEquals

class ProfileTest {
    @Test
    fun `profile is correct`() {
        assertEquals(
            expected = javaClass.getResource("/profile.json")!!.readText().trimAll(),
            actual = json().encodeToString(profile()).trimAll(),
        )
    }
}

private fun String.trimAll() = trimStart().trimEnd().trimIndent()

private fun profile() =
        Profile(
            name = "test",
            complexModifications = ComplexModifications(rules = listOf()),
            simpleModifications = listOf(
                SimpleModification(
                    from =
                        SimpleModificationKey(
                            keyCode = KeyCode.CapsLock,
                        ),
                    to =
                        listOf(
                            SimpleModificationValue(
                                keyCode = ModifierKeyCode.LeftCommand,
                            )
                        )
                )
            )
        )