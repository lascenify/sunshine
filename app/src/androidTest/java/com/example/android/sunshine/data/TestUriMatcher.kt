package com.example.android.sunshine.data
import android.content.UriMatcher
import com.example.android.sunshine.framework.provider.WeatherContract
import com.example.android.sunshine.framework.provider.WeatherContract.WeatherEntry.buildWeatherUriWithDate
import com.example.android.sunshine.framework.provider.WeatherProvider
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import java.lang.reflect.InvocationTargetException

class TestUriMatcher {
    private var testMatcher: UriMatcher? = null
    @Before
    fun before() {
        try {
            val buildUriMatcher =
                WeatherProvider::class.java.getDeclaredMethod("buildUriMatcher")
            testMatcher = buildUriMatcher.invoke(WeatherProvider::class.java) as UriMatcher
            REFLECTED_WEATHER_CODE = getStaticIntegerField(
                WeatherProvider::class.java,
                weatherCodeVariableName
            )!!
            REFLECTED_WEATHER_WITH_DATE_CODE = getStaticIntegerField(
                WeatherProvider::class.java,
                weatherCodeWithDateVariableName
            )!!
        } catch (e: NoSuchFieldException) {
            Assert.fail(studentReadableNoSuchField(e))
        } catch (e: IllegalAccessException) {
            Assert.fail(e.message)
        } catch (e: NoSuchMethodException) {
            val noBuildUriMatcherMethodFound =
                "It doesn't appear that you have created a method called buildUriMatcher in " +
                        "the WeatherProvider class."
            Assert.fail(noBuildUriMatcherMethodFound)
        } catch (e: InvocationTargetException) {
            Assert.fail(e.message)
        }
    }

    /**
     * Students: This function tests that your UriMatcher returns the correct integer value for
     * each of the Uri types that our ContentProvider can handle. Uncomment this when you are
     * ready to test your UriMatcher.
     */
    @Test
    fun testUriMatcher() { /* Test that the code returned from our matcher matches the expected weather code */
        val weatherUriDoesNotMatch =
            "Error: The CODE_WEATHER URI was matched incorrectly."
        val actualWeatherCode = testMatcher!!.match(TEST_WEATHER_DIR)
        val expectedWeatherCode = REFLECTED_WEATHER_CODE
        Assert.assertEquals(
            weatherUriDoesNotMatch,
            expectedWeatherCode,
            actualWeatherCode
        )
        /*
         * Test that the code returned from our matcher matches the expected weather with date code
         */
        val weatherWithDateUriCodeDoesNotMatch =
            "Error: The CODE_WEATHER WITH DATE URI was matched incorrectly."
        val actualWeatherWithDateCode =
            testMatcher!!.match(TEST_WEATHER_WITH_DATE_DIR)
        val expectedWeatherWithDateCode =
            REFLECTED_WEATHER_WITH_DATE_CODE
        Assert.assertEquals(
            weatherWithDateUriCodeDoesNotMatch,
            expectedWeatherWithDateCode,
            actualWeatherWithDateCode
        )
    }

    companion object {
        private val TEST_WEATHER_DIR = WeatherContract.WeatherEntry.CONTENT_URI
        private val TEST_WEATHER_WITH_DATE_DIR = buildWeatherUriWithDate(DATE_NORMALIZED)
        private const val weatherCodeVariableName = "CODE_WEATHER"
        private var REFLECTED_WEATHER_CODE = 0
        private const val weatherCodeWithDateVariableName = "CODE_WEATHER_WITH_DATE"
        private var REFLECTED_WEATHER_WITH_DATE_CODE = 0
    }
}