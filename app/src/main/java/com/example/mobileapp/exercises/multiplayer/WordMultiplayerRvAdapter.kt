package com.example.mobileapp.exercises.multiplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R

class WordMultiplayerRvAdapter(private val itemList: List<WordMultiplayerItem>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<WordMultiplayerRvAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, clickListener: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.btnWord)
        val resultTextView: TextView = itemView.findViewById(R.id.tvResultText)

        init {
            button.setOnClickListener {
                clickListener(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word_multiplayer, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.resultTextView.text = ""
        holder.button.text = currentItem.word
        holder.button.isSelected = currentItem.isSelected
        val context = holder.button.context
        holder.button.setTextColor(
            if (currentItem.isSelected) context.getColor(R.color.white)
            else context.getColor(R.color.dark_text)
        )
        holder.resultTextView.setTextColor(
            if (currentItem.isWrong || currentItem.isCorrect) context.getColor(R.color.white)
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
        val userLabel = context.getString(R.string.multiplayer_player_label)
        val opponentLabel = context.getString(R.string.multiplayer_opponent_label)
        if (currentItem.isSelected && currentItem.isOpponentAnswer) {
            holder.resultTextView.text = context.getString(
                R.string.multiplayer_both_label,
                userLabel,
                opponentLabel
            )
        } else if (currentItem.isSelected) {
            holder.resultTextView.text = userLabel
        } else if (currentItem.isOpponentAnswer) {
            holder.resultTextView.text = opponentLabel
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}