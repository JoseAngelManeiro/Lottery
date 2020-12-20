package com.joseangelmaneiro.lottery.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.joseangelmaneiro.lottery.R
import com.joseangelmaneiro.lottery.model.NumberItem
import java.text.NumberFormat
import java.util.*

class NumbersAdapter(
  private val items: List<NumberItem>,
  private val onItemClickListener: (NumberItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private val DRAW_NOT_STARTED_LABEL = "El sorteo aún no ha empezado"
  private val DRAW_IN_PROGRESS_LABEL = "Sorteo en curso"
  private val NO_PRIZE_LIBEL = "Sin premio"
  private val PRIZE_SUFIX = " de premio"

  private val numberFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY).apply {
    maximumFractionDigits = 0
  }

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
      eurosBetTextView.text = numberFormat.format(numberItem.eurosBet)
      when (numberItem.status) {
        0 -> {
          prizeTextView.setPrize(DRAW_NOT_STARTED_LABEL, R.color.blue_grey)
        }
        1 -> {
          if (numberItem.prize == 0) {
            prizeTextView.setPrize(DRAW_IN_PROGRESS_LABEL, R.color.blue_grey)
          } else {
            prizeTextView.setPrize(numberFormat.format(numberItem.prize).plus(PRIZE_SUFIX),
              R.color.green)
          }
        }
        else -> {
          if (numberItem.prize == 0) {
            prizeTextView.setPrize(NO_PRIZE_LIBEL, R.color.blue_grey)
          } else {
            prizeTextView.setPrize(numberFormat.format(numberItem.prize).plus(PRIZE_SUFIX),
              R.color.green)
          }
        }
      }
      if (adapterPosition == items.lastIndex) {
        paddingBottomView.visibility = View.VISIBLE
      } else {
        paddingBottomView.visibility = View.GONE
      }
    }
  }

  private fun TextView.setPrize(text: String, colorResource: Int) {
    this.text = text
    this.setTextColor(ContextCompat.getColor(this.context, colorResource))
  }
}
