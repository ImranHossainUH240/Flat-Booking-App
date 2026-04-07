package com.example.flatbookingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the map fragment and initialize it
        val mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // This triggers automatically when the map finishes loading from the internet
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set coordinates for the "Studio Flat near University" (London coordinates for demo)
        val studioFlatLocation = LatLng(51.5074, -0.1278)

        // Add a red pin to the map
        mMap.addMarker(
            MarkerOptions()
                .position(studioFlatLocation)
                .title("Studio Flat near University")
                .snippet("$650/mo")
        )

        // Move the camera to the flat and zoom in (15f is a nice street-level zoom)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(studioFlatLocation, 15f))
    }
}