package com.game.ponini.api

import retrofit2.Call
import retrofit2.http.POST

/**
 * Created by Viнt@rь on 21.10.2020
 */
interface API {

    @POST("/test")
    fun test(request: Any): Call<Any>
}