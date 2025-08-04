package sh.kau.karabiner

import sh.kau.karabiner.ModifierKeyCode.LeftShift
import sh.kau.karabiner.ModifierKeyCode.RightShift
import kotlin.test.Test
import kotlin.test.assertEquals

class KarabinerRuleTest {

    @Test
    fun `can configure multiple To`() {
        assertEquals(
            expected = javaClass.getResource("/multipleTo.json")!!.readText(),
            actual =
                json().encodeToString(
                    ComplexModifications(
                        title = "multipleTo",
                        rules = listOf(
                            karabinerRule {
                                description = "AB (right shift a)"
                                mapping {
                                    from = From(KeyCode.A, FromModifiers(mandatory = listOf(RightShift)))
                                    to =
                                        listOf(
                                            To(KeyCode.A, listOf(LeftShift)),
                                            To(KeyCode.B, listOf(LeftShift))
                                        )
                                }
                            }
                        ),
                    )
                ),
        )
    }
}

