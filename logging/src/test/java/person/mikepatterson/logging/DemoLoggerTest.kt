package person.mikepatterson.logging

import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import timber.log.Timber

internal class DemoLoggerTest {

    @Before
    fun setup() {
        mockkStatic(Timber::class)
    }

    @After
    fun teardown() {
        unmockkStatic(Timber::class)
    }

    @Test
    fun `verify that init calls Timber_plant`() {
        DemoLogger.init()

        verify(exactly = 1) { Timber.plant(any()) }
    }

    @Test
    fun `verify that d passes through to Timber_d`() {
        // this informs Mockk to avoid actually running the static method of Timber
        every { Timber.d("debug_message") } just Runs

        DemoLogger.d("debug_message")

        verify(exactly = 1) { Timber.d("debug_message") }
    }

    @Test
    fun `verify that w passes through to Timber_w`() {
        every { Timber.w("warning_message") } just Runs

        DemoLogger.w("warning_message")

        verify(exactly = 1) { Timber.w("warning_message") }
    }

    @Test
    fun `verify that e passes through to Timber_e`() {
        every { Timber.e("error_message") } just Runs

        DemoLogger.e("error_message")

        verify(exactly = 1) { Timber.e("error_message") }
    }

    @Test
    fun `verify that e with a throwable passes through to Timber_e`() {
        val throwable = mockk<Throwable>()
        every { Timber.e(throwable, "error_message") } just Runs

        DemoLogger.e("error_message", throwable)

        verify(exactly = 1) { Timber.e(throwable, "error_message") }
    }
}
