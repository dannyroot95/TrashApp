package com.optic.uberclonedriverkotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.firestore.ktx.toObject
import com.optic.uberclonedriverkotlin.R
import com.optic.uberclonedriverkotlin.databinding.ActivityCalificationClientBinding
import com.optic.uberclonedriverkotlin.databinding.ActivityMapTripBinding
import com.optic.uberclonedriverkotlin.models.History
import com.optic.uberclonedriverkotlin.providers.HistoryProvider

class CalificationClientActivity : AppCompatActivity() {

    private var history: History? = null
    private lateinit var binding: ActivityCalificationClientBinding
    private var extraPrice = 0.0
    private var historyProvider = HistoryProvider()
    private var calification = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalificationClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        extraPrice = intent.getDoubleExtra("price", 0.0)
        binding.textViewPrice.text = "${String.format("%.1f", extraPrice)}$"

        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, value, b ->
            calification = value
        }

        binding.btnCalification.setOnClickListener {
            if (history?.id != null) {
                updateCalification(history?.id!!)
            }
            else {
                Toast.makeText(this, "El id del historial es nulo", Toast.LENGTH_LONG).show()
            }
        }

        getHistory()
    }

    private fun updateCalification(idDocument: String) {
        historyProvider.updateCalificationToClient(idDocument, calification).addOnCompleteListener {
            if (it.isSuccessful) {
                goToMap()
            }
            else {
                Toast.makeText(this@CalificationClientActivity, "Error al actualizar la calificacion", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun goToMap() {
        val i = Intent(this, MapActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getHistory() {
        historyProvider.getLastHistory().get().addOnSuccessListener { query ->
            if (query != null) {

                if (query.documents.size > 0) {
                    history = query.documents[0].toObject(History::class.java)
                    history?.id = query.documents[0].id
                    binding.textViewOrigin.text = history?.origin
                    binding.textViewDestination.text = history?.destination
                    binding.textViewTimeAndDistance.text = "${history?.time} Min - ${String.format("%.1f", history?.km)} Km"

                    Log.d("FIRESTORE", "hISTORIAL: ${history?.toJson()}")
                }
                else {
                    Toast.makeText(this, "No se encontro el historial", Toast.LENGTH_LONG).show()
                }

            }
        }
    }
}