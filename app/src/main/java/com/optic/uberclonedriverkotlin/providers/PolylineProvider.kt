package com.optic.uberclonedriverkotlin.providers

import android.app.Activity
import android.graphics.Color
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.optic.uberclonedriverkotlin.models.Microroutes

class PolylineProvider {

    val db = Firebase.firestore

    fun getPolyline(map : GoogleMap,activity: Activity){
        val myPlate = "12-X34"
        db.collection("zones").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val plates = document.data["plates"] as? List<*>
                val microroute =  document.data["microroutes"] as? List<*>
                plates!!.forEachIndexed { index, plate ->
                    if(plate == myPlate){
                        val currentMicroroute = microroute!![index].toString()
                        db.collection("Drivers").document(AuthProvider().getId()).get().addOnSuccessListener { snapshot ->
                            if(snapshot.exists()){
                                val plateNumber = snapshot.data!!["plateNumber"]
                                if(plateNumber != null){
                                    db.collection("microroutes").whereEqualTo("name",currentMicroroute).get().addOnSuccessListener {querySnapshot ->
                                        querySnapshot.forEach(){ e ->
                                            if(e.data["name"]!! == currentMicroroute){

                                                val value = e.toObject(Microroutes::class.java)
                                                val coordenadas = value.positions

                                                val polylineOptions = PolylineOptions()

                                                for (coordenada in coordenadas) {
                                                    polylineOptions.add(LatLng(coordenada.lat, coordenada.lng))
                                                }

                                                polylineOptions.color(Color.BLACK)
                                                polylineOptions.width(16f)

                                                map.addPolyline(polylineOptions)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }

    }

}