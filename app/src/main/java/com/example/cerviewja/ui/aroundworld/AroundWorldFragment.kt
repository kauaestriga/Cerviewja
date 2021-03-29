package com.example.cerviewja.ui.aroundworld

import android.location.Geocoder
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.View
import com.example.cerviewja.R
import com.example.cerviewja.domain.entity.Description
import com.example.cerviewja.ui.base.BaseFragment
import com.example.cerviewja.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import kotlin.collections.ArrayList

class AroundWorldFragment : BaseFragment(), OnMapReadyCallback {

    override val layout: Int = R.layout.fragment_around_world

    private lateinit var mMap: GoogleMap
    private lateinit var descriptions: ArrayList<Description>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadCountry()
    }

    private fun loadCountry() {
        showLoading()

        try {
            beersRefs
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    descriptions = documentSnapshot.toObjects(Description::class.java) as ArrayList<Description>
                    callMaps()
                    hideLoading()
                }
                .addOnFailureListener { exception ->
                    showErrorMessage(exception.message)
                    hideLoading()
                }

        } catch (e: Exception) {
            e.printStackTrace()
            showToastMessage(getString(R.string.stranger_error))
            hideLoading()
        }
    }

    private fun callMaps() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        val countrys = listOf("Brasil", "Japão", "Alemanha")
        val geocoder = Geocoder(requireContext(), Locale.getDefault())

        descriptions.forEach { description ->
            val addressGeocoding = geocoder.getFromLocationName(description.origem, 1)
            val latLng = LatLng(addressGeocoding[0].latitude,addressGeocoding[0].longitude)

            mMap.addMarker(MarkerOptions()
                .position(latLng)
                .title(description.origem)
            )

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }

    }
}