package com.subaiqiao.androidutils.modules.privacyData.pictureBackup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.checkbox.MaterialCheckBox
import com.subaiqiao.androidutils.R
import com.subaiqiao.androidutils.modules.privacyData.pictureBackup.entity.MediaItem

class PictureItemAdapter(
    val onSelectedCountChanged: (Int) -> Unit,
    val onItemCheckChanged: (Int, Boolean) -> Unit
) : ListAdapter<MediaItem, PictureItemAdapter.PictureViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MediaItem>() {
            override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_picture, parent, false)
        return PictureViewHolder(view)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: MaterialCheckBox = itemView.findViewById(R.id.check_box)
        private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        private val fileName: TextView = itemView.findViewById(R.id.file_name)
        private val createTime: TextView = itemView.findViewById(R.id.create_time)

        fun bind(item: MediaItem) {
            fileName.text = item.displayName
            createTime.text = item.date
            checkBox.isChecked = item.selected
            // 图片加载逻辑（如 Glide）
             Glide.with(itemView.context).load(item.uri).into(thumbnail)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                item.selected = isChecked
                onItemCheckChanged(absoluteAdapterPosition, isChecked)
                onSelectedCountChanged(currentList.count { it.selected })
            }
        }
    }

    // 全选/取消全选方法
    fun toggleSelectAll() {
        val allSelected = !isAllSelected()
        val newList = currentList.toMutableList().map { item -> item.copy(selected = allSelected)  }
        submitList(newList)
        onSelectedCountChanged(if (allSelected) newList.size else 0)
    }

    // 判断是否全部选中
    fun isAllSelected(): Boolean {
        return currentList.all { it.selected }
    }
}
