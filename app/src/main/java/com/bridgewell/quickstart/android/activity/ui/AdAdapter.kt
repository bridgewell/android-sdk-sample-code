package com.bridgewell.quickstart.android.activity.ui

import com.bridgewell.quickstart.android.data.AdType
import com.bridgewell.quickstart.android.databinding.ListItemAdTypeBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdAdapter(
    private val onItemClicked: (AdType) -> Unit
) : RecyclerView.Adapter<AdAdapter.AdTypeViewHolder>() {

    private val list: List<AdType> = listOf(
        AdType.BANNER,
        AdType.STICKY_BOTTOM_RIGHT,
        AdType.WEB_VIEW
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdTypeViewHolder {
        val binding = ListItemAdTypeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val viewHolder = AdTypeViewHolder(binding)
        viewHolder.setOnItemClickedListener(onItemClicked)
        return viewHolder
    }

    override fun onBindViewHolder(holder: AdTypeViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    class AdTypeViewHolder(
        private val binding: ListItemAdTypeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var adType: AdType? = null

        fun bind(adType: AdType) {
            this.adType = adType
            binding.tvName.text = itemView.context.getString(adType.titleStringRes)
        }

        fun setOnItemClickedListener(onItemClicked: (AdType) -> Unit) {
            binding.root.setOnClickListener {
                onItemClicked(adType ?: return@setOnClickListener)
            }
        }
    }
}