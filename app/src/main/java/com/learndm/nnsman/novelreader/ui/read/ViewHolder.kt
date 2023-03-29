package com.learndm.nnsman.novelreader.ui.read

import android.view.View
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.learndm.nnsman.databinding.ItemReadSettingBgBinding
import com.learndm.nnsman.novelreader.ui.flyer.ReadTheme
import com.learndm.nnsman.utils.getColor

class ViewHolder(private val binding: ItemReadSettingBgBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(isSelected: Boolean,theme: ReadTheme) {
        binding.viewRing.background.setTint(binding.root.context.getColor(theme.toolBarContentColor,null))
        binding.viewBg.background.setTint(binding.root.context.getColor(theme.toolBarBgColor,null))
        binding.viewRing.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
    }

    fun setClickListener(listener: View.OnClickListener) {
        binding.root.setOnClickListener(listener)
    }
}