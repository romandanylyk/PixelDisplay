package com.rd.pixeldisplay.bluetooth

import android.bluetooth.BluetoothAdapter

object BluetoothManager {

    private var adapter: BluetoothAdapter? = null

    init {
        adapter = BluetoothAdapter.getDefaultAdapter()
    }

    fun isBluetoothAvailable(): Boolean {
        return adapter?.isEnabled!!
    }

    fun enable() {
        if (!adapter?.isEnabled!!) adapter?.enable()
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

    private fun isDiscovering(): Boolean {
        return adapter?.isDiscovering!!
    }
}