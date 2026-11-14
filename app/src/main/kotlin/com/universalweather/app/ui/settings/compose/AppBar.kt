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

package com.universalweather.app.ui.settings.compose

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.universalweather.app.R
import com.universalweather.app.common.utils.helpers.IntentHelper

@Composable
fun AboutActivityIconButton(
    context: Context,
    modifier: Modifier = Modifier,
) = IconButton(
    modifier = modifier,
    onClick = { IntentHelper.startAboutActivity(context) }
) {
    Icon(
        imageVector = Icons.Outlined.Info,
        contentDescription = stringResource(R.string.action_about),
        tint = MaterialTheme.colorScheme.onSurface
    )
}
