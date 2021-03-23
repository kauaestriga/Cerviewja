package com.example.cerviewja.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Description(
    var nome: String? = null,
    var cervejaria: String? = null,
    var estilo: String? = null,
    var origem: String? = null,
    var preco: Double? = null,
    var teorAlcoolico: Double? = null
) : Parcelable