package com.game.ponini.api

import com.game.ponini.model.test.TestRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by Viнt@rь on 21.10.2020
 */
interface API {

    @Headers("PackageName: test.package.name")
    @POST("/test")
    suspend fun test(@Body request: TestRequest): Call<Any>

    @GET("/push")
    fun getPush(): Call<Any>
}