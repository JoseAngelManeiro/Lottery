package com.joseangelmaneiro.lottery.data.firestore

import com.google.firebase.Timestamp

data class FirestoreModel(
    val navidad: List<String> = emptyList(),
    val elNino: List<String> = emptyList(),
    val updatedAt: Timestamp? = null
)
