package com.rd.pixeldisplay.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

object BluetoothController {

    const val ACTION_REQUEST_ENABLE_BLUETOOTH = 10000
    const val ACTION_REQUEST_ENABLE_LOCATION = 20000
    private var adapter: BluetoothAdapter? = null

    init {
        adapter = BluetoothAdapter.getDefaultAdapter()
    }

    fun isBluetoothSupported() = adapter != null

    fun isBluetoothAvailable() = adapter?.isEnabled!!

    fun isLocationAvailable(activity: Activity): Boolean {
        val permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    fun requestBluetoothEnable(activity: AppCompatActivity?) {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activity?.startActivityForResult(intent, ACTION_REQUEST_ENABLE_BLUETOOTH)
    }

    fun requestLocationPermission(activity: AppCompatActivity) {
        val isLocationEnabled = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        if (isLocationEnabled != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            ActivityCompat.requestPermissions(activity, permissions, ACTION_REQUEST_ENABLE_LOCATION)
        }
    }

    fun startDiscovery() {
        if (!isDiscovering()) {
            adapter?.startDiscovery()
        }
    }

    fun cancelDiscovery() {
        if (isDiscovering()) {
            adapter?.cancelDiscovery()
        }
    }

    fun isDiscovering(): Boolean {
        return adapter?.isDiscovering!!
    }
}