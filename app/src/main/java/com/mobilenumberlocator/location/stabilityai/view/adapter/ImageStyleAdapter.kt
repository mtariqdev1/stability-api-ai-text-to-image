package com.mobilenumberlocator.location.stabilityai.view.adapter

import android.content.ComponentCallbacks
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobilenumberlocator.location.stabilityai.databinding.ItemViewImageSizeBinding
import com.mobilenumberlocator.location.stabilityai.model.ImageSizesModel
import com.mobilenumberlocator.location.stabilityai.model.ImageStylesModel
import okhttp3.internal.notify
import javax.inject.Inject

class ImageStyleAdapter @Inject constructor(
    private val context: Context
) :
    RecyclerView.Adapter<ImageStyleAdapter.ViewHolder>() {
    var dataList: ArrayList<ImageStylesModel> = arrayListOf()

    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemViewImageSizeBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    fun updateData(list: ArrayList<ImageStylesModel>) {
        dataList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    fun onSizeChanged(callbacks: (Int) -> Unit) {
        onItemClick = callbacks
    }


    inner class ViewHolder(val binding: ItemViewImageSizeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.tvValue.text = dataList[position].name

            if (dataList[position].isSelected) {
                binding.ivSelected.visibility = View.VISIBLE
            } else {
                binding.ivSelected.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(position)
            }
        }
    }

}