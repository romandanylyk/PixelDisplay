package com.rd.pixeldisplay.bluetooth.discovery

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.rd.pixeldisplay.bluetooth.BluetoothManager
import java.util.*
import java.util.concurrent.TimeUnit

object BluetoothDiscoveryManager {

    private val DEFAULT_BLUETOOTH_DISCOVERY_DURATION = TimeUnit.SECONDS.toMillis(12)
    private val discoveredDevices = ArrayList<BluetoothDevice>()

    fun observ(activity: AppCompatActivity, listener: Listener?) {
        activity.lifecycle.addObserver(BluetoothDiscoveryObserver(activity, listener))
    }

    fun startDiscovery() {
        cancelDiscovery()
        BluetoothManager.startDiscovery()
        Handler().postDelayed({ cancelDiscovery() }, DEFAULT_BLUETOOTH_DISCOVERY_DURATION)
    }

    fun cancelDiscovery() {
        BluetoothManager.cancelDiscovery()
        discoveredDevices.clear()
    }

    interface Listener {
        fun onBluetoothDeviceFound(device: BluetoothDevice)
    }

    class BluetoothDiscoveryObserver(private val activity: AppCompatActivity, private val listener: Listener?) : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        private fun registerStateReceiver() {
            val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            activity.registerReceiver(discoverReceiver, intentFilter)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun unregisterDiscoverReceiver() {
            activity.unregisterReceiver(discoverReceiver)
        }

        private val discoverReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (BluetoothDevice.ACTION_FOUND == intent.action) {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                    if (isNewDevice(device)) {
                        addDiscoveredDevice(device)
                        listener?.onBluetoothDeviceFound(device)
                    }
                }
            }
        }

        private fun isNewDevice(device: BluetoothDevice?): Boolean {
            if (device == null || TextUtils.isEmpty(device.name) || TextUtils.isEmpty(device.address)) {
                return false
            }

            for (bluetoothDevice in discoveredDevices) {
                if (bluetoothDevice.address == device.address) {
                    return false
                }
            }

            return true
        }

        private fun addDiscoveredDevice(device: BluetoothDevice?) {
            if (device != null) {
                discoveredDevices.add(device)
            }
        }
    }
}