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

package com.universalweather.app.ui.main.adapters.location

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.universalweather.app.domain.location.model.Location
import com.universalweather.app.databinding.ItemLocationCardBinding
import com.universalweather.app.sources.SourceManager
import com.universalweather.app.ui.common.adapters.SyncListAdapter
import com.universalweather.app.ui.theme.resource.ResourcesProviderFactory
import com.universalweather.app.ui.theme.resource.providers.ResourceProvider

/**
 * Location adapter.
 */
class LocationAdapter(
    private val mContext: Context,
    locationList: List<Location>,
    selectedId: String?,
    private val sourceManager: SourceManager,
    private val mClickListener: (String) -> Unit,
    private val mDragListener: (LocationHolder) -> Unit,
) : SyncListAdapter<LocationModel, LocationHolder>(
    ArrayList(),
    object : DiffUtil.ItemCallback<LocationModel>() {
        override fun areItemsTheSame(oldItem: LocationModel, newItem: LocationModel): Boolean {
            return oldItem.areItemsTheSame(newItem)
        }

        override fun areContentsTheSame(oldItem: LocationModel, newItem: LocationModel): Boolean {
            return oldItem.areContentsTheSame(newItem)
        }
    }
) {
    private val mResourceProvider: ResourceProvider = ResourcesProviderFactory.newInstance

    init {
        update(locationList, selectedId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
        return LocationHolder(
            ItemLocationCardBinding.inflate(LayoutInflater.from(parent.context)),
            mClickListener,
            mDragListener
        )
    }

    override fun onBindViewHolder(holder: LocationHolder, position: Int) {
        holder.onBindView(mContext, getItem(position), mResourceProvider)
    }

    override fun onBindViewHolder(holder: LocationHolder, position: Int, payloads: List<Any>) {
        holder.onBindView(mContext, getItem(position), mResourceProvider)
    }

    fun update(selectedId: String?) {
        val modelList: MutableList<LocationModel> = ArrayList(itemCount)
        for (model in currentList) {
            modelList.add(
                LocationModel(
                    mContext,
                    model.location,
                    model.location.formattedId == selectedId
                )
            )
        }
        submitList(modelList)
    }

    fun update(newList: List<Location>, selectedId: String?) {
        val modelList: MutableList<LocationModel> = ArrayList(newList.size)
        for (l in newList) {
            modelList.add(
                LocationModel(
                    mContext,
                    l,
                    l.formattedId == selectedId
                )
            )
        }
        submitList(modelList)
    }

    fun update(from: Int, to: Int) {
        submitMove(from, to)
    }
}
