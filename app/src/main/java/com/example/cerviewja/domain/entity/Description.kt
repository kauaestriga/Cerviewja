package com.example.cerviewja.domain.entity

import android.os.Parcelable
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

@Parcelize
class Description(
    var id: String? = null,
    var nome: String? = null,
    var cervejaria: String? = null,
    var estilo: String? = null,
    var origem: String? = null,
    var preco: Double? = null,
    var teorAlcoolico: Double? = null
) : Parcelable {

    fun toMap() : Map<String, Any> {
        val gson = Gson()
        val json: String = gson.toJson(this)

        val mapType = object : TypeToken<Map<String, Any>>() {}.type
        return gson.fromJson<Map<String, Any>>(json, mapType)
    }
}