package com.app.weather.GeocodingApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiInterface {
    @GET("search")
    fun getGeocodingData(
        @Query("q") address : String,
        @Query("api_key") apiKey : String
    ) : Call<GeocodingApiData>
}