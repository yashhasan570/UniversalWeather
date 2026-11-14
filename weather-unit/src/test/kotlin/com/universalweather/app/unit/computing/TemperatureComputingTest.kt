/*
 * This file is part of Universal Weather.
 *
 * Universal Weather is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * Universal Weather is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Universal Weather. If not, see <https://www.gnu.org/licenses/>.
 */

package com.universalweather.app.unit.computing

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import com.universalweather.app.unit.temperature.Temperature.Companion.celsius
import org.junit.jupiter.api.Test

/**
 * TODO: To be completed
 */
class TemperatureComputingTest {

    @Test
    fun computeHumidexTest() = runTest {
        computeHumidex(null, null) shouldBe null
        computeHumidex(null, 13.0.celsius) shouldBe null
        computeHumidex(20.0.celsius, null) shouldBe null
        computeHumidex(20.0.celsius, 13.0.celsius)?.inCelsius shouldBe 22.8
        computeHumidex(39.0.celsius, 26.0.celsius)?.inCelsius shouldBe 52.5
    }
}
