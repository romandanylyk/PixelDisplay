package com.rd.pixeldisplay.bluetooth

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
import java.util.*
import java.util.concurrent.TimeUnit

object BluetoothDiscoveryManager {

    private val DEFAULT_BLUETOOTH_DISCOVERY_DURATION = TimeUnit.SECONDS.toMillis(12)
    private val discoveredDevices = ArrayList<BluetoothDevice>()
    private var listener: DiscoveryListener? = null

    fun observ(activity: AppCompatActivity, listener: DiscoveryListener?) {
        BluetoothDiscoveryManager.listener = listener
        activity.lifecycle.addObserver(BluetoothDiscoveryObserver(activity))
    }

    fun startDiscovery() {
        cancelDiscovery()
        BluetoothController.startDiscovery()

        listener?.onBluetoothDiscoveryStarted()
        Handler().postDelayed({ cancelDiscovery() }, DEFAULT_BLUETOOTH_DISCOVERY_DURATION)
    }

    fun cancelDiscovery() {
        if (BluetoothController.isDiscovering()) {
            BluetoothController.cancelDiscovery()
            discoveredDevices.clear()
            listener?.onBluetoothDiscoveryEnded()
        }
    }

    interface DiscoveryListener {

        fun onBluetoothDiscoveryStarted()

        fun onBluetoothDeviceFound(device: BluetoothDevice)

        fun onBluetoothDiscoveryEnded()
    }

    class BluetoothDiscoveryObserver(private val activity: AppCompatActivity) : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        private fun registerStateReceiver() {
            val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            activity.registerReceiver(discoverReceiver, intentFilter)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun unregisterDiscoverReceiver() {
            activity.unregisterReceiver(discoverReceiver)
            cancelDiscovery()
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