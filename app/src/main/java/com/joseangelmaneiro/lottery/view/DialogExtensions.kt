package com.joseangelmaneiro.lottery.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.joseangelmaneiro.lottery.R
import com.joseangelmaneiro.lottery.model.Ticket
import kotlinx.android.synthetic.main.dialog_add_ticket.view.*

fun AppCompatActivity.showAddTicketDialog(
    listener: (Ticket) -> Unit
) {
    val view = this.layoutInflater.inflate(R.layout.dialog_add_ticket, null)
    AlertDialog.Builder(this)
        .setTitle("Añade un décimo de lotería")
        .setView(view)
        .setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton("Añadir décimo") { dialog, _ ->
            val number = view.number_edit_text.text.toString()
            val eurosBet = view.euros_bet_edit_text.text.toString()
            if (number.isNotEmpty() &&
                number.toInt() >= 0 &&
                eurosBet.isNotEmpty() &&
                eurosBet.toInt() > 0) {
                val ticket = Ticket(number, eurosBet.toInt())
                dialog.dismiss()
                listener.invoke(ticket)
            }
        }
        .setCancelable(false)
        .create()
        .show()
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}