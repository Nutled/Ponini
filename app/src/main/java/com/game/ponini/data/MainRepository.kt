package com.game.ponini.data

import com.game.ponini.api.API
import com.game.ponini.model.main.MainRequest
import com.game.ponini.model.push.PushResponse
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Viнt@rь on 21.10.2020
 */
@Singleton
class MainRepository @Inject constructor(
        private val api: API
) {

    suspend fun game(request: MainRequest): Response<Void> = api.game(request)

    suspend fun error(request: MainRequest): Response<Void> = api.error(request)

    suspend fun getPush(): PushResponse = api.getPush()
}