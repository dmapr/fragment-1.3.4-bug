package com.rnd8x8.fragment_134_bug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView

class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
    fun bindPosition(position: Int) {
        val avatarView = itemView.findViewById<ImageView>(R.id.avatar)
        val containerView = itemView.findViewById<FrameLayout>(R.id.text_container)

        avatarView.transitionName ="avatar$position"
        containerView.transitionName = "lorem_ipsum$position"

        itemView.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                avatarView to avatarView.transitionName,
                containerView to containerView.transitionName
            )

            val args = Bundle()
            args.putInt("position", position)
            it.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, args, null, extras)
        }
    }

}

class ItemRecyclerAdapter: RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindPosition(position)
    }

    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    companion object {
        private const val ITEM_COUNT = 20;
    }
}