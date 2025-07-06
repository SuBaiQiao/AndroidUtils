package com.subaiqiao.androidutils.modules.privacyData.pictureBackup.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.checkbox.MaterialCheckBox
import com.subaiqiao.androidutils.R
import com.subaiqiao.androidutils.modules.privacyData.pictureBackup.entity.MediaItem
import com.subaiqiao.androidutils.modules.privacyData.pictureBackup.entity.UploadStatus

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

    // 用于更新上传状态
    fun updateUploadStatus(position: Int, status: UploadStatus, progress: Int = 0) {
        val currentList = currentList
        if (position !in currentList.indices) return

        val oldItem = currentList[position]
        val newItem = oldItem.copy(uploadStatus = status, uploadProgress = progress)

        // 仅当 item 实际发生变化时才提交新列表
        if (oldItem != newItem) {
            val newList = currentList.toMutableList().apply {
                this[position] = newItem
            }
            submitList(newList)
        } else {
            notifyItemChanged(position)
        }
    }

    inner class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: MaterialCheckBox = itemView.findViewById(R.id.check_box)
        private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        private val fileName: TextView = itemView.findViewById(R.id.file_name)
        private val createTime: TextView = itemView.findViewById(R.id.create_time)

        // 新增进度条和百分比文本
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        private val progressText: TextView = itemView.findViewById(R.id.progress_text)

        @SuppressLint("SetTextI18n")
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
            // 控制进度条和文字显示
            when (item.uploadStatus) {
                UploadStatus.NotStarted -> {
                    progressBar.progress = item.uploadProgress
                    progressText.text = "${item.uploadProgress}%"
                    progressBar.progressTintList = ContextCompat.getColorStateList(itemView.context, R.color.ant_design_default)
                    progressText.setTextColor(ContextCompat.getColorStateList(itemView.context, R.color.ant_design_default))
                }
                UploadStatus.Uploading -> {
                    progressBar.progress = item.uploadProgress
                    progressText.text = "${item.uploadProgress}%"
                    progressBar.progressTintList = ContextCompat.getColorStateList(itemView.context, R.color.ant_design_primary)
                    progressText.setTextColor(ContextCompat.getColorStateList(itemView.context, R.color.ant_design_primary))
                }
                UploadStatus.Completed -> {
                    progressBar.progress = 100
                    progressText.text = "100%"
                    progressBar.progressTintList = ContextCompat.getColorStateList(itemView.context, R.color.ant_design_success)
                    progressText.setTextColor(ContextCompat.getColorStateList(itemView.context, R.color.ant_design_success))
                }
                UploadStatus.Failed -> {
                    progressBar.progress = 0
                    progressText.text = "0%"
                    progressBar.progressTintList = ContextCompat.getColorStateList(itemView.context, R.color.ant_design_danger)
                    progressText.setTextColor(ContextCompat.getColorStateList(itemView.context, R.color.ant_design_danger))
                }
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
