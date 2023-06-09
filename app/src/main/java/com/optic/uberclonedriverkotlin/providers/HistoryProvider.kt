package com.optic.uberclonedriverkotlin.providers

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.optic.uberclonedriverkotlin.models.Booking
import com.optic.uberclonedriverkotlin.models.History

class HistoryProvider {

    val db = Firebase.firestore.collection("Histories")
    val authProvider = AuthProvider()

    fun create(history: History): Task<DocumentReference> {
        return db.add(history).addOnFailureListener {
            Log.d("FIRESTORE", "ERROR: ${it.message}")
        }
    }

    fun getHistoryById(id: String): Task<DocumentSnapshot> {
        return db.document(id).get()
    }

    fun getLastHistory(): Query { // CONSULTA COMPUESTA - INDICE
        return db.whereEqualTo("idDriver", authProvider.getId()).orderBy("timestamp", Query.Direction.DESCENDING).limit(1)
    }

    fun getHistories(): Query { // CONSULTA COMPUESTA - INDICE
        return db.whereEqualTo("idDriver", authProvider.getId()).orderBy("timestamp", Query.Direction.DESCENDING)
    }

    fun getBooking(): Query {
        return db.whereEqualTo("idDriver", authProvider.getId())
    }

    fun updateCalificationToClient(id: String, calification: Float): Task<Void> {
        return db.document(id).update("calificationToClient", calification).addOnFailureListener { exception ->
            Log.d("FIRESTORE", "ERROR: ${exception.message}")
        }
    }

}