package com.example.cerviewja.ui.aroundworld

import android.os.Bundle
import android.view.View
import com.example.cerviewja.R
import com.example.cerviewja.ui.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AroundWorldFragment : BaseFragment(), OnMapReadyCallback {

    override val layout: Int = R.layout.fragment_around_world

    private lateinit var mMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val umEndereco = LatLng(-23.6058202,-46.6663158)
        mMap.addMarker(MarkerOptions()
            .position(umEndereco)
            .title("Exemplo no Mapa")
            .snippet("Breve descrição")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(umEndereco, 16f))
    }
}