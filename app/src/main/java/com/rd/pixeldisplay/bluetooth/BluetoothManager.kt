package com.rd.pixeldisplay.bluetooth

import android.bluetooth.BluetoothAdapter

class BluetoothManager {

    private var adapter: BluetoothAdapter? = null

    init {
        adapter = BluetoothAdapter.getDefaultAdapter()
    }

    public fun isBlueoothAvailable(): Boolean {
        return isBluetoothSupported() && isBluetoothEnabled()
    }

    public fun isBluetoothSupported(): Boolean {
        return adapter != null
    }

    public fun isBluetoothEnabled(): Boolean {
        return adapter?.isEnabled!!
    }
}