package com.example.mobileapp.exercises.words

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R

class WordRvAdapter(private val itemList: List<WordItem>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<WordRvAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, clickListener: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.btnWord)
        init {
            button.setOnClickListener {
                clickListener(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.button.text = currentItem.word
        holder.button.isSelected = currentItem.isSelected
        val context = holder.button.context
        holder.button.setTextColor(
            if (currentItem.isSelected) context.getColor(R.color.white)
            else context.getColor(R.color.dark_text)
        )
        if (currentItem.isWrong) {
            holder.button.background = context.getDrawable(R.drawable.rounded_word_wrong_background)
        }
        else if (currentItem.isCorrect) {
            holder.button.background = context.getDrawable(R.drawable.rounded_word_correct_background)
        } else {
            holder.button.background = context.getDrawable(R.drawable.rounded_word_background)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}