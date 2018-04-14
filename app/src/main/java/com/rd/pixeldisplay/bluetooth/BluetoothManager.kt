package com.rd.pixeldisplay.bluetooth

import android.bluetooth.BluetoothAdapter

class BluetoothManager {

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
}