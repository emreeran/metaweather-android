package com.emreeran.weather.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.emreeran.weather.AppExecutors
import com.emreeran.weather.R
import com.emreeran.weather.databinding.LocationItemBinding
import com.emreeran.weather.db.entity.Location
import com.emreeran.weather.ui.common.DataBoundListAdapter

/**
 * Created by Emre Eran on 11.08.2018.
 */
class LocationAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val callback: ((Location) -> Unit)?
) : DataBoundListAdapter<Location, LocationItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem.woeId == newItem.woeId
            }

            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem.woeId == newItem.woeId
                        && oldItem.createdAt == newItem.createdAt
            }
        }
) {
    override fun createBinding(parent: ViewGroup): LocationItemBinding {
        val binding = DataBindingUtil
                .inflate<LocationItemBinding>(
                        LayoutInflater.from(parent.context),
                        R.layout.location_item,
                        parent,
                        false,
                        dataBindingComponent
                )
        binding.root.setOnClickListener { _ ->
            binding.location?.let {
                callback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: LocationItemBinding, item: Location) {
        binding.location = item
    }
}