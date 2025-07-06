package com.subaiqiao.androidutils.modules.privacyData.pictureBackup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.subaiqiao.androidutils.R
import com.subaiqiao.androidutils.modules.privacyData.pictureBackup.entity.PictureItem

class PictureItemAdapter(private val onSelectionChanged: (Int) -> Unit) :
    ListAdapter<PictureItem, PictureItemAdapter.PictureViewHolder>(DiffCallback()) {

    private val selectedItems = mutableSetOf<Int>()

    inner class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.check_box)
        private val fileName: TextView = itemView.findViewById(R.id.file_name)
        private val createTime: TextView = itemView.findViewById(R.id.create_time)
        private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)

        fun bind(item: PictureItem) {
            fileName.text = item.name
            createTime.text = item.createTime
            thumbnail.setImageResource(item.thumbnailResId)

            checkBox.isChecked = selectedItems.contains(item.id)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedItems.add(item.id)
                } else {
                    selectedItems.remove(item.id)
                }
                onSelectionChanged(selectedItems.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_picture, parent, false)
        return PictureViewHolder(view)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<PictureItem>() {
        override fun areItemsTheSame(oldItem: PictureItem, newItem: PictureItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PictureItem, newItem: PictureItem): Boolean {
            return oldItem == newItem
        }
    }
}