package com.game.ponini.model.push

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Viнt@rь on 24.10.2020
 */
data class PushResponse(

    @JsonAdapter(BitmapDeserializer::class)
    @SerializedName("icon")
    val icon: Bitmap,

    @JsonAdapter(BitmapDeserializer::class)
    @SerializedName("image")
    val image: Bitmap,

    @SerializedName("text")
    val text: String,

    @SerializedName("title")
    val title: String
)

class BitmapDeserializer : JsonDeserializer<Bitmap> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Bitmap {
        val decodedString = Base64.decode(
            context.deserialize(json, object : TypeToken<String>() {}.type) as String,
            Base64.DEFAULT
        )
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
}