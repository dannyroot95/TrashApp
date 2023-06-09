package com.optic.uberclonedriverkotlin.models

data class Microroutes(

    val id: String? = null,
    val name: String ? = null,
    val descript: String ? = null,
    val turn: String ? = null,
    val positions : ArrayList<location> = ArrayList()

)

data class location(
    val lat : Double = 0.0,
    val lng : Double = 0.0
)