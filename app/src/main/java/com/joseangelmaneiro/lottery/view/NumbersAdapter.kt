package com.joseangelmaneiro.lottery.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.joseangelmaneiro.lottery.LotteryType
import com.joseangelmaneiro.lottery.R
import com.joseangelmaneiro.lottery.model.NumberItem
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class NumbersAdapter(
  private val items: List<NumberItem>,
  private val lotteryType: LotteryType,
  private val onItemClickListener: (NumberItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private val DRAW_NOT_STARTED_LABEL = "Sorteo no iniciado"
  private val DRAW_IN_PROGRESS_LABEL = "Sorteo en curso"
  private val NO_PRIZE_LABEL = "Sin premio"
  private val PRIZE_SUFIX = " de premio"
  private val NO_INFO_LABEL = "Sin información"

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
    private val timestampTextView = itemView.findViewById(R.id.timestamp_text_view) as TextView

    init {
      itemView.setOnClickListener {
        onItemClickListener(items[adapterPosition])
      }
    }

    fun bind(numberItem: NumberItem) {
      val syncDate = getDate(numberItem.timestamp)
      val isValidSyncDate = isValidSyncDate(syncDate, lotteryType)
      numberTextView.text = numberItem.number
      eurosBetTextView.text = numberFormat.format(numberItem.eurosBet)
      when {
        numberItem.status == -1 -> {
          prizeTextView.setPrize(NO_INFO_LABEL, R.color.blue_grey)
        }
        numberItem.status == 0 || !isValidSyncDate -> {
          prizeTextView.setPrize(DRAW_NOT_STARTED_LABEL, R.color.blue_grey)
        }
        numberItem.status == 1 -> {
          if (numberItem.prize == 0) {
            prizeTextView.setPrize(DRAW_IN_PROGRESS_LABEL, R.color.blue_grey)
          } else {
            prizeTextView.setPrize(numberFormat.format(numberItem.prize).plus(PRIZE_SUFIX),
              R.color.green)
          }
        }
        else -> {
          if (numberItem.prize == 0) {
            prizeTextView.setPrize(NO_PRIZE_LABEL, R.color.blue_grey)
          } else {
            prizeTextView.setPrize(numberFormat.format(numberItem.prize).plus(PRIZE_SUFIX),
              R.color.green)
          }
        }
      }
      timestampTextView.text = if (isValidSyncDate) "Sorteo $syncDate" else ""
      if (adapterPosition == items.lastIndex) {
        paddingBottomView.visibility = View.VISIBLE
      } else {
        paddingBottomView.visibility = View.GONE
      }
    }
  }

  private fun getDate(timestamp: Int): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp * 1000L
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    return simpleDateFormat.format(calendar.time)
  }

  private fun isValidSyncDate(syncDate: String, lotteryType: LotteryType): Boolean {
    return when (lotteryType) {
      LotteryType.NAVIDAD -> syncDate.startsWith("22/12")
      LotteryType.EL_NINO -> syncDate.startsWith("06/01")
    }
  }

  private fun TextView.setPrize(text: String, colorResource: Int) {
    this.text = text
    this.setTextColor(ContextCompat.getColor(this.context, colorResource))
  }
}
