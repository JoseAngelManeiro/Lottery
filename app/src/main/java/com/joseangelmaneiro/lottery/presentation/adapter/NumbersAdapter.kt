package com.joseangelmaneiro.lottery.presentation.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joseangelmaneiro.lottery.R
import com.joseangelmaneiro.lottery.model.NumberItem
import com.joseangelmaneiro.lottery.model.PrizeStatus
import com.joseangelmaneiro.lottery.presentation.inflate
import java.text.NumberFormat
import java.util.Locale

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
    private val numberTextView: TextView = itemView.findViewById(R.id.number_text_view)
    private val prizeTextView: TextView = itemView.findViewById(R.id.prize_text_view)
    private val paddingBottomView: View = itemView.findViewById(R.id.padding_bottom_view)

    init {
      itemView.setOnClickListener {
        onItemClickListener(items[adapterPosition])
      }
    }

    fun bind(numberItem: NumberItem) {
      numberTextView.text = numberItem.number

      prizeTextView.text = when (numberItem.prize.status) {
          PrizeStatus.NoInfo -> ""
          PrizeStatus.NonWinning -> "Sin premio"
          PrizeStatus.Winning -> numberItem.prize.value.asPrize() + "€ de premio"
      }

      if (adapterPosition == items.lastIndex) {
        paddingBottomView.visibility = View.VISIBLE
      } else {
        paddingBottomView.visibility = View.GONE
      }
    }

    private fun Int.asPrize(): String {
      val formatter = NumberFormat.getIntegerInstance(Locale("es", "ES"))
      return formatter.format(this)
    }
  }
}
