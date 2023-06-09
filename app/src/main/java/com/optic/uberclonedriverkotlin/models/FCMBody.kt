package com.optic.uberclonedriverkotlin.models

class FCMBody(
    val to: String,
    val priority: String,
    val ttl: String,
    val data: MutableMap<String, String>,
) {
}