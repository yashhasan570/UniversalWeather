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

package com.universalweather.app.ui.settings.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import dagger.hilt.android.AndroidEntryPoint
import com.universalweather.app.UniversalWeather
import com.universalweather.app.BuildConfig
import com.universalweather.app.R
import com.universalweather.app.common.activities.UniversalActivity
import com.universalweather.app.common.extensions.currentLocale
import com.universalweather.app.common.extensions.plus
import com.universalweather.app.common.source.RemovedSource
import com.universalweather.app.sources.SourceManager
import com.universalweather.app.ui.common.widgets.Material3Scaffold
import com.universalweather.app.ui.common.widgets.generateCollapsedScrollBehavior
import com.universalweather.app.ui.common.widgets.insets.FitStatusBarTopAppBar
import com.universalweather.app.ui.settings.preference.SmallSeparatorItem
import com.universalweather.app.ui.settings.preference.bottomInsetItem
import com.universalweather.app.ui.settings.preference.clickablePreferenceItem
import com.universalweather.app.ui.settings.preference.composables.PreferenceScreen
import com.universalweather.app.ui.settings.preference.composables.PreferenceViewWithCard
import com.universalweather.app.ui.theme.compose.UniversalWeatherTheme
import java.text.Collator
import javax.inject.Inject

@AndroidEntryPoint
class PrivacyPolicyActivity : UniversalActivity() {

    @Inject
    lateinit var sourceManager: SourceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UniversalWeatherTheme {
                ContentView()
            }
        }
    }

    @Composable
    private fun ContentView() {
        val context = LocalContext.current
        val scrollBehavior = generateCollapsedScrollBehavior()
        val uriHandler = LocalUriHandler.current
        val sources = remember {
            sourceManager.getHttpSources()
                .filter { it !is RemovedSource && it.privacyPolicyUrl.startsWith("http") }
                .sortedWith { s1, s2 ->
                    // Sort by name because there are now a lot of sources
                    Collator.getInstance(
                        this@PrivacyPolicyActivity.currentLocale
                    ).compare(s1.name, s2.name)
                }
        }

        Material3Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                FitStatusBarTopAppBar(
                    title = stringResource(R.string.about_privacy_policy),
                    onBackPressed = { finish() },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { paddingValues ->
            PreferenceScreen(
                paddingValues = paddingValues.plus(PaddingValues(horizontal = dimensionResource(R.dimen.normal_margin)))
            ) {
                if (!context.getString(R.string.brand_name).contains("breezy", ignoreCase = true) ||
                    UniversalWeather.instance.isSignedByUniversal ||
                    UniversalWeather.instance.debugMode
                ) {
                    clickablePreferenceItem(R.string.brand_name) { id ->
                        val url = BuildConfig.PRIVACY_POLICY_LINK
                        PreferenceViewWithCard(
                            title = stringResource(id),
                            summary = url,
                            isFirst = true
                        ) {
                            if (url.startsWith("https://") &&
                                (
                                    !url.contains("breezy", ignoreCase = true) ||
                                        UniversalWeather.instance.isSignedByUniversal ||
                                        UniversalWeather.instance.debugMode
                                    )
                            ) {
                                uriHandler.openUri(url)
                            }
                        }
                    }
                }

                itemsIndexed(sources) { index, preferenceSource ->
                    SmallSeparatorItem()
                    PreferenceViewWithCard(
                        title = preferenceSource.name,
                        summary = preferenceSource.privacyPolicyUrl,
                        isLast = index == sources.lastIndex
                    ) {
                        uriHandler.openUri(preferenceSource.privacyPolicyUrl)
                    }
                }

                bottomInsetItem()
            }
        }
    }
}
