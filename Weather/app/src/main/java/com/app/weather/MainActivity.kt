package com.app.weather

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.weather.WeatherAPIClassesCoordinates.WeatherApiInterface
import com.app.weather.WeatherAPIClassesCoordinates.WeatherData
import com.app.weather.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var longitude: String
    private lateinit var latitude: String

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val dialog = ProgressDialog(this)
//        dialog.setMessage("Fetching Data...")
//        dialog.setCancelable(false)
//        dialog.setInverseBackgroundForced(false)
//        dialog.show()


        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) || permissions.getOrDefault(
                    Manifest.permission.ACCESS_COARSE_LOCATION, false
                ) -> {
                    val result = fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        CancellationTokenSource().token
                    )
                    result.addOnCompleteListener {
                        latitude = it.result.latitude.toString()
                        longitude = it.result.longitude.toString()
                        LocationDataClass.latitude = latitude
                        LocationDataClass.longitude = longitude

                        val latitude = LocationDataClass.latitude
                        val longitude = LocationDataClass.longitude

                        var weatherData: WeatherData? = null
                        // Define the parameters for the API call
                        val currentParameters =
                            "temperature_2m,precipitation,rain,showers,wind_speed_10m,wind_direction_10m"
                        val hourlyParameters =
                            "temperature_2m,rain,showers,wind_speed_10m,wind_direction_10m,temperature_80m"
                        val dailyParameters =
                            "temperature_2m_max,temperature_2m_min,sunrise,sunset,daylight_duration,rain_sum,precipitation_sum,wind_speed_10m_max"
                        val timezone = "auto"


                        val retrofitBuilder = Retrofit.Builder()
                            .baseUrl("https://api.open-meteo.com/v1/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(WeatherApiInterface::class.java)
                        retrofitBuilder.getWeatherData(
                            latitude,
                            longitude,
                            currentParameters,
                            hourlyParameters,
                            dailyParameters,
                            timezone
                        )?.enqueue(object : Callback<WeatherData?> {
                            override fun onResponse(
                                p0: Call<WeatherData?>,
                                p1: Response<WeatherData?>
                            ) {
                                val response = p1.body()
                                weatherData = response
                                if (weatherData != null) {
                                    LocationDataClass.weatherData = weatherData as WeatherData
                                    Handler().postDelayed({
                                        val intent = Intent(applicationContext , HomeActivity ::class.java)
                                        startActivity(intent)
                                        finish()
                                    } , 1800)
                                }else {
                                    Toast.makeText(this@MainActivity , "Error 404 not found" , Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(p0: Call<WeatherData?>, p1: Throwable) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Error 404 Not Found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        })
                    }
                }
            }
        }
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}