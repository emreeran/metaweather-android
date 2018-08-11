package com.emreeran.weather.ui.common

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Emre Eran on 11.08.2018.
 */
class DataBoundViewHolder<out T : ViewDataBinding> constructor(val binding: T)
    : RecyclerView.ViewHolder(binding.root)