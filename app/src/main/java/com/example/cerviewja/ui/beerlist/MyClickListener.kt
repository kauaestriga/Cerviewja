package com.example.cerviewja.ui.beerlist

import com.example.cerviewja.domain.entity.Description

interface MyClickListener {

    fun onClick(description: Description)

    fun onLongClick(description: Description)

    fun onShareClick(description: Description)

    fun onCallClick()
}