package com.optic.uberclonedriverkotlin.models

class FCMResponse (
    val success: Int? = null,
    val failure: Int? = null,
    val canonical_ids: Int? = null,
    val multicast_id: Long? = null,
    val results: ArrayList<Any> = ArrayList<Any>(),
) {
}