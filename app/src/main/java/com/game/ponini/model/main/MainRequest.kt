package com.game.ponini.model.main

import com.google.gson.annotations.SerializedName

/**
 * Created by Viнt@rь on 21.10.2020
 */
data class MainRequest(

    @SerializedName("deeplink_fb")
    val deepLinkFB: String?,

    @SerializedName("deeplink_aps")
    val deepLinkAPS: String?,

    //@SerializedName("referrer")
    //val referrer: String, //TODO mb need remove

    @SerializedName("accelerometer")
    val accelerometer: List<Float>?,
)