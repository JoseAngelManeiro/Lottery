package com.joseangelmaneiro.lottery.presentation

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import com.joseangelmaneiro.lottery.LotteryType
import com.joseangelmaneiro.lottery.R
import com.joseangelmaneiro.lottery.model.Ticket

fun Activity.showAddTicketDialog(
    listener: (Ticket) -> Unit
) {
    val view = this.layoutInflater.inflate(R.layout.dialog_add_ticket, null)
    val numberEditText = view.findViewById<EditText>(R.id.number_edit_text)
    AlertDialog.Builder(this)
        .setTitle("Añade un décimo de lotería")
        .setView(view)
        .setNegativeButton("Cancelar", null)
        .setPositiveButton("Añadir décimo") { _, _ ->
            val number = numberEditText.text.toString()
            if (number.isNotEmpty() && number.toInt() >= 0) {
                val ticket = Ticket(number.padStart(5, '0'))
                listener.invoke(ticket)
            }
        }
        .setCancelable(false)
        .create()
        .show()
}

fun Activity.showTicketInfoDialog(
    ticket: Ticket,
    delete: (Ticket) -> Unit
) {
    AlertDialog.Builder(this)
        .setTitle(ticket.number)
        .setMessage("Eliminar este décimo de lotería")
        .setNegativeButton("Eliminar") { _, _ ->
            delete(ticket)
        }
        .setPositiveButton("Cancelar", null)
        .setCancelable(false)
        .create()
        .show()
}

fun Activity.showDeleteAllTicketsDialog(
    lotteryType: LotteryType,
    deleteAll: () -> Unit
) {
    AlertDialog.Builder(this)
        .setTitle("Sorteo de " + if (lotteryType == LotteryType.NAVIDAD) "Navidad" else "El Niño")
        .setMessage("¿Quieres eliminar todos los números?")
        .setNegativeButton("Cancelar", null)
        .setPositiveButton("Aceptar") { _, _ ->
            deleteAll()
        }
        .setCancelable(false)
        .create()
        .show()
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
