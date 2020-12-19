package com.joseangelmaneiro.lottery.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.joseangelmaneiro.lottery.R
import com.joseangelmaneiro.lottery.model.NumberItem

class NumbersAdapter(
  private val items: List<NumberItem>,
  private val onItemClickListener: (NumberItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return ItemViewHolder(parent.inflate(R.layout.item_number))
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as ItemViewHolder).bind(items[position])
  }

  inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val numberTextView = itemView.findViewById(R.id.number_text_view) as TextView
    private val eurosBetTextView = itemView.findViewById(R.id.euros_bet_text_view) as TextView
    private val prizeTextView = itemView.findViewById(R.id.prize_text_view) as TextView
    private val paddingBottomView = itemView.findViewById(R.id.padding_bottom_view) as View

    init {
      itemView.setOnClickListener {
        onItemClickListener(items[adapterPosition])
      }
    }

    fun bind(numberItem: NumberItem) {
      numberTextView.text = numberItem.number
      eurosBetTextView.text = numberItem.eurosBet.toString().plus("€")
      if (numberItem.prize == 0) {
        prizeTextView.text = "Sin premio"
        prizeTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey))
      } else {
        prizeTextView.text = numberItem.prize.toString().plus("€ de premio")
        prizeTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
      }
      if (adapterPosition == items.lastIndex) {
        paddingBottomView.visibility = View.VISIBLE
      } else {
        paddingBottomView.visibility = View.GONE
      }
    }
  }
}
