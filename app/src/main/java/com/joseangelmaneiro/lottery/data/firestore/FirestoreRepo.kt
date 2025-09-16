package com.joseangelmaneiro.lottery.data.firestore

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FieldValue
import com.joseangelmaneiro.lottery.LotteryType
import kotlinx.coroutines.tasks.await

object FirestoreRepo {

    private val db = Firebase.firestore

    private fun docRef(): DocumentReference {
        val collectionId = "lottery"
        val documentId = "lottery-numbers123"
        return db.collection(collectionId).document(documentId)
    }

    suspend fun init() {
        val ref = docRef()
        db.runTransaction { tx ->
            val snap = tx.get(ref)
            if (!snap.exists()) {
                val data = hashMapOf(
                    "navidad" to emptyList<String>(),
                    "elNino" to emptyList<String>(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                tx.set(ref, data)
            }
        }.await()
    }

    // Real-time listener
    fun listenNumbers(
        onUpdate: (FirestoreModel) -> Unit,
        onError: (Exception) -> Unit = {}
    ) = docRef().addSnapshotListener { snap, e ->
        if (e != null) { onError(e); return@addSnapshotListener }
        val data = snap?.toObject(FirestoreModel::class.java) ?: FirestoreModel()
        onUpdate(data)
    }

    suspend fun addNumber(
        lotteryType: LotteryType,
        number: String
    ) {
        val field = lotteryType.firestoreFieldKey
        val normalized = number.trim().padStart(5, '0')
        require(normalized.matches(Regex("\\d{5}"))) { "Número inválido" }

        db.runTransaction { tx ->
            val ref = docRef()
            val current = tx.get(ref).toObject(FirestoreModel::class.java)
            val list = if (lotteryType == LotteryType.NAVIDAD) {
                current?.navidad ?: emptyList()
            } else {
                current?.elNino ?: emptyList()
            }
            if (normalized !in list) {
                val newList = (list + normalized).sorted()
                tx.update(ref, mapOf(field to newList, "updatedAt" to FieldValue.serverTimestamp()))
            }
        }.await()
    }

    suspend fun removeNumber(
        lotteryType: LotteryType,
        number: String
    ) {
        val field = lotteryType.firestoreFieldKey
        val normalized = number.trim().padStart(5, '0')
        db.runTransaction { tx ->
            val ref = docRef()
            val current = tx.get(ref).toObject(FirestoreModel::class.java)
            val list = if (lotteryType == LotteryType.NAVIDAD) {
                current?.navidad ?: emptyList()
            } else {
                current?.elNino ?: emptyList()
            }
            val newList = list.filterNot { it == normalized }
            if (newList.size != list.size) {
                tx.update(ref, mapOf(field to newList, "updatedAt" to FieldValue.serverTimestamp()))
            }
        }.await()
    }

    suspend fun removeAllNumbers(
        lotteryType: LotteryType
    ) {
        val field = lotteryType.firestoreFieldKey
        db.runTransaction { tx ->
            val ref = docRef()
            val newList = emptyList<String>()
            tx.update(ref, mapOf(field to newList, "updatedAt" to FieldValue.serverTimestamp()))
        }.await()
    }
}
