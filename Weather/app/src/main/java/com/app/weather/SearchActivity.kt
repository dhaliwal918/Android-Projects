package com.app.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.weather.GeocodingApi.GeocodingApiData
import com.app.weather.GeocodingApi.GeocodingApiInterface
import com.app.weather.databinding.ActivitySearchAcitivityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchAcitivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // https://geocode.maps.co/search?q=talwandi%20mange%20khan&api_key=

        val retrofit = Retrofit.Builder()
            .baseUrl("https://geocode.maps.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingApiInterface::class.java)
        val retrofitData = retrofit.getGeocodingData("zira , ferozpur , punjab" , "")
        retrofitData.enqueue(object : Callback<GeocodingApiData> {
            override fun onResponse(p0: Call<GeocodingApiData>, p1: Response<GeocodingApiData>) {
                val responseBody = p1.body()
                if (responseBody != null) {

                }else {

                }
            }

            override fun onFailure(p0: Call<GeocodingApiData>, p1: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}