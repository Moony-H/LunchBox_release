package com.kimleehanjang.lunchbox.refactoring.base

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

abstract class BaseActivity:AppCompatActivity() {
    abstract fun permissionGranted(requestCode: Int)
    abstract fun permissionDenied(requestCode: Int)

    fun requirePermissions(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //마쉬멜로우 미만의 버전이면 안해도 됨
            //requestCode를 함께 전달
            permissionGranted(requestCode)
        } else {
            //모든 권한이 승인 되었는지 확인
            val isAllPermissionGranted = permissions.all {
                checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
            }
            if (isAllPermissionGranted) {
                //승인이 다 되었으면 승인 되었다고 자식 class에 전달
                permissionGranted(requestCode)
            } else {
                //안되었으면 사용자에게 권한을 요청
                ActivityCompat.requestPermissions(this, permissions, requestCode)
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED })
            permissionGranted(requestCode)
        else
            permissionDenied(requestCode)
    }
}