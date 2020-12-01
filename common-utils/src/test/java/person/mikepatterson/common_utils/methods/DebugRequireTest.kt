package person.mikepatterson.common_utils.methods

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

// TODO: update this
class DebugRequireTest {

    @Before
    fun setup() {
        // name found by opening kotlin bytecode
        mockkStatic("person.mikepatterson.common_utils.methods.IsDebugBuildKt")
    }

    @After
    fun teardown() {
        unmockkStatic("person.mikepatterson.common_utils.methods.IsDebugBuildKt")
    }

    @Test
    fun `confirm debugRequires with a false parameter throws an IllegalArgumentException if isDebugBuild is true`() {
        every { isDebugBuild() } returns true

        try {
            debugRequire(false)
            Assert.fail("No error was thrown by debugRequire but an error was expected.")
        } catch (e: IllegalArgumentException) {
            assert(true) { "An error was thrown by debugRequire as expected." }
        }
    }

    @Test
    fun `confirm debugRequires with a false parameter does NOT throw an IllegalArgumentException if isDebugBuild is false`() {
        every { isDebugBuild() } returns false

        try {
            debugRequire(false)
            assert(true) { "No error was thrown by debugRequire and none was expected." }
        } catch (e: Exception) {
            Assert.fail("An error was thrown by debugRequire but no error was expected")
        }
    }

    @Test
    fun `confirm debugRequires with a true parameter does NOT throw an IllegalArgumentException if isDebugBuild is true`() {
        every { isDebugBuild() } returns true

        try {
            debugRequire(true)
            assert(true) { "No error was thrown by debugRequire and none was expected." }
        } catch (e: Exception) {
            Assert.fail("An error was thrown by debugRequire but no error was expected")
        }
    }

    @Test
    fun `confirm debugRequires with a true parameter does NOT throw an IllegalArgumentException if isDebugBuild is false`() {
        every { isDebugBuild() } returns false

        try {
            debugRequire(true)
            assert(true) { "No error was thrown by debugRequire and none was expected." }
        } catch (e: Exception) {
            Assert.fail("An error was thrown by debugRequire but no error was expected")
        }
    }

    // TODO: verify the message is used correctly
}
