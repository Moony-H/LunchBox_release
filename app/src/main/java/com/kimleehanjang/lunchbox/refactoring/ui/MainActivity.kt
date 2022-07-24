package com.kimleehanjang.lunchbox.refactoring.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import com.kimleehanjang.lunchbox.R
import com.kimleehanjang.lunchbox.databinding.ActivityMainBinding
import com.kimleehanjang.lunchbox.refactoring.base.BaseActivity
import com.kimleehanjang.lunchbox.refactoring.tag.FragmentTag
import com.kimleehanjang.lunchbox.refactoring.viewModels.MainViewModel
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity:BaseActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private var backKeyPressedTime: Long = 0
    private val locationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onBackPressed() {
        val mainFragment =
            supportFragmentManager.findFragmentByTag(FragmentTag.FRAGMENT_MAIN.tag) as MainFragment?
        mainFragment?.let { fragment ->
            if (fragment.childFragmentManager.backStackEntryCount > 0)
                fragment.childFragmentManager.popBackStackImmediate()
            else {
                if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
                    backKeyPressedTime = System.currentTimeMillis()
                    Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show()

                } else {
                    ActivityCompat.finishAffinity(this)
                    exitProcess(0)
                }
            }

        } ?: run {
            if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
                backKeyPressedTime = System.currentTimeMillis()
                Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show()

            } else {
                ActivityCompat.finishAffinity(this)
                exitProcess(0)
            }
            super.onBackPressed()
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")
        _binding = ActivityMainBinding.inflate(layoutInflater)

        //권한 요청
        requirePermissions(
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), 1000
        )

        viewModel.frontFragment.observe(this) {
            Log.d("MainActivity", "top ui is ${FragmentTag.values()[it].tag}")
        }

        //viewModel.userPosition.observe(this, userPositionObserver)

        setContentView(binding.root)


        //메모리에 값을 저장하기 위한 클래스
        val pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE)

        //isFirst 란 변수가 NULL 이면 true 를 가져옴.(최초 실행일 때는 NULL) NULL 이 아니면 최초인지 아닌지를 가져옴
        val first = pref.getBoolean("isFirst", true)
        if (first) {
            //최초실행일 때
            Log.d("app/Running", "app was launched for the first time")

            //isFirst 란변수 생성후 false 를 넣어주고 apply
            pref.edit().putBoolean("isFirst", false).apply()



            supportFragmentManager.commit {
                replace(
                    R.id.activity_main_container,
                    StartFragment(),
                    FragmentTag.FRAGMENT_START.tag
                )
            }


        } else {
            //최초실행이 아닐 때
            Log.d("app/Running", "app was launched several times")
            //메인 프래그먼트 실행
            supportFragmentManager.commit {
                replace(R.id.activity_main_container, MainFragment(), FragmentTag.FRAGMENT_MAIN.tag)
            }
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("MissingPermission")
    override fun permissionGranted(requestCode: Int) {
        if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.O){
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                2000,
                10f,
                object :LocationListener{
                    override fun onLocationChanged(location: Location) {
                        location?.let {
                            val position = LatLng(it.latitude, it.longitude)
                            viewModel.setUserPosition(position)
                            Log.d("test","gps changed")
                        } ?: run {
                            Log.d("MainActivity", "Gps is off")
                        }
                    }

                    override fun onProviderDisabled(provider: String) {
                        //super.onProviderDisabled(provider)

                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        super.onStatusChanged(provider, status, extras)
                    }

                }
            )

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000,
                10f,
                object :LocationListener{
                    override fun onLocationChanged(location: Location) {
                        location?.let {
                            val position = LatLng(it.latitude, it.longitude)
                            viewModel.setUserPosition(position)
                            Log.d("test","gps changed")
                        } ?: run {
                            Log.d("MainActivity", "Gps is off")
                        }
                    }

                    override fun onProviderDisabled(provider: String) {
                        //super.onProviderDisabled(provider)

                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        //super.onStatusChanged(provider, status, extras)
                    }

                }
            )
        }
        else{
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000,
                20f,
                gpsLocationListener
            )

            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                2000,
                10f,
                networkLocationListener
            )
        }

        Log.d("MainActivity", "gps permission granted")
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("MainActivity", "gps provider enabled")



        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.d("MainActivity", "network provider enabled")



        } else {
            Log.d("MainActivity", "gps disabled")
            Toast.makeText(baseContext, "GPS가 꺼져 있습니다. GPS를 켜 주세요", Toast.LENGTH_LONG).show()
        }


    }

    private val gpsLocationListener = LocationListener { location ->
        location?.let {
            val position = LatLng(it.latitude, it.longitude)
            viewModel.setUserPosition(position)
        } ?: run {
            Log.d("MainActivity", "Gps is off")
        }

    }

    private val networkLocationListener = LocationListener { location ->
        location?.let {
            val position = LatLng(it.latitude, it.longitude)
            viewModel.setUserPosition(position)
        } ?: run {
            Log.d("MainActivity", "Gps is off")
        }

    }

    //private val userPositionObserver = Observer { latlng: LatLng ->
    //    locationManager.removeUpdates(gpsLocationListener)
    //    locationManager.removeUpdates(networkLocationListener)
    //}

    override fun permissionDenied(requestCode: Int) {
        Log.d("MainActivity", "gps permission denied")
        Toast.makeText(baseContext, "위치 권한이 거부되었습니다. 위치를 수동으로 설정해 주세요.", Toast.LENGTH_LONG).show()
    }
}