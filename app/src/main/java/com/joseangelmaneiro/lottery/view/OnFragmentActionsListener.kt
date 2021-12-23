package com.joseangelmaneiro.lottery.view

import com.joseangelmaneiro.lottery.model.Ticket

interface OnFragmentActionsListener {
    fun addButtonClicked(function: (Ticket) -> Unit)
    fun error(function: () -> Unit)
    fun onTicketClicked(ticket: Ticket, function: () -> Unit)
}
