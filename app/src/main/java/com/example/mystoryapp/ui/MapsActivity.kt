package com.example.mystoryapp.ui

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import com.example.mystoryapp.AppPreferences
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityMapsBinding
import com.example.mystoryapp.retrofit.ListStoryResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val storyViewModel: StoryViewModel by viewModels {
        ViewModelFactory(this)
    }
    private var storyWithLocation = listOf<ListStoryResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val appPreferences = AppPreferences(this)
        val token = appPreferences.authToken

        if (token != null) {
            storyViewModel.getStories(token)
        }

        storyViewModel.message.observe(this) {
            getLocationUser(storyViewModel.storyy)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle()

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
    }

    private fun getLocationUser(story: List<ListStoryResponse>) {
        if (story.isNotEmpty()) {
            for (stories in story) {
                if (stories.lat != null && stories.lon != null){
                    val position = LatLng(stories.lat, stories.lon)
                    storyWithLocation = storyWithLocation + stories
                    mMap.addMarker(
                        MarkerOptions().position(position).title(stories.name)
                    )
                }
            }
        }
        if(storyWithLocation.isNotEmpty()) {
            val latLng = LatLng(storyWithLocation[0].lat!!, storyWithLocation[0].lon!!)
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng, INITIAL_ZOOM
                )
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        const val TAG = "MapsActivity"
        const val INITIAL_ZOOM = 6f

    }
}