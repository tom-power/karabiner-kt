package sh.kau.karabiner

import sh.kau.karabiner.ModifierKeyCode.LeftShift
import kotlin.test.Test
import kotlin.test.assertEquals

class PointingButtonTest {

    @Test
    fun `can make a complex modification from a pointing button`() {
        assertEquals(
            expected = javaClass.getResource("/pointingButton.json")!!.readText(),
            actual =
                json().encodeToString(
                    ComplexModifications(
                        title = "pointingButton",
                        rules = listOf(
                            karabinerRule {
                                description = "button1 (button2 to button1)"
                                mapping {
                                    fromPointingButton = "button2"
                                    fromModifiers = FromModifiers(mandatory = listOf(LeftShift))
                                    pointingButton = "button1"
                                    toModifiers = listOf(ModifierKeyCode.LeftCommand)
                                }
                            }
                        ),
                    )
                ),
        )
    }
}

