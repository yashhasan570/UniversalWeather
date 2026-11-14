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

package universal.buildlogic

import org.gradle.api.Project

interface BuildConfig {
    // You’re NOT allowed to redistribute modified APKs with the brand config enabled, see license terms
    val isUniversal: Boolean
}

val Project.Config: BuildConfig get() = object : BuildConfig {
    override val isUniversal: Boolean = project.hasProperty("universal")
}
