package com.example.laboratorio11

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.laboratorio11.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import android.Manifest
import android.os.Looper

class MainActivity : AppCompatActivity() {

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val  PERMISSION_ID=42
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(allPermissionsrantedGPS()){
            mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this)
            leerubicacionactual()
            println("if")
        }
        else{
            println("else")
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_ID)

        }
        binding.btndetectar.setOnClickListener {
            mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this)
            leerubicacionactual() }

    }
    private fun allPermissionsrantedGPS()= Companion.REQUIERED_PERMISSIONS_GPS.all{
        ContextCompat.checkSelfPermission(baseContext,it)== PackageManager.PERMISSION_GRANTED
    }

    private fun leerubicacionactual(){
        if(checkPermisiions()){
            if(isLocationEnabled()){
                if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    mFusedLocationClient.lastLocation.addOnCompleteListener(this){task->
                        var location: Location?=task.result
                        if(location==null){
                            requestNewLocationData()
                            println("requesternewlocation")
                        }
                        else{
                            binding.lbllatitud.text="Latitud= "+location.latitude.toString()
                            binding.lbllongitud.text="Longitud= "+location.longitude.toString()
                            println("leerubicanion")
                        }
                    }
                }
            }else{
                Toast.makeText(this, "Activar ubicación",Toast.LENGTH_SHORT).show()
                val intent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                this.finish()
            }
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_ID)
        }
    }
    private fun requestNewLocationData(){
        var mLocationRequest= LocationRequest()
        mLocationRequest.priority=com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval=0
        mLocationRequest.fastestInterval=0
        mLocationRequest.numUpdates=1
        mFusedLocationClient=LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallBack, Looper.myLooper())
    }

    private val mLocationCallBack=object: LocationCallback(){
        override fun onLocationResult(locationResult:LocationResult){
            var mLastLocation: Location=locationResult.lastLocation
            binding.lbllatitud.text="Latitud= "+ mLastLocation.latitude.toString()
            binding.lbllongitud.text="Longitud= "+ mLastLocation.longitude.toString()
            println("mlocationcallback")

        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermisiions(): Boolean{
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
        ){
            return  true
        }
        return false
    }

    companion object{
        private val REQUIERED_PERMISSIONS_GPS= arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    }
}