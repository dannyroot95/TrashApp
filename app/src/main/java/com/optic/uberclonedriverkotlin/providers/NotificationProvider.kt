package com.optic.uberclonedriverkotlin.providers

import com.optic.uberclonedriverkotlin.api.IFCMApi
import com.optic.uberclonedriverkotlin.api.RetrofitClient
import com.optic.uberclonedriverkotlin.models.FCMBody
import com.optic.uberclonedriverkotlin.models.FCMResponse
import retrofit2.Call

class NotificationProvider {

    private val URL = "https://fcm.googleapis.com"

    fun sendNotification(body: FCMBody): Call<FCMResponse> {
        return RetrofitClient.getClient(URL).create(IFCMApi::class.java).send(body)
    }

}