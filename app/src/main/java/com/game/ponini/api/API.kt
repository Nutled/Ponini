package com.game.ponini.api

import com.game.ponini.BuildConfig
import com.game.ponini.model.main.MainRequest
import com.game.ponini.model.push.PushResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by Viнt@rь on 21.10.2020
 */
interface API {

    // MAIN API
    @Headers("GID: ${BuildConfig.GID}", "PackageName: ${BuildConfig.APPLICATION_ID}")
    @POST("/")
    suspend fun main(@Body request: MainRequest): Response<Void>

    @Headers("GID: ${BuildConfig.GID}", "PackageName: ${BuildConfig.APPLICATION_ID}")
    @GET("/push")
    suspend fun getPush(): PushResponse

    // TEST APIs
    @Headers("123: game", "GID: ${BuildConfig.GID}", "PackageName: ${BuildConfig.APPLICATION_ID}")
    @POST("/")
    suspend fun game(@Body request: MainRequest): Response<Void>

    @Headers("123: site", "GID: ${BuildConfig.GID}", "PackageName: ${BuildConfig.APPLICATION_ID}")
    @POST("/")
    suspend fun redirect(@Body request: MainRequest): Response<Void>

    @Headers("123: empty", "GID: ${BuildConfig.GID}", "PackageName: ${BuildConfig.APPLICATION_ID}")
    @POST("/")
    suspend fun redirectNoBody(@Body request: MainRequest): Response<Void>

    @POST("/")
    suspend fun error(@Body request: MainRequest): Response<Void>
}