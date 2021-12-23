package com.joseangelmaneiro.lottery.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.joseangelmaneiro.lottery.LotteryType
import com.joseangelmaneiro.lottery.model.Ticket

class LocalDataSource(
    context: Context,
    lotteryType: LotteryType
) {

    private val PREF_FILE_KEY = "com.joseangelmaneiro.lottery.PREFERENCE_FILE_KEY"
    private val PREF_TICKETS_KEY = if (lotteryType == LotteryType.NAVIDAD) {
        "pref_tickets_key"
    } else {
        "pref_tickets_key_nino"
    }

    private val sharedPreferences = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
    private val gson = Gson()
    private val ticketListType = object : TypeToken<List<Ticket>>() {}.type

    private fun saveTickets(tickets: List<Ticket>): Boolean {
        return with(sharedPreferences.edit()) {
            val value = gson.toJson(tickets, ticketListType)
            putString(PREF_TICKETS_KEY, value)
            commit()
        }
    }

    fun saveTicket(ticket: Ticket): Boolean {
        val tickets = getTickets().toMutableList()
        tickets.add(ticket)
        return saveTickets(tickets)
    }

    fun getTickets(): List<Ticket> {
        val json = sharedPreferences.getString(PREF_TICKETS_KEY, null)
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            gson.fromJson(json, ticketListType)
        }
    }

    fun deleteTicket(ticket: Ticket): Boolean {
        val tickets = getTickets().toMutableList()
        tickets.remove(ticket)
        return saveTickets(tickets)
    }
}