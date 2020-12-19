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
        .setNegativeButton("Cancelar", null)
        .setPositiveButton("Añadir décimo") { _, _ ->
            val number = view.number_edit_text.text.toString()
            val eurosBet = view.euros_bet_edit_text.text.toString()
            if (number.isNotEmpty() &&
                number.toInt() >= 0 &&
                eurosBet.isNotEmpty() &&
                eurosBet.toInt() > 0) {
                val ticket = Ticket(number.padStart(5, '0'), eurosBet.toInt())
                listener.invoke(ticket)
            }
        }
        .setCancelable(false)
        .create()
        .show()
}

fun AppCompatActivity.showErrorDialog(
    retry: () -> Unit
) {
    AlertDialog.Builder(this)
        .setTitle("Ha ocurrido un error")
        .setMessage("¿Qué desea hacer?")
        .setNegativeButton("Cancelar", null)
        .setPositiveButton("Reintentar") { _, _ ->
            retry()
        }
        .setCancelable(false)
        .create()
        .show()
}

fun AppCompatActivity.showTicketInfoDialog(
    ticket: Ticket,
    delete: (Ticket) -> Unit
) {
    AlertDialog.Builder(this)
        .setTitle(ticket.number)
        .setMessage("Estás jugando " + ticket.eurosBet + "€" + " a este número.")
        .setNegativeButton("Eliminar décimo") { _, _ ->
            delete(ticket)
        }
        .setPositiveButton("Cancelar", null)
        .setCancelable(false)
        .create()
        .show()
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}